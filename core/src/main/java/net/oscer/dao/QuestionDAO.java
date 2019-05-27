package net.oscer.dao;

import net.oscer.beans.Question;
import net.oscer.common.ApiResult;
import net.oscer.common.PatternUtil;
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

    public int count(int status) {
        String sql = "select count(id) from questions where status=0 ";
        return getDbQuery().stat_cache(getCache_region(), "count#0", sql);
    }

    public List<Question> all(int page, int size) {
        String sql = "select id from questions where status=0 order by id desc";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "status#0", 100, sql, page, size);
        return Question.ME.loadList(ids);
    }
}
