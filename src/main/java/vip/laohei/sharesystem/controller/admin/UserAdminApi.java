package vip.laohei.sharesystem.controller.admin;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import vip.laohei.sharesystem.entity.User;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IUserService;

/**
 * 用户接口 - 后台管理接口
 * 
 * @author laohei
 *
 */
@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/laohei-admin/user")
@Api(value = "/laohei-admin/user", tags = { "用户接口" }, description = "后台管理接口")
public class UserAdminApi {

	@Autowired
	private IUserService userService;

	/**
	 * 获取图灵验证码
	 * 
	 * @param response
	 * @param captchaKey
	 */
	@GetMapping("/captcha/{captcha_key}")
	@ApiOperation(value = "captcha", notes = "获取图灵验证码")
	public void getCaptcha(HttpServletResponse response, @PathVariable("captcha_key") String captchaKey) {
		try {
			userService.getCaptcha(response, captchaKey);
		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	/**
	 * 登录
	 * 
	 * @param captcha
	 * @param captcha_key
	 * @param user
	 * @return
	 */
	@PostMapping("/login/{captcha}/{captcha_key}")
	@ApiOperation(value = "login", notes = "后台管理登录模块")
	public ResponseResult login(@PathVariable("captcha") String captcha, @PathVariable("captcha_key") String captchaKey, @RequestBody User user) {
		return userService.doLogin(captcha, captchaKey, user);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/user_info/{userId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "user_info", notes = "获取用户信息")
	public ResponseResult getUserInfo(@PathVariable("userId") String userId) {
		return userService.getUserInfo(userId);
	}

	/**
	 * 修改用户名
	 * 
	 * @param userId
	 * @param user
	 * @return
	 */
	@PutMapping("/user_info/{userId}")
	@ApiOperation(value = "user_info", notes = "修改用户名")
	public ResponseResult updateUserName(@PathVariable("userId") String userId, @RequestBody User user) {
		return userService.updateUserName(userId, user);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	@PutMapping("/user_password/{userId}/{oldPassword}/{newPassword}")
	@ApiOperation(value = "user_password", notes = "修改用户密码")
	public ResponseResult updateUserPassword(@PathVariable("userId") String userId, @PathVariable("oldPassword") String oldPassword, @PathVariable("newPassword") String newPassword) {
		return userService.updateUserPassword(userId, oldPassword, newPassword);
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
	@GetMapping("/logout")
	@ApiOperation(value = "logout", notes = "后台管理退出登录模块")
	public ResponseResult logout() {
		return userService.doLogout();
	}

}
