<#include "../layout/front/layout.ftl"/>
<@html title_="登录">

<div class="layui-container fly-marginTop">
  <div class="fly-panel fly-panel-user" pad10>
    <div class="layui-tab layui-tab-brief" lay-filter="user">
      <ul class="layui-tab-title">
        <li class="layui-this">登入</li>
        <li><a href="/u/reg">注册</a></li>
      </ul>
      <div class="layui-form layui-tab-content" id="LAY_ucm" style="padding: 20px 0;">
        <div class="layui-tab-item layui-show">
          <div class="layui-form layui-form-pane">
              <div class="layui-form-item">
                <label for="L_email" class="layui-form-label">邮箱</label>
                <div class="layui-input-inline">
                  <input type="text" id="name" name="name" required lay-verify="required" autocomplete="off" class="layui-input">
                </div>
              </div>
              <div class="layui-form-item">
                <label for="L_pass" class="layui-form-label">密码</label>
                <div class="layui-input-inline">
                  <input type="password" id="pwd" name="pwd" required lay-verify="required" autocomplete="off" class="layui-input">
                </div>
              </div>
              <div class="layui-form-item">
                <button class="layui-btn" onclick="login_check()">立即登录</button>
                <span style="padding-left:20px;">
                  <a href="/u/forget">忘记密码？</a>
                </span>
              </div>
              <div class="layui-form-item fly-form-app">
                <span>或者使用第三方账号登入</span>
                <a href="/oauth/before_bind?rp=gitee"
                   onclick="layer.msg('正在通过Gitee登入', {icon:16, shade: 0.1, time:0})" title="Gitee登入">
                  <img src="/res/images/logo_gitee_light_cn_with_domain_name.png" style="max-height: 36px;">
                </a>
                <a href="/oauth/before_bind?rp=github"
                   onclick="layer.msg('正在通过Github登入', {icon:16, shade: 0.1, time:0})" title="Github登入">
                  <img src="/res/images/github.png" style="max-height: 36px;">
                </a>
              </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="../../res/js/login.js"></script>

<script>
  $(document).keydown(function (e) {
    if (e.keyCode == 13) {
      login_check();
    }
  });
</script>
</@html>