package net.oscer.config.provider;

import net.oscer.framework.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第三方网站枚举
 *
 * @author kz
 * @create 2019-04-12 17:28
 **/
public class OauthEnum {

    public static final String DEFAULT = "pc";
    public static final List<String> fromList = new ArrayList<>();
    public static final Map<String, String> fromMap = new HashMap<>();

    static {
        for (FROM f : FROM.values()) {
            fromList.add(f.getForm());
            fromMap.put(f.getForm(), f.getForm());
        }
    }

    public enum FROM {
        //WECHAT("wechat", "微信"),
        //QQ("qq", "QQ"),
        GITEE("gitee", "码云"),
        OSC("osc", "开源中国社区"),
        GITHUB("github", "github");

        FROM(String form, String text) {
            this.form = form;
            this.text = text;
        }

        private String form;
        private String text;

        public String getForm() {
            return form;
        }

        public void setForm(String form) {
            this.form = form;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static String getFrom(String from) {
        if (StringUtils.isBlank(StringUtils.trimToNull(from))) {
            return DEFAULT;
        }
        from = from.toLowerCase();
        if (fromMap.get(from) == null) {
            return DEFAULT;
        }
        return fromMap.get(from);
    }
}
