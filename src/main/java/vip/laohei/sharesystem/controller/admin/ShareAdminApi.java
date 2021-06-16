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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import vip.laohei.sharesystem.entity.Share;
import vip.laohei.sharesystem.entity.ShareCategory;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IShareService;

/**
 * 分享接口 - 后台管理接口
 * 
 * @author laohei
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/laohei-admin/share")
@Api(value = "/laohei-admin/share", tags = { "分享接口" }, description = "后台管理接口")
public class ShareAdminApi {

	@Autowired
	private IShareService shareService;

	/**
	 * 发布分享内容
	 * 
	 * @param share
	 * @return
	 */
	@PostMapping
	@ApiOperation(value = "share", notes = "发布分享内容")
	public ResponseResult postShare(@RequestBody Share share) {
		return shareService.postShare(share);
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
	@GetMapping("/list/{page}/{size}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "list", notes = "获取分享内容列表")
	public ResponseResult getShareList( //
			@PathVariable("page") int page, //
			@PathVariable("size") int size, //
			@RequestParam(value = "keyword", required = false) String keyword, //
			@RequestParam(value = "shareCategoryId", required = false) String shareCategoryId, //
			@RequestParam(value = "state", required = false) String state, //
			@RequestParam(value = "top", required = false) String top) {
		return shareService.listShare(page, size, keyword, shareCategoryId, state, top);
	}

	/**
	 * 获取分享内容
	 * 
	 * @param shareId
	 * @return
	 */
	@GetMapping("/{shareId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "share", notes = "获取分享内容")
	public ResponseResult getShare(@PathVariable("shareId") String shareId) {
		return shareService.getShareById(shareId);
	}

	/**
	 * 置顶分享内容
	 * 
	 * @param shareId
	 * @return
	 */
	@PutMapping("/top/{shareId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "top", notes = "置顶分享内容")
	public ResponseResult topShare(@PathVariable("shareId") String shareId) {
		return shareService.topShare(shareId);
	}

	/**
	 * 修改分享内容
	 * 
	 * @param shareId
	 * @param share
	 * @return
	 */
	@PutMapping("/{shareId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "share", notes = "修改分享内容")
	public ResponseResult updateShare( //
			@PathVariable("shareId") String shareId, //
			@RequestBody Share share) {
		return shareService.updateShare(shareId, share);
	}

	/**
	 * 修改分享内容显示状态
	 * 
	 * @param shareId
	 * @return
	 */
	@PutMapping("/state/{shareId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "state", notes = "修改分享内容显示状态")
	public ResponseResult updateShareState(@PathVariable("shareId") String shareId) {
		return shareService.updateShareState(shareId);
	}

	/**
	 * 删除分享内容
	 * 
	 * @param shareId
	 * @return
	 */
	@DeleteMapping("/{shareId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "share", notes = "删除分享内容")
	public ResponseResult deleteShare(@PathVariable("shareId") String shareId) {
		return shareService.deleteShare(shareId);
	}

	/**
	 * 添加分享内容分类
	 * 
	 * @param shareCategory
	 * @return
	 */
	@PostMapping("/category")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "category", notes = "添加分享内容分类")
	public ResponseResult addShareCategory(@RequestBody ShareCategory shareCategory) {
		return shareService.addShareCategory(shareCategory);
	}

	/**
	 * 获取分享内容分类列表
	 * 
	 * @param page 页码
	 * @param size 每页数量
	 * @return
	 */
	@GetMapping("/category/list/{page}/{size}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "category/list", notes = "获取分享内容分类列表")
	public ResponseResult getShareCategoryList( //
			@PathVariable("page") int page, //
			@PathVariable("size") int size) {
		return shareService.listShareCategory(page, size);
	}

	/**
	 * 获取分享内容分类
	 * 
	 * @param categoryId
	 * @return
	 */
	@GetMapping("/category/{categoryId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "category", notes = "获取分享内容分类")
	public ResponseResult getShareCategory(@PathVariable("categoryId") String categoryId) {
		return shareService.getShareCategory(categoryId);
	}

	/**
	 * 修改分享内容分类
	 * 
	 * @param categoryId
	 * @param shareCategory
	 * @return
	 */
	@PutMapping("/category/{categoryId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "category", notes = "修改分享内容分类")
	public ResponseResult updateShareCategory( //
			@PathVariable("categoryId") String categoryId, //
			@RequestBody ShareCategory shareCategory) {
		return shareService.updateShareCategory(categoryId, shareCategory);
	}

	/**
	 * 删除分享内容分类
	 * <p>
	 * 假删除，就是改变状态，让前台看不到
	 * 
	 * @param categoryId
	 * @return
	 */
	@DeleteMapping("/category/{categoryId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "category", notes = "删除分享内容分类")
	public ResponseResult deleteShareCategory(@PathVariable("categoryId") String categoryId) {
		return shareService.deleteShareCategoryByState(categoryId);
	}

	/**
	 * 获取分享内容标签列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/label/list/{page}/{size}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "label/list", notes = "获取分享内容标签列表")
	public ResponseResult getShareLabelList( //
			@PathVariable("page") int page, //
			@PathVariable("size") int size) {
		return shareService.listShareLabel(page, size);
	}

}
