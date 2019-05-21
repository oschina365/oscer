package net.oscer.controller;

import net.oscer.beans.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

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

    public Integer pageSize = 10;

    public Integer pageNumber = 1;

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


    @ModelAttribute
    protected void setRequest(HttpServletRequest request, ModelMap map) {
        //第几页
        String number = request.getParameter("number");
        String size = request.getParameter("size");
        this.pageNumber = StringUtils.isEmpty(number) ? 1 : Integer.parseInt(number);
        this.pageSize = StringUtils.isEmpty(size) ? pageSize : Integer.parseInt(size);
        this.request = request;
        map.put("size", pageSize);
        map.put("login_user", getLoginUser());
    }


    /**
     * 获取当前登录用户
     *
     * @return
     */
    public User getLoginUser() {
        User login_user = (User) SecurityUtils.getSubject().getPrincipal();
        return login_user == null ? null : login_user;
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

}
