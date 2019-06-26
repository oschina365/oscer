package net.oscer.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阅读统计枚举
 *
 * @author kz
 * @create 2019-06-26 16:33
 **/
public class ViewEnum {

    public static final Map<String, String> type_map = new HashMap();
    public static final List<String> type_list = new ArrayList<>();

    static {
        for (TYPE T : TYPE.values()) {
            type_map.put(T.getKey(), T.getText());
            type_list.add(T.getKey());
        }
    }

    public enum TYPE {
        QUESTION("question", "帖子"),
        BLOG("blog", "博客"),
        TWEET("tweet", "动弹");

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
}
