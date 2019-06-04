package net.oscer.framework;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.math.NumberUtils;


/**
 * 用于处理HTTP请求的工具类
 */
public class RequestUtils {
    public final static String NOT_USE_HTTP_ONLY_COOKIE = "NOT_USE_HTTP_ONLY_COOKIE";
    public final static String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)";

    /**
     * 获取浏览器提交的整形参数
     *
     * @param param
     * @param defaultValue
     * @return
     */
    public static int getParam(String param, int defaultValue) {
        return NumberUtils.toInt(param, defaultValue);
    }


    public static long[] getParamValues(String[] values) {
        if (values == null) return null;
        return (long[]) ConvertUtils.convert(values, long.class);
    }

    /**
     * 获取浏览器提交的字符串参�?
     *
     * @param param
     * @param defaultValue
     * @return
     */
    public static String getParam(String param, String defaultValue) {
        return (StringUtils.isEmpty(param)) ? defaultValue : param;
    }

    /**
     * 判断字符串是否是一个IP地址
     *
     * @param addr
     * @return
     */
    public static boolean isIPAddr(String addr) {
        if (StringUtils.isEmpty(addr)) {
            return false;
        }
        String[] ips = StringUtils.split(addr, '.');
        if (ips.length != 4) {
            return false;
        }
        try {
            int ipa = Integer.parseInt(ips[0]);
            int ipb = Integer.parseInt(ips[1]);
            int ipc = Integer.parseInt(ips[2]);
            int ipd = Integer.parseInt(ips[3]);
            return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
                    && ipc <= 255 && ipd >= 0 && ipd <= 255;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 获取用户访问URL中的根域名
     * 例如: www.dlog.cn -> dlog.cn
     *
     * @return
     */
    public static String getDomainOfServerName(String host) {
        if (isIPAddr(host)) {
            return null;
        }
        String[] names = StringUtils.split(host, '.');
        int len = names.length;
        if (len == 1) {
            return null;
        }
        if (len == 3) {
            return makeup(names[len - 2], names[len - 1]);
        }
        if (len > 3) {
            String dp = names[len - 2];
            if (dp.equalsIgnoreCase("com") || dp.equalsIgnoreCase("gov") || dp.equalsIgnoreCase("net") || dp.equalsIgnoreCase("edu") || dp.equalsIgnoreCase("org")) {
                return makeup(names[len - 3], names[len - 2], names[len - 1]);
            } else {
                return makeup(names[len - 2], names[len - 1]);
            }
        }
        return host;
    }

    private static String makeup(String... ps) {
        StringBuilder s = new StringBuilder();
        for (int idx = 0; idx < ps.length; idx++) {
            if (idx > 0) {
                s.append('.');
            }
            s.append(ps[idx]);
        }
        return s.toString();
    }
}
