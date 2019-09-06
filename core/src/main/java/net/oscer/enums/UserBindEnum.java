package net.oscer.enums;

/**
 * 第三方登录方式
 **/
public class UserBindEnum {

    public enum FROM {
        GITEE("gitee", "码云"),
        GITHUB("github", "github"),
        WEIXIN("weixin", "微信"),
        OSC("osc", "开源中国");

        private String key;
        private String text;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        FROM(String key, String text) {
            this.key = key;
            this.text = text;
        }
    }
}
