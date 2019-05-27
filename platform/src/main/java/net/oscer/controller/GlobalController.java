package net.oscer.controller;

import net.oscer.beans.Node;
import net.oscer.beans.Question;
import net.oscer.dao.NodeDAO;
import net.oscer.dao.QuestionDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        return "index";
    }

    @RequestMapping("/{node}")
    public String fist_node(@PathVariable("node") String node) {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        request.setAttribute("nodes", nodes);
        request.setAttribute("current_node", "/" + node);
        return "index";
    }
}
