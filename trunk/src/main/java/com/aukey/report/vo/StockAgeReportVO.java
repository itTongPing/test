package com.aukey.report.vo;

public class StockAgeReportVO {

	//部门名称
	private String departName;
	// SKU
	private String sku;

	// SKU名称
	private String sku_name;

	// 法人主体
	private String legaler_name;

	// 品类名称
	private String category_name;

	// 仓库名称
	private String warehouse_name;

	// 库存数量
	private Integer stock_count;

	// 标准单价
	private Double price;

	// 金额
	private Double money;

	// 0-15天库龄
	private Integer age_0_15;

	// 16-30天库龄
	private Integer age_15_30;

	// 31-60天库龄
	private Integer age_30_60;

	// 61-90天库龄
	private Integer age_60_90;

	// 91-120天库龄
	private Integer age_90_120;

	// 121-150天库龄
    private Integer age_120_150;
    
	// 121-180天库龄
	private Integer age_150_180;

	// 181-270天库龄
	private Integer age_180_270;

	// 271-365天库龄
	private Integer age_270_365;

	// 366-730天库龄
	private Integer age_365_730;

	// 731天以上库龄
	private Integer age_731;
	
	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

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

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getWarehouse_name() {
		return warehouse_name;
	}

	public void setWarehouse_name(String warehouse_name) {
		this.warehouse_name = warehouse_name;
	}

	public Integer getStock_count() {
		return stock_count;
	}

	public void setStock_count(Integer stock_count) {
		this.stock_count = stock_count;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getAge_0_15() {
		return age_0_15;
	}

	public void setAge_0_15(Integer age_0_15) {
		this.age_0_15 = age_0_15;
	}

	public Integer getAge_15_30() {
		return age_15_30;
	}

	public void setAge_15_30(Integer age_15_30) {
		this.age_15_30 = age_15_30;
	}

	public Integer getAge_30_60() {
		return age_30_60;
	}

	public void setAge_30_60(Integer age_30_60) {
		this.age_30_60 = age_30_60;
	}

	public Integer getAge_60_90() {
		return age_60_90;
	}

	public void setAge_60_90(Integer age_60_90) {
		this.age_60_90 = age_60_90;
	}

	public Integer getAge_90_120() {
		return age_90_120;
	}

	public void setAge_90_120(Integer age_90_120) {
		this.age_90_120 = age_90_120;
	}



	public Integer getAge_120_150() {
		return age_120_150;
	}

	public void setAge_120_150(Integer age_120_150) {
		this.age_120_150 = age_120_150;
	}

	public Integer getAge_150_180() {
		return age_150_180;
	}

	public void setAge_150_180(Integer age_150_180) {
		this.age_150_180 = age_150_180;
	}

	public Integer getAge_180_270() {
		return age_180_270;
	}

	public void setAge_180_270(Integer age_180_270) {
		this.age_180_270 = age_180_270;
	}

	public Integer getAge_270_365() {
		return age_270_365;
	}

	public void setAge_270_365(Integer age_270_365) {
		this.age_270_365 = age_270_365;
	}

	public Integer getAge_365_730() {
		return age_365_730;
	}

	public void setAge_365_730(Integer age_365_730) {
		this.age_365_730 = age_365_730;
	}

	public Integer getAge_731() {
		return age_731;
	}

	public void setAge_731(Integer age_731) {
		this.age_731 = age_731;
	}

	public String getLegaler_name() {
		return legaler_name;
	}

	public void setLegaler_name(String legaler_name) {
		this.legaler_name = legaler_name;
	}
}
