package net.oscer.beans;


import net.oscer.db.Entity;
import net.oscer.framework.FormatTool;

/**
 * <p>
 * 动弹表
 * </p>
 *
 * @author kz
 * @since 2018-02-11
 */
@Entity.Cache(region = "Tweet")
public class Tweet extends Basic {

    public static final Tweet ME = new Tweet();

    /**
     * 用户开放的动弹
     */
    public static final String CACHE_COUNT_USER_OPEN_TWEET = "count_user_open_tweet";

    /**
     * 用户自己查看自己的动弹
     */
    public static final String CACHE_COUNT_USER_TWEET = "count_user_tweet";

    /**
     * 热门动弹ID集合
     */
    public static final String CACHE_HOT_TWEET_IDS = "ids_hot_tweet";

    /**
     * 用户ID
     */
    private long user_id;

    /**
     * 内容（如：文字，表情等）
     */
    private String content;
    /**
     * 状态（-1：用户隐藏， 0：系统隐藏， 1：显示）
     */
    private Integer status;

    /**
     * 缩略图1（质量高点）
     */
    private String photos;

    /**
     * 缩略图2 （质量低点）
     */
    private String thumbs;
    /**
     * 格式化时间
     */
    public String insert_date_str;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getThumbs() {
        return thumbs;
    }

    public void setThumbs(String thumbs) {
        this.thumbs = thumbs;
    }

    public String getInsert_date_str() {
        return FormatTool.format_intell_time(getInsert_date());
    }

    public void setInsert_date_str(String insert_date_str) {
        this.insert_date_str = FormatTool.format_intell_time(getInsert_date());
    }

    /**
     * 当前登录用户是否点赞该动弹
     *
     * @param g_use_id
     * @return
     */
    public boolean gUserHasPraise(long g_use_id) {
        if (g_use_id <= 0L || this == null) {
            return false;
        }

        /*Praise detail = PraiseDAO.ME.selectOneByTypeObjUserId(TypePraiseEnum.VALUE.TWEET.getKey(), this.getId(), g_use_id);
        if (detail == null || detail.getStatus() == Praise.PRAISE_NO) {
            return false;
        }*/
        return true;
    }
}
