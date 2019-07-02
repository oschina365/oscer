package net.oscer.api.vo;


import net.oscer.framework.ImageUtils;
import net.oscer.framework.LinkTool;
import net.oscer.framework.UploadUtils;

import java.io.Serializable;

/**
 * 七牛上传返回参数
 *
 * @author KZ
 * @date 2018-07-30
 */
public class UploadResultVO implements Serializable {

    /**
     * 上传图片返回状态
     */
    public enum status {
        SUCCESS(0, "成功"),
        FAIL(1, "失败");

        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        status(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    public static class data {
        private String src;
        private String thumb;//缩略图
        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        @Override
        public String toString() {
            return "src" + '\"' + ":\"" + src + '\"'+
                    ",\"thumb" + '\"' + ":\"" + thumb + '\"';
        }
    }

    private int code;
    private String msg;
    private String key;
    private long size;
    private String name;
    private data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UploadResultVO.data getData() {
        return data;
    }

    public void setData(UploadResultVO.data data) {
        this.data = data;
    }

    public UploadResultVO() {
    }

    public UploadResultVO(int code) {
        this.code = code;
    }

    public UploadResultVO(int code, String msg, String key, long size, String name, data data) {
        this.code = code;
        this.msg = msg;
        this.key = key;
        this.size = size;
        this.name = name;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\"" + ":" + code +
                ", \"msg\"" + ":" + "\"" + msg + "\"" +
                ", \"key\"" + ":" + "\"" + key + "\"" +
                ", \"name\"" + ":" + "\"" + name + "\"" +
                ", \"data\":{\"" + data + "}" +
                ", \"size\"" + ":" + size +
                '}';
    }

    /**
     * 返回成功
     *
     * @param msg
     * @param key
     * @param size
     * @return
     */
    public static UploadResultVO success(String domain, String msg, String key, long size, String new_name, data data, String thumb) {
        UploadResultVO vo = new UploadResultVO(status.SUCCESS.getCode(), msg, key, size, new_name, data);
        vo.setKey(vo.getKey());
        UploadResultVO.data d = new UploadResultVO.data();
        d.setSrc(LinkTool.getHost("media") + new_name);
        d.setThumb(LinkTool.getHost("media") + new_name);
        if (UploadUtils.map.get(ImageUtils.getSuffix(thumb).toLowerCase()) == null) {
            d.setThumb(LinkTool.getHost("media") + thumb);
        }
        vo.setData(d);
        return vo;
    }

    /**
     * 返回失败
     *
     * @param msg
     * @return
     */
    public static UploadResultVO failWith(String msg) {
        return new UploadResultVO(status.FAIL.getCode());
    }

}