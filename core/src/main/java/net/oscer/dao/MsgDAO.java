package net.oscer.dao;

import net.oscer.beans.Msg;

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
}
