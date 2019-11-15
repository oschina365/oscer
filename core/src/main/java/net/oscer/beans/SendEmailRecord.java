package net.oscer.beans;


import net.oscer.db.Entity;

/**
 * <p>
 * 发送邮箱记录表
 * </p>
 *
 * @author kz
 * @since 2018-01-29
 */
@Entity.Cache(region = "SendEmailRecord")
public class SendEmailRecord extends Entity {

    public static final SendEmailRecord ME = new SendEmailRecord();

    public static final long SEND_REGISTER_INTERVAL_MIN = 30L;

    /**
     * 发布注册邮件间隔时间
     */
    public static final long SEND_REGISTER_INTERVAL_TIME = 1000 * 60 * SEND_REGISTER_INTERVAL_MIN;

    /**
     * 发送类型(admin:后台系统 web:用户系统 user:用户)
     */
    public static final String TYPE_ADMIN = "admin";
    public static final String TYPE_WEB = "web";
    public static final String TYPE_USER = "user";

    /**
     * 发送人的邮箱
     */
    private String send_email;
    /**
     * 收件人的邮箱
     */
    private String receive_email;
    /**
     * 发送类型(admin:后台系统 web:用户系统 user:用户)
     */
    private String type;
    /**
     * 发送者ID（0表示系统）
     */
    private long sender;
    /**
     * 接受者ID
     */
    private long receiver;

    /**
     * 发送的内容
     */
    private String text;

    /**
     * 发送的主题
     */
    private String subject;

    /**
     * 邮件模板类型
     */
    private String email_type;

    public String getSend_email() {
        return send_email;
    }

    public void setSend_email(String send_email) {
        this.send_email = send_email;
    }

    public String getReceive_email() {
        return receive_email;
    }

    public void setReceive_email(String receive_email) {
        this.receive_email = receive_email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmail_type() {
        return email_type;
    }

    public void setEmail_type(String email_type) {
        this.email_type = email_type;
    }
}
