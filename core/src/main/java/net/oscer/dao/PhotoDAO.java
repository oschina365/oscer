package net.oscer.dao;

import net.oscer.beans.Photo;
import net.oscer.db.CacheMgr;

import java.util.List;

/**
 * 照片
 *
 * @author kz
 * @create 2020-08-04 18:28
 **/
public class PhotoDAO extends CommonDao<Photo> {

    public final static PhotoDAO ME = new PhotoDAO();

    @Override
    protected String databaseName() {
        return "mysql";
    }

    public List<Photo> photos(long user, int page, int size) {
        String sql = "select id from photos where user=? order by upload_time desc ";
        List<Long> ids = getDbQuery().query_slice_cache(long.class, getCache_region(), "photos#" + user, 100, sql, page, size, user);
        return Photo.ME.loadList(ids);
    }

    public int count(long user) {
        String sql = "select count(*) from photos where user=? ";
        return getDbQuery().stat_cache(getCache_region(), "count#" + user, sql, user);
    }

    public void evict(long user) {
        CacheMgr.evict(getCache_region(), "photos#" + user);
        CacheMgr.evict(getCache_region(), "count#" + user);
    }
}
