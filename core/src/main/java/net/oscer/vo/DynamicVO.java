package net.oscer.vo;

import net.oscer.beans.CommentQuestion;
import net.oscer.beans.Dynamic;
import net.oscer.beans.Question;
import net.oscer.beans.User;
import net.oscer.db.Entity;
import net.oscer.framework.FormatTool;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 最新动态构造
 *
 * @author kz
 * @create 2019-11-18 16:19
 **/
public class DynamicVO {

    private Dynamic d;

    private String sdf_insert_date;

    /**
     * 帖子，评论或者回复评论，也要查询出所属帖子
     */
    private Question q;
    /**
     * 作者
     */
    private UserVO author;

    /**
     * 评论或回复评论
     */
    private CommentQuestion commentQuestion;

    public Dynamic getD() {
        return d;
    }

    public void setD(Dynamic d) {
        this.d = d;
    }

    public Question getQ() {
        return q;
    }

    public void setQ(Question q) {
        this.q = q;
    }

    public UserVO getAuthor() {
        return author;
    }

    public void setAuthor(UserVO author) {
        this.author = author;
    }

    public CommentQuestion getCommentQuestion() {
        return commentQuestion;
    }

    public void setCommentQuestion(CommentQuestion commentQuestion) {
        this.commentQuestion = commentQuestion;
    }

    public String getSdf_insert_date() {
        return sdf_insert_date;
    }

    public void setSdf_insert_date(String sdf_insert_date) {
        this.sdf_insert_date = sdf_insert_date;
    }

    /**
     * @param list
     * @return
     */
    public static List<DynamicVO> construct(List<Dynamic> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(DynamicVO::construct).collect(Collectors.toList());

    }

    public static DynamicVO construct(Dynamic d) {
        DynamicVO vo = new DynamicVO();
        User u = User.ME.get(d.getUser());
        if (u == null || u.getStatus() != Entity.STATUS_NORMAL) {
            return null;
        }
        Question question = Question.ME.get(d.getQuestion());
        if (question == null || question.getStatus() != 0) {
            return null;
        }
        CommentQuestion c = CommentQuestion.ME.get(d.getComment());
        if (d.getComment() > 0L && (c == null || c.getStatus() != 0)) {
            return null;
        }
        vo.setSdf_insert_date(FormatTool.format_intell_time(d.getInsert_date()));
        vo.setD(d);
        vo.setAuthor(UserVO.convert(u));
        vo.setQ(question);
        vo.setCommentQuestion(c);
        return vo;
    }
}
