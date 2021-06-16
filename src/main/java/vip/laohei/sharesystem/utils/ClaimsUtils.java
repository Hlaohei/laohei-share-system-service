package vip.laohei.sharesystem.utils;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import vip.laohei.sharesystem.entity.User;

/**
 * Claims 工具类
 * 
 * @author laohei
 *
 */
public class ClaimsUtils {

	public static final String ID = "id";
	public static final String USER_NAME = "user_name";

	public static Map<String, Object> user2Claims(User user) {

		Map<String, Object> claims = new HashMap<>();
		claims.put(ID, user.getId());
		claims.put(USER_NAME, user.getName());

		return claims;
	}

	public static User claim2user(Claims claims) {
		User user = new User();

		String id = (String) claims.get(ID);
		user.setId(id);
		String userName = (String) claims.get(USER_NAME);
		user.setName(userName);

		return user;
	}

}
