package com.chenzuhuang.util.file;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>压缩文件</p>
 *
 * @author chenzuhuang
 * @version 1.0
 * @date 2015/8/7
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
    public static void zipDirectory(File srcDir, File zipFile, boolean overwrite) throws Exception {
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
        zipDirectory(zipOutput, srcDir, srcDir.getName());
    }

    /**
     * 压缩目录的辅助方法。递归检查子目录。
     * @param zipOutput
     * @param file 当前文件
     * @param base 当前文件在压缩包里的绝对名称
     * @throws Exception
     */
    private static void zipDirectory(ZipOutputStream zipOutput, File file, String base) throws Exception {
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            zipOutput.putNextEntry(new ZipEntry(base + "/"));
            base = (base.length() == 0) ? "" : base + "/";
            System.out.println(base);
            for (int i = 0; i < fileList.length; i++) {
                zipDirectory(zipOutput, fileList[i], base + fileList[i].getName());
            }
        }
        else {
            zipOutput.putNextEntry(new ZipEntry(base));
            System.out.println(base);
            FileInputStream fileInputStream = new FileInputStream(file);
            int length = 0;
            byte[] buffer = new byte[2048];
            while ( (length = fileInputStream.read(buffer)) != -1) {
                zipOutput.write(buffer, 0, length);
                System.out.println(length);
                zipOutput.flush();
            }
            fileInputStream.close();
        }
    }

    /**
     * 压缩一个目录。如果存在同名文件，则会覆盖
     * @param srcDir 需要压缩 的目录
     * @param zipFile 压缩产生的文件
     * @throws Exception
     */
    public static void zipDirectory(File srcDir, File zipFile) throws Exception {
        zipDirectory(srcDir, zipFile, true);
    }

    /**
     * 压缩一个目录。压缩输出的文件名为(目录名.zip)
     * @param srcDir
     */
    public static void zipDirectory(File srcDir) throws Exception {
        zipDirectory(srcDir, new File(srcDir.getAbsolutePath()+".zip"), true);
    }

    /**
     * 对一个文件列表进行压缩。
     * @param files 文件列表
     * @param zipFile 压缩后产生的文件
     * @param overwrite 如存在同名文件，是否覆盖
     * @throws Exception
     */
    public static void zipFiles(File[] files, File zipFile, boolean overwrite) throws Exception {
        if (zipFile == null || files == null) {
            throw new Exception("zipFile和srcDir不能为空!");
        }
        if (files.length == 0) {
            throw new Exception("不能对一个空的文件列表进行压缩");
        }
        if (!overwrite && zipFile.exists()) {
            throw new Exception(zipFile.getAbsolutePath() + "文件已存在，参数设定了不能覆盖。");
        }
        if (!zipFile.exists()) {
            zipFile.createNewFile();
        }

        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));

        byte[] buffer = new byte[2048];
        int length = 0;
        for (File file: files) {
            FileInputStream fileInputStream = new FileInputStream(file);
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            while ((length = fileInputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, length);
                zipOutputStream.flush();
            }
            fileInputStream.close();
        }

        zipOutputStream.close();
    }

    /**
     * 对一个文件列表进行压缩。
     * @param fileList 文件列表
     * @param zipFile 压缩后产生的文件
     * @param overwrite 如果已经存在同名文件，是否覆盖。
     * @throws Exception
     */
    public static void zipFiles(List<File> fileList, File zipFile, boolean overwrite) throws Exception {
        zipFiles(fileList.toArray(new File[fileList.size()]), zipFile, overwrite);
    }

    /**
     * 对一个文件列表进行压缩，如存在同名目标文件，则会覆盖它。
     * @param fileList
     * @param zipFile
     * @throws Exception
     */
    public static void zipFiles(List<File> fileList, File zipFile) throws Exception {
        zipFiles(fileList.toArray(new File[fileList.size()]), zipFile, true);
    }

    /**
     * 对一个文件列表进行压缩，如存在同名目标文件，则会覆盖它。
     * @param files 文件列表
     * @param zipFile 压缩后生成的文件
     * @throws Exception
     */
    public static void zipFiles(File[] files, File zipFile) throws Exception {
        zipFiles(files, zipFile, true);
    }



    //test
    public static void main(String[] args) throws Exception {
//		zipFile(new File("D:/Test.java"), new File("D:/Test.zip"), false);
//        zipDiretory(new File("D:/Temp"), new File("D:/Temp.zip"), true);

        List<File> fileList = new ArrayList<File>();
        fileList.add(new File("D:/1.txt"));
        fileList.add(new File("D:/2.txt"));
        File[] files = {new File("D:/1.txt"), new File("D:/2.txt")};
//        zipFiles(fileList.toArray(new File[fileList.size()]), new File("D:/test.zip"), true);
        zipFiles(fileList, new File("D:/test.zip"));
        zipDirectory(new File("D:/user"), new File("D:/user.zip"), true);
    }

}

