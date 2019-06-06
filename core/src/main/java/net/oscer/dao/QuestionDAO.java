package net.oscer.dao;

import net.oscer.beans.Question;
import net.oscer.common.ApiResult;
import net.oscer.common.PatternUtil;
import net.oscer.db.CacheMgr;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 帖子
 *
 * @author kz
 * @create 2019-05-23 18:29
 **/
public class QuestionDAO extends CommonDao<Question> {

    public final static QuestionDAO ME = new QuestionDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 发帖表单校验
     *
     * @param form
     * @param is_reward
     * @return
     */
    public ApiResult check(Question form, int is_reward) {
        if (form == null || StringUtils.isBlank(form.getTitle())) {
            return ApiResult.failWithMessage("请填写标题");
        }

        if (StringUtils.isBlank(form.getContent())) {
            return ApiResult.failWithMessage("请填写内容");
        }

        long node = Question.ME.NodeOrDefault(form.getNode());
        if (node == 0) {
            return ApiResult.failWithMessage("该节点不存在");
        }

        int reward_point = Question.ME.RewardPointOrDefault(form.getReward_point());
        if (is_reward == 1 && reward_point < Question.REWARD_POINT_DEFAULT) {
            return ApiResult.failWithMessage("请选择悬赏积分");
        }
        if (form.getOriginal() != null && form.getOriginal() == Question.ORIGINAL_1 && !PatternUtil.check(PatternUtil.CHECK_SITE, form.getOriginal_url())) {
            return ApiResult.failWithMessage("请输入正确的转帖地址");
        }
        return ApiResult.success();

    }

    public int count(long node) {
        String sql = "select count(id) from questions where status=0 ";
        if (node > 0L) {
            sql = "select count(id) from questions where node=" + node + " and status=0 ";
        }
        return getDbQuery().stat_cache(getCache_region(), "count#0" + node, sql);
    }

    public List<Question> tops(int limit) {
        String sql = "select id from questions where status=0 and system_top > 0 order by recomm desc,system_top desc limit ?";
        List<Long> ids = getDbQuery().query_cache(long.class, isCacheNullObject(), getCache_region(), "tops#" + limit, sql, limit);
        return Question.ME.loadList(ids);
    }

    public List<Question> all(long node, int page, int size) {
        String sql = "select id from questions where status=0 order by system_top desc,recomm desc, id desc";
        if (node > 0L) {
            sql = "select id from questions where node=" + node + " and status=0 order by system_top desc,recomm desc, id desc";
        }
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "status#0" + node, 100, sql, page, size);
        return Question.ME.loadList(ids);
    }

    /**
     * 根据节点查询对应的本周热议帖子
     *
     * @param node  为0时候，首页查询本周热帖，不区分节点
     * @param limit
     * @return
     */
    public List<Question> hots(long node, int limit) {
        String sql = "select id from questions where status=0 and YEARWEEK(date_format(insert_date,'%Y-%m-%d'))= YEARWEEK(now())" +
                " order by (collect_count*10+praise_count*5+comment_count*2+view_count) desc limit ?";
        if (node > 0L) {
            sql = "select id from questions where status=0 and node=" + node + " and YEARWEEK(date_format(insert_date,'%Y-%m-%d'))= YEARWEEK(now())" +
                    " order by (collect_count*10+praise_count*5+comment_count*2+view_count) desc limit ?";
        }
        List<Long> ids = getDbQuery().query_cache(long.class, isCacheNullObject(), getCache_region(), "hots#" + node + "#" + limit, sql, limit);
        return Question.ME.loadList(ids);
    }

    public void evictNode(long node) {
        CacheMgr.evict(getCache_region(), "status#0" + node);
        CacheMgr.evict(getCache_region(), "count#0" + node);
        CacheMgr.evict(getCache_region(), "status#0" + 0);
        CacheMgr.evict(getCache_region(), "count#0" + 0);
    }

    public void evictTops() {
        CacheMgr.evict(getCache_region(), "tops#" + Question.SYSTEM_LIMIT_TOP);
    }
}
