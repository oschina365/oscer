package net.oscer.task;


import net.oscer.dao.UserDAO;
import net.oscer.db.DbQuery;
import net.oscer.db.TransactionService;
import net.oscer.framework.FormatTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author kz
 * @date 2019年6月3日17:09:19
 */
@Component
public class TaskResetScoreToday {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 每天凌晨23点59分59秒时刻，重置当日签到积分数据
     *
     * @throws Exception
     */
    @Scheduled(cron = "59 59 23 * * ?")
    public void timer() throws Exception {
        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {
                UserDAO.ME.resetScoreToday();
                logger.info("time={},重置今日积分数据字段成功！", FormatTool.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
        });
    }

}
