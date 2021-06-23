package net.oscer.vo;


import net.oscer.beans.LastMsg;
import net.oscer.beans.Msg;
import net.oscer.beans.User;
import net.oscer.framework.FormatTool;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private int count;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static List<MsgVO> construct(List<LastMsg> list) {
        return CollectionUtils.isEmpty(list) ? null : list.stream().map(MsgVO::construct).collect(Collectors.toList());
    }

    public static MsgVO construct(LastMsg msg) {
        MsgVO vo = new MsgVO();
        User sender = User.ME.get(msg.getSender());
        User receiver = User.ME.get(msg.getReceiver());
        vo.setSender(UserVO.convert(sender));
        vo.setReceiver(UserVO.convert(receiver));
        vo.setContent(msg.getContent());
        vo.setSdf_insert_date(FormatTool.format_intell_time(msg.getInsert_date()));
        vo.setCount(msg.getCount());
        return vo;
    }

    public static List<MsgVO> construct_msg(List<Msg> list) {
        return CollectionUtils.isEmpty(list) ? null : list.stream().map(MsgVO::construct).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static MsgVO construct(Msg m) {
        if (m == null) {
            return null;
        }
        MsgVO vo = new MsgVO();
        User sender = User.ME.get(m.getSender());
        if (sender == null) {
            return null;
        }
        User receiver = User.ME.get(m.getReceiver());
        if (receiver == null) {
            return null;
        }
        vo.setSender(UserVO.convert(sender));
        vo.setReceiver(UserVO.convert(receiver));
        vo.setContent(m.getContent());
        vo.setSdf_insert_date(FormatTool.format_intell_time(m.getInsert_date()));
        return vo;
    }
}
