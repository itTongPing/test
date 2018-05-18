/**初始化表格**/

var TableInit = function()
{
	
	var stockType = null;
	var oTableInit = new Object();
	//初始化表格
	oTableInit.Init = function()
	{
		var purchaseTransferColumns = [];
		//-----列名前部分
		purchaseTransferColumns.push({
            field : 'purchaseOrderId',
            title : '采购单号',
            align : 'center',
            width : 100
        },
        {
            field : 'purchaseDemandId',
            title : '需求单号',
            align : 'center',
            width : 100
        },
    	{
            field : 'warehouseName',
            title : '目的仓',
            align : 'center',
            width : 100
        });
		
		//------列名中部
		var url = window.location.href;
		stockType = url.charAt(url.length - 1);
		if(stockType == '1'){
			purchaseTransferColumns.push(
					{
                        field: 'quantity',
                        title: '采购数量',
                        align: 'center',
                        width: 150,
                        formatter:function(value,row)
                        {
                        	return parseInt(value);
                        }
                    },
                    {
                        field: 'inWayQuantity',
                        title: '在途数量',
                        align: 'center',
                        width: 150,
                        formatter:function(value,row)
                        {
                        	return parseInt(value);
                        }
                    }
			);	
		}else if(stockType == '2'){
			purchaseTransferColumns.push(
					{
                        field: 'quantity',
                        title: '采购体积',
                        align: 'center',
                        width: 150,
                        formatter:function(value,row)
                        {
                        	return parseFloat(value);
                        }
                    },
                    {
                        field: 'inWayQuantity',
                        title: '在途体积',
                        align: 'center',
                        width: 150,
                        formatter:function(value,row)
                        {
                        	return parseFloat(value);
                        }
                    }
			);	
		}else{
			purchaseTransferColumns.push(
					{
                        field: 'quantity',
                        title: '采购金额',
                        align: 'center',
                        width: 150,
                        formatter:function(value,row)
                        {
                        	return parseFloat(value);
                        }
                    },
                    {
                        field: 'inWayQuantity',
                        title: '在途金额',
                        align: 'center',
                        width: 150,
                        formatter:function(value,row)
                        {
                        	return parseFloat(value);
                        }
                    }
			);	
		}
		
		//----列名尾部
		purchaseTransferColumns.push( {
            field: 'deliverDate',
            title: '采购审核日期',
            align: 'center',
            width: 150,
            formatter:function(value,row)
            {
            	var date = new Date(value);
            	if(value==null)
            	{
            		return"-"
            	}else
            	{
            		return formatDate(date);
            	}
            }
        });
		
		
		bootstrapTable = $('#purchaseTransferTable').bootstrapTable(
	            {
	                url:'/report/centerStock/search/purchaseTransfer', // 请求后台的URL（*）
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
	                //uniqueId: "packageId", // 每一行的唯一标识，一般为主键列
	                cardView: false,
	                contentType: "application/json",
	                //responseHandler : responseHandler,
	                queryParamsType: '',
	                resizable: true, //列是否可以拖动
	                fixedWindow:true,// 表头浮动
	                reorderableColumns: true, //列是否可以重新排序
	                //showColumns: true, //是否显示自定义列，就是那个下拉显示列打勾的
	                undefinedText: "-",//为undefined的时候显示的文本
	                onClickCell: function (a, b, c, d) {
	                    var args = Array.prototype.slice.call(arguments, 1);
	                },
	                columns : purchaseTransferColumns,
	                    onLoadSuccess: function (data) {
	                    	flyer.scrollBar($('#purchaseTransferTable')); //初始化滚动条

	                    },
	                    onLoadError:function(data){
	                    	
	                    }
	            });
	}
	
	//var queryParamsBySku = new Object();
	oTableInit.queryParams = function (params)
	{
		var sku = $("#sku").val().trim().replace(/^\s+|\s+$/g, ""); // sku;
		params.sku = sku;
		var stocks = $("#stocks").val().join(',');//仓库
        if(stocks==""){
    		var stocksArr = [];
    		$("#stocks").find('option').each(function(){
    			if(-1 != $(this).val()){
    				stocksArr.push($(this).val());
    			}
    		});
    		stocks = stocksArr.join(',');
    	}
        params.stocks = stocks;
        params.stockType=stockType;
        return params
	}
	return oTableInit;
}
//初始化界面
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
	$("#purchaseTransferTable").bootstrapTable('destroy');
	var oTable = new TableInit();
    oTable.Init();
}
//重置搜索条件
function searchReset()
{
	$("#stocks").selectpicker('val','-1');
}

//格式化时间
function formatDate(date) {
	  return date.getFullYear()
	        + "-" + (date.getMonth()>8?(date.getMonth()+1):"0"+(date.getMonth()+1))
	        + "-" + (date.getDate()>9?date.getDate():"0"+date.getDate());
}
