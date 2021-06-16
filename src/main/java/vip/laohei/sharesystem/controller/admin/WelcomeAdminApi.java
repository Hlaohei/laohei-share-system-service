package vip.laohei.sharesystem.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.ISettingsService;
import vip.laohei.sharesystem.service.IShareService;
import vip.laohei.sharesystem.service.IUserService;
import vip.laohei.sharesystem.service.IWorksService;

/**
 * 欢迎信息 - 后台管理接口
 * 
 * @author laohei
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/laohei-admin/welcome")
@Api(value = "/laohei-admin/welcome", tags = { "欢迎信息" }, description = "后台管理接口")
public class WelcomeAdminApi {

	@Autowired
	private IUserService userService;

	@Autowired
	private ISettingsService settingsService;

	@Autowired
	private IShareService shareService;

	@Autowired
	private IWorksService worksService;

	/**
	 * 获取用户信息
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/user_info/{userId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "user_info", notes = "获取用户信息")
	public ResponseResult getUserInfo(@PathVariable("userId") String userId) {
		return userService.getUserInfo(userId);
	}

	/**
	 * 获取网站访问量
	 * 
	 * @return
	 */
	@GetMapping("/website_view_count")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "website_view_count", notes = "获取网站访问量")
	public ResponseResult getWebsiteViewCount() {
		return settingsService.getWebsiteViewCount();
	}

	/**
	 * 获取分享内容数量
	 * 
	 * @return
	 */
	@GetMapping("/share_count")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "share_count", notes = "获取分享内容数量")
	public ResponseResult getShareCount() {
		return shareService.getShareCount();
	}

	/**
	 * 获取分享内容置顶数量
	 * 
	 * @return
	 */
	@GetMapping("/share_top_count")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "share_top_count", notes = "获取分享内容置顶数量")
	public ResponseResult getShareTopCount() {
		return shareService.getShareTopCount();
	}

	/**
	 * 获取作品数量
	 * 
	 * @return
	 */
	@GetMapping("/works_count")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "works_count", notes = "获取作品数量")
	public ResponseResult getWorksCount() {
		return worksService.getWorksCount();
	}

}
