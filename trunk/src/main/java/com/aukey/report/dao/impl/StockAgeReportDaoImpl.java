package com.aukey.report.dao.impl;

import com.aukey.report.base.BaseDao;
import com.aukey.report.dao.StockAgeReportDao;
import com.aukey.report.dto.StockAgeReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.PageResult;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.StockAgeReportVO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StockAgeReportDaoImpl extends BaseDao implements StockAgeReportDao {

	private Logger logger = Logger.getLogger(getClass());

	private static final String SQL_QUERY = "SELECT t3.`name` as departName,`legaler_name`, `sku`, `sku_name`, `category_name`, `warehouse_name`, `stock_count`, `price`, `money`, `age_0_15`, `age_15_30`, `age_30_60`, `age_60_90`, `age_90_120`, `age_120_150`,`age_150_180`, `age_180_270`, `age_270_365`, `age_365_730`, `age_731` FROM stock_age_report t "
			+ "LEFT JOIN aukey_report.center_sku_depart_relation t2 ON t.sku = t2.sku_code "
			+ "LEFT JOIN cas.company_org t3 ON t3.id= t2.department_id where 1=1";
	//private static final String SQL_QUERY1 = "SELECT `legaler_name`, `sku`, `sku_name`, `category_name`, `warehouse_name`, `stock_count`, `price`, `money`, `legaler_name`,`age_0_15`, `age_15_30`, `age_30_60`, `age_60_90`, `age_90_120`, `age_120_180`, `age_180_270`, `age_270_365`, `age_365_730`, `age_731` FROM stock_age_report where 1=1";
	@Override
	public TableData<StockAgeReportVO> queryPage(PageParam pageParam, StockAgeReportParam param) {
		PageResult<StockAgeReportVO> pageResult = new PageResult<StockAgeReportVO>(pageParam.getPageNum(), pageParam.getNumPerPage());

		StringBuilder sb = new StringBuilder(SQL_QUERY);
		List<Object> ob = new ArrayList<Object>();

		// sku过滤
		if (StringUtils.isNotEmpty(param.getSku())) {
			if (param.getSku().indexOf(",") > 0) {
				sb.append(" and sku in (");
				String[] skuCodes = param.getSku().split(",");
				for (int i = 0; i < skuCodes.length; i++) {
					String skuCode = skuCodes[i];
					sb.append("?");
					if (i < skuCodes.length - 1) {
						sb.append(",");
					}
					ob.add(skuCode);
				}
				sb.append(")");
			} else {
				sb.append(" and sku like ?");
				ob.add("%" + param.getSku() + "%");
			}
		}

		// 法人主体过滤
		if (StringUtils.isNotEmpty(param.getLegaler()) && !"-1".equalsIgnoreCase(param.getLegaler())) {
			if (param.getLegaler().indexOf(",") > 0) {
				String[] legalers = param.getLegaler().split(",");
				sb.append(" and legaler in(");
				for (int i = 0; i < legalers.length; i++) {
					sb.append("?");
					if (i < legalers.length - 1) {
						sb.append(",");
					}
					ob.add(legalers[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and legaler = ?");
				ob.add(param.getLegaler());
			}
		}

		// 仓库过滤
		if (StringUtils.isNotEmpty(param.getWarehouse()) && !"-1".equalsIgnoreCase(param.getWarehouse())) {
			if (param.getWarehouse().indexOf(",") > 0) {
				sb.append(" and warehouse in (");
				String[] warehouses = param.getWarehouse().split(",");
				for (int i = 0; i < warehouses.length; i++) {
					String warehouse = warehouses[i];
					sb.append("?");
					if (i < warehouses.length - 1) {
						sb.append(",");
					}
					ob.add(warehouse);
				}
				sb.append(")");
			} else {
				sb.append(" and warehouse = ?");
				ob.add(param.getWarehouse());
			}
		}

		int total = getRecordsTotal(sb.toString(), ob.toArray());

		sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
		final BeanPropertyRowMapper<StockAgeReportVO> rowMapper = new BeanPropertyRowMapper<StockAgeReportVO>(StockAgeReportVO.class);

		logger.info(sb.toString());

		List<StockAgeReportVO> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
		TableData<StockAgeReportVO> data = new TableData<StockAgeReportVO>();
		data.setRows(rows);
		data.setTotal(total);
		return data;
	}

	@Override
	public int count(StockAgeReportParam param) {
		StringBuilder sb = new StringBuilder(SQL_QUERY);
		List<Object> ob = new ArrayList<Object>();

		// sku过滤
		if (StringUtils.isNotEmpty(param.getSku())) {
			if (param.getSku().indexOf(",") > 0) {
				sb.append(" and sku in (");
				String[] skuCodes = param.getSku().split(",");
				for (int i = 0; i < skuCodes.length; i++) {
					String skuCode = skuCodes[i];
					sb.append("?");
					if (i < skuCodes.length - 1) {
						sb.append(",");
					}
					ob.add(skuCode);
				}
				sb.append(")");
			} else {
				sb.append(" and sku like ?");
				ob.add("%" + param.getSku() + "%");
			}
		}

		// 仓库过滤
		if (StringUtils.isNotEmpty(param.getWarehouse()) && !"-1".equalsIgnoreCase(param.getWarehouse())) {
			if (param.getWarehouse().indexOf(",") > 0) {
				sb.append(" and warehouse in (");
				String[] warehouses = param.getWarehouse().split(",");
				for (int i = 0; i < warehouses.length; i++) {
					String warehouse = warehouses[i];
					sb.append("?");
					if (i < warehouses.length - 1) {
						sb.append(",");
					}
					ob.add(warehouse);
				}
				sb.append(")");
			} else {
				sb.append(" and warehouse = ?");
				ob.add(param.getWarehouse());
			}
		}

		return getRecordsTotal(sb.toString(), ob.toArray());
	}
}
