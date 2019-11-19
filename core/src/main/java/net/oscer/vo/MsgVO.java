package net.oscer.vo;


import java.io.Serializable;

/**
 * 私信
 *
 * @author kz
 * @create 2019-11-19 18:39
 **/
public class MsgVO implements Serializable {

    /**
     * 发信人
     */
    private UserVO sender;

    /**
     * 收信人
     */
    private UserVO receiver;

    /**
     * 内容
     */
    private String content;

    /**
     * 发信时间
     */
    private String sdf_insert_date;

    public UserVO getSender() {
        return sender;
    }

    public void setSender(UserVO sender) {
        this.sender = sender;
    }

    public UserVO getReceiver() {
        return receiver;
    }

    public void setReceiver(UserVO receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSdf_insert_date() {
        return sdf_insert_date;
    }

    public void setSdf_insert_date(String sdf_insert_date) {
        this.sdf_insert_date = sdf_insert_date;
    }
}
