package net.oscer.dao;

import net.oscer.beans.CommentQuestion;
import net.oscer.db.CacheMgr;

import java.util.List;

/**
 * 帖子评论
 *
 * @author kz
 * @create 2019-05-29 11:54
 **/
public class CommentQuestionDAO extends CommonDao<CommentQuestion> {

    public static final CommentQuestionDAO ME = new CommentQuestionDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public List<CommentQuestion> weekHots() {
        String sql = "";
        return null;
    }

    public void evictCount(long question) {
        CacheMgr.evict(CommentQuestion.ME.CacheRegion(), "count#" + question);
    }

    public int count(long question) {
        String sql = "select count(id) from comment_questions where question=?";
        return getDbQuery().stat_cache(CommentQuestion.ME.CacheRegion(), "count#" + question, sql, question);
    }

    public List<CommentQuestion> list(long question, int page, int size) {
        String sql = "select id from comment_questions where question=? ";
        List<Long> ids = getDbQuery().query_slice(long.class, sql, page, size, question);
        return CommentQuestion.ME.loadList(ids);
    }
}
