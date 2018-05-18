package com.aukey.report.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aukey.report.domain.StarSign;
import com.aukey.report.domain.StorageReport;
import com.aukey.report.domain.User;
@Mapper
public interface StorageReportMapper {
	
	//根据过滤条件查询数据
	public List<StorageReport>  selectList(Map<String, Object> param);

	public Integer getTotalStorage(Map<String, Object> param);
	
	public List<StarSign>  findAllByUserIdAndUserType(Map<String, Object> param);
	
	public List<Map<String, Object>>    selectUserList(Map<String, Object> param);
	
	public List<Map<String, Object>> selectStorageBytype(Map<String, Object> map);
	
	public List<Map<String, Object>> selectStorageBytype2(Map<String, Object> map);
}
