package com.aukey.report.dao.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aukey.report.base.BaseDao;
import com.aukey.report.dao.PurchaseWarnDao;
import com.aukey.report.domain.PurchaseWarnReport;
import com.aukey.report.dto.PurchaseWarnParam;
import com.aukey.report.mapper.PurchaseWarnMapper;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.PageResult;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.DepartmentResult;
import com.aukey.report.vo.LegalerResult;
import com.aukey.report.vo.DepartmentResult.DepartmentVO;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CommonUtil;
import com.aukey.util.DateUtil;
import com.aukey.util.HttpUtil;
import com.aukey.util.URLBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.template.SimpleDate;

@Repository
public class PurchaseWarnDaoImpl extends BaseDao implements PurchaseWarnDao {

	private Logger logger = Logger.getLogger(getClass());

	private static final String SQL_QUERY = "SELECT pwr.purchase_no,purchase_warn_demand, pwr.sku_code, pwr.sku_name, pwr.legaler_name, pwr.supplier_name, pwr.purchase_date, pwr.purchase_count, pwr.currency, price_tax, pwr.price_without_tax,"+
													"pwr.purchase_sum, pwr.buyer_name, pwr.department_id,  purchase_group_name,  pwr.stock_count, pwr.return_count, IF(purchase_count - ifnull(stock_count, 0) - ifnull(return_count, 0) + ifnull(stop_return_count, 0)>0,purchase_count - ifnull(stock_count, 0) - ifnull(return_count, 0) + ifnull(stop_return_count, 0),0) AS lack_count,"+
												"(	CASE WHEN purchase_count - ifnull(stock_count, 0) - ifnull(return_count, 0) + ifnull(stop_return_count, 0) > 0 THEN IF(datediff(now(), before_stock_date)>0,datediff(now(), before_stock_date),0) ELSE IF(datediff((SELECT MIN(create_date) FROM supply_sign.`storage` s WHERE s.purchase_number = pwr.purchase_no AND s.sku_code = pwr.sku_code),before_stock_date)<0,0,datediff((SELECT MIN(create_date) FROM supply_sign.`storage` s WHERE s.purchase_number = pwr.purchase_no AND s.sku_code = pwr.sku_code),before_stock_date)) END) AS out_date,"+
												"(CASE WHEN datediff(now(), before_stock_date) > 0 THEN IF(purchase_count - ifnull(stock_count, 0) - ifnull(return_count, 0) + ifnull(stop_return_count, 0)>0,purchase_count - ifnull(stock_count, 0) - ifnull(return_count, 0) + ifnull(stop_return_count, 0),0) END ) AS out_date_count,"+
												"pwr.before_stock_date,"+
												"pwr.last_update_date "+
											"FROM "+
												"purchase_warn_report pwr "+
											"WHERE "+
												"1 = 1";// AND purchase_count - ifnull(stock_count, 0) - ifnull(return_count, 0) + ifnull(stop_return_count, 0)>0
	private static final String SQL_COUNT = "purchase_warn_report where 1=1";// AND purchase_count - ifnull(stock_count, 0) - ifnull(return_count, 0) + ifnull(stop_return_count, 0)>0
	
	private static final String SQL_PURNO = "SELECT purchase_no FROM purchase_warn_report where 1=1";//AND purchase_count - ifnull(stock_count, 0) - ifnull(return_count, 0) + ifnull(stop_return_count, 0)>0
	
	//需求部门的接口
	@Value("${purchase.department.sales.api.url}")
    private String all_department_sales_list_url;
	@Autowired
	private PurchaseWarnMapper purchaseWarnMapper;
	
	
	
	@Override
	public TableData<PurchaseWarnReport> queryPage(PageParam pageParam, PurchaseWarnParam param) {
		PageResult<PurchaseWarnReport> pageResult = new PageResult<PurchaseWarnReport>(pageParam.getPageNum(), pageParam.getNumPerPage());
		TableData<PurchaseWarnReport> data = null;
		try {
			StringBuilder sb = new StringBuilder(SQL_QUERY);
			StringBuilder sc = new StringBuilder(SQL_COUNT);
			List<Object> ob = new ArrayList<Object>();
			boolean flagPay = true;
			List<Map<String,Object>> userGroup2 = RoleUtil.getUserGroup();
			for (Map<String, Object> map : userGroup2) 
			{
				if("CUSTOMS".equals(map.get("cateogryCode")))
				{
					flagPay = false;
					break;
				}
			}
			// 时间过滤
			if (param.getFrom_date() != null) {
				sb.append(" and purchase_date >= ?");
				sc.append(" and purchase_date >= ?");
				ob.add(param.getFrom_date());
			}
			if (param.getTo_date() != null) {
				sb.append(" and purchase_date < ?");
				sc.append(" and purchase_date < ?");
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(param.getTo_date());
				calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
				ob.add(calendar.getTime());
			}

			// skucode过滤
			if (StringUtils.isNotEmpty(param.getSku_code())&&param.getSku_code()!=null) {
				if (param.getSku_code().indexOf(",") > 0) {
					sb.append(" and sku_code in (");
					sc.append(" and sku_code in (");
					String[] skuCodes = param.getSku_code().split(",");
					for (int i = 0; i < skuCodes.length; i++) {
						String skuCode = skuCodes[i];
						sb.append("?");
						sc.append("?");
						if (i < skuCodes.length - 1) {
							sb.append(",");
							sc.append(",");
						}
						ob.add(skuCode);
					}
					sb.append(")");
					sc.append(")");
				} else {
					sb.append(" and sku_code like ?");
					sc.append(" and sku_code like ?");
					ob.add("%" + param.getSku_code() + "%");
				}
			}
			// skucode过滤
					if (StringUtils.isNotEmpty(param.getSku_code())&&param.getSku_code()!=null) {
						if (param.getSku_code().indexOf(",") > 0) {
							sb.append(" and sku_code in (");
							sc.append(" and sku_code in (");
							String[] skuCodes = param.getSku_code().split(",");
							for (int i = 0; i < skuCodes.length; i++) {
								String skuCode = skuCodes[i];
								sb.append("?");
								sc.append("?");
								if (i < skuCodes.length - 1) {
									sb.append(",");
									sc.append(",");
								}
								ob.add(skuCode);
							}
							sb.append(")");
							sc.append(")");
						} else {
							sb.append(" and sku_code like ?");
							sc.append(" and sku_code like ?");
							ob.add("%" + param.getSku_code() + "%");
						}
					}
					// 供应商过滤
					if (StringUtils.isNotEmpty(param.getSupplier_id())&&param.getSupplier_id()!=null) {
						if (param.getSupplier_id().indexOf(",") > 0) {
							sb.append(" and supplier_name in (");
							sc.append(" and supplier_name in (");
							String[] supplierIds = param.getSupplier_id().split(",");
							for (int i = 0; i < supplierIds.length; i++) {
								String supplierId = supplierIds[i];
								sb.append("?");
								sc.append("?");
								if (i < supplierIds.length - 1) {
									sb.append(",");
									sc.append(",");
								}
								ob.add(supplierId);
							}
							sb.append(")");
							sc.append(")");
						} else {
							sb.append(" and supplier_name like ?");
							sc.append(" and supplier_name like ?");
							ob.add("%" + param.getSupplier_id() + "%");
						}
					}
					// 采购订单过滤
					if (StringUtils.isNotEmpty(param.getPurchase_order_id())&&param.getPurchase_order_id()!=null) {
						if (param.getPurchase_order_id().indexOf(",") > 0) {
							sb.append(" and purchase_no in (");
							sc.append(" and purchase_no in (");
							String[] supplierIds = param.getPurchase_order_id().split(",");
							for (int i = 0; i < supplierIds.length; i++) {
								String supplierId = supplierIds[i];
								sb.append("?");
								sc.append("?");
								if (i < supplierIds.length - 1) {
									sb.append(",");
									sc.append(",");
								}
								ob.add(supplierId);
							}
							sb.append(")");
							sc.append(")");
						} else {
							sb.append(" and purchase_no like ?");
							sc.append(" and purchase_no like ?");
							ob.add("%" + param.getPurchase_order_id() + "%");
						}
					}

			// 法人主体过滤
			if (param.getLegaler_id()!=null&&StringUtils.isNotEmpty(param.getLegaler_id()) && !"-1".equalsIgnoreCase(param.getLegaler_id())) {
				if (param.getLegaler_id().indexOf(",") > 0) {
					String[] legalers = param.getLegaler_id().split(",");
					sb.append(" and legaler_id in(");
					sc.append(" and legaler_id in(");
					for (int i = 0; i < legalers.length; i++) {
						sb.append("?");
						sc.append("?");
						if (i < legalers.length - 1) {
							sb.append(",");
							sc.append(",");
						}
						ob.add(legalers[i]);
					}
					sb.append(")");
					sc.append(")");
				} else {
					sb.append(" and legaler_id = ?");
					sc.append(" and legaler_id = ?");
					ob.add(param.getLegaler_id());
				}
			}

			// 预计时间过滤
					if (param.getBefore_from_data() != null) {
						sb.append(" and before_stock_date >= ?");
						sc.append(" and before_stock_date >= ?");
						ob.add(param.getBefore_from_data());
					}
					if (param.getBefore_to_data() != null) {
						sb.append(" and before_stock_date < ?");
						sc.append(" and before_stock_date < ?");
						Calendar calendar = new GregorianCalendar();
						calendar.setTime(param.getBefore_to_data());
						calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
						ob.add(calendar.getTime());
					}
			//业务部门过滤（需求部门）
			
					if (param.getDepartment_id()!=null&&StringUtils.isNotEmpty(param.getDepartment_id()) && !"-1".equalsIgnoreCase(param.getDepartment_id())) {
						if (param.getDepartment_id().indexOf(",") > 0) {
							String[] legalers = param.getDepartment_id().split(",");
							sb.append(" and department_id in(");
							sc.append(" and department_id in(");
							for (int i = 0; i < legalers.length; i++) {
								sb.append("?");
								sc.append("?");
								if (i < legalers.length - 1) {
									sb.append(",");
									sc.append(",");
								}
								ob.add(legalers[i]);
							}
							sb.append(")");
							sc.append(")");
						} else {
							sb.append(" and department_id = ?");
							sc.append(" and department_id = ?");
							ob.add(param.getDepartment_id());
						}
					}
					//采购部门过滤（具有权限控制）
					if (param.getPurchase_group_id()!=null&&StringUtils.isNotEmpty(param.getPurchase_group_id()) && !"-1".equalsIgnoreCase(param.getPurchase_group_id())) {
						if (param.getPurchase_group_id().indexOf(",") > 0) {
							String[] grouoIds = param.getPurchase_group_id().split(",");
							sb.append(" and purchase_group_id in(");
							sc.append(" and purchase_group_id in(");
							for (int i = 0; i < grouoIds.length; i++) {
								sb.append("?");
								sc.append("?");
								if (i < grouoIds.length - 1) {
									sb.append(",");
									sc.append(",");
								}
								ob.add(grouoIds[i]);
							}
							sb.append(")");
							sc.append(")");
						} else {
							sb.append(" and purchase_group_id = ?");
							sc.append(" and purchase_group_id = ?");
							ob.add(param.getPurchase_group_id());
						}
						/*sb.append(")");
						sc.append(")");*/
				}
					//是否加上不欠货
					if("0".equals(param.getFlag()))
					{
						sb.append(" AND purchase_count - ifnull(stock_count, 0) - ifnull(return_count, 0) + ifnull(stop_return_count, 0)>0");
						sc.append(" AND purchase_count - ifnull(stock_count, 0) - ifnull(return_count, 0) + ifnull(stop_return_count, 0)>0");
					}
		if(flagPay)
		{
			//用户采购部门权限控制
			StringBuilder sqlPurNo = new StringBuilder(SQL_PURNO);//采购员
			StringBuilder sqlPurNo2 = new StringBuilder(" UNION ALL ");
			sqlPurNo2.append(sqlPurNo);//采购主管以上
			List<Map<String,Object>> userGroup = RoleUtil.getUserSPurchaseGroup();
			logger.info(userGroup);
			String buyerId = "";//采购员Id
			String buyerPurchaseGroupId = "";//采购员所在组
			String purchaseGroupIds = "";//采购主管以上
			for (Map<String, Object> map : userGroup) 
			{
				boolean flag = false;//判断是否是采购员 false：是采购员，true:不是采购员
				for (Map<String, Object> map2 : userGroup) 
				{
					//orgGroupId
					if(map.get("orgGroupId").equals(map2.get("orgGroupId")))
					{
						//map2.get("orgCode")
						if(!map2.get("orgCode").equals("4003"))
						{
							flag = true;//非采购员
							break;
						}
					}
				}
				if(flag)
				{
					purchaseGroupIds += map.get("orgGroupId")+",";
				}else
				{
					buyerId = RoleUtil.getUserId();
					buyerPurchaseGroupId += map.get("orgGroupId")+",";
				}
			}
			sqlPurNo.append(" and buyer_id= ?");
			ob.add(buyerId);//采购员
			//采购员所在组
			if (CommonUtil.isNotEmpty(buyerPurchaseGroupId)) {
				if (buyerPurchaseGroupId.indexOf(",") > 0) {
					String[] grouoIds = buyerPurchaseGroupId.split(",");
					sqlPurNo.append(" and purchase_group_id in(");
					for (int i = 0; i < grouoIds.length; i++) {
						sqlPurNo.append("?");
						if (i < grouoIds.length - 1) {
							sqlPurNo.append(",");
						}
						ob.add(grouoIds[i]);
					}
					sqlPurNo.append(")");
				} else {
					sqlPurNo.append(" and purchase_group_id = ?");
					ob.add(buyerPurchaseGroupId);
				}
			}
			//采购主管以上
			if (CommonUtil.isNotEmpty(purchaseGroupIds)) {
				if (purchaseGroupIds.indexOf(",") > 0) {
					String[] grouoIds = purchaseGroupIds.split(",");
					sqlPurNo2.append(" and purchase_group_id in(");
					for (int i = 0; i < grouoIds.length; i++) {
						sqlPurNo2.append("?");
						if (i < grouoIds.length - 1) {
							sqlPurNo2.append(",");
						}
						ob.add(grouoIds[i]);
					}
					sqlPurNo2.append(")");
				} else {
					sqlPurNo2.append(" and purchase_group_id = ?");
					ob.add(purchaseGroupIds);
				}
			}else
			{
				sqlPurNo2.append(" and purchase_group_id = ?");
				ob.add(purchaseGroupIds);
			}
			//sqlPurNo.append(" purchase_group_id in")
			
			sb.append(" and purchase_no in("+sqlPurNo+sqlPurNo2+")");
			sc.append(" and purchase_no in("+sqlPurNo+sqlPurNo2+")");
		}
		
		int total = getRecordsTotal2(sc.toString(), ob.toArray());
		
			
		sb.append(" order by purchase_date desc");
		sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
		final BeanPropertyRowMapper<PurchaseWarnReport> rowMapper = new BeanPropertyRowMapper<PurchaseWarnReport>(PurchaseWarnReport.class);

		logger.info(sb.toString());

		List<PurchaseWarnReport> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
		if(rows.size()==0){
			total=0;
		}
		// 获取需求部门列表
		List<DepartmentVO> department_list = new ArrayList<DepartmentVO>();
		
			AjaxResponse all_department_result = HttpUtil.doGet(all_department_sales_list_url);
			if (all_department_result.getData() != null) {
				DepartmentResult DepartmentResult = JSON.parseObject(all_department_result.getData().toString(), DepartmentResult.class);
				department_list = DepartmentResult.getData();
			}
		
		for (DepartmentVO departmentVO : department_list) {
			for (PurchaseWarnReport pr : rows) {
				if((departmentVO.getGroupId()+"").equals(pr.getDepartment_id())){
					pr.setDepartment_id(departmentVO.getGroupName());
				}
			}
		}
		data = new TableData<PurchaseWarnReport>();
		if(rows==null||rows.isEmpty()){
			
		}else{
			//对rows增加指派时间
			List<String> demandList=rows.stream().map(PurchaseWarnReport::getPurchase_warn_demand).collect(Collectors.toList());
			//根据需求单获取指派时间和销售员
			List<Map<String,Object>> assignMapList=purchaseWarnMapper.queryAssignTime(demandList);
			Map<String,Date> assignMap=new HashMap<String,Date>();
			Map<String,Object> deptMap=new HashMap<String,Object>();
			for(Map<String,Object> map:assignMapList){
				
				assignMap.put(map.get("requirement_no").toString(), map.get("assign_time")==null?null:DateUtil.dateFormat(map.get("assign_time").toString()));
				deptMap.put(map.get("requirement_no").toString()+"#", map.get("name")==null?"-":map.get("name").toString());
			}
			for(PurchaseWarnReport report:rows){
				if(assignMap.containsKey(report.getPurchase_warn_demand())){
					report.setAssignTime(assignMap.get(report.getPurchase_warn_demand()));
				}
				if(deptMap.containsKey(report.getPurchase_warn_demand()+"#")){
					report.setDeptName(deptMap.get(report.getPurchase_warn_demand()+"#").toString());
				}
			}
		}
		
		data.setRows(rows);
		data.setTotal(total);
		} catch (Exception e) {
			logger.error("获取需求部门列表异常",e);
			e.printStackTrace();
		}
		return data;
		
	}

	@Override
	public int count(PurchaseWarnParam param) {
		StringBuilder sb = new StringBuilder(SQL_COUNT);
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

		// skucode过滤
		if (StringUtils.isNotEmpty(param.getSku_code())) {
			if (param.getSku_code().indexOf(",") > 0) {
				sb.append(" and sku_code in (");
				String[] skuCodes = param.getSku_code().split(",");
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
				sb.append(" and sku_code like ?");
				ob.add("%" + param.getSku_code() + "%");
			}
		}
		// skucode过滤
				if (StringUtils.isNotEmpty(param.getSku_code())) {
					if (param.getSku_code().indexOf(",") > 0) {
						sb.append(" and sku_code in (");
						String[] skuCodes = param.getSku_code().split(",");
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
						sb.append(" and sku_code like ?");
						ob.add("%" + param.getSku_code() + "%");
					}
				}
				// 供应商过滤
				if (StringUtils.isNotEmpty(param.getSupplier_id())) {
					if (param.getSupplier_id().indexOf(",") > 0) {
						sb.append(" and supplier_name in (");
						String[] supplierIds = param.getSupplier_id().split(",");
						for (int i = 0; i < supplierIds.length; i++) {
							String supplierId = supplierIds[i];
							sb.append("?");
							if (i < supplierIds.length - 1) {
								sb.append(",");
							}
							ob.add(supplierId);
						}
						sb.append(")");
					} else {
						sb.append(" and supplier_name like ?");
						ob.add("%" + param.getSupplier_id() + "%");
					}
				}
				// 采购订单过滤
				if (StringUtils.isNotEmpty(param.getPurchase_order_id())) {
					if (param.getPurchase_order_id().indexOf(",") > 0) {
						sb.append(" and purchase_no in (");
						String[] supplierIds = param.getPurchase_order_id().split(",");
						for (int i = 0; i < supplierIds.length; i++) {
							String supplierId = supplierIds[i];
							sb.append("?");
							if (i < supplierIds.length - 1) {
								sb.append(",");
							}
							ob.add(supplierId);
						}
						sb.append(")");
					} else {
						sb.append(" and purchase_no like ?");
						ob.add("%" + param.getPurchase_order_id() + "%");
					}
				}

		// 法人主体过滤
		if (StringUtils.isNotEmpty(param.getLegaler_id()) && !"-1".equalsIgnoreCase(param.getLegaler_id())) {
			if (param.getLegaler_id().indexOf(",") > 0) {
				String[] legalers = param.getLegaler_id().split(",");
				sb.append(" and legaler_id in(");
				for (int i = 0; i < legalers.length; i++) {
					sb.append("?");
					if (i < legalers.length - 1) {
						sb.append(",");
					}
					ob.add(legalers[i]);
				}
				sb.append(")");
			} else {
				sb.append(" and legaler_id = ?");
				ob.add(param.getLegaler_id());
			}
		}

		// 预计时间过滤
				if (param.getBefore_from_data() != null) {
					sb.append(" and before_stock_date >= ?");
					ob.add(param.getBefore_from_data());
				}
				if (param.getBefore_from_data() != null) {
					sb.append(" and before_stock_date < ?");
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(param.getBefore_to_data());
					calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
					ob.add(calendar.getTime());
				}
		//采购部门过滤
		
				if (StringUtils.isNotEmpty(param.getDepartment_id()) && !"-1".equalsIgnoreCase(param.getDepartment_id())) {
					if (param.getDepartment_id().indexOf(",") > 0) {
						String[] legalers = param.getDepartment_id().split(",");
						sb.append(" and department_id in(");
						for (int i = 0; i < legalers.length; i++) {
							sb.append("?");
							if (i < legalers.length - 1) {
								sb.append(",");
							}
							ob.add(legalers[i]);
						}
						sb.append(")");
					} else {
						sb.append(" and department_id = ?");
						ob.add(param.getDepartment_id());
					}
				}
				
		return getRecordsTotal(sb.toString(), ob.toArray());
	}
	/*private List<PurchaseWarnReport> getRepleaceSkuName(List<PurchaseWarnReport> list,Set<String> skuCode){
		
		List<String> listSku = new ArrayList<String>(skuCode);
		Map<String,String> set = new HashMap<String, String>();
		for (String string : listSku) {
			AjaxResponse response = HttpUtil.doGet(UrlSkuGet+string);
			try {
				SkuVO skuVO = JSON.parseObject(response.getData()+"", SkuVO.class);
				set.put(skuVO.getCode(), skuVO.getName());
			} catch (Exception e) {
				logger.error("获取sku名称失败");
				e.printStackTrace();
			}
		}
		for (PurchaseWarnReport pw : list) {
			if(set.containsKey(pw.getSku_code())){
				pw.setSku_name(set.get(pw.getSku_code()));
			}
		}
		
		return list;
	}*/
	
}