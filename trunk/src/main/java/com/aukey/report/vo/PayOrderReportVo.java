package com.aukey.report.vo;


public class PayOrderReportVo {

	private Integer requestId;// 请款单号
	private String requestOrderId;// 请款单号
	private String billDate;// 请款日期
	private Integer corporationId;
	private String corporationName;// 法人主体
	private Integer supplierId;
	private String supplierName;// 供应商
	private String currency;// 币别
	private String purchaseOrderId;// 采购单号
	private String purchaseOrderDate;// 采购日期
	private String type;// 单据类型
	private Double sumInvoiceMoney;//入库金额
	private Double sumTotalMoney;// 货款金额
	private Double sumReceiMoney;// 请款金额
	private Double sumPayedMoney;// 已付金额
	private Double requestRatio;// 付款比率
	private String payWay;// 结算方式
	private Integer requester;
	private String requesterName;// 请款人
	private Integer buyer;
	private String buyerName;// 采购员
	private Integer requestDeptId;
	private String requestDeptName;// 请款部门
	private String payOrderId;// 付款单号
	private String thirdOrderId;// 第三方订单编号
	private String payStatus;// 付款状态

	public String getRequestOrderId() {
		return requestOrderId;
	}

	public void setRequestOrderId(String requestOrderId) {
		this.requestOrderId = requestOrderId;
	}

	public String getBillDate() {
		return billDate.substring(0, 16);
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getCorporationName() {
		return corporationName;
	}

	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}


	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getRequestDeptId() {
		return requestDeptId;
	}

	public void setRequestDeptId(Integer requestDeptId) {
		this.requestDeptId = requestDeptId;
	}

	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getPurchaseOrderDate() {
		try {
			return purchaseOrderDate.substring(0, 16);			
		} catch (Exception e) {
			return "-";
		}
	}

	public void setPurchaseOrderDate(String purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public String getRequestDeptName() {
		return requestDeptName;
	}

	public void setRequestDeptName(String requestDeptName) {
		this.requestDeptName = requestDeptName;
	}

	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}

	public String getThirdOrderId() {
		return thirdOrderId;
	}

	public void setThirdOrderId(String thirdOrderId) {
		this.thirdOrderId = thirdOrderId;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
	}

	public Integer getCorporationId() {
		return corporationId;
	}

	public void setCorporationId(Integer corporationId) {
		this.corporationId = corporationId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getRequester() {
		return requester;
	}

	public void setRequester(Integer requester) {
		this.requester = requester;
	}

	public Integer getBuyer() {
		return buyer;
	}

	public void setBuyer(Integer buyer) {
		this.buyer = buyer;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public Integer getPrrequestDeptId() {
		return requestDeptId;
	}

	public void setPrrequestDeptId(Integer prrequestDeptId) {
		this.requestDeptId = prrequestDeptId;
	}

	public Double getSumTotalMoney() {
		return sumTotalMoney;
	}

	public void setSumTotalMoney(Double sumTotalMoney) {
		this.sumTotalMoney = sumTotalMoney;
	}

	public Double getSumReceiMoney() {
		return sumReceiMoney;
	}

	public void setSumReceiMoney(Double sumReceiMoney) {
		this.sumReceiMoney = sumReceiMoney;
	}
	
	public Double getSumPayedMoney() {
		return sumPayedMoney;
	}
	
	public void setSumPayedMoney(Double sumPayedMoney) {
		this.sumPayedMoney = sumPayedMoney;
	}

	public Double getRequestRatio() {
		return requestRatio;
	}

	public void setRequestRatio(Double requestRatio) {
		this.requestRatio = requestRatio;
	}

	public Double getSumInvoiceMoney() {
		return sumInvoiceMoney;
	}

	public void setSumInvoiceMoney(Double sumInvoiceMoney) {
		this.sumInvoiceMoney = sumInvoiceMoney;
	}
}
