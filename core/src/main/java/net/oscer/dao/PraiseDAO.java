package net.oscer.dao;

import net.oscer.beans.Praise;
import net.oscer.enums.TypePraiseEnum;
import net.oscer.framework.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author kz
 * @date 2019年1月3日17:17:25
 */
public class PraiseDAO extends CommonDao<Praise> {

    public static final PraiseDAO ME = new PraiseDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public List<Praise> list(int type, long user) {
        if (user <= 0L || TypePraiseEnum.type_map.get(type) == null) {
            return null;
        }
        String sql = "select id from praises where type=? and user_id=? ";
        List<Long> ids = getDbQuery().query_cache(long.class, isCacheNullObject(), Praise.ME.CacheRegion(), "list#" + type + "#" + user, sql, type, user);
        return Praise.ME.loadList(ids);
    }

    public boolean has_praise(long obj_id, int type, List<Praise> list) {
        if (obj_id <= 0L || TypePraiseEnum.type_map.get(type) == null || CollectionUtils.isEmpty(list)) {
            return false;
        }
        return list.stream().anyMatch(p -> p.getObj_id() == obj_id && p.getType() == type);
    }

    /**
     * 根据类型，对象ID，点赞用户ID查询一条记录
     *
     * @param type    1：博客 2：动弹 3：评论 ,TypePraiseEnum
     * @param obj_id
     * @param user_id
     * @return
     */
    public Praise selectOneByTypeObjUserId(int type, long obj_id, long user_id) {
        if (obj_id <= 0L || user_id <= 0L || TypePraiseEnum.type_map.get(type) == null) {
            return null;
        }
        String sql = "select id from praises where type=? and obj_id=? and user_id=?";
        return Praise.ME.getById(Praise.ME, sql, type, obj_id, user_id);
    }

    /**
     * 查询一组数据
     *
     * @param type
     * @param user_id
     * @param obj_ids
     * @return
     */
    public List<Praise> listByTypeUserIdBlogIds(int type, long user_id, List<Long> obj_ids) {
        if (user_id <= 0L || TypePraiseEnum.type_map.get(type) == null || CollectionUtils.isEmpty(obj_ids)) {
            return null;
        }
        String sql = "select id from praises where type=? and user_id=? and obj_id in (" + StringUtils.join(obj_ids, ",") + ")";
        return Praise.ME.loadList(getDbQuery().query(long.class, sql, type, user_id));
    }

    public int option(Praise p, long obj_id, long user_id, int type) {
        Praise old = PraiseDAO.ME.selectOneByTypeObjUserId(type, obj_id, user_id);

        p.setStatus(Praise.PRAISE_YES);
        //点赞
        int praise_count = 1;
        if (old != null && old.getStatus() == Praise.PRAISE_YES) {
            //取消点赞
            praise_count = -1;
            p.setStatus(Praise.PRAISE_NO);
        }
        p.setUser_id(user_id);
        p.setType(type);
        if (old == null || old.getId() <= 0L) {
            p.save();
        } else {
            p.setId(old.getId());
            p.setLast_date(new Date());
            p.doUpdate();
            p.evict(true);
        }
        return praise_count;
    }
}
