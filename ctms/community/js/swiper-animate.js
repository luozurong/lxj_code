var onceSwiperColor = true;
var swiperScrollFlag = true;
var navSwiper;
var barwidth = 0; //导航粉色条的长度px
var tSpeed = 10;
var screenWidthVs = window.screen.width/414;
var activeIndex = 0;
function initSwipers(num){
  if(swiperScrollFlag){
    lineColor(0);
    swiperScrollFlag = false;
    navSwiper = new Swiper('#nav', {
      slidesPerView: 'auto',
      freeMode: true,
      on: {
        init: function() {
          navSlideWidth = this.slides.eq(0).css('width'); //导航字数需要统一,每个导航宽度一致
          bar = this.$el.find('.bar')
          clientWidth = this.$el.find('.swiper-slide').eq(0)[0].clientWidth;
          var navDiv = document.getElementsByClassName("swiper-slide")[0].clientWidth;  
          document.getElementsByClassName("bar")[0].style.width = navDiv + "px";
          bar.transition(tSpeed)
          navSum = this.slides[this.slides.length - 1].offsetLeft //最后一个slide的位置
          clientWidth = parseInt(this.$wrapperEl.css('width')) //Nav的可视宽度
          navWidth = 0
          for (i = 0; i < this.slides.length; i++) {
            navWidth += parseInt(this.slides.eq(i).css('width'))
          }
          topBar = this.$el.parents('body').find('#top') //页头
        },
      },
    });
  }

  //点击导航栏
  navSwiper.on('tap', function(e) {
    clickIndex = this.clickedIndex
    clickSlide = this.slides.eq(clickIndex)
    pageSwiper.slideTo(clickIndex, 0);
    this.slides.find('span').css('color', 'rgba(153,153,153,1)');
    clickSlide.find('span').css('color', 'rgba(51,51,51,1)');
  });

  //列表页面
  var pageSwiper = new Swiper('#page', {
    watchSlidesProgress: true,
    resistanceRatio: 0,
    on: {
      transitionStart: function() {
        activeIndex = this.activeIndex;
        var swiperOne = document.getElementsByClassName("swiper-wrapper1")[0];
        var navActiveSlideLeft = swiperOne.getElementsByClassName("swiper-slide")[this.activeIndex].offsetLeft;

        if (navActiveSlideLeft < (clientWidth - parseInt(navSlideWidth)) / 2) { //滚动居中
          navSwiper.setTranslate(0)
        } else if (navActiveSlideLeft > navWidth - (parseInt(navSlideWidth) + clientWidth) / 2) {
          navSwiper.setTranslate(clientWidth - navWidth)
        } else {
          navSwiper.setTranslate((clientWidth - parseInt(navSlideWidth)) / 2 - navActiveSlideLeft)
        }
        page.indexFlag = activeIndex;
      }
    }
  });
}

//上拉滚动
function scrollSwiper(){
  activeIndex = page.activeIndex
  var navDiv = document.getElementsByClassName("swiper-slide")[page.activeIndex].clientWidth;
  document.getElementsByClassName("bar")[0].style.width = navDiv + "px";
  if(page.onceFlag){
    page.onceFlag = false;
    var scrollSwiper = new Swiper('.scroll', {
      slidesOffsetBefore: 0,
      direction: 'vertical',
      freeMode: true,
      slidesPerView: 'auto',
      observer: true,
      observeParents: true,
      mousewheel: {
        releaseOnEdges: true,
      },
      on: {
        transitionEnd: function(){
          //下拉到底部，回调操作
          if(this.height - this.slidesSizesGrid >= this.translate && this.slidesSizesGrid >= this.height){
            page.infoDetailAjax(page.activeIndex);
          }
        }
      }
    })
    return scrollSwiper;
  }
}

function lineColor(num){
  var swiperOne = document.getElementsByClassName("swiper-wrapper1")[0];
  var navDiv = swiperOne.getElementsByClassName("swiper-slide")[num].clientWidth; //滚动条长度
  document.getElementsByClassName("bar")[0].style.width = navDiv + "px"; 

  var leftLong = swiperOne.getElementsByClassName("swiper-slide")[num].offsetLeft //滚动条滚动位置
  document.getElementsByClassName("bar")[0].style.transform = 'translateX(' + leftLong + 'px)'
  document.getElementsByClassName("bar")[0].style.webkitTransform = 'translateX(' + leftLong + 'px)'

  var spanSwiperColor = swiperOne.getElementsByClassName("swiper-span"); //文字颜色
  for(var i = 0; i < spanSwiperColor.length; i++){
    if(num == i){
      swiperOne.getElementsByClassName("swiper-span")[i].style.color = 'rgba(51,51,51,1)';
      swiperOne.getElementsByClassName("swiper-div")[i].style.display = 'block';
       swiperOne.getElementsByClassName("swiper-div")[i].style.width = spanSwiperColor[i].clientWidth + "px";
    }else{
      swiperOne.getElementsByClassName("swiper-span")[i].style.color = 'rgba(153,153,153,1)';
       swiperOne.getElementsByClassName("swiper-div")[i].style.display = 'none';
    }
  } 
}
