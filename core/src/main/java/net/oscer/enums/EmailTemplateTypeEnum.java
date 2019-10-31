package net.oscer.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 邮件模板类型
 * </p>
 *
 * @author kz
 * @date 2018-03-09
 */
public class EmailTemplateTypeEnum {

    public final static Map<String, String> TYPE_MAP = new HashMap<>();

    public enum TYPE {
        ERROR_REDIS("error_redis", "系统提示：redis没有启动"),
        REGISTER("register", "注册用户"),
        RETRIEVE_PASSWORD("retrieve_password", "找回密码"),
        MEMO("memo", "备忘录提醒");

        private String key;
        private String desc;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        TYPE(String key, String desc) {
            this.key = key;
            this.desc = desc;
        }
    }

    static {
        for (TYPE t : TYPE.values()) {
            TYPE_MAP.put(t.getKey(), t.getDesc());
        }
    }
}
