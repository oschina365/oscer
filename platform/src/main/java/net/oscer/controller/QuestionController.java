package net.oscer.controller;

import net.oscer.beans.Node;
import net.oscer.beans.Question;
import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.dao.NodeDAO;
import net.oscer.dao.QuestionDAO;
import net.oscer.vo.QuestionVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.oscer.beans.Question.MAX_LENGTH_TITLE;

/**
 * 帖子类
 *
 * @author kz
 * @date 2019年5月23日18:09:34
 **/
@RequestMapping("/q")
@Controller
public class QuestionController extends BaseController {

    /**
     * 帖子详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id) {
        Question q = Question.ME.get(id);
        if (null == q || q.getId() <= 0L) {
            return "/error/404";
        }
        if (q.getStatus() != 0) {
            return "403";
        }
        User u = User.ME.get(q.getUser());
        if (null == u || u.getId() <= 0L || u.getStatus() != User.STATUS_NORMAL) {
            return "403";
        }
        request.setAttribute("q", q);
        request.setAttribute("u", u);
        return "/question/detail";
    }

    /**
     * 帖子列表
     * 首页帖子列表，节点帖子列表
     *
     * @param id
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public ApiResult list(@RequestParam(value = "id", defaultValue = "0", required = false) Long id) {
        Map<String, Object> map = new HashMap<>();
        //帖子列表
        List<Question> questions = QuestionDAO.ME.all(id, pageNumber, pageSize);
        map.put("questions", QuestionVO.list(questions));
        //帖子总数
        int count = QuestionDAO.ME.count(id);
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }

    /**
     * 添加帖子页面
     *
     * @return
     */
    @GetMapping("/add")
    public String index() {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        request.setAttribute("nodes", nodes);
        return "question/add";
    }

    /**
     * 添加帖子方法
     *
     * @param form
     * @param is_reward
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ApiResult add(Question form, @RequestParam(value = "is_reward", defaultValue = "0") int is_reward) {
        User login_user = getLoginUser();
        if (login_user == null || !login_user.status_is_normal()) {
            return ApiResult.failWithMessage("请登录后重试");
        }
        ApiResult result = QuestionDAO.ME.check(form, is_reward);
        if (result == null || result.getCode() == ApiResult.fail) {
            return result;
        }

        form.setUser(login_user.getId());
        form.setTitle(StringUtils.abbreviate(form.getTitle(), MAX_LENGTH_TITLE));
        if (is_reward == 0) {
            form.setReward_point(0);
        }
        form.save();
        return ApiResult.success("发帖成功");
    }

}
