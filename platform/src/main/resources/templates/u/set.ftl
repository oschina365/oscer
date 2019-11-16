<#include "../layout/front/layout.ftl"/>
<@html title_="个人主页-oscer社区">

<div class="layui-container fly-marginTop fly-user-main">
  <#include '../layout/user/left_nav.ftl'/>

  <div class="fly-panel fly-panel-user" pad10>
    <div class="layui-tab layui-tab-brief" lay-filter="user">
      <ul class="layui-tab-title" id="LAY_mine">
        <li class="layui-this" lay-id="info">我的资料</li>
        <li lay-id="avatar">头像</li>
        <li lay-id="pass">密码</li>
        <li lay-id="bind">帐号绑定</li>
      </ul>
      <div class="layui-tab-content" style="padding: 20px 0;">
        <div class="layui-form layui-form-pane layui-tab-item layui-show">
          <form method="post" action="/uni/set_info">
            <div class="layui-form-item">
              <label for="L_email" class="layui-form-label">邮箱</label>
              <div class="layui-input-inline">
                <input type="text" name="email" required lay-verify="email" autocomplete="off" value="${login_user.email}" class="layui-input">
              </div>
              <div class="layui-form-mid layui-word-aux">如果您在邮箱已激活的情况下，变更了邮箱，需<a href="activate.html" style="font-size: 12px; color: #4f99cf;">重新验证邮箱</a>。</div>
            </div>
            <div class="layui-form-item">
              <label for="L_username" class="layui-form-label">昵称</label>
              <div class="layui-input-inline">
                <input type="text" name="nickname" required lay-verify="required" autocomplete="off" value="${login_user.nickname!''}" class="layui-input">
              </div>
              <div class="layui-inline">
                <div class="layui-input-inline">
                  <input type="radio" name="sex" value="1" <#if login_user.sex==1>checked</#if> title="男">
                  <input type="radio" name="sex" value="2" <#if login_user.sex==2>checked</#if> title="女">
                </div>
              </div>
            </div>
            <div class="layui-form-item">
              <label for="L_city" class="layui-form-label">城市</label>
              <div class="layui-input-inline">
                <input type="text" name="city" autocomplete="off" value="${login_user.city!''}" class="layui-input">
              </div>
            </div>
            <div class="layui-form-item layui-form-text">
              <label for="L_sign" class="layui-form-label">签名</label>
              <div class="layui-input-block">
                <textarea placeholder="随便写些什么刷下存在感" id="L_sign"  name="self_info" autocomplete="off" class="layui-textarea" style="height: 80px;">${login_user.self_info!''}</textarea>
              </div>
            </div>
            <div class="layui-form-item">
              <button class="layui-btn" lay-filter="infoEdit" lay-submit>确认修改</button>
            </div>
          </div>

          <div class="layui-form layui-form-pane layui-tab-item">
            <div class="layui-form-item">
              <input type="hidden" id="headimg"/>
              <div class="avatar-add">
                <button type="button" class="layui-btn upload-img" onclick="upload()">
                  <i class="layui-icon">&#xe67c;</i>上传头像
                </button>
                <img src="${login_user.headimg}" class="thumbBox">
                <span class="loading"></span>
              </div>
            </div>
          </div>

          <div class="layui-form layui-form-pane layui-tab-item">
            <form action="/u/repass" method="post">
              <div class="layui-form-item">
                <div class="layui-form-mid layui-word-aux">修改密码成功后将会退出登录</div>
              </div>
              <div class="layui-form-item">
                <label for="L_pass" class="layui-form-label">新密码</label>
                <div class="layui-input-inline">
                  <input type="password" id="L_pass"  required lay-verify="required" autocomplete="off" class="layui-input">
                </div>
                <div class="layui-form-mid layui-word-aux">6到16个字符</div>
              </div>
              <div class="layui-form-item">
                <label for="L_repass" class="layui-form-label">确认密码</label>
                <div class="layui-input-inline">
                  <input type="password" id="L_repass"  required lay-verify="required" autocomplete="off" class="layui-input">
                </div>
              </div>
              <div class="layui-form-item">
                <button class="layui-btn" key="set-mine" lay-filter="pwdEdit" lay-submit>确认修改</button>
              </div>
            </form>
          </div>
          
          <div class="layui-form layui-form-pane layui-tab-item">
            <ul class="app-bind">
              <#list froms as form>
                <li class="fly-msg app-havebind">
                  <i class="iconfont icon-qq"></i>
                  <#--<#list bindMap?keys as key>
                      ${bindMap[key]}
                  </#list>-->
                 <#if bindMap[form]??>
                   <span>已成功绑定<b>${form}</b>，您可以使用${form}帐号直接登录,</span>
                   <a href="javascript:;" class="acc-unbind" type="qq_id">解除绑定</a>
                   <#else >
                   <a href="/oauth/before_bind?rp=${form}" onclick="layer.msg('正在绑定${form}', {icon:16, shade: 0.1, time:0})" class="acc-bind" type="qq_id">立即绑定</a>
                   <span>，即可使用${form}帐号登录</span>
                 </#if>
                </li>
              </#list>

            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script>

layui.config({
  version: "3.0.0"
  , base: '../../res/mods/'
}).extend({
  fly: 'index'
}).use(['fly', 'face', 'jquery'], function () {
  var form = layui.form, $ = layui.jquery, layer = layui.layer, upload = layui.upload;

  //上传缩略图
  upload.render({
    elem: '.thumbBox',
    url: '/up/lay',
    done: function(res, index, upload){
      var src = res.result;
      $("#headimg").val(src);

      $('.thumbBox').attr('src',src);
    }
  });

  /**
   * 修改基本信息
   */
  form.on("submit(infoEdit)", function (data) {
    $.ajax({
      url: '/uni/set_info',
      method: 'post',
      dataType: 'json',
      data: data.field,
      success: function (res) {
        if (res.code == 1) {
          layer.msg('修改成功', {icon:6, shade: 0.1, time:500});
        } else {
          layer.alert(res.message);
        }
      }
    });
    return false;
  });

  /**
   * 修改密码
   */
  form.on("submit(pwdEdit)", function () {
    var L_pass = $("#L_pass").val(),L_repass = $("#L_repass").val();

    if(!L_pass){
      layer.alert("请输入新密码");
    }

    if(L_pass!=L_repass){
      layer.alert("两次密码不一致");
    }

    $.ajax({
      url: '/u/set_pwd',
      method: 'post',
      dataType: 'json',
      data: {"L_pass":L_pass,"L_repass":L_repass},
      success: function (res) {
        if (res.code == 1) {
          layer.alert('修改成功', function(index){
            window.location.href="/u/logout";
            layer.close(index);
          });
        } else {
          layer.alert(res.message);
        }
      }
    });
    return false;
  });

  window.upload = function () {
    var headimg = $("#headimg").val();
    $.ajax({
      url: '/u/set_headimg',
      method: 'post',
      dataType: 'json',
      data: {"headimg":headimg},
      success: function (data) {
        if (data && data.code == 1) {
          layer.msg("修改成功", {icon: 6});
          window.location.reload();
        } else {
          layer.msg("操作失败", {icon: 6});
        }
      }
    });
  }
});
</script>

</@html>