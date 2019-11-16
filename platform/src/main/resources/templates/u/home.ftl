<#include "../layout/front/layout.ftl"/>
<@html title_="个人主页-oscer社区">

<div class="layui-container fly-marginTop fly-user-main">

    <#include '../layout/user/left_nav.ftl'/>

    <div class="fly-panel fly-panel-user" pad10>
        <#--<div class="fly-msg" style="margin-top: 15px;">
          您的邮箱尚未验证，这比较影响您的帐号安全，<a href="activate.html">立即去激活？</a>
        </div>-->

        <div class="layui-tab layui-tab-brief" lay-filter="user">
            <ul class="layui-tab-title" id="LAY_mine">
                <li data-type="mine-jie" lay-id="index" class="layui-this">
                    我的发帖（<span id="pub_count">0</span>）
                </li>
                <li data-type="collection" lay-id="collection">
                    我收藏的帖（<span id="collect_count">0</span>）
                </li>
                <li data-type="collection" lay-id="collection">
                    我的关注（<span id="follow_count">0</span>）
                </li>
                <li data-type="collection" lay-id="collection">
                    我的粉丝（<span id="fan_count">0</span>）
                </li>
            </ul>
            <div class="layui-tab-content" style="padding: 20px 0;">
                <div class="layui-tab-item layui-show">
                    <ul class="mine-view jie-row">
                        <div id="pubBodys"></div>
                    </ul>
                    <div id="page_pub"></div>
                </div>
                <div class="layui-tab-item">
                    <ul class="mine-view jie-row">
                        <div id="collectBodys"></div>
                    </ul>
                    <div id="page_collect"></div>
                </div>
                <div class="layui-tab-item">
                    <ul class="mine-view jie-row">
                        <div id="followBodys"></div>
                    </ul>
                    <div id="page_follow"></div>
                </div>
                <div class="layui-tab-item">
                    <ul class="mine-view jie-row">
                        <div id="fanBodys"></div>
                    </ul>
                    <div id="page_fan"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script id="pubsTpl" type="text/html">
    {{#  if(d.list!=null&&d.list.length> 0){ }}
    {{#  layui.each(d.list, function(index, item){ }}
    <li>
        <a class="jie-title" href="/q/{{item.id}}" target="_blank">{{item.title}}</a>
        <i>{{item.sdf_insert_date}}</i>
        <a class="mine-edit" href="/q/edit/{{item.id}}">编辑</a>
        <a class="mine-edit" onclick="del({{item.id}})" style="background-color: #01AAED;cursor: pointer;">删除</a>
        <em>{{item.view_count||0}} 阅/{{item.comment_count||0}} 答</em>

    </li>
    {{#  }); }}
    {{#} else { }}
    <div class="fly-none">没有相关帖子</div>
    {{# }}}

</script>

<script id="collectsTpl" type="text/html">
    {{d.list}}
    {{#  if(d.list!=null&&d.list.length> 0){ }}
    {{#  layui.each(d.list, function(index, item){ }}
    <li>
        <a class="jie-title" href="/q/{{item.id}}" target="_blank">{{item.title}}</a>
        <i>{{item.sdf_insert_date}}</i>
        <a class="mine-edit" onclick="collect({{item.id}})" style="background-color: #01AAED;cursor: pointer;">取消收藏</a>
        <em>{{item.view_count||0}} 阅/{{item.comment_count||0}} 答</em>

    </li>
    {{#  }); }}
    {{#} else { }}
    <div class="fly-none">没有相关帖子</div>
    {{# }}}

</script>

<script id="followsTpl" type="text/html">
    {{#  if(d.list!=null&&d.list.length> 0){ }}
    {{#  layui.each(d.list, function(index, item){ }}
    <li>
        <a href="/u/{{item.id}}" target="_blank" class="fly-avatar" style="left: unset;top: unset;">
            <img src="{{item.headimg}}" alt="{{item.nickname||item.username}}">
            <span style="background-color: white;color: black;font-size: 14px;">{{item.nickname||item.username}}</span>
            <span style="background-color: white;color: black;font-size: 14px;">积分:{{item.score}}</span>
            <span style="background-color: white;color: black;font-size: 14px;">注册时间:{{item.sdf_insert_date}}</span>
        </a>
        <a class="mine-edit" style="background-color: #ff0000;cursor: pointer;" onclick="follow({{item.id}})">取消关注</a>
    </li>
    {{#  }); }}
    {{#} else { }}
    <div class="fly-none">没有相关帖子</div>
    {{# }}}

</script>

<script id="fansTpl" type="text/html">
    {{#  if(d.list!=null&&d.list.length> 0){ }}
    {{#  layui.each(d.list, function(index, item){ }}
    <li>
        <a href="/u/{{item.id}}" target="_blank" class="fly-avatar" style="left: unset;top: unset;">
            <img src="{{item.headimg}}" alt="{{item.nickname||item.username}}">
            <span style="background-color: white;color: black;font-size: 14px;">{{item.nickname||item.username}}</span>
            <span style="background-color: white;color: black;font-size: 14px;">积分:{{item.score}}</span>
            <span style="background-color: white;color: black;font-size: 14px;">注册时间:{{item.sdf_insert_date}}</span>
        </a>
        {{# if(fn(d.currentFans,item.id) ){ }}
        <span style="color: #ff0000;font-size: 14px;">互相关注</span>
        {{# }}}
        <a class="mine-edit" style="background-color: #24998D;cursor: pointer;" onclick="follow({{item.id}})">
            {{# if(fn(d.currentFans,item.id) ){ }}
                取消关注
            {{# }}}
            {{# if(!fn(d.currentFans,item.id) ){ }}
            关注
            {{# }}}
        </a>
        <a class="mine-edit" style="background-color: #3366CC;cursor: pointer;" onclick="follow({{item.id}})">私信</a>
    </li>
    {{#  }); }}
    {{#} else { }}
    <div class="fly-none">没有相关帖子</div>
    {{# }}}

</script>

<script>
    function fn(list,id) {
        for(var i in list){
            if(list[i]==id){
                return true;
            }
        }
        return false;
    }
    layui.config({
        version: "3.0.0"
        , base: '../../res/mods/'
    }).extend({
        fly: 'index'
    }).use(['fly', 'face', 'laypage', 'laytpl', 'jquery'], function () {
        var form = layui.form, laypage = layui.laypage, laytpl = layui.laytpl, $ = layui.jquery, layer = layui.layer;
        pubs(1);
        collects(1);
        follows(1);
        fans(1);
        /**
         * 查询数据列表,我的发帖
         * @param number
         */
        function pubs(number) {
            $.ajax({
                url: '/u/pubs',
                method: 'post',
                dataType: 'json',
                data: {"number": number},
                success: function (data) {
                    if (data && data.code == 1) {
                        var listData = {"list": data.result.questions};
                        var getTpl = pubsTpl.innerHTML, view = document.getElementById('pubBodys');

                        laytpl(getTpl).render(listData, function (html) {
                            view.innerHTML = html;
                        });
                        form.render();
                        $("#pub_count").text(data.result.count);
                        if(data.result.count >0){

                            if (number === 1) {
                                //分页标签
                                pageBar_pub(data.result.count, 10);
                            }
                        }

                    }
                }
            });
        }


        /**
         * 查询数据列表,我的收藏
         * @param number
         */
        function collects(number) {
            $.ajax({
                url: '/u/collects',
                method: 'post',
                dataType: 'json',
                data: {"number": number},
                success: function (data) {
                    if (data && data.code == 1) {
                        var listData = {"list": data.result.questions_collect};
                        var getTpl = collectsTpl.innerHTML, view = document.getElementById('collectBodys');


                        laytpl(getTpl).render(listData, function (html) {
                            view.innerHTML = html;
                        });
                        form.render();
                        $("#collect_count").text(data.result.count);
                        if(data.result.count >0){
                            if (number === 1) {
                                //分页标签
                                pageBar_collect(data.result.count, 10);
                            }
                        }

                    }
                }
            });
        }

        /**
         * 查询数据列表,我的关注
         * @param number
         */
        function follows(number) {
            $.ajax({
                url: '/u/follows',
                method: 'post',
                dataType: 'json',
                data: {"number": number},
                success: function (data) {
                    if (data && data.code == 1) {
                        var listData = {"list": data.result.follows};
                        var getTpl = followsTpl.innerHTML, view = document.getElementById('followBodys');


                        laytpl(getTpl).render(listData, function (html) {
                            view.innerHTML = html;
                        });
                        form.render();
                        $("#follow_count").text(data.result.count);
                        if(data.result.count >0){
                            if (number === 1) {
                                //分页标签
                                pageBar_follow(data.result.count, 10);
                            }
                        }

                    }
                }
            });
        }

        /**
         * 查询数据列表,我的粉丝
         * @param number
         */
        function fans(number) {
            $.ajax({
                url: '/u/fans',
                method: 'post',
                dataType: 'json',
                data: {"number": number},
                success: function (data) {
                    if (data && data.code == 1) {
                        var listData = {"list": data.result.fans,"currentFans":data.result.currentFans};
                        var getTpl = fansTpl.innerHTML, view = document.getElementById('fanBodys');

                        laytpl(getTpl).render(listData, function (html) {
                            view.innerHTML = html;
                        });
                        form.render();
                        $("#fan_count").text(data.result.count);
                        if(data.result.count >0){
                            if (number === 1) {
                                //分页标签
                                pageBar_fan(data.result.count, 10);
                            }
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
        function pageBar_pub(count, limit) {
            var themes = ['#ff0000', '#eb4310', '#3f9337', '#219167', '#239676', '#24998d', '#1f9baa', '#0080ff', '#3366cc', '#800080', '#a1488e', '#c71585', '#bd2158'];

            laypage.render({
                elem: "page_pub",
                limit: limit,
                count: count,
                first: '首页',
                last: '尾页',
                theme: themes[parseInt(Math.random() * themes.length)],
                layout: ['prev', 'page', 'next'],
                jump: function (obj, first) {
                    if (!first) {
                        $("#number").val(obj.curr);
                        pubs(obj.curr);
                    }
                }
            });
        }


        /**
         * 数据分页
         * @param count
         * @param limit
         */
        function pageBar_collect(count, limit) {
            var themes = ['#ff0000', '#eb4310', '#3f9337', '#219167', '#239676', '#24998d', '#1f9baa', '#0080ff', '#3366cc', '#800080', '#a1488e', '#c71585', '#bd2158'];

            laypage.render({
                elem: "page_collect",
                limit: limit,
                count: count,
                first: '首页',
                last: '尾页',
                theme: themes[parseInt(Math.random() * themes.length)],
                layout: ['prev', 'page', 'next'],
                jump: function (obj, first) {
                    if (!first) {
                        $("#number").val(obj.curr);
                        collects(obj.curr);
                    }
                }
            });
        }

        /**
         * 数据分页
         * @param count
         * @param limit
         */
        function pageBar_follow(count, limit) {
            var themes = ['#ff0000', '#eb4310', '#3f9337', '#219167', '#239676', '#24998d', '#1f9baa', '#0080ff', '#3366cc', '#800080', '#a1488e', '#c71585', '#bd2158'];

            laypage.render({
                elem: "page_follow",
                limit: limit,
                count: count,
                first: '首页',
                last: '尾页',
                theme: themes[parseInt(Math.random() * themes.length)],
                layout: ['prev', 'page', 'next'],
                jump: function (obj, first) {
                    if (!first) {
                        $("#number").val(obj.curr);
                        collects(obj.curr);
                    }
                }
            });
        }

        /**
         * 数据分页
         * @param count
         * @param limit
         */
        function pageBar_fan(count, limit) {
            var themes = ['#ff0000', '#eb4310', '#3f9337', '#219167', '#239676', '#24998d', '#1f9baa', '#0080ff', '#3366cc', '#800080', '#a1488e', '#c71585', '#bd2158'];

            laypage.render({
                elem: "page_fan",
                limit: limit,
                count: count,
                first: '首页',
                last: '尾页',
                theme: themes[parseInt(Math.random() * themes.length)],
                layout: ['prev', 'page', 'next'],
                jump: function (obj, first) {
                    if (!first) {
                        $("#number").val(obj.curr);
                        fans(obj.curr);
                    }
                }
            });
        }

        window.del = function (id) {
            $.ajax({
                url: '/uni/q/delete/'+id,
                method: 'post',
                dataType: 'json',
                success: function (data) {
                    if (data && data.code == 1) {
                        layer.msg("删除成功", {icon: 6});
                        setTimeout(function () {
                            pubs(1);
                        },800)

                    } else {
                        layer.msg(data.message?data.message:"删除失败~",{icon:5});
                    }
                }
            });
        }

        window.collect = function (id) {
            $.ajax({
                url: '/uni/q/collect',
                method: 'post',
                dataType: 'json',
                data:{'id':id},
                success: function (data) {
                    if (data && data.code == 1) {
                        layer.msg("取消收藏成功", {icon: 6});
                        setTimeout(function () {
                            collects(1);
                        },800)

                    } else {
                        layer.msg(data.message?data.message:"操作失败~",{icon:5});
                    }
                }
            });
        }


        window.follow = function(id) {
            $.ajax({
                url: '/f/follow',
                method: 'post',
                dataType: 'json',
                data: {"friend":id},
                success: function (d) {
                    if (d && d.code == 1) {
                        layui.layer.msg(d.message ? d.message : "操作成功", {icon: 6});
                    } else {
                        layui.layer.msg(d.message ? d.message : "网络问题，请重试", {icon: 5});
                    }
                    setTimeout(function () {
                        location.reload();
                    },500)

                },error:function () {
                    layui.layer.msg("网络问题，请重试", {icon: 5});
                }
            });
        };
    });

</script>

</@html>