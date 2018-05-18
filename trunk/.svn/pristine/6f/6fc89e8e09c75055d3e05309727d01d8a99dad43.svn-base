package com.aukey.report.service;

import java.util.List;
import java.util.Map;

import com.aukey.report.dto.PayOrderReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.PayOrderReportVo;

public interface PayOrderReportService {

	TableData<PayOrderReportVo> listPage(PageParam pageParam, PayOrderReportParam param);
	
	Long count(Map<String, Object> paramsMap);
	
	Map<String, Object> generateParams(PageParam pageParam, PayOrderReportParam param);

	List<Map<String, Object>> getAllNewSupplierPayment();

	List<Map<String, Object>> getAllCorporationList();

	List<Integer> getAllDeptId();

	List<PayOrderReportVo> bigDataSplitGeneateAttribute(Map<String, Object> paramsMap, boolean isSendEmail);


}
