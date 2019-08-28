package net.oscer.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author kz
 * @date 2018/09/20
 */
public class TetxAntispamScanConfig {

    protected static String accessKeyId;
    protected static String accessKeySecret;

    protected static String regionId;


    protected static Map<String, String> cn_region_map = new HashMap<>();//区域
    protected static Map<String, String> label_map = new HashMap<>();//包含垃圾的提示

    static {
        for (CN_REGION cn_region : CN_REGION.values()) {
            cn_region_map.put(cn_region.key, cn_region.region);
        }

        for (Label label : Label.values()) {
            label_map.put(label.code, label.msg);
        }

        Properties properties = new Properties();

        try {
            properties.load(TetxAntispamScanConfig.class.getResourceAsStream("/ali_config.properties"));
            accessKeyId = properties.getProperty("accessKeyId");
            accessKeySecret = properties.getProperty("accessKeySecret");
            regionId = properties.getProperty("regionId");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 区域
     */
    public enum CN_REGION {
        CN_SHANGHAI("cn-shanghai", "green.cn-shanghai.aliyuncs.com"),
        CN_HANGZHOU("cn-hangzhou", "green.cn-hangzhou.aliyuncs.com"),
        CN_LOCAL("local", "api.green.alibaba.com");

        private String key;
        private String region;

        CN_REGION(String key, String region) {
            this.key = key;
            this.region = region;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }

    /**
     * 包含垃圾的提示
     */
    public enum Label {
        SPAM("spam", "含垃圾信息"),
        AD("ad", "广告"),
        POLITICS("politics", "涉政"),
        TERRORISM("terrorism", "暴恐"),
        ABUSE("abuse", "辱骂"),
        PORN("porn", "色情"),
        FLOOD("flood", "灌水"),
        CONTRABAND("contraband", "违禁"),
        MEANINGLESS("meaningless", "无意义");

        private String code;
        private String msg;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        Label(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    /**
     * 建议用户执行的操作
     */
    public enum Suggestion {
        PASS("pass", "文本正常"),
        REVIEW("review", "需要人工审核"),
        BLOCK("block", "文本违规");

        private String code;
        private String msg;

        Suggestion(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class Result {
        private boolean success;//检测通过
        private String msg;//包含垃圾标志,提示语

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Result(boolean success, String label) {
            this.success = success;
            if (!success) {
                this.msg = label_map.get(label) != null ? label_map.get(label) : "内容存在垃圾";
            }
        }

        @Override
        public String toString() {
            return "Result{" +
                    "success=" + success +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }

    /**
     * 检测通过
     *
     * @return
     */
    public static Result success() {
        return new Result(true, null);
    }

    /**
     * 检测失败
     *
     * @param label
     * @return
     */
    public static Result fail(String label) {
        return new Result(false, label);
    }

    /**
     * 获取区域
     *
     * @return
     */
    protected static String getDomain() {
        if (cn_region_map.get(regionId) != null) {
            return cn_region_map.get(regionId);
        }
        return CN_REGION.CN_SHANGHAI.getRegion();
    }

    protected static String getEndPointName() {
        return regionId;
    }

}
