package net.oscer.dao;

import net.oscer.beans.CommentQuestion;
import net.oscer.beans.User;
import net.oscer.db.CacheMgr;
import net.oscer.vo.CountVO;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.oscer.db.Entity.CACHE_ONE_MIN;

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

    /**
     * 回帖周榜,获取回帖最多的用户
     *
     * @return
     */
    public List<Map<User, Integer>> weekUserCommentHots() {
        String sql = "SELECT user as id,count(1) count FROM  comment_questions WHERE " +
                "YEARWEEK( date_format(  insert_date,'%Y-%m-%d' ) ) = YEARWEEK( now()) GROUP BY user order by count desc limit 5";
        List<CountVO> list = getDbQuery().query_cache(CountVO.class, false, CACHE_ONE_MIN, "weekUserCommentHots", sql);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<Map<User, Integer>> result = new LinkedList<>();

        list.stream().filter(c -> (null != c && c.getId() > 0)).forEach(c -> {
            Map<User, Integer> map = new HashMap<>(1);
            User u = User.ME.get(c.getId());
            map.put(u, c.getCount());
            result.add(map);
        });
        return result;
    }

    /**
     * 回帖周榜,获取帖子
     *
     * @return
     */
    public List<CommentQuestion> weekCommentHots() {
        String sql = "SELECT question as id,count(1) count FROM comment_questions WHERE " +
                "YEARWEEK( date_format(  insert_date,'%Y-%m-%d' ) ) = YEARWEEK( now()) GROUP BY question order by count desc limit 5";
        List<CountVO> list = getDbQuery().query_cache(CountVO.class, false, CommentQuestion.ME.CacheRegion(), "weekCommentHots", sql);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<Long> ids = list.stream().filter(c -> (null != c && c.getId() > 0)).map(CountVO::getId).collect(Collectors.toList());
        return CommentQuestion.ME.loadList(ids);
    }

    public void evict(long question, long user) {
        CacheMgr.evict(CommentQuestion.ME.CacheRegion(), "count#" + question);
        CacheMgr.evict(CommentQuestion.ME.CacheRegion(), "question#all#" + question);
        CacheMgr.evict(CommentQuestion.ME.CacheRegion(), "question#" + question);
        CacheMgr.evict(CommentQuestion.ME.CacheRegion(), "rankMap#" + question);
        CacheMgr.evict(CommentQuestion.ME.CacheRegion(), "allByUser#" + user + "#" + 0);
        CacheMgr.evict(CommentQuestion.ME.CacheRegion(), "allByUser#" + user + "#" + 1);
    }

    public int count(long question) {
        String sql = "select count(id) from comment_questions where question=?";
        return getDbQuery().stat_cache(CommentQuestion.ME.CacheRegion(), "count#" + question, sql, question);
    }

    public List<CommentQuestion> list(long question) {
        String sql = "select id from comment_questions where question=? order by id desc";
        List<Long> ids = getDbQuery().query_cache(long.class, false, CommentQuestion.ME.CacheRegion(), "question#all#" + question, sql, question);
        return CommentQuestion.ME.loadList(ids);
    }

    public List<CommentQuestion> list(long question, int page, int size) {
        String sql = "select id from comment_questions where question=? order by id desc";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, CommentQuestion.ME.CacheRegion(), "question#" + question, 100, sql, page, size, question);
        return CommentQuestion.ME.loadList(ids);
    }

    /**
     * 查询用户对帖子的评论
     *
     * @param user
     * @param status
     */
    public List<CommentQuestion> allByUser(long user, long status) {
        if (user <= 0L) {
            return null;
        }
        String sql = "select max(id) from comment_questions where user=? and status =? GROUP BY question order by id desc";
        if (status > 0) {
            sql = "select max(id) from comment_questions where user=? and 1=? GROUP BY question order by id desc";
        }
        List<Long> ids = getDbQuery().query_cache(long.class, false, CommentQuestion.ME.CacheRegion(), "allByUser#" + user + "#" + status, sql, user, status);
        return CommentQuestion.ME.loadList(ids);
    }

    /**
     * 删除改帖子的所有评论
     *
     * @param question
     */
    public void delete(long question) {
        String sql = "delete from comment_questions where question=?";
        getDbQuery().update(sql, question);
    }
}
