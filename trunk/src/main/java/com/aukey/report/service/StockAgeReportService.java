package com.aukey.report.service;

import com.aukey.report.dto.StockAgeReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.StockAgeReportVO;

public interface StockAgeReportService {

	TableData<StockAgeReportVO> listPage(PageParam pageParam, StockAgeReportParam param);

	int count(StockAgeReportParam param);
}
