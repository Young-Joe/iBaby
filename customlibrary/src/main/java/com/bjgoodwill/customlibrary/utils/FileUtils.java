package com.bjgoodwill.customlibrary.utils;

import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**    
 * 文件操作封装类
 * 主要用于创建、删除SD卡上的文件
 *
 */
public class FileUtils {

	/* SD卡文件根路径 */
	private static String SDCardRoot;
	public static int BUFFER_SIZE = 4098;

	static {
		SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
	}

	/**
	 * 获取SD卡文件根路径
	 * 
	 * @return SD卡文件根路径
	 * @author 张乾 研发中心
	 */
	public static String getDSPATH() {
		return SDCardRoot;
	}

	/**
	 * 根据文件名及路径，在SD卡上创建文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param dir
	 *            路径
	 * @return File类对象
	 * @throws IOException
	 * @author 张乾 研发中心
	 */
	public static File createSDFile(String fileName, String dir) throws IOException {
		File file = new File(dir + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 创建文件路径
	 * 
	 * @param dirName
	 *            文件路径
	 * @return File类对象
	 * @author 张乾 研发中心
	 */
	public static File createSDDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * 判断SD卡上该文件是否存在
	 * 
	 * @param filePath
	 *            文件名
	 * @return 存在：true，不存在：false
	 * @author 张乾 研发中心
	 */
	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * 判断SD卡上该文件夹是否存在,不存在则创建
	 *
	 * @param filePath
	 *            文件夹名
	 * @return 存在：true，不存在：false
	 * @author QiaoJF
	 */
	public static void isFileExistElseCreate(String filePath){
		if(isFileExist(filePath)){
			return;
		}else{
			createSDDir(filePath);
		}
	}

	/**
	 * 判断SD卡上该文件是否存在,不存在则创建
	 *
	 * @param fileName
	 *            文件名
	 * @return 存在：true，不存在：false
	 * @author QiaoJF
	 */
	public static void isFileExistElseCreate(String filePath, String fileName) {
		if(!isFileExist(filePath)){
			createSDDir(filePath);
		}
		try {
			createSDFile(fileName, filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void write2SDFromByte(String path, String fileName, byte[] data) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(fileName, path);
			output = new FileOutputStream(file);
			output.write(data);
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 遍历删除文件夹下所有文件
	 * 
	 * @param dir
	 *            File类对象
	 * @return 删除成功：true，删除失败：false
	 * @author 张乾 研发中心
	 */
	public boolean deleteRecursive(File dir) {
		boolean flag = false;
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				File temp = new File(dir, children[i]);
				if (temp.isDirectory()) {
					deleteRecursive(temp);
				} else {
					temp.delete();
				}
			}

			flag = dir.delete();
		}
		return flag;
	}

	/**
	 * 自动更新下载文件
	 * 
	 * @param url 文件下载地址
	 *            downFilePath文件下载到sd卡目录 downFileName下载文件名
	 * @author 王亚帅 研发中心
	 */
	public static void fileDownload(String url, String downFilePath,
			String downFileName) {
		try {
			File file = new File(downFilePath, downFileName);
			if (file.exists()) {
				file.delete();
			}
			URL myURl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myURl.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", url);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
			InputStream is = conn.getInputStream();
			int fileSize = conn.getContentLength();
			if (fileSize <= 0) {
				throw new RuntimeException("无法获取文件");
			} else if (is == null) {
				throw new RuntimeException("InputStream is null");
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int downloadSize = 0;
			do {
				int numread = is.read(buf);
				if (numread == -1) {
					break;
				}
				fileOutputStream.write(buf, 0, numread);
				downloadSize += numread;
			} while (downloadSize < fileSize);

			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制文件流
	 * 
	 * @param in
	 *            输入流
	 * @param out
	 *            输出流
	 * @throws IOException
	 * @author 吴志敏 研发中心
	 */
	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		int length = 0;
		while ((length = in.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
	}

	/**
	 * 关闭流
	 * 
	 * @param c
	 *            流对象
	 * @author 吴志敏 研发中心
	 */
	public static void closeStream(Closeable c) {
		try {
			c.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 读文件，返回String
	 * 
	 * @param fileName
	 * @return
	 */
//	public static String readFileAsString(String fileName) {
//		String res = "";
//		try {
//			FileInputStream fin = new FileInputStream(fileName);
//			int length = fin.available();
//			byte[] buffer = new byte[length];
//			fin.read(buffer);
//			res = EncodingUtils.getString(buffer, "UTF-8");
//			fin.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return res;
//	}

	@SuppressWarnings("resource")
	public static byte[] readFile(String filePath) throws IOException {
		File file = new File(filePath);
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	/**
	 * 
	 * 
	 * 根据绝对路径删除文件
	 * 
	 * @param path
	 */
	public static boolean deleteFileByPath(String path) {
		boolean flag = false;
		try {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
				flag = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	public static void saveFile(String filePath, String content) {
		FileOutputStream fw;
		try {
			fw = new FileOutputStream(filePath);
			fw.write(content.getBytes());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除老的apk安装包
	 */
//	public static void deleteOldApk(){
//		File appFile = new File(AppConstant.getSysPath());
//		if(appFile.exists() && appFile.isDirectory()){
//			String[] children = appFile.list();
//			for(int i = 0; i < children.length; i++){
//				if(children[i].contains("NurseStationScreen")){
//					File item = new File(AppConstant.getSysPath(), children[i]);
//					item.delete();
//				}
//			}
//		}
//	}

}
