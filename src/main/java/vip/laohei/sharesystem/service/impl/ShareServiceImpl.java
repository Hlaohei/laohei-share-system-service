package vip.laohei.sharesystem.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import vip.laohei.sharesystem.dao.ShareCategoryDao;
import vip.laohei.sharesystem.dao.ShareDao;
import vip.laohei.sharesystem.dao.ShareLabelDao;
import vip.laohei.sharesystem.dao.ShareNoContentDao;
import vip.laohei.sharesystem.entity.Share;
import vip.laohei.sharesystem.entity.ShareCategory;
import vip.laohei.sharesystem.entity.ShareLabel;
import vip.laohei.sharesystem.entity.ShareNoContent;
import vip.laohei.sharesystem.entity.User;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IShareService;
import vip.laohei.sharesystem.service.IUserService;
import vip.laohei.sharesystem.utils.Constants;
import vip.laohei.sharesystem.utils.IdWorker;
import vip.laohei.sharesystem.utils.RedisUtils;
import vip.laohei.sharesystem.utils.TextUtils;

/**
 * 分享服务 实现类
 * 
 * @author laohei
 *
 */
@Service
@Transactional
public class ShareServiceImpl extends BaseService implements IShareService {

	@Autowired
	private IUserService userService;

	@Autowired
	private ShareDao shareDao;

	@Autowired
	private ShareNoContentDao shareNoContentDao;

	@Autowired
	private ShareLabelDao shareLabelDao;

	@Autowired
	private ShareCategoryDao shareCategoryDao;

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisUtils redisUtils;

	@Autowired
	private Gson gson;

	/**
	 * 发布分享内容
	 * <p>
	 * 自己用的分享系统，只允许自己发布内容，所以可以做一个简单的数据库增删改查功能
	 * 
	 * @param share
	 * @return
	 */
	@Override
	public ResponseResult postShare(Share share) {
		User user = userService.checkUser();
		if (user == null) {
			return ResponseResult.FAILED("账号未登录，请先登录管理员账号");
		}

		// 检查数据完整性
		// 标题、分类ID、概述、内容、标签、封面、状态（默认显示）、置顶（默认不置顶）
		// 检查标题
		String title = share.getTitle();
		if (TextUtils.isEmpty(title)) {
			return ResponseResult.FAILED("标题不可以为空，请填写标题");
		}
		if (title.length() > Constants.Share.TITLE_MAX_LENGTH) {
			return ResponseResult.FAILED("标题不可以超过" + Constants.Share.TITLE_MAX_LENGTH + "个字符");
		}
		// 检查分类ID
		String shareCategoryId = share.getShareCategoryId();
		if (TextUtils.isEmpty(shareCategoryId)) {
			return ResponseResult.FAILED("分类不能为空，请选择一个分类");
		}
		// 检查概述
		String summary = share.getSummary();
		if (TextUtils.isEmpty(summary)) {
			return ResponseResult.FAILED("概述不能为空，请填写分享概述");
		}
		if (summary.length() > Constants.Share.SUMMARY_MAX_LENGTH) {
			return ResponseResult.FAILED("概述不可以超过" + Constants.Share.SUMMARY_MAX_LENGTH + "个字符");
		}
		// 检查内容
		String content = share.getContent();
		if (TextUtils.isEmpty(content)) {
			return ResponseResult.FAILED("内容不能为空，请填写分享内容");
		}
		// 检查标签 格式为 XX-XX-XX-XX-XX
		String labels = share.getLabel();
		if (TextUtils.isEmpty(labels)) {
			return ResponseResult.FAILED("标签不能为空，请填写分享标签");
		}
		// 封面可以为空，如果没有封面会显示默认背景

		// 补充其余数据
		share.setId(idWorker.nextId() + "");
		share.setCreateTime(new Date());
		share.setUpdateTime(new Date());

		// 保存到数据库
		shareDao.save(share);

		// 标签统计，把 XX-XX-XX-XX-XX 打散，入库，统计
		this.setupLabels(labels);

		// 返回结果
		return ResponseResult.SUCCESS("发布分享内容成功");
	}

	/**
	 * 根据发布文章时的标签自动创建标签统计
	 * 
	 * @param labels
	 */
	private void setupLabels(String labels) {
		List<String> labelList = new ArrayList<>();
		if (labels.contains("-")) {
			labelList.addAll(Arrays.asList(labels.split("-")));
		} else {
			labelList.add(labels);
		}
		// 入库，统计
		for (String label : labelList) {
			int result = shareLabelDao.updateCountByName(label);
			shareLabelDao.updateTimeByName(new Date(), label);
			if (result == 0) {
				ShareLabel targetLabel = new ShareLabel();
				targetLabel.setId(idWorker.nextId() + "");
				targetLabel.setCount(1);
				targetLabel.setName(label);
				targetLabel.setCreateTime(new Date());
				targetLabel.setUpdateTime(new Date());
				shareLabelDao.save(targetLabel);
			}
		}
	}

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
	@Override
	public ResponseResult listShare(int page, int size, String keyword, String shareCategoryId, String state, String top) {
		// 简单处理 page 和 size
		page = checkPage(page);
		size = checkSize(size);

		// 处理分页和排序条件
		Sort sort = Sort.by(Sort.Order.desc("createTime"));
		Pageable pageable = PageRequest.of(page - 1, size, sort);

		// 开始查询
		Page<ShareNoContent> all = shareNoContentDao.findAll(new Specification<ShareNoContent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ShareNoContent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (!TextUtils.isEmpty(keyword)) {
					Predicate titlePre = cb.like(root.get("title").as(String.class), "%" + keyword + "%");
					predicates.add(titlePre);
				}
				if (!TextUtils.isEmpty(shareCategoryId)) {
					Predicate categoryIdPre = cb.equal(root.get("shareCategoryId").as(String.class), shareCategoryId);
					predicates.add(categoryIdPre);
				}
				if (!TextUtils.isEmpty(state)) {
					Predicate statePre = cb.equal(root.get("state").as(String.class), state);
					predicates.add(statePre);
				}
				if (!TextUtils.isEmpty(top)) {
					Predicate topPre = cb.equal(root.get("top").as(String.class), top);
					predicates.add(topPre);
				}
				Predicate[] preArray = new Predicate[predicates.size()];
				predicates.toArray(preArray);
				return cb.and(preArray);
			}
		}, pageable);

		return ResponseResult.SUCCESS("获取分享内容列表成功").setData(all);
	}

	/**
	 * 获取分享内容
	 * 
	 * @param shareId
	 * @return
	 */
	@Override
	public ResponseResult getShareById(String shareId) {
		// 先从 redis 里获取文章
		// 如果没有，再去 mysql 里获取
		String shareJson = (String) redisUtils.get(Constants.Share.KEY_SHARE_CACHE + shareId);
		if (!TextUtils.isEmpty(shareJson)) {
			Share share = gson.fromJson(shareJson, Share.class);
			// 增加阅读量
			redisUtils.incr(Constants.Share.KEY_SHARE_VIEW_COUNT + shareId, 1);

			return ResponseResult.SUCCESS("获取分享内容成功-缓存").setData(share);
		}

		// 查询出分享内容
		Share share = shareDao.findOneById(shareId);
		if (share == null) {
			return ResponseResult.FAILED("分享内容不存在");
		}

		// 判断分享内容显示状态
		String state = share.getState();
		if (Constants.Share.STATE_SHOW.equals(state)) {
			// 正常显示的才可以增加阅读量
			// 设置阅读量的 key，先从 redis 里拿，如果 redis 里没有，就从 share 中获取，并且添加到 redis 里
			String viewCount = (String) redisUtils.get(Constants.Share.KEY_SHARE_VIEW_COUNT + shareId);
			if (TextUtils.isEmpty(viewCount)) {
				long newCount = share.getViewCount() + 1;
				redisUtils.set(Constants.Share.KEY_SHARE_VIEW_COUNT + shareId, String.valueOf(newCount));
			} else {
				// 有的话就更新到 mysql 中
				long newCount = redisUtils.incr(Constants.Share.KEY_SHARE_VIEW_COUNT + shareId, 1);
				share.setViewCount(newCount);
				shareDao.save(share);
			}

			// 添加这一个内容到 redis 缓存里，保存 5 分钟
			redisUtils.set(Constants.Share.KEY_SHARE_CACHE + shareId, gson.toJson(share), Constants.TimeValueSecond.MIN_5);
		}

		return ResponseResult.SUCCESS("获取分享内容成功").setData(share);
	}

	/**
	 * 置顶分享内容
	 * 
	 * @param shareId
	 * @return
	 */
	@Override
	public ResponseResult topShare(String shareId) {
		// 检查分享内容是否存在，必须是已发布的内容才能置顶
		Share share = shareDao.findOneById(shareId);
		if (share == null) {
			return ResponseResult.FAILED("分享内容不存在");
		}

		String top = share.getTop();
		if (Constants.Share.NOT_TOP.equals(top)) {
			share.setTop(Constants.Share.IS_TOP);
			shareDao.save(share);
			return ResponseResult.SUCCESS("置顶分享内容成功");
		}
		if (Constants.Share.IS_TOP.equals(top)) {
			share.setTop(Constants.Share.NOT_TOP);
			shareDao.save(share);
			return ResponseResult.SUCCESS("取消置顶成功");
		}

		return ResponseResult.FAILED("不支持该操作");
	}

	/**
	 * 修改分享内容
	 * 
	 * @param shareId
	 * @param share
	 * @return
	 */
	@Override
	public ResponseResult updateShare(String shareId, Share share) {
		// 先从数据库里面把分享内容找出来
		Share shareFromDb = shareDao.findOneById(shareId);
		if (shareFromDb == null) {
			return ResponseResult.FAILED("分享内容不存在");
		}

		// 内容修改
		// 标题可修改
		String title = share.getTitle();
		if (!TextUtils.isEmpty(title)) {
			shareFromDb.setTitle(title);
		}
		// 分类可修改
		String shareCategoryId = share.getShareCategoryId();
		if (!TextUtils.isEmpty(shareCategoryId)) {
			shareFromDb.setShareCategoryId(shareCategoryId);
		}
		// 概述可修改
		String summary = share.getSummary();
		if (!TextUtils.isEmpty(summary)) {
			shareFromDb.setSummary(summary);
		}
		// 内容可修改
		String content = share.getContent();
		if (!TextUtils.isEmpty(content)) {
			shareFromDb.setContent(content);
		}
		// 标签可修改
		String label = share.getLabel();
		if (!TextUtils.isEmpty(label)) {
			shareFromDb.setLabel(label);

			// 标签统计，把 XX-XX-XX-XX-XX 打散，入库，统计
			this.setupLabels(label);
		}
		// 封面可修改
		String cover = share.getCover();
		if (!TextUtils.isEmpty(cover)) {
			shareFromDb.setCover(cover);
		}
		// 设置更新时间
		shareFromDb.setUpdateTime(new Date());

		// 保存数据
		shareDao.save(shareFromDb);

		// 返回结果
		return ResponseResult.SUCCESS("修改分享内容成功");
	}

	/**
	 * 修改分享内容显示状态
	 * 
	 * @param shareId
	 * @return
	 */
	@Override
	public ResponseResult updateShareState(String shareId) {
		// 直接数据库修改 state 状态
		// 检查分享内容是否存在
		Share share = shareDao.findOneById(shareId);
		if (share == null) {
			return ResponseResult.FAILED("分享内容不存在");
		}

		String state = share.getState();
		if (Constants.Share.STATE_NOT_SHOW.equals(state)) {
			share.setState(Constants.Share.STATE_SHOW);
			shareDao.save(share);
			return ResponseResult.SUCCESS("显示分享内容成功");
		}
		if (Constants.Share.STATE_SHOW.equals(state)) {
			share.setState(Constants.Share.STATE_NOT_SHOW);
			shareDao.save(share);
			return ResponseResult.SUCCESS("隐藏分享内容成功");
		}

		return ResponseResult.FAILED("不支持该操作");
	}

	/**
	 * 删除分享内容
	 * 
	 * @param shareId
	 * @return
	 */
	@Override
	public ResponseResult deleteShare(String shareId) {
		// 直接删除数据库内容
		int result = shareDao.deleteAllById(shareId);
		if (result > 0) {
			redisUtils.del(Constants.Share.KEY_SHARE_CACHE + shareId);
			redisUtils.del(Constants.Share.KEY_SHARE_VIEW_COUNT + shareId);

			// 返回信息
			return ResponseResult.SUCCESS("删除分享内容成功");
		}

		return ResponseResult.FAILED("删除分享内容失败，内容不存在");
	}

	/**
	 * 添加分享内容分类
	 * 
	 * @param shareCategory
	 * @return
	 */
	@Override
	public ResponseResult addShareCategory(ShareCategory shareCategory) {
		// 检查数据完整性
		// 必须要填写的数据：分类名称、分类拼音、分类描述
		if (TextUtils.isEmpty(shareCategory.getName())) {
			return ResponseResult.FAILED("分类名称不可以为空，请填写分类名称");
		}
		if (TextUtils.isEmpty(shareCategory.getPinyin())) {
			return ResponseResult.FAILED("分类拼音不可以为空，请填写分类拼音");
		}
		if (TextUtils.isEmpty(shareCategory.getDescription())) {
			return ResponseResult.FAILED("分类描述不可以为空，请填写分类描述");
		}

		// 补全数据
		shareCategory.setId(idWorker.nextId() + "");
		shareCategory.setState("1");
		shareCategory.setCreateTime(new Date());
		shareCategory.setUpdateTime(new Date());

		// 保存数据
		shareCategoryDao.save(shareCategory);

		return ResponseResult.SUCCESS("添加分类成功");
	}

	/**
	 * 获取分享内容分类列表
	 * 
	 * @param page 页码
	 * @param size 每页数量
	 * @return
	 */
	@Override
	public ResponseResult listShareCategory(int page, int size) {
		// 简单处理 page 和 size
		page = checkPage(page);
		size = checkSize(size);

		// 处理分页和排序条件
		Sort sort = Sort.by(Sort.Order.asc("createTime"));
		Pageable pageable = PageRequest.of(page - 1, size, sort);
		Page<ShareCategory> all = shareCategoryDao.findAll(pageable);

		return ResponseResult.SUCCESS("获取分类列表成功").setData(all);
	}

	/**
	 * 获取分享内容分类
	 * 
	 * @param categoryId
	 * @return
	 */
	@Override
	public ResponseResult getShareCategory(String categoryId) {
		// 直接从数据库查询
		ShareCategory category = shareCategoryDao.findOneById(categoryId);
		if (category == null) {
			return ResponseResult.FAILED("分类不存在");
		}
		return ResponseResult.SUCCESS("获取分类成功").setData(category);
	}

	/**
	 * 修改分享内容分类
	 * 
	 * @param categoryId
	 * @param shareCategory
	 * @return
	 */
	@Override
	public ResponseResult updateShareCategory(String categoryId, ShareCategory shareCategory) {
		// 先从数据库找出来
		ShareCategory categoryFromDb = shareCategoryDao.findOneById(categoryId);
		if (categoryFromDb == null) {
			return ResponseResult.FAILED("分类不存在");
		}

		// 判断要修改的数据内容
		String name = shareCategory.getName();
		if (!TextUtils.isEmpty(name)) {
			categoryFromDb.setName(name);
		}
		String pinyin = shareCategory.getPinyin();
		if (!TextUtils.isEmpty(pinyin)) {
			categoryFromDb.setPinyin(pinyin);
		}
		String description = shareCategory.getDescription();
		if (!TextUtils.isEmpty(description)) {
			categoryFromDb.setDescription(description);
		}
		categoryFromDb.setState(shareCategory.getState());
		// 设置更新时间
		categoryFromDb.setUpdateTime(new Date());

		// 保存数据
		shareCategoryDao.save(categoryFromDb);

		// 返回结果
		return ResponseResult.SUCCESS("修改分类成功");
	}

	/**
	 * 删除分享内容分类
	 * <p>
	 * 假删除，就是改变状态，让前台看不到
	 * 
	 * @param categoryId
	 * @return
	 */
	@Override
	public ResponseResult deleteShareCategoryByState(String categoryId) {
		// 通过修改显示状态假删除分类
		int result = shareCategoryDao.deleteCategoryByUpdateState(categoryId);
		if (result == 0) {
			return ResponseResult.FAILED("分类不存在");
		}

		return ResponseResult.SUCCESS("删除分类成功");
	}

	/**
	 * 获取分享内容标签列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@Override
	public ResponseResult listShareLabel(int page, int size) {

		// 简单处理 page 和 size
		page = checkPage(page);
		size = checkSize(size);

		// 处理分页和排序条件
		Sort sort = Sort.by(Sort.Order.desc("count"));
		Pageable pageable = PageRequest.of(page - 1, size, sort);
		Page<ShareLabel> all = shareLabelDao.findAll(pageable);

		return ResponseResult.SUCCESS("获取分享内容标签列表成功").setData(all);
	}

	/**
	 * 获取分享内容数量
	 * 
	 * @return
	 */
	@Override
	public ResponseResult getShareCount() {
		long count = shareNoContentDao.count();

		return ResponseResult.SUCCESS("获取分享内容数量成功").setData(count);
	}

	/**
	 * 获取分享内容置顶数量
	 * 
	 * @return
	 */
	@Override
	public ResponseResult getShareTopCount() {
		int shareTopCount = shareNoContentDao.getShareTopCount();
		if (shareTopCount == 0) {
			ResponseResult.FAILED("没有置顶的分享内容");
		}

		return ResponseResult.SUCCESS("获取分享内容置顶数量成功").setData(shareTopCount);
	}

}
