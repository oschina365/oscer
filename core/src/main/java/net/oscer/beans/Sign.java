package net.oscer.beans;

import net.oscer.db.Entity;

import java.util.Date;

/**
 * <p>
 * 签到表
 * </p>
 *
 * @author kz
 * @since 2019-05-29
 */
@Entity.Cache(region = "Sign")
public class Sign extends Entity {

    public static final Sign ME = new Sign();

    /**
     * 用户ID
     */
    private Integer user;
    /**
     * 签到的年数，比如今年是2019年，那这个值就是2019
     */
    private Integer sign_year;
    /**
     * 最后一次签到时间
     */
    private Date last_sign_day;
    /**
     * 总共累计签到多少天
     */
    private Integer total_count;
    /**
     * 连续签到多少天，中断签到则为0
     */
    private Integer series_count;

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getSign_year() {
        return sign_year;
    }

    public void setSign_year(Integer sign_year) {
        this.sign_year = sign_year;
    }

    public Date getLast_sign_day() {
        return last_sign_day;
    }

    public void setLast_sign_day(Date last_sign_day) {
        this.last_sign_day = last_sign_day;
    }

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Integer getSeries_count() {
        return series_count;
    }

    public void setSeries_count(Integer series_count) {
        this.series_count = series_count;
    }
}
