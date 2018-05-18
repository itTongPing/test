package com.aukey.report.domain;

import java.util.Date;

public class TransferOverseaInfo {
	//海运日期
	private Date shippingDate;
	
	//产品所在仓
	private Integer warehouseId;
	
	//skuCode
	private String skuCode;
	
	//操作
	private String operateInner;
	
	//发货条码
	private String shipmentCode;
	
	//原条码
	private String shipmentCodeOld;
	
	//箱唛
	private String shipMark;
	
	//发货数量
	private Integer shipQuantity;
	
	//单箱数量
	private Integer boxCount;
	
	//箱数
	private Integer boxs;
	
	//单箱重量
	private Double boxWeight;
	
	//长(cm)
	private Integer length;
	
	//宽(cm)
	private Integer width;
	
	//高(cm)
	private Integer height;
	
	//发送地址
	private String shipAddress;
	
	//FBA仓库代码
	private String fbaCode;
	
	//亚马逊账号
	private String amazonAccount;
	
	//申请人
	private String creator;
	
	//Reference ID
	private String referenceId;
	
	//收件人
	private String receiver;
	
	//国家二字代码
	private String countryCode;
	
	//州省
	private String province;
	
	//城市
	private String city;
	
	//街道一
	private String address1;
	
	//邮编
	private String postCode;
	
	//国家全称
	private String countryName;
	
	//Shipment ID
	private String shipmentId;
	
	//Fnsku
	private String fnSku;
	
	//已接收数量
	private Integer quantityReceived;
	
	//对应EXCEL文件名
	private String fileName;

	public Date getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getOperateInner() {
		return operateInner;
	}

	public void setOperateInner(String operateInner) {
		this.operateInner = operateInner;
	}

	public String getShipmentCode() {
		return shipmentCode;
	}

	public void setShipmentCode(String shipmentCode) {
		this.shipmentCode = shipmentCode;
	}

	public String getShipmentCodeOld() {
		return shipmentCodeOld;
	}

	public void setShipmentCodeOld(String shipmentCodeOld) {
		this.shipmentCodeOld = shipmentCodeOld;
	}

	public String getShipMark() {
		return shipMark;
	}

	public void setShipMark(String shipMark) {
		this.shipMark = shipMark;
	}

	public Integer getShipQuantity() {
		return shipQuantity;
	}

	public void setShipQuantity(Integer shipQuantity) {
		this.shipQuantity = shipQuantity;
	}

	public Integer getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(Integer boxCount) {
		this.boxCount = boxCount;
	}

	public Integer getBoxs() {
		return boxs;
	}

	public void setBoxs(Integer boxs) {
		this.boxs = boxs;
	}

	public Double getBoxWeight() {
		return boxWeight;
	}

	public void setBoxWeight(Double boxWeight) {
		this.boxWeight = boxWeight;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(String shipAddress) {
		this.shipAddress = shipAddress;
	}

	public String getFbaCode() {
		return fbaCode;
	}

	public void setFbaCode(String fbaCode) {
		this.fbaCode = fbaCode;
	}

	public String getAmazonAccount() {
		return amazonAccount;
	}

	public void setAmazonAccount(String amazonAccount) {
		this.amazonAccount = amazonAccount;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
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

	public Integer getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(Integer quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public TransferOverseaInfo(Date shippingDate, Integer warehouseId, String skuCode, String operateInner,
			String shipmentCode, String shipmentCodeOld, String shipMark, Integer shipQuantity, Integer boxCount,
			Integer boxs, Double boxWeight, Integer length, Integer width, Integer height, String shipAddress,
			String fbaCode, String amazonAccount, String creator, String referenceId, String receiver,
			String countryCode, String province, String city, String address1, String postCode, String countryName,
			String shipmentId, String fnSku, Integer quantityReceived, String fileName) {
		super();
		this.shippingDate = shippingDate;
		this.warehouseId = warehouseId;
		this.skuCode = skuCode;
		this.operateInner = operateInner;
		this.shipmentCode = shipmentCode;
		this.shipmentCodeOld = shipmentCodeOld;
		this.shipMark = shipMark;
		this.shipQuantity = shipQuantity;
		this.boxCount = boxCount;
		this.boxs = boxs;
		this.boxWeight = boxWeight;
		this.length = length;
		this.width = width;
		this.height = height;
		this.shipAddress = shipAddress;
		this.fbaCode = fbaCode;
		this.amazonAccount = amazonAccount;
		this.creator = creator;
		this.referenceId = referenceId;
		this.receiver = receiver;
		this.countryCode = countryCode;
		this.province = province;
		this.city = city;
		this.address1 = address1;
		this.postCode = postCode;
		this.countryName = countryName;
		this.shipmentId = shipmentId;
		this.fnSku = fnSku;
		this.quantityReceived = quantityReceived;
		this.fileName = fileName;
	}

	public TransferOverseaInfo() {
		super();
	}
	

}
