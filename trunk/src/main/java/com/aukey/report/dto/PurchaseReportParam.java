package com.aukey.report.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseReportParam
{

    // 从 入库时间
    private Date from_date;

    // 到
    private Date to_date;

    // SKU
    private String sku;

    // 供应商名称
    private String supplier;

    // 法人主体,下拉框多选
    private String legaler;

    // 采购单号
    private String purchase_no;

    // 仓库,下拉框多选
    private String stock_id;

    // 是否含税,下拉框多选
    private String include_tax;

    // 采购部门,下拉框多选
    private String department;

    // 支付方式
    private String payType;

    // 从 采购时间
    private Date from_purchase_date;

    // 到
    private Date to_purchase_date;

    // 采购员
    private String buyer;

    // 需求部门
    private String dept_xq;

    // 采购主管
    private List<Integer> purchaseGroupIdList=new ArrayList<>();

    // 采购员
    private Integer purchaseUser;

    public List<Integer> getPurchaseGroupIdList()
    {
        return purchaseGroupIdList;
    }

    public void setPurchaseGroupIdList(List<Integer> purchaseGroupIdList)
    {
        this.purchaseGroupIdList = purchaseGroupIdList;
    }

    public Integer getPurchaseUser()
    {
        return purchaseUser;
    }

    public void setPurchaseUser(Integer purchaseUser)
    {
        this.purchaseUser = purchaseUser;
    }

    public String getDept_xq()
    {
        return dept_xq;
    }

    public void setDept_xq(String dept_xq)
    {
        this.dept_xq = dept_xq;
    }

    public Date getFrom_date()
    {
        return from_date;
    }

    public void setFrom_date(Date from_date)
    {
        this.from_date = from_date;
    }

    public Date getTo_date()
    {
        return to_date;
    }

    public void setTo_date(Date to_date)
    {
        this.to_date = to_date;
    }

    public String getSku()
    {
        return sku;
    }

    public void setSku(String sku)
    {
        this.sku = sku;
    }

    public String getSupplier()
    {
        return supplier;
    }

    public void setSupplier(String supplier)
    {
        this.supplier = supplier;
    }

    public String getLegaler()
    {
        return legaler;
    }

    public void setLegaler(String legaler)
    {
        this.legaler = legaler;
    }

    public String getPurchase_no()
    {
        return purchase_no;
    }

    public void setPurchase_no(String purchase_no)
    {
        this.purchase_no = purchase_no;
    }

    public String getStock_id()
    {
        return stock_id;
    }

    public void setStock_id(String stock_id)
    {
        this.stock_id = stock_id;
    }

    public String getInclude_tax()
    {
        return include_tax;
    }

    public void setInclude_tax(String include_tax)
    {
        this.include_tax = include_tax;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getPayType()
    {
        return payType;
    }

    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public Date getFrom_purchase_date()
    {
        return from_purchase_date;
    }

    public void setFrom_purchase_date(Date from_purchase_date)
    {
        this.from_purchase_date = from_purchase_date;
    }

    public Date getTo_purchase_date()
    {
        return to_purchase_date;
    }

    public void setTo_purchase_date(Date to_purchase_date)
    {
        this.to_purchase_date = to_purchase_date;
    }

    public String getBuyer()
    {
        return buyer;
    }

    public void setBuyer(String buyer)
    {
        this.buyer = buyer;
    }

}
