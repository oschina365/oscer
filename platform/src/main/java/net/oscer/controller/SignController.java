package net.oscer.controller;

import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.dao.SignDAO;
import net.oscer.dao.SignDetailDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 签到
 *
 * @author kz
 * @create 2019-05-30 11:03
 **/
@RequestMapping("/sign")
@Controller
public class SignController extends BaseController {

    /**
     * 签到
     *
     * @return
     */
    @PostMapping
    @ResponseBody
    public ApiResult index() throws Exception {
        User login_user = getLoginUser();
        if (login_user == null || !login_user.status_is_normal()) {
            return ApiResult.failWithMessage("请登录后重试");
        }
        boolean signedToday = SignDetailDAO.ME.signedToday(login_user.getId());
        if (signedToday) {
            return ApiResult.failWithMessage("今天已签到了！");
        }
        boolean sign = SignDetailDAO.ME.signingToday(login_user.getId());
        if (!sign) {
            return ApiResult.failWithMessage("签到失败");
        }
        User u = User.ME.get(login_user.getId());
        return ApiResult.successWithObject(u.getScore_today());
    }

    /**
     * 活跃签到榜
     *
     * @return
     */
    @GetMapping("/active_signs")
    @ResponseBody
    public ApiResult active_signs() {
        return ApiResult.successWithObject(SignDAO.ME.active_signs());
    }
}
