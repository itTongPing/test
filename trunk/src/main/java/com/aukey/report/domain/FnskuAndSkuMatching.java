package com.aukey.report.domain;

import java.math.BigDecimal;

public class FnskuAndSkuMatching {
	//调拨id
	private Long id;
	//调出仓
	private String fromWarehouseName;
	//目标仓
	private String toWarehouseName;
	//SKU
	private String sku;
	//SKU名称
	private String skuName;
	//调出仓sku
	private String fromWsskuBox;
	//单品重量
	private Double goodsWeight;
	//单品长
	private Double goodsPackageLength;
	//单品宽
	private Double goodsPackageWidth;
	//单品高
	private Double goodsPackageHeight;
	//单品体积
	private Double goodsPackageVolume;
	//单箱数量
	private Integer boxCount;
	//单箱重量
	private BigDecimal boxWeight;
	//单箱长
	private BigDecimal boxLength;
	//单箱宽
	private BigDecimal boxWidth;
	//单箱高
	private BigDecimal boxHeight;
	//单箱体积
	private BigDecimal boxVolume;
	//fnsku
	private String fnsku;
	//采购金额
	private Double taxUnitPrice;
	//销售部门
	private String saleDepartment;
	//产品照片(url)
	private String pictureUrl;
	//shipmentID
	private String shipmentID;
	
	public FnskuAndSkuMatching() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public FnskuAndSkuMatching(Long id, String fromWarehouseName, String toWarehouseName, String sku, String skuName,
			String fromWsskuBox, Double goodsWeight, Double goodsPackageLength, Double goodsPackageWidth,
			Double goodsPackageHeight, Double goodsPackageVolume, Integer boxCount, BigDecimal boxWeight,
			BigDecimal boxLength, BigDecimal boxWidth, BigDecimal boxHeight, BigDecimal boxVolume, String fnsku,
			Double taxUnitPrice, String saleDepartment, String pictureUrl) {
		super();
		this.id = id;
		this.fromWarehouseName = fromWarehouseName;
		this.toWarehouseName = toWarehouseName;
		this.sku = sku;
		this.skuName = skuName;
		this.fromWsskuBox = fromWsskuBox;
		this.goodsWeight = goodsWeight;
		this.goodsPackageLength = goodsPackageLength;
		this.goodsPackageWidth = goodsPackageWidth;
		this.goodsPackageHeight = goodsPackageHeight;
		this.goodsPackageVolume = goodsPackageVolume;
		this.boxCount = boxCount;
		this.boxWeight = boxWeight;
		this.boxLength = boxLength;
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
		this.boxVolume = boxVolume;
		this.fnsku = fnsku;
		this.taxUnitPrice = taxUnitPrice;
		this.saleDepartment = saleDepartment;
		this.pictureUrl = pictureUrl;
	}



	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFromWarehouseName() {
		return fromWarehouseName;
	}
	public void setFromWarehouseName(String fromWarehouseName) {
		this.fromWarehouseName = fromWarehouseName;
	}
	public String getToWarehouseName() {
		return toWarehouseName;
	}
	public void setToWarehouseName(String toWarehouseName) {
		this.toWarehouseName = toWarehouseName;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	
	
	public String getFromWsskuBox() {
		return fromWsskuBox;
	}



	public void setFromWsskuBox(String fromWsskuBox) {
		this.fromWsskuBox = fromWsskuBox;
	}



	public Double getGoodsWeight() {
		return goodsWeight;
	}
	public void setGoodsWeight(Double goodsWeight) {
		this.goodsWeight = goodsWeight;
	}
	public Double getGoodsPackageLength() {
		return goodsPackageLength;
	}
	public void setGoodsPackageLength(Double goodsPackageLength) {
		this.goodsPackageLength = goodsPackageLength;
	}
	public Double getGoodsPackageWidth() {
		return goodsPackageWidth;
	}
	public void setGoodsPackageWidth(Double goodsPackageWidth) {
		this.goodsPackageWidth = goodsPackageWidth;
	}
	public Double getGoodsPackageHeight() {
		return goodsPackageHeight;
	}
	public void setGoodsPackageHeight(Double goodsPackageHeight) {
		this.goodsPackageHeight = goodsPackageHeight;
	}
	
	public Double getGoodsPackageVolume() {
		return goodsPackageVolume;
	}

	public void setGoodsPackageVolume(Double goodsPackageVolume) {
		this.goodsPackageVolume = goodsPackageVolume;
	}

	public Integer getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(Integer boxCount) {
		this.boxCount = boxCount;
	}

	public BigDecimal getBoxWeight() {
		return boxWeight;
	}
	public void setBoxWeight(BigDecimal boxWeight) {
		this.boxWeight = boxWeight;
	}
	public BigDecimal getBoxLength() {
		return boxLength;
	}
	public void setBoxLength(BigDecimal boxLength) {
		this.boxLength = boxLength;
	}
	public BigDecimal getBoxWidth() {
		return boxWidth;
	}
	public void setBoxWidth(BigDecimal boxWidth) {
		this.boxWidth = boxWidth;
	}
	public BigDecimal getBoxHeight() {
		return boxHeight;
	}
	public void setBoxHeight(BigDecimal boxHeight) {
		this.boxHeight = boxHeight;
	}
	public BigDecimal getBoxVolume() {
		return boxVolume;
	}
	public void setBoxVolume(BigDecimal boxVolume) {
		this.boxVolume = boxVolume;
	}
	public String getFnsku() {
		return fnsku;
	}
	public void setFnsku(String fnsku) {
		this.fnsku = fnsku;
	}



	public Double getTaxUnitPrice() {
		return taxUnitPrice;
	}



	public void setTaxUnitPrice(Double taxUnitPrice) {
		this.taxUnitPrice = taxUnitPrice;
	}



	public String getPictureUrl() {
		return pictureUrl;
	}



	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}



	public String getSaleDepartment() {
		return saleDepartment;
	}



	public void setSaleDepartment(String saleDepartment) {
		this.saleDepartment = saleDepartment;
	}



	public String getShipmentID() {
		return shipmentID;
	}



	public void setShipmentID(String shipmentID) {
		this.shipmentID = shipmentID;
	}
	
	
}
