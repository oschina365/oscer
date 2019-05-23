package net.oscer.controller;

import net.oscer.beans.Node;
import net.oscer.beans.Question;
import net.oscer.common.ApiResult;
import net.oscer.dao.NodeDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 帖子类
 *
 * @author kz
 * @date 2019年5月23日18:09:34
 **/
@RequestMapping("/q")
@Controller
public class QuestionController extends BaseController {

    @GetMapping("/add")
    public String index() {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        request.setAttribute("nodes", nodes);
        return "question/add";
    }

    @PostMapping("/add")
    @ResponseBody
    public ApiResult add(Question form) {
        return ApiResult.success();
    }

}
