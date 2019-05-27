package net.oscer.controller;

import net.oscer.beans.Node;
import net.oscer.beans.Question;
import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.dao.NodeDAO;
import net.oscer.dao.QuestionDAO;
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

    @PostMapping("/list")
    @ResponseBody
    public ApiResult list() {
        Map<String, Object> map = new HashMap<>();
        //帖子列表
        List<Question> questions = QuestionDAO.ME.all(pageNumber, pageSize);
        map.put("questions", questions);
        //帖子总数
        int count = QuestionDAO.ME.count(Node.STATUS_NORMAL);
        map.put("count", count);
        //全部节点
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, -1);
        Map<Long, String> nodeMap = nodes.stream().collect(Collectors.toMap(Node::getId, Node::getName));
        map.put("nodeMap", nodeMap);
        //帖子用户信息
        List<Long> users = questions.stream().map(Question::getUser).collect(Collectors.toList());
        List<User> userList = User.ME.loadList(users);
        if (CollectionUtils.isNotEmpty(userList)) {
            Map<Long, User> userMap = userList.stream().filter(Objects::nonNull).collect(Collectors.toMap(User::getId, u -> u, (u1, u2) -> u2));
            map.put("userMap", userMap);
        }
        return ApiResult.successWithObject(map);
    }

    @GetMapping("/add")
    public String index() {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        request.setAttribute("nodes", nodes);
        return "question/add";
    }

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
