// customer start

// lm
var lm_height;
var lm_speed = 0;

function gnb($d1n, $d2m, $d3m){
	var $id = "";

	$("#snb").children().filter("[class^=snb]").each(function(){
		$id = $(this).attr("class").replace("snb", "");

		if($d1n == $id){
			$(".snb"+$d1n).addClass("active");
			if($d2m)	$(".snb"+$d1n+">ul>li").eq(parseInt($d2m) - 1).addClass("active");
			if($d3m)	$(".snbb"+$d2m+">ul>li").eq(parseInt($d3m) - 1).addClass("active");
		}
	});

	$("#snb > li > a").click(function(){
		$(this).parent().addClass("active").siblings().removeClass("active");
	});

	if($(".snb"+$d1n+">ul").length > 0){
		$(".snb"+$d1n+">ul").animate(
			{
				height:$(".snb"+$d1n+">ul")},lm_speed,
				function(){
					$(".snb"+$d1n+">ul").slideDown("fast");
				}
		);
		$(".mdepth2").parent().addClass('hasSub');
	}
	if($(".snbb"+$d2m+">ul").length > 0){
		$(".snbb"+$d2m+">ul").animate(
			{
				height:$(".snbb"+$d2m+">ul")},lm_speed,
				function(){
					$(".snbb"+$d2m+">ul").slideDown("fast");
				}
		);
	}

}

$(document).on("click", "#snb > li > a", mLst01);
$(document).on("click", "#snb > li > ul.mdepth2 > li > a", mLst02);

function mLst01(e){
	$thisp = $(this).parent();

	var chk = false;
	$("ul.mdepth1 > li").removeClass("active");

	var dropDown = $(this).next(".mdepth2");
	$(".mdepth2").not(dropDown).slideUp("fast");
	dropDown.stop(false, true).slideToggle("fast", function(){
		if($(this).is(":hidden")){
			$thisp.removeClass("active");
			chk = false;
		}else{
			$thisp.addClass("active");
			chk = true;
		}
	});

	if(!chk){
		$(this).parent().find(".mdepth3").each(function(idx){
			if($(this).css("display") == "block"){
				$(this).parent().removeClass("active");
				$(this).parent().find(".mdepth3").hide();
			}
		});
	}
}

function mLst02(e){
	if($(this).next(".mdepth3").find("li:eq(0)").length > 0){
		e.preventDefault();
	}

	$thisp = $(this).parent();

	$(".mdepth2 > li").removeClass("active");

	var dropDown = $(this).next(".mdepth3");
	//$(".mdepth3").not(dropDown).slideUp("fast");
	dropDown.stop(false, true).slideToggle("fast", function(){
		if($(this).is(":hidden")){
			$thisp.removeClass("active");
		}else{
			$thisp.addClass("active");
		}
	});
}


$(function() {
/*
    $( ".datepicker" ).datepicker({
        prevText: '?????????',
        nextText: '?????????',
        currentText: '??????',
        monthNames: ['1???(JAN)','2???(FEB)','3???(MAR)','4???(APR)','5???(MAY)','6???(JUN)',
        '7???(JUL)','8???(AUG)','9???(SEP)','10???(OCT)','11???(NOV)','12???(DEC)'],
        monthNamesShort: ['1???','2???','3???','4???','5???','6???',
        '7???','8???','9???','10???','11???','12???'],
        dayNames: ['???','???','???','???','???','???','???'],
        dayNamesShort: ['???','???','???','???','???','???','???'],
		dayNamesMin: ['???','???','???','???','???','???','???'],
		showOn: "button",
		buttonText:"??????",
		buttonImageOnly: false,
		changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd'
	});
*/
	/* MonthPicker ?????? */
	options = {
		pattern: 'yyyy-mm', // Default is 'mm/yyyy' and separator char is not mandatory
		selectedYear: 2017,
		startYear: 2008,
		finalYear: 2018,
		monthNames: ['1???', '2???', '3???', '4???', '5???', '6???', '7???', '8???', '9???', '10???', '11???', '12???']
	};

	/* MonthPicker Set */
	/*
	$('.monthpicker').monthpicker(options);

	/* ?????? ????????? MonthPicker Show */
    $('.btn_monthpicker').bind('click', function () {
        $('.monthpicker').monthpicker('show');
    });
    /* MonthPicker ?????? ?????????
    $('#monthpicker').monthpicker().bind('monthpicker-click-month', function (e, month) {
        alert("???????????? ?????? : " + month + "???");
    });
	*/
	$(".famliy-link button").click(function () {
		$(this).next().slideToggle(200);
	});
	$(".famliy-link").each(function(){
		$(this).bind({
			"mouseleave":function(){
				$(".famliy-link > ul").slideUp(200);
			}
		});
	});

	$(".tit-area .btn-help").click(function () {
		$(this).next(".pop-help").slideToggle(200);
	});
	$(".tit-area .pop-help .bt-close").click(function () {
		$(this).parent().parent(".pop-help").slideToggle(200);
	});

	$(".btn-close").click(function () {
		$("#hideDv").slideToggle(200);
		$(this).toggleClass("active");
		$(this).find("span").toggle();
	});

});

function adjustScript() {
    var hei = $(window).height();
	var hei01 = $('#header').height();
	var hei02 = $('#footer').height();
	$("#container").css({height:hei-254});
}

$(function() {
    $(window).resize(function() {
        adjustScript();
    });
	adjustScript();
});

$(function() {

	// main slide
	mainSlide = new Swiper('.main-slide', {
		pagination: '.main-slide .pagination',
		pagination: {
			el: '.main-slide .pagination',
		},
		//paginationElement:'button',
        autoplay: 2500,
        //autoplayDisableOnInteraction: false
		loop:true,
		grabCursor: true,
		paginationClickable: true
	});

   var fileTarget = $('.filebox .upload-hidden');

    fileTarget.on('change', function(){
        if(window.FileReader){
            // ????????? ??????
            var filename = $(this)[0].files[0].name;
        }

        else {
            // Old IE ????????? ??????
            var filename = $(this).val().split('/').pop().split('\\').pop();
        };

        $(this).siblings('.upload-name').val(filename);
    });

	$('label.file').each(function () {
		var target = $(this);
		var target_input = $(this).find('input[type=file]');

		target.find('input[type=file]').on('change', function (event) {
				console.log($(this).val());
				var file_name = $(this).val();
				target.find('input[type=text]').val(file_name);
			});

		target.on('click', 'input[type=text], span', function () {
			target_input.click();
			return false;
		});
	})

	// 171027 ??????
	// ???????????? ?????? ??????
	var tabContainers = $('div.tab-bx .tc');
	tabContainers.hide().filter(':first').show();
	$('div.tab-bx .div-tab a').click(function () {
		tabContainers.hide();
		tabContainers.filter(this.hash).show();
		$('div.tab-bx .div-tab a').parent().removeClass('active');
		$(this).parent().addClass('active');
		return false;
	}).filter(':first').click();

	$('.n3-row .row ul li').click(function () {
		$(this).addClass('active').siblings().removeClass('active');
		return false;
	}).filter(':first').click();

	var tabCon = $('div.tab-wrap .tc');
	tabCon.hide().filter(':first').show();
	$('div.tab-wrap .div-tab a').click(function () {
		tabCon.hide();
		tabCon.filter(this.hash).show();
		$('div.tab-wrap .div-tab a').parent().removeClass('active');
		$(this).parent().addClass('active');
		return false;
	}).filter(':first').click();

	// faq
	$(".faq-wrap .tr-a").hide();
	$(".faq-wrap .tr-q").click(function(){
   		$(this).next(".tr-a").slideToggle(0)
    		.siblings(".tr-a:visible").slideUp(0)
    	$(this).toggleClass("active")
          	.siblings(".tr-q").removeClass("active")
   });

});