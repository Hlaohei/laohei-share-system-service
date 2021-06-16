package vip.laohei.sharesystem.service.impl;

import java.awt.FontFormatException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import vip.laohei.sharesystem.dao.RefreshTokenDao;
import vip.laohei.sharesystem.dao.UserDao;
import vip.laohei.sharesystem.entity.RefreshToken;
import vip.laohei.sharesystem.entity.User;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IUserService;
import vip.laohei.sharesystem.utils.ClaimsUtils;
import vip.laohei.sharesystem.utils.Constants;
import vip.laohei.sharesystem.utils.CookieUtils;
import vip.laohei.sharesystem.utils.IdWorker;
import vip.laohei.sharesystem.utils.JwtUtil;
import vip.laohei.sharesystem.utils.RedisUtils;
import vip.laohei.sharesystem.utils.TextUtils;

/**
 * 用户服务 实现类
 * 
 * @author laohei
 *
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl extends BaseService implements IUserService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private UserDao userDao;

	@Autowired
	private Random random;

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private RefreshTokenDao refreshTokenDao;

	@Autowired
	private Gson gson;

	/**
	 * 拿到 request
	 * 
	 * @return
	 */
	private HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return requestAttributes.getRequest();
	}

	/**
	 * 拿到 response
	 * 
	 * @return
	 */
	private HttpServletResponse getResponse() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return requestAttributes.getResponse();
	}

	/**
	 * 获取图灵验证码
	 * 
	 * @throws FontFormatException
	 * @throws IOException
	 * 
	 */
	@Override
	public void getCaptcha(HttpServletResponse response, String captchaKey) throws Exception {
		if (TextUtils.isEmpty(captchaKey) || captchaKey.length() < 13) {
			return;
		}
		long key;
		try {
			key = Long.parseLong(captchaKey);
		} catch (Exception e) {
			return;
		}
		// 时间戳可以用之后就正常使用
		// 设置请求头为输出图片类型
		response.setContentType("image/gif");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		int captchaType = random.nextInt(3);
		Captcha targetCaptcha;
		if (captchaType == 0) {
			// 三个参数分别为宽、高、位数
			targetCaptcha = new SpecCaptcha(300, 100, 5);
		} else if (captchaType == 1) {
			// gif类型
			targetCaptcha = new GifCaptcha(300, 100);
		} else {
			// 算术类型
			targetCaptcha = new ArithmeticCaptcha(300, 100);
			targetCaptcha.setLen(2); // 几位数运算，默认是两位
		}
		// 设置字体
		int index = random.nextInt(captcha_font_types.length);
		log.info("图灵验证码的字体样式 == > " + index);
		targetCaptcha.setFont(captcha_font_types[index]);
		// 设置类型，纯数字、纯字母、字母数字混合
		targetCaptcha.setCharType(Captcha.TYPE_DEFAULT);

		String content = targetCaptcha.text().toLowerCase();
		log.info("图灵验证码的内容 == > " + content);

		// 保存到 redis 里面
		// 删除时机：
		// 1.自然过期，也就是 10 分钟后自己删除
		// 2.验证码用完以后删除
		// 3.用完的情况：看 get 的地方
		redisUtils.set(Constants.User.KEY_CAPTCHA_CONTENT + key, content, 60 * 10);

		// 输出图片流
		targetCaptcha.out(response.getOutputStream());
	}

	/**
	 * 图灵验证码字体类型
	 */
	public static final int[] captcha_font_types = { //
			Captcha.FONT_1, //
			Captcha.FONT_2, //
			Captcha.FONT_3, //
			Captcha.FONT_4, //
			Captcha.FONT_5, //
			Captcha.FONT_6, //
			Captcha.FONT_7, //
			Captcha.FONT_8, //
			Captcha.FONT_9, //
			Captcha.FONT_10 //
	};

	/**
	 * 登录
	 * <p>
	 * 需要提交的数据： <br>
	 * 1.用户名 <br>
	 * 2.密码 <br>
	 * 3.图灵验证码 <br>
	 * 4.图灵验证码的 key(申请验证码时候的 13位 时间戳) <br>
	 * 
	 * @param captcha
	 * @param captcha_key
	 * @param user
	 * @return
	 */
	@Override
	public ResponseResult doLogin(String captcha, String captchaKey, User user) {
		HttpServletRequest request = getRequest();
		HttpServletResponse response = getResponse();

		// 先检查验证码是否正确，如果正确才继续执行
		String captchaValue = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
		if (!captcha.equals(captchaValue)) {
			return ResponseResult.FAILED("人类验证码不正确");
		}

		// 检查用户名是否为空
		String userName = user.getName();
		if (TextUtils.isEmpty(userName)) {
			return ResponseResult.FAILED("用户名不能为空");
		}
		// 检查密码是否为空
		String password = user.getPassword();
		if (TextUtils.isEmpty(password)) {
			return ResponseResult.FAILED("密码不可以为空");
		}

		// 查找数据库是否有这个用户
		User userFromDb = userDao.findOneByName(userName);
		if (userFromDb == null) {
			return ResponseResult.FAILED("用户名或密码不正确");
		}

		// 用户存在就对比密码
		boolean compared = bCryptPasswordEncoder.matches(password, userFromDb.getPassword());
		if (!compared) {
			return ResponseResult.FAILED("用户名密码不正确");
		}

		// 修改更新时间和登录IP
		userFromDb.setLoginIp(request.getRemoteAddr());
		userFromDb.setUpdateTime(new Date());

		// 验证成功，删除 redis 里的验证码
		redisUtils.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);

		String tokenKey = createToken(response, userFromDb);

		return ResponseResult.SUCCESS("登录成功").setData(tokenKey);
	}

	/**
	 * 创建 token
	 * 
	 * @param response
	 * @param userFromDb
	 * @return
	 */
	private String createToken(HttpServletResponse response, User userFromDb) {
		String oldTokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKE_KEY);

		RefreshToken oldRefreshToken = refreshTokenDao.findOneByUserId(userFromDb.getId());
		if (oldRefreshToken != null) {
			redisUtils.del(Constants.User.KEY_TOKEN + oldRefreshToken.getTokenKey());
		}
		refreshTokenDao.deleteTokenKey(oldTokenKey);

		// 生成 token
		Map<String, Object> claims = ClaimsUtils.user2Claims(userFromDb);
		// token 默认有效时长为 2 小时
		String token = JwtUtil.createToken(claims);
		// 返回 token 的 md5 值，token 会保存在 redis 里
		// 前端访问的时候，携带 token 的 md5key，从 redis 中获取即可
		String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());

		// 保存 token 到 redis 里，有效期为 2 小时，key 是 tokenKey
		redisUtils.set(Constants.User.KEY_TOKEN + tokenKey, token, Constants.TimeValueSecond.HOUR_2);

		// 把 token 写到 cookies 里
		// 域名要动态更新，可以从 request 中获取，之后弄一个工具类
		CookieUtils.setUpCookie(response, Constants.User.COOKIE_TOKE_KEY, tokenKey);

		// 先判断数据库里面有没有 refreshToken
		// 如果有的话就更新
		// 如果没有就新创建
		RefreshToken refreshToken = oldRefreshToken;
		if (refreshToken == null) {
			refreshToken = new RefreshToken();
			refreshToken.setId(idWorker.nextId() + "");
			refreshToken.setUserId(userFromDb.getId());
			refreshToken.setCreateTime(new Date());
		}

		// 不管是过期了还是新登录，都会生成/更新 refreshToken
		// 生成 refreshToken
		String refreshTokenValue = JwtUtil.createRefreshToken(userFromDb.getId(), Constants.TimeValueMillisecond.MONTH);

		// 保存到数据库里面
		// refreshToken, tokenKey, 用户 ID, 创建时间, 更新时间
		refreshToken.setRefreshToken(refreshTokenValue);
		refreshToken.setTokenKey(tokenKey);
		refreshToken.setUpdateTime(new Date());

		refreshTokenDao.save(refreshToken);

		return tokenKey;
	}

	/**
	 * 检查用户是否登录
	 * <p>
	 * 本质就是通过携带的 token_key 检查用户是否登录，如果登录就返回用户信息
	 * 
	 * @return
	 */
	@Override
	public User checkUser() {
		// 拿到 token_key
		String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKE_KEY);
		log.info("检查用户的 tokenKey == > " + tokenKey);
		if (TextUtils.isEmpty(tokenKey)) {
			return null;
		}

		// 登陆过的话就解析用户信息
		User user = parseByTokenKey(tokenKey);

		if (user == null) {
			// 说明解析出错，token 过期了
			// 1.去 mysql 数据库查询 refreshToken
			RefreshToken refreshToken = refreshTokenDao.findOneByTokenKey(tokenKey);

			// 2.如果不存在，就是当前访问没有登录
			if (refreshToken == null) {
				log.info("refresh token 是空的...");
				return null;
			}

			// 3.如果存在，就解析 refreshToken
			try {
				JwtUtil.parseJWT(refreshToken.getRefreshToken());
				// 5.如果 refreToken 有效，创建新的 token，和新的 refreshToken
				String userId = refreshToken.getUserId();
				User userFromDb = userDao.findOneById(userId);
				// 删掉 refreshToken 的记录
				String newTokenKey = createToken(getResponse(), userFromDb);
				// 返回 token
				log.info("创建新的 token 和 refreshToken ...");
				return parseByTokenKey(newTokenKey);
			} catch (Exception e2) {
				log.info("refresh token 过期了...");
				// 4.如果 refreshToken 过期了，就当前访问没有登录，提示用户登录
				return null;
			}
		}
		return user;
	}

	/**
	 * 通过 tokenKey 解析出 token 里的用户信息
	 * 
	 * @param tokenKey
	 * @return
	 */
	private User parseByTokenKey(String tokenKey) {
		String token = (String) redisUtils.get(Constants.User.KEY_TOKEN + tokenKey);
		log.info("通过 tokenKey 解析出 token == > " + token);

		if (token != null) {
			try {
				Claims claims = JwtUtil.parseJWT(token);
				return ClaimsUtils.claim2user(claims);
			} catch (Exception e) {
				log.info("通过 tokenKey 解析出 token == > " + tokenKey + "已过期...");
				return null;
			}
		}
		return null;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @return
	 */

	@Override
	public ResponseResult getUserInfo(String userId) {
		// 从数据库里面获取用户信息
		User user = userDao.findOneById(userId);
		// 判断是否查询到结果
		if (user == null) {
			// 如果不存在，就返回不存在信息
			return ResponseResult.FAILED("用户不存在");
		}

		// 如果存在，就复制对象，清空密码等关键信息
		String userJson = gson.toJson(user);
		User newUser = gson.fromJson(userJson, User.class);
		newUser.setPassword("");

		return ResponseResult.SUCCESS("获取成功").setData(newUser);
	}

	/**
	 * 修改用户名
	 * 
	 * @param userId
	 * @param user
	 * @return
	 */
	@Override
	public ResponseResult updateUserName(String userId, User user) {
		// 从 token 里面解析出来的 user，为了校验权限
		// 只有用户自己才可以修改自己的信息
		User userFromTokenKey = checkUser();
		if (userFromTokenKey == null) {
			return ResponseResult.FAILED("用户未登录");
		}
		User userFromDb = userDao.findOneById(userFromTokenKey.getId());

		// 判断当前用户的 ID 和即将要修改的用户 ID 是否一致，如果一致才可以修改
		if (!userFromDb.getId().equals(userId)) {
			return ResponseResult.FAILED("没有权限修改此用户信息");
		}

		// 可以进行修改用户名
		String userName = user.getName();
		if (!TextUtils.isEmpty(userName)) {
			User userByUserName = userDao.findOneByName(userName);
			if (userByUserName != null) {
				return ResponseResult.FAILED("该用户名已经注册");
			}
			userFromDb.setName(userName);
		}
		userFromDb.setUpdateTime(new Date());
		userDao.save(userFromDb);

		// 干掉 redis 里面的 token，下一次请求，需要解析 token 的，就会根据 refreshToken 重新创建一个
		String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKE_KEY);
		redisUtils.del(Constants.User.KEY_TOKEN + tokenKey);

		return ResponseResult.SUCCESS("用户信息更新成功");
	}

	/**
	 * 修改用户密码
	 * 
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@Override
	public ResponseResult updateUserPassword(String userId, String oldPassword, String newPassword) {
		// 从 token 里面解析出来的 user，为了校验权限
		// 只有用户自己才可以修改自己的信息
		User userFromTokenKey = checkUser();
		if (userFromTokenKey == null) {
			return ResponseResult.FAILED("用户未登录");
		}
		User userFromDb = userDao.findOneById(userFromTokenKey.getId());

		// 判断当前用户的 ID 和即将要修改的用户 ID 是否一致，如果一致才可以修改
		if (!userFromDb.getId().equals(userId)) {
			return ResponseResult.FAILED("没有权限修改此用户信息");
		}

		// 对比旧密码是否正确
		boolean matches = bCryptPasswordEncoder.matches(oldPassword, userFromDb.getPassword());
		if (!matches) {
			return ResponseResult.FAILED("旧密码不正确，请重新输入");
		}

		// 新密码进行加密保存
		String encodePassword = bCryptPasswordEncoder.encode(newPassword);
		userFromDb.setPassword(encodePassword);
		userFromDb.setUpdateTime(new Date());

		userDao.save(userFromDb);

		return ResponseResult.SUCCESS("密码修改成功");
	}

	/**
	 * 退出登录
	 * <p>
	 * 1.拿到 token_key <br>
	 * 2.删除 redis 里对应的token <br>
	 * 3.删除 mysql 里对应的 refreshToken <br>
	 * 4.删除 cookie 里的 token_key <br>
	 * 
	 * @return
	 */
	@Override
	public ResponseResult doLogout() {
		// 拿到 token_key
		String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKE_KEY);
		if (TextUtils.isEmpty(tokenKey)) {
			return ResponseResult.FAILED("账号未登录");
		}

		// 删除 redis 里的 token，因为各端是独立的，所以可以删
		redisUtils.del(Constants.User.KEY_TOKEN + tokenKey);

		// 删除 mysql 里的 refreshToken
		refreshTokenDao.deleteAllByTokenKey(tokenKey);

		// 删除 cookie 里的 token_key
		CookieUtils.deleteCookie(getResponse(), Constants.User.COOKIE_TOKE_KEY);

		return ResponseResult.SUCCESS("退出登录成功");
	}

}
