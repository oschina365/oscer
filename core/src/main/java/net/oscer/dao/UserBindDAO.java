package net.oscer.dao;

import net.oscer.beans.UserBind;
import net.oscer.db.CacheMgr;
import net.oscer.framework.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 第三方登录
 *
 * @author kz
 * @create 2019-04-15 14:49
 **/
public class UserBindDAO extends CommonDao<UserBind> {

    public static final UserBindDAO ME = new UserBindDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public UserBind bindByName(String provider, String name) {
        String sql = "select id from user_binds where provider=? and name=?";
        Number id = getDbQuery().read(Number.class, sql, provider, name);
        if (null == id) {
            return null;
        }
        return UserBind.ME.get(id.longValue());
    }

    public UserBind bindByUnion_id(String provider, String union_id, String name) {
        if (StringUtils.isBlank(provider) || StringUtils.isBlank(union_id)) {
            return null;
        }
        String sql = "select id from user_binds where provider=? and union_id=? and name=? limit 1";
        Number id = getDbQuery().read(Number.class, sql, provider, union_id, name);
        if (null == id) {
            return null;
        }
        return UserBind.ME.get(id.longValue());
    }

    /**
     * 列出用户绑定的第三方账号列表
     *
     * @param user
     * @return
     */
    public List<UserBind> listByUser(long user) {
        if (user <= 0L) {
            return null;
        }
        String sql = "select id from user_binds where user=? order by id desc";
        List<Number> ids = getDbQuery().query_cache(Number.class, false, UserBind.ME.CacheRegion(), "user#" + user, sql, user);
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        List<Long> s = ids.stream().map(Number::longValue).collect(Collectors.toList());
        return UserBind.ME.loadList(s);
    }

    public void evict(long user) {
        CacheMgr.evict(UserBind.ME.CacheRegion(), "user#" + user);
    }
}
