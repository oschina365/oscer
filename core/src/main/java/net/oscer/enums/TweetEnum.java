package net.oscer.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 动弹相关枚举
 */
public class TweetEnum {

    public static final Map<String, String> show_sql_map = new HashMap<>();

    /**
     * 首页动弹
     */
    public enum HOME_SHOW {
        HOT("hot", "热门动弹", " order by (praise_count+collect_count+comment_count) desc,view_count desc limit 6"),
        NEW("new", "最新动弹", " order by id desc limit 6"),
        ALL("all", "动弹广场", " order by id desc");

        private String code;
        private String desc;
        private String sql;

        HOME_SHOW(String code, String desc, String sql) {
            this.code = code;
            this.desc = desc;
            this.sql = sql;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }
    }

    static {
        for (HOME_SHOW S : HOME_SHOW.values()) {
            show_sql_map.put(S.getCode(), S.getSql());
        }
    }

}
