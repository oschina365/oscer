package net.oscer.controller;

import net.oscer.common.ApiResult;
import net.oscer.common.URLConnectionUtil;
import net.oscer.framework.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author kz
 * @create 2021-10-13 9:42
 **/
@RequestMapping("/api/ab/")
@RestController
public class AbWebController {

    @PostMapping("web")
    public ApiResult web(@RequestParam("websiteList") String websiteList, @RequestParam("num") int num) {
        System.out.println(websiteList);
        if (StringUtils.isBlank(websiteList)) {
            return ApiResult.failWithMessage("网址列表为空");
        }
        List<String> urls = Arrays.asList(websiteList.split(","));
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(100, 150, 1, TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>());
        Map<String, String> headerMap = new HashMap<>();
        for (int i = 0; i < num; i++) {
            urls.parallelStream().forEach(row -> {
                poolExecutor.execute(new exec(headerMap, row));
            });
        }
        return ApiResult.success();
    }

    static class exec implements Runnable {
        Map<String, String> headerMap;
        String url;

        public exec(Map<String, String> headerMap, String url) {
            this.headerMap = headerMap;
            this.url = url;
        }

        @Override
        public void run() {
            String str = URLConnectionUtil.sendGet((url), null, null);
        }
    }

    public static void main(String[] args) {
        List<String> urls = new ArrayList<String>(){{
            add("http://124.222.233.124/api/");
            add("http://124.222.233.124/api/articles?current=1");
            add("http://124.222.233.124/api/articles?current=2");
            add("http://124.222.233.124/api/articles/54");
            add("http://124.222.233.124/api/comments?current=1&type=1&articleId=54");
        }};
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(100, 150, 1, TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>());
        Map<String, String> headerMap = new HashMap<>();
        for (int i = 0; i < 10000000; i++) {
            urls.parallelStream().forEach(row -> {
                poolExecutor.execute(new exec(headerMap, row));
            });
        }
    }

}
