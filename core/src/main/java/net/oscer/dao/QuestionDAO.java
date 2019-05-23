package net.oscer.dao;

import net.oscer.beans.Question;

/**
 * 帖子
 *
 * @author kz
 * @create 2019-05-23 18:29
 **/
public class QuestionDAO extends CommonDao<Question> {

    public final static QuestionDAO ME = new QuestionDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }
}
