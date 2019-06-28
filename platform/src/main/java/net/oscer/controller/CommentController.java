package net.oscer.controller;

import net.oscer.beans.CommentQuestion;
import net.oscer.beans.Question;
import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.dao.CommentQuestionDAO;
import net.oscer.db.CacheMgr;
import net.oscer.framework.StringUtils;
import net.oscer.vo.CommentQuestionVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        User login_user = getLoginUser();
        if (login_user == null) {
            return ApiResult.failWithMessage("请登录后再试");
        }
        if (null == id || id <= 0L) {
            return ApiResult.failWithMessage("该帖子不存在");
        }
        String content = param("content");
        if (StringUtils.isBlank(content)) {
            return ApiResult.failWithMessage("请输入内容");
        }
        Question q = Question.ME.get(id);
        if (null == q || q.getId() <= 0L) {
            return ApiResult.failWithMessage("该帖子不存在");
        }
        CommentQuestion c = new CommentQuestion();
        c.setUser(login_user.getId());
        c.setQuestion(id);
        c.setContent(content);
        c.save();
        q.setComment_count(q.getComment_count() + 1);
        q.doUpdate();
        CommentQuestionDAO.ME.evict(id, login_user.getId());
        return ApiResult.success();
    }

    /**
     * 帖子列表
     * 首页帖子列表，节点帖子列表
     *
     * @param id
     * @return
     */
    @PostMapping("/question")
    @ResponseBody
    public ApiResult list(@RequestParam(value = "id", defaultValue = "0", required = false) Long id) {
        User login_user = getLoginUser();
        Map<String, Object> map = new HashMap<>(2);
        //评论列表 --分页
        List<CommentQuestion> comments = CommentQuestionDAO.ME.list(id, pageNumber, pageSize);
        if (CacheMgr.exists(CommentQuestion.ME.CacheRegion(), "rankMap#" + id)) {
            map.put("rankMap", CacheMgr.get(CommentQuestion.ME.CacheRegion(), "rankMap#" + id));
        } else {
            //全部评论
            List<CommentQuestion> allComments = CommentQuestionDAO.ME.list(id);
            if (CollectionUtils.isNotEmpty(allComments)) {
                Map<Long, Integer> rankMap = new HashMap<>(allComments.size());
                for (int i = 0; i < allComments.size(); i++) {
                    rankMap.put(allComments.get(i).getId(), allComments.size() - i);
                }
                map.put("rankMap", rankMap);
                CacheMgr.set(CommentQuestion.ME.CacheRegion(), "rankMap#" + id, rankMap);
            }
        }
        map.put("comments", CommentQuestionVO.list(id, login_user, comments));
        //帖子总数
        int count = CommentQuestionDAO.ME.count(id);
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }
}
