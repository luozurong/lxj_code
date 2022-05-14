package com.jlit.uaas.enums;



public enum LoginKeys {
	
	USERACCOUNT("用户账号","userAccount"),
	USERTYPE("用户权限类型","userType"),
	USERTOKEN("用户token","userToken"),
	USERPLATFORM("用户登录平台","selected_platform"),
	USERNAME("用户姓名","userName"),
	USERID("用户ID","userId"),
	ROLETYPE("用户类型","roleType"),
	ROLENAME("角色名称","roleName"),
	DATAAREA("数据域","dataArea"),
	USERLIST("权限用户列表","userList"),
	DEPARTLIST("权限部门列表","departList"),
	USERDETAIL("用户对象信息","userDetailVo"),
	RESOURCEMENU1("一级菜单","resourceMenu1"),
	RESOURCEMENU2("二级菜单","resourceMenu2"),
	RESOURCEBUTTON("按钮菜单","resourceButton"),
	RESOURCEBUTTONCODE("按钮菜单CODE","resourceButtonCodeSet"),
	RESOURCEBUTTONURL("按钮菜单URL","resourceUrlSet"),
	;
	private LoginKeys(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
     * 名称
     */
    private String name;
    
    

    public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



	/**
     * 类型的值
     */
    private String value;
	
}
