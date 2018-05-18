package com.aukey.report.service.stockverify.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.stockverify.StockVerifyReport;
import com.aukey.report.mapper.stockverify.StockVerifyReportMapper;
import com.aukey.report.service.stockverify.StockVerifyReportService;

@Service
public class StockVerifyReportServiceImpl implements StockVerifyReportService {

	@Autowired
	private StockVerifyReportMapper stockVerifyReportMapper;
	
	@Override
	public List<StockVerifyReport> selectAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return stockVerifyReportMapper.selectAll(map);
	}

	@Override
	public Integer selectAllCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return stockVerifyReportMapper.selectAllCount(map);
	}

}
