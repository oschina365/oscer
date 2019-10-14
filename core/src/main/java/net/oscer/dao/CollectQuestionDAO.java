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
        CacheMgr.evict(CollectQuestion.ME.CacheRegion(), "page_list#" + user + "#" + -1);
        CacheMgr.evict(CollectQuestion.ME.CacheRegion(), "page_list#" + user + "#" + 0);
        CacheMgr.evict(CollectQuestion.ME.CacheRegion(), "page_list#" + user + "#" + 1);
        CacheMgr.evict(CollectQuestion.ME.CacheRegion(), "count#" + user + "#" + -1);
        CacheMgr.evict(CollectQuestion.ME.CacheRegion(), "count#" + user + "#" + 0);
        CacheMgr.evict(CollectQuestion.ME.CacheRegion(), "count#" + user + "#" + 1);
    }

    public int count(long user, int status) {
        String sql = "select count(*) from collect_questions where user=? and status=? ";
        return getDbQuery().stat_cache(CollectQuestion.ME.CacheRegion(), "count#" + user + "#" + status, sql, user, status);
    }

    /**
     * 查询用户的收藏列表,分页查询
     *
     * @param user
     * @param status
     * @return
     */
    public List<CollectQuestion> page_list(long user, int status, int page, int size) {
        if (user <= 0L) {
            return null;
        }
        String sql = "select id from collect_questions where user=? and status=? order by last_date desc";
        if (status < 0) {
            sql = "select id from collect_questions where user=? and -2<? order by last_date desc";
        }
        List<Long> ids = getDbQuery().query_slice_cache(long.class, CollectQuestion.ME.CacheRegion(),
                "page_list#" + user + "#" + status, 100, sql, page, size, user, status);
        return CollectQuestion.ME.loadList(ids);
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
     * @param question
     * @return
     */
    public CollectQuestion getByUser(long user, long question) {
        if (user <= 0L) {
            return null;
        }
        return getDbQuery().read(CollectQuestion.class, "select * from collect_questions where user=? and question=?", user, question);
    }

    public void deleteByQuestion(long question) {
        try {
            getDbQuery().update("delete from collect_questions where question=?", question);
        } catch (Exception e) {

        }
    }

}
