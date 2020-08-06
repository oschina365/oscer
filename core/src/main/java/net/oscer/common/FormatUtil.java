package net.oscer.common;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具
 *
 * @author kz
 * @date
 */
public class FormatUtil {

    /**
     * 中文字符
     */
    public final static Pattern chinese_pattern = Pattern.compile("^[\u4e00-\u9fa5]+$");

    private static final String SUFFIXES = "jpeg|gif|jpg|png|bmp|svg|webp";

    /**
     * 判断是否中文
     *
     * @param string
     * @return
     */
    public static boolean isChinese(String string) {
        if (StringUtils.isEmpty(string)) {
            return false;
        }
        return chinese_pattern.matcher(string).matches();
    }

    /**
     * 获取html文本中的第一张图片地址
     *
     * @param htmlContent
     * @return
     */
    public static String getFirstImageUrl(String htmlContent) {
        if (StringUtils.isNotBlank(htmlContent)) {
            Document document = Jsoup.parse(htmlContent);
            Element element = document.select("img[src~=(?i)\\.(gif|png|bmp|svg|jpe?g)]").first();
            if (null != element) {
                return element.attr("src");
            }
        }
        return "";
    }

    /**
     * 读取gif
     *
     * @param imageUrl 图片
     * @return
     */
    public static GifDecoder.GifImage readGif(String imageUrl) {
        GifDecoder.GifImage gif = null;
        try {
            gif = GifDecoder.read(getImageFromNetByUrl(imageUrl));
        } catch (IOException e) {
        }
        return gif;
    }

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            byte[] btImg = readInputStream(inStream);
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
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
     * 从html文本中获取图片
     *
     * @param htmlContent
     * @return
     */
    public static Elements getImages(String htmlContent) {
        if (StringUtils.isEmpty(htmlContent)) {
            return null;
        }

        Document document = Jsoup.parse(htmlContent);
        Elements elements = document.select("img[src~=(?i)\\.(gif|png|jpg|bmp|svg|jpe?g)]");
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        return elements;
    }

    /**
     * 获取图片属性
     *
     * @param imageUrl
     * @return
     * @throws IOException
     */
    public BufferedImage readImage(String imageUrl) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000).setConnectionRequestTimeout(1000).setSocketTimeout(1000).build();
        HttpGet httpGet = new HttpGet(imageUrl);
        httpGet.setConfig(requestConfig);
        HttpResponse response = httpClient.execute(httpGet);
        return ImageIO.read(response.getEntity().getContent());
    }

    /**
     * 截取图片文件名后缀名
     *
     * @param name
     * @return
     */
    public static String getSuffix(String name) {
        Pattern pat = Pattern.compile("[\\w]+[\\.](" + SUFFIXES + ")");
        Matcher mc = pat.matcher(name.toLowerCase());
        String fileName = null;
        while (mc.find()) {
            // 截取文件名后缀名
            fileName = mc.group();
        }
        if (fileName != null) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null;
    }

    /**
     * 从一段HTML中萃取纯文本
     *
     * @param html
     * @return
     */
    public static String getPlainText(String html) {
        if (StringUtils.isEmpty(html)) {
            return null;
        }
        return Jsoup.parse(html).text();
    }

    /**
     * 删除html中多余的空白
     *
     * @param html
     * @return
     */
    public static String trim_html(String html) {
        return StringUtils.replace(StringUtils.replace(html, "\r\n", ""), "\t", "");
    }

    public static void main(String[] args) {
        System.out.println(readGif("https://static.oschina.net/uploads/space/2017/0322/164434_Y8pi_2743772.gif"));
        System.out.println(readGif("https://static.oschina.net/uploads/space/2017/0403/213210_l5bz_2743772.gif"));
    }

}
