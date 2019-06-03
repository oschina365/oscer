package net.oscer.dao;

import net.oscer.beans.Sign;
import net.oscer.beans.SignDetail;
import net.oscer.beans.User;
import net.oscer.db.CacheMgr;
import net.oscer.db.DbQuery;
import net.oscer.db.TransactionService;
import net.oscer.framework.FormatTool;

import java.util.Date;

/**
 * 签到详细
 *
 * @author kz
 * @create 2019-05-30 11:10
 **/
public class SignDetailDAO extends CommonDao<SignDetail> {

    public static final SignDetailDAO ME = new SignDetailDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 查询今天签到
     *
     * @param user
     * @return
     */
    public Sign todaySign(long user) {
        if (user <= 0L) {
            return null;
        }
        Sign s = SignDAO.ME.selectByUser(user);
        if (s == null) {
            return null;
        }
        return s;
    }

    /**
     * 今天是否已经签到，每天只能签到一次
     *
     * @return
     */
    public boolean signedToday(long user) {
        Sign s = todaySign(user);
        return (s == null || s.getId() <= 0L) ? false : FormatTool.intervalSameDay(new Date(), s.getLast_sign_day(), "yyyyMMdd");
    }

    /**
     * 今天签到
     *
     * @param user
     * @return
     */
    public boolean signingToday(long user) throws Exception {
        User u = User.ME.get(user);
        if (null == u) {
            return false;
        }
        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {
                SignDetail detail = new SignDetail();
                detail.setUser(user);
                detail.setSign_day(Integer.valueOf(FormatTool.formatDate(new Date(), "yyyyMMdd")));
                detail.save();

                Sign s = SignDAO.ME.selectByUser(user);
                s.setUser(user);
                s.setSign_year(Integer.valueOf(FormatTool.formatDate(new Date(), "yyyy")));
                s.setTotal_count(s.getId() > 0L ? (s.getTotal_count() + 1) : 1);
                s.setSeries_count(s.getId() > 0L ? (FormatTool.intervalOneDay(new Date(), s.getLast_sign_day(), "yyyyMMdd") ? (s.getSeries_count() + 1) : 1) : 1);
                s.setLast_sign_day(new Date());
                if (s.getId() > 0L) {
                    s.doUpdate();
                } else {
                    s.save();
                }

                u.setScore_today(Sign.ME.sign_score(s.getSeries_count()));
                u.setScore(u.getScore() + u.getScore_today());
                u.doUpdate();
                CacheMgr.evict(Sign.ME.CacheRegion(), String.valueOf(user));
                CacheMgr.evict(User.ME.CacheRegion(), "count_signed_today");
            }
        });

        return true;
    }
}
