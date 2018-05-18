package com.aukey.report.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aukey.report.domain.TransferOverseaInfo;
import com.aukey.report.domain.transferExportInfo;

@Mapper
public interface TransferOverseaMapper {

	/**
	 * 插入数据
	 * @param list
	 * @return
	 */
	public Integer saveTransferOversea(List<TransferOverseaInfo> list);
	
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
