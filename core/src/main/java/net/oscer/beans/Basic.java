package net.oscer.beans;


import net.oscer.db.CacheMgr;
import net.oscer.db.DbQuery;
import net.oscer.db.Entity;

import java.util.Map;

public class Basic extends Entity {

    /**
     * 阅读数
     */
    public int view_count;
    /**
     * 评论数
     */
    public int comment_count;
    /**
     * 点赞数
     */
    public int praise_count;
    /**
     * 收藏数
     */
    public int collect_count;

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(int praise_count) {
        this.praise_count = praise_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    /**
     * 更新数量
     *
     * @param datas
     * @param update_cache
     * @return
     */
    public void UpdateViewCount(Map<Long, Integer> datas, String column, boolean update_cache) {
        String sql = "UPDATE " + tableName() + " SET " + column + "+=" + column + "+? WHERE id=?";
        int i = 0;
        Object[][] args = new Object[datas.size()][2];
        for (long id : datas.keySet()) {
            int count = datas.get(id);
            args[i][1] = id;
            args[i][0] = count;
            if (update_cache) {
                Basic obj = (Basic) CacheMgr.get(CacheRegion(), String.valueOf(id));
                if (obj != null) {
                    obj.setView_count(obj.getView_count() + count);
                    CacheMgr.set(CacheRegion(), String.valueOf(id), obj);
                }
            }
            i++;
        }
        DbQuery.get(databaseName()).batch(sql, args);
    }
}
