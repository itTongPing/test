window.loadCount = 1;//表格刷新次数
$(function () {
    /*var msgstr=$("#msg").val();
     if(msgstr!=null && msgstr!=""){
     $.jGrowl(decodeURIComponent($("#msg").val()));
     }*/

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
var searchResult = function () {
    window.search_click = true;
    $('#table02').bootstrapTable('refresh', {
        query: {
            pageNumber: 1
        }
    });
    $('#table02').bootstrapTable('selectPage', 1);
}


//查询表格参数
function query_params(params) {
    $.ajaxSetup({
        traditional: true
    });
    var paramdata = getData();//获取数据
    params.limit = params.pageSize;
    $.extend(params, paramdata);//合并数据
    loadCount++;
    return params;
}

//高级搜索

function searchH() {
    $("#searchH").toggle();
}

//重置按钮
function searchReset() {
    // 点击搜索按钮之后 清空高级搜索里面的数据 重置为默认值
    $('.search-form-div select').selectpicker('val', '-1');
    $('#queryForm')[0].reset();// reset方法是javascript原声的 所以需要转换为js对象
}

//查询以及导出数据获取
function getData() {
    //开始时间
    var from_date = $("#from_date").val();
    //结束时间
    var to_date = $("#to_date").val();
    //供应商名称
    if ($("#supplier").val()) {
        $("#supplier").val($("#supplier").val().trim().replace(/\s+/g, ',').replace(/,{2,}/g, ','))
    }
    var supplier = $("#supplier").val().split(',');
    //法人主体
    var legaler = $("#legaler").val();
    //采购部门
    var department = $("#department").val();
    //采购单号
    if ($("#purchase_no").val()) {
        $("#purchase_no").val($("#purchase_no").val().trim().replace(/\s+/g, ',').replace(/,{2,}/g, ','))
    }
    var purchase_no = $("#purchase_no").val().split(',');
    //含税
    var include_tax = $("#include_tax").val();
    //付款状态
    var pay_status = $("#pay_status").val();
    //开票状态
    var invoice_status = $("#invoice_status").val();
    //合同状态
    var contract_status = $("#contract_status").val();
    var exportData = {
        from_date: from_date,
        to_date: to_date,
        supplier: supplier,
        legaler: legaler,
        department: department,
        purchase_no: purchase_no,
        include_tax: include_tax,
        pay_status: pay_status,
        invoice_status: invoice_status,
        contract_status: contract_status
    };
    return exportData
}


//表格加载完成回调事件
$(function () {
    //法人主体下拉列表参数处理
    $("#legaler").prev('.dropdown-menu.open').find('.text').each(function () {
        var newStringArr = $(this).text().split("=")
        $(this).text(newStringArr[0]);
        $(this).val(newStringArr[1]);
    })
    $("#legaler option").not(':first').each(function () {
        var newStringArr = $(this).text().split("=")
        $(this).text(newStringArr[0]);
        $(this).val(newStringArr[1]);
    })

    $("#table02").on("load-success.bs.table", function () {
        if (window.search_click) {

        }
        window.search_click = false;
        if (loadCount != 2 && window.search_click) {
            $.jGrowl("查询成功");
        }
    })
    //表格加载失败回调方法
    $("#table02").on("load-error.bs.table", function () {
        window.search_click = false;
        //查询错误提示
        $.jGrowl("查询出错");

    })

})


$('#table02').bootstrapTable({
    url: '/report/purchase/exe/search',
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
    columns: [{
        field: 'supplier_name',
        /*税费属于订单的   所以放到上面来  */
        title: '供应商名称',
        align: 'center',
        class: 'firstTd'
    }, {
        field: 'legaler_name',
        title: '法人主体',
        align: 'center',
        class: 'firstTd'
    }, {
        field: 'purchase_no',
        title: '采购单号',
        align: 'center',
        class: 'firstTd'
    }, {
        field: 'purchase_date',
        title: '采购日期',
        align: 'center',
        class: 'firstTd'
    }, {
        field: 'purchase_amount_all',
        title: '采购金额',
        align: 'center',
        class: 'firstTd'
    }, {
        field: 'purchase_currency',
        title: '采购币别',
        align: 'center',
        class: 'firstTd'
    }, {
        field: 'purchaser_name',
        title: '采购员',
        align: 'center',
        class: 'firstTd'
    },
        {
            field: 'department_name',
            title: '采购部门',
            align: 'center',
            class: 'firstTd'
        }, {
            field: 'inventory_warehouse_name',
            title: '入库仓库',
            align: 'center',
            class: 'firstTd warehouse_name'
        }, {
            field: 'un_inventory_amount',
            title: '未入库金额',
            align: 'center',
            class: 'firstTd'
        }, {
            field: 'inventory_amount',
            title: '入库金额',
            align: 'center',
            class: 'firstTd'
        }, {
            field: 'inventory_status',
            title: '入库状态',
            align: 'center',
            class: 'firstTd'
        }, {
            field: 'un_payment',
            title: '未付金额',
            align: 'center',
            class: 'firstTd'
        }, {
            field: 'payment_all',
            title: '已付合计',
            align: 'center',
            class: 'firstTd'
        }, {
            field: 'payment_status',
            title: '支付状态',
            align: 'center',
            class: 'firstTd'
        }, {
            field: 'payment',
            title: '已付金额',
            align: 'center',
            formatter: mergeCell('payment')
        }, {
            field: 'currency_pay',
            title: '支付币别',
            align: 'center',
            formatter: mergeCell('currency_pay')
        }, {
            field: 'exchange_rate',
            title: '汇率',
            align: 'center',
            formatter: mergeCell('exchange_rate')
        }
//      , {
//        field: 'cost_surplus',
//        title: '付款人',
//        align: 'center',
//        class: 'firstTd'
//    }
        , {
            field: 'is_tax',
            title: '含税',
            align: 'center',
            formatter: function (value) {
                if (value == '含税') {
                    return '是';
                }
                return '否';
            },
            class: 'firstTd'
        }, {
            field: 'bill_status',
            title: '开票状态',
            align: 'center',
            class: 'firstTd'
        }, {
            field: 'un_bill_amount',
            title: '未开票金额',
            align: 'center',
            class: 'firstTd'
        }, {
            field: 'bill_amount',
            title: '已开票金额',
            align: 'center',
            class: 'firstTd'
        }, {
            field: 'bill_contract',
            title: '合同状态',
            align: 'center',
            class: 'firstTd'
        }
    ],
    onLoadSuccess: function(data){
        flyer.scrollBar($('#table02'));
    }

});

function mergeCell(dataName, cellNum) {
    if (cellNum != 1) {
        return function (data, row) {
            var construct = '';
            var arrLength = row.payment.length;
            row.payment.forEach(function (ele, index) {
                if (index != arrLength - 1) {
                    construct += '<div class = "tdBox">' + ele[dataName] + '</div>';
                } else {
                    construct += '<div class = "lastBox">' + ele[dataName] + '</div>';
                }
            })
            return construct;
        }
    } else {
        return function (data, row) {
            return row.detail[0][dataName];
        }
    }
}
$(function () {
    $("#queryForm").on('submit', function () {
        //获取对应的时间间隔
        var allowExport = true;
        var Data = $(this).serialize();
        $.ajax({
            url: '/report/purchase/exe/preExport',
            contentType: 'json',
            async: false,
            method: 'get',
            data: Data,
            success: function (req) {
                if (!req.success) {
                    $.jGrowl(req.message);
                    allowExport = false;
                }
            },
            error: function (req) {
                $.jGrowl('请求失败');
                allowExport = false;
            }
        })
        if (!allowExport) {
            return false;
        }
    })
});

function form_submit() {
    var selectEles = $("#table02").bootstrapTable('getSelections');
    if (selectEles.length > 0) {
        $("#queryForm").submit();
    } else {
        var options = $('#table02').bootstrapTable('getOptions');
        var totalRows = options.totalRows;
        if (totalRows >= 60000) {
            var ele_click = $("#exoprtButton");
            //上锁让其他操作被禁止
            $(".clock_div").show();
            $("#exportExcel").modal('show');
        } else {
            $("#queryForm").submit();
        }
    }
}

function exportExcel() {
    var exportData = getData();
    $.ajax({
        url: "/report/purchase/exe/export",
        type: "get",
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        data: exportData,
        success: function (data) {
            $("#exportExcel").modal("hide");
            if (data != null && data.errCode == 1) {
                $.jGrowl('发送邮件成功，请稍候查收');
            }
            else {
                $.jGrowl('请求错误，请重试！');
            }
        },
        error: function () {
            $("#exportExcel").modal("show");
            $.jGrowl('请求错误，请重试！');
        }
    });
    //$("#queryForm").submit();
}