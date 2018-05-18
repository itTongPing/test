package com.aukey.report.service;

import java.util.List;

import com.aukey.report.dto.PurchaseReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.PayMethodVO;
import com.aukey.report.vo.PurchaseReportVO;

public interface PurchaseReportService {

	TableData<PurchaseReportVO> listPage(PageParam pageParam, PurchaseReportParam param,Integer userId);

	List<PayMethodVO> payMethodList();

	int count(PurchaseReportParam param,Integer userId);

}
