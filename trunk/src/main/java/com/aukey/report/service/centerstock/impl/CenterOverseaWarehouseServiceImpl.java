package com.aukey.report.service.centerstock.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.centerstock.CenterOverseaWarehouse;
import com.aukey.report.mapper.centerstock.CenterOverseaWarehouseMapper;
import com.aukey.report.service.centerstock.CenterOverseaWarehouseService;

@Service
public class CenterOverseaWarehouseServiceImpl implements CenterOverseaWarehouseService {

	@Autowired
	private CenterOverseaWarehouseMapper centerOverseaWarehouseMapper;
	
	@Override
	public List<CenterOverseaWarehouse> selectAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerOverseaWarehouseMapper.selectAll(map);
	}

	@Override
	public Integer selectAllCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerOverseaWarehouseMapper.selectAllCount(map);
	}

}
