package com.aukey.report.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aukey.report.domain.PurchaseWarnReport;

@Mapper
public interface PurchaseWarnMapper {
	/**
	 * 查询所有采购预警报表的信息
	 * @return
	 */
	public List<PurchaseWarnReport> getPurchaseWarnAll();
	/**
	 * 查询法人主体
	 * @param id
	 * @return
	 */
	public String serachLegalerName(String id);
	/**
	 * 查询采购员
	 * @param userId
	 * @return
	 */
	public String serachUserName(String userId);
	public List<Map<String, Object>> queryAssignTime(List<String> demandList);
	
}
