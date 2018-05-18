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
var searchResult = function(){
	window.search_click = true;
	$('#table01').bootstrapTable('refresh',{
		query:{
			pageNumber:1
		}
	});
	$('#table01').bootstrapTable('selectPage',1);
}



//查询表格参数
function query_params(params) {
	$.ajaxSetup({
		traditional: true
	});
    var paramdata = getData();//获取数据
	params.limit = params.pageSize;
    $.extend(params,paramdata);//合并数据
    loadCount++;
    return params;
}

//重置按钮
function searchReset()
{
    // 点击搜索按钮之后 清空高级搜索里面的数据 重置为默认值
    $('.search-form-div select').selectpicker('val','-1');
	$('#queryForm')[0].reset();// reset方法是javascript原声的 所以需要转换为js对象
}

//查询以及导出数据获取
function getData(){
	//sku
	if($("#sku").val()){
		$("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var SKU = $("#sku").val().split(',');
	
	//仓库
	var warehouses = $("#warehouses").val();
	//法人主体
	var legaler = $("#legaler").val();
	var exportData = {
			sku:SKU,
			warehouse:warehouses,
			legaler:legaler
		};
	return exportData
}
$('#table01').bootstrapTable({
	queryParamsType: '',
	resizable : true, // 列是否可以拖动
	reorderableColumns : true, // 列是否可以重新排序
	showColumns : true, // 是否显示自定义列
	fixedWindow : true, // 表头浮动
});
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
$(function(){
	$("#queryForm").on('submit',function(){
		//获取对应的时间间隔
		var allowExport = true;
		var Data = $(this).serialize();
		$.ajax({
			url:'/report/stock/age/preExport',
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