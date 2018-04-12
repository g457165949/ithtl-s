package com.ven.config;

import com.ven.model.account.User;
import com.ven.model.system.SysPermission;
import com.ven.model.system.SysRole;
import com.ven.service.UserService;
import com.ven.utils.ByteSourceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;

    /**
     * 身份(认证)
     */
    /* 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        // 获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        System.out.println("获取用户名:" + username);
        // 通过username从数据库中查找 User对象，如果找到，没找到.
        // 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User user = userService.findByUsername(username);

        if (user == null) {
            return null;
        }else if(user.getStatus() == 0){
            throw new LockedAccountException();
        }

        System.out.println("----->>username:" + user.getUsername() + "--password:" + user.getPassword());
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("user", user);

        ByteSource salt = ByteSourceUtils.bytes(username);    //使用账号作为盐值
        System.out.println("user:---->>" + username + ":" + salt);

        return new SimpleAuthenticationInfo(user, user.getPassword(), salt, getName());
    }

    /**
     * 身份(授权)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) throws AuthenticationException {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
        user = userService.findOne(user.getId());
        for (SysRole role : user.getSysRoleList()) {
            System.out.println("---->>role:" + role.getName());
            authorizationInfo.addRole(role.getName());
            for (SysPermission p : role.getSysPermissions()) {
                if(StringUtils.isNotBlank(p.getUrl())){
                    System.out.println("------>>url:" + p.getPerms());
                    authorizationInfo.addStringPermission(p.getPerms());
                }
            }
        }
        return authorizationInfo;
    }

    //清除权限缓存缓存
    public void clearCached() {
        super.clearCache(SecurityUtils.getSubject().getPrincipals());
    }

    /**
     * 重新赋值权限(在比如:给一个角色临时添加一个权限,需要调用此方法刷新权限,否则还是没有刚赋值的权限)
     * @param myRealm 自定义的realm
     * @param username 用户名
     */
//    public static void reloadAuthorizing(String username){
//        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
//        MyShiroRealm shiroRealm = (MyShiroRealm)rsm.getRealms().iterator().next();
//        Subject subject = SecurityUtils.getSubject();
//        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
//
//        shiroRealm.clearAllCachedAuthorizationInfo();//清楚所有用户权限
//        //第一个参数为用户名,第二个参数为realmName,test想要操作权限的用户
//        SimplePrincipalCollection principals = new SimplePrincipalCollection(username,realmName);
//        subject.runAs(principals);
//        shiroRealm.getAuthorizationCache().remove(subject.getPrincipals());
//        shiroRealm.getAuthorizationCache().remove(username);
//        subject.releaseRunAs();
//    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
