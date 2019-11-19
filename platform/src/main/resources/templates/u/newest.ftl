<#include "../layout/front/layout.ftl"/>
<@html title_="最新动态-oscer社区">

<div class="layui-container fly-marginTop fly-user-main">
  <#include '../layout/user/left_nav.ftl'/>
  
  <div class="fly-panel fly-panel-user" pad10>
	  <div class="layui-tab layui-tab-brief" lay-filter="user" id="LAY_msg" style="margin-top: 8px;">
          <ul class="home-jieda " id="bodys">
          </ul>
          <div id="page"></div>
	  </div>
  </div>

</div>

<script id="listTpl" type="text/html">
    {{#  if(d.list!=null&&d.list.length> 0){ }}
    {{#  layui.each(d.list, function(index, item){ }}
    <li>
        <a href="/u/{{item.author.id}}" class="fly-avatar" style="top: unset;"> <img src="{{item.author.headimg}}" alt="{{item.author.username}}"> </a>
        <p>

            <span>{{item.sdf_insert_date}}</span>
        </p>
        {{# if(item.d.comment> 0){ }}
            发表了评论：{{item.commentQuestion.content}}
        {{#} else { }}
        发布了新的帖子：
        {{# }}}
        <div class="home-dacontent">
            <a href="/q/{{item.q.id}}" target="_blank">{{item.q.title}}</a>
        </div>
    </li>
    {{#  }); }}
    {{#} else { }}
    <div class="fly-none">没有相关帖子</div>
    {{# }}}

</script>

<script>
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
                url: '/uni/newest',
                method: 'post',
                dataType: 'json',
                data: {"number": number},
                success: function (data) {
                    if (data && data.code == 1) {
                        var listData = {"list": data.result.list};
                        var getTpl = listTpl.innerHTML, view = document.getElementById('bodys');

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

</@html>