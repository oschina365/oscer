package net.oscer.beans;


import com.qiniu.common.QiniuException;
import com.qiniu.storage.model.FileInfo;
import net.oscer.db.DbQuery;
import net.oscer.db.Entity;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 访问链接详情表
 * </p>
 *
 * @since 2020-11-10
 */
public class VisitDetail extends Entity implements ApplicationRunner {
    public final static VisitDetail ME = new VisitDetail();

    public static final String SOURCE_WEB = "web";
    public static final String SOURCE_APP = "app";

    /**
     * 专区ID
     */
    private long circle;
    /**
     * 访问来源，Pc,Android,Ios
     */
    private String source;
    /**
     * 访问链接地址
     */
    private String url;
    /**
     * 访问ip
     */
    private String visit_ip;
    /**
     * 登录者ID，游客=0
     */
    private long user;

    /**
     * 访问标识，登录用户，此ID=用户ID，游客=系统时间纳秒
     */
    private String uid;

    /**
     * 访问的文章ID
     */
    private long obj_id;

    /**
     * 访问的文章类型
     */
    private int obj_type;

    /**
     * 访问时间
     */
    private Timestamp visit_time;

    /**
     * 访问时间 格式：20201111
     */
    private long visit_date;

    /**
     * 保存数据的机器来源
     */
    private String data_from;

    public long getCircle() {
        return circle;
    }

    public void setCircle(long circle) {
        this.circle = circle;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVisit_ip() {
        return visit_ip;
    }

    public void setVisit_ip(String visit_ip) {
        this.visit_ip = visit_ip;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public Timestamp getVisit_time() {
        return visit_time;
    }

    public void setVisit_time(Timestamp visit_time) {
        this.visit_time = visit_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getObj_id() {
        return obj_id;
    }

    public void setObj_id(long obj_id) {
        this.obj_id = obj_id;
    }

    public int getObj_type() {
        return obj_type;
    }

    public void setObj_type(int obj_type) {
        this.obj_type = obj_type;
    }

    public long getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(long visit_date) {
        this.visit_date = visit_date;
    }

    public String getData_from() {
        return data_from;
    }

    public void setData_from(String data_from) {
        this.data_from = data_from;
    }

    @Override
    protected String tableName() {
        return "osc_visit_details";
    }


    @Async
    public void run() {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(100, 150, 1, TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>());

        int size = 100000;
        List<Integer> dates = Arrays.asList(20201201,20201202,20201203,20201204,20201205);
        for (Integer date : dates) {
            String count_sql = "select count(*) from osc_visit_details_bak where visit_date=?";
            int total = DbQuery.get("mysql").stat(count_sql, date);
            int nums = (total / size) + ((total % size) == 0 ? 0 : 1);
            for (int i = 1; i <= nums; i++) {
                String sql = "select * from osc_visit_details_bak where visit_date=?  order by visit_time asc ";
                List<VisitDetail> list = DbQuery.get("mysql").query_slice(VisitDetail.class, sql,i , size, date);
                /*list.stream().forEach(v -> {
                    try {
                        v.setId(0L);
                        v.save();
                    } catch (Exception e) {
                    }
                });*/
                for (VisitDetail v : list) {
                    poolExecutor.execute(new Save(v));
                }
            }
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        run();
    }


    class Save implements Runnable {
        VisitDetail d;

        public Save(VisitDetail d) {
            this.d = d;
        }

        @Override
        public void run() {
            try {
                d.setId(0L);
                d.save();
            } catch (Exception e) {
            }
        }
    }

    public static List<Integer> visit_date() {
        String sql = "select DISTINCT(visit_date) from osc_visit_details_bak where circle=4 and obj_type=51 order by visit_date asc;";
        return DbQuery.get("mysql").query(Integer.class, sql);
    }


    public static void main(String[] args) {
        List<String> as = new ArrayList<>();
        as.add("1");
        as.add("2");
        System.out.println(as);
       /* as.remove("2");
        System.out.println(as);*/
        Iterator<String> aaa = as.iterator();
        while (aaa.hasNext()){
            if(aaa.next().equals("2")){
                aaa.remove();
            }
        }
        System.out.println(as);

        try {
            Class c = Class.forName("net.oscer.common.ApiResult");
            System.out.println(c);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}

