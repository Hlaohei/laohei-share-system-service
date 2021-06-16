package vip.laohei.sharesystem.utils;

/**
 * 所有常量
 * 
 * @author laohei
 *
 */
public interface Constants {

	interface User {
		String ROLE_ADMIN = "role_admin";
		String DEFAULT_STATE = "1";
		String COOKIE_TOKE_KEY = "laohei_website_token";
		// redis 的 key
		String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
		String KEY_TOKEN = "key_token_";
		String KEY_COMMIT_TOKEN_RECORD = "key_commit_token_record_";
	}

	interface WebsiteInfo {
		String WEB_SITE_TITLE = "web_site_title";
		String WEB_SITE_DESCRIPTION = "web_site_description";
		String WEB_SITE_KEYWORDS = "web_site_keywords";
		String WEB_SITE_VIEW_COUNT = "web_site_view_count";

		String WEB_SITE_BEIAN_ICP = "web_site_beian_icp";
		String WEB_SITE_BEIAN_GONGAN = "web_site_beian_gongan";
	}

	interface Page {
		int DEFAULT_PAGE = 1;
		int DEFAULT_SIZE = 2;
	}

	interface ImageType {
		String PREFIX = "image/";
		String TYPE_JPEG = "jpeg";
		String TYPE_JPG = "jpg";
		String TYPE_PNG = "png";
		String TYPE_GIF = "gif";
		String TYPE_JPEG_WITH_PREFIX = PREFIX + "jpeg";
		String TYPE_JPG_WITH_PREFIX = PREFIX + "jpg";
		String TYPE_PNG_WITH_PREFIX = PREFIX + "png";
		String TYPE_GIF_WITH_PREFIX = PREFIX + "gif";
	}

	/**
	 * 单位是 毫秒
	 */
	interface TimeValueMillisecond {
		int MIN = 60 * 1000;
		int MIN_5 = 60 * 1000 * 5;
		int HOUR = 60 * MIN;
		int HOUR_2 = 60 * MIN * 2;
		int DAY = 24 * HOUR;
		int WEEK = 7 * DAY;
		int MONTH = 30 * DAY;
	}

	/**
	 * 单位是 秒
	 */
	interface TimeValueSecond {
		int SECOND_10 = 10;
		int HALF_MIN = 30;
		int MIN = 60;
		int MIN_5 = 60 * 5;
		int MIN_10 = 60 * 10;
		int HOUR = 60 * MIN;
		int HOUR_2 = 60 * MIN * 2;
		int DAY = 24 * HOUR;
		int WEEK = 7 * DAY;
		int MONTH = 30 * DAY;
	}

	interface Share {
		int TITLE_MAX_LENGTH = 50;
		int SUMMARY_MAX_LENGTH = 250;

		// 0表示不显示，1表示显示
		String STATE_NOT_SHOW = "0";
		String STATE_SHOW = "1";

		// 0表示不置顶，1表示置顶
		String NOT_TOP = "0";
		String IS_TOP = "1";

		String KEY_SHARE_CACHE = "key_share_cache_";
		String KEY_SHARE_VIEW_COUNT = "key_share_view_count_";
		String KEY_SHARE_LIST_FIRST_PAGE = "key_share_list_first_page";
	}

	interface Works {
		// 0表示不显示，1表示显示
		String STATE_NOT_SHOW = "0";
		String STATE_SHOW = "1";
	}

	interface Resume {
		// 0表示不显示，1表示显示
		String STATE_NOT_SHOW = "0";
		String STATE_SHOW = "1";
	}

	interface WebsiteBackground {
		// 0表示不显示，1表示显示
		String STATE_NOT_SHOW = "0";
		String STATE_SHOW = "1";

		// 0表示不置顶，1表示置顶
		String FIXED_NOT_FIXED = "0";
		String FIXED_IS_FIXED = "1";
	}

}
