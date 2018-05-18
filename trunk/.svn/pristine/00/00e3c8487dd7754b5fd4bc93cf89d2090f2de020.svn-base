package com.aukey.report.service.centerstock.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.centerstock.CenterOverseaTransportInTransit;
import com.aukey.report.mapper.centerstock.CenterOverseaTransportInTransitMapper;
import com.aukey.report.service.centerstock.CenterOverseaTransportInTransitService;

@Service
public class CenterOverseaTransportInTransitServiceImpl implements CenterOverseaTransportInTransitService {

	@Autowired
	private CenterOverseaTransportInTransitMapper centerOverseaTransportInTransitMapper;
	
	@Override
	public List<CenterOverseaTransportInTransit> selectAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerOverseaTransportInTransitMapper.selectAll(map);
	}

	@Override
	public Integer selectAllCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerOverseaTransportInTransitMapper.selectAllCount(map);
	}

}
