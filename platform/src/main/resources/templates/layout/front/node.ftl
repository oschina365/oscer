<div class="fly-panel fly-column">
    <input type="hidden" id="show" value="${show!}"/>
    <div class="layui-container">
        <ul class="layui-clear">
            <#if nodes??>
                <#list nodes as node>
                    <li <#if current_node?? && current_node.url == node.url> class="layui-this"</#if>><a href="${node.url}">${node.name}</a></li>
                </#list>
            </#if>

            <li class="layui-hide-xs layui-hide-sm layui-show-md-inline-block"><span class="fly-mid"></span></li>

        </ul>

        <#if login_user??>
            <div class="layui-hide-xs fly-column-right ">
                <span class="fly-search"><i class="layui-icon"></i></span>
                <a href="/q/add" class="layui-btn">发表新帖</a>
            </div>
        </#if>
    </div>
</div>