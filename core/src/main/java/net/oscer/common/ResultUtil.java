package net.oscer.common;

import com.alibaba.fastjson.JSONObject;
import net.oscer.enums.ResultEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * 返回工具类
 *
 * @author kz
 * @date 2018-01-16
 */
public class ResultUtil {

    public static final String CODE = "code";
    public static final String MSG = "msg";

    /**
     * 返回成功
     *
     * @param msg
     * @return
     */
    public static JSONObject getSuccessResult(String msg) {
        JSONObject result = new JSONObject();
        result.put(CODE, ResultEnum.STATUS.SUCCESS.getCode());
        result.put(MSG, ResultEnum.STATUS.SUCCESS.getDesc());
        if (StringUtils.isNotEmpty(msg)) {
            result.put(MSG, msg);
        }
        return result;
    }


    /**
     * 返回失败
     *
     * @param msg
     * @return
     */
    public static JSONObject getFailResult(String msg) {
        JSONObject result = new JSONObject();
        result.put(CODE, ResultEnum.STATUS.FAIL.getCode());
        result.put(MSG, ResultEnum.STATUS.FAIL.getDesc());
        if (StringUtils.isNotEmpty(msg)) {
            result.put(MSG, msg);
        }
        return result;
    }
}
