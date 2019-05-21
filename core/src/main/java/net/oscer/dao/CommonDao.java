package net.oscer.dao;

import net.oscer.db.DbQuery;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * DAO基类
 *
 * @param <T>
 */
public abstract class CommonDao<T> {

    protected static final int DEFAULT_CACHE_OBJ_COUNT = 100;

    //sql
    protected String sql;
    //缓存名
    protected String cache_region;
    //是否缓存 默认不缓存
    private boolean cacheNullObject = false;

    protected boolean isCacheNullObject() {
        return cacheNullObject;
    }

    protected void setCacheNullObject(boolean cacheNullObject) {
        this.cacheNullObject = cacheNullObject;
    }

    protected String getCache_region() {
        if (this.cache_region == null) {
            Type genType = this.getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            return ((Class) params[0]).getSimpleName();
        }
        return this.cache_region;
    }

    protected void setCache_region(String cache_region) {
        this.cache_region = cache_region;
    }

    protected String getSql() {
        return sql;
    }

    protected void setSql(String sql) {
        this.sql = sql;
    }

    protected abstract String databaseName();

    protected DbQuery getDbQuery() {
        return DbQuery.get(databaseName());
    }

    /**
     * 获取当前方法名
     *
     * @return
     */
    protected String getMethodName() {
        return new Throwable().getStackTrace()[1].getMethodName();
    }

    /**
     * 获取当前方法名
     *
     * @return
     */
    protected String getMethodName(long id) {
        return new Throwable().getStackTrace()[1].getMethodName() + "_" + id;
    }

}
