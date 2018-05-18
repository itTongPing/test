package com.aukey.report.service.centerstock.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.centerstock.CenterHeadInTransit;
import com.aukey.report.mapper.centerstock.CenterHeadInTransitMapper;
import com.aukey.report.service.centerstock.CenterHeadInTransitService;
@Service
public class CenterHeadInTransitServiceImpl implements CenterHeadInTransitService {

	@Autowired
	private CenterHeadInTransitMapper centerHeadInTransitMapper;
	
	@Override
	public List<CenterHeadInTransit> selectAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerHeadInTransitMapper.selectAll(map);
	}

	@Override
	public Integer selectAllCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerHeadInTransitMapper.selectAllCount(map);
	}

}
