package vip.laohei.sharesystem.service;

import org.springframework.web.multipart.MultipartFile;

import vip.laohei.sharesystem.response.ResponseResult;

public interface IImageService {

	/**
	 * 上传图片文件
	 * <p>
	 * 我这里直接上传到测试服务器
	 * 
	 * @param image
	 * @return
	 */
	ResponseResult uploadImage(MultipartFile image);

	/**
	 * 获取图片列表
	 * 
	 * @param page
	 * @param size
	 * @param keyword
	 * @return
	 */
	ResponseResult listImage(int page, int size, String keyword);

	/**
	 * 
	 * 删除图片文件
	 * <p>
	 * 假删除，只是把图片的状态改成 0
	 * 
	 * @param imageId
	 * @return
	 */
	ResponseResult deleteImage(String imageId);

}
