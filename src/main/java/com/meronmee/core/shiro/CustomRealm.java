package com.meronmee.core.shiro;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.meronmee.base.model.User;
import com.meronmee.base.service.UserService;
import com.meronmee.core.utils.BaseUtils;

/**
 * 自定义权限的认证类<p>
 * Realm 的作用类似数据源，Shiro从Realm获取相关数据（如用户、角色、权限）
 * 
 */
public class CustomRealm extends AuthorizingRealm {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	/**
	 * 认证（登录控制）
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//log.info("------CustomRealm#doGetAuthenticationInfo------");
		// token中包含用户输入的用户名和密码 
		// 第一步从token中取出用户名 		
		String username = BaseUtils.toString(token.getPrincipal()); 
				
		// 第二步：根据用户输入的username从数据库查询		
	    User user = this.userService.findByUsernameOrPhone(username);
	    
		//如果查询不到返回null，将抛出一个org.apache.shiro.authc.UnknownAccountException
		if(user == null) {
			return null; 
		} 
		
		//获取数据库中保存的用户密码 
		String password = user.getPassword(); 
	  
		//返回认证信息
		//第一个参数principal一般可以是username或userId或直接是User实体，
		//在其他使用到principal的地方也就应该转换成对应的类型。这里直接存入User实体，SecurityUtils.getSubject().getPrincipal()可以直接获得用户
		AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user, password, getName()); 
		
		return authcInfo;
	}

	/**
	 * 	授权(访问控制)
	 *  授权查询回调函数, 进行鉴权，在缓存中无用户的授权信息时调用.<p>
	  * 该操作只有在第一次进行权限验证的时候才会初始化，将用户所拥有的所有权限放入AuthorizationInfo对象
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//log.info("------CustomRealm#doGetAuthorizationInfo------");
		//primaryPrincipal在上一步 doGetAuthenticationInfo 中填充到了 SimpleAuthenticationInfo 中 
		User user = (User)principals.getPrimaryPrincipal(); 
		
		SimpleAuthorizationInfo authzInfo = new SimpleAuthorizationInfo();

		//查询用户角色信息
		List<String> roles = this.userService.getUserPermissions(user);
		//组装角色信息
		authzInfo.addRoles(roles);		
		
		
		//查询用户权限信息
		List<String> permissions = this.userService.getUserPermissions(user);		
		//组装权限信息
		authzInfo.addStringPermissions(permissions);
				  
		return authzInfo;
	}
		
	 /**
	  * 清除权限缓存<p>
	  * 当用户权限发生变化，需要手动调用clearCachedAuthorizationInfo方法去清除用户权限缓存
	  */
	 public void clearCachedAuthorizationInfo() { 
		  PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals(); 
		  super.clearCache(principals); 
		  
		  /*
		  SimplePrincipalCollection pc = new SimplePrincipalCollection();
		  pc.add(username, super.getName());
		  super.clearCachedAuthorizationInfo(pc);
		  */
	 } 
	 
	/**
     * 清空所有关联认证
     */
    public void clearAllCachedAuthorizationInfo() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            for (Object key : cache.keys()) {
                cache.remove(key);
            }
        }
    }
}
