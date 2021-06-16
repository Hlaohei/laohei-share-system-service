package vip.laohei.sharesystem.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IResumeService;

/**
 * 简历接口 - 前台内容接口
 * 
 * @author laohei
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/portal/resume")
@Api(value = "/portal/resume", tags = { "简历接口" }, description = "前台内容接口")
public class ResumePortalApi {

	@Autowired
	private IResumeService resumeService;

	/**
	 * 获取简历信息列表
	 * 
	 * @return
	 */
	@GetMapping("/list")
	@ApiOperation(value = "resume/list", notes = "获取简历信息列表")
	public ResponseResult getResumeList() {
		return resumeService.listResume();
	}

}
