package com.aukey.report.utils.export;

import java.util.List;

/**
 * 描述: 数据导出,数据源
 * 
 * @author xiehz
 *
 */
public interface ExportDataSource<T> {
	<T> List<T> getData();
}
