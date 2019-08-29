package net.oscer.vo;

import net.oscer.beans.*;
import net.oscer.dao.VisitDAO;
import net.oscer.db.Entity;
import net.oscer.framework.FormatTool;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * 最近阅读
 *
 * @author MRCHENIKE
 * @create 2019-08-22 10:40
 **/
public class ReadVO extends Visit {

    /**
     * 格式化的阅读时间
     */
    private String sdf_read_time;

    /**
     * 文章作者
     */
    private User author;

    private Entity entity;

    public String getSdf_read_time() {
        return sdf_read_time;
    }

    public void setSdf_read_time(String sdf_read_time) {
        this.sdf_read_time = sdf_read_time;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public static Entity obj(int obj_type) {
        if (obj_type <= 0) {
            return null;
        }
        switch (obj_type) {
            case Visit.USER:
                return User.ME;
            case Visit.BLOG:
                return Blog.ME;
            case Visit.TWEET:
                return Tweet.ME;
            case Visit.QUESTION:
                return Question.ME;

            default:
                return null;
        }
    }

    public static Entity obj(int obj_type, long obj_id) {
        if (obj_type <= 0 || obj_id <= 0L) {
            return null;
        }
        switch (obj_type) {
            case Visit.USER:
                return User.ME.get(obj_id);
            case Visit.BLOG:
                return Blog.ME.get(obj_id);
            case Visit.TWEET:
                return Tweet.ME.get(obj_id);
            case Visit.QUESTION:
                return Question.ME.get(obj_id);

            default:
                return null;
        }
    }


    /**
     * 用户阅读记录
     *
     * @return
     */
    public static List<ReadVO> listByUserObjType(long user, int obj_type) {
        if (user <= 0L || obj_type <= 0) {
            return null;
        }
        List<Visit> visits = VisitDAO.ME.listByUserAndObjType(user, obj_type);
        if (CollectionUtils.isEmpty(visits)) {
            return null;
        }
        List<Long> users = new ArrayList<>();
        List<Long> obj_ids = new ArrayList<>();
        visits.parallelStream().forEach(v -> {
            users.add(v.getUser_id());
            obj_ids.add(v.getObj_id());
        });
        User.ME.loadList(users);
        Entity obj = obj(obj_type);
        obj.loadList(obj_ids);

        List<ReadVO> list = new LinkedList<>();
        for (Visit v : visits) {

            ReadVO vo = new ReadVO();
            Entity e = obj.get(v.getObj_id());
            if(e==null){
                continue;
            }
            vo.setAuthor(obj(Visit.USER).get(e.user()));
            vo.setEntity(e);
            vo.setSdf_read_time(FormatTool.format_intell_time(v.getLast_date()));
            vo.setId(v.getId());
            vo.setObj_id(v.getObj_id());
            list.add(vo);
        }
        return list;
    }
}
