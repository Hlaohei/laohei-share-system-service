package vip.laohei.sharesystem.service.impl;

import vip.laohei.sharesystem.utils.Constants;

/**
 * 基础服务类
 * 
 * @author laohei
 *
 */
public class BaseService {

	/**
	 * 检查页码
	 * 
	 * @param page
	 * @return
	 */
	int checkPage(int page) {
		if (page < Constants.Page.DEFAULT_PAGE) {
			page = Constants.Page.DEFAULT_PAGE;
		}
		return page;
	}

	/**
	 * 检查每页数量
	 * 
	 * @param size
	 * @return
	 */
	int checkSize(int size) {
		if (size < Constants.Page.DEFAULT_SIZE) {
			size = Constants.Page.DEFAULT_SIZE;
		}
		return size;
	}

}
