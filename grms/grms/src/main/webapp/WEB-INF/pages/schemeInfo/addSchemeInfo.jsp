
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

<head lang="en">
<meta charset="UTF-8">
<title>上传提案</title>
<meta http-equiv="X-UA-Compatible" content="edge" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/reset.css" />

<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/easyui/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/common.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/schemeInfo/css/people-common.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/schemeInfo/css/addSchemeInfo.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>/common/css/default.css" />
</head>

<body>
	<div class="wrap">
		<div class="border fl"></div>
		<div class="topTitle clearfix">
			<div class="path1 fl">提案管理</div>
			<div class="path2 fl">新建提案</div>
		</div>
        <div class="conditions-select clearfix">
            <div class="clearfix">
                <div class="account adjust1 fl">提案名称：</div>
                <div class="account1 fl" style="width:auto;">
                    <input id="name" style="width: 180px;border-radius: 5px;padding-left: 10px;" maxlength="20"/>
                </div>
                <div id="nameNotNull" class="account adjust1 fl" style="display: none;"><span style="color: red">名称不可为空</span></div>
            </div>
            <div class="clearfix clearfixed">
                <div class="account fl">模板附件：</div>
                <div class="account1 fl">
                	<input type="text" id="fileName" style="border-radius: 5px;padding-left: 10px;" placeholder="最大100M" readonly="readonly">
                    <input type="file" class="inputfixed" id="file" onchange="uploadFile('file')"/>
                </div>
                <div class="upload fl" style="width: 120px;border-radius: 3px;margin-left: 20px;color: #51d2b6;background: #fff;border:1px solid #51d2b6;" id="upload">上传</div>
            </div>
            <div class="clearfix pad">
            	<div class="quxiao " onclick="goback()" id="cancel">取消</div>
                <div class="comfirm " onclick="saveSchemeInfo()" id="sure">完成</div>
            </div>
        </div>
	</div>
	
	<script type="text/javascript" src="<%=basePath%>/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/common/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>/common/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=basePath%>/common/plugin/common.js"></script>
	<script type="text/javascript" src="<%=basePath%>/schemeInfo/js/addSchemeInfo.js"></script>
</body>

</html>