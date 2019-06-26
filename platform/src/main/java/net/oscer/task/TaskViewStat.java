package net.oscer.task;


import net.oscer.db.DbQuery;
import net.oscer.db.TransactionService;
import net.oscer.service.ViewService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author kz
 * @date 2019年6月26日17:09:19
 */
@Component
public class TaskViewStat {

    /**
     * 每隔5分钟更新阅读数
     *
     * @throws Exception
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void timer() throws Exception {
        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {
                ViewService.run();
            }
        });
    }

}
