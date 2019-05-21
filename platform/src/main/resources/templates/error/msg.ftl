<!doctype html>
<html class="no-js">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>温馨提示--></title>

    <!-- Set render engine for 360 browser -->
    <meta name="renderer" content="webkit">

    <!-- No Baidu Siteapp-->
    <meta http-equiv="Cache-Control" content="no-siteapp"/>

    <link rel="stylesheet" href="http://cdn.amazeui.org/amazeui/2.3.0/css/amazeui.min.css">
</head>
<body>
<div class="admin-content">
<#if success>
    <div class="am-cf am-padding">
        <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">成功</strong></div>
    </div>
    <div class="am-g">
        <div class="am-u-sm-12">
            <h2 class="am-text-center am-margin-top-lg">操作成功！</h2>
            <p class="am-text-center">页面正在跳转中，还剩<span class="am-badge am-badge-danger am-text-xl" id="time"></span>秒……
            </p>
            <div class="am-btn-group am-btn-group-justify" style="width: 50%;text-align: center;margin:0 auto;">
                <a class="am-btn am-btn-warning" role="button" href="javascript:;" onclick="returnBack();">返回上级</a>
                <a class="am-btn am-btn-danger" role="button" href="/user/main/home">回到首页</a>
            </div>
        </div>
    </div>
<#else>
    <div class="am-cf am-padding">
        <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">错误</strong> /
            <small>如果您看到这个页面，那么操作失败了……</small>
        </div>
    </div>
    <div class="am-g">
        <div class="am-u-sm-12">
            <h2 class="am-text-center am-margin-top-lg">操作失败！</h2>
            <p class="am-text-center">错误详情：${msg!}</p>
            <div class="am-btn-group am-btn-group-justify" style="width: 50%;text-align: center;margin:0 auto;">
                <a class="am-btn am-btn-warning" role="button" href="javascript:;" onclick="returnBack();">返回上级</a>
                <a class="am-btn am-btn-danger" role="button" href="/user/main/home">回到首页</a>
            </div>
        </div>
    </div>
</#if>
</div>
<!-- content end -->
<!--[if (gte IE 9)|!(IE)]><!-->
<script src="http://cdn.bootcss.com/jquery/2.1.3/jquery.min.js"></script>
<!--<![endif]-->
<!--[if lte IE 8 ]>
<script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
<![endif]-->
<script>
    var returnBack = function () {
        window.location.href = document.referrer;
    }
    <#if success>
    $(function () {
        var $time = $("#time"), time = 3;
        $time.text(time);
        setInterval(function () {
            if (time == 0) {
                window.location.href = "${call_url!'/user/main/home'}";
                return;
            }
            $time.text(--time);
        }, 1000);
    })
    </#if>
</script>
</body>
</html>