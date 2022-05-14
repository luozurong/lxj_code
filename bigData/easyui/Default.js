$(function () {
    InitLeftMenu();
    $('body').layout();
})

function InitLeftMenu() {
    $('.easyui-accordion li a').click(function () {
        var tabTitle = $(this).text();
        var url = $(this).attr("href");
       
        addTab(tabTitle, url);
        $('.easyui-accordion li div').removeClass("selected");
        $(this).parent().addClass("selected");
    }).hover(function () {
        $(this).parent().addClass("hover");
    }, function () {
        $(this).parent().removeClass("hover");
    });
}

function addTab(subtitle, url) {
    if (!$('#tabs').tabs('exists', subtitle)) {
        $('#tabs').tabs('add', {
            title: subtitle,
            content: createFrame(url),
            closable: true,
            width: $('#mainPanle').width() - 10,
            height: $('#mainPanle').height() - 36
        });
    } else {
        $('#tabs').tabs('select', subtitle);
    }
}

function createFrame(url) {
    var iframeName=url.substring((url.lastIndexOf('/')+1),url.lastIndexOf('.')); 
    var s = '<iframe name="'+iframeName+'" scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:99%;"></iframe>';
    return s;
}
