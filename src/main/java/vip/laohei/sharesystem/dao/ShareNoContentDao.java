package vip.laohei.sharesystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import vip.laohei.sharesystem.entity.ShareNoContent;

/**
 * ShareNoContentDao
 * 
 * @author laohei
 *
 */
public interface ShareNoContentDao extends JpaRepository<ShareNoContent, String>, JpaSpecificationExecutor<ShareNoContent> {

	ShareNoContent findOneById(String id);

	/**
	 * 只获取置顶的分享内容数量
	 * 
	 * @return
	 */
	@Query(nativeQuery = true, value = "SELECT COUNT(*) FROM `tb_share` WHERE `top` = '1'")
	int getShareTopCount();

}
