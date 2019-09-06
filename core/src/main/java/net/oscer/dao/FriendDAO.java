package net.oscer.dao;

import net.oscer.beans.Friend;
import net.oscer.db.CacheMgr;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 关注/粉丝
 *
 * @author kz
 * @create 2019-09-06 16:58
 **/
public class FriendDAO extends CommonDao<Friend> {

    public final static FriendDAO ME = new FriendDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public void save(long user, long friend) {
        try {
            Friend f = new Friend();
            f.setUser(user);
            f.setFriend(friend);
            f.save();
        } catch (Exception e) {
            String sql = "delete from friends where user=? and friend=?";
            getDbQuery().update(sql, user, friend);
        }

        evict(user);
        evict(friend);
    }

    public void evict(long user) {
        CacheMgr.evict(getCache_region(), "listFollows#" + user);
        CacheMgr.evict(getCache_region(), "listFans#" + user);
    }

    public void updateName(long id, long user, long friend, String name) {
    }

    public boolean followed(long user, long friend) {
        if (user <= 0L || friend <= 0L) {
            return false;
        }
        List<Friend> list = listFollows(user);
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        for (Friend f : list) {
            if (f != null && friend == f.getFriend()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 我的关注
     *
     * @param user
     * @return
     */
    public List<Friend> listFollows(long user) {
        String sql = "select * from friends where user=? order by id desc";
        return getDbQuery().query_cache(Friend.class, false, getCache_region(), "listFollows#" + user, sql, user);
    }

    /**
     * 我的粉丝
     *
     * @param user
     * @return
     */
    public List<Friend> listFans(long user) {
        String sql = "select * from friends where friend=? order by id desc";
        return getDbQuery().query_cache(Friend.class, false, getCache_region(), "listFans#" + user, sql, user);
    }
}
