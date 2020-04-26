<#include "../public.ftl"/>
<#macro html title_="oscer" js_=[]>
<!DOCTYPE html>
<html lang="zh" class="app">
<head>
    <title>${title_!''}</title>
    <#include "head.ftl"/>
<script src="/res/resume/js/index.js"></script>
<link rel="stylesheet" href="/res/resume/css/typo.css">
<link rel="stylesheet" href="/res/resume/css/font-awesome.min.css">
<link rel="stylesheet" href="/res/resume/css/index.css">
</head>
<body>
    <#nested />
</body>
</html>
</#macro>

<#macro script_>
    <#nested />
</#macro>