<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*" %>
<%@ page language="java" import="com.hori.vo.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html style="height: 100%;">
	<head>
	<meta charset="UTF-8">
	<title>三位一体综合业务协同系统</title>
	<meta name="renderer" content="webkit" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--<link rel="stylesheet" type="text/css" href="../common/css/default.css" />-->
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />
    <!--<link rel="stylesheet" type="text/css" href="../common/easyui/themes/icon.css">-->
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
    <link rel="shortcut icon" href="<%=basePath%>/homepage/images/ywhlzt.ico" type="image/x-icon">


	    <link rel="stylesheet" type="text/css" href="<%=basePath%>/homepage/css/index.css"/>
	</head>
	<body style="padding-top:60px;">	
		<!--顶部-->
	   <div style="width:100%;overflow:hidden;position:fixed;top:0;left:0;z-index:10;" >
		   	<div class="logoPic fl">
		   	</div>
			<div class="fl">
				<div class="title-text">三位一体综合业务协同系统</div>
			</div>
		   	<div class="tips fr clearfix">
				<div class="admin fl" onclick="setPerson()">
		   			<img src="<%=basePath%>/common/images/ic_administrator.png"/>
		   			<span id="" class="userName">${userName}</span>
		   		</div>
				<div class="border fl"></div>
		   	<%-- 	<div class="mesIcon fl">
		   			<img src="<%=basePath%>/common/images/ic_message.png"/>
		   			<span class="mesNum" id="allNum"></span>
		   		</div> --%>
		   		<div class="logout fr" onclick="logout()"></div>
		   	</div>
		    <div id="mask-top"></div>
	    </div>
		<div id="cc" style="width:100%;padding-left:200px;box-sizing:border-box;height:100%;position:fixed;top:60px; left:0;padding-bottom:60px;">
		    <!--左部-->
		    <div class="yw_left" style="width:200px;float:left;height:100%;margin-left:-200px;background:#2c323e;box-sizing:border-box;padding-bottom:60px;overflow:auto;">
		    	<div>
					<ul class="menuUl">
						<%
							try{
								List<AuthorLoginVo> rootMenus = (List<AuthorLoginVo> ) request.getSession().getAttribute("resourceMenu1");
							
								List<AuthorLoginVo> subMenus = (List<AuthorLoginVo> ) request.getSession().getAttribute("resourceMenu2");
								//查找符合条件的菜单
								for (AuthorLoginVo rootMenu : rootMenus) {// 抽取出权限内的root菜单
									String s =  "<li> ";
									s +=  "<div class='logo-img-black'><img src='homepage/"+rootMenu.getResourseIcon().split("_")[0]+"_02.png'  alt=''/></div> ";
									s +=  "<div class='logo-img-white'   style='display: none'><img src='homepage/"+rootMenu.getResourseIcon()+"' alt=''/></div> ";
									s +=  "<span class='icon'></span> ";
									if("首页".equals(rootMenu.getResourceDesc())){
										s +=  "<div class='left-border  left-border-check'></div> ";
										s+="<a class='menuItem open' target='mainFrame' href='"+rootMenu.getResourceUrl().split("#")[0]+"'>"+rootMenu.getResourceDesc()+"</a>";
									}else if("待办事项".equals(rootMenu.getResourceDesc())){
										s +=  "<div class='left-border'></div> ";
										s+="<a class='menuItem' target='mainFrame' href='"+rootMenu.getResourceUrl().split("#")[0]+"'>"+rootMenu.getResourceDesc()+"</a>";
									}else{
										s +=  "<div class='left-border'></div> ";
										s+="<a class='menuItem'>"+rootMenu.getResourceDesc()+"</a>";
										s+="<div class='submenu'>";
										int index=0;
										for (AuthorLoginVo subMenu : subMenus) {
											if (rootMenu.getResourceId().equals(subMenu.getResourceParentId()))	{
												s+="<a class='submenuItem' target='mainFrame' href='"+subMenu.getResourceUrl()+"'>"+subMenu.getResourceDesc()+"</a>";								
											}
										}
										s+="</div>";
									}
									
									s+="</li>";
									out.println(s);
								}
							}catch(Exception e){
								
							}
						%>
					</ul>
				</div>
                <div id="mask-left"></div>
		    </div>    
		    <div class="center"   style="float:left;width:100%;height:100%;box-sizing:0;position: relative;">
		    	<ins><span id="zhankai" onclick="leftshoworhide($(this))"></span></ins>
		    	<iframe name="mainFrame" id="mainFrame" frameborder="0"  src="/grms/pendingEvent/pendingEventList.html" style="width:100%;height: 100%;"></iframe>
		    </div>    
		</div>
        <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.min.js"></script>
        <script type="text/javascript"	src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
        <script type="text/javascript"	src="<%=basePath%>/common/plugin/common.js"></script>
        <script src="<%=basePath%>/homepage/js/index.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>
