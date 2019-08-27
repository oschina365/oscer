package net.oscer.beans;


import net.oscer.db.Entity;

@Entity.Cache(region = "Visit")
public class Visit extends Entity {

    public static final Visit ME = new Visit();

    public static final int USER =1;
    public static final int BLOG =2;
    public static final int TWEET =3;
    public static final int QUESTION =4;

    /**
     * 用户ID
     */
    private long user_id;
    /**
     * 对象
     */
    private long obj_id;
    /**
     * 对象类型（1:user,2:blog,3:tweet 4:question）
     */
    private int obj_type;


    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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
}
