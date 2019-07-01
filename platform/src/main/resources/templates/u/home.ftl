<#include "../layout/front/layout.ftl"/>
<@html title_="个人主页-oscer社区">

<div class="layui-container fly-marginTop fly-user-main">

    <#include '../layout/user/left_nav.ftl'/>

    <div class="fly-panel fly-panel-user" pad20>
        <#--
        <div class="fly-msg" style="margin-top: 15px;">
          您的邮箱尚未验证，这比较影响您的帐号安全，<a href="activate.html">立即去激活？</a>
        </div>-->

        <div class="layui-tab layui-tab-brief" lay-filter="user">
            <ul class="layui-tab-title" id="LAY_mine">
                <li data-type="mine-jie" lay-id="index" class="layui-this">
                    我发的帖（<span><#if questions_pub?? && (questions_pub?size > 0)>${questions_pub?size}<#else >0</#if></span>）
                </li>
                <li data-type="collection" data-url="/collection/find/" lay-id="collection">
                    我收藏的帖（<span><#if questions_collect?? && (questions_collect?size > 0)>${questions_collect?size}<#else >0</#if></span>）
                </li>
            </ul>
            <div class="layui-tab-content" style="padding: 20px 0;">
                <div class="layui-tab-item layui-show">
                    <ul class="mine-view jie-row">
                        <#if questions_pub?? && (questions_pub?size > 0)>
                            <#list questions_pub as pub>
                                <li>
                                <a class="jie-title" href="/q/${pub.id}" target="_blank">${pub.title}</a>
                                <i>${pub.insert_date}</i>
                                <a class="mine-edit" href="/q/edit/${pub.id}">编辑</a>
                                <em>${pub.view_count!'0'}阅/${pub.comment_count!'0'}答</em>
                                </li>
                            </#list>
                            <#else >
                                <p style="text-align: center;">暂无发帖</p>
                        </#if>

                    </ul>
                    <div id="LAY_page"></div>
                </div>
                <div class="layui-tab-item">
                    <ul class="mine-view jie-row">
                        <#if questions_collect?? && (questions_collect?size > 0)>
                            <#list questions_collect as collect>
                                <li>
                                    <a class="jie-title" href="/q/${collect.id}" target="_blank">${collect.title}</a>
                                </li>
                            </#list>
                            <#else >
                            <p style="text-align: center;">暂无收藏</p>
                        </#if>
                    </ul>
                    <div id="LAY_page1"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="../../res/layui/layui.js"></script>
<script>

    layui.config({
        version: "3.0.0"
        , base: '../../res/mods/'
    }).extend({
        fly: 'index'
    }).use('fly');
</script>

</@html>