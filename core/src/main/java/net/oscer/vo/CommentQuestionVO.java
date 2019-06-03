package net.oscer.vo;

import net.oscer.beans.CommentQuestion;
import net.oscer.beans.User;
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

    public static List<CommentQuestionVO> list(List<CommentQuestion> commentQuestions) {
        if (CollectionUtils.isEmpty(commentQuestions)) {
            return null;
        }
        List<Long> all_users = new ArrayList<>();
        commentQuestions.stream().filter(q -> q != null && q.getId() > 0L).forEach(q -> {
            all_users.add(q.getUser());
            if (q.getReply_user() > 0L) {
                all_users.add(q.getReply_user());
            }
        });
        if (CollectionUtils.isEmpty(all_users)) {
            return null;
        }
        User.ME.loadList(all_users);
        List<CommentQuestionVO> list = new ArrayList<>();
        commentQuestions.stream().forEach(cq -> {
            CommentQuestionVO vo = new CommentQuestionVO();
            vo.setCq(cq);
            vo.setCu(User.ME.get(cq.getUser()));
            list.add(vo);
        });
        return list;
    }
}
