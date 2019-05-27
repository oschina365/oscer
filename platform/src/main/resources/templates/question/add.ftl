<#include "../layout/front/layout.ftl"/>
<@html title_="发帖">

<div class="layui-container fly-marginTop">
  <div class="fly-panel" pad20 style="padding-top: 5px;">
    <!--<div class="fly-none">没有权限</div>-->
    <div class="layui-form layui-form-pane">
      <div class="layui-tab layui-tab-brief" lay-filter="user">
        <ul class="layui-tab-title">
          <li><a href="/">返回</a></li>
          <li class="layui-this">发表新帖<!-- 编辑帖子 --></li>
        </ul>
        <div class="layui-form layui-tab-content" id="LAY_ucm" style="padding: 20px 0;">
          <div class="layui-tab-item layui-show">
            <form action="/q/add" method="post" id="listForm">
              <div class="layui-row layui-col-space15 layui-form-item">
                <div class="layui-col-md3">
                  <label class="layui-form-label">选择节点</label>
                  <div class="layui-input-block">
                    <select lay-verify="required" name="node" lay-filter="column">
                      <#if nodes??>
                        <#list nodes as node>
                          <option value="${node.id}">${node.name}</option>
                        </#list>
                      </#if>
                    </select>
                  </div>
                </div>
                <div class="layui-col-md9">
                  <label for="L_title" class="layui-form-label">标题</label>
                  <div class="layui-input-block">
                    <input type="text" id="L_title" name="title" required lay-verify="required" autocomplete="off" class="layui-input">
                  </div>
                </div>
              </div>
              <div class="layui-form-item layui-form-text">
                <div class="layui-input-block">
                  <textarea id="L_content" name="content" required lay-verify="required" placeholder="详细描述" class="layui-textarea fly-editor" style="height: 260px;"></textarea>
                </div>
              </div>
              <div class="layui-form-item">
                <label class="layui-form-label">是否悬赏</label>
                <div class="layui-input-block">
                  <input type="checkbox" lay-skin="switch" lay-filter="reward" >
                  <input type="hidden" id="is_reward" name="is_reward" value="0"/>
                </div>
              </div>
              <div class="layui-form-item layui-hide" id="reward_input">
                <div class="layui-inline">
                  <label class="layui-form-label">悬赏金币</label>
                  <div class="layui-input-inline" style="width: 190px;">
                    <select name="reward_point">
                      <option value="10">10</option>
                      <option value="20">20</option>
                      <option value="50">50</option>
                      <option value="100">100</option>
                    </select>
                  </div>
                  <div class="layui-form-mid layui-word-aux">发表后无法更改金币</div>
                </div>
              </div>
             <#-- <div class="layui-form-item">
                <label for="L_vercode" class="layui-form-label">人类验证</label>
                <div class="layui-input-inline">
                  <input type="text" id="L_vercode" name="vercode" required lay-verify="required" placeholder="请回答后面的问题" autocomplete="off" class="layui-input">
                </div>
                <div class="layui-form-mid">
                  <span style="color: #c00;">1+1=?</span>
                </div>
              </div>-->
              <div class="layui-form-item">
                <button class="layui-btn" lay-filter="questionAdd" lay-submit>立即发布</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>


<script src="/res/layui/layui.js"></script>
<script>

layui.config({
  version: "3.0.0"
  ,base: '../../res/mods/'
}).use(['form','layer'],function(){
  var form = layui.form, layer = parent.layer === undefined ? layui.layer : parent.layer, $ = layui.jquery;

  form.on('switch(reward)', function(data){
    if(data.elem.checked){
      $("#reward_input").removeClass("layui-hide");
      $("#is_reward").val(1);
    }else{
      $("#reward_input").addClass("layui-hide");
      $("#is_reward").val(0);
    }
  });

  //监听提交
  form.on('submit(questionAdd)', function(data){
    $.ajax({
      url:'/q/add',
      method:'post',
      dataType:'json',
      data:data.field,
      success:function(data){0

        if(data && data.code==1){
          parent.layer.msg(data.message ? data.message : "发帖成功~", {icon: 6});
        }else{
          layer.msg(data.message?data.message:"创建失败~",{icon:5});
        }
      }
    });
    return false;
  });
}).extend({
  fly: 'index'
}).use('fly');
</script>

</@html>