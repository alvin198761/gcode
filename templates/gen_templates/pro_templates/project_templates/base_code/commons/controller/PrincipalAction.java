package ${project.base_package}.common.controller;

import ${project.base_package}.common.acl.AdminUserSessionSubject;
import ${project.base_package}.sys.auth.adminsysuser.AdminSysUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

/**
 * @author ${project.author}
 * @功能说明 controller共用的取权限的方法, 需要权限时Controller继承本类即可
 * @date ${project.date}
 */

public abstract class PrincipalAction {

	protected AdminUserSessionSubject getUserSubject(Principal principal) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
		return (AdminUserSessionSubject) token.getPrincipal();
	}

	/**
	 * @功能说明 获取当前用户信息
	 */
	protected AdminSysUser getUser(Principal principal) {
		return getUserSubject(principal).getUser();
	}

	/**
	 * @功能说明 获取当前用户ID
	 */
	protected Integer getUserId(Principal principal) {
		return getUserSubject(principal).getUser().getUser_id();
	}
}
