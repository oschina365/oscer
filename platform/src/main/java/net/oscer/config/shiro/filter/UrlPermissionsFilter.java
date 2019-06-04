package net.oscer.config.shiro.filter;


import net.oscer.beans.Node;
import net.oscer.dao.NodeDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/**
 * @author kz
 * @date 2018-01-01
 */
@Component("urlPermissionsFilter")
public class UrlPermissionsFilter extends PermissionsAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        String curUrl = getRequestUrl(request);
        Subject subject = SecurityUtils.getSubject();

        if (StringUtils.equalsIgnoreCase(curUrl, "/")) {
            return true;
        }

        if (StringUtils.startsWith(curUrl, "/user")) {
            return true;
        }

        if (StringUtils.startsWith(curUrl, "/druid")) {
            return true;
        }

        if (StringUtils.startsWith(curUrl, "/res/")) {
            return true;
        }

        if (StringUtils.startsWith(curUrl, "/q/")) {
            return true;
        }

        if (StringUtils.startsWith(curUrl, "/sign")) {
            return true;
        }

        if (StringUtils.startsWith(curUrl, "/oauth")) {
            return true;
        }

        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);

        if (CollectionUtils.isNotEmpty(nodes)) {
            if (nodes.stream().filter(n -> StringUtils.isNotBlank(n.getUrl())).anyMatch(node -> StringUtils.equalsIgnoreCase(node.getUrl(), curUrl))) {
                return true;
            }

        }

        if (
                StringUtils.endsWithAny(curUrl, ".js", ".css", ".html")
                        || StringUtils.endsWithAny(curUrl, ".jpg", ".png", ".gif", ".jpeg", ".map", ".ico")
                        || StringUtils.startsWith(curUrl, "/api/")
                        || curUrl.contains("/tools/")
                        || curUrl.contains(".js")
                        || curUrl.contains(".css")
                        || StringUtils.equals(curUrl, "/unauthor")) {
            return true;
        }
        if (subject.getPrincipal() == null) {
            return false;
        }
        /*SysUser user = ((SysUser) subject.getPrincipal());
        if (user != null && user.getStatus().equals(StatusEnum.NORMAL.getKey())) {
            return true;
        }*/
        return false;
    }

    /**
     * 获取当前URL+Parameter
     *
     * @param request 拦截请求request
     * @return 返回完整URL
     * @author lance
     * @since 2014年12月18日 下午3:09:26
     */
    private String getRequestUrl(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String queryString = req.getQueryString();

        queryString = StringUtils.isBlank(queryString) ? "" : "?" + queryString;
        return req.getRequestURI() + queryString;
    }
}
