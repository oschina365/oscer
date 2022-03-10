package net.oscer.beans;

import net.oscer.db.Entity;

import java.util.Date;

/**
 * 照片
 *
 * @author kz
 * @create 2020-8-4 18:25:29
 **/
@Entity.Cache(region = "Photo")
public class Photo extends Entity {

    public static final Photo ME = new Photo();

    private long user;
    private String url;
    private String remark;
    private int year;
    private int month;
    private int day;
    private Date upload_time;
    private String type;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Date getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(Date upload_time) {
        this.upload_time = upload_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
