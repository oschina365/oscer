package net.oscer.vo;

import net.oscer.beans.User;
import net.oscer.framework.FormatTool;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 返回页面可见的信息
 *
 * @author MRCHENIKE
 * @create 2019-09-10 16:54
 **/
public class UserVO {

    public static final UserVO ME = new UserVO();

    private long id;
    private String nickname;
    private String username;
    private String headimg;
    private String name;
    private String self_info;
    private int score;
    private String sdf_insert_date;
    private Integer sex;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelf_info() {
        return self_info;
    }

    public void setSelf_info(String self_info) {
        this.self_info = self_info;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSdf_insert_date() {
        return sdf_insert_date;
    }

    public void setSdf_insert_date(String sdf_insert_date) {
        this.sdf_insert_date = sdf_insert_date;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public static UserVO convert(User user) {
        if (user == null || user.getId() <= 0L) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setName(user.getName());
        vo.setNickname(user.getNickname());
        vo.setUsername(user.getUsername());
        vo.setHeadimg(user.getHeadimg());
        vo.setScore(user.getScore());
        vo.setSelf_info(user.getSelf_info());
        vo.setSex(user.getSex());
        vo.setSdf_insert_date(FormatTool.format_intell_time(user.getInsert_date()));
        return vo;
    }

    public static List<UserVO> converts(List<User> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(u -> convert(u)).collect(Collectors.toList());
    }
}
