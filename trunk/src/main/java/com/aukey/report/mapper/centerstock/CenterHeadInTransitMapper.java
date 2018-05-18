package com.aukey.report.mapper.centerstock;

import com.aukey.report.domain.centerstock.CenterHeadInTransit;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CenterHeadInTransitMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table center_head_in_transit
     *
     * @mbggenerated Fri Jan 26 14:51:56 CST 2018
     */
    List<CenterHeadInTransit> selectAll(Map<String, Object> map);
    Integer selectAllCount(Map<String, Object> map);
}