package net.oscer.enums;

import java.util.Arrays;
import java.util.List;

/**
 * url放过
 *
 * @author kz
 * @create 2019-06-26 17:57
 **/
public class UrlPassEnum {

    public static final String user = "/user/";
    public static final String u = "/u/";
    public static final String druid = "/druid";
    public static final String res = "/res/";
    public static final String q = "/q/";
    public static final String oauth = "/oauth";
    public static final String comment = "/comment";
    public static final String sign = "/sign";
    public static final String api = "/api/";
    public static final String tw = "/tw";
    public static final String uni = "/uni/";
    public static final String f = "/f/";
    public static final String error_500 = "/500";
    public static final String error_400 = "/400";
    public static final String up = "/up/";
    public static final String captcha = "/captcha/";
    public static final String resume = "/resume";
    public static final String photo = "/p";
    public static final String tool = "/tool/";
    public static final String tweet = "/tweet/";

    public static final List<String> list = Arrays.asList(user, u, druid, res, q, oauth, comment, sign, api, tw, uni, f, error_500, error_400, up, captcha, resume, photo, tool, tweet);
}
