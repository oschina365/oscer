package net.oscer.sendcloud.common.vo;

import java.io.InputStream;

/**
 * 附件
 *
 * @author kz
 * @date 2018-03-12
 */
public class Attachment {

    public InputStream content;
    public String name;

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Attachment(InputStream content, String name) {
        this.content = content;
        this.name = name;
    }
}