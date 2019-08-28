package net.oscer.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本审核方式
 **/
public class TextCheckEnum {

    public static final List<String> TYPE_LIST = new ArrayList<>();

    public enum TYPE {
        BAIDU("baidu", "百度文本审核"),
        ALIYUN("aliyun", "阿里云文本审核");

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

        TYPE(String key, String text) {
            this.key = key;
            this.text = text;
        }
    }

    static {
        for (TYPE t : TYPE.values()) {
            TYPE_LIST.add(t.getKey());
        }
    }
}
