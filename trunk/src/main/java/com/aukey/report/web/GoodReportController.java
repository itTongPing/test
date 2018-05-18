package com.aukey.report.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.aukey.report.base.BaseController;
import com.aukey.report.domain.Email;
import com.aukey.report.domain.QcWarehouseInfo;
import com.aukey.report.dto.DeclareReportParam;
import com.aukey.report.dto.GoodReportParam;
import com.aukey.report.service.GoodReportService;
import com.aukey.report.service.SendEmailService;
import com.aukey.report.service.StorageReportService;
import com.aukey.report.utils.GoodsReceiveDetailedExcel;
import com.aukey.report.utils.PurchaseWarnExcel;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.WareHouseUtil;
import com.aukey.report.utils.export.DataField;
import com.aukey.report.utils.export.DataPage;
import com.aukey.report.utils.export.ExportDataSource;
import com.aukey.report.utils.export.excel.ExcelDataExportor;
import com.aukey.report.utils.export.excel.MODE;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.GoodReportVO;
import com.aukey.report.vo.InventoryResult;
import com.aukey.report.vo.InventoryResult.InventoryVO;
import com.aukey.report.vo.LegalerResult;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CommonUtil;
import com.aukey.util.DateUtil;
import com.aukey.util.HttpUtil;
import com.aukey.util.ThreadPool;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 商品收发明细报表
 * 
 * @author xiehz
 *
 */
@RequestMapping("/report/good")
@Controller
public class GoodReportController extends BaseController {

	private Logger logger = Logger.getLogger(getClass());

	@Value("${inventory.api.url}")
	private String inventory_name_list_url;

	@Value("${legaler.api.url}")
	private String legaler_list_url;
	
	@Value("${product.warehouse.url}")
	private String warehouseUrl;
	
	@Value("${spring.mail.username}")
    private String from;

	@Autowired
	private GoodReportService goodReportService;
	
	@Autowired
	private StorageReportService storageReportService;
	private static int MAX_ROW = 60000;
	@Resource
    private SendEmailService sendEmailService;
	@RequestMapping("/index")
	public String index(ModelMap modelMap) {

		// 获取法人主体列表
		Map<String, Object> legaler_resultMap = null;
		AjaxResponse legaler_result = HttpUtil.doGet(legaler_list_url);
		try {
		if (legaler_result.getData() != null) {
			LegalerResult LegalerResult = JSON.parseObject(legaler_result.getData().toString(), LegalerResult.class);
			String str = LegalerResult.getData();
			ObjectMapper objMapper = new ObjectMapper();
			legaler_resultMap = objMapper.readValue(str, HashMap.class);
			
		}
		} catch (IOException e) {
			 logger.error("ObjectMapper对象转换不成功");
		}
		modelMap.addAttribute("legalers", legaler_resultMap);

		// 获取仓库名称列表
//		List<InventoryVO> inventoryV_list = new ArrayList<InventoryVO>();
//		AjaxResponse inventory_name_result = HttpUtil.doGet(inventory_name_list_url);
//		if (inventory_name_result.getData() != null) {
//			InventoryResult inventoryResult = JSON.parseObject(inventory_name_result.getData().toString(), InventoryResult.class);
//			List<InventoryVO> inventoryV_list_all = inventoryResult.getList();
//			for (InventoryVO vo : inventoryV_list_all) {
//				// if (whs.contains(vo.getStock_id())) {
//				// inventoryV_list.add(vo);
//				// }
//				inventoryV_list.add(vo);
//			}
//		}
//		modelMap.addAttribute("inventorys", inventoryV_list);
		
		
		  //权限控制
	   List<Map<String, Object>> groups = RoleUtil.getUserGroup();
       boolean financeUser = false;
	   for (Map<String, Object> loginGroup : groups)
       {
			 String calssNameCode = loginGroup.get("cateogryCode").toString(); // 角色分类
			 
			 // 如果是财务相关类-应付、关税和出纳
           if (calssNameCode.equals("TARIFF") || calssNameCode.equals("CUSTOMS")
               || calssNameCode.equals("CASHIER"))
           {
               financeUser = true;
           }
       }
	
	   // 如果是财务相关类-应付、关税和出纳
	   if(financeUser){
		 //获取中转仓仓库
		   Map<String, Object> map = new HashMap<String, Object>();
		   List<String> list = new ArrayList<String>();
		   list.add("3");
		   //list.add("2");
		   list.add("11");
		   map.put("stock_types", list);
           modelMap.addAttribute("inventorys", this.storageReportService.selectStorageBytype2(map));
	   }else{
		    String warehouseIds = "";
	        for (Integer warehouseId : RoleUtil.getUserWarehouse())
	        {
	            warehouseIds += warehouseId + ",";
	        }
	        WareHouseUtil w = new WareHouseUtil();
	        if (!warehouseIds.equals(""))
	        {	
	        	//获取不良品仓
        	  /*  Map<String, Object> map = new HashMap<String, Object>();
		        List<String> list = new ArrayList<String>();
		        list.add("2");
		        map.put("stock_types", list);
		        List<Map<String,Object>> storageBytype = this.storageReportService.selectStorageBytype2(map);
		        List<QcWarehouseInfo> list2 = new ArrayList<QcWarehouseInfo>();
		        for (Map<String, Object> map2 : storageBytype) {
					QcWarehouseInfo warehouseInfo = new QcWarehouseInfo();
					warehouseInfo.setStock_id(Integer.valueOf(map2.get("stock_id").toString()));
					warehouseInfo.setWarehouse_name(map2.get("warehouse_name").toString());
					list2.add(warehouseInfo);
				}*/
	        	//获取中转仓仓库
		        List<QcWarehouseInfo> warehouse = w.getWarehouse("3",warehouseIds, warehouseUrl);
		        //获取海外中转仓
		        List<QcWarehouseInfo> list3 = w.getWarehouse("11",warehouseIds, warehouseUrl);
		        //warehouse.addAll(list2);
		        warehouse.addAll(list3);
		        
	            modelMap.addAttribute("inventorys",warehouse);
	        }
		   
	   }
        
		return "goods_send_recieve_detail";
	}

	@RequestMapping("/search")
	@ResponseBody
	public TableData<GoodReportVO> search(GoodReportParam param, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "limit", defaultValue = "10") int limit) {
		PageParam pageParam = new PageParam(pageNumber, limit);
		boolean flag = param.isFlag();
		Date from_date = param.getFrom_date();
		Date to_date = param.getTo_date();
		String warehouse = param.getWarehouse();
		/*if(!flag){
			param = new GoodReportParam();
			param.setFrom_date(from_date);
			param.setTo_date(to_date);
			param.setLegaler("-1");
			param.setWarehouse(warehouse);
		}
		*/
		TableData<GoodReportVO> result = goodReportService.listPage(pageParam, param);
		return result;
	}
	
	@RequestMapping("/preExport")
	@ResponseBody
	public AjaxResponse preExport(GoodReportParam param, HttpServletRequest request, HttpServletResponse response) {
		int count = goodReportService.count(param);
		if (count > exportMaxNumber) {
			return new AjaxResponse().failure("导出的数据量太大，请选择过滤条件");
		}
		return new AjaxResponse();
	}

	@RequestMapping("/export")
	public String export(GoodReportParam param, HttpServletRequest request, HttpServletResponse response) {
		PageParam pageParams = new PageParam(1, exportMaxNumber);
		String test = from;
		TableData<GoodReportVO> pageResult = goodReportService.listPage(pageParams, param);
		long total = pageResult.getTotal();
		//List<GoodReportVO> list = pageResult.getRows();
		if(total>MAX_ROW){
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					 logger.info("-----------开始生成Excel----------------------");
					 SecurityContextHolder.getContext().setAuthentication(authentication);
					 List<GoodReportVO> list = pageResult.getRows();
					 GoodsReceiveDetailedExcel payExcel = new GoodsReceiveDetailedExcel();
 	            	HSSFWorkbook workbook = new HSSFWorkbook();
 	            	HSSFSheet sheet = workbook.createSheet("商品收发明细报表");//创建一个工作表
 	            	//sheet.autoSizeColumn(1, true);
 	            	OutputStream out;
 	            	List<File> fileUrls = new ArrayList<>();
 	            	
                     int page = 1;
                     int fromIndex = 0;
                     int toIndex = MAX_ROW;
                     String fileUrl = new File("").getAbsolutePath() + File.separator + "商品收发明细报表"
                         + DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + "[" + fromIndex + "," + toIndex + "].xls";
                     fileUrls.add(new File(fileUrl));                        
                     payExcel.setSheetTitle(workbook, sheet);
                     try {
						out = new FileOutputStream(fileUrl);
						while (fromIndex < total){
							if (fromIndex % MAX_ROW == 0 && fromIndex != 0){
								toIndex = fromIndex + MAX_ROW;
								if (toIndex > total) {
									toIndex = Integer.parseInt(total+"");
								}
								fileUrl = new File("").getAbsolutePath() + File.separator + "商品收发明细报表"
										+ DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + "[" + fromIndex + "-" + toIndex
										+ "].xls";
								out = new FileOutputStream(fileUrl);
								fileUrls.add(new File(fileUrl));
								
								workbook = new HSSFWorkbook();
								sheet = workbook.createSheet("商品收发明细报表");
								payExcel.setSheetTitle(workbook, sheet);
								page++;
							}
							payExcel.appendSheetRow(workbook, sheet, 1, list.subList(fromIndex, toIndex-1));
							logger.info("当前为第[" + page + "]张，位置为：[" + fromIndex + "," + toIndex + "]");
							
							workbook.write(out);
							workbook.close();
							if (out != null) {
								try {
									out.flush();
									out.close();
									out = null;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							fromIndex = toIndex;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                     logger.info("-----------Excel生成结束----------------------");
                     try {
						logger.info("-----------开始压缩----------------------");
						    String userAccount = RoleUtil.getUserAccount();
						    //String userAccount = "wangxiaowen@aukeys.com";
						    String zipPath = new File("").getAbsolutePath() + File.separator + "商品收发明细报表"
						        + DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + ".zip";
						    zipFile(zipPath, fileUrls);
						    logger.info("-----------压缩结束----------------------");
						    Thread.sleep(500);

		                    for (File file : fileUrls)
		                    {
		                        file.delete();
		                    }

		                    // 发送邮件
		                    logger.info("-----------开始发送邮件----------------------");
		                    Email email = new Email();
		                    email.setFrom(from);
		                    email.setSubject("商品收发明细报表");
		                    email.setFileName(zipPath);
		                    System.out.println(userAccount);
		                    email.setTo(userAccount);
		                    email.setText("商品收发明细报表数据。本邮件由系统自动发送，请勿回复！");

		                    sendEmailService.attachments(email, "zip");
		                    logger.info("-----------邮件发送完毕----------------------");

		                    Thread.sleep(500);
		                    new File(zipPath).delete();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("-----------邮件发送失败,系统出现错误----------------------");
						e.printStackTrace();
					}

                     
				}
			};
			 ThreadPool.submit(runnable);
	    	    try
             {
                 String msg = URLEncoder.encode("导出成功,导出文件大于6万的会发送至邮箱，请稍等查看！", "UTF-8");
               
             }
             catch (UnsupportedEncodingException e)
             {
                 e.printStackTrace();
             }
	    	    ModelAndView mv = new ModelAndView("index");
	    		
	    		return "redirect:index"; 
		}else{
			try {
				
				if("-1".equals(param.getWarehouse())){
					
					String warehouseIds = "";
			        for (Integer warehouseId : RoleUtil.getUserWarehouse())
			        {
			            warehouseIds += warehouseId + ",";
			        }
			        param.setWarehouse(warehouseIds);
				}
				
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				final String userAgent = request.getHeader("USER-AGENT"); 
				 String fileName = "商品收发明细报表_" + DateUtil.dateFormat(new Date(), "yyyy-MM-dd") + ".xls";
	             if(StringUtils.contains(userAgent, "MSIE")){//IE浏览器  
	            	 fileName = URLEncoder.encode(fileName,"UTF8");  
	             }else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器  
	            	 fileName = new String(fileName.getBytes(), "ISO8859-1");  
	             }else{  
	            	 fileName = URLEncoder.encode(fileName,"UTF8");//其他浏览器  
	             }  
				response.setHeader("Content-disposition", "attachment; filename=" + fileName);
				OutputStream os = response.getOutputStream();
				//如果具有两个权限
				int columns = 14;
				if(RoleUtil.CanShanPinExportAll("report_reportgood_money_view")
						&& RoleUtil.CanShanPinExportAll("report_reportgood_intercourse_view")){
					columns = 17;
				}else if(RoleUtil.CanShanPinExportAll("report_reportgood_money_view")){
					
					columns = 16;
				}else if(RoleUtil.CanShanPinExportAll("report_reportgood_intercourse_view")){
					
					columns = 15;
				}
				
				DataField[] dataFields = new DataField[columns];

				dataFields[0] = new DataField("序号", "no");
				dataFields[1] = new DataField("SKU", "sku");
				dataFields[2] = new DataField("SKU名称", "sku_name");
				dataFields[3] = new DataField("单据编号", "document_number");
				dataFields[4] = new DataField("单据日期", "document_date");
				dataFields[5] = new DataField("业务类型", "business_type");
				dataFields[6] = new DataField("是否含税", "is_tax");
				dataFields[7] = new DataField("法人主体", "legaler_name");
				dataFields[8] = new DataField("仓库", "warehouse_name");
				dataFields[9] = new DataField("数量", "quantity");
				dataFields[10] = new DataField("币别", "currency");
				dataFields[11] = new DataField("制单人", "creator_name");
				dataFields[12] = new DataField("运输方式", "transport_type");
				dataFields[13] = new DataField("需求部门", "department_name");
				if(RoleUtil.CanShanPinExportAll("report_reportgood_money_view")
						&& RoleUtil.CanShanPinExportAll("report_reportgood_intercourse_view")){
					dataFields[14] = new DataField("金额", "cost");
					dataFields[15] = new DataField("单价", "price");
					dataFields[16] = new DataField("来往单位", "intercourse_unit_name");
				}else if(RoleUtil.CanShanPinExportAll("report_reportgood_money_view")){
					
					dataFields[14] = new DataField("金额", "cost");
					dataFields[15] = new DataField("单价", "price");
					
				}else if(RoleUtil.CanShanPinExportAll("report_reportgood_intercourse_view")){
					dataFields[14] = new DataField("来往单位", "intercourse_unit_name");
				}
				DataPage pageParam = new DataPage(60000);
				new ExcelDataExportor<Object>(pageParam, dataFields, new ExportDataSource<Object>() {
					@Override
					public List getData() {
						List<GoodReportVO> list = pageResult.getRows();
						List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
						if (!list.isEmpty()) {
							int temp = 0;
							for (int i = 0; i < list.size(); i++) {
								Map<String, Object> mapParam = new HashMap<String, Object>();
								GoodReportVO vo = (GoodReportVO) list.get(i);
								mapParam.put("no", ++temp);
								mapParam.put("sku", vo.getSku());
								mapParam.put("sku_name", vo.getSku_name());
								mapParam.put("document_number", vo.getDocument_number());
								mapParam.put("document_date", vo.getDocument_date());
								mapParam.put("business_type", vo.getBusiness_type());
								mapParam.put("is_tax", CommonUtil.isNotEmpty(vo.getIs_tax())?"0".equals(vo.getIs_tax())?"否":"是":"-");
								mapParam.put("legaler_name", vo.getLegaler_name());
								mapParam.put("warehouse_name", vo.getWarehouse_name());
								mapParam.put("quantity", vo.getQuantity());
								mapParam.put("currency", vo.getCurrency());
								mapParam.put("creator_name", vo.getCreator_name());
								mapParam.put("department_name", vo.getDepartment_name());
								mapParam.put("transport_type", "1".equals(vo.getTransport_type())?"海运":"0".equals(vo.getTransport_type())?"空运":"-");
								
								if(RoleUtil.CanShanPinExportAll("report_reportgood_money_view")
										&& RoleUtil.CanShanPinExportAll("report_reportgood_intercourse_view")){
									mapParam.put("cost", vo.getCost());
									mapParam.put("price", vo.getPrice());
									mapParam.put("intercourse_unit_name", vo.getIntercourse_unit_name());
								}else if(RoleUtil.CanShanPinExportAll("report_reportgood_money_view")){
									
									mapParam.put("cost", vo.getCost());
									mapParam.put("price", vo.getPrice());
									
								}else if(RoleUtil.CanShanPinExportAll("report_reportgood_intercourse_view")){
									mapParam.put("intercourse_unit_name", vo.getIntercourse_unit_name());
								}
								lists.add(mapParam);
							}
						}
						return lists;
					}
				}, os, MODE.EXCEL).export();
			} catch (UnsupportedEncodingException e) {
				logger.error("下载报表", e);
			} catch (IOException e) {
				logger.error("IO", e);
			}
			ModelAndView mv = new ModelAndView("goods_send_recieve_detail");
			
			return "";
		}
		
		
	}
	/**
     * 压缩文件或路径
     * 
     * @param zip 压缩的目的地址
     * @param srcFiles 压缩的源文件
     */
    public void zipFile(String zip, List<File> srcFiles)
    {
        File zipFile = new File(zip);
        InputStream input = null;
        ZipOutputStream zipOut;
        try
        {
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOut.setComment("hello");
            for (int i = 0; i < srcFiles.size(); i++)
            {
                input = new FileInputStream(srcFiles.get(i));
                zipOut.putNextEntry(new ZipEntry(srcFiles.get(i).getName()));
                int temp = 0;
                while ((temp = input.read()) != -1)
                {
                    zipOut.write(temp);
                }
                input.close();
            }
            zipOut.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
    }

}
