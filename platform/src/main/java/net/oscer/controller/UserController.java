package net.oscer.controller;

import net.oscer.beans.CollectQuestion;
import net.oscer.beans.CommentQuestion;
import net.oscer.beans.Question;
import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.dao.CollectQuestionDAO;
import net.oscer.dao.CommentQuestionDAO;
import net.oscer.dao.QuestionDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.oscer.db.Entity.OFFLINE;
import static net.oscer.db.Entity.STATUS_NORMAL;

/**
 * 用户相关业务类
 *
 * @author kz
 * @date 2019年5月17日18:38:06
 **/

@Controller
@RequestMapping("/u/")
public class UserController extends BaseController {

    /**
     * 个人空间主页
     *
     * @return
     */
    @GetMapping("home")
    public String home() {
        User loginUser = getLoginUser();
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return "/error/404";
        }
        List<Question> questions = QuestionDAO.ME.allByUser(loginUser.getId(), 1);
        request.setAttribute("questions_pub", questions);
        List<CollectQuestion> collects = CollectQuestionDAO.ME.list(loginUser.getId(), CollectQuestion.STATUS_SHOW);
        if (CollectionUtils.isNotEmpty(collects)) {
            List<Long> ids = collects.stream().filter(c -> (null != c && c.getId() > 0L && c.getQuestion() > 0L)).map(CollectQuestion::getQuestion).collect(Collectors.toList());
            request.setAttribute("questions_collect", Question.ME.loadList(ids));
        }
        return "/u/home";
    }

    /**
     * 基本设置
     *
     * @return
     */
    @GetMapping("set")
    public String set() {
        User loginUser = getLoginUser();
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return "/error/404";
        }
        return "/u/set";
    }

    /**
     * 用户信息主页
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public String home(@PathVariable("id") long id) {
        if (id <= 0L) {
            return "/error/404";
        }
        User u = User.ME.get(id);
        int status = 0;
        User loginUser = getLoginUser();
        if (null != loginUser && loginUser.getId() == id) {
            status = 1;
        }
        if (null == u || u.getStatus() != STATUS_NORMAL) {
            return "/error/404";
        }
        List<Question> questions = QuestionDAO.ME.allByUser(id, status);

        List<CommentQuestion> comments = CommentQuestionDAO.ME.allByUser(id, status);
        request.setAttribute("u", u);
        request.setAttribute("vip", u.vip());
        request.setAttribute("questions", questions);
        if (CollectionUtils.isNotEmpty(comments)) {
            Map<String, Question> map = new HashMap<>(comments.size());
            for (CommentQuestion c : comments) {
                Question q = Question.ME.get(c.getQuestion());
                if (null == q || q.getStatus() != 0) {
                    continue;
                }
                map.put(q.getId() + "", q);

            }
            request.setAttribute("comments", comments);
            request.setAttribute("commentMap", map);
        }

        return "/u/info";
    }

    /**
     * 登录页面
     *
     * @return
     */
    @GetMapping("login")
    public String login() {
        return "/u/login";
    }

    /**
     * 注册页面
     *
     * @return
     */
    @GetMapping("reg")
    public String reg() {
        return "/u/reg";
    }

    /**
     * 用户登录
     *
     * @param name
     * @param pwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ApiResult login(@RequestParam("name") String name, @RequestParam("pwd") String pwd) {
        try {
            loginUser(name, pwd);
            return ApiResult.success("登录成功");
        } catch (Exception e) {
            return ApiResult.failWithMessage(StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "登录失败");
        }
    }

    /**
     * 退出
     *
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout() {
        try {
            User login_user = getLoginUser();
            login_user.setLogout_time(new Date());
            login_user.setOnline(OFFLINE);
            login_user.doUpdate();
            SecurityUtils.getSubject().logout();
            deleteUserInCookie();
        } catch (Exception e) {
            logger.warn("登出系统异常");
        }
        return "redirect:/";
    }

    /**
     * 更改用户基本信息
     *
     * @return
     */
    @PostMapping("set_info")
    @ResponseBody
    public ApiResult set_info() {

        return ApiResult.success();
    }
}
