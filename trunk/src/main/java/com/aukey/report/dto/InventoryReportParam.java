package com.aukey.report.dto;

public class InventoryReportParam {

	// SKU
	private String sku;

	// SKU名称
	private String sku_name;

	// 运输方式,下拉框多选
	private String transport_type;

	// 仓库名称,下拉框多选
	private String inventory_name;

	// 法人主体,下拉框多选
	private String legaler;

	// 国家
	private String country;

	// 是否含税,下拉框多选
	private String include_tax;

	// 需求部门,下拉框多选
	private String department;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public String getTransport_type() {
		return transport_type;
	}

	public void setTransport_type(String transport_type) {
		this.transport_type = transport_type;
	}

	public String getInventory_name() {
		return inventory_name;
	}

	public void setInventory_name(String inventory_name) {
		this.inventory_name = inventory_name;
	}

	public String getLegaler() {
		return legaler;
	}

	public void setLegaler(String legaler) {
		this.legaler = legaler;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getInclude_tax() {
		return include_tax;
	}

	public void setInclude_tax(String include_tax) {
		this.include_tax = include_tax;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

}
