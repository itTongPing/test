package com.aukey.report.service.centerstock.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.centerstock.CenterPurchaseInTransit;
import com.aukey.report.mapper.centerstock.CenterPurchaseInTransitMapper;
import com.aukey.report.service.centerstock.CenterPurchaseInTransitService;

@Service
public class CenterPurchaseInTransitServiceImpl implements CenterPurchaseInTransitService {

	@Autowired
	private CenterPurchaseInTransitMapper centerPurchaseInTransitMapper;
	
	@Override
	public List<CenterPurchaseInTransit> selectAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerPurchaseInTransitMapper.selectAll(map);
	}

	@Override
	public Integer selectAllCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerPurchaseInTransitMapper.selectAllCount(map);
	}

}
