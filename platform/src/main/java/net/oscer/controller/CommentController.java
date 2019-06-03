package net.oscer.controller;

import net.oscer.common.ApiResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 回答
 *
 * @author kz
 * @create 2019-05-31 11:43
 **/
@RequestMapping("/comment")
@Controller
public class CommentController extends BaseController {


    /**
     * 回答帖子
     *
     * @param id
     * @return
     */
    @PostMapping("/q/{id}")
    @ResponseBody
    public ApiResult question(@PathVariable("id") Long id) {
        return ApiResult.success();
    }
}
