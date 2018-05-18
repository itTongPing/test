window.loadCount = 1;//表格刷新次数
var _ctx = $("meta[name='ctx']").attr("content");
var aaa = new Date();
aaa.setMonth(new Date().getMonth()-1);
var nowtime = new Date();
$("#from_date").datetimepicker({
    format: 'yyyy-mm-dd',
    autoclose: true,
    language: 'zh-CN',
    minView: '2',
    toggleActive: false,
    zIndexOffset: 1,
    endDate: nowtime
}).datetimepicker('setDate',aaa);
$("#to_date").datetimepicker({
    format: 'yyyy-mm-dd',
    autoclose: true,
    language: 'zh-CN',
    minView: '2',
    toggleActive: false,
    zIndexOffset: 1
}).datetimepicker('setDate',nowtime);
$("#month").datetimepicker({
    format: 'yyyy-mm',
    autoclose: true,
    language: 'zh-CN',
    minView: '2',
    toggleActive: false,
    zIndexOffset: 1,
    endDate: null
});
$("#from_audit_date,#to_audit_date,#from_verif_date,#to_verif_date").datetimepicker({
    format: 'yyyy-mm-dd',
    autoclose: true,
    language: 'zh-CN',
    minView: '2',
    toggleActive: false,
    zIndexOffset: 1,
    endDate: null
});
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
$('#table01').bootstrapTable({
	queryParamsType: '',
	resizable : true, // 列是否可以拖动
	reorderableColumns : true, // 列是否可以重新排序
	showColumns : true, // 是否显示自定义列
	fixedWindow : true, // 表头浮动
});

//导出事件中的表单提交
var form_submit=function(){
	$("#queryForm").submit();
}

//表格查询
var searchResult = function(){
	
//	if(CompareDate(from_date,to_date)){
////		$.jGrowl('错误开始时间大于结束时间');
//		alert("错误开始时间大于结束时间");
//		return;
//	}
	if(isNull($("#to_date").val())){}else{
		if($("#from_date").val()>$("#to_date").val()){
			$.jGrowl('错误,申报日期开始时间大于结束时间');
			return;
		}
	}
	if(isNull($("#to_audit_date").val())){}else{
		if($("#from_audit_date").val()>$("#to_audit_date").val()){
			$.jGrowl('错误,审核时间开始时间大于结束时间');
			return;
		}
	}
	if(isNull($("#to_verif_date").val())){}else{
		if($("#from_verif_date").val()>$("#to_verif_date").val()){
			$.jGrowl('错误,核验时间开始时间大于结束时间');
			return;
		}
	}
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

//高级搜索

function searchH(){
	$("#searchH").toggle();
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
	//月份
	var month = $("#month").val().split('-').join("");
	//sku
	if($("#sku").val()){
		$("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var sku = $("#sku").val().split(',');
	//报关单号
	if($("#declare_no").val()){
		$("#declare_no").val($("#declare_no").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var declare_no = $("#declare_no").val().split(',');
	//法人主体
	var legaler = $("#legaler").val();
	//采购单号
	if($("#purchase_no").val()){
		$("#purchase_no").val($("#purchase_no").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var purchase_no = $("#purchase_no").val().split(',');
	
	var purchase_no = $("#purchase_no").val().split(',');
	//供应商名称
	if($("#supplyAlias").val()){
		$("#supplyAlias").val($("#supplyAlias").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var supplier = $("#supplyAlias").val().split(',');
	//采购员
	if($("#buyer").val()){
		$("#buyer").val($("#buyer").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var buyer = $("#buyer").val().split(',');
	//采购部门
	var department = $("#department").val();
	//开始时间
	var from_date = $("#from_date").val();
	//结束时间
	var to_date = $("#to_date").val();
	//关联单号
	if($("#related_no").val()){
		$("#related_no").val($("#related_no").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	//报关状态
	var declare_status = $("#declare_status").val();
	//退税说明
	var drawback_explain = $("#drawback_explain").val();
	var related_no = $("#related_no").val().split(',');
	//退税审核员
	if($("#auditor_name").val()){
		$("#auditor_name").val($("#auditor_name").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var auditor_name = $("#auditor_name").val();
	//退税核验员
	if($("#verifer_name").val()){
		$("#verifer_name").val($("#verifer_name").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
	}
	var verifer_name = $("#verifer_name").val();
	// 审核时间
	var from_audit_date = $("#from_audit_date").val();
	var to_audit_date = $("#to_audit_date").val();
	// 核验时间
	var from_verif_date = $("#from_verif_date").val();
	var to_verif_date = $("#to_verif_date").val();
	//是否无退税核验
	var has_no_verifer = $("#has_no_verifer").prop("checked")?1:0;
	var exportData = {
			month:month,
			sku:sku,
			declare_no:declare_no,
			legaler:legaler,
			purchase_no:purchase_no,
			supplier:supplier,
			department:department,
			buyer:buyer,
			from_date:from_date,
			to_date:to_date,
			related_no:related_no,
			declare_status:declare_status,
			drawback_explain:drawback_explain,
			auditor_name:auditor_name,
			verifer_name:verifer_name,
			from_audit_date:from_audit_date,
			to_audit_date:to_audit_date,
			from_verif_date:from_verif_date,
			to_verif_date:to_verif_date,
			has_no_verifer:has_no_verifer
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

function show_paper_detail(obj){
	$("#paper_detail").modal("show");
	//刷新表格数据
	var opt = {
        url: "/report/declare/detail",
        query:{
        	relate_ids:obj
        }
    };
	$("#paper_detail_table").bootstrapTable('refresh', opt);
}

//弹窗结构
function paper_detail(related_no,row){
	return '<a onclick = "show_paper_detail(['+row.relate_ids+'])" style = color:#2cc3a9;>'+related_no+'</a>'
}
//格式时间
 function formatDate(value,rowl,index){
	 if(isNull(value)){return '-'}else{
	 var date = new Date(value);
	 return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
	 }
 }
 
$(function(){	
	
	$("#queryForm").on('submit',function(){
		//获取对应的时间间隔
		var allowExport = true;
		var Data = $(this).serialize();
		$.ajax({
			url:'/report/declare/preExport',
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
            url : _ctx + "report/declare/getSupplierInfo",
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
})
//比较两个时间大小
//function compareDate(Date start,Date end){
//	if(start>end){
//		
//		return false;
//	}
//	return true;
//}

$('#table01').on('load-success.bs.table', function () {
    flyer.scrollBar($('#table01'));
});

function toContractOn(value){
	if(value == null || value == ""){
		return value  = "-";
	}else{
		 return '<a target="_blank" class="title_class"  style = "width:200px;color: #2cc3a9;" title="'+value+'" >' + value + '</a>';
	}
}

function explain_formatter(value,index,row){
	if(isNull(value)|| value==''){
		return '-';
	}else{
		return value;
	}
}

function fastExport(){
	var params = getData();
//	console.log(params);
//	$("#fastExportBtn").attr('disabled',"true");//按钮不可点击 
/*	setTimeout(function(){
		$('#fastExportBtn').removeAttr("disabled"); //移除disabled属性 
	},1000*60*2);*/
	
	var parmasStr = "?month=" + params.month + "&sku=" + params.sku + "&declare_no=" + params.declare_no + "&legaler=" + params.legaler 
	+ "&purchase_no=" + params.purchase_no +	"&supplier=" + params.supplier + "&department=" + params.department + "&buyer=" + params.buyer 
	+ "&from_date=" + params.from_date + "&to_date=" + params.to_date + "&related_no=" + params.related_no + "&declare_status=" + params.declare_status 
	+ "&drawback_explain=" + params.drawback_explain + "&auditor_name=" + params.auditor_name + "&verifer_name=" + params.verifer_name 
	+ "&from_audit_date=" + params.from_audit_date + "&to_audit_date=" + params.to_audit_date + "&from_verif_date=" + params.from_verif_date 
	+ "&to_verif_date=" + params.to_verif_date + "&has_no_verifer=" + params.has_no_verifer;
	
	 window.open("/report/declare/declareInvoiceExport" + parmasStr);//在另外新建窗口中打开窗口
}