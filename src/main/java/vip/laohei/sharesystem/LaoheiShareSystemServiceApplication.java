package vip.laohei.sharesystem;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.gson.Gson;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import vip.laohei.sharesystem.utils.IdWorker;
import vip.laohei.sharesystem.utils.RedisUtils;

/**
 * laohei 分享系统
 * 
 * @version 0.1
 * @author laohei
 */
@EnableSwagger2
@SpringBootApplication
public class LaoheiShareSystemServiceApplication {

	public static void main(String[] args) throws UnknownHostException {
		ConfigurableApplicationContext application = SpringApplication.run(LaoheiShareSystemServiceApplication.class, args);

		/**
		 * 不重要
		 */
		// 获取运行IP和端口
		Environment env = application.getEnvironment();
		String ip = InetAddress.getLocalHost().getHostAddress();
		String port = env.getProperty("server.port");
		System.out.println("  _                   _              _               _         \n" //
				+ " | |                 | |            (_)             (_)        \n" //
				+ " | |   __ _    ___   | |__     ___   _      __   __  _   _ __  \n" //
				+ " | |  / _` |  / _ \\  | '_ \\   / _ \\ | |     \\ \\ / / | | | '_ \\ \n" //
				+ " | | | (_| | | (_) | | | | | |  __/ | |  _   \\ V /  | | | |_) |\n" //
				+ " |_|  \\__,_|  \\___/  |_| |_|  \\___| |_| (_)   \\_/   |_| | .__/ \n" //
				+ "                                                        | |    \n" //
				+ " laohei-share-system  (v0.1)                            |_|  ");
		System.out.println(" **********************************************************\n" //
				+ " *                                                        *\n" //
				+ " *   .=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-.   *\n" //
				+ " *   |                     ______                     |   *\n" //
				+ " *   |                  .-\"      \"-.                  |   *\n" //
				+ " *   |                 /            \\                 |   *\n" //
				+ " *   |     _          |              |          _     |   *\n" //
				+ " *   |    ( \\         |,  .-.  .-.  ,|         / )    |   *\n" //
				+ " *   |     > \"=._     | )(__/  \\__)( |     _.=\" <     |   *\n" //
				+ " *   |    (_/\"=._\"=._ |/     /\\     \\| _.=\"_.=\"\\_)    |   *\n" //
				+ " *   |           \"=._\"(_     ^^     _)\"_.=\"           |   *\n" //
				+ " *   |               \"=\\__|IIIIII|__/=\"               |   *\n" //
				+ " *   |              _.=\"| \\IIIIII/ |\"=._              |   *\n" //
				+ " *   |    _     _.=\"_.=\"\\          /\"=._\"=._     _    |   *\n" //
				+ " *   |   ( \\_.=\"_.=\"     `--------`     \"=._\"=._/ )   |   *\n" //
				+ " *   |    > _.=\"                            \"=._ <    |   *\n" //
				+ " *   |   (_/                                    \\_)   |   *\n" //
				+ " *   |                                                |   *\n" //
				+ " *   '-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-='   *\n" //
				+ " *                                                        *\n" //
				+ " **********************************************************\n");
		System.out.println(" ****************************************************************\n" //
				+ " *                                                              *\n" //
				+ " *\t 本地访问：http://127.0.0.1:" + port + "                    \t*\n" //
				+ " *\t 外网访问：http://" + ip + ":" + port + "                   \t*\n" //
				+ " *\t API接口测试：http://127.0.0.1:" + port + "/swagger-ui.html \t*\n" //
				+ " *\t API接口文档：http://127.0.0.1:" + port + "/doc.html     \t\t*\n" //
				+ " *                                                              *\n" //
				+ " ****************************************************************");
	}

	@Bean
	public IdWorker createIdWorker() {
		return new IdWorker(0, 0);
	}

	@Bean
	public BCryptPasswordEncoder createPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RedisUtils createRedisUtils() {
		return new RedisUtils();
	}

	@Bean
	public Random createRandom() {
		return new Random();
	}

	@Bean
	public Gson createGson() {
		return new Gson();
	}
}
