package com.aukey.report.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.aukey.util.CommonUtil;

public class StorageReport {
	
	private String storageNumber;
	private String qualityInspectionNumber;
	private String purchaseNumber;
	private Integer supplierId;
	private String inspector;
	private Integer warehouseId;
	private String muWarehouseName;
	private Integer nondefectiveNumber;
	private Integer rejectsNumber;
	private Integer sparePartsNumber;
	private String storageLocationCode;
	private String createUser;
	private Timestamp createDate;
	private String skuCode;
	private String skuName;
	private String type;
	private String corporationId;
	private String corporationName;
	private String transportType;
	private String transportTypeName;
	private String isTax;
	private String isTaxName;
	private String reqUserName;
	private String saleName;
	private String deptName;
	private String signType;
	private String userId;
	private String inspectorName;
	private String createUserName;
	private String warehouseName;
	private String legalPersonName;
	private String isStarSign;
	private String stockname;//目标仓
	
	private String supplier_name; 
	private String tax_unit_price;
	private String no_tax_price; 
	//币种
	private String currency;
	//入库体积
	private Double volume;
	
	//过滤条件
	private List<String> corporationIds;//法人主体集合
	private Date createDateStart;//开始时间
	private Date createDateEnd;//结束时间
	private List<String> warehouseIds;//仓库集合
	private List<String> purchaseOrders;//采购单号集合
	private List<String> storageNumbers;//入库单号集合
	private List<String> qualityInspectionNumbers;//质检单号集合
	
	private String stockInSum;
	
	

	public String getStockInSum() {
		
		if("1".equals(this.isTax))
		{
			if(CommonUtil.isNotEmpty(this.tax_unit_price))
			{
				this.stockInSum=Double.valueOf(this.tax_unit_price)*(this.nondefectiveNumber+this.rejectsNumber)+"";
			}
			
		}else
		{
			if(CommonUtil.isNotEmpty(this.no_tax_price))
			{
				this.stockInSum=Double.valueOf(this.no_tax_price)*(this.nondefectiveNumber+this.rejectsNumber)+"";
			}
			
		}
		
		return this.stockInSum;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public String getTax_unit_price() {
		return tax_unit_price;
	}
	public void setTax_unit_price(String tax_unit_price) {
		this.tax_unit_price = tax_unit_price;
	}
	public String getNo_tax_price() {
		return no_tax_price;
	}
	public void setNo_tax_price(String no_tax_price) {
		this.no_tax_price = no_tax_price;
	}
	public String getStockname() {
		return stockname;
	}
	public void setStockname(String stockname) {
		this.stockname = stockname;
	}
	public String getInspectorName() {
		return inspectorName;
	}
	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getLegalPersonName() {
		return legalPersonName;
	}
	public void setLegalPersonName(String legalPersonName) {
		this.legalPersonName = legalPersonName;
	}
	public String getIsStarSign() {
		return isStarSign;
	}
	public void setIsStarSign(String isStarSign) {
		this.isStarSign = isStarSign;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<String> getPurchaseOrders() {
		return purchaseOrders;
	}
	public void setPurchaseOrders(List<String> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}
	public List<String> getStorageNumbers() {
		return storageNumbers;
	}
	public void setStorageNumbers(List<String> storageNumbers) {
		this.storageNumbers = storageNumbers;
	}
	public List<String> getQualityInspectionNumbers() {
		return qualityInspectionNumbers;
	}
	public void setQualityInspectionNumbers(List<String> qualityInspectionNumbers) {
		this.qualityInspectionNumbers = qualityInspectionNumbers;
	}
	public List<String> getWarehouseIds() {
		return warehouseIds;
	}
	public void setWarehouseIds(List<String> warehouseIds) {
		this.warehouseIds = warehouseIds;
	}
	public Date getCreateDateStart() {
		return createDateStart;
	}
	public void setCreateDateStart(Date createDateStart) {
		this.createDateStart = createDateStart;
	}
	public Date getCreateDateEnd() {
		return createDateEnd;
	}
	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	public List<String> getCorporationIds() {
		return corporationIds;
	}
	public void setCorporationIds(List<String> corporationIds) {
		this.corporationIds = corporationIds;
	}
	
	public String getStorageNumber() {
		return storageNumber;
	}
	public void setStorageNumber(String storageNumber) {
		this.storageNumber = storageNumber;
	}
	public String getQualityInspectionNumber() {
		return qualityInspectionNumber;
	}
	public void setQualityInspectionNumber(String qualityInspectionNumber) {
		this.qualityInspectionNumber = qualityInspectionNumber;
	}
	public String getPurchaseNumber() {
		return purchaseNumber;
	}
	public void setPurchaseNumber(String purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getInspector() {
		return inspector;
	}
	public void setInspector(String inspector) {
		this.inspector = inspector;
	}
	public Integer getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getMuWarehouseName() {
		return muWarehouseName;
	}
	public void setMuWarehouseName(String muWarehouseName) {
		this.muWarehouseName = muWarehouseName;
	}
	public Integer getNondefectiveNumber() {
		return nondefectiveNumber;
	}
	public void setNondefectiveNumber(Integer nondefectiveNumber) {
		this.nondefectiveNumber = nondefectiveNumber;
	}
	public Integer getRejectsNumber() {
		return rejectsNumber;
	}
	public void setRejectsNumber(Integer rejectsNumber) {
		this.rejectsNumber = rejectsNumber;
	}
	public Integer getSparePartsNumber() {
		return sparePartsNumber;
	}
	public void setSparePartsNumber(Integer sparePartsNumber) {
		this.sparePartsNumber = sparePartsNumber;
	}
	public String getStorageLocationCode() {
		return storageLocationCode;
	}
	public void setStorageLocationCode(String storageLocationCode) {
		this.storageLocationCode = storageLocationCode;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCorporationId() {
		return corporationId;
	}
	public void setCorporationId(String corporationId) {
		this.corporationId = corporationId;
	}
	public String getCorporationName() {
		return corporationName;
	}
	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}
	public String getTransportType() {
		return transportType;
	}
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}
	public String getTransportTypeName() {
		return transportTypeName;
	}
	public void setTransportTypeName(String transportTypeName) {
		this.transportTypeName = transportTypeName;
	}
	public String getIsTax() {
		return isTax;
	}
	public void setIsTax(String isTax) {
		this.isTax = isTax;
	}
	public String getIsTaxName() {
		return isTaxName;
	}
	public void setIsTaxName(String isTaxName) {
		this.isTaxName = isTaxName;
	}
	public String getReqUserName() {
		return reqUserName;
	}
	public void setReqUserName(String reqUserName) {
		this.reqUserName = reqUserName;
	}
	public String getSaleName() {
		return saleName;
	}
	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	
	
}
