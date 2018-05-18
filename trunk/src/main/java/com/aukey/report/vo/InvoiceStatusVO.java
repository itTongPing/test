package com.aukey.report.vo;

public class InvoiceStatusVO {

	// 采购单号
	private String purchase_order_id;

	// sku
	private String sku;

	// 开票数量
	private Integer count;

	public String getPurchase_order_id() {
		return purchase_order_id;
	}

	public void setPurchase_order_id(String purchase_order_id) {
		this.purchase_order_id = purchase_order_id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
