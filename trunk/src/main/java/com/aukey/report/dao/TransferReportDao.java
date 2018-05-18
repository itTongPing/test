package com.aukey.report.dao;

import com.aukey.report.dto.TransferReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.CustomsFactorsVO;
import com.aukey.report.vo.TransferReportVO;

import java.util.List;

public interface TransferReportDao {

	TableData<TransferReportVO> queryPage(PageParam pageParam, TransferReportParam param);

	int count(TransferReportParam param);

    List<CustomsFactorsVO> queryUnitPrice(String declare_order_id, String sku);
}
