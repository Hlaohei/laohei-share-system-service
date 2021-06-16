package vip.laohei.sharesystem.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IShareService;
import vip.laohei.sharesystem.utils.Constants;

/**
 * 分享接口 - 前台内容接口
 * 
 * @author laohei
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/portal/share")
@Api(value = "/portal/share", tags = { "分享接口" }, description = "前台内容接口")
public class SharePortalApi {

	@Autowired
	private IShareService shareService;

	/**
	 * 获取置顶分享内容
	 * 
	 * @return
	 */
	@GetMapping("/top")
	@ApiOperation(value = "top", notes = "获取置顶分享内容")
	public ResponseResult getTopShare() {
		return shareService.listShare(1, 5, null, null, Constants.Share.STATE_SHOW, Constants.Share.IS_TOP);
	}

	/**
	 * 获取推荐分享内容
	 * 
	 * @return
	 */
	@GetMapping("/recommend/{page}/{size}")
	@ApiOperation(value = "recommend", notes = "获取推荐分享内容")
	public ResponseResult getRecommendShare( //
			@PathVariable("page") int page, //
			@PathVariable("size") int size) {
		return shareService.listShare(page, size, null, null, Constants.Share.STATE_SHOW, Constants.Share.NOT_TOP);
	}

	/**
	 * 获取分享内容分类列表
	 * 
	 * @return
	 */
	@GetMapping("/list/share_category")
	@ApiOperation(value = "list/share_category", notes = "获取分享内容分类列表")
	public ResponseResult getShareCategoryList() {
		return shareService.listShareCategory(1, 50);
	}

	/**
	 * 通过分类获取分享内容
	 * 
	 * @param categoryId
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/list/{categoryId}/{page}/{size}")
	@ApiOperation(value = "list", notes = "通过分类获取分享内容")
	public ResponseResult listShareByCategoryId( //
			@PathVariable("categoryId") String categoryId, //
			@PathVariable("page") int page, //
			@PathVariable("size") int size) {
		return shareService.listShare(page, size, null, categoryId, Constants.Share.STATE_SHOW, Constants.Share.NOT_TOP);
	}

	/**
	 * 获取分享内容
	 * 
	 * @param shareId
	 * @return
	 */
	@GetMapping("/{shareId}")
	@ApiOperation(value = "share", notes = "获取分享内容")
	public ResponseResult getShare(@PathVariable("shareId") String shareId) {
		return shareService.getShareById(shareId);
	}

	/**
	 * 获取标签云信息
	 * 
	 * @param size
	 * @return
	 */
	@GetMapping("/label/{size}")
	@ApiOperation(value = "label", notes = "获取标签云信息")
	public ResponseResult getLabels(@PathVariable("size") int size) {
		return shareService.listShareLabel(1, size);
	}

	/**
	 * 搜索分享内容
	 * 
	 * @param keyword
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/search/{keyword}/{page}/{size}")
	@ApiOperation(value = "search", notes = "搜索分享内容")
	public ResponseResult searchShare( //
			@PathVariable("keyword") String keyword, //
			@PathVariable("page") int page, //
			@PathVariable("size") int size) {
		return shareService.listShare(page, size, keyword, null, Constants.Share.STATE_SHOW, null);
	}

}
