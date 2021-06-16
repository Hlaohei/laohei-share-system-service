package vip.laohei.sharesystem.service;

import vip.laohei.sharesystem.entity.Share;
import vip.laohei.sharesystem.entity.ShareCategory;
import vip.laohei.sharesystem.response.ResponseResult;

public interface IShareService {

	/**
	 * 发布分享内容
	 * 
	 * @param share
	 * @return
	 */
	ResponseResult postShare(Share share);

	/**
	 * 获取分享内容列表
	 * 
	 * @param page            页码
	 * @param size            每页数量
	 * @param keyword         搜索关键词
	 * @param shareCategoryId 分享分类ID
	 * @param state           分享内容显示状态
	 * @param top             分享内容置顶状态
	 * @return
	 */
	ResponseResult listShare(int page, int size, String keyword, String shareCategoryId, String state, String top);

	/**
	 * 获取分享内容
	 * 
	 * @param shareId
	 * @return
	 */
	ResponseResult getShareById(String shareId);

	/**
	 * 置顶分享内容
	 * 
	 * @param shareId
	 * @return
	 */
	ResponseResult topShare(String shareId);

	/**
	 * 修改分享内容
	 * 
	 * @param shareId
	 * @param share
	 * @return
	 */
	ResponseResult updateShare(String shareId, Share share);

	/**
	 * 修改分享内容显示状态
	 * 
	 * @param shareId
	 * @return
	 */
	ResponseResult updateShareState(String shareId);

	/**
	 * 删除分享内容
	 * 
	 * @param shareId
	 * @return
	 */
	ResponseResult deleteShare(String shareId);

	/**
	 * 添加分享内容分类
	 * 
	 * @param shareCategory
	 * @return
	 */
	ResponseResult addShareCategory(ShareCategory shareCategory);

	/**
	 * 获取分享内容分类列表
	 * 
	 * @param page 页码
	 * @param size 每页数量
	 * @return
	 */
	ResponseResult listShareCategory(int page, int size);

	/**
	 * 获取分享内容分类
	 * 
	 * @param categoryId
	 * @return
	 */
	ResponseResult getShareCategory(String categoryId);

	/**
	 * 修改分享内容分类
	 * 
	 * @param categoryId
	 * @param shareCategory
	 * @return
	 */
	ResponseResult updateShareCategory(String categoryId, ShareCategory shareCategory);

	/**
	 * 删除分享内容分类
	 * <p>
	 * 假删除，就是改变状态，让前台看不到
	 * 
	 * @param categoryId
	 * @return
	 */
	ResponseResult deleteShareCategoryByState(String categoryId);

	/**
	 * 获取分享内容标签列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	ResponseResult listShareLabel(int page, int size);

	/**
	 * 获取分享内容数量
	 * 
	 * @return
	 */
	ResponseResult getShareCount();

	/**
	 * 获取分享内容置顶数量
	 * 
	 * @return
	 */
	ResponseResult getShareTopCount();

}
