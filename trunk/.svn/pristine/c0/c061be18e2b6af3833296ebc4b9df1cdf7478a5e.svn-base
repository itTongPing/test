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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.aukey.report.base.BaseController;
import com.aukey.report.domain.Email;
import com.aukey.report.domain.base.Result;
import com.aukey.report.dto.GoodReportParam;
import com.aukey.report.dto.PurchaseExeReportParam;
import com.aukey.report.service.PurchaseExeReportService;
import com.aukey.report.service.SendEmailService;
import com.aukey.report.utils.PurchaseReportExcel;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.export.DataField;
import com.aukey.report.utils.export.DataPage;
import com.aukey.report.utils.export.ExportDataSource;
import com.aukey.report.utils.export.excel.ExcelDataExportor;
import com.aukey.report.utils.export.excel.MODE;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.DepartmentResult;
import com.aukey.report.vo.DepartmentResult.DepartmentVO;
import com.aukey.report.vo.InventoryResult;
import com.aukey.report.vo.InventoryResult.InventoryVO;
import com.aukey.report.vo.LegalerResult;
import com.aukey.report.vo.PurchaseExeReportVO;
import com.aukey.util.AjaxResponse;
import com.aukey.util.DateUtil;
import com.aukey.util.HttpUtil;
import com.aukey.util.ThreadPool;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 采购执行汇总报表
 * 
 * @author xiehz
 *
 */
@RequestMapping("/report/purchase/exe")
@Controller
public class PurchaseExeReportController extends BaseController {

	private Logger logger = Logger.getLogger(getClass());

	@Value("${legaler.api.url}")
	private String legaler_list_url;

	@Value("${purchase.department.api.url}")
	private String all_department_list_url;

	@Value("${inventory.api.url}")
	private String inventory_name_list_url;
	
	@Value("${spring.mail.username}")
    private String from;
	
	private static int MAX_ROW = 60000;

	@Autowired
	private PurchaseExeReportService purchaseExeReportService;
	
	@Resource
    private SendEmailService sendEmailService;

	@RequestMapping("/index")
	public String index(ModelMap modelMap) {

		// 获取法人主体列表
		Map<String, Object> legaler_resultMap = null;
		AjaxResponse legaler_result = HttpUtil.doGet(legaler_list_url);
		if (legaler_result.getData() != null) {
			LegalerResult LegalerResult = JSON.parseObject(legaler_result.getData().toString(), LegalerResult.class);
			String str = LegalerResult.getData();
			ObjectMapper objMapper = new ObjectMapper();
			try {
				legaler_resultMap = objMapper.readValue(str, HashMap.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		modelMap.addAttribute("legalers", legaler_resultMap);

		// 获取需求部门列表
		List<DepartmentVO> department_list = new ArrayList<DepartmentVO>();
		AjaxResponse all_department_result = HttpUtil.doGet(all_department_list_url);
		if (all_department_result.getData() != null) {
			DepartmentResult DepartmentResult = JSON.parseObject(all_department_result.getData().toString(), DepartmentResult.class);
			department_list = DepartmentResult.getData();
		}
		modelMap.addAttribute("departments", department_list);

		// 获取仓库名称列表
		List<InventoryVO> inventoryV_list = new ArrayList<InventoryVO>();
		AjaxResponse inventory_name_result = HttpUtil.doGet(inventory_name_list_url);
		if (inventory_name_result.getData() != null) {
			try {
				InventoryResult inventoryResult = JSON.parseObject(inventory_name_result.getData().toString(), InventoryResult.class);
				List<InventoryVO> inventoryV_list_all = inventoryResult.getList();
				for (InventoryVO vo : inventoryV_list_all) {
					// if (whs.contains(vo.getStock_id())) {
					// inventoryV_list.add(vo);
					// }
					inventoryV_list.add(vo);
				}
			} catch (Exception e) {
				
				logger.error("JSON格式转换出现问题");
			}
		}
		modelMap.addAttribute("inventorys", inventoryV_list);
		return "purchaseAllReport";
	}

	@RequestMapping("/search")
	@ResponseBody
	public TableData<PurchaseExeReportVO> search(PurchaseExeReportParam param, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "limit", defaultValue = "10") int limit) {
		PageParam pageParam = new PageParam(pageNumber, limit);
		TableData<PurchaseExeReportVO> result = purchaseExeReportService.listPage(pageParam, param);
		return result;
	}
	
	@RequestMapping("/preExport")
	@ResponseBody
	public AjaxResponse preExport(PurchaseExeReportParam param, HttpServletRequest request, HttpServletResponse response) {
		int count = purchaseExeReportService.count(param);
		if (count > exportMaxNumber) {
			return new AjaxResponse().failure("导出的数据量太大，请选择过滤条件");
		}
		return new AjaxResponse();
	}

	@RequestMapping("/export")
	@ResponseBody
	public Result export(RedirectAttributes attr,PurchaseExeReportParam param, HttpServletRequest request, HttpServletResponse response) {
		Result results = new Result();
        results.setErrCode(0);
		try {
			int total = purchaseExeReportService.count(param);
			if(total > MAX_ROW){
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				Runnable runnable = new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						logger.info("-----------开始生成Excel----------------------");
						SecurityContextHolder.getContext().setAuthentication(authentication);
						PageParam pageParam = new PageParam(1, exportMaxNumber);
						TableData<PurchaseExeReportVO> pageResult = purchaseExeReportService.listPage(pageParam, param);
					
						List<PurchaseExeReportVO> rows = pageResult.getRows();
						PurchaseReportExcel payExcel = new PurchaseReportExcel();
						HSSFWorkbook workbook = new HSSFWorkbook();
						HSSFSheet sheet = workbook.createSheet("采购执行汇总报表");// 创建一个工作表
						OutputStream out;
						List<File> fileUrls = new ArrayList<>();
						
						int page = 1;
						int fromIndex = 0;
						int toIndex = MAX_ROW;
						String fileUrl = new File("").getAbsolutePath() + File.separator + "采购执行汇总报表"
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
			            			fileUrl = new File("").getAbsolutePath() + File.separator + "采购执行汇总报表"
											+ DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss") + "[" + fromIndex + "-" + toIndex
											+ "].xls";
			            			out = new FileOutputStream(fileUrl);
									fileUrls.add(new File(fileUrl));
									workbook = new HSSFWorkbook();
									sheet = workbook.createSheet("采购执行汇总报表");
									payExcel.setSheetTitle(workbook, sheet);
									page++;
			            			
								}
								payExcel.appendSheetRow(workbook, sheet, 1, rows.subList(fromIndex, toIndex));
								logger.info("当前为第[" + page + "]张，位置为：[" + fromIndex + "," + toIndex + "]");
								
								workbook.write(out);
								workbook.close();
								if (out != null){
									try {
										out.flush();
										out.close();
										out = null;
									} catch (Exception e) {
										// TODO Auto-generated catch block
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
							String zipPath = new File("").getAbsolutePath() + File.separator + "采购执行汇总报表"
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
			                    email.setSubject("采购执行汇总报表");
			                    email.setFileName(zipPath);
			                    email.setTo(userAccount);
			                    //email.setTo("weiadi@aukeys.com");
			                    email.setText("采购执行汇总报表。本邮件由系统自动发送，请勿回复！");
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
				try {
					String msg = URLEncoder.encode("导出成功,导出文件大于6万的会发送至邮箱，请稍等查看！", "UTF-8");
					attr.addFlashAttribute("msg", msg);
					results.setErrCode(1);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// return "redirect:index";
				
			}else{
				try {
					final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String filename = "采购执行汇总报表_" + sdf.format(new Date());
					response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
					OutputStream os = response.getOutputStream();

					int columns = 21;
					DataField[] dataFields = new DataField[columns];

					dataFields[0] = new DataField("序号", "no");
					dataFields[1] = new DataField("供应商名称", "supplier_name");
					dataFields[2] = new DataField("法人主体", "legaler_name");
					dataFields[3] = new DataField("采购单号", "purchase_no");
					dataFields[4] = new DataField("采购日期", "purchase_date");
					dataFields[5] = new DataField("采购金额", "purchase_amount_all");
					dataFields[6] = new DataField("采购币别", "purchase_currency");
					dataFields[7] = new DataField("采购员", "purchaser_name");
					dataFields[8] = new DataField("采购部门", "department_name");
					dataFields[9] = new DataField("入库仓库", "inventory_warehouse_name");

					dataFields[10] = new DataField("入库金额", "inventory_amount");
					dataFields[11] = new DataField("未入库金额", "un_inventory_amount");
					dataFields[12] = new DataField("入库状态", "inventory_status");
					dataFields[13] = new DataField("未付金额", "un_payment");

					dataFields[14] = new DataField("已付合计", "payment_all");
					dataFields[15] = new DataField("支付状态", "payment_status");
					dataFields[16] = new DataField("是否含税", "is_tax");
					dataFields[17] = new DataField("开票状态", "bill_status");

					dataFields[18] = new DataField("未开票金额", "un_bill_amount");
					dataFields[19] = new DataField("已开票金额", "bill_amount");
					dataFields[20] = new DataField("开票合同", "bill_contract");

					DataPage pageParam = new DataPage(60000);
					new ExcelDataExportor<Object>(pageParam, dataFields, new ExportDataSource<Object>() {
						@Override
						public List getData() {
							PageParam pageParam = new PageParam(1, exportMaxNumber);
							TableData<PurchaseExeReportVO> pageResult = purchaseExeReportService.listPage(pageParam, param);

							List<PurchaseExeReportVO> list = pageResult.getRows();
							List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
							if (!list.isEmpty()) {
								int temp = 0;
								for (int i = 0; i < list.size(); i++) {
									Map<String, Object> mapParam = new HashMap<String, Object>();
									PurchaseExeReportVO vo = (PurchaseExeReportVO) list.get(i);
									mapParam.put("no", ++temp);
									mapParam.put("supplier_name", vo.getSupplier_name());
									mapParam.put("legaler_name", vo.getLegaler_name());
									mapParam.put("purchase_no", vo.getPurchase_no());
									mapParam.put("purchase_date", vo.getPurchase_date());
									mapParam.put("purchase_amount_all", vo.getPurchase_amount_all());
									mapParam.put("purchase_currency", vo.getPurchase_currency());
									mapParam.put("purchaser_name", vo.getPurchaser_name());
									mapParam.put("department_name", vo.getDepartment_name());
									mapParam.put("inventory_warehouse_name", vo.getInventory_warehouse_name());
									mapParam.put("inventory_amount", vo.getInventory_amount());
									mapParam.put("un_inventory_amount", vo.getUn_inventory_amount());
									mapParam.put("inventory_status", vo.getInventory_status());
									mapParam.put("un_payment", vo.getUn_payment());
									mapParam.put("payment_all", vo.getPayment_all());
									mapParam.put("payment_status", vo.getPayment_status());
									mapParam.put("is_tax", vo.getIs_tax());
									mapParam.put("bill_status", vo.getBill_status());
									mapParam.put("un_bill_amount", vo.getUn_bill_amount());
									mapParam.put("bill_amount", vo.getBill_amount());
									mapParam.put("bill_contract", vo.getBill_contract());
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
				//return "";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("导出采购执行报表", e);
			e.printStackTrace();
		}
		
		return results;
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
