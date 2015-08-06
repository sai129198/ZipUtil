package com.chenzuhuang.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 
 * @author chenzuhuang
 * @since 1.0
 */
public class ZipUtil {
	
	/**
	 * 压缩文件
	 * @param srcFile 需要压缩的文件
	 * @param zipFile 压缩后的文件(.zip后缀需要自己添加)
	 * @param overwrite 当文件已存在时是否覆盖
	 * @throws Exception
	 */
	public static void zipFile(File srcFile, File zipFile, boolean overwrite) throws Exception {
		if (zipFile == null || srcFile == null) {
			throw new Exception("zipFile和srcFile不能为空!");
		}
		if (!overwrite && zipFile.exists()) {
			throw new Exception(zipFile.getAbsolutePath() + "文件已存在，参数设定了不能覆盖。");
		}
		if (!zipFile.exists()) {
			zipFile.createNewFile();
		}
		
		FileInputStream fileInput = new FileInputStream(srcFile);
		ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(zipFile));
		
		byte[] buffer = new byte[2048];
		ZipEntry zipEntry = new ZipEntry(srcFile.getName());	//压缩包里面的文件名
		zipOutput.putNextEntry(zipEntry);
		
		int len = 0;
		while ((len = fileInput.read(buffer)) != -1) {
			zipOutput.write(buffer, 0, len);
			zipOutput.flush();
		}
		fileInput.close();
		zipOutput.close();
	}
	
	/**
	 * 压缩一个目录。
	 * @param srcDir 需要压缩的目录
	 * @param zipFile 压缩后的文件
	 * @param overwrite 是否覆盖已存在文件
	 * @throws Exception
	 */
	public static void zipDiretory(File srcDir, File zipFile, boolean overwrite) throws Exception {
		if (zipFile == null || srcDir == null) {
			throw new Exception("zipFile和srcDir不能为空!");
		}
		if (!overwrite && zipFile.exists()) {
			throw new Exception(zipFile.getAbsolutePath() + "文件已存在，参数设定了不能覆盖。");
		}
		if (!zipFile.exists()) {
			zipFile.createNewFile();
		}
		
		ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(zipFile));
		zipDiretory(zipOutput, srcDir, srcDir.getName());
	}
	
	/**
	 * 压缩目录的辅助方法。递归检查子目录。
	 * @param zipOutput 
	 * @param file 当前文件
	 * @param base 当前文件在压缩包里的绝对名称
	 * @throws Exception
	 */
	private static void zipDiretory(ZipOutputStream zipOutput, File file, String base) throws Exception {
		if (file.isDirectory()) {
           File[] fileList = file.listFiles();
           zipOutput.putNextEntry(new ZipEntry(base + "/"));
           base = base.length() == 0 ? "" : base + "/";
           for (int i = 0; i < fileList.length; i++) {
        	   zipDiretory(zipOutput, fileList[i], base + fileList[i].getName());
           }
        }
        else {
        	zipOutput.putNextEntry(new ZipEntry(base));
        	FileInputStream in = new FileInputStream(file);
        	int length = 0;
        	byte[] buffer = new byte[2048];
        	while ( (length = in.read(buffer)) != -1) {
        		zipOutput.write(buffer, 0, length);
        		zipOutput.flush();
        	}
        	in.close();
        }
    }

	//test
	public static void main(String[] args) throws Exception {
//		zipFile(new File("D:/Test.java"), new File("D:/Test.zip"), false);
		zipDiretory(new File("D:/Temp"), new File("D:/Temp.zip"), true);
	}

}
