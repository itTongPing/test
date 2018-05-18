package com.aukey.report.domain;

import java.util.Date;

public class StrictSupplier
{
    /**
     * 供应商id
     */
    private Integer supplierId;
    /**
     * 营业执照编码
     */
    private String licenceCode;
    /**
     * 供应商名称
     */
    private String name;
    /**
     * 供应商编码
     */
    private String code;
    /**
     * 别名
     */
    private String alias;
    /**
     * 公司类型：‘0’:生产加工型企业；"1"贸易型企业;“2”个体工商户;“3”：零星采购门店个体户;"4"网站阿里个体户；“5”网站淘宝个体户
     */
    private String type;

    private String address;
    /**
     * 公司性质 '0'(退税),'1'(非退税)
     */
    private String qualification;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;
    /**
     * 供应商级别
     */
    private String supplierLevel;
    private Integer corporationId;
    /**
     * 法人主体
     */
    private String corporationName;

    private String aliAccount;

    private String verifyStatus;
    /**
     * 网店/官网
     */
    private String website;

    private String funState;

    private String dataState;

    private String commitState;

    public Integer getSupplierId()
    {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId)
    {
        this.supplierId = supplierId;
    }

    public String getLicenceCode()
    {
        return licenceCode;
    }

    public void setLicenceCode(String licenceCode)
    {
        this.licenceCode = licenceCode == null ? null : licenceCode.trim();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name == null ? null : name.trim();
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code == null ? null : code.trim();
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias == null ? null : alias.trim();
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type == null ? null : type.trim();
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address == null ? null : address.trim();
    }

    public String getQualification()
    {
        return qualification;
    }

    public void setQualification(String qualification)
    {
        this.qualification = qualification == null ? null : qualification.trim();
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(String createUser)
    {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(String updateUser)
    {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public String getSupplierLevel()
    {
        return supplierLevel;
    }

    public void setSupplierLevel(String supplierLevel)
    {
        this.supplierLevel = supplierLevel == null ? null : supplierLevel.trim();
    }

    public String getVerifyStatus()
    {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus)
    {
        this.verifyStatus = verifyStatus == null ? null : verifyStatus.trim();
    }

    public String getAliAccount()
    {
        return aliAccount;
    }

    public void setAliAccount(String aliAccount)
    {
        this.aliAccount = aliAccount;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public String getFunState()
    {
        return funState;
    }

    public void setFunState(String funState)
    {
        this.funState = funState;
    }

    public String getDataState()
    {
        return dataState;
    }

    public void setDataState(String dataState)
    {
        this.dataState = dataState;
    }

    public String getCommitState()
    {
        return commitState;
    }

    public void setCommitState(String commitState)
    {
        this.commitState = commitState;
    }

    public Integer getCorporationId()
    {
        return corporationId;
    }

    public void setCorporationId(Integer corporationId)
    {
        this.corporationId = corporationId;
    }

    public String getCorporationName()
    {
        return corporationName;
    }

    public void setCorporationName(String corporationName)
    {
        this.corporationName = corporationName;
    }

}