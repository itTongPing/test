package com.aukey.report.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.aukey.report.base.BaseDao;
import com.aukey.report.dao.DeclareReportDao;
import com.aukey.report.dto.DeclareReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.PageResult;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.DeclareItemVO;
import com.aukey.report.vo.DeclareReportVO;
import com.github.pagehelper.StringUtil;

@Repository
public class DeclareReportDaoImpl extends BaseDao implements DeclareReportDao {

	private Logger logger = Logger.getLogger(getClass());


	private static final String SQL_QUERY_MAIN = "SELECT `legaler`, `legaler_name`, `related_no`, `declare_status`, `sign_date`, `declare_date`, `export_date`, `declare_no`, `contract_no`, `port`, `item_no`, `customs_no`, `category`, `transaction_count`, `transaction_unit`, `price_tax`, `total_price_tax`, `total_usd_tax`, `purchase_no`, `sku`, `buyer`, `buyer_name`, `department`, `department_name`, `supplier`, `supplier_name`, `drawback_rate`, `drawback_sum`, `producer`, `producer_name`, `bill_count`, `bill_no_tax_sum`, `no_bill_count`, (CASE drawback_explain WHEN 'N/A' THEN '-' ELSE drawback_explain END) AS drawback_explain, `bill_month`, `relate_ids`, `group_column`,`auditor_name`,`verifer_name`,`audit_time`,`verif_time`,contract_number FROM declare_report t WHERE 1 = 1";


	private static final String SQL_ORDER_BY = " ORDER BY t.related_no desc,t.`item_no` + 1 ASC"; 
	
	private static final String SQL_EXPORT = "SELECT * FROM ( SELECT t.relate_id, t.legaler, t.related_no, dr.declare_status, t.sign_date, t.declare_date, t.export_date, t.declare_no, t.contract_no, t.`port`, dr.item_no, t.customs_no, t.category, sum(t.transaction_count) AS transaction_count, t.transaction_unit, IFNULL(b.price_wihtout_tax, 0.00) AS price_wihtout_tax, t.price_tax, t.total_price_tax, t.total_usd_tax, t.purchase_no, t.sku, t.buyer, t.department, t.supplier, t.drawback_rate, t.drawback_sum, t.producer, IFNULL(b.bill_count, 0) AS 'bill_count', IFNULL( t.price_tax * b.bill_count / (1 + t.tax_rate), 0.00 ) AS 'bill_no_tax_sum', t.no_bill_count, t.drawback_explain, t.bill_month, t.declare_id, t.declare_element, t.brand, t.type, t.rule_unit, t.hs_code_id, t.currency, b.bill_no, t.legaler_name, t.supplier_name, t.department_name, t.buyer_name, t.producer_name, t.auditor_name, t.verifer_name, t.audit_time, t.verif_time, b.invoice_serial_number FROM declare_report_single t JOIN declare_report dr ON t.customs_no = dr.customs_no AND t.category = dr.category AND t.drawback_rate = dr.drawback_rate AND t.price_tax = dr.price_tax AND FIND_IN_SET(t.relate_id, dr.relate_ids) LEFT JOIN declare_report_item b ON t.relate_id = b.relate_id AND t.purchase_no = b.purchase_no GROUP BY IFNULL(b.id, UUID())) t WHERE 1 = 1 ";

    private static final String SQL_DECLARE_INVOICE_EXPORT = "SELECT * FROM declare_invoice_export t where 1 = 1 ";
	
	private static final String SQL_REPORT_ITEM = "select bill_no,invoice_serial_number,category,price_tax,price_wihtout_tax,bill_count,sign_date,`name` as operator,entry_date,invoice_count,surplus_count,invoice_status from declare_report_item LEFT JOIN cas.`user` ON user_id=operator where 1=1";

	@Override
	public TableData<DeclareReportVO> queryPage(PageParam pageParam, DeclareReportParam param) {
		PageResult<DeclareReportVO> pageResult = new PageResult<DeclareReportVO>(pageParam.getPageNum(), pageParam.getNumPerPage());
		StringBuilder sb = new StringBuilder(SQL_QUERY_MAIN);
		List<Object> ob = new ArrayList<Object>();

		// 开票月份
		if (StringUtil.isNotEmpty(param.getMonth())) {
			sb.append(" and t.bill_month = ?");
			ob.add(param.getMonth());
		}

		// sku过滤
		if (StringUtils.isNotEmpty(param.getSku())) {
			if (param.getSku().indexOf(",") > 0) {
				sb.append(" and (");
				String[] skuCodes = param.getSku().split(",");
				StringBuilder sb_temp = new StringBuilder();
				for (int i = 0; i < skuCodes.length; i++) {
					sb_temp.append("FIND_IN_SET(?,t.sku) or ");
					ob.add(skuCodes[i]);
				}
				String temp = sb_temp.substring(0, sb_temp.lastIndexOf("or"));
				sb.append(temp);
				sb.append(")");
			} else {
				sb.append(" and t.sku like ?");
				ob.add("%" + param.getSku() + "%");
			}
		}

		// 报关单号过滤
		if (StringUtils.isNotEmpty(param.getDeclare_no())) {
			if (param.getDeclare_no().indexOf(",") > 0) {
				String[] purchaseNos = param.getDeclare_no().split(",");
				sb.append(" and t.declare_no in(");
				for (int i = 0; i < purchaseNos.length; i++) {
					sb.append("?");
					if (i < purchaseNos.length - 1) {
						sb.append(",");
					}
					ob.add(purchaseNos[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.declare_no like ?");
				ob.add("%" + param.getDeclare_no() + "%");

			}
		}

		// 关联编号过滤
		if (StringUtils.isNotEmpty(param.getRelated_no())) {
			if (param.getRelated_no().indexOf(",") > 0) {
				String[] relateNos = param.getRelated_no().split(",");
				sb.append(" and t.related_no in(");
				for (int i = 0; i < relateNos.length; i++) {
					sb.append("?");
					if (i < relateNos.length - 1) {
						sb.append(",");
					}
					ob.add(relateNos[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.related_no like ?");
				ob.add("%" + param.getRelated_no() + "%");

			}
		}

		// 法人主体过滤
		if (StringUtils.isNotEmpty(param.getLegaler()) && !"-1".equalsIgnoreCase(param.getLegaler())) {
			sb.append(" and t.legaler in(").append(param.getLegaler()).append(")");
		}

		// 采购单号过滤
		if (StringUtils.isNotEmpty(param.getPurchase_no())) {
			if (param.getPurchase_no().indexOf(",") > 0) {
				sb.append(" and (");
				String[] purchaseNos = param.getPurchase_no().split(",");
				StringBuilder sb_temp = new StringBuilder();
				for (int i = 0; i < purchaseNos.length; i++) {
					sb_temp.append("FIND_IN_SET(?,t.purchase_no) or ");
					ob.add(purchaseNos[i]);
				}
				String temp = sb_temp.substring(0, sb_temp.lastIndexOf("or"));
				sb.append(temp);
				sb.append(")");
			} else {
				sb.append(" and t.purchase_no like ?");
				ob.add("%" + param.getPurchase_no() + "%");
			}
		}

		// 供应商过滤
		if (StringUtils.isNotEmpty(param.getSupplier())) {
			if (param.getSupplier().indexOf(",") > 0) {
				String[] suppliers = param.getSupplier().split(",");
				sb.append(" and t.supplier_name in(");
				for (int i = 0; i < suppliers.length; i++) {
					sb.append("?");
					if (i < suppliers.length - 1) {
						sb.append(",");
					}
					ob.add(suppliers[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.supplier_name like ?");
				ob.add("%" + param.getSupplier() + "%");
			}
		}

		// 采购部门过渡
		if (StringUtils.isNotEmpty(param.getDepartment()) && !"-1".equalsIgnoreCase(param.getDepartment())) {
			if (param.getDepartment().indexOf(",") > 0) {
				String[] dptId = param.getDepartment().split(",");
				sb.append(" and t.department in(");
				for (int i = 0; i < dptId.length; i++) {
					sb.append("?");
					if (i < dptId.length - 1) {
						sb.append(",");
					}
					ob.add(dptId[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.department =?");
				ob.add(param.getDepartment());
			}
		}

		// 采购员过滤
		if (StringUtils.isNotEmpty(param.getBuyer())) {
			if (param.getBuyer().indexOf(",") > 0) {
				String[] buyers = param.getBuyer().split(",");
				sb.append(" and t.buyer_name in(");
				for (int i = 0; i < buyers.length; i++) {
					sb.append("?");
					if (i < buyers.length - 1) {
						sb.append(",");
					}
					ob.add(buyers[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.buyer_name like ?");
				ob.add("%" + param.getBuyer() + "%");
			}
		}

		// 时间过滤
		if (param.getFrom_date() != null) {
			sb.append(" and t.declare_date >= ?");
			ob.add(param.getFrom_date());
		}
		if (param.getTo_date() != null) {
			sb.append(" and t.declare_date < ?");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(param.getTo_date());
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			ob.add(calendar.getTime());
		}

		// 报关状态过滤
		if (StringUtils.isNotEmpty(param.getDeclare_status()) && !"-1".equalsIgnoreCase(param.getDeclare_status())) {
			if (param.getDeclare_status().indexOf(",") > 0) {
				String[] dptId = param.getDeclare_status().split(",");
				sb.append(" and t.declare_status in(");
				for (int i = 0; i < dptId.length; i++) {
					sb.append("?");
					if (i < dptId.length - 1) {
						sb.append(",");
					}
					ob.add(dptId[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.declare_status =?");
				ob.add(param.getDeclare_status());
			}
		}

		// 退税说明过滤
		if (StringUtils.isNotEmpty(param.getDrawback_explain())) {
			sb.append(" and t.drawback_explain like ?");
			ob.add("%" + param.getDrawback_explain() + "%");
		}

		// 退税审核员过滤
		if (StringUtils.isNotEmpty(param.getAuditor_name())) {
			if (param.getAuditor_name().indexOf(",") > 0) {
				String[] auditors = param.getAuditor_name().split(",");
				sb.append(" and t.auditor_name in(");
				for (int i = 0; i < auditors.length; i++) {
					sb.append("?");
					if (i < auditors.length - 1) {
						sb.append(",");
					}
					ob.add(auditors[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.auditor_name like ?");
				ob.add("%" + param.getAuditor_name() + "%");
			}
		}

		// 退税核验员过滤
		if (StringUtils.isNotEmpty(param.getVerifer_name())) {
			if (param.getVerifer_name().indexOf(",") > 0) {
				String[] verifers = param.getVerifer_name().split(",");
				sb.append(" and t.verifer_name in(");
				for (int i = 0; i < verifers.length; i++) {
					sb.append("?");
					if (i < verifers.length - 1) {
						sb.append(",");
					}
					ob.add(verifers[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.verifer_name like ?");
				ob.add("%" + param.getVerifer_name() + "%");
			}
		}

		// 审核时间过滤
		if (param.getFrom_audit_date() != null) {
			sb.append(" and t.audit_time >= ?");
			ob.add(param.getFrom_audit_date());
		}
		if (param.getTo_audit_date() != null) {
			sb.append(" and t.audit_time < ?");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(param.getTo_audit_date());
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			ob.add(calendar.getTime());
		}

		// 核验时间过滤
		if (param.getFrom_verif_date() != null) {
			sb.append(" and t.verif_time >= ?");
			ob.add(param.getFrom_verif_date());
		}
		if (param.getTo_verif_date() != null) {
			sb.append(" and t.verif_time < ?");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(param.getTo_verif_date());
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			ob.add(calendar.getTime());
		}

		// 无退税核验人过滤
		if (StringUtil.isNotEmpty(param.getHas_no_verifer()) && param.getHas_no_verifer().equals("1")) {
			sb.append(" and t.verifer is null");
		}

		int total = getRecordsTotal(sb.toString(), ob.toArray());
		sb.append(SQL_ORDER_BY);
		sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
		final BeanPropertyRowMapper<DeclareReportVO> rowMapper = new BeanPropertyRowMapper<DeclareReportVO>(DeclareReportVO.class);

		logger.info(sb.toString());
		List<DeclareReportVO> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
		TableData<DeclareReportVO> data = new TableData<DeclareReportVO>();
		data.setRows(rows);
		data.setTotal(total);
		return data;
	}
	
	@Override
	public TableData<DeclareReportVO> queryDeclareInvoice(PageParam pageParam, DeclareReportParam param) {
		PageResult<DeclareReportVO> pageResult = new PageResult<DeclareReportVO>(pageParam.getPageNum(), pageParam.getNumPerPage());
		List<Object> ob = new ArrayList<Object>();
		// 无退税核验人过滤
		if (StringUtil.isNotEmpty(param.getHas_no_verifer())&& param.getHas_no_verifer().equals("1")) {
			param.setHas_no_verifer("on");
		}
		StringBuilder sb = paramsToSqlStr(SQL_DECLARE_INVOICE_EXPORT, param, ob);
		sb.append(" order by related_no, CONVERT(item_no, SIGNED)");
		sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());	
		BeanPropertyRowMapper<DeclareReportVO> rowMapper = new BeanPropertyRowMapper<DeclareReportVO>(DeclareReportVO.class);
		List<DeclareReportVO> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
		TableData<DeclareReportVO> data = new TableData<DeclareReportVO>();
		data.setRows(rows);
		return data;
	}

	@Override
	public TableData<DeclareReportVO> queryPage2(PageParam pageParam, DeclareReportParam param) {
		PageResult<DeclareReportVO> pageResult = new PageResult<DeclareReportVO>(pageParam.getPageNum(), pageParam.getNumPerPage());
		List<Object> ob = new ArrayList<Object>();
		StringBuilder sb = paramsToSqlStr(SQL_EXPORT, param, ob);

		int total = getRecordsTotal(sb.toString(), ob.toArray());
		sb.append(" ORDER BY t.`drawback_explain`,t.item_no");
		sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
		final BeanPropertyRowMapper<DeclareReportVO> rowMapper = new BeanPropertyRowMapper<DeclareReportVO>(DeclareReportVO.class);

		logger.info(sb.toString());

		List<DeclareReportVO> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
		
//		StringBuilder sqlSb = new StringBuilder("SELECT * FROM declare_report_single WHERE relate_id IN (");
//		for (int index = 0; index < rows.size(); index++) {
//			if(index < rows.size() - 1){
//				sqlSb.append(rows.get(index) + ",");				
//			} else {
//				sqlSb.append(rows.get(index) + ")");								
//			}
//		}
//		getJdbcTemplate().query(sqlSb.toString(), rowMapper);
		
		TableData<DeclareReportVO> data = new TableData<DeclareReportVO>();
		data.setRows(rows);
		data.setTotal(total);
		return data;
	}
	
	private StringBuilder paramsToSqlStr(String begainSql, DeclareReportParam param, List<Object> ob){
		StringBuilder sb = new StringBuilder(begainSql);
		// 开票月份
				if (StringUtil.isNotEmpty(param.getMonth())) {
					sb.append(" and t.bill_month = ?");
					ob.add(param.getMonth().replace("-", ""));
				}

				// sku过滤
				if (StringUtils.isNotEmpty(param.getSku())) {
					if (param.getSku().indexOf(",") > 0) {
						sb.append(" and t.sku in (");
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
						sb.append(" and t.sku like ?");
						ob.add("%" + param.getSku() + "%");
					}
				}

				// 报关单号过滤
				if (StringUtils.isNotEmpty(param.getDeclare_no())) {
					if (param.getDeclare_no().indexOf(",") > 0) {
						String[] purchaseNos = param.getDeclare_no().split(",");
						sb.append(" and t.declare_no in(");
						for (int i = 0; i < purchaseNos.length; i++) {
							sb.append("?");
							if (i < purchaseNos.length - 1) {
								sb.append(",");
							}
							ob.add(purchaseNos[i]);
						}
						sb.append(")");
					} else {
						sb.append(" and t.declare_no like ?");
						ob.add("%" + param.getDeclare_no() + "%");

					}
				}

				// 关联编号过滤
				if (StringUtils.isNotEmpty(param.getRelated_no())) {
					if (param.getRelated_no().indexOf(",") > 0) {
						String[] relateNos = param.getRelated_no().split(",");
						sb.append(" and t.related_no in(");
						for (int i = 0; i < relateNos.length; i++) {
							sb.append("?");
							if (i < relateNos.length - 1) {
								sb.append(",");
							}
							ob.add(relateNos[i]);
						}
						sb.append(")");
					} else {
						sb.append(" and t.related_no like ?");
						ob.add("%" + param.getRelated_no() + "%");

					}
				}

				// 法人主体过滤
				if (StringUtils.isNotEmpty(param.getLegaler()) && !"-1".equalsIgnoreCase(param.getLegaler())) {
					sb.append(" and t.legaler in(").append(param.getLegaler()).append(")");
				}

				// 采购单号过滤
				if (StringUtils.isNotEmpty(param.getPurchase_no())) {
					if (param.getPurchase_no().indexOf(",") > 0) {
						String[] purchaseNos = param.getPurchase_no().split(",");
						sb.append(" and t.purchase_no in(");
						for (int i = 0; i < purchaseNos.length; i++) {
							sb.append("?");
							if (i < purchaseNos.length - 1) {
								sb.append(",");
							}
							ob.add(purchaseNos[i]);
						}
						sb.append(")");
					} else {
						sb.append(" and t.purchase_no like ?");
						ob.add("%" + param.getPurchase_no() + "%");

					}
				}

				// 供应商过滤
				if (StringUtils.isNotEmpty(param.getSupplier())) {
					if (param.getSupplier().indexOf(",") > 0) {
						String[] suppliers = param.getSupplier().split(",");
						sb.append(" and t.supplier_name in(");
						for (int i = 0; i < suppliers.length; i++) {
							sb.append("?");
							if (i < suppliers.length - 1) {
								sb.append(",");
							}
							ob.add(suppliers[i]);
						}
						sb.append(")");
					} else {
						sb.append(" and t.supplier_name like ?");
						ob.add("%" + param.getSupplier() + "%");
					}
				}

				// 采购部门过渡
				if (StringUtils.isNotEmpty(param.getDepartment()) && !"-1".equalsIgnoreCase(param.getDepartment())) {
					if (param.getDepartment().indexOf(",") > 0) {
						String[] dptId = param.getDepartment().split(",");
						sb.append(" and t.department in(");
						for (int i = 0; i < dptId.length; i++) {
							sb.append("?");
							if (i < dptId.length - 1) {
								sb.append(",");
							}
							ob.add(dptId[i]);
						}
						sb.append(")");
					} else {
						sb.append(" and t.department =?");
						ob.add(param.getDepartment());
					}
				}

				// 采购员过滤
				if (StringUtils.isNotEmpty(param.getBuyer())) {
					if (param.getBuyer().indexOf(",") > 0) {
						String[] buyers = param.getBuyer().split(",");
						sb.append(" and t.buyer_name in(");
						for (int i = 0; i < buyers.length; i++) {
							sb.append("?");
							if (i < buyers.length - 1) {
								sb.append(",");
							}
							ob.add(buyers[i]);
						}
						sb.append(")");
					} else {
						sb.append(" and t.buyer_name like ?");
						ob.add("%" + param.getBuyer() + "%");
					}
				}

				// 时间过滤
				if (param.getFrom_date() != null) {
					sb.append(" and t.declare_date >= ?");
					ob.add(param.getFrom_date());
				}
				if (param.getTo_date() != null) {
					sb.append(" and t.declare_date < ?");
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(param.getTo_date());
					calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
					ob.add(calendar.getTime());
				}

				// 报关状态过滤
				if (StringUtils.isNotEmpty(param.getDeclare_status()) && !"-1".equalsIgnoreCase(param.getDeclare_status())) {
					if (param.getDeclare_status().indexOf(",") > 0) {
						String[] dptId = param.getDeclare_status().split(",");
						sb.append(" and t.declare_status in(");
						for (int i = 0; i < dptId.length; i++) {
							sb.append("?");
							if (i < dptId.length - 1) {
								sb.append(",");
							}
							ob.add(dptId[i]);
						}
						sb.append(")");
					} else {
						sb.append(" and t.declare_status =?");
						ob.add(param.getDeclare_status());
					}
				}

				// 退税说明过滤
				if (StringUtils.isNotEmpty(param.getDrawback_explain())) {
					sb.append(" and t.drawback_explain like ?");
					ob.add("%" + param.getDrawback_explain() + "%");
				}

				// 退税审核员过滤
				if (StringUtils.isNotEmpty(param.getAuditor_name())) {
					if (param.getAuditor_name().indexOf(",") > 0) {
						String[] auditors = param.getAuditor_name().split(",");
						sb.append(" and t.auditor_name in(");
						for (int i = 0; i < auditors.length; i++) {
							sb.append("?");
							if (i < auditors.length - 1) {
								sb.append(",");
							}
							ob.add(auditors[i]);
						}
						sb.append(")");
					} else {
						sb.append(" and t.auditor_name like ?");
						ob.add("%" + param.getAuditor_name() + "%");
					}
				}

				// 退税核验员过滤
				if (StringUtils.isNotEmpty(param.getVerifer_name())) {
					if (param.getVerifer_name().indexOf(",") > 0) {
						String[] verifers = param.getVerifer_name().split(",");
						sb.append(" and t.verifer_name in(");
						for (int i = 0; i < verifers.length; i++) {
							sb.append("?");
							if (i < verifers.length - 1) {
								sb.append(",");
							}
							ob.add(verifers[i]);
						}
						sb.append(")");
					} else {
						sb.append(" and t.verifer_name like ?");
						ob.add("%" + param.getVerifer_name() + "%");
					}
				}

				// 审核时间过滤
				if (param.getFrom_audit_date() != null) {
					sb.append(" and t.audit_time >= ?");
					ob.add(param.getFrom_audit_date());
				}
				if (param.getTo_audit_date() != null) {
					sb.append(" and t.audit_time < ?");
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(param.getTo_audit_date());
					calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
					ob.add(calendar.getTime());
				}

				// 核验时间过滤
				if (param.getFrom_verif_date() != null) {
					sb.append(" and t.verif_time >= ?");
					ob.add(param.getFrom_verif_date());
				}
				if (param.getTo_verif_date() != null) {
					sb.append(" and t.verif_time < ?");
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(param.getTo_verif_date());
					calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
					ob.add(calendar.getTime());
				}
				
				// 无退税核验人过滤
				if (StringUtil.isNotEmpty(param.getHas_no_verifer()) && param.getHas_no_verifer().equals("on")) {
					sb.append(" and t.verifer_name is null");
				}
		return sb;
	}

	@Override
	public int count(DeclareReportParam param) {
		StringBuilder sb = new StringBuilder(SQL_EXPORT);
		List<Object> ob = new ArrayList<Object>();
		// 开票月份
		if (StringUtil.isNotEmpty(param.getMonth())) {
			sb.append(" and t.bill_month = ?");
			ob.add(param.getMonth());
		}

		// sku过滤
		if (StringUtils.isNotEmpty(param.getSku())) {
			if (param.getSku().indexOf(",") > 0) {
				sb.append(" and t.sku in (");
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
				sb.append(" and t.sku like ?");
				ob.add("%" + param.getSku() + "%");
			}
		}

		// 报关单号过滤
		if (StringUtils.isNotEmpty(param.getDeclare_no())) {
			if (param.getDeclare_no().indexOf(",") > 0) {
				String[] purchaseNos = param.getDeclare_no().split(",");
				sb.append(" and t.declare_no in(");
				for (int i = 0; i < purchaseNos.length; i++) {
					sb.append("?");
					if (i < purchaseNos.length - 1) {
						sb.append(",");
					}
					ob.add(purchaseNos[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.declare_no like ?");
				ob.add("%" + param.getDeclare_no() + "%");

			}
		}

		// 关联编号过滤
		if (StringUtils.isNotEmpty(param.getRelated_no())) {
			if (param.getRelated_no().indexOf(",") > 0) {
				String[] relateNos = param.getRelated_no().split(",");
				sb.append(" and t.related_no in(");
				for (int i = 0; i < relateNos.length; i++) {
					sb.append("?");
					if (i < relateNos.length - 1) {
						sb.append(",");
					}
					ob.add(relateNos[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.related_no like ?");
				ob.add("%" + param.getRelated_no() + "%");

			}
		}

		// 法人主体过滤
		if (StringUtils.isNotEmpty(param.getLegaler()) && !"-1".equalsIgnoreCase(param.getLegaler())) {
			sb.append(" and t.legaler in(").append(param.getLegaler()).append(")");
		}

		// 采购单号过滤
		if (StringUtils.isNotEmpty(param.getPurchase_no())) {
			if (param.getPurchase_no().indexOf(",") > 0) {
				String[] purchaseNos = param.getPurchase_no().split(",");
				sb.append(" and t.purchase_no in(");
				for (int i = 0; i < purchaseNos.length; i++) {
					sb.append("?");
					if (i < purchaseNos.length - 1) {
						sb.append(",");
					}
					ob.add(purchaseNos[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.purchase_no like ?");
				ob.add("%" + param.getPurchase_no() + "%");
			}
		}

		// 供应商过滤
		if (StringUtils.isNotEmpty(param.getSupplier())) {
			if (param.getSupplier().indexOf(",") > 0) {
				String[] suppliers = param.getSupplier().split(",");
				sb.append(" and t.supplier_name in(");
				for (int i = 0; i < suppliers.length; i++) {
					sb.append("?");
					if (i < suppliers.length - 1) {
						sb.append(",");
					}
					ob.add(suppliers[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.supplier_name like ?");
				ob.add("%" + param.getSupplier() + "%");
			}
		}

		// 采购部门过渡
		if (StringUtils.isNotEmpty(param.getDepartment()) && !"-1".equalsIgnoreCase(param.getDepartment())) {
			if (param.getDepartment().indexOf(",") > 0) {
				String[] dptId = param.getDepartment().split(",");
				sb.append(" and t.department in(");
				for (int i = 0; i < dptId.length; i++) {
					sb.append("?");
					if (i < dptId.length - 1) {
						sb.append(",");
					}
					ob.add(dptId[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.department =?");
				ob.add(param.getDepartment());
			}
		}

		// 采购员过滤
		if (StringUtils.isNotEmpty(param.getBuyer())) {
			if (param.getBuyer().indexOf(",") > 0) {
				String[] buyers = param.getBuyer().split(",");
				sb.append(" and t.buyer_name in(");
				for (int i = 0; i < buyers.length; i++) {
					sb.append("?");
					if (i < buyers.length - 1) {
						sb.append(",");
					}
					ob.add(buyers[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.buyer_name like ?");
				ob.add("%" + param.getBuyer() + "%");
			}
		}

		// 时间过滤
		if (param.getFrom_date() != null) {
			sb.append(" and t.declare_date >= ?");
			ob.add(param.getFrom_date());
		}
		if (param.getTo_date() != null) {
			sb.append(" and t.declare_date < ?");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(param.getTo_date());
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			ob.add(calendar.getTime());
		}

		// 报关状态过滤
		if (StringUtils.isNotEmpty(param.getDeclare_status()) && !"-1".equalsIgnoreCase(param.getDeclare_status())) {
			if (param.getDeclare_status().indexOf(",") > 0) {
				String[] dptId = param.getDeclare_status().split(",");
				sb.append(" and t.declare_status in(");
				for (int i = 0; i < dptId.length; i++) {
					sb.append("?");
					if (i < dptId.length - 1) {
						sb.append(",");
					}
					ob.add(dptId[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.declare_status =?");
				ob.add(param.getDeclare_status());
			}
		}

		// 退税说明过滤
		if (StringUtils.isNotEmpty(param.getDrawback_explain())) {
			sb.append(" and t.drawback_explain like ?");
			ob.add("%" + param.getDrawback_explain() + "%");
		}
		// 退税审核员过滤
		if (StringUtils.isNotEmpty(param.getAuditor_name())) {
			if (param.getAuditor_name().indexOf(",") > 0) {
				String[] auditors = param.getAuditor_name().split(",");
				sb.append(" and t.auditor_name in(");
				for (int i = 0; i < auditors.length; i++) {
					sb.append("?");
					if (i < auditors.length - 1) {
						sb.append(",");
					}
					ob.add(auditors[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.auditor_name like ?");
				ob.add("%" + param.getAuditor_name() + "%");
			}
		}

		// 退税核验员过滤
		if (StringUtils.isNotEmpty(param.getVerifer_name())) {
			if (param.getVerifer_name().indexOf(",") > 0) {
				String[] verifers = param.getVerifer_name().split(",");
				sb.append(" and t.verifer_name in(");
				for (int i = 0; i < verifers.length; i++) {
					sb.append("?");
					if (i < verifers.length - 1) {
						sb.append(",");
					}
					ob.add(verifers[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and t.verifer_name like ?");
				ob.add("%" + param.getVerifer_name() + "%");
			}
		}

		// 审核时间过滤
		if (param.getFrom_audit_date() != null) {
			sb.append(" and t.audit_time >= ?");
			ob.add(param.getFrom_audit_date());
		}
		if (param.getTo_audit_date() != null) {
			sb.append(" and t.audit_time < ?");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(param.getTo_audit_date());
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			ob.add(calendar.getTime());
		}

		// 核验时间过滤
		if (param.getFrom_verif_date() != null) {
			sb.append(" and t.verif_time >= ?");
			ob.add(param.getFrom_verif_date());
		}
		if (param.getTo_verif_date() != null) {
			sb.append(" and t.verif_time < ?");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(param.getTo_verif_date());
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			ob.add(calendar.getTime());
		}
		
		// 无退税核验人过滤
		if (StringUtil.isNotEmpty(param.getHas_no_verifer()) && param.getHas_no_verifer().equals("on")) {
			sb.append(" and t.verifer_name is null");
		}

		return getRecordsTotal(sb.toString(), ob.toArray());
	}

	@Override
	public TableData<DeclareItemVO> queryPage3(PageParam pageParam, DeclareReportParam param) {
		PageResult<DeclareItemVO> pageResult = new PageResult<DeclareItemVO>(pageParam.getPageNum(), pageParam.getNumPerPage());
		StringBuilder sb = new StringBuilder(SQL_REPORT_ITEM);
		List<Object> ob = new ArrayList<Object>();

		if (StringUtils.isNotEmpty(param.getRelate_ids())) {
			if (param.getRelate_ids().indexOf(",") > 0) {
				sb.append(" and relate_id in (");
				String[] skuCodes = param.getRelate_ids().split(",");
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
				sb.append(" and relate_id = ?");
				ob.add(param.getRelate_ids());
			}
		} else {
			return new TableData<DeclareItemVO>();
		}

		int total = getRecordsTotal(sb.toString(), ob.toArray());
		sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
		final BeanPropertyRowMapper<DeclareItemVO> rowMapper = new BeanPropertyRowMapper<DeclareItemVO>(DeclareItemVO.class);

		logger.info(sb.toString());

		List<DeclareItemVO> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
		TableData<DeclareItemVO> data = new TableData<DeclareItemVO>();
		data.setRows(rows);
		data.setTotal(total);
		return data;

	}

}
