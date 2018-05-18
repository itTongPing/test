package com.aukey.report.dto;

public class PayOrderReportParam {
	
	private String billBeginDate; // 请款开始日期
	private String billEndDate; // 请款结束日期
	private String requestOrderId;// 请款单号
	private String supplierName; // 供应商
	private String supplierId; // 供应商
	private String corporationName; // 法人主体
	private String corporationId; // 法人主体
	private String purchaseOrderId; // 采购单号
	private String purchaseBeginDate; // 采购开始日期
	private String purchaseEndDate; // 采购结束日期
	private String purchaseDepartment; // 请款部门
	private String payMethod; // 结算方式
	private String payStatus; // 付款状态

	public String getBillBeginDate() {
		return billBeginDate;
	}

	public void setBillBeginDate(String billBeginDate) {
		this.billBeginDate = billBeginDate;
	}

	public String getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getRequestOrderId() {
		return requestOrderId;
	}

	public void setRequestOrderId(String requestOrderId) {
		this.requestOrderId = requestOrderId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getCorporationName() {
		return corporationName;
	}

	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}

	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getPurchaseBeginDate() {
		return purchaseBeginDate;
	}

	public void setPurchaseBeginDate(String purchaseBeginDate) {
		this.purchaseBeginDate = purchaseBeginDate;
	}

	public String getPurchaseEndDate() {
		return purchaseEndDate;
	}

	public void setPurchaseEndDate(String purchaseEndDate) {
		this.purchaseEndDate = purchaseEndDate;
	}

	public String getPurchaseDepartment() {
		return purchaseDepartment;
	}

	public void setPurchaseDepartment(String purchaseDepartment) {
		this.purchaseDepartment = purchaseDepartment;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getSupplierId() {
		return supplierId;
	}
	
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	
	public String getCorporationId() {
		return corporationId;
	}
	
	public void setCorporationId(String corporationId) {
		this.corporationId = corporationId;
	}
}
