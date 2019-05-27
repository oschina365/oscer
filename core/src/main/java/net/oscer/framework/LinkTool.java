package net.oscer.framework;

import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;


public class LinkTool {

    private static final Logger log = LoggerFactory.getLogger(LinkTool.class);

    public static String root() {
        return getHost("root");
    }

    public static String statics() {
        return getHost("static");
    }

    /**
     * 取core内的host文件
     * global下
     */
    public static String getHost(String key) {
        return getHost(key, "global");
    }

    /**
     * 取core内的host文件
     * tio
     */
    public static String getTio(String key) {
        return getHost(key, "tio");
    }

    public static String getConfigKey(String config_name, String key, String g) {
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        String url = "";
        try {
            InputStream stream = LinkTool.class.getResourceAsStream("/" + config_name);
            Ini ini = new Ini(stream);
            if (ini.get(g) == null) {
                return null;
            }
            url = ini.get(g).get(key);
            stream.close();
            return url;
        } catch (Exception e) {
            log.error("host文件读取错误", e.getMessage());
        }
        return url;
    }

    /**
     * 取core内的host文件
     */
    public static String getHost(String key, String g) {
        return getConfigKey("hosts.properties", key, g);
    }

}
