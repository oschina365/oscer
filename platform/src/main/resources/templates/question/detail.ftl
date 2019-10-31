<#include "../layout/front/layout.ftl"/>
<@html title_="${q.title}-oscer社区">

<div class="layui-container" style="margin-top: 8px;">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md8 content detail">
            <div class="fly-panel detail-box">
                <h1>${q.title}</h1>
                <div class="fly-detail-info">
                    <#if q.status==2>
                        <span class="layui-badge">审核中</span>
                    </#if>
                    <#if q.status==1>
                        <span class="layui-badge layui-bg-orange">私有</span>
                    </#if>

                    <#if q.reward_point gt 0>
                        <#if q.reward_comment == 0>
                            <span class="layui-badge layui-btn-warm" >悬赏中</span>
                        <#else >
                            <span class="layui-badge" style="background-color: #5FB878;">已悬赏</span>
                        </#if>
                    </#if>

                    <#if q.top ==1><span class="layui-badge layui-bg-black">顶</span></#if>

                    <#if q.recomm ==1><span class="layui-badge layui-bg-red">推荐</span></#if>

                    <#if login_user??>
                        <div class="fly-admin-box">
                            <#if login_user.id=author || login_user.id=2>
                            <a onclick="del()"><span class="layui-btn layui-btn-xs jie-admin layui-btn-danger">删除</span></a>
                            </#if>

                            <#if login_user.id=2>
                                <#if q.recomm ==1>
                                    <a onclick="recomm()"><span class="layui-btn layui-btn-xs"style="background-color:#ccc;">取消推荐</span></a>
                                <#else >
                                    <a onclick="recomm()"><span class="layui-btn layui-btn-xs " >推荐</span></a>
                                </#if>
                                <#if q.system_top ==1>
                                    <a onclick="as_top()"><span class="layui-btn layui-btn-xs" style="background-color:#ccc;">取消置顶</span></a>
                                <#else >
                                    <a onclick="as_top()"><span class="layui-btn layui-btn-xs">置顶</span></a>
                                </#if>
                            </#if>

                            <#if login_user?? && (login_user.id=author || login_user.id==2)>
                                <a href="/q/edit/${q.id}"><span class="layui-btn layui-btn-xs jie-admin">编辑此贴</span></a>
                            </#if>

                            <#if login_user?? && login_user.id!=author>
                                <#if followed>
                                    <a onclick="follow()"><span class="layui-btn layui-btn-xs jie-admin layui-btn-danger">取消关注</span></a>
                                <#else >
                                    <a onclick="follow()"><span class="layui-btn layui-btn-xs jie-admin layui-btn-normal">关注</span></a>
                                </#if>
                                <a href="add.html"><span class="layui-btn layui-btn-xs jie-admin layui-btn-warm">私信</span></a>
                            </#if>
                        </div>
                    </#if>


                </div>

                <div class="detail-about">
                    <a class="fly-avatar" href="/u/${u.id}"><img src="${u.headimg}" alt="${u.nickname!u.username!u.name}"></a>
                    <div class="fly-detail-user">
                        <a href="/u/${u.id}" class="fly-link">
                            <cite>${u.nickname!u.username!u.name}</cite>
                            <#--<i class="iconfont icon-renzheng" title="认证信息：{{ rows.user.approve }}"></i>
                            <i class="layui-badge fly-badge-vip">VIP3</i>-->
                        </a>
                        <span>${q.insert_date}</span>


                    </div>
                    <div class="detail-hits" id="LAY_jieAdmin">
                        <#if q.reward_point gt 0>
                            <span style="padding-right: 10px; color: #FF7200">悬赏：${q.reward_point!'0'}积分</span>
                        </#if>

                        <span class="fly-list-nums" style="position: unset">
                            <a href="#comments"><i class="iconfont" title="回答">&#xe60c;</i> ${q.comment_count!'0'}</a>
                            <i class="iconfont" title="人气">&#xe60b;</i> ${q.view_count!'0'}
                            <a onclick="collect()">
                                <#if collected?? && collected>
                                    <i class="layui-icon" title="收藏" style="color: red" >&#xe67a;</i> ${q.collect_count!'0'}
                                    <#else >
                                    <i class="layui-icon" title="收藏">&#xe67b;</i> ${q.collect_count!'0'}
                                </#if>
                            </a>

                        </span>

                    </div>
                </div>

                <div class="detail-body layui-text photos">${q.content}</div>
            </div>
            <a name="comments"></a>
            <div class="fly-panel detail-box" id="flyReply">
                <fieldset class="layui-elem-field layui-field-title" style="text-align: center;">
                    <legend>回帖</legend>
                </fieldset>

                <ul class="jieda" id="jieda"><div id="commentBodys"></div></ul>
                <div id="page" style="text-align: center;"></div>
            </div>

            <div class="fly-panel detail-box">
                <div class="layui-form layui-form-pane">
                    <form action="/uni/user_pub_q_comment" method="post" id="commentFrom">
                        <input type="hidden" name="id" value="${q.id}"/>
                        <div class="layui-form-item layui-form-text">
                            <input name="comment" type="hidden"/>
                            <div class="layui-input-block">
                                <textarea name="content" placeholder="<#if login_user??>请输入内容<#else >请先登录再评论</#if>" class="layui-textarea fly-editor" style="height: 150px;"></textarea>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <button class="layui-btn" lay-filter="commentAdd" lay-submit>发布评论</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <#if authorQuestions?? && authorQuestions?size gt 0>
            <div class="layui-col-md4">
            <dl class="fly-panel fly-list-one">
            <dt class="fly-panel-title">楼主其它帖子</dt>
            <#list authorQuestions as orther>
                <#if orther?? && orther.title??>
                    <dd>
                    <a href="">${orther.title}</a>
                    <span><i class="iconfont icon-pinglun1"></i> ${orther.comment_count}</span>
                    </dd>
                </#if>

            </#list>

            <!-- 无数据时 -->
            <!--
            <div class="fly-none">没有相关数据</div>
            -->
            </dl>
            </div>
        </#if>
    </div>
</div>

<script id="commentListTpl" type="text/html">
    {{#  if(d.list!=null&&d.list.length> 0){ }}
    {{#  layui.each(d.list, function(index, item){ }}
    <li class="jieda-daan">
        <div class="detail-about detail-about-reply">
            <p>{{d.rankMap[item.cq.id]}}楼<span>&nbsp;&nbsp;&nbsp;{{item.sdf_insert_date}}</span></p>
            <a class="fly-avatar" href="">
                <img src="{{item.cu.headimg}}" alt=" ">
            </a>
            <div class="fly-detail-user" style="margin-top: 10px;">
                <a href="/u/{{item.cu.id}}" class="fly-link">
                    <cite>{{item.cu.nickname||item.cu.username}}</cite>
                    <#--<i class="iconfont icon-renzheng" title="认证信息：XXX"></i>-->
                    {{# if(item.cu.vip_text){ }}
                    <i class="layui-badge fly-badge-vip">{{item.cu.vip_text}}</i>
                    {{# }}}
                </a>

                {{# if(item.cu.id==2){ }}<span>(楼主)</span>{{# }}}
            </div>

            {{# if(item.bestComment){ }} <i class="iconfont icon-caina" title="最佳答案"></i>{{# }}}

            <div class="jieda-reply">
                <a onclick="praise({{item.cq.id}})">
              <span class="jieda-zan {{# if(item.praise){ }}zanok{{# }}}" type="zan">
                <i class="iconfont icon-zan"></i>
                <em>{{item.cq.praise_count}}</em>
              </span>
                </a>
                <span type="reply">
                <a onclick="show_reply_dom({{item.cq.id}})">
                <i class="iconfont icon-svgmoban53"></i>
                回复
                </a>
              </span>

                {{# if(item.bestComment){ }}
                <div class="jieda-admin">
                    <span type="edit">编辑</span>
                    <span type="del">删除</span>
                    <!-- <span class="jieda-accept" type="accept">采纳</span> -->
                </div>
                {{# }}}
            </div>

        </div>
        <div class="detail-body jieda-body photos">
            <p>{{item.cq.content}}</p>
        </div>
        {{# if(item.childs && item.childs.length>0){ }}
            <div class="childs" id="childs_{{item.cq.id}}">
                <ul class="jieda" id="jieda">
                    <div id="commentChildBodys">
                        {{#  layui.each(item.childs, function(index, child){ }}
                            <li class="jieda-daan"  {{# if(index>2){ }} style="display: none" {{# }}} >
                                <div class="detail-about detail-about-reply"> <p><span>&nbsp;&nbsp;&nbsp;{{child.sdf_insert_date}}</span></p>
                                    <a class="fly-avatar" href="">
                                        <img src="{{child.cu.headimg}}" alt=" ">
                                    </a>
                                    <div class="fly-detail-user" style="margin-top: 10px;">
                                        <a href="/u/{{child.cu.id}}" class="fly-link">
                                            <cite>{{child.cu.nickname||child.cu.username}}</cite>
                                            {{# if(child.cu.vip_text){ }}
                                            <i class="layui-badge fly-badge-vip">{{child.cu.vip_text}}</i>
                                            {{# }}}
                                        </a>
                                        {{# if(child.cu.id==2){ }}<span>(楼主)</span>{{# }}}
                                    </div>
                                    <div class="jieda-reply">
                                        <a onclick="praise({{child.cq.id}})">
                                            <span class="jieda-zan " type="zan"> <i class="iconfont icon-zan"></i>  <em>{{child.cq.praise_count}}</em> </span>
                                        </a>
                                        <span type="reply">
                                            <a onclick="show_reply_dom({{item.cq.id}},{{child.cq.id}})"> <i class="iconfont icon-svgmoban53"></i> 回复 </a>
                                        </span>
                                    </div>
                                </div>
                                <div class="detail-body jieda-body photos" style="margin: unset;"> <p>{{child.cq.content}}</p> </div>
                                <div id="reply_div_{{child.cq.id}}" class="layui-form-item layui-form-text" style="margin-bottom: 22px;display: none">
                                    <textarea name="content" id="content_{{child.cq.id}}" placeholder="<#if login_user??>请输入内容<#else >请先登录再评论</#if>" class="layui-textarea fly-editor"></textarea>
                                </div>
                            </li>
                        {{#  }); }}
                    </div>
                </ul>
                {{# if(item.cq.reply_count >0 && item.cq.reply_count >5){ }}
                <a onclick="more_child_comments({{item.cq.id}})" style="cursor:pointer">查看更多回复</a>
                {{# }}}
            </div>
        {{# }}}


        <div id="reply_div_{{item.cq.id}}" class="layui-form-item layui-form-text" style="margin-bottom: 22px;display: none">
            <textarea name="content" id="content_{{item.cq.id}}" placeholder="<#if login_user??>请输入内容<#else >请先登录再评论</#if>" class="layui-textarea fly-editor"></textarea>
        </div>
    </li>
    {{#  }); }}
    {{#} else { }}
    <li class="fly-none">消灭零回复</li>
    {{# }}}

</script>

<script src="/res/js/jquery.form.js"></script>
<script>
    function show_reply_dom(id,childid) {
        var a = id;
        if(childid){
            a = childid;
        }
        var l = $("#reply_div_"+a)[0].style.display;
        if(l=='none'){
            $("#reply_div_"+a).show();
        }else{
            var content = $("#content_"+a).val();
            console.log(content);
            if(!content){
                $("#reply_div_"+a).hide();
            }else{
                $.ajax({
                    url: '/uni/user_pub_q_comment',
                    method: 'post',
                    dataType: 'json',
                    data: {'id':${q.id},'content':content,'parent':id},
                    success: function (res) {
                        if (res.code == 1) {
                            layer.msg('评论成功', {icon:6, shade: 0.1, time:500});
                            location.reload();

                        } else {
                            layer.alert(res.message);
                        }
                    }
                });
                return false;
            }

        }
    }

    function more_child_comments(id) {
        if(id){
            $("#childs_"+id+" li").show();
        }
    }

    function follow() {
        $.ajax({
            url: '/f/follow',
            method: 'post',
            dataType: 'json',
            data: {"friend":${author}},
            success: function (d) {
                if (d && d.code == 1) {
                    layui.layer.msg(d.message ? d.message : "操作成功", {icon: 6});
                } else {
                    layui.layer.msg(d.message ? d.message : "网络问题，请重试", {icon: 5});
                }
                location.reload();
            },error:function () {
                layui.layer.msg("网络问题，请重试", {icon: 5});
            }
        });
    };
    function del() {
        $.ajax({
            url: '/uni/q/delete/'+${q.id},
            method: 'post',
            dataType: 'json',
            success: function (data) {
                if (data && data.code == 1) {
                    layer.msg("删除成功", {icon: 6});
                    setTimeout(function () {
                        location.href="/";
                    },800)

                } else {
                    layui.layer.msg(data.message?data.message:"删除失败~",{icon:5});
                }
            }
        });
    }

    function recomm() {
        $.ajax({
            url: '/q/recomm',
            method: 'post',
            dataType: 'json',
            data: {"id":${q.id}},
            success: function (data) {
                if (data && data.code == 1) {
                    layui.layer.msg("操作成功", {icon: 6});
                    window.location.reload();
                } else {
                    layui.layer.msg("操作失败", {icon: 6});
                }
            }
        });
    };

    function as_top() {
        $.ajax({
            url: '/q/as_top',
            method: 'post',
            dataType: 'json',
            data: {"id":${q.id}},
            success: function (data) {
                if (data && data.code == 1) {
                    layui.layer.msg("操作成功", {icon: 6});
                    window.location.reload();
                } else {
                    layui.layer.msg("操作失败", {icon: 6});
                }
            }
        });
    };

    function collect(){
        $.ajax({
            url: '/uni/q/collect',
            method: 'post',
            dataType: 'json',
            data: {"id":${q.id}},
            success: function (d) {
                if (d && d.code == 1) {
                    layer.msg(d.message ? d.message : "操作成功", {icon: 6});
                } else {
                    layer.msg(d.message ? d.message : "网络问题，请重试", {icon: 5});
                }
            }
        });
    };


    layui.config({
        version: "3.0.0"
        , base: '../../res/mods/'
    }).extend({
        fly: 'index'
    }).use(['fly', 'face', 'laypage', 'laytpl', 'jquery'], function () {
        var form = layui.form, laypage = layui.laypage, laytpl = layui.laytpl, $ = layui.jquery, layer = layui.layer;

        dataList(1);
        /**
         * 查询数据列表
         * @param number
         */
        function dataList(number) {
            $.ajax({
                url: '/uni/q/comments',
                method: 'post',
                dataType: 'json',
                data: {"id":${q.id}, "number": number},
                success: function (data) {
                    if (data && data.code == 1) {
                        var listData = {"list": data.result.comments,"rankMap": data.result.rankMap};
                        var getTpl = commentListTpl.innerHTML, view = document.getElementById('commentBodys');

                        laytpl(getTpl).render(listData, function (html) {
                            view.innerHTML = html;
                        });
                        if (number === 1 && data.result.count>0) {
                            //分页标签
                            pageBar(data.result.count, 10);
                        }
                    }
                }
            });
        }

        /**
         * 数据分页
         * @param count
         * @param limit
         */
        function pageBar(count, limit) {
            var themes = ['#ff0000', '#eb4310', '#3f9337', '#219167', '#239676', '#24998d', '#1f9baa', '#0080ff', '#3366cc', '#800080', '#a1488e', '#c71585', '#bd2158'];

            laypage.render({
                elem: "page",
                limit: limit,
                count: count,
                first: '首页',
                last: '尾页',
                theme: themes[parseInt(Math.random() * themes.length)],
                layout: ['prev', 'page', 'next'],
                jump: function (obj, first) {
                    if (!first) {
                        $("#number").val(obj.curr);
                        dataList(obj.curr);
                    }
                }
            });
        }

        form.on("submit(commentAdd)", function (data) {
            $.ajax({
                url: '/uni/user_pub_q_comment',
                method: 'post',
                dataType: 'json',
                data: data.field,
                success: function (res) {
                    if (res.code == 1) {
                        layer.msg('评论成功', {icon:6, shade: 0.1, time:500});
                        location.reload();

                    } else {
                        layer.alert(res.message);
                    }
                }
            });
            return false;
        });




    });

</script>

</@html>