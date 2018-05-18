package com.aukey.report.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.SalePartInfo;
import com.aukey.report.mapper.SaleGroupMapper;
import com.aukey.report.service.ISaleGroupService;

@Service
public class SaleGroupServiceImpl implements ISaleGroupService {

	
	@Autowired
	private SaleGroupMapper saleGroupMapper;
	
	
	
	@Override
	public List<SalePartInfo> getSaleByPurchase(Map<String, Object> purchaseIdMap) {
		// TODO Auto-generated method stub
		return saleGroupMapper.getSaleByPurchase(purchaseIdMap);
	}

}
