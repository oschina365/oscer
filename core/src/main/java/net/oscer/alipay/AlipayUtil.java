package net.oscer.alipay;

import com.alipay.api.*;
import org.springframework.stereotype.Service;

/**
 * @author kz
 * @create 2021-09-07 10:35
 **/
@Service
public class AlipayUtil {

    /**
     * 是否返回空
     */
    public static final Boolean returnNull = false;

    /**
     * 获取支付客户端
     *
     * @return
     */
    public AlipayClient getAlipayClient(AlipayBaseConfig config) {
        return new DefaultAlipayClient(AlipayBase.ALIPAY_GATEWAY, config.getAppId(), config.getPrivateKey(),
                AlipayBase.FORMAT_TYPE, AlipayBase.CHARSET, config.getAlipayPublicKey(), AlipayBase.SIGN_TYPE_RSA2);
    }

    /**
     * 执行
     *
     * @param request
     * @param config
     * @return
     */
    public AlipayResponse execute(AlipayRequest request, AlipayBaseConfig config) {
        try {
            AlipayResponse response = getAlipayClient(config).execute(request);
            if (response.isSuccess()) {
                return response;
            }
        } catch (AlipayApiException e) {
        }
        return null;
    }

    /**
     * 执行
     *
     * @param request
     * @param config
     * @return
     */
    public AlipayResponse execute(AlipayRequest request, AlipayBaseConfig config, Boolean returnNull) {
        if (returnNull) {
            return execute(request, config);
        }
        AlipayResponse response = null;
        try {
            response = getAlipayClient(config).execute(request);
        } catch (AlipayApiException e) {
        }
        return response;
    }
}
