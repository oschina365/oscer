<!----start-header---->

<div class="fly-header layui-bg-black">
    <div class="layui-container">
        <a class="fly-logo" href="/">
            <img src="/res/images/oscer.png" style="max-height: 36px;" alt="oscer">
        </a>
        <ul class="layui-nav fly-nav layui-hide-xs">
            <li class="layui-nav-item layui-this"><a href="/">首页</a></li>
            <#--<#if login_user?? && login_user.id<=2>
                <li class="layui-nav-item">
                    <a href="/tw">体温</a>
                </li>
            </#if>-->
            <li class="layui-nav-item layui-this"><a href="/tweet">动弹广场</a></li>
        </ul>

        <ul class="layui-nav fly-nav-user">

            <!-- 登入后的状态 -->
            <#if login_user??>
                <li class="layui-nav-item">
                <a class="fly-nav-avatar" href="javascript:;">
                <cite class="layui-hide-xs">${login_user.nickname!login_user.username}</cite>
                <i class="iconfont <#--icon-renzheng--> layui-hide-xs" title="${login_user.nickname!login_user.username}"></i>
                <#if vip_text??><i class="layui-badge fly-badge-vip layui-hide-xs">${vip_text!'普通'}</i></#if>
                <img src="${login_user.headimg}">
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="/u/${login_user.id}"><i class="layui-icon" style="margin-left: 2px; font-size: 22px;">&#xe68e;</i>我的主页</a></dd>
                    <dd><a href="/u/home"><i class="layui-icon">&#xe62a;</i>我的发帖</a></dd>
                    <dd><a href="/u/msg"><i class="iconfont icon-tongzhi" style="top: 4px;"></i>我的消息</a></dd>
                    <hr style="margin: 5px 0;">
                    <dd><a href="/u/logout" style="text-align: center;">退出</a></dd>                </dl>
                </li>
            <#else >
                <!-- 未登入的状态 -->
                <li class="layui-nav-item">
                    <a class="iconfont icon-touxiang layui-hide-xs" href="/u/login"></a>
                </li>
                <li class="layui-nav-item">
                    <a href="/u/login">登入</a>
                </li>
                <li class="layui-nav-item">
                    <a href="/u/reg">注册</a>
                </li>
            </#if>
        </ul>
    </div>
</div>

<!----end-header---->