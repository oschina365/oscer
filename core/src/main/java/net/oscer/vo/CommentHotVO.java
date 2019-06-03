package net.oscer.vo;

import net.oscer.beans.User;

/**
 * 回帖
 *
 * @author kz
 * @create 2019-05-31 18:29
 **/
public class CommentHotVO {

    /**
     * 评论用户ID
     */
    private long user;

    /**
     * 评论用户
     */
    private User cu;

    /**
     * 回答次数
     */
    private int comment_count;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public User getCu() {
        return cu;
    }

    public void setCu(User cu) {
        this.cu = cu;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }
}
