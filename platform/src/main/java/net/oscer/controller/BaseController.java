package net.oscer.controller;

import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.framework.CryptUtils;
import net.oscer.framework.FormatTool;
import net.oscer.framework.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static com.alibaba.druid.util.DruidWebUtils.getRemoteAddr;
import static net.oscer.db.Entity.OFFLINE;
import static net.oscer.db.Entity.STATUS_NORMAL;

/**
 * @author kz
 * @date 2017年12月1日14:32:07
 */
public class BaseController {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required = false)
    protected HttpServletRequest request;

    @Autowired(required = false)
    protected HttpServletResponse response;

    public static final long TIME_OUT = 1000L * 60 * 60 * 24 * 365;

    public Integer pageSize = 10;

    public Integer pageNumber = 1;

    public Map<String, Cookie> cookies;

    public final static String UTF_8 = "UTF-8";

    public final static String COOKIE_LOGIN = "uid";

    public final static int MAX_AGE = 86400 * 365;

    public final static byte[] E_KEY = new byte[]{'.', 'O', 'S', 'C', 'E', 'R', 'S', 'S'};

    public final static int COOKIE_LENGTH_START = 5;//一开始cookie中存的字段信息长度
    public final static int COOKIE_LENGTH_END = 8;  //现在扩展的cookie中存的字段信息长度

    /**
     * 换算单位为毫秒的除数
     */
    public final static BigDecimal divisor = new BigDecimal(1000000);

    /**
     * 前端页面路径1
     */
    public final static String FRONT_VIEW_PAGE_PATH_LIFE = "page/life/";

    /**
     * 前端页面路径2
     */
    public final static String FRONT_VIEW_PAGE_PATH_LIFES = "page/lifes/";

    /**
     * 用户页面路径
     */
    public final static String USER_VIEW_PAGE_PATH = "page/user/";

    public void setErrorMsg(String msg) {
        request.setAttribute("error_msg", msg);
    }


    @ModelAttribute
    protected void setRequest(HttpServletRequest request) {
        //第几页
        String number = request.getParameter("number");
        String size = request.getParameter("size");
        this.pageNumber = StringUtils.isEmpty(number) ? 1 : Integer.parseInt(number);
        this.pageSize = StringUtils.isEmpty(size) ? 10 : Integer.parseInt(size);
        this.request = request;
        User login_user = getLoginUser();
        if (null != login_user) {
            request.setAttribute("login_user", getLoginUser());
            request.setAttribute("vip_text", login_user.vip());
        }
        if (request.getCookies() != null) {
            cookies = new HashMap<>();
            for (Cookie cookie : request.getCookies()) {
                cookies.put(cookie.getName(), cookie);
            }
        }
        request.setAttribute("currentUrl", request.getRequestURI());
        if (StringUtils.isNotEmpty(request.getParameter("show"))) {
            request.setAttribute("show", "show");
        }

    }


    /**
     * 获取当前登录用户
     *
     * @return
     */
    public User getLoginUser() {
        User login_user = null;
        try {
            login_user = (User) SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            return null;
        }
        if(true){
            return login_user;
        }
        return login_user == null ? (getUserFromCookie() == null ? null : getUserFromCookie()) : (login_user.getId() <= 0 ? null : login_user);
    }

    /**
     * 从 cookie 中获取用户信息（用于判断用户是否登录、权限验证）
     *
     * @return
     */
    private User getUserFromCookie() {
        User user = null;
        try {
            Cookie cookie = cookie(COOKIE_LOGIN);
            if (cookie != null && StringUtils.isNotBlank(cookie.getValue())) {
                String uuid = cookie.getValue();
                String ck = decrypt(uuid, E_KEY);
                final String[] items = StringUtils.split(ck, '|');
                if (items != null && items.length >= COOKIE_LENGTH_START && items.length <= COOKIE_LENGTH_END) {
                    Long id = NumberUtils.toLong(items[0], -1L);
                    String pwd = items[1];
                    User foundUser = User.ME.get(id);
                    if (foundUser.getOnline() == OFFLINE) {
                        return null;
                    }
                    if (foundUser != null && StringUtils.equalsIgnoreCase(foundUser.getSalt(), pwd) && foundUser.status_is_normal()) {
                        //TODO: post login
                        return foundUser;
                    }
                }
            }
        } catch (Exception e) {
        }
        return user;
    }

    public long getUserId() {
        return getLoginUser().getId();
    }

    public String getUserName() {
        return getLoginUser().getUsername();
    }

    public String getNickName() {
        return getLoginUser().getNickname();
    }

    public String getHeadimg() {
        return getLoginUser().getHeadimg();
    }

    /**
     * 上传为空文件
     *
     * @param multipartFile
     * @return
     */
    protected boolean isEmptyFile(MultipartFile multipartFile) {
        return multipartFile == null || multipartFile.getSize() == 0L;
    }

    /**
     * 文件大小效验
     *
     * @param //multipartFile
     * @return
     */
   /* protected boolean fileSizeValidator(MultipartFile multipartFile) {
        if (null != multipartFile) {
            try {
                return (multipartFile.getBytes().length <= CoreConstants.SIGN_FILE_MAX_SIZE);
            } catch (IOException e) {
                logger.error("文件上传异常", e);
            }
        }
        return true;
    }*/
    protected OutputStream getExcelOutputStream(String title, HttpServletResponse response) {
        try {
            OutputStream os = response.getOutputStream();
            response.reset();
            // 设定输出文件头
            response.setHeader("Content-disposition", "attachment; filename=" + new String(title.getBytes("UTF-8"), "ISO8859-1") + ".xls");
            response.setContentType("application/msexcel");
            return os;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void writeImageOutputStream(String title, byte[] bytes, HttpServletResponse response) {
        try {
            OutputStream os = response.getOutputStream();
            response.reset();
            // 设定输出文件头
            response.setHeader("Content-disposition", "attachment; filename=" + new String(title.getBytes("UTF-8"), "ISO8859-1") + ".png");
            response.setContentType("application/echartsPng");
            os.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private <T> T getFromSession(HttpServletRequest request, String key) {
        Object object = request.getSession().getAttribute(key);
        if (object == null) {
            return null;
        }
        return (T) object;
    }

    protected void removeSession(HttpServletRequest request, String key) {
        request.getSession().removeAttribute(key);
    }


    /**
     * 检查账号是否登录，且正常
     *
     * @return
     */
    @PostMapping("/user/has_login")
    @ResponseBody
    public ApiResult has_login() {
        User login_user = getLoginUser();
        if (login_user == null) {
            return ApiResult.failWithMessage("请登录后重试");
        }
        if (login_user.getStatus() != User.STATUS_NORMAL) {
            return ApiResult.failWithMessage("账号被屏蔽或不存在");
        }
        return ApiResult.successWithObject(login_user);
    }

    public String param(String name) {
        return request.getParameter(name);
    }

    public String param(String name, String def_value) {
        String v = request.getParameter(name);
        if (v == null) {
            v = def_value;
        }
        return v;
    }

    public int param(String name, int def_value) {
        return (int) param(name, (long) def_value);
    }

    public long param(String name, long def_value) {
        try {
            return Long.parseLong(param(name));
        } catch (Exception e) {
        }
        return def_value;
    }

    public String[] params(String name) {
        return request.getParameterValues(name);
    }

    public Object session(String name) {
        return request.getSession().getAttribute(name);
    }

    public void session(String name, Object obj) {
        if (obj == null) {
            request.getSession().removeAttribute(name);
        } else {
            request.getSession().setAttribute(name, obj);
        }
    }

    public String sessionId() {
        return request.getSession().getId();
    }

    public Cookie cookie(String name) {
        if(cookies==null || StringUtils.isEmpty(name)){
            return null;
        }
        return cookies.get(name);
    }

    public void cookie(String name, String value, int max_age, boolean all_sub_domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(max_age);
        if (all_sub_domain) {
            String serverName = request.getServerName();
            String domain = RequestUtils.getDomainOfServerName(serverName);
            if (domain != null && domain.indexOf('.') != -1) {
                cookie.setDomain(domain);
            }
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public Cookie deleteCookie(String name, boolean all_sub_domain) {
        Cookie cookie = cookie(name);
        if(cookie==null){
            return null;
        }
        cookie(name, "", 0, all_sub_domain);
        return cookie;
    }


    public void redirect(String uri) throws IOException {
        response.sendRedirect(uri);
    }

    /**
     * 输出信息到浏览器
     *
     * @param msg
     * @throws IOException
     */
    public void print(Object msg) throws IOException {
        if (!UTF_8.equalsIgnoreCase(response.getCharacterEncoding())) {
            response.setCharacterEncoding(UTF_8);
        }
        response.getWriter().print(msg);
    }

    public String ip() {
        String ip = getRemoteAddr(request);
        if (ip == null) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 在 Cookie 中保存登录用户信息
     *
     * @param user
     */
    public void saveUserInCookie(User user) {
        String new_value = genLoginKey(user, ip(), request.getHeader("user-agent"));
        cookie(COOKIE_LOGIN, new_value, MAX_AGE, true);
    }

    public static void loginUser(String name, String pwd) {
        UsernamePasswordToken token = new UsernamePasswordToken(name, pwd, false);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        subject.getSession().setTimeout(TIME_OUT);
    }

    /**
     * 生成用户登录标识字符串
     * 修改这里的拼接规则时，请注意同步修改 userFromUUID 这个方法中的解密规则
     *
     * @param user
     * @param ip
     * @param user_agent
     * @return
     */
    private static String genLoginKey(User user, String ip, String user_agent) {
        user_agent = clearUserAgent(user_agent);
        StringBuilder sb = new StringBuilder();
        sb.append(user.getId());
        sb.append('|');
        sb.append(user.getSalt());
        sb.append('|');
        sb.append(ip);
        sb.append('|');
        sb.append(user_agent.hashCode());
        sb.append('|');
        sb.append(System.currentTimeMillis());
        sb.append('|');
        if (StringUtils.isNotBlank(user.getEmail()) && FormatTool.is_email(user.getEmail())) {
            sb.append(user.getEmail());
        }
        sb.append('|');
        sb.append(user.getName());
        sb.append('|');
        sb.append(user.getIdent());
        return encrypt(sb.toString(), E_KEY);
    }

    private static String clearUserAgent(String ua) {
        if (StringUtils.isBlank(ua)) {
            return "";
        }
        int idx = StringUtils.indexOf(ua, "staticlogin");
        return (idx > 0) ? StringUtils.substring(ua, 0, idx) : ua;
    }

    /**
     * 加密
     *
     * @param value
     * @return
     * @throws Exception
     */
    private static String encrypt(String value, byte[] key) {
        byte[] data = CryptUtils.encrypt(value.getBytes(), key);
        try {
            return URLEncoder.encode(new String(Base64.getEncoder().encode(data)), UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     *
     * @param value
     * @return
     * @throws Exception
     */
    public static String decrypt(String value, byte[] key) {
        try {
            value = URLDecoder.decode(value, UTF_8);
            if (StringUtils.isBlank(value)) {
                return null;
            }
            byte[] data = Base64.getDecoder().decode(value.getBytes());
            return new String(CryptUtils.decrypt(data, key));
        } catch (Exception e) {
            return null;
        }
    }

    public void error(int errorCode, String... msgs) throws IOException {
        String msg = (msgs != null && msgs.length > 0) ? msgs[0] : null;
        response.sendError(errorCode, msg);
    }

    /**
     * 删除 Cookie 中的登录信息
     */
    public void deleteUserInCookie() {
        request.removeAttribute("login_user");
        request.removeAttribute("vip_text");
        deleteCookie(COOKIE_LOGIN, true);
    }

    /**
     * 获取当前登录用户
     *
     * @param id
     * @return
     */
    public User current_user(Long id) {
        User loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser;
        }
        if (id == null || id <= 0L) {
            return null;
        }
        return User.ME.get(id);
    }

}
