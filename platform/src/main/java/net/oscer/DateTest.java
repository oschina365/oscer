package net.oscer;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.oscer.common.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/**
 * 时间比较
 *
 * @author kz
 * @create 2021-07-30 18:51
 **/
public class DateTest {

    public static class TimeVO {
        /**
         * 开始时间(年月日)
         */
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private Date startDate;

        /**
         * 结束时间(年月日)
         */
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
        private Date endDate;

        /**
         * 开始时间(时分秒)
         */
        private String startTime;

        /**
         * 结束时间(时分秒)
         */
        private String endTime;

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public TimeVO(Date startDate, Date endDate, String startTime, String endTime) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimeVO timeVO = (TimeVO) o;
            return Objects.equals(startDate, timeVO.startDate) &&
                    Objects.equals(endDate, timeVO.endDate) &&
                    Objects.equals(startTime, timeVO.startTime) &&
                    Objects.equals(endTime, timeVO.endTime);
        }
    }

    public static void main(String[] args) {
        String startDate1 = "2021-07-30", endDate1 = "2021-08-01";
        String startTime1 = "08:00:00", endTime1 = "10:00:00";
        TimeVO time1 = new TimeVO(
                DateUtil.parse(startDate1, "yyyy-MM-dd"),
                DateUtil.parse(endDate1, "yyyy-MM-dd"),
                startTime1, endTime1);


        String startDate2 = "2021-07-31", endDate2 = "2021-08-01";
        String startTime2 = "12:00:00", endTime2 = "14:00:00";

        TimeVO time2 = new TimeVO(
                DateUtil.parse(startDate2, "yyyy-MM-dd"),
                DateUtil.parse(endDate2, "yyyy-MM-dd"),
                startTime2, endTime2);

        conflict(time1, time2);
    }

    public static boolean conflict(TimeVO time1, TimeVO time2) {
        if (time1.equals(time2)) {
            System.out.println("time2与time1有交集");
            return true;
        }
        if (time2.getStartDate().before(time1.getStartDate())) {
            System.out.println("time2与time1没有交集");
            return false;
        }
        if (time2.getStartDate().after(time1.getEndDate())) {
            System.out.println("time2与time1没有交集");
            return false;
        }
        System.out.println("time2与time1在年月日上有交集");
        String sameTime = "2021-01-01 ";
        Date hourBegin1 = DateUtil.parse(sameTime + time1.getStartTime(), "yyyy-MM-dd HH:mm:ss");
        Date hourEnd1 = DateUtil.parse(sameTime + time1.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        Date hourBegin2 = DateUtil.parse(sameTime + time2.getStartTime(), "yyyy-MM-dd HH:mm:ss");
        Date hourEnd2 = DateUtil.parse(sameTime + time2.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        System.out.println(hourBegin1.getTime());
        System.out.println(hourBegin2.getTime());
        if (hourBegin2.getTime() < hourBegin1.getTime() || hourBegin2.getTime() > hourEnd1.getTime()) {
            System.out.println("time2与time1在时分秒上没有交集");
            return false;
        }
        System.out.println("time2与time1在时分秒上有交集");
        return true;
    }
}
