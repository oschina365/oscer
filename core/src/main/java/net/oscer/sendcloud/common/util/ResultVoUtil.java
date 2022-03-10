package net.oscer.sendcloud.common.util;


import net.oscer.sendcloud.common.enums.ResultEnum;
import net.oscer.sendcloud.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author kz
 * @date 2018-01-18
 */
public class ResultVoUtil {

    public static ResultVO getSuccessResult(String msg) {
        ResultVO result = new ResultVO();
        result.setCode(ResultEnum.STATUS.SUCCESS.getCode());
        result.setMsg(ResultEnum.STATUS.SUCCESS.getDesc());
        if (StringUtils.isNotEmpty(msg)) {
            result.setMsg(msg);
        }

        return result;
    }

    public static ResultVO getFailResult(String msg) {
        ResultVO result = new ResultVO();
        result.setCode(ResultEnum.STATUS.FAIL.getCode());
        result.setMsg(ResultEnum.STATUS.FAIL.getDesc());
        if (StringUtils.isNotEmpty(msg)) {
            result.setMsg(msg);
        }

        return result;
    }
}
