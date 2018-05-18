package com.aukey.report.web;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.aukey.report.base.BaseController;
import com.aukey.report.domain.Email;
import com.aukey.report.domain.PurchaseWarnReport;
import com.aukey.report.domain.StrictSupplier;
import com.aukey.report.domain.Supplier;
import com.aukey.report.domain.base.Result;
import com.aukey.report.dto.PurchaseWarnParam;
import com.aukey.report.service.PurchaseWarnService;
import com.aukey.report.service.SendEmailService;
import com.aukey.report.utils.CommonUtil;
import com.aukey.report.utils.PurchaseWarnExcel;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.DepartmentResult;
import com.aukey.report.vo.DepartmentResult.DepartmentVO;
import com.aukey.report.vo.LegalerResult;
import com.aukey.util.AjaxResponse;
import com.aukey.util.DateUtil;
import com.aukey.util.HttpUtil;
import com.aukey.util.ThreadPool;
import com.aukey.util.URLBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
	
	/**
	 * 采购预警报表
	 * @author wangxiaowen
	 *
	 */
	@RequestMapping("/report/PurchaseWarn")
	@Controller
	public class PurchaseWarnReportController extends BaseController {
		
		private Logger logger = Logger.getLogger(getClass());
		private static int MAX_ROW = 60000;
		
		
		@Autowired
		private PurchaseWarnService purchaseWarnService;
		
		
		@Resource
	    private SendEmailService sendEmailService;
		
		@Value("${spring.mail.username}")
	    private String from;
		
		@Value("${legaler.api.url}")
		private String legaler_list_url;
		
		@Value("${purchase.department.api.url}")
		private String all_department_list_url;
		//需求部门的接口
		@Value("${purchase.department.sales.api.url}")
	    private String all_department_sales_list_url;
		
		/**
	     * * 供应商模糊搜索
	     */
	    @Value("${String.aliaslike.url}")
	    private String aliaslikeUrl;
		//
	    /**
	     * * 供应链域名
	     */
	    @Value("${String.chain.demand}")
	    private String chainDemand;
	    
	    /**
	     * 销售组
	     */
	    @Value("${String.cas.kai.url}")
	    private String saleDeptByGroup;
	    
		@RequestMapping("/index")
		public String index(ModelMap modelMap) {
			
			
			// 获取法人主体列表
			Map<String, Object> legaler_resultMap = null;
			try {
				AjaxResponse legaler_result = HttpUtil.doGet(legaler_list_url);
				if (legaler_result.getData() != null) {
					LegalerResult LegalerResult = JSON.parseObject(legaler_result.getData().toString(), LegalerResult.class);
					String str = LegalerResult.getData();
					ObjectMapper objMapper = new ObjectMapper();
					legaler_resultMap = objMapper.readValue(str, HashMap.class);
				}
			} catch (Exception e) {
				logger.error("获取法人主体列表获取异常",e);
				e.printStackTrace();
			}
			
			boolean flagPay = true;//判断是否应付
			//String[] deptIds = {};
			List<Integer> deptIds = new ArrayList<Integer>();
			List<Map<String,Object>> userGroup2 = RoleUtil.getUserGroup();
			for (Map<String, Object> map : userGroup2) 
			{
				if("CUSTOMS".equals(map.get("cateogryCode")))
				{
					flagPay = false;
					break;
				}else if("PURCHASE".equals(map.get("cateogryCode")))
				{
					deptIds.add((Integer) map.get("orgGroupId"));
				}
			}
			if(!flagPay)
			{
				// 获取需求部门列表
				List<DepartmentVO> department_list = new ArrayList<DepartmentVO>();
				try {
					AjaxResponse all_department_result = HttpUtil.doGet(all_department_sales_list_url);
					if (all_department_result.getData() != null) {
						DepartmentResult DepartmentResult = JSON.parseObject(all_department_result.getData().toString(), DepartmentResult.class);
						department_list = DepartmentResult.getData();
					}
				} catch (Exception e) {
					logger.error("获取需求部门列表异常",e);
					e.printStackTrace();
				}
				modelMap.addAttribute("department", department_list);
			}else
			{
				//saleDeptByGroup
				Map<String,Object[]> map = new HashMap<String,Object[]>();
				map.put("groupIds", deptIds.toArray());
				 List<Map<String,Object>> sale = getSale(map);
				modelMap.addAttribute("department", sale);
				
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String nowtime = format.format(new Date());
			
			Calendar c = Calendar.getInstance(); 
			c.add(Calendar.MONTH, -1);
			String starttime = format.format(c.getTime());
			modelMap.addAttribute("nowtime", nowtime);
			modelMap.addAttribute("starttime", starttime);
			modelMap.addAttribute("legalerName", legaler_resultMap);
			
			modelMap.addAttribute("chainDemand", chainDemand);
			
			return "purchaseWarnReport";
		}
		/**
		 * 获取所有采购预警信息
		 * @param request
		 * @return
		 */
		@RequestMapping("/search")
		@ResponseBody
		public TableData<PurchaseWarnReport> search(HttpServletRequest request ,PurchaseWarnParam param, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
			PageParam pageParam = new PageParam(pageNumber, pageSize);
			PurchaseWarnParam params=null;
			Integer userId = Integer.valueOf(request.getAttribute("userId").toString());//获取当前用户的Id
	        HttpSession session = request.getSession();
	        if(pageNumber>1){
	        	session.setAttribute("pageNumber",pageNumber);
	        	params = (PurchaseWarnParam) session.getAttribute("param");
	        }else{
	        	if(session.getAttribute("pageNumber")!=null){
	    	        if(pageNumber!=(Integer)session.getAttribute("pageNumber")){
	    	        	session.setAttribute("pageNumber",pageNumber);
	    	        	params = (PurchaseWarnParam) session.getAttribute("param");
	    	        }else{
	    	        	session.setAttribute("param",param);
	    	        	params=param;
	    	        }
	            }else{
	            	session.setAttribute("param",param);
	            	params=param;
	            }
	        }
	        logger.info("-----------开始查询采购预警----------------------");
	        dataFilterByUserRole(pageParam, userId, params);
			TableData<PurchaseWarnReport> result = purchaseWarnService.queryPage(pageParam, params,userId);
			logger.info("-----------查询采购预警结束----------------------");
			return result;
		}
		//
		/*@RequestMapping("/preExport")
		@ResponseBody
		public AjaxResponse preExport(PurchaseWarnParam param, HttpServletRequest request, HttpServletResponse response) {
			int count = purchaseWarnService.count(param);
			if (count > 60000) {
				return new AjaxResponse().failure("导出的数据量太大，请选择过滤条件");
			}
			return new AjaxResponse();
		}
		 
		@RequestMapping("/export")
		public void export(PurchaseWarnParam param, HttpServletRequest request, HttpServletResponse response) {
			try {
				final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String filename = "采购预警报表_" + sdf.format(new Date());
				response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题  
				response.setCharacterEncoding("utf-8");  
				response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");  
				OutputStream os = response.getOutputStream();

				int columns = 20;
				DataField[] dataFields = new DataField[columns];
				dataFields[0] = new DataField("序号", "no");
				dataFields[1] = new DataField("法人主体", "legaler_name");
				dataFields[2] = new DataField("供应商", "supplier_name");
				dataFields[3] = new DataField("采购单号", "purchase_no");
				dataFields[4] = new DataField("采购时间", "purchase_date");
				dataFields[5] = new DataField("sku", "sku_code");
				dataFields[6] = new DataField("sku名称", "sku_name");
				dataFields[7] = new DataField("订单数量", "purchase_count");
				dataFields[8] = new DataField("币别", "currency");
				dataFields[9] = new DataField("含税单价", "price_tax");
				dataFields[10] = new DataField("未税单价", "price_without_tax");

				dataFields[11] = new DataField("订单金额", "purchase_sum");
				dataFields[12] = new DataField("采购员", "buyer_name");
				dataFields[13] = new DataField("业务部门", "department_id");
				dataFields[14] = new DataField("入库数量", "stock_count");

				dataFields[15] = new DataField("退货数量", "return_count");
				dataFields[16] = new DataField("欠货数量", "lack_count");
				dataFields[17] = new DataField("超期时间", "out_date");
				dataFields[18] = new DataField("超期数量", "out_date_count");

				dataFields[19] = new DataField("预计交货时间", "before_stock_date");
				DataPage pageParam = new DataPage(60000);
				new ExcelDataExportor<Object>(pageParam, dataFields, new ExportDataSource<Object>() {
					@Override
					public List getData() {
						Integer userId = 0;
				        String username = "";
				        String password = "";
				        if (request.getAttribute("userId") != null)
				        {
				            userId = Integer.valueOf(request.getAttribute("userId").toString());
				            username = request.getAttribute("account").toString();
				            password = request.getAttribute("password").toString();
				        }
						PageParam pageParam = new PageParam(1, 60000);
						TableData<PurchaseWarnReport> pageResult = purchaseWarnService.queryPage(pageParam, param);

						List<PurchaseWarnReport> list = pageResult.getRows();
						List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
						if (!list.isEmpty()) {
							int temp = 0;
							for (int i = 0; i < list.size(); i++) {
								Map<String, Object> mapParam = new HashMap<String, Object>();
								PurchaseWarnReport vo = (PurchaseWarnReport) list.get(i);
								mapParam.put("no", ++temp);
								mapParam.put("legaler_name", vo.getLegaler_name());
								mapParam.put("supplier_name", vo.getSupplier_name());
								mapParam.put("purchase_no", vo.getPurchase_no());
								mapParam.put("purchase_date", vo.getPurchase_date());
								mapParam.put("sku_code", vo.getSku_code());
								mapParam.put("sku_name", vo.getSku_name());
								mapParam.put("purchase_count", vo.getPurchase_count());
								mapParam.put("currency", vo.getCurrency());
								mapParam.put("price_tax", vo.getPrice_tax());
								mapParam.put("price_without_tax", vo.getPrice_without_tax());
								mapParam.put("purchase_sum", vo.getPurchase_sum());
								mapParam.put("buyer_name", vo.getBuyer_name());
								mapParam.put("department_id", vo.getDepartment_id());
								mapParam.put("stock_count", vo.getStock_count());
								mapParam.put("return_count", vo.getReturn_count());
								mapParam.put("lack_count", vo.getLack_count());
								mapParam.put("out_date", vo.getOut_date());
								mapParam.put("out_date_count", vo.getOut_date_count());
								mapParam.put("before_stock_date", vo.getBefore_stock_date());
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
			
		}*/
		@RequestMapping("/export")
	    public String export(RedirectAttributes attr,PurchaseWarnParam param, 
	    		 @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
	    	        @RequestParam(value = "limit", defaultValue = "10") int limit,
	    		 HttpServletRequest request, HttpServletResponse response){
			Integer userId = Integer.valueOf(request.getAttribute("userId").toString());//获取当前用户的Id
	    	PageParam pageParam = new PageParam(pageNumber, limit);
	    	try {
				Object attribute = request.getAttribute("requestOrderId");
				System.out.println("-------"+attribute);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				logger.error("---------没有requestOrderId这个参数----------");
				e2.printStackTrace();
			}
	    	dataFilterByUserRole(pageParam, userId, param);
	    	TableData<PurchaseWarnReport> pageResult = purchaseWarnService.queryPage(pageParam, param,userId);
	    	Long orderRow = pageResult.getTotal();
	    	if (orderRow > MAX_ROW) {
	    		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    	        Runnable runnable = new Runnable() {
	    	            @Override
	    	            public void run() {
	    	                logger.info("-----------开始生成Excel----------------------");
	    	                SecurityContextHolder.getContext().setAuthentication(authentication);
	    	                List<PurchaseWarnReport> infoList = pageResult.getRows();
	    	                
	    	                PurchaseWarnExcel payExcel = new PurchaseWarnExcel();
	    	            	HSSFWorkbook workbook = new HSSFWorkbook();
	    	            	HSSFSheet sheet = workbook.createSheet("采购预警报表");//创建一个工作表
	    	            	OutputStream out;
	    	            	List<File> fileUrls = new ArrayList<>();
	    	            	
	                        int page = 1;
	                        int fromIndex = 0;
	                        int toIndex = MAX_ROW;
	                        String fileUrl = new File("").getAbsolutePath() + File.separator + "采购预警报表"
	                            + DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + "[" + fromIndex + "," + toIndex + "].xls";
	                        fileUrls.add(new File(fileUrl));                        
	                        payExcel.setSheetTitle(workbook, sheet);
	                        
							try {
								out = new FileOutputStream(fileUrl);
								while (fromIndex < orderRow) {
									if (fromIndex % MAX_ROW == 0 && fromIndex != 0) {
										toIndex = fromIndex + MAX_ROW;
										if (toIndex > orderRow) {
											toIndex = Integer.parseInt(orderRow+"");
										}
										fileUrl = new File("").getAbsolutePath() + File.separator + "采购预警报表"
												+ DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + "[" + fromIndex + "-" + toIndex
												+ "].xls";
										out = new FileOutputStream(fileUrl);
										fileUrls.add(new File(fileUrl));
										
										workbook = new HSSFWorkbook();
										sheet = workbook.createSheet("采购预警报表");
										payExcel.setSheetTitle(workbook, sheet);
										page++;
									}
									payExcel.appendSheetRow(workbook, sheet, 1, infoList.subList(fromIndex, toIndex-1));
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
							} catch (Exception e1) {
								e1.printStackTrace();
							}      
							
							
			                logger.info("-----------Excel生成结束----------------------");

			                try {

			                    logger.info("-----------开始压缩----------------------");
			                    String userAccount = RoleUtil.getUserAccount();
			                    //String userAccount = "wangxiaowen@aukeys.com";
			                    String zipPath = new File("").getAbsolutePath() + File.separator + "采购预警报表"
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
			                    email.setSubject("采购预警报表");
			                    email.setFileName(zipPath);
			                    email.setTo(userAccount);
			                    email.setText("采购预警报表数据。本邮件由系统自动发送，请勿回复！");

			                    sendEmailService.attachments(email, "zip");
			                    logger.info("-----------邮件发送完毕----------------------");

			                    Thread.sleep(500);
			                    new File(zipPath).delete();
			                }
			                catch (Exception e)
			                {
			                    logger.error("-----------邮件发送失败,系统出现错误----------------------");
			                    e.printStackTrace();
			                }
							
							
	    	            }
	    	         };

	    	    ThreadPool.submit(runnable);
	    	    try
                {
                    String msg = URLEncoder.encode("导出成功,导出文件大于6万的会发送至邮箱，请稍等查看！", "UTF-8");
                    attr.addFlashAttribute("msg", msg);
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
	    	    return "redirect:index";
	    	} else {
	             try {
	            	 List<PurchaseWarnReport> infoList = pageResult.getRows();
	            	 // 1.创建excel标题行
	            	 HSSFWorkbook workbook = new HSSFWorkbook();
	            	 HSSFSheet sheet = workbook.createSheet("采购预警报表");
	            	 PurchaseWarnExcel payExcel = new PurchaseWarnExcel();
	            	 payExcel.setSheetTitle(workbook, sheet);
	            	 payExcel.appendSheetRow(workbook, sheet, 1, infoList);           	 
	            	 //response.setContentType("application/vnd.ms-excel; charset=utf-8");
	            	 String fileName = "采购预警报表"+DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + ".xls";
	            	 response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题  
	 				 response.setCharacterEncoding("utf-8");  
	 				 response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1"));    	 
	            	 workbook.write(response.getOutputStream());
	            	 response.getOutputStream().flush();
	            	 response.getOutputStream().close();
	            	 try
	                 {
	                     String msg = URLEncoder.encode("导出成功！", "UTF-8");
	                     attr.addFlashAttribute("msg", msg);
	                 }
	                 catch (UnsupportedEncodingException e)
	                 {
	                     e.printStackTrace();
	                 }
	            	 
				} catch (Exception e) {
					e.printStackTrace();
					try
	                {
	                    String msg = URLEncoder.encode("导出失败，请重新导出！", "UTF-8");
	                    attr.addFlashAttribute("msg", msg);
	                }
	                catch (UnsupportedEncodingException e2)
	                {
	                    e2.printStackTrace();
	                }
					
				}
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
		 /*@RequestMapping("/getBuyer")
		 @ResponseBody
		 public Result getBuyerName(HttpServletRequest request,@Param(value="userId")String userId){
			 String userName = purchaseWarnService.serachUserName(userId);
			 Result result = new Result();
			 if(userName!=null){
				 result.setErrMsg(userName);
				 result.setSuccess(true);
				 return result;
			 }
			 result.setData(userId);
			 result.setSuccess(false);
			 return result;
		 }*/
		 
		 /**
		  *	获取供应商信息
		  *
		  */
		 /*@RequestMapping("/getSupplierInfo1")
		 @ResponseBody
		 public List<Supplier> getSupplierInfo(HttpServletRequest request,@RequestParam(value = "name", required = false) String name){
			 List<Supplier> list = supplierService.getSupplierList(name);
			 return list;
		 }*/
		 
		@RequestMapping(value = "/getSupplierInfo", method = RequestMethod.GET)
		@ResponseBody
	    public List<StrictSupplier> getSupplierAllInfo(@RequestParam("supplyAlias") String supplyAlias)
	    {
	        List<StrictSupplier> resu = new ArrayList<StrictSupplier>();
	        if (supplyAlias == null || CommonUtil.isEmpty(supplyAlias))
	        {
	            return null;
	        }
	        Map<String, Object> paramMaps = new HashMap<String, Object>();
	        paramMaps.put("supplierName", supplyAlias);
	        AjaxResponse result = HttpUtil.doGet(URLBuilder.generate(paramMaps, aliaslikeUrl));
	        if (result.isSuccess() && result.getData() != null)
	        {
	            Result re = JSON.parseObject(result.getData().toString(), Result.class);
	            if (re != null && re.getSuccess())
	            {
	                List<Map<String, Object>> dataMapList = (List<Map<String, Object>>) re.getData();
	                if (CollectionUtils.isNotEmpty(dataMapList))
	                {
	                    for (Map<String, Object> datas : dataMapList)
	                    {
	                        StrictSupplier str = new StrictSupplier();
	                        Integer id = (Integer) datas.get("id");
	                        String username = (String) datas.get("name");
	                        str.setSupplierId(id);
	                        str.setAlias(username);
	                        resu.add(str);
	                    }

	                }
	                else
	                {
	                    return null;
	                }
	            }
	        }
	        else
	        {
	            // 主管的下级为空的时候
	        }
	        return resu;
	    }
		//权限控制
		private void dataFilterByUserRole(PageParam pageParam,Integer userId,PurchaseWarnParam param){
			
			boolean purchaseFlag = false;//采购
			boolean warehouseOrPayFlag = false;//仓库和财务
			StringBuilder groupIds= new StringBuilder();
			
			List<Map<String, Object>> groups = RoleUtil.getUserGroup();
			for (Map<String, Object> map : groups) {
				if(map.get("cateogryCode").equals("PURCHASE")){//采购员
					groupIds.append(map.get("orgGroupId").toString()+",");
					purchaseFlag=true;
					
				}
				if(map.get("cateogryCode").equals("WAREHOUSE") || map.get("cateogryCode").equals("CUSTOMS") || 
						map.get("cateogryCode").equals("CASHIER") || map.get("cateogryCode").equals("TARIFF")){
					//仓库或是财务部门
					warehouseOrPayFlag=true;
				}
			}
			if(purchaseFlag){
				param.setPurchase_group_id(groupIds.toString());
			}else if(warehouseOrPayFlag){
				param.setPurchase_group_id(null);
			}else{
				pageParam.setNumPerPage(0);
				pageParam.setPageNum(0);
			}
			/*String[] split = groupIds.toString().split(",");
			System.out.println("------???"+Arrays.toString(split));
			for (int i = 0; i < split.length; i++) {
				if(i==split.length-1){
					System.out.println("------???"+split[i]);
				}
			}*/
			
		} 
		
		
		private List<Map<String,Object>> getSale(Map<String,Object[]> map)
		{
			//saleDeptByGroup
			String param = "";
			/*try {
				param = Arrays.toString((long[]) map.get("groupIds"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
			Object[] strings = map.get("groupIds");
			param+="[";
			for (int i = 0; i < strings.length; i++) 
			{
				if(strings.length==1)
				{
					param+=strings[i];
					break;
				}
				
				if(i==0)
				{
					param+=strings[i]+",";
				}else if(i==strings.length-1)
				{
					param+=strings[i];
				}else
				{
					param+=strings[i]+",";
				}
			}
			param+="]";
			param = saleDeptByGroup+"?groupIds="+param;
			
			AjaxResponse ajaxResponse = HttpUtil.doGet(param);
			DepartmentResult parseArray = null;
			if(ajaxResponse.getData()!=null)
			{
				//Result re = JSON.parseObject(ajaxResponse.getData().toString(), Result.class);
				
				parseArray = JSON.parseObject(ajaxResponse.getData().toString(),DepartmentResult.class);
				logger.info(parseArray.getData().get(0).getGroupId()+"--"+parseArray.getData().get(0).getGroupName());
				//List<DepartmentVO> list = parseArray.getData();
				int size = parseArray.getData().size();
				for (int i = 0; i < size; i++) 
				{
					if("销售类".equals(parseArray.getData().get(i).getCategoryName()))
					{
						/*list.remove(i);*/
						Map<String,Object> map2 = new HashMap<String,Object>();
						map2.put("groupId", parseArray.getData().get(i).getGroupId());
						map2.put("groupName", parseArray.getData().get(i).getGroupName());
						list.add(map2);
						
					}
				}
				
			}
			return list;
			
		}
	
	}
