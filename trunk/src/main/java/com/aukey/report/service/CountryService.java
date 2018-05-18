package com.aukey.report.service;

import java.util.List;
import java.util.Map;

import com.aukey.report.domain.Country;

public interface CountryService
{
    /**
     * 根据国家id查询名称
     * @param map
     * @return
     */
    List<Country> getCountryName(Map<String, Object> map);

}
