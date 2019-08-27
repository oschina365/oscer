package net.oscer.beans;

import net.oscer.db.Entity;

/**
 * <p>
 * 帖子评论表
 * </p>
 *
 * @author kz
 * @since 2019-05-29
 */
@Entity.Cache(region = "CommentQuestion")
public class CommentQuestion extends Entity {

    public static final CommentQuestion ME = new CommentQuestion();

    /**
     * 帖子ID
     */
    private long question;
    /**
     * 评论者ID
     */
    private long user;
    /**
     * 回复D
     */
    private long parent;
    /**
     * 评论的内容
     */
    private String content;
    /**
     * 状态（1：系统冻结 0：正常显示）
     */
    private Integer status;
    /**
     * 点赞数量
     */
    private Integer praise_count;

    public long getQuestion() {
        return question;
    }

    public void setQuestion(long question) {
        this.question = question;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getParent() {
        return parent;
    }

    public void setParent(long parent) {
        this.parent = parent;
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

    public Integer getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(Integer praise_count) {
        this.praise_count = praise_count;
    }
}
