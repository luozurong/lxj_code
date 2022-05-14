
var myScroll,pullDownEl, pullDownOffset,pullUpEl, pullUpOffset,generatedCount = 0,scrollAjax = false;
function loaded(loadedFunc,upFunc,downFunc) {
    pullDownEl = document.getElementById('pullDown');
    pullDownOffset = pullDownEl.offsetHeight;
    pullUpEl = document.getElementById('pullUp');
    pullUpOffset = pullUpEl.offsetHeight;
    myScroll = new iScroll('wrapper', {
      /*  scrollY: false, 
        freeScroll: true ,
        scrollbars: true, 
        useTransition: true,*/
        topOffset: pullDownOffset,
       /* bounceEasing: "quadratic",*/
        vScroll:true,
        bounce:true,
        ignoreBoundaries: false,
        //freeScroll:true,
        //bindToWrapper:true,
        checkDOMChanges:true,
        //onBeforeScrollStart: null,
        onRefresh: function () {
            if (pullDownEl.className.match('loading')) {
                if(scrollAjax){
                     scrollAjax = false;
                     pullDownEl.className = '';
                     pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉刷新';
                }
            } else if (pullUpEl.className.match('loading')) {
                if(scrollAjax){
                    scrollAjax = false;
                    pullUpEl.className = '';
                    document.querySelector('.pullUpImg').style.display = "none";
                    pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多';  
                }
            }
        },
        onScrollMove: function () {
            if((this.y < this.maxScrollY) && (this.pointY < 1)) {
                this.scrollTo(0, this.maxScrollY, 400);
                return;
            } else if(this.y > 0 && (this.pointY > window.innerHeight - 1)) {
                this.scrollTo(0, 0, 400);
                return;
            }else if (this.y > 5 && !pullDownEl.className.match('flip')) {
                pullDownEl.className = 'flip';
                pullDownEl.querySelector('.pullDownLabel').innerHTML = '释放刷新';
               // this.minScrollY = 0;
            } else if (this.y < 5 && pullDownEl.className.match('flip')) {
               pullDownEl.className = '';
               pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Pull down to refresh...';
               // this.minScrollY = -pullDownOffset;
            } else if (this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')&& this.maxScrollY < -5) {
                pullUpEl.className = 'flip';
                pullUpEl.querySelector('.pullUpLabel').innerHTML = '释放刷新';
                document.querySelector('.pullUpImg').style.display = "none";
                //this.maxScrollY = this.maxScrollY;
            } else if (this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
                pullUpEl.className = '';
                document.querySelector('.pullUpImg').style.display = "none";
                pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载更多';
                //sthis.maxScrollY = pullUpOffset;
            }
        },
        onScrollEnd: function () {
            scrollAjax = false;
            if (pullDownEl.className.match('flip')) {
                pullDownEl.className = 'loading';
                pullDownEl.querySelector('.pullDownLabel').innerHTML = '加载中···';
                pullDownAction(downFunc);

            } else if (pullUpEl.className.match('flip')) {
                pullUpEl.className = 'loading';
                document.querySelector('.pullUpImg').style.display = "block";
                pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载中···';
                pullUpAction(upFunc);
            }
            if(this.y < (this.maxScrollY - 5)){
                return;
            }else if(this.y > (this.maxScrollY + 5)){
                return;
            }
        }
    });
    loadAction(loadedFunc);
}
document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);//阻止冒泡
//document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 11); }, false);

//初始状态，加载数据
function loadAction(loadedFuncs){
    setTimeout(function () {
       loadedFuncs();
     //   myScroll.refresh();
    }, 1);
}

//下拉刷新当前数据
function pullDownAction (downFunc) {
    setTimeout(function () {
        downFunc();
      //  myScroll.refresh();
    }, 1);
}

//上拉加载更多数据
function pullUpAction (upFunc) {
    setTimeout(function () {
        upFunc();
       // myScroll.refresh();
    }, 1);
}
