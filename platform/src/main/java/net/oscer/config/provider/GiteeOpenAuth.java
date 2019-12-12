package net.oscer.config.provider;

import net.oscer.framework.HttpKits;
import net.oscer.framework.StringUtils;
import org.apache.commons.io.IOUtils;
import org.brickred.socialauth.Profile;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 步骤：https://gitee.com/api/v5/oauth_doc
 *
 * @author kz
 */
public class GiteeOpenAuth {

    private static final String client_id = "33b7e5b64a347f438df3d557d2532a1c8729ce6e14b950c7cc01dde3f4e72983";
    private static final String client_secret = "91f31387bc5d8838121c0f216320969b5bc3ebebabce452ecccfe124c757942e";
    private static final String GET_CODE_URL = "https://gitee.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://gitee.com/oauth/token";
    private static final String GET_USER_INFO = "https://gitee.com/api/v5/user";

    /**
     * 组装请求code的URL
     *
     * @param redirect_uri 回调地址
     * @return
     * @throws Exception
     */
    public static String getAuthorizeUrl(String redirect_uri) throws Exception {
        return GET_CODE_URL + "?client_id=" + client_id + "&redirect_uri=" + URLEncoder.encode(redirect_uri, "UTF-8") + "&response_type=code&scope=user_info";
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
            redirect_url = "http://www.oscer.net/oauth/after_bind_gitee";
        }
        Map<String, String> params = new HashMap<>();
        params.put("client_id", client_id);
        params.put("client_secret", client_secret);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", redirect_url);
        params.put("code", code);
        params.put("scope", "user_info");
        String result = HttpTools.post(ACCESS_TOKEN_URL, params);
        JSONObject json = new JSONObject(result);
        return json;
    }

    public static JSONObject getUserInfo(String access_token) throws Exception {
        String userInfoUrl = GET_USER_INFO + "?access_token=" + access_token;
        InputStream result = HttpKits.get(userInfoUrl);
        if (result == null) {
            return null;
        }
        String info = IOUtils.toString(result);
        JSONObject json = new JSONObject(info);
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
            profile.setFullName(info.getString("name"));
            profile.setEmail(info.getString("name"));
            profile.setProfileImageURL(info.getString("avatar_url"));
            //gitee账号id,id唯一
            profile.setValidatedId(info.get("id").toString());
          /*  if (StringUtils.isNotEmpty(info.getString("gender"))) {
                profile.setGender(info.getString("gender"));
            }*/
            return profile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
