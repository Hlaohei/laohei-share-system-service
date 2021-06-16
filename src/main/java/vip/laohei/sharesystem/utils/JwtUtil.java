package vip.laohei.sharesystem.utils;

import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token 工具类
 * 
 * @author laohei
 *
 */
public class JwtUtil {

	// 盐值
	private static String key = "ad128433d8e3356e7024009bf6add2ab";

	// 毫秒为单位
	private static long ttl = Constants.TimeValueMillisecond.HOUR_2; // 默认 2 小时

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		JwtUtil.key = key;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		JwtUtil.ttl = ttl;
	}

	/**
	 * 创建 JWT
	 * 
	 * @param claims 载荷内容
	 * @param ttl    有效时长
	 * @return
	 */
	public static String createToken(Map<String, Object> claims, long ttl) {
		JwtUtil.ttl = ttl;
		return createToken(claims);
	}

	public static String createRefreshToken(String userId, long ttl) {
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		JwtBuilder builder = Jwts.builder().setId(userId).setIssuedAt(now).signWith(SignatureAlgorithm.HS256, key);
		if (ttl > 0) {
			builder.setExpiration(new Date(nowMillis + ttl));
		}
		return builder.compact();
	}

	/**
	 * 创建 JWT
	 * 
	 * @param claims 载荷内容
	 * @return
	 */
	public static String createToken(Map<String, Object> claims) {

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		JwtBuilder builder = Jwts.builder().setIssuedAt(now).signWith(SignatureAlgorithm.HS256, key);

		if (claims != null) {
			builder.setClaims(claims);
		}

		if (ttl > 0) {
			builder.setExpiration(new Date(nowMillis + ttl));
		}
		return builder.compact();
	}

	/**
	 * 解析 token
	 * 
	 * @param jwtStr
	 * @return
	 */
	public static Claims parseJWT(String jwtStr) {
		return Jwts.parser().setSigningKey(key).parseClaimsJws(jwtStr).getBody();
	}

}