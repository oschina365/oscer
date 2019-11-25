package net.oscer.beans;

import net.oscer.db.Entity;

/**
 * <p>
 * 私信详细表
 * </p>
 *
 * @author kz
 * @since 2019-11-19
 */
@Entity.Cache(region = "Msg")
public class Msg extends Entity {

    public final static Msg ME = new Msg();

    public static final int TYPE_SYSTEM = 0;
    public static final int TYPE_USER = 1;

    private long user;
    private long friend;

    /**
     * 发送者
     */
    private long sender;
    /**
     * 接收者
     */
    private long receiver;
    /**
     * 私信内容
     */
    private String content;

    /**
     * 私信类型（系统私信：0，个人私信：1）
     */
    private Integer type;
    /**
     * 私信来源（网页端，小程序）
     */
    private String source;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getFriend() {
        return friend;
    }

    public void setFriend(long friend) {
        this.friend = friend;
    }

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public long getReceiver() {
        return receiver;
    }

    public void setReceiver(long receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
