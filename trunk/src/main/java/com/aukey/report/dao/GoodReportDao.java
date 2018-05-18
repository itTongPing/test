package com.aukey.report.dao;

import com.aukey.report.dto.GoodReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.GoodReportVO;

public interface GoodReportDao {

	TableData<GoodReportVO> queryPage(PageParam pageParam, GoodReportParam param);

	int count(GoodReportParam param);
}
