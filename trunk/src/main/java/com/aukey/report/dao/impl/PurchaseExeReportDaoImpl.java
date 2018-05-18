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
import com.aukey.report.dao.PurchaseExeReportDao;
import com.aukey.report.dto.PurchaseExeReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.PageResult;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.PayDtail;
import com.aukey.report.vo.PurchaseExeReportVO;

@Repository
public class PurchaseExeReportDaoImpl extends BaseDao implements PurchaseExeReportDao {

	private Logger logger = Logger.getLogger(getClass());

	private static final String SQL_QUERY = "SELECT `supplier_name`, `legaler_name`, `purchase_no`, `purchase_date`, `purchase_amount_all`, `purchase_currency`, `purchaser_name`, `purchase_department` AS department_name, `inventory_warehouse_name`, `inventory_amount`, `un_inventory_amount`, CASE `inventory_status` WHEN 0 THEN '未处理' WHEN 1 THEN '审核中' WHEN 2 THEN '未入库' WHEN 3 THEN '部分入库' WHEN 4 THEN '全部入库' END AS inventory_status, `payment_all`, `un_payment`, CASE `payment_status` WHEN 0 THEN '未请款' WHEN 1 THEN '未付款' WHEN 2 THEN '部分付款' WHEN 3 THEN '已全付' END AS payment_status, CASE `is_tax` WHEN 0 THEN '不含税' WHEN 1 THEN '含税' END AS is_tax, `exchange_rate`, CASE `bill_status` WHEN 0 THEN '全部开票' WHEN 1 THEN '未开票' WHEN 2 THEN '部分开票' END AS bill_status, `un_bill_amount`, `bill_amount`, CASE `bill_contract` WHEN 0 THEN '未签署' ELSE '已签署' END AS bill_contract FROM purchase_exec_report WHERE 1 = 1";

	private static final String SQL_QUERY_DETAIL = "SELECT t.relate_order_id AS purchase_no, t.currency AS currency_pay, '' AS exchange_rate, COALESCE(SUM(CASE WHEN t.`verify_status`='FLOW_END' THEN t.recei_money ELSE 0 END),0) AS payment FROM ( SELECT rro.*, rf.verify_status, rf.`request_id` FROM (( SELECT relate_id, relate_order_id, recei_money, unpaid_money, currency FROM supply_bankroll.`request_relate_order` WHERE relate_order_id = ?) rro JOIN supply_bankroll.`request_funds_order_relate` rfor ON ( rro.`relate_id` = rfor.`relate_id` ) JOIN supply_bankroll.`request_funds_order` rf ON ( rfor.`request_id` = rf.`request_id` AND data_status = '1' AND func_state = '1' ))) t GROUP BY t.relate_order_id, t.currency";

	@Override
	public TableData<PurchaseExeReportVO> queryPage(PageParam pageParam, PurchaseExeReportParam param) {
		PageResult<PurchaseExeReportVO> pageResult = new PageResult<PurchaseExeReportVO>(pageParam.getPageNum(), pageParam.getNumPerPage());

		StringBuilder sb = new StringBuilder(SQL_QUERY);
		List<Object> ob = new ArrayList<Object>();

		// 时间过滤
		if (param.getFrom_date() != null) {
			sb.append(" and purchase_date >= ?");
			ob.add(param.getFrom_date());
		}
		if (param.getTo_date() != null) {
			sb.append(" and purchase_date < ?");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(param.getTo_date());
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			ob.add(calendar.getTime());
		}

		// 供应商名称过滤
		if (StringUtils.isNotEmpty(param.getSupplier())) {
			if (param.getSupplier().indexOf(",") > 0) {
				sb.append(" and supplier_name in (");
				String[] suppliers = param.getSupplier().split(",");
				for (int i = 0; i < suppliers.length; i++) {
					String supplier = suppliers[i];
					sb.append("?");
					if (i < suppliers.length - 1) {
						sb.append(",");
					}
					ob.add(supplier);
				}
				sb.append(")");
			} else {
				sb.append(" and supplier_name like ?");
				ob.add("%" + param.getSupplier() + "%");
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

		// 采购部门过渡
		if (StringUtils.isNotEmpty(param.getDepartment()) && !"-1".equalsIgnoreCase(param.getDepartment())) {
			if (param.getDepartment().indexOf(",") > 0) {
				String[] dptId = param.getDepartment().split(",");
				sb.append(" and purchase_department_id in(");
				for (int i = 0; i < dptId.length; i++) {
					sb.append("?");
					if (i < dptId.length - 1) {
						sb.append(",");
					}
					ob.add(dptId[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and purchase_department_id =?");
				ob.add(param.getDepartment());
			}
		}

		// 采购单号过滤
		if (StringUtils.isNotEmpty(param.getPurchase_no())) {
			if (param.getPurchase_no().indexOf(",") > 0) {
				sb.append(" and purchase_no in (");
				String[] orgs = param.getPurchase_no().split(",");
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
				sb.append(" and purchase_no like ?");
				ob.add("%" + param.getPurchase_no() + "%");
			}
		}

		// 是否含税过滤
		if (StringUtils.isNotEmpty(param.getInclude_tax()) && !"-1".equalsIgnoreCase(param.getInclude_tax())) {
			sb.append(" and is_tax ='").append(param.getInclude_tax()).append("'");
		}

		// 付款状态过滤
		if (StringUtils.isNotEmpty(param.getPay_status()) && !"-1".equalsIgnoreCase(param.getPay_status())) {
			if (param.getPay_status().indexOf(",") > 0) {
				sb.append(" and payment_status in (");
				String[] orgs = param.getPay_status().split(",");
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
				sb.append(" and payment_status = ?");
				ob.add(param.getPay_status());
			}
		}

		// 开票状态过滤
		if (StringUtils.isNotEmpty(param.getInvoice_status()) && !"-1".equalsIgnoreCase(param.getInvoice_status())) {
			if (param.getInvoice_status().indexOf(",") > 0) {
				sb.append(" and bill_status in (");
				String[] bills = param.getInvoice_status().split(",");
				for (int i = 0; i < bills.length; i++) {
					String org = bills[i];
					sb.append("?");
					if (i < bills.length - 1) {
						sb.append(",");
					}
					ob.add(org);
				}
				sb.append(")");
			} else {
				sb.append(" and bill_status =?");
				ob.add(param.getInvoice_status());
			}
		}

		// 合同状态过滤
		if (StringUtils.isNotEmpty(param.getContract_status()) && !"-1".equalsIgnoreCase(param.getContract_status())) {
			if (param.getContract_status().equals("0")) {
				sb.append(" and bill_contract = 0 ");
			} else {
				sb.append(" and bill_contract > 0 ");
			}
		}

		int total = getRecordsTotal(sb.toString(), ob.toArray());

		sb.append(" order by purchase_date desc");
		sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
		final BeanPropertyRowMapper<PurchaseExeReportVO> rowMapper = new BeanPropertyRowMapper<PurchaseExeReportVO>(PurchaseExeReportVO.class);

		logger.info(sb.toString());

		List<PurchaseExeReportVO> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);

		for (PurchaseExeReportVO vo : rows) {
			String purchase_no = vo.getPurchase_no();
			List<Object> ob_detail = new ArrayList<Object>();
			ob_detail.add(purchase_no);
			BeanPropertyRowMapper<PayDtail> rowMapper_detail = new BeanPropertyRowMapper<PayDtail>(PayDtail.class);
			List<PayDtail> list = getJdbcTemplate().query(SQL_QUERY_DETAIL, ob_detail.toArray(), rowMapper_detail);
			vo.setPayment(list);
		}

		TableData<PurchaseExeReportVO> data = new TableData<PurchaseExeReportVO>();
		data.setRows(rows);
		data.setTotal(total);
		return data;
	}

	@Override
	public int count(PurchaseExeReportParam param) {
		StringBuilder sb = new StringBuilder(SQL_QUERY);
		List<Object> ob = new ArrayList<Object>();

		// 时间过滤
		if (param.getFrom_date() != null) {
			sb.append(" and purchase_date >= ?");
			ob.add(param.getFrom_date());
		}
		if (param.getTo_date() != null) {
			sb.append(" and purchase_date < ?");
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(param.getTo_date());
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			ob.add(calendar.getTime());
		}

		// 供应商名称过滤
		if (StringUtils.isNotEmpty(param.getSupplier())) {
			if (param.getSupplier().indexOf(",") > 0) {
				sb.append(" and supplier_name in (");
				String[] suppliers = param.getSupplier().split(",");
				for (int i = 0; i < suppliers.length; i++) {
					String supplier = suppliers[i];
					sb.append("?");
					if (i < suppliers.length - 1) {
						sb.append(",");
					}
					ob.add(supplier);
				}
				sb.append(")");
			} else {
				sb.append(" and supplier_name like ?");
				ob.add("%" + param.getSupplier() + "%");
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

		// 采购部门过渡
		if (StringUtils.isNotEmpty(param.getDepartment()) && !"-1".equalsIgnoreCase(param.getDepartment())) {
			if (param.getDepartment().indexOf(",") > 0) {
				String[] dptId = param.getDepartment().split(",");
				sb.append(" and purchase_department_id in(");
				for (int i = 0; i < dptId.length; i++) {
					sb.append("?");
					if (i < dptId.length - 1) {
						sb.append(",");
					}
					ob.add(dptId[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and purchase_department_id =?");
				ob.add(param.getDepartment());
			}
		}

		// 采购单号过滤
		if (StringUtils.isNotEmpty(param.getPurchase_no())) {
			if (param.getPurchase_no().indexOf(",") > 0) {
				sb.append(" and purchase_no in (");
				String[] orgs = param.getPurchase_no().split(",");
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
				sb.append(" and purchase_no like ?");
				ob.add("%" + param.getPurchase_no() + "%");
			}
		}

		// 是否含税过滤
		if (StringUtils.isNotEmpty(param.getInclude_tax()) && !"-1".equalsIgnoreCase(param.getInclude_tax())) {
			sb.append(" and is_tax ='").append(param.getInclude_tax()).append("'");
		}

		// 付款状态过滤
		if (StringUtils.isNotEmpty(param.getPay_status()) && !"-1".equalsIgnoreCase(param.getPay_status())) {
			if (param.getPay_status().indexOf(",") > 0) {
				sb.append(" and payment_status in (");
				String[] orgs = param.getPay_status().split(",");
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
				sb.append(" and payment_status = ?");
				ob.add(param.getPay_status());
			}
		}

		// 开票状态过滤
		if (StringUtils.isNotEmpty(param.getInvoice_status()) && !"-1".equalsIgnoreCase(param.getInvoice_status())) {
			if (param.getInvoice_status().indexOf(",") > 0) {
				sb.append(" and bill_status in (");
				String[] bills = param.getInvoice_status().split(",");
				for (int i = 0; i < bills.length; i++) {
					String org = bills[i];
					sb.append("?");
					if (i < bills.length - 1) {
						sb.append(",");
					}
					ob.add(org);
				}
				sb.append(")");
			} else {
				sb.append(" and bill_status =?");
				ob.add(param.getInvoice_status());
			}
		}

		// 合同状态过滤
		if (StringUtils.isNotEmpty(param.getContract_status()) && !"-1".equalsIgnoreCase(param.getContract_status())) {
			if (param.getContract_status().equals("0")) {
				sb.append(" and bill_contract = 0 ");
			} else {
				sb.append(" and bill_contract > 0 ");
			}
		}

		return getRecordsTotal(sb.toString(), ob.toArray());
	}
}
