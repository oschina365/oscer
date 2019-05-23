<!----start-header---->

<div class="fly-header layui-bg-black">
    <div class="layui-container">
        <a class="fly-logo" href="/">
            <img src="/res/images/oscer.png" style="max-height: 36px;" alt="layui">
        </a>
        <ul class="layui-nav fly-nav layui-hide-xs">
            <li class="layui-nav-item layui-this">
                <a href="/">首页</a>
            </li>
            <li class="layui-nav-item">
                <a href="/nodes">节点</a>
            </li>

        </ul>

        <ul class="layui-nav fly-nav-user">

            <!-- 登入后的状态 -->
            <#if login_user??>
                <li class="layui-nav-item">
                <a class="fly-nav-avatar" href="javascript:;">
                <cite class="layui-hide-xs">${login_user.nickname!login_user.username}</cite>
            <i class="iconfont icon-renzheng layui-hide-xs" title="${login_user.nickname!login_user.username}"></i>
            <i class="layui-badge fly-badge-vip layui-hide-xs">VIP3</i>
            <img src="${login_user.headimg}">
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="/user/set"><i class="layui-icon">&#xe620;</i>基本设置</a></dd>
                    <dd><a href="/user/message"><i class="iconfont icon-tongzhi" style="top: 4px;"></i>我的消息</a>
                    </dd>
                    <dd><a href="/user/home"><i class="layui-icon" style="margin-left: 2px; font-size: 22px;">&#xe68e;</i>我的主页</a>
                    </dd>
                    <hr style="margin: 5px 0;">
                    <dd><a href="/user/logout" style="text-align: center;">退出</a></dd>
                </dl>
                </li>
            <#else >
                <!-- 未登入的状态 -->
                <li class="layui-nav-item">
                    <a class="iconfont icon-touxiang layui-hide-xs" href="/user/login"></a>
                </li>
                <li class="layui-nav-item">
                    <a href="/user/login">登入</a>
                </li>
                <li class="layui-nav-item">
                    <a href="/user/reg">注册</a>
                </li>
                <li class="layui-nav-item layui-hide-xs">
                    <a href="/oauth/before_bind?rp=gitee"
                       onclick="layer.msg('正在通过Gitee登入', {icon:16, shade: 0.1, time:0})" title="Gitee登入">
                        <img src="/res/images/logo_gitee_white_cn.png" style="max-height: 36px;">
                    </a>
                </li>
            </#if>
        </ul>
    </div>
</div>

<!----end-header---->