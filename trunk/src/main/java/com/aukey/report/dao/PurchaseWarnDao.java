package com.aukey.report.dao;

import java.util.List;
import java.util.Set;

import com.aukey.report.domain.PurchaseWarnReport;
import com.aukey.report.dto.PurchaseWarnParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;

public interface PurchaseWarnDao {
	
	TableData<PurchaseWarnReport> queryPage(PageParam pageParam, PurchaseWarnParam param);
	int count(PurchaseWarnParam param);
}
