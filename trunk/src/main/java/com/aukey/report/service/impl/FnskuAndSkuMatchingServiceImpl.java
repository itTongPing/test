package com.aukey.report.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.domain.base.TableData;
import com.aukey.report.domain.FnskuAndSkuMatching;
import com.aukey.report.domain.SkuNameList;
import com.aukey.report.mapper.FnskuAndSkuMatchingMapper;
import com.aukey.report.service.FnskuAndSkuMatchingService;
import com.aukey.report.utils.GetPicUrlBySkuCodes;

@Service
public class FnskuAndSkuMatchingServiceImpl implements FnskuAndSkuMatchingService {
	
	@Autowired
	private FnskuAndSkuMatchingMapper fnskuAndSkuMatchingMapper;

	@Override
	public TableData<FnskuAndSkuMatching> selectAllList(Map<String, Object> map) {
		TableData<FnskuAndSkuMatching> tableData = new TableData<FnskuAndSkuMatching>();
		List<FnskuAndSkuMatching> list=new ArrayList<FnskuAndSkuMatching>();
		list=fnskuAndSkuMatchingMapper.selectAllList(map);
		List<SkuNameList> SkuNameList =new ArrayList<SkuNameList>();
		for(int i=0;i<list.size();i=i+400){
			List<String> skuCodes=new LinkedList<String>();
			for(int j=i;j<list.size() && j<i+400;j++){
				String sku=list.get(j).getSku();
				skuCodes.add(sku);			
			}
			if(GetPicUrlBySkuCodes.getSkuBySkuCode(skuCodes)!=null){
				SkuNameList.addAll(GetPicUrlBySkuCodes.getSkuBySkuCode(skuCodes));
			}
		}	
		if(list!=null){
			for(FnskuAndSkuMatching fasm:list){
				if(SkuNameList!=null){
					for(SkuNameList skuName:SkuNameList){
						if(fasm.getSku().equals(skuName.getSku())){
							if(skuName.getSkuNameList()!=null && skuName.getSkuNameList().get(0)!=null){
								fasm.setPictureUrl(skuName.getSkuNameList().get(0).getWebPath());
							}
							break;
						}
					}
				}
			}
		}
		
		Integer total=fnskuAndSkuMatchingMapper.setAllTotal(map);
		tableData.setRows(list);
		tableData.setTotal(total);	
		return tableData;
	}

}
