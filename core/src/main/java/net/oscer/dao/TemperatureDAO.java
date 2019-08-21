package net.oscer.dao;

import net.oscer.beans.Temperature;
import net.oscer.db.CacheMgr;

import java.util.List;

/**
 * 温度记录
 *
 * @author kz
 * @create 2019-08-06 18:19
 **/
public class TemperatureDAO extends CommonDao<Temperature> {

    public static final TemperatureDAO ME = new TemperatureDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public List<Temperature> listByType(long user, String type) {
        String sql = "select * from temperatures where user=? and type=? order by create_time desc";
        String cacheKey = "listByType#" + user + "#" + type;
        return getDbQuery().query_cache(Temperature.class, false, getCache_region(), cacheKey, sql, user, type);
    }

    public void evict(long user, String type) {
        String cacheKey = "listByType#" + user + "#" + type;
        CacheMgr.evict(getCache_region(), cacheKey);
    }
}
