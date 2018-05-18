package com.aukey.report.service;

import com.aukey.report.dto.DeclareReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.DeclareItemVO;
import com.aukey.report.vo.DeclareReportVO;

public interface DeclareReportService {

	TableData<DeclareReportVO> listPage(PageParam pageParam, DeclareReportParam param);

	TableData<DeclareReportVO> listPage2(PageParam pageParam, DeclareReportParam param);

	int count(DeclareReportParam param);

	TableData<DeclareItemVO> listPage3(PageParam pageParam, DeclareReportParam param);

	/**
	 * 快速导出采购报关报表
	 * @param pageParam
	 * @param param
	 * @return
	 */
	TableData<DeclareReportVO> queryDeclareInvoice(PageParam pageParam, DeclareReportParam param);
}
