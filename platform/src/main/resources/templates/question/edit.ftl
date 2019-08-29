<#include "../layout/front/layout.ftl"/>
<@html title_="编辑帖子">

<div class="layui-container fly-marginTop">
    <div class="fly-panel" pad20 style="padding-top: 5px;">
        <#if login_user?? && login_user.id==author>
            <div class="layui-form layui-form-pane">
                <div class="layui-tab layui-tab-brief" lay-filter="user">
                    <ul class="layui-tab-title">
                        <li><a href="/">返回</a></li>
                        <li class="layui-this">编辑帖子</li>
                    </ul>
                    <div class="layui-form layui-tab-content" id="LAY_ucm" style="padding: 20px 0;">
                    <div class="layui-tab-item layui-show">
                        <form action="/q/edit" method="post" id="listForm">
                            <input type="hidden" name="id" id="id" value="${q.id}"/>
                            <input type="hidden" name="content" id="content"/>

                            <div class="layui-row layui-col-space15 layui-form-item">
                                <div class="layui-col-md3">
                                    <label class="layui-form-label">选择节点</label>
                                    <div class="layui-input-block">
                                        <select lay-verify="required" name="node" lay-filter="column">
                                        <#if nodes??>
                                            <#list nodes as node>
                                                <option value="${node.id}" <#if node.id=q.node>selected</#if>>${node.name}</option>
                                            </#list>
                                        </#if>
                                        </select>
                                    </div>
                                </div>
                                <div class="layui-col-md9">
                                    <label for="L_title" class="layui-form-label">标题</label>
                                    <div class="layui-input-block">
                                        <input type="text" id="L_title" name="title" required lay-verify="required" autocomplete="off" class="layui-input" value="${q.title!''}">
                                    </div>
                                </div>
                            </div>

                            <div class="layui-form-item layui-form-text">
                                <div class="layui-input-block">
                                    <textarea id="contents" placeholder="详细描述" class="layui-textarea fly-editor" style="height: 260px;">${q.content!''}</textarea>
                                </div>
                            </div>

                            <div class="layui-form-item">
                                <button class="layui-btn" lay-filter="questionEdit" lay-submit>保存编辑</button>
                            </div>
                        </form>
                    </div>
                </div>
                </div>
            </div>
        <#else >
            <div class="fly-none">没有权限</div>
        </#if>
    </div>
</div>
<script src="http://cdn.bootcss.com/jquery/2.1.3/jquery.min.js"></script>
<script src="/res/js/jquery.form.js"></script>
<script src="/res/layui/layui.js"></script>
<script>
    layui.use(['form', 'layer', 'layedit', 'laydate', 'upload'], function () {
        var form = layui.form,
            layer = parent.layer === undefined ? layui.layer : top.layer,
            laypage = layui.laypage,
            upload = layui.upload,
            layedit = layui.layedit,
            laydate = layui.laydate,
            $ = layui.jquery;

        //用于同步编辑器内容到textarea
        layedit.sync(editIndex);

        //编辑一个编辑器
        layedit.set({
            uploadImage: {
                url: '/api/upload' //接口url
                , type: 'post' //默认post
            }
        });
        var editIndex = layedit.build('contents', {
            tool: ["strong", "italic", "underline", "del", "|", "left", "center", "right", "|", "link", "unlink", "face", "image", "code"],
            height: 400,

        });

        form.on("submit(questionEdit)", function (data) {

            var content = layedit.getContent(editIndex);
            $("#content").val(content);

            var options = {
                dataType: "json",
                success: function (d) {
                    if (d && d.code == 1) {
                        console.log(d);
                        parent.layer.msg("编辑成功~", {icon: 6});
                        setTimeout(function () {
                            window.location.href = "/q/" + d.result;
                        }, 800)
                    } else {
                        layer.msg(d.message ? d.message : "编辑失败~", {icon: 5});
                    }

                },
                error: function (d) {
                    layer.msg(d.message ? d.message : "编辑失败~", {icon: 5});
                }
            };
            $("#listForm").ajaxSubmit(options);
            return false;
        })
    })

</script>
</@html>