package vip.laohei.sharesystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vip.laohei.sharesystem.entity.Image;

/**
 * ImageDao
 * 
 * @author laohei
 *
 */
public interface ImageDao extends JpaRepository<Image, String>, JpaSpecificationExecutor<Image> {

	/**
	 * 通过修改状态删除图片 假删除
	 * 
	 * @param imageId
	 * @return
	 */
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE `tb_image` SET `state` = '0' WHERE `id` = ?")
	int deleteImageByUpdateState(String imageId);

}
