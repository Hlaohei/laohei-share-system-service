package vip.laohei.sharesystem.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import vip.laohei.sharesystem.entity.User;
import vip.laohei.sharesystem.response.ResponseResult;

public interface IUserService {

	/**
	 * 获取图灵验证码
	 * 
	 * @param response
	 * @param captchaKey
	 * @throws IOException
	 */
	void getCaptcha(HttpServletResponse response, String captchaKey) throws Exception;

	/**
	 * 登录
	 * 
	 * @param captcha
	 * @param captcha_key
	 * @param user
	 * @return
	 */
	ResponseResult doLogin(String captcha, String captchaKey, User user);

	/**
	 * 检查用户是否登录
	 * 
	 * @return
	 */
	User checkUser();

	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	ResponseResult getUserInfo(String userId);

	/**
	 * 修改用户名
	 * 
	 * @param userId
	 * @param user
	 * @return
	 */
	ResponseResult updateUserName(String userId, User user);

	/**
	 * 修改用户密码
	 * 
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	ResponseResult updateUserPassword(String userId, String oldPassword, String newPassword);

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
	ResponseResult doLogout();

}
