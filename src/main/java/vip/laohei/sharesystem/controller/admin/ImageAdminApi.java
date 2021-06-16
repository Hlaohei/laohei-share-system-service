package vip.laohei.sharesystem.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IImageService;

/**
 * 图片接口 - 后台管理接口
 * 
 * @author laohei
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/laohei-admin/image")
@Api(value = "/laohei-admin/image", tags = { "图片接口" }, description = "后台管理接口")
public class ImageAdminApi {

	@Autowired
	private IImageService imageService;

	/**
	 * 上传图片文件
	 * <p>
	 * 我这里直接上传到测试服务器
	 * 
	 * @param image
	 * @return
	 */
	@PostMapping
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "image", notes = "上传图片到服务器")
	public ResponseResult uploadImage(@RequestParam("image") MultipartFile image) {
		return imageService.uploadImage(image);
	}

	/**
	 * 获取图片列表
	 * 
	 * @param page
	 * @param size
	 * @param keyword
	 * @return
	 */
	@GetMapping("/list/{page}/{size}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "image/list", notes = "获取图片列表")
	public ResponseResult getImageList(//
			@PathVariable("page") int page, //
			@PathVariable("size") int size, //
			@RequestParam(value = "keyword", required = false) String keyword) {
		return imageService.listImage(page, size, keyword);
	}

	/**
	 * 
	 * 删除图片文件
	 * <p>
	 * 假删除，只是把图片的状态改成 0
	 * 
	 * @param imageId
	 * @return
	 */
	@DeleteMapping("/{imageId}")
	@PreAuthorize("@permission.login()")
	@ApiOperation(value = "image", notes = "删除图片文件")
	public ResponseResult deleteImage(@PathVariable("imageId") String imageId) {
		return imageService.deleteImage(imageId);
	}

}
