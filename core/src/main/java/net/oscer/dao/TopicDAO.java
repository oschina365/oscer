package net.oscer.dao;


import net.oscer.beans.Topic;
import net.oscer.common.ApiResult;
import net.oscer.framework.StringUtils;

import java.util.List;

/**
 * @author kz
 * @date 2019/01/23ll
 */
public class TopicDAO extends CommonDao<Topic> {

    public static final TopicDAO ME = new TopicDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 保存话题
     *
     * @param user_id
     * @param tweet_id
     * @param topic_name
     * @return
     */
    public ApiResult save_topic(long user_id, long tweet_id, String topic_name) {
        if (user_id <= 0L || tweet_id <= 0L || StringUtils.isBlank(topic_name)) {
            return ApiResult.failWithMessage("");
        }
        Topic t = new Topic();
        t.setUser_id(user_id);
        t.setName(topic_name);
        t.setTweet_id(tweet_id);
        t.save();
        return ApiResult.success();
    }

    /**
     * 查询话题
     *
     * @param name
     * @param page
     * @param size
     * @return
     */
    public List<Topic> listByName(String name, int page, int size) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        String sql = "select id from topics where status=0 and name =? order by id desc";
        return Topic.ME.sliceByIds(Topic.ME, sql, page, size, name);
    }


}
