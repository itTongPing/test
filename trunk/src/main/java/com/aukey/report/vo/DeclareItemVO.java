package com.aukey.report.vo;

import java.util.Date;

public class DeclareItemVO {

	// 发票单号
	private String bill_no;
	// 品名
	private String category;
	// 含税单价
	private Double price_tax;
	// 不含税单价
	private Double price_wihtout_tax;
	// 开票数量
	private Integer bill_count;
	// 开票时间
	private Date sign_date;
	// 录入人
	private String operator;
	// 录入时间
	private Date entry_date;
	// 发票数量
	private Integer invoice_count;
	// 剩余数量
	private Integer surplus_count;
	// 开票状态
	private String invoice_status;
	// 发票编码
	private String invoice_serial_number;

	public String getBill_no() {
		return bill_no;
	}

	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getPrice_tax() {
		return price_tax;
	}

	public void setPrice_tax(Double price_tax) {
		this.price_tax = price_tax;
	}

	public Double getPrice_wihtout_tax() {
		return price_wihtout_tax;
	}

	public void setPrice_wihtout_tax(Double price_wihtout_tax) {
		this.price_wihtout_tax = price_wihtout_tax;
	}

	public Integer getBill_count() {
		return bill_count;
	}

	public void setBill_count(Integer bill_count) {
		this.bill_count = bill_count;
	}

	public Date getSign_date() {
		return sign_date;
	}

	public void setSign_date(Date sign_date) {
		this.sign_date = sign_date;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getEntry_date() {
		return entry_date;
	}

	public void setEntry_date(Date entry_date) {
		this.entry_date = entry_date;
	}

	public Integer getInvoice_count() {
		return invoice_count;
	}

	public void setInvoice_count(Integer invoice_count) {
		this.invoice_count = invoice_count;
	}

	public Integer getSurplus_count() {
		return surplus_count;
	}

	public void setSurplus_count(Integer surplus_count) {
		this.surplus_count = surplus_count;
	}

	public String getInvoice_status() {
		return invoice_status;
	}

	public void setInvoice_status(String invoice_status) {
		this.invoice_status = invoice_status;
	}

	public String getInvoice_serial_number() {
		return invoice_serial_number;
	}

	public void setInvoice_serial_number(String invoice_serial_number) {
		this.invoice_serial_number = invoice_serial_number;
	}

}
