package net.oscer.beans;


import net.oscer.db.DbQuery;
import net.oscer.db.Entity;
import net.oscer.framework.FormatTool;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 注册用户
 *
 * @author liudong
 */
@Entity.Cache(region = "User")
public class User extends Entity implements Serializable {

    public final static User ME = new User();

    /**
     * 用户权限
     */
    public final static int ROLE_GENERAL = 1;

    public static final int SEX_UNKONW = 0;
    public static final int SEX_BOY = 1;
    public static final int SEX_GIRL = 2;

    /**
     * 用户名(登录名,如jack)
     */
    private String username;
    /**
     * 密码(明文)
     */
    private String password;
    /**
     * 密码（加密）
     */
    private String salt;
    /**
     * 头像
     */
    private String headimg;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 用户真实姓名
     */
    private String name;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机
     */
    private String phone;
    /**
     * 公司名称
     */
    private String company;
    /**
     * 职位
     */
    private String job;
    /**
     * 微信
     */
    private String wx;
    /**
     * 微信二维码加好友，图片地址
     */
    private String wx_qrcode;
    /**
     * qq
     */
    private String qq;
    /**
     * 性别（0：保密 1：男 2：女）
     */
    private Integer sex;
    /**
     * 自我简介
     */
    private String self_info;
    /**
     * 最后登录时间
     */
    private Date login_time;
    /**
     * 登出时间(手动登出,系统登出即session超时时间)
     */
    private Date logout_time;

    /**
     * 冻结：-1 正常:0 封号：1  手动注销：2  系统销毁：3
     */
    private Integer status;

    /**
     * 个性域名
     */
    private String ident;

    /**
     * 登录IP
     */
    private String login_ip;

    /**
     * 阅读数
     */
    private int view_count;

    /**
     * 用户是否在线
     * 登录状态（0:下线 1:上线）
     */
    private int online;

    /**
     * 积分
     */
    private int score;

    /**
     * 今天获得的积分
     */
    private int score_today;

    /**
     * 城市
     */
    private String city;

    private String vip_text;

    private String sdf_insert_date;

    private String sdf_last_date;


    /**
     * 注册来源，默认是pc
     */
    private String from;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getWx_qrcode() {
        return wx_qrcode;
    }

    public void setWx_qrcode(String wx_qrcode) {
        this.wx_qrcode = wx_qrcode;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSelf_info() {
        return self_info;
    }

    public void setSelf_info(String self_info) {
        this.self_info = self_info;
    }

    public Date getLogin_time() {
        return login_time;
    }

    public void setLogin_time(Date login_time) {
        this.login_time = login_time;
    }

    public Date getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(Date logout_time) {
        this.logout_time = logout_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore_today() {
        return score_today;
    }

    public void setScore_today(int score_today) {
        this.score_today = score_today;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVip_text() {
        return vip();
    }

    public void setVip_text(String vip_text) {
        this.vip_text = vip();
    }

    public String getSdf_insert_date() {
        return FormatTool.format_intell_time(this.insert_date);
    }

    public void setSdf_insert_date(String sdf_insert_date) {
        this.sdf_insert_date = FormatTool.format_intell_time(this.insert_date);
    }

    public String getSdf_last_date() {
        return FormatTool.format_intell_time(this.last_date);
    }

    public void setSdf_last_date(String sdf_last_date) {
        this.sdf_last_date = FormatTool.format_intell_time(this.last_date);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 用户是否正常
     *
     * @return
     */
    public boolean status_is_normal() {
        if (this != null && this.getStatus() == Entity.STATUS_NORMAL) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验密码
     *
     * @param input_pwd
     * @param user
     * @return
     */
    public boolean _ValidatePwd(String input_pwd, User user) {
        if (StringUtils.isBlank(input_pwd) || user == null || user.getId() <= 0L) {
            return false;
        }
        if (input_pwd.equals(user.getSalt())) {
            return true;
        }
        return _GeneratePwdHash(input_pwd, user).equals(user.getSalt());
    }

    /**
     * 生成密码哈希
     *
     * @param input_pwd
     * @param user
     * @return
     */
    public String _GeneratePwdHash(String input_pwd, User user) {
        return DigestUtils.shaHex(user.getEmail() + "->" + DigestUtils.shaHex(input_pwd));
    }

    /**
     * 生成密码哈希
     *
     * @param input_pwd
     * @param email
     * @return
     */
    public String _GeneratePwdHash(String input_pwd, String email) {
        return DigestUtils.shaHex(email + "->" + DigestUtils.shaHex(input_pwd));
    }

    /**
     * 通过个性域名,邮箱,手机号码查询用户信息，然后对比输入的密码
     *
     * @param ident
     * @return
     */
    public User _GeneratePwdHashCommon(String ident) {
        String sql = "select id from users where ident =? or email = ? or phone = ? or username=? ";
        Number n = DbQuery.get("mysql").read(Number.class, sql, ident, ident, ident, ident);
        if (n != null && n.longValue() > 0L) {
            return User.ME.get(n.longValue());
        }
        return null;
    }

    /**
     * 通过个性域名
     *
     * @param ident
     * @return
     */
    public boolean _GeneratePwdHashCommonBoolean(String input_pwd, String ident) {
        User u = _GeneratePwdHashCommon(ident);
        if (u != null) {
            return _ValidatePwd(input_pwd, u);
        }
        return false;
    }

    public String vip() {
        if (this == null || this.getId() <= 0L) {
            return null;
        }
        if (this.getId() <= 2) {
            return "管理员";
        }
        if (this.getScore() >= VIP7_SCORE) {
            return VIP_MAP.get(VIP7_SCORE);
        }
        if (this.getScore() >= VIP6_SCORE) {
            return VIP_MAP.get(VIP6_SCORE);
        }
        if (this.getScore() >= VIP5_SCORE) {
            return VIP_MAP.get(VIP5_SCORE);
        }
        if (this.getScore() >= VIP4_SCORE) {
            return VIP_MAP.get(VIP4_SCORE);
        }
        if (this.getScore() >= VIP3_SCORE) {
            return VIP_MAP.get(VIP3_SCORE);
        }
        if (this.getScore() >= VIP2_SCORE) {
            return VIP_MAP.get(VIP2_SCORE);
        }
        if (this.getScore() >= VIP1_SCORE) {
            return VIP_MAP.get(VIP1_SCORE);
        }
        return null;
    }
}
