package com.aukey.report.service.centerstock.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.centerstock.CenterFbaStockCount;
import com.aukey.report.mapper.centerstock.CenterFbaStockCountMapper;
import com.aukey.report.service.centerstock.CenterFbaStockCountService;

@Service
public class CenterFbaStockCountServiceImpl implements CenterFbaStockCountService {

	@Autowired
	private CenterFbaStockCountMapper centerFbaStockCountMapper;
	
	@Override
	public List<CenterFbaStockCount> selectAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerFbaStockCountMapper.selectAll(map);
	}

	@Override
	public Integer selectAllCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerFbaStockCountMapper.selectAllCount(map);
	}

}
