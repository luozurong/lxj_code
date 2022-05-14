package com.hori.lxjsdk.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

@Action(value = "testAction")
@Namespace("/test")  //定义一个命名空间，表示活动模块内容
public class TestAction extends BaseAction{

	private static final long serialVersionUID = -7495087527769692502L;

	
	@Action(value="test",results={@Result(name="success",location="/WEB-INF/pages/test.jsp")})
	   public String test(){
		   
		   return SUCCESS;
	   }
}
