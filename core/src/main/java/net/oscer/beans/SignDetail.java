package net.oscer.beans;

import net.oscer.db.Entity;

/**
 * <p>
 * 签到详情表
 * </p>
 *
 * @author kz
 * @since 2019-05-29
 */
@Entity.Cache(region = "SignDetail")
public class SignDetail extends Entity {

    public static final SignDetail ME = new SignDetail();

    /**
     * 用户ID
     */
    private long user;
    /**
     * 签到时间（如：20190529）
     */
    private Integer sign_day;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public Integer getSign_day() {
        return sign_day;
    }

    public void setSign_day(Integer sign_day) {
        this.sign_day = sign_day;
    }
}
