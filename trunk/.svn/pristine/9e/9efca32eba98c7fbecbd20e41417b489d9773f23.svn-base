package com.aukey.report.base;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据访问层查询对象的基类.
 * 
 * @author xiehz
 *
 */
public abstract class BaseDao {

	private static final Logger logger = Logger.getLogger(BaseDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public int getRecordsTotal(String sql, Object... args) {
		logger.debug("*****************SQL : " + sql);
		Long totalCount = 0L;
		try {
			StringBuilder countSql = new StringBuilder();
			countSql.append("select count(*) as recordsTotal from (").append(sql).append(") temp_records_cnt");
			Map<String, Object> result = getJdbcTemplate().queryForMap(countSql.toString(), args);
			totalCount = (Long) result.get("recordsTotal");
		} catch (Exception e) {
			logger.error("查询数据量失败的原因：",e);
		}
		
		logger.debug("************totalCount************" + totalCount);
		return totalCount.intValue();
	}

	
	public int getRecordsTotal2(String sql, Object... args) {
		logger.debug("*****************SQL : " + sql);
		Long totalCount = 0L;
		try {
			StringBuilder countSql = new StringBuilder();
			countSql.append("select count(*) as recordsTotal from ").append(sql);
			Map<String, Object> result = getJdbcTemplate().queryForMap(countSql.toString(), args);
			totalCount = (Long) result.get("recordsTotal");
		} catch (Exception e) {
			logger.error("查询数据量失败的原因：",e);
		}
		
		logger.debug("************totalCount************" + totalCount);
		return totalCount.intValue();
	}
	
}
