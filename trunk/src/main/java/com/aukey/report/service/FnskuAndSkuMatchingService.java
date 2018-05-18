package com.aukey.report.service;

import java.util.Map;

import com.aukey.domain.base.TableData;
import com.aukey.report.domain.FnskuAndSkuMatching;

public interface FnskuAndSkuMatchingService {

	TableData<FnskuAndSkuMatching> selectAllList(Map<String, Object> map);

}
