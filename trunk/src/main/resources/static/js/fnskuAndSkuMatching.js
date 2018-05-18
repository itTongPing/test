/************************FNSKU与SKU匹配表js****************************/
/*初始化表格*/
var firstInPage = true;
var getParamDate = new Object();
var TableInit = function()
{
	var oTableInit = new Object();
	//初始化表格
	oTableInit.Init = function()
	{
		 bootstrapTable = $('#fnskuAndSkuMatching').bootstrapTable
		 ({
			 url:'/report/FnskuAndSkuMatching/search', // 请求后台的URL（*）
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
                     field : 'fromWarehouseName',
                     title : '调出仓',
                     align : 'center',
                     width : 100
                     
                 }, 
                 {
                     field : 'toWarehouseName',
                     title : '目标仓',
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
                     formatter: function (value, row, index) {
                     	if(value == 'N/A'){
                     		value = '-';
                     	}
                         return '<div class="maxLength" title="' + value + '">' + value + '</div>';
                     }
                 },
                 {
                     field : 'fromWsskuBox',
                     title : '调出仓sku',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'goodsWeight',
                     title : '单品重量',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'goodsPackageLength',
                     title : '单品长',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'goodsPackageWidth',
                     title : '单品宽',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'goodsPackageHeight',
                     title : '单品高',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'goodsPackageVolume',
                     title : '单品体积',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'boxCount',
                     title : '单箱数量',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'boxWeight',
                     title : '单箱重量',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'boxLength',
                     title : '单箱长',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'boxWidth',
                     title : '单箱宽',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'boxHeight',
                     title : '单箱高',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'boxVolume',
                     title : '单箱体积',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'shipmentID',
                     title : 'shipmentID',
                     align : 'center',
                     formatter: function (value, row, index) {
                     	if(value == 'N/A' || value =='' || value== null){
                     		value = '-';
                     	}
                        return value;
                     }
                 },
                 {
                     field : 'fnsku',
                     title : 'fnsku',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'taxUnitPrice',
                     title : '采购单价',
                     align : 'center',
                     width : 100 ,
                     formatter: function (value, row, index) {
                         if (value == 0) {
                             return '-';
                         }
                         return value;
                     }
                 },
                 {
                     field : 'saleDepartment',
                     title : '销售部门',
                     align : 'center',
                     width : 100 
                    
                 },
                 {
                     field : 'pictureUrl',
                     title : '产品照片url',
                     align : 'center',
                     width : 100 ,
                     formatter:function(value,row)
                     {
                     	if(value==null)
                     	{
                     		return value;
                     	}else
                     	{
                     		return '<a  target="_blank" href='+value+'>图片</a>'
                     	}
                     }
                 }],
                 onLoadSuccess: function (data) {
                 	flyer.scrollBar($('#fnskuAndSkuMatching')); //初始化滚动条
                 	footer();
                 	if(firstInPage)
                 	{   
                 		
                     	firstInPage = false;
                 	}
                 	
                 },
                 onLoadError:function(data){
                	 $.jGrowl('查询出错');
                 }
		 });
	}
	
	oTableInit.queryParams = function (params)
	{
		/*if($("#sku").val()){
			$("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
		}
		if($("#fromWsskuBox").val()){
			$("#fromWsskuBox").val($("#fromWsskuBox").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
		}
		if($("#fnsku").val()){
			$("#fnsku").val($("#fnsku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
		}
		var sku = $("#sku").val();
        var fromWsskuBox = $("#fromWsskuBox").val(); 
        var fnsku = $("#fnsku").val();
        params.sku = sku;
        params.fromWsskuBox = fromWsskuBox;
        params.fnsku = fnsku;
        getParamDate = params;*/
		params.sku=getParamDate.sku;
		params.fromWsskuBox = getParamDate.fromWsskuBox;
		params.fnsku = getParamDate.fnsku;
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
    $(document).keydown(function(event){ 
		if(event.keyCode==13){ 
			searchResult();
		}
	});  
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
// 查询
function searchResult()
{
	if($("#sku").val()){
		$("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	if($("#fromWsskuBox").val()){
		$("#fromWsskuBox").val($("#fromWsskuBox").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	if($("#fnsku").val()){
		$("#fnsku").val($("#fnsku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var sku = $("#sku").val();
    var fromWsskuBox = $("#fromWsskuBox").val(); 
    var fnsku = $("#fnsku").val();
    getParamDate.sku = sku;
    getParamDate.fromWsskuBox = fromWsskuBox;
    getParamDate.fnsku = fnsku;
	keyNumbers = [];
	$("#fnskuAndSkuMatching").bootstrapTable('destroy');
	var oTable = new TableInit();
    oTable.Init();
}
//跳转页面
/*function jumpPage(code,val)
{
	var url ='/report/centerStock/stock/index?sku='+val+'&code='+code;
	window.open(url);
}*/
//重置搜索条件
function searchReset()
{
	$("#sku").val("");
	$("#fromWsskuBox").val("");
	$("#fnsku").val("");
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
	
	//var selections = $('#fnskuAndSkuMatching').bootstrapTable('getAllSelections', 'states');
	var paramSL = "";
	
	if(keyNumbers.length > 0 )
	{
		paramSL = "?id="+keyNumbers.join(',');
	}else
	{	
		if(getParamDate.sku==undefined){
			getParamDate.sku="";
		}
		if(getParamDate.fromWsskuBox==undefined){
			getParamDate.fromWsskuBox="";
		}
		if(getParamDate.fnsku==undefined){
			getParamDate.fnsku="";
		}
		var sku = getParamDate.sku;
		var fromWsskuBox = getParamDate.fromWsskuBox;
		var fnsku = getParamDate.fnsku;
		paramSL = "?sku="+sku+"&fromWsskuBox="+fromWsskuBox+"&fnsku="+fnsku;
	}
	var url ='/report/FnskuAndSkuMatching/export'+paramSL;
	location.href = url;
}



/****************************************************发货计划列表分页选中功能  开始******************************************************/
var selections = [], keyNumbers = [], $table = $('#fnskuAndSkuMatching');
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