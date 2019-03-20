package com.android.sdk.port;

public class RoleBean {
	private String roleId = "";
	private String roleName = "";
	private String serverId = "";
	private String serverName = "";
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	@Override
	public String toString() {
		return "RoleBean [roleId=" + roleId + ", roleName=" + roleName + ", serverId=" + serverId + ", serverName="
				+ serverName + "]";
	}
	
	
}
