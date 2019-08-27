package net.oscer.controller;


import net.oscer.beans.Blog;
import net.oscer.beans.Tweet;
import net.oscer.beans.User;
import net.oscer.beans.Visit;
import net.oscer.dao.VisitDAO;
import net.oscer.db.CacheMgr;
import net.oscer.db.Entity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * 访问统计
 *
 * @author kz
 */
@Controller
@RequestMapping("/visit/")
public class VisitController extends BaseController {

    /**
     * 博客增加阅读数,已阅读将不再增加次数
     */
    @GetMapping("blog")
    public void blog(@RequestParam("id") Long id) {
        User loginUser = getLoginUser();
        if (loginUser != null && loginUser.getStatus() == User.STATUS_NORMAL) {

            if (id > 0L) {
                Blog blog = Blog.ME.get(id);
                if (blog != null && blog.getStatus() == 1 && blog.getOpenup() == 1 && blog.getUser_id() != loginUser.getId()) {
                    Visit V = VisitDAO.ME.selectByUserIdAndObjType(loginUser.getId(), id, Visit.BLOG);
                    if (V == null) {
                        blog.updateField("view_count", blog.getView_count() + 1);
                        VisitDAO.ME.save(loginUser.getId(), id, Visit.BLOG);
                    } else {
                        V.updateField("last_date", new Date());
                    }
                }
            }
        }
    }

    /**
     * 用户增加阅读数,已阅读将不再增加次数
     */
    @GetMapping("user")
    public void user(@RequestParam("id") Long id) {
        User loginUser = getLoginUser();
        if (loginUser != null && loginUser.getStatus() == User.STATUS_NORMAL) {

            if (id > 0L) {
                User user = User.ME.get(id);
                if (user != null && user.getStatus() == User.STATUS_NORMAL && user.getId() != loginUser.getId()) {
                    Visit V = VisitDAO.ME.selectByUserIdAndObjType(loginUser.getId(), id, Visit.USER);
                    if (V == null) {
                        user.updateField("view_count", user.getView_count() + 1);
                        VisitDAO.ME.save(loginUser.getId(), id, Visit.USER);
                    } else {
                        V.updateField("last_date", new Date());
                    }
                }
            }
        }
    }

    /**
     * 动弹增加阅读数,已阅读将不再增加次数
     */
    @GetMapping
    public void tweet(@RequestParam("id") Long id) {
        User loginUser = getLoginUser();
        if (loginUser != null && loginUser.getStatus() == User.STATUS_NORMAL) {

            if (id > 0L) {
                Tweet tweet = Tweet.ME.get(id);
                if (tweet != null && tweet.getStatus() == 1 && tweet.getUser_id() != loginUser.getId()) {
                    Visit V = VisitDAO.ME.selectByUserIdAndObjType(loginUser.getId(), id, Visit.TWEET);
                    if (V == null) {
                        tweet.updateField("view_count", tweet.getView_count() + 1);
                        CacheMgr.evict(Entity.CACHE_HTML, "www_tweet_hot");
                        VisitDAO.ME.save(loginUser.getId(), id, Visit.TWEET);
                    } else {
                        V.updateField("last_date", new Date());
                    }
                }
            }
        }
    }
}
