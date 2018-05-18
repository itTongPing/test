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
initTable();
//ajax请求
/*$.ajax({
    url : _ctx + "report/purchase/searchCount",
    type : 'get',
    async : false,
    data : getData(),
    success : function(res)
    {
        console.info(res);
        getCount(res);
        //$.jGrowl('请求失败,请重新添加需求');
    },
    error : function()
    {
        $.jGrowl('请求失败');
    }
});*/
/*]]>*/
});

//初始化表格开始
function initTable()
{
    $('#table01').bootstrapTable({
        url: '/report/purchase/search', // 请求后台的URL（*）
        method: 'get', // 请求方式（*）
        striped: false, // 是否显示行间隔色
        cache: true, // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true, // 是否显示分页（*）
        sidePagination: "server", // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1, // 初始化加载第一页，默认第一页
        pageSize: 20, // 每页的记录行数（*）
        pageList: [20, 50, 100, 200], // 可供选择的每页的行数（*）
        queryParams: query_params,// 传递参数（*）
        width: 'auto',
        uniqueId: "sku", // 每一行的唯一标识，一般为主键列
        cardView: false,
        contentType: "application/json",
        queryParamsType: '',
        undefinedText: '-',
        resizable: true, // 列是否可以拖动
        reorderableColumns: true, // 列是否可以重新排序
        showColumns: true, // 是否显示自定义列，就是那个下拉显示列打勾的
        fixedWindow: true,//新增加的测试代码
        columns: [
            {
                field: 'legaler',
                title: '法人主体',
                align: 'center'
            },
            {
                field: 'supplier',
                title: '供应商',
                align: 'center'
            },
            {
                field: 'purchase_no',
                title: '采购单号',
                align: 'center'
            },
            {
                field: 'purchase_date',
                title: '采购日期',
                align: 'center'
            },
            {
                field: 'sku',
                title: 'sku',
                align: 'center'
            },
            {
                field: 'sku_name',
                title: 'sku名称',
                align: 'center',
                classes: 'skuNameWidth',
                formatter: function (value, row) {
                    return '<span class="text_over" title="' + row.sku_name + '">' + row.sku_name + '</span>';
                }
            },
            {
                field: 'tax_rate',
                title: '增值税率',
                align: 'center'
            },
            {
                field: 'purchase_count',
                title: '采购数量',
                align: 'center',
                class: 'purchaseCountClass'
            },
            {
                field: 'price_wihtout_tax',
                title: '不含税单价',
                align: 'center'
            },
            {
                field: 'price_tax',
                title: '含税单价',
                align: 'center'
            },
            {
                field: 'purchase_sum',
                title: '采购金额',
                align: 'center',
                class: 'purchaseSumClass'
            },
            /*{
                field: 'purchaseCurrencySum',
                title: '采购本币金额',
                align: 'center',
                class: 'purchaseCurrencySum'
            },*/
            {
                field: 'purchase_money_type',
                title: '采购币别',
                align: 'center'
            },
            {
                field: 'department',
                title: '采购部门',
                align: 'center'
            },
            {
                field: 'deptNamexq',
                title: '需求部门',
                align: 'center'
            },
            {
                field: 'stock_number',
                title: '入库单号',
                align: 'center'
            },
            {
                field: 'stock_date',
                title: '入库日期',
                align: 'center'
            },
            {
                field: 'stock_name',
                title: '仓库',
                align: 'center'
            },
            {
                field: 'stock_count',
                title: '入库数量',
                align: 'center',
                class: 'stockCountClass'
            },
            {
                field: 'stock_sum',
                title: '入库金额',
                align: 'center',
                class: 'stockSumClass'
            },
            /*{
                field: 'stockCurrencySum',
                title: '入库本币金额',
                align: 'center',
                class: 'stockCurrencySum'
            },*/
            {
                field: 'category',
                title: '品类',
                align: 'center'
            },
            {
                field: 'bill_name',
                title: '开票品名',
                align: 'center',
                formatter: function (value, row) {
                    if(row.include_tax=='否') {
                        return '-';
                    }else{
                        return value;
                    }
                }
            },
            {
                field: 'bill_unit',
                title: '开票单位',
                align: 'center',
                formatter: function (value, row) {
                    if(row.include_tax=='否') {
                        return '-';
                    }else{
                        return value;
                    }
                }
            },
            {
                field: 'no_bill_count',
                title: '未开数量',
                align: 'center',
                class: 'noBillCountClass',
                formatter: function (value, row) {
                    if(row.include_tax=='否') {
                        return '-';
                    }else{
                        return value;
                    }
                }
            },
            {
                field: 'noMakeInvoice',
                title: '未开票金额',
                align: 'center',
                class: 'noMakeInvoiceClass',
                formatter: function (value, row) {
                    if(row.include_tax=='否')
                    {
                        return '-';
                    }else{
                        return fomatFloat(row.no_bill_count*row.price_tax,2);
                    }
                }
            },
            /*{
                field: 'noMakeInvoiceCurrency',
                title: '未开票本币金额',
                align: 'center',
                class: 'noMakeInvoiceCurrency'
            },*/
            {
                field: 'makeInvoice',
                title: '已开票金额',
                align: 'center',
                class: 'makeinvoiceClass',
                formatter: function (value, row) {
                    if(row.include_tax=='否')
                    {
                        return '-';
                    }else{
                        return fomatFloat((row.stock_count-row.no_bill_count)*row.price_tax,2);
                    }
                }
            },
            /*{
                field: 'makeInvoiceCurrency',
                title: '已开票本币金额',
                align: 'center',
                class: 'makeInvoiceCurrency'
            },*/
            {
                field: 'invoiceCode',
                title: '发票号',
                align: 'center',
            },
            {
                field: 'bill_status',
                title: '开票状态',
                align: 'center'
            },
            {
                field: 'brand',
                title: '品牌',
                align: 'center'
            },
            {
                field: 'version',
                title: '型号',
                align: 'center'
            },
            {
                field: 'include_tax',
                title: '含税',
                align: 'center'
            },
            {
                field: 'pay_type_name',
                title: '结算方式',
                align: 'center'
            }
        ],
        onLoadSuccess : function(data)
        {
            var data = $('#table01').bootstrapTable('getData',true);
            var pageNumber = $('#table01').bootstrapTable('getOptions').pageNumber;
            if (data.length == 0 && pageNumber != 1)
            {
                $('#table01').bootstrapTable('selectPage',1);
            }
            $("input[disabled]").parents('.bs-checkbox').attr('title','加载中无法选择')
            $(".fixed-table-loading").hide();
            if(window.once!=1) $.jGrowl('查询成功');
            flyer.scrollBar($('#table01')); //初始化滚动条
        },
        onLoadError:function(data){
            $.jGrowl('查询出错');
        }
    });
}
//初始化表格结束
//初始化化参数
/***********************************************************************/
//开始时间
var from_date = $("#from_date").val();
//结束时间
var to_date = $("#to_date").val();
//sku
if($("#sku").val()){
    $("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
}
var sku = $("#sku").val().split(',');
//供应商名称
var supplier =  $("#supplyAlias").val();
//法人主体
var legaler = $("#legaler").val();
//采购单号
if($("#purchase_no").val()){
    $("#purchase_no").val($("#purchase_no").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
}
var purchase_no = $("#purchase_no").val().split(',');
//仓库
var stock_id = $("#stock_id").val();
//采购员
if($("#buyer").val()){
    $("#buyer").val($("#buyer").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
}
var buyer = $("#buyer").val().split(',');
//是否含税
var include_tax = $("#include_tax").val();
//采购部门
var department = $("#department").val();
// 需求部门
var dept_xq = $("#departmentSale").val();
//结算类型
var pay_type = $("#pay_type").val();
//结算方式
var payType = $("#payMethod").val();
//开始时间
var from_purchase_date = $("#from_purchase_date").val();
//结束时间
var to_purchase_date = $("#to_purchase_date").val();
/***********************************************************************/
/****导出开始*****/
function exportExcel(){
    //获取参数
    var paramsL='?from_date='+from_date+'&to_date='+to_date+
                '&sku='+sku+'&supplier='+supplier+'&purchase_no='+purchase_no+
                '&buyer='+buyer+'&pay_type='+pay_type+'&payType='+payType+'&stock_id='+stock_id+
                '&include_tax='+include_tax+'&department='+department+'&legaler='+legaler+'&from_purchase_date='+from_purchase_date+
                '&to_purchase_date='+to_purchase_date+'&dept_xq='+dept_xq;
    var allowExport = true;
    var Data = $(this).serialize();
    $.ajax({
        url:'/report/purchase/preExport'+paramsL,
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
    if(allowExport){
        window.open('/report/purchase/export'+paramsL);
    }
}
/****导出结束*****/
//表格查询
var searchResult = function(){
    window.search_click = true;
    $('#table01').bootstrapTable('refresh',{
        query:{
            pageNumber:1
        }
    });
//    $('#table01').bootstrapTable('selectPage',1);
    
    //ajax请求
    /*$.ajax({
        url : _ctx + "report/purchase/searchCount",
        type : 'get',
        async : false,
        data : getData(),
        success : function(res)
        {
            console.info(res);
            getCount(res);
            //$.jGrowl('请求失败,请重新添加需求');
        },
        error : function()
        {
            $.jGrowl('请求失败');
        }
    });
*/  
    
}


function fomatFloat(src,pos){      
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);     
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
    $('#queryForm2')[0].reset();// reset方法是javascript原声的 所以需要转换为js对象
}

//查询以及导出数据获取
function getData(){
    //开始时间
    from_date = $("#from_date").val();
    //结束时间
    to_date = $("#to_date").val();
    //sku
    if($("#sku").val()){
        $("#sku").val($("#sku").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
    }
    sku = $("#sku").val().split(',');
    //供应商名称
    supplier =  $("#supplyAlias").val();
    //法人主体
    legaler = $("#legaler").val();
    //采购单号
    if($("#purchase_no").val()){
        $("#purchase_no").val($("#purchase_no").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
    }
    purchase_no = $("#purchase_no").val().split(',');
    //仓库
    stock_id = $("#stock_id").val();
    if(stock_id == -1){
        var stock_ids = [];
        $("#stock_id").find('option').each(function(){
            if(-1 != $(this).val()){
                stock_ids.push($(this).val());
            }
        });
        stock_id = stock_ids.join(',');
    }
    //stock_id = $("#stock_id").val();
    //采购员
    if($("#buyer").val()){
        $("#buyer").val($("#buyer").val().trim().replace(/\s+/g,',').replace(/,{2,}/g,','))
    }
    buyer = $("#buyer").val().split(',');
    //是否含税
    include_tax = $("#include_tax").val();
    //采购部门
    department = $("#department").val();
    // 需求部门
    dept_xq = $("#departmentSale").val();
    //结算类型
    var pay_type = $("#pay_type").val();
    //结算方式
    payType = $("#payMethod").val();
    //开始时间
    from_purchase_date = $("#from_purchase_date").val();
    //结束时间
    to_purchase_date = $("#to_purchase_date").val();
    if(payType[0]=='-1'){
        switch($("#pay_type").val()[0]){
            case '-1':
                payType = ['-1'];
            break;
            case '0':
                payType.pop();
                $("#payMethod option").each(function(index,ele){
                    payType.push($(ele).val());
                });
                payType.shift();
            break;
            case '1':
                payType.pop();
                $("#payMethod option").each(function(index,ele){
                    payType.push($(ele).val());
                });
                payType.shift();
            break;
        }
    }
    var exportData = {
            from_date:from_date,
            sku:sku,
            to_date:to_date,
            supplier:supplier,
            legaler:legaler,
            purchase_no:purchase_no,
            stock_id:stock_id,
            include_tax:include_tax,
            department:department,
            dept_xq:dept_xq,
            pay_type:pay_type,
            payType:payType,
            from_purchase_date:from_purchase_date,
            to_purchase_date:to_purchase_date,
            buyer:buyer
        };
    //console.log(exportData);
    return exportData
}


//表格加载完成回调事件
$(function(){
    /*//导出提交表单
    $("#export_btn").click(function(){
        //支付类型
        var payType = $("#payMethod").val();
        if(payType[0]=='-1'){
            switch($("#pay_type").val()[0]){
                case '-1':
                    payType = ['-1'];
                break;
                case '0':
                    payType.pop();
                    $("#payMethod option").each(function(index,ele){
                        payType.push($(ele).val());
                    });
                    payType.shift();
                break;
                case '1':
                    payType.pop();
                    $("#payMethod option").each(function(index,ele){
                        payType.push($(ele).val());
                    });
                    payType.shift();
                break;
            }
            $("#payMethod").val(payType);
        }
        $("#queryForm").submit();
    });*/
    //点击enter触发搜索事件
    $(document).keydown(function(event){ 
        if(event.keyCode==13){ 
            searchResult();
        }
    });
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
        //表头悬浮
        if(loadCount!=2 && window.search_click){
//            $.jGrowl("查询成功");
            search_click = false;
        }
        
        var getData=$('#table01').bootstrapTable("getData");
        //获取采购数量
        var purchase_count="purchase_count";
        var allPurchaseCount=calculationData(getData,purchase_count);   
        //获取采购金额
        var purchase_sum="purchase_sum";
        var allPurchaseSum=calculationData(getData,purchase_sum);
        //获取入库数量
        var stock_count="stock_count";
        var allstock_count=calculationData(getData,stock_count);
        //获取入库金额
        var stock_sum="stock_sum";
        var allstock_sum=calculationData(getData,stock_sum).toFixed(2);
        
        //入库本币金额
        //var stockCurrencySum = "stockCurrencySum";
        //var allstockCurrencySum = calculationData(getData,stockCurrencySum).toFixed(2);
        //获取未开数量
        var no_bill_count="no_bill_count";
        //var allno_bill_count=calculationData(getData,no_bill_count);
        var allno_bill_count=calcunoBillCount(getData,no_bill_count);
        
        //获得已开票金额
        var makeInvoice = "makeInvoice";
        var all_makeInvoice = calcumakeInvoice(getData,makeInvoice);
        
        //获得已开票本币金额
        //var makeInvoiceCurrency = "makeInvoiceCurrency";
        //var allMakeInvoiceCurrency = calculationData(getData,makeInvoiceCurrency).toFixed(2);
        //获得未开票本币金额
        //var noMakeInvoiceCurrency = "noMakeInvoiceCurrency";
        //var allNoMakeInvoiceCurrency = calculationData(getData,noMakeInvoiceCurrency).toFixed(2);
        
        //获取未开票金额
        var allnoMakeInvoice=0;
        $.each(getData,function(index,value){
            if(value.include_tax=='否')
            {
            //  allnoMakeInvoice+=fomatFloat(value.no_bill_count*value.price_wihtout_tax,2);
            }else{
                allnoMakeInvoice+=fomatFloat(value.no_bill_count*value.price_tax,2);
            }
        })
        //已开票金额
//      var makeInvoice="makeInvoice";
//      var allmakeInvoice=calculationData(getData,makeInvoice);
//      console.log(allmakeInvoice);
//      $("#table01 tbody").append('<tr><td>总数</td><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td><td>'+allPurchaseCount+'</td><td>-</td><td>-</td><td>'+allPurchaseSum+'</td><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td><td>'+allstock_count+'</td><td>'+allstock_sum+'</td><td>-</td><td>-</td><td>-</td><td>'+allno_bill_count+'</td><td>'+allnoMakeInvoice.toFixed(2)+'</td><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td><td>-</td></tr>')
        
        $('.purchaseCountClass').eq(0).find(".th-inner").text('采购数量(' + allPurchaseCount + ')' );
        $('.purchaseSumClass').eq(0).find(".th-inner").text('采购金额(' + allPurchaseSum.toFixed(2) + ')' );
        $('.stockCountClass').eq(0).find(".th-inner").text('入库数量(' + allstock_count + ')' );
        $('.stockSumClass').eq(0).find(".th-inner").text('入库金额(' + allstock_sum + ')' );
        //$('.stockSumClass').eq(0).find(".th-inner").text('入库金额(' + allstockCurrencySum + ')' );
        $('.noBillCountClass').eq(0).find(".th-inner").text('未开数量(' + allno_bill_count + ')' );
        $('.noMakeInvoiceClass').eq(0).find(".th-inner").text('未开票金额(' + allnoMakeInvoice.toFixed(2) + ')' );
        $('.makeinvoiceClass').eq(0).find(".th-inner").text('已开票金额(' + all_makeInvoice + ')' );
        //$('.stockCurrencySum').eq(0).find(".th-inner").text('入库本币金额(' + allstockCurrencySum + ')' );
        //$('.makeInvoiceCurrency').eq(0).find(".th-inner").text('已开票本币金额(' + allMakeInvoiceCurrency + ')' );
        //$('.noMakeInvoiceCurrency').eq(0).find(".th-inner").text('未开票本币金额(' + allNoMakeInvoiceCurrency + ')' );
        
    })
    
    
    function  calcunoBillCount(getData,className){
        
        var number=0;
        $.each(getData,function(index,value){
            
            if(value["include_tax"] != '否') {
                number += value["no_bill_count"];
            }
        });
        return number;
    }
    
    function calcumakeInvoice(getData,className){
        var number=0;
        $.each(getData,function(index,value){
                
                if(value['include_tax'] != '否'){
                    
                    number+= (fomatFloat((value['stock_count']-value['no_bill_count'])*value['price_tax'],2));
                }
            
        });
        return number;
    }
    
    function calculationData(getData,className){
        var number=0;
        $.each(getData,function(index,value){
            if(value[className]!=null){
                number+=value[className]
            }
        });
        return number;
    }
    //表格加载失败回调方法
    $("#table01").on("load-error.bs.table",function(){
        //表头悬浮
        //查询错误提示
        $.jGrowl("查询出错");
        
    })
    
})

//关联下拉菜单
$(function(){
    //初始化
    var preArr = [],aftArr = [],allArr = [];
    aftArr.push($("<option value='-1' selected='true'>全部</option>"));
    preArr.push($("<option value='-1' selected='true'>全部</option>"));
    $("#payMethod option").each(function(){
        //将两种情况的拆开来
        allArr.push($(this));
        switch($(this).attr("data-type")){
            case '0':
                aftArr.push($(this));
            break;
            case '1':
                preArr.push($(this));
            break;
        }
    });
    //关联选择
    $("#pay_type").on("change",function(){
        $("#payMethod").empty();
        switch($(this).val()[0]){
            case '0':
                $.each(aftArr,function(index,ele){
                    $("#payMethod").append(ele);
                });
                $("#payMethod").selectpicker("refresh");
                $("#payMethod").selectpicker("val",'-1');
            break;
            case '1':
                $.each(preArr,function(index,ele){
                    $("#payMethod").append(ele);
                });
                $("#payMethod").selectpicker("refresh");
                $("#payMethod").selectpicker("val",'-1');
            break;
            default:
                $.each(allArr,function(index,ele){
                    $("#payMethod").append(ele);
                });
                $("#payMethod").selectpicker("refresh");
                $("#payMethod").selectpicker("val",'-1');
            break;
        }
    });
});
/*$(function(){
    $("#queryForm").on('submit',function(){
        //获取对应的时间间隔
        var paramsL='?from_date='+from_date+'&to_date='+to_date+
        '&sku='+sku+'&supplier='+supplier+'&purchase_no='+purchase_no+
        '&buyer='+buyer+'&pay_type='+pay_type+'&payType='+payType+'&stock_id='+stock_id+
        '&include_tax='+include_tax+'&department='+department+'&legaler='+legaler+'&from_purchase_date='+from_purchase_date+
        '&to_purchase_date='+to_purchase_date+'&dept_xq='+dept_xq;
        var allowExport = true;
        var Data = $(this).serialize();
        $.ajax({
            url:'/report/purchase/preExport'+paramsL,
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
});*/
//function makeInvoice(value, row, index)
//{
//  if(value==null||value==undefined){
//      return value="显示";
//  }
//}

/*************供应商联想搜索******************/
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
                                + '\',\'' + obj.stockid + '\')">' + obj.alias + '</div>');
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
/*******************************/
/****汇总金额入库数量****/

/*function getCount(res)
{
    console.info(res);
    var data = res.data;
    $("#recordCount").remove();
    var $node = $("<div id='recordCount'>入库总数量:"+data.stockCount+"              入库总金额:"+data.stockMoney.toFixed(2)+"</div>");
    $('#table01').after($node);
}
*/

/****汇总金额入库数量****/
