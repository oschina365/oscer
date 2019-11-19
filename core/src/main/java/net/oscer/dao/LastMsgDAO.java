package net.oscer.dao;

import net.oscer.beans.LastMsg;
import net.oscer.db.CacheMgr;

import java.util.List;

/**
 * 最后一条私信表
 *
 * @author kz
 * @create 2019-11-19 17:26
 **/
public class LastMsgDAO extends CommonDao<LastMsg> {

    public final static LastMsgDAO ME = new LastMsgDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 所有私信数量
     *
     * @param user
     * @return
     */
    public int count(long user) {
        String sql = "select count(*) from last_msgs where sender=?";
        return getDbQuery().stat_cache(getCache_region(), "count#" + user, sql, user);
    }

    /**
     * 分页获取所有私信
     *
     * @param user
     * @param page
     * @param size
     * @return
     */
    public List<LastMsg> msgs(long user, int page, int size) {
        String sql = "select id from last_msgs where sender=? order by msg_id desc";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "msgs#" + user, 20, sql, page, size, user);
        return LastMsg.ME.loadList(ids);
    }

    /**
     * 分类获取私信数量
     *
     * @param user
     * @param type
     * @return
     */
    public int count(long user, int type) {
        String sql = "select count(*) from last_msgs where sender=? and type=?";
        return getDbQuery().stat_cache(getCache_region(), "count#" + user + "type#" + type, sql, user, type);
    }

    /**
     * 分类分页获取私信
     *
     * @param user
     * @param type
     * @param page
     * @param size
     * @return
     */
    public List<LastMsg> msgs(long user, int type, int page, int size) {
        String sql = "select id from last_msgs where sender=? and type=? order by msg_id desc";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "msgs#" + user + "type#" + type, 20, sql, page, size, user);
        return LastMsg.ME.loadList(ids);
    }

    public void evict(long user, long receiver, int type) {
        evict(user, type);
        evict(receiver, type);
    }

    public void evict(long user, int type) {
        CacheMgr.evict(getCache_region(), "count#" + user);
        CacheMgr.evict(getCache_region(), "count#" + user + "type#" + type);
        CacheMgr.evict(getCache_region(), "msgs#" + user);
        CacheMgr.evict(getCache_region(), "msgs#" + user + "type#" + type);
    }

    public LastMsg construct(long sender, long receiver, String content, int type, String source, long msg_id) {
        LastMsg m = new LastMsg();
        m.setSender(sender);
        m.setReceiver(receiver);
        m.setContent(content);
        m.setType(type);
        m.setSource(source);
        m.setMsg_id(msg_id);
        return m;
    }

    public void saveOrUpdate(LastMsg m) {
        String sql = " insert into last_msgs (sender,receiver,content,type,source,msg_id)  values(?,?,?,?,?,?) on  DUPLICATE key " +
                "update sender=?,receiver=?,content=?,type=?,source=?,msg_id=?";
        getDbQuery().update(sql, m.getSender(), m.getReceiver(), m.getContent(), m.getType(), m.getSource(), m.getMsg_id(),
                m.getSender(), m.getReceiver(), m.getContent(), m.getType(), m.getSource(), m.getMsg_id());
    }


}
