package net.oscer.vo;

import net.oscer.beans.CommentQuestion;
import net.oscer.beans.Question;
import net.oscer.beans.User;
import net.oscer.framework.FormatTool;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 回帖
 *
 * @author kz
 * @create 2019-05-31 18:29
 **/
public class CommentQuestionVO {

    /**
     * 帖子评论
     */
    private CommentQuestion cq;
    /**
     * 评论用户
     */
    private User cu;

    private String sdf_insert_date;

    /**
     * 次条评论是否是最佳答案
     */
    private boolean bestComment;

    /**
     * 当前登录用户是否给予次条评论点赞
     */
    private boolean praise;

    public CommentQuestion getCq() {
        return cq;
    }

    public void setCq(CommentQuestion cq) {
        this.cq = cq;
    }

    public User getCu() {
        return cu;
    }

    public void setCu(User cu) {
        this.cu = cu;
    }

    public String getSdf_insert_date() {
        return sdf_insert_date;
    }

    public void setSdf_insert_date(String sdf_insert_date) {
        this.sdf_insert_date = sdf_insert_date;
    }

    public boolean isBestComment() {
        return bestComment;
    }

    public void setBestComment(boolean bestComment) {
        this.bestComment = bestComment;
    }

    public boolean isPraise() {
        return praise;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

    public static List<CommentQuestionVO> list(Long question, User login_user, List<CommentQuestion> commentQuestions) {
        if (CollectionUtils.isEmpty(commentQuestions)) {
            return null;
        }

        List<Long> all_users = new ArrayList<>();
        commentQuestions.stream().filter(c -> c != null && c.getId() > 0L).forEach(c -> {
            all_users.add(c.getUser());
            if (c.getReply_user() > 0L) {
                all_users.add(c.getReply_user());
            }
        });
        if (CollectionUtils.isEmpty(all_users)) {
            return null;
        }
        User.ME.loadList(all_users);

        Question q = Question.ME.get(question);

        boolean has_praise = false;
        if (null != login_user) {

        }

        List<CommentQuestionVO> list = new ArrayList<>();
        commentQuestions.stream().forEach(cq -> {
            CommentQuestionVO vo = new CommentQuestionVO();
            vo.setCq(cq);
            vo.setCu(User.ME.get(cq.getUser()));
            vo.setSdf_insert_date(FormatTool.format_intell_time(cq.getInsert_date()));
            vo.setBestComment(false);
            if (cq.getId() == q.getReward_comment()) {
                vo.setBestComment(true);
            }
            list.add(vo);
        });
        return list;
    }
}
