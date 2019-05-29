package net.oscer.beans;

import net.oscer.dao.NodeDAO;
import net.oscer.db.Entity;
import net.oscer.framework.FormatTool;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 帖子
 * </p>
 *
 * @author kz
 * @since 2019-05-23
 */
@Entity.Cache(region = "Question")
public class Question extends Entity {

    public final static Question ME = new Question();

    /**
     * 默认最小的悬赏积分
     */
    public static final int REWARD_POINT_DEFAULT = 10;

    public final static List<Integer> reward_points = Arrays.asList(10, 20, 50, 100);

    /**
     * 标题最大长度
     */
    public static final int MAX_LENGTH_TITLE = 150;

    /**
     * 原创
     */
    public static final int ORIGINAL_0 = 0;

    /**
     * 转帖
     */
    public static final int ORIGINAL_1 = 1;

    /**
     * 查询多少条置顶帖子
     */
    public static final int SYSTEM_LIMIT_TOP = 5;

    /**
     * 用户ID
     */
    private long user;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 状态（0：显示 1：隐藏 2：待审核）
     */
    private Integer status;
    /**
     * 是否置顶（1：置顶），贴主自己置顶
     */
    private Integer top;
    /**
     * 视图封面
     */
    private String banner;
    /**
     * 阅读量
     */
    private Integer view_count;
    /**
     * 收藏量
     */
    private Integer collect_count;
    /**
     * 点赞量
     */
    private Integer praise_count;
    /**
     * 评论量
     */
    private Integer comment_count;

    /**
     * 是否禁止评论（1：禁止）
     */
    private Integer forbid_comment;
    /**
     * 是否推荐（1：推荐）
     */
    private Integer recomm;
    /**
     * 是否原创（1：不是，即转帖）
     */
    private Integer original;
    /**
     * 转帖地址
     */
    private String original_url;
    /**
     * 最后的回帖人
     */
    private long last_comment_user;
    /**
     * 最后的回帖时间
     */
    private Date last_comment_time;
    /**
     * 悬赏的积分
     */
    private Integer reward_point;
    /**
     * 被悬赏者，即最佳回帖人
     */
    private long reward_user;

    /**
     * 节点ID
     */
    private long node;

    private String sdf_insert_date;

    private String sdf_last_date;

    /**
     * 添加时间
     */
    public Date insert_date;
    /**
     * 更新时间
     */
    public Date last_date;

    /**
     * 系统置顶
     */
    public int system_top;

    @Override
    public Date getInsert_date() {
        return insert_date;
    }

    @Override
    public void setInsert_date(Date insert_date) {
        this.insert_date = insert_date;
        this.sdf_insert_date = format_insert_date();
    }

    @Override
    public Date getLast_date() {
        return last_date;
    }

    @Override
    public void setLast_date(Date last_date) {
        this.last_date = last_date;
        this.sdf_last_date = format_last_date();
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Integer getView_count() {
        return view_count;
    }

    public void setView_count(Integer view_count) {
        this.view_count = view_count;
    }

    public Integer getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(Integer collect_count) {
        this.collect_count = collect_count;
    }

    public Integer getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(Integer praise_count) {
        this.praise_count = praise_count;
    }

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    public Integer getForbid_comment() {
        return forbid_comment;
    }

    public void setForbid_comment(Integer forbid_comment) {
        this.forbid_comment = forbid_comment;
    }

    public Integer getRecomm() {
        return recomm;
    }

    public void setRecomm(Integer recomm) {
        this.recomm = recomm;
    }

    public Integer getOriginal() {
        return original;
    }

    public void setOriginal(Integer original) {
        this.original = original;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public long getLast_comment_user() {
        return last_comment_user;
    }

    public void setLast_comment_user(long last_comment_user) {
        this.last_comment_user = last_comment_user;
    }

    public Date getLast_comment_time() {
        return last_comment_time;
    }

    public void setLast_comment_time(Date last_comment_time) {
        this.last_comment_time = last_comment_time;
    }

    public Integer getReward_point() {
        return reward_point;
    }

    public void setReward_point(Integer reward_point) {
        this.reward_point = reward_point;
    }

    public long getReward_user() {
        return reward_user;
    }

    public void setReward_user(long reward_user) {
        this.reward_user = reward_user;
    }

    public long getNode() {
        return node;
    }

    public void setNode(long node) {
        this.node = node;
    }

    public String getSdf_insert_date() {
        return sdf_insert_date;
    }

    public void setSdf_insert_date(String sdf_insert_date) {
        this.sdf_insert_date = sdf_insert_date;
    }

    public String getSdf_last_date() {
        return sdf_last_date;
    }

    public void setSdf_last_date(String sdf_last_date) {
        this.sdf_last_date = sdf_last_date;
    }

    public int getSystem_top() {
        return system_top;
    }

    public void setSystem_top(int system_top) {
        this.system_top = system_top;
    }

    public long NodeOrDefault(long node) {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        if (CollectionUtils.isNotEmpty(nodes)) {
            if (nodes.stream().filter(n -> StringUtils.isNotBlank(n.getUrl())).anyMatch(n -> n.getId() == node)) {
                return node;
            }

        }
        return 0;
    }

    /**
     * 获取悬赏积分
     *
     * @param choose_reward_point
     * @return
     */
    public int RewardPointOrDefault(int choose_reward_point) {
        if (reward_points.contains(choose_reward_point)) {
            return choose_reward_point;
        }
        return REWARD_POINT_DEFAULT;
    }

    public String format_insert_date() {
        return FormatTool.format_intell_time(this.insert_date);
    }

    public String format_last_date() {
        return FormatTool.format_intell_time(this.last_date);
    }


}
