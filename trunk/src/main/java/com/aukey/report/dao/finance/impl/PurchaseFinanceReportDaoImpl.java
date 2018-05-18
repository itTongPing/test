package com.aukey.report.dao.finance.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.aukey.report.base.BaseDao;
import com.aukey.report.dao.finance.PurchaseFinanceReportDao;
import com.aukey.report.domain.base.Result;
import com.aukey.report.dto.PurchaseReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.PageResult;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.InvoiceStatusVO;
import com.aukey.report.vo.PayMethodVO;
import com.aukey.report.vo.PurchaseReportVO;
import com.aukey.report.vo.finance.FinanceCount;
import com.aukey.util.CommonUtil;

@Repository
public class PurchaseFinanceReportDaoImpl extends BaseDao implements PurchaseFinanceReportDao {
	 private Logger logger = Logger.getLogger(getClass());
	 
	 private final int SPTEP_SIZE = 500;//步长

	    private static final String SQL_QUERY_TEST_NEW = "SELECT a.id, a.legaler_name AS legaler, a.supplier_name AS supplier, a.purchase_no, a.purchase_date, a.sku AS sku, a.sku_name AS sku_name, a.purchase_count, a.price_wihtout_tax, a.price_tax, a.purchase_sum, a.purchase_money_type, a.buyer_name AS buyer, a.department_name AS department, a.stock_number, a.stock_date, a.stock_name AS stock_name, a.stock_count, a.stock_sum, a.usage_inventory, a.available_inventory, a.category_name AS category, a.bill_name, a.bill_unit, a.no_bill_count, a.bill_status, a.brand, a.version, CASE a.include_tax WHEN '1' THEN '是' WHEN '0' THEN '否' END AS include_tax, CASE a.transport_type WHEN '0' THEN '空运' WHEN '1' THEN '海运' END AS transport_type,a.pay_type_name,a.dept_id_xq as deptIdxq,a.dept_name_xq as deptNamexq, IF(puro.exchange_rate_copy=0.000000,1.000000,puro.exchange_rate_copy) as exchangRate FROM purchase_report a LEFT JOIN supply_chain.purchase_order puro ON puro.purchase_order_id = a.purchase_no WHERE a.stock not IN (12,13,29,80,141,147)";

	    private static final String SQL_QUERY_PAY_TYPE = "SELECT payment_method_id AS id,method AS name,CASE prepay WHEN 0 THEN 0 ELSE 1 END AS type FROM supplier2.base_payment_method";

	    private static final String SQL_QUERY_INVOICE = "SELECT purchase_order_id,sku,`开票数量` as count FROM view_purchase_invoice";

	    private static final String SQL_QUERY_SUM_NEW  = "SELECT "+
											"SUM(IF(a.include_tax = '0',a.price_wihtout_tax,a.price_tax)*a.stock_count*IF(puro.exchange_rate_copy = 0.000000,1,puro.exchange_rate_copy)) AS moneySum,"+
											"SUM(a.stock_count) AS count "+
										"FROM "+
											"purchase_report a "+
											"LEFT JOIN supply_chain.purchase_order puro ON puro.purchase_order_id = a.purchase_no "+
										"WHERE "+
											"a.stock NOT IN (12, 13, 29, 80, 141, 147)";
	    
	    
	    @Override
	    public TableData<PurchaseReportVO> queryPage(PageParam pageParam, PurchaseReportParam param)
	    {
	        PageResult<PurchaseReportVO> pageResult = null;
	        if(pageParam !=null)
	        {
	        	pageResult = new PageResult<PurchaseReportVO>(pageParam.getPageNum(),
	                    pageParam.getNumPerPage());
	        }
	        StringBuilder sb = new StringBuilder(SQL_QUERY_TEST_NEW);
	        List<Object> ob = new ArrayList<Object>();

	        // 入库时间过滤
	        if (param.getFrom_date() != null)
	        {
	            sb.append(" and a.stock_date >= ?");
	            ob.add(param.getFrom_date());
	        }
	        if (param.getTo_date() != null)
	        {
	            sb.append(" and a.stock_date < ?");
	            Calendar calendar = new GregorianCalendar();
	            calendar.setTime(param.getTo_date());
	            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
	            ob.add(calendar.getTime());
	        }

	        // 采购时间过滤
	        if (param.getFrom_purchase_date() != null)
	        {
	            sb.append(" and a.purchase_date >= ?");
	            ob.add(param.getFrom_purchase_date());
	        }
	        if (param.getTo_purchase_date() != null)
	        {
	            sb.append(" and a.purchase_date < ?");
	            Calendar calendar = new GregorianCalendar();
	            calendar.setTime(param.getTo_purchase_date());
	            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
	            ob.add(calendar.getTime());
	        }

	        // sku过滤
	        if (StringUtils.isNotEmpty(param.getSku()))
	        {
	            if (param.getSku().indexOf(",") > 0)
	            {
	                sb.append(" and a.sku in (");
	                String[] skuCodes = param.getSku().split(",");
	                for (int i = 0; i < skuCodes.length; i++)
	                {
	                    String skuCode = skuCodes[i];
	                    sb.append("?");
	                    if (i < skuCodes.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(skuCode);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.sku like ?");
	                ob.add("%" + param.getSku() + "%");
	            }
	        }

	        // 供应商过滤
	        if (StringUtils.isNotEmpty(param.getSupplier()))
	        {
	            if (param.getSupplier().indexOf(",") > 0)
	            {
	                String[] supplierNames = param.getSupplier().split(",");
	                sb.append(" and a.`supplier_name` in(");
	                for (int i = 0; i < supplierNames.length; i++)
	                {
	                    sb.append("?");
	                    if (i < supplierNames.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(supplierNames[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.`supplier_name` like ?");
	                ob.add("%" + param.getSupplier() + "%");
	            }
	        }

	        // 采购员过滤
	        if (StringUtils.isNotEmpty(param.getBuyer()))
	        {
	            if (param.getBuyer().indexOf(",") > 0)
	            {
	                String[] buyers = param.getBuyer().split(",");
	                sb.append(" and a.buyer_name in(");
	                for (int i = 0; i < buyers.length; i++)
	                {
	                    sb.append("?");
	                    if (i < buyers.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(buyers[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.buyer_name like ?");
	                ob.add("%" + param.getBuyer() + "%");
	            }
	        }

	        // 法人主体过滤
	        if (StringUtils.isNotEmpty(param.getLegaler()) && !"-1".equalsIgnoreCase(param.getLegaler()))
	        {
	            sb.append(" and a.legaler in(").append(param.getLegaler()).append(")");
	        }

	        // 采购单号过滤
	        if (StringUtils.isNotEmpty(param.getPurchase_no()))
	        {
	            if (param.getPurchase_no().indexOf(",") > 0)
	            {
	                String[] purchaseNos = param.getPurchase_no().split(",");
	                sb.append(" and a.purchase_no in(");
	                for (int i = 0; i < purchaseNos.length; i++)
	                {
	                    sb.append("?");
	                    if (i < purchaseNos.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(purchaseNos[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.purchase_no like ?");
	                ob.add("%" + param.getPurchase_no() + "%");

	            }
	        }

	        // 仓库过滤
	        if (StringUtils.isNotEmpty(param.getStock_id()) && !"-1".equalsIgnoreCase(param.getStock_id()))
	        {
	            if (param.getStock_id().indexOf(",") > 0)
	            {
	                String[] stockIds = param.getStock_id().split(",");
	                sb.append(" and a.stock in(");
	                for (int i = 0; i < stockIds.length; i++)
	                {
	                    sb.append("?");
	                    if (i < stockIds.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(stockIds[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.stock = ?");
	                ob.add(param.getStock_id());
	            }
	        }

	        // 是否含税过滤
	        if (StringUtils.isNotEmpty(param.getInclude_tax()) && !"-1".equalsIgnoreCase(param.getInclude_tax()))
	        {
	            sb.append(" and a.include_tax ='").append(param.getInclude_tax()).append("'");
	        }

	        // 采购部门过渡
	        if (StringUtils.isNotEmpty(param.getDepartment()) && !"-1".equalsIgnoreCase(param.getDepartment()))
	        {
	            if (param.getDepartment().indexOf(",") > 0)
	            {
	                String[] dptId = param.getDepartment().split(",");
	                sb.append(" and a.department in(");
	                for (int i = 0; i < dptId.length; i++)
	                {
	                    sb.append("?");
	                    if (i < dptId.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(dptId[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.department =?");
	                ob.add(param.getDepartment());
	            }
	        }
	        //需求部门过滤	
	        if (StringUtils.isNotEmpty(param.getDept_xq()) && !"-1".equalsIgnoreCase(param.getDept_xq()))
	        {
	            if (param.getDept_xq().indexOf(",") > 0)
	            {
	                String[] dptId = param.getDept_xq().split(",");
	                sb.append(" and a.dept_id_xq in(");
	                for (int i = 0; i < dptId.length; i++)
	                {
	                    sb.append("?");
	                    if (i < dptId.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(dptId[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.dept_id_xq =?");
	                ob.add(param.getDept_xq());
	            }
	        }

	        // 支付方式过渡
	        if (StringUtils.isNotEmpty(param.getPayType()) && !"-1".equalsIgnoreCase(param.getPayType()))

	        {
	            if (param.getPayType().indexOf(",") > 0)
	            {

	                String[] payId = param.getPayType().split(",");
	                sb.append(" and a.pay_type in(");
	                for (

	                int i = 0; i < payId.length; i++)
	                {
	                    sb.append("?");
	                    if (i < payId.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(payId[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.pay_type =?");
	                ob.add(param.getPayType());
	            }
	        }
	        // 权限控制
	        if (param.getPurchaseGroupIdList().size() != 0)
	        {
	            sb.append(" and a.department in(");
	            List<Integer> list = param.getPurchaseGroupIdList();
	            for (int i = 0; i < list.size(); i++)
	            {
	                sb.append("?");
	                if (i < list.size() - 1)
	                {
	                    sb.append(",");
	                }
	                ob.add(list.get(i));
	            }
	            sb.append(")");
	        }

	        // 采购员过滤
	        if (param.getPurchaseUser() != null)
	        {
	            sb.append(" and a.buyer ='" + param.getPurchaseUser() + "'");
	        }

	        int total = getRecordsTotal(sb.toString(), ob.toArray());

	        sb.append(" order by a.stock_date desc");
	        if(pageResult!=null)
	        {
	        	sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
	        }

	        final BeanPropertyRowMapper<PurchaseReportVO> rowMapper = new BeanPropertyRowMapper<PurchaseReportVO>(
	            PurchaseReportVO.class);
	        logger.info(sb.toString());

	        List<PurchaseReportVO> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
	        
	        int infoSize = rows.size();//数据大小
			if(infoSize > SPTEP_SIZE){
				int fromIndex = 0;
				while (fromIndex < infoSize) {
					int toIndex = fromIndex + SPTEP_SIZE;
					if(toIndex > infoSize){
						toIndex = infoSize;
					}
					appendOtherInfo(rows.subList(fromIndex, toIndex));
					fromIndex = fromIndex + SPTEP_SIZE;
				}
			} else {
				appendOtherInfo(rows);
			}
	      

	        TableData<PurchaseReportVO> data = new TableData<PurchaseReportVO>();
	        data.setRows(rows);
	        data.setTotal(total);
	        return data;
	    }
	    
	    

	    /**
	     * 根据采购单从采购模块中获取税率
	     * @param rows
	     */
	    private void appendOtherInfo(List<PurchaseReportVO> rows) {
	    	StringBuffer purchaseOrderIds = new StringBuffer();
	    	List<String> purchaseOrderIdList = new ArrayList<String>();
	    	for (PurchaseReportVO purchaseReportVO : rows) {
	    		String purchaseNo = purchaseReportVO.getPurchase_no();
	    		if(!purchaseOrderIdList.contains(purchaseNo)){
	    			purchaseOrderIds.append(",'"+purchaseNo+"'");
	    			purchaseOrderIdList.add(purchaseNo);
	    		}
	    	}
	    	purchaseOrderIdList.clear();
	    	purchaseOrderIdList = null;
	    	if(CommonUtil.isEmpty(purchaseOrderIds.toString())){
	    		return;
	    	}
	    	final BeanPropertyRowMapper<PurchaseReportVO> rowMapper = new BeanPropertyRowMapper<PurchaseReportVO>(PurchaseReportVO.class);
	    	String purchaseTaxRate = "SELECT t.purchase_order_id AS purchase_no, t1.tax_rate "
	    			+ "FROM supply_chain.purchase_order t "
	    			+ "JOIN supply_chain.purchase_demand t1 ON t.purchase_order_id = t1.purchase_order_id "
	    			+ "WHERE t.is_tax = '1' AND t.purchase_order_id IN ("+ purchaseOrderIds.toString().substring(1)+ ") GROUP BY t.purchase_order_id";
	    	List<PurchaseReportVO> purchaseTaxRateInfos = getJdbcTemplate().query(purchaseTaxRate, rowMapper);
	    	
	    	 for (PurchaseReportVO row : rows) {
	    		 String purchaseOrderId = row.getPurchase_no(); 
	    		 row.setTax_rate(null);
	 			for (PurchaseReportVO info : purchaseTaxRateInfos) {
	 				if(purchaseOrderId.equals(info.getPurchase_no()) && info.getTax_rate().doubleValue() > 0.0){
	 					row.setTax_rate(info.getTax_rate());
	 					break;
	 				}
	 			}
	    	 }
		}



		@Override
	    public List<PayMethodVO> queryList()
	    {
	        StringBuilder sb = new StringBuilder(SQL_QUERY_PAY_TYPE);
	        final BeanPropertyRowMapper<PayMethodVO> rowMapper = new BeanPropertyRowMapper<PayMethodVO>(PayMethodVO.class);
	        return getJdbcTemplate().query(sb.toString(), rowMapper);
	    }

	    @Override
	    public int count(PurchaseReportParam param)
	    {
	    	StringBuilder sb = new StringBuilder(SQL_QUERY_TEST_NEW);
	        List<Object> ob = new ArrayList<Object>();

	        // 入库时间过滤
	        if (param.getFrom_date() != null)
	        {
	            sb.append(" and a.stock_date >= ?");
	            ob.add(param.getFrom_date());
	        }
	        if (param.getTo_date() != null)
	        {
	            sb.append(" and a.stock_date < ?");
	            Calendar calendar = new GregorianCalendar();
	            calendar.setTime(param.getTo_date());
	            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
	            ob.add(calendar.getTime());
	        }

	        // 采购时间过滤
	        if (param.getFrom_purchase_date() != null)
	        {
	            sb.append(" and a.purchase_date >= ?");
	            ob.add(param.getFrom_purchase_date());
	        }
	        if (param.getTo_purchase_date() != null)
	        {
	            sb.append(" and a.purchase_date < ?");
	            Calendar calendar = new GregorianCalendar();
	            calendar.setTime(param.getTo_purchase_date());
	            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
	            ob.add(calendar.getTime());
	        }

	        // sku过滤
	        if (StringUtils.isNotEmpty(param.getSku()))
	        {
	            if (param.getSku().indexOf(",") > 0)
	            {
	                sb.append(" and a.sku in (");
	                String[] skuCodes = param.getSku().split(",");
	                for (int i = 0; i < skuCodes.length; i++)
	                {
	                    String skuCode = skuCodes[i];
	                    sb.append("?");
	                    if (i < skuCodes.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(skuCode);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.sku like ?");
	                ob.add("%" + param.getSku() + "%");
	            }
	        }

	        // 供应商过滤
	        if (StringUtils.isNotEmpty(param.getSupplier()))
	        {
	            if (param.getSupplier().indexOf(",") > 0)
	            {
	                String[] supplierNames = param.getSupplier().split(",");
	                sb.append(" and a.`supplier_name` in(");
	                for (int i = 0; i < supplierNames.length; i++)
	                {
	                    sb.append("?");
	                    if (i < supplierNames.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(supplierNames[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.`supplier_name` like ?");
	                ob.add("%" + param.getSupplier() + "%");
	            }
	        }

	        // 采购员过滤
	        if (StringUtils.isNotEmpty(param.getBuyer()))
	        {
	            if (param.getBuyer().indexOf(",") > 0)
	            {
	                String[] buyers = param.getBuyer().split(",");
	                sb.append(" and a.buyer_name in(");
	                for (int i = 0; i < buyers.length; i++)
	                {
	                    sb.append("?");
	                    if (i < buyers.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(buyers[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.buyer_name like ?");
	                ob.add("%" + param.getBuyer() + "%");
	            }
	        }

	        // 法人主体过滤
	        if (StringUtils.isNotEmpty(param.getLegaler()) && !"-1".equalsIgnoreCase(param.getLegaler()))
	        {
	            sb.append(" and a.legaler in(").append(param.getLegaler()).append(")");
	        }

	        // 采购单号过滤
	        if (StringUtils.isNotEmpty(param.getPurchase_no()))
	        {
	            if (param.getPurchase_no().indexOf(",") > 0)
	            {
	                String[] purchaseNos = param.getPurchase_no().split(",");
	                sb.append(" and a.purchase_no in(");
	                for (int i = 0; i < purchaseNos.length; i++)
	                {
	                    sb.append("?");
	                    if (i < purchaseNos.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(purchaseNos[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.purchase_no like ?");
	                ob.add("%" + param.getPurchase_no() + "%");

	            }
	        }

	        // 仓库过滤
	        if (StringUtils.isNotEmpty(param.getStock_id()) && !"-1".equalsIgnoreCase(param.getStock_id()))
	        {
	            if (param.getStock_id().indexOf(",") > 0)
	            {
	                String[] stockIds = param.getStock_id().split(",");
	                sb.append(" and a.stock in(");
	                for (int i = 0; i < stockIds.length; i++)
	                {
	                    sb.append("?");
	                    if (i < stockIds.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(stockIds[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.stock = ?");
	                ob.add(param.getStock_id());
	            }
	        }

	        // 是否含税过滤
	        if (StringUtils.isNotEmpty(param.getInclude_tax()) && !"-1".equalsIgnoreCase(param.getInclude_tax()))
	        {
	            sb.append(" and a.include_tax ='").append(param.getInclude_tax()).append("'");
	        }

	        // 采购部门过渡
	        if (StringUtils.isNotEmpty(param.getDepartment()) && !"-1".equalsIgnoreCase(param.getDepartment()))
	        {
	            if (param.getDepartment().indexOf(",") > 0)
	            {
	                String[] dptId = param.getDepartment().split(",");
	                sb.append(" and a.department in(");
	                for (int i = 0; i < dptId.length; i++)
	                {
	                    sb.append("?");
	                    if (i < dptId.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(dptId[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.department =?");
	                ob.add(param.getDepartment());
	            }
	        }
	        //需求部门过滤	
	        if (StringUtils.isNotEmpty(param.getDept_xq()) && !"-1".equalsIgnoreCase(param.getDept_xq()))
	        {
	            if (param.getDept_xq().indexOf(",") > 0)
	            {
	                String[] dptId = param.getDept_xq().split(",");
	                sb.append(" and a.dept_id_xq in(");
	                for (int i = 0; i < dptId.length; i++)
	                {
	                    sb.append("?");
	                    if (i < dptId.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(dptId[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.dept_id_xq =?");
	                ob.add(param.getDept_xq());
	            }
	        }

	        // 支付方式过渡
	        if (StringUtils.isNotEmpty(param.getPayType()) && !"-1".equalsIgnoreCase(param.getPayType()))

	        {
	            if (param.getPayType().indexOf(",") > 0)
	            {

	                String[] payId = param.getPayType().split(",");
	                sb.append(" and a.pay_type in(");
	                for (

	                int i = 0; i < payId.length; i++)
	                {
	                    sb.append("?");
	                    if (i < payId.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(payId[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.pay_type =?");
	                ob.add(param.getPayType());
	            }
	        }
	        // 权限控制
	        if (param.getPurchaseGroupIdList().size() != 0)
	        {
	            sb.append(" and a.department in(");
	            List<Integer> list = param.getPurchaseGroupIdList();
	            for (int i = 0; i < list.size(); i++)
	            {
	                sb.append("?");
	                if (i < list.size() - 1)
	                {
	                    sb.append(",");
	                }
	                ob.add(list.get(i));
	            }
	            sb.append(")");
	        }

	        // 采购员过滤
	        if (param.getPurchaseUser() != null)
	        {
	            sb.append(" and a.buyer ='" + param.getPurchaseUser() + "'");
	        }
	        return getRecordsTotal(sb.toString(), ob.toArray());
	    }

	    public void sync()
	    {
	        final BeanPropertyRowMapper<InvoiceStatusVO> rowMapper_1 = new BeanPropertyRowMapper<InvoiceStatusVO>(
	            InvoiceStatusVO.class);
	        List<InvoiceStatusVO> rows_1 = getJdbcTemplate().query(SQL_QUERY_INVOICE, new ArrayList<Object>().toArray(),
	            rowMapper_1);
	        for (InvoiceStatusVO invoiceStatusVO : rows_1)
	        {
	            StringBuilder sb_temp = new StringBuilder(SQL_QUERY_TEST_NEW);
	            sb_temp.append(" and a.purchase_no = ? and a.sku = ? order by a.stock_date desc");
	            List<Object> ob_temp = new ArrayList<Object>();
	            ob_temp.add(invoiceStatusVO.getPurchase_order_id());
	            ob_temp.add(invoiceStatusVO.getSku());
	            final BeanPropertyRowMapper<PurchaseReportVO> rowMapper_temp = new BeanPropertyRowMapper<PurchaseReportVO>(
	                PurchaseReportVO.class);
	            List<PurchaseReportVO> rows_temp = getJdbcTemplate().query(sb_temp.toString(), ob_temp.toArray(),
	                rowMapper_temp);
	            // 计算
	            for (PurchaseReportVO vo : rows_temp)
	            {
	                for (InvoiceStatusVO vo_1 : rows_1)
	                {
	                    if (vo_1.getPurchase_order_id().equals(vo.getPurchase_no()) && vo_1.getSku().equals(vo.getSku()))
	                    {
	                        if (vo_1.getCount() <= 0)
	                        {
	                            vo.setNo_bill_count(vo.getStock_count());
	                            vo.setBill_status("未开票");
	                        }
	                        else if (vo_1.getCount() >= vo.getStock_count())
	                        {
	                            vo.setNo_bill_count(0);
	                            vo.setBill_status("已开票");
	                        }
	                        else
	                        {
	                            vo.setNo_bill_count(vo.getStock_count() - vo_1.getCount());
	                            vo.setBill_status("部分开票");
	                        }
	                        vo_1.setCount(vo_1.getCount() - vo.getStock_count());
	                    }
	                }
	            }

	            // 循环去更新
	            for (PurchaseReportVO vo : rows_temp)
	            {
	                String sql_update = "update purchase_report set no_bill_count = ?,bill_status=? where id = ?";
	                List<Object> ob_upadte = new ArrayList<Object>();
	                ob_upadte.add(vo.getNo_bill_count());
	                ob_upadte.add(vo.getBill_status());
	                ob_upadte.add(vo.getId());
	                int result = getJdbcTemplate().update(sql_update, ob_upadte.toArray());
	            }
	        }

	    }

		@Override
		public Result selectCount(PageParam pageParam, PurchaseReportParam param) {
			PageResult<PurchaseReportVO> pageResult = null;
	        if(pageParam !=null)
	        {
	        	pageResult = new PageResult<PurchaseReportVO>(pageParam.getPageNum(),
	                    pageParam.getNumPerPage());
	        }
	        StringBuilder sb = new StringBuilder(SQL_QUERY_SUM_NEW);
	        List<Object> ob = new ArrayList<Object>();

	        // 入库时间过滤
	        if (param.getFrom_date() != null)
	        {
	            sb.append(" and a.stock_date >= ?");
	            ob.add(param.getFrom_date());
	        }
	        if (param.getTo_date() != null)
	        {
	            sb.append(" and a.stock_date < ?");
	            Calendar calendar = new GregorianCalendar();
	            calendar.setTime(param.getTo_date());
	            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
	            ob.add(calendar.getTime());
	        }

	        // 采购时间过滤
	        if (param.getFrom_purchase_date() != null)
	        {
	            sb.append(" and a.purchase_date >= ?");
	            ob.add(param.getFrom_purchase_date());
	        }
	        if (param.getTo_purchase_date() != null)
	        {
	            sb.append(" and a.purchase_date < ?");
	            Calendar calendar = new GregorianCalendar();
	            calendar.setTime(param.getTo_purchase_date());
	            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
	            ob.add(calendar.getTime());
	        }

	        // sku过滤
	        if (StringUtils.isNotEmpty(param.getSku()))
	        {
	            if (param.getSku().indexOf(",") > 0)
	            {
	                sb.append(" and a.sku in (");
	                String[] skuCodes = param.getSku().split(",");
	                for (int i = 0; i < skuCodes.length; i++)
	                {
	                    String skuCode = skuCodes[i];
	                    sb.append("?");
	                    if (i < skuCodes.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(skuCode);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.sku like ?");
	                ob.add("%" + param.getSku() + "%");
	            }
	        }

	        // 供应商过滤
	        if (StringUtils.isNotEmpty(param.getSupplier()))
	        {
	            if (param.getSupplier().indexOf(",") > 0)
	            {
	                String[] supplierNames = param.getSupplier().split(",");
	                sb.append(" and a.`supplier_name` in(");
	                for (int i = 0; i < supplierNames.length; i++)
	                {
	                    sb.append("?");
	                    if (i < supplierNames.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(supplierNames[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.`supplier_name` like ?");
	                ob.add("%" + param.getSupplier() + "%");
	            }
	        }

	        // 采购员过滤
	        if (StringUtils.isNotEmpty(param.getBuyer()))
	        {
	            if (param.getBuyer().indexOf(",") > 0)
	            {
	                String[] buyers = param.getBuyer().split(",");
	                sb.append(" and a.buyer_name in(");
	                for (int i = 0; i < buyers.length; i++)
	                {
	                    sb.append("?");
	                    if (i < buyers.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(buyers[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.buyer_name like ?");
	                ob.add("%" + param.getBuyer() + "%");
	            }
	        }

	        // 法人主体过滤
	        if (StringUtils.isNotEmpty(param.getLegaler()) && !"-1".equalsIgnoreCase(param.getLegaler()))
	        {
	            sb.append(" and a.legaler in(").append(param.getLegaler()).append(")");
	        }

	        // 采购单号过滤
	        if (StringUtils.isNotEmpty(param.getPurchase_no()))
	        {
	            if (param.getPurchase_no().indexOf(",") > 0)
	            {
	                String[] purchaseNos = param.getPurchase_no().split(",");
	                sb.append(" and a.purchase_no in(");
	                for (int i = 0; i < purchaseNos.length; i++)
	                {
	                    sb.append("?");
	                    if (i < purchaseNos.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(purchaseNos[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.purchase_no like ?");
	                ob.add("%" + param.getPurchase_no() + "%");

	            }
	        }

	        // 仓库过滤
	        if (StringUtils.isNotEmpty(param.getStock_id()) && !"-1".equalsIgnoreCase(param.getStock_id()))
	        {
	            if (param.getStock_id().indexOf(",") > 0)
	            {
	                String[] stockIds = param.getStock_id().split(",");
	                sb.append(" and a.stock in(");
	                for (int i = 0; i < stockIds.length; i++)
	                {
	                    sb.append("?");
	                    if (i < stockIds.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(stockIds[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.stock = ?");
	                ob.add(param.getStock_id());
	            }
	        }

	        // 是否含税过滤
	        if (StringUtils.isNotEmpty(param.getInclude_tax()) && !"-1".equalsIgnoreCase(param.getInclude_tax()))
	        {
	            sb.append(" and a.include_tax ='").append(param.getInclude_tax()).append("'");
	        }

	        // 采购部门过渡
	        if (StringUtils.isNotEmpty(param.getDepartment()) && !"-1".equalsIgnoreCase(param.getDepartment()))
	        {
	            if (param.getDepartment().indexOf(",") > 0)
	            {
	                String[] dptId = param.getDepartment().split(",");
	                sb.append(" and a.department in(");
	                for (int i = 0; i < dptId.length; i++)
	                {
	                    sb.append("?");
	                    if (i < dptId.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(dptId[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.department =?");
	                ob.add(param.getDepartment());
	            }
	        }
	        //需求部门过滤	
	        if (StringUtils.isNotEmpty(param.getDept_xq()) && !"-1".equalsIgnoreCase(param.getDept_xq()))
	        {
	            if (param.getDept_xq().indexOf(",") > 0)
	            {
	                String[] dptId = param.getDept_xq().split(",");
	                sb.append(" and a.dept_id_xq in(");
	                for (int i = 0; i < dptId.length; i++)
	                {
	                    sb.append("?");
	                    if (i < dptId.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(dptId[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.dept_id_xq =?");
	                ob.add(param.getDept_xq());
	            }
	        }

	        // 支付方式过渡
	        if (StringUtils.isNotEmpty(param.getPayType()) && !"-1".equalsIgnoreCase(param.getPayType()))

	        {
	            if (param.getPayType().indexOf(",") > 0)
	            {

	                String[] payId = param.getPayType().split(",");
	                sb.append(" and a.pay_type in(");
	                for (

	                int i = 0; i < payId.length; i++)
	                {
	                    sb.append("?");
	                    if (i < payId.length - 1)
	                    {
	                        sb.append(",");
	                    }
	                    ob.add(payId[i]);
	                }
	                sb.append(")");
	            }
	            else
	            {
	                sb.append(" and a.pay_type =?");
	                ob.add(param.getPayType());
	            }
	        }
	        // 权限控制
	        if (param.getPurchaseGroupIdList().size() != 0)
	        {
	            sb.append(" and a.department in(");
	            List<Integer> list = param.getPurchaseGroupIdList();
	            for (int i = 0; i < list.size(); i++)
	            {
	                sb.append("?");
	                if (i < list.size() - 1)
	                {
	                    sb.append(",");
	                }
	                ob.add(list.get(i));
	            }
	            sb.append(")");
	        }

	        // 采购员过滤
	        if (param.getPurchaseUser() != null)
	        {
	            sb.append(" and a.buyer ='" + param.getPurchaseUser() + "'");
	        }


	        if(pageResult!=null)
	        {
	        	sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
	        }

	        final BeanPropertyRowMapper<FinanceCount> rowMapper = new BeanPropertyRowMapper<FinanceCount>(
	        		FinanceCount.class);
	        logger.info(sb.toString());

	        List<FinanceCount> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
	        logger.info(rows.get(0).getCount()+" "+rows.get(0).getMoneySum());
	        
	        Map<String,Object> map = new HashMap<String,Object>();
	        map.put("count", rows.get(0).getCount());
	        map.put("moneySum", rows.get(0).getMoneySum());
	        
	        Result result = new Result();
	        result.setData(map);
	        
			return result;
		}
	
}
