package net.oscer.controller;

import net.oscer.beans.*;
import net.oscer.common.ApiResult;
import net.oscer.dao.*;
import net.oscer.db.CacheMgr;
import net.oscer.db.DbQuery;
import net.oscer.db.TransactionService;
import net.oscer.enums.TextCheckEnum;
import net.oscer.enums.ViewEnum;
import net.oscer.framework.FormatTool;
import net.oscer.framework.StringUtils;
import net.oscer.service.UserService;
import net.oscer.service.ViewService;
import net.oscer.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static net.oscer.beans.Msg.TYPE_USER;
import static net.oscer.beans.Question.MAX_LENGTH_TITLE;
import static net.oscer.beans.User.SEX_GIRL;
import static net.oscer.beans.User.SEX_UNKONW;
import static net.oscer.db.Entity.ONLINE;
import static net.oscer.db.Entity.STATUS_NORMAL;

/**
 * 通用api，所有端通用
 *
 * @author kz
 * @create 2019-08-10 13:06
 **/
@RequestMapping("/uni/")
@Controller
public class UniController extends BaseController {

    /**
     * 判断是否存在第三方
     * 第三方登录
     *
     * @param from     注册方式
     * @param union_id 第三方唯一ID
     * @return
     */
    @PostMapping("user_bind_login")
    @ResponseBody
    public ApiResult user_bind_login(@RequestParam("provider") String provider, @RequestParam("union_id") String union_id, @RequestParam("from") String from,
                                     @RequestParam("nickname") String nickname, @RequestParam("headimg") String headimg, @RequestParam("sex") String sex) {
        UserBind bind = UserBindDAO.ME.bindByUnion_id(provider, union_id);
        if (bind == null || bind.getUser() <= 0L) {
            //未注册用户
            User reg = new User();
            reg.setOnline(ONLINE);
            reg.setLogin_time(new Date());
            reg.setHeadimg(headimg);
            reg.setNickname(nickname);
            reg.setUsername(nickname);
            reg.setPassword(RandomStringUtils.randomAlphanumeric(8));
            reg.setEmail(union_id);
            reg.setSalt(User.ME._GeneratePwdHash(reg.getPassword(), reg.getEmail()));
            reg.save();

            bind = new UserBind();
            bind.setUnion_id(union_id);
            bind.setProvider(provider);
            bind.setName(nickname);
            bind.setHeadimg(headimg);
            bind.setUser(reg.getId());
            bind.setFrom(from);
            bind.save();
        } else {
            bind.setHeadimg(headimg);
            bind.setFrom(from);
            bind.doUpdate();
        }
        User bindUser = User.ME.get(bind.getUser());
        if (bindUser == null) {
            return ApiResult.failWithMessage("网络异常");
        }
        if (bindUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("账号已被屏蔽");
        }
        bindUser.setOnline(ONLINE);
        bindUser.setLogin_time(new Date());
        bindUser.doUpdate();
        return ApiResult.successWithObject(bindUser);
    }

    /**
     * 获取所有顶级节点名称
     *
     * @return
     */
    @PostMapping("first_nodes")
    @ResponseBody
    public ApiResult nodes() {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);
        Map<String, Object> map = new HashMap<>();
        map.put("nodes", nodes);
        return ApiResult.successWithObject(map);
    }

    /**
     * 获取所有节点名称
     *
     * @return
     */
    @PostMapping("all_nodes")
    @ResponseBody
    public ApiResult all_nodes() {
        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, -1);
        Map<String, Object> map = new HashMap<>();
        map.put("nodes", nodes);
        return ApiResult.successWithObject(map);
    }

    /**
     * 帖子列表
     * 首页帖子列表，节点帖子列表
     *
     * @param id
     * @param show 最新发帖，最新回帖
     * @return
     */
    @PostMapping("q/list")
    @ResponseBody
    public ApiResult list(@RequestParam(value = "id", defaultValue = "0", required = false) Long id,
                          @RequestParam(value = "rhtml", defaultValue = "0", required = false) String rhtml,
                          @RequestParam(value = "show", required = false) String show) {
        Map<String, Object> map = new HashMap<>();
        //帖子列表
        List<Question> questions = QuestionDAO.ME.all(id, pageNumber, 10, show);
        map.put("questions", QuestionVO.list(questions, getLoginUser(), rhtml));
        //帖子总数
        int count = QuestionDAO.ME.countNode(id);
        map.put("count", count);
        map.put("lastPage", (count / 10) + ((count % 10) == 0L ? 0L : 1L));
        return ApiResult.successWithObject(map);
    }

    /**
     * 帖子详情
     *
     * @param id
     * @return
     */
    @PostMapping("q/{id}")
    @ResponseBody
    public ApiResult question_detail(@PathVariable("id") Long id, @RequestParam(value = "user", required = false) Long user,
                                     @RequestParam(value = "rhtml", defaultValue = "0", required = false) String rhtml) {
        Question q = Question.ME.get(id);
        if (null == q || q.getId() <= 0L) {
            return ApiResult.failWithMessage("帖子不存在");
        }
        if (q.getStatus() != 0) {
            return ApiResult.failWithMessage("帖子已屏蔽");
        }
        User u = User.ME.get(q.getUser());
        if (null == u || u.getId() <= 0L || u.getStatus() != User.STATUS_NORMAL) {
            return ApiResult.failWithMessage("用户被屏蔽");
        }
        User loginUser = current_user(user);
        ViewService.keyCache(q.getId(), ViewEnum.TYPE.QUESTION.getKey());
        if (loginUser != null && loginUser.status_is_normal() && loginUser.getId() != q.getUser()) {
            VisitDAO.ME.save(loginUser.getId(), q.getId(), Visit.QUESTION);
        }
        List<Question> list = Arrays.asList(q);
        Map<String, Object> map = new HashMap<>();
        map.put("q", q);
        map.put("u", UserVO.convert(u));
        map.put("detail", QuestionVO.list(list, loginUser, rhtml));
        return ApiResult.successWithObject(map);
    }

    /**
     * 评论列表
     *
     * @param id
     * @return
     */
    @PostMapping("q/comments")
    @ResponseBody
    public ApiResult question_comments(@RequestParam(value = "id", defaultValue = "0", required = false) Long id,
                                       @RequestParam(value = "rhtml", defaultValue = "0", required = false) String rhtml) {
        User login_user = getLoginUser();
        Map<String, Object> map = new HashMap<>(2);
        //评论列表 --分页
        List<CommentQuestion> comments = CommentQuestionDAO.ME.first(id, pageNumber, pageSize);
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
        map.put("comments", CommentQuestionVO.list(id, login_user, comments, rhtml));
        //帖子总数
        int count = CommentQuestionDAO.ME.first_count(id);
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }

    /**
     * 删除帖子方法
     *
     * @return
     */
    @PostMapping("/q/delete/{id}")
    @ResponseBody
    public ApiResult delete(@PathVariable("id") Long id, @RequestParam(value = "user", required = false) Long user) throws Exception {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        if (id <= 0L) {
            return ApiResult.failWithMessage("不存在此帖子");
        }
        Question q = Question.ME.get(id);
        if (loginUser.getId() != q.getUser()) {
            return ApiResult.failWithMessage("无权限删除此贴");
        }

        long user_id = loginUser.getId();
        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {
                q.delete();
                CollectQuestionDAO.ME.deleteByQuestion(id);
                VisitDAO.ME.deleteByObjIdObjType(id, Visit.QUESTION);
                CommentQuestionDAO.ME.delete(id);
                QuestionDAO.ME.evictNode(q.getNode());
                QuestionDAO.ME.evict(user_id);
            }
        });
        return ApiResult.success();
    }

    /**
     * 回答帖子
     *
     * @param id
     * @return
     */
    @PostMapping("/user_pub_q_comment")
    @ResponseBody
    public ApiResult user_pub_q_comment(@RequestParam("id") long id, @RequestParam(value = "user", required = false) Long user) throws Exception {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请先登录");
        }
        if (id <= 0L) {
            return ApiResult.failWithMessage("该帖子不存在");
        }
        final String[] content = {param("content")};
        long parent = param("parent", 0L);
        if (StringUtils.isBlank(content[0])) {
            return ApiResult.failWithMessage("请输入内容");
        }
        Question q = Question.ME.get(id);
        if (null == q || q.getId() <= 0L) {
            return ApiResult.failWithMessage("该帖子不存在");
        }
        CommentQuestion commentQuestion = CommentQuestion.ME.get(parent);
        if (parent > 0L) {
            if (null == commentQuestion || commentQuestion.getId() <= 0L) {
                return ApiResult.failWithMessage("原评论不存在");
            }
        }

        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {

                if (commentQuestion != null && commentQuestion.getId() > 0L) {
                    commentQuestion.setReply_count(commentQuestion.getReply_count() + 1);
                    commentQuestion.setLast_date(new Date());
                    commentQuestion.doUpdate();
                    commentQuestion.evict(true);
                }
                CommentQuestion c = new CommentQuestion();
                c.setUser(loginUser.getId());
                c.setQuestion(id);
                content[0] = FormatTool.text(content[0]);
                c.setContent(FormatTool.fixContent(false, null, 0L, 0, 0, content[0]));
                c.setParent(parent);
                c.save();
                q.setLast_comment_user(loginUser.getId());
                q.setLast_comment_time(new Date());
                q.setComment_count(q.getComment_count() + 1);
                q.doUpdate();
                DynamicDAO.ME.save(loginUser.getId(), id, c.getId());
                QuestionDAO.ME.evictNode(q.getNode());
                CommentQuestionDAO.ME.evict(id, loginUser.getId());
                CacheMgr.evict(CommentQuestion.ME.CacheRegion(), "childs#" + id + "#" + parent);
            }
        });

        return ApiResult.success();
    }

    /**
     * 收藏/取消收藏
     *
     * @param id
     * @return
     */
    @PostMapping("/q/collect")
    @ResponseBody
    public ApiResult collect(@RequestParam("id") long id, @RequestParam(value = "user", required = false) Long user) throws Exception {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        Question q = Question.ME.get(id);
        if (null == q) {
            return ApiResult.failWithMessage("该帖子不存在");
        }
        if (q.getStatus() != 0) {
            return ApiResult.failWithMessage("该帖子已删除");
        }
        if (q.getUser() == loginUser.getId()) {
            return ApiResult.failWithMessage("自己的帖子不能被收藏");
        }
        final String[] message = {"收藏成功"};
        User finalLoginUser = loginUser;
        CollectQuestion collectQuestion = null;
        final boolean[] collect = {true};
        DbQuery.get("mysql").transaction(new TransactionService() {
            @Override
            public void execute() throws Exception {
                CollectQuestion collectQuestion = CollectQuestionDAO.ME.getByUser(finalLoginUser.getId(), q.getId());
                if (null == collectQuestion) {
                    collectQuestion = new CollectQuestion();
                    collectQuestion.setUser(finalLoginUser.getId());
                    collectQuestion.setQuestion(id);
                    collectQuestion.save();
                    q.setCollect_count(q.getCollect_count() + 1);
                    q.doUpdate();
                } else {

                    int count = 1;
                    if (collectQuestion.getStatus() == CollectQuestion.STATUS_SHOW) {
                        collectQuestion.setStatus(CollectQuestion.STATUS_HIDE);
                        count = -1;
                        message[0] = "取消收藏成功";
                        collect[0] = false;
                    } else {
                        collectQuestion.setStatus(CollectQuestion.STATUS_SHOW);
                    }
                    q.setCollect_count(q.getCollect_count() + count);
                    q.doUpdate();
                    collectQuestion.doUpdate();
                }
            }

        });
        CollectQuestionDAO.ME.evict(loginUser.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("message", message[0]);
        map.put("collect", collect[0]);
        map.put("count", q.getCollect_count());
        return ApiResult.successWithObject(map);
    }


    /**
     * 用户发表的帖子
     *
     * @param user
     * @return
     */
    @PostMapping("user_pub_articles/{user}")
    @ResponseBody
    public ApiResult user_pub_articles(@PathVariable("user") Long user) {
        List<Question> articles = QuestionDAO.ME.allByUser(user, 1);
        Map<String, Object> map = new HashMap<>(1);
        User loginUser = User.ME.get(user);
        map.put("articles", QuestionVO.list(articles, loginUser, "1"));
        return ApiResult.successWithObject(map);
    }

    /**
     * 用户发表的帖子
     *
     * @param user
     * @return
     */
    @PostMapping("user_collect_articles/{user}")
    @ResponseBody
    public ApiResult user_collect_articles(@PathVariable("user") Long user) {
        List<CollectQuestion> collects = CollectQuestionDAO.ME.list(user, CollectQuestion.STATUS_SHOW);
        if (CollectionUtils.isEmpty(collects)) {
            return ApiResult.failWithMessage("暂无收藏");
        }
        List<Long> ids = collects.stream().filter(c -> (null != c && c.getId() > 0L && c.getQuestion() > 0L)).map(CollectQuestion::getQuestion).collect(Collectors.toList());
        List<Question> list = Question.ME.loadList(ids);
        Map<String, Object> map = new HashMap<>(1);
        User loginUser = User.ME.get(user);
        map.put("collects", QuestionVO.list(list, loginUser, "1"));
        return ApiResult.successWithObject(map);
    }

    /**
     * 用户的帖子访问记录
     *
     * @param user
     * @return
     */
    @PostMapping("user_visits/{user}")
    @ResponseBody
    public ApiResult user_visits(@PathVariable("user") Long user, @RequestParam(value = "obj_type", required = false) Integer obj_type) {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        if (obj_type == null || obj_type <= 0) {
            obj_type = Visit.QUESTION;
        }
        List<ReadVO> readVOS = ReadVO.listByUserObjType(loginUser.getId(), obj_type);
        if (CollectionUtils.isEmpty(readVOS)) {
            return ApiResult.failWithMessage("暂无阅读记录");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("reads", readVOS);
        return ApiResult.successWithObject(map);
    }

    /**
     * 用户的帖子评论记录
     * 每个帖子取最后一条评论
     *
     * @param user
     * @return
     */
    @PostMapping("user_comments_q/{user}")
    @ResponseBody
    public ApiResult user_comments_q(@PathVariable("user") Long user) {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        List<CommentQuestion> comments = CommentQuestionDAO.ME.allByUser(loginUser.getId(), 0);
        Map<String, Object> map = new HashMap<>();
        map.put("comments", UserCommentVO.listUserComments(comments));
        return ApiResult.successWithObject(map);
    }

    /**
     * 添加帖子方法
     *
     * @param form
     * @return
     */
    @PostMapping("/q/add")
    @ResponseBody
    public ApiResult add(Question form, @RequestParam("user") Long user) throws Exception {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        ApiResult result = QuestionDAO.ME.check(form, 1);
        if (result == null || result.getCode() == ApiResult.fail) {
            return result;
        }
        if (form.getReward_point() > loginUser.getScore()) {
            return ApiResult.failWithMessage("积分不够哦~");
        }
        if (loginUser.getId() > 2 && !QuestionDAO.ME.canPub(loginUser.getId())) {
            return ApiResult.failWithMessage("发帖太快啦");
        }
        form.setUser(loginUser.getId());
        form.setTitle(StringUtils.abbreviate(FormatTool.text(form.getTitle()), MAX_LENGTH_TITLE));
        form.setContent(FormatTool.cleanBody(form.getContent(), false));

        //百度文本审核检测
        result = UserService.content_need_check(loginUser.getId(), form.getTitle() + form.getContent(), TextCheckEnum.TYPE.BAIDU.getKey());
        if (result.getCode() == ApiResult.fail) {
            return result;
        }
        QuestionDAO.ME.save(form);
        Node n = Node.ME.get(form.getNode());
        QuestionDAO.ME.evictNode(form.getNode());
        QuestionDAO.ME.evict(loginUser.getId());
        return ApiResult.successWithObject(n.getUrl());
    }

    /**
     * 更改用户基本信息
     *
     * @return
     */
    @PostMapping("set_info")
    @ResponseBody
    public ApiResult set_info(User form, @RequestParam(value = "user", required = false) Long user) {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }

        /*if (org.apache.commons.lang3.StringUtils.isBlank(form.getEmail())) {
            return ApiResult.failWithMessage("请填写邮箱");
        }*/
        if (StringUtils.isNotEmpty(form.getEmail()) && !FormatTool.is_email(form.getEmail())) {
            return ApiResult.failWithMessage("请填写正确的邮箱");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(form.getNickname())) {
            return ApiResult.failWithMessage("请填写昵称");
        }
        if (form.getSex() != null && (form.getSex() > SEX_GIRL || form.getSex() < SEX_UNKONW)) {
            return ApiResult.failWithMessage("请选择性别");
        }

        loginUser.setEmail(StringUtils.isEmpty(form.getEmail()) ? loginUser.getEmail() : form.getEmail());
        loginUser.setNickname(form.getNickname());
        loginUser.setSex(form.getSex());
        loginUser.setSalt(loginUser._GeneratePwdHash(loginUser.getPassword(), loginUser.getEmail()));
        loginUser.setCity(form.getCity());
        loginUser.setSelf_info(StringUtils.isEmpty(form.getSelf_info()) ? loginUser.getSelf_info() : form.getSelf_info());
        if (StringUtils.isNotEmpty(form.getHeadimg())) {
            loginUser.setHeadimg(form.getHeadimg());

        }
        loginUser.doUpdate();
        return ApiResult.successWithObject(loginUser);
    }

    /**
     * 子评论列表
     *
     * @return
     */
    @PostMapping("question_child_comments")
    @ResponseBody
    public ApiResult question_child_comments(@RequestParam(value = "user", required = false) Long user) {
        User loginUser = current_user(user);
        long question = param("question", 0L);
        long parent = param("parent", 0L);
        Question q = Question.ME.get(question);
        if (q == null || q.getStatus() != 0) {
            return ApiResult.failWithMessage("帖子不存在");
        }
        CommentQuestion c = CommentQuestion.ME.get(parent);
        if (c == null) {
            return ApiResult.failWithMessage("该评论不存在");
        }
        List<CommentQuestion> childs = CommentQuestionDAO.ME.childs(question, parent, pageNumber, pageSize);
        if (CollectionUtils.isEmpty(childs)) {
            return null;
        }
        return ApiResult.successWithObject(CommentQuestionVO.list(question, loginUser, childs, ""));
    }

    /**
     * 更改用户基本信息
     *
     * @return
     */
    @PostMapping("newest")
    @ResponseBody
    public ApiResult newest(@RequestParam(value = "user", required = false) Long user) {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", DynamicVO.construct(DynamicDAO.ME.listByUser(loginUser.getId(), pageNumber, pageSize)));
        map.put("count", DynamicDAO.ME.countByUser(loginUser.getId()));
        return ApiResult.successWithObject(map);
    }

    /**
     * 私信列表
     * 私信类型（系统私信：0，个人私信：1）,不传参数即查询全部
     *
     * @return
     */
    @PostMapping("lastmsgs")
    @ResponseBody
    public ApiResult lastmsgs(@RequestParam(value = "user", required = false) Long user) throws Exception {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        int type = param("type", -1);
        long user_id = loginUser.getId();
        List<LastMsg> list = null;
        int count = 0;
        if (type == -1) {
            list = LastMsgDAO.ME.msgs(user_id, pageNumber, pageSize);
            count = LastMsgDAO.ME.count(user_id);
        } else {
            list = LastMsgDAO.ME.msgs(user_id, type, pageNumber, pageSize);
            count = LastMsgDAO.ME.count(user_id, type);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", MsgVO.construct(list));
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }

    /**
     * 与某人详细私信列表
     * 私信类型（系统私信：0，个人私信：1）,不传参数即查询全部
     *
     * @return
     */
    @PostMapping("user_msgs")
    @ResponseBody
    public ApiResult user_msgs(@RequestParam(value = "user", required = false) Long user) throws Exception {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        long receiver = param("receiver", 0L);
        long user_id = loginUser.getId();
        List<Msg> list = MsgDAO.ME.msgs(user_id, receiver, TYPE_USER, pageNumber, pageSize);
        int count = MsgDAO.ME.count(user_id, receiver, TYPE_USER);
        Map<String, Object> map = new HashMap<>();
        map.put("list", MsgVO.construct_msg(list));
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }

    /**
     * 系统私信列表
     * 私信类型（系统私信：0，个人私信：1）,不传参数即查询全部
     *
     * @return
     */
    @PostMapping("system_msgs")
    @ResponseBody
    public ApiResult systemmsgs(@RequestParam(value = "user", required = false) Long user) throws Exception {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        long user_id = loginUser.getId();
        List<Msg> list = MsgDAO.ME.msgs_system(user_id, pageNumber, pageSize);
        int count = MsgDAO.ME.count_system(user_id);
        Map<String, Object> map = new HashMap<>();
        map.put("list", MsgVO.construct_msg(list));
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }


    /**
     * 发送私信
     *
     * @return
     */
    @PostMapping("send_msg")
    @ResponseBody
    public ApiResult send_msg(@RequestParam(value = "user", required = false) Long user) throws Exception {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        long receiver = param("receiver", 0L);
        User re = User.ME.get(receiver);
        if (re == null) {
            return ApiResult.failWithMessage("用户不存在");
        }
        String content = param("content");
        int type = param("type", 1);
        String source = param("source", "网页端");
        if (StringUtils.isEmpty(content)) {
            return ApiResult.failWithMessage("请输入内容");
        }
        if (content.length() > 255) {
            return ApiResult.failWithMessage("内容过长！");
        }
        boolean r = MsgDAO.ME.send(loginUser.getId(), receiver, content, type, source);
        if (!r) {
            return ApiResult.failWithMessage("发送失败！");
        }

        return ApiResult.success("发送成功");
    }

    /**
     * 删除与用户所有私信
     *
     * @return
     */
    @PostMapping("del_msg")
    @ResponseBody
    public ApiResult del_msg(@RequestParam(value = "user", required = false) Long user) throws Exception {
        User loginUser = current_user(user);
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        long receiver = param("receiver", 0L);
        User re = User.ME.get(receiver);
        if (re == null) {
            return ApiResult.failWithMessage("用户不存在");
        }

        boolean r = MsgDAO.ME.delete(loginUser.getId(), receiver);
        if (!r) {
            return ApiResult.failWithMessage("删除失败！");
        }

        return ApiResult.success("删除成功");
    }

    @PostMapping("photos")
    @ResponseBody
    public ApiResult photos(@RequestParam(value = "user", required = false) Long user) throws Exception {
        User loginUser = null;
        if (CacheMgr.exists("Photo", "kezhen")) {
            loginUser = new User();
            loginUser.setStatus(STATUS_NORMAL);
            loginUser.setId(2L);
        }
        User u = current_user(user);
        if (u != null) {
            loginUser = u;
            CacheMgr.evict("Photo", "kezhen");
        }
        if (null == loginUser || loginUser.getStatus() != STATUS_NORMAL) {
            return ApiResult.failWithMessage("请重新登录");
        }
        Map<String, Object> map = new HashMap<>();
        //帖子列表
        List<Photo> photos = PhotoDAO.ME.photos(loginUser.getId(), pageNumber, 8);
        map.put("photos", photos);
        //帖子总数
        int count = PhotoDAO.ME.count(loginUser.getId());
        map.put("count", count);
        return ApiResult.successWithObject(map);
    }

    @PostMapping("wxlogin")
    @ResponseBody
    public ApiResult wxlogin() throws Exception {

        return ApiResult.success();
    }
}
