package net.oscer.controller;

import net.oscer.beans.CommentQuestion;
import net.oscer.beans.Question;
import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.dao.CommentQuestionDAO;
import net.oscer.dao.QuestionDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final long TIME_OUT = 1000L * 60 * 60 * 24 * 365;

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
                map.put(q.getId()+"", q);

            }
            request.setAttribute("comments", comments);
            request.setAttribute("commentMap", map);
        }

        return "/u/home";
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
            UsernamePasswordToken token = new UsernamePasswordToken(name, pwd, false);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            subject.getSession().setTimeout(TIME_OUT);
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
}
