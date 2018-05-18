package com.aukey.report.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.aukey.report.base.BaseController;
import com.aukey.report.domain.StrictSupplier;
import com.aukey.report.domain.base.Result;
import com.aukey.report.dto.DeclareReportParam;
import com.aukey.report.service.DeclareReportService;
import com.aukey.report.utils.export.DataField;
import com.aukey.report.utils.export.DataPage;
import com.aukey.report.utils.export.ExportDataSource;
import com.aukey.report.utils.export.excel.ExcelDataExportor;
import com.aukey.report.utils.export.excel.MODE;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.DeclareItemVO;
import com.aukey.report.vo.DeclareReportVO;
import com.aukey.report.vo.DepartmentResult;
import com.aukey.report.vo.DepartmentResult.DepartmentVO;
import com.aukey.report.vo.LegalerResult;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CommonUtil;
import com.aukey.util.HttpUtil;
import com.aukey.util.URLBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 采购报关报表
 * 
 * @author xiehz
 *
 */
@RequestMapping("/report/declare")
@Controller
public class DeclareReportController extends BaseController {

	private Logger logger = Logger.getLogger(getClass());

	@Value("${inventory.api.url}")
	private String inventory_name_list_url;

	@Value("${legaler.api.url}")
	private String legaler_list_url;

	@Value("${purchase.department.api.url}")
	private String all_department_list_url;

	@Value("${department.api.url}")
	private String department_list_url;
	
	/**
     * * 供应商模糊搜索
     */
    @Value("${String.aliaslike.url}")
    private String aliaslikeUrl;

	@Autowired
	private DeclareReportService declareReportService;

	@RequestMapping("/index")
	public String index(ModelMap modelMap) {

		// 获取法人主体列表
		
	  try{
		  Map<String, Object> legaler_resultMap = null;
			AjaxResponse legaler_result = HttpUtil.doGet(legaler_list_url);
			if (legaler_result.getData() != null) {
				LegalerResult LegalerResult = JSON.parseObject(legaler_result.getData().toString(), LegalerResult.class);
				String str = LegalerResult.getData();
				ObjectMapper objMapper = new ObjectMapper();
				try {
					legaler_resultMap = objMapper.readValue(str, HashMap.class);
				} catch (IOException e) {
					logger.error("JSON格式转换有问题");
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
	  }catch(Exception e){
		  logger.error("采购报关报表报错", e);
		  e.printStackTrace();
	  }
		
		return "purchase_manage";
	}

	@RequestMapping("/search")
	@ResponseBody
	public TableData<DeclareReportVO> search(DeclareReportParam param, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "limit", defaultValue = "10") int limit) {
		PageParam pageParam = new PageParam(pageNumber, limit);
		
		TableData<DeclareReportVO> result = declareReportService.listPage(pageParam, param);
		createContract_number(result);
		return result;
	}

	@RequestMapping("/detail")
	@ResponseBody
	public TableData<DeclareItemVO> detail(DeclareReportParam param, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "limit", defaultValue = "10") int limit) {
		PageParam pageParam = new PageParam(pageNumber, limit);
		TableData<DeclareItemVO> result = declareReportService.listPage3(pageParam, param);
		return result;
	}

	
	
	//生成合同号
		private void createContract_number(TableData<DeclareReportVO> result){
			
			if(result != null && result.getRows() != null){
				
				result.getRows().stream().forEach(entity ->{  
					
								// 1为海运 大鹏海关  0为空运 深圳湾关
					 			String pot  = entity.getPort();//运输方式名称
					 			
					 			String seller = entity.getLegaler_name();//法人主体
					 			
					 			StringBuffer declareOrderId = new StringBuffer(entity.getRelated_no());//报关单号
					 			String contactNo = entity.getContract_no();
					 			
					            String result1 = "-";//最后的展示结果
					           
					            if(contactNo!=null && !"".equals(contactNo)){
					            	result1 = contactNo;
					            }else{
					        	  if(pot!=null && !"".equals(pot)
					        			  	&& seller!=null && !"".equals(seller)
					        			  	&& declareOrderId!=null && !"".equals(declareOrderId)){
					        		  String ends =  declareOrderId.substring(declareOrderId.length()-4,declareOrderId.length());//截取最后四个字符串
					                   String  starts = declareOrderId.substring(2,8);//截取开始字符串  除去'GD'
					                   pot = "大鹏海关".equals(pot)?"H":"深圳湾关".equals(pot)?"K":"H";//运输方式标志
					                  if(seller.indexOf("傲基")>-1){
					                	  result1 = "AJ";
					                  }else if(seller.indexOf("傲科海")>-1){
					                	  result1 = "AKH";
					                  }else if(seller.indexOf("傲视")>-1){
					                	  result1 = "AS";
					                  }else if(seller.indexOf("品隆")>-1){
					                	  result1 = "PL";
					                  }else{
					                	  result1 = "GD";//默认显示报关单
					                  }
					                  result1 += starts + pot + ends;
					              }
					          }
					          
					          //设置报关合同号
					          entity.setContract_no(result1);
					
					
			    }); 
				
			}
			
			
		}
		


	@RequestMapping("/preExport")
	@ResponseBody
	public AjaxResponse preExport(DeclareReportParam param, HttpServletRequest request, HttpServletResponse response) {
		int count = declareReportService.count(param);
		if (count > exportMaxNumber) {
			return new AjaxResponse().failure("导出的数据量太大，请选择过滤条件");
		}
		return new AjaxResponse();
	}
	
	@RequestMapping("/declareInvoiceExport")
	public @ResponseBody void declareInvoiceExport(DeclareReportParam params, HttpServletRequest request, HttpServletResponse response) {
		try {
			//部分开票可能是 全部开票和未开票的组合，会导致查询不到数据，于是取消“部分开票”过滤条件
			if(params.getDeclare_status().equals("部分开票")){
				params.setDeclare_status("-1");
			}
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String filename = "采购报关报表_" + sdf.format(new Date());
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));			
			response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题  
			response.setCharacterEncoding("utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");  
			OutputStream os = response.getOutputStream();
			int columns = 36;
			DataField[] dataFields = createDataField(columns);	
			DataPage pageParam = new DataPage(60000);
			new ExcelDataExportor<Object>(pageParam, dataFields, new ExportDataSource<Object>() {
				@Override
				public List getData() {
					PageParam pageParam = new PageParam(1, exportMaxNumber);
					TableData<DeclareReportVO> pageResult = declareReportService.queryDeclareInvoice(pageParam, params);
					//修改合同号
					createContract_number(pageResult);
					List<DeclareReportVO> list = pageResult.getRows();
					List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
					if (!list.isEmpty()) {
						int temp = 0;
						for (int i = 0; i < list.size(); i++) {
							Map<String, Object> mapParam = new HashMap<String, Object>();
							DeclareReportVO vo = (DeclareReportVO) list.get(i);
							mapParam.put("no", ++temp);
							mapParam.put("legaler_name", vo.getLegaler_name());
							mapParam.put("related_no", vo.getRelated_no());
							mapParam.put("declare_status", vo.getDeclare_status());
							mapParam.put("sign_date", vo.getSign_date());
							mapParam.put("declare_date", vo.getDeclare_date());
							mapParam.put("export_date", vo.getExport_date());
							mapParam.put("declare_no", vo.getDeclare_no());
							mapParam.put("contract_no", vo.getContract_no());
							mapParam.put("port", vo.getPort());
							mapParam.put("item_no", vo.getItem_no());
							mapParam.put("customs_no", vo.getCustoms_no());
							mapParam.put("transaction_count", vo.getTransaction_count());
							mapParam.put("transaction_unit", vo.getTransaction_unit());
							mapParam.put("price_tax", vo.getPrice_tax());
							mapParam.put("total_price_tax", vo.getTotal_price_tax());
							mapParam.put("total_usd_tax", vo.getTotal_usd_tax());
							mapParam.put("purchase_no", vo.getPurchase_no());
							mapParam.put("sku", vo.getSku());
							mapParam.put("category", vo.getCategory());
							mapParam.put("buyer_name", vo.getBuyer_name());
							mapParam.put("department_name", vo.getDepartment_name());
							mapParam.put("supplier_name", vo.getSupplier_name());
							mapParam.put("drawback_rate", vo.getDrawback_rate());
							mapParam.put("producer_name", vo.getProducer_name());
							mapParam.put("bill_count", vo.getBill_count());
							mapParam.put("bill_no_tax_sum", vo.getBill_no_tax_sum());
							mapParam.put("no_bill_count", vo.getNo_bill_count());
							mapParam.put("drawback_explain", vo.getDrawback_explain());
							mapParam.put("bill_month", vo.getBill_month());
							mapParam.put("bill_no", vo.getBill_no());

							mapParam.put("auditor_name", vo.getAuditor_name());
							mapParam.put("verifer_name", vo.getVerifer_name());
							mapParam.put("audit_time", vo.getAudit_time());
							mapParam.put("verif_time", vo.getVerif_time());
							mapParam.put("invoice_serial_number", vo.getInvoice_serial_number());
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
	}
	

	@RequestMapping("/export")
	public ModelAndView export(DeclareReportParam param, HttpServletRequest request, HttpServletResponse response) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String filename = "采购报关报表_" + sdf.format(new Date());
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
			
			response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题  
			response.setCharacterEncoding("utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");  
			OutputStream os = response.getOutputStream();

			int columns = 36;
			// if (RoleUtil.CanExportAll()) {
			// columns = 30;
			// }
			DataField[] dataFields = createDataField(columns);			

			// if (RoleUtil.CanExportAll()) {
			//
			// }
			DataPage pageParam = new DataPage(60000);
			new ExcelDataExportor<Object>(pageParam, dataFields, new ExportDataSource<Object>() {
				@Override
				public List getData() {
					PageParam pageParam = new PageParam(1, exportMaxNumber);

					TableData<DeclareReportVO> pageResult = declareReportService.listPage2(pageParam, param);
					//修改合同号
					createContract_number(pageResult);


					List<DeclareReportVO> list = pageResult.getRows();
					List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
					if (!list.isEmpty()) {
						int temp = 0;
						for (int i = 0; i < list.size(); i++) {
							Map<String, Object> mapParam = new HashMap<String, Object>();
							DeclareReportVO vo = (DeclareReportVO) list.get(i);
							mapParam.put("no", ++temp);
							mapParam.put("legaler_name", vo.getLegaler_name());
							mapParam.put("related_no", vo.getRelated_no());
							mapParam.put("declare_status", vo.getDeclare_status());
							mapParam.put("sign_date", vo.getSign_date());
							mapParam.put("declare_date", vo.getDeclare_date());
							mapParam.put("export_date", vo.getExport_date());
							mapParam.put("declare_no", vo.getDeclare_no());
							mapParam.put("contract_no", vo.getContract_no());
							mapParam.put("port", vo.getPort());
							mapParam.put("item_no", vo.getItem_no());
							mapParam.put("customs_no", vo.getCustoms_no());
							mapParam.put("transaction_count", vo.getTransaction_count());
							mapParam.put("transaction_unit", vo.getTransaction_unit());
							/*
							 * mapParam.put("price_wihtout_tax",
							 * vo.getPrice_wihtout_tax());
							 */
							mapParam.put("price_tax", vo.getPrice_tax());
							mapParam.put("total_price_tax", vo.getTotal_price_tax());
							mapParam.put("total_usd_tax", vo.getTotal_usd_tax());
							mapParam.put("purchase_no", vo.getPurchase_no());
							mapParam.put("sku", vo.getSku());
							mapParam.put("category", vo.getCategory());
							mapParam.put("buyer_name", vo.getBuyer_name());
							mapParam.put("department_name", vo.getDepartment_name());
							mapParam.put("supplier_name", vo.getSupplier_name());
							mapParam.put("drawback_rate", vo.getDrawback_rate());
							mapParam.put("producer_name", vo.getProducer_name());
							mapParam.put("bill_count", vo.getBill_count());
							mapParam.put("bill_no_tax_sum", vo.getBill_no_tax_sum());
							mapParam.put("no_bill_count", vo.getNo_bill_count());
							mapParam.put("drawback_explain", vo.getDrawback_explain());
							mapParam.put("bill_month", vo.getBill_month());
							mapParam.put("bill_no", vo.getBill_no());

							mapParam.put("auditor_name", vo.getAuditor_name());
							mapParam.put("verifer_name", vo.getVerifer_name());
							mapParam.put("audit_time", vo.getAudit_time());
							mapParam.put("verif_time", vo.getVerif_time());
							mapParam.put("invoice_serial_number", vo.getInvoice_serial_number());
							//
							// if (RoleUtil.CanExportAll()) {
							// mapParam.put("sum", vo.getSum());
							// mapParam.put("total_area", vo.getTotal_area());
							// }

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
		ModelAndView mv = new ModelAndView("purchase_manage");
		return mv;
	}
	
	private DataField[] createDataField(int columns){
		
		DataField[] dataFields = new DataField[columns];
		dataFields[0] = new DataField("序号", "no");
		dataFields[1] = new DataField("法人主体", "legaler_name");
		dataFields[2] = new DataField("关联编号", "related_no");
		dataFields[3] = new DataField("报关状态", "declare_status");
		dataFields[4] = new DataField("填单日期", "sign_date");
		dataFields[5] = new DataField("申报日期", "declare_date");
		dataFields[6] = new DataField("出口日期", "export_date");
		dataFields[7] = new DataField("报关单号", "declare_no");
		dataFields[8] = new DataField("报关合同号", "contract_no");
		dataFields[9] = new DataField("口岸", "port");
		dataFields[10] = new DataField("项号", "item_no");
		dataFields[11] = new DataField("海关编号", "customs_no");
		dataFields[12] = new DataField("发票月份", "bill_month");
		dataFields[13] = new DataField("成交数量", "transaction_count");
		dataFields[14] = new DataField("成交单位", "transaction_unit");
		/*
		 * dataFields[15] = new DataField("不含税单价RMB", "price_wihtout_tax");
		 */
		dataFields[15] = new DataField("单价RMB", "price_tax");

		dataFields[16] = new DataField("含税总价RMB", "total_price_tax");
		dataFields[17] = new DataField("报关总价USD", "total_usd_tax");
		dataFields[18] = new DataField("采购单号", "purchase_no");
		dataFields[19] = new DataField("SKU", "sku");
		dataFields[20] = new DataField("品类", "category");
		dataFields[21] = new DataField("采购员", "buyer_name");
		dataFields[22] = new DataField("采购部门", "department_name");
		dataFields[23] = new DataField("供应商", "supplier_name");
		dataFields[24] = new DataField("退税率", "drawback_rate");
		dataFields[25] = new DataField("制单人", "producer_name");
		dataFields[26] = new DataField("开票数量", "bill_count");
		dataFields[27] = new DataField("开票不含税金额", "bill_no_tax_sum");
		dataFields[28] = new DataField("未开票数量", "no_bill_count");
		dataFields[29] = new DataField("退税说明", "drawback_explain");
		dataFields[30] = new DataField("发票号", "bill_no");

		dataFields[31] = new DataField("退税审核员", "auditor_name");
		dataFields[32] = new DataField("退税核验员", "verifer_name");
		dataFields[33] = new DataField("审核时间", "audit_time");
		dataFields[34] = new DataField("核验时间", "verif_time");
		dataFields[35] = new DataField("发票编码", "invoice_serial_number");
		return dataFields;
	}
	
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

	
}
