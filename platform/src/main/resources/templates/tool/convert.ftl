<#include "../layout/front/layout.ftl"/>
<@html title_="自动生成set方法">

<div class="layui-container fly-marginTop">
  <div class="fly-panel fly-panel-user" pad10>
    <div class="layui-tab layui-tab-brief" lay-filter="user">
      <ul class="layui-tab-title">
        <li class="layui-this">自动生成set方法</li>
      </ul>
      <div>
        <label>说明：开发中，往往两个类型很有多重名的属性，使用 <b>BeanUtils.copyProperties</b>会有性能和报错兼容性问题，这时候需要手动写</label><br>
        <label>生成的代码示例：</label>
        <div>
          <p>public void convert(User user){<br />
            &nbsp;&nbsp; &nbsp;this.setSex(user.getSex());<br />
            &nbsp;&nbsp; &nbsp;this.setAge(user.getAge());<br />
            &nbsp;&nbsp; &nbsp;this.setName(user.getName());<br />
            }</p>

        </div>
      </div>
      <div class="layui-form layui-tab-content" id="LAY_ucm" style="padding: 20px 0;">
        <div class="layui-tab-item layui-show">
          <div class="layui-form layui-form-pane">
              <div class="layui-form-item">
                <label for="L_email" class="layui-form-label">类名A</label>
                <div class="layui-input-inline">
                  <input type="text" id="classA" name="classA" required lay-verify="required" autocomplete="off" class="layui-input" >
                  <p>说明：请输入完整路径,如com.xxx.xxx.User</p>
                </div>
              </div>
              <div class="layui-form-item">
                <label for="L_pass" class="layui-form-label">类名B</label>
                <div class="layui-input-inline">
                  <input type="text" id="classB" name="classB" required lay-verify="required" autocomplete="off" class="layui-input">
                  <p>说明：请输入完整路径,如com.xxx.xxx.Account</p>
                </div>
              </div>
              <div class="layui-form-item">
                <button class="layui-btn" onclick="convert()">立即生成</button>
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
      convert();
    }
  });


  /**
   * 登录检查
   */
  function convert() {
    layui.use('layer', function(){
      var layer = layui.layer;
      var classA = $("#classA").val();
      var classB = $("#classB").val();
      if (!classA) {
        layer.msg("请输入完整路径A",{icon:5});
        return false;
      } else if (!classB) {
        layer.msg("请输入完整路径B",{icon:5});
        return false;
      } else {
        $.ajax({
          url: '/u/login',
          type: 'post',
          data: {'classA': classA, 'classB': classB},
          dataType: 'json',
          success: function (d) {
            if (d.code==1) {
              layer.msg(d.message?d.message:"登录成功",{icon:6,time:500},function () {
                location.href = "/";
              });
            } else {
              layer.msg(d.message?d.message:"登录失败",{icon:5});
            }
          }
        });
      }
    });
  }
</script>
</@html>