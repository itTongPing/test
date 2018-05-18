window.loadCount = 1;//表格刷新次数
$(function () {
	
    var url_str = window.location.href;
    $('.selectpicker').selectpicker({noneSelectedText: ''});
/*<![CDATA[*/
//多选框功能（初始化值为-1）
$('.selectpicker').not("#declare_status").on('changed.bs.select', function (e, clickedIndex) {
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
            if (selects.indexOf("") == 0)
                $(this).selectpicker('val', selects.splice(1, selects.length));
            else if (selects.indexOf("") == -1 && selects.length == total - 1) {
                $(this).selectpicker('val', "");
            }
        }
    }
});
/*]]>*/
});
//表格查询
var  paramdata =  getData();
var _paramdata =  getData();
var searchResult = function(){
	window.search_click = true;
	paramdata = getData();//获取数据
	$('#table01').bootstrapTable('refresh',{
		query:{
			pageNumber:1,
			offset:0
		}
	});
}


//查询表格参数
function query_params(params) {
	$.ajaxSetup({
		traditional: true
	});
	params.limit = params.pageSize;
    $.extend(params,paramdata);//合并数据
    loadCount++;
    return params;
}

//高级搜索

function searchH(){
	$("#searchH").toggle();
}

//重置按钮
function searchReset()
{
	paramdata = _paramdata;
    // 点击搜索按钮之后 清空高级搜索里面的数据 重置为默认值
    $('.search-form-div select').selectpicker('val','');		
	$('#corporationName').Val('');
    $.each($('input'),function(index, ele)
    {
        $ele = $(ele);
        // 表示是下拉列表
        if ($ele.hasClass('textCheck'))
        {
            $ele.parent().val('');
        } else
        { // 输入框
            $ele.val('');
        }
    });
    $('.selectpicker').selectpicker('val', '');
    var aaa = new Date();
    aaa.setMonth(new Date().getMonth()-1);
    var nowtime = new Date();
    $("#billBeginDate").datetimepicker({
        format: 'yyyy-mm-dd',
        autoclose: true,
        language: 'zh-CN',
        minView: '2',
        toggleActive: false,
        zIndexOffset: 1,
        endDate: nowtime
    }).datetimepicker('setDate',aaa);
    $("#billEndDate").datetimepicker({
        format: 'yyyy-mm-dd',
        autoclose: true,
        language: 'zh-CN',
        minView: '2',
        toggleActive: false,
        zIndexOffset: 1
    }).datetimepicker('setDate',nowtime)
}

//查询以及导出数据获取
function getData(){

	var billBeginDate = $("#billBeginDate").val();
	var billEndDate = $("#billEndDate").val();
	var requestOrderId = $("#requestOrderId").val().trim().replace(/^\s+|\s+$/g, "");
	var supplierName = $("#supplierName").val().trim().replace(/^\s+|\s+$/g, "");
	var corporationName = $("#corporationName").val().join(",");
	var purchaseOrderId = $("#purchaseOrderId").val().trim().replace(/^\s+|\s+$/g, "");
	var purchaseBeginDate = $("#purchaseBeginDate").val();
	var purchaseEndDate = $("#purchaseEndDate").val();
	var purchaseDepartment = $("#purchaseDepartment").val().join(",");
	var payMethod = $("#payMethod").val().join(",");
	var orderStatus = $("#orderStatus").val().join(",");
	
	var exportData = {
			billBeginDate: billBeginDate,			//请款开始日期
			billEndDate: billEndDate,				//请款结束日期
			requestOrderId : requestOrderId,		//请款单号
			supplierName : supplierName,			//供应商 		
			corporationName : corporationName,		//法人主体
			purchaseOrderId : purchaseOrderId,		//采购单号
			purchaseBeginDate : purchaseBeginDate,	//采购开始日期
			purchaseEndDate : purchaseEndDate,		//采购结束日期
			purchaseDepartment: purchaseDepartment,	//请款部门
			payMethod : payMethod,					//结算方式
			payStatus : orderStatus					//付款状态
		};
	return exportData
}


//表格加载完成回调事件
$(function(){
	//法人主体下拉列表参数处理
	$("#legaler").prev('.dropdown-menu.open').find('.text').each(function(){
		var newStringArr = $(this).text().split("=")
		$(this).text(newStringArr[0]);
		$(this).val(newStringArr[1]);
	});
	$("#legaler option").not(':first').each(function(){
		var newStringArr = $(this).text().split("=")
		$(this).text(newStringArr[0]);
		$(this).val(newStringArr[1]);
	});
		
	$("#table01").on("load-success.bs.table",function(){
		flyer.scrollBar($('#table01'));
		if(loadCount!=2 && window.search_click){
			$.jGrowl("查询成功");
			search_click = false;
		}
	});
	//表格加载失败回调方法
	$("#table01").on("load-error.bs.table",function(){
		//查询错误提示
		$.jGrowl("查询出错");
		
	});
	
	$("#requestOrderId").bind("blur", function () {
        $(this).val($(this).val().trim().replace(/ +/g, ","));
    });
});
//取消被点击的时候消失
function closeConfirm()
{
    $('.confirm').hide()
    $(".clock_div").hide();
}
$('#table01').bootstrapTable({
	queryParamsType: '',
	resizable : true, // 列是否可以拖动
	reorderableColumns : true, // 列是否可以重新排序
	showColumns : true, // 是否显示自定义列
	fixedWindow : true, // 表头浮动
});
//确认导出框
function confirmExportShow() {
	closeConfirm();
    var selectEles = $("#table01").bootstrapTable('getSelections');
    //勾选导出
    if (selectEles.length > 0) {
        exportExcel();
    } else {//非勾选导出，判断数量
        var options = $('#table01').bootstrapTable('getOptions');
        var totalRows = options.totalRows;
        if (totalRows >= 30000) {//导出项大于3w，询问导出
            var ele_click = $("#exoprtButton");
            //上锁让其他操作被禁止
            $(".clock_div").show();
            $('.confirm').css({
                display: 'block',
                left: ele_click.position().left + 50,
                top: ele_click.position().top + 30,
                zIndex: 100000000
            });
        } else {
            exportExcel();
        }
    }
}

function exportExcel(){
	
	 var selectEles=$("#table01").bootstrapTable('getSelections');
	 //勾选导出
	 if(selectEles.length > 0){
		 var requestOrderId = new Array();
		$(selectEles).each(function(i, item) {
			requestOrderId.push(item.requestOrderId);
		});
		window.open("/report/payOrder/export?requestOrderId=" + requestOrderId.join(","));//在另外新建窗口中打开窗口
		return;
	 }
	
	var billBeginDate = $("#billBeginDate").val();
	var billEndDate = $("#billEndDate").val();
	var requestOrderId = $("#requestOrderId").val().trim().replace(/^\s+|\s+$/g, "");
	var supplierName = $("#supplierName").val().trim().replace(/^\s+|\s+$/g, "");
	var corporationName = $("#corporationName").val().join(",");
	var purchaseOrderId = $("#purchaseOrderId").val().trim().replace(/^\s+|\s+$/g, "");
	var purchaseBeginDate = $("#purchaseBeginDate").val();
	var purchaseEndDate = $("#purchaseEndDate").val();
	var purchaseDepartment = $("#purchaseDepartment").val().join(",");
	var payMethod = $("#payMethod").val().join(",");
	var orderStatus = $("#orderStatus").val().join(",");
	
	
	var parmasStr = "?billBeginDate=" + billBeginDate + "&billEndDate=" + billEndDate +  "&requestOrderId=" + requestOrderId
	+ "&supplierName=" + supplierName + "&corporationName=" + corporationName + "&purchaseOrderId=" + purchaseOrderId
	+ "&purchaseBeginDate=" +purchaseBeginDate + "&purchaseEndDate=" + purchaseEndDate + "&purchaseDepartment=" + purchaseDepartment
	+ "&payMethod=" +payMethod + "&payStatus=" + orderStatus;
	
	window.open("/report/payOrder/export" + parmasStr);
	
}

$(function(){
	$("#queryForm").on('click',function(){
		cosnole.log(getData());
		
//		window.open("/report/payOrder/export?request_order_id=" + );
		
//		//获取对应的时间间隔
//		var allowExport = true;
//		var Data = $(this).serialize();
//		$.ajax({
//			url:'/report/payOrder/export',
//			contentType:'json',
//			async:false,
//			method:'get',
//			data:getData(),
//			success:function(req){
//				if(!req.success){
//					$.jGrowl(req.message);
//					allowExport = false;
//				}
//			},
//			error:function(req){
//				$.jGrowl('请求失败');
//				allowExport = false;
//			}
//		})
//		if(!allowExport){
//			return false;
//		}
	})
});

var aaa = new Date();
aaa.setMonth(new Date().getMonth()-1);
var nowtime = new Date();
$("input[name=from_date]").datetimepicker({
    format: 'yyyy-mm-dd',
    autoclose: true,
    language: 'zh-CN',
    minView: '2',
    toggleActive: false,
    zIndexOffset: 1,
    endDate: nowtime
});
$("input[name=to_date]").datetimepicker({
    format: 'yyyy-mm-dd',
    autoclose: true,
    language: 'zh-CN',
    minView: '2',
    toggleActive: false,
    zIndexOffset: 1
});


//时间转换方法
function timeStampToTime(value) {
	var newDate = new Date();
	newDate.setTime(value);
	return newDate.format('yyyy-MM-dd hh:mm');
}

function warpContent(value) {
	if(value == 'N/A' || value == null || typeof(value) == 'undefined'){
		return "-";
	} else {
		return '<span class="purchaseOrderWarp" title="'+value+'">' + value + '</>';		
	}
}

function formatContent(value){
	if(value == 'N/A' || value == null || typeof(value) == 'undefined'){
		return "-";
	} else {
		return warpContent(value);		
	}	
}

function toOrderType(value){
	if("0" == value){
		return "预付";
	} else if("1" == value){
		return "账期";
	}
}

function toOrderStatus(value){
	if("0" == value){
		return "未支付";
	} else if("1" == value){
		return "已支付";
	} else if("2" == value){
		return "已拒付";
	} else if("3" == value){
		return "支付中";
	}
}

function toPayedMoney(value, row, index){
	if("1" == row.payStatus){
		return toFormatMoney(value);
	} else {
		return 0.00;
	}
}

function toFormatMoney(value){
	try {
		return value.toFixed(2);		
	} catch (e) {
		return "-";
	}
}

$("#supplierName").on("keyup",function(){
    /* ".text-supplier", */
    if($(this).val()!= ""){
    		$(this).next().find("tbody").html("");
    		//ajax
    		var params = {"supplierName": $(this).val()};
    		var url = _ctx + "report/payOrder/gainSupplierDataByAlias";
    		$("#supplierTbody").html("");
    		var next = $(this).next();
			$.CommonGetAjax(url, params, function(data) {
				if (data != null) {
					if (data.success == true) {
						var supplierList = data.data.supplierSelect;
						var accountList = data.data.accountList;
						$("#supplierTbody tr").remove();
						if (supplierList.length>0&&supplierList) {
							$.each(supplierList, function (i, k){
								$("#supplierTbody").append('<tr><td style="cursor:pointer" onclick="selectSupplierGainValue(this)" corporationId="'
										+k.corporationId+'" corporationName="'+k.corporationName+'"  value="'+k.supperlierId+'">'+k.name+'</td></tr>');
							});
						}else {
							$("#supplierTbody").append('<tr><td >没有匹配到选项</td></tr>');
						}
						next.show(500);
						$("#accountListValue").val(JSON.stringify(accountList));
					} else {
						$.jGrowl(data.message);
					}
				}
			}, function(data) {
				if (data.success == false) {
					$.jGrowl(data.message);
				} else {
					$.jGrowl("保存失败");
				}
			},"true");
    	}else {
    		$(this).next().hide(500);
    		$(this).attr('title',null);
		}
});

$("#supplierName").on("blur",function(){
    $(this).next().hide(500);
});

function selectSupplierGainValue(da){
	$("#supplierName").val($(da).text()).attr('title',$(da).text());
}
