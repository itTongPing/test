package com.aukey.report.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.StorageReport;
import com.aukey.report.domain.WarehouseAssessmentReport;
import com.aukey.report.domain.base.TableData;
import com.aukey.report.mapper.WarehouseAssessmentReportMapper;
import com.aukey.report.service.WarehouseAssessmentReportService;
@Service
public class WarehouseAssessmentReportServiceImpl  implements WarehouseAssessmentReportService{
	
	
	@Autowired
	private WarehouseAssessmentReportMapper mapper; // 多表查询用dao
	
	@Override
	public List<WarehouseAssessmentReport> selectList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
		return this.mapper.selectList(map);
	}

	@Override
	public int selectCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.mapper.selectCount(map);
	}

	@Override
	public TableData<WarehouseAssessmentReport> getWarehouseAssessmentReportList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		TableData<WarehouseAssessmentReport> tableData = 
					new TableData<WarehouseAssessmentReport>();
		List<WarehouseAssessmentReport> data =this.selectList(param);
		int count = this.selectCount(param);
		tableData.setRows(data);
		tableData.setTotal(count);
		return tableData;
	}

	
	
}
