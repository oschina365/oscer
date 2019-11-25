package net.oscer.dao;

import net.oscer.beans.LastMsg;
import net.oscer.beans.Msg;
import net.oscer.db.CacheMgr;
import net.oscer.db.DbQuery;
import net.oscer.db.TransactionService;
import net.oscer.framework.StringUtils;

import java.util.List;

/**
 * 私信详细表
 *
 * @author kz
 * @create 2019-11-19 17:26
 **/
public class MsgDAO extends CommonDao<Msg> {

    public final static MsgDAO ME = new MsgDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 系统私信数量
     *
     * @param user
     * @return
     */
    public int count_system(long user) {
        String sql = "select count(*) from msgs where user=? and type=0";
        return getDbQuery().stat_cache(getCache_region(), "count#" + user, sql, user);
    }

    /**
     * 与某用户私信数量
     *
     * @param user
     * @return
     */
    public int count(long user, long receiver) {
        String sql = "select count(*) from msgs where user=? and friend=?";
        return getDbQuery().stat_cache(getCache_region(), "count#" + user + "#" + receiver, sql, user, receiver);
    }

    /**
     * 与某用户私信数量
     *
     * @param user
     * @return
     */
    public int count(long user, long receiver, int type) {
        String sql = "select count(*) from msgs where user=? and friend=?";
        return getDbQuery().stat_cache(getCache_region(), "count#" + user + "#" + receiver + "#" + type, sql, user, receiver);
    }

    /**
     * 分页获取用户的所有系统私信
     *
     * @param user
     * @param page
     * @param size
     * @return
     */
    public List<Msg> msgs_system(long user, int page, int size) {
        String sql = "select id from msgs where user=? and type=0 order by id desc";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "msgs#" + user, 20, sql, page, size, user);
        return Msg.ME.loadList(ids);
    }

    /**
     * 分页获取所有与某用户私信
     *
     * @param user
     * @param page
     * @param size
     * @return
     */
    public List<Msg> msgs(long user, long receiver, int page, int size) {
        String sql = "select id from msgs where user=? and friend=? order by id desc";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "msgs#" + user + "#" + receiver, 20, sql, page, size, user, receiver);
        return Msg.ME.loadList(ids);
    }

    /**
     * 分页获取所有与某用户私信
     *
     * @param user
     * @param page
     * @param size
     * @return
     */
    public List<Msg> msgs(long user, long receiver, int type, int page, int size) {
        String sql = "select id from msgs where user=? and friend=? and type=? order by id desc";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "msgs#" + user + "#" + receiver + "#" + type, 20, sql, page, size, user, receiver, type);
        return Msg.ME.loadList(ids);
    }

    public void evict(long user, long receiver) {
        CacheMgr.evict(getCache_region(), "count#" + user + "#" + receiver);
        CacheMgr.evict(getCache_region(), "msgs#" + user + "#" + receiver);
        CacheMgr.evict(getCache_region(), "count#" + user + "#" + receiver + "#0");
        CacheMgr.evict(getCache_region(), "msgs#" + user + "#" + receiver + "#0");
        CacheMgr.evict(getCache_region(), "count#" + user + "#" + receiver + "#1");
        CacheMgr.evict(getCache_region(), "msgs#" + user + "#" + receiver + "#1");
        CacheMgr.evict(getCache_region(), "count#" + user);
        CacheMgr.evict(getCache_region(), "msgs#" + user);
    }

    public Msg construct(long sender, long receiver, String content, int type, String source) {
        Msg m = new Msg();
        m.setUser(receiver);
        m.setFriend(sender);
        m.setSender(sender);
        m.setReceiver(receiver);
        m.setContent(content);
        m.setType(type);
        m.setSource(source);
        return m;
    }

    public boolean send(long sender, long receiver, String content, int type, String source) throws Exception {
        if (sender <= 0L || receiver <= 0L || StringUtils.isEmpty(content)) {
            return false;
        }
        if (sender <= 2L || receiver <= 2L) {
            type = 0;
        }
        int finalType = type;
        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {
                Msg send = construct(sender, receiver, content, finalType, source);
                Msg rece = construct(receiver, sender, content, finalType, source);
                rece.setSender(sender);
                rece.setReceiver(receiver);
                long send_id = send.save();
                long rece_id = rece.save();
                LastMsg s = LastMsgDAO.ME.construct(sender, receiver, content, finalType, source, send_id);
                LastMsg r = LastMsgDAO.ME.construct(receiver, sender, content, finalType, source, rece_id);
                LastMsgDAO.ME.saveOrUpdate(s);
                LastMsgDAO.ME.saveOrUpdate(r);
                evict(sender, receiver);
                evict(receiver, sender);
                LastMsgDAO.ME.evict(sender, receiver, finalType);
            }
        });
        return true;
    }
}
