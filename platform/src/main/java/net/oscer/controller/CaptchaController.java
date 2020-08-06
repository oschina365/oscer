package net.oscer.controller;

import net.oscer.common.IpUtil;
import net.oscer.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 验证码
 *
 * @author kz
 * @date 2017年12月1日18:55:17
 */
@Controller
@RequestMapping("/captcha/")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;


    /**
     * 生成验证码
     *
     * @param request
     * @param response
     */
    @RequestMapping
    public void index(String key, HttpServletRequest request, HttpServletResponse response) {
        captchaService.captcha(key, request, response);
    }

    /**
     * 校验验证按
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/check")
    public void check(String key, String value, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip = IpUtil.getIpAddress(request);
        key = ip.concat("_").concat(key);
        captchaService.check(key, value, request, response);
    }
}
