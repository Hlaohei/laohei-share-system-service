package vip.laohei.sharesystem.service;

import vip.laohei.sharesystem.entity.Works;
import vip.laohei.sharesystem.entity.WorksCategory;
import vip.laohei.sharesystem.response.ResponseResult;

public interface IWorksService {

	/**
	 * 发布作品
	 * 
	 * @param works
	 * @return
	 */
	ResponseResult postWorks(Works works);

	/**
	 * 获取作品列表
	 * 
	 * @param page
	 * @param size
	 * @param keyword
	 * @param worksCategoryId
	 * @param state
	 * @return
	 */
	ResponseResult listWorks(int page, int size, String keyword, String worksCategoryId, String state);

	/**
	 * 获取单个作品
	 * 
	 * @param worksId
	 * @return
	 */
	ResponseResult getWorksById(String worksId);

	/**
	 * 修改作品内容
	 * 
	 * @param worksId
	 * @param works
	 * @return
	 */
	ResponseResult updateWorks(String worksId, Works works);

	/**
	 * 修改作品显示状态
	 * 
	 * @param worksId
	 * @return
	 */
	ResponseResult updateWorksState(String worksId);

	/**
	 * 删除作品
	 * 
	 * @param worksId
	 * @return
	 */
	ResponseResult deleteWorks(String worksId);

	/**
	 * 添加作品分类
	 * 
	 * @param worksCategory
	 * @return
	 */
	ResponseResult addWorksCategory(WorksCategory worksCategory);

	/**
	 * 获取作品分类列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	ResponseResult listWorksCategory(int page, int size);

	/**
	 * 获取作品分类
	 * 
	 * @param categoryId
	 * @return
	 */
	ResponseResult getWorksCategory(String categoryId);

	/**
	 * 修改作品分类
	 * 
	 * @param categoryId
	 * @param worksCategory
	 * @return
	 */
	ResponseResult updateWorksCategory(String categoryId, WorksCategory worksCategory);

	/**
	 * 删除作品分类
	 * <p>
	 * 假删除，就是改变状态，让前台看不到
	 * 
	 * @param categoryId
	 * @return
	 */
	ResponseResult deleteWorksCategoryByState(String categoryId);

	/**
	 * 获取作品数量
	 * 
	 * @return
	 */
	ResponseResult getWorksCount();

}
