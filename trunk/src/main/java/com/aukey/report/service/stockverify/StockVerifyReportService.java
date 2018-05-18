package com.aukey.report.service.stockverify;

import java.util.List;
import java.util.Map;

import com.aukey.report.domain.stockverify.StockVerifyReport;

public interface StockVerifyReportService {
	
	List<StockVerifyReport> selectAll(Map<String, Object> map);
    
    Integer selectAllCount(Map<String, Object> map);

}
