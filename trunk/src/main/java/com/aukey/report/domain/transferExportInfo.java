package com.aukey.report.domain;

public class transferExportInfo {
	private Integer warehouseIds;//出库仓
	private String shipmentId;//Shipment ID
	private String fnSku;//snsku
	private String skuCode;//sku
	private Integer shipQuantity;//发货数量
	private Integer inWayQuantity;//在途数量
	private Integer receiveQuantity;//收货数量
	private String warehouseName;//仓库名
	private String fileName; //文件名
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public Integer getWarehouseIds() {
		return warehouseIds;
	}
	public void setWarehouseIds(Integer warehouseIds) {
		this.warehouseIds = warehouseIds;
	}
	public String getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(String shipmentId) {
		this.shipmentId = shipmentId;
	}
	public String getFnSku() {
		return fnSku;
	}
	public void setFnSku(String fnSku) {
		this.fnSku = fnSku;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public Integer getShipQuantity() {
		return shipQuantity;
	}
	public void setShipQuantity(Integer shipQuantity) {
		this.shipQuantity = shipQuantity;
	}
	public Integer getInWayQuantity() {
		return inWayQuantity;
	}
	public void setInWayQuantity(Integer inWayQuantity) {
		this.inWayQuantity = inWayQuantity;
	}
	public Integer getReceiveQuantity() {
		return receiveQuantity;
	}
	public void setReceiveQuantity(Integer receiveQuantity) {
		this.receiveQuantity = receiveQuantity;
	}
	
	
}
