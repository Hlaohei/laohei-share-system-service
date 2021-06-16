package vip.laohei.sharesystem.controller.test;

import java.util.Date;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import vip.laohei.sharesystem.entity.Image;

/**
 * 测试接口
 * 
 * @author laohei
 *
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(value = "/test", tags = { "测试接口" }, description = "测试接口")
public class TestApi {

	@PreAuthorize("@permission.login()")
	@GetMapping("/hello")
	@ApiOperation(value = "hello", notes = "简单的测试服务器")
	public String hello() {
		Image image = new Image();
		image.setId("123");
		image.setName("测试图片");
		image.setUrl("https://laohei.vip/");
		image.setPath("/laohei/image");
		image.setFileType("image/jpeg");
		image.setState("1");
		image.setCreateTime(new Date());
		image.setUpdateTime(new Date());

		log.info(image.toString());

		log.info("hello......");
		return "hello";
	}

}
