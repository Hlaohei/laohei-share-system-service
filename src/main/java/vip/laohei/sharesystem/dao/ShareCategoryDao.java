package vip.laohei.sharesystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vip.laohei.sharesystem.entity.ShareCategory;

/**
 * ShareCategoryDao
 * 
 * @author laohei
 *
 */
public interface ShareCategoryDao extends JpaRepository<ShareCategory, String>, JpaSpecificationExecutor<ShareCategory> {

	ShareCategory findOneById(String id);

	/**
	 * 根据修改状态删除分类 假删除
	 * 
	 * @param categoryId
	 * @return
	 */
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE `tb_share_category` SET `state` = '0' WHERE `id` = ?")
	int deleteCategoryByUpdateState(String categoryId);

}
