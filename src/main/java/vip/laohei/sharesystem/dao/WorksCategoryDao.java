package vip.laohei.sharesystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vip.laohei.sharesystem.entity.WorksCategory;

/**
 * WorksCategoryDao
 * 
 * @author laohei
 *
 */
public interface WorksCategoryDao extends JpaRepository<WorksCategory, String>, JpaSpecificationExecutor<WorksCategory> {

	WorksCategory findOneById(String id);

	/**
	 * 通过更新状态删除分类 假删除
	 * 
	 * @param categoryId
	 * @return
	 */
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE `tb_works_category` SET `state` = '0' WHERE `id` = ?")
	int deleteCategoryByUpdateState(String categoryId);

}
