package vip.laohei.sharesystem.service.impl;

import java.util.ArrayList;
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

import vip.laohei.sharesystem.dao.WorksCategoryDao;
import vip.laohei.sharesystem.dao.WorksDao;
import vip.laohei.sharesystem.entity.User;
import vip.laohei.sharesystem.entity.Works;
import vip.laohei.sharesystem.entity.WorksCategory;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IUserService;
import vip.laohei.sharesystem.service.IWorksService;
import vip.laohei.sharesystem.utils.Constants;
import vip.laohei.sharesystem.utils.IdWorker;
import vip.laohei.sharesystem.utils.TextUtils;

/**
 * 作品服务 实现类
 * 
 * @author laohei
 *
 */
@Service
@Transactional
public class WorksServiceImpl extends BaseService implements IWorksService {

	@Autowired
	private IUserService userService;

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private WorksDao worksDao;

	@Autowired
	private WorksCategoryDao worksCategoryDao;

	/**
	 * 发布作品
	 * 
	 * @param works
	 * @return
	 */
	@Override
	public ResponseResult postWorks(Works works) {
		User user = userService.checkUser();
		if (user == null) {
			return ResponseResult.FAILED("账号未登录，请先登录管理员账号");
		}

		// 检查数据完整性
		// 名称、分类ID、概述、url链接、封面、状态（默认显示）
		// 检查名称
		String name = works.getName();
		if (TextUtils.isEmpty(name)) {
			return ResponseResult.FAILED("名称不可以为空，请填写名称");
		}
		if (name.length() > Constants.Share.TITLE_MAX_LENGTH) {
			return ResponseResult.FAILED("名称不可以超过" + Constants.Share.TITLE_MAX_LENGTH + "个字符");
		}
		// 检查分类ID
		String worksCategoryId = works.getWorksCategoryId();
		if (TextUtils.isEmpty(worksCategoryId)) {
			return ResponseResult.FAILED("分类不能为空，请选择一个分类");
		}
		// 检查概述
		String summary = works.getSummary();
		if (TextUtils.isEmpty(summary)) {
			return ResponseResult.FAILED("概述不能为空，请填写作品概述");
		}
		// 检查url链接
		String url = works.getUrl();
		if (TextUtils.isEmpty(url)) {
			return ResponseResult.FAILED("链接不能为空，请填写作品链接");
		}
		// 封面可以为空，如果没有封面会显示默认背景

		// 补充其余数据
		works.setId(idWorker.nextId() + "");
		works.setCreateTime(new Date());
		works.setUpdateTime(new Date());

		// 保存到数据库
		worksDao.save(works);

		// 返回结果
		return ResponseResult.SUCCESS("发布作品成功");
	}

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
	@Override
	public ResponseResult listWorks(int page, int size, String keyword, String worksCategoryId, String state) {
		// 简单处理 page 和 size
		page = checkPage(page);
		size = checkSize(size);

		// 处理分页和排序条件
		Sort sort = Sort.by(Sort.Order.desc("createTime"));
		Pageable pageable = PageRequest.of(page - 1, size, sort);

		// 开始查询
		Page<Works> all = worksDao.findAll(new Specification<Works>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Works> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (!TextUtils.isEmpty(keyword)) {
					Predicate namePre = cb.like(root.get("name").as(String.class), "%" + keyword + "%");
					predicates.add(namePre);
				}
				if (!TextUtils.isEmpty(worksCategoryId)) {
					Predicate categoryIdPre = cb.equal(root.get("worksCategoryId").as(String.class), worksCategoryId);
					predicates.add(categoryIdPre);
				}
				if (!TextUtils.isEmpty(state)) {
					Predicate statePre = cb.equal(root.get("state").as(String.class), state);
					predicates.add(statePre);
				}
				Predicate[] preArray = new Predicate[predicates.size()];
				predicates.toArray(preArray);
				return cb.and(preArray);
			}
		}, pageable);

		return ResponseResult.SUCCESS("获取作品列表成功").setData(all);
	}

	/**
	 * 获取单个作品
	 * 
	 * @param worksId
	 * @return
	 */
	@Override
	public ResponseResult getWorksById(String worksId) {
		Works works = worksDao.findOneById(worksId);
		if (works == null) {
			return ResponseResult.FAILED("作品不存在");
		}

		return ResponseResult.SUCCESS("获取作品成功").setData(works);
	}

	/**
	 * 修改作品内容
	 * 
	 * @param worksId
	 * @param works
	 * @return
	 */
	@Override
	public ResponseResult updateWorks(String worksId, Works works) {
		// 先从数据库中找出对应作品
		Works worksFromDb = worksDao.findOneById(worksId);
		if (worksFromDb == null) {
			return ResponseResult.FAILED("作品不存在");
		}

		// 内容修改
		// 名称可修改
		String name = works.getName();
		if (!TextUtils.isEmpty(name)) {
			worksFromDb.setName(name);
		}
		// 分类可修改
		String worksCategoryId = works.getWorksCategoryId();
		if (!TextUtils.isEmpty(worksCategoryId)) {
			worksFromDb.setWorksCategoryId(worksCategoryId);
		}
		// 概述可修改
		String summary = works.getSummary();
		if (!TextUtils.isEmpty(summary)) {
			worksFromDb.setSummary(summary);
		}
		// 链接可修改
		String url = works.getUrl();
		if (!TextUtils.isEmpty(url)) {
			worksFromDb.setUrl(url);
		}
		// 封面可修改
		String cover = works.getCover();
		if (!TextUtils.isEmpty(cover)) {
			worksFromDb.setCover(cover);
		}
		// 设置修改时间
		worksFromDb.setUpdateTime(new Date());

		// 保存数据
		worksDao.save(worksFromDb);

		// 返回信息
		return ResponseResult.SUCCESS("修改作品成功");
	}

	/**
	 * 修改作品显示状态
	 * 
	 * @param worksId
	 * @return
	 */
	@Override
	public ResponseResult updateWorksState(String worksId) {
		// 直接数据库修改 state 状态
		// 检查作品是否存在
		Works works = worksDao.findOneById(worksId);
		if (works == null) {
			return ResponseResult.FAILED("作品不存在");
		}

		String state = works.getState();
		if (Constants.Works.STATE_NOT_SHOW.equals(state)) {
			works.setState(Constants.Works.STATE_SHOW);
			worksDao.save(works);
			return ResponseResult.SUCCESS("显示作品成功");
		}
		if (Constants.Works.STATE_SHOW.equals(state)) {
			works.setState(Constants.Works.STATE_NOT_SHOW);
			worksDao.save(works);
			return ResponseResult.SUCCESS("隐藏作品成功");
		}

		return ResponseResult.FAILED("不支持该操作");
	}

	/**
	 * 删除作品
	 * 
	 * @param worksId
	 * @return
	 */
	@Override
	public ResponseResult deleteWorks(String worksId) {
		// 直接删除数据库内容
		int result = worksDao.deleteAllById(worksId);
		if (result > 0) {
			// 返回信息
			return ResponseResult.SUCCESS("删除作品成功");
		}

		return ResponseResult.FAILED("删除作品失败，作品不存在");
	}

	/**
	 * 添加作品分类
	 * 
	 * @param worksCategory
	 * @return
	 */
	@Override
	public ResponseResult addWorksCategory(WorksCategory worksCategory) {
		// 检查数据完整性
		// 必须要填写的数据：分类名称、分类拼音、分类描述
		if (TextUtils.isEmpty(worksCategory.getName())) {
			return ResponseResult.FAILED("分类名称不可以为空，请填写分类名称");
		}
		if (TextUtils.isEmpty(worksCategory.getPinyin())) {
			return ResponseResult.FAILED("分类拼音不可以为空，请填写分类拼音");
		}
		if (TextUtils.isEmpty(worksCategory.getDescription())) {
			return ResponseResult.FAILED("分类描述不可以为空，请填写分类描述");
		}

		// 补全数据
		worksCategory.setId(idWorker.nextId() + "");
		worksCategory.setState("1");
		worksCategory.setCreateTime(new Date());
		worksCategory.setUpdateTime(new Date());

		// 保存数据
		worksCategoryDao.save(worksCategory);

		return ResponseResult.SUCCESS("添加分类成功");
	}

	/**
	 * 获取作品分类列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@Override
	public ResponseResult listWorksCategory(int page, int size) {
		// 简单处理 page 和 size
		page = checkPage(page);
		size = checkSize(size);

		// 处理分页和排序条件
		Sort sort = Sort.by(Sort.Order.asc("createTime"));
		Pageable pageable = PageRequest.of(page - 1, size, sort);
		Page<WorksCategory> all = worksCategoryDao.findAll(pageable);

		return ResponseResult.SUCCESS("获取分类列表成功").setData(all);
	}

	/**
	 * 获取作品分类
	 * 
	 * @param categoryId
	 * @return
	 */
	@Override
	public ResponseResult getWorksCategory(String categoryId) {
		// 直接从数据库查询
		WorksCategory category = worksCategoryDao.findOneById(categoryId);
		if (category == null) {
			return ResponseResult.FAILED("分类不存在");
		}
		return ResponseResult.SUCCESS("获取分类成功").setData(category);
	}

	/**
	 * 修改作品分类
	 * 
	 * @param categoryId
	 * @param worksCategory
	 * @return
	 */
	@Override
	public ResponseResult updateWorksCategory(String categoryId, WorksCategory worksCategory) {
		// 先从数据库找出来
		WorksCategory categoryFromDb = worksCategoryDao.findOneById(categoryId);
		if (categoryFromDb == null) {
			return ResponseResult.FAILED("分类不存在");
		}

		// 判断要修改的数据内容
		String name = worksCategory.getName();
		if (!TextUtils.isEmpty(name)) {
			categoryFromDb.setName(name);
		}
		String pinyin = worksCategory.getPinyin();
		if (!TextUtils.isEmpty(pinyin)) {
			categoryFromDb.setPinyin(pinyin);
		}
		String description = worksCategory.getDescription();
		if (!TextUtils.isEmpty(description)) {
			categoryFromDb.setDescription(description);
		}
		categoryFromDb.setState(worksCategory.getState());
		// 设置更新时间
		categoryFromDb.setUpdateTime(new Date());

		// 保存数据
		worksCategoryDao.save(categoryFromDb);

		// 返回结果
		return ResponseResult.SUCCESS("修改分类成功");
	}

	/**
	 * 删除作品分类
	 * <p>
	 * 假删除，就是改变状态，让前台看不到
	 * 
	 * @param categoryId
	 * @return
	 */
	@Override
	public ResponseResult deleteWorksCategoryByState(String categoryId) {
		// 通过修改显示状态假删除分类
		int result = worksCategoryDao.deleteCategoryByUpdateState(categoryId);
		if (result == 0) {
			return ResponseResult.FAILED("分类不存在");
		}

		return ResponseResult.SUCCESS("删除分类成功");
	}

	/**
	 * 获取作品数量
	 * 
	 * @return
	 */
	@Override
	public ResponseResult getWorksCount() {
		long count = worksDao.count();

		return ResponseResult.SUCCESS("获取作品数量成功").setData(count);
	}

}
