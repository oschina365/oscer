<#include "../layout/front/layout.ftl"/>
<@html title_="oscer社区-404">

<div class="layui-container fly-marginTop">
	<div class="fly-panel">
	  <div class="fly-none">
	    <h2><i class="iconfont icon-404"></i></h2>
	    <p>页面或者数据被运到火星了，啥都看不到了…</p>
	  </div>
	</div>
</div>


<script src="/res/layui/layui.js"></script>
<script>
layui.cache.page = '';
layui.cache.user = {
  username: '游客'
  ,uid: -1
  ,avatar: '../../res/images/avatar/00.jpg'
  ,experience: 123
  ,sex: '男'
};
layui.config({
  version: "3.0.0"
  ,base: '../../res/mods/'
}).extend({
  fly: 'index'
}).use('fly');
</script>

</@html>