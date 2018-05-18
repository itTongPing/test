var firstInPage = true;
var getParamDate = new Object();
var TableInit = function()
{
	var oTableInit = new Object();
	//初始化表格
	oTableInit.Init = function()
	{
		 bootstrapTable = $('#skuDepartRelation').bootstrapTable
		 ({
			 url:'/report/centerSkuDepartRelation/search', // 请求后台的URL（*）
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
             uniqueId: "id", // 每一行的唯一标识，一般为主键列
             cardView: false,
             contentType: "application/json",
             /*responseHandler : responseHandler,*/
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
                 /*{
                     checkbox : true,
                     field : "states",
                     width : 45,
                     cellStyle : {
                         css : {
                             maxWidth : '45px'
                         }
                     }
                 },*/
                 {
                     field : 'skuCode',
                     title : 'SKU',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'skuName',
                     title : 'SKU名称',
                     align : 'center',
                     class:'skuName',
                     width : 50,
                     formatter: function (value, row, index) {
                         var sHtml = "";
                         
                         if (value == '' || value == null) {
                             sHtml = "<div>-</div>";
                         }
                         else {
                             sHtml = "<div title='"+value+"'>"+value+"</div>";
                             /*if (value.length>100)
                             {
                                 sHtml = "<div title='"+value+"'>"+value.substring(0,100)+"..."+"</div>";
                             }else
                             {

                             }*/

                         }
                         
                         return sHtml;
                     }
                 },
                 {
                     field : 'departmentName',
                     title : '事业部',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'purchasePrice',
                     title : '采购单价',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'length',
                     title : '长',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'width',
                     title : '宽',
                     align : 'center',
                     width : 100
                 },
                 {
                     field : 'height',
                     title : '高',
                     align : 'center',
                     width : 100
                 }],
                 onLoadSuccess: function (data) {
                 	flyer.scrollBar($('#skuDepartRelation')); //初始化滚动条
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
	    			$(this).attr("title",$("#skuDepartRelation").find(a).text());
	    		});
	    	})
	}
	
	oTableInit.queryParams = function (params)
	{
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
        params.sku = sku;
        params.deptIds = deptIds;
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
});

//查询
function searchResult()
{
	 keyNumbers = [];
	$("#skuDepartRelation").bootstrapTable('destroy');
	var oTable = new TableInit();
    oTable.Init();
}

function importExcel(){
	$('#file').trigger("click");
}
function importDelivery(){
	var fileName = $('#file').val();
	 if(null == fileName || "" == fileName){
	$.jGrowl('请先选择择要导入的文件！');
	        return;
	    }
	 var suffixArray = fileName.split(".");
	var suffix = suffixArray[suffixArray.length-1];
	var suffixPicture = "xlsx";
	//var rs = suffixPicture.indexOf(suffix.toLowerCase());
	if(suffix!=suffixPicture) {
	    $.jGrowl('只支持xlsx,请重新上传！');
	        return;
	    }
	 var files = document.getElementById('file').files;//获取文件
	 var size = files[0].size/(1024*1024);
	if(size>=10)
	{
		$.jGrowl('文件限制10M以内!!');
	    return;
	}
	var imgUrl = "/report/centerSkuDepartRelation/upLoadFile";
	var formdata = new FormData();
	formdata.append("file",files[0]);//文件
	$.showLoading("正在导入中");
	flyer.ajax
    ({
    	url : imgUrl,
    	data: formdata,
	    processData: false,
	    contentType: false,
	    timeout:0,
    	loadding: true,
    	beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        processData: false, //告诉jq不要去处理发送的数据
        contentType: false //告诉jq不要设置Content-Type请求头
    }).done(function(res)
    {
    	$.hideLoading();
    	if(res.success){
    		$.jGrowl("导入成功");
    	}else{
    		$.jGrowl(res.errMsg);
    		setTimeout('flush()',2000);
    		return;
    	}
    	if(res.success){
    		/*$.hideLoading();
    		//刷新页面
*/    		setTimeout('flush()',2000);
    	}
    });
}
//重置
function searchReset()
{
	$("#dept").selectpicker('val','-1');
	$("#sku").val("");
}
//刷新页面
function flush()
{
	window.location.reload();
}