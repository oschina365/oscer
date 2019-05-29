package net.oscer.controller;

import net.oscer.beans.Node;
import net.oscer.beans.Question;
import net.oscer.dao.NodeDAO;
import net.oscer.dao.QuestionDAO;
import net.oscer.vo.QuestionVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 全局访问类
 *
 * @author kz
 * @date 2019年3月14日16:36:12
 **/
@RequestMapping
@Controller
public class GlobalController extends BaseController {

    @RequestMapping("/")
    public String index() {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        request.setAttribute("nodes", nodes);
        //查询置顶的帖子
        List<Question> tops = QuestionDAO.ME.tops(Question.SYSTEM_LIMIT_TOP);
        request.setAttribute("tops", QuestionVO.list(tops));
        //查询对应的本周热议帖子
        List<Question> weekHots = QuestionDAO.ME.hots(0L, 10);
        request.setAttribute("weekHots", weekHots);
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
