package com.aukey.report.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aukey.report.domain.StockModel;
import com.aukey.report.domain.StorageReport;
import com.aukey.report.domain.base.Result;
import com.aukey.report.domain.base.TableData;

public interface StorageReportService {

	
	public List<StockModel> getStockByType(String[] stockTypes);
	TableData<StorageReport> getStorageReportList(Map<String, Object> param);

	Result exportExcel(List<StorageReport> rows, Integer userId, String username, String password,
			HttpServletRequest request, HttpServletResponse response);
	
	
	public Map<String, String>    selectUserList(List<String> list);
	
	
	public Integer getTotalStorage(Map<String, Object> param);
	
	public List<Map<String, Object>> selectStorageBytype(Map<String, Object> map);
	
	public List<Map<String, Object>> selectStorageBytype2(Map<String, Object> map);

}
