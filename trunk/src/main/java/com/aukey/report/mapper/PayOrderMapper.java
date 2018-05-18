package com.aukey.report.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aukey.report.vo.PayOrderReportVo;

@Mapper
public interface PayOrderMapper {

	List<PayOrderReportVo> queryPayOrderReportInfo(Map<String, Object> params);
	
	Long count(Map<String, Object> params);
	
	Long simpleSelectCount(Map<String, Object> params);

	/**
	 * 跨库获取采购单的入库金额
	 * @param purchaseOrderId
	 * @return
	 */
	public Map<String, Object> getInStoryTotalMoneyByPurchaseOrderIds(String purchaseOrderId);

	List<Map<String, Object>> getAllNewSupplierPayment();

	List<Map<String, Object>> getAllCorporationList();

	List<Integer> getAllDeptId();

	List<PayOrderReportVo> queryPayOrderReportInfoWithPurchaseInfo(Map<String, Object> params);
}
