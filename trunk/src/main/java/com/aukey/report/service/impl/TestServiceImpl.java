package com.aukey.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.dao.InventoryReportDao;
import com.aukey.report.dto.InventoryReportParam;
import com.aukey.report.mapper.TestMapper;
import com.aukey.report.service.TestService;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.InventoryReportVO;

@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private TestMapper testMapper; // 单表操作用mapper

	@Autowired
	private InventoryReportDao testDao; // 多表查询用dao

	@Override
	public TableData<InventoryReportVO> listPage(PageParam pageParam, InventoryReportParam param) {
		return testDao.queryTestPage(pageParam, param);
	}

	@Override
	public int count(InventoryReportParam param) {
		return testDao.count(param);
	}

}
