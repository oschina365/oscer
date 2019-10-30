package net.oscer.controller;

import net.oscer.beans.*;
import net.oscer.common.ApiResult;
import net.oscer.config.provider.OauthEnum;
import net.oscer.dao.*;
import net.oscer.db.Entity;
import net.oscer.framework.FormatTool;
import net.oscer.vo.UserVO;
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
     * 我的发帖-web端
     *
     * @return
     */
    @PostMapping("pubs")
    @ResponseBody
    public ApiResult pubs() {
        User loginUser = getLoginUser();
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("未登录");
        }
        List<Question> questions = QuestionDAO.ME.page(loginUser.getId(), 1, pageNumber, pageSize);
        int count = QuestionDAO.ME.user_pub_count(loginUser.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("questions", questions);
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }

    /**
     * 我的收藏-web端
     *
     * @return
     */
    @PostMapping("collects")
    @ResponseBody
    public ApiResult collects() {
        User loginUser = getLoginUser();
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("未登录");
        }
        List<CollectQuestion> collects = CollectQuestionDAO.ME.page_list(loginUser.getId(), CollectQuestion.STATUS_SHOW, pageNumber, pageSize);
        if (CollectionUtils.isNotEmpty(collects)) {
            List<Long> ids = collects.stream().filter(c -> (null != c && c.getId() > 0L && c.getQuestion() > 0L)).map(CollectQuestion::getQuestion).collect(Collectors.toList());
            int count = CollectQuestionDAO.ME.count(loginUser.getId(), CollectQuestion.STATUS_SHOW);
            Map<String, Object> map = new HashMap<>();
            map.put("questions_collect", Question.ME.loadList(ids));
            map.put("count", count);
            return ApiResult.successWithObject(map);
        }
        return ApiResult.failWithMessage("");
    }

    /**
     * 我的关注-web端
     *
     * @return
     */
    @PostMapping("follows")
    @ResponseBody
    public ApiResult follows() {
        User loginUser = getLoginUser();
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("未登录");
        }
        List<User> list = FriendDAO.ME.pageFollows(loginUser.getId(), pageNumber, pageSize);
        int count = FriendDAO.ME.countFollow(loginUser.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("follows", UserVO.converts(list));
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }

    /**
     * 我的粉丝-web端
     *
     * @return
     */
    @PostMapping("fans")
    @ResponseBody
    public ApiResult fans() {
        User loginUser = getLoginUser();
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("未登录");
        }
        List<User> list = FriendDAO.ME.pageFans(loginUser.getId(), pageNumber, pageSize);
        int count = FriendDAO.ME.countFan(loginUser.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("currentFans", FriendDAO.ME.listFans(loginUser.getId(),pageNumber,pageSize));
        map.put("fans", UserVO.converts(list));
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }

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

        if (null == u || u.getStatus() != STATUS_NORMAL) {
            setErrorMsg("用户不存在或被封");
            return "/error/404";
        }

        int status = 0;
        User loginUser = getLoginUser();
        if (null != loginUser) {
            request.setAttribute("followed", FriendDAO.ME.followed(loginUser.getId(), u.getId()));
        }
        if (null != loginUser && loginUser.getId() == id) {
            status = 1;

        }

        if(loginUser!=null && loginUser.getId()<=2L){
            status = 1;
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
