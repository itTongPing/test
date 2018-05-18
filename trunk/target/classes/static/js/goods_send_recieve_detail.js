window.loadCount = 1;//表格刷新次数
$(function () {
    var url_str = window.location.href;
    $('.selectpicker').selectpicker({noneSelectedText: ''});
/*<![CDATA[*/
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
    $('.search-form-div select').selectpicker('val','-1');
	$('#queryForm')[0].reset();// reset方法是javascript原声的 所以需要转换为js对象
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
	//往来单位
	if($("#organization").val()){
		$("#organization").val($("#organization").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var organization = $("#organization").val().split(',');
	//单据编号
	if($("#receipt_no").val()){
		$("#receipt_no").val($("#receipt_no").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var receipt_no = $("#receipt_no").val().split(',');
	//仓库
	var warehouse = $("#warehouse").val();
	if(warehouse == -1){
		
		var wareHouses = [];
		$("#warehouse").find('option').each(function(){
			if(-1 != $(this).val()){
				wareHouses.push($(this).val());
			}
		});
		warehouse = wareHouses.join(',');
	}
	
	var exportData = {
			from_date:from_date,
			to_date:to_date,
			sku:sku,
			legaler:legaler,
			organization:organization,
			receipt_no:receipt_no,
			warehouse:warehouse
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
	})
	$("#legaler option").not(':first').each(function(){
		var newStringArr = $(this).text().split("=")
		$(this).text(newStringArr[0]);
		$(this).val(newStringArr[1]);
	})
		
	$("#table01").on("load-success.bs.table",function(){
        flyer.scrollBar($('#table01'));
		if(loadCount!=2 && window.search_click){
			$.jGrowl("查询成功");
			search_click = false;
		}
	})
	//表格加载失败回调方法
	$("#table01").on("load-error.bs.table",function(){
		//查询错误提示
		$.jGrowl("查询出错");
		
	})
	
})


$('#table01').bootstrapTable({
    url:'/report/good/search', 
    method: 'get', //请求方式（*）
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
    uniqueId: "sku", //每一行的唯一标识，一般为主键列
    contentType: "application/json",
	queryParamsType: '',
    resizable : true, // 列是否可以拖动
    reorderableColumns : true, // 列是否可以重新排序
    showColumns : true, // 是否显示自定义列
    fixedWindow : true, // 表头浮动
});
$(function(){
	$("#queryForm").on('submit',function(){
		//获取对应的时间间隔
		var allowExport = true;
		var Data = $(this).serialize();
		$.ajax({
			url:'/report/good/preExport',
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
	})
});
//确认导出框
function confirmExportShow() {
        var options = $('#table01').bootstrapTable('getOptions');
        var totalRows = options.totalRows;
        if (totalRows >= 60000) {//导出项大于6w，询问导出
            var ele_click = $("#exoprtButton");
            //上锁让其他操作被禁止
            $(".clock_div").show();
            $("#exportExcel").modal('show');
        } else {
            exportExcel();
        }   
}
//导出
function exportExcel(){
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
	//往来单位
    var organization =  $("#organization").val();
  //订单编号
	if($("#receipt_no").val()){
		$("#receipt_no").val($("#receipt_no").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var receipt_no = $("#receipt_no").val().split(',');
	//采购部门
	var warehouse = $("#warehouse").val();
	if(warehouse == -1){
		
		var wareHouses = [];
		$("#warehouse").find('option').each(function(){
			if(-1 != $(this).val()){
				wareHouses.push($(this).val());
			}
		});
		warehouse = wareHouses.join(',');
	}
	//var warehouse = $("#warehouse").val();
	//非勾选的长度
	var options = $('#tab_Warn').bootstrapTable('getOptions');
    var totalRows = options.totalRows;
	var parmasStr = "?from_date=" + from_date + "&to_date=" + to_date +  "&sku=" + sku
	+ "&legaler=" + legaler+"&receipt_no="+ receipt_no+"&organization="+ organization+"&warehouse="+ warehouse +"&limit=" + totalRows;
	$("#exportExcel").modal("hide");
	window.location.href="/report/good/export" + parmasStr;
}
//格式化含税
function formatIsTax(index,row)
{
	console.info(row.is_tax);
	if(row.is_tax!=null)
	{
		if(row.is_tax=='0')
		{
			return '否';
		}else
		{
			return '是';
		}
	}else
	{
		return '-';
	}
}