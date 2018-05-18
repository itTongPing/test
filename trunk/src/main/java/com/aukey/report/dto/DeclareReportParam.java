package com.aukey.report.dto;

import java.util.Date;

public class DeclareReportParam {

	// 月份
	private String month;

	// SKU
	private String sku;

	// 报关单号
	private String declare_no;

	// 关联单号
	private String related_no;

	// 法人主体,下拉框多选
	private String legaler;

	// 采购单号
	private String purchase_no;

	// 供应商名称
	private String supplier;

	// 采购部门,下拉框多选
	private String department;

	// 采购员
	private String buyer;

	// 从
	private Date from_date;

	// 到
	private Date to_date;

	private String relate_ids;

	// 报关状态: 1:开票完成 2:未审核 3:未开票 4:部分开票
	private String declare_status;

	// 退税说明
	private String drawback_explain;

	// 退税审核员
	private String auditor_name;

	// 退税核验员
	private String verifer_name;

	// 审核时间
	private Date from_audit_date;
	private Date to_audit_date;

	// 核验时间
	private Date from_verif_date;
	private Date to_verif_date;

	// 是否无退税核验 0:否 1:是
	private String has_no_verifer;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getDeclare_no() {
		return declare_no;
	}

	public void setDeclare_no(String declare_no) {
		this.declare_no = declare_no;
	}

	public String getRelated_no() {
		return related_no;
	}

	public void setRelated_no(String related_no) {
		this.related_no = related_no;
	}

	public String getLegaler() {
		return legaler;
	}

	public void setLegaler(String legaler) {
		this.legaler = legaler;
	}

	public String getPurchase_no() {
		return purchase_no;
	}

	public void setPurchase_no(String purchase_no) {
		this.purchase_no = purchase_no;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
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

	public String getRelate_ids() {
		return relate_ids;
	}

	public void setRelate_ids(String relate_ids) {
		this.relate_ids = relate_ids;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDeclare_status() {
		return declare_status;
	}

	public void setDeclare_status(String declare_status) {
		this.declare_status = declare_status;
	}

	public String getDrawback_explain() {
		return drawback_explain;
	}

	public void setDrawback_explain(String drawback_explain) {
		this.drawback_explain = drawback_explain;
	}

	public String getAuditor_name() {
		return auditor_name;
	}

	public void setAuditor_name(String auditor_name) {
		this.auditor_name = auditor_name;
	}

	public String getVerifer_name() {
		return verifer_name;
	}

	public void setVerifer_name(String verifer_name) {
		this.verifer_name = verifer_name;
	}

	public Date getFrom_audit_date() {
		return from_audit_date;
	}

	public void setFrom_audit_date(Date from_audit_date) {
		this.from_audit_date = from_audit_date;
	}

	public Date getTo_audit_date() {
		return to_audit_date;
	}

	public void setTo_audit_date(Date to_audit_date) {
		this.to_audit_date = to_audit_date;
	}

	public Date getFrom_verif_date() {
		return from_verif_date;
	}

	public void setFrom_verif_date(Date from_verif_date) {
		this.from_verif_date = from_verif_date;
	}

	public Date getTo_verif_date() {
		return to_verif_date;
	}

	public void setTo_verif_date(Date to_verif_date) {
		this.to_verif_date = to_verif_date;
	}

	public String getHas_no_verifer() {
		return has_no_verifer;
	}

	public void setHas_no_verifer(String has_no_verifer) {
		this.has_no_verifer = has_no_verifer;
	}

}
