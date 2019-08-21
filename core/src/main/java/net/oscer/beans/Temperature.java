package net.oscer.beans;

import net.oscer.db.Entity;
import net.oscer.framework.FormatTool;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 温度记录
 *
 * @author kz
 * @create 2019-08-06 18:17
 **/
@Entity.Cache(region = "Temperature")
public class Temperature extends Entity {

    public static final Temperature ME = new Temperature();

    /**
     * 体温
     */
    public static final String TYPE_TEMPERATURE = "temperature";

    /**
     * 用户ID
     */
    private long user;
    /**
     * 温度值 体温：36.52
     */
    private BigDecimal temperature;

    /**
     * 添加时间
     */
    private Timestamp create_time;

    /**
     * 如：20190806
     */
    private int day;

    /**
     * 类型
     */
    private String type;

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String formatCreateTime(){
        return FormatTool.formatDate(this.getCreate_time(),"yyyy-MM-dd HH:mm:ss");
    }
}
