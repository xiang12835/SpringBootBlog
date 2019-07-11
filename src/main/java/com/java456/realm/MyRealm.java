package com.java456.realm;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.java456.dao.base.UserDao;
import com.java456.entity.base.User;
import com.java456.service.base.UserService;

/**
 * 自定义Realm
 *
 */
public class MyRealm extends AuthorizingRealm{


	@Resource
	private UserService  userService;
	@Resource
	private UserDao  userDao;
	
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String name=(String) SecurityUtils.getSubject().getPrincipal();
		
		User user=userDao.findByName(name);
		
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		info.addStringPermission("d货"); //添加权限
		
		/*List<Role> roleList=roleRepository.findByUserId(user.getId());
		Set<String> roles=new HashSet<String>();
		for(Role role:roleList){
			roles.add(role.getName());
			List<Menu> menuList=menuRepository.findByRoleId(role.getId());
			for(Menu menu:menuList){
				info.addStringPermission(menu.getName()); //添加权限
			}
		}
		info.setRoles(roles);*/
		return info;
	}

	/**
	 * 权限认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String name=(String)token.getPrincipal();
		User user=userDao.findByName(name);
		if(user!=null){
			AuthenticationInfo authcInfo=new SimpleAuthenticationInfo(user.getName(),user.getPwd(),"xxx");
			return authcInfo;
		}else{
			return null;				
		}
	}

}
