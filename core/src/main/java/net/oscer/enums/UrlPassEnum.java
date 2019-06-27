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

    public static final List<String> list = Arrays.asList(user, u, druid, res, q, oauth, comment,sign);
}
