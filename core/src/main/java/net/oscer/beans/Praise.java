package net.oscer.beans;


import net.oscer.db.Entity;

/**
 * <p>
 * 点赞详细表
 * </p>
 *
 * @author kz
 * @since 2018-04-11
 */
@Entity.Cache(region = "Praise")
public class Praise extends Entity {

    public static final Praise ME = new Praise();

    /**
     * 点赞
     */
    public static final int PRAISE_YES = 1;
    /**
     * 取消点赞
     */
    public static final int PRAISE_NO = 2;

    /**
     * 对象ID
     */
    private long obj_id;
    /**
     * 用户ID
     */
    private long user_id;
    /**
     * 1：点赞 2：不点赞
     */
    private Integer status;

    /**
     * 1：博客 2：动弹 3：评论 4：帖子,TypePraiseEnum
     */
    private Integer type;

    public long getObj_id() {
        return obj_id;
    }

    public void setObj_id(long obj_id) {
        this.obj_id = obj_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
