package com.aukey.report.domain;

public class SalePartInfo {
	
	//销售id
	private Integer sales_group_id;
	//采购id
	private Integer purchase_group_id;
	//销售名称
	private String sale_name;
	public Integer getSales_group_id() {
		return sales_group_id;
	}
	public void setSales_group_id(Integer sales_group_id) {
		this.sales_group_id = sales_group_id;
	}
	public Integer getPurchase_group_id() {
		return purchase_group_id;
	}
	public void setPurchase_group_id(Integer purchase_group_id) {
		this.purchase_group_id = purchase_group_id;
	}
	public String getSale_name() {
		return sale_name;
	}
	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}
	
}
