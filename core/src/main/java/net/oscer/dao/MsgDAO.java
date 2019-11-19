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
     * 与某用户私信数量
     *
     * @param user
     * @return
     */
    public int count(long user, long receiver) {
        String sql = "select count(*) from msgs where sender=? and receiver=?";
        return getDbQuery().stat_cache(getCache_region(), "count#" + user + "#" + receiver, sql, user, receiver);
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
        String sql = "select id from msgs where sender=? and receiver=? order by id desc";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "msgs#" + user + "#" + receiver, 20, sql, page, size, user);
        return Msg.ME.loadList(ids);
    }

    public void evict(long user, long receiver) {
        CacheMgr.evict(getCache_region(), "count#" + user + "#" + receiver);
        CacheMgr.evict(getCache_region(), "msgs#" + user + "#" + receiver);
    }

    public Msg construct(long sender, long receiver, String content, int type, String source) {
        Msg m = new Msg();
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
        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {
                Msg send = construct(sender, receiver, content, type, source);
                Msg rece = construct(receiver, sender, content, type, source);
                long send_id = send.save();
                long rece_id = rece.save();
                LastMsg s = LastMsgDAO.ME.construct(sender, receiver, content, type, source, send_id);
                LastMsg r = LastMsgDAO.ME.construct(receiver, sender, content, type, source, rece_id);
                LastMsgDAO.ME.saveOrUpdate(s);
                LastMsgDAO.ME.saveOrUpdate(r);
                evict(sender, receiver);
                evict(receiver, sender);
                LastMsgDAO.ME.evict(sender, receiver, type);
            }
        });
        return true;
    }
}
