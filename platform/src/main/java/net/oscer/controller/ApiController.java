package net.oscer.controller;

import net.oscer.api.vo.UploadResultVO;
import net.oscer.beans.SendEmailRecord;
import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.common.PlaceHoldersConstants;
import net.oscer.dao.SendEmailRecordDAO;
import net.oscer.dao.UserDAO;
import net.oscer.enums.EmailTemplateTypeEnum;
import net.oscer.framework.*;
import net.oscer.sendcloud.common.vo.ResultVO;
import net.oscer.sendcloud.common.vo.SendCloudEmailParamVO;
import net.oscer.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * api
 *
 * @author kz
 * @create 2019-06-28 17:11
 **/
@RequestMapping("/api/")
@Controller
public class ApiController extends BaseController {

    public static final int STATUS_SUCCESS_CODE = 200;

    @Autowired
    private CaptchaService captchaService;

    /**
     * 发送邮件
     *
     * @param email
     */
    @PostMapping("email")
    @ResponseBody
    public ApiResult email(@RequestParam(value = "email", required = true) String email) throws Exception {
        if (StringUtils.isBlank(email)) {
            return ApiResult.failWithMessage("请填写邮箱");
        }
        if (!FormatTool.is_email(email)) {
            return ApiResult.failWithMessage("请填写正确的邮箱");
        }

        if (UserDAO.ME.selectByEmail(email) != null) {
            return ApiResult.failWithMessage("该邮箱已被注册");
        }
        SendEmailRecord record = SendEmailRecordDAO.ME.selectByEmail(email, SendEmailRecord.TYPE_WEB, EmailTemplateTypeEnum.TYPE.REGISTER.getKey());
        if (record != null && (System.currentTimeMillis() - record.getLast_date().getTime()) < SendEmailRecord.SEND_REGISTER_INTERVAL_TIME) {
            return ApiResult.failWithMessage("验证码已经发送，有效时间为" + SendEmailRecord.SEND_REGISTER_INTERVAL_MIN + "分钟");
        }
        String code = captchaService.getCode(email, request);
        if (StringUtils.isBlank(code)) {
            return ApiResult.failWithMessage("网络错误，请重试");
        }

        Map<String, String> map = new HashMap<>(1);
        map.put(PlaceHoldersConstants.CODE, code);
        SendCloudEmailParamVO vo = Send.createSendEmailVo(null, email, EmailTemplateTypeEnum.TYPE.REGISTER.getKey());
        vo.setPlaceholders(map);
        ResultVO resultVO = Send.sendEmail(vo);
        if (resultVO == null || resultVO.getCode() != STATUS_SUCCESS_CODE) {
            return ApiResult.failWithMessage("发送邮件失败，请重试");
        }
        record = new SendEmailRecord();
        record.setSend_email(vo.getFrom());
        record.setReceive_email(email);
        record.setReceiver(0L);
        record.setType(SendEmailRecord.TYPE_WEB);
        record.setInsert_date(new Date());
        record.setLast_date(new Date());
        record.setSubject(vo.getSubject());
        record.setText(vo.getContent());
        record.setEmail_type(EmailTemplateTypeEnum.TYPE.REGISTER.getKey());
        record.save();
        return ApiResult.success();
    }

    /**
     * 校验注册验证码
     *
     * @return
     */
    @PostMapping("check_register_code")
    @ResponseBody
    public ApiResult check_register_code() throws IOException {
        String email = param("key");
        String code = param("value");
        if (StringUtils.isBlank(email) || StringUtils.isBlank(code)) {
            return ApiResult.failWithMessage("验证码不对");
        }
        return captchaService.check(email, code, request);
    }

    /**
     * 发送忘记密码邮件
     *
     * @return
     */
    @PostMapping("send_forget_email")
    public ApiResult send_forget_email() throws Exception {
        String email = param("email", "");
        String username = param("username", "");
        if (StringUtils.isBlank(username)) {
            return ApiResult.failWithMessage("请填写用户名");
        }
        if (StringUtils.isBlank(email)) {
            return ApiResult.failWithMessage("请填写邮箱");
        }
        if (!FormatTool.is_email(email)) {
            return ApiResult.failWithMessage("请填写正确的邮箱");
        }

        User user = UserDAO.ME.selectByNameAndEmail(username, email);
        if (user == null) {
            return ApiResult.failWithMessage("用户名或邮箱不存在");
        }

        SendEmailRecord record = SendEmailRecordDAO.ME.selectByEmail(email, SendEmailRecord.TYPE_WEB, EmailTemplateTypeEnum.TYPE.RETRIEVE_PASSWORD.getKey());
        if (record != null && (System.currentTimeMillis() - record.getLast_date().getTime()) < SendEmailRecord.SEND_REGISTER_INTERVAL_TIME) {
            return ApiResult.failWithMessage("验证码已经发送，有效时间为" + SendEmailRecord.SEND_REGISTER_INTERVAL_MIN + "分钟");
        }
        String code = captchaService.getCode(email, request);
        if (StringUtils.isBlank(code)) {
            return ApiResult.failWithMessage("网络错误，请重试");
        }

        Map<String, String> map = new HashMap<>(1);
        map.put(PlaceHoldersConstants.USER_NAME, user.getUsername());
        map.put(PlaceHoldersConstants.PASSWORD, user.getPassword());
        SendCloudEmailParamVO vo = Send.createSendEmailVo(null, email, EmailTemplateTypeEnum.TYPE.RETRIEVE_PASSWORD.getKey());
        vo.setPlaceholders(map);
        ResultVO resultVO = Send.sendEmail(vo);
        if (resultVO == null || resultVO.getCode() != STATUS_SUCCESS_CODE) {
            return ApiResult.failWithMessage("发送邮件失败，请重试");
        }
        record = new SendEmailRecord();
        record.setSend_email(vo.getFrom());
        record.setReceive_email(email);
        record.setReceiver(0L);
        record.setType(SendEmailRecord.TYPE_WEB);
        record.setInsert_date(new Date());
        record.setLast_date(new Date());
        record.setSubject(vo.getSubject());
        record.setText(vo.getContent());
        record.setEmail_type(EmailTemplateTypeEnum.TYPE.RETRIEVE_PASSWORD.getKey());
        record.save();
        return ApiResult.success();
    }

    /**
     * 上传图片
     *
     * @param multipartFile
     */
    @PostMapping("upload")
    @ResponseBody
    public UploadResultVO upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return UploadResultVO.failWith("请选择图片");
        }

        User loginUser = getLoginUser();
        if (loginUser == null || loginUser.getId() <= 0) {
            return UploadResultVO.failWith("请登录重试");
        }

        String fileName = multipartFile.getOriginalFilename();
        String url = request.getSession().getServletContext().getRealPath("/") + fileName;
        byte[] bytes = multipartFile.getBytes();
        Path path = Paths.get(url);
        Files.write(path, bytes);

        File copyFile = new File(url);
        String newFileName = loginUser.getId() + "_" + fileName, thumbFileName = loginUser.getId() + "_thumb_" + fileName;
        File desFile = new File(LinkTool.getHost("static") + File.separator + newFileName);
        File thumbFile = new File(LinkTool.getHost("static") + File.separator + thumbFileName);
        UploadUtils.uploadImage(UploadUtils.IMAGE_TYPE_BLOG, loginUser.getId(), copyFile, desFile, thumbFile);
        UploadResultVO vo = UploadResultVO.success("", "上传成功", desFile.getName(), desFile.length(), desFile.getName(), null, thumbFileName);
        return vo;
    }
}
