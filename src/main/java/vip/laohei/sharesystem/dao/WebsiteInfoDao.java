package vip.laohei.sharesystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vip.laohei.sharesystem.entity.WebsiteInfo;

/**
 * WebsiteInfoDao
 * 
 * @author laohei
 *
 */
public interface WebsiteInfoDao extends JpaRepository<WebsiteInfo, String>, JpaSpecificationExecutor<WebsiteInfo> {

	WebsiteInfo findOneById(String id);

	/**
	 * 获取网站访问量信息 只有一条整体数据
	 * 
	 * @return
	 */
	@Modifying
	@Query(nativeQuery = true, value = "SELECT `view_count` FROM `tb_website_info` WHERE `id` = '001'")
	String findWebsiteViewCount();

}
