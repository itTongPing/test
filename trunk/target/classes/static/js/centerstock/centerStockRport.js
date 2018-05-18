/************************中央库存查询报表js****************************/
/*初始化表格*/
var firstInPage = true;
var getParamDate = new Object();
var TableInit = function()
{
	var oTableInit = new Object();
	//初始化表格
	oTableInit.Init = function()
	{
		 bootstrapTable = $('#centerStockTable').bootstrapTable
		 ({
			 url:'/report/centerStock/search', // 请求后台的URL（*）
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
             uniqueId: "packageId", // 每一行的唯一标识，一般为主键列
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
                     field : 'upc',
                     title : 'UPC',
                     align : 'center',
                     width : 100
                 }, 
                 {
                     field : 'sku',
                     title : 'SKU',
                     align : 'center',
                     width : 100
                     
                 }, 
                 {
                     field : 'skuName',
                     title : 'SKU名称',
                     align : 'center',
                     width : 50,
                     class: 'skuName'
                 },
                 {
                     field: 'deptName',
                     title: '事业部',
                     align: 'center',
                     width: 150/*,
                     visible:false*/
                 },{
                     field : 'shippedLast24hs',
                     title : '过去 24 小时发货数量',
                     align : 'center',
                     width : 100
                 },{
                     field : 'shippedLast7ds',
                     title : '过去 7 天发货数量',
                     align : 'center',
                     width : 100
                 },{
                     field : 'shippedLast30ds',
                     title : '过去 30 天发货数量',
                     align : 'center',
                     width : 100
                 },{
                     field : 'shippedLast90ds',
                     title : '过去 90 天发货数量',
                     align : 'center',
                     width : 100
                 },{
                     field : 'shippedLast180ds',
                     title : '过去 180 天发货数量',
                     align : 'center',
                     width : 100
                 },{
                     field : 'shippedLast365ds',
                     title : '过去 365 天发货数量',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'fbaTransfer',
                     title : 'FBA退货在途',
                     align : 'center',
                     width: 150,
                     formatter:function(value,row)
                     {
                     	if(0==parseFloat(value))
                     	{
                     		return value;
                     	}else
                     	{
                     		return '<a onClick="jumpPage(\''+'fbaTransfer'+'\',\'' + encodeURIComponent(row.sku) +'\')">' + value + '</a>'
                     	}
                     }
                 },
                 {
                     field : 'fbaStockCount',
                     title : 'FBA库存',
                     align : 'center',
                     width: 150,
                     formatter:function(value,row)
                     {
                     	if(0==parseFloat(value))
                     	{
                     		return value;
                     	}else
                     	{
                     		return '<a onClick="jumpPage(\''+'fba'+'\',\'' + encodeURIComponent(row.sku) +'\')">' + value + '</a>'
                     	}
                     }
                 },
                 {
                     field : 'overseaStockTransfer',
                     title : '海外仓转运在途',
                     align : 'center',
                     width: 150,
                     formatter:function(value,row)
                     {
                     	if(0==parseFloat(value))
                     	{
                     		return value;
                     	}else
                     	{
                     		return '<a onClick="jumpPage(\''+'overseaTransfer'+'\',\'' + encodeURIComponent(row.sku) +'\')">' + value + '</a>'
                     	}
                     }
                 },
                 {
                     field : 'overseaStockCount',
                     title : '海外仓库存',
                     align : 'center',
                     width : 100,
                     formatter:function(value,row)
                     {
                     	if(0==parseFloat(value))
                     	{
                     		return value;
                     	}else
                     	{
                     		return '<a onClick="jumpPage(\''+'overseaStock'+'\',\'' + encodeURIComponent(row.sku) +'\')">' + value + '</a>'
                     	}
                     }
                 }, {
                     field : 'firstAirTransfer',
                     title : '头程空运在途',
                     align : 'center',
                     width : 100,
                     formatter:function(value,row)
                     {
                     	if(0==parseFloat(value))
                     	{
                     		return value;
                     	}else
                     	{
                     		return '<a onClick="jumpPage(\''+'firstAir'+'\',\'' + encodeURIComponent(row.sku) +'\')">' + value + '</a>'
                     	}
                     }
                 },
                 {
                     field : 'firstShipTransfer',
                     title : '头程海运在途',
                     align : 'center',
                     width : 100,
                     formatter:function(value,row)
                     {
                     	if(0==parseFloat(value))
                     	{
                     		return value;
                     	}else
                     	{
                     		return '<a onClick="jumpPage(\''+'firstShip'+'\',\'' + encodeURIComponent(row.sku) +'\')">' + value + '</a>'
                     	}
                     }
                 },{
                     field : 'firstTrainsTransfer',
                     title : '头程铁路在途',
                     align : 'center',
                     width : 100,
                     formatter:function(value,row)
                     {
                     	if(0==parseFloat(value))
                     	{
                     		return value;
                     	}else
                     	{
                     		return '<a onClick="jumpPage(\''+'firstTrains'+'\',\'' + encodeURIComponent(row.sku) +'\')">' + value + '</a>'
                     	}
                     }
                 },{
                     field : 'transferWarehouseCount',
                     title : '国内中转仓库存',
                     align : 'center',
                     width : 100,
                     formatter:function(value,row)
                     {
                     	if(0==parseFloat(value))
                     	{
                     		return value;
                     	}else
                     	{
                     		return '<a onClick="jumpPage(\''+'warehouseTransfer'+'\',\'' + encodeURIComponent(row.sku) +'\')">' + value + '</a>'
                     	}
                     }
                 },{
                     field : 'purchaseTransfer',
                     title : '采购订单在途',
                     align : 'center',
                     width : 100,
                     formatter:function(value,row)
                     {
                     	if(0==parseFloat(value))
                     	{
                     		return value;
                     	}else
                     	{
                     		return '<a onClick="jumpPage(\''+'purchaseInWay'+'\',\'' + encodeURIComponent(row.sku) +'\')">' + value + '</a>'
                     	}
                     }
                 },{
                     field : 'purchaseCycle',
                     title : '采购周期',
                     align : 'center',
                     width : 100
                 }],
                 onLoadSuccess: function (data) {
                 	flyer.scrollBar($('#centerStockTable')); //初始化滚动条
                 	if(firstInPage)
                 	{   
                 		
                     	firstInPage = false;
                 	}
                 	
                 },
                 onLoadError:function(data){
                	 $.jGrowl('查询出错');
                 }
		 });
		 $("#centerStockTable").on("post-body.bs.table",function(){
	    		$("td.skuName").each(function(i,t){
	    			var a=$("td.skuName")[i]
	    			$(this).attr("title",$("#centerStockTable").find(a).text());
	    		});
	    	})
	}
	
	oTableInit.queryParams = function (params)
	{
		if($("#upc").val()){
			$("#upc").val($("#upc").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
		}
		if($("#sku").val()){
			$("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
		}
		var upc = $("#upc").val(); //upc
        var sku = $("#sku").val(); // sku;
        var deptIds = $("#dept").val().join(',');//部门
        if(deptIds==""){
    		var deptIdArr = [];
    		$("#dept").find('option').each(function(){
    			if(-1 != $(this).val()){
    				deptIdArr.push($(this).val());
    			}
    		});
    		deptIds = deptIdArr.join(',');
    	}
        var stockType = $("#stockTypeId").val();
        params.sku = sku;
        params.upc = upc;
        params.deptIds = deptIds;
        params.stockType = stockType;
        getParamDate = params;
        return params
	}
	return oTableInit;
}

/*function responseHandler(res) {
    $.each(res.rows, function (i, row) {
        row.states = $.inArray(row.packageId, packageIds) !== -1;
    });
    return res;
}*/
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
    
    
});

function stockTypeChange(stockType){
	searchResult();
}



// 查询
function searchResult()
{
	 keyNumbers = [];
	$("#centerStockTable").bootstrapTable('destroy');
	var oTable = new TableInit();
    oTable.Init();
}
//跳转页面
function jumpPage(code,val)
{
	var stockType = $("#stockTypeId").val();
	var url ='/report/centerStock/stock/index?sku='+val+'&code='+code+'&stockType='+stockType;
	window.open(url);
}
//重置搜索条件
function searchReset()
{
	$("#dept").selectpicker('val','-1');
	$("#upc").val("");
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
	
	//var selections = $('#centerStockTable').bootstrapTable('getAllSelections', 'states');
	var paramSL = "";
	
	if(keyNumbers.length > 0 )
	{
		paramSL = "?id="+keyNumbers.join(',');
	}else
	{
		var upc = getParamDate.upc;
		var sku = getParamDate.sku;
		var deptIds = getParamDate.deptIds;
		var stockType = getParamDate.stockType;
		paramSL = "?upc="+upc+"&sku="+sku+"&deptIds="+deptIds+"&stockType="+stockType;
	}
	var url ='/report/centerStock/export'+paramSL;//'/report/centerStock/export'+paramSL
	location.href = url;
}



/****************************************************发货计划列表分页选中功能  开始******************************************************/
var selections = [], keyNumbers = [], $table = $('#centerStockTable');
$table.on('check.bs.table check-all.bs.table',function()
{
    var StorageNumbers = getStorageNumberselections();
    
    $.each(StorageNumbers,function(i, obj)
    {
        if ($.inArray(obj.id,keyNumbers) == -1)
        {
            selections.push(obj);
            keyNumbers.push(obj.id);
        }
    });
    
});
$table.on('uncheck-all.bs.table',function(e, rows)
{
    $.each(rows,function(i, obj)
    {
   	 keyNumbers.splice($.inArray(obj.id,keyNumbers),1);
        $.each(selections,function(j, select)
        {
        	if (obj.id == select.id)
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
        if (obj.id == row.id)
        {
            selections.splice(i,1);
            return false;
        }
    });
    keyNumbers.splice($.inArray(row.id,keyNumbers),1);
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
    $.each(res.rows,function(i, row)
    {
        row.states = $.inArray(row.id,keyNumbers) !== -1;
    });
    return res;
}
/****************************************************发货计划列表分页选中功能  结束******************************************************/