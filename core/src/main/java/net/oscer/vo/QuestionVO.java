package net.oscer.vo;

import net.oscer.beans.*;
import net.oscer.dao.CollectQuestionDAO;
import net.oscer.framework.FormatTool;
import net.oscer.framework.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子查询
 *
 * @author kz
 * @create 2019-05-28 18:35
 **/
public class QuestionVO {

    /**
     * 帖子
     */
    private Question q;
    /**
     * 帖子所属节点
     */
    private Node n;
    /**
     * 帖子作者
     */
    private UserVO q_user;
    /**
     * 最后的评论用户
     */
    private UserVO c_user;
    /**
     * 悬赏帖子，最佳评论用户
     */
    private UserVO r_user;
    /**
     * 悬赏帖子，最佳评论
     */
    private CommentQuestion reward_comment;

    /**
     * 登录用户
     */
    private UserVO login_user;

    /**
     * 登录用户是否收藏该帖子
     */
    private boolean collected;

    /**
     * 帖子背景图片
     */
    private String banner;

    /**
     * 帖子多张图片
     */
    private List<String> banners;

    private boolean hasBanner;
    /**
     * 1~3 1：单图（居左） 2：单图或视频（居右） 3：多图或无图
     */
    private int type;

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

    public UserVO getQ_user() {
        return q_user;
    }

    public void setQ_user(UserVO q_user) {
        this.q_user = q_user;
    }

    public UserVO getC_user() {
        return c_user;
    }

    public void setC_user(UserVO c_user) {
        this.c_user = c_user;
    }

    public UserVO getR_user() {
        return r_user;
    }

    public void setR_user(UserVO r_user) {
        this.r_user = r_user;
    }

    public CommentQuestion getReward_comment() {
        return reward_comment;
    }

    public void setReward_comment(CommentQuestion reward_comment) {
        this.reward_comment = reward_comment;
    }

    public UserVO getLogin_user() {
        return login_user;
    }

    public void setLogin_user(UserVO login_user) {
        this.login_user = login_user;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public List<String> getBanners() {
        return banners;
    }

    public void setBanners(List<String> banners) {
        this.banners = banners;
    }

    public boolean isHasBanner() {
        return hasBanner;
    }

    public void setHasBanner(boolean hasBanner) {
        this.hasBanner = hasBanner;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static List<QuestionVO> list(List<Question> questions, User login_user, String rhtml) {
        if (CollectionUtils.isEmpty(questions)) {
            return null;
        }
        List<Long> all_users = new ArrayList<>();
        questions.stream().filter(q -> q != null && q.getId() > 0L).forEach(q -> {
            all_users.add(q.getUser());
            if (q.getLast_comment_user() > 0L) {
                all_users.add(q.getLast_comment_user());
            }
        });

        if (CollectionUtils.isEmpty(all_users)) {
            return null;
        }

        User.ME.loadList(all_users);
        List<QuestionVO> list = new ArrayList<>();

        List<Long> collect_ids = new ArrayList<>();
        if (login_user != null) {
            List<CollectQuestion> collects = CollectQuestionDAO.ME.list(login_user.getId(), CollectQuestion.STATUS_SHOW);
            if (CollectionUtils.isNotEmpty(collects)) {
                collect_ids = collects.stream().map(CollectQuestion::getQuestion).collect(Collectors.toList());
            }
        }

        List<Long> finalCollect_ids = collect_ids;
        questions.stream().filter(q -> q != null && q.getId() > 0L).forEach(q -> {
            if (StringUtils.equalsIgnoreCase(rhtml, "1")) {
                q.setTitle(FormatTool.reHtml(q.getTitle()));
            }
            QuestionVO vo = new QuestionVO();
            vo.setQ(q);
            if (q.getNode() > 0) {
                vo.setN(Node.ME.get(q.getNode()));
            }
            vo.setQ_user(UserVO.convert(User.ME.get(q.getUser())));
            if (q.getLast_comment_user() > 0L) {
                vo.setC_user(UserVO.convert(User.ME.get(q.getLast_comment_user())));
            }
            if (q.getReward_comment() > 0L) {
                CommentQuestion commentQuestion = CommentQuestion.ME.get(q.getReward_comment());
                if (commentQuestion != null) {
                    User r_user = User.ME.get(commentQuestion.getUser());
                    vo.setR_user(UserVO.convert(r_user));
                    vo.setReward_comment(commentQuestion);
                }
            }
            if (login_user != null) {
                vo.setLogin_user(UserVO.convert(login_user));
                vo.setCollected(CollectionUtils.isNotEmpty(finalCollect_ids) ? finalCollect_ids.contains(q.getId()) : false);
            }
            //vo.setBanner(StringUtils.getFirstImageUrl(q.getContent()));
            vo.setBanners(StringUtils.getMoreImageUrl(q.getContent(), 3));
            vo.setType(3);
            if (CollectionUtils.isEmpty(vo.getBanners()) || vo.getBanners().size() == 3) {
                vo.setType(3);
            }
            if (CollectionUtils.isNotEmpty(vo.getBanners()) && vo.getBanners().size() == 1) {
                vo.setType(2);
            }
            vo.setHasBanner(false);
            if (StringUtils.isNotBlank(vo.getBanner())) {
                vo.setHasBanner(true);
            }
            list.add(vo);
        });
        return list;
    }
}
