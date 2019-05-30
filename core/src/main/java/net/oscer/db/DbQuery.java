package net.oscer.db;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Closeable;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 数据库查询封装接口，支持多数据库
 *
 * @author kz
 */
public class DbQuery implements Closeable, AutoCloseable {

    private final static QueryRunner _g_runner = new QueryRunner();

    private String databaseName;
    private Connection conn;

    private DbQuery(String databaseName) {
        this.databaseName = databaseName;
        this.conn = (databaseName != null) ? DBManager.getConnection(databaseName) : DBManager.getConnection();
    }

    public final static DbQuery get(String... databaseName) {
        return new DbQuery((databaseName.length > 0) ? databaseName[0] : null);
    }

    public final Connection conn() {
        return conn;
    }

    public static String sql(String key) {
        return DBManager.sql(key);
    }

    public Array array(String typeName, Object[] objs) {
        try {
            return conn.createArrayOf(typeName, objs);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 读取某个对象
     *
     * @param sql
     * @param params
     * @return
     */
    public <T> T read(Class<T> beanClass, String sql, Object... params) {
        try {
            return (T) _g_runner.query(conn, sql, _IsPrimitive(beanClass) ? _g_scaleHandler : new BeanHandler(beanClass), trimSQLParams(sql, params));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public <T> T read_cache(Class<T> beanClass, boolean cacheNullObject, String cache, String key, String sql, Object... params) {
        return (T) CacheMgr.get(cache, key, (k) -> read(beanClass, sql, params), cacheNullObject);
    }

    public List<Object[]> list(String sql, Object... params) {
        try {
            return _g_runner.query(conn, sql, new ArrayListHandler(), trimSQLParams(sql, params));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 对象查询
     *
     * @param <T>
     * @param beanClass
     * @param sql
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T> List<T> query(Class<T> beanClass, String sql, Object... params) {
        try {
            return (List<T>) _g_runner.query(conn, sql, _IsPrimitive(beanClass) ? _g_columnListHandler : new BeanListHandler(beanClass), trimSQLParams(sql, params));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 支持缓存的对象查询
     *
     * @param <T>
     * @param beanClass
     * @param cache_region
     * @param key
     * @param sql
     * @param params
     * @return
     */
    public <T> List<T> query_cache(Class<T> beanClass, boolean cacheNullObject, String cache_region, String key, String sql, Object... params) {
        return (List<T>) CacheMgr.get(cache_region, key, (k) -> query(beanClass, sql, params), cacheNullObject);
    }

    /**
     * 分页查询
     *
     * @param <T>
     * @param beanClass
     * @param sql
     * @param page
     * @param count
     * @param params
     * @return
     */
    public <T> List<T> query_slice(Class<T> beanClass, String sql, int page, int count, Object... params) {
        if (page < 0 || count < 0) {
            throw new IllegalArgumentException("Illegal parameter of 'page' or 'count', Must be positive.");
        }
        int from = (page - 1) * count;
        count = (count > 0) ? count : Integer.MAX_VALUE;
        return query(beanClass, sql + " LIMIT ? OFFSET ?", ArrayUtils.addAll(params, new Object[]{count, from}));
    }

    /**
     * 支持缓存的分页查询
     *
     * @param <T>
     * @param beanClass
     * @param cache
     * @param cache_key
     * @param cache_obj_count
     * @param sql
     * @param page
     * @param count
     * @param params
     * @return
     */
    public <T> List<T> query_slice_cache(Class<T> beanClass, String cache, String cache_key, int cache_obj_count,
                                         String sql, int page, int count, Object... params) {
        List<T> objs = (List<T>) CacheMgr.get(cache, cache_key, (key) -> query_slice(beanClass, sql, 1, cache_obj_count, params));
        if (CollectionUtils.isEmpty(objs)) {
            return objs;
        }
        int from = (page - 1) * count;
        if (from < 0) {
            return null;
        }
        // 超出缓存的范围
        if ((from + count) > cache_obj_count) {
            return cache_query_slice("1d", cache + ':' + cache_key, beanClass, sql, page, count, params);
        }
        int end = Math.min(from + count, objs.size());
        if (from >= end) {
            return null;
        }
        return objs.subList(from, end);
    }

    private <T> List<T> cache_query_slice(String cache, String cache_key, Class<T> cls, String sql, int page, int count, Object... params) {
        String key = cache_key + "#slice#" + page + "#" + count;
        return (List<T>) CacheMgr.get(cache, key, (k) -> query_slice(cls, sql, page, count, trimSQLParams(sql, params)));
    }

    /**
     * 执行统计查询语句，语句的执行结果必须只返回一个数值
     *
     * @param sql
     * @param params
     * @return
     */
    public int stat(String sql, Object... params) {
        try {
            Number num = (Number) _g_runner.query(conn, sql, _g_scaleHandler, trimSQLParams(sql, params));
            return (num != null) ? num.intValue() : -1;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 根据SQL中问号的个数对参数进行处理
     *
     * @param sql
     * @param params
     * @return
     */
    private static Object[] trimSQLParams(String sql, Object[] params) {
        return params;
    }

    /**
     * 执行统计查询语句，语句的执行结果必须只返回一个数值
     *
     * @param cache_region
     * @param key
     * @param sql
     * @param params
     * @return
     */
    public int stat_cache(String cache_region, String key, String sql, Object... params) {
        return (Integer) CacheMgr.get(cache_region, key, (k) -> stat(sql, params));
    }

    /**
     * 执行极值查询语句，语句的执行结果返回一个double数值
     *
     * @param sql
     * @param params
     * @return
     */
    public double extr(String sql, Object... params) {
        try {
            Number num = (Number) _g_runner.query(conn, sql, _g_scaleHandler, trimSQLParams(sql, params));
            return (num != null) ? num.doubleValue() : -1;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 执行极值查询语句，语句的执行结果返回一个double数值
     *
     * @param cache_region
     * @param key
     * @param sql
     * @param params
     * @return
     */
    public double extr_cache(String cache_region, String key, String sql, Object... params) {
        return (double) CacheMgr.get(cache_region, key, (k) -> extr(sql, params));
    }

    /**
     * 执行INSERT/UPDATE/DELETE语句
     *
     * @param sql
     * @param params
     * @return
     */
    public int update(String sql, Object... params) {
        try {
            return _g_runner.update(conn, sql, trimSQLParams(sql, params));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 批量执行指定的SQL语句
     *
     * @param sql
     * @param params
     * @return
     */
    public int[] batch(String sql, Object[][] params) {
        try {
            boolean automit = true;
            try {
                automit = conn.getAutoCommit();
                conn.setAutoCommit(false);
                int[] m = _g_runner.batch(conn, sql, params);
                conn.commit();
                return m;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(automit);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 将业务加入事务控制
     *
     * @param executor
     * @throws Exception
     */
    public Object batch(Function<Connection, Object> executor) {
        try {
            Object result;
            Integer level = TRANSACTION_LEVEL.get();
            //判断是否已经启动了事务处理
            boolean AlreadyBeginTransaction = (level != null);
            Boolean autoCommit = null;
            try {
                if (level == null) {
                    autoCommit = conn.getAutoCommit();
                    level = 1;
                } else {
                    level = level + 1;
                }
                TRANSACTION_LEVEL.set(level);
                if (!AlreadyBeginTransaction && conn.getAutoCommit()) {
                    conn.setAutoCommit(false);
                }
                result = executor.apply(conn);
                level = level - 1;
                TRANSACTION_LEVEL.set(level);
                if (level == 0) {
                    conn.commit();
                    TRANSACTION_LEVEL.remove();
                }
            } catch (SQLException e) {
                if (conn != null && !AlreadyBeginTransaction) {
                    conn.rollback();
                }
                throw e;
            } finally {
                if (autoCommit != null && !AlreadyBeginTransaction) {
                    try {
                        conn.setAutoCommit(autoCommit);
                    } catch (SQLException e) {
                    }
                }
            }
            return result;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 将业务加入事务控制
     *
     * @param service
     * @throws Exception
     */
    public void transaction(TransactionService service) throws Exception {
        Integer level = TRANSACTION_LEVEL.get();
        Boolean autoCommit = null;
        if (level == null) {
            autoCommit = conn.getAutoCommit();
            level = 1;
        } else {
            level = level + 1;
        }
        TRANSACTION_LEVEL.set(level);
        try {
            if (conn.getAutoCommit()) {
                conn.setAutoCommit(false);
            }
            service.execute();
            level = level - 1;
            TRANSACTION_LEVEL.set(level);
            if (level == 0) {
                conn.commit();
                TRANSACTION_LEVEL.remove();
            }
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    /**
                     * 数据回滚的时候，要清除此次操作中涉及到的缓存,
                     * 只对使用了CacheUtils的Pojo有效，并且Pojo中的cacheUtils需要设置keepCacheOfThisThread为true
                     */
                } catch (SQLException e1) {
                    throw new DBException(e);
                }
            }
            if (e instanceof SQLException) {
                throw new DBException(e);
            } else {
                throw e;
            }
        } finally {
            if (autoCommit != null) {
                conn.setAutoCommit(autoCommit);
            }
        }
    }

    private final static ThreadLocal<Integer> TRANSACTION_LEVEL = new ThreadLocal<Integer>();

    private final static ColumnListHandler _g_columnListHandler = new ColumnListHandler() {
        @Override
        protected Object handleRow(ResultSet rs) throws SQLException {
            Object obj = super.handleRow(rs);
            if (obj instanceof BigInteger) {
                return ((BigInteger) obj).longValue();
            }
            return obj;
        }

    };

    private final static ScalarHandler _g_scaleHandler = new ScalarHandler() {
        @Override
        public Object handle(ResultSet rs) throws SQLException {
            Object obj = super.handle(rs);
            if (obj instanceof BigInteger) {
                return ((BigInteger) obj).longValue();
            }
            return obj;
        }
    };

    private final static List<Class<?>> PrimitiveClasses = new ArrayList<Class<?>>() {{
        add(Long.class);
        add(Integer.class);
        add(Number.class);
        add(String.class);
        add(java.util.Date.class);
        add(Date.class);
        add(Timestamp.class);
    }};

    private final static boolean _IsPrimitive(Class<?> cls) {
        return cls.isPrimitive() || PrimitiveClasses.contains(cls);
    }

    @Override
    public void close() {
        DBManager.closeConnection(databaseName);
    }
}
