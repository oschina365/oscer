<#include "../layout/front/layout.ftl"/>
<@html title_="oscer社区">

    <#include '../layout/front/node.ftl'/>

    <div class="layui-container">
    <form action="/tw/add" method="post" id="listForm">
        <div class="layui-form-item">
            <label class="layui-form-label">温度</label>
            <div class="layui-input-block">
                <input type="text" name="temperature" lay-verify="title" autocomplete="off" placeholder="请输入温度值"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">时间</label>
            <div class="layui-input-block">
                <input type="text" name="create_time" id="date" lay-verify="date" placeholder="请选择时间" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit="" lay-filter="add">添加</button>
            </div>
        </div>
    </form>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <div id="speedChartMain" style="width: 100%; height: 400px;"></div>
        </div>
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
        </h2>
        <div class="fly-list-info">
            <a href="/u/{{item.q_user.id}}" link>
                <cite>{{item.q_user.nickname||item.q_user.username}}</cite>
                <#--<i class="iconfont icon-renzheng" title="认证信息：XXX"></i>-->
                {{# if(item.q_user.vip_text){ }} <i class="layui-badge fly-badge-vip">{{item.q_user.vip_text}}</i> {{#
                }}}
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
        {{# if(item.q.recomm==1){ }}
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
<script src="/res/js/jquery.form.js"></script>
<script src="https://cdn.bootcss.com/echarts/4.2.1/echarts.min.js"></script>

<script>

    layui.config({
        version: "3.0.0"
        , base: '/res/mods/' //这里实际使用时，建议改成绝对路径
    }).use(['form', 'layer', 'jquery', 'laypage', 'laytpl', 'util', 'laydate'], function () {
        var form = layui.form
            , layer = parent.layer === undefined ? layui.layer : parent.layer
            ,  $ = layui.jquery, laydate = layui.laydate,form=layui.form;

        //日期
        laydate.render({
            type: 'datetime', //默认，可不填
            elem: '#date',
            format: 'yyyy-MM-dd HH:mm:ss' //可任意组合
        });

        $.ajax({
            url: '/tw/list',
            type: 'post',
            dataType: 'json',
            success: function (d) {
                if (d && d.code == 1) {

                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('speedChartMain'));
                    option = {
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            data: ['体温']
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        toolbox: {
                            feature: {
                                saveAsImage: {}
                            }
                        },
                        xAxis: {
                            type: 'category',
                            boundaryGap: false,
                            data: d.result.dates
                        },
                        yAxis: {

                            type : 'value',
                            name : '温度',
                            min: 34,
                            max: 38,
                            axisLabel: {
                                formatter: '{value} ℃'
                            }
                        },
                        series: [
                            {
                                name: '体温',
                                type: 'line',
                                stack: '总量',
                                data: d.result.nums,
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top',
                                        formatter: function(a) {

                                            return a.data+"℃";
                                        }
                                    }
                                }
                            }

                        ]
                    };
                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);

                } else {
                    layer.msg(d.message ? d.message : "暂无数据", {icon: 5});
                }
            }, error: function (d) {
                layer.msg(d.message ? d.message : "网络错误", {icon: 5});
            }
        });

        form.on("submit(add)",function(data){
            var options = {
                dataType: "json",
                success: function (d) {
                    if(d && d.code==1){
                        parent.layer.msg("添加成功~", {icon: 6});
                        setTimeout(function () {
                            window.location.reload();
                        },800)
                    }else{
                        layer.msg(d.message?d.message:"添加失败~",{icon:5});
                    }
                    return false;

                },
                error: function (d) {
                    layer.msg(d.message?d.message:"添加失败~",{icon:5});
                }
            };
            $("#listForm").ajaxSubmit(options);
            return false;
        })

    }).extend({
        fly: 'index'
    }).use('fly');

</script>

</@html>