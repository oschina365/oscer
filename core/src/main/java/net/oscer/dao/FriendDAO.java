package net.oscer.dao;

import net.oscer.beans.Friend;
import net.oscer.beans.User;
import net.oscer.db.CacheMgr;
import net.oscer.framework.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

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
        CacheMgr.evict(getCache_region(), "pageFollows#" + user);
        CacheMgr.evict(getCache_region(), "countFollow#" + user);
        CacheMgr.evict(getCache_region(), "pageFans#" + user);
        CacheMgr.evict(getCache_region(), "countFan#" + user);
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

    public int countFollow(long user) {
        String sql = "select count(*) from friends where user=? ";
        return getDbQuery().stat_cache(getCache_region(), "countFollow#" + user, sql, user);
    }

    /**
     * 我的关注,分页
     *
     * @param user
     * @return
     */
    public List<User> pageFollows(long user, int page, int size) {
        String sql = "select friend from friends where user=? order by id desc";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "pageFollows#" + user, 30, sql, page, size, user);
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        return User.ME.loadList(ids);
    }

    public int countFan(long user) {
        String sql = "select count(*) from friends where friend=? ";
        return getDbQuery().stat_cache(getCache_region(), "countFan#" + user, sql, user);
    }

    /**
     * 我的粉丝,分页
     *
     * @param user
     * @return
     */
    public List<User> pageFans(long user, int page, int size) {
        String sql = "select user from friends where friend=? order by id desc";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "pageFans#" + user, 30, sql, page, size, user);
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        return User.ME.loadList(ids);
    }

    public List<Long> listFans(long user, int page, int size) {
        String sql = "select user from friends where friend=? order by id desc";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "pageFans#" + user, 30, sql, page, size, user);
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        sql = "select friend from friends where user =? and friend in (" + StringUtils.join(ids, ",") + ")";
        return getDbQuery().query(long.class, sql, user);

    }
}


