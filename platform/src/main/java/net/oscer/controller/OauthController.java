package net.oscer.controller;


import com.alibaba.fastjson.JSONObject;
import net.oscer.beans.User;
import net.oscer.beans.UserBind;
import net.oscer.common.ApiResult;
import net.oscer.config.provider.*;
import net.oscer.dao.MsgDAO;
import net.oscer.dao.UserBindDAO;
import net.oscer.dao.UserDAO;
import net.oscer.db.CacheMgr;
import net.oscer.framework.FormatTool;
import net.oscer.framework.LinkTool;
import net.oscer.framework.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.exception.SocialAuthException;
import org.brickred.socialauth.util.OAuthConfig;
import org.brickred.socialauth.util.SocialAuthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 第三方登录
 *
 * @author kz
 * @create 2019-04-10 18:23
 **/
@Controller
@RequestMapping("/oauth")
public class OauthController extends BaseController {

    private final static String AFTER_BIND_URL = "http://www.oscer.net/oauth/after_bind";
    private final static String AFTER_BIND_GITEE = "http://www.oscer.net/oauth/after_bind_gitee";
    private final static String AFTER_BIND_OSC = "http://www.oscer.net/oauth/after_bind_osc";

    public final static String SOCIAL_AUTH_CACHE = "1h";
    private final static String SOCIAL_AUTH_KEY = "socialauth_id";

    private final static SocialAuthConfig config;
    private final static AuthManager m = new AuthManager();

    static {
        config = SocialAuthConfig.getDefault();

        try {
            config.load();
            // 加载扩展的登录provider
            for (String provider_name : Arrays.asList("github", "weibo", "qq")) {
                String className = config.getApplicationProperties().getProperty(provider_name);
                if (StringUtils.isNotBlank(className)) {
                    String consumer_key = config.getApplicationProperties().getProperty(provider_name + ".consumer_key");
                    String consumer_secret = config.getApplicationProperties().getProperty(provider_name + ".consumer_secret");
                    String custom_permissions = config.getApplicationProperties().getProperty(provider_name + ".custom_permissions");
                    OAuthConfig c = new OAuthConfig(consumer_key, consumer_secret);
                    if (custom_permissions != null) {
                        c.setCustomPermissions(custom_permissions);
                    }
                    c.setProviderImplClass(Class.forName(className));
                    config.addProviderConfig(provider_name, c);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SocialAuthManager _getSocialAuthManager(String socialauth_id) {
        try {
            return (socialauth_id != null) ? m.getManager() : null;
        } finally {
            deleteCookie(SOCIAL_AUTH_KEY, false);
        }
    }

    /**
     * 禁用第三方登录
     *
     * @return
     */
    @PostMapping
    public ApiResult option() {
        User loginUser = getLoginUser();
        if (null == loginUser || loginUser.getId() <= 0L) {
            return ApiResult.failWithMessage("请登录");
        }
        long bind = param("id", 0L);
        int type = param("type", 0);

        if (bind <= 0L) {
            return ApiResult.failWithMessage("请选择禁用的第三方");
        }
        if (type == 0 || type > 3) {
            return ApiResult.failWithMessage("操作不对");
        }
        List<UserBind> binds = UserBindDAO.ME.listByUser(loginUser.getId());

        if (CollectionUtils.isEmpty(binds)) {
            return ApiResult.failWithMessage("尚未绑定任何第三方");
        }
        int bindSize = binds.size();
        binds = binds.stream().filter(b -> (null != b && (b.getId() == bind))).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(binds)) {
            return ApiResult.failWithMessage("该第三方不存在");
        }
        UserBind b = binds.get(0);
        String msg = "";
        if (type == 1) {
            msg = "禁用成功";
            b.setStatus(2);
            b.setLast_date(new Date());
            b.doUpdate();
        } else if (type == 2) {
            msg = "启用成功";
            b.setStatus(1);
            b.setLast_date(new Date());
            b.doUpdate();
        } else {
            msg = "删除成功";
            if (bindSize <= 1 && !FormatTool.is_email(loginUser.getEmail())) {
                return ApiResult.failWithMessage("操作失败！删除后，您的账号就无法登录");
            }
            b.delete();
            CacheMgr.evict(UserBind.ME.CacheRegion(), "user#" + loginUser.getId());
        }
        return ApiResult.success(msg);
    }


    @GetMapping("/before_bind")
    @ResponseBody
    public void before_bind() throws Exception {
        String rp = param("rp", "");
        if (OauthEnum.fromList.contains(rp)) {
            if (StringUtils.equalsIgnoreCase(rp, OauthEnum.FROM.GITEE.getForm())) {
                doBindGitee();
                return;
            }
            if (StringUtils.equalsIgnoreCase(rp, OauthEnum.FROM.OSC.getForm())) {
                doBindOsc();
                return;
            }
            try {
                String after_bind_url = AFTER_BIND_URL;
                _saveManagerAndGo(rp, after_bind_url);
            } catch (SocialAuthException e) {
                if (e.getCause() instanceof IOException) {
                    //throw ctx.error("openid_connect_timeout");
                } else {
                    throw e;
                }
            }
        } else {
            redirect(LinkTool.root());
        }
    }

    /**
     * 绑定帐号认证返回处理
     *
     * @throws ServletException
     * @throws Exception
     */
    @GetMapping("/after_bind")
    public void after_bind() throws Exception {
        Cookie ck = cookie(SOCIAL_AUTH_KEY);
        String socialauth_id = (ck != null) ? ck.getValue() : null;
        String state = param("state", "");
        // get the auth provider manager from session
        SocialAuthManager manager = _getSocialAuthManager(socialauth_id);
        if (manager == null) {

            redirect(LinkTool.root());
            return;
        }
        Profile p = null;
        try {
            // call connect method of manager which returns the provider object.
            // Pass request parameter map while calling connect method.
            Map<String, String> map = SocialAuthUtil.getRequestParametersMap(request);
            AuthProvider provider = manager.connect(map);
            // check OAuth2 CSRF token
            if (Arrays.asList("weibo", "github", "hotmail").contains(provider.getProviderId()) && !StringUtils.equals(state, socialauth_id)) {
                redirect(LinkTool.root());
                return;
            }
            // get profile
            p = provider.getUserProfile();
        } catch (Exception e) {
            e.printStackTrace();
            redirect(LinkTool.root());
            return;
        }
        doBind(new AuthProfile(p));
    }

    private void doBindOsc() throws Exception {
        String state = RandomStringUtils.randomAlphanumeric(10);
        deleteCookie(SOCIAL_AUTH_KEY, false);
        cookie(SOCIAL_AUTH_KEY, state, -1, false);
        redirect(OscOpenAuth.getAuthorizeUrl(AFTER_BIND_OSC));
        CacheMgr.set(SOCIAL_AUTH_CACHE, "OSC#" + state, state);
    }

    private void doBindGitee() throws Exception {
        String state = RandomStringUtils.randomAlphanumeric(20);
        try {
            deleteCookie(SOCIAL_AUTH_KEY, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cookie(SOCIAL_AUTH_KEY, state, -1, false);
        redirect(GiteeOpenAuth.getAuthorizeUrl(AFTER_BIND_GITEE));
        CacheMgr.set(SOCIAL_AUTH_CACHE, "GITEE#" + state, state);
    }

    @GetMapping("/after_bind_osc")
    @ResponseBody
    public void after_bind_osc() throws Exception {
        String code = param("code");
        System.out.println(code);
        Cookie ck = cookie(SOCIAL_AUTH_KEY);
        String state = (ck != null) ? ck.getValue() : null;
        String redirect_url = param("goto", "");
        String state_secret = (String) CacheMgr.get(SOCIAL_AUTH_CACHE, "OSC#" + state);
        Profile p = OscOpenAuth.getProfile(code, state, state_secret, redirect_url);
        if (p != null) {
            p.setProviderId(OauthEnum.FROM.OSC.getForm());
        }
        doBind(new AuthProfile(p));
    }

    @GetMapping("/after_bind_gitee")
    @ResponseBody
    public void after_bind_gitee() throws Exception {
        String code = param("code");
        Cookie ck = cookie(SOCIAL_AUTH_KEY);
        String state = (ck != null) ? ck.getValue() : null;
        String redirect_url = param("goto", "");
        String state_secret = (String) CacheMgr.get(SOCIAL_AUTH_CACHE, "GITEE#" + state);
        Profile p = GiteeOpenAuth.getProfile(code, state, state_secret, redirect_url);
        if (p != null) {
            p.setProviderId(OauthEnum.FROM.GITEE.getForm());
        }
        doBind(new AuthProfile(p));
    }

    private void doBind(AuthProfile authProfile) throws Exception {
        User loginUser = getLoginUser();
        UserBind bind = null;
        if (loginUser != null) {
            if (null != authProfile && StringUtils.isNotBlank(authProfile.getFullName())) {
                if (StringUtils.isNotBlank(authProfile.getValidatedId())) {
                    bind = UserBindDAO.ME.bindByUnion_id(authProfile.getProviderId(), authProfile.getValidatedId(), authProfile.getFullName());
                } else {
                    bind = UserBindDAO.ME.bindByName(authProfile.getProviderId(), authProfile.getFullName());
                }
                if (bind == null) {
                    bindReg(loginUser.getId(), authProfile);
                }
            }
            redirect(LinkTool.root());
            return;
        }

        if (null == authProfile && null == loginUser) {
            redirect(LinkTool.root() + "?error=profile");
            return;
        }
        System.out.println("authProfile:" + JSONObject.toJSONString(authProfile));
        if (StringUtils.isNotBlank(authProfile.getValidatedId())) {
            bind = UserBindDAO.ME.bindByUnion_id(authProfile.getProviderId(), authProfile.getValidatedId(), authProfile.getFullName());
        } else {
            bind = UserBindDAO.ME.bindByName(authProfile.getProviderId(), authProfile.getFullName());
        }
        System.out.println("bind:" + bind);
        if (bind != null && bind.getId() > 0L) {
            loginUser = User.ME.get(bind.getUser());
        } else {
            loginUser = openidReg(authProfile);
        }
        if (null == loginUser) {
            redirect(LinkTool.root());
            return;
        }
        System.out.println("exist:" + loginUser);
        ApiResult result = UserDAO.ME.login(authProfile.getFullName(), authProfile.getValidatedId(), ip(), true, authProfile.getProviderId());
        if (result.getCode() == ApiResult.fail) {
            String html = "<p>Redirecting...</p><script type='text/javascript'>   location.href='" + LinkTool.root() + "';</script>";
            print(html);
        } else {
            UserBindDAO.ME.evict(loginUser.getId());
            loginUser(loginUser.getUsername(), loginUser.getSalt());
            saveUserInCookie((User) result.getResult());
            redirect("http://www.oscer.net");
            return;
        }

    }

    private User openidReg(AuthProfile authProfile) throws Exception {
        if (authProfile == null || StringUtils.isBlank(authProfile.getFullName())) {
            return null;
        }
        User reg = new User();
        UserBind bind = new UserBind();
        reg.setUsername(authProfile.getFullName());
        reg.setEmail(authProfile.getFullName());
        String pwd = RandomStringUtils.randomAlphanumeric(8);
        reg.setPassword(pwd);

        if (StringUtils.isNotBlank(authProfile.getEmail())) {
            reg.setEmail(authProfile.getEmail());
            bind.setEmail(authProfile.getEmail());
        }
        if (StringUtils.isNotBlank(authProfile.getProfileImageURL())) {
            reg.setHeadimg(authProfile.getProfileImageURL());
            bind.setHeadimg(authProfile.getProfileImageURL());
        } else {
            reg.setHeadimg("http://img.oscer.net/avatar/" + (int) (Math.random() * 12) + ".jpg");
        }

        reg.setSalt(User.ME._GeneratePwdHash(pwd, reg.getEmail()));
        reg.setFrom(OauthEnum.getFrom(authProfile.getProviderId()));
        if (StringUtils.isNotEmpty(authProfile.getGender())) {
            int sex = 0;
            if (StringUtils.equalsIgnoreCase(authProfile.getGender(), "female")) {
                sex = 2;
            }
            if (StringUtils.equalsIgnoreCase(authProfile.getGender(), "man")) {
                sex = 1;
            }
            reg.setSex(sex);
        }
        reg.save();

        bind.setUser(reg.getId());
        bind.setName(authProfile.getFullName());
        bind.setProvider(authProfile.getProviderId());
        bind.setFrom("pc");
        if (StringUtils.isNotBlank(authProfile.getValidatedId())) {
            bind.setUnion_id(authProfile.getValidatedId());

        }
        bind.save();
        CacheMgr.evict(UserBind.ME.CacheRegion(), "user#" + reg.getId());
        MsgDAO.ME.send(2L, reg.getId(), "欢迎加入OSCER社区！", 0, "网页端");
        return reg;
    }

    private void bindReg(long user, AuthProfile authProfile) {
        if (authProfile == null || StringUtils.isBlank(authProfile.getFullName())) {

        } else {
            UserBind bind = new UserBind();


            if (StringUtils.isNotBlank(authProfile.getEmail())) {
                bind.setEmail(authProfile.getEmail());
            }
            if (StringUtils.isNotBlank(authProfile.getProfileImageURL())) {
                bind.setHeadimg(authProfile.getProfileImageURL());
            }

            bind.setUser(user);
            bind.setName(authProfile.getFullName());
            bind.setProvider(authProfile.getProviderId());
            bind.setFrom("pc");
            if (StringUtils.isNotBlank(authProfile.getValidatedId())) {
                bind.setUnion_id(authProfile.getValidatedId());

            }
            bind.save();
            CacheMgr.evict(UserBind.ME.CacheRegion(), "user#" + user);
        }
    }


    public static void main(String[] args) {
        /*CacheMgr.set("1h","127.0.0.1",1);
        System.out.println(RandomStringUtils.randomAlphanumeric(8));
        System.out.println(CacheMgr.get("1h","127.0.0.1"));*/
        List<Long> list = new ArrayList<>();
        Map<String, Long> a = new HashMap<>();
        list.add(1L);
        list.add(2L);
    }


    /**
     * 准备跳转到认证网站
     *
     * @param op
     * @param url
     * @throws Exception
     */
    private void _saveManagerAndGo(String op, String url) throws Exception {
        SocialAuthManager manager = new SocialAuthManager();
        manager.setSocialAuthConfig(config);
        // get Provider URL to which you should redirect for authentication.
        String auth_url = manager.getAuthenticationUrl(op, url);
        // Store in session
        String socialauth_id = RandomStringUtils.randomAlphanumeric(20);
        deleteCookie(SOCIAL_AUTH_KEY, false);
        cookie(SOCIAL_AUTH_KEY, socialauth_id, -1, false);
        m.setManager(manager);
        // OAuth2 CSRF protection
        String csrf_token = "&state=" + socialauth_id;
        // Redirect to the URL
        response.setContentType("text/html");
        String html = "<p>Redirecting...</p><script type='text/javascript'>location.href='" + auth_url + csrf_token + "';</script>";
        print(html);
    }
}
