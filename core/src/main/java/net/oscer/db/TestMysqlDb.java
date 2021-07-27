package net.oscer.db;

import com.alibaba.fastjson.JSON;
import net.oscer.beans.User;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据库测试
 */
public class TestMysqlDb {

    private final static Log log = LogFactory.getLog(TestMysqlDb.class);

    public static final String bill_name_1 = "sale_bill_1";
    public static final String bill_name_3 = "sale_bill_3";
    public static final String tenantCode_1 = "10000001";
    public static final String tenantCode_35 = "10000035";

    public static void main(String[] args) throws SQLException {
        //bill-数据中心，错误的   order_head-中台数据，正确的
        List<Map<String, Object>> bills1 = bills(bill_name_1, tenantCode_1, 2);
        List<Map<String, Object>> bills35 = bills(bill_name_3, tenantCode_35,2);
        //bills35.stream().filter(b -> b.get("bill_no").equals("74761623374120589")).findFirst();
        //List<Map<String, Object>> order1 = mid1OrderHead(tenantCode_1);
        //List<Map<String, Object>> order35 = mid2OrderHead(tenantCode_35);
        //order35.stream().filter(b -> b.get("order_number").equals("74761623374120589")).findFirst();
        List<Map<String, Object>> order35_refunds = mid2OrderRefund(tenantCode_35);
        List<Map<String, Object>> order1_refunds = mid1OrderRefund(tenantCode_1);
        //sync(bill_name_1, tenantCode_1, bills1, order1);
        sync(bill_name_3, tenantCode_35, bills35, order35_refunds,true);

        //sync(bill_name_1, tenantCode_1, bills1, order1,false);
        //sync(bill_name_1, tenantCode_1, bills1, order1_refunds, true);
    }

    public static Date seckillBegin() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2021);
        c.set(Calendar.MONTH, Calendar.AUGUST);
        c.set(Calendar.DAY_OF_MONTH, 17);
        c.set(Calendar.HOUR_OF_DAY, 15);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        return c.getTime();
    }


    public static void sync(String tableName, String tenantCode, List<Map<String, Object>> bills, List<Map<String, Object>> orders, boolean refund) {
        Map<String, Map<String, Object>> orderMap = new HashMap<>();
        long begin = System.currentTimeMillis();
        for (Map<String, Object> order : orders) {
            if (refund) {
                orderMap.put(order.get("mid_return_order_number") + "_" + order.get("return_order_number"), order);
            } else {
                orderMap.put(order.get("mid_order_number") + "_" + order.get("order_number"), order);
            }

        }

        for (Map<String, Object> bill : bills) {
            Map<String, Object> order = orderMap.get(bill.get("mid_bill_no") + "_" + bill.get("bill_no"));

            if (order == null) {
                writeFailRecord(JSON.toJSONString(bill));
                continue;
            }
            try {
                boolean b = true;
                if (refund) {
                    System.out.println(order.get("id").toString());
                    if(order.get("id").toString().equals("623106430152531970")){
                        System.out.println(order);
                    }
                    b = updateBill(tableName, bill.get("tenant_code"), bill.get("id"), order.get("can_pos"), order.get("finish_time"), order.get("posflag"), order.get("pos_time"));
                } else {
                    b = updateBill(tableName, bill.get("tenant_code"), bill.get("id"), order.get("can_pos"), order.get("finish_time"), order.get("posflag"), order.get("pos_time"));
                }

                if (!b) {
                    writeFailRecord("更新失败：" + JSON.toJSONString(bill));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        log.info(String.format("完成更新商家[商家编号：%s]数据任务耗时：%s 毫秒", tenantCode, (end - begin)));
    }

    /**
     * 失败的内容
     *
     * @param content
     */
    public static void writeFailRecord(String content) {
        try {
            String filePath = System.getProperty("user.dir") + File.separator + "docs" + File.separator + "error_record" + File.separator + "sync_bill.txt";
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileWriter fw = new FileWriter(filePath, true);
            fw.write(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss:SSS") + ":>>> " + content);
            fw.write(System.getProperty("line.separator"));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean updateBill(String tableName, Object tenantCode, Object id, Object p1, Object p2, Object p3, Object p4) throws SQLException {
        String sql = "update " + tableName + " set write_off_flag=?,finish_time=?,pos_flag=?,pos_time=? where tenant_code=? and id=? ";
        return DBData.updateById(sql, p1, p2, p3, p4, tenantCode, id);
    }

    public static List<Map<String, Object>> bills(String tableName, String tenantCode, int billType) throws SQLException {
        String sql = "select * from " + tableName + " where tenant_code=? and bill_type=? and create_time > '2021-07-01 00:00:00' ";
        List<Map<String, Object>> result = DBData.findBySql(sql, tenantCode, billType);
        String billTypeMsg = "正单";
        if (billType == 2) {
            billTypeMsg = "退单";
        }
        log.info(String.format("[%s]表商家编号[%s]查询的结果%s数量：%s", tableName, tenantCode, billTypeMsg, result.size()));
        return result;
    }

    /**
     * 正单
     *
     * @param tenantCode
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> mid1OrderHead(String tenantCode) throws SQLException {
        String sql = "select * from order_head where tenant_code=? and create_time > '2021-07-13 00:00:00' ";
        List<Map<String, Object>> result = DBData1.findBySql(sql, tenantCode);
        log.info(String.format("[mid1数据库的order_head表]商家编号[%s]查询的 正单 结果数量：%s", tenantCode, result.size()));
        return result;
    }

    /**
     * 正单
     *
     * @param tenantCode
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> mid2OrderHead(String tenantCode) throws SQLException {
        String sql = "select * from order_head where tenant_code=? and create_time > '2021-07-13 00:00:00' ";
        List<Map<String, Object>> result = DBData2.findBySql(sql, tenantCode);
        log.info(String.format("[mid2数据库的order_head表]商家编号[%s]查询的 正单 结果数量：%s", tenantCode, result.size()));
        return result;
    }

    /**
     * 退单
     *
     * @param tenantCode
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> mid1OrderRefund(String tenantCode) throws SQLException {
        String sql = "select * from return_order_head where tenant_code=? and create_time > '2021-07-01 00:00:00' ";
        List<Map<String, Object>> result = DBData1.findBySql(sql, tenantCode);
        log.info(String.format("[mid1数据库的order_head表]商家编号[%s]查询的 退单 结果数量：%s", tenantCode, result.size()));
        return result;
    }

    /**
     * 退单
     *
     * @param tenantCode
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> mid2OrderRefund(String tenantCode) throws SQLException {
        String sql = "select * from return_order_head where tenant_code=? and create_time > '2021-07-13 00:00:00' ";
        List<Map<String, Object>> result = DBData2.findBySql(sql, tenantCode);
        log.info(String.format("[mid2数据库的order_head表]商家编号[%s]查询的 退单 结果数量：%s", tenantCode, result.size()));
        return result;
    }

}
