package com.aukey.report.vo;

import java.util.List;

public class InventoryResult {

	private String status;

	private List<InventoryVO> list;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<InventoryVO> getList() {
		return list;
	}

	public void setList(List<InventoryVO> list) {
		this.list = list;
	}

	public class InventoryVO {
		private int stock_id;
		private String warehouse_no;
		private String warehouse_name;
		private String stockType;
		private String countryId;
		private String countryName;
		private String funcState;
		private String stockAddress;

		public int getStock_id() {
			return stock_id;
		}

		public void setStock_id(int stock_id) {
			this.stock_id = stock_id;
		}

		public String getWarehouse_no() {
			return warehouse_no;
		}

		public void setWarehouse_no(String warehouse_no) {
			this.warehouse_no = warehouse_no;
		}

		public String getWarehouse_name() {
			return warehouse_name;
		}

		public void setWarehouse_name(String warehouse_name) {
			this.warehouse_name = warehouse_name;
		}

		public String getStockType() {
			return stockType;
		}

		public void setStockType(String stockType) {
			this.stockType = stockType;
		}

		public String getCountryId() {
			return countryId;
		}

		public void setCountryId(String countryId) {
			this.countryId = countryId;
		}

		public String getCountryName() {
			return countryName;
		}

		public void setCountryName(String countryName) {
			this.countryName = countryName;
		}

		public String getFuncState() {
			return funcState;
		}

		public void setFuncState(String funcState) {
			this.funcState = funcState;
		}

		public String getStockAddress() {
			return stockAddress;
		}

		public void setStockAddress(String stockAddress) {
			this.stockAddress = stockAddress;
		}

	}

}
