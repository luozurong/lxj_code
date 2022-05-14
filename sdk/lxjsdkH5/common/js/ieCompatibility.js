var browser=navigator.appName 
var b_version=navigator.appVersion 
var version=b_version.split(";"); 
var trim_Version=version[1].replace(/[ ]/g,""); 
var host = "http://118.190.8.134:8090";
if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE6.0") 
{ 
	window.location.href = host+"/lxjsdkH5/index/ieCompatibility.html";
} 
else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE7.0") 
{ 
	window.location.href = host+"/lxjsdkH5/index/ieCompatibility.html";
} 
else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0") 
{ 
	window.location.href = host+"/lxjsdkH5/index/ieCompatibility.html";
} 