<#--img url -->
<#macro img type_=0 u_=""><#if type_=0>data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsQAAA7EAZUrDhsAAAANSURBVBhXYzh8+PB/AAffA0nNPuCLAAAAAElFTkSuQmCC<#else><#if u_?starts_with("http://")>${u_}<#else>${imgurl!'http://o7tc4e966.bkt.clouddn.com/'}${u_}<#switch type_>
    <#case 1>?imageView2/0/w/800/h/400<#break><#case 2>?imageView2/0/w/400/h/200<#break><#case 3>?imageView2/0/w/200/h/200<#break><#case 4>?imageView2/0/w/750/h/450<#break><#case 5>?imageView2/0/w/600/h/600<#break><#case 6>?/thumbnail/600x<#break><#case 7>?imageMogr2/thumbnail/!15p<#break><#case 8>?imageMogr2/thumbnail/56x42<#break>
    <#case 9>?imageMogr2/thumbnail/80x60<#break><#case 10>?imageView2/0/w/288/h/182<#break><#case 11>?imageMogr2/thumbnail/!50p<#break><#case 12>?imageMogr2/thumbnail/240x200<#break>
</#switch></#if></#if></#macro>
<#--res url -->
<#assign root = request.contextPath/>
<#--<#assign static = "http://p6cfjoz1f.bkt.clouddn.com"/>-->
<#assign static = request.contextPath/>
<#macro res u_=""><#if context??>${context.ver('/resources/static/'+u_)}<#else>${static}/js/${u_}</#if></#macro>

