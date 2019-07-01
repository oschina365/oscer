package net.oscer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * api
 *
 * @author kz
 * @create 2019-06-28 17:11
 **/
@RequestMapping("/api/")
@Controller
public class ApiController extends BaseController {

    /**
     * 发送邮件
     *
     * @param email
     */
    @PostMapping("email")
    @ResponseBody
    public void email(@RequestParam(value = "email", required = true) String email) {

    }
}
