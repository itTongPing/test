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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.aukey.report.base.BaseController;
import com.aukey.report.domain.Email;
import com.aukey.report.dto.TransferReportParam;
import com.aukey.report.service.SendEmailService;
import com.aukey.report.service.TransferReportService;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.TransferReportExcel;
import com.aukey.report.utils.export.DataField;
import com.aukey.report.utils.export.DataPage;
import com.aukey.report.utils.export.ExportDataSource;
import com.aukey.report.utils.export.excel.ExcelDataExportor;
import com.aukey.report.utils.export.excel.MODE;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.InventoryResult;
import com.aukey.report.vo.InventoryResult.InventoryVO;
import com.aukey.report.vo.LegalerResult;
import com.aukey.report.vo.TransferReportVO;
import com.aukey.util.AjaxResponse;
import com.aukey.util.DateUtil;
import com.aukey.util.HttpUtil;
import com.aukey.util.ThreadPool;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 调拔明细报表
 * 
 * @author xiehz
 *
 */
@RequestMapping("/report/transfer")
@Controller
public class TransferReportController extends BaseController
{

    private Logger logger = Logger.getLogger(getClass());

    private static int MAX_ROW = 60000;

    @Value("${inventory.api.url}")
    private String inventory_name_list_url;

    @Value("${transfer.api.url}")
    private String transfer_name_list_url;

    @Value("${legaler.api.url}")
    private String legaler_list_url;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private TransferReportService transferReportService;

    @Resource
    private SendEmailService sendEmailService;

    @RequestMapping("/index")
    public String index(ModelMap modelMap)
    {

        // 是否含税
        
            List<Map<String, String>> include_tax_list = new ArrayList<Map<String, String>>();
            Map<String, String> include_tax_item1 = new HashMap<String, String>();
            include_tax_item1.put("name", "是");
            include_tax_item1.put("value", "1");
            include_tax_list.add(include_tax_item1);
            Map<String, String> include_tax_item2 = new HashMap<String, String>();
            include_tax_item2.put("name", "否");
            include_tax_item2.put("value", "0");
            include_tax_list.add(include_tax_item2);
            modelMap.addAttribute("tax", include_tax_list);

            // 获取法人主体列表
            Map<String, Object> legaler_resultMap = null;
            AjaxResponse legaler_result = HttpUtil.doGet(legaler_list_url);
           if (legaler_result.getData() != null)
        {
        	LegalerResult LegalerResult = null;
        	try{
        		LegalerResult = JSON.parseObject(legaler_result.getData().toString(),
                        LegalerResult.class);
        	}catch(Exception e){
        		logger.error("TransferReportController类的index方法获取法人主体列表时JSON数据转换不成功"+e.getMessage());
        	}
        	if(LegalerResult !=null)
        	{
	            String str = LegalerResult.getData();
	            ObjectMapper objMapper = new ObjectMapper();
	            try
	            {
	                legaler_resultMap = objMapper.readValue(str, HashMap.class);
	            }
	            catch (IOException e)
	            {
	                logger.error("ObjectMapper对象转换不成功");
	            }
        	}
        }
        modelMap.addAttribute("legalers", legaler_resultMap);

        // 获取仓库名称列表
        List<InventoryVO> inventoryV_list = new ArrayList<InventoryVO>();
        AjaxResponse inventory_name_result = HttpUtil.doGet(inventory_name_list_url);
        if (inventory_name_result.getData() != null)
        {	
        	InventoryResult inventoryResult = null;
        	try{
        		inventoryResult = JSON.parseObject(inventory_name_result.getData().toString(),
                        InventoryResult.class);
        	}catch(Exception e){
        		logger.error("TransferReportController类的index方法获取仓库名时JSON数据转换不成功"+e.getMessage());
        	}
            
        	if(inventoryResult !=null){
        		 List<InventoryVO> inventoryV_list_all = inventoryResult.getList();
                 for (InventoryVO vo : inventoryV_list_all)
                 {
                     // if (whs.contains(vo.getStock_id())) {
                     // inventoryV_list.add(vo);
                     // }
                     inventoryV_list.add(vo);
                 }
        	}
           
        }
        modelMap.addAttribute("warehouses", inventoryV_list);
        // 获取中转仓列表
        List<InventoryVO> transferWarehouses = new ArrayList<InventoryVO>();
        AjaxResponse transferName = HttpUtil.doGet(transfer_name_list_url);
        if (transferName.getData() != null)
        {
        	InventoryResult transferResult =null;
        	try{
        		transferResult = JSON.parseObject(transferName.getData().toString(),
                        InventoryResult.class);
        	}catch(Exception e){
        		logger.error("TransferReportController类的index方法获取中转仓列表时JSON数据转换不成功"+e.getMessage());
        	}
        	
        	if(transferResult !=null){
	    	     List<InventoryVO> transferList = transferResult.getList();
	             for (InventoryVO vo : transferList)
	             {
	                 transferWarehouses.add(vo);
	             }
        	}
        }
        modelMap.addAttribute("transfer", transferWarehouses);
        return "transferReport";
    }

    @RequestMapping("/search")
    @ResponseBody
    public TableData<TransferReportVO> search(TransferReportParam param,
        @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
        @RequestParam(value = "limit", defaultValue = "10") int limit)
    {
        PageParam pageParam = new PageParam(pageNumber, limit);
        TableData<TransferReportVO> result = transferReportService.listPage(pageParam, param);
        return result;
    }

    @RequestMapping("/preExport")
    @ResponseBody
    public AjaxResponse preExport(TransferReportParam param, HttpServletRequest request, HttpServletResponse response)
    {
        int count = transferReportService.count(param);
        if (count > exportMaxNumber)
        {
            return new AjaxResponse().failure("导出的数据量太大，请选择过滤条件");
        }
        return new AjaxResponse();
    }

    @RequestMapping("/export")
    public String export(RedirectAttributes attr, TransferReportParam param, HttpServletRequest request,
        HttpServletResponse response)
    {
        PageParam pagePara = new PageParam(1, exportMaxNumber);
        TableData<TransferReportVO> pageResult = transferReportService.listPage(pagePara, param);
        long total = pageResult.getTotal();
        if (total > MAX_ROW)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    logger.info("-----------开始生成Excel----------------------");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    List<TransferReportVO> rows = pageResult.getRows();
                    TransferReportExcel payExcel = new TransferReportExcel();
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("调拨明细报表");// 创建一个工作表
                    OutputStream out;
                    List<File> fileUrls = new ArrayList<>();

                    int page = 1;
                    int fromIndex = 0;
                    int toIndex = MAX_ROW;

                    String fileUrl = new File("").getAbsolutePath() + File.separator + "调拨明细报表"
                        + DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + "[" + fromIndex + "," + toIndex + "].xls";
                    fileUrls.add(new File(fileUrl));
                    payExcel.setSheetTitle(workbook, sheet);
                    try
                    {
                        out = new FileOutputStream(fileUrl);
                        while (fromIndex < total)
                        {
                            if (fromIndex % MAX_ROW == 0 && fromIndex != 0)
                            {
                                toIndex = fromIndex + MAX_ROW;
                                if (toIndex > total)
                                {
                                    toIndex = Integer.parseInt(total + "");
                                }
                                fileUrl = new File("").getAbsolutePath() + File.separator + "调拨明细报表"
                                    + DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + "[" + fromIndex + "-"
                                    + toIndex + "].xls";
                                out = new FileOutputStream(fileUrl);
                                fileUrls.add(new File(fileUrl));
                                workbook = new HSSFWorkbook();
                                sheet = workbook.createSheet("调拨明细报表");
                                payExcel.setSheetTitle(workbook, sheet);
                                page++;
                            }
                            payExcel.appendSheetRow(workbook, sheet, 1, rows.subList(fromIndex, toIndex));
                            logger.info("当前为第[" + page + "]张，位置为：[" + fromIndex + "," + toIndex + "]");

                            workbook.write(out);
                            workbook.close();
                            if (out != null)
                            {
                                try
                                {
                                    out.flush();
                                    out.close();
                                    out = null;
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            fromIndex = toIndex;
                        }
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    logger.info("-----------Excel生成结束----------------------");

                    try
                    {
                        logger.info("-----------开始压缩----------------------");
                        String userAccount = RoleUtil.getUserAccount();
                        String zipPath = new File("").getAbsolutePath() + File.separator + "调拨明细报表"
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
                        email.setSubject("调拨明细报表");
                        email.setFileName(zipPath);
                        email.setTo(userAccount);
                        // email.setTo("weiadi@aukeys.com");
                        email.setText("调拨明细报表数据。本邮件由系统自动发送，请勿回复！");
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
            /*
             * ModelAndView mv = new ModelAndView("transferReport"); return mv;
             */
            return "redirect:index";
        }
        else
        {
            try
            {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String filename = "商品收发明细报表_" + sdf.format(new Date());
                // 解决不同浏览器文件名显示乱码的问题
                String userAgent = request.getHeader("USER-AGENT");
                if (StringUtils.contains(userAgent, "MSIE"))
                {// IE浏览器
                    filename = URLEncoder.encode(filename, "UTF8");
                }
                else if (StringUtils.contains(userAgent, "Mozilla"))
                {// google,火狐浏览器
                    filename = new String(filename.getBytes(), "ISO8859-1");
                }
                else
                {
                    filename = URLEncoder.encode(filename, "UTF8");// 其他浏览器
                }
                response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
                OutputStream os = response.getOutputStream();

                int columns = 35;
                DataField[] dataFields = new DataField[columns];

                dataFields[0] = new DataField("序号", "no");
                dataFields[1] = new DataField("调拔单号", "transfer_no");
                dataFields[2] = new DataField("单据日期", "transfer_date");
                dataFields[3] = new DataField("法人主体", "legal_name");
                dataFields[4] = new DataField("出库日期", "outtime");
                dataFields[5] = new DataField("调出仓", "out_warehouse_name");
                dataFields[6] = new DataField("线路仓", "pass_warehouse_name");
                dataFields[7] = new DataField("目标仓", "target_warehouse_name");
                dataFields[8] = new DataField("SKU", "sku");
                dataFields[9] = new DataField("SKU名称", "sku_name");

                dataFields[10] = new DataField("箱号", "box_no");
                dataFields[11] = new DataField("单箱数量", "box_count");
                dataFields[12] = new DataField("运输方式", "transport_type");
                dataFields[13] = new DataField("货运毛重", "actual_weight");

                dataFields[14] = new DataField("长", "box_grow");
                dataFields[15] = new DataField("宽", "box_broad");
                dataFields[16] = new DataField("高", "box_height");
                dataFields[17] = new DataField("体积", "box_volume");

                dataFields[18] = new DataField("调拔状态", "transfer_status");
                dataFields[19] = new DataField("是否含税", "is_tax");
                dataFields[20] = new DataField("站点", "site_name");
                dataFields[21] = new DataField("店铺", "account_name");
                dataFields[22] = new DataField("shipment_id", "shipment_id");
                dataFields[23] = new DataField("fnsku", "fnsku");
                dataFields[24] = new DataField("sellersku", "sellersku");
                dataFields[25] = new DataField("采购金额", "money");
                dataFields[26] = new DataField("税率", "tax_rate");
                dataFields[27] = new DataField("退税率", "return_tax");
                dataFields[28] = new DataField("报关单号", "declare_order_id");
                dataFields[29] = new DataField("出口日期", "declare_order_date");
                dataFields[30] = new DataField("海关单号", "customs_number");
                dataFields[31] = new DataField("采购单价", "unit_price");
                dataFields[32] = new DataField("报关金额", "declare_money");
                dataFields[33] = new DataField("币种", "currency");
                dataFields[34] = new DataField("项号", "num");

                DataPage pageParam = new DataPage(60000);
                new ExcelDataExportor<Object>(pageParam, dataFields, new ExportDataSource<Object>()
                {
                    @Override
                    public List getData()
                    {
                        PageParam pageParam = new PageParam(1, exportMaxNumber);
                        TableData<TransferReportVO> pageResult = transferReportService.listPage(pageParam, param);

                        List<TransferReportVO> list = pageResult.getRows();
                        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
                        if (!list.isEmpty())
                        {
                            int temp = 0;
                            for (int i = 0; i < list.size(); i++)
                            {
                                Map<String, Object> mapParam = new HashMap<String, Object>();
                                TransferReportVO vo = (TransferReportVO) list.get(i);
                                mapParam.put("no", ++temp);
                                mapParam.put("transfer_no", vo.getTransfer_no());
                                mapParam.put("transfer_date", vo.getTransfer_date());
                                mapParam.put("legal_name", vo.getLegal_name());
                                mapParam.put("outtime", vo.getOuttime());
                                mapParam.put("out_warehouse_name", vo.getOut_warehouse_name());
                                mapParam.put("pass_warehouse_name", vo.getPass_warehouse_name());
                                mapParam.put("target_warehouse_name", vo.getTarget_warehouse_name());
                                mapParam.put("sku", vo.getSku());
                                mapParam.put("sku_name", vo.getSku_name());
                                mapParam.put("box_no", vo.getBox_no());
                                mapParam.put("box_count", vo.getBox_count());
                                mapParam.put("transport_type", vo.getTransport_type());
                                mapParam.put("actual_weight", vo.getActual_weight());
                                mapParam.put("box_grow", vo.getBox_grow());
                                mapParam.put("box_broad", vo.getBox_broad());
                                mapParam.put("box_height", vo.getBox_height());
                                mapParam.put("box_volume", vo.getBox_volume());
                                mapParam.put("transfer_status", vo.getTransfer_status());
                                mapParam.put("is_tax", vo.getIs_tax());
                                mapParam.put("site_name", vo.getSite_name());

                                mapParam.put("account_name", vo.getAccount_name());
                                mapParam.put("shipment_id", vo.getShipment_id());
                                mapParam.put("fnsku", vo.getFnsku());
                                mapParam.put("sellersku", vo.getSellersku());
                                mapParam.put("money", vo.getMoney());
                                mapParam.put("tax_rate", vo.getTax_rate());
                                mapParam.put("return_tax", vo.getReturn_tax());
                                mapParam.put("declare_order_id", vo.getDeclare_order_id());
                                mapParam.put("declare_order_date", vo.getDeclare_order_date());
                                mapParam.put("customs_number", vo.getCustoms_number());
                                mapParam.put("unit_price", vo.getUnit_price());
                                mapParam.put("declare_money", vo.getDeclare_money());
                                mapParam.put("currency", vo.getCurrency());
                                mapParam.put("num", vo.getNum());
                                lists.add(mapParam);
                            }
                        }
                        return lists;
                    }
                }, os, MODE.EXCEL).export();
            }
            catch (UnsupportedEncodingException e)
            {
                logger.error("下载报表", e);
            }
            catch (IOException e)
            {
                logger.error("IO", e);
            }
            /*
             * ModelAndView mv = new ModelAndView("transferReport"); return mv;
             */
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
