package net.oscer.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 百度文本审核-违禁labels类型说明
 *
 * @author kz
 * @create 2019-08-28 15:41
 **/
public class BaiduCheckLabelEnum {

    public static final Map<Integer, String> map = new HashMap<>();

    static {
        for (LABEL L : LABEL.values()) {
            map.put(L.getCode(), L.getText());
        }
    }

    public enum LABEL {
        L_1(1, "暴恐违禁"),
        L_2(2, "文本色情"),
        L_3(3, "政治敏感"),
        L_4(4, "恶意推广"),
        L_5(5, "低俗辱骂"),
        L_6(6, "低质灌水"),
        L_99(99, "内容违规");

        private int code;
        private String text;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        LABEL(int code, String text) {
            this.code = code;
            this.text = text;
        }
    }
}
