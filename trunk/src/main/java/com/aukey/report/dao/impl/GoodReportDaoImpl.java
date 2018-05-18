package com.aukey.report.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.aukey.report.base.BaseDao;
import com.aukey.report.dao.GoodReportDao;
import com.aukey.report.dto.GoodReportParam;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.PageResult;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.GoodReportVO;
import com.aukey.report.vo.PurchaseReportVO;

@Repository
public class GoodReportDaoImpl extends BaseDao implements GoodReportDao {

	private Logger logger = Logger.getLogger(getClass());
	private static final String SQL_QUERY = "select `sku` , `sku_name` ,  `document_number`,`document_date`, `business_type`,`is_tax` ,`legaler`,"+
	  "`legaler_name`,`intercourse_unit`,`intercourse_unit_name`, `warehouse`,`warehouse_name`,`quantity`,"+
	  "`price`, `cost`,`currency`,`creator`,`creator_name`,`transport_type`,`department`, `department_name`,`last_update_time`"+
	  "from sku_report WHERE 1 = 1"; 

	//private static final String SQL_QUERY = "SELECT sku, sku_name, document_number AS receipt_no, document_date AS receipt_date, business_type, CASE is_tax WHEN 1 THEN '是' WHEN 0 THEN '否' END AS is_tax, legaler_name, intercourse_unit_name, warehouse_name, count_in, price_in, cost_in, currency_in, count_out, price_out, cost_out, currency_out, count_surplus, price_surplus, cost_surplus FROM sku_report WHERE 1 = 1";

	@Override
	public TableData<GoodReportVO> queryPage(PageParam pageParam, GoodReportParam param) {
		PageResult<PurchaseReportVO> pageResult = new PageResult<PurchaseReportVO>(pageParam.getPageNum(), pageParam.getNumPerPage());

		StringBuilder sb = new StringBuilder(SQL_QUERY);
		List<Object> ob = new ArrayList<Object>();

		// 时间过滤
		if (param.getFrom_date() != null) {
			sb.append(" and document_date >= ?");
			ob.add(param.getFrom_date());
		}
		if (param.getTo_date() != null) {
			sb.append(" and document_date < ?");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(param.getTo_date());
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			ob.add(calendar.getTime());
		}

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

		// 往来单位过滤
		if (StringUtils.isNotEmpty(param.getOrganization())) {
			if (param.getOrganization().indexOf(",") > 0) {
				sb.append(" and intercourse_unit_name in (");
				String[] orgs = param.getOrganization().split(",");
				for (int i = 0; i < orgs.length; i++) {
					String org = orgs[i];
					sb.append("?");
					if (i < orgs.length - 1) {
						sb.append(",");
					}
					ob.add(org);
				}
				sb.append(")");
			} else {
				sb.append(" and intercourse_unit_name like ?");
				ob.add("%" + param.getOrganization() + "%");
			}
		}

		// 单据编号过滤
		if (StringUtils.isNotEmpty(param.getReceipt_no())) {
			if (param.getReceipt_no().indexOf(",") > 0) {
				sb.append(" and document_number in (");
				String[] nums = param.getReceipt_no().split(",");
				for (int i = 0; i < nums.length; i++) {
					String num = nums[i];
					sb.append("?");
					if (i < nums.length - 1) {
						sb.append(",");
					}
					ob.add(num);
				}
				sb.append(")");
			} else {
				sb.append(" and document_number like ?");
				ob.add("%" + param.getReceipt_no() + "%");
			}
		}

		// 仓库过滤
		if (StringUtils.isNotEmpty(param.getWarehouse()) && !"-1".equalsIgnoreCase(param.getWarehouse())) {
			if (param.getWarehouse().indexOf(",") > 0) {
				String[] warehouses = param.getWarehouse().split(",");
				sb.append(" and warehouse in(");
				for (int i = 0; i < warehouses.length; i++) {
					sb.append("?");
					if (i < warehouses.length - 1) {
						sb.append(",");
					}
					ob.add(warehouses[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and warehouse = ?");
				ob.add(param.getWarehouse());
			}
		}
		logger.info(sb.toString());
		 TableData<GoodReportVO> data = new TableData<GoodReportVO>();
		
		 //如果有中转仓权限 或者有财务相关角色 则查看数据
		if(param.getWarehouse() != null && !"".equals(param.getWarehouse())
				){
			
			int total = getRecordsTotal(sb.toString(), ob.toArray());

			sb.append(" order by document_date desc");
			sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
			final BeanPropertyRowMapper<GoodReportVO> rowMapper = new BeanPropertyRowMapper<GoodReportVO>(GoodReportVO.class);
			List<GoodReportVO> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
			/*for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
				GoodReportVO goodReportVO = (GoodReportVO) iterator.next();
				String business_type = goodReportVO.getBusiness_type();
				if(goodReportVO != null && !"".equals(business_type) && business_type.equals("无主海外调拨出库") || business_type.equals("无主调拨出库") || business_type.equals("无主盘盈入库") || business_type.equals("无主调拨入库") || business_type.equals("无主盘亏出库")){
					goodReportVO.setLegaler_name("傲基国际有限公司");
					goodReportVO.setIs_tax("0");
				}
				if(goodReportVO != null && !"".equals(business_type) && business_type.equals("需求单无主调拨出库")){
					goodReportVO.setIs_tax("0");
				}
			}*/
			data.setRows(rows);
			data.setTotal(total);
			
		}else{
			data.setRows(new ArrayList<GoodReportVO>());
			data.setTotal(0);
		}
		return data;
	}

	@Override
	public int count(GoodReportParam param) {
		StringBuilder sb = new StringBuilder(SQL_QUERY);
		List<Object> ob = new ArrayList<Object>();
		// 时间过滤
		if (param.getFrom_date() != null) {
			sb.append(" and document_date >= ?");
			ob.add(param.getFrom_date());
		}
		if (param.getTo_date() != null) {
			sb.append(" and document_date < ?");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(param.getTo_date());
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			ob.add(calendar.getTime());
		}

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

		// 往来单位过滤
		if (StringUtils.isNotEmpty(param.getOrganization())) {
			if (param.getOrganization().indexOf(",") > 0) {
				sb.append(" and intercourse_unit_name in (");
				String[] orgs = param.getOrganization().split(",");
				for (int i = 0; i < orgs.length; i++) {
					String org = orgs[i];
					sb.append("?");
					if (i < orgs.length - 1) {
						sb.append(",");
					}
					ob.add(org);
				}
				sb.append(")");
			} else {
				sb.append(" and intercourse_unit_name like ?");
				ob.add("%" + param.getOrganization() + "%");
			}
		}

		// 单据编号过滤
		if (StringUtils.isNotEmpty(param.getReceipt_no())) {
			if (param.getReceipt_no().indexOf(",") > 0) {
				sb.append(" and document_number in (");
				String[] nums = param.getReceipt_no().split(",");
				for (int i = 0; i < nums.length; i++) {
					String num = nums[i];
					sb.append("?");
					if (i < nums.length - 1) {
						sb.append(",");
					}
					ob.add(num);
				}
				sb.append(")");
			} else {
				sb.append(" and document_number like ?");
				ob.add("%" + param.getReceipt_no() + "%");
			}
		}

		// 仓库过滤
		if (StringUtils.isNotEmpty(param.getWarehouse()) && !"-1".equalsIgnoreCase(param.getWarehouse())) {
			if (param.getWarehouse().indexOf(",") > 0) {
				String[] warehouses = param.getWarehouse().split(",");
				sb.append(" and warehouse in(");
				for (int i = 0; i < warehouses.length; i++) {
					sb.append("?");
					if (i < warehouses.length - 1) {
						sb.append(",");
					}
					ob.add(warehouses[i]);
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
