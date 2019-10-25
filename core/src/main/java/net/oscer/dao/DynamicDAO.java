package net.oscer.dao;

import net.oscer.beans.Dynamic;

/**
 * 动态表
 *
 * @author kz
 * @create 2019-10-25 18:10
 **/
public class DynamicDAO extends CommonDao<Dynamic>{

    public static final DynamicDAO ME = new DynamicDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

}
