package com.aukey.report.domain;

public class QcWarehouseInfo
{
	//仓库id
	private Integer stock_id;
	//仓库名字
	private String warehouse_name;
	public Integer getStock_id() {
		return stock_id;
	}
	public void setStock_id(Integer stock_id) {
		this.stock_id = stock_id;
	}
	public String getWarehouse_name() {
		return warehouse_name;
	}
	public void setWarehouse_name(String warehouse_name) {
		this.warehouse_name = warehouse_name;
	}
  
}