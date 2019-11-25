<#include "../public.ftl"/>
<#macro html title_="oscer" js_=[]>
<!DOCTYPE html>
<html lang="zh" class="app">
<head>
    <title>${title_!''}</title>
    <#include "head.ftl"/>
</head>
<body>
    <#nested />
</body>
</html>
</#macro>

<#macro script_>
    <#nested />
</#macro>