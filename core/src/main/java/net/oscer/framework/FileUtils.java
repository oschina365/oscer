package net.oscer.framework;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件处理工具类
 *
 * @author jmy
 * @version 1.0.7
 * @date 2020/3/31 0031 14:01
 */
public class FileUtils {

    /**
     * 网络图片转化为二进制数组
     *
     * @param url
     * @return
     */
    public static byte[] getFileStream(String url) {
        try {
            // 得到图片的二进制数据
            byte[] btImg = readInputStream(getFileStreamFromUrl(url));
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片转化
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 获取网络文件流
     *
     * @param url
     * @return
     */
    public static InputStream getFileStreamFromUrl(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
