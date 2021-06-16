package vip.laohei.sharesystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vip.laohei.sharesystem.entity.RefreshToken;

/**
 * RefreshTokenDao
 * 
 * @author laohei
 *
 */
public interface RefreshTokenDao extends JpaRepository<RefreshToken, String>, JpaSpecificationExecutor<RefreshToken> {

	RefreshToken findOneByTokenKey(String tokenKey);

	RefreshToken findOneByUserId(String userId);

	int deleteAllByUserId(String userId);

	int deleteAllByTokenKey(String tokenKey);

	/**
	 * 通过更新 token_key 删除 token_key
	 * 
	 * @param tokenKey
	 */
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE `tb_refresh_token` SET `token_key` = '' WHERE `token_key` = ?")
	void deleteTokenKey(String tokenKey);

}
