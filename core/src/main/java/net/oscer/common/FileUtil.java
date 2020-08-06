package net.oscer.common;


import net.oscer.framework.ConfigTool;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * 文件上传分类,不同文件上传的有不同flag
 *
 * @author kz
 * @date 2017/11/17
 */
public class FileUtil {

    private final static Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 连接超时 5秒
     */
    private final static int HTTP_CONNECT_TIMEOUT = 5 * 1000;

    /**
     * 读取超时 10秒
     */
    private final static int HTTP_READ_TIMEOUT = 10 * 1000;


    /**
     * 获取文件后缀
     *
     * @param path
     * @return
     */
    public static String getFileSuffix(String path) {
        if (StringUtils.isBlank(path)) {
            return StringUtils.EMPTY;
        }
        int lastNum = StringUtils.lastIndexOf(path, ".");
        if (lastNum == -1) {
            return path;
        }
        return StringUtils.substring(path, lastNum + 1);
    }

    public static int getFileType(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return 0;
        }
        String suffixStr = ConfigTool.getProp("qiniu.suffixImage");
        if (StringUtils.isNotBlank(suffixStr)) {
            String suffix = getFileSuffix(fileName);
            boolean isImage = StringUtils.contains(suffixStr, suffix);
            if (isImage) {
                return 1;
            }
        }
        return 0;
    }

    public static String getAllFileName(File file) {
        if (file == null) {
            return StringUtils.EMPTY;
        }
        String name = file.getName();
        String path = getPath();
        StringBuffer sb = new StringBuffer(path);
        String newName = getFileName(name);
        sb.append(newName);
        return sb.toString();
    }

    public static String getPath() {
        String path = ConfigTool.getProp("qiniu.path");
        String datePath = DateFormatUtils.format(new Date(), "/yyyy/MM/dd/");
        File file = new File(datePath);
        file.mkdirs();
        return path.concat(datePath);
    }

    public static String getFileName(String name) {
        String suffix = getFileSuffix(name);
        String key = System.currentTimeMillis() + "" + ((int) ((Math.random() * 9 + 1) * 100000)) + "".concat(".").concat(suffix);
        return key;
    }

    public static byte[] getFileByte(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }


    /**
     * 保存图片
     *
     * @param imgUrl
     * @param locFilePath
     * @return
     * @throws Exception
     */
    public static boolean savePic(String imgUrl, String locFilePath) {
        boolean bool = false;
        BufferedImage src = null;
        HttpURLConnection conn = null;
        try {
            URL url2 = new URL(imgUrl);
            long begin = System.currentTimeMillis();
            conn = (HttpURLConnection) url2.openConnection();
            conn.setConnectTimeout(HTTP_CONNECT_TIMEOUT);//设置连接参数
            conn.setReadTimeout(HTTP_READ_TIMEOUT);
            conn.connect(); //连接
            long connTime = System.currentTimeMillis() - begin;
            int size = conn.getContentLength();
            long downloadBegin = System.currentTimeMillis();
            src = ImageIO.read(conn.getInputStream());
            log.info((new StringBuilder(60).append("连接耗时:").append(connTime).append(" 大小:").append(size / 1024).append("kb 下载耗时:").append(System.currentTimeMillis() - downloadBegin)).toString());
        } catch (MalformedURLException e) {
            log.error("下载链接异常", e);
        } catch (IOException e) {
            log.error("下载异常", e);
        } catch (Exception e) {
            log.error("下载异常", e);
        } finally {
            conn.disconnect();//释放连接
        }
        try {
            if (src == null) {
                return bool;
            }
            long begin = System.currentTimeMillis();
            File f = new File(locFilePath);
            f.getParentFile().mkdirs();
            ImageIO.write(src, "jpeg", f);
            log.info("图片保存耗时:" + (System.currentTimeMillis() - begin));
            bool = true;
        } catch (IOException e) {
            log.error("保存图片异常", e);
        } catch (Exception e) {
            log.error("保存图片异常", e);
        }
        return bool;
    }
}
