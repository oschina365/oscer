<!DOCTYPE html>
<html lang="zh" class="app">
<head>
    <title>oscer社区</title>
    <#include "layout/front/head.ftl"/>
</head>
<#include "layout/front/header.ftl"/>
<body>


    <#include 'layout/front/node.ftl'/>

    <div class="layui-container">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md8">

            <div class="fly-panel layui-hide-xs">
                <#include 'layout/front/carousel.ftl'/>
            </div>
            <#if tops??>
                <div class="fly-panel">
                    <div class="fly-panel-title fly-filter">&nbsp;置顶</div>
                    <ul class="fly-list">
                        <#list tops as top>
                            <li>
                                <a href="/u/${top.q_user.id}" class="fly-avatar">
                                    <img src="${top.q_user.headimg}"alt="${top.q_user.nickname!top.q_user.username}">
                                </a>
                                <h2>
                                    <a class="layui-badge" href="${top.n.url}" target="_blank">${top.n.name}</a>
                                        <#if top.q.recomm gt 0><a class="layui-badge" style="color: red;border: 1px solid red">荐</a></#if>
                                    <a href="/q/${top.q.id}">${top.q.title!''}</a>
                                </h2>
                                <div class="fly-list-info">
                                    <a href="/u/${top.q_user.id}" link>
                                    <cite>${top.q_user.nickname!top.q_user.username}</cite>
                                    <#--<i class="iconfont icon-renzheng" title="认证信息：XXX"></i>-->
                                    <#if top.q_user.vip_text?? && top.q_user.vip_text?length gt 0><i class="layui-badge fly-badge-vip">${top.q_user.vip_text}</i></#if>
                                    </a>
                                    <span>${top.q.sdf_insert_date!''}</span>

                                    <#if top.q.reward_point gt 0>
                                        <span class="fly-list-kiss layui-hide-xs" title="悬赏积分">
                                            <i class="iconfont icon-kiss"></i> ${top.q.reward_point}
                                        </span>
                                    </#if>
                                    <#if top.q.reward_comment gt 0>
                                        <span class="layui-badge fly-badge-accept layui-hide-xs">已结</span>
                                    </#if>
                                    <span class="fly-list-nums"><a href="/q/${top.q.id}" target="_blank">
                                        <i class="iconfont icon-pinglun1" title="回答"></i></a> ${top.q.comment_count}
                                    </span>
                                </div>
                                <div class="fly-list-badge">
                                <!--
                                <span class="layui-badge layui-bg-black">置顶</span>
                                <span class="layui-badge layui-bg-red">精帖</span>
                                -->
                                </div>
                            </li>
                        </#list>
                    </ul>
                </div>
            </#if>

            <div class="fly-panel" style="margin-bottom: 0;">

                <div class="fly-panel-title fly-filter">
                    <a href="/" <#if show??><#else >class="layui-this"</#if>>最新发帖</a>
                    <span class="fly-mid"></span>
                    <a href="?show=reply" <#if show?? && show == 'show'> class="layui-this"</#if>>最新回帖</a>

                    <div class="layui-hide-sm fly-column-right ">
                        <a onclick="add();" class="layui-btn layui-btn-sm layui-bg-green">发表新帖</a>
                    </div>
                </div>


                <ul class="fly-list">
                    <div id="questionBodys"></div>
                </ul>
                <div id="page"></div>
            </div>
        </div>

        <#include 'layout/front/index_right_nav.ftl'/>
    </div>
    </div>

    <script id="questionListTpl" type="text/html">
    {{#  if(d.list!=null&&d.list.length> 0){ }}
    {{#  layui.each(d.list, function(index, item){ }}
    <li>
        <a href="/u/{{item.q_user.id}}" class="fly-avatar">
            <img src="{{item.q_user.headimg}}" alt="{{item.q_user.nickname||item.q_user.username}}">
        </a>
        <h2>
            <a class="layui-badge" href="{{item.n.url}}" target="_blank">{{item.n.name}}</a>
            <a href="/q/{{item.q.id}}" target="_blank">{{item.q.title}}</a>
            {{# if(item.q.system_top> 0){ }}<a class="layui-badge layui-bg-blue">顶</a>{{# }}}
            {{# if(item.q.recomm==1){ }}  <a class="layui-badge layui-bg-red">荐</a>{{# }}}
        </h2>
        <div class="fly-list-info">
            <a href="/u/{{item.q_user.id}}" link>
                <cite>{{item.q_user.nickname||item.q_user.username}}</cite>
                {{# if(item.q_user.id<= 2){ }}
                <i class="iconfont icon-renzheng" title="认证信息：管理员"></i>
                {{# }}}
                {{# if(item.q_user.vip_text){ }} <i class="layui-badge fly-badge-vip">{{item.q_user.vip_text}}</i>  {{# }}}
            </a>
            <span>{{item.q.sdf_insert_date}}</span>

            {{# if(item.q.reward_point> 0){ }}
            <span class="fly-list-kiss layui-hide-xs" title="悬赏积分"><i class="layui-icon  layui-icon-diamond"></i> {{item.q.reward_point}}</span>
            {{# }}}
            {{# if(item.q.reward_comment> 0){ }}
            <span class="layui-badge fly-badge-accept layui-hide-xs">已悬赏</span>
            {{# }}}
            <span class="fly-list-nums">
                <i class="iconfont" title="人气">&#xe60b;</i> {{item.q.view_count||0}}
                <a href="/q/{{item.q.id}}" target="_blank"><i class="iconfont icon-pinglun1" title="回答"></i> </a>{{item.q.comment_count||0}}
            </span>

        </div>

    </li>
    {{#  }); }}
    {{#} else { }}
    <div class="fly-none">没有相关帖子</div>
    {{# }}}

</script>
    <script>

    function add() {
        $.ajax({
            url: '/u/logined',
            method: 'post',
            dataType: 'json',
            success: function (d) {
                if (d && d.code == 1) {
                    window.open("/q/add","_blank");
                } else {
                    layui.layer.msg(d.message ? d.message : "网络问题，请重试", {icon: 5});
                }
            },error:function () {
                layui.layer.msg("网络问题，请重试", {icon: 5});
            }
        });
    };

    layui.use('carousel', function () {
        var carousel = layui.carousel;

        //建造实例
        carousel.render({
            elem: '#carousel-banners'
            , width: '100%' //设置容器宽度
            , height: '242px'
            , arrow: 'hover' //始终显示箭头
            , anim: 'fade' //切换动画方式
            , indicator: 'none'
        });
    });

    layui.config({
        version: "3.0.0"
        , base: '/res/mods/' //这里实际使用时，建议改成绝对路径
    }).use(['form', 'layer', 'jquery', 'laypage', 'laytpl', 'util'], function () {
        var form = layui.form
            , layer = parent.layer === undefined ? layui.layer : parent.layer
            , laypage = layui.laypage, laytpl = layui.laytpl, $ = layui.jquery;

        dataList(1);

        /**
         * 查询数据列表
         * @param number
         */
        function dataList(number) {
            var show = $("#show").val();
            $.ajax({
                url: '/uni/q/list',
                method: 'post',
                dataType: 'json',
                data: {"number": number,"show":show},
                success: function (data) {
                    if (data && data.code == 1) {
                        var listData = {"list": data.result.questions};
                        var getTpl = questionListTpl.innerHTML, view = document.getElementById('questionBodys');


                        laytpl(getTpl).render(listData, function (html) {
                            view.innerHTML = html;
                        });
                        form.render();

                        if(data.result.count >0){
                            if (number === 1) {
                                //分页标签
                                pageBar(data.result.count, 10);
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
    }).extend({
        fly: 'index'
    }).use('fly');

</script>


</body>
<#include "layout/front/footer.ftl"/>
</html>