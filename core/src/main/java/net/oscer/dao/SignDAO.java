package net.oscer.dao;

import net.oscer.beans.Sign;

import java.util.List;

/**
 * 签到
 *
 * @author kz
 * @create 2019-05-30 11:11
 **/
public class SignDAO extends CommonDao<Sign> {

    public static final SignDAO ME = new SignDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public Sign selectByUser(long user) {
        if (user <= 0L) {
            return null;
        }
        String sql = "select * from signs where user=?";
        Sign s = getDbQuery().read_cache(Sign.class, isCacheNullObject(), getCache_region(), String.valueOf(user), sql, user);
        return s == null ? new Sign() : s;
    }

    /**
     * 活跃签到榜
     *
     * @return
     */
    public List<Sign> active_signs() {
        String sql = "select id from signs where series_count > 0 order by series_count desc limit 10";
        List<Long> ids = getDbQuery().query_cache(Long.class, isCacheNullObject(), getCache_region(), "active_signs", sql);
        return Sign.ME.loadList(ids);
    }
}
