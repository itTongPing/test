package com.aukey.report.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.aukey.report.base.BaseDao;
import com.aukey.report.dao.InventoryReportDao;
import com.aukey.report.dto.InventoryReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.PageResult;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.InventoryReportVO;

@Repository
public class InventoryReportDaoImpl extends BaseDao implements InventoryReportDao {

	private Logger logger = Logger.getLogger(getClass());

	private static final String SQL_QUERY_INVENTORY = "SELECT IFNULL(a.actual_inventory, 0) AS actual_inventory, IFNULL(a.available_inventory, 0) AS available_inventory, IFNULL(a.count_in, 0) AS count_in, IFNULL(a.count_out, 0) AS count_out, CASE a.include_tax WHEN '1' THEN '是' WHEN '0' THEN '否' END AS include_tax, IFNULL(a.sum, 0) AS sum, IFNULL(a.total_area, 0) AS total_area, CASE a.transport_type WHEN '0' THEN '空运' WHEN '1' THEN '海运' WHEN '2' THEN '无' WHEN '3' THEN '铁运' END AS transport_type, IFNULL(a.usage_inventory, 0) AS usage_inventory, IFNULL(a.way_inventory, 0) AS way_inventory, a.legaler_name AS legaler, a.department_name AS department, a.country_name AS country, a.sku_name AS sku_name, a.sku AS sku, a.category_name AS category, a.inventory_name AS inventory_name FROM stock_report a WHERE 1 = 1";

	@Override
	public TableData<InventoryReportVO> queryTestPage(PageParam pageParam, InventoryReportParam param) {
		PageResult<InventoryReportVO> pageResult = new PageResult<InventoryReportVO>(pageParam.getPageNum(), pageParam.getNumPerPage());

		StringBuilder sb = new StringBuilder(SQL_QUERY_INVENTORY);
		List<Object> ob = new ArrayList<Object>();

		// sku名称过滤
		if (StringUtils.isNotEmpty(param.getSku_name())) {
			if (param.getSku_name().indexOf(",") > 0) {
				sb.append(" and a.sku_name in (");
				String[] skuNames = param.getSku_name().split(",");
				for (int i = 0; i < skuNames.length; i++) {
					String skuName = skuNames[i];
					sb.append("?");
					if (i < skuNames.length - 1) {
						sb.append(",");
					}
					ob.add(skuName);
				}
				sb.append(")");
			} else {
				sb.append(" and a.sku_name like ?");
				ob.add("%" + param.getSku_name() + "%");
			}
		}

		// sku过滤
		if (StringUtils.isNotEmpty(param.getSku())) {
			if (param.getSku().indexOf(",") > 0) {
				sb.append(" and a.sku in (");
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
				sb.append(" and a.sku like ?");
				ob.add("%" + param.getSku() + "%");
			}
		}

		if (StringUtils.isNotEmpty(param.getTransport_type()) && !"-1".equalsIgnoreCase(param.getTransport_type())) {
			sb.append(" and a.transport_type in(").append(param.getTransport_type()).append(")");
		}
		if (StringUtils.isNotEmpty(param.getLegaler()) && !"-1".equalsIgnoreCase(param.getLegaler())) {
			sb.append(" and a.legaler in(").append(param.getLegaler()).append(")");
		}
		if (StringUtils.isNotEmpty(param.getInclude_tax()) && !"-1".equalsIgnoreCase(param.getInclude_tax())) {
			sb.append(" and a.include_tax ='").append(param.getInclude_tax()).append("'");
		}

		// 仓库id
		if (StringUtils.isNotEmpty(param.getInventory_name()) && !"-1".equalsIgnoreCase(param.getInventory_name())) {
			sb.append(" and a.inventory in(").append(param.getInventory_name()).append(")");
		}
		// 需求部门id
		if (StringUtils.isNotEmpty(param.getDepartment()) && !"-1".equalsIgnoreCase(param.getDepartment())) {
			sb.append(" and a.department in(").append(param.getDepartment()).append(")");
		}

		int total = getRecordsTotal(sb.toString(), ob.toArray());

		sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
		final BeanPropertyRowMapper<InventoryReportVO> rowMapper = new BeanPropertyRowMapper<InventoryReportVO>(InventoryReportVO.class);

		logger.info(sb.toString());

		List<InventoryReportVO> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
		TableData<InventoryReportVO> data = new TableData<InventoryReportVO>();
		data.setRows(rows);
		data.setTotal(total);
		return data;
	}

	@Override
	public int count(InventoryReportParam param) {
		StringBuilder sb = new StringBuilder(SQL_QUERY_INVENTORY);
		List<Object> ob = new ArrayList<Object>();

		// sku名称过滤
		if (StringUtils.isNotEmpty(param.getSku_name())) {
			if (param.getSku_name().indexOf(",") > 0) {
				sb.append(" and a.sku_name in (");
				String[] skuNames = param.getSku_name().split(",");
				for (int i = 0; i < skuNames.length; i++) {
					String skuName = skuNames[i];
					sb.append("?");
					if (i < skuNames.length - 1) {
						sb.append(",");
					}
					ob.add(skuName);
				}
				sb.append(")");
			} else {
				sb.append(" and a.sku_name like ?");
				ob.add("%" + param.getSku_name() + "%");
			}
		}

		// sku过滤
		if (StringUtils.isNotEmpty(param.getSku())) {
			if (param.getSku().indexOf(",") > 0) {
				sb.append(" and a.sku in (");
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
				sb.append(" and a.sku like ?");
				ob.add("%" + param.getSku() + "%");
			}
		}

		if (StringUtils.isNotEmpty(param.getTransport_type()) && !"-1".equalsIgnoreCase(param.getTransport_type())) {
			sb.append(" and a.transport_type ='").append(param.getTransport_type()).append("'");
		}
		if (StringUtils.isNotEmpty(param.getLegaler()) && !"-1".equalsIgnoreCase(param.getLegaler())) {
			sb.append(" and a.legaler in(").append(param.getLegaler()).append(")");
		}
		if (StringUtils.isNotEmpty(param.getInclude_tax()) && !"-1".equalsIgnoreCase(param.getInclude_tax())) {
			sb.append(" and a.include_tax ='").append(param.getInclude_tax()).append("'");
		}

		// 仓库id
		if (StringUtils.isNotEmpty(param.getInventory_name()) && !"-1".equalsIgnoreCase(param.getInventory_name())) {
			sb.append(" and a.inventory in(").append(param.getInventory_name()).append(")");
		}
		// 需求部门id
		if (StringUtils.isNotEmpty(param.getDepartment()) && !"-1".equalsIgnoreCase(param.getDepartment())) {
			sb.append(" and a.department in(").append(param.getDepartment()).append(")");
		}

		return getRecordsTotal(sb.toString(), ob.toArray());
	}

}
