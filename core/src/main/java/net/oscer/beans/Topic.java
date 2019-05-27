package net.oscer.beans;


import net.oscer.db.Entity;

/**
 * 话题
 *
 * @author kz
 * @date 2019/01/23
 */
@Entity.Cache(region = "Topic")
public class Topic extends Entity {

    public static final Topic ME = new Topic();

    /**
     * 话题名称
     */
    private String name;
    /**
     * 动弹ID
     */
    private long tweet_id;
    /**
     * 排序(越大越靠前)
     */
    private int sort;
    /**
     * 是否显示（1：不显示）
     */
    private int status;
    /**
     * 用户ID
     */
    private long user_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(long tweet_id) {
        this.tweet_id = tweet_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
