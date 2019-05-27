package net.oscer.beans;


import net.oscer.db.CacheMgr;
import net.oscer.db.Entity;

/**
 * <p>
 * 提醒表
 * </p>
 *
 * @author kz
 * @since 2018-07-27
 */
@Entity.Cache(region = "Remind")
public class Remind extends Entity {

    public static final Remind ME = new Remind();

    public static final int TYPE_BLOG = 1;
    public static final int TYPE_TWEET = 2;
    public static final int TYPE_REFER = 3;
    public static final int TYPE_REPLY = 4;

    public static final int STATUS_UN_READED = 0;
    public static final int STATUS_READED = 1;

    /**
     * 内容
     */
    private String content;
    /**
     * 类型（1:博客，2:动弹，3:提到别人，即@某人，4:回复某人）
     */
    private int type;
    /**
     * 对象ID
     */
    private long obj_id;

    /**
     * 主动提及的用户ID
     */
    private long sender;
    /**
     * 用户ID
     */
    private long refer;

    /**
     * 已读状态（0：未读，1：已读）
     */
    private int status_read;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getObj_id() {
        return obj_id;
    }

    public void setObj_id(long obj_id) {
        this.obj_id = obj_id;
    }

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public long getRefer() {
        return refer;
    }

    public void setRefer(long refer) {
        this.refer = refer;
    }

    public int getStatus_read() {
        return status_read;
    }

    public void setStatus_read(int status_read) {
        this.status_read = status_read;
    }

    public void saveRemind(String content, long sender, int type, long obj_id, long refer) {
        if (sender != refer) {
            if (null == selectExist(sender, type, obj_id, refer)) {
                Remind r = ME;
                r.setContent(content);
                r.setSender(sender);
                r.setObj_id(obj_id);
                r.setType(type);
                r.setRefer(refer);
                r.setId(0L);
                r.save();
                CacheMgr.evict(Entity.CACHE_HTML, "remind_" + refer);
            }
        }
    }

    public Number selectExist(long sender, int type, long obj_id, long refer) {
        String sql = "select id from reminds where sender=? and type=? and obj_id=? and refer=? and status_read=0";
        return ME.getId(ME, sql, sender, type, obj_id, refer);
    }
}
