package com.aukey.report.service;

import com.aukey.report.dto.TransferReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.TransferReportVO;

public interface TransferReportService {

	TableData<TransferReportVO> listPage(PageParam pageParam, TransferReportParam param);

	int count(TransferReportParam param);
}
