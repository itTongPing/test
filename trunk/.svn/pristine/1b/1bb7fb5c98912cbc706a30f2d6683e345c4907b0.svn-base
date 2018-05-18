/** ***********************************************表格初始化脚本****************************************************************************** */
window.loadCount = 1;//表格刷新次数
$(function(){
	var data = $("#res").val();
	if(data != null && data!=''){
		$.jGrowl(data);
	}
	
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
	
	$("#table01").bootstrapTable({
		method:'get',//请求方式
		url:'/TransferExport/search', //接口地址
	    cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	    pagination: true, //是否显示分页（*）
	    sortOrder: "asc", //排序方式
	    sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
	    pageNumber: 1, //初始化加载第一页，默认第一页
	    pageSize: 20, //每页的记录行数（*）
	    pageList: [20, 50, 100, 200], //可供选择的每页的行数（*）
	    //strictSearch: true,
	    clickToSelect: true, //是否启用点击选中行
	    queryParams: query_params, //传递参数（*）
	    contentType: "application/json",
		resizable : true, // 列是否可以拖动
		reorderableColumns : true, // 列是否可以重新排序
		showColumns : true, // 是否显示自定义列
		fixedWindow : true, // 表头浮动
	    columns: [{
            field: 'warehouseName',
            title: '出库仓',
            align: 'center'
            //formatter:formatData('warehouseIds',row) 
        },{
        	field: 'shipmentId',
            title: 'Shipment ID',
            align: 'center'
            //formatter:formatData('shipmentId',row)
        },{
        	field: 'fnSku',
            title: 'FnSKU',
            align: 'center'
            //formatter:formatData('fnSku',row)
        },{
        	field: 'skuCode',
            title: 'SKU',
            align: 'center'
           // formatter:formatData('skuCode',row)
        },{
        	field: 'shipQuantity',
            title: '发货数量',
            align: 'center'
            //formatter:formatData('shipQuantity',row)
        },{
        	field: 'inWayQuantity',
            title: '在途数量',
            align: 'center',
            formatter:function(value,row){
            	if(value==null || value==''){
            		return '-';
            	}else{
            		return value;
            	}
            }
        },{
        	field: 'receiveQuantity',
            title: '收货数量',
            align: 'center',
            formatter:function(value,row){
            	if(value==null || value==''){
            		return '-';
            	}else{
            		return value;
            	}
            }
        }],
		onLoadSuccess: function (data) {
			flyer.scrollBar($('#table01'));
		}
	});
});
var  paramdata =  getData();
//查询以及导出数据获取
function getData(){
	//sku
	if($("#sku").val()){
		$("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var sku = $("#sku").val().split(',');
	//var sku='DEATC100735,';
	//仓库
	var warehouse = $("#wareHouseId").val();
	if(warehouse != ''){
		
		var wareHouses = [];
		$("#wareHouseId").find('option:selected').each(function(){
				wareHouses.push($(this).val());
		});
		warehouse = wareHouses.join(',');
	}else{
		warehouse='-1';
	}
	
	var exportData = {
			sku:sku,
			warehouse:warehouse
		};
	return exportData
}
//查询表格参数
function query_params(params) {
	$.ajaxSetup({
		traditional: true
	});
    $.extend(params,paramdata);//合并数据
    loadCount++;
    return params;
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
	    var rs = suffixPicture.indexOf(suffix.toLowerCase());
	    if(rs<0 && rs2<0) {
	        $.jGrowl('只支持xlsx,请重新上传！');
	        return;
	    }
	    var imgUrl = "/TransferExport/uploadFile";
	    $("#uploadForm").attr("action", imgUrl);
	    $("#uploadForm").ajaxForm({
	    		beforeSend : function(request)
			        {
			            request.setRequestHeader(header,token);
			        },
			    success:function(data){
			    	//alert(data);
			    	var jsonData = JSON.parse(data)
			    	if(jsonData.success){
			    		$.jGrowl(jsonData.data);
			    		
			    	}else{
			    		$.jGrowl(jsonData.data);
			    	}
			    	if(jsonData.success){
			    		//刷新页面
			    		setTimeout('refreshIndex()',1000);
			    		
			    	}else{
			    		setTimeout('refreshIndex()',1000);
			    	}
			    	
			    },
			    dataType:"text"
	    }).submit();
	
}

/*function formatData(dataName,row){
	var data = row[dataName];
	if(data==null || data==''){
		return '-';
	}else{
		return data;
	}
}*/
//表格查询
var searchResult = function(){
	paramdata =  getData();
	window.search_click = true;
	$('#table01').bootstrapTable('refresh',{
		query:{
			pageNumber:1,
			limit:20,
			sku:paramdata.sku,
			warehouse:paramdata.warehouse
		}
	});
	$('#table01').bootstrapTable('selectPage',1);
}
//导出功能
function exportExcel(){
	//var selectEles=$("#tab_Warn").bootstrapTable('getSelections');
	 //勾选导出
	 /*if(selectEles.length > 0){
		 var warehouse = new Array();
		 var requestOrderId = new Array();
		$(selectEles).each(function(i, item) {
			requestOrderId.push(item.requestOrderId);
		});
		window.open("/TransferExport/exportFile?requestOrderId=" + requestOrderId.join(","));//在另外新建窗口中打开窗口
		//使用ajax请求
		
		return;
	 }*/
	paramdata =  getData();
	 var parmasStr = "?sku=" +paramdata.sku + "&warehouse="+paramdata.warehouse;
	 window.location.href="/TransferExport/exportFile" + parmasStr;
	/*paramdata =  getData();
	$.ajax({
		url : "/TransferExport/exportFile",
        type : 'get',
        async : true,
        data : query_params,
        success:function(data){
        	//var jsonData = JSON.parse(data);
        	console.log(data);
        	if(data.success){
        		$.jGrowl("导出成功");
        	}else{
        		$.jGrowl("导入失败");
        	}
        }
	});*/

}
//重置搜索条件
function searchReset(){
	
	//$('.search-form-div select').selectpicker('val','');
	$("#wareHouseId").selectpicker('val','-1');
	$("#sku").val('');
}
function refreshIndex(){
	window.location.reload();
}