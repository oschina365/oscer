package net.oscer.beans;


import net.oscer.db.Entity;

/**
 * <p>
 * 邮件模板表
 * </p>
 *
 * @author kz
 * @since 2018-03-08
 */
@Entity.Cache(region = "SendEmailTemplate")
public class SendEmailTemplate extends Entity {

    public static final SendEmailTemplate ME = new SendEmailTemplate();

    /**
     * 邮件标题
     */
    private String title;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 模板类型
     */
    private String type;
    /**
     * 0：正常 1：删除
     */
    private Integer status;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
