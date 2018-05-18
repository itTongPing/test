package com.aukey.report.service;


import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.aukey.report.domain.transferExportInfo;


public interface TransferOverseaService {
	/**
	 * 插入数据
	 * @param list
	 * @return
	 */
	public Integer saveTransferOversea(Sheet sheet,String fileName);
	
	/**
	 * 删除已经存在的文件
	 */
	public Integer delExistsFile(String fileName);
	
	/**
	 * 查询海外调拨记录表
	 * @param map
	 * @return
	 */
	public List<transferExportInfo> selectTransferOverseaInfo(Map<String, Object> map);
	
	/**
	 * 查询海外调拨记录条数
	 * @param map
	 * @return
	 */
	public int count(Map<String, Object> map);

}
