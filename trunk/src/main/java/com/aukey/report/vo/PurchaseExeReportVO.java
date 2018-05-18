package com.aukey.report.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseExeReportVO {

	// 供应商名称
	private String supplier_name;

	// 法人主体
	private String legaler_name;

	// 采购单号
	private String purchase_no;

	// 采购日期
	private Date purchase_date;

	// 采购金额
	private Double purchase_amount_all;

	// 采购币别
	private String purchase_currency;

	// 采购员
	private String purchaser_name;

	// 采购部门
	private String department_name;

	// 入库仓库
	private String inventory_warehouse_name;

	// 入库金额
	private Double inventory_amount;

	// 未入库金额
	private Double un_inventory_amount;

	// 入库状态
	private String inventory_status;

	// 未付金额
	private Double un_payment;

	// 已付合计
	private Double payment_all;

	// 支付状态
	private String payment_status;

	// 已付金额，支付币别，汇率，付款人 (子项)
	private List<PayDtail> payment = new ArrayList<PayDtail>();

	// 是否含税
	private String is_tax;

	// 开票状态
	private String bill_status;

	// 未开票金额
	private Double un_bill_amount;

	// 已开票金额
	private Double bill_amount;

	// 开票合同
	private String bill_contract;

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public String getLegaler_name() {
		return legaler_name;
	}

	public void setLegaler_name(String legaler_name) {
		this.legaler_name = legaler_name;
	}

	public String getPurchase_no() {
		return purchase_no;
	}

	public void setPurchase_no(String purchase_no) {
		this.purchase_no = purchase_no;
	}

	public Date getPurchase_date() {
		return purchase_date;
	}

	public void setPurchase_date(Date purchase_date) {
		this.purchase_date = purchase_date;
	}

	public Double getPurchase_amount_all() {
		return purchase_amount_all;
	}

	public void setPurchase_amount_all(Double purchase_amount_all) {
		this.purchase_amount_all = purchase_amount_all;
	}

	public String getPurchase_currency() {
		return purchase_currency;
	}

	public void setPurchase_currency(String purchase_currency) {
		this.purchase_currency = purchase_currency;
	}

	public String getPurchaser_name() {
		return purchaser_name;
	}

	public void setPurchaser_name(String purchaser_name) {
		this.purchaser_name = purchaser_name;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public String getInventory_warehouse_name() {
		return inventory_warehouse_name;
	}

	public void setInventory_warehouse_name(String inventory_warehouse_name) {
		this.inventory_warehouse_name = inventory_warehouse_name;
	}

	public Double getInventory_amount() {
		return inventory_amount;
	}

	public void setInventory_amount(Double inventory_amount) {
		this.inventory_amount = inventory_amount;
	}

	public Double getUn_inventory_amount() {
		return un_inventory_amount;
	}

	public void setUn_inventory_amount(Double un_inventory_amount) {
		this.un_inventory_amount = un_inventory_amount;
	}

	public String getInventory_status() {
		return inventory_status;
	}

	public void setInventory_status(String inventory_status) {
		this.inventory_status = inventory_status;
	}

	public Double getUn_payment() {
		return un_payment;
	}

	public void setUn_payment(Double un_payment) {
		this.un_payment = un_payment;
	}

	public Double getPayment_all() {
		return payment_all;
	}

	public void setPayment_all(Double payment_all) {
		this.payment_all = payment_all;
	}

	public String getPayment_status() {
		return payment_status;
	}

	public void setPayment_status(String payment_status) {
		this.payment_status = payment_status;
	}

	public List<PayDtail> getPayment() {
		return payment;
	}

	public void setPayment(List<PayDtail> payment) {
		this.payment = payment;
	}

	public String getIs_tax() {
		return is_tax;
	}

	public void setIs_tax(String is_tax) {
		this.is_tax = is_tax;
	}

	public String getBill_status() {
		return bill_status;
	}

	public void setBill_status(String bill_status) {
		this.bill_status = bill_status;
	}

	public Double getBill_amount() {
		return bill_amount;
	}

	public void setBill_amount(Double bill_amount) {
		this.bill_amount = bill_amount;
	}

	public String getBill_contract() {
		return bill_contract;
	}

	public void setBill_contract(String bill_contract) {
		this.bill_contract = bill_contract;
	}

	public Double getUn_bill_amount() {
		return un_bill_amount;
	}

	public void setUn_bill_amount(Double un_bill_amount) {
		this.un_bill_amount = un_bill_amount;
	}

}
