package net.oscer.framework;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kz
 */
public class ImageUtils {


    public static final ImageUtils ME = new ImageUtils();

    private static final String SUFFIXES = "jpeg|gif|jpg|png|bmp|svg|webp";

    /**
     * 获取目录下所有文件
     *
     * @param path
     * @return
     */
    public static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
            }
            //获取子目录下的所有文件
            if (tempList[i].isDirectory()) {
                getFiles(tempList[i].getPath());
            }
        }
        return files;
    }

    public static String getSuffix(String name) {
        Pattern pat = Pattern.compile("[\\w]+[\\.](" + SUFFIXES + ")");// 正则判断
        Matcher mc = pat.matcher(name.toLowerCase());// 条件匹配
        String fileName = null;
        while (mc.find()) {
            fileName = mc.group();// 截取文件名后缀名
        }
        if (fileName != null) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null;
    }
}
