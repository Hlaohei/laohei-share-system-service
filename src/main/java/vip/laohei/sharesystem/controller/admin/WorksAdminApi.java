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
import vip.laohei.sharesystem.entity.Works;
import vip.laohei.sharesystem.entity.WorksCategory;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IWorksService;

/**
 * 作品接口 - 后台管理接口
 * 
 * @author laohei
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/laohei-admin/works")
@Api(value = "/laohei-admin/works", tags = { "作品接口" }, description = "后台管理接口")
public class WorksAdminApi {

	@Autowired
	private IWorksService worksService;

	/**
	 * 发布作品
	 * 
	 * @param works
	 * @return
	 */
	@PostMapping
	@ApiOperation(value = "works", notes = "发布作品")
	public ResponseResult postWorks(@RequestBody Works works) {
		return worksService.postWorks(works);
	}

	/**
	 * 获取作品列表
	 * 
	 * @param page
	 * @param size
	 * @param keyword
	 * @param worksCategoryId
	 * @param state
	 * @return
	 */
	@GetMapping("/list/{page}/{size}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "list", notes = "获取作品列表")
	public ResponseResult getWorksList( //
			@PathVariable("page") int page, //
			@PathVariable("size") int size, //
			@RequestParam(value = "keyword", required = false) String keyword, //
			@RequestParam(value = "worksCategoryId", required = false) String worksCategoryId, //
			@RequestParam(value = "state", required = false) String state) {
		return worksService.listWorks(page, size, keyword, worksCategoryId, state);
	}

	/**
	 * 获取单个作品
	 * 
	 * @param worksId
	 * @return
	 */
	@GetMapping("/{worksId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "works", notes = "获取单个作品")
	public ResponseResult getWorks(@PathVariable("worksId") String worksId) {
		return worksService.getWorksById(worksId);
	}

	/**
	 * 修改作品内容
	 * 
	 * @param worksId
	 * @param works
	 * @return
	 */
	@PutMapping("/{worksId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "works", notes = "修改作品内容")
	public ResponseResult updateWorks( //
			@PathVariable("worksId") String worksId, //
			@RequestBody Works works) {
		return worksService.updateWorks(worksId, works);
	}

	/**
	 * 修改作品显示状态
	 * 
	 * @param worksId
	 * @return
	 */
	@PutMapping("/state/{worksId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "state", notes = "修改作品显示状态")
	public ResponseResult updateWorksState(@PathVariable("worksId") String worksId) {
		return worksService.updateWorksState(worksId);
	}

	/**
	 * 删除作品
	 * 
	 * @param worksId
	 * @return
	 */
	@DeleteMapping("/{worksId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "works", notes = "删除作品")
	public ResponseResult deleteWorks(@PathVariable("worksId") String worksId) {
		return worksService.deleteWorks(worksId);
	}

	/**
	 * 添加作品分类
	 * 
	 * @param worksCategory
	 * @return
	 */
	@PostMapping("/category")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "category", notes = "添加作品分类")
	public ResponseResult addWorksCategory(@RequestBody WorksCategory worksCategory) {
		return worksService.addWorksCategory(worksCategory);
	}

	/**
	 * 获取作品分类列表
	 * 
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/category/list/{page}/{size}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "category/list", notes = "获取作品分类列表")
	public ResponseResult getWorksCategoryList( //
			@PathVariable("page") int page, //
			@PathVariable("size") int size) {
		return worksService.listWorksCategory(page, size);
	}

	/**
	 * 获取作品分类
	 * 
	 * @param categoryId
	 * @return
	 */
	@GetMapping("/category/{categoryId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "category", notes = "获取作品分类")
	public ResponseResult getWorksCategory(@PathVariable("categoryId") String categoryId) {
		return worksService.getWorksCategory(categoryId);
	}

	/**
	 * 修改作品分类
	 * 
	 * @param categoryId
	 * @param worksCategory
	 * @return
	 */
	@PutMapping("/category/{categoryId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "category", notes = "修改作品分类")
	public ResponseResult updateWorksCategory( //
			@PathVariable("categoryId") String categoryId, //
			@RequestBody WorksCategory worksCategory) {
		return worksService.updateWorksCategory(categoryId, worksCategory);
	}

	/**
	 * 删除作品分类
	 * <p>
	 * 假删除，就是改变状态，让前台看不到
	 * 
	 * @param categoryId
	 * @return
	 */
	@DeleteMapping("/category/{categoryId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "category", notes = "删除作品分类")
	public ResponseResult deleteWorksCategory(@PathVariable("categoryId") String categoryId) {
		return worksService.deleteWorksCategoryByState(categoryId);
	}

}
