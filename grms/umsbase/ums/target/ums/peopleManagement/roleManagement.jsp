<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() ;
%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>角色管理</title>
    <meta http-equiv="X-UA-Compatible" content="edge" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/ums/common/css/reset.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/ums/common/css/default.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/ums/common/easyui/themes/default/easyui.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/ums/common/easyui/themes/icon.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/ums/common/css/common.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/ums/peopleManagement/css/people-common.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/ums/peopleManagement/css/roleManagement.css" />

</head>
<body>
    <div class="wrap">
        <div class="topTitle clearfix">
            <div class="path1 fl">人员管理</div>
            <div class="path2 fl">角色管理</div>
        </div>
        <div class="conditions-select clearfix">
            <div class="time fl">时间：</div>
            <div class="time-star fl">
                <div class="datebox-1"></div>
            </div>
            <div class="to-line fl">--</div>
            <div class="time-end fl">
                <div class="datebox-2"></div>
            </div>
        <!--     <div class="account fl">用户类型：</div>
            <div class="account fl">
                <input id="roleType" value=""/>
            </div>  -->
            <div class="account fl">角色：</div>
            <div class="account1 fl">
                <input value="" id="roleName"/>
            </div>
            <div class="create fl" data-id="0" onclick="roleWin(this)" id="create">创建</div>
            <div class="search fl" onclick="selectAll()" id="select">搜索</div>
        </div>
        <div class="content-wrap">
            <table id="dg"></table>
            <div id="pp" style="position: relative;"></div>
        </div>
    </div>

    <div id="roleWin" style="display: none">
    <div class="roleWin-wrap" id='roleWin1'>
            <div class="detail-wrap"> 
           
              <div class="role-name clearfix">
                    <div class="cl1 fl"><span class="cl1-1">*</span>用户类型</div>
                    <div class="cl2 fl">
                        <input id="roleType"  value=""  maxlength="20" />
                    </div>
                </div> 
                <div class="role-name clearfix">
                    <div class="cl1 fl"><span class="cl1-1">*</span>角色名称</div>
                    <div class="cl2 fl">
                        <input type="text" value="" id="roleName1" maxlength="20"/>
                    </div>
                </div>
                <div class="role-detail clearfix">
                    <div class="cl1 fl"></span>&nbsp;&nbsp;角色备注</span></div>
                    <div class="cl2 fl">
                        <textarea class="textarea-1" id="note1" maxlength="100"></textarea>
                    </div>
                </div>
                <div class="yunwei clearfix">
                    <div class="cl1 fl"><span class="cl1-1">*</span>角色权限</div>
                </div> 
                <div class="tree-wrap">
                    <ul id="box1"></ul>
                </div>
                
                
            </div>

<!--             <div class="the-bottom clearfix" id='button'>
                <span class="quxiao fl" id='button1'>取消</span>
                <span class="comfirm fl" id='button2'>保存</span>

            </div> -->
        </div>


    </div>
    <script type="text/javascript"	src="<%=basePath%>/ums/common/easyui/jquery.min.js"></script>
    <script type="text/javascript"	src="<%=basePath%>/ums/common/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/ums/common/easyui/locale/easyui-lang-zh_CN.js" ></script>
    <script type="text/javascript"	src="<%=basePath%>/ums/common/plugin/common.js"></script>
    <script type="text/javascript"	src="<%=basePath%>/ums/peopleManagement/js/roleManagement.js"></script>
</body>
</html>

























