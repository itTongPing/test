package com.aukey.report.domain.centerstock;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

public class CenterOverseaTransportInTransit {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column center_oversea_transport_in_transit.transfer_no
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    private String transferNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column center_oversea_transport_in_transit.out_warehouse
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    private Integer outWarehouse;
    private String outWarehouseName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column center_oversea_transport_in_transit.in_warehouse
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    private Integer inWarehouse;
    private String inWarehouseName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column center_oversea_transport_in_transit.sku
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    private String sku;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column center_oversea_transport_in_transit.quantity
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    private BigDecimal quantity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column center_oversea_transport_in_transit.deliver_date
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    private Date deliverDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column center_oversea_transport_in_transit.transfer_no
     *
     * @return the value of center_oversea_transport_in_transit.transfer_no
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    public String getTransferNo() {
        return transferNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column center_oversea_transport_in_transit.transfer_no
     *
     * @param transferNo the value for center_oversea_transport_in_transit.transfer_no
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    public void setTransferNo(String transferNo) {
        this.transferNo = transferNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column center_oversea_transport_in_transit.out_warehouse
     *
     * @return the value of center_oversea_transport_in_transit.out_warehouse
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    public Integer getOutWarehouse() {
        return outWarehouse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column center_oversea_transport_in_transit.out_warehouse
     *
     * @param outWarehouse the value for center_oversea_transport_in_transit.out_warehouse
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    public void setOutWarehouse(Integer outWarehouse) {
        this.outWarehouse = outWarehouse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column center_oversea_transport_in_transit.in_warehouse
     *
     * @return the value of center_oversea_transport_in_transit.in_warehouse
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    public Integer getInWarehouse() {
        return inWarehouse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column center_oversea_transport_in_transit.in_warehouse
     *
     * @param inWarehouse the value for center_oversea_transport_in_transit.in_warehouse
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    public void setInWarehouse(Integer inWarehouse) {
        this.inWarehouse = inWarehouse;
    }
    
    public String getOutWarehouseName() {
		return outWarehouseName;
	}

	public void setOutWarehouseName(String outWarehouseName) {
		this.outWarehouseName = outWarehouseName;
	}

	public String getInWarehouseName() {
		return inWarehouseName;
	}

	public void setInWarehouseName(String inWarehouseName) {
		this.inWarehouseName = inWarehouseName;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column center_oversea_transport_in_transit.sku
     *
     * @return the value of center_oversea_transport_in_transit.sku
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    public String getSku() {
        return sku;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column center_oversea_transport_in_transit.sku
     *
     * @param sku the value for center_oversea_transport_in_transit.sku
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    public void setSku(String sku) {
        this.sku = sku;
    }



    public BigDecimal getQuantity() {
		return new BigDecimal(new DecimalFormat("#.000000").format(quantity));
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column center_oversea_transport_in_transit.deliver_date
     *
     * @return the value of center_oversea_transport_in_transit.deliver_date
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    public Date getDeliverDate() {
        return deliverDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column center_oversea_transport_in_transit.deliver_date
     *
     * @param deliverDate the value for center_oversea_transport_in_transit.deliver_date
     *
     * @mbggenerated Fri Jan 26 15:44:32 CST 2018
     */
    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }
}