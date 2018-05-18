package com.aukey.report.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.springframework.web.servlet.ModelAndView;

import com.aukey.domain.GroupInfo;
import com.aukey.report.base.BaseController;
import com.aukey.report.domain.Email;
import com.aukey.report.dto.PayOrderReportParam;
import com.aukey.report.service.PayOrderReportService;
import com.aukey.report.service.SendEmailService;
import com.aukey.report.utils.CommonUtil;
import com.aukey.report.utils.PayOrderInfoExcel;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.PayOrderReportVo;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CasUtil;
import com.aukey.util.DateUtil;
import com.aukey.util.HttpUtil;
import com.aukey.util.ThreadPool;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 付款执行情况报表
 * 
 * @author DengZhibin
 *
 */
@RequestMapping("/report/payOrder")
@Controller
public class PayOrderReportController extends BaseController
{

    private Logger logger = Logger.getLogger(getClass());
    
    private static int MAX_ROW = 30000;

//    private static int SIZE = 500;
    
    /**
     * cas uri
     */
    @Value("${cas.uri}")
    private String casUri;
    
    @Value("${spring.mail.username}")
    private String from;
    
    @Value("${String.gain.alias.supplier}")
    private String gainAliasSupplierUrl;
    
    @Autowired
    private PayOrderReportService payOrderReportService;
    
    @Resource
    private SendEmailService sendEmailService;


    @RequestMapping("/index")
    public ModelAndView index(ModelMap modelMap)
    {
    	 ModelAndView mv = new ModelAndView("pay_order_report_list");
    	 
    	// 获取付款方式
    	 List<Map<String, Object>> payWayList = payOrderReportService.getAllNewSupplierPayment();
    	 modelMap.addAttribute("payWayList", payWayList);
         
         // 获取付款法人
         List<Map<String, Object>> corporationList = payOrderReportService.getAllCorporationList();
         modelMap.addAttribute("corporationList", corporationList);         
         
         // 获取付款请款部门
         List<Integer> deptIds = payOrderReportService.getAllDeptId();
         List<GroupInfo> groupList = CasUtil.selectGroupNameByGroupIds(casUri, deptIds.toArray(new Integer[deptIds.size()]));
         mv.addObject("groupList", groupList);        
    	 
         return mv;
    }
    
    @RequestMapping("/search")
    @ResponseBody
    public TableData<PayOrderReportVo> search(PayOrderReportParam param,
        @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
        @RequestParam(value = "limit", defaultValue = "10") int limit, HttpServletRequest request) {
        PageParam pageParam = new PageParam(pageNumber, limit);
        TableData<PayOrderReportVo> result = payOrderReportService.listPage(pageParam, param);
        return result;
    }

   
    @RequestMapping("/export")
    public void export(PayOrderReportParam param, 
    		 @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
    	        @RequestParam(value = "limit", defaultValue = "10") int limit,
    		 HttpServletRequest request, HttpServletResponse response){
    	
    	PageParam pageParam = new PageParam(pageNumber, limit);
    	Map<String, Object> paramsMap = payOrderReportService.generateParams(pageParam, param);
    	Long orderRow = payOrderReportService.count(paramsMap);
    	paramsMap.put("pageNumber", 0);
    	paramsMap.put("limit", orderRow);
    	if (orderRow > MAX_ROW) {
    		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	        Runnable runnable = new Runnable() {
    	            @Override
    	            public void run() {
    	                logger.info("-----------开始生成Excel----------------------");
    	                SecurityContextHolder.getContext().setAuthentication(authentication);
    	                List<PayOrderReportVo> infoList = payOrderReportService.bigDataSplitGeneateAttribute(paramsMap, true);
    	                Integer dataSize =  infoList.size();
    	                
    	                PayOrderInfoExcel payExcel = new PayOrderInfoExcel();
    	            	HSSFWorkbook workbook = new HSSFWorkbook();
    	            	HSSFSheet sheet = workbook.createSheet("付款执行情况报表");//创建一个工作表
    	            	OutputStream out;
    	            	List<File> fileUrls = new ArrayList<>();
    	            	
                        int page = 1;
                        int fromIndex = 0;
                        int toIndex = MAX_ROW;
                        String fileUrl = new File("").getAbsolutePath() + File.separator + "付款执行情况报表"
                            + DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + "[" + fromIndex + "," + toIndex + "].xls";
                        fileUrls.add(new File(fileUrl));                        
                        payExcel.setSheetTitle(workbook, sheet);
                        
						try {
							out = new FileOutputStream(fileUrl);
							while (fromIndex < dataSize) {
								if (fromIndex % MAX_ROW == 0 && fromIndex != 0) {
									toIndex = fromIndex + MAX_ROW;
									if (toIndex > dataSize) {
										toIndex = dataSize;
									}
									fileUrl = new File("").getAbsolutePath() + File.separator + "付款执行情况报表"
											+ DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + "[" + fromIndex + "-" + toIndex
											+ "].xls";
									out = new FileOutputStream(fileUrl);
									fileUrls.add(new File(fileUrl));
									
									workbook = new HSSFWorkbook();
									sheet = workbook.createSheet("付款执行情况报表");
									payExcel.setSheetTitle(workbook, sheet);
									page++;
								}
								payExcel.appendSheetRow(workbook, sheet, 1, infoList.subList(fromIndex, toIndex));
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
		                    String zipPath = new File("").getAbsolutePath() + File.separator + "付款执行情况报表"
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
		                    email.setSubject("付款执行情况报表");
		                    email.setFileName(zipPath);
		                    email.setTo(userAccount);
		                    email.setText("付款执行情况报表数据。本邮件由系统自动发送，请勿回复！");

		                    sendEmailService.attachments(email, "zip");
		                    logger.info("-----------邮件发送完毕----------------------");

		                    Thread.sleep(500);
		                    new File(zipPath).delete();
		                }
		                catch (Exception e)
		                {
		                    logger.error("-----------邮件发送失败,系统出现错误----------------------",e);
		                }
						
						
    	            }
    	         };

    	    ThreadPool.submit(runnable);
    	    
    	} else {
             try {
            	 List<PayOrderReportVo> infoList = payOrderReportService.bigDataSplitGeneateAttribute(paramsMap, false);
            	 // 1.创建excel标题行
            	 HSSFWorkbook workbook = new HSSFWorkbook();
            	 HSSFSheet sheet = workbook.createSheet("付款执行情况报表");
            	 PayOrderInfoExcel payExcel = new PayOrderInfoExcel();
            	 payExcel.setSheetTitle(workbook, sheet);
            	 payExcel.appendSheetRow(workbook, sheet, 1, infoList);            	 
            	 response.setContentType("application/vnd.ms-excel; charset=utf-8");
            	 String fileName = DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + ".xls";
            	 response.setHeader("Content-disposition", "attachment;filename=\"" + fileName + " ");            	 
            	 workbook.write(response.getOutputStream());
            	 response.getOutputStream().flush();
            	 response.getOutputStream().close();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取供应商数据
     * @param supplierName 供应商别名
     * @return
     */
    @RequestMapping(value = "/gainSupplierDataByAlias", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse gainSupplierDataForAjax(String supplierName,String enable,String drawbackStatus)
    {
        AjaxResponse response = new AjaxResponse();
        response.setData(gainSupplierData(supplierName,enable,drawbackStatus));
        return response;
    }
    
    /**
     * 根据供应（缺损）商名称，获取供应商详细信息
     * @param supplierName 供应商名称（缺损名称）
     * @return
     */
    @SuppressWarnings("unchecked")
	private Map<String, Object> gainSupplierData(String supplierName,String enable,String drawbackStatus)
    {
        ObjectMapper objMapper = new ObjectMapper();
        Map<String, Object> resultMap = new HashMap<>();
        if(CommonUtil.isEmpty(enable)){
            enable = "";
        }
        if(CommonUtil.isEmpty(drawbackStatus)){
            drawbackStatus = "";
        }
        String url = String.format("%s?name=%s&enable=%s&drawback_status=%s", gainAliasSupplierUrl, supplierName, enable, drawbackStatus);
        AjaxResponse response = HttpUtil.doGet(url);
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<Map<String, Object>> selectMapList = new ArrayList<>();
        Map<Integer, List<Map<String, Object>>> accountListMap = new HashMap<>();
        resultMap.put("accountList", accountListMap);
        resultMap.put("supplierSelect", selectMapList);
        Map<String, Object> responseMap = null;
        try
        {
            if(response.getData()!=null)
            {
                responseMap = objMapper.readValue(response.getData().toString(), HashMap.class);
                if(responseMap.get("data")!=null)
                {
                    mapList = (List<Map<String, Object>>) responseMap.get("data");
                }
                
                if (CommonUtil.isEmpty(mapList))
                {
                    return resultMap;
                }
                for (Map<String, Object> map : mapList)
                {
                    Integer supperlierId = Integer.parseInt(map.get("supplierId").toString());
                    String alias = map.get("alias").toString();
                    String name = map.get("name").toString();
                    String abbreviation = map.get("abbreviation").toString();
                    String corporationName = map.get("corporationName") == null ? ""
                        : map.get("corporationName").toString();
                    Integer corporationId = map.get("corporationId") == null ? null
                        : Integer.parseInt(map.get("corporationId").toString());
                    Map<String, Object> nameMap = new HashMap<>();
                    nameMap.put("abbreviation", abbreviation);
                    nameMap.put("alias", alias);
                    nameMap.put("name", name);
                    nameMap.put("supperlierId", supperlierId);
                    nameMap.put("corporationId", corporationId);
                    nameMap.put("corporationName", corporationName);
                    selectMapList.add(nameMap);
                    List<Map<String, Object>> resultAccountList = new ArrayList<>();
                    List<Map<String, Object>> accountList = (List<Map<String, Object>>) map.get("accountList");
                    if(accountList != null){
                    	for (Map<String, Object> account : accountList) {
                    		if (CommonUtil.isEmpty(account.get("accountBank").toString())) {
                    			continue;
                    		}
                    		if (CommonUtil.isEmpty(account.get("accountCode").toString())) {
                    			continue;
                    		}
                    		if (CommonUtil.isEmpty(account.get("currencyName").toString())){
                    			continue;
                    		}
                    		resultAccountList.add(account);
                    	}                	
                    }
                    accountListMap.put(supperlierId, resultAccountList);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return resultMap;

    }
    
    
    @RequestMapping("/count")
    @ResponseBody
    public long count(PayOrderReportParam param,
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "limit", defaultValue = "10") int limit, HttpServletRequest request) {
        
        PageParam pageParam = new PageParam(pageNumber, limit);
    	Map<String, Object> paramsMap = payOrderReportService.generateParams(pageParam, param);
        long result = payOrderReportService.count(paramsMap);
        
        return result;
    }
    
}
