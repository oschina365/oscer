package net.oscer.dao;

import net.oscer.beans.Remind;
import net.oscer.db.CacheMgr;
import net.oscer.db.Entity;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class RemindDAO extends CommonDao<Remind> {

    public static final RemindDAO ME = new RemindDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 查询用户的未读消息
     *
     * @param user_id
     * @return
     */
    public List<Remind> unReadList(long user_id) {
        if (user_id <= 0L) {
            return null;
        }
        String sql = "select id from reminds where refer=? and status_read = ? order by id desc";
        List<Long> ids = getDbQuery().query(long.class, sql, user_id, Remind.STATUS_UN_READED);
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        return Remind.ME.loadList(ids);
    }

    /**
     * 消息已读
     *
     * @param refer
     * @param id
     */
    public void read(long refer, long id) {
        if (id > 0L) {
            String sql = "update reminds set status_read=? where obj_id =? and refer=? and status_read=0";
            getDbQuery().update(sql, Remind.STATUS_READED, id, refer);
        }
    }
}
