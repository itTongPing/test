package com.aukey.report.service.centerstock.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.centerstock.CenterFbaInTransit;
import com.aukey.report.mapper.centerstock.CenterFbaInTransitMapper;
import com.aukey.report.service.centerstock.CenterFbaInTransitService;

@Service
public class CenterFbaInTransitServiceImpl implements CenterFbaInTransitService {

	@Autowired
	private CenterFbaInTransitMapper centerFbaInTransitMapper;
	
	@Override
	public List<CenterFbaInTransit> selectAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerFbaInTransitMapper.selectAll(map);
	}

	@Override
	public Integer selectAllCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerFbaInTransitMapper.selectAllCount(map);
	}

}
