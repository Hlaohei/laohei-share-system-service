package vip.laohei.sharesystem.service;

import vip.laohei.sharesystem.entity.Resume;
import vip.laohei.sharesystem.response.ResponseResult;

public interface IResumeService {

	/**
	 * 添加简历信息
	 * <p>
	 * 数据为单个键值对
	 * 
	 * @param resume
	 * @return
	 */
	ResponseResult addResume(Resume resume);

	/**
	 * 获取简历信息列表
	 * 
	 * @return
	 */
	ResponseResult listResume();

	/**
	 * 获取简历信息
	 * 
	 * @param resumeId
	 * @return
	 */
	ResponseResult getResume(String resumeId);

	/**
	 * 修改简历信息
	 * <p>
	 * 数据为单个键值对
	 * 
	 * @param resumeId
	 * @param resume
	 * @return
	 */
	ResponseResult updateResume(String resumeId, Resume resume);

	/**
	 * 修改简历数据显示状态
	 * 
	 * @param resumeId
	 * @return
	 */
	ResponseResult updateResumeState(String resumeId);

	/**
	 * 删除简历信息
	 * 
	 * @param resumeId
	 * @return
	 */
	ResponseResult deleteResume(String resumeId);

}
