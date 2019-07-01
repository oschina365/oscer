package net.oscer.dao;

import net.oscer.beans.CollectQuestion;
import net.oscer.beans.CommentQuestion;
import net.oscer.db.CacheMgr;

import java.util.List;

/**
 * 帖子收藏
 *
 * @author kz
 * @create 2019-07-01 15:36
 **/
public class CollectQuestionDAO extends CommonDao<CollectQuestion> {

    public static final CollectQuestionDAO ME = new CollectQuestionDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public void evict(long user) {
        CacheMgr.evict(CollectQuestion.ME.CacheRegion(), "list#" + user + "#" + -1);
        CacheMgr.evict(CollectQuestion.ME.CacheRegion(), "list#" + user + "#" + 0);
        CacheMgr.evict(CollectQuestion.ME.CacheRegion(), "list#" + user + "#" + 1);
    }

    /**
     * 查询用户的收藏列表
     *
     * @param user
     * @param status
     * @return
     */
    public List<CollectQuestion> list(long user, int status) {
        if (user <= 0L) {
            return null;
        }
        String sql = "select id from collect_questions where user=? and status=? order by last_date desc";
        if (status < 0) {
            sql = "select id from collect_questions where user=? and -2<? order by last_date desc";
        }
        List<Long> ids = getDbQuery().query_cache(long.class, false, CollectQuestion.ME.CacheRegion(), "list#" + user + "#" + status, sql, user, status);
        return CollectQuestion.ME.loadList(ids);
    }

    /**
     * 根据帖子查询
     *
     * @param user
     * @return
     */
    public CollectQuestion getByUser(long user) {
        if (user <= 0L) {
            return null;
        }
        return getDbQuery().read(CollectQuestion.class, "select * from collect_questions where user=?", user);
    }

}
