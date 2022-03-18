<#include "../layout/front/layout.ftl"/>
<@html title_="系统私信-oscer社区">

<div class="layui-container fly-marginTop fly-user-main">

  <#include '../layout/user/left_nav.ftl'/>

  <div class="fly-panel fly-panel-user" pad10>
    <div class="layui-tab layui-tab-brief" lay-filter="user">
      <div class="layui-tab-content" style="padding: 10px 0;">
        <div class="layui-tab-item layui-show">
          <ul class="fly-list">
            <div id="msgBodys"></div>
          </ul>
          <div id="page_msg"></div>
        </div>
      </div>
    </div>
  </div>
</div>

<script id="msgTpl" type="text/html">
  {{#  if(d.list!=null&&d.list.length> 0){ }}
  {{#  layui.each(d.list, function(index, item){ }}
  {{# if(item!=null){ }}
  <li>
    <a href="/u/{{item.receiver.id}}" class="fly-avatar"> <img src="{{item.receiver.headimg}}" alt="{{item.receiver.username}}"> </a>
    <h2><a href="/u/{{item.receiver.id}}" target="_blank">{{item.content}}</a></h2>
    <div class="fly-list-info">
      <a href="/u/{{item.receiver.id}}" link=""> <cite>{{item.receiver.username}}</cite></a>
      {{#  if(item.receiver.id<=2){ }}
        <span style="color: #5FB878;">管理员 </span>
      {{# }}}
      <span>{{item.sdf_insert_date}}</span>
      <a class="layui-btn layui-btn-xs layui-bg-red">删除</a>
      <a class="layui-btn layui-btn-xs layui-bg-green" onclick="send({{item.receiver.id}});">回复</a>
    </div>

  </li>
  {{# }}}
  {{#  }); }}
  {{#} else { }}
  <div class="fly-none">没有相关消息</div>
  {{# }}}
</script>

<script>
  function send(id) {
    layer.prompt({
      formType: 2,
      title: '发送私信',
      area: ['400px', '150px'] ,//自定义文本域宽高
      btn: ['发送','取消'] //按钮
    }, function(value, index, elem){
      $.ajax({
        url: '/uni/send_msg',
        method: 'post',
        dataType: 'json',
        data: {"receiver":id,"content":value},
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
      layer.close(index);
    });
  };
  layui.config({
    version: "3.0.0"
    , base: '../../res/mods/'
  }).extend({
    fly: 'index'
  }).use(['fly', 'face', 'laypage', 'laytpl', 'jquery'], function () {
    var form = layui.form, laypage = layui.laypage, laytpl = layui.laytpl, $ = layui.jquery, layer = layui.layer;
    msgs(1);


    /**
     * 查询数据列表,我的收藏
     * @param number
     */
    function msgs(number) {
      $.ajax({
        url: '/uni/system_msgs',
        method: 'post',
        dataType: 'json',
        data: {"number": number},
        success: function (data) {
          if (data && data.code == 1) {
            var listData = {"list": data.result.list};
            var getTpl = msgTpl.innerHTML, view = document.getElementById('msgBodys');

            laytpl(getTpl).render(listData, function (html) {
              view.innerHTML = html;
            });
            form.render();
            $("#user_count").text(data.result.count);
            if(data.result.count >0){
              if (number === 1) {
                //分页标签
                pageBar_msgs(data.result.count, 10);
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
    function pageBar_msgs(count, limit) {
      var themes = ['#ff0000', '#eb4310', '#3f9337', '#219167', '#239676', '#24998d', '#1f9baa', '#0080ff', '#3366cc', '#800080', '#a1488e', '#c71585', '#bd2158'];

      laypage.render({
        elem: "page_msg",
        limit: limit,
        count: count,
        first: '首页',
        last: '尾页',
        theme: themes[parseInt(Math.random() * themes.length)],
        layout: ['prev', 'page', 'next'],
        jump: function (obj, first) {
          if (!first) {
            $("#number").val(obj.curr);
            msgs(obj.curr);
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

  });

</script>

</@html>