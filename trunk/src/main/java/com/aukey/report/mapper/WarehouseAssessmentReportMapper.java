package com.aukey.report.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aukey.report.domain.WarehouseAssessmentReport;
@Mapper
public interface WarehouseAssessmentReportMapper {
	
	public List<WarehouseAssessmentReport> selectList(Map<String, Object> map);
		
	public int selectCount(Map<String, Object> map);
}
