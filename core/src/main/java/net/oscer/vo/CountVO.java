package net.oscer.vo;

import java.io.Serializable;

/**
 * 数量排序
 *
 * @author kz
 * @create 2019-06-25 17:12
 **/
public class CountVO implements Serializable {

    private Long id;
    private Integer count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
