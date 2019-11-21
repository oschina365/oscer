<#--个人空间左侧导航栏-->
    <ul class="layui-nav layui-nav-tree layui-inline" lay-filter="user">
        <#if login_user??>
            <li class="layui-nav-item">
                <a href="/u/${login_user.id}">
                <i class="layui-icon">&#xe609;</i>
                我的主页
                </a>
            </li>
        </#if>
        <li class="layui-nav-item <#if currentUrl?? && currentUrl=='/u/newest'>layui-this</#if>">
            <a href="/u/newest">
                <i class="layui-icon">&#xe63a;</i>
                最新动态
            </a>
        </li>
        <li class="layui-nav-item <#if currentUrl?? && currentUrl=='/u/home'>layui-this</#if>">
            <a href="/u/home">
                <i class="layui-icon">&#xe612;</i>
                用户中心
            </a>
        </li>
        <li class="layui-nav-item <#if currentUrl?? && currentUrl=='/u/msg'>layui-this</#if>">
            <a href="/u/msg">
                <i class="layui-icon">&#xe611;</i>
                用户私信
            </a>
        </li>
        <li class="layui-nav-item <#if currentUrl?? && currentUrl=='/u/sys_msg'>layui-this</#if>">
            <a href="/u/sys_msg">
                <i class="layui-icon">&#xe606;</i>
                系统消息
            </a>
        </li>
        <li class="layui-nav-item <#if currentUrl?? && currentUrl=="/u/set">layui-this</#if>">
            <a href="/u/set">
                <i class="layui-icon">&#xe620;</i>
                基本设置
            </a>
        </li>
    </ul>

    <div class="site-tree-mobile layui-hide">
        <i class="layui-icon">&#xe602;</i>
    </div>
    <div class="site-mobile-shade"></div>

    <div class="site-tree-mobile layui-hide">
        <i class="layui-icon">&#xe602;</i>
    </div>
    <div class="site-mobile-shade"></div>
<#---->