package vip.laohei.sharesystem.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import vip.laohei.sharesystem.entity.User;
import vip.laohei.sharesystem.service.IUserService;
import vip.laohei.sharesystem.utils.Constants;
import vip.laohei.sharesystem.utils.CookieUtils;
import vip.laohei.sharesystem.utils.TextUtils;

/**
 * 定义检查用户是否登录的注解
 * 
 * @author laohei
 *
 */
@Service("permission")
public class PermissionService {

	@Autowired
	private IUserService userService;

	/**
	 * 判断是否登录
	 * 
	 * @return
	 */
	public boolean login() {
		// 拿到 request 和 response
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();

		String tokenKey = null;
		try {
			tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKE_KEY);
		} catch (Exception e) {
			if (TextUtils.isEmpty(tokenKey)) {
				return false;
			}
			return false;
		}

		User user = userService.checkUser();
		if (user == null) {
			return false;
		}

		return true;
	}

}
