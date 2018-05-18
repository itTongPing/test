package com.aukey.report.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.aukey.report.domain.SkuNameList;
import com.aukey.util.AjaxResponse;
import com.aukey.util.HttpUtil;
import com.aukey.util.URLBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

public class GetPicUrlBySkuCodes {
	
	public static List<SkuNameList> getSkuBySkuCode(List<String> skuCodeList)
    {

        Map<String, String> skuCodes = new HashMap<>();

        if (CollectionUtils.isNotEmpty(skuCodeList))
        {
        	skuCodeList.forEach(t -> {
                if (null != t)
                {

                    skuCodes.put(t, t);
                }
                ;

            });
        }
   
        List<SkuNameList> ret = null;
        StringBuffer skus = new StringBuffer();
        if (null != skuCodes && !skuCodes.isEmpty())
        {
            skuCodes.forEach((key, value) -> {
                skus.append(key + ",");
            });
        }
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("skuCode", String.valueOf(skus));
        String params = URLBuilder.generate(maps, "http://product.qa.aukeyit.com/product/picturesAndName");
        AjaxResponse result = HttpUtil.doGet(params);
        if (result.isSuccess() && result.getData() != null)
        {
            try
            {
                ret = JSON.parseObject(result.getData().toString(), new TypeReference<List<SkuNameList>>()
                {
                }.getType());
            }
            catch (Exception e)
            {

            }

        }
        return ret;
    }
		
}
