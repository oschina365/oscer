package net.oscer.config.provider;

import java.net.URLEncoder;

/**
 * 微信
 *
 * @author MRCHENIKE
 * @create 2020-09-25 18:12
 **/
public class WeChatAuth {

    private static final String APPID = "wx00dc77dadaf54f3b";
    private static final String APPSECRET = "b0d6e6264c2a30340b85943743a88012";
    private static final String GET_CODE_URL = "https://open.weixin.qq.com/connect/qrconnect";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    private static final String USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";
    //https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx00dc77dadaf54f3b&redirect_uri=
    // https%3A%2F%2Fwww.oschina.net%2Faction%2Fopenid%2Fafter_bind_wechat%3Fgoto%3D&response_type=code&
    // scope=snsapi_base&state=CRfUby9T93gUHGBpzQpy#wechat_redirect

    private static final String AUTHORIZ_SCOPE = "snsapi_userinfo";
    private static final String AUTHORIZ_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    //https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
    private static final String GET_UNONNID = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 微信登录
     *
     * @param redirect_uri 回调地址
     * @param state        用于防止csrf攻击的随机字符串
     * @return
     * @throws Exception
     */
    public static String getAuthorize2Url(String redirect_uri, String state) throws Exception {
        return AUTHORIZ_URL + "?appid=" + APPID + "&redirect_uri=" + URLEncoder.encode(redirect_uri, "UTF-8")
                + "&response_type=code&scope=" + AUTHORIZ_SCOPE + "&state=" + state + "#wechat_redirect";
    }

    public static String getUnonnidUrl(String appid, String secret, String code) {
        return GET_UNONNID + "?appid=" + APPID + "&secret=" + APPSECRET + "&js_code=" + code + "&grant_type=authorization_code";
    }
}
