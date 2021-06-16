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
import vip.laohei.sharesystem.service.IWorksService;
import vip.laohei.sharesystem.utils.Constants;

/**
 * 作品接口 - 前台内容接口
 * 
 * @author laohei
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/portal/works")
@Api(value = "/portal/works", tags = { "作品接口" }, description = "前台内容接口")
public class WorksPortalApi {

	@Autowired
	private IWorksService worksService;

	/**
	 * 获取作品列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/list/{page}/{size}")
	@ApiOperation(value = "list", notes = "获取作品列表")
	public ResponseResult getWorksList( //
			@PathVariable("page") int page, //
			@PathVariable("size") int size) {
		return worksService.listWorks(page, size, null, null, Constants.Works.STATE_SHOW);
	}

	/**
	 * 获取作品分类列表
	 * 
	 * @return
	 */
	@GetMapping("/list/works_category")
	@ApiOperation(value = "list/works_category", notes = "获取作品分类列表")
	public ResponseResult getWorksCategoryList() {
		return worksService.listWorksCategory(1, 50);
	}

	/**
	 * 通过分类获取作品列表
	 * 
	 * @param categoryId
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/list/{categoryId}/{page}/{size}")
	@ApiOperation(value = "list", notes = "通过分类获取作品列表")
	public ResponseResult listWorksByCategoryId( //
			@PathVariable("categoryId") String categoryId, //
			@PathVariable("page") int page, //
			@PathVariable("size") int size) {
		return worksService.listWorks(page, size, null, categoryId, Constants.Works.STATE_SHOW);
	}

}
