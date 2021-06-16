package vip.laohei.sharesystem.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vip.laohei.sharesystem.entity.ShareLabel;

/**
 * ShareLabelDao
 * 
 * @author laohei
 *
 */
public interface ShareLabelDao extends JpaRepository<ShareLabel, String>, JpaSpecificationExecutor<ShareLabel> {

	/**
	 * 根据 ID 查找一个标签
	 * 
	 * @param id
	 * @return
	 */
	ShareLabelDao findOneById(String id);

	/**
	 * 根据标签名进行数量+1
	 * 
	 * @param labelName
	 * @return
	 */
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE `tb_share_label` SET `count` = `count` + 1 WHERE `name` = ?")
	int updateCountByName(String labelName);

	/**
	 * 根据标签名修改更新
	 * 
	 * @param updateTime
	 * @param labelName
	 * @return
	 */
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE `tb_share_label` SET `update_time` = ? WHERE `name` = ?")
	int updateTimeByName(Date updateTime, String labelName);

}
