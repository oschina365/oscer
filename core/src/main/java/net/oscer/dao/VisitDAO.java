package net.oscer.dao;


import net.oscer.beans.Visit;
import net.oscer.db.CacheMgr;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class VisitDAO extends CommonDao<Visit> {

    public static final VisitDAO ME = new VisitDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public void save(long user_id, long obj_id, int obj_type) {
        if (user_id <= 0L || obj_id <= 0L || obj_type <= 0) {
            return;
        }
        String mysql = "INSERT INTO visits(user_id,obj_id,obj_type) VALUES(?,?,?) ON DUPLICATE KEY UPDATE last_date = now()";
        getDbQuery().update(mysql, user_id, obj_id, obj_type);
        CacheMgr.evict(getCache_region(), user_id + "#" + obj_type);
        return;
    }

    /**
     * 查询一条记录
     *
     * @param user_id
     * @param obj_id
     * @param obj_type
     * @return
     */
    public Visit selectByUserIdAndObjType(long user_id, long obj_id, int obj_type) {
        if (user_id <= 0L || obj_id <= 0L || obj_type <= 0) {
            return null;
        }
        String sql = "select id from visits where user_id=? and obj_id=? and obj_type=?";
        Number i = getDbQuery().read(Number.class, sql, user_id, obj_id, obj_type);
        if (i == null || i.longValue() <= 0L) {
            return null;
        }
        return getDbQuery().read_cache(Visit.class, false, Visit.ME.CacheRegion(), String.valueOf(i.longValue()), sql, user_id, obj_id, obj_type);
    }

    /**
     * 查询某类型，该用户所有访问记录
     *
     * @param user_id
     * @param obj_type
     * @return
     */
    public List<Visit> listByUserAndObjType(long user_id, int obj_type) {
        if (user_id <= 0L || obj_type <= 0) {
            return null;
        }
        String sql = "select * from visits where user_id=? and obj_type=? order by last_date desc,id desc";
        return getDbQuery().query_cache(Visit.class, false, getCache_region(), user_id + "#" + obj_type, sql, user_id, obj_type);
    }

    public void deleteByObjIdObjType(long obj_id, int obj_type) {
        try {
            getDbQuery().update("delete from visits where obj_id=? and obj_type=?", obj_id, obj_type);
        } catch (Exception e) {

        }
    }

}
