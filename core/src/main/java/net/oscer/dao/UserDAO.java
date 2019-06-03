package net.oscer.dao;

import net.oscer.beans.User;
import net.oscer.db.CacheMgr;
import net.oscer.framework.LinkTool;
import net.oscer.framework.StringUtils;

public class UserDAO extends CommonDao<User> {

    public static final UserDAO ME = new UserDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 查询用户
     *
     * @param id
     * @return
     */
    public User getById(long id) {
        return User.ME.get(id);
    }

    public User getByIdent(String ident) {
        if (StringUtils.isBlank(ident)) {
            return null;
        }
        String sql = "select id from users where ident =?";
        Number n = getDbQuery().read(Number.class, sql, ident);
        if (n == null || n.longValue() <= 0L) {
            return null;
        }
        return User.ME.get(n.longValue());
    }

    /**
     * 查询用户
     *
     * @param str
     * @return
     */
    public User getUser(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        User user = null;
        user = getByUsername(str);
        if (null != user) {
            return user;
        }
        user = getByNickname(str);
        if (null != user) {
            return user;
        }
        user = getByname(str);
        if (null != user) {
            return user;
        }
        return null;
    }

    /**
     * 根据用户名(注册名)查询
     *
     * @param username
     * @return
     */
    public User getByUsername(String username) {
        String sql = "select id from users where username=?";
        return User.ME.getById(User.ME, sql, username);
    }

    /**
     * 根据用户个性名查询
     *
     * @param nickname
     * @return
     */
    public User getByNickname(String nickname) {
        String sql = "select id from users where nickname=?";
        return User.ME.getById(User.ME, sql, nickname);
    }

    /**
     * 根据用户真实名查询
     *
     * @param name
     * @return
     */
    public User getByname(String name) {
        String sql = "select id from users where name=?";
        return User.ME.getById(User.ME, sql, name);
    }

    /**
     * 用户空间链接
     *
     * @param id
     * @return
     */
    public String user_link(long id) {
        if (id <= 0L) {
            return null;
        }
        String root = LinkTool.root();
        StringBuilder sb = new StringBuilder(root);
        if (root.length() > 0 && root.charAt(0) != '/') {
            sb.append('/');
        }
        User u = User.ME.get(id);
        if (StringUtils.isNotBlank(u.getIdent())) {
            sb.append("u/" + u.getIdent());
        } else {
            sb.append("u/" + id);
        }

        return sb.toString();
    }

    /**
     * 查询今天已签到的人数
     *
     * @return
     */
    public int count_signed_today() {
        String sql = "select count(id) from users where score_today >0";
        return getDbQuery().stat_cache(getCache_region(), "count_signed_today", sql);
    }

    public void evict_count_signed_today() {
        CacheMgr.evict(getCache_region(), "count_signed_today");
    }

    /**
     * 重置今日获得积分字段
     */
    public void resetScoreToday() {
        String sql = "update users set score_today=0";
        getDbQuery().update(sql);
        evict_count_signed_today();
    }

}
