/*$(window.parent.document).find("#iframeId").load(function () {
    var main = $(window.parent.document).find("#iframeId");
    var thisheight = $(document).height() + 30;
    main.height(thisheight);
});*/
function changeFrameHeight(){
    var ifm= document.getElementById("iframeId");
    ifm.height=document.documentElement.clientHeight;
}
window.onresize=function(){
     changeFrameHeight();
}