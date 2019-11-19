<#include "../layout/front/layout.ftl"/>
<@html title_="注册-oscer社区">

    <div class="layui-container fly-marginTop">
        <div class="fly-panel fly-panel-user" pad10>
            <div class="layui-tab layui-tab-brief" lay-filter="user">
                <ul class="layui-tab-title">
                    <li><a href="login">登入</a></li>
                    <li class="layui-this">注册</li>
                </ul>
                <div class="layui-form layui-tab-content" id="LAY_ucm" style="padding: 20px 0;">
                    <div class="layui-tab-item layui-show">
                        <div class="layui-form layui-form-pane">

                                <div class="layui-form-item">
                                    <label for="L_email" class="layui-form-label">邮箱</label>
                                    <div class="layui-input-inline">
                                        <input type="text" id="register_email" name="email" required lay-verify="email"
                                               autocomplete="off" class="layui-input">
                                    </div>
                                    <div class="layui-form-mid layui-word-aux">请输入正确的邮箱地址</div>
                                </div>
                                <div class="layui-form-item">
                                    <label for="L_username" class="layui-form-label">昵称</label>
                                    <div class="layui-input-inline">
                                        <input type="text" id="register_username" name="username" required
                                               lay-verify="required" autocomplete="off" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label for="L_pass" class="layui-form-label">密码</label>
                                    <div class="layui-input-inline">
                                        <input type="password" id="register_password" name="pass" required lay-verify="required"
                                               autocomplete="off" class="layui-input">
                                    </div>
                                    <div class="layui-form-mid layui-word-aux">6到16个字符，字母与数字结合</div>
                                </div>
                                <div class="layui-form-item">
                                    <label for="L_repass" class="layui-form-label">确认密码</label>
                                    <div class="layui-input-inline">
                                        <input type="password" id="register_password_re" name="repass" required
                                               lay-verify="required" autocomplete="off" class="layui-input">
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <label for="L_vercode" class="layui-form-label">验证码</label>
                                    <div class="layui-input-inline">
                                        <input type="text" id="vercode" name="vercode" required lay-verify="required"
                                               placeholder="请输入邮箱收到的验证码" autocomplete="off" class="layui-input">
                                    </div>
                                    <div class="layui-form-mid" style="padding: 0!important;">
                                        <button type="button" class="layui-btn layui-btn-normal" onclick="sendImageCode('register_email')" id="imageCodeImg">
                                            获取验证码
                                        </button>
                                    </div>
                                </div>
                                <div class="layui-form-item">
                                    <button class="layui-btn" onclick="register_check()">立即注册</button>
                                </div>
                                <div class="layui-form-item fly-form-app">
                                    <span>或者直接使用第三方账号快捷注册</span>
                                    <a href="/oauth/before_bind?rp=gitee"
                                       onclick="layer.msg('正在通过Gitee登入', {icon:16, shade: 0.1, time:0})"
                                       title="Gitee登入">
                                        <img src="/res/images/logo_gitee_light_cn_with_domain_name.png"
                                             style="max-height: 36px;">
                                    </a>
                                    <a href="/oauth/before_bind?rp=github"
                                       onclick="layer.msg('正在通过Github登入', {icon:16, shade: 0.1, time:0})"
                                       title="Github登入">
                                        <img src="/res/images/github.png" style="max-height: 36px;">
                                    </a>
                                </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
    <script src="/res/js/login.js"></script>
    <script>
        layui.config({
            version: "3.0.0"
            , base: '../../res/mods/'
        }).extend({
            fly: 'index'
        }).use(['fly', 'face', 'laypage', 'laytpl', 'jquery'], function () {
            var form = layui.form, $ = layui.jquery, layer = layui.layer;

            window.sendImageCode = function (email_id) {
                layui.use('layer', function(){
                    var timerYzm = null;
                    var btn = $(this);
                    var layer = layui.layer;
                    var email = $("#"+email_id).val();
                    var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
                    if (!myreg.test(email)) {
                        layer.msg("请填写正确的邮箱!", {icon: 5});
                        return false;
                    }
                    $.ajax({
                        url: "/api/email",
                        data: {"email": email,"type":"register"},
                        scriptCharset: 'utf-8',
                        type:"post",
                        success: function (data) {
                            if(data && data.code==1){
                                layer.msg("验证码发送成功!", {icon: 6});
                                var t = 60;
                                btn.prev().focus();
                                btn.addClass('disabled').val((t--) + '秒后可重新获取');
                                btn.parent().find('.yzmTip').html('验证码已下发至手机');
                                timerYzm = setInterval(function () {
                                    if (t < 1) {
                                        btn.removeClass('disabled').val('获取验证码');
                                    } else {
                                        btn.val(t + '秒后可重新获取');
                                    }
                                    t--;
                                }, 1000);
                            }else{
                                layer.msg(data.message||"验证码发送失败!", {icon: 5});
                            }
                        },error:function () {
                            layer.msg("验证码发送成功!", {icon: 6});
                            var t = 60;
                            btn.prev().focus();
                            btn.addClass('disabled').val((t--) + '秒后可重新获取');
                            btn.parent().find('.yzmTip').html('验证码已下发至手机');
                            timerYzm = setInterval(function () {
                                if (t < 1) {
                                    btn.removeClass('disabled').val('获取验证码');
                                } else {
                                    btn.val(t + '秒后可重新获取');
                                }
                                t--;
                            }, 1000);
                        }
                    });
                });
            }

        });
    </script>

</@html>