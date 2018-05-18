window.loadCount = 1;//表格刷新次数
$(function () {
    var msgstr=$("#msg").val();
    if(msgstr!=null && msgstr!=""){
        $.jGrowl(decodeURIComponent($("#msg").val()));
    }
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
	params.limit = params.pageSize;
    var paramdata = getData();//获取数据
    $.extend(params,paramdata);//合并数据
    loadCount++;
    return params;
}

//重置按钮
function searchReset()
{
    // 点击搜索按钮之后 清空高级搜索里面的数据 重置为默认值
    $('.search-form-div select').selectpicker('val','-1');
	//$('#queryForm')[0].reset();// reset方法是javascript原声的 所以需要转换为js对象
	$("#transfer_no").val("");
	$("#out_from_date").val("");
	$("#out_to_date").val("");
	$("#shipmentid").val("");
	$("#fnsku").val("");
	$("#sellersku").val("");
    $("#accountName").val("");
}

//查询以及导出数据获取
function getData(){
    //时间
    var from_date = $("#from_date").val();
    var to_date = $("#to_date").val();
    //出库时间
    var out_from_date = $("#out_from_date").val();
    var out_to_date = $("#out_to_date").val();
    //sku
    if($("#sku").val()){
        $("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
    }
    var sku = $("#sku").val().split(',');
    //法人主体
    var legaler = $("#legaler").val();
    //调拨单号
    if($("#transfer_no").val()){
        $("#transfer_no").val($("#transfer_no").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
    }
    var transfer_no = $("#transfer_no").val().split(',');
    //目标仓
    var target_warehouse = $("#target_warehouse").val();
    //线路仓
    var pass_warehouse = $("#pass_warehouse").val();
    //调出仓
    var out_warehouse = $("#out_warehouse").val();
    //含税
    var is_tax = $("#is_tax").val();
    //调拨状态
    var transfer_status = $("#transfer_status").val();

    
    if($("#shipmentid").val()){
        $("#shipmentid").val($("#shipmentid").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
    }
    var shipmentid = $("#shipmentid").val().split(',');
    
    if($("#fnsku").val()){
        $("#fnsku").val($("#fnsku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
    }
    var fnsku = $("#fnsku").val().split(',');
    
    if($("#sellersku").val()){
        $("#sellersku").val($("#sellersku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
    }
    var sellersku = $("#sellersku").val().split(',');
    
    if($("#accountName").val()){
        $("#accountName").val($("#accountName").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
    }
    var account_name = $("#accountName").val().split(',');
    var exportData = {
            from_date:from_date,
            to_date:to_date,
            sku:sku,
            legaler:legaler,
            transfer_no:transfer_no,
            target_warehouse:target_warehouse,
            pass_warehouse:pass_warehouse,
            out_warehouse:out_warehouse,
            is_tax:is_tax,
            transfer_status:transfer_status,
            out_from_date:out_from_date,
            out_to_date:out_to_date,
            shipmentid:shipmentid,
            fnsku:fnsku,
            sellersku:sellersku,
            accountName:account_name
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
})
$(function(){
    $("#queryForm").on('submit',function(){
        //获取对应的时间间隔
        var allowExport = true;
        var Data = $(this).serialize();
        $.ajax({
            url:'/report/transfer/preExport',
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

$(function(){
    $('#table01').bootstrapTable({
        url:'/report/transfer/search',
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
        queryParamsType : '',
        uniqueId: "sku", //每一行的唯一标识，一般为主键列
        contentType: "application/json",
        resizable : true, // 列是否可以拖动
        reorderableColumns : true, // 列是否可以重新排序
        showColumns : true, // 是否显示自定义列
        fixedWindow : true, // 表头浮动
        columns: [{
            field: 'transfer_no',
            title: '调拨单号',
            align: 'left',
            class: 'firstTd'
        }, {
            field: 'transfer_date',
            title: '单据日期',
            align: 'left',
            class: 'firstTd'
        }, {
            field: 'legal_name',
            title: '法人主体',
            align: 'left',
            class: 'firstTd'
        }, {
            field: 'outtime',
            title: '出库时间',
            align: 'left',
            class: 'firstTd'
        }, {
            field: 'out_warehouse_name',
            title: '调出仓',
            align: 'left',
            class: 'firstTd'
        }, {
            field: 'pass_warehouse_name',
            title: '线路仓',
            align: 'left',
            class: 'firstTd'
        }, {
            field: 'target_warehouse_name',
            title: '目标仓',
            align: 'left',
            class: 'firstTd'
        },
            {
                field: 'SKU',
                title: 'SKU',
                align: 'left',
                class: 'firstTd',
                formatter:mergeCell('sku',2)
            },{
                field: 'sku_name',
                title: 'SKU名称',
                align: 'left',
                class: 'firstTd title_class',
                formatter:mergeCell('sku_name',2)
            },{
                field: 'name',
                title: '品名',
                align: 'left',
                class: 'firstTd',
                formatter:mergeCell('name',2)
            },{
                field: 'box_no',
                title: '箱号',
                align: 'left',
                class: 'firstTd'
            },{
                field: 'box_count',
                title: '单箱数量',
                align: 'left',
                class: 'firstTd',
                formatter:mergeCell('box_count',2)
            },{
                field: 'quantity_received',
                title: '已到货数量',
                align: 'left',
                class: 'firstTd'
            },{
                field: 'transport_type',
                title: '运输方式',
                align: 'left',
                class: 'firstTd'
            },{
                field: 'actual_weight',
                title: '货运毛重',
                align: 'left',
                class: 'firstTd'
            }, {
                field: 'box_grow',
                title: '长',
                align: 'left',
                class: 'firstTd'
            }, {
                field: 'box_broad',
                title: '宽',
                align: 'left',
                class: 'firstTd'
            }, {
                field: 'box_height',
                title: '高',
                align: 'left',
                class: 'firstTd'
            }, {
                field: 'box_volume',
                title: '体积',
                align: 'left',
                class: 'firstTd'
            }, {
                field: 'transfer_status',
                title: '调拨状态',
                align: 'left',
                class: 'firstTd'
            }, {
                field: 'is_tax',
                title: '含税',
                align: 'left',
                class: 'firstTd'
            }, {
            field: 'site_name',
            title: '站点',
            align: 'left',
            class: 'firstTd'
        }, {
            field: 'account_name',
            title: '店铺',
            align: 'left',
            class: 'firstTd'
        },{
            field: 'shipment_id',
            title: 'shipmentid',
            align: 'left',
            class: 'firstTd'
        },{
            field: 'fnsku',
            title: 'fnsku',
            align: 'left',
            class: 'firstTd',
            formatter: function (value, row) {
                if(value == null){
                    return '<span  title="-">-</span>';
                }else{
                    return '<span  title="' + value
                        + '">' + value + '</span>';
                }
            }
        },{
            field: 'sellersku',
            title: 'sellersku',
            align: 'left',
            class: 'firstTd'
        },{
            field: 'money',
            title: '采购金额',
            align: 'left',
            formatter:mergeCell('money',2)
        },{
            field: 'tax_rate',
            title: '税率',
            align: 'left',
            formatter:mergeCell('tax_rate',2)
        },{
            field: 'return_tax',
            title: '退税率',
            align: 'left',
            formatter:mergeCell('return_tax',2)
        },{
            field: 'currency',
            title: '币种',
            align: 'left'
        },{
            field: 'declare_money',
            title: '报关金额',
            align: 'left',
            formatter:mergeCell('declare_money',2)
        },{
            field: 'declare_order_id',
            title: '报关单号',
            align: 'left',
            formatter:mergeCell('declare_order_id',2)
        },{
            field: 'num',
            title: '项号',
            align: 'left',
            formatter: function (value, row) {
                if(value == null){
                    return '<span  title="-">-</span>';
                }else{
                    return '<span  title="' + value
                        + '">' + value + '</span>';
                }
            }
        },{
            field: 'customs_number',
            title: '海关单号',
            align: 'left',
            formatter:mergeCell('customs_number',2)
        },{
            field: 'unit_price',
            title: '采购单价',
            align: 'left',
            formatter:mergeCell('unit_price',2)
        },{
            field: 'declare_order_date',
            title: '出口日期',
            align: 'left',
            formatter:mergeCell('declare_order_date',2)
        }
        ],
        onLoadSuccess:function(){
            flyer.scrollBar($('#table01'));
            if(loadCount!=2 && window.search_click){
                $.jGrowl("查询成功");
                search_click = false;
            }
        },error:function(){
            $.jGrowl("查询出错");
        }
    });

});


function mergeCell(dataName,cellNum){
    
        return function(data,row){
            if(!row[dataName]){
                return '-';
            }
            return '<span title="'+row[dataName]+'">'+row[dataName]+'</span>';
        }
}

//时间插件
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

//时间插件
var bbb = new Date();
bbb.setMonth(new Date().getMonth()-1);
var nowtime2 = new Date();
$("#out_from_date").datetimepicker({
    format: 'yyyy-mm-dd',
    autoclose: true,
    language: 'zh-CN',
    minView: '2',
    toggleActive: false,
    zIndexOffset: 1,
    endDate: nowtime2
})
$("#out_to_date").datetimepicker({
    format: 'yyyy-mm-dd',
    autoclose: true,
    language: 'zh-CN',
    minView: '2',
    toggleActive: false,
    zIndexOffset: 1
})

//高级搜索
function searchH(){
    $("#searchH").toggle();
}
//确认导出框
function confirmExportShow() {
    var selectEles = $("#table01").bootstrapTable('getSelections');
    //勾选导出
    if (selectEles.length > 0) {
        exportExcel();
    } else {//非勾选导出，判断数量
        var options = $('#table01').bootstrapTable('getOptions');
        var totalRows = options.totalRows;
        if (totalRows >= 6000) {//导出项大于6w，询问导出
            var ele_click = $("#exoprtButton");
            $(".clock_div").show();
            $("#exportExcel").modal('show');
        } else {
            exportExcel();
        }
    }
}
//导出
function exportExcel(){
    
     var selectEles=$("#table01").bootstrapTable('getSelections');
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
            $("#sku").val($.trim($("#sku").val()).replace(/\s+/g,',').replace(/,{2,}/g,','))
        }
        var sku = $("#sku").val().split(',');
        //法人主体
        if($("#legaler").val()==null){
            $("#legaler").val('');
        }
        var legaler = $("#legaler").val();
        
        //调拨单号
        if($("#transfer_no").val()){
            $("#transfer_no").val($.trim($("#transfer_no").val()).replace(/\s+/g,',').replace(/,{2,}/g,','))
        }
        var transfer_no = $("#transfer_no").val().split(',');
        
        //目标仓
        if($("#target_warehouse").val() == null){
            $("#target_warehouse").val('');
        }
        var target_warehouse = $("#target_warehouse").val();
        
        //线路仓
        if($("#pass_warehouse").val() == null){
            $("#pass_warehouse").val('');
        }
        var pass_warehouse = $("#pass_warehouse").val();
        
        //调出仓
        if($("#out_warehouse").val() == null){
            $("#out_warehouse").val('');
        }
        var out_warehouse = $("#out_warehouse").val();
        
        //是否含税
        if($("#is_tax").val() == null){
            $("#is_tax").val('');
        }
        var is_tax = $("#is_tax").val();
        
        //调拨状态
        if($("#transfer_status") == null){
            $("#transfer_status").val('');
        }
        var transfer_status = $("#transfer_status").val();
        
        //开始时间
        var out_from_date = $("#out_from_date").val();
        //结束时间
        var out_to_date = $("#out_to_date").val();
        
        //shipmentid
        if($("#shipmentid").val()){
            $("#shipmentid").val($.trim($("#shipmentid").val()).replace(/\s+/g,',').replace(/,{2,}/g,','))
        }
        var shipmentid = $("#shipmentid").val().split(',');
        
        //fnsku
        if($("#fnsku").val()){
            $("#fnsku").val($.trim($("#fnsku").val()).replace(/\s+/g,',').replace(/,{2,}/g,','))
        }
        var fnsku = $("#fnsku").val().split(',');
        
        //sellersku
        if($("#sellersku").val()){
            $("#sellersku").val($.trim($("#sellersku").val()).replace(/\s+/g,',').replace(/,{2,}/g,','))
        }
        var sellersku = $("#sellersku").val().split(',');
        
        // 店铺
        if($("#accountName").val()){
            $("#accountName").val($.trim($("#accountName").val()).replace(/\s+/g,',').replace(/,{2,}/g,','))
        }
        var accountName = $("#accountName").val().split(',');
    //非勾选的长度
        var options = $('#tab_Warn').bootstrapTable('getOptions');
        var totalRows = options.totalRows;
    
    var parmasStr = "?from_date=" + from_date + "&to_date=" + to_date +  "&sku=" + sku
    + "&legaler=" + legaler + "&transfer_no=" + transfer_no + "&target_warehouse=" + target_warehouse
    + "&pass_warehouse=" +pass_warehouse +"&out_warehouse="+out_warehouse+"&is_tax="+is_tax
    + "&transfer_status="+transfer_status+"&out_from_date=" + out_from_date + "&out_to_date=" + out_to_date
    + "&shipmentid="+shipmentid+"&fnsku="+fnsku+"&sellersku="+sellersku+"&accountName="+accountName+"&limit=" + totalRows;
    //window.open("/report/PurchaseWarn/export" + parmasStr);
    $("#exportExcel").modal("hide");
     window.location.href="/report/transfer/export" + parmasStr;
}