package vip.laohei.sharesystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vip.laohei.sharesystem.entity.User;

/**
 * UserDao
 * 
 * @author laohei
 *
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

	/**
	 * 根据用户名查找用户
	 * 
	 * @param userName
	 * @return
	 */
	User findOneByName(String userName);

	/**
	 * 根据用户 ID 获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	User findOneById(String userId);

}
