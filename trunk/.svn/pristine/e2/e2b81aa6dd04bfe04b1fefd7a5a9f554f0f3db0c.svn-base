		/*<![CDATA[*/


    
		// 作一个搜索的标识符
		var searchFlag = true;
		function footer() {
		    $("footer").addClass("hidden"); // 隐藏底部信息
		    var conH = $(".main-container").height(); // 容器的高度
		    var winH = $(window).height(); // 页面的高度
		    if (conH < winH) { // 容器的高度小于页面的高度的时候 说明没有滚动条 那么显示出底部信息
		        $("footer").removeClass("hidden").css('marginLeft', 0);
		        $(".fixed-table-pagination").css("bottom", "60px");
		    } else { // 当出现滚动条的时候 隐藏底部信息
		        $("footer").addClass("hidden");
		        $(".fixed-table-pagination").css("bottom", "0");
		    }
		    $('body').scroll();
		}
		
		function isTaxFormatter(value, row, index){
			
			if(value == 0){
				return '否';
			}else if(value == 1){
				return '是';
			}
			
		}
		
		var TableInit = function() {
		    var oTableInit = new Object();
		    oTableInit.init = function() {
		        $('#table').bootstrapTable({
		            url: '/report/warehouseAssessmentReport/search', // 请求后台的URL（*）
		            method: 'get', // 请求方式（*）
		            striped: true, // 是否显示行间隔色
		            cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		            pagination: true, // 是否显示分页（*）
		            singleSelect: false,
		            sortable: true, // 是否启用排序
		            // sortName:"timestamp", //按照订单时间排序
		            // sortOrder: "asc", //排序方式
		            sidePagination: "server", // 分页方式：client客户端分页，server服务端分页（*）
		            pageNumber: 1, // 初始化加载第一页，默认第一页
		            pageSize: 20, // 每页的记录行数（*）
		            pageList: [20, 50, 100, 200], // 可供选择的每页的行数（*）
		            clickToSelect: true, // 是否启用点击选中行
		            queryParams: oTableInit.queryParams, // 传递参数（*）
		            width: 'auto',
		            uniqueId: "id", // 每一行的唯一标识，一般为主键列
		            cardView: false,
		            contentType: "application/json",
		            queryParamsType: '',
					resizable : true, // 列是否可以拖动
					reorderableColumns : true, // 列是否可以重新排序
					showColumns : true, // 是否显示自定义列
					fixedWindow : true, // 表头浮动
		            undefinedText: "-", // 为undefined的时候显示的文本
		            responseHandler:responseHandler, //在渲染页面数据之前执行的方法，此配置很重要!!!!!!!
		            columns: [{
		            		field :'states',
		                    checkbox: true
		                },
		                {
		                    field: 'quality_control_id',
		                    title: '质检单号',
		                    align: 'center',
		                },
		                {
		                    field: 'sku_code',
		                    title: 'SKU',
		                    align: 'center',
		                    class: 'title_class'
		                },
		                {
		                    field: 'inspector_name',
		                    title: '质检员',
		                    align: 'center',
		                    
		                },
		                {
		                    field: 'create_date',
		                    title: '质检时间',
		                    align: 'center',
		                    width:'200px'
		                },
		                {
		                    field: 'inspector_result',
		                    title: '质检结果',
		                    align: 'center',
		                    width:150
		                },
		                {
		                    field: 'purchase_no',
		                    title: '采购订单号',
		                    align: 'center',
		                },
		                {
		                    field: 'sale_name',
		                    title: '采购员',
		                    align: 'center',
		                },
		                {
		                    field: 'leader_create_time',
		                    title: '采购订单审核时间',
		                    align: 'center',
		                },
		                {
		                    field: 'storage_number',
		                    title: '入库单号',
		                    align: 'center',
		                }, 
		                {
		                    field: 'storage_user_name',
		                    title: '入库员',
		                    align: 'center',
		                }, 
		                {
		                    field: 'storage_create_date',
		                    title: '入库时间',
		                    align: 'center',
		                },
		                {
		                    field: 'stock_name',
		                    title: '仓库',
		                    align: 'center',
		                }
		            ],
		            onLoadSuccess: function() {
						flyer.scrollBar($('#table'));
		                // searchFlag = false;
		                showUtil();
		                footer();
		            }

		        });
		    };

		    var recordSearch = null; // 给一个变量记录当点击翻页前的刷新或搜索回传的值

		       oTableInit.queryParams = function(params) {
		    	
		    	var signWarehouse = $("#warehouseId").val().join(',')||''; //仓库
		    	
		    	if(signWarehouse == ''){//如果是全部
		    		var wareHouses = [];
		    		$("#warehouseId").find('option').each(function(){
		    			if("" != $(this).val()){
		    				wareHouses.push($(this).val());
		    			}
		    		});
		    		params.signWarehouse = wareHouses.join(',');
		    	}else{//如果选定某个几个仓库
		    		params.signWarehouse = signWarehouse;
		    	}
		    	
		    	
		        if (searchFlag) {
					var queryNumber = $("#queryNumber").val()||'';//入库/采购/质检单号
		            var skuCode = $("#skuCode_s").val()||''; //sku名称
		            var createDateStart = $("#createDateStart").val()||'';//入库开始时间
		            var createDateEnd = $("#createDateEnd").val()||'';//入库结束时间
		            var inspector_name = $("#inspector_name").val()||'';//质检员
		            var inspector_result = $("#inspector_result").val()||'';//质检结果
		            var sale_name =$("#sale_name").val()||'';
		            var leaderCreateTimeStart = $("#leaderCreateTimeStart").val()||'';
		            var leaderCreateTimeEnd = $("#leaderCreateTimeEnd").val()||'';
		            var storageUserName = $("#storageUserName").val()||'';
		            
		            
		            var storageCreateDateStart = $("#storageCreateDateStart").val()||'';
		            var storageCreateDateEnd = $("#storageCreateDateEnd").val()||'';
		            
		            params.queryNumber = queryNumber;
		            params.skuCode = skuCode;
		            params.createDateStart = createDateStart;
		            params.createDateEnd = createDateEnd;
		            params.inspector_name = inspector_name;
		            params.inspector_result = inspector_result;
		            params.sale_name = sale_name;
		            params.leaderCreateTimeStart =leaderCreateTimeStart;
		            params.leaderCreateTimeEnd = leaderCreateTimeEnd;
		            params.storageUserName = storageUserName;
		            params.storageCreateDateStart = storageCreateDateStart;
		            params.storageCreateDateEnd = storageCreateDateEnd;
		            
		            params.limit = params.pageSize
		            return params;
		        } else {
		            recordSearch.pageNumber = params.pageNumber;
		            recordSearch.limit = params.pageSize;
		            return recordSearch; // 返回上一次刷新或搜索的值。
		        }
		        
		    }
		    return oTableInit;
		};

		function showUtil() {
		    $("td").each(function() {
		        if ($(this).html() == null || $(this).html() == "undefined" || $(this).html() == "N/A") {
		            $(this).html("-");
		        }
		    });
		}




		// var swh = $("#signWarehouse").find("option"); //得到全部的长度
		// var string = "";
		// for (var i = 1; i < swh.length - 1; i++) {
		// string += swh[i].value + ",";
		// }
		// string += swh[swh.length - 1].value;


		// 时间选项控制
		function form_datetime() {
		    var newDate = new Date();
		    var year = newDate.getFullYear();
		    var month = padleft0(newDate.getMonth() + 1);
		    var day = padleft0(newDate.getDate());

		    // 补齐两位数
		    function padleft0(obj) {
		        return obj.toString().replace(/^[0-9]{1}$/, "0" + obj);
		    }
		    $(".form_datetime").datetimepicker({
		        language: "zh-CN",
		        minView: "month",
		        autoclose: true
		    });
		    $('.form_datetime').datetimepicker('setEndDate', year + "-" + month + "-" + day);
		}

		// 日历插件配置方法
		$(".form_datetime").datetimepicker({
		    format: 'yyyy-mm-dd',
		    autoclose: true,
		    language: 'zh-CN',
		    minView: '2',
		});
		// 重置按钮
		$(".resetbutton").click(function() {
		    //$('.searchInput').val('');
			// 点击搜索按钮之后 清空高级搜索里面的数据 重置为默认值
			$('.search-form-div select').selectpicker('val','');
			$('#queryForm')[0].reset();// reset方法是javascript原声的 所以需要转换为js对象
		});

		function signtype(value) {
		    if (value == "0") {
		        return "签收";
		    } else if (value == "1") {
		        return "拒收";
		    } else {
		        return "-";
		    }
		}

		function signstatus(value) {
		    if (value == "0") {
		        return "正常";
		    } else if (value == "1") {
		        return "<span style='color:red'>异常</span>";
		    } else {
		        return "-";
		    }
		}

		// 点击搜索时查询
		function searchResult() {
		    searchFlag = true;
		    var data = $('#table').bootstrapTable("getData");
		    if (data && data.length == 0) {
		        $('#table').bootstrapTable('refresh');
		    } else {
		        $('#table').bootstrapTable('selectPage', 1);
		    }
		}

		// 查询参数
		// function query_params(params) {
		// var queryNumber = "";
		// var signWarehouse = "";
		// var createUser = "";
		// var createDateStart = "";
		// var createDateEnd = "";
		// if (searchFlag) {
		// queryNumber = $("#queryNumber").val();
		// signWarehouse = $("#signWarehouse").val();
		// createUser = $("#createUser").val();
		// createDateStart = $("#createDateStart").val();
		// createDateEnd = $("#createDateEnd").val();
		// }
		// var d = new Date();
		// var date = new Date((d.getFullYear() + "-" + (d.getMonth() + 1) + "-"
		// + d.getDate()).replace("-", "/").replace("-", "/"));
		// var createDateStart = $("#createDateStart").val();
		// if (createDateStart) {
		// var date2 = new Date(createDateStart.replace("-", "/").replace("-",
		// "/"));
		// if (date2 > date) {
		// $.jGrowl('开始时间应小于北京时间');
		// $("#createDateStart").val("")
		// return false;
		// } else {
		// params.createDateEnd = createDateEnd;
		// }
		// }
		// var createDateEnd = $("#createDateEnd").val();
		// if (createDateStart && createDateEnd) {
		// if (createDateEnd < createDateStart) {
		// $.jGrowl('结束时间应大于开始时间');
		// return false;
		// } else {
		// params.createDateEnd = createDateEnd;
		// }
		// }
		// if (isNotNull(queryNumber)) {
		// params.queryNumber = queryNumber;
		// }
		// if (isNotNull(createUser)) {
		// params.createUser = createUser;
		// }
		// if (isNotNull(signWarehouse)) {
		// params.signWarehouse = signWarehouse.join(",");
		// }
		// if (isNotNull(createDateStart)) {
		// params.createDateStart = createDateStart;
		// }
		// if (isNotNull(createDateEnd)) {
		// params.createDateEnd = createDateEnd;
		// }
		// return params;
		// }






		function checkTableLength() {
		    if ($("#table").bootstrapTable('getSelections').length　 > 1) {
		        $.jGrowl('只能操作单个签收单');
		        return true;
		    }
		    return false;
		}

		function hideUserChange() {
		    $('.user_change').hide();
		    if ($("#table").bootstrapTable('getSelections').length < 1) {
		        limitshow('至少选择一项！');
		        return false;
		    }
		    return true;
		}




		var map = {
		    qualityControlId: "qualityControlId",
		    skuCode: "skuCode",
		    skuImgUrl: "skuImgUrl",
		    qualifiedQuantity: "qualifiedQuantity",
		    rejectsQuantity: "rejectsQuantity",
		    purchaseOrder: "purchaseOrder",
		    storageLocationCode: "storageLocationCode",
		    locationCodeId: "locationCodeId",
		    inspector: "inspector",
		    warehouseId: "warehouseId",
		    purchaseId: "purchaseId",
		    type: "type",
		    rejectsBoxQuantity: "rejectsBoxQuantity",
		    qualifiedBoxQuantity: "qualifiedBoxQuantity",
		    cod: "cod",
		    supplierId: "supplierId"
		};




		// 初始化质检单号
		function initReceive(isqc) {
		    $("#qs_area-qs_text").hide();
		    var qcId = $('.searchInput').val();

		    Object.keys(map).forEach(function(key) {
		        var domObj = $("." + key)
		        if (domObj[0].tagName == "INPUT") {
		            domObj.val('');
		        } else if (domObj[0].tagName == "IMG") {
		            domObj.attr('src', '');
		        } else {
		            domObj.text('');
		        }
		    });

		    if (qcId.indexOf("IQC") >= 0) {
		        if (qcId.length != 14) {
		            $.jGrowl("请输入正确的质检单号！");
		            return false;
		        }
		    } else {
		        $.jGrowl("请输入正确的质检单号！");
		        return false;
		    }


		    $.ajax({
		        method: "GET",
		        url: "./getQcInfo?qcId=" + qcId,
		        contentType: "application/json; charset=utf-8",
		        beforeSend: function(request) {
		            if (isAllNotEmpty(header, token))
		                request.setRequestHeader(header, token);
		        },
		        success: function(result) {
		            if (result.message != "可以入库") {
		                $.jGrowl(result.message);
		                return false;
		            }else{
                        $(".form-control.input_text.storageLocationCode").attr("disabled", false);
					}
					// 移动焦点到库位编码位置
                    setTimeout(function () {
                        $(".storageLocationCode").select();
                        $(".storageLocationCode").focus();
                    },100)
		            result[map["type"]] = isqc;
		            if (result) {
		                Object.keys(map).forEach(function(key) {
		                    var domObj = $("." + key)
		                    if (domObj[0].tagName == "INPUT") {
       		                        domObj.val(result[map[key]]);
		                    } else if (domObj[0].tagName == "IMG") {
		                    	if(result.skuImgUrl){
	                                var isNull=result.skuImgUrl.substr((result.skuImgUrl.lastIndexOf('/'))+1);
									if(isNull=='null'){
									  domObj.attr('src', '/images/detail.png');	
									}else{
	                                   domObj.attr('src', result[map[key]]); 
									}   
			                    }
		                    } else {
		                    	// 如果是采购订单号的话 需要加上超链接
		                    	if (key==='purchaseOrder') {
									domObj.html('<a href="'+window.urlconfig.chain+'/demand/order2?orderid='+result[map[key]]+'&microname=采购'+'" target="_blank">'+result[map[key]]+'</a>');
								}else {
       		                        domObj.text(result[map[key]]);
								}
		                    }
		                });
		            } else {
		                $.jGrowl("未找到该质检单号");
		            }
		            if(isqc == 0){					    
						$(".form-control.input_text.storageLocationCode").val("");
						$("#warehouseCode").hide();
		            }else{
					    $("#warehouseCode").show();
		            }

		            if(result.storageLocationCode && result.locationCodeId){
						$(".form-control.input_text.storageLocationCode").attr("disabled", true);
						$(".hasLocator").val("true");
					}else{
	                    $(".form-control.input_text.storageLocationCode").val('').attr("disabled", false);	
					}
		        }
		    });
		}

		//高级搜索
		function searchH(){
			$("#searchH").toggle();
		}

		$(function(){
			   $('.selectpicker').selectpicker({noneSelectedText: ''});
			   /*<![CDATA[*/
			   //多选框功能（初始化值为-1）
			   $('.selectpicker').on('changed.bs.select', function (e, clickedIndex) {
			    platformChanged = true;
			    var selects = $(this).selectpicker('val');
			    var total = $(this).find('option').length;
			    selects = selects == null ? [] : selects;
			    if (selects.length == 0 || selects.length == total)
			     $(this).selectpicker('val', "");
			    else {
			     if (clickedIndex == 0) {
			      $(this).selectpicker('deselectAll');
			      $(this).selectpicker('val', "");
			     } else if (clickedIndex > 0) {
			      if (selects.indexOf("-1") == 0)
			       $(this).selectpicker('val', selects.splice(1, selects.length));
			      else if (selects.indexOf("") == -1 && selects.length == total - 1) {
			       $(this).selectpicker('val', "");
			      }
			     }
			    }
			   });
			   /*]]>*/
			  });

		// 点击搜索时执行
		function searchCheckInfo() {
		    $(".rejectsQuantityDiv").show();
		    $(".qualifiedQuantityDiv").show();
           
		    initReceive();
            
		}




		function starFormatter(value, row, index) {
		    if (row.isStarSign == 0) {
		        return "<a class='like' title='未标记' ><img src='/images/tag.png'></a>"
		    } else if (row.isStarSign == 1) {
		        return "<a class='like' title='标记' ><img src='/images/tag_selected.png'></a>"
		    }
		}

		function starSign(typeId, isStarSign) {
		    // 如果当前状态是未标记 则进行标记操作 否则进行取消标记操作
		    $.ajax({
		        url: "./starSign",
		        type: "get",
		        beforeSend: function(request) {
		            request.setRequestHeader(header, token);
		        },
		        data: {
		            "typeId": typeId,
		            "isStarSign": isStarSign
		        },
		        success: function(data) {
		            if (data == 'Y') {
		                $.jGrowl('标记成功！');
		                // 并把当前需求单标记颜色改为金色
		                $('#table').bootstrapTable('refresh');
		                return;
		            } else if (data == 'N') {
		                $.jGrowl('取消标记成功！');
		                // 并把当前需求单标记颜色改为金色
		                $('#table').bootstrapTable('refresh');
		                return;
		            } else {
		                $.jGrowl("入库单标记失败：" + data.message);
		                // 颜色不变
		                return;
		            }
		        }
		    });
		}

		function saveStorage(obj) {
			
			var checkValue = false;
			$('.storageLocationCodeContainer').find("li").each(function(){
				if($(this).text() == $('.storageLocationCode').val()){
					checkValue = true;
					$(".locationCodeId").attr("id",$(this).attr("id"));
					return false
				}
			})
			if($(".hasLocator").val() == 'true'){
				checkValue = true;
			}
			if(obj.type == '0'){
				checkValue = true;
			}
			if(!checkValue){
				$.jGrowl("库位编码不正确");
			    $('.ruku_button').removeAttr("style");
			}else{
				$.ajax({
			        method: "POST",
			        url: "./saveStorage",
			        data: JSON.stringify(obj),
			        contentType: "application/json; charset=utf-8",
			        beforeSend: function(request) {
			            if (isAllNotEmpty(header, token))
			                request.setRequestHeader(header, token);
			        },
			        success: function(result) {
			            /*
						 * if(result.success){ $.jGrowl("入库成功"); }else{
						 * $.jGrowl(result.message); }
						 */
	
			            if (result.exceptionMessage) {
			                $("#qs_area-qs_text").text(result.exceptionMessage).show();
			            } else {
			                $.jGrowl("入库成功");
			            }
	
			            Object.keys(map).forEach(function(key) {
			                var domObj = $("." + key)
			                if (domObj[0].tagName == "INPUT") {
			                    domObj.val('');
			                } else if (domObj[0].tagName == "IMG") {
			                    domObj.attr('src', '');
			                } else {
			                    domObj.text('');
			                }
			            });
			        }
			    });
			    $('.ruku_button').removeAttr("style");
			    $("#qualityControlInput").select().val('');															
			}
		}

		/*
		 * function doExport(selector, params) { var options = { //ignoreRow:
		 * [1,11,12,-2], ignoreColumn: [0,11], //pdfmake: {enabled: true},
		 * tableName: 'Countries', worksheetName: 'Sign' }; $.extend(true,
		 * options, params); $(selector).tableExport(options); }
		 */


		$(function() {
		    $('#updateStorage').click(function() {
		        if (hideUserChange()) {
		            if (checkTableLength()) {
		                return false;
		            }
		            var obj = $("#table").bootstrapTable('getSelections')[0];
		            $("#qualityInspectionNumber").val(obj.qualityInspectionNumber);
		            formEditOrview.action = _ctx + "warehouse/updateStorage";
		            formEditOrview.submit();
		        }
		    });
		    // 加载页面的时候先把库位编码input设置为只读。
		    $(".form-control.input_text.storageLocationCode").attr("disabled", true);
		});

		$(function() {

		    $('#updateWarehouse').click(function() {
		        if ($('#qualityInspectionNumber').val() == '') {
		            $.jGrowl("请输入质检单号");
		            return false;
		        }
		        if ($('#storageLocationCode').val() == '') {
		            $.jGrowl("请输入库位编码");
		            return false;
		        }
		        $.ajax({
		            type: "post",
		            url: _ctx + 'warehouse/updateStorages',
		            beforeSend: function(request) {
		                request.setRequestHeader(header, token);
		            },
		            data: {
		                "qualityInspectionNumber": $('#qualityInspectionNumber').val(),
		                "nondefectiveNumber": $('#nondefectiveNumber').val(),
		                "rejectsNumber": $('#rejectsNumber').val(),
		                "storageLocationCode": $('#storageLocationCode').val()
		            },
		            success: function(data) {
		                $("#signCode").val(data.signCode);
		                if (pram == "print") {
		                    printPage();
		                }
		            }
		        });

		        var storage = {};
		        storage.qualityInspectionNumber = $('#qualityInspectionNumber').text();
		        storage.nondefectiveNumber = $('#nondefectiveNumber').val();
		        storage.rejectsNumber = $('#rejectsNumber').val();
		        // storage.purchaseNumber = $('.purchaseOrder').text();
		        storage.storageLocationCode = $('#storageLocationCode').val();
		        Object.keys(map).forEach(function(key) {
		            var domObj = $("." + key);
		            if (domObj[0].tagName == "INPUT") {
		                storage[key] = domObj.val();
		            } else {
		                storage[key] = domObj.text();
		            }
		        });
		        saveStorage(storage);
		    });
		});


		function hideUserChange() {
		    if ($("#table").bootstrapTable('getSelections').length < 1) {
		        limitshow('至少选择一项！');
		        return false;
		    }
		    return true;
		}

		function checkTableLength() {
		    if ($("#table").bootstrapTable('getSelections').length　 > 1) {
		        $.jGrowl('只能操作单个入库单！');
		        return true;
		    }
		    return false;
		}

		// 重新设计的弹出框
		function limitshow(txt) {
		    // 初始化
		    $('.box').css({ display: 'none' });
		    $('.fixed-table-container').css('margin-top', '0px');
		    clearTimeout(timeout);
		    $('.box').css({ display: 'inline-block', left: '48px', top: '44px' })
		    $('.fixed-table-container').css('margin-top', '-26px');
		    $('.box').html(txt);
		    // 10s之后自动消失
		    timeout = setTimeout(function() {
		        $('.box').css({ display: 'none' });
		        $('.fixed-table-container').css('margin-top', '0px');
		    }, 10000);
		    // 判断是否有点击事件
		    timer = setInterval(function() {
		        if ($('.selected[data-uniqueid]').length != 0) {
		            $('.box').css({ display: 'none' });
		            $('.fixed-table-container').css('margin-top', '0px');
		            clearInterval(timer);
		        }
		    }, 100)
		}
		$(function() {
		    $("#qualityControlInput").focus(); // 初始聚焦的过程
		    $('#addStorage').click(function() {
		    	window.open('./warehouse_add');// 打开新窗口
// window.location.href = "./warehouse_add";
		    });
		    /* 苏荣 2017-5-13 库位编码检索方法 开始 */
		    $('.storageLocationCode').focus(focusAndKeyup);
		    $('.storageLocationCode').keyup(focusAndKeyup);
		    /* 库位编码检索方法 结束 */

		    // 这里是搜索框输入完毕，当按下enter时触发的事件。
		    
		    
		    var isZh = false;
		    $("#qualityControlInput").off().on({
		        keydown: function (e) {
			        if (e.keyCode == 13 || window.event.keyCode == 13) {
	                     
			            var inputStr = $("#qualityControlInput").val().split(",");
	                                    
	                    // var str=$("#qualityControlInput").val();
						// str=str.replace(/，/ig,',');
						if(inputStr.length==1){
					      $("#qualityControlInput").val(inputStr[0]);		
	                      searchCheckInfo();					  
						  return false;
						}else{						
	                    $("#qualityControlInput").val(inputStr[1]);
						}
	                
			            if (inputStr[2] == "1") {
			                $(".rejectsQuantityDiv").show();
			                $(".qualifiedQuantityDiv").show();
			            } else if (inputStr[2] == "0") {
			                $(".rejectsQuantityDiv").show();
			                $(".qualifiedQuantityDiv").hide();
			            }
			            
			            initReceive(inputStr[2]);
			            $("#qualityControlInput").select();
			        }
		        },
		        oevent: function (e) {

		        },
		        compositionstart: function () {
		            isZh = true;
		        },
		        compositionend: function () {
		        	console.log($("#qualityControlInput").val());
		        	var inputStr = $("#qualityControlInput").val().replace("，",",");
		        	console.log(inputStr);
		            var inputStr = inputStr.split(",");
		            console.log(inputStr);
                    
                    // var str=$("#qualityControlInput").val();
					// str=str.replace(/，/ig,',');
					if(inputStr.length==1){
				        $("#qualityControlInput").val(inputStr[0]);		
                        searchCheckInfo();					  
					    return false;
					}else{						
						$("#qualityControlInput").val(inputStr[1]);
					}
                
		            if (inputStr[2] == "1") {
		                $(".rejectsQuantityDiv").show();
		                $(".qualifiedQuantityDiv").show();
		            } else if (inputStr[2] == "0") {
		                $(".rejectsQuantityDiv").show();
		                $(".qualifiedQuantityDiv").hide();
		            }
		            
		            initReceive(inputStr[2]);
		            $("#qualityControlInput").select();
		        }

		    });

		    $('.ruku_button').click(function() {
		        if ($('.searchInput').val() == '') {

		            $.jGrowl("请输入质检单号");
		            return false;
		        }
		        if ($('.purchaseOrder').text() == '') {
		            $.jGrowl("未找到采购单号");
		            return false;
		        }
              
            // 注释后让不良品质检单扫描后可以入库---文军辉
		        // if ($('.storageLocationCode').val() == '' ||
				// $('.locationCodeId').val() == '') {
		        // $.jGrowl("当前库位编码无效");
		        // return false;
		        // }
		        $('.ruku_button').attr("style", "pointer-events:none");
		        var storage = {};
		        storage.qualityInspectionNumber = $('.qualityControlId').text();
		        storage.nondefectiveNumber = $('.qualifiedQuantity').val();
		        storage.rejectsNumber = $('.rejectsQuantity').val();
		        storage.purchaseNumber = $('.purchaseOrder').text();
		        storage.skuCode = $('.skuCode').val();

		        Object.keys(map).forEach(function(key) {
		            var domObj = $("." + key);
		            if (domObj[0].tagName == "INPUT") {
		                storage[key] = domObj.val();
		            } else {
		                storage[key] = domObj.text();
		            }
		        });
		        storage.storageLocationCode = $('.locationCodeId').val();
		        saveStorage(storage);
		    });
		    window.onload = window.onresize = function() {
		        footer();
		    }

		    function footer() {
		        $("footer").addClass("hidden"); // 隐藏底部信息
		        var conH = $(".main-container").height(); // 容器的高度
		        var winH = $(window).height(); // 页面的高度
		        if (conH < winH) { // 容器的高度小于页面的高度的时候 说明没有滚动条 那么显示出底部信息
		            $("footer").removeClass("hidden").css('marginLeft', 0);
		            $(".fixed-table-pagination").css("bottom", "60px");
		        } else { // 当出现滚动条的时候 隐藏底部信息
		            $("footer").addClass("hidden");
		            $(".fixed-table-pagination").css("bottom", "0");
		        }
		        // $('body').scroll();
		    }
		    $(function() {
		        var tableInit = new TableInit();
		        tableInit.init();
		        $("#warehouseNumber").focus();
		        form_datetime();
		    });
		});
		/*
		 * 苏荣 2017-5-13 功能: 检索出库位编码之，用户点击相应的库位之后的点击事件处理函数 参数:{ obj：'当前用户点击的li元素' },
		 */
		function getValue(obj) {
		    var name = $(obj).text();
		    var id = $(obj).attr('id');
		    $('.storageLocationCode').val(name).attr('id', id);
		    $('.locationCodeId').val(id);
		}
		/*
		 * 苏荣 2017-5-13 功能: 根据用户输入的库位关键字和仓库id获取对应的库位 参数:{ key:'用户输入的库位关键字'
		 * warehouseId:'仓库id' }, 备注：此方法中获取数据的ajax请求必须使用非异步的方式获取
		 */
		function getStorageLocationCodeContainer(key, warehouseId) {
		    var storageLocationCode = [];
		    $.ajax({
		        type: "get",
		        // contentType: "application/json; charset=utf-8",
		        url: '/warehouse/getLocatorInfo',
		        data: {
		            "locatorCode": key == null ? '' : key,
		            "warehouseId": warehouseId ? warehouseId : 8
		        },
		        async: false,
		        beforeSend: function(request) {
		            request.setRequestHeader(header, token);
		        },
		        success: function(data) {
		            storageLocationCode = JSON.parse(data);
		        }
		    });
		    return storageLocationCode;
		}
		/*
		 * 苏荣 2017-5-13 功能： 新增入库中的库位检索输入框的keyup事件和focus事件的处理函数
		 */
		function focusAndKeyup() {
		    var _t = this,
		        storageLocationCodeContainer = $('.storageLocationCodeContainer'), // 存放库位编码的div容器
		        data = [], // 需要显示的数据 getStorageLocationCodeContainer（）此方法获取数据
		        parentBox = $(_t).parents('div.in_box'), // 输入框父元素 用于定位使用
		        parentOffset = parentBox.offset(), // 父元素的偏移量对象
		        left = $(_t).offset().left, // 父元素向左偏移的距离
		        top = $(_t).offset().top + $(_t).outerHeight() + 2, // 父元素向上偏移的距离
		        width = $(_t).outerWidth(); // 当前元素的宽带
		    if ($('.warehouseId').val() == '') {
		        $.jGrowl('请输入质检单号');
		        return;
		    }
		    storageLocationCodeContainer.html('');
		    data = getStorageLocationCodeContainer($('.storageLocationCode').val(), $('.warehouseId').val());
		    if (data.length > 0) {
		        // 当只返回一条数据的时候 不需要进行遍历
		        if (data.length == 1) {
		            storageLocationCodeContainer.
		            append('<ul><li id="' + data[0].locatorId + '" onclick="getValue(this)" >' + data[0].locatorCode + '</li></ul>').css({
		                position: 'absolute',
		                width: width,
		                top: top,
		                left: left
		            });
		        } else {
		            var ul = '<ul>';
		            $.each(data, function(index, item) {
		                ul += '<li id="' + item.locatorId + '" onclick="getValue(this)" >' + item.locatorCode + '</li>';
		            });
		            storageLocationCodeContainer.css({
		                position: 'absolute',
		                width: width,
		                top: top,
		                left: left
		            }).append(ul);
		        }
		    } else {
		        storageLocationCodeContainer.
		        append('<ul><li>没有找到匹配的记录</li></ul>').css({
		            position: 'absolute',
		            width: width,
		            top: top,
		            left: left
		        });
		    }
		    storageLocationCodeContainer.show(500);
		    // 监听光标移开事件
		    $(_t).on('blur', function() {
		        $('.storageLocationCodeContainer').hide(500);
		    });
		    $(window).resize(function() {
		        if ($('.storageLocationCodeContainer').css('display') == 'block') {
		            $(_t).focus();
		        }
		    });
		}
		/* ]]> */
		//表格加载完成回调事件
		$(function() {
			//法人主体下拉列表参数处理
			$("#legaler").prev('.dropdown-menu.open').find('.text').each(function () {
				var newStringArr = $(this).text().split("=")
				$(this).text(newStringArr[0]);
				$(this).val(newStringArr[1]);
			})
			$("#legaler option").not(':first').each(function () {
				var newStringArr = $(this).text().split("=")
				$(this).text(newStringArr[0]);
				$(this).val(newStringArr[1]);
			})
		});
		

		
		//导出数据
		 function exportStorageList() {
	           
			 
				if(keyNumbers.length > 0 ){//如果存在 勾选
						
					 $.jGrowl('入库数据正在导出，请稍后');
				     var queryString = "checkIds="+keyNumbers.join(',');
				     var url = "exportStorageList?" + queryString;
				     console.info(url);
				     window.location.href = url;
					
				}else{ //不存在勾选 则是看过滤条件
					var signWarehouse = $("#signWarehouse").val().join(',')||''; //仓库
			    	
			    	if(signWarehouse == ''){//如果是全部
			    		var wareHouses = [];
			    		$("#signWarehouse").find('option').each(function(){
			    			if("" != $(this).val()){
			    				wareHouses.push($(this).val());
			    			}
			    		});
			    		signWarehouse = wareHouses.join(',');
			    	}
		            
		            var queryNumber = $("#queryNumber").val();//入库/采购/质检单号
		            var skuCode = $("#skuCode_s").val(); //sku名称
		            var createDateStart = $("#createDateStart").val();//入库开始时间
		            var createDateEnd = $("#createDateEnd").val();//入库结束时间
		            //var signType = $("#signType").val();//标记
		            
		            var legaler = $("#legaler").val().join(',')||''; //法人主体
		            var isTax = $("#includeTax").val().trim()||''; //含税方式
		            //var department=$("#department").val().join(',')||'';//需求部门
		            var transferType=$("#transferType").val().trim()||'';//运输方式
		            
		            
		            $.jGrowl('入库数据正在导出，请稍后');
		            var queryString = "queryNumber="+queryNumber+"&skuCode="+skuCode+"&signWarehouse="+signWarehouse
		            +"&createDateStart="+createDateStart+"&createDateEnd="+createDateEnd
		            +"&legaler="+legaler+"&isTax="+isTax+"&transferType="+transferType;
		            window.location.href="exportStorageList?" + queryString;
				 
				}
	            
		 }
		 

		 
/****************************************************发货计划列表分页选中功能  开始******************************************************/
		 var selections = [], keyNumbers = [], $table = $('#table');
		 $table.on('check.bs.table check-all.bs.table',function()
		 {
		     var StorageNumbers = getStorageNumberselections();
		   
		     
		     $.each(StorageNumbers,function(i, obj)
		     {
		         if ($.inArray(obj.storageNumber,StorageNumbers) == -1)
		         {
		             selections.push(obj);
		             keyNumbers.push(obj.storageNumber);
		         }
		     });
		     console.info("------");
		     console.info(selections);
		     console.info(keyNumbers);
		     
		 });
		 $table.on('uncheck-all.bs.table',function(e, rows)
		 {
		     $.each(rows,function(i, obj)
		     {
		    	 keyNumbers.splice($.inArray(obj.storageNumber,keyNumbers),1);
		         $.each(selections,function(j, select)
		         {
		             if (obj.storageNumber == select.storageNumber)
		             {
		                 selections.splice(j,1);
		                 return false;
		             }
		         });
		     });
		     console.info("------");
		     console.info(selections);
		     console.info(keyNumbers);
		 });
		 $table.on('uncheck.bs.table',function(e, row)
		 {
		     $.each(selections,function(i, obj)
		     {
		         if (obj.storageNumber == row.storageNumber)
		         {
		             selections.splice(i,1);
		             return false;
		         }
		     });
		     keyNumbers.splice($.inArray(row.id,keyNumbers),1);
		     console.info("------");
		     console.info(selections);
		     console.info(keyNumbers);
		 });
		 function getStorageNumberselections()
		 {
		     return $.map($table.bootstrapTable('getSelections'),function(row)
		     {
		         return row;
		     });
		     
		 }
		 function responseHandler(res)
		 {
		 	console.info("--responseHandler--");
		 	console.info(res);
		 	console.info(keyNumbers);
		     $.each(res.rows,function(i, row)
		     {
		         row.states = $.inArray(row.storageNumber,keyNumbers) !== -1;
		     });
		     return res;
		 }
/****************************************************发货计划列表分页选中功能  结束******************************************************/

		 
		 