package com.aukey.report.service;

import com.aukey.report.dto.GoodReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.GoodReportVO;

public interface GoodReportService {

	TableData<GoodReportVO> listPage(PageParam pageParam, GoodReportParam param);

	int count(GoodReportParam param);
}
