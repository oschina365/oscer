package net.oscer.beans;


import net.oscer.db.Entity;

/**
 * <p>
 * 动态表
 * </p>
 *
 * @author kz
 * @since 2019-10-25
 */
@Entity.Cache(region = "Dynamic")
public class Dynamic extends Entity {

    public static final Dynamic ME = new Dynamic();

    /**
     * 用户ID
     */
    private long user;
    /**
     * 对象ID，如帖子ID，评论ID
     */
    private long obj_id;
    /**
     * 对象类型，如帖子1,评论2
     */
    private int obj_type;
    /**
     * 显示状态，显示1，如用户被封号，就隐藏
     */
    private Integer status;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getObj_id() {
        return obj_id;
    }

    public void setObj_id(long obj_id) {
        this.obj_id = obj_id;
    }

    public int getObj_type() {
        return obj_type;
    }

    public void setObj_type(int obj_type) {
        this.obj_type = obj_type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
