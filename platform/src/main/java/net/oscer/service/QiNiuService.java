package net.oscer.service;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import net.oscer.api.vo.UploadResultVO;
import net.oscer.beans.Photo;
import net.oscer.beans.SysFile;
import net.oscer.common.*;
import net.oscer.enums.QiNiuEnum;
import net.oscer.enums.ResultEnum;
import net.oscer.framework.FormatTool;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * 七牛上传辅助类
 *
 * @author kz
 */
public class QiNiuService {
    private static Logger logger = LoggerFactory.getLogger(QiNiuService.class);
    private static String domain = StringUtils.EMPTY;

    /**
     * 图片超过2MB加上廋身参数
     */
    private static final long NEED_IMAGE_SLIM = 1024 * 1024 * 2;

    private static final String IMAGE_SLIM_NAME = "?imageslim";


    public static String url(String filename) {
        try {
            if (StringUtils.isBlank(domain)) {
                domain = net.oscer.framework.ConfigTool.getProp("qiniu.domain");
            }
            return "http://" + domain + "/" + new URLCodec("UTF-8").encode(filename).replace("+", "%20");
        } catch (Exception e) {
            logger.error("QiNiuService@url error", e);
            return StringUtils.EMPTY;
        }
    }

    public void run(File file, SysFile sysFile) {
        Thread upThread = new Thread(new UploadProcess(file, sysFile));
        upThread.start();
        logger.info("QiNiu file-Thread[" + upThread.getId() + "] begin on");
    }


    public static boolean upload(File file, SysFile sysFile) {
        try {
            JSONObject jo = QiNiuApi.upload(file);
            save(jo, sysFile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("uploadProcess@QiNiuUpload save model error", e);
        }

        return false;
    }

    public static JSONObject uploadFileByte(byte[] fileByte, String fileKey) {
        JSONObject result = ResultUtil.getSuccessResult("上传成功");
        try {
            result = QiNiuApi.uploadFileByte(fileByte, fileKey);
            return result;
        } catch (Exception e) {
            result.put("code", ResultEnum.STATUS.FAIL.getCode());
            logger.error("uploadProcess@QiNiuUpload save model error", e);
        }

        return result;
    }

    public static JSONObject uploadFileByte(byte[] fileByte, String fileKey, SysFile sysFile) {
        JSONObject result = ResultUtil.getSuccessResult("上传成功");
        try {
            result = QiNiuApi.uploadFileByte(fileByte, fileKey);
            save(result, sysFile);
            return result;
        } catch (Exception e) {
            result.put("code", ResultEnum.STATUS.FAIL.getCode());
            logger.error("uploadProcess@QiNiuUpload save model error", e);
        }

        return result;
    }

    public static void save(JSONObject jo, SysFile sysFile) {
        if ("success".equals(jo.getString("result_code"))) {
            String key = jo.getString("key");
            sysFile.setFile_status(QiNiuEnum.STATUS.SUCCESS.getKey());
            sysFile.setFile_suffix(FileUtil.getFileSuffix(key));
            sysFile.setInsert_date(new Date());
            logger.info("[upload]上传结果返回-->key:" + key);
        } else {
            logger.info("[upload]上传失败");
            sysFile.setFile_status(QiNiuEnum.STATUS.FAIL.getKey());
        }
        sysFile.save();

    }

    public static void save(JSONObject jo, Photo photo) {
        if ("success".equals(jo.getString("result_code"))) {
            String key = jo.getString("key");
            logger.info("[upload]上传结果返回-->key:" + key);
        } else {
            logger.info("[upload]上传失败");
        }
        photo.save();
    }

    class UploadProcess implements Runnable {
        private File file;
        private SysFile sysFile;

        public UploadProcess(File file, SysFile sysFile) {
            this.file = file;
            this.sysFile = sysFile;
        }

        @Override
        public void run() {
            upload(file, sysFile);
        }
    }


    /**
     * 移动文件，要求空间在同一账号下
     *
     * @param fromBucket  源空间名称
     * @param fromFileKey 源文件名称
     * @param toBucket    目的空间名称
     * @param toFileKey   目的文件名称
     * @param force       强制覆盖空间中已有同名（和 toFileKey 相同）的文件
     * @throws QiniuException
     */
    public static void moveFile(String fromBucket, String fromFileKey, String toBucket, String toFileKey, boolean force) throws QiniuException {
        QiNiuApi.moveFile(fromBucket, fromFileKey, toBucket, toFileKey, force);
    }

    /**
     * 删除指定空间、文件名的文件
     *
     * @param bucket 空间名称
     * @param key    文件名称
     * @throws QiniuException
     * @link http://developer.qiniu.com/kodo/api/delete
     */
    public static void delete(String bucket, String key) throws QiniuException {
        QiNiuApi.delete(bucket, key);
    }

    public static UploadResultVO pic(MultipartFile multipartFile, long userId) throws IOException {
        if (userId <= 0L) {
            return UploadResultVO.failWith("请先登录");
        }
        long begin = System.currentTimeMillis();
        if (multipartFile.isEmpty()) {
            return UploadResultVO.failWith("图片为空");
        }
        String originFileName = multipartFile.getOriginalFilename();
        if (!StringUtils.contains(SystemConstant.QINIU_SUFFIXIMAGE, FileUtil.getFileSuffix(originFileName).toUpperCase())) {
            return UploadResultVO.failWith("图片类型不对");
        }
        long fileSize = multipartFile.getSize();
        String newFileName = userId + SystemConstant.QINIU_SLASH + originFileName;
        SysFile sysFile = new SysFile();
        sysFile.setUser_id(userId);
        sysFile.setFile_type(0);
        sysFile.setFile_name(originFileName);
        sysFile.setFile_size(fileSize);
        if (fileSize > NEED_IMAGE_SLIM) {
            sysFile.setFile_key(originFileName + IMAGE_SLIM_NAME);
        }
        JSONObject result = new JSONObject();
        try {
            result = uploadFileByte(multipartFile.getBytes(), newFileName, sysFile);
        } catch (QiniuException q) {
            new QiNiuApi(net.oscer.framework.ConfigTool.getProp("qiniu.access"), net.oscer.framework.ConfigTool.getProp("qiniu.secret"), net.oscer.framework.ConfigTool.getProp("qiniu.bucket"));
            logger.info("##########七牛重新连接成功##########");
            result = uploadFileByte(multipartFile.getBytes(), newFileName, sysFile);
        }
        logger.info("本次上传耗时：{}ms,文件名：{}，上传完毕时间：{}", (System.currentTimeMillis() - begin), originFileName, DateUtil.format(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        return UploadResultVO.successWith(net.oscer.framework.ConfigTool.getProp("qiniu.domain"), "上传成功", result.getString("key"), fileSize, originFileName, null);
    }

    public static UploadResultVO photo(MultipartFile multipartFile, long userId) throws IOException {
        if (userId <= 0L) {
            return UploadResultVO.failWith("请先登录");
        }
        long begin = System.currentTimeMillis();
        if (multipartFile.isEmpty()) {
            return UploadResultVO.failWith("图片为空");
        }
        String originFileName = multipartFile.getOriginalFilename();
        if (!StringUtils.contains(SystemConstant.QINIU_SUFFIXIMAGE, FileUtil.getFileSuffix(originFileName).toUpperCase())) {
            return UploadResultVO.failWith("图片类型不对");
        }
        long fileSize = multipartFile.getSize();
        String newFileName = userId + SystemConstant.QINIU_SLASH + "photo" + SystemConstant.QINIU_SLASH + originFileName;
        Photo p = new Photo();
        Date now = new Date();
        p.setUser(userId);
        p.setUpload_time(now);
        Map<String, Object> map = FormatTool.getTime(now);
        p.setYear((Integer) map.get("year"));
        p.setMonth((Integer) map.get("month"));
        p.setDay((Integer) map.get("day"));
        JSONObject result = new JSONObject();
        try {
            result = uploadFileByte(multipartFile.getBytes(), newFileName);
        } catch (QiniuException q) {
            new QiNiuApi(net.oscer.framework.ConfigTool.getProp("qiniu.access"), net.oscer.framework.ConfigTool.getProp("qiniu.secret"), net.oscer.framework.ConfigTool.getProp("qiniu.bucket"));
            logger.info("##########七牛重新连接成功##########");
            result = uploadFileByte(multipartFile.getBytes(), newFileName);
        }
        String domain = net.oscer.framework.ConfigTool.getProp("qiniu.domain");
        String key = result.getString("key");
        if (key != null) {
            p.setUrl("http://" + domain + "/" + key);
            p.save();
        }
        logger.info("本次上传耗时：{}ms,文件名：{}，上传完毕时间：{}", (System.currentTimeMillis() - begin), originFileName, DateUtil.format(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));
        return UploadResultVO.successWith(domain, "上传成功", key, fileSize, originFileName, null);
    }


    /**
     * 切图
     *
     * @param src
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public BufferedImage cropImg(BufferedImage src, int x, int y, int w, int h) {
        ImageFilter cropFilter = new CropImageFilter(x, y, w, h);
        Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(src.getSource(), cropFilter));
        BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = newImg.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return newImg;
    }

    /**
     * 获取图片名称
     *
     * @param url
     * @return
     */
    public String getImageName(String url) {
        int lastindex = url.lastIndexOf("/");
        String imgName = url.substring(lastindex + 1);
        return imgName;

    }

    /**
     * 获取图片类型
     *
     * @param url
     * @return
     */
    public String getImageType(String url) {
        int lastindex = url.lastIndexOf("/");
        String imgName = url.substring(lastindex + 1);
        int pointIndex = imgName.indexOf(".");
        return imgName.substring(pointIndex + 1);
    }

    public String uploadFromThird(String content, String userId) {
        Elements images = FormatUtil.getImages(content);
        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                String imageUrl = images.get(i).attr("src");
                String key = userId + SystemConstant.SEPARATOR_UNDERLINE + DateUtil.format(new Date(), DateUtil.YYYYMMDDHHMMSS) + SystemConstant.SEPARATOR_UNDERLINE + i + SystemConstant.SEPARATOR_DOT + getImageType(imageUrl);
                JSONObject result = uploadFileByte(FormatUtil.getImageFromNetByUrl(imageUrl), key);
                content = content.replaceAll("\\" + imageUrl, "http://" + net.oscer.framework.ConfigTool.getProp("qiniu.domain") + "/" + result.getString("key"));
            }
        }
        return content;
    }

}
