package net.oscer.controller;

import net.oscer.beans.*;
import net.oscer.common.ApiResult;
import net.oscer.dao.CollectQuestionDAO;
import net.oscer.dao.CommentQuestionDAO;
import net.oscer.dao.NodeDAO;
import net.oscer.dao.QuestionDAO;
import net.oscer.db.CacheMgr;
import net.oscer.db.DbQuery;
import net.oscer.db.TransactionService;
import net.oscer.enums.ViewEnum;
import net.oscer.service.ViewService;
import net.oscer.vo.CommentQuestionVO;
import net.oscer.vo.QuestionVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.oscer.db.Entity.STATUS_NORMAL;

/**
 * 通用api，所有端通用
 *
 * @author kz
 * @create 2019-08-10 13:06
 **/
@RequestMapping("/uni/")
@Controller
public class UniController extends BaseController {

    /**
     * 获取所有节点名称
     *
     * @return
     */
    @PostMapping("nodes")
    @ResponseBody
    public ApiResult nodes() {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        Map<String, Object> map = new HashMap<>();
        map.put("nodes", nodes);
        return ApiResult.successWithObject(map);
    }

    /**
     * 帖子列表
     * 首页帖子列表，节点帖子列表
     *
     * @param id
     * @return
     */
    @PostMapping("q/list")
    @ResponseBody
    public ApiResult list(@RequestParam(value = "id", defaultValue = "0", required = false) Long id) {
        Map<String, Object> map = new HashMap<>();
        //帖子列表
        List<Question> questions = QuestionDAO.ME.all(id, pageNumber, 10);
        map.put("questions", QuestionVO.list(questions, getLoginUser()));
        //帖子总数
        int count = QuestionDAO.ME.count(id);
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }

    /**
     * 帖子详情
     *
     * @param id
     * @return
     */
    @PostMapping("q/{id}")
    @ResponseBody
    public ApiResult question_detail(@PathVariable("id") Long id) {
        Question q = Question.ME.get(id);
        if (null == q || q.getId() <= 0L) {
            return ApiResult.failWithMessage("帖子不存在");
        }
        if (q.getStatus() != 0) {
            return ApiResult.failWithMessage("帖子已屏蔽");
        }
        User u = User.ME.get(q.getUser());
        if (null == u || u.getId() <= 0L || u.getStatus() != User.STATUS_NORMAL) {
            return ApiResult.failWithMessage("用户被屏蔽");
        }
        ViewService.keyCache(q.getId(), ViewEnum.TYPE.QUESTION.getKey());
        List<Question> list = Arrays.asList(q);
        Map<String, Object> map = new HashMap<>();
        map.put("q", q);
        map.put("u", u);
        map.put("detail", QuestionVO.list(list, getLoginUser()));
        return ApiResult.successWithObject(map);
    }

    /**
     * 评论列表
     *
     * @param id
     * @return
     */
    @PostMapping("q/comments")
    @ResponseBody
    public ApiResult question_comments(@RequestParam(value = "id", defaultValue = "0", required = false) Long id) {
        User login_user = getLoginUser();
        Map<String, Object> map = new HashMap<>(2);
        //评论列表 --分页
        List<CommentQuestion> comments = CommentQuestionDAO.ME.list(id, pageNumber, pageSize);
        if (CacheMgr.exists(CommentQuestion.ME.CacheRegion(), "rankMap#" + id)) {
            map.put("rankMap", CacheMgr.get(CommentQuestion.ME.CacheRegion(), "rankMap#" + id));
        } else {
            //全部评论
            List<CommentQuestion> allComments = CommentQuestionDAO.ME.list(id);
            if (CollectionUtils.isNotEmpty(allComments)) {
                Map<Long, Integer> rankMap = new HashMap<>(allComments.size());
                for (int i = 0; i < allComments.size(); i++) {
                    rankMap.put(allComments.get(i).getId(), allComments.size() - i);
                }
                map.put("rankMap", rankMap);
                CacheMgr.set(CommentQuestion.ME.CacheRegion(), "rankMap#" + id, rankMap);
            }
        }
        map.put("comments", CommentQuestionVO.list(id, login_user, comments));
        //帖子总数
        int count = CommentQuestionDAO.ME.count(id);
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }

    /**
     * 删除帖子方法
     *
     * @return
     */
    @PostMapping("/q/delete/{id}")
    @ResponseBody
    public ApiResult delete(@PathVariable("id") Long id, @RequestParam(value = "user", required = true) Long user) throws Exception {
        User login_user = User.ME.get(user);
        if (login_user == null || !login_user.status_is_normal()) {
            return ApiResult.failWithMessage("请登录后重试");
        }
        if (id <= 0L) {
            return ApiResult.failWithMessage("不存在此帖子");
        }
        Question q = Question.ME.get(id);
        if (login_user.getId() != q.getUser()) {
            return ApiResult.failWithMessage("无权限删除此贴");
        }

        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {
                q.delete();
                CommentQuestionDAO.ME.delete(id);
                QuestionDAO.ME.evictNode(q.getNode());
            }
        });
        return ApiResult.success();
    }

    /**
     * 收藏/取消收藏
     *
     * @param id
     * @return
     */
    @PostMapping("/q/collect")
    @ResponseBody
    public ApiResult collect(@RequestParam("id") long id, @RequestParam(value = "user", required = false) Long user) throws Exception {
        User loginUser = null;
        if (user > 0L) {
            loginUser = User.ME.get(user);
        } else {
            loginUser = getLoginUser();
        }
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        Question q = Question.ME.get(id);
        if (null == q) {
            return ApiResult.failWithMessage("该帖子不存在");
        }
        if (q.getStatus() != 0) {
            return ApiResult.failWithMessage("该帖子已删除");
        }
        if (q.getUser() == loginUser.getId()) {
            return ApiResult.failWithMessage("自己的帖子不能被收藏");
        }
        final String[] message = {"收藏成功"};
        User finalLoginUser = loginUser;
        CollectQuestion collectQuestion = null;
        final boolean[] collect = {true};
        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {
                CollectQuestion collectQuestion = CollectQuestionDAO.ME.getByUser(finalLoginUser.getId(), q.getId());
                if (null == collectQuestion) {
                    collectQuestion = new CollectQuestion();
                    collectQuestion.setUser(finalLoginUser.getId());
                    collectQuestion.setQuestion(id);
                    collectQuestion.save();
                    q.setCollect_count(q.getCollect_count() + 1);
                    q.doUpdate();
                } else {

                    int count = 1;
                    if (collectQuestion.getStatus() == CollectQuestion.STATUS_SHOW) {
                        collectQuestion.setStatus(CollectQuestion.STATUS_HIDE);
                        count = -1;
                        message[0] = "取消收藏成功";
                        collect[0] = false;
                    } else {
                        collectQuestion.setStatus(CollectQuestion.STATUS_SHOW);
                    }
                    q.setCollect_count(q.getCollect_count() + count);
                    q.doUpdate();
                    collectQuestion.doUpdate();
                }
            }

        });
        CollectQuestionDAO.ME.evict(loginUser.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("message", message[0]);
        map.put("collect", collect[0]);
        map.put("count", q.getCollect_count());
        return ApiResult.successWithObject(map);
    }


    /**
     * 用户发表的帖子
     *
     * @param user
     * @return
     */
    @PostMapping("user_pub_articles/{user}")
    @ResponseBody
    public ApiResult user_pub_articles(@PathVariable("user") Long user) {
        List<Question> articles = QuestionDAO.ME.allByUser(user, 1);
        Map<String, Object> map = new HashMap<>(1);
        User loginUser = User.ME.get(user);
        map.put("articles", QuestionVO.list(articles, loginUser));
        return ApiResult.successWithObject(map);
    }

    /**
     * 用户发表的帖子
     *
     * @param user
     * @return
     */
    @PostMapping("user_collect_articles/{user}")
    @ResponseBody
    public ApiResult user_collect_articles(@PathVariable("user") Long user) {
        List<CollectQuestion> collects = CollectQuestionDAO.ME.list(user, CollectQuestion.STATUS_SHOW);
        if (CollectionUtils.isEmpty(collects)) {
            return ApiResult.failWithMessage("暂无收藏");
        }
        List<Long> ids = collects.stream().filter(c -> (null != c && c.getId() > 0L && c.getQuestion() > 0L)).map(CollectQuestion::getQuestion).collect(Collectors.toList());
        List<Question> list = Question.ME.loadList(ids);
        Map<String, Object> map = new HashMap<>(1);
        User loginUser = User.ME.get(user);
        map.put("collects", QuestionVO.list(list, loginUser));
        return ApiResult.successWithObject(map);
    }
}
