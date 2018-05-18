package com.aukey.report.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import com.alibaba.fastjson.JSON;
import com.aukey.report.domain.Email;

import com.aukey.report.domain.StarSign;
import com.aukey.report.domain.Stock;
import com.aukey.report.domain.StockModel;
import com.aukey.report.domain.StorageReport;
import com.aukey.report.domain.base.Result;
import com.aukey.report.domain.base.TableData;
import com.aukey.report.mapper.StorageReportMapper;
import com.aukey.report.service.SendEmailService;
import com.aukey.report.service.StorageReportService;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.WareHouseUtil;
import com.aukey.util.CommonUtil;
import com.aukey.util.DateUtil;
import com.aukey.util.ThreadPool;
import com.fasterxml.jackson.core.type.TypeReference;
@Service
public class StorageReportServiceImpl implements StorageReportService{
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private StorageReportMapper storageReportMapper;
	
	@Value("${product.warehouse.url}")
	private String warehouseUrl;
	
	@Value("${cas.userIdInfo.url}")
	private String userIdInfoUrl;

	
	/**
     * 邮件发送人
     */
    @Value("${spring.mail.username}")
    private String from;
	
	@Autowired
	private SendEmailService sendEmailService;
	
	@Autowired
    RedisTemplate<String, String> redisService;
	
	
	
	  public List<StockModel> getStockByType(String[] stockTypes) {
	        List<StockModel> stockList = new ArrayList<>();
	        if (stockTypes.length > 0) {
	            String stocks = redisService.opsForValue().get("stockList");
	            if (CommonUtil.isNotEmpty(stocks)) {
	                List<StockModel> allStockList = JSON.parseObject(stocks, new TypeReference<List<StockModel>>() {
	                }.getType());
	                if (allStockList != null && allStockList.size() > 0) {
	                    for (String stockType : stockTypes) {
	                        for (StockModel stock : allStockList) {
	                            if (CommonUtil.isNotEmpty(stock.getStockType()) && stock.getStockType().equals(stockType)
	                                    && "1".equals(stock.getFuncState())) {
	                                stockList.add(stock);
	                            }
	                        }
	                    }
	                }
	            }
	        }
	        return stockList;
	    }
	
	
	
	
	


	@Override
	public TableData<StorageReport> getStorageReportList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		
		TableData<StorageReport> storageList = new TableData<>();
        String userId = String.valueOf(RoleUtil.getUser().get("userId"));
        try
        {
            List<StorageReport> storages = storageReportMapper.selectList(param);
            if (null != storages && storages.size() > 0)
            {
                List<StarSign> starSignList = storageReportMapper.findAllByUserIdAndUserType(param);
                /**
                 * 获取对应的仓库
                 */
                Map<String, String> stocks = WareHouseUtil.getAllStocksName(warehouseUrl, null);
                String supplierIds = "";
                // 质检员Id
                //List<String> InspectorIds = new ArrayList<String>();
                Set<String> InspectorIdsHash = new HashSet<String>();
                // 入库员Id
                //List<String> createUserIds = new ArrayList<String>();
                Set<String> createUserIdsHash = new HashSet<String>();
                for (StorageReport storage : storages)
                {
                    storage.setWarehouseName(stocks.get(storage.getWarehouseId()));
                    supplierIds += "," + storage.getSupplierId();
                    InspectorIdsHash.add(String.valueOf(storage.getInspector()));
                    createUserIdsHash.add(String.valueOf(storage.getCreateUser()));
                    for (int i = 0; i < starSignList.size(); i++)
                    {
                        if (storage.getStorageNumber().equals(starSignList.get(i).getTypeId())
                            && userId.equals(starSignList.get(i).getUserId().toString()))
                        {
                             storage.setIsStarSign("1");
                        }else{
                        	 storage.setIsStarSign("0");
                        }
                    }
                }
                //质检员list
                List<String> InspectorIds = new ArrayList<String>(InspectorIdsHash);
                //入库员list
                List<String> createUserIds = new ArrayList<String>(createUserIdsHash);
                if (CommonUtil.isNotEmpty(supplierIds) && InspectorIds.size() > 0 && createUserIds.size() > 0)
                {
                     
                    /**
                     * 获取质检员账户对应得名字
                     */
                    Map<String, String> inspectorInfo = selectUserList(InspectorIds);
                    Map<String, String> userInfo = selectUserList(createUserIds);
                    
                    //Map<String, String> inspectorInfo = WareHouseUtil.getUNameById(userIdInfoUrl, InspectorIds);
                    //Map<String, String> userInfo = WareHouseUtil.getUNameById(userIdInfoUrl, createUserIds);
                    
                    for (StorageReport s : storages)
                    {
                        // s.setSupplierName(supplierInfo.get(s.getSupplierId().toString()));
                        s.setInspectorName(inspectorInfo.get(s.getInspector()));
                        s.setCreateUserName(userInfo.get(s.getCreateUser()));
                        s.setWarehouseName(stocks.get(s.getWarehouseId().toString()));
                    }
                }
            }
            Integer total = storageReportMapper.getTotalStorage(param);
            storageList.setRows(storages);
            storageList.setTotal(total);
        }
        catch (Exception e)
        {
            logger.error("----------查询库存列表出现错误------------" + e.getMessage());
            e.printStackTrace();
        }
	    return storageList;
		
	}
	
	
	
	@Override
	public Map<String, String> selectUserList(List<String> list) {
		// TODO Auto-generated method stub
		Map<String, Object> map =new HashMap<String, Object>();
		map.put("list", list);
		List<Map<String, Object>> userList = storageReportMapper.selectUserList(map);
		Map<String, String> map2 =new HashMap<String, String>();
		for(Map<String, Object> m : userList){
			
			map2.put(String.valueOf(m.get("userId")),String.valueOf(m.get("name")) );
		}
		
		return map2;
	}
	
	 private HSSFWorkbook work = new HSSFWorkbook();

	@Override
	public Result exportExcel(List<StorageReport> list, Integer userId, String username, String password,
					HttpServletRequest request, HttpServletResponse response) {
					//username="wangxiaowen@aukeys.com";
				 Result result = new Result();
		             // 大于60000导出以发邮件的方式
		             if (list.size() > 30000) {
		                 Runnable runnable = new Runnable() {
							
							@Override
							public void run() {
								try {
									logger.info("-----------开始生成Excel----------------------");
								     String fileUrl = writeExcel(list, userId, username, password, request);
								     logger.info("-----------Excel生成结束----------------------");
				                     // 发送邮件
				                     logger.info("-----------开始发送邮件----------------------");
				                     Email email = new Email();
				                     email.setFrom(from);
				                     email.setSubject("入库报表导出数据");
				                     email.setFileName(fileUrl);
				                     email.setTo(username);
				                     //email.setFileName("Transfer To SKU Delivery Plan " + DateUtil.dateFormat(new Date(), "yyyy-MM-dd") + ".xls");
				                     // email.setTo("fangmin@aukeys.com");
				                     email.setText("入库报表导出数据。本邮件由系统自动发送，请勿回复！");
				                     sendEmailService.attachments(email, ".xls");
				                     logger.info("-----------邮件发送完毕----------------------");
				                 } catch (Exception e) {
				                     logger.error("-----------邮件发送失败,系统出现错误----------------------");
				                     logger.error("导出转SKU发货计划异常：", e);
				                 }
								
							}
						};
						ThreadPool.submit(runnable);
						result.setData("导出到邮箱，请稍后注意查收");
						result.setSuccess(true);
		             }
		             // 导出到桌面
		             else {
		                 try {
		                	 logger.info("-----------开始生成Excel----------------------");
		    			     String fileUrl = writeExcel(list, userId, username, password, request);
		    			     logger.info("-----------Excel生成结束----------------------");
		                	 	String fileName = URLEncoder
		                                .encode("入库数据_" + DateUtil.dateFormat(new Date(), "yyyyMMdd") + ".xls", "UTF-8");
		                        response.setHeader("Content-disposition", "attachment; filename=" + fileName + "xls");
		                        response.setContentType("application/vnd.ms-excel; charset=utf-8");
		                        response.setContentType("APPLICATION/OCTET-STREAM");
		                        response.setHeader("Content-disposition", "attachment;filename=\"" + fileName + " ");
		                        work.write(response.getOutputStream());
		                        // 定义输出类型
		                        response.getOutputStream().flush();
		                        response.getOutputStream().close();
		                 } catch (IOException e) {
		                	 logger.error("入库导出数据异常：", e);
		                 }
		                 //result.setData("导出成功！");
						 result.setSuccess(true);
		             }
		         
         
		
		        /*
		        if (list != null ) {
		            logger.info("-----------开始生成Excel----------------------");
		            String fileUrl = writeExcel(list, userId, username, password, request);
		           
		                   try{
		                        String fileName = URLEncoder
		                                .encode("入库数据_" + DateUtil.dateFormat(new Date(), "yyyyMMdd") + ".xls", "UTF-8");
		                        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
		                        response.setContentType("application/vnd.ms-excel; charset=utf-8");
		                        response.setContentType("APPLICATION/OCTET-STREAM");
		                        response.setHeader("Content-disposition", "attachment;filename=\"" + fileName + " ");
		                        work.write(response.getOutputStream());
		                        // 定义输出类型
		                        response.getOutputStream().flush();
		                        response.getOutputStream().close();
		                    } catch (IOException e) {
		                        logger.error("入库导出数据异常：", e);
		                    }
			  }*/
		  return result;
		
	}	
	private String writeExcel(List<StorageReport> list, int userId, String account, String password,
		                              HttpServletRequest request) {
		        OutputStream out = null;
		        String fileUrl = new File("").getAbsolutePath() + File.separator + "入库数据"
		                + DateUtil.dateFormat(new Date(), "yyyy-MM-dd") + ".xls";
		        try {
		            out = new FileOutputStream(fileUrl);
		            out = writeExcel(list, out, userId, account, password, request);
		            if (null == out) {
		                return null;
		            }
		        } catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } finally {
		            if (out != null) {
		                try {
		                    out.close();
		                } catch (IOException e) {
		                    e.printStackTrace();
		                }
		            }
		        }
		        return fileUrl;
		    }
			
			 private String textFormat(String value){
		  	   return "N/A".equals(value)?"-":value;
		     }
			 private String formatMoney(String value)
			 {
				 if(CommonUtil.isNotEmpty(value))
				 {
					 BigDecimal bigDecimal = new BigDecimal(value);
					 double value2 = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
					 return value2+"";
					 
				 }else
				 {
					 return "-";
				 }
			 }
			 //设置表头样式
		     private HSSFCellStyle headFont(HSSFWorkbook wb){
		  	   	 HSSFFont headFont = wb.createFont();
		         headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		         headFont.setFontName("宋体");
		         headFont.setFontHeightInPoints((short) 16);//设置字体大小
		       
		         headFont.setFontHeightInPoints((short) 11);
		         HSSFCellStyle headStyle = wb.createCellStyle();
		         headStyle.setFont(headFont);
		         headStyle.setBorderTop((short)1);
		         headStyle.setBorderRight((short)1);
		         headStyle.setBorderBottom((short)1);
		         headStyle.setBorderLeft((short)1);
		         headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		         headStyle.setFillBackgroundColor((short) 13);
		         return headStyle;
		     }
		     
		     
		     //单元格样式
		       private CellStyle bodyFont(HSSFWorkbook wb){
		    	   
		           CellStyle cellStyle = wb.createCellStyle();
		           cellStyle.setWrapText(true);//设置自动换行
		           cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		           cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		           cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		           cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		           cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		           cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		           Font cellFont = wb.createFont();
		           cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		           cellStyle.setFont(cellFont);
		    	   return cellStyle;
		       }
		     
		       
				public OutputStream writeExcel(List<StorageReport> list, OutputStream out,int userId, String account,
				             String password, HttpServletRequest request) {
									try {
										work = new HSSFWorkbook();
										
										//CellStyle bodyCss = bodyFont(work);
										
										SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										
										HSSFSheet sheet = work.createSheet();
										//固定表头
										sheet.createFreezePane( 0, 1, 0, 1 );
										
										int i = 0;
										
										for (int k = 0; k < list.size(); k++) {
												StorageReport plan = list.get(k);
												
												
												if (k % 65535 == 0) {//每65535创建sheet
													
													 if (k != 0) sheet = work.createSheet();//如果数据超过了，则在第二页显示
													//插入标题栏
													i = createHeadRow(work, sheet, 0);
													
													//i = 1;//数据内容从 rowIndex=1开始
											    }
												
												HSSFRow row = sheet.getRow(i);
												if (row == null) {
													row = sheet.createRow(i);
												}
												
												//如果具有三个权限
												int columns = 21;
												if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")
														&& RoleUtil.CanShanPinExportAll("aukey-report_storage_price")
														&& RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")){
													columns = 25;
												}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")
														&& RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
													
													columns = 24;
												}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")
														&& RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")){
													
													columns = 23;
												}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")
														&& RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
													columns = 24;
												}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")){
													columns = 22;
												}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
													columns = 22;
												}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")){
													columns = 22;
												}
												
												for (int j = 0; j < columns; j++) {
													HSSFCell cell = row.createCell(j);
												    //cell.setCellStyle(bodyCss);
													switch (j) {
														    case 0:
														    	//入库单号
														    	if (null != plan.getStorageNumber()) {
														    		 cell.setCellValue(textFormat(plan.getStorageNumber()));
																}else{
																	 cell.setCellValue("");
																}
														        break;
														    case 1:
														    	//质检单号
														    	if (null != plan.getQualityInspectionNumber()) {
														    		cell.setCellValue(textFormat(plan.getQualityInspectionNumber()));
																}else{
																	cell.setCellValue("");
																}
														        break;
														    case 2:
														    	//采购单号
														        cell.setCellValue(textFormat(plan.getPurchaseNumber()));
														        break;
														    case 3:
														    	//SKU
														    	cell.setCellValue(textFormat(plan.getSkuCode()));
														    	break;
														    case 4:
														    	//SKUname
														    	cell.setCellValue(textFormat(plan.getSkuName()));
															    break;
															case 5:
																//质检员
																cell.setCellValue(textFormat(plan.getInspectorName()));
															    break;
															case 6:
																//入库员
																cell.setCellValue(textFormat(plan.getCreateUserName()));
															    break;
															case 7:
																//仓库名称
															    cell.setCellValue(textFormat(plan.getWarehouseName()) );
															    break;
															case 8:
														
																//入库良品数
																cell.setCellValue((plan.getNondefectiveNumber()));
															    break;
															case 9:{
																
																
																//不良品数
																cell.setCellValue((plan.getRejectsNumber()));
																}
															    break;
															case 10:{
															
																	//入库体积
																	if(plan.getVolume()!=null && plan.getVolume()!=0){
																		cell.setCellValue( String .format("%.6f",plan.getVolume()));
																	}else{
																		cell.setCellValue("");
																	}
																}
															    break;
															case 11:
																//入库金额
															        cell.setCellValue(formatMoney(plan.getStockInSum()));
															    break; 
														   case 12:
																//目标仓
															        cell.setCellValue(textFormat(plan.getStockname()));
															    break;    
															case 13:
																//法人主体
															        cell.setCellValue(textFormat(plan.getCorporationName()));
															    break;
															
														    case 14:
														    	//运输方式
														    	 String transport =plan.getTransportTypeName();
														    	 
														    	  
														           cell.setCellValue(transport);
														           
														        break;
														    case 15:
														       //是否退税
																String drawbackType = plan.getIsTax();
																//0不含税
																	
															   cell.setCellValue("0".equals(drawbackType)?"否":"是");
														        break;
														    case 16:
														    	//入库时间
														    	cell.setCellValue(format2.format(plan.getCreateDate()));
														        break;
														    case 17:
														    	//采购员
														            cell.setCellValue(textFormat(plan.getReqUserName()));
														        break;
														    case 18:
														    	//销售人员
														            cell.setCellValue(textFormat(plan.getSaleName()));
														        break;
														    case 19:
														    	//部门名称
														            cell.setCellValue(textFormat(plan.getDeptName()));
														        break;   
														    case 20:
														    	//数据来源
														    	 if("RKB".equals(plan.getStorageNumber().substring(0, 3))){
														    		 cell.setCellValue("内部");
														    		 
														    	 }else{
														    		 cell.setCellValue("外部");
														    	 }
														        break;
														    default:
														        break;
														}
													if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")
															&& RoleUtil.CanShanPinExportAll("aukey-report_storage_price")
															&& RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")){
														if(j == 21){
															cell.setCellValue(plan.getSupplier_name());
														}else if(j == 22){
															cell.setCellValue(plan.getTax_unit_price());
														}else if(j == 23){
															cell.setCellValue(plan.getNo_tax_price());
														}else if(j==24){
															cell.setCellValue(plan.getCurrency());
														}
													}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")
															&& RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
														
														if(j == 21){
															cell.setCellValue(plan.getSupplier_name());
														}else if(j == 22){
															cell.setCellValue(plan.getTax_unit_price());
														}else if(j == 23){
															cell.setCellValue(plan.getNo_tax_price());
														}
													}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")
															&& RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")){
														
														if(j == 21){
															cell.setCellValue(plan.getSupplier_name());
														}else if(j == 22){
															cell.setCellValue(plan.getCurrency());
														}
													}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")
															&& RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
														if(j == 21){
															cell.setCellValue(plan.getTax_unit_price());
														}else if(j == 22){
															cell.setCellValue(plan.getNo_tax_price());
														}else if(j == 23){
															cell.setCellValue(plan.getCurrency());
														}
													}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")){
														if(j == 21){
															cell.setCellValue(plan.getSupplier_name());
														}
													}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
														if(j == 21){
															cell.setCellValue(plan.getTax_unit_price());
														}else if(j == 22){
															cell.setCellValue(plan.getNo_tax_price());
														}
													}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")){
														if(j == 21){
															cell.setCellValue(plan.getCurrency());
														}
													}
													/*if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")
															&& RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
														if(j == 19){
															cell.setCellValue(plan.getSupplier_name());
														}else if(j == 20){
															cell.setCellValue(plan.getTax_unit_price());
														}else if(j == 21){
															cell.setCellValue(plan.getNo_tax_price());
														}else if(j==22){
															cell.setCellValue(plan.getCurrency());
														}
													}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")){
														
														if(j == 19){
															cell.setCellValue(plan.getSupplier_name());
														}
													}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
														
														if(j == 19){
															cell.setCellValue(plan.getTax_unit_price());
														}else if(j == 20){
															cell.setCellValue(plan.getNo_tax_price());
														}
													}*/
													
												}
												
												i += 1;
											 }		
												
												work.write(out);
												work.close();
										} catch (Exception e) {
												logger.error("导出出现异常啦：", e);
										}
									return out;

							
					   }	
					   
			
					   public int createHeadRow( HSSFWorkbook work, HSSFSheet sheet, int currRowNum) {
						   
						   LinkedHashMap<String, String> map = new LinkedHashMap<>();
						   
						    //设置表头样式
					        HSSFCellStyle cs2 =  headFont(work);
					        
					        String[] headerArray =
					                {"入库单号","质检单号", 
					        		        "采购单号", 
					        		        "SKU", 
					        		        "SKU名称" ,
					        		        "质检员", 
					        		        "入库员", 
					        		        "仓库名称", 
					        		        "良品入库数量",
					        		        "不良品入库数量",
					        		        "入库体积",
					        		        "入库金额",
					        		        "目标仓", 
					        		        "法人主体", 
					        		        "运输方式",
					        		        "是否含税",
					        		        "入库时间",
					        		        "采购员",
					        		        "销售人员",
					        		        "部门名称",
					        		       "数据来源"};
					       
					        
					        Integer[] widthArray =
					                {8000, 8000, 8000, 8000, 8000, 8000, 8000,8000, 8000, 8000,8000, 8000, 8000, 8000, 8000
					                		, 8000, 8000, 8000,8000,8000,8000};
					        
					        List<String> list1 =  new ArrayList<String>(Arrays.asList(headerArray)); 
					        List<Integer> list2 = new ArrayList<Integer>(Arrays.asList(widthArray));  
					        
					        /*if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")
									&& RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
					        	
					        	list1.add("供应商");
					        	list1.add("含税单价");
					        	list1.add("不含税单价");
					        	
					        	list2.add(8000);
					        	list2.add(8000);
					        	list2.add(8000);
							}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")){
								list1.add("供应商");
								list2.add(8000);
							}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
								list1.add("含税单价");
					        	list1.add("不含税单价");
					        	list2.add(8000);
					        	list2.add(8000);
							}*/
					        if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")
									&& RoleUtil.CanShanPinExportAll("aukey-report_storage_price")
									&& RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")){
					        	list1.add("供应商");
					        	list1.add("含税单价");
					        	list1.add("不含税单价");
					        	list1.add("币别");
					        	list2.add(8000);
					        	list2.add(8000);
					        	list2.add(8000);
					        	list2.add(8000);
							}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")
									&& RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
								
								list1.add("供应商");
					        	list1.add("含税单价");
					        	list1.add("不含税单价");
					        	list2.add(8000);
					        	list2.add(8000);
					        	list2.add(8000);
							}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")
									&& RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")){
								
								list1.add("供应商");
								list1.add("币别");
								list2.add(8000);
					        	list2.add(8000);
							}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")
									&& RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
								list1.add("含税单价");
					        	list1.add("不含税单价");
					        	list1.add("币别");
					        	list2.add(8000);
					        	list2.add(8000);
					        	list2.add(8000);
							}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_supplier")){
								list1.add("供应商");
								list2.add(8000);
							}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_price")){
								list1.add("含税单价");
					        	list1.add("不含税单价");
					        	list2.add(8000);
					        	list2.add(8000);
							}else if(RoleUtil.CanShanPinExportAll("aukey-report_storage_currency")){
								list1.add("币别");
								list2.add(8000);
							}
					        
					        // 设置列宽
					        HSSFRow headRow = sheet.createRow(currRowNum);
					        HSSFCell headCell = headRow.createCell((short) 2);
					        for (int i = 0; i < list1.size(); i++) {
					                sheet.setColumnWidth((short) i, list2.get(i));
					                headCell = headRow.createCell((short) i);
					                headCell.setCellValue(list1.get(i));
					                headCell.setCellStyle(cs2);
					        }
					        currRowNum += 1;
					        return currRowNum;
					    }






					@Override
					public Integer getTotalStorage(Map<String, Object> param) {
						// TODO Auto-generated method stub
						return this.storageReportMapper.getTotalStorage(param);
					}







					@Override
					public List<Map<String, Object>> selectStorageBytype(Map<String, Object> map) {
						// TODO Auto-generated method stub
						return this.storageReportMapper.selectStorageBytype(map);
					}







					@Override
					public List<Map<String, Object>> selectStorageBytype2(Map<String, Object> map) {
						// TODO Auto-generated method stub
						return storageReportMapper.selectStorageBytype2(map);
					}
				
		

}
