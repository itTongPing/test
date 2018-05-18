package com.aukey.report.service;

import com.aukey.report.dto.PurchaseExeReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.PurchaseExeReportVO;

public interface PurchaseExeReportService {

	TableData<PurchaseExeReportVO> listPage(PageParam pageParam, PurchaseExeReportParam param);

	int count(PurchaseExeReportParam param);
}
