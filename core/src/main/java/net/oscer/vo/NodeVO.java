package net.oscer.vo;

import net.oscer.beans.Node;

import java.util.List;

/**
 * 节点
 **/
public class NodeVO {

    /**
     * 父节点
     */
    private Node parent;
    /**
     * 子节点集合
     */
    private List<Node> childrens;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<Node> childrens) {
        this.childrens = childrens;
    }
}
