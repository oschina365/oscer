package net.oscer.beans;

import net.oscer.db.Entity;

/**
 * 敏感词列表
 *
 * @author kz
 * @create 2019-08-28 16:31
 **/
@Entity.Cache(region = "BadWord")
public class BadWord extends Entity {

    public static final BadWord ME = new BadWord();

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
