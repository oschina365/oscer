package net.oscer.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 点赞类型
 *
 * @author kz
 * @date 2019-01-03
 */
public class TypePraiseEnum {

    public static final Map<Integer, String> type_map = new HashMap<>();

    public enum VALUE {
        BLOG(1, "博客"),
        TWEET(2, "动弹"),
        COMMENT(3, "评论,包含评论的回复"),
        QUESTION(4, "帖子");

        private int key;
        private String msg;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        VALUE(int key, String msg) {
            this.key = key;
            this.msg = msg;
        }
    }

    static {
        for (VALUE V : VALUE.values()) {
            type_map.put(V.getKey(), V.getMsg());
        }
    }

}
