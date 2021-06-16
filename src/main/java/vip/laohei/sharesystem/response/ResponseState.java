package vip.laohei.sharesystem.response;

/**
 * 自定义统一返回数据格式信息
 * 
 * @author laohei
 *
 */
public enum ResponseState {

	SUCCESS(true, 2000, "操作成功"), //
	FAILED(false, 4000, "操作失败"), //
	//
	LOGIN_SUCCESS(true, 2001, "登录成功"), //
	//
	LOGIN_FAILED(false, 4001, "登录失败"), //
	//
	ERROR_403(false, 4403, "账号权限不足，请先登录管理员账号"), //
	ERROR_404(false, 4404, "页面丢失"), //
	ERROR_504(false, 4504, "系统繁忙，请稍后重试"), //
	ERROR_505(false, 4505, "请求错误，请检查提交数据"), //
	//
	;

	private boolean success;

	private int code;

	private String message;

	ResponseState(boolean success, int code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
