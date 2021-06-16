package vip.laohei.sharesystem.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import lombok.extern.slf4j.Slf4j;

/**
 * ftp 工具类 目前服务器写死
 * 
 * @author laohei
 *
 */
@Slf4j
public class FtpUtils {

	@SuppressWarnings("unused")
	private static String LOCAL_CHARSET = "GBK";

	// ftp服务器地址
//	@Value("${ftp.server}")
	private static String hostname = "132.226.23.246";

	// ftp服务器端口
//	@Value("${ftp.port}")
	private static int port = 21;

	// ftp登录账号
//	@Value("${ftp.userName}")
	private static String username = "laohei-image";

	// ftp登录密码
//	@Value("${ftp.userPassword}")
	private static String password = "HX5wJzEfc52K2nrh";

	// ftp保存目录
//	@Value("${ftp.bastPath}")
	private static String basePath = "/upload";

	/**
	 * 初始化ftp服务器
	 */
	public static FTPClient getFtpClient() {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("utf-8");

		try {
			// 设置连接超时时间
			ftpClient.setDataTimeout(1000 * 120);
			log.info("连接 FTP 服务器 ==> " + hostname + ":" + port);
			// 连接 FTP 服务器
			ftpClient.connect(hostname, port);
			// 登录 FTP 服务器
			ftpClient.login(username, password);
			// 是否成功登录服务器
			int replyCode = ftpClient.getReplyCode();
			if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
				// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
				LOCAL_CHARSET = "UTF-8";
			}
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				log.info("连接 FTP 服务器 失败 ==> " + hostname + ":" + port);
			}
			log.info("连接 FTP 服务器 成功 ==> " + hostname + ":" + port);
		} catch (MalformedURLException e) {
			log.info(e.getMessage(), e);
		} catch (IOException e) {
			log.info(e.getMessage(), e);
		}
		return ftpClient;
	}

	/**
	 * 上传文件
	 *
	 * @param targetDir   ftp服务保存地址
	 * @param fileName    上传到ftp的文件名
	 * @param inputStream 输入文件流
	 * @return
	 */
	public static boolean uploadFileToFtp(String targetDir, String fileName, InputStream inputStream) {
		boolean isSuccess = false;
		String servicePath = String.format("%s%s%s", basePath, "/", targetDir);
		FTPClient ftpClient = getFtpClient();
		try {
			if (ftpClient.isConnected()) {
				log.info("开始上传文件到FTP，文件名称 ==> " + fileName);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);// 设置上传文件类型为二进制，否则将无法打开文件
				ftpClient.makeDirectory(servicePath);
				ftpClient.changeWorkingDirectory(servicePath);
				// 设置为被动模式(如上传文件夹成功，不能上传文件，注释这行，否则报错refused:connect )
				ftpClient.enterLocalPassiveMode();// 设置被动模式，文件传输端口设置
				ftpClient.storeFile(fileName, inputStream);
				inputStream.close();
				ftpClient.logout();
				isSuccess = true;
				log.info(fileName + "文件上传到FTP成功");
			} else {
				log.info("FTP连接建立失败");
			}
		} catch (Exception e) {
			log.info(fileName + "文件上传到FTP出现异常");
			log.info(e.getMessage(), e);
		} finally {
			closeFtpClient(ftpClient);
			closeStream(inputStream);
		}
		return isSuccess;
	}

	public static void closeStream(Closeable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (IOException e) {
				log.info(e.getMessage(), e);
			}
		}
	}

	// 改变目录路径
	public static boolean changeWorkingDirectory(FTPClient ftpClient, String directory) {
		boolean flag = true;
		try {
			flag = ftpClient.changeWorkingDirectory(directory);
			if (flag) {
				log.info("进入文件夹" + directory + " 成功！");

			} else {
				log.info("进入文件夹" + directory + " 失败！开始创建文件夹");
			}
		} catch (IOException e) {
			log.info(e.getMessage(), e);
		}
		return flag;
	}

	// 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
	public static boolean CreateDirecroty(FTPClient ftpClient, String remote) throws IOException {
		boolean success = true;

		String directory = remote;
		if (!remote.endsWith(File.separator)) {
			directory = directory + File.separator;
		}
		// 如果远程目录不存在，则递归创建远程服务器目录
		if (!directory.equalsIgnoreCase(File.separator) && !changeWorkingDirectory(ftpClient, new String(directory))) {
			int start = 0;
			int end = 0;
			if (directory.startsWith(File.separator)) {
				start = 1;
			} else {
				start = 0;
			}
			end = directory.indexOf(File.separator, start);
			String path = "";
			String paths = "";
			while (true) {
				String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
				path = path + File.separator + subDirectory;
				if (!existFile(ftpClient, path)) {
					if (makeDirectory(ftpClient, subDirectory)) {
						changeWorkingDirectory(ftpClient, subDirectory);
					} else {
						log.info("创建目录[" + subDirectory + "]失败");
						changeWorkingDirectory(ftpClient, subDirectory);
					}
				} else {
					changeWorkingDirectory(ftpClient, subDirectory);
				}

				paths = paths + File.separator + subDirectory;
				start = end + 1;
				end = directory.indexOf(File.separator, start);
				// 检查所有目录是否创建完毕
				if (end <= start) {
					break;
				}
			}
		}
		return success;
	}

	// 判断ftp服务器文件是否存在
	public static boolean existFile(FTPClient ftpClient, String path) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		if (ftpFileArr.length > 0) {
			flag = true;
		}
		return flag;
	}

	// 创建目录
	public static boolean makeDirectory(FTPClient ftpClient, String dir) {
		boolean flag = true;
		try {
			flag = ftpClient.makeDirectory(dir);
			if (flag) {
				log.info("创建文件夹" + dir + " 成功！");

			} else {
				log.info("创建文件夹" + dir + " 失败！");
			}
		} catch (Exception e) {
			log.info(e.getMessage(), e);
		}
		return flag;
	}

	public static void closeFtpClient(FTPClient ftpClient) {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				System.out.printf(e.getMessage(), e);
			}
		}
	}

}
