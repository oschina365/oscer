package net.oscer.config.provider;

import net.oscer.framework.StringUtils;
import org.brickred.socialauth.Profile;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 步骤：https://www.oschina.net/openapi/docs
 */
public class OscOpenAuth {
    private static final String client_id = "aCRruX7VYHw9nAnmLwFm";
    private static final String client_secret = "NxDO4ttEUE7yuTKnmaX7A0KmOF6vUjz8";
    private static final String GET_CODE_URL = "https://www.oschina.net/action/oauth2/authorize";
    private static final String ACCESS_TOKEN_URL = "https://www.oschina.net/action/openapi/token";
    private static final String GET_USER_INFO = "https://www.oschina.net/action/openapi/user";

    /**
     * 组装请求code的URL
     *
     * @param redirect_uri 回调地址
     * @return
     * @throws Exception
     */
    public static String getAuthorizeUrl(String redirect_uri) throws Exception {
        return GET_CODE_URL + "?client_id=" + client_id + "&redirect_uri=" + URLEncoder.encode(redirect_uri, "UTF-8") + "&response_type=code";
    }

    /**
     * 获取token
     *
     * @param code
     * @param redirect_url
     * @return
     * @throws Exception
     */
    public static JSONObject getAccessTokenInfo(String code, String redirect_url) throws Exception {
        if (redirect_url.equals("")) {
            redirect_url = "https://www.oscer.net/oauth/after_bind_osc";
        }
        Map<String, String> params = new HashMap<>();
        params.put("client_id", client_id);
        params.put("client_secret", client_secret);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", redirect_url);
        params.put("code", code);
        params.put("dataType", "json");
        String result = HttpTools.post(ACCESS_TOKEN_URL, params);
        JSONObject json = new JSONObject(result);
        return json;
    }

    public static JSONObject getUserInfo(String access_token) throws Exception {
        String userInfoUrl = GET_USER_INFO + "?access_token=" + access_token;
        String result = HttpTools.get(userInfoUrl);
        JSONObject json = new JSONObject(result);
        return json;
    }


    /**
     * 判断返回的Json对象中是否含有有效的参数值
     *
     * @param json
     * @param validateKey
     * @return
     */
    private static boolean isAccessJson(JSONObject json, String validateKey) {
        try {
            return null != json && StringUtils.isNotBlank(validateKey) && json.has(validateKey) && StringUtils.isNotBlank(json.getString(validateKey));
        } catch (Exception e) {
            return false;
        }
    }

    public static Profile getProfile(String code, String state, String state_secret, String redirect_url) {
        //过滤掉csrf攻击
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state) || !state.equals(state_secret)) {
            return null;
        }
        try {
            JSONObject json = getAccessTokenInfo(code, redirect_url);

            if (!isAccessJson(json, "access_token")) {
                return null;
            }
            String access_token = json.getString("access_token");
            JSONObject info = getUserInfo(access_token);

            Profile profile = new Profile();
            profile.setFirstName(info.getString("name"));
            profile.setFullName(info.getString("name"));
            profile.setEmail(info.getString("email"));
            profile.setProfileImageURL(info.getString("avatar"));
            //gitee账号id,id唯一
            profile.setValidatedId(String.valueOf(info.getLong("id")));
            profile.setGender(info.getString("gender"));
            System.out.println(info);
            return profile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
