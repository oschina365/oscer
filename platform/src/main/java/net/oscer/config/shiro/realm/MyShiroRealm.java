package net.oscer.config.shiro.realm;

import net.oscer.beans.User;
import net.oscer.db.CacheMgr;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static net.oscer.db.Entity.ONLINE;


/**
 * shiro身份校验核心类
 *
 * @author kz
 * @date 2018年1月1日 下午3:19:48
 */
@Component
public class MyShiroRealm extends AuthorizingRealm {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 错误登录次数
     */
    private static final int MAX_LOGIN_NUM = 5;

    public static final String LOGIN_COUNT = "login_count";

    public static final String LOCK_USER = "lock_user";


    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     *
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String name = token.getUsername();
        String password = String.valueOf(token.getPassword());
        String msg = "";
        int login_count = 0;
        User u = null;
        //计数大于5时，设置用户被锁定一小时
        if (CacheMgr.exists(LOGIN_COUNT, name) && (int) CacheMgr.get(LOGIN_COUNT, name) >= MAX_LOGIN_NUM) {
            CacheMgr.set(LOCK_USER, name, name);
        }
        if (CacheMgr.exists(LOCK_USER, name)) {
            msg = "密码输错误5次，请一个小时后再尝试！";
            throw new DisabledAccountException(msg);
        }
        if (!User.ME._GeneratePwdHashCommonBoolean(password, name)) {
            if (CacheMgr.exists(LOGIN_COUNT, name)) {
                login_count = (int) CacheMgr.get(LOGIN_COUNT, name);
            }
            login_count += 1;
            CacheMgr.set(LOGIN_COUNT, name, login_count);
            msg = "帐号或密码不正确！";
            throw new AccountException(msg);
        }
        u = User.ME._GeneratePwdHashCommon(name);
        if (!u.status_is_normal()) {
            msg = "账号被屏蔽,请联系管理员";
            throw new AccountException(msg);
        }
        u.setLogin_time(new Date());
        u.setOnline(ONLINE);
        u.setScore(u.getScore());
        //u.doUpdate();
        Map<String, Object> map = new HashMap<>();
        map.put("login_time", new Date());
        map.put("online", ONLINE);
        map.put("score", u.getScore());
        u.updateFields(map);
        logger.info("身份认证成功，登录用户:{}", name);

        return new SimpleAuthenticationInfo(u, password, getName());
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限认证方法：MyShiroRealm.doGetAuthorizationInfo()");
        /*SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        String userId = user.getId();*/
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        /*//根据用户ID查询角色（role），放入到Authorization里。
        String roleId = sysRoleUserService.selectRoleIdByUserId(userId);
        Set<String> roleSet = new HashSet<String>();
        roleSet.add(roleId);

        //根据用户ID查询权限（permission），放入到Authorization里。
        List<SysRoleFunctionVO> permissionList = new ArrayList<>();
        Set<String> permissionSet = new HashSet<String>();
        for (SysRoleFunctionVO functionVO : permissionList) {
            permissionSet.add(functionVO.getFunctionUrl());
        }*/
        info.setStringPermissions(null);
        return info;
    }

}
