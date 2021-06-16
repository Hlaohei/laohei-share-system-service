package vip.laohei.sharesystem.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vip.laohei.sharesystem.dao.WebsiteBackgroundDao;
import vip.laohei.sharesystem.dao.WebsiteInfoDao;
import vip.laohei.sharesystem.entity.WebsiteBackground;
import vip.laohei.sharesystem.entity.WebsiteInfo;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.ISettingsService;
import vip.laohei.sharesystem.utils.Constants;
import vip.laohei.sharesystem.utils.IdWorker;
import vip.laohei.sharesystem.utils.RedisUtils;
import vip.laohei.sharesystem.utils.TextUtils;

/**
 * 网站设置 实现类
 * 
 * @author laohei
 *
 */
@Service
@Transactional
public class SettingsServiceImpl extends BaseService implements ISettingsService {

	@Autowired
	private WebsiteInfoDao websiteInfoDao;

	@Autowired
	private WebsiteBackgroundDao websiteBackgroundDao;

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private Random random;

	/**
	 * 获取网站信息
	 * 
	 * @return
	 */
	@Override
	public ResponseResult getWebsiteInfo() {
		WebsiteInfo websiteInfo = websiteInfoDao.findOneById("001");

		return ResponseResult.SUCCESS("获取网站信息成功").setData(websiteInfo);
	}

	/**
	 * 修改网站信息
	 * 
	 * @param websiteInfo
	 * @return
	 */
	@Override
	public ResponseResult updateWebsiteInfo(WebsiteInfo websiteInfo) {
		// 检查数据完整性
		String title = websiteInfo.getTitle();
		if (TextUtils.isEmpty(title)) {
			return ResponseResult.FAILED("网站标题不可以为空");
		}
		String description = websiteInfo.getDescription();
		if (TextUtils.isEmpty(description)) {
			return ResponseResult.FAILED("网站描述不可以为空");
		}
		String keywords = websiteInfo.getKeywords();
		if (TextUtils.isEmpty(keywords)) {
			return ResponseResult.FAILED("网站关键字不可以为空");
		}
		// 其他数据可以为空

		// 先从数据库找出网站信息
		WebsiteInfo websiteInfoFromDb = websiteInfoDao.findOneById("001");

		websiteInfoFromDb.setTitle(title);
		websiteInfoFromDb.setDescription(description);
		websiteInfoFromDb.setKeywords(keywords);
		websiteInfoFromDb.setBeianIcp(websiteInfo.getBeianIcp());
		websiteInfoFromDb.setBeianIcpUrl(websiteInfo.getBeianIcpUrl());
		websiteInfoFromDb.setBeianGongan(websiteInfo.getBeianGongan());
		websiteInfoFromDb.setBeianGonganUrl(websiteInfo.getBeianGonganUrl());
		websiteInfoFromDb.setNotice(websiteInfo.getNotice());
		websiteInfoFromDb.setUpdateTime(new Date());

		// 保存数据
		websiteInfoDao.save(websiteInfoFromDb);

		return ResponseResult.SUCCESS("更新网站信息成功");
	}

	/**
	 * 添加动态背景
	 * 
	 * @param websiteBackground
	 * @return
	 */
	@Override
	public ResponseResult addWebsiteBackground(WebsiteBackground websiteBackground) {
		// 检查数据完整性
		// 名称、描述、url链接、状态（默认显示）
		// 检查名称
		String name = websiteBackground.getName();
		if (TextUtils.isEmpty(name)) {
			return ResponseResult.FAILED("名称不可以为空，请填写名称");
		}
		if (name.length() > Constants.Share.TITLE_MAX_LENGTH) {
			return ResponseResult.FAILED("名称不可以超过" + Constants.Share.TITLE_MAX_LENGTH + "个字符");
		}
		// 检查描述
		String description = websiteBackground.getDescription();
		if (TextUtils.isEmpty(description)) {
			return ResponseResult.FAILED("描述不能为空，请填写描述");
		}
		// 检查url链接
		String url = websiteBackground.getUrl();
		if (TextUtils.isEmpty(url)) {
			return ResponseResult.FAILED("链接不能为空，请填写链接");
		}

		// 补充其余数据
		websiteBackground.setId(idWorker.nextId() + "");
		websiteBackground.setCreateTime(new Date());
		websiteBackground.setUpdateTime(new Date());

		// 保存到数据库
		websiteBackgroundDao.save(websiteBackground);

		// 返回结果
		return ResponseResult.SUCCESS("添加动态背景成功");
	}

	/**
	 * 获取动态背景列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@Override
	public ResponseResult listWebsiteBackground(int page, int size) {
		// 简单处理 page 和 size
		page = checkPage(page);
		size = checkSize(size);

		// 处理分页和排序条件
		Sort sort = Sort.by(Sort.Order.desc("createTime"));
		Pageable pageable = PageRequest.of(page - 1, size, sort);
		Page<WebsiteBackground> all = websiteBackgroundDao.findAll(pageable);

		return ResponseResult.SUCCESS("获取动态背景列表成功").setData(all);
	}

	/**
	 * 获取动态背景
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	@Override
	public ResponseResult getWebsiteBackground(String websiteBackgroundId) {
		// 直接从数据库查询
		WebsiteBackground websiteBackground = websiteBackgroundDao.findOneById(websiteBackgroundId);
		if (websiteBackground == null) {
			return ResponseResult.FAILED("动态背景不存在");
		}
		return ResponseResult.SUCCESS("获取动态背景成功").setData(websiteBackground);
	}

	/**
	 * 修改动态背景
	 * 
	 * @param websiteBackgroundId
	 * @param websiteBackground
	 * @return
	 */
	@Override
	public ResponseResult updateWebsiteBackground(String websiteBackgroundId, WebsiteBackground websiteBackground) {
		// 先从数据库找出来
		WebsiteBackground websiteBackgroundFromDb = websiteBackgroundDao.findOneById(websiteBackgroundId);
		if (websiteBackgroundFromDb == null) {
			return ResponseResult.FAILED("动态背景不存在");
		}

		// 判断要修改的数据内容
		String name = websiteBackground.getName();
		if (!TextUtils.isEmpty(name)) {
			websiteBackgroundFromDb.setName(name);
		}
		String description = websiteBackground.getDescription();
		if (!TextUtils.isEmpty(description)) {
			websiteBackgroundFromDb.setDescription(description);
		}
		String url = websiteBackground.getUrl();
		if (!TextUtils.isEmpty(url)) {
			websiteBackgroundFromDb.setUrl(url);
		}
		// 设置更新时间
		websiteBackgroundFromDb.setUpdateTime(new Date());

		// 保存数据
		websiteBackgroundDao.save(websiteBackgroundFromDb);

		// 返回结果
		return ResponseResult.SUCCESS("修改动态背景成功");
	}

	/**
	 * 修改动态背景固定状态
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	@Override
	public ResponseResult updateWebsiteBackgroundFixed(String websiteBackgroundId) {
		// 直接数据库修改 state 状态
		// 检查分享内容是否存在
		WebsiteBackground websiteBackground = websiteBackgroundDao.findOneById(websiteBackgroundId);
		if (websiteBackground == null) {
			return ResponseResult.FAILED("动态背景不存在");
		}

		String fixed = websiteBackground.getFixed();
		if (Constants.WebsiteBackground.FIXED_NOT_FIXED.equals(fixed)) {
			websiteBackground.setFixed(Constants.WebsiteBackground.FIXED_IS_FIXED);
			websiteBackgroundDao.save(websiteBackground);
			return ResponseResult.SUCCESS("固定动态背景成功");
		}
		if (Constants.WebsiteBackground.FIXED_IS_FIXED.equals(fixed)) {
			websiteBackground.setFixed(Constants.WebsiteBackground.FIXED_NOT_FIXED);
			websiteBackgroundDao.save(websiteBackground);
			return ResponseResult.SUCCESS("取消固定动态背景成功");
		}

		return ResponseResult.FAILED("不支持该操作");
	}

	/**
	 * 修改动态背景显示状态
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	@Override
	public ResponseResult updateWebsiteBackgroundState(String websiteBackgroundId) {
		// 直接数据库修改 state 状态
		// 检查分享内容是否存在
		WebsiteBackground websiteBackground = websiteBackgroundDao.findOneById(websiteBackgroundId);
		if (websiteBackground == null) {
			return ResponseResult.FAILED("动态背景不存在");
		}

		String state = websiteBackground.getState();
		if (Constants.WebsiteBackground.STATE_NOT_SHOW.equals(state)) {
			websiteBackground.setState(Constants.WebsiteBackground.STATE_SHOW);
			websiteBackgroundDao.save(websiteBackground);
			return ResponseResult.SUCCESS("显示动态背景成功");
		}
		if (Constants.WebsiteBackground.STATE_SHOW.equals(state)) {
			websiteBackground.setState(Constants.WebsiteBackground.STATE_NOT_SHOW);
			websiteBackgroundDao.save(websiteBackground);
			return ResponseResult.SUCCESS("隐藏动态背景成功");
		}

		return ResponseResult.FAILED("不支持该操作");
	}

	/**
	 * 删除动态背景
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	@Override
	public ResponseResult deleteWebsiteBackground(String websiteBackgroundId) {
		// 直接删除数据库内容
		int result = websiteBackgroundDao.deleteAllById(websiteBackgroundId);
		if (result > 0) {
			// 返回信息
			return ResponseResult.SUCCESS("删除动态背景成功");
		}

		return ResponseResult.FAILED("删除动态背景失败，动态背景不存在");
	}

	/**
	 * 获取网站访问量
	 * 
	 * @return
	 */
	@Override
	public ResponseResult getWebsiteViewCount() {
		// 先从 redis 里拿出来
		String viewCountFromRedis = (String) redisUtils.get(Constants.WebsiteInfo.WEB_SITE_VIEW_COUNT);

		// 从数据库中找出来
		// String viewCountFromDb = websiteInfoDao.findWebsiteViewCount();
		WebsiteInfo websiteInfo = websiteInfoDao.findOneById("001");
		String viewCountFromDb = String.valueOf(websiteInfo.getViewCount());

		if (TextUtils.isEmpty(viewCountFromRedis)) {
			viewCountFromRedis = viewCountFromDb;
			redisUtils.set(Constants.WebsiteInfo.WEB_SITE_VIEW_COUNT, viewCountFromRedis);
		} else {
			// 把 redis 里的数据更新到数据库里面
			websiteInfo.setViewCount(Long.parseLong(viewCountFromRedis));
			websiteInfo.setUpdateTime(new Date());
			websiteInfoDao.save(websiteInfo);
		}

		Map<String, Long> result = new HashMap<>();
		result.put(Constants.WebsiteInfo.WEB_SITE_VIEW_COUNT, Long.parseLong(viewCountFromRedis));

		return ResponseResult.SUCCESS("获取网站访问量成功").setData(result);
	}

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
	@Override
	public void updateViewCount() {
		// 先从 redis 里拿出来
		String viewCountFromRedis = (String) redisUtils.get(Constants.WebsiteInfo.WEB_SITE_VIEW_COUNT);

		if (viewCountFromRedis == null) {
			WebsiteInfo websiteInfo = websiteInfoDao.findOneById("001");
			String viewCountFromDb = String.valueOf(websiteInfo.getViewCount());
			redisUtils.set(Constants.WebsiteInfo.WEB_SITE_VIEW_COUNT, viewCountFromDb);
		} else {
			// 自增
			redisUtils.incr(Constants.WebsiteInfo.WEB_SITE_VIEW_COUNT, 1);
		}

	}

	/**
	 * 获取网站动态背景 (根据随机或固定状态获取)
	 * 
	 * @return
	 */
	@Override
	public ResponseResult getWebsiteBackgroundByRandom() {
		// 先匹配有没有固定的网站背景
		WebsiteBackground fixedWebsiteBackground = websiteBackgroundDao.findOneByFixed();
		if (fixedWebsiteBackground != null) {
			return ResponseResult.SUCCESS("获取网站动态背景成功").setData(fixedWebsiteBackground);
		}

		// 如果没有固定的网站背景，就从全部背景里面随机选一个
		List<WebsiteBackground> all = websiteBackgroundDao.findAllByState();

		int n = random.nextInt(all.size());
		WebsiteBackground randomWebsiteBackground = all.get(n);

		return ResponseResult.SUCCESS("获取网站动态背景成功").setData(randomWebsiteBackground);
	}

}
