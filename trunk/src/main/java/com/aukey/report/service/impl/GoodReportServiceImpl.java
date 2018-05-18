package com.aukey.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.dao.GoodReportDao;
import com.aukey.report.dto.GoodReportParam;
import com.aukey.report.service.GoodReportService;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.GoodReportVO;

@Service
public class GoodReportServiceImpl implements GoodReportService {

	@Autowired
	private GoodReportDao goodReportDao; // 多表查询用dao

	@Override
	public TableData<GoodReportVO> listPage(PageParam pageParam, GoodReportParam param) {
		return goodReportDao.queryPage(pageParam, param);
	}

	@Override
	public int count(GoodReportParam param) {
		return goodReportDao.count(param);
	}

}
