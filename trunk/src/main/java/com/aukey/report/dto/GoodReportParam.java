package com.aukey.report.dto;

import java.util.Date;

public class GoodReportParam {

	// 从
	private Date from_date;

	// 到
	private Date to_date;

	// SKU
	private String sku;

	// 法人主体,下拉框多选
	private String legaler;

	// 往来单位
	private String organization;

	// 单据编号
	private String receipt_no;

	// 仓库,下拉框多选
	private String warehouse;
    private boolean flag;
    
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Date getFrom_date() {
		return from_date;
	}

	public void setFrom_date(Date from_date) {
		this.from_date = from_date;
	}

	public Date getTo_date() {
		return to_date;
	}

	public void setTo_date(Date to_date) {
		this.to_date = to_date;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getLegaler() {
		return legaler;
	}

	public void setLegaler(String legaler) {
		this.legaler = legaler;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getReceipt_no() {
		return receipt_no;
	}

	public void setReceipt_no(String receipt_no) {
		this.receipt_no = receipt_no;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
    
}
