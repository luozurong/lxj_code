
	$(".docCenter-item>div").click(function(){
		if($(this).next().css("display") == "none"){
			$(this).next().show();
			$(this).find("i").addClass("docCenter-icon-active");
		}else{
			$(this).find("i").removeClass("docCenter-icon-active");
			$(this).next().hide();
		}
	})
	$(".docCenter-item ul li a").click(function(){
		var dataVal = $(this).attr("data-val");
		console.log(dataVal);
		$("#iframeId").attr("src",dataVal);
		window.onresize=function(){  
		     changeFrameHeight();  
		}
	});
