package net.oscer.controller;

import com.qiniu.common.QiniuException;
import net.oscer.api.vo.UploadResultVO;
import net.oscer.common.SystemConstant;
import net.oscer.service.QiNiuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import util.ConfigTool;

import java.io.IOException;


/**
 * @author kz
 * @date 2017年12月1日14:32:07
 * @desc 七牛上传
 */
@Controller
@RequestMapping(value = "/up")
public class UploadController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping
    public String index() {
        return "login/index";
    }


    /**
     * 移动文件，要求空间在同一账号下
     *
     * @param fileName    源文件名
     * @param newBucket   目标空间名
     * @param newFileName 目标文件名
     * @throws QiniuException
     */
    public void moveQiNiuFile(String fileName, String newBucket, String newFileName) throws QiniuException {
        QiNiuService.moveFile(SystemConstant.QINIU_BUCKET, fileName, newBucket, newFileName, false);
    }

    /**
     * 根据文件名删除七牛对应的文件
     *
     * @param fileName
     */
    public void delQiNiuFile(String fileName) throws QiniuException {
        QiNiuService.delete(SystemConstant.QINIU_BUCKET, fileName);
    }




    /**
     * 图片上传，采用webUpload单次上传
     * 转化为byte数组上传
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "/pic", method = RequestMethod.POST)
    @ResponseBody
    public UploadResultVO pic(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return QiNiuService.pic(multipartFile, getUserId());
    }

    /**
     * 图片上传,不压缩图片
     * 返回格式适合于layui上传图片
     * 转化为byte数组上传
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "/lay", method = RequestMethod.POST)
    @ResponseBody
    public String lay(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        UploadResultVO vo = QiNiuService.pic(multipartFile, getUserId());
        return vo.toString();
    }

    /**
     * 图片上传，压缩图片
     * 返回格式适合于layui上传图片
     * 进行切图
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping(value = "/lay2", method = RequestMethod.POST)
    @ResponseBody
    public String lay2(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        UploadResultVO vo = QiNiuService.pic(multipartFile, getUserId());
        vo.setKey(vo.getKey()+"?imageslim&imageView2/0/w/1000/h/500");
        vo.getData().setSrc(vo.getKey());
        return vo.toString();
    }
}
