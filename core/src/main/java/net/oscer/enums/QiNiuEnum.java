package net.oscer.enums;

/**
 * 七牛
 *
 * @author kz
 * @date 2017-11-20
 */
public class QiNiuEnum {

    /**
     * 文件上传状态
     */
    public enum STATUS {
        FAIL(-1, "上传失败"),
        SUCCESS(0, "上传成功");

        /**
         * 文件上传状态
         */
        private Integer key;
        /**
         * 文件上传状态描述
         */
        private String desc;

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        STATUS(Integer key, String desc) {
            this.key = key;
            this.desc = desc;
        }
    }
}
