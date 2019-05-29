package net.oscer.vo;

import net.oscer.beans.Node;
import net.oscer.beans.Question;
import net.oscer.beans.User;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子查询
 *
 * @author kz
 * @create 2019-05-28 18:35
 **/
public class QuestionVO {

    private Question q;
    private Node n;
    private User q_user;
    private User c_user;
    private User r_user;

    public Question getQ() {
        return q;
    }

    public void setQ(Question q) {
        this.q = q;
    }

    public Node getN() {
        return n;
    }

    public void setN(Node n) {
        this.n = n;
    }

    public User getQ_user() {
        return q_user;
    }

    public void setQ_user(User q_user) {
        this.q_user = q_user;
    }

    public User getC_user() {
        return c_user;
    }

    public void setC_user(User c_user) {
        this.c_user = c_user;
    }

    public User getR_user() {
        return r_user;
    }

    public void setR_user(User r_user) {
        this.r_user = r_user;
    }

    public static List<QuestionVO> list(List<Question> questions) {
        if (CollectionUtils.isEmpty(questions)) {
            return null;
        }
        List<Long> all_users = new ArrayList<>();
        questions.stream().filter(q -> q != null && q.getId() > 0L).forEach(q -> {
            all_users.add(q.getUser());
            if (q.getLast_comment_user() > 0L) {
                all_users.add(q.getLast_comment_user());
            }
            if (q.getReward_user() > 0L) {
                all_users.add(q.getReward_user());
            }
        });

        if (CollectionUtils.isEmpty(all_users)) {
            return null;
        }

        User.ME.loadList(all_users);
        List<QuestionVO> list = new ArrayList<>();
        questions.stream().filter(q -> q != null && q.getId() > 0L).forEach(q -> {
            QuestionVO vo = new QuestionVO();
            vo.setQ(q);
            if (q.getNode() > 0) {
                vo.setN(Node.ME.get(q.getNode()));
            }
            vo.setQ_user(User.ME.get(q.getUser()));
            if (q.getLast_comment_user() > 0L) {
                vo.setQ_user(User.ME.get(q.getLast_comment_user()));
            }
            if (q.getReward_user() > 0L) {
                vo.setQ_user(User.ME.get(q.getReward_user()));
            }
            list.add(vo);
        });
        return list;
    }
}
