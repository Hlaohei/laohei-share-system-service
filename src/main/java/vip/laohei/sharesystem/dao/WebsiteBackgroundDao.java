package vip.laohei.sharesystem.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vip.laohei.sharesystem.entity.WebsiteBackground;

/**
 * WebsiteBackgroundDao
 * 
 * @author laohei
 *
 */
public interface WebsiteBackgroundDao extends JpaRepository<WebsiteBackground, String>, JpaSpecificationExecutor<WebsiteBackground> {

	WebsiteBackground findOneById(String id);

	@Modifying
	int deleteAllById(String id);

	/**
	 * 获取所有正常显示状态的网站背景
	 * 
	 * @return
	 */
	@Query(nativeQuery = true, value = "SELECT * FROM `tb_website_background` WHERE `state` = '1'")
	List<WebsiteBackground> findAllByState();

	/**
	 * 获取固定一个的网站背景
	 * 
	 * @return
	 */
	@Query(nativeQuery = true, value = "SELECT * FROM `tb_website_background` WHERE `fixed` = '1'")
	WebsiteBackground findOneByFixed();

}
