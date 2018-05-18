package com.aukey.report.service.cas.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aukey.report.domain.base.Result;
import com.aukey.report.service.cas.OrgService;
import com.aukey.util.AjaxResponse;
import com.aukey.util.HttpUtil;
import com.aukey.util.URLBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

@Service("orgService")
public class OrgServiceImpl implements OrgService
{
    /**
     * 采购部门所对应的需求部门
     */
    @Value("${String.cas.saleDept.url}")
    private String saleDeptUrl;

    @Override
    public List<Map<String, Object>> querySaleDept(List<Integer> list)
    {
        Map<String, Object> md = new HashMap<>();
        md.put("list", list);
        String pd = URLBuilder.generate(md, "http://localhost:8089/org/querySaleDept");
        AjaxResponse red = HttpUtil.doPostForJsonParam(saleDeptUrl,JSON.toJSONString(list));
        // AjaxResponse red = HttpUtil.doPost(saleDeptUrl,
        // JSONObject.toJSONString(list));
        List<Map<String, Object>> sp = new ArrayList<>();
        if (red.isSuccess())
        {
            Result re = JSONObject.parseObject(red.getData().toString(), Result.class);
            if (re != null && re.getData() != null)
            {
                sp = JSON.parseObject(re.getData().toString(), new TypeReference<List<Map<String, Object>>>()
                {
                }.getType());
            }
        }
        return sp;
    }

}
