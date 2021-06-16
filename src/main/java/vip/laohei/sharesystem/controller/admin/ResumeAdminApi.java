package vip.laohei.sharesystem.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import vip.laohei.sharesystem.entity.Resume;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IResumeService;

/**
 * 简历接口 - 后台管理接口
 * 
 * @author laohei
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/laohei-admin/resume")
@Api(value = "/laohei-admin/resume", tags = { "简历接口" }, description = "后台管理接口")
public class ResumeAdminApi {

	@Autowired
	private IResumeService resumeService;

	/**
	 * 添加简历信息
	 * <p>
	 * 数据为单个键值对
	 * 
	 * @param resume
	 * @return
	 */
	@PostMapping
	@ApiOperation(value = "resume", notes = "添加简历信息")
	public ResponseResult addResume(@RequestBody Resume resume) {
		return resumeService.addResume(resume);
	}

	/**
	 * 获取简历信息列表
	 * 
	 * @return
	 */
	@GetMapping("/list")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "resume/list", notes = "获取简历信息列表")
	public ResponseResult getResumeList() {
		return resumeService.listResume();
	}

	/**
	 * 获取简历信息
	 * 
	 * @param resumeId
	 * @return
	 */
	@GetMapping("/{resumeId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "resume", notes = "获取简历信息")
	public ResponseResult getResume(@PathVariable("resumeId") String resumeId) {
		return resumeService.getResume(resumeId);
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
	@PutMapping("/{resumeId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "resume", notes = "修改简历信息")
	public ResponseResult updateResume( //
			@PathVariable("resumeId") String resumeId, //
			@RequestBody Resume resume) {
		return resumeService.updateResume(resumeId, resume);
	}

	/**
	 * 修改简历数据显示状态
	 * 
	 * @param resumeId
	 * @return
	 */
	@PutMapping("/state/{resumeId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "resume/state", notes = "修改简历信息显示状态")
	public ResponseResult updateResumeState(@PathVariable("resumeId") String resumeId) {
		return resumeService.updateResumeState(resumeId);
	}

	/**
	 * 删除简历信息
	 * 
	 * @param resumeId
	 * @return
	 */
	@DeleteMapping("/{resumeId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "resume", notes = "删除简历信息")
	public ResponseResult deleteResume(@PathVariable("resumeId") String resumeId) {
		return resumeService.deleteResume(resumeId);
	}

}
