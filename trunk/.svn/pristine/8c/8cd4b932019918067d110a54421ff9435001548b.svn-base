package com.aukey.report.web.finance;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aukey.report.domain.Email;
import com.aukey.report.dto.PurchaseReportParam;
import com.aukey.report.service.SendEmailService;
import com.aukey.report.service.finance.PurchaseFinanceReportService;
import com.aukey.report.utils.CommonUtil;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.PurchaseReportVO;
import com.aukey.util.AjaxResponse;
import com.aukey.util.DateUtil;
import com.aukeys.ibatis.sql.SqlParser;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFileOutputStream;


/**
 * 同步线程
 */
public class OrderExcelSyncThread implements Callable<AjaxResponse> {

	 /**
     * 日志
     */
    private Logger logger = Logger.getLogger(getClass());
    
    private Map<String, Object> conditon;
    
	private PurchaseReportParam param;
	
	private PurchaseFinanceReportService purchaseFinanceReportService;
	
	private SendEmailService sendNewEmail;
	
	private String userAccount;
	
	private String from;
	
	private String zipPath;
	
	private Integer userId;
	
	private  PageParam pageParam;
	
	public OrderExcelSyncThread(PurchaseReportParam param,PurchaseFinanceReportService purchaseFinanceReportService,
								SendEmailService sendNewEmail,String userAccount,String from,String zipPath,Integer userId,
								PageParam pageParam)
	    {
	        this.param = param;
	        this.purchaseFinanceReportService = purchaseFinanceReportService;
	        this.sendNewEmail=sendNewEmail;
	        this.userAccount = userAccount;
	        this.from = from;
	        this.zipPath = zipPath;
	        this.userId=userId;
	        this.pageParam = pageParam;
	    }
	
	@Override
	public AjaxResponse call() throws Exception {
		AjaxResponse response = new AjaxResponse();
        synchronized(this) {
            try
            {
                response = orderExcel();
                response.setSuccess(true);
            }catch (Exception e) {
                logger.error("生成请款单失败：", e);
                response.setSuccess(false);
                response.setMessage("请款方法调用失败");
                return response;
            }
        }
        return response;
	}
	
    /**
     * 生成采购入库单 
     */
    public AjaxResponse orderExcel() throws Exception
    {
    	AjaxResponse response = new AjaxResponse();
        String fileZipName = new File("").getAbsolutePath() + File.separator + "order"
                + DateUtil.dateFormat(new Date(), "yyyyMMddHHmm") + ".zip";
        ZipOutputStream out = null;
        OutputStream out2 = null;
        try {
            out2 = new FileOutputStream(fileZipName);
            out = new ZipOutputStream(out2);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        logger.info("-----------开始生成Excel----------------------");
        //TODO
        Map<String,Object> resultMap = WriteExcel(param);
        String fileUrl= resultMap.get("fileUrl").toString();
        String code=    resultMap.get("code").toString();
        if(code.equals("发送")){
            //发邮件
            logger.info("-----------Excel生成结束----------------------");
            logger.info("-----------开始压缩----------------------");
            try {
    
                ZipUtils.zipFile(fileUrl, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("-----------压缩结束----------------------");
            logger.info("-----------删除excel文件开始----------------------");
            File file1 = new File(fileUrl);
            if (file1.exists()) {
                file1.delete();
            }
            logger.info("-----------删除excel文件结束----------------------");
    
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    
            if (fileZipName != null) {
                try {
                    // 发送邮件
                    logger.info("-----------开始发送邮件----------------------");
                    Email email = new Email();
                    email.setFrom(from);
                    email.setSubject("采购入库财务查询报表数据");
                    email.setFileName(fileZipName);
                    email.setTo(userAccount);
    
                    email.setText("采购入库财务查报表数据。本邮件由系统自动发送，请勿回复！");
                    sendNewEmail.attachments(email,".zip");
                    logger.info("-----------邮件发送完毕----------------------");
                    logger.info("-----------删除压缩包开始----------------------");
                    File file2 = new File(fileZipName);
                    if (file2.exists()) {
                        file2.delete();
                    }
                    logger.info("-----------删除压缩包结束----------------------");
                } catch (Exception e) {
                    logger.error("-----------邮件发送失败,系统出现错误----------------------");
                    e.printStackTrace();
                }
            }
        }else{
            //上传公司服务器
            //发邮件
            logger.info("-----------Excel生成结束----------------------");
            logger.info("-----------开始压缩----------------------");
            try {
    
                ZipUtils.zipFile(fileUrl, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("-----------压缩结束----------------------");
            logger.info("-----------删除excel文件开始----------------------");
            File file1 = new File(fileUrl);
            if (file1.exists()) {
                file1.delete();
            }
            logger.info("-----------删除excel文件结束----------------------");
    
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    
            if (fileZipName != null) {
                logger.info("-----------上传文件到服务器开始----------------------");
                try
                {
                    imageUploadUtils(fileZipName, zipPath);
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                logger.info("-----------上传文件到服务器结束----------------------");
                try {
                    // 发送邮件
                    logger.info("-----------开始发送邮件----------------------");
                     String [] arys=  fileZipName.split("order");
                    Email email = new Email();
                    email.setFrom(from);
                    email.setSubject("采购入库财务查询报表数据");
                    email.setFileName("order"+arys[1]);
                    email.setTo(userAccount);
    
                    email.setText("采购入库财务查询报表数据。本邮件由系统自动发送，请勿回复！");
                    sendNewEmail.attachments(email,".zip");
                    logger.info("-----------邮件发送完毕----------------------");
                    logger.info("-----------删除压缩包开始----------------------");
                    File file2 = new File(fileZipName);
                    if (file2.exists()) {
                        file2.delete();
                    }
                    logger.info("-----------删除压缩包结束----------------------");
                } catch (Exception e) {
                    logger.error("-----------邮件发送失败,系统出现错误----------------------");
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

	/**
	 * 生成excel
	 * @return
	 */
	private Map<String, Object> WriteExcel(PurchaseReportParam param) {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		
		List<PurchaseReportVO> listSum = new ArrayList<PurchaseReportVO>();
		SqlParser.stopParse4Short();
		int count = purchaseFinanceReportService.count(param, userId);
		if (count==0) 
		{
	        return null;
	     }
		int page = 1;
        int limit = 100000;
        pageParam.setPageNum(page);
        pageParam.setNumPerPage(limit);
		
        logger.info("-----------生成list----------------------");
		TableData<PurchaseReportVO> listPage = purchaseFinanceReportService.listPage(pageParam, param, userId);
		if(listPage.getRows().isEmpty())
		{
			return null;
		}else
		{
			listSum.addAll(listPage.getRows());
		}
		
		resultMap.put("code", "发送");
		while (page <= (count%100000+1))
		{
			resultMap.put("code", "上传");
			logger.info("-----------生成list----------------------");
			page += 1;
			pageParam.setPageNum(page);
			TableData<PurchaseReportVO> listPage2 = purchaseFinanceReportService.listPage(pageParam, param, userId);
			if(listPage2.getRows().isEmpty())
			{
				break;
			}else
			{
				listSum.addAll(listPage2.getRows());
			}
		}
		SqlParser.restore2Parse();
		
		OutputStream out = null;
        String fileUrl = new File("").getAbsolutePath() + File.separator + "order"
                + DateUtil.dateFormat(new Date(), "yyyyMMddHHmm") + ".xlsx";
        resultMap.put("fileUrl", fileUrl);
        try {
			out = new FileOutputStream(fileUrl);
			out = downloadExcel(listSum , out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return resultMap;
	}

	private OutputStream downloadExcel(List<PurchaseReportVO> listSum, OutputStream out) {
		String[] headerArray = { "序号", "法人主体", "供应商","采购单号", "采购日期", "SKU","SKU名称", "采购数量", "不含税单价", "含税单价", "采购金额","采购本币金额","采购币别","采购员",
				"采购部门", "需求部门", "入库单号","入库日期", "仓库", "入库数量", "入库金额", "入库本币金额", "品类","开票品名", "开票单位", "未开票数量", "未开票金额","未开票本币金额","已开票金额",
				"已开票本币金额", "开票状态", "品牌", "型号", "是否含税", "结算方式"};

	   int[] widthArray = { 4000, 4000, 3000, 5000,4000,5000, 4000, 3000, 1500, 2000, 2000, 6000, 2000, 2000, 2000, 2000, 2000,2000,
				2000, 2000, 2000, 3000,2000,2000, 2000, 2000, 2000,2000, 5000,2000,5000,2000 ,8000, 8000, 6000};
           SXSSFWorkbook wb = new SXSSFWorkbook();
           SXSSFRow row = null;
       // sheet个数
       int normalSheetCount = 0;
       int exceptSheetCount = 0;
       int totalSheetCount = 0;
       int sheetRowCount = 100000;
       if (listSum.size() % sheetRowCount == 0 && listSum.size() != 0) {
           normalSheetCount = listSum.size() / sheetRowCount;
       } else {
           normalSheetCount = (listSum.size() / sheetRowCount) + 1;
       }
       totalSheetCount = normalSheetCount + exceptSheetCount;
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 右对齐的格式
       CellStyle csAlignEnd = wb.createCellStyle();
       csAlignEnd.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
       int rowIndex=0;
       SXSSFSheet sheet = null;
       long startTime = System.currentTimeMillis();
       for (int y = 0; y < totalSheetCount; y++) {
           logger.info("-----------采购入库报表创建sheet----------------------");
           sheet = wb.createSheet("sheet" + (y + 1));
           int currRowNum = 0;
           row = sheet.createRow(currRowNum);
           SXSSFCell cell =null;
           for (int i = 0; i < headerArray.length; i++)
           {
               sheet.setColumnWidth((short) i, (short) widthArray[i]);
               cell = row.createCell((short) i);
               // cell.setEncoding((short) 1);
               cell.setCellType(1);
               cell.setCellValue(headerArray[i]);
               //cell.setCellStyle(cs2);
           }

               logger.info("-----------采购入库报表创建行----------------------");
               
               
               if (listSum != null && !listSum.isEmpty()) {
					// 用来控制第几行开始
					int num = 1;
					//来控制第几个开始获取数据
	                   int rowH = 1;
					// ***************************** 循环中间放值开始
	                // *****************************//*
						for (int h = rowIndex+1; h <= listSum.size(); h++) {
							row =sheet.createRow(num);
		                       cell = row.createCell((short) 0);
		                       cell.setCellType(1);
		                       cell.setCellValue(h);

		                       cell = row.createCell((short) 1);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getLegaler()) ? "-"
		                               : listSum.get(h - 1).getLegaler() + "");
		                       
		                       cell = row.createCell((short) 2);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getSupplier()) ? "-"
		                               : listSum.get(h - 1).getSupplier() + "");
		                       
		                       cell = row.createCell((short) 3);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getPurchase_no()) ? "-"
		                               : listSum.get(h - 1).getPurchase_no() + "");
		                       
		                       cell = row.createCell((short) 4);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getPurchase_date()) ? "-"
		                               : format.format(listSum.get(h - 1).getPurchase_date()));
		                       
		                       cell = row.createCell((short) 5);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getSku()) ? "-"
		                               : listSum.get(h - 1).getSku() + "");
		                       
		                       cell = row.createCell((short) 6);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getSku_name()) ? "-"
		                               : listSum.get(h - 1).getSku_name() + "");
		                       
		                       cell = row.createCell((short) 7);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getPurchase_count()) ? "-"
		                               : listSum.get(h - 1).getPurchase_count() + "");
		                       
		                       cell = row.createCell((short) 8);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getPrice_wihtout_tax()) ? "-"
		                               : listSum.get(h - 1).getPrice_wihtout_tax() + "");
		                       
		                       cell = row.createCell((short) 9);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getPrice_tax()) ? "-"
		                               : listSum.get(h - 1).getPrice_tax() + "");
		                       
		                       cell = row.createCell((short) 10);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getPurchase_sum()) ? "-"
		                               : listSum.get(h - 1).getPurchase_sum() + "");
		                       
		                       cell = row.createCell((short) 11);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getPurchaseCurrencySum()) ? "-"
		                               : formatterMoney(listSum.get(h - 1).getPurchaseCurrencySum()) + "");
		                       
		                       cell = row.createCell((short) 12);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getPurchase_money_type()) ? "-"
		                               : listSum.get(h - 1).getPurchase_money_type() + "");
		                       
		                       cell = row.createCell((short) 13);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getBuyer()) ? "-"
		                               : listSum.get(h - 1).getBuyer() + "");
		                       
		                       cell = row.createCell((short) 14);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getDepartment()) ? "-"
		                               : listSum.get(h - 1).getDepartment() + "");
		                       
		                       cell = row.createCell((short) 15);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getDeptNamexq()) ? "-"
		                               : listSum.get(h - 1).getDeptNamexq() + "");
		                       
		                       cell = row.createCell((short) 16);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getStock_number()) ? "-"
		                               : listSum.get(h - 1).getStock_number() + "");
		                       
		                       cell = row.createCell((short) 17);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getStock_date()) ? "-"
		                               : format.format(listSum.get(h - 1).getStock_date()) + "");
		                       
		                       cell = row.createCell((short) 18);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getStock_name()) ? "-"
		                               : listSum.get(h - 1).getStock_name() + "");
		                       
		                       cell = row.createCell((short) 19);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getStock_count()) ? "-"
		                               : listSum.get(h - 1).getStock_count() + "");
		                       
		                       cell = row.createCell((short) 20);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getStock_sum()) ? "-"
		                               : listSum.get(h - 1).getStock_sum() + "");
		                       
		                       cell = row.createCell((short) 21);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getStockCurrencySum()) ? "-"
		                               : formatterMoney(listSum.get(h - 1).getStockCurrencySum()) + "");
		                       
		                       cell = row.createCell((short) 22);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getCategory()) ? "-"
		                               : listSum.get(h - 1).getCategory() + "");
		                       
		                       cell = row.createCell((short) 23);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getBill_name()) ? "-"
		                               : listSum.get(h - 1).getBill_name() + "");
		                       
		                       cell = row.createCell((short) 24);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getBill_unit()) ? "-"
		                               : listSum.get(h - 1).getBill_unit() + "");
		                       
		                       cell = row.createCell((short) 25);
		                       cell.setCellType(1);
		                       if(CommonUtil.isEmpty(listSum.get(h - 1).getNo_bill_count()) || "否".equals(listSum.get(h - 1).getInclude_tax()))
		                       {
		                    	   cell.setCellValue("-");
		                       }else
		                       {
		                    	   cell.setCellValue(listSum.get(h - 1).getNo_bill_count());
		                       }
		                       
		                       /*cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getNo_bill_count()) ? "-"
		                               : listSum.get(h - 1).getNo_bill_count() + "");*/
		                       
		                       cell = row.createCell((short) 26);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getNoMakeInvoice()) ? "-"
		                               : listSum.get(h - 1).getNoMakeInvoice() + "");
		                       
		                       cell = row.createCell((short) 27);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getNoMakeInvoiceCurrency()) ? "-"
		                               : listSum.get(h - 1).getNoMakeInvoiceCurrency() + "");
		                       
		                       cell = row.createCell((short) 28);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getMakeInvoice()) ? "-"
		                               : listSum.get(h - 1).getMakeInvoice() + "");
		                       
		                       cell = row.createCell((short) 29);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getMakeInvoiceCurrency()) ? "-"
		                               : formatterMoney(listSum.get(h - 1).getMakeInvoiceCurrency()) + "");
		                       
		                       
		                       cell = row.createCell((short) 30);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getBill_status()) ? "-"
		                               : listSum.get(h - 1).getBill_status() + "");
		                       
		                       cell = row.createCell((short) 31);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getBrand()) ? "-"
		                               : listSum.get(h - 1).getBrand() + "");
		                       
		                       cell = row.createCell((short) 32);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getVersion()) ? "-"
		                               : listSum.get(h - 1).getVersion() + "");
		                       
		                       cell = row.createCell((short) 33);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getInclude_tax()) ? "-"
		                               : listSum.get(h - 1).getInclude_tax() + "");
		                       
		                       cell = row.createCell((short) 34);
		                       cell.setCellType(1);
		                       cell.setCellValue(CommonUtil.isEmpty(listSum.get(h - 1).getPay_type_name()) ? "-"
		                               : listSum.get(h - 1).getPay_type_name() + "");
		                       num++;
		                       if(h%100000==0)
		                       {
		                    	   rowIndex = h;
		                    	   break;
		                       }
		                       
		                       }
		                      
		                  
		                       /*if ((num-1) == sheetRowCount)
		                       {
		                           rowIndex=rowH;
		                           break;
		                       }*/
						}
						logger.info("-----------采购报表一个sheet结束----------------------");
						long endTime = System.currentTimeMillis();
						logger.info("采购报表时间" + (endTime - startTime) / 1000 + "秒");
						// ***************************** 循环中间放值结束
						// *****************************//
				}
       try {
           wb.write(out);
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } finally {
           try {
               wb.close();
           } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
       }
       return out;
	}
	
	   // 文件上传
	public void imageUploadUtils(String fileUrl, String path) throws IOException
	   {
	       FileInputStream  file = new FileInputStream (fileUrl);
	           // 文件保存路径
	           // String filePath =
	           // "smb://aukeyit:aukeyit2016@10.1.1.186/files/test/" +
	           // excleFile[0].getOriginalFilename();
	           // String filePath = path + excleFile[i].getOriginalFilename();
	       String [] arys=  fileUrl.split("order");
	       String filePath = path + "order"+arys[1];
	           SmbFileOutputStream smbOut = null;
	           BufferedInputStream bf = null;
	           try
	           {
	               bf = new BufferedInputStream(file);
	               smbOut = new SmbFileOutputStream(filePath, false);
	               byte[] bt = new byte[8192];
	               int n = bf.read(bt);
	               while (n != -1)
	               {
	                   smbOut.write(bt, 0, n);
	                   smbOut.flush();
	                   n = bf.read(bt);
	               }
	           }
	           catch (SmbException e)
	           {
	               e.printStackTrace();
	           }
	           finally
	           {
	               try
	               {
	                   if (null != smbOut)
	                       smbOut.close();
	                   if (null != bf)

	                       bf.close();
	               }
	               catch (Exception e2)
	               {
	                   e2.printStackTrace();
	               }
	           }
	   }
	public String formatterMoney(Double money){
		if(CommonUtil.isEmpty(money)){
			return "-";
		}
		DecimalFormat format = new DecimalFormat("0.00");
		
		return format.format(money);
	}

}
