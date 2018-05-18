package com.aukey.report.service;

import java.util.List;
import java.util.Map;

import com.aukey.report.domain.StorageReport;
import com.aukey.report.domain.WarehouseAssessmentReport;
import com.aukey.report.domain.base.TableData;

public interface WarehouseAssessmentReportService {
	
	
	public List<WarehouseAssessmentReport> selectList(Map<String, Object> map);
	
	public int selectCount(Map<String, Object> map);

	public TableData<WarehouseAssessmentReport> getWarehouseAssessmentReportList(Map<String, Object> param);
}
