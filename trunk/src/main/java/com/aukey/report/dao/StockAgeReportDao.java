package com.aukey.report.dao;

import com.aukey.report.dto.StockAgeReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.StockAgeReportVO;

public interface StockAgeReportDao {

	TableData<StockAgeReportVO> queryPage(PageParam pageParam, StockAgeReportParam param);

	int count(StockAgeReportParam param);
}
