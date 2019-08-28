package net.oscer.service;


import net.oscer.common.ApiResult;
import net.oscer.enums.TextCheckEnum;
import net.oscer.framework.FormatTool;
import net.oscer.framework.StringUtils;

import java.util.Map;

/**
 * 文本检测服务类
 **/
public class TextCheckService {

    /**
     * 自动选择一种方式检测
     *
     * @return
     */
    public static ApiResult auto(long userId, String content, String type) {
        if (!TextCheckEnum.TYPE_LIST.contains(type)) {
            type = TextCheckEnum.TYPE.ALIYUN.getKey();
        }
        if (StringUtils.equalsIgnoreCase(type, TextCheckEnum.TYPE.BAIDU.getKey())) {
            return baidu(userId, content);
        }
        return aliyun(content);
    }

    /**
     * 百度文本审核
     *
     * @param userId
     * @param content
     * @return
     */
    public static ApiResult baidu(long userId, String content) {
        content = FormatTool.text(content);
        if (StringUtils.isBlank(content)) {
            return ApiResult.failWithMessage("内容为空，请填写内容");
        }
        Map<Integer, String> result = null;
        try {
            result = TextCheckBaiduService.baidu().checkText(userId, content);
        } catch (Exception e) {
            return ApiResult.success();
        }
        if (result == null) {
            return ApiResult.success();
        }
        return ApiResult.failWithMessageAndObject("", result);
    }

    /**
     * 阿里绿网文本审核
     *
     * @param content
     * @return
     */
    public static ApiResult aliyun(String content) {
        content = FormatTool.text(content);
        if (StringUtils.isBlank(FormatTool.text(content))) {
            return ApiResult.failWithMessage("内容为空，请填写内容");
        }
        TetxAntispamScanConfig.Result result = null;
        try {
            result = TextCheckAliyunService.check(content);
        } catch (Exception e) {
            return ApiResult.success();
        }
        if (!result.isSuccess()) {
            return ApiResult.failWithMessage("内容存在违规");
        }
        return ApiResult.success();
    }
}
