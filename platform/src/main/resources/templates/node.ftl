<#include "layout/front/layout.ftl"/>
<@html title_="oscer社区">

<#include 'layout/front/node.ftl'/>

<div class="layui-container">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md8">
            <div class="fly-panel" style="margin-bottom: 0;">

                <div class="fly-panel-title fly-filter">
                    <a href="" class="layui-this">综合</a>
                    <span class="fly-mid"></span>
                    <a href="">未结</a>
                    <span class="fly-mid"></span>
                    <a href="">已结</a>
                    <span class="fly-mid"></span>
                    <a href="">精华</a>
                    <span class="fly-filter-right layui-hide-xs">
                    <a href="" class="layui-this">按最新</a>
            <span class="fly-mid"></span>
            <a href="">按热议</a>
          </span>
                </div>

                <ul class="fly-list"><div id="questionBodys"></div></ul>
                <div id="page"></div>
            </div>
        </div>
        <div class="layui-col-md4">

            <dl class="fly-panel fly-list-one">
                <dt class="fly-panel-title">本周热议</dt>
                <#if weekHots??>
                    <#list weekHots as hot>
                        <dd>
                        <a href="/q/${hot.id}">${hot.title}</a>
                        <span><i class="iconfont icon-pinglun1"></i> ${hot.comment_count}</span>
                        </dd>
                    </#list>
                <#else >
                    <div class="fly-none">没有相关数据</div>
                </#if>
            </dl>

            <div class="fly-panel">
                <div class="fly-panel-title">
                    这里可作为广告区域
                </div>
                <div class="fly-panel-main">
                    <a href="http://layim.layui.com/?from=fly" target="_blank" class="fly-zanzhu"
                       time-limit="2017.09.25-2099.01.01" style="background-color: #5FB878;">LayIM 3.0 - layui 旗舰之作</a>
                </div>
            </div>

        </div>
    </div>
</div>

<script id="questionListTpl" type="text/html">
    {{#  if(d.list!=null&&d.list.length> 0){ }}
    {{#  layui.each(d.list, function(index, item){ }}
    <li>
        <a href="/user/{{item.q_user.id}}" class="fly-avatar">
            <img src="{{item.q_user.headimg}}"alt="{{item.q_user.nickname||item.q_user.username}}">
        </a>
        <h2>
            <a class="layui-badge" href="{{item.n.url}}" target="_blank">{{item.n.name}}</a>
            <a href="/q/{{item.q.id}}" target="_blank">{{item.q.title}}</a>
        </h2>
        <div class="fly-list-info">
            <a href="/user/{{item.q_user.id}}" link>
                <cite>{{item.q_user.nickname||item.q_user.username}}</cite>
                <#--<i class="iconfont icon-renzheng" title="认证信息：XXX"></i>-->
                <i class="layui-badge fly-badge-vip">{{item.q_user.vip_text}}</i>
            </a>
            <span>{{item.q.sdf_insert_date}}</span>

            {{#  if(item.q.reward_point> 0){ }}
            <span class="fly-list-kiss layui-hide-xs" title="悬赏积分"><i class="layui-icon  layui-icon-diamond"></i> {{item.q.reward_point}}</span>
            {{# }}}
            {{#  if(item.q.reward_user> 0){ }}
            <span class="layui-badge fly-badge-accept layui-hide-xs">已悬赏</span>
            {{# }}}
            <span class="fly-list-nums">
                <a href="/q/{{item.q.id}}" target="_blank"><i class="iconfont icon-pinglun1" title="回答"></i> </a>{{item.q.comment_count||0}}
            </span>
        </div>
        {{#  if(item.q.recomm==1){ }}
        <div class="fly-list-badge">
            <span class="layui-badge layui-bg-red">精帖</span>
        </div>
        {{# }}}
    </li>
    {{#  }); }}
    {{#} else { }}
        <div class="fly-none">没有相关帖子</div>
    {{# }}}

</script>

<script src="http://cdn.bootcss.com/jquery/2.1.3/jquery.min.js"></script>
<script src="/res/layui/layui.js"></script>
<script>
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
            $.ajax({
                url: '/q/list',
                method: 'post',
                dataType: 'json',
                data: {"number": number,"id":${current_node.id}},
                success: function (data) {
                    if (data && data.code == 1) {
                        var listData = {"list": data.result.questions};
                        var getTpl = questionListTpl.innerHTML, view = document.getElementById('questionBodys');

                        laytpl(getTpl).render(listData, function (html) {
                            view.innerHTML = html;
                        });
                        form.render();
                        if (number === 1) {
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
            var themes = ['#ff0000', '#eb4310', '#f6941d', '#fbb417', '#ffff00', '#cdd541', '#99cc33', '#3f9337', '#219167', '#239676', '#24998d', '#1f9baa', '#0080ff', '#3366cc', '#333399', '#003366', '#800080', '#a1488e', '#c71585', '#bd2158'];

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
<script type="text/javascript">
    var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
    document.write(unescape("%3Cspan id='cnzz_stat_icon_30088308'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "w.cnzz.com/c.php%3Fid%3D30088308' type='text/javascript'%3E%3C/script%3E"));
</script>
</@html>