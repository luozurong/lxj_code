<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://www.opensymphony.com/oscache" prefix="cache"%>
<%
	String host = request.getServerName();
	int port = request.getServerPort();
	String contextPath = request.getContextPath();
	request.setAttribute("host", host);
	request.setAttribute("port", port);
	request.setAttribute("contextPath", contextPath);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>QUI 网页界面集成框架</title>
<!--框架必需start-->
<link href="/bin/css/import_basic.css" rel="stylesheet" type="text/css"/>
<link href="/bin/skins/sky/import_skin.css" rel="stylesheet" type="text/css" id="skin" themeColor="blue" prePath="./"/>
<script type="text/javascript" src="/bin/js/jquery-1.4.js"></script>
<script type="text/javascript" src="/bin/js/bsFormat.js"></script>
<!--框架必需end-->

<!--引入组件start-->
<script type="text/javascript" src="/bin/js/attention/zDialog/zDrag.js"></script>
<script type="text/javascript" src="/bin/js/attention/zDialog/zDialog.js"></script>
<!--引入弹窗组件end-->

<!--修正IE6支持透明png图片start-->
<script type="text/javascript" src="js/method/pngFix/supersleight.js"></script>
<!--修正IE6支持透明png图片end-->
<script>
	$(function(){
		var hiconIdx=0;
		var hiconIndex=jQuery.jCookie('hiconIndex');
		if(hiconIndex!=false){
			hiconIdx=parseInt(hiconIndex);
		}
		$(".nav_icon_h_item >a").filter(':eq('+hiconIdx+')').addClass("current");
		$(".nav_icon_h_item >a").each(function(i){
			$(this).click(function(){
				$(".nav_icon_h_item >a").removeClass("current");
				$(this).addClass("current");
				document.getElementById("frmleft").contentWindow.showTab(i)
				jQuery.jCookie('hiconIndex',i.toString());
			})
		})
	})
	
</script>
<style>
a {
	behavior:url(js/method/focus.htc)
}
</style>
<!--弹出式提示框start-->
<script type="text/javascript" src="js/attention/messager.js"></script>
<script>
	$(function(){
		setTimeout("openMsg()",4000)
	})
	function openMsg(){
	$.messager.lays(250, 140);
	$.messager.show(0,'<ul><li><a href="javascript:openWin()"><span class="icon_lightOn">系统消息：3条</span></a></li><div class="clear"></div>'+
	'<li><a href="javascript:openWin()"><span class="icon_lightOn">公共消息：10条</span></a></li><div class="clear"></div>'+
	'<li><a href="javascript:openWin()"><span class="icon_lightOn">私人消息：5条</span></a></li><div class="clear"></div>'+
	'<li><a href="javascript:openWin()"><span class="icon_lightOn">未读消息：15条</span></a></li><div class="clear"></div></ul>','stay');
	}
	function openWin(){
		top.Dialog.open({URL:"templete/msgBox.html",Title:"信件箱"});
	}
</script>
<!--弹出式提示框end-->

<!--图标工具箱start-->
<script type="text/javascript" src="js/attention/floatPanel.js"></script>
<script>
$(function(){
	$("#floatPanel-1").floatPanel({
		width:300,
		direction:"mr",
		init:"hide",
		topDistance:200,
		animatefirst:"false",
		iframe:"templete/toolBox.html",
		beforeClickText:"便捷工具箱",
		afterClickText:"便捷工具箱"
	})	
})
</script>
<!--图标工具箱end-->
</head>
<body>
<div id="floatPanel-1"></div>		
<div id="mainFrame">
<!--头部与导航start-->
<div id="hbox">
	<div id="bs_bannercenter">
	<div id="bs_bannerleft">
	<div id="bs_bannerright2">
		<div class="bs_banner_logo_hmenu"></div>
		<div class="bs_banner_title"></div>
		<div class="nav_icon_h">
			<div class="nav_icon_h_item">
			<a href="javascript:;">
				<div class="nav_icon_h_item_img"><img src="icons/png/06.png"/></div>
				<div class="nav_icon_h_item_text">初始页模板</div>
			</a>
			</div>
			<div class="nav_icon_h_item">
			<a href="javascript:;">
				<div class="nav_icon_h_item_img"><img src="icons/png/07.png"/></div>
				<div class="nav_icon_h_item_text">表格模板</div>
			</a>
			</div>
			<div class="nav_icon_h_item">
			<a href="javascript:;">
				<div class="nav_icon_h_item_img"><img src="icons/png/08.png"/></div>
				<div class="nav_icon_h_item_text">表单模板</div>
			</a>
			</div>
			<div class="nav_icon_h_item">
			<a href="javascript:;">
				<div class="nav_icon_h_item_img"><img src="icons/png/23.png"/></div>
				<div class="nav_icon_h_item_text">图片模板</div>
			</a>
			</div>
			<div class="nav_icon_h_item">
			<a href="javascript:;">
				<div class="nav_icon_h_item_img"><img src="icons/png/35.png"/></div>
				<div class="nav_icon_h_item_text">多层嵌套</div>
			</a>
			</div>
			<div class="nav_icon_h_item">
			<a href="javascript:;">
				<div class="nav_icon_h_item_img"><img src="icons/png/27.png"/></div>
				<div class="nav_icon_h_item_text">图表模板</div>
			</a>
			</div>
			<div class="nav_icon_h_item">
			<a href="javascript:;">
				<div class="nav_icon_h_item_img"><img src="icons/png/28.png"/></div>
				<div class="nav_icon_h_item_text">其他</div>
			</a>
			</div>
			<div class="nav_icon_h_item">
			<a href="javascript:;">
				<div class="nav_icon_h_item_img"><img src="icons/png/02.png"/></div>
				<div class="nav_icon_h_item_text">二级示例</div>
			</a>
			</div>
			<div class="nav_icon_h_item">
			<a href="javascript:;">
				<div class="nav_icon_h_item_img"><img src="icons/png/05.png"/></div>
				<div class="nav_icon_h_item_text">基本模板</div>
			</a>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	</div>
	</div>
	<div id="bs_navcenter">
	<div id="bs_navleft">
	<div id="bs_navright">
		<div class="bs_nav">
			<div class="bs_navleft">
				<li>
					欢迎admin用户，今天是
				<script>
					var weekDayLabels = new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
					var now = new Date();
				    var year=now.getFullYear();
					var month=now.getMonth()+1;
					var day=now.getDate()
				    var currentime = year+"年"+month+"月"+day+"日 "+weekDayLabels[now.getDay()]
					document.write(currentime)
				</script>
				</li>
				<li class="fontTitle">&nbsp;&nbsp;字号:</li>
				<li class="fontChange"><span><a href="javascript:;" setFont="16">大</a></span></li>
				<li class="fontChange"><span><a href="javascript:;" setFont="14">中</a></span></li>
				<li class="fontChange"><span><a href="javascript:;" setFont="12">小</a></span></li>
				<div class="clear"></div>
			</div>
			<div class="bs_navright">
				<span class="icon_btn_up hand" id="fullSrceen" hideLeft="false">全屏</span> <!--如果将hideLeft设为true则全屏时左侧也会被隐藏-->
				<span class="icon_mark hand" onclick='top.Dialog.open({URL:"leftPages/skin_icon_h.html",Title:"皮肤管理",Width:720,Height:445});'>皮肤管理</span>
				<span class="icon_no hand" onclick='top.Dialog.confirm("确定要退出系统吗",function(){window.location="login.html"});'>退出系统</span>
				<a href="../pages/choose.html"><span class="icon_home hand">返回结构选择</span></a>
				<div class="clear"></div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	</div>
	</div>	
</div>
<!--头部与导航end-->

<table width="100%" cellpadding="0" cellspacing="0" class="table_border0">
	<tr>
		<!--左侧区域start-->
		<td id="hideCon" class="ver01 ali01">
							<div id="lbox">
								<div id="lbox_topcenter">
								<div id="lbox_topleft">
								<div id="lbox_topright">
									<div class="lbox_title">操作菜单</div>
								</div>
								</div>
								</div>
								<div id="lbox_middlecenter">
								<div id="lbox_middleleft">
								<div id="lbox_middleright">
										<div id="bs_left">
										<IFRAME scrolling="no" width="100%"  frameBorder=0 id=frmleft name=frmleft src="leftPages/box4.html"  allowTransparency="true"></IFRAME>
										</div>
										<!--更改左侧栏的宽度需要修改id="bs_left"的样式-->
								</div>
								</div>
								</div>
								<div id="lbox_bottomcenter">
								<div id="lbox_bottomleft">
								<div id="lbox_bottomright">
									<div class="lbox_foot"></div>
								</div>
								</div>
								</div>
							</div>
		</td>
		<!--左侧区域end-->
		
		<!--中间栏区域start-->
		<td class="main_shutiao">
			<div class="bs_leftArr" id="bs_center" title="收缩面板"></div>
		</td>
		<!--中间栏区域end-->
		
		<!--右侧区域start-->
		<td class="ali01 ver01"  width="100%">
							<div id="rbox">
								<div id="rbox_topcenter">
								<div id="rbox_topleft">
								<div id="rbox_topright">
									<div class="rbox_title">
										操作内容
									</div>
								</div>
								</div>
								</div>
								<div id="rbox_middlecenter">
								<div id="rbox_middleleft">
								<div id="rbox_middleright">
									<div id="bs_right">
									       <IFRAME scrolling="no" width="100%" frameBorder=0 id=frmright name=frmright src="templete/open.html"  allowTransparency="true"></IFRAME>
									</div>
								</div>
								</div>
								</div>
								<div id="rbox_bottomcenter" >
								<div id="rbox_bottomleft">
								<div id="rbox_bottomright">

								</div>
								</div>
								</div>
							</div>
		</td>
		<!--右侧区域end-->
	</tr>
</table>

<!--尾部区域start-->
<div id="fbox">
	<div id="bs_footcenter">
	<div id="bs_footleft">
	<div id="bs_footright" class="white">
		QUI网页界面集成框架 COPYRIGHT 2010 @ www.quickui.net
	</div>
	</div>
	</div>
</div>
</div>
<!--尾部区域end-->

<!--浏览器resize事件修正start-->
<div id="resizeFix"></div>
<!--浏览器resize事件修正end-->

<!--载进度条start-->
<div class="progressBg" id="progress" style="display:none;"><div class="progressBar"></div></div>
<!--载进度条end-->
</body>
</html>
