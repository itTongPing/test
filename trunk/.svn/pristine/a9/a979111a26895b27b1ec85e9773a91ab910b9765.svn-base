package com.aukey.report.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.aukey.domain.GroupInfo;
import com.aukey.domain.UserInfo;
import com.aukey.report.dto.PayOrderReportParam;
import com.aukey.report.mapper.PayOrderMapper;
import com.aukey.report.service.ISupplierService;
import com.aukey.report.service.PayOrderReportService;
import com.aukey.report.utils.CommonUtil;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.PayOrderReportVo;
import com.aukey.util.CasUtil;
import com.aukey.util.ThreadPool;

@Service
public class PayOrderReportServiceImpl implements PayOrderReportService {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private PayOrderMapper payOrderMapper;
    /**
     * 供应商接口
     */
    @Autowired
    private ISupplierService supplierService;
    
    /**
     * cas uri
     */
    @Value("${cas.uri}")
    private String casUri;
	
	@Override
	public TableData<PayOrderReportVo> listPage(PageParam pageParam, PayOrderReportParam param) {
		
		TableData<PayOrderReportVo> data = new TableData<>();

		try {
			Map<String, Object> params = generateParams(pageParam, param);
			if(param == null){
				return data;
			}
						
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Future<List<PayOrderReportVo>> future = ThreadPool.execute(() -> {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				List<PayOrderReportVo> reportList = payOrderMapper.queryPayOrderReportInfo(params);   
				if(CommonUtil.isEmpty(reportList)){
					reportList = new ArrayList<PayOrderReportVo>();
					return reportList;
				}
				
				geneateAttribute(reportList);
				
				getInStoryTotalMoneyByPurchaseOrderId(reportList);   		
				
				return reportList;
			});
			
			Future<Long> future1 = ThreadPool.execute(new Callable<Long>() {
				@Override
				public Long call() throws Exception {
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
					boolean purchaseSearch = false;
					if (params.get("purchaseSearch") != null) {
						purchaseSearch = true;
					}
					Long count  =  0L;	 
					// 存在采购查询条件	
					if (purchaseSearch) {
						count = payOrderMapper.count(params);			
					} else {
						count = payOrderMapper.simpleSelectCount(params);
					}
					
					return count;
				}
			});
			data.setRows(future.get());
			data.setTotal(future1.get());
		} catch (Exception e) {
			logger.error("付款执行情况报表失败", e);
		} 
		
		
		return data;
	}

	private void geneateAttribute(List<PayOrderReportVo> reportList) {
		List<Integer> supplierIds = new ArrayList<>();
		List<Integer> corporationIds = new ArrayList<>();
		List<Integer> groupIds = new ArrayList<>();
		List<Integer> userIds = new ArrayList<>();
		for (PayOrderReportVo info : reportList) {
			supplierIds.add(info.getSupplierId());
			corporationIds.add(info.getCorporationId());
			groupIds.add(info.getRequestDeptId());
			userIds.add(info.getRequester());
			userIds.add(info.getBuyer());
		}
		
		Map<Integer, String> suppliers = supplierService.querySupplierListBysupplierIds(supplierIds);
		Map<Integer, String> corporations = supplierService.queryCorporationNameByCorporationIds(corporationIds);
		List<GroupInfo> groups = CasUtil.selectGroupNameByGroupIds(casUri, groupIds.toArray(new Integer[groupIds.size()]));
		List<UserInfo> users = CasUtil.selectUserInfoByUserIds(casUri, userIds.toArray(new Integer[userIds.size()]));
		for (PayOrderReportVo info : reportList) {
			String supplierName = suppliers.get(info.getSupplierId());
			if (supplierName != null){
				info.setSupplierName(supplierName != null ? supplierName : "-");
			}
			String corporationName = corporations.get(info.getCorporationId());
			if (corporationName != null) {
				info.setCorporationName(corporationName != null ? corporationName : "-");
			}
			for (GroupInfo group : groups) {
				if (group != null && group.getGroupId().equals(info.getRequestDeptId())) {
					info.setRequestDeptName(CommonUtil.isNotEmpty(group.getGroupName()) ? group.getGroupName() : "-");
					break;
				}
			}
			for (UserInfo user : users) {
				if (user != null && user.getUserId().equals(info.getRequester())) {
					info.setRequesterName(user.getName());
				}
				if (user != null && user.getUserId().equals(info.getBuyer())) {
					info.setBuyerName(user.getName());
				}
			}
			
//			if("1".equals(info.getPayStatus())){
			
			info.setSumPayedMoney(info.getSumReceiMoney());	
			if(info.getSumTotalMoney()!=null && info.getSumTotalMoney() != 0){
				info.setRequestRatio(info.getSumReceiMoney()/info.getSumTotalMoney());	
			}
				
//			} else {
//				info.setSumPayedMoney(0.00);
//				info.setRequestRatio(0.00);
//			}
		}
	}

	@Override
	public Map<String, Object> generateParams(PageParam pageParam, PayOrderReportParam param) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("page", (pageParam.getPageNum()-1)*pageParam.getNumPerPage());
		params.put("limit", pageParam.getNumPerPage());
		
		if(CommonUtil.isNotEmpty(param.getBillBeginDate())){
			params.put("billBeginDate",param.getBillBeginDate() + " 00:00:00");			
		}
		if(CommonUtil.isNotEmpty(param.getBillEndDate())){
			params.put("billEndDate",param.getBillEndDate() + " 23:59:59");			
		}
		if(CommonUtil.isNotEmpty(param.getRequestOrderId())){
			String requestOrderId = param.getRequestOrderId();
			params.put("requestOrderId", putValidateValue(requestOrderId));
        }
		
		//将供应商名称转换成id
		if(CommonUtil.isNotEmpty(param.getSupplierName())){
			String supplierName = param.getSupplierName();
			if (CommonUtil.isNotEmpty(supplierName)) {
				List<Integer> supplierIds = supplierService.getSerachSupplierID(supplierName);
				if (CommonUtil.isNotEmpty(supplierIds)) {
					params.put("supplierId", supplierIds);
				}
				// 供应商不存在，直接返回空
				if (CommonUtil.isEmpty(supplierIds)) {
					return null;
				}
			}
		}
		
		if(CommonUtil.isNotEmpty(param.getCorporationName())){
			String corporationId = param.getCorporationName();
			params.put("corporationId",putValidateValue(corporationId));
		}
		
		if(CommonUtil.isNotEmpty(param.getPurchaseOrderId())){
			String purchaseOrderId = param.getPurchaseOrderId();
			params.put("purchaseOrderId",putValidateValue(purchaseOrderId));
		}
		
		if(CommonUtil.isNotEmpty(param.getPurchaseBeginDate())){
			params.put("purchaseBeginDate",param.getPurchaseBeginDate() + " 00:00:00");
		}
		if(CommonUtil.isNotEmpty(param.getPurchaseEndDate())){
			params.put("purchaseEndDate",param.getPurchaseEndDate() + " 23:59:59");
		}
		
		if(CommonUtil.isNotEmpty(param.getPurchaseDepartment())){
			String purchaseDepartment = param.getPurchaseDepartment();
			params.put("purchaseDepartment",putValidateValue(purchaseDepartment));
		}
		
		if(CommonUtil.isNotEmpty(param.getPayMethod())){
			String payMethod = param.getPayMethod();
			params.put("payMethod",putValidateValue(payMethod));
		}
		
		if(CommonUtil.isNotEmpty(param.getPayStatus())){
			String payStatus = param.getPayStatus();
			params.put("payStatus",putValidateValue(payStatus));
		}
		
		boolean purchaseSearch = false;
		if (params.get("purchaseOrderId") != null
				|| params.get("purchaseBeginDate") != null
				|| params.get("purchaseEndDate") != null
				|| params.get("payMethod") != null
				|| params.get("payStatus") != null) {
			purchaseSearch = true;
		}
		
		// 存在采购查询条件
		if(purchaseSearch){
			params.put("purchaseSearch", purchaseSearch);
		} 
		
		return params;
	}

	private void getInStoryTotalMoneyByPurchaseOrderId(List<PayOrderReportVo> reportList) {
		try {
			
			for (PayOrderReportVo info : reportList) {
				String purchaseOrderIdStr = info.getPurchaseOrderId();
				Double sumOfMoney = 0.0;
				if(CommonUtil.isNotEmpty(purchaseOrderIdStr)){
					String[] orderIds = purchaseOrderIdStr.split(",");
					for (String orderId : orderIds) {
						Map<String, Object> purchaseOrderIdToTotalStoryMoney = 
								payOrderMapper.getInStoryTotalMoneyByPurchaseOrderIds(orderId);						
						Double totalMoney = 0.0;
						try {
							totalMoney = Double.parseDouble(purchaseOrderIdToTotalStoryMoney.get("totalMoney").toString());
						} catch (Exception e) {}
						sumOfMoney += totalMoney; 
					}					
				}
				info.setSumInvoiceMoney(sumOfMoney);
			}			
		} catch (Exception e) {
			logger.error("获取入库总金额失败，", e);
		}
	}
	
    /**
     * 将前端json对象数组，转换成List
     * @param param json数组
     * @return
     */
	public static List<String> putValidateValue(String param) {
		List<String> statuses = new ArrayList<String>();

		String[] statusList = param.split(",");
		for (Object status : statusList) {
			if (status instanceof String && CommonUtil.isNotEmpty(status)) {
				statuses.add(status + "");
			}
		}
		if (CommonUtil.isNotEmpty(statuses)) {
			return statuses;
		}

		return null;
	}

	@Override
	public List<Map<String, Object>> getAllNewSupplierPayment() {
		
		return payOrderMapper.getAllNewSupplierPayment();
	}

	@Override
	public List<Map<String, Object>> getAllCorporationList() {
		return payOrderMapper.getAllCorporationList();
	}

	@Override
	public List<Integer> getAllDeptId() {
		return payOrderMapper.getAllDeptId();
	}

	@Override
	public Long count(Map<String, Object> paramsMap) {

		boolean purchaseSearch = false;
		if (paramsMap.get("purchaseOrderId") != null
				|| paramsMap.get("purchaseBeginDate") != null
				|| paramsMap.get("purchaseEndDate") != null
				|| paramsMap.get("payMethod") != null) {
			purchaseSearch = true;
		}
		
		Long count  =  payOrderMapper.count(paramsMap);
		// 存在采购查询条件	
		if (purchaseSearch) {
			count = payOrderMapper.count(paramsMap);			
		} else {
			count = payOrderMapper.simpleSelectCount(paramsMap);
		}		
		return count;
	}

	@Override
	public List<PayOrderReportVo> bigDataSplitGeneateAttribute(Map<String, Object> paramsMap, boolean isSendEmail) {
	    final int SIZE = 500;
	    List<PayOrderReportVo> reportList = payOrderMapper.queryPayOrderReportInfo(paramsMap);   
	    if(CommonUtil.isEmpty(reportList)){
	    	return new ArrayList<PayOrderReportVo>();
	    }
	    int orderRow = reportList.size();
	    int count = 1;
	    if (orderRow % SIZE == 0)  {
	    	count = orderRow / SIZE;
	    }  else {
	    	count = orderRow / SIZE + 1;
	    }
	    
	    if(isSendEmail){
	    	for (int i = 0; i < count; i++) {
	    		int fromIndex = i * SIZE;
	    		int toIndex = fromIndex + SIZE;
	    		if (toIndex > orderRow) {
	    			toIndex = orderRow;
	    		}
	    		
	    		geneateAttribute(reportList.subList(fromIndex, toIndex));
	    		
	    		getInStoryTotalMoneyByPurchaseOrderId(reportList.subList(fromIndex, toIndex));
	    		logger.info("当前初始化完成用户账号信息共：["+ orderRow +"]，当前位置为：[" + fromIndex + "," + toIndex + "]");
	    	}	    	
	    } else {
	    	useThreadPoolLoadDate(reportList);
	    }
	    
	    return reportList;  
	}
	
	private void useThreadPoolLoadDate(final List<PayOrderReportVo> reportList){
		final int SIZE = 500;
	    int orderRow = reportList.size();
	    int count = 1;
	    if (orderRow % SIZE == 0)  {
	    	count = orderRow / SIZE;
	    }  else {
	    	count = orderRow / SIZE + 1;
	    }

	    CountDownLatch endLatch = new CountDownLatch(count);
	    ExecutorService poolExecutor = Executors.newFixedThreadPool(count);
	    
		for (int i = 0; i < count; i++) {
    		int fromIndex = i * SIZE;
    		int toIndex = fromIndex + SIZE;
    		if (toIndex > orderRow) {
    			toIndex = orderRow;
    		}
    		
    		geneateAttribute(reportList.subList(fromIndex, toIndex));
    		
    		final int endIndex = toIndex;
    		Runnable runnable = new Runnable(){
    			public void run() {
    				getInStoryTotalMoneyByPurchaseOrderId(reportList.subList(fromIndex, endIndex));	
    				endLatch.countDown();
    			}
    		}; 

    		try {
    			poolExecutor.submit(runnable);
    		} catch (Exception e) {
    			logger.error("付款执行报表获取退款金额提交线程池失败(step2)：" , e);
    		}
    		
		}
		
        try {
			endLatch.await();
			poolExecutor.shutdown();
		} catch (InterruptedException e) {
			logger.error("付款执行报表获取退款金额线程同步失败：" , e);
		}  
	}

}
