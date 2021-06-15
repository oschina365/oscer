<#include "../layout/front/layout.ftl"/>
<@html title_="我的照片">

<div class="fly-main" style="overflow: hidden;">

  <div class="layui-upload" style="margin-top: 20px;">
    <button type="button" class="layui-btn" id="test2">多图片上传</button>
    <blockquote class="layui-elem-quote layui-quote-nm" style="margin-top: 10px;display: none;" id="prevImage">
      预览图：
      <div class="layui-upload-list" id="demo2"></div>
    </blockquote>
  </div>

  <ul class="fly-case-list" id="photosUl">
  </ul>

  <div style="text-align: center;" id="page">
  </div>

</div>
  <script src="/res/resume/js/index.js"></script>
<script id="listTpl" type="text/html">
    {{#  if(d.list!=null&&d.list.length> 0){ }}
    {{#  layui.each(d.list, function(index, item){ }}
    {{# if(item){ }}
    <li data-id="{{item.id}}">
      <div class="fly-case-img photos"  >
        {{# if(item.type=="image"){ }}
        <img src="{{item.url}}" class="photos">
        {{#} else { }}
        <video width="239" height="150" controls>
          <source src="{{item.url}}"  type="video/mp4">
          <source src="{{item.url}}"  type="video/ogg">
          您的浏览器不支持 HTML5 video 标签。
        </video>
        {{# }}}
      </div>

      <h2>
        <a class="layui-btn primary sm" href="{{item.url}}">查看大图</a>
        {{item.year}}年{{item.month}}月{{item.day}}日</h2>
    </li>
    {{#} else { }}
    {{# }}}
    {{#  }); }}
    {{#} else { }}
    <div class="fly-none">没有图片</div>
    {{# }}}

  </script>
<script>
  layui.config({
    version: "3.0.0"
    , base: '/res/mods/' //这里实际使用时，建议改成绝对路径
  }).use(['form', 'layer', 'jquery', 'laypage', 'laytpl','upload'], function () {
    var form = layui.form,upload = layui.upload
            , layer = parent.layer === undefined ? layui.layer : parent.layer
            , laypage = layui.laypage, laytpl = layui.laytpl, $ = layui.jquery;

//相册
    layer.photos({
      photos: '.photos'
      ,zIndex: 9999999999
      ,anim: -1
    });
    //多图片上传
    upload.render({
      elem: '#test2'
      ,url: '/up/photo' //改成您自己的上传接口
      ,multiple: true
      ,before: function(obj){
        //预读本地文件示例，不支持ie8
        obj.preview(function(index, file, result){
          $('#demo2').append('<img src="'+ result +'" alt="'+ file.name +'" class="layui-upload-img">')
        });
      }
      ,done: function(res){
        //上传完毕
        $("#prevImage").show();
      }
    });

    dataList(1);
    /**
     * 查询数据列表
     * @param number
     */
    function dataList(number) {
      $.ajax({
        url: '/uni/photos',
        method: 'post',
        dataType: 'json',
        data: {"number": number},
        success: function (data) {
          if (data && data.code == 1) {
            var listData = {"list": data.result.photos};
            console.log(listData);
            var getTpl = listTpl.innerHTML, view = document.getElementById('photosUl');

            laytpl(getTpl).render(listData, function (html) {
              console.log(html);
              view.innerHTML = html;
            });
            form.render();

            if(data.result.count >0){
              if (number === 1) {
                //分页标签
                pageBar(data.result.count, 8);
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