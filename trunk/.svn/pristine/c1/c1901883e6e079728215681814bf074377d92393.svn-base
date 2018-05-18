package com.aukey.report.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class PurchaseWarnParam {
	
	// 从
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private Date from_date;

		// 到
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private Date to_date;

		// SKU
		private String sku_code;
		
		//供应商
		private String supplier_id;

		// 法人主体,下拉框多选
		private String legaler_id;
		
		//采购订单
		private String purchase_order_id;
		
		//预计交货日期 从
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private Date before_from_data;
		
		//预计交货日期 到
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private Date before_to_data;
		
		//业务部门(需求部门)
		private String department_id;
		
		//采购部门
		private String purchase_group_id;
		
		//判断是否有欠货
		private String flag;//'0'欠货,'1'包含不欠货的
		
		
		
		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

		public String getPurchase_group_id() {
			return purchase_group_id;
		}

		public void setPurchase_group_id(String purchase_group_id) {
			this.purchase_group_id = purchase_group_id;
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

		public String getPurchase_order_id() {
			return purchase_order_id;
		}

		public void setPurchase_order_id(String purchase_order_id) {
			this.purchase_order_id = purchase_order_id;
		}

		

		

		public Date getBefore_from_data() {
			return before_from_data;
		}

		public void setBefore_from_data(Date before_from_data) {
			this.before_from_data = before_from_data;
		}

		public Date getBefore_to_data() {
			return before_to_data;
		}

		public void setBefore_to_data(Date before_to_data) {
			this.before_to_data = before_to_data;
		}

		public String getDepartment_id() {
			return department_id;
		}

		public void setDepartment_id(String department_id) {
			this.department_id = department_id;
		}

		public String getSupplier_id() {
			return supplier_id;
		}

		public void setSupplier_id(String supplier_id) {
			this.supplier_id = supplier_id;
		}
		
		
}
