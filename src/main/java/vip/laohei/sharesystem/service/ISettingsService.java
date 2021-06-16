package vip.laohei.sharesystem.service;

import vip.laohei.sharesystem.entity.WebsiteBackground;
import vip.laohei.sharesystem.entity.WebsiteInfo;
import vip.laohei.sharesystem.response.ResponseResult;

public interface ISettingsService {

	/**
	 * 获取网站信息
	 * 
	 * @return
	 */
	ResponseResult getWebsiteInfo();

	/**
	 * 修改网站信息
	 * 
	 * @param websiteInfo
	 * @return
	 */
	ResponseResult updateWebsiteInfo(WebsiteInfo websiteInfo);

	/**
	 * 添加动态背景
	 * 
	 * @param websiteBackground
	 * @return
	 */
	ResponseResult addWebsiteBackground(WebsiteBackground websiteBackground);

	/**
	 * 获取动态背景列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	ResponseResult listWebsiteBackground(int page, int size);

	/**
	 * 获取动态背景
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	ResponseResult getWebsiteBackground(String websiteBackgroundId);

	/**
	 * 修改动态背景
	 * 
	 * @param websiteBackgroundId
	 * @param websiteBackground
	 * @return
	 */
	ResponseResult updateWebsiteBackground(String websiteBackgroundId, WebsiteBackground websiteBackground);

	/**
	 * 修改动态背景固定状态
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	ResponseResult updateWebsiteBackgroundFixed(String websiteBackgroundId);

	/**
	 * 修改动态背景显示状态
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	ResponseResult updateWebsiteBackgroundState(String websiteBackgroundId);

	/**
	 * 删除动态背景
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	ResponseResult deleteWebsiteBackground(String websiteBackgroundId);

	/**
	 * 获取网站访问量
	 * 
	 * @return
	 */
	ResponseResult getWebsiteViewCount();

	/**
	 * 统计页面访问量
	 * <p>
	 * 递增的统计： <br>
	 * 统计信息，通过 redis 来统计，数据也会保存到 mysql 里面 <br>
	 * 不会每次都更新到 mysql 里，当用户去获取访问量的时候，会更新一次 <br>
	 * 平时的调用，只增加 redis 里的访问量
	 * <p>
	 * 保存 redis 时机：每个页面访问的时候-->如果不存在，就从 mysql 里面拿，写到 redis 里面，如果有，就自增 <br>
	 * 保存 mysql 时机：用户读取网站总访问量的时候-->读取 redis 里的数据，并且保存到 mysql 中，如果 redis 里面没有，就读取
	 * mysql 写到 redis 里
	 */
	void updateViewCount();

	/**
	 * 获取网站动态背景 (根据随机或固定状态获取)
	 * 
	 * @return
	 */
	ResponseResult getWebsiteBackgroundByRandom();

}
