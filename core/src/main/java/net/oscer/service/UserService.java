package net.oscer.service;


import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.dao.BadWordDAO;
import net.oscer.framework.FormatTool;
import net.oscer.framework.StringUtils;

import java.io.IOException;

import static net.oscer.service.TextCheckBaiduService.REG_TIME_90;

/**
 * 用户相关服务类
 **/
public class UserService {

    public static UserService ME = new UserService();

    /**
     * 多少积分以下就需要检测
     */
    public final static int MIN_SCORE_NEEDED = 50;

    /**
     * 判断用户需要检测
     * 如需要（true），发博客，发帖子就需要调文本审核接口
     *
     * @param userId
     * @return
     */
    public static boolean user_need_check(long userId) {
        User user = User.ME.get(userId);
        if (user == null) {
            return false;
        }
        //官方人员无需检测
        if (userId <= 2) {
            return false;
        }
        //注册时间超过三个月的就不需要检测
        boolean check = (System.currentTimeMillis() - user.getInsert_date().getTime()) < REG_TIME_90;
        if (!check) {
            return false;
        }

        return true;
    }

    /**
     * 根据用户ID，内容，审核的方式进行检测
     *
     * @param userId
     * @param content
     * @param checkType
     * @return
     */
    public static ApiResult content_need_check(long userId, String content, String checkType) {
        if (StringUtils.isBlank(FormatTool.text(content))) {
            return ApiResult.failWithMessage("内容为空，请填写内容");
        }
        //敏感词检测
        ApiResult apiResult = BadWordDAO.ME.check(content, true);

        if (apiResult != null && apiResult.getCode() == 0) {
            return apiResult;
        }
        /*if (!user_need_check(userId)) {
            return ApiResult.success();
        }*/
        return TextCheckService.auto(userId, content, checkType);
    }
}
