package com.aukey.report.service.purchaseSell.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.dao.purchaseSell.PurchaseSellStockDao;
import com.aukey.report.domain.purchaseSell.PurchaseSellStockVO;
import com.aukey.report.dto.purchaseSell.PurchaseSellStockParam;
import com.aukey.report.mapper.centerstock.CenterStockReportMapper;
import com.aukey.report.service.purchaseSell.PurchaseSellStockService;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
@Service
public class PurchaseSellStockServiceImpl implements PurchaseSellStockService{

	@Autowired
	private PurchaseSellStockDao purchaseSellStockDao;
	@Autowired
	private CenterStockReportMapper centerStockReportMapper;
	
	@Override
	public TableData<PurchaseSellStockVO> listPage(PageParam pageParam,
			PurchaseSellStockParam param) {
		 return purchaseSellStockDao.queryPage(pageParam, param);
	}

	@Override
	public int count(PurchaseSellStockParam param) {
		return purchaseSellStockDao.count(param);
	}

	@Override
	public List<Map<String, Object>> getDepartment(Map<String,Object> param) {
		return centerStockReportMapper.findAllOrgs(param);
	}

}
