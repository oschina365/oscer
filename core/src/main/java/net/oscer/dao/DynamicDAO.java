package net.oscer.dao;

import net.oscer.beans.Dynamic;
import net.oscer.db.CacheMgr;

import java.util.List;

/**
 * 动态表
 *
 * @author kz
 * @create 2019-10-25 18:10
 **/
public class DynamicDAO extends CommonDao<Dynamic> {

    public static final DynamicDAO ME = new DynamicDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public boolean save(long user, long question) {
        Dynamic d = new Dynamic();
        d.setUser(user);
        d.setQuestion(question);
        d.save();
        return true;
    }

    public boolean save(long user, long question, long comment) {
        Dynamic d = new Dynamic();
        d.setUser(user);
        d.setQuestion(question);
        d.setComment(comment);
        d.save();
        return true;
    }

    public void evict(long user){
        CacheMgr.evict(getCache_region(), "countByUser#" + user);
        CacheMgr.evict(getCache_region(), "listByUser#" + user);
    }

    public int countByUser(long user) {
        String sql = "select count(*) from dynamics  d left join friends f on d.user=f.friend where f.user=? order by d.id desc ";
        return getDbQuery().stat_cache(getCache_region(), "countByUser#" + user, sql, user);
    }

    public List<Dynamic> listByUser(long user, int page, int size) {
        String sql = "select d.* from dynamics  d left join friends f on d.user=f.friend where f.user=? order by d.id desc ";
        return getDbQuery().query_slice_cache(Dynamic.class, getCache_region(), "listByUser#" + user, 50, sql, page, size, user);
    }

}
