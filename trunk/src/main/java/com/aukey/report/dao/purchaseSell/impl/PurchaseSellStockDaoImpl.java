package com.aukey.report.dao.purchaseSell.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.aukey.report.base.BaseDao;
import com.aukey.report.dao.purchaseSell.PurchaseSellStockDao;
import com.aukey.report.domain.purchaseSell.PurchaseSellStockVO;
import com.aukey.report.dto.purchaseSell.PurchaseSellStockParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.PageResult;
import com.aukey.report.utils.page.TableData;
@Repository
public class PurchaseSellStockDaoImpl extends BaseDao implements PurchaseSellStockDao {

	private Logger logger = Logger.getLogger(getClass());
	
	private static final String SQL_QUERY = "select a.legaler_name as legalerName,a.company_name as companyName,a.sku,a.sku_name as skuName,a.warehouse_name as warehouseName,a.category_name as categoryName,SUM(a.count_in) as inCount,SUM(a.price_in) as inPrice,SUM(a.volume_in) as inSize,SUM(a.count_out)as outCount,SUM(a.price_out) as outPrice,SUM(a.volume_out)as outSize,SUM(a.count_stock)as stockCount,SUM(a.price_stock)as stockPrice,SUM(a.volume_stock) as stockSize,SUM(a.count_occupy) as occupyCount,SUM(a.price_occupy) as occupyPrice,SUM(a.volume_occupy)as occupySize,SUM(a.count_onway)as onwayCount,SUM(a.price_onway) as onwayPrice,SUM(a.volume_onway)as onwaySize from aukey_report.purchase_sell_report a where 1=1";
	
	@Override
	public TableData<PurchaseSellStockVO> queryPage(PageParam pageParam,
			PurchaseSellStockParam param) {
       		PageResult<PurchaseSellStockVO> pageResult = new PageResult<PurchaseSellStockVO>(pageParam.getPageNum(), pageParam.getNumPerPage());	
		StringBuilder sb = new StringBuilder(SQL_QUERY);
		List<Object> ob = new ArrayList<Object>();
		
		// 时间过滤
		if (param.getDateStart() != null) {
			sb.append(" and (a.document_date >= ?");
			ob.add(param.getDateStart());
			
			if (param.getDateEnd() != null) {
				sb.append(" and a.document_date < ?");
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(param.getDateEnd());
				calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				ob.add(calendar.getTime());
			}
		}else{
			if (param.getDateEnd() != null) {
				sb.append(" and (a.document_date < ?");
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(param.getDateEnd());
				calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				ob.add(calendar.getTime());
			}
		}
		
		if(param.getDateStart() != null || param.getDateEnd() != null ){
			
			sb.append(" or a.document_date is null)");
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

		// 仓库过滤
		if (StringUtils.isNotEmpty(param.getWarehouse()) && !"-1".equalsIgnoreCase(param.getWarehouse())) {
			if (param.getWarehouse().indexOf(",") > 0) {
				sb.append(" and a.warehouse in (");
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
				sb.append(" and a.warehouse = ?");
				ob.add(param.getWarehouse());
			}
		}
		//事业部过滤
 		if (StringUtils.isNotEmpty(param.getDeptId()) && !"-1".equalsIgnoreCase(param.getDeptId())) {
			if (param.getDeptId().indexOf(",") > 0) {
				sb.append(" and a.company_id in (");
				String[] dept = param.getDeptId().split(",");
				for (int i = 0; i < dept.length; i++) {
					String deptId = dept[i];
					sb.append("?");
					if (i < dept.length - 1) {
						sb.append(",");
					}
					ob.add(deptId);
				}
				sb.append(")");
			} else {
				sb.append(" and a.company_id = ?");
				ob.add(param.getDeptId());
			}
		}
		
		sb.append(" GROUP BY a.sku,a.legaler,a.warehouse");
		int total = getRecordsTotal(sb.toString(), ob.toArray());

		sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
		final BeanPropertyRowMapper<PurchaseSellStockVO> rowMapper = new BeanPropertyRowMapper<PurchaseSellStockVO>(PurchaseSellStockVO.class);

		logger.info(sb.toString());

		List<PurchaseSellStockVO> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
		TableData<PurchaseSellStockVO> data = new TableData<PurchaseSellStockVO>();
		data.setRows(rows);
		data.setTotal(total);
		return data;
	}

	@Override
	public int count(PurchaseSellStockParam param) {
		StringBuilder sb = new StringBuilder(SQL_QUERY);
		List<Object> ob = new ArrayList<Object>();
		
		    // 时间过滤
				if (param.getDateStart() != null) {
					sb.append(" and (a.document_date >= ?");
					ob.add(param.getDateStart());
					
					if (param.getDateEnd() != null) {
						sb.append(" and a.document_date < ?");
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(param.getDateEnd());
						calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
						ob.add(calendar.getTime());
					}
				}else{
					if (param.getDateEnd() != null) {
						sb.append(" and (a.document_date < ?");
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(param.getDateEnd());
						calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
						ob.add(calendar.getTime());
					}
				}
				
				if(param.getDateStart() != null || param.getDateEnd() != null ){
					
					sb.append(" or a.document_date is null)");
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

		// 仓库过滤
		if (StringUtils.isNotEmpty(param.getWarehouse()) && !"-1".equalsIgnoreCase(param.getWarehouse())) {
			if (param.getWarehouse().indexOf(",") > 0) {
				sb.append(" and a.warehouse in (");
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
				sb.append(" and a.warehouse = ?");
				ob.add(param.getWarehouse());
			}
		}
		
		//事业部过滤
 		if (StringUtils.isNotEmpty(param.getDeptId()) && !"-1".equalsIgnoreCase(param.getDeptId())) {
			if (param.getDeptId().indexOf(",") > 0) {
				sb.append(" and a.company_id in (");
				String[] dept = param.getDeptId().split(",");
				for (int i = 0; i < dept.length; i++) {
					String deptId = dept[i];
					sb.append("?");
					if (i < dept.length - 1) {
						sb.append(",");
					}
					ob.add(deptId);
				}
				sb.append(")");
			} else {
				sb.append(" and a.company_id = ?");
				ob.add(param.getDeptId());
			}
		}
		sb.append(" GROUP BY a.sku,a.legaler,a.warehouse");
		return getRecordsTotal(sb.toString(), ob.toArray());
	}
}
