package com.aukey.report.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.aukey.report.domain.StrictSupplier;
import com.aukey.report.service.ISupplierService;
import com.aukey.report.utils.CommonUtil;
import com.aukey.util.AjaxResponse;
import com.aukey.util.URLBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

@Service("supplierService")
public class ISupplierServiceImpl implements ISupplierService {

	private Logger logger = Logger.getLogger(getClass());
    /**
     * 根据法人主体ID查询法人主体名称
     */
    @Value("${String.get.corporation.name.by.id}")
    private String getCorporationNamesByIds;
    
    /**
     * 根据供应商ID列表信息查询供应商信息
     */
    @Value("${String.get.supplier.name.by.supplierId}")
    private String getSupplierNamesBySupplierId;
    
    /**
     * 高级搜索 给采购订单列表
     */
    @Value("${String.manage.corporations.url}")
    private   String corporations;
    
    /**
     * 高级搜索 给采购订单列表
     */
    @Value("${String.manage.searchsupplier.url}")
    private   String searchsupplier;
	
	
	@Override
	public Map<Integer, String> querySupplierListBysupplierIds(List<Integer> supplierIds) {
		 Map<Integer, String> supplier = new HashMap<Integer, String>();
         try {
        	 removeDuplicate(supplierIds);
        	
        	 //当页面要查询的供应商个数大于200条时，采用分片
         	int BEGIN_INDEX = 0;
        	int LAST_INDEX = supplierIds.size();
        	int SIZE = 200;
    		
        	for (int index = BEGIN_INDEX; index < LAST_INDEX;) {
        		int endIndex = index + SIZE;
        		if (endIndex > LAST_INDEX) {
        			endIndex = LAST_INDEX;
        		}
        		
        		
        		StringBuffer ids = new StringBuffer();
        		for (int i = 0; i < SIZE && index + i < endIndex; i++) {
        			ids.append("," + supplierIds.get(index + i));
				}
        		
        		index = index + SIZE;        		

        		Map<String, Object> maps = new HashMap<String, Object>();
        		maps.put("supplierIds", ids.substring(1));
        		String params = URLBuilder.generate(maps, getSupplierNamesBySupplierId);
        		AjaxResponse result = com.aukey.util.HttpUtil.doGet(params);
        		String data = result.getData().toString();
        		if (CommonUtil.isNotEmpty(data)) {
        			Map<Integer, Map<String, String>> supplierInfos = JSON
        					.parseObject(data, new TypeReference<Map<Integer, Map<String, String>>>() {
        					}.getType());
        			for (Entry<Integer, Map<String, String>> entry : supplierInfos.entrySet()) {
        				Map<String, String> infor = entry.getValue();
        				Integer supplierId = Integer.parseInt(infor.get("supplierId"));
        				String supplierName = infor.get("name");//获取供应名称
        				supplier.put(supplierId, supplierName);
        			}
        		}
        	}
        	
        	
         } catch (Exception e) {
        	 logger.error("通过法人主体获取名称失败"+e.getMessage());
        	 e.printStackTrace();
         }
         return supplier;
	}
	
	@Override
	public Map<Integer, String> queryCorporationNameByCorporationIds(List<Integer> corporationIds) {
		 Map<Integer, String> corporation = new HashMap<Integer, String>();
         
         try {
        	 removeDuplicate(corporationIds);
        	 // 获取所有法人主体
        	 List<StrictSupplier> supplyList = getCorporations();
        	 if (CommonUtil.isNotEmpty(supplyList)) {
        		for (StrictSupplier corporations : supplyList) {
        			corporation.put(corporations.getCorporationId(),
        					corporations.getCorporationName());
				}
        	 }
			
		} catch (Exception e) {
			 logger.error("通过法人主体获取名称失败"+e.getMessage());
        	 e.printStackTrace();
		}
         return corporation;
	}
	
	/**
	 * 移除集合中的重复元素
	 */
	private void removeDuplicate(List<Integer> list) {
		if(list.size() == 1){//一个元素无须清理
			return;
		}
		HashSet<Integer> h = new HashSet<Integer>(list);
		list.clear();
		list.addAll(h);
	}
	
    /**
     * 查询法人主体
     * @see com.aukey.supply.bankroll.service.ISupplierService#getCorporations()
     */
    @Override
    public List<StrictSupplier> getCorporations()
    {
		List<StrictSupplier> infos = new ArrayList<StrictSupplier>();
		try {
			Map<String, Object> maps = new HashMap<String, Object>();
			String params = URLBuilder.generate(maps, corporations);
			AjaxResponse result = com.aukey.util.HttpUtil.doGet(params);

			AjaxResponse result2 = JSON.parseObject(result.getData().toString(), AjaxResponse.class);

			if (CommonUtil.isNotEmpty(result2.getData())) {
				String data = result2.getData().toString();
				Map<String, String> corporationInfos = JSON.parseObject(data,
						new TypeReference<Map<String, String>>() {
						}.getType());
				for (Entry<String, String> infoMap : corporationInfos.entrySet()) {
					StrictSupplier info = new StrictSupplier();
					info.setCorporationId(Integer.parseInt(infoMap.getValue()));
					info.setCorporationName(infoMap.getKey());
					infos.add(info);
				}
			}
		} catch (Exception ex) {
			logger.error("查询法人主体失败;", ex);
		}
        return infos;
    }

    @Override
    public List<Integer> getSerachSupplierID(String name)
    {
        Map<String, Object> maps = new HashMap<String, Object>();
        if (CommonUtil.isNotEmpty(name)){
            maps.put("name", name);
        }
        
        String params = URLBuilder.generate(maps, searchsupplier);
        AjaxResponse result = com.aukey.util.HttpUtil.doGet(params);
        StringBuilder data = (StringBuilder) result.getData();
        List<Integer> accounts=new ArrayList<Integer>();
        if(CommonUtil.isNotEmpty(data.toString())){
        accounts = JSON.parseObject(data.toString(),
            new TypeReference<List<Integer>>()
            {
            }.getType());
        }
        return accounts;
    }
    
}
