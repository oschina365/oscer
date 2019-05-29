package net.oscer.dao;

import net.oscer.beans.CommentQuestion;

/**
 * 帖子评论
 *
 * @author MRCHENIKE
 * @create 2019-05-29 11:54
 **/
public class CommentQuestionDAO extends CommonDao<CommentQuestion> {

    @Override
    protected String databaseName() {
        return "mysql";
    }
}
