package net.oscer.controller;

import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.dao.FriendDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static net.oscer.db.Entity.STATUS_NORMAL;

/**
 * 关注/粉丝
 *
 * @author MRCHENIKE
 * @create 2019-09-06 17:13
 **/
@Controller
@RequestMapping("/f/")
public class FriendController extends BaseController {

    /**
     * 关注/取消关注
     *
     * @param user
     * @param friend
     * @return
     */
    @PostMapping("follow")
    @ResponseBody
    public ApiResult follow(@RequestParam(value = "user", required = false) Long user,
                            @RequestParam("friend") Long friend) {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        if (friend == null || friend <= 0L) {
            return ApiResult.failWithMessage("改用户不存在");
        }
        FriendDAO.ME.save(loginUser.getId(), friend);
        return ApiResult.success();

    }
}
