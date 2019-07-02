package net.oscer.framework;

import net.coobird.thumbnailator.Thumbnails;
import net.oscer.common.ApiResult;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadUtils {

    /**
     * 图片类型
     */
    public static final int IMAGE_TYPE_TWEET = 1;//动弹
    public static final int IMAGE_TYPE_BLOG = 2;//博客
    public static final int IMAGE_TYPE_COMMENT = 3;//评论
    public static final int IMAGE_TYPE_COMMENT_REPLY = 4;
    public static final int IMAGE_TYPE_ALBUM = 5;//用户相册
    public static final int IMAGE_TYPE_USER_HEADIMG = 6;//用户头像

    public static final Map<String, String> map = new HashMap<>();

    static {
        map.put("gif", "gif");
        map.put("ico", "ico");
    }

    /**
     * 上传图片
     *
     * @param image_type    图片类型
     * @param user_id       用户ID
     * @param imgFile       原文件
     * @param destFile      图片保存路径
     * @param thumbFileName
     * @return
     */
    public static ApiResult uploadImage(int image_type, long user_id, File imgFile, File destFile, File thumbFileName) {
        try {
            FileUtils.copyFile(imgFile, destFile);
            if (map.get(ImageUtils.getSuffix(imgFile.getName()).toLowerCase()) == null) {
                Thumbnails.of(destFile).scale(1f).outputQuality(0.5f).toFile(destFile);
                Thumbnails.of(destFile).scale(1f).outputQuality(0.35f).toFile(thumbFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResult.success();
    }
}
