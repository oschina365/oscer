package net.oscer.beans;

import net.oscer.db.Entity;

/**
 * <p>
 * 最后一条私信表
 * </p>
 *
 * @author kz
 * @since 2019-11-19
 */
@Entity.Cache(region = "LastMsg")
public class LastMsg extends Entity {

    public final static LastMsg ME = new LastMsg();

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
     * 私信ID
     */
    private long msg_id;

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

    public long getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(long msg_id) {
        this.msg_id = msg_id;
    }
}
