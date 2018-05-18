package com.aukey.report.domain;

import java.util.List;

public class SkuNameList
{
    private String sku;

    private List<SkuNamePic> skuNameList;

    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSku()
    {
        return sku;
    }

    public void setSku(String sku)
    {
        this.sku = sku;
    }

    public List<SkuNamePic> getSkuNameList()
    {
        return skuNameList;
    }

    public void setSkuNameList(List<SkuNamePic> skuNameList)
    {
        this.skuNameList = skuNameList;
    }

}
