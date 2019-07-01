<#include "../layout/front/layout.ftl"/>
<@html title_="${u.nickname!u.username}的主页">

<div class="fly-home fly-panel" style="background-image: url();">
  <img src="${u.headimg!'/res/images/oscer.png'}" alt="${u.nickname!u.username}">
  <#--<i class="iconfont icon-renzheng" title="社区认证"></i>-->
  <h1>
    ${u.nickname!u.username}
    <#if u.sex==1>
      <i class="iconfont icon-nan"></i>
      <#elseif u.sex==2>
      <i class="iconfont icon-nv"></i>
    </#if>
    <#if vip?? && vip?length gt 0><i class="layui-badge fly-badge-vip">${vip}</i></#if>
    <!--
    <span style="color:#c00;">（管理员）</span>
    <span style="color:#5FB878;">（社区之光）</span>
    <span>（该号已被封）</span>
    -->
  </h1>

  <#if u.id==2><p style="padding: 10px 0; color: #5FB878;">认证信息：管理员 </p></#if>

  <p class="fly-home-info">
    <i class="layui-icon  layui-icon-diamond" title="积分"></i><span style="color: #FF7200;">${u.score!'0'}</span>
    <i class="iconfont icon-shijian"></i><span>${u.insert_date} 加入</span>
    <#--<i class="iconfont icon-chengshi"></i><span>来自杭州</span>-->
  </p>

  <p class="fly-home-sign">（${u.self_info!'这个人很懒~~~'}）</p>

  <div class="fly-sns" data-user="">
    <a href="javascript:;" class="layui-btn layui-btn-primary fly-imActive" data-type="addFriend">加为好友</a>
    <a href="javascript:;" class="layui-btn layui-btn-normal fly-imActive" data-type="chat">发起会话</a>
  </div>

</div>

<div class="layui-container">
  <div class="layui-row layui-col-space15">
    <div class="layui-col-md6 fly-home-jie">
      <div class="fly-panel">
        <h3 class="fly-panel-title">最近的发帖</h3>
        <ul class="jie-row">
          <#if questions??>
              <#list questions as q>
                <li>
                  <#if q.recomm gt 0><span class="fly-jing">精</span></#if>
                  <#if q.top gt 0><span class="fly-stick">顶</span></#if>
                  <a href="/q/${q.id}" class="jie-title" target="_blank"> ${q.title}</a>
                  <i>${q.insert_date?string('yyyy-MM-dd')}</i>
                  <em class="layui-hide-xs">${q.view_count!'0'}阅/${q.comment_count!'0'}答</em>
                </li>
              </#list>
            <#else >
              <div class="fly-none" style="min-height: 50px; padding:30px 0; height:auto;"><i style="font-size:14px;">暂无发帖</i></div>
          </#if>

        </ul>
      </div>
    </div>
    
    <div class="layui-col-md6 fly-home-da">
      <div class="fly-panel">
        <h3 class="fly-panel-title">最近的回帖</h3>
        <ul class="home-jieda">
          <#if comments?? && commentMap??>
            <#list comments as c>
              <#if commentMap[''+c.question]??>
                <li>
                  <p>
                  <span>${c.insert_date}</span>
                  在<a href="/q/${commentMap[''+c.question].id}" target="_blank"> ${commentMap[''+c.question].title}</a>中回答：
                  </p>
                  <div class="home-dacontent">
                  ${c.content}
                  </div>
                </li>
              </#if>

            </#list>
            <#else >
              <div class="fly-none" style="min-height: 50px; padding:30px 0; height:auto;"><span>没有回答任何帖子</span></div>
          </#if>

        </ul>
      </div>
    </div>
  </div>
</div>

<script src="../../res/layui/layui.js"></script>
<script>
layui.cache.page = 'user';
layui.cache.user = {
  username: '游客'
  ,uid: -1
  ,avatar: '../../res/images/avatar/00.jpg'
  ,experience: 83
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