package net.oscer.enums;

/**
 * 动态表的对象类型
 *
 * @author kz
 * @create 2019-10-25 18:12
 **/
public class DynamicEnum {

    public enum TYPE {
        QUESTION(1, "帖子"),
        QUESTION_COMMENT(2, "帖子评论");

        private int type;
        private String txt;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        TYPE(int type, String txt) {
            this.type = type;
            this.txt = txt;
        }
    }

    public enum STATUS {
        SHOW(0, "显示"),
        HIDDEN(1, "隐藏");

        private int type;
        private String txt;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        STATUS(int type, String txt) {
            this.type = type;
            this.txt = txt;
        }
    }
}
