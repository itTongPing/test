(function ($) {
    var _showLoading = function(text) {
        $poploading = $('.pop-loading');
        if ($poploading.length == 0) {
            var $poploading = $("<div class='pop-loading'><div class='pop-loading-cnt'><div class='pop-loading-icon'></div><div class='pop-loading-text'></div></div></div>");
            $('body').prepend($poploading);
        }
        if ($poploading.css('display') == 'flex') return;
        $poploading.css('display', 'flex').find('.pop-loading-text').text(text || '正在加载中');
    };
    var _hideLoading = function() {
        $('.pop-loading').css('display', 'none');
    };

    var _showDialog = function(options) {
        var config = {
            title: '提示',
            content: '',
            className: '',
            buttons: [{
                label: '确定',
                type: 'confirm',
                callback: function() {
                    _hideDialog();
                }
            }]
        };
        var $popdialog = $('.pop-dialog');
        if ($popdialog.length == 0) {
            $popdialog = $(`<div class='pop-dialog'>
											<div class='pop-dialog-wrapper'>
												<div class='pop-dialog-header'>
													<span class='pop-dialog-title'>{title}</span>
												</div>
												<div class='pop-dialog-content'>
													{content}
												</div>
												<div class='pop-dialog-footer'></div>
											</div>
										</div>`);
            $('body').prepend($popdialog);
        }
        // else{
        //     $popdialog.removeClass('hide');
        // }
        $.extend(config, options);

        $.each(config, function(k, v) {
            switch (k) {
                case 'title':
                    var $popheader = $popdialog.find('.pop-dialog-header');
                    if ($.isEmptyObject(v)) {
                        $popheader.remove();
                    } else {
                        if ($popheader.length == 0) {
                            $popheader = $(`<div class='pop-dialog-header'>
															<span class='pop-dialog-title'>{title}</span>
														</div>`);
                            $popheader.prependTo($popdialog.find('.pop-dialog-wrapper'));
                        }
                        $popdialog.find('.pop-dialog-title').html(v);
                    }
                    break;
                case 'content':
                    var $popcontent = $popdialog.find('.pop-dialog-content');
                    if ($.isEmptyObject(v)) {
                        $popcontent.remove();
                    } else {
                        if ($popcontent.length == 0) {
                            $popcontent = $(`<div class='pop-dialog-content'>
															{content}
														</div>`);
                            $popcontent.insertBefore($popdialog.find('.pop-dialog-footer'));
                        }
                        $popcontent.html(v);
                    }
                    break;
                case 'className':
                    $popdialog.addClass(v);
                    break;
                case 'buttons':
                    var tap = window.ontouchstart ? 'touchstart' : 'click';
                    var $popfooter = $popdialog.find('.pop-dialog-footer');
                    $popfooter.empty();
                    $.each(v, function(i, btn) {
                        $(`<a href='javascript:;' class='pop-dialog-btn'>{label}</a>`)
                            .text(btn.label)
                            .attr('data-index', i)
                            .addClass($.isEmptyObject(btn.type) ? '' : 'pop-dialog-btn_' + btn.type)
                            .off(tap)
                            .on(tap, typeof btn.callback == 'function' ? btn.callback : $.noop)
                            .appendTo($popfooter);
                    });
                    break;
            }
        });
    };
    var _hideDialog = function() {
        var $popdialog = $('.pop-dialog');
        if ($popdialog){    //&& $popdialog.hasClass('hide')
            $popdialog.remove();
            // return;
        }
        // $popdialog.addClass('hide');
    };

    var _justTools = function(elem, options){
        this.elem = elem;
        this.set = options;
        //this.obj = null;
    };
    _justTools.prototype = {
        addAnimation: function(){
            switch(this.set.animation){
                case 'none':
                    break;
                case 'fadeIn':
                    this.obj.addClass('animated fadeIn');
                    break;
                case 'flipIn':
                    switch(this.set.gravity){
                        case 'top':
                            this.obj.addClass('animated flipInTop');
                            break;
                        case 'bottom':
                            this.obj.addClass('animated flipInBottom');
                            break;
                        case 'left':
                            this.obj.addClass('animated flipInLeft');
                            break;
                        case 'right':
                            this.obj.addClass('animated flipInRight');
                            break;
                    }
                    break;
                case 'moveInLeft':
                    this.obj.addClass('animated moveLeft');
                    break;
                case 'moveInTop':
                    this.obj.addClass('animated moveTop');
                    break;
                case 'moveInBottom':
                    this.obj.addClass('animated moveBottom');
                    break;
                case 'moveInRight':
                    this.obj.addClass('animated moveRight');
                    break;
            }
        },
        close:function(){
            this.obj.remove();
            this.elem.removeAttr("just-open");
        },
        setPosition:function(){
            var setPos = {};
            var pos = { x: this.elem.offset().left, y: this.elem.offset().top };
            var wh = { w: this.elem.outerWidth(), h: this.elem.outerHeight() };
            var rightTmp = ( pos.x + wh.w / 2 ) + this.obj.outerWidth() / 2 ;
            var leftTmp = ( pos.x + wh.w / 2 ) - this.obj.outerWidth() / 2 ;
            //console.log(leftTmp)
            switch(this.set.gravity){
                case 'top':
                    if(rightTmp > $(window).width() ){
                        setPos = {
                            x: pos.x + wh.w - this.obj.outerWidth(),
                            y: pos.y - this.obj.outerHeight() - this.set.distance
                        };
                        this.obj.find(".just-" + this.set.gravity).css("left", this.obj.outerWidth() - wh.w/2 + "px")
                    }else if( leftTmp < 0 ){
                        setPos = {
                            x: pos.x,
                            y: pos.y - this.obj.outerHeight() - this.set.distance
                        };
                        this.obj.find(".just-" + this.set.gravity).css("left", wh.w/2 + "px")
                    }else{
                        setPos = {
                            x: pos.x - (this.obj.outerWidth() - wh.w)/2,
                            y: pos.y - this.obj.outerHeight() - this.set.distance
                        };
                    }
                    break;
                case 'bottom':
                    if(rightTmp > $(window).width() ){
                        setPos = {
                            x: pos.x + wh.w - this.obj.outerWidth(),
                            y: pos.y + wh.h + this.set.distance
                        };
                        this.obj.find(".just-" + this.set.gravity).css("left", this.obj.outerWidth() - wh.w/2 + "px")
                    }else if( leftTmp < 0 ){
                        setPos = {
                            x: pos.x,
                            y: pos.y + wh.h + this.set.distance
                        };
                        this.obj.find(".just-" + this.set.gravity).css("left", wh.w/2 + "px")
                    }else{
                        setPos = {
                            x: pos.x - (this.obj.outerWidth() - wh.w)/2,
                            y: pos.y + wh.h + this.set.distance
                        };
                    }
                    break;
                case 'left':
                    setPos = {
                        x: pos.x - this.obj.outerWidth() - this.set.distance,
                        y: pos.y - (this.obj.outerHeight() - wh.h)/2
                    };
                    break;
                case 'right':
                    setPos = {
                        x: pos.x + wh.w + this.set.distance,
                        y: pos.y - (this.obj.outerHeight() - wh.h)/2
                    };
                    break;
            }
            this.obj.css({"left": setPos.x + "px", "top": setPos.y + "px"});
        },
        setEvent:function(){
            var that = this;
            if(that.set.events =="click" || that.set.events =="onclick"){
                that.obj.one("click", function(e){
                    console.log(1)
                    e.preventDefault();
                    e.stopPropagation();
                })
            }
            if(that.set.events =="mouseover" || that.set.events =="onmouseover" || that.set.events =="mouseenter"){
                this.elem.one("mouseout, mouseleave",function(){
                    console.log(2);
                    that.close();
                }).one("click", function(e){
                    console.log(3);
                    e.preventDefault();
                    e.stopPropagation();
                })
            }
        },
        setConfirmEvents:function(){
            var that = this;
            var yes = this.obj.find(".just-yes");
            var no = this.obj.find(".just-no");
            yes.click(function(){
                if(that.set.onYes(that)==true){
                    that.close();
                };
            })
            no.click(function(){
                that.close();
                that.set.onNo(that);
            })
        },
        addConfirm:function(){
            this.obj.append("<div class='just-confirm'><button type='button' class='just-yes'>"
                + this.set.yes +"</button><button type='button' class='just-no'>" + this.set.no +"</button></div>");
            this.setConfirmEvents();
        },
        setContent:function(){
            this.obj = $("<div class='just-tooltip " + this.set.theme + "'" +
                "style='width:" + this.set.width + "'><div class='just-con'>" + this.set.contents + "</div>" + "<span class='just-" + this.set.gravity + "'></span></div>");
            if(this.set.confirm==true){
                this.addConfirm();
            }
            $("body").append(this.obj);
            this.setEvent();
            this.addAnimation();

        },
        getEvent:function(){
            if(window.event){return window.event};
            var func=this.getEvent.caller;
            while(func!=null){
                var arg0 = func.arguments[0];
                if(arg0){
                    console.log(arg0.constructor)
                    if((arg0.constructor==Event || arg0.constructor==MouseEvent)
                        || (typeof(arg0)=="object" && arg0.preventDefault && arg0.stopPropagation)){
                        return arg0;
                    }
                }
                func = func.caller;
            }
            //$._data(this.elem.get(0)).events.click[0].events
            return null;
        },
        destroy:function(){
            $("div[just-open]").removeAttr("just-open");
            $(".just-tooltip").remove();
        },
        init:function(){
            var that = this;
            var e = that.getEvent();
            that.set.events = e.type;
            e.preventDefault();
            e.stopPropagation();
            $document = $(document);
            if(that.set.events =="click" || that.set.events =="onclick"){
                that.destroy();
                $document.one("click", function(e){
                    that.destroy();
                });
            }
            that.setContent();
            that.setPosition();

            $document.one("resize", function(){
                that.setPosition();
            })
        }
    };
    var _justToolsTip = function(options){
        if(this.attr("just-open")){
            return;
        }
        this.attr("just-open", "show");
        var defaults = {
            id:new Date().getTime(),
            height:"auto",
            width:"auto",
            contents:'',
            gravity: 'top',  //top, left, bottom, right
            theme: '',//className
            distance:10,
            animation: 'none', //none, fadeIn, flipIn, moveInLeft, moveInTop, moveInBottom, moveInRight
            confirm: false,
            yes: '确定',
            no: '取消',
            onYes: function(){}, //返回ture，关闭tools
            onNo: function(){}
        }
        this.each(function(i){
            options = $.extend(defaults, options);
            var tooltip = new _justTools($(this), options);
            tooltip.init();
        });
    };

    //计算两个日期天数差的函数
    var _dateDiff = function (sDate1, sDate2) {  //sDate1和sDate2是yyyy-MM-dd格式
        if(sDate1 == '' || sDate2 == '' || sDate1 == null || sDate2 == null){
            return '-';
        }
        var aDate, oDate1, oDate2, iDays;
        aDate = sDate1.split("-");
        oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);  //转换为yyyy-MM-dd格式
        aDate = sDate2.split("-");
        oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);
        iDays = (oDate2 - oDate1) / (1000 * 60 * 60 * 24); //把相差的毫秒数转换为天数

        return iDays;  //返回相差天数
    };
    //计算两个日期天数差的函数（除去中间的周末）
    var _dateDiffNoWeek = function (sDate1, sDate2) {  //sDate1和sDate2要是yyyy-MM-dd格式
        if(sDate1 == '' || sDate2 == '' || sDate1 == null || sDate2 == null){
            return '-';
        }
        var aDate, oDate1, oDate2,weekEnds;
        aDate = sDate1.split("-");
        oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);
        aDate = sDate2.split("-");
        oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);
        delta = (oDate2 - oDate1) / (1000 * 60 * 60 * 24);

        weekEnds = 0;
        for(i = 0; i < delta; i++)
        {
            if(oDate1.getDay() == 0 || oDate1.getDay() == 6) weekEnds ++;
            oDate1 = oDate1.valueOf();
            oDate1 += 1000 * 60 * 60 * 24;
            oDate1 = new Date(oDate1);
        }
        return delta - weekEnds;
    };

    $.fn.justToolsTip = function(options){
        if(this.attr("just-open")){
            return;
        }
        this.attr("just-open", "show");
        var defaults = {
            id:new Date().getTime(),
            height:"auto",
            width:"auto",
            contents:'',
            gravity: 'top',  //top, left, bottom, right
            theme: '',//className
            distance:10,
            animation: 'none', //none, fadeIn, flipIn, moveInLeft, moveInTop, moveInBottom, moveInRight
            confirm: false,
            yes: '确定',
            no: '取消',
            onYes: function(){}, //返回ture，关闭tools
            onNo: function(){}
        }
        this.each(function(i){
            options = $.extend(defaults, options);
            var tooltip = new _justTools($(this), options);
            tooltip.init();
        });
    };
    $.extend(true, $,{
        showLoading: _showLoading,
        hideLoading: _hideLoading,
        showDialog: _showDialog,
        hideDialog: _hideDialog,
        dateDiff: _dateDiff,
        dateDiffNoWeek: _dateDiffNoWeek
    });
})(jQuery);
