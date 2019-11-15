package net.oscer.beans;

import net.oscer.db.Entity;

import java.util.Date;

/**
 * <p>
 * 文件系统表
 * </p>
 *
 * @author kz
 * @since 2018-01-18
 */
public class SysFile extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private long user_id;
    /**
     * 文件名称
     */
    private String file_name;
    /**
     * 大小
     */
    private long file_size;
    /**
     * 七牛KEY名
     */
    private String file_key;
    /**
     * 后缀
     */
    private String file_suffix;
    /**
     * 1:图片 2:视频
     */
    private Integer file_type;
    /**
     * 路径
     */
    private String file_path;
    /**
     * 微信素材ID
     */
    private String media_id;
    /**
     * 文件上传成功状态（成功：0 失败 ：-1）
     */
    private Integer file_status;
    /**
     * 文件插入时间
     */
    private Date insert_date;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public String getFile_key() {
        return file_key;
    }

    public void setFile_key(String file_key) {
        this.file_key = file_key;
    }

    public String getFile_suffix() {
        return file_suffix;
    }

    public void setFile_suffix(String file_suffix) {
        this.file_suffix = file_suffix;
    }

    public Integer getFile_type() {
        return file_type;
    }

    public void setFile_type(Integer file_type) {
        this.file_type = file_type;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public Integer getFile_status() {
        return file_status;
    }

    public void setFile_status(Integer file_status) {
        this.file_status = file_status;
    }

    @Override
    public Date getInsert_date() {
        return insert_date;
    }

    @Override
    public void setInsert_date(Date insert_date) {
        this.insert_date = insert_date;
    }
}
