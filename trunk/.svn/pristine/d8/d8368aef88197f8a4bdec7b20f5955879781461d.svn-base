package com.aukey.report.service.centerstock.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.centerstock.CenterStockQuery;
import com.aukey.report.domain.centerstock.CenterStockReport;
import com.aukey.report.mapper.centerstock.CenterStockReportMapper;
import com.aukey.report.service.centerstock.CenterStockReportService;

@Service
public class CenterStockReportServiceImpl implements CenterStockReportService {

	@Autowired 
	private CenterStockReportMapper centerStockReportMapper;
	
	@Override
	public List<CenterStockReport> selectAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		List<CenterStockReport> selectAll=null;
		try {
			selectAll = centerStockReportMapper.selectAll(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return selectAll;
	}

	@Override
	public Integer selectAllCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectAllCount(map);
	}

	@Override
	public List<CenterStockQuery> selectFbaStock(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectFbaStock(map);
	}

	@Override
	public Integer selectFbaStockCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectFbaStockCount(map);
	}

	@Override
	public List<CenterStockQuery> selectOverseaStockTransfer(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectOverseaStockTransfer(map);
	}

	@Override
	public Integer selectOverseaStockTransferCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectOverseaStockTransferCount(map);
	}

	@Override
	public List<CenterStockQuery> selectFirstAirTransfer(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectFirstAirTransfer(map);
	}

	@Override
	public Integer selectFirstAirTransferCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectOverseaStockTransferCount(map);
	}

	@Override
	public List<CenterStockQuery> selectTransferWarehouse(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectTransferWarehouse(map);
	}

	@Override
	public Integer selectTransferWarehouseCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectTransferWarehouseCount(map);
	}

	@Override
	public List<CenterStockQuery> selectPurchaseTransfer(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectPurchaseTransfer(map);
	}

	@Override
	public Integer selectPurchaseTransferCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectPurchaseTransferCount(map);
	}

	@Override
	public List<CenterStockQuery> selectFbaTransfer(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectFbaTransfer(map);
	}

	@Override
	public Integer selectFbaTransferCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.selectFbaTransferCount(map);
	}

	@Override
	public List<Map<String, Object>> findAllOrgs(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerStockReportMapper.findAllOrgs(map);
	}
	

}
