package com.aukey.report.domain;

import java.util.Date;

public class WarehouseAssessmentReport {
	
	 private String id;
	 private String quality_control_id;//质检单号
	 private String sku_code;
	 private String create_user;
	 private String inspector_name;//质检员
	 private Date create_date;//质检时间
	 private String inspector_result;//质检结果
	 private Integer purchase_quantity ;//采购数量
	 private Integer qualified_quantity ;// 合格商品数量
	 private String purchase_no ;//采购订单号
	 private String buyer_id;
	 private String sale_name;//采购员
	 private Date leader_create_time ;//采购订单审核时间
	 private String storage_number ;//入库单号
	 private String storage_user_id;//入库员ID
	 private String  storage_user_name;//入库员
	 private Date storage_create_date;//入库时间
	 private String warehouse_id;
	 private String stock_name;
	 
	 
	 
	public String getStock_name() {
		return stock_name;
	}
	public void setStock_name(String stock_name) {
		this.stock_name = stock_name;
	}
	public String getWarehouse_id() {
		return warehouse_id;
	}
	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQuality_control_id() {
		return quality_control_id;
	}
	public void setQuality_control_id(String quality_control_id) {
		this.quality_control_id = quality_control_id;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getInspector_name() {
		return inspector_name;
	}
	public void setInspector_name(String inspector_name) {
		this.inspector_name = inspector_name;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getInspector_result() {
		return inspector_result;
	}
	public void setInspector_result(String inspector_result) {
		this.inspector_result = inspector_result;
	}
	public Integer getPurchase_quantity() {
		return purchase_quantity;
	}
	public void setPurchase_quantity(Integer purchase_quantity) {
		this.purchase_quantity = purchase_quantity;
	}
	public Integer getQualified_quantity() {
		return qualified_quantity;
	}
	public void setQualified_quantity(Integer qualified_quantity) {
		this.qualified_quantity = qualified_quantity;
	}
	public String getPurchase_no() {
		return purchase_no;
	}
	public void setPurchase_no(String purchase_no) {
		this.purchase_no = purchase_no;
	}
	public String getBuyer_id() {
		return buyer_id;
	}
	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}
	public String getSale_name() {
		return sale_name;
	}
	public void setSale_name(String sale_name) {
		this.sale_name = sale_name;
	}
	public Date getLeader_create_time() {
		return leader_create_time;
	}
	public void setLeader_create_time(Date leader_create_time) {
		this.leader_create_time = leader_create_time;
	}
	public String getStorage_number() {
		return storage_number;
	}
	public void setStorage_number(String storage_number) {
		this.storage_number = storage_number;
	}
	public String getStorage_user_id() {
		return storage_user_id;
	}
	public void setStorage_user_id(String storage_user_id) {
		this.storage_user_id = storage_user_id;
	}
	public String getStorage_user_name() {
		return storage_user_name;
	}
	public void setStorage_user_name(String storage_user_name) {
		this.storage_user_name = storage_user_name;
	}
	public Date getStorage_create_date() {
		return storage_create_date;
	}
	public void setStorage_create_date(Date storage_create_date) {
		this.storage_create_date = storage_create_date;
	}
	
}
