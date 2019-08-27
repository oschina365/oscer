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
import net.oscer.framework.FormatTool;
import net.oscer.service.ViewService;
import net.oscer.vo.QuestionVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.oscer.beans.Question.MAX_LENGTH_TITLE;
import static net.oscer.db.Entity.STATUS_NORMAL;

/**
 * 帖子类
 *
 * @author kz
 * @date 2019年5月23日18:09:34
 **/
@RequestMapping("/q")
@Controller
public class QuestionController extends BaseController {


    /**
     * 帖子详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id) {
        Question q = Question.ME.get(id);
        if (null == q || q.getId() <= 0L) {
            return "/error/404";
        }
        if (q.getStatus() != 0) {
            return "403";
        }
        User u = User.ME.get(q.getUser());
        if (null == u || u.getId() <= 0L || u.getStatus() != User.STATUS_NORMAL) {
            return "403";
        }
        User loginUser = getLoginUser();
        if (loginUser != null) {
            CollectQuestion collectQuestion = CollectQuestionDAO.ME.getByUser(loginUser.getId(), q.getId());
            request.setAttribute("collected", collectQuestion != null && collectQuestion.getStatus() == CollectQuestion.STATUS_SHOW ? true : false);
        }
        ViewService.keyCache(q.getId(), ViewEnum.TYPE.QUESTION.getKey());
        request.setAttribute("author", q.getUser());
        request.setAttribute("q", q);
        request.setAttribute("u", u);
        return "/question/detail";
    }


    /**
     * 添加帖子页面
     *
     * @return
     */
    @GetMapping("/add")
    public String index() {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        request.setAttribute("nodes", nodes);
        return "question/add";
    }

    /**
     * 添加帖子方法
     *
     * @param form
     * @param is_reward
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ApiResult add(Question form, @RequestParam(value = "is_reward", defaultValue = "0") int is_reward) {
        User login_user = getLoginUser();
        if (login_user == null || !login_user.status_is_normal()) {
            return ApiResult.failWithMessage("请登录后重试");
        }
        ApiResult result = QuestionDAO.ME.check(form, is_reward);
        if (result == null || result.getCode() == ApiResult.fail) {
            return result;
        }

        form.setUser(login_user.getId());
        form.setTitle(net.oscer.framework.StringUtils.abbreviate(FormatTool.text(form.getTitle()), MAX_LENGTH_TITLE));
        if (is_reward == 0) {
            form.setReward_point(0);
        } else {
            if (form.getReward_point() > login_user.getScore()) {
                return ApiResult.failWithMessage("积分不够哦~");
            }
        }
        form.setContent(net.oscer.framework.StringUtils.abbreviate(FormatTool.cleanBody(form.getContent(),false), MAX_LENGTH_TITLE));
        form.save();
        Node n = Node.ME.get(form.getNode());
        QuestionDAO.ME.evictNode(form.getNode());
        QuestionDAO.ME.evict(login_user.getId());
        return ApiResult.successWithObject(n.getUrl());
    }

    /**
     * 编辑帖子
     *
     * @param id
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id) {
        Question q = Question.ME.get(id);
        if (null == q || q.getId() <= 0L) {
            return "/error/404";
        }
        if (q.getStatus() != 0) {
            return "403";
        }
        User u = User.ME.get(q.getUser());
        if (null == u || u.getId() <= 0L || u.getStatus() != User.STATUS_NORMAL) {
            return "403";
        }
        if (q.getUser() != u.getId()) {
            return "/error/404";
        }
        request.setAttribute("q", q);
        request.setAttribute("u", u);
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        request.setAttribute("nodes", nodes);
        return "/question/edit";
    }

    /**
     * 添加帖子方法
     *
     * @param form
     * @return
     */
    @PostMapping("/edit")
    @ResponseBody
    public ApiResult edit(Question form) {
        User login_user = getLoginUser();
        if (login_user == null || !login_user.status_is_normal()) {
            return ApiResult.failWithMessage("请登录后重试");
        }
        Long id = form.getId();
        Question old = Question.ME.get(id);
        if (form == null || old == null || login_user.getId() != old.getUser()) {
            return ApiResult.failWithMessage("该帖子不存在");
        }
        form.setReward_point(old.getReward_point());
        ApiResult result = QuestionDAO.ME.check(form, 0);
        if (result == null || result.getCode() == ApiResult.fail) {
            return result;
        }

        form.setUser(login_user.getId());
        form.setTitle(StringUtils.abbreviate(form.getTitle(), MAX_LENGTH_TITLE));
        form.doUpdate();
        QuestionDAO.ME.evictNode(form.getNode());
        return ApiResult.successWithObject(form.getId());
    }

    /**
     * 推荐/取消推荐
     *
     * @param id
     * @return
     */
    @PostMapping("/recomm")
    @ResponseBody
    public ApiResult recomm(@RequestParam("id") long id) {
        Question q = Question.ME.get(id);
        if (null == q) {
            return ApiResult.failWithMessage("该帖子不存在");
        }
        if (q.getStatus() != 0) {
            return ApiResult.failWithMessage("该帖子已删除");
        }
        if (q.getRecomm() == 0) {
            q.setRecomm(1);
        } else {
            q.setRecomm(0);
        }
        q.doUpdate();
        return ApiResult.success();
    }

    /**
     * 系统置顶/取消置顶
     *
     * @param id
     * @return
     */
    @PostMapping("/as_top")
    @ResponseBody
    public ApiResult as_top(@RequestParam("id") long id) {
        Question q = Question.ME.get(id);
        if (null == q) {
            return ApiResult.failWithMessage("该帖子不存在");
        }
        if (q.getStatus() != 0) {
            return ApiResult.failWithMessage("该帖子已删除");
        }
        if (q.getSystem_top() == 0) {
            q.setSystem_top(1);
        } else {
            q.setSystem_top(0);
        }
        q.doUpdate();
        QuestionDAO.ME.evictTops();
        return ApiResult.success();
    }


}
