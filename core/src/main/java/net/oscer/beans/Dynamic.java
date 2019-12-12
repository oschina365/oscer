package net.oscer.beans;


import net.oscer.db.Entity;

/**
 * <p>
 * 动态表
 * </p>
 *
 * @author kz
 * @since 2019-10-25
 */
@Entity.Cache(region = "Dynamic")
public class Dynamic extends Entity {

    public static final Dynamic ME = new Dynamic();

    /**
     * 用户ID
     */
    private long user;
    /**
     * 帖子ID
     */
    private long question;

    /**
     * 显示状态，正常显示为0，如用户被封号,隐私帖子，就隐藏
     * 隐私帖子：1，用户被封号：2
     */
    private Integer status;

    /**
     * 评论ID
     */
    private long comment;

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

    public long getComment() {
        return comment;
    }

    public void setComment(long comment) {
        this.comment = comment;
    }
}
