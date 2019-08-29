package net.oscer.config.shiro.filter;


import net.oscer.beans.Node;
import net.oscer.beans.User;
import net.oscer.dao.NodeDAO;
import net.oscer.db.CacheMgr;
import net.oscer.enums.IpPassEnum;
import net.oscer.enums.UrlPassEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
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

    /**
     * 总访问数量
     */
    public static final int MAX_VIEW_COUNT = 24 * 60 * 60 * 1000 * 1000;

    /**
     * 单个访问数量
     */
    public static final int MAX_SINGLE_COUNT = 100;

    public static final String CACHE_TOTAL = "Total";

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        String curUrl = getRequestUrl(request);
        Subject subject = SecurityUtils.getSubject();

        User user = ((User) subject.getPrincipal());
        String ip = request.getRemoteAddr();

        long id = 0L;
        if (user != null && user.getId() > 0L) {
            id = user.getId();
        }

        if (StringUtils.startsWith(curUrl, "/error/")) {
            return true;
        }

        if (!canView(ip, id, curUrl)) {
            this.setUnauthorizedUrl("/error/4003");
            return false;
        }

        if (StringUtils.equalsIgnoreCase(curUrl, "/")) {
            return true;
        }

        if (StringUtils.equalsIgnoreCase(curUrl, "/?show=reply")) {
            return true;
        }

        List<String> urls = UrlPassEnum.list;
        boolean pass = false;
        for (String url : urls) {
            if (StringUtils.startsWith(curUrl, url)) {
                pass = true;
                break;
            }
        }
        if (pass) {
            return true;
        }

        List<Node> nodes = NodeDAO.ME.nodes(Node.STATUS_NORMAL, 0);

        if (CollectionUtils.isNotEmpty(nodes)) {
            if (nodes.stream().filter(n -> StringUtils.isNotBlank(n.getUrl())).anyMatch(node -> StringUtils.startsWith(curUrl, node.getUrl()))) {
                return true;
            }

        }


        /*SysUser user = ((SysUser) subject.getPrincipal());
        if (user != null && user.getStatus().equals(StatusEnum.NORMAL.getKey())) {
            return true;
        }*/
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        /*Subject subject = this.getSubject(request, response);
        if (subject.getPrincipal() == null) {
            //未登录用户处理
            this.saveRequestAndRedirectToLogin(request, response);
        }*/
        String unauthorizedUrl = this.getUnauthorizedUrl();
        if (org.apache.shiro.util.StringUtils.hasText(unauthorizedUrl)) {
            WebUtils.issueRedirect(request, response, unauthorizedUrl);
        } else {
            WebUtils.issueRedirect(request, response, "/error/404");
            //WebUtils.toHttp(response).sendError(500);
        }
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

    /**
     * 对ip或者某个用户进行访问限制
     *
     * @param ip
     * @param user
     * @return
     */
    public boolean canView(String ip, long user, String url) {
        if (ip.equalsIgnoreCase(IpPassEnum.local) || ip.equalsIgnoreCase(IpPassEnum.local) || ip.equalsIgnoreCase(IpPassEnum.remote_local)) {
            return true;
        }
        //单个ip所有访问链接次数
        Object totalView = CacheMgr.get(CACHE_TOTAL, ip);
        if (totalView != null && ((Integer) totalView) > MAX_VIEW_COUNT) {
            return false;
        }
        if (user > 0L) {
            //所有访问链接次数
            Object total = CacheMgr.get(CACHE_TOTAL, String.valueOf(user));
            if (total != null && ((Integer) total) > MAX_VIEW_COUNT) {
                return false;
            }
            //单个访问链接次数--用户
            Object urlView = CacheMgr.get(url, String.valueOf(user));
            if (urlView != null && ((Integer) urlView) > MAX_SINGLE_COUNT) {
                return false;
            }
            CacheMgr.incr(CACHE_TOTAL, String.valueOf(user));
            if (!UrlPassEnum.list.contains(url)) {
                CacheMgr.incr(url, String.valueOf(user));
            }

        } else {
            //单个ip
            Object urlView = CacheMgr.get(ip, url);
            if (urlView != null && ((Integer) urlView) > MAX_SINGLE_COUNT) {
                return false;
            }
        }
        CacheMgr.incr(CACHE_TOTAL, ip);
        if (!UrlPassEnum.list.contains(url)) {
            CacheMgr.incr(ip, url);
        }

        return true;

    }
}
