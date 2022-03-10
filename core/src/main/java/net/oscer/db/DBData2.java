package net.oscer.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class DBData2 {

    private static DruidDataSource dataSource = new DruidDataSource();
    private static Properties jdbcPros = new Properties();

    static {
        try {
            jdbcPros.load(DBData2.class.getResourceAsStream("/db2.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataSource.setDriverClassName(jdbcPros.getProperty("driverClassName"));
        dataSource.setUrl(jdbcPros.getProperty("jdbcUrl"));
        dataSource.setUsername(jdbcPros.getProperty("username"));
        dataSource.setPassword(jdbcPros.getProperty("password"));
        dataSource.setMaxActive(Integer.parseInt(jdbcPros.getProperty("maxActive")));
    }

    public static QueryRunner runner() {
        return new QueryRunner(dataSource);
    }

    public static Map<String, Object> findById(long id, String table) throws SQLException {
        String sql = "select * from " + table + " where id=?";
        return runner().query(sql, new MapHandler(), id);
    }

    public static List<Map<String, Object>> findByCondition(String condition, long conditionValue, String table)
            throws SQLException {
        String sql = "select * from " + table + " where " + condition + "=?";
        return runner().query(sql, new MapListHandler(), conditionValue);
    }

    public static List<Map<String, Object>> pageDataById(long beginId, int pageSize, String table)
            throws SQLException {
        String sql = "select * from " + table + " where id > ? order by id asc limit ?";
        return runner().query(sql, new MapListHandler(), beginId, pageSize);
    }

    public static List<Map<String, Object>> pageDataById(String selectTarget, long beginId, int pageSize, String table)
            throws SQLException {
        String sql = "select " + selectTarget + " from " + table + " where id > ? order by id asc limit ?";
        return runner().query(sql, new MapListHandler(), beginId, pageSize);
    }

    public static List<Map<String, Object>> pageDataByIdBetween(long startId, long endId, String table) throws SQLException {
        String sql = "select * from " + table + " where id >= ? and id < ?";
        return runner().query(sql, new MapListHandler(), startId, endId);
    }//end of method

    public static List<Map<String, Object>> findAll(String tableName) throws SQLException {
        String sql = "select * from " + tableName;
        return runner().query(sql, new MapListHandler());
    }

    public static List<Map<String, Object>> findBySql(String sql) throws SQLException {
        return runner().query(sql, new MapListHandler());
    }

    public static List<Map<String, Object>> findBySql(String sql, Object... params) throws SQLException {
        return runner().query(sql, new MapListHandler(), params);
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(DBData2.findById(12, "osc_users"));
    }
}
