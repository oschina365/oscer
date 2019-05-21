package net.oscer.controller;

import net.oscer.db.CacheMgr;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 全局访问类
 *
 * @author kz
 * @date   2019年3月14日16:36:12
 **/
@RequestMapping
@Controller
public class GlobalController extends BaseController{

    @RequestMapping("/")
    public String index(){
        if(CacheMgr.exists("test","key")){
            System.out.println("存在");
            System.out.println(CacheMgr.get("test","key"));
        }else{
            CacheMgr.set("test","key",1);
        }

        return "index";
    }
}
