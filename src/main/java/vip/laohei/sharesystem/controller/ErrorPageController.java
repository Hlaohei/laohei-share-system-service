package vip.laohei.sharesystem.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vip.laohei.sharesystem.response.ResponseResult;

/**
 * 错误码转统一接口返回
 * 
 * @author laohei
 *
 */
@CrossOrigin
@RestController
public class ErrorPageController {

	@GetMapping("/403")
	public ResponseResult page403() {
		return ResponseResult.ERROR_403();
	}

	@GetMapping("/404")
	public ResponseResult page404() {
		return ResponseResult.ERROR_404();
	}

	@GetMapping("/504")
	public ResponseResult page504() {
		return ResponseResult.ERROR_504();
	}

	@GetMapping("/505")
	public ResponseResult page505() {
		return ResponseResult.ERROR_505();
	}

}
