package com.aukey.report.domain;

import java.util.Date;
/**
 * 
 * @author wangxiaowen
 * 采购预警报表类
 *
 */
public class PurchaseWarnReport {
	//订单号
	private String purchase_no;
	//需求单号
	private String purchase_warn_demand;
	//sku编号
	private String sku_code;
	//法人主体id	
	private String legaler_id;
	//法人主体名称
	private String legaler_name;
	//供应商
	private String supplier_name;
	//需求人
	private String deptName;
	//指派采购员日期
	private Date assignTime;
	//采购单日期
	private Date purchase_date;
	//采购数量
	private Integer purchase_count;
	//币别
	private String currency;
	//含税单价
	private Double price_tax;
	//未税单价
	private Double price_without_tax;
	//订单总金额
	private Double purchase_sum;
	//采购员
	private String buyer_name;
	//业务部门
	private String department_id;
	//入库数量
	private Integer stock_count;
	//退货数量
	private Integer return_count;
	//欠货数量
	private Integer lack_count;
	//超期时间
	private Integer out_date;
	//超期数量
	private Integer out_date_count;
	//预计交货时间
	private Date before_stock_date;
	//最新更新时间
	private Date last_update_date;
	//sku名称
	private String sku_name;
	//采购部门id
	private Integer purchase_group_id;
	//采购部门
	private String purchase_group_name;

	public Date getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(Date assignTime) {
		this.assignTime = assignTime;
	}

	public Integer getPurchase_group_id() {
		return purchase_group_id;
	}

	public void setPurchase_group_id(Integer purchase_group_id) {
		this.purchase_group_id = purchase_group_id;
	}

	public String getPurchase_group_name() {
		return purchase_group_name;
	}

	public void setPurchase_group_name(String purchase_group_name) {
		this.purchase_group_name = purchase_group_name;
	}

	public String getLegaler_name() {
		return legaler_name;
	}

	public void setLegaler_name(String legaler_name) {
		this.legaler_name = legaler_name;
	}

	
	
	public String getPurchase_warn_demand() {
		return purchase_warn_demand;
	}

	public void setPurchase_warn_demand(String purchase_warn_demand) {
		this.purchase_warn_demand = purchase_warn_demand;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public String getPurchase_no() {
		return purchase_no;
	}

	public void setPurchase_no(String purchase_no) {
		this.purchase_no = purchase_no;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getLegaler_id() {
		return legaler_id;
	}

	public void setLegaler_id(String legaler_id) {
		this.legaler_id = legaler_id;
	}

	public String getSupplier_name() {
		return supplier_name;
	}

	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	public Date getPurchase_date() {
		return purchase_date;
	}

	public void setPurchase_date(Date purchase_date) {
		this.purchase_date = purchase_date;
	}

	public Integer getPurchase_count() {
		return purchase_count;
	}

	public void setPurchase_count(Integer purchase_count) {
		this.purchase_count = purchase_count;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getPrice_tax() {
		return price_tax;
	}

	public void setPrice_tax(Double price_tax) {
		this.price_tax = price_tax;
	}

	public Double getPrice_without_tax() {
		return price_without_tax;
	}

	public void setPrice_without_tax(Double price_without_tax) {
		this.price_without_tax = price_without_tax;
	}

	public Double getPurchase_sum() {
		return purchase_sum;
	}

	public void setPurchase_sum(Double purchase_sum) {
		this.purchase_sum = purchase_sum;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public Integer getStock_count() {
		return stock_count;
	}

	public void setStock_count(Integer stock_count) {
		this.stock_count = stock_count;
	}

	public Integer getReturn_count() {
		return return_count;
	}

	public void setReturn_count(Integer return_count) {
		this.return_count = return_count;
	}

	public Integer getLack_count() {
		return lack_count;
	}

	public void setLack_count(Integer lack_count) {
		this.lack_count = lack_count;
	}

	public Integer getOut_date() {
		return out_date;
	}

	public void setOut_date(Integer out_date) {
		this.out_date = out_date;
	}

	public Integer getOut_date_count() {
		return out_date_count;
	}

	public void setOut_date_count(Integer out_date_count) {
		this.out_date_count = out_date_count;
	}

	public Date getBefore_stock_date() {
		return before_stock_date;
	}

	public void setBefore_stock_date(Date before_stock_date) {
		this.before_stock_date = before_stock_date;
	}

	public Date getLast_update_date() {
		return last_update_date;
	}

	public void setLast_update_date(Date last_update_date) {
		this.last_update_date = last_update_date;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

}
