package com.aukey.report.dao.finance;

import java.util.List;

import com.aukey.report.domain.base.Result;
import com.aukey.report.dto.PurchaseReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.PayMethodVO;
import com.aukey.report.vo.PurchaseReportVO;

public interface PurchaseFinanceReportDao {
	TableData<PurchaseReportVO> queryPage(PageParam pageParam, PurchaseReportParam param);

	List<PayMethodVO> queryList();

	int count(PurchaseReportParam param);
	
	Result selectCount(PageParam pageParam, PurchaseReportParam param);
}
