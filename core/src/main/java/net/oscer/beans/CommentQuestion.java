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

    /**
     * 帖子ID
     */
    private Integer question;
    /**
     * 评论者ID
     */
    private Integer user;
    /**
     * 回复评论者ID
     */
    private Integer reply_user;
    /**
     * 评论的内容
     */
    private String content;
    /**
     * 状态（0：系统冻结 1：正常显示）
     */
    private Integer status;
    /**
     * 点赞数量
     */
    private Integer praise_count;

    public Integer getQuestion() {
        return question;
    }

    public void setQuestion(Integer question) {
        this.question = question;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getReply_user() {
        return reply_user;
    }

    public void setReply_user(Integer reply_user) {
        this.reply_user = reply_user;
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
