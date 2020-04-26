package net.oscer.controller;

import net.oscer.beans.*;
import net.oscer.dao.*;
import net.oscer.vo.QuestionVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 全局访问类
 *
 * @author kz
 * @date 2019年3月14日16:36:12
 **/
@RequestMapping
@Controller
public class GlobalController extends BaseController {

    @RequestMapping("/500")
    public String error_500() {
        return "/error/500";
    }

    @RequestMapping("/error/{code}")
    public String error(@PathVariable("code") String code) {
        return "/error/" + code;
    }

    @RequestMapping("/resume")
    public String resume() {
        return "/resume/2";
    }

    @RequestMapping("/")
    public String index() {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        request.setAttribute("nodes", nodes);
        //查询置顶的帖子
        List<Question> tops = QuestionDAO.ME.tops(Question.SYSTEM_LIMIT_TOP);
        request.setAttribute("tops", QuestionVO.list(tops,getLoginUser(), "0"));

        //回帖周榜
        List<Map<User, Integer>> weekUserCommentHots = CommentQuestionDAO.ME.weekUserCommentHots();
        request.setAttribute("weekUserCommentHots", weekUserCommentHots);

        //查询对应的本周热议帖子
        List<Question> weekHots = QuestionDAO.ME.hots(0L, 10);
        request.setAttribute("weekHots", weekHots);

        User login_user = getLoginUser();
        if (login_user != null && login_user.getId() > 0L) {
            //查询今日是否已经签到
            Sign s = SignDAO.ME.selectByUser(login_user.getId());
            if (s != null && s.getId() > 0L) {
                request.setAttribute("sign_today_score", Sign.ME.sign_score(s.getSeries_count()));
            }
            request.setAttribute("sign", SignDetailDAO.ME.todaySign(login_user.getId()));
            request.setAttribute("signed", SignDetailDAO.ME.signedToday(login_user.getId()));
        }
        //查询今天已签到的人数
        request.setAttribute("count_signed_today", UserDAO.ME.count_signed_today());


        return "index";
    }

    /**
     * 一级节点页面
     *
     * @param node
     * @return
     */
    @RequestMapping("/{node}")
    public String node(@PathVariable("node") String node, @RequestParam(value = "limit", defaultValue = "6") Integer limit) {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        Node current_node = nodes.stream().filter(n -> (n != null && n.getUrl().substring(1, n.getUrl().length()).equalsIgnoreCase(node))).findFirst().orElse(null);
        if (current_node == null || current_node.getId() <= 0L || current_node.getStatus() == Node.STATUS_FORBID) {
            return "/error/404";
        }
        request.setAttribute("nodes", nodes);
        request.setAttribute("current_node", current_node);
        //查询对应的本周热议帖子
        List<Question> weekHots = QuestionDAO.ME.hots(current_node.getId(), limit);
        request.setAttribute("weekHots", weekHots);
        return "node";
    }
}
