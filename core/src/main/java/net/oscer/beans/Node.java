package net.oscer.beans;

import net.oscer.db.Entity;

import java.util.Arrays;
import java.util.List;

/**
 * 节点
 *
 * @author kz
 * @create 2019-05-22 18:04
 **/
@Entity.Cache(region = "Node")
public class Node extends Entity {

    public final static Node ME = new Node();

    /**
     * 正常显示
     */
    public static final int STATUS_NORMAL = 0;
    /**
     * 禁止显示
     */
    public static final int STATUS_FORBID = 1;

    public static final List<Integer> statusList = Arrays.asList(STATUS_NORMAL, STATUS_FORBID);

    /**
     * 节点名称
     */
    private String name;
    /**
     * 介绍
     */
    private String info;
    /**
     * 排序
     */
    private int sort;
    /**
     * 状态
     */
    private int status;
    /**
     * 链接地址
     */
    private String url;

    /**
     * 父节点
     */
    private int parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }
}
