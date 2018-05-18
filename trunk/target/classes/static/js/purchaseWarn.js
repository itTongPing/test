window.loadCount = 1;//表格刷新次数

var _ctx = $("meta[name='ctx']").attr("content");	
var chainDemand = $("#chainDemand").val();    

$(function(){
		var msgstr=$("#msg").val();
    	if(msgstr!=null && msgstr!=""){
    		$.jGrowl(decodeURIComponent($("#msg").val()));
    	}
	var url_str = window.location.href;
    $('.selectpicker').selectpicker({noneSelectedText: ''});
	//多选框功能（初始化值为-1）
	$('.selectpicker').on('changed.bs.select', function (e, clickedIndex) {
	    platformChanged = true;
	    var selects = $(this).selectpicker('val');
	    var total = $(this).find('option').length;
	    selects = selects == null ? [] : selects;
	    if (selects.length == 0 || selects.length == total)
	        $(this).selectpicker('val', "-1");
	    else {
	        if (clickedIndex == 0) {
	            $(this).selectpicker('deselectAll');
	            $(this).selectpicker('val', "-1");
	        } else if (clickedIndex > 0) {
	            if (selects.indexOf("-1") == 0)
	                $(this).selectpicker('val', selects.splice(1, selects.length));
	            else if (selects.indexOf("") == -1 && selects.length == total - 1) {
	                $(this).selectpicker('val', "-1");
	            }
	        }
	    }
	});
	
	// 日历插件配置方法
	$(".form_datetime").datetimepicker({
	    format: 'yyyy-mm-dd',
	    autoclose: true,
	    language: 'zh-CN',
	    minView: '2',
	});
	//点击enter触发搜索事件
	$(document).keydown(function(event){ 
		if(event.keyCode==13){ 
			searchResult();
		} 
	})
	//导出表单提交
	/*$("#export_btn").click(function(){
		$("#queryForm").submit();
	});*/
	//初始化采购预警表
	$("#tab_Warn").bootstrapTable({
		method:'get',//请求方式
		url:'/report/PurchaseWarn/search', //接口地址
		 method: 'get', //请求方式（*）
		    cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		    pagination: true, //是否显示分页（*）
		    sortOrder: "asc", //排序方式
		    sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
		    pageNumber: 1, //初始化加载第一页，默认第一页
		    pageSize: 20, //每页的记录行数（*）
		    pageList: [20, 50, 100, 200], //可供选择的每页的行数（*）
			queryParamsType: '',
		    fixedWindow:true,// 表头浮动
            resizable: true, // 列是否可以拖动
            reorderableColumns: true, // 列是否可以重新排序
            showColumns: true, // 是否显示自定义列，就是那个下拉显示列打勾的 
		    //strictSearch: true,
		    clickToSelect: true, //是否启用点击选中行
		    queryParams: query_params, //传递参数（*）
		    contentType: "application/json",
        columns: [{
            field: 'legaler_name',
            title: '法人主体',
            align: 'center'
        },{
        	field: 'supplier_name',
            title: '供应商名称',
            align: 'center'
        },{
        	field: 'purchase_no',
            title: '采购单号',
            align: 'center',
            formatter:function(value,row,index){
            	return "<a href='"+chainDemand+"/demand/order2?orderid="+value+"' target='_Blank'>"+value+"</a>";
            }
        }
        ,{
        	field: 'purchase_warn_demand',
            title: '需求单号',
            align: 'center',
            formatter:function(value,row,index){
            	return "<a href='"+chainDemand+"/port/requirementUpdate?requirementNo="+value+"' target='_Blank'>"+value+"</a>";
            }
        },{
        	field: 'deptName',
            title: '需求人',
            align: 'center'
        }
        ,{
        	field: 'assignTime',
            title: '指派采购员时间',
            align: 'center',
            formatter:function(value,row,index){
            	if(value==null){
            		return '-';
            	}
            	var date = new Date(value);
            	return formatDate(date);
            }
        },{
        	field: 'purchase_date',
            title: '生成订单时间',
            align: 'center',
            formatter:function(value,row,index){
            	var date = new Date(value);
            	return formatDate(date);
            }
        },{
        	field: 'sku_code',
            title: 'sku',
            align: 'center'
        },{
        	field: 'sku_name',
            title: 'sku名称',
            align: 'center',
            class: 'skuNameWidth'
        },{
        	field: 'purchase_count',
            title: '订单数量',
            align: 'center'
        },{
        	field: 'currency',
            title: '币别',
            align: 'center'
        },{
        	field: 'price_tax',
            title: '含税单价',
            align: 'center'
        },{
        	field: 'price_without_tax',
            title: '未税单价',
            align: 'center'
        },{
        	field: 'purchase_sum',
            title: '订单金额',
            align: 'center'
        },{
        	field: 'buyer_name',
            title: '采购员',
            align: 'center'
        },{
        	field: 'purchase_group_name',
            title: '采购部门',
            align: 'center'
        },{
        	field: 'department_id',
            title: '业务部门',
            align: 'center'
        },{
        	field: 'stock_count',
            title: '入库数量',
            align: 'center'
        },{
        	field: 'return_count',
            title: '退货数量',
            align: 'center'
        },{
        	field: 'lack_count',
            title: '欠货数量',
            align: 'center',
            class:'hight-coll'
        },{
        	field: 'out_date',
            title: '超期时间',
            align: 'center',
            class:'hight-coll'
        },{
        	field: 'out_date_count',
            title: '超期数量',
            align: 'center',
            class:'hight-coll'
        },{
        	field: 'before_stock_date',
            title: '预计交货时间',
            align: 'center',
            formatter:function(value,row,index){
            	var date = new Date(value);
            	return formatDate(date);
            }
        }],
        onLoadSuccess : function(data)
        {
            $("input[disabled]").parents('.bs-checkbox').attr('title','加载中无法选择')
            $(".fixed-table-loading").hide();
            if(window.once!=1) $.jGrowl('查询成功');
            flyer.scrollBar($('#tab_Warn')); //初始化滚动条
        },
        onLoadError:function(data){
        	$.jGrowl('查询出错');
        } 
	});
	$("#tab_Warn").on("post-body.bs.table",function(){
		$("td.skuNameWidth").each(function(i,t){
			var a=$("td.skuNameWidth")[i]
			$(this).attr("title",$("#tab_Warn").find(a).text());
		});
	})
	
	  
  flyer.inputSpaceToComma($('#sku'),$('#purchase_no'));
	
	//准备导出
	/*$("#queryForm").on('submit',function(){
		//获取对应的时间间隔
		var allowExport = true;
		var Data = $(this).serialize();
		$.ajax({
			url:'/report/PurchaseWarn/preExport',
			contentType:'json',
			async:false,
			method:'get',
			data:Data,
			success:function(req){
				if(!req.success){
					$.jGrowl(req.message);
					allowExport = false;
				}
			},
			error:function(req){
				$.jGrowl('请求失败');
				allowExport = false;
			}
		})
		if(!allowExport){
			return false;
		}
	})*/
	
		//法人主体下拉列表参数处理
		$("#legaler").prev('.dropdown-menu.open').find('.text').each(function(){
			var newStringArr = $(this).text().split("=");
			$(this).text(newStringArr[0]);
			$(this).val(newStringArr[1]);
		});
		$("#legaler option").not(':first').each(function(){
			var newStringArr = $(this).text().split("=");
			$(this).text(newStringArr[0]);
			$(this).val(newStringArr[1]);
		});
		
		
	});
//搜索
var searchResult = function(){
	if(isNull($("#to_date").val())){}else{
		if($("#from_date").val()>$("#to_date").val()){
			$.jGrowl('错误,采购单开始时间大于结束时间');
			return;
		}
	}
	if(isNull($("#before_to_date").val())){}else{
		if($("#before_from_date").val()>$("#before_to_date").val()){
			$.jGrowl('错误,预计交货时间开始时间大于结束时间');
			return;
		}
	}
	
	
	window.search_click = true;
	$('#tab_Warn').bootstrapTable('refresh',{
		query:{
			pageNumber:1
		}
	});
	//$('#tab_Warn').bootstrapTable('selectPage',1);
}
//高级搜索
function searchH(){
$("#searchH").toggle();
}

//重置按钮
function searchReset()
{
    // 点击搜索按钮之后 清空高级搜索里面的数据 重置为默认值
	$("#formCheck")[0].reset();
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
    $('.selectpicker').selectpicker('val', '-1');
    
}

//查询以及导出数据获取
function getData(){
	//开始时间
	var from_date = $("#from_date").val();
	//结束时间
	var to_date = $("#to_date").val();
	//sku
	if($("#sku").val()){
		$("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var sku = $("#sku").val().split(',');
	//法人主体
	var legaler = $("#legaler").val();
	if($("#supplier").val()){
		$("#supplier").val($("#supplier").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	//供应商单位
    var supplier =  $("#supplyAlias").val();
	//订单编号
	if($("#purchase_no").val()){
		$("#purchase_no").val($("#purchase_no").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	//开始时间
	var before_from_date = $("#before_from_date").val();
	//结束时间
	var before_to_date = $("#before_to_date").val();
	var purchase_no = $("#purchase_no").val().split(',');
	//采购部门
	var department = $("#department").val();
	
	//判断是否起欠货
	var flag = '1';//欠货不为0的
	var ju = $("#checkBOX").prop('checked');
	if(ju)
	{
		flag = '0';//所有
	}
	var exportData = {
			from_date:from_date,
			to_date:to_date,
			sku_code:sku,
			legaler_id:legaler,
			supplier_id:supplier,
			purchase_order_id:purchase_no,
			department_id:department,
			before_from_data:before_from_date,
			before_to_data:before_to_date,
			flag:flag
		};
	return exportData
}

//查询表格参数
function query_params(params) {
	$.ajaxSetup({
		traditional: true
	});
    var paramdata = getData();//获取数据
    
    $.extend(params,paramdata);//合并数据
    loadCount++;
    return params;
}

//采购列表使用供应商选择
function getStockList(obj, e)
{
    // 当获取光标的时候，获取当前所在行的tr的id 然后给仓库弹出框的div的id的值设置currentTrId
    var e = e || window.event;
    e.stopPropagation();
    var currentTrId = $(obj).parent().parent().attr('id');
    //$(obj).attr('id',currentTrId + 'warehouse');
    var stock = $(obj).val();
    // 过滤敏感词汇
//    if ($.inArray(stock,city) != -1)
//    {
//        return;
//    }
    if (stock != null)
    { console.log(stock+"---"+_ctx)
        var wa = $(".warehouse");
        $.ajax({
            url : _ctx + "report/PurchaseWarn/getSupplierInfo",
            type : 'get',
            async : true,
            data : {
                supplyAlias : stock
            },
            success : function(data)
            {
                if (data && data.length > 0)
                {
                    wa.html("");
                    $.each(data,function(i, obj)
                    {
                        wa.append('<div stockId="\'' + obj.supplierId + '\'" onclick="getValueToInput(\'' + obj.alias
                                + '\',\'' + currentTrId + '\')">' + obj.alias + '</div>');
                    });
                    if ($(obj).parent().width() + 16 > 200)
                    {
                        wa.css("width",$(obj).parent().width() + 16);
                    } else
                    {
                        wa.css("width","200");
                    }
                    ;
                    wa.show(500);
                } else
                {
                    wa.html("").append('<ul><li>没有匹配到选项，请重新输入</li></ul>')
                    wa.show(500);
                }
                ;
                $(obj).parent().append(wa).css("position"," relative");
            },
            error : function()
            {
                $.jGrowl('请求失败,请重新添加需求');
            }
        });
    } else
    {
        $(".warehouse").hide();
    }
}
//选中仓库后，为输入框赋值
function getValueToInput(warehouseName, currentTrId)
{
    $('#supplyAlias').val(warehouseName);
    //alert( $('#supplyAlias').val());
    $(".warehouse").toggle();
}
//隐藏供应商下拉框
$(window).on("click",function(){
	 $(".warehouse").hide();
});

//确认导出框
function confirmExportShow() {
    var selectEles = $("#tab_Warn").bootstrapTable('getSelections');
    //勾选导出
    if (selectEles.length > 0) {
        exportExcel();
    } else {//非勾选导出，判断数量
        var options = $('#tab_Warn').bootstrapTable('getOptions');
        var totalRows = options.totalRows;
        if (totalRows >= 60000) {//导出项大于6w，询问导出
            var ele_click = $("#exoprtButton");
            //上锁让其他操作被禁止
            $(".clock_div").show();
            $("#exportExcel").modal('show');
            /*$('.confirm').css({
                display: 'block',
                left: ele_click.position().left + 50,
                top: ele_click.position().top + 30,
                zIndex: 100000000
            });*/
        } else {
            exportExcel();
        }
    }
}
//取消被点击的时候消失
/*function closeConfirm()
{
    $('.confirm').hide()
    $(".clock_div").hide();
}*/
//导出
function exportExcel(){
	
	 var selectEles=$("#tab_Warn").bootstrapTable('getSelections');
	 //勾选导出
	 if(selectEles.length > 0){
		 var requestOrderId = new Array();
		$(selectEles).each(function(i, item) {
			requestOrderId.push(item.requestOrderId);
		});
		window.open("/report/PurchaseWarn/export?requestOrderId=" + requestOrderId.join(","));//在另外新建窗口中打开窗口
		//使用ajax请求
		
		return;
	 }
	//开始时间
		var from_date = $("#from_date").val();
		//结束时间
		var to_date = $("#to_date").val();
		//sku
		if($("#sku").val()){
			$("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
		}
		var sku = $("#sku").val().split(',');
		//法人主体
		if($("#legaler").val()==null){
			$("#legaler").val('');
		}
		var legaler = $("#legaler").val();
		if($("#supplier").val()){
			$("#supplier").val($("#supplier").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
		}
		//供应商单位
	    var supplier =  $("#supplyAlias").val();
		//订单编号
		if($("#purchase_no").val()){
			$("#purchase_no").val($("#purchase_no").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
		}
		var purchase_no = $("#purchase_no").val().split(',');
		//开始时间
		var before_from_date = $("#before_from_date").val();
		//结束时间
		var before_to_date = $("#before_to_date").val();
		
		//采购部门
		if($("#department").val()==null){
			$("#department").val('');
		}
		var department = $("#department").val();
		
		//判断是否起欠货
		var flag = '1';//欠货不为0的
		var ju = $("#checkBOX").prop('checked');
		console.info(ju);
		if(ju)
		{
			flag = '0';//所有
		}
	//非勾选的长度
		var options = $('#tab_Warn').bootstrapTable('getOptions');
        var totalRows = options.totalRows;
	
	var parmasStr = "?from_date=" + from_date + "&to_date=" + to_date +  "&sku_code=" + sku
	+ "&legaler_id=" + legaler + "&supplier_id=" + supplier + "&purchase_order_id=" + purchase_no
	+ "&department_id=" +department + "&before_from_data=" + before_from_date + "&before_to_data=" + before_to_date+"&flag="+flag+"&limit=" + totalRows;
	//window.open("/report/PurchaseWarn/export" + parmasStr);
	$("#exportExcel").modal("hide");
	 window.location.href="/report/PurchaseWarn/export" + parmasStr;
}
//格式化时间
function formatDate(date) {
	  return date.getFullYear()
	        + "-" + (date.getMonth()>8?(date.getMonth()+1):"0"+(date.getMonth()+1))
	        + "-" + (date.getDate()>9?date.getDate():"0"+date.getDate());
}