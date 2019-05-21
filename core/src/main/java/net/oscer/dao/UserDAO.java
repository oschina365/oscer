package net.oscer.dao;

import net.oscer.beans.User;
import org.apache.commons.lang3.StringUtils;


public class UserDAO extends CommonDao<User> {

    public static final UserDAO ME = new UserDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    /**
     * 查询用户
     *
     * @param id
     * @return
     */
    public User getById(long id) {
        return User.ME.get(id);
    }

    public User getByIdent(String ident) {
        if (StringUtils.isBlank(ident)) {
            return null;
        }
        String sql = "select id from users where ident =?";
        Number n = getDbQuery().read(Number.class, sql, ident);
        if (n == null || n.longValue() <= 0L) {
            return null;
        }
        return User.ME.get(n.longValue());
    }


}
