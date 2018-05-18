package com.aukey.report.service;

import java.util.List;
import java.util.Map;

import com.aukey.report.domain.StrictSupplier;
/**
 * 
 * 供应商接口的实现
 *
 * <p>detailed comment
 * @author a2 2017年1月17日
 * @see
 * @since 1.0
 */
public interface ISupplierService
{
    /**
     * 根据供应商ID列表从导入的E登数据中，查询供应商信息
     * @return 供应商信息列表
     */
    public Map<Integer, String> querySupplierListBysupplierIds(List<Integer> supplierIds);
    
    /**
     * 根据法人主体Id获取其名称
     * @param corporationIds
     * @return
     */
    public Map<Integer, String> queryCorporationNameByCorporationIds(List<Integer> corporationIds);

    /**
     * 
     * 法人主体下拉框
     * 
     *
     * @return List<StrictSupplier> 供应商实体类
     */
    public  List<StrictSupplier> getCorporations();

	public List<Integer> getSerachSupplierID(String supplierName);
}
