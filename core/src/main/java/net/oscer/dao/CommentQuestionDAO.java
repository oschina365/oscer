package net.oscer.dao;

import net.oscer.beans.CommentQuestion;

import java.util.List;

/**
 * 帖子评论
 *
 * @author kz
 * @create 2019-05-29 11:54
 **/
public class CommentQuestionDAO extends CommonDao<CommentQuestion> {

    public static final CommentQuestionDAO ME = new CommentQuestionDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public List<CommentQuestion> weekHots() {
        String sql = "";
        return null;
    }
}
