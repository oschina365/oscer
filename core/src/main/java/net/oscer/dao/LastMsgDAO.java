package net.oscer.dao;

import net.oscer.beans.LastMsg;

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
}
