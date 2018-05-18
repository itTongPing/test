package com.aukey.report.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.dao.PurchaseWarnDao;
import com.aukey.report.domain.PurchaseWarnReport;
import com.aukey.report.dto.PurchaseWarnParam;
import com.aukey.report.service.PurchaseWarnService;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;

@Service
public class PurchaseWarnServiceImpl implements PurchaseWarnService {

	
	@Autowired
	private PurchaseWarnDao purchaseWarnDao;
	
	@Override
	public TableData<PurchaseWarnReport> queryPage(PageParam pageParam, PurchaseWarnParam param,Integer userId) {
		// TODO Auto-generated method stub
		return purchaseWarnDao.queryPage(pageParam, param);
	}

	@Override
	public int count(PurchaseWarnParam param) {
		// TODO Auto-generated method stub
		return purchaseWarnDao.count(param);
	}

	

	
	
	
}
