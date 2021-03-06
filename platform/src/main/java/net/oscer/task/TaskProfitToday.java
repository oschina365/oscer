package net.oscer.task;


import net.oscer.dao.UserDAO;
import net.oscer.db.DbQuery;
import net.oscer.db.TransactionService;
import net.oscer.framework.FormatTool;
import net.oscer.framework.Profit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author kz
 * @date 2020年4月26日14:07:18
 */
@Component
public class TaskProfitToday {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 每隔一分钟请求今日头条
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 */59 * * * ?")
    public void timer() throws Exception {
        //Profit.speed_toutiao_kz();
    }

}
