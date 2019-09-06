package net.oscer.beans;

import net.oscer.db.Entity;

import java.io.Serializable;

/**
 * 关注/粉丝 表
 *
 * @author kz
 * @create 2019-09-06 16:56
 **/
@Entity.Cache(region = "Friend")
public class Friend extends Entity implements Serializable {

    public final static Friend ME = new Friend();

    private long user;
    private long friend;
    /**
     * 备注
     */
    private String name;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getFriend() {
        return friend;
    }

    public void setFriend(long friend) {
        this.friend = friend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
