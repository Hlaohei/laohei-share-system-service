package vip.laohei.sharesystem.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import vip.laohei.sharesystem.dao.ImageDao;
import vip.laohei.sharesystem.entity.Image;
import vip.laohei.sharesystem.response.ResponseResult;
import vip.laohei.sharesystem.service.IImageService;
import vip.laohei.sharesystem.utils.Constants;
import vip.laohei.sharesystem.utils.FtpUtils;
import vip.laohei.sharesystem.utils.IdWorker;
import vip.laohei.sharesystem.utils.TextUtils;

/**
 * 图片服务 实现类
 * 
 * @author laohei
 *
 */
@Slf4j
@Service
@Transactional
public class ImageServiceImpl extends BaseService implements IImageService {

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private ImageDao imageDao;

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");

	@Value("${laohei.image.max-size}")
	public long maxSize;

	@Value("${ftp.imageUrl}")
	public String imageBaseUrl;

	/**
	 * 上传图片文件
	 * <p>
	 * 我这里直接上传到测试服务器
	 * 
	 * @param image
	 * @return
	 */
	@Override
	public ResponseResult uploadImage(MultipartFile image) {
		// 判断是否有文件
		if (image.getSize() == 0) {
			return ResponseResult.FAILED("图片不可以为空");
		}

		// 判断文件类型，我们只支持图片文件上传，比如：png，jpg，gif
		String fileType = image.getContentType().toLowerCase();
		log.info("fileType ==> " + fileType);
		if (TextUtils.isEmpty(fileType)) {
			return ResponseResult.FAILED("图片格式错误");
		}

		// 获取相关数据，比如说：文件类型，文件名称
		String originalFileName = image.getOriginalFilename();
		log.info("originalFileName ==> " + originalFileName);
		String type = getType(fileType, originalFileName);
		log.info("type ==> " + type);
		if (type == null) {
			return ResponseResult.FAILED("不支持此图片类型");
		}

		// 限制文件的大小
		long size = image.getSize();
		log.info("maxSize ==> " + maxSize + "  size ==> " + size);
		if (size > maxSize) {
			return ResponseResult.FAILED("图片最大仅支持" + (maxSize / 1024 / 1024) + "MB");
		}

		// 根据我们定的规则进行命名
		String id = idWorker.nextId() + "";
		log.info("uuid ==> " + id);
		// 生成随机名称，各种办法都可以，只要不一样就行
		String targetName = id + "-+-" + image.getOriginalFilename();
		log.info("targetName ==> " + targetName);

		// 配置图片的保存目录
		// 规则：配置目录、日期、类型、ID
		// 当前毫秒数
		long currentMillisecond = System.currentTimeMillis();
		// 当前日期
		String currentDay = simpleDateFormat.format(currentMillisecond);
		log.info("currentDay ==> " + currentDay);
		// 日期路径
		String dayPath = "image/" + currentDay;
		log.info("dayPath ==> " + dayPath);

		String targetPath = dayPath + "/";
		log.info("targetPath ==> " + targetPath);

		try {
			boolean upload = FtpUtils.uploadFileToFtp(targetPath, targetName, image.getInputStream());
			log.info("upload ==> " + upload);

			if (!upload) {
				return ResponseResult.FAILED("图片上传失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseResult.FAILED("图片上传失败");
		}

		String resultPath = imageBaseUrl + targetPath + targetName;

		Image imageInfo = new Image();
		imageInfo.setId(id);
		imageInfo.setName(image.getOriginalFilename());
		imageInfo.setUrl(resultPath);
		imageInfo.setPath(targetPath + targetName);
		imageInfo.setFileType(image.getContentType());
		imageInfo.setState("1");
		imageInfo.setCreateTime(new Date());
		imageInfo.setUpdateTime(new Date());

		// 保存到数据库
		imageDao.save(imageInfo);

		return ResponseResult.SUCCESS("图片上传成功").setData(imageInfo);
	}

	/**
	 * 获取图片文件类型
	 * 
	 * @param fileType
	 * @param name
	 * @return
	 */
	private String getType(String fileType, String name) {
		String type = null;
		if (Constants.ImageType.TYPE_PNG_WITH_PREFIX.equals(fileType) && name.endsWith(Constants.ImageType.TYPE_PNG)) {
			type = Constants.ImageType.TYPE_PNG;
		} else if (Constants.ImageType.TYPE_GIF_WITH_PREFIX.equals(fileType) && name.endsWith(Constants.ImageType.TYPE_GIF)) {
			type = Constants.ImageType.TYPE_GIF;
		} else if (Constants.ImageType.TYPE_JPG_WITH_PREFIX.equals(fileType) && name.endsWith(Constants.ImageType.TYPE_JPG)) {
			type = Constants.ImageType.TYPE_JPG;
		} else if (Constants.ImageType.TYPE_JPEG_WITH_PREFIX.equals(fileType) && name.endsWith(Constants.ImageType.TYPE_JPG)) {
			type = Constants.ImageType.TYPE_JPG;
		} else if (Constants.ImageType.TYPE_JPEG_WITH_PREFIX.equals(fileType) && name.endsWith(Constants.ImageType.TYPE_JPEG)) {
			type = Constants.ImageType.TYPE_JPEG;
		}
		return type;
	}

	/**
	 * 获取图片列表
	 * 
	 * @param page
	 * @param size
	 * @param keyword
	 * @return
	 */
	@Override
	public ResponseResult listImage(int page, int size, String keyword) {
		// 处理 page，size
		page = checkPage(page);
		size = checkSize(size);

		// 创建分页条件
		Sort sort = Sort.by(Sort.Order.desc("createTime"));
		Pageable pageable = PageRequest.of(page - 1, size, sort);

		// 查询
		Page<Image> all = imageDao.findAll(new Specification<Image>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Image> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<>();
				if (!TextUtils.isEmpty(keyword)) {
					Predicate namePre = cb.like(root.get("name").as(String.class), "%" + keyword + "%");
					predicates.add(namePre);
				}
				Predicate[] preArray = new Predicate[predicates.size()];
				predicates.toArray(preArray);
				return cb.and(preArray);
			}
		}, pageable);

		// 返回结果
		return ResponseResult.SUCCESS("获取图片列表成功").setData(all);
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
	@Override
	public ResponseResult deleteImage(String imageId) {
		int result = imageDao.deleteImageByUpdateState(imageId);
		if (result > 0) {
			return ResponseResult.SUCCESS("删除图片成功");
		}
		return ResponseResult.FAILED("删除图片失败");
	}

}
