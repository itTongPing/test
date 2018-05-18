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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.aukey.report.base.BaseController;
import com.aukey.report.dto.DeclareReportParam;
import com.aukey.report.dto.InventoryReportParam;
import com.aukey.report.service.TestService;
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
import com.aukey.report.vo.InventoryReportVO;
import com.aukey.util.AjaxResponse;
import com.aukey.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 库存查询报表
 * 
 * @author xiehz
 *
 */
@RequestMapping("/report/inventory")
@Controller
public class InventoryReportController extends BaseController {

	private Logger logger = Logger.getLogger(getClass());

	@Value("${inventory.api.url}")
	private String inventory_name_list_url;

	@Value("${legaler.api.url}")
	private String legaler_list_url;

	@Value("${all.department.api.url}")
	private String all_department_list_url;

	@Value("${department.api.url}")
	private String department_list_url;

	@Autowired
	private TestService testService;

	@RequestMapping("/index")
	public String index(ModelMap modelMap) {
		// 获取运输方式
		List<Map<String, String>> transport_type_list = new ArrayList<Map<String, String>>();
		Map<String, String> transport_type_item1 = new HashMap<String, String>();
		transport_type_item1.put("name", "空运");
		transport_type_item1.put("value", "0");
		transport_type_list.add(transport_type_item1);
		Map<String, String> transport_type_item2 = new HashMap<String, String>();
		transport_type_item2.put("name", "海运");
		transport_type_item2.put("value", "1");
		transport_type_list.add(transport_type_item2);
		Map<String, String> transport_type_item3 = new HashMap<String, String>();
		transport_type_item3.put("name", "无");
		transport_type_item3.put("value", "2");
		transport_type_list.add(transport_type_item3);
		Map<String, String> transport_type_item4 = new HashMap<String, String>();
		transport_type_item4.put("name", "铁运");
		transport_type_item4.put("value", "3");
		transport_type_list.add(transport_type_item4);

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

		modelMap.addAttribute("transport", transport_type_list);
		modelMap.addAttribute("tax", include_tax_list);

		

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
				logger.error("ObjectMapper对象转换不成功");
			}
		}
		modelMap.addAttribute("legalers", legaler_resultMap);

		// 获取国家列表
		// List<Country> countryList = countryService.getCountryName(new
		// HashMap<String, Object>());
		// modelMap.addAttribute("countrys", countryList);
		
		
//		List<Integer> whs = RoleUtil.getWareHouseIds();
//		List<Integer> dpts = RoleUtil.getSalesGroupIds();
		
		// 获取仓库名称列表
		List<InventoryVO> inventoryV_list = new ArrayList<InventoryVO>();
		AjaxResponse inventory_name_result = HttpUtil.doGet(inventory_name_list_url);
		if (inventory_name_result.getData() != null) {
			InventoryResult inventoryResult = JSON.parseObject(inventory_name_result.getData().toString(), InventoryResult.class);
			List<InventoryVO> inventoryV_list_all = inventoryResult.getList();
			for (InventoryVO vo : inventoryV_list_all) {
//				if (whs.contains(vo.getStock_id())) {
//					inventoryV_list.add(vo);
//				}
				inventoryV_list.add(vo);
			}
		}
		modelMap.addAttribute("inventorys", inventoryV_list);
		
		// 获取需求部门列表
		List<DepartmentVO> department_list = new ArrayList<DepartmentVO>();
		AjaxResponse all_department_result = HttpUtil.doGet(all_department_list_url);
		if (all_department_result.getData() != null) {
			DepartmentResult DepartmentResult = JSON.parseObject(all_department_result.getData().toString(), DepartmentResult.class);
			department_list = DepartmentResult.getData();
		}
		modelMap.addAttribute("departments", department_list);
		return "index";
	}

	@RequestMapping("/search")
	@ResponseBody
	public TableData<InventoryReportVO> search(InventoryReportParam param, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "limit", defaultValue = "10") int limit) {
		PageParam pageParam = new PageParam(pageNumber, limit);
		TableData<InventoryReportVO> result = testService.listPage(pageParam, param);
		return result;
	}
	
	@RequestMapping("/preExport")
	@ResponseBody
	public AjaxResponse preExport(InventoryReportParam param, HttpServletRequest request, HttpServletResponse response) {
		int count = testService.count(param);
		if (count > exportMaxNumber) {
			return new AjaxResponse().failure("导出的数据量太大，请选择过滤条件");
		}
		return new AjaxResponse();
	}

	@RequestMapping("/export")
	public ModelAndView export(InventoryReportParam param, HttpServletRequest request, HttpServletResponse response) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String filename = "库存查询报表_" + sdf.format(new Date());
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
			OutputStream os = response.getOutputStream();
			
			int columns = 15;
			if (RoleUtil.CanExportAll()) {
				columns = 17;
			}
			DataField[] dataFields = new DataField[columns];

			dataFields[0] = new DataField("序号", "no");
			dataFields[1] = new DataField("法人主体", "legaler");
			dataFields[2] = new DataField("需求部门", "department");
			dataFields[3] = new DataField("SKU", "sku");
			dataFields[4] = new DataField("SKU名称", "sku_name");
			dataFields[5] = new DataField("品类", "category");
			dataFields[6] = new DataField("仓库名称", "inventory_name");
			dataFields[7] = new DataField("入库数量", "count_in");
			dataFields[8] = new DataField("出库数量", "count_out");
			dataFields[9] = new DataField("占用库存", "usage_inventory");
			dataFields[10] = new DataField("调拔在途库存", "way_inventory");
			dataFields[11] = new DataField("可用库存", "available_inventory");
			dataFields[12] = new DataField("实际库存", "actual_inventory");
			dataFields[13] = new DataField("是否含税", "include_tax");
			dataFields[14] = new DataField("运输方式", "transport_type");
			
//			dataFields[15] = new DataField("国家", "country");

			if (RoleUtil.CanExportAll()) {
				dataFields[15] = new DataField("金额", "sum");
				dataFields[16] = new DataField("总体积", "total_area");
			}
			DataPage pageParam = new DataPage(60000);
			new ExcelDataExportor<Object>(pageParam, dataFields, new ExportDataSource<Object>() {
				@Override
				public List getData() {
					PageParam pageParam = new PageParam(1, exportMaxNumber);
					TableData<InventoryReportVO> pageResult = testService.listPage(pageParam, param);

					List<InventoryReportVO> list = pageResult.getRows();
					List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
					if (!list.isEmpty()) {
						int temp = 0;
						for (int i = 0; i < list.size(); i++) {
							Map<String, Object> mapParam = new HashMap<String, Object>();
							InventoryReportVO vo = (InventoryReportVO) list.get(i);
							mapParam.put("no", ++temp);
							mapParam.put("legaler", vo.getLegaler());
							mapParam.put("department", vo.getDepartment());
//							mapParam.put("country", vo.getCountry());
							mapParam.put("sku", vo.getSku());
							mapParam.put("sku_name", vo.getSku_name());
							mapParam.put("category", vo.getCategory());
							mapParam.put("inventory_name", vo.getInventory_name());
							mapParam.put("count_in", vo.getCount_in());
							mapParam.put("count_out", vo.getCount_out());
							mapParam.put("usage_inventory", vo.getUsage_inventory());
							mapParam.put("way_inventory", vo.getWay_inventory());
							mapParam.put("available_inventory", vo.getAvailable_inventory());
							mapParam.put("actual_inventory", vo.getActual_inventory());
							mapParam.put("include_tax", vo.getInclude_tax());
							mapParam.put("transport_type", vo.getTransport_type());
							
							if (RoleUtil.CanExportAll()) {
								mapParam.put("sum", vo.getSum());
								mapParam.put("total_area", vo.getTotal_area());
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
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}

}
