package vip.laohei.sharesystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger 接口文档初始化
 * 
 * @author laohei
 *
 */
@Configuration
public class Swagger2Config {

	public static final String VERSION = "0.1";

	/**
	 * 测试API，接口前缀：test
	 * 
	 * @return
	 */
	@Bean
	public Docket testTestApi() {
		return new Docket(DocumentationType.SWAGGER_2) //
				// .globalOperationParameters(setRequestHeaders()) //
				.apiInfo(testTestApiInfo()) //
				.select() //
				.apis(RequestHandlerSelectors.basePackage("vip.laohei.sharesystem.controller.test"))
				// 加了ApiOperation注解的类，才生成接口文档
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) //
				.paths(PathSelectors.any()) //
				.build().groupName("测试接口");
	}

	private ApiInfo testTestApiInfo() {
		return new ApiInfoBuilder().title("laohei分享系统测试接口文档") //
				.description("测试接口文档") //
				.version(VERSION) //
				.build();
	}

	/**
	 * 后台管理API，接口前缀：laohei-admin
	 * 
	 * @return
	 */
	@Bean
	public Docket adminApi() {
		return new Docket(DocumentationType.SWAGGER_2) //
				// .globalOperationParameters(setRequestHeaders()) //
				.apiInfo(adminApiInfo()) //
				.select() //
				.apis(RequestHandlerSelectors.basePackage("vip.laohei.sharesystem.controller.admin"))
				// 加了ApiOperation注解的类，才生成接口文档
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) //
				.paths(PathSelectors.any()) //
				.build().groupName("后台管理接口");
	}

	private ApiInfo adminApiInfo() {
		Contact contact = new Contact("laohei", "https://laohei.vip", "hi@laohei.vip");
		return new ApiInfoBuilder().title("laohei分享系统-后台管理接口文档") //
				.description("后台管理接口文档") //
				.contact(contact) //
				.version(VERSION) //
				.build();
	}

	/**
	 * 前台 API，接口前缀：portal
	 * 
	 * @return
	 */
	@Bean
	public Docket portalApi() {
		return new Docket(DocumentationType.SWAGGER_2) //
				// .globalOperationParameters(setRequestHeaders()) //
				.apiInfo(portalApiInfo()) //
				.select() //
				.apis(RequestHandlerSelectors.basePackage("vip.laohei.sharesystem.controller.portal"))
				// 加了ApiOperation注解的类，才生成接口文档
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) //
				.paths(PathSelectors.any()) //
				.build().groupName("前台接口");
	}

	private ApiInfo portalApiInfo() {
		Contact contact = new Contact("laohei", "https://laohei.vip", "hi@laohei.vip");
		return new ApiInfoBuilder().title("laohei分享系统-前台接口文档") //
				.description("前台接口文档") //
				.contact(contact) //
				.version(VERSION) //
				.build();
	}

}
