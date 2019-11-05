package net.oscer.task;


import net.oscer.dao.UserDAO;
import net.oscer.db.DbQuery;
import net.oscer.db.TransactionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 以定时器方式去查询数据，解决mysql 8小时内无连接的问题
 * @author kz
 * @date 2019年10月30日10:23:25
 */
@Component
public class TaskKeepConnection {

    /**
     * 每隔10分钟查询
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void timer() throws Exception {
        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {
                UserDAO.ME.evict_count_signed_today();
                UserDAO.ME.count_signed_today();
            }
        });
    }

}
