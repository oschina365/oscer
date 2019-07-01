package net.oscer.beans;

import net.oscer.db.Entity;
import net.oscer.framework.FormatTool;

import java.util.Date;

/**
 * <p>
 * 帖子收藏
 * </p>
 *
 * @author kz
 * @since 2019-07-01
 */
@Entity.Cache(region = "CollectQuestion")
public class CollectQuestion extends Entity {

    public final static CollectQuestion ME = new CollectQuestion();

    /**
     * 收藏
     */
    public static final int STATUS_SHOW = 0;

    /**
     * 不收藏
     */
    public static final int STATUS_HIDE = 1;
    /**
     * 用户ID
     */
    private long user;
    /**
     * 帖子ID
     */
    private long question;

    /**
     * 状态（0：收藏 1：不收藏）
     */
    private Integer status;

    /**
     * 添加时间
     */
    public Date insert_date;
    /**
     * 更新时间
     */
    public Date last_date;

    private String sdf_insert_date;

    private String sdf_last_date;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getQuestion() {
        return question;
    }

    public void setQuestion(long question) {
        this.question = question;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public Date getInsert_date() {
        return insert_date;
    }

    @Override
    public void setInsert_date(Date insert_date) {
        this.insert_date = insert_date;
    }

    @Override
    public Date getLast_date() {
        return last_date;
    }

    @Override
    public void setLast_date(Date last_date) {
        this.last_date = last_date;
    }

    public String getSdf_insert_date() {
        return sdf_insert_date;
    }

    public void setSdf_insert_date(String sdf_insert_date) {
        this.sdf_insert_date = sdf_insert_date;
    }

    public String getSdf_last_date() {
        return sdf_last_date;
    }

    public void setSdf_last_date(String sdf_last_date) {
        this.sdf_last_date = sdf_last_date;
    }

    public String format_insert_date() {
        return FormatTool.format_intell_time(this.insert_date);
    }

    public String format_last_date() {
        return FormatTool.format_intell_time(this.last_date);
    }


}
