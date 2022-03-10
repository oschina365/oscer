package net.oscer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 工具类
 *
 * @author kz
 * @create 2021-05-26 15:56
 **/
@Controller
@RequestMapping("/tool/")
public class ToolController extends BaseController {

    @GetMapping("convert")
    public String convert() {
        return "/tool/convert";
    }

    @PostMapping("gen/convert")
    @ResponseBody
    public String convertGen(String classA, String classB) {
        return "/tool/convert";
    }
}
