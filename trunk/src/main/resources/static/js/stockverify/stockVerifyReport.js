/***************************海外仓库存核实报表***********************************/
/*初始化表格*/
var firstInPage = true;
var getParamDate = new Object();
var TableInit = function()
{
	var oTableInit = new Object();
	//初始化表格
	oTableInit.Init = function()
	{
		 bootstrapTable = $('#tableList').bootstrapTable
		 ({
			 url:'/report/stockverify/search', // 请求后台的URL（*）
             method: 'get', // 请求方式（*）
             striped: false, // 是否显示行间隔色
             cache: false, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
             pagination: true, // 是否显示分页（*）
             sortable: true, // 是否启用排序
             // sortName:"timestamp", //按照订单时间排序
             // sortOrder: "asc", //排序方式
             sidePagination: "server", // 分页方式：client客户端分页，server服务端分页（*）
             pageNumber: 1, //初始化加载第一页，默认第一页
             pageSize: 20, //每页的记录行数（*）
             pageList: [20, 50, 100, 200], // 可供选择的每页的行数（*）
             strictSearch: true,
             clickToSelect: true, // 是否启用点击选中行
             queryParams: oTableInit.queryParams,// 传递参数（*）
             width: 'auto',
             uniqueId: "stockVerifyId", // 每一行的唯一标识，一般为主键列
             cardView: false,
             contentType: "application/json",
             responseHandler : responseHandler,
             queryParamsType: '',
             //resizable: true, //列是否可以拖动
             fixedWindow:true,// 表头浮动
             reorderableColumns: true, //列是否可以重新排序
             showColumns: true, //是否显示自定义列，就是那个下拉显示列打勾的
             undefinedText: "-",//为undefined的时候显示的文本
             onClickCell: function (a, b, c, d) {
                 var args = Array.prototype.slice.call(arguments, 1);
             },
             columns : [
                 {
                     checkbox : true,
                     field : "states",
                     width : 45,
                     cellStyle : {
                         css : {
                             maxWidth : '45px'
                         }
                     }
                 },
                 {
                     field : 'departmentName',
                     title : '事业部',
                     align : 'center',
                     width : 50
                 }, 
                 {
                     field : 'skuName',
                     title : 'SKU名称',
                     align : 'center',
                     width : 100,
                     class : 'skuName'
                     
                 }, 
                 {
                     field : 'categoryName',
                     title : '品类',
                     align : 'center',
                     width : 100,
                     class: 'skuName'
                 },
                 {
                     field: 'stockName',
                     title: '仓库名称',
                     align: 'center',
                     width: 100,
                 },
                 {
                     field: 'skuCode',
                     title: '系统SKU',
                     align: 'center',
                     width: 100,
                 },
                 {
                     field: 'systemBoxNumber',
                     title: '系统箱数',
                     align: 'center',
                     width: 50,
                 },
                 {
                     field: 'systemSingleBoxNumber',
                     title: '系统单箱数量',
                     align: 'center',
                     width: 50,
                 },
                 {
                     field: 'systemNumberSum',
                     title: '系统数量',
                     align: 'center',
                     width: 50,
                 },
                 {
                     field: 'length',
                     title: '长',
                     align: 'center',
                     width: 50,
                 },
                 {
                     field: 'width',
                     title: '宽',
                     align: 'center',
                     width: 50,
                 },
                 {
                     field: 'height',
                     title: '高',
                     align: 'center',
                     width: 50,
                 },
                 {
                     field: 'volume',
                     title: '总体积',
                     align: 'center',
                     width: 50,
                 },
                 {
                     field: 'weight',
                     title: '总重量',
                     align: 'center',
                     width: 50,
                 },
                 {
                     field: 'price',
                     title: '单价',
                     align: 'center',
                     width: 50,
                 },
                 {
                     field: 'wsku',
                     title: '所在仓SKU',
                     align: 'center',
                     width: 50,
                 },
                 {
                     field: 'inStockCount',
                     title: '所在仓库存',
                     align: 'center',
                     width: 50,
                 },
                 {
                     field: 'differenceQuantity',
                     title: '差异库存',
                     align: 'center',
                     width: 50,
                 }],
                 onLoadSuccess: function (data) {
                 	flyer.scrollBar($('#tableList')); //初始化滚动条
                 	if(firstInPage)
                 	{   
                 		
                     	firstInPage = false;
                 	}
                 	
                 },
                 onLoadError:function(data){
                	 $.jGrowl('查询出错');
                 }
		 });
		 $("#tableList").on("post-body.bs.table",function(){
	    		$("td.skuName").each(function(i,t){
	    			var a=$("td.skuName")[i]
	    			$(this).attr("title",$("#tableList").find(a).text());
	    		});
	    	})
	}
	
	oTableInit.queryParams = function (params)
	{
		if($("#sku").val()){
			$("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
		}
		if($("#wsku").val()){
			$("#wsku").val($("#wsku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
		}
		var wsku = $("#wsku").val(); //upc
        var sku = $("#sku").val(); // sku;
        var stockIds = $("#stock").val().join(',');//仓库
        if(stockIds==""){
    		var stockIdArr = [];
    		$("#stock").find('option').each(function(){
    			if(-1 != $(this).val()){
    				stockIdArr.push($(this).val());
    			}
    		});
    		stockIds = stockIdArr.join(',');
    	}
        params.wsku = wsku;
        params.sku = sku;
        params.stockIds = stockIds;
        getParamDate = params;
        return params
	}
	
	return oTableInit;
}
/*初始化页面*/
$(function(){
	//初始化表格
	var oTable = new TableInit();
    oTable.Init();
    
    $('.selectpicker').on('changed.bs.select',function(e, clickedIndex)
    {
        //platformChanged = true;
        var selects = $(this).selectpicker('val');
        var total = $(this).find('option').length;
        selects = selects == null ? [] : selects;
        if (selects.length == 0 || selects.length == total)
            $(this).selectpicker('val',"-1");
        else
        {
            if (clickedIndex == 0)
            {
                $(this).selectpicker('deselectAll');
                $(this).selectpicker('val',"-1");
            } else if (clickedIndex > 0)
            {
                if (selects.indexOf("-1") == 0)
                    $(this).selectpicker('val',selects.splice(1,selects.length));
                else if (selects.indexOf("-1") == -1 && selects.length == total - 1)
                {
                    $(this).selectpicker('val',"-1");
                }
            }
        }
    });
    setTimeout(() => {
    	$table = $('#tableList');
    	$table.on('check.bs.table check-all.bs.table',function()
    			{
    			    var StorageNumbers = getStorageNumberselections();
    			    
    			    $.each(StorageNumbers,function(i, obj)
    			    {
    			        if ($.inArray(obj.stockVerifyId,keyNumbers) == -1)
    			        {
    			            selections.push(obj);
    			            keyNumbers.push(obj.stockVerifyId);
    			        }
    			    });
    			    
    			});
    			$table.on('uncheck-all.bs.table',function(e, rows)
    			{
    			    $.each(rows,function(i, obj)
    			    {
    			   	 keyNumbers.splice($.inArray(obj.stockVerifyId,keyNumbers),1);
    			        $.each(selections,function(j, select)
    			        {
    			        	if (obj.stockVerifyId == select.stockVerifyId)
    			            {
    			                selections.splice(j,1);
    			                return false;
    			            }
    			        });
    			    });
    			});
    			$table.on('uncheck.bs.table',function(e, row)
    			{
    			    $.each(selections,function(i, obj)
    			    {
    			        if (obj.stockVerifyId == row.stockVerifyId)
    			        {
    			            selections.splice(i,1);
    			            return false;
    			        }
    			    });
    			    keyNumbers.splice($.inArray(row.stockVerifyId,keyNumbers),1);
    			});
	}, 200);
    
});
// 查询
function searchResult()
{
	 keyNumbers = [];
	$("#tableList").bootstrapTable('destroy');
	var oTable = new TableInit();
    oTable.Init();
}
//重置搜索条件
function searchReset()
{
	$("#stock").selectpicker('val','-1');
	$("#wsku").val("");
	$("#sku").val("");
}
//按F5清空搜索条件
$(document).keyup(function(event)
{
    var event = event || window.event; // 获取event对象
    var keyCode = event.keyCode || event.which; // 获取keyCode
    // 按enter键触发表格搜索
    if (keyCode == 116)
    {
    	searchReset();
    }
});
//导出excel
function confirmExportShow()
{
	var paramSL = "";
	
	if(keyNumbers.length > 0 )
	{
		paramSL = "?id="+keyNumbers.join(',');
	}else
	{
		var wsku = getParamDate.wsku;
		var sku = getParamDate.sku;
		var stockIds = getParamDate.stockIds;
		paramSL = "?wsku="+wsku+"&sku="+sku+"&stockIds="+stockIds;
	}
	var url ='/report/stockverify/export'+paramSL;
	window.location.href = url;
}



/****************************************************发货计划列表分页选中功能  开始******************************************************/
var selections = [], keyNumbers = [], $table = $('#tableList');;

function getStorageNumberselections()
{
    return $.map($table.bootstrapTable('getSelections'),function(row)
    {
        return row;
    });
    
}
function responseHandler(res)
{
    $.each(res.rows,function(i, row)
    {
        row.states = $.inArray(row.stockVerifyId,keyNumbers) !== -1;
    });
    return res;
}
/****************************************************发货计划列表分页选中功能  结束******************************************************/