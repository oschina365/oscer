package net.oscer.dao;

import net.oscer.beans.Node;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 节点
 *
 * @author kz
 * @create 2019-05-22 18:25
 **/
public class NodeDAO extends CommonDao<Node> {

    public final static NodeDAO ME = new NodeDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public List<Node> nodes(int status, int parent) {
        if (!Node.statusList.contains(status)) {
            return null;
        }
        String sql = "select * from nodes where status=? order by sort asc", cacheKey = "status#" + status;
        if (parent == 0) {
            sql = "select * from nodes where status=? and parent =0 order by sort asc";
            cacheKey = "status#" + status + "#parent#" + parent;
        }
        if (parent > 0) {
            Node father = Node.ME.get(parent);
            if (father == null || father.getStatus() == Node.STATUS_FORBID) {
                return null;
            }
            sql = "select * from nodes where status=? and parent = " + parent + " order by sort asc";
            cacheKey = "status#" + status + "#parent#" + parent;
        }
        List<Number> numbers = getDbQuery().query_cache(Number.class, false, Node.ME.CacheRegion(), cacheKey, sql, status);
        List<Long> ids = numbers.stream().map(Number::longValue).collect(Collectors.toList());
        return Node.ME.loadList(ids);

    }
}
