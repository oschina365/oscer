package net.oscer.controller;

import net.oscer.api.vo.UploadResultVO;
import net.oscer.beans.User;
import net.oscer.common.ApiResult;
import net.oscer.framework.LinkTool;
import net.oscer.framework.UploadUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * api
 *
 * @author kz
 * @create 2019-06-28 17:11
 **/
@RequestMapping("/api/")
@Controller
public class ApiController extends BaseController {

    /**
     * 发送邮件
     *
     * @param email
     */
    @PostMapping("email")
    @ResponseBody
    public void email(@RequestParam(value = "email", required = true) String email) {

    }

    /**
     * 发送邮件
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
