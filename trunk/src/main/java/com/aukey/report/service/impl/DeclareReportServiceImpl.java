package com.aukey.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.dao.DeclareReportDao;
import com.aukey.report.dto.DeclareReportParam;
import com.aukey.report.service.DeclareReportService;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.DeclareItemVO;
import com.aukey.report.vo.DeclareReportVO;

@Service
public class DeclareReportServiceImpl implements DeclareReportService {

	@Autowired
	private DeclareReportDao declareReportDao; // 多表查询用dao

	@Override
	public TableData<DeclareReportVO> listPage(PageParam pageParam, DeclareReportParam param) {
		return declareReportDao.queryPage(pageParam, param);
	}

	@Override
	public TableData<DeclareReportVO> listPage2(PageParam pageParam, DeclareReportParam param) {
		return declareReportDao.queryPage2(pageParam, param);
	}

	@Override
	public int count(DeclareReportParam param) {
		return declareReportDao.count(param);
	}

	@Override
	public TableData<DeclareItemVO> listPage3(PageParam pageParam, DeclareReportParam param) {
		return declareReportDao.queryPage3(pageParam, param);
	}

	@Override
	public TableData<DeclareReportVO> queryDeclareInvoice(PageParam pageParam, DeclareReportParam param) {
		return declareReportDao.queryDeclareInvoice(pageParam, param);
	}

}
