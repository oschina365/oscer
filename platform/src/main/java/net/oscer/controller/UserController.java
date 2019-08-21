package net.oscer.controller;

import net.oscer.beans.*;
import net.oscer.common.ApiResult;
import net.oscer.config.provider.OauthEnum;
import net.oscer.dao.CollectQuestionDAO;
import net.oscer.dao.CommentQuestionDAO;
import net.oscer.dao.QuestionDAO;
import net.oscer.dao.UserBindDAO;
import net.oscer.db.Entity;
import net.oscer.framework.FormatTool;
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

import static net.oscer.beans.User.SEX_GIRL;
import static net.oscer.beans.User.SEX_UNKONW;
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
        List<UserBind> binds = UserBindDAO.ME.listByUser(loginUser.getId());
        request.setAttribute("froms", OauthEnum.fromList);
        if (CollectionUtils.isNotEmpty(binds)) {
            Map<String, UserBind> bindMap = binds.stream().collect(Collectors.toMap(UserBind::getProvider, u -> u));
            request.setAttribute("bindMap", bindMap);
        }
        return "/u/set";
    }

    @GetMapping("msg")
    public String msg() {
        return "/u/msg";
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
            User login_user = getLoginUser();
            if (login_user != null && login_user.getStatus() == STATUS_NORMAL) {
                return ApiResult.successWithObject(login_user);
            } else {
                return ApiResult.failWithMessage("登录失败");
            }

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
    public ApiResult set_info(User form) {
        User loginUser = getLoginUser();
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录后再试");
        }
        if (StringUtils.isBlank(form.getEmail())) {
            return ApiResult.failWithMessage("请填写邮箱");
        }
        if (!FormatTool.is_email(form.getEmail())) {
            return ApiResult.failWithMessage("请填写正确的邮箱");
        }
        if (StringUtils.isBlank(form.getNickname())) {
            return ApiResult.failWithMessage("请填写昵称");
        }
        if (form.getSex() > SEX_GIRL || form.getSex() < SEX_UNKONW) {
            return ApiResult.failWithMessage("请选择性别");
        }

        loginUser.setEmail(form.getEmail());
        loginUser.setNickname(form.getNickname());
        loginUser.setSex(form.getSex());
        loginUser.setSalt(loginUser._GeneratePwdHash(loginUser.getPassword(), loginUser.getEmail()));
        loginUser.setCity(form.getCity());
        loginUser.doUpdate();
        return ApiResult.success("修改成功");
    }

    /**
     * 修改密码
     *
     * @return
     */
    @PostMapping("set_pwd")
    @ResponseBody
    public ApiResult set_pwd() {
        User loginUser = getLoginUser();
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录后再试");
        }
        String L_pass = request.getParameter("L_pass");
        String L_repass = request.getParameter("L_repass");
        if (StringUtils.isBlank(L_pass)) {
            return ApiResult.failWithMessage("请输入新密码");
        }
        if (L_pass.trim().length() < 6 || L_pass.trim().length() > 16) {
            return ApiResult.failWithMessage("密码长度在6~16位");
        }
        if (!StringUtils.equals(L_pass, L_repass)) {
            return ApiResult.failWithMessage("两次密码不相同");
        }
        loginUser.setPassword(L_pass);
        loginUser.setSalt(loginUser._GeneratePwdHash(L_pass, loginUser.getEmail()));
        loginUser.doUpdate();
        return ApiResult.success("修改成功");
    }

    /**
     * 设置头像
     *
     * @return
     */
    @PostMapping("set_headimg")
    @ResponseBody
    public ApiResult set_headimg() {
        User loginUser = getLoginUser();
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录后再试");
        }
        String headimg = request.getParameter("headimg");
        if (StringUtils.isBlank(headimg)) {
            return ApiResult.failWithMessage("请上传图片");
        }
        loginUser.setHeadimg(headimg);
        loginUser.doUpdate();
        return ApiResult.success("上传成功");
    }
}
