package com.aukey.report.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.domain.Country;
import com.aukey.report.mapper.CountryMapper;
import com.aukey.report.service.CountryService;

@Service("countryService")
public class CountryServiceImpl implements CountryService
{
    @Autowired
    private CountryMapper countryMapper;
    
    @Override
    public List<Country> getCountryName(Map<String, Object> map)
    {
        return countryMapper.getCountryName(map);
    }

}
