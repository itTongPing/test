package com.aukey.report.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aukey.report.domain.AllWarehouse;
import com.aukey.report.domain.QcWarehouseInfo;
import com.aukey.report.domain.Stock;
import com.aukey.report.domain.User;
import com.aukey.report.domain.UserMessage;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CommonUtil;
import com.aukey.util.HttpUtil;
import com.aukey.util.URLBuilder;

public class WareHouseUtil {
	
		 /**
	     * 通过id获取用户名
	     */
	  public static Map<String, String> getUNameById(String userInfoUrl,List<String> accounts) {
	        Map<String, Object> maps = new HashMap<String, Object>();
	        UserMessage userInfo = new UserMessage();
	        maps.put("userIds", accounts);
	        //String params = URLBuilder.generate(maps, userInfoUrl);
	        
	        String content = JSON.toJSONString(maps);
	        AjaxResponse result = HttpUtil.doPostForJsonParam(userInfoUrl, content);
	        
	        //AjaxResponse result = HttpUtil.doGet(params);
	        if (result.isSuccess() && result.getData() != null )
	        {
	        	try {
	        		userInfo = JSON.parseObject(result.getData().toString(), new TypeReference<UserMessage>(){}.getType());
				} catch (Exception e) {
					
				}
	            
	        }
	        Map<String, String> userInfoMap = new HashMap<>();
	        if(null!= userInfo.getData()) {
	            for(User user: userInfo.getData()) {
	                userInfoMap.put(user.getUserId()+"",user.getName());
	            }
	        }
	        return userInfoMap;
	 }
	
	
	 public static Map<String, String> getAllStocksName(String stockNameUrl,List<Integer> stockId) {
	        Map<String, Object> maps = new HashMap<String, Object>();
	        List<Stock> dataStock = new ArrayList<>();
	        if(stockId != null){
	            maps.put("stockId", stockId);
	        }
	        String params = URLBuilder.generate(maps, stockNameUrl);
	        AjaxResponse result = HttpUtil.doGet(params);
	        if(CommonUtil.isNotEmpty(result.getData()+"")){
	            if (result.isSuccess() && result.getData() != null)
	            {
	                Map<String, Object> data = JSON.parseObject(result.getData().toString(), new TypeReference<Map<String, Object>>()
	                {
	                }.getType());
	                List<Map<String, Object>> stockData = JSON.parseObject(data.get("list")+"", new TypeReference<List<Map<String, Object>>>()
	                {
	                }.getType());
	                for(Map<String, Object> stockMap : stockData){
	                    Stock stock = new Stock();
	                    stock.setName(stockMap.get("warehouse_name")+"");
	                    stock.setStockId(Integer.valueOf(stockMap.get("stock_id")+""));
	                    stock.setStockType(stockMap.get("stockType")+"");
	                    dataStock.add(stock);
	                }
	            }
	        }        
	        Map<String, String> stockMap = new HashMap<>();
	        if (CollectionUtils.isNotEmpty(dataStock))
	        {
	            dataStock.forEach(c -> {
	                stockMap.put(c.getStockId() + "", c.getName());
	            });
	        }
	        return stockMap;
	    }
	 
	 	/**
	     * 获取仓库类型为 2,3,4,8 的仓库
	     * @return
	     */
	    public List<QcWarehouseInfo> getWarehouseTwo(String warehouseIds,String warehouseUrl)
	    {
	        List<QcWarehouseInfo> list = new ArrayList<QcWarehouseInfo>();
	        AllWarehouse all = new AllWarehouse();
	        try
	        {
	            Map<String, Object> maps = new HashMap<String, Object>();
	            maps.put("stockType", "2,3,4,8");
	            if(warehouseIds != null){
	                maps.put("stockId", warehouseIds);
	            }
	            String params = URLBuilder.generate(maps, warehouseUrl);
	            AjaxResponse result = com.aukey.util.HttpUtil.doGet(params);
	            StringBuilder data = (StringBuilder) result.getData();
	            if (CommonUtil.isNotEmpty(data.toString()))
	            {
	                all = JSON.parseObject(data.toString(), new TypeReference<AllWarehouse>()
	                {
	                }.getType());
	                list = all.getList();
	            }
	        }
	        catch (Exception e)
	        {
	            return null;
	        }
	        return list;
	    }
	    
	    
	    
	    
	    
	    /**
	     * 根据仓库类型 获取相应的仓库
	     * @return
	     */
	    public List<QcWarehouseInfo> getWarehouse(String stockType,String warehouseIds,String warehouseUrl)
	    {
	        List<QcWarehouseInfo> list = new ArrayList<QcWarehouseInfo>();
	        AllWarehouse all = new AllWarehouse();
	        try
	        {
	            Map<String, Object> maps = new HashMap<String, Object>();
	            maps.put("stockType", stockType);
	            if(warehouseIds != null){
	                maps.put("stockId", warehouseIds);
	            }
	            String params = URLBuilder.generate(maps, warehouseUrl);
	            AjaxResponse result = com.aukey.util.HttpUtil.doGet(params);
	            StringBuilder data = (StringBuilder) result.getData();
	            if (CommonUtil.isNotEmpty(data.toString()))
	            {
	                all = JSON.parseObject(data.toString(), new TypeReference<AllWarehouse>()
	                {
	                }.getType());
	                list = all.getList();
	            }
	        }
	        catch (Exception e)
	        {
	            return null;
	        }
	        return list;
	    }
}
