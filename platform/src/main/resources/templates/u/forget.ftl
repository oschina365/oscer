<#include "../layout/front/layout.ftl"/>
<@html title_="登录">

<div class="layui-container fly-marginTop">
  <div class="fly-panel fly-panel-user" pad10>
    <div class="layui-tab layui-tab-brief" lay-filter="user">
      <ul class="layui-tab-title">
        <li><a href="/u/login">登入</a></li>
        <li class="layui-this">找回密码</li>
      </ul>
      <div class="layui-form layui-tab-content" id="LAY_ucm" style="padding: 20px 0;">
        <div class="layui-tab-item layui-show">
          <div class="layui-form layui-form-pane">
            <div class="layui-form-item">
              <label for="L_email" class="layui-form-label">邮箱</label>
              <div class="layui-input-inline">
                <input type="text" id="forget_email" name="email" required lay-verify="required" autocomplete="off" class="layui-input" placeholder="请输入正确的邮箱地址">
              </div>
            </div>
            <div class="layui-form-item">
              <label for="L_vercode" class="layui-form-label">验证码</label>
              <div class="layui-input-inline">
                <input type="text" id="forget_code" name="vercode" required lay-verify="required" placeholder="请输入验证码" autocomplete="off" class="layui-input">
              </div>
              <span style="color: #c00;"><div class="code" id="imageCodeImg"><img src="/captcha/?key=forgetPwdImageCode" width="116" height="36"></div></span>
            </div>
            <div class="layui-form-item">
              <button class="layui-btn" onclick="forgetpwd_check();">提交</button>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>


</div>

  <script src="/res/js/login.js"></script>

  <script>
    $(document).keydown(function (e) {
      if (e.keyCode == 13) {
        forgetpwd_check();
      }
    });

    $("#imageCodeImg").click(function(){
      $(this).find("img").attr("src", "/captcha/?key=forgetPwdImageCode");
    });
  </script>
</@html>