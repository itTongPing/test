package com.aukey.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.dao.StockAgeReportDao;
import com.aukey.report.dto.StockAgeReportParam;
import com.aukey.report.service.StockAgeReportService;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.StockAgeReportVO;

@Service
public class StockAgeReportServiceImpl implements StockAgeReportService {

    @Autowired
    private StockAgeReportDao stockAgeReportDao; // 多表查询用dao

    @Override
    public TableData<StockAgeReportVO> listPage(PageParam pageParam, StockAgeReportParam param) {
        return stockAgeReportDao.queryPage(pageParam, param);
    }

	@Override
	public int count(StockAgeReportParam param) {
		return stockAgeReportDao.count(param);
	}

}
