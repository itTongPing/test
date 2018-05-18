package com.aukey.report.service.centerstock.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.centerstock.CenterDomesticTransferWarehouse;
import com.aukey.report.mapper.centerstock.CenterDomesticTransferWarehouseMapper;
import com.aukey.report.service.centerstock.CenterDomesticTransferWarehouseService;

@Service
public class CenterDomesticTransferWarehouseServiceImpl implements CenterDomesticTransferWarehouseService {

	@Autowired
	private CenterDomesticTransferWarehouseMapper centerDomesticTransferWarehouseMapper;
	
	@Override
	public List<CenterDomesticTransferWarehouse> selectAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerDomesticTransferWarehouseMapper.selectAll(map);
	}

	@Override
	public Integer selectAllCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerDomesticTransferWarehouseMapper.selectAllCount(map);
	}

}
