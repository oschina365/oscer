package net.oscer.vo;

import net.oscer.beans.CommentQuestion;
import net.oscer.beans.Question;
import net.oscer.beans.User;
import net.oscer.framework.FormatTool;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户评论帖子列表
 *
 * @author kz
 * @create 2019-08-22 14:58
 **/
public class UserCommentVO extends CommentQuestion {

    /**
     * 帖子作者
     */
    private UserVO author;
    private Question q;
    private String question_pub_time;


    public UserVO getAuthor() {
        return author;
    }

    public void setAuthor(UserVO author) {
        this.author = author;
    }

    public Question getQ() {
        return q;
    }

    public void setQ(Question q) {
        this.q = q;
    }

    public String getQuestion_pub_time() {
        return question_pub_time;
    }

    public void setQuestion_pub_time(String question_pub_time) {
        this.question_pub_time = question_pub_time;
    }

    /**
     * 用户的帖子评论记录
     * 每个帖子取最后一条评论
     *
     * @param commentQuestions
     * @return
     */
    public static List<UserCommentVO> listUserComments(List<CommentQuestion> commentQuestions) {
        if (CollectionUtils.isEmpty(commentQuestions)) {
            return null;
        }

        List<Long> ids = commentQuestions.stream().map(CommentQuestion::getQuestion).collect(Collectors.toList());
        Question.ME.loadList(ids);

        List<UserCommentVO> list = new ArrayList<>();

        for (CommentQuestion c : commentQuestions) {
            UserCommentVO vo = new UserCommentVO();
            Question q = Question.ME.get(c.getQuestion());
            if (q == null) {
                continue;
            }
            User author = User.ME.get(q.getUser());
            if (author == null || !author.status_is_normal()) {
                continue;
            }
            vo.setAuthor(UserVO.convert(author));
            vo.setQ(q);
            vo.setQuestion(c.getQuestion());
            vo.setQuestion_pub_time(FormatTool.format_intell_time(q.getInsert_date()));
            vo.setContent(c.getContent());
            list.add(vo);
        }
        return list;
    }
}
