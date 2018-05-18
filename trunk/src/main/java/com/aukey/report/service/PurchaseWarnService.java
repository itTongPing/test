package com.aukey.report.service;

import java.util.List;

import com.aukey.report.domain.PurchaseWarnReport;
import com.aukey.report.dto.PurchaseWarnParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;

public interface PurchaseWarnService {
	
	
	TableData<PurchaseWarnReport> queryPage(PageParam pageParam, PurchaseWarnParam param,Integer userId);
	int count(PurchaseWarnParam param);

}
