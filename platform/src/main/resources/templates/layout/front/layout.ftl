<#include "../public.ftl"/>
<#macro html title_="oscer" js_=[] css_=[]>
<!DOCTYPE html>
<html lang="zh" class="app">
<head>
    <title>${title_!''}</title>
    <#include "head.ftl"/>
    <#list css_ as css>
        ${css}
    </#list>
</head>
<body>
    <#include "header.ftl"/>
    <#nested />
    <#include "footer.ftl"/>
    <#list js_ as li>
    <script src="<@res u_=li/>"></script>
    </#list>


</body>
</html>
</#macro>

<#macro script_>
    <#nested />
</#macro>