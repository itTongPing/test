package com.aukey.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.dao.PurchaseExeReportDao;
import com.aukey.report.dto.PurchaseExeReportParam;
import com.aukey.report.service.PurchaseExeReportService;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.PurchaseExeReportVO;

@Service
public class PurchaseExeReportServiceImpl implements PurchaseExeReportService {

	@Autowired
	private PurchaseExeReportDao purchaseExeReportDao; // 多表查询用dao

	@Override
	public TableData<PurchaseExeReportVO> listPage(PageParam pageParam, PurchaseExeReportParam param) {
		return purchaseExeReportDao.queryPage(pageParam, param);
	}

	@Override
	public int count(PurchaseExeReportParam param) {
		return purchaseExeReportDao.count(param);
	}

}
