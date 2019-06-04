package net.oscer.beans;


import net.oscer.db.Entity;

/**
 * 第三方账号关系表
 *
 * @author kz
 * @create 2019-04-12 17:40
 **/
@Entity.Cache(region = "UserBind")
public class UserBind extends Entity {

    public final static UserBind ME = new UserBind();

    private long user;
    private String provider;
    private String name;
    private String email;
    private String headimg;
    /**
     * 1：正常 2禁用
     */
    private int status;
    private String from;
    private String union_id;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUnion_id() {
        return union_id;
    }

    public void setUnion_id(String union_id) {
        this.union_id = union_id;
    }
}
