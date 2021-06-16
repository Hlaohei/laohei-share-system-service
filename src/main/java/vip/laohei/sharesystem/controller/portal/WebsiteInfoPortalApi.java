package vip.laohei.sharesystem.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.ISettingsService;

/**
 * 网站信息 - 前台内容接口
 * 
 * @author laohei
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/portal/website_info")
@Api(value = "/portal/website_info", tags = { "网站信息" }, description = "前台内容接口")
public class WebsiteInfoPortalApi {

	@Autowired
	private ISettingsService settingsService;

	/**
	 * 获取网站信息
	 * 
	 * @return
	 */
	@GetMapping
	@ApiOperation(value = "website_info", notes = "获取网站信息")
	public ResponseResult getWebsiteInfo() {
		return settingsService.getWebsiteInfo();
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
	@PutMapping("/view_count")
	@ApiOperation(value = "website_info/view_count", notes = "统计页面访问量")
	public void updateViewCount() {
		settingsService.updateViewCount();
	}

	/**
	 * 获取网站动态背景 (根据随机或固定状态获取)
	 * 
	 * @return
	 */
	@GetMapping("/website_background")
	@ApiOperation(value = "website_background", notes = "获取网站动态背景")
	public ResponseResult getWebsiteBackground() {
		return settingsService.getWebsiteBackgroundByRandom();
	}

}
