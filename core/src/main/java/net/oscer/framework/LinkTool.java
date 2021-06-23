package net.oscer.framework;

import net.oscer.beans.User;
import org.apache.commons.lang3.math.NumberUtils;
import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


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

    public static class Data{
        private int a;
        private String b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public Data(int a, String b) {
            this.a = a;
            this.b = b;
        }
    }

    public static void main(String[] args) {
        List<Data> list = new ArrayList<>();
        list.add(new Data(1,"1"));
        list.add(new Data(2,"2"));

        System.out.println(list);

        list.parallelStream().forEach(a->a.setA(3));

        System.out.println(list);
    }

}
