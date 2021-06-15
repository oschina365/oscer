<div class="layui-col-md4">

    <#--<div class="fly-panel">
        <div class="fly-panel" style="padding: 10px 0; text-align: center;">
            <img src="/res/images/oscer_wx.jpg" style="max-width: 100%;max-height: 215px;" alt="oscer">
            <p style="position: relative; color: #999;">扫码体验小程序</p>
        </div>
    </div>-->

    <div class="fly-panel">
        <div class="fly-panel" style="padding: 10px 0; text-align: center;">
            <#include '../../tweet/tweet_push.ftl'/>
            <#include '../../tweet/tweet_slide.ftl'/>
            <#include '../../tweet/tweet_list.ftl'/>
        </div>
    </div>


    <div class="fly-panel fly-signin">
        <div class="fly-panel-title">
            签到
            <i class="fly-mid"></i>
            <a href="javascript:;" class="fly-link" id="LAY_signinHelp">说明</a>
            <i class="fly-mid"></i>
            <#if login_user?? >
                <span class="fly-signin-days">已连续签到<cite id="series_count"><#if sign??>${sign.series_count!'0'}<#else >0</#if></cite>天</span>
            </#if>
        </div>
        <div class="fly-panel-main fly-signin-main">
            <#if login_user?? >
                <#if signed?? && signed >
                    <!-- 已签到状态 -->
                    <div id="LAY_signed">
                    <button class="layui-btn layui-btn-disabled">今日已签到</button>
                    <span>获得了<cite>${sign_today_score!'5'}</cite>积分</span>
                    </div>
                <#else >
                    <div id="LAY_signing">
                    <button class="layui-btn layui-bg-orange">今日签到</button>
                    <span>
                    <c>可获得</c><cite>${sign_today_score!'5'}</cite>积分</span>
                    </div>
                </#if>
            <#else >
                <div id="LAY_sign">
                <button class="layui-btn layui-bg-orange">签到</button>
                <span>已有${count_signed_today!'0'}人签到</span>
                </div>
            </#if>

        </div>
    </div>

    <#--<#include 'layout/front/ad.ftl'/>-->

    <div class="fly-panel fly-rank fly-rank-reply" id="LAY_replyRank">
        <h3 class="fly-panel-title">回贴周榜</h3>
        <#if weekUserCommentHots??>
            <dl>
            <#list weekUserCommentHots as item>
                <dd>
                <#list item as key, value>
                    <a href="/u/${key.id}">
                <img src="${key.headimg}"><cite>${key.nickname!key.username}</cite><i>${value!'0'}次回答</i>
                    </a>
                </#list>
                </dd>
            </#list>
            </dl>
        <#else >
            <div class="fly-none" style="min-height: 10px;padding-top: 10px;">没有相关数据</div>
        </#if>
    </div>

    <dl class="fly-panel fly-list-one">
        <dt class="fly-panel-title">本周热议</dt>
        <#if weekHots??>
            <#list weekHots as hot>
                <#if hot?? && hot.id gt 0>
                    <dd>
                    <a href="/q/${hot.id}">${hot.title}</a>
                    <span><i class="iconfont icon-pinglun1"></i> ${hot.comment_count}</span>
                    </dd>
                </#if>

            </#list>
        <#else >
            <div class="fly-none" style="min-height: 10px;padding-top: 10px;padding-bottom: 14px;">没有相关数据</div>
        </#if>
    </dl>

    <div class="fly-panel fly-link">
        <h3 class="fly-panel-title">友情链接</h3>
        <dl class="fly-panel-main">
            <dd><a href="http://www.layui.com" target="_blank">layui</a>
            <dd>
            <dd><a href="https://www.oschina.net" target="_blank">开源中国</a>
            <dd>
            <#--<dd><a href="" class="fly-link">申请友链</a>-->
            <dd>
        </dl>
    </div>
</div>