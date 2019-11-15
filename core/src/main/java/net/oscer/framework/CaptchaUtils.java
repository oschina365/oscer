package net.oscer.framework;


import net.oscer.beans.SendEmailRecord;
import net.oscer.common.ApiResult;
import net.oscer.dao.SendEmailRecordDAO;
import net.oscer.enums.EmailTemplateTypeEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.oscer.beans.SendEmailRecord.SEND_REGISTER_INTERVAL_TIME;

/**
 * @author kz
 * @date 2018-01-10
 */
public class CaptchaUtils {

    public static String getCode(String keyName, HttpServletRequest request) {
        String ip = IpUtil.getIpAddress(request);
        String key = ip.concat("_").concat(keyName);
        code(key, request);
        Object jo = request.getSession().getAttribute(key);
        if (jo == null) {
            return null;
        }
        return jo.toString();
    }

    public static void code(String key, HttpServletRequest request) {
        request.getSession().setAttribute(key, (int) ((Math.random() * 9 + 1) * 1000));
    }


    public static void captcha(String key, HttpServletRequest request, HttpServletResponse response) {
        //设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Cache-Control", "no-store");
        //设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0L);
        response.setContentType("image/jpeg");
        RandomValidateCode randomValidateCode = new RandomValidateCode();
        try {
            //输出图片方法
            randomValidateCode.getRandcode(key, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ApiResult check(String key, String value, HttpServletRequest request) throws IOException {

        SendEmailRecord record = SendEmailRecordDAO.ME.selectByEmail(key, SendEmailRecord.TYPE_WEB, EmailTemplateTypeEnum.TYPE.REGISTER.getKey());
        if (record == null) {
            return ApiResult.failWithMessage("请重新获取验证码");
        }
        if ((System.currentTimeMillis() - record.getInsert_date().getTime()) < SEND_REGISTER_INTERVAL_TIME) {
            return ApiResult.success();
        } else {
            return ApiResult.failWithMessage("验证码已过期，请重新获取");
        }
    }
}
