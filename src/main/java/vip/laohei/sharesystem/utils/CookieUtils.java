package vip.laohei.sharesystem.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * cookies 工具类
 * 
 * @author laohei
 *
 */
@Slf4j
public class CookieUtils {

	public static final int default_age = 60 * 60 * 24 * 365; // 1年

	public static final String domain = "127.0.0.1";

	/**
	 * 设置 cookie 值
	 * 
	 * @param response
	 * @param key
	 * @param value
	 */
	public static void setUpCookie(HttpServletResponse response, String key, String value) {
		setUpCookie(response, key, value, default_age);
	}

	/**
	 * 设置 cookie 值
	 * 
	 * @param response
	 * @param key
	 * @param value
	 * @param age
	 */
	public static void setUpCookie(HttpServletResponse response, String key, String value, int age) {
		Cookie cookie = new Cookie(key, value);
		cookie.setPath("/");
//		cookie.setDomain(domain);
		cookie.setMaxAge(age);
		response.addCookie(cookie);
	}

	/**
	 * 删除 cookie
	 * 
	 * @param response
	 * @param key
	 */
	public static void deleteCookie(HttpServletResponse response, String key) {
		setUpCookie(response, key, null, 0);
	}

	/**
	 * 获取 cookie
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			log.info("cookies is null...");
			return null;
		}
		for (Cookie cookie : cookies) {
			if (key.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

}
