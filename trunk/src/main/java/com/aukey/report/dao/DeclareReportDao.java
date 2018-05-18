package com.aukey.report.dao;

import com.aukey.report.dto.DeclareReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.DeclareItemVO;
import com.aukey.report.vo.DeclareReportVO;

public interface DeclareReportDao {

	TableData<DeclareReportVO> queryPage(PageParam pageParam, DeclareReportParam param);

	TableData<DeclareReportVO> queryPage2(PageParam pageParam, DeclareReportParam param);

	int count(DeclareReportParam param);

	TableData<DeclareItemVO> queryPage3(PageParam pageParam, DeclareReportParam param);

	/**
	 * 快速导出采购报关报表
	 * @param pageParam
	 * @param param
	 * @return
	 */
	TableData<DeclareReportVO> queryDeclareInvoice(PageParam pageParam, DeclareReportParam param);
}
