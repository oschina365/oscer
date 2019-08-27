package net.oscer.beans;



import net.oscer.db.Entity;

import java.util.Date;

/**
 * <p>
 * 博客主表
 * </p>
 *
 * @author kz
 * @since 2018-02-10
 */
@Entity.Cache(region = "Blog")
public class Blog extends Basic {

    public static final Blog ME = new Blog();

    /**
     * 发布博客间隔时间
     */
    public static final long PUSH_BLOG_INTERVAL_TIME = 1000 * 60 * 5L;

    /**
     * 登录用户当天所发表的博客数量
     */
    public final static int USER_EVERY_DAY_MAX_PUSH_BLOG_COUNT = 50;

    public static final String CACHE_LAST_BLOG = "user_last_blog";

    /**
     * 用户开放的博客
     */
    public static final String CACHE_COUNT_USER_OPEN_BLOG = "count_user_open_blog";

    /**
     * 用户自己查看自己的博客
     */
    public static final String CACHE_COUNT_USER_BLOG = "count_user_blog";

    /**
     * 摘要（截取于内容，传文本90位）
     */
    public static final int BLOG_ABSTRACTS_MAX_LENGTH = 90;

    /**
     * 用户ID
     */
    private long user_id;
    /**
     * 博客标题
     */
    private String title;
    /**
     * 博客摘要
     */
    private String abstracts;
    /**
     * 博客封面
     */
    private String banner;
    /**
     * 是否对平台开放(0:否 1：是)
     */
    private Integer openup;
    /**
     * 状态（-1：用户隐藏， 0：系统隐藏， 1：显示）
     */
    private Integer status;

    /**
     * 推荐：数字越大越在前，默认1
     */
    private Integer recommend_type;

    /**
     * 添加推荐的时间
     */
    private Date recommend_date;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Integer getOpenup() {
        return openup;
    }

    public void setOpenup(Integer openup) {
        this.openup = openup;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRecommend_type() {
        return recommend_type;
    }

    public void setRecommend_type(Integer recommend_type) {
        this.recommend_type = recommend_type;
    }

    public Date getRecommend_date() {
        return recommend_date;
    }

    public void setRecommend_date(Date recommend_date) {
        this.recommend_date = recommend_date;
    }

    /**
     * 当前登录用户是否点赞该博客
     *
     * @param g_use_id
     * @return
     */
    public boolean gUserHasPraise(long g_use_id) {
        if (g_use_id <= 0L || this == null) {
            return false;
        }

        /*Praise detail = PraiseDAO.ME.selectOneByTypeObjUserId(TypePraiseEnum.VALUE.BLOG.getKey(), this.getId(), g_use_id);
        if (detail == null || detail.getStatus() == Praise.PRAISE_NO) {
            return false;
        }*/
        return true;
    }

}
