package net.oscer.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.dao.TweetDAO;
import net.oscer.view.TweetViewObject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 首页动弹列表
 * </p>
 *
 * @author kz
 * @date 2018-03-28
 */
@Controller
@RequestMapping("/tweet")
public class TweetsController extends BaseController {

    @GetMapping("/id")
    public String detail(@PathVariable("id") long id) {
        TweetViewObject viewObject = new TweetViewObject(id);
        if (viewObject == null) {
            return "/error/404";
        }
        return "/tweet/detail";
    }

    @PostMapping("/view")
    @ResponseBody
    public ApiResult view(@RequestParam long id) {
        TweetViewObject viewObject = new TweetViewObject(id);
        return ApiResult.successWithObject(viewObject);
    }

    /**
     * 动弹广场页面
     *
     * @return
     */
    @GetMapping
    public String index() {
        List<TweetViewObject> tweetViewObjects = TweetViewObject.tweetViewObjectList(getLoginUser(), "hot", 1, 6);
        request.setAttribute("hotTweets", tweetViewObjects);
        return "/tweet/index";
    }

    @PostMapping("/page")
    @ResponseBody
    public ApiResult page() {
        Map<String, Object> map = new HashMap<>();
        List<TweetViewObject> tweetViewObjects = TweetViewObject.tweetViewObjectList(getLoginUser(), "all", pageNumber, 10);
        map.put("data", tweetViewObjects);
        int total = TweetDAO.ME.total();
        map.put("total", total);
        map.put("lastPage", (total / 10) + ((total % 10) == 0L ? 0L : 1L));
        return ApiResult.successWithObject(map);
    }


    /**
     * 首页动弹列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public List<TweetViewObject> list() {
        User loginUser = getLoginUser();
        long start = System.nanoTime();
        List<TweetViewObject> tweetViewObjects = TweetViewObject.tweetViewObjectList(loginUser, "new", 1, 6);
        long end = System.nanoTime();
        double cost = new BigDecimal(end - start).divide(divisor).doubleValue();
        logger.info("select tweet list, query time:>>>  {}ms", cost);
        return tweetViewObjects;
    }

    public static void main(String[] args) {
        BigDecimal a = BigDecimal.valueOf(2);
        System.out.println(a.compareTo(BigDecimal.valueOf(0)));
    }


}
