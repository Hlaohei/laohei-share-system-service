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
import vip.laohei.sharesystem.entity.WebsiteBackground;
import vip.laohei.sharesystem.entity.WebsiteInfo;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.ISettingsService;

/**
 * 设置接口 - 后台管理接口
 * 
 * @author laohei
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/laohei-admin/settings")
@Api(value = "/laohei-admin/settings", tags = { "设置接口" }, description = "后台管理接口")
public class SettingsAdminApi {

	@Autowired
	private ISettingsService settingsService;

	/**
	 * 获取网站信息
	 * 
	 * @return
	 */
	@GetMapping("/website_info")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "website_info", notes = "获取网站信息")
	public ResponseResult getWebsiteInfo() {
		return settingsService.getWebsiteInfo();
	}

	/**
	 * 修改网站信息
	 * 
	 * @param websiteInfo
	 * @return
	 */
	@PutMapping("/website_info")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "website_info", notes = "修改网站信息")
	public ResponseResult updateWebsiteInfo(@RequestBody WebsiteInfo websiteInfo) {
		return settingsService.updateWebsiteInfo(websiteInfo);
	}

	/**
	 * 添加动态背景
	 * 
	 * @param websiteBackground
	 * @return
	 */
	@PostMapping("/website_background")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "website_background", notes = "添加动态背景")
	public ResponseResult addWebsiteBackground(@RequestBody WebsiteBackground websiteBackground) {
		return settingsService.addWebsiteBackground(websiteBackground);
	}

	/**
	 * 获取动态背景列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/website_background/list/{page}/{size}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "website_background/list", notes = "获取动态背景列表")
	public ResponseResult getWebsiteBackgroundList( //
			@PathVariable("page") int page, //
			@PathVariable("size") int size) {
		return settingsService.listWebsiteBackground(page, size);
	}

	/**
	 * 获取动态背景
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	@GetMapping("/website_background/{websiteBackgroundId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "website_background", notes = "获取动态背景")
	public ResponseResult getWebsiteBackground(@PathVariable("websiteBackgroundId") String websiteBackgroundId) {
		return settingsService.getWebsiteBackground(websiteBackgroundId);
	}

	/**
	 * 修改动态背景
	 * 
	 * @param websiteBackgroundId
	 * @param websiteBackground
	 * @return
	 */
	@PutMapping("/website_background/{websiteBackgroundId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "website_background", notes = "修改动态背景")
	public ResponseResult updateWebsiteBackground( //
			@PathVariable("websiteBackgroundId") String websiteBackgroundId, //
			@RequestBody WebsiteBackground websiteBackground) {
		return settingsService.updateWebsiteBackground(websiteBackgroundId, websiteBackground);
	}

	/**
	 * 修改动态背景固定状态
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	@PutMapping("/website_background/fixed/{websiteBackgroundId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "website_background/fixed", notes = "修改动态背景固定状态")
	public ResponseResult updateWebsiteBackgroundFixed(@PathVariable("websiteBackgroundId") String websiteBackgroundId) {
		return settingsService.updateWebsiteBackgroundFixed(websiteBackgroundId);
	}

	/**
	 * 修改动态背景显示状态
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	@PutMapping("/website_background/state/{websiteBackgroundId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "website_background/state", notes = "修改动态背景显示状态")
	public ResponseResult updateWebsiteBackgroundState(@PathVariable("websiteBackgroundId") String websiteBackgroundId) {
		return settingsService.updateWebsiteBackgroundState(websiteBackgroundId);
	}

	/**
	 * 删除动态背景
	 * 
	 * @param websiteBackgroundId
	 * @return
	 */
	@DeleteMapping("/website_background/{websiteBackgroundId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "website_background", notes = "删除动态背景")
	public ResponseResult deleteWebsiteBackground(@PathVariable("websiteBackgroundId") String websiteBackgroundId) {
		return settingsService.deleteWebsiteBackground(websiteBackgroundId);
	}

}
