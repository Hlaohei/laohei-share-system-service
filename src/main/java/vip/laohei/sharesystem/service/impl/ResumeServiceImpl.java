package vip.laohei.sharesystem.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vip.laohei.sharesystem.dao.ResumeDao;
import vip.laohei.sharesystem.entity.Resume;
import vip.laohei.sharesystem.entity.User;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IResumeService;
import vip.laohei.sharesystem.service.IUserService;
import vip.laohei.sharesystem.utils.Constants;
import vip.laohei.sharesystem.utils.IdWorker;
import vip.laohei.sharesystem.utils.TextUtils;

/**
 * 简历服务 实现类
 * 
 * @author laohei
 *
 */
@Service
@Transactional
public class ResumeServiceImpl extends BaseService implements IResumeService {

	@Autowired
	private IUserService userService;

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private ResumeDao resumeDao;

	/**
	 * 添加简历信息
	 * <p>
	 * 数据为单个键值对
	 * 
	 * @param resume
	 * @return
	 */
	@Override
	public ResponseResult addResume(Resume resume) {
		User user = userService.checkUser();
		if (user == null) {
			return ResponseResult.FAILED("账号未登录，请先登录管理员账号");
		}

		// 检查数据完整性
		// 关键字、信息、顺序、状态（默认显示）
		// 检查关键字
		String key = resume.getKey();
		if (TextUtils.isEmpty(key)) {
			return ResponseResult.FAILED("简历关键字不可以为空，请填写简历关键字");
		}
		if (key.length() > Constants.Share.TITLE_MAX_LENGTH) {
			return ResponseResult.FAILED("简历关键字不可以超过" + Constants.Share.TITLE_MAX_LENGTH + "个字符");
		}
		// 检查关键字对应信息
		String value = resume.getValue();
		if (TextUtils.isEmpty(value)) {
			return ResponseResult.FAILED("简历信息不可以为空，请填写简历信息");
		}

		// 补充其余数据
		resume.setId(idWorker.nextId() + "");
		resume.setCreateTime(new Date());
		resume.setUpdateTime(new Date());

		// 保存到数据库
		resumeDao.save(resume);

		// 返回结果
		return ResponseResult.SUCCESS("添加简历信息成功");
	}

	/**
	 * 获取简历信息列表
	 * 
	 * @return
	 */
	@Override
	public ResponseResult listResume() {
		// 创建排序条件
		Sort sort = Sort.by(Sort.Order.asc("order"));
		List<Resume> all = resumeDao.findAll(sort);

		return ResponseResult.SUCCESS("获取简历信息列表成功").setData(all);
	}

	/**
	 * 获取简历信息
	 * 
	 * @param resumeId
	 * @return
	 */
	@Override
	public ResponseResult getResume(String resumeId) {
		Resume resume = resumeDao.findOneById(resumeId);
		if (resume == null) {
			return ResponseResult.FAILED("简历信息不存在");
		}

		return ResponseResult.SUCCESS("获取简历信息成功").setData(resume);
	}

	/**
	 * 修改简历信息
	 * <p>
	 * 数据为单个键值对
	 * 
	 * @param resumeId
	 * @param resume
	 * @return
	 */
	@Override
	public ResponseResult updateResume(String resumeId, Resume resume) {
		// 先从数据库中找出对应简历信息
		Resume resumeFromDb = resumeDao.findOneById(resumeId);
		if (resumeFromDb == null) {
			return ResponseResult.FAILED("简历信息不存在");
		}

		// 内容修改
		String key = resume.getKey();
		if (!TextUtils.isEmpty(key)) {
			resumeFromDb.setKey(key);
		}
		String value = resume.getValue();
		if (!TextUtils.isEmpty(value)) {
			resumeFromDb.setValue(value);
		}
		String order = resume.getOrder();
		if (!TextUtils.isEmpty(order)) {
			resumeFromDb.setOrder(order);
		}
		// 设置修改时间
		resumeFromDb.setUpdateTime(new Date());

		// 保存数据
		resumeDao.save(resumeFromDb);

		// 返回信息
		return ResponseResult.SUCCESS("修改简历信息成功");
	}

	/**
	 * 修改简历数据显示状态
	 * 
	 * @param resumeId
	 * @return
	 */
	@Override
	public ResponseResult updateResumeState(String resumeId) {
		// 直接数据库修改 state 状态
		// 先从数据库中找出对应简历信息
		Resume resume = resumeDao.findOneById(resumeId);
		if (resume == null) {
			return ResponseResult.FAILED("简历信息不存在");
		}

		String state = resume.getState();
		if (Constants.Resume.STATE_NOT_SHOW.equals(state)) {
			resume.setState(Constants.Resume.STATE_SHOW);
			resumeDao.save(resume);
			return ResponseResult.SUCCESS("显示简历信息成功");
		}
		if (Constants.Resume.STATE_SHOW.equals(state)) {
			resume.setState(Constants.Resume.STATE_NOT_SHOW);
			resumeDao.save(resume);
			return ResponseResult.SUCCESS("隐藏简历信息成功");
		}

		return ResponseResult.FAILED("不支持该操作");
	}

	/**
	 * 删除简历信息
	 * 
	 * @param resumeId
	 * @return
	 */
	@Override
	public ResponseResult deleteResume(String resumeId) {
		// 直接删除数据库内容
		int result = resumeDao.deleteAllById(resumeId);
		if (result > 0) {
			// 返回信息
			return ResponseResult.SUCCESS("删除简历信息成功");
		}

		return ResponseResult.FAILED("删除简历信息失败，简历信息不存在");
	}

}
