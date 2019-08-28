package net.oscer.service;

import net.oscer.beans.Question;
import net.oscer.db.CacheMgr;
import net.oscer.db.DbQuery;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.oscer.db.Entity.CACHE_VIEW;
import static net.oscer.enums.ViewEnum.*;

/**
 * 阅读统计
 *
 * @author kz
 * @create 2019-06-26 16:29
 **/
public class ViewService {

    public static final String SEPARATOR = "_";

    public static void keyCache(long id, String type) {
        if (id <= 0L || type_map.get(type) == null) {
            return;
        }
        setCache(type + SEPARATOR + id);
    }

    public static void setCache(String key) {
        Object o = CacheMgr.get(CACHE_VIEW, key);
        if (o == null) {
            CacheMgr.set(CACHE_VIEW, key, 1);
            return;
        }
        if (o != null && !(o instanceof Number)) {
            return;
        }
        int i = (int) o;
        CacheMgr.set(CACHE_VIEW, key, (i + 1));
    }

    public static int count(String key) {
        Object o = CacheMgr.get(CACHE_VIEW, key);
        if (o == null) {
            return 0;
        }
        return (Integer) o;
    }

    /**
     * 获取所有的key
     *
     * @return
     */
    public static List<String> keys() {
        return new ArrayList<>(CacheMgr.keys(CACHE_VIEW));
    }

    public static void run() {
        List<String> keys = keys();
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        for (String key : keys) {
            String[] arrays = key.split(SEPARATOR);
            if (arrays == null || arrays.length < 1) {
                return;
            }
            String type = arrays[0];
            long id = Long.valueOf(arrays[1]);
            Map<Long, Integer> map = new HashMap<>();
            if (!type_list.contains(type)) {
                return;
            }
            int count = count(key);

            if (type.equalsIgnoreCase(TYPE.QUESTION.getKey())) {
                Question q = Question.ME.get(id);
                if (q != null) {
                    String sql = "update questions set view_count =? where id =?";
                    /*q.setView_count(q.getView_count() + count);
                    q.doUpdate();*/
                    DbQuery.get("mysql").update(sql, q.getView_count() + count, q.getId());
                    q.evict(true);
                }

            } else if (type.equalsIgnoreCase(TYPE.BLOG.getKey())) {

            } else if (type.equalsIgnoreCase(TYPE.TWEET.getKey())) {

            }
            CacheMgr.evict(CACHE_VIEW, key);
        }
    }
}
