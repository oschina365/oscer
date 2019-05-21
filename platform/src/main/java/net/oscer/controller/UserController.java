package net.oscer.controller;

import net.oscer.common.ApiResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户相关业务类
 *
 * @author kz
 * @date 2019年5月17日18:38:06
 **/

@Controller
@RequestMapping("/user/")
public class UserController extends BaseController{

    public static final long TIME_OUT = 1000L * 60 * 60 * 24 * 365;

    @GetMapping("login")
    public String login() {
        return "/user/login";
    }

    @GetMapping("reg")
    public String reg() {
        return "/user/reg";
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
}
