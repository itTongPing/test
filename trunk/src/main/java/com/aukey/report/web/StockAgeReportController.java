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
import com.aukey.report.dto.StockAgeReportParam;
import com.aukey.report.service.StockAgeReportService;
import com.aukey.report.utils.export.DataField;
import com.aukey.report.utils.export.DataPage;
import com.aukey.report.utils.export.ExportDataSource;
import com.aukey.report.utils.export.excel.ExcelDataExportor;
import com.aukey.report.utils.export.excel.MODE;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.InventoryResult;
import com.aukey.report.vo.LegalerResult;
import com.aukey.report.vo.InventoryResult.InventoryVO;
import com.aukey.report.vo.StockAgeReportVO;
import com.aukey.util.AjaxResponse;
import com.aukey.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 库龄报表
 *
 * @author xiehz
 */
@RequestMapping("/report/stock/age")
@Controller
public class StockAgeReportController extends BaseController {

	private Logger logger = Logger.getLogger(getClass());

	@Value("${legaler.api.url}")
	private String legaler_list_url;

	@Value("${inventory.api.url}")
	private String inventory_name_list_url;

	@Autowired
	private StockAgeReportService stockAgeReportService;

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
				logger.error("ObjectMapper对象转换不成功");
			}
		}
		modelMap.addAttribute("legalers", legaler_resultMap);
		// 获取仓库名称列表
		List<InventoryVO> inventoryV_list = new ArrayList<InventoryVO>();
		AjaxResponse inventory_name_result = HttpUtil.doGet(inventory_name_list_url);
		if (inventory_name_result.getData() != null) {
			InventoryResult inventoryResult = null;
			try {
				inventoryResult = JSON.parseObject(inventory_name_result.getData().toString(), InventoryResult.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("JSON格式转换出了问题");
			}
			List<InventoryVO> inventoryV_list_all = null;
			if(inventoryResult != null){
				inventoryV_list_all = inventoryResult.getList();
				for (InventoryVO vo : inventoryV_list_all) {
					// if (whs.contains(vo.getStock_id())) {
					// inventoryV_list.add(vo);
					// }
					inventoryV_list.add(vo);
				}
				modelMap.addAttribute("warehouses", inventoryV_list);
			}
			
		}
		return "stockAgeReport";
	}

	@RequestMapping("/search")
	@ResponseBody
	public TableData<StockAgeReportVO> search(StockAgeReportParam param, @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "limit", defaultValue = "10") int limit) {
		PageParam pageParam = new PageParam(pageNumber, limit);
		TableData<StockAgeReportVO> result = stockAgeReportService.listPage(pageParam, param);
		return result;
	}

	@RequestMapping("/preExport")
	@ResponseBody
	public AjaxResponse preExport(StockAgeReportParam param, HttpServletRequest request, HttpServletResponse response) {
		int count = stockAgeReportService.count(param);
		if (count > exportMaxNumber) {
			return new AjaxResponse().failure("导出的数据量太大，请选择过滤条件");
		}
		return new AjaxResponse();
	}

	@RequestMapping("/export")
	public ModelAndView export(StockAgeReportParam param, HttpServletRequest request, HttpServletResponse response) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String filename = "库龄报表_" + sdf.format(new Date());
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
			OutputStream os = response.getOutputStream();

			int columns = 21;
			DataField[] dataFields = new DataField[columns];
			dataFields[0] = new DataField("事业部", "departName");
			dataFields[1] = new DataField("序号", "no");
			dataFields[2] = new DataField("SKU", "sku");
			dataFields[3] = new DataField("SKU名称", "sku_name");
			dataFields[4] = new DataField("品类名称", "category_name");
			dataFields[5] = new DataField("仓库名称", "warehouse_name");
			dataFields[6] = new DataField("库存数量", "stock_count");
			dataFields[7] = new DataField("标准单价", "price");
			dataFields[8] = new DataField("金额", "money");
			dataFields[9] = new DataField("法人主体", "legaler_name");
			dataFields[10] = new DataField(" 0-15天库龄", "age_0_15");
			dataFields[11] = new DataField("16-30天库龄", "age_15_30");

			dataFields[12] = new DataField("31-60天库龄", "age_30_60");
			dataFields[13] = new DataField("61-90天库龄", "age_60_90");
			dataFields[14] = new DataField("91-120天库龄", "age_90_120");
			dataFields[15] = new DataField("121-150天库龄", "age_120_150");
			dataFields[16] = new DataField("151-180天库龄", "age_150_180");

			dataFields[17] = new DataField("181-270天库龄", "age_180_270");
			dataFields[18] = new DataField(" 271-365天库龄", "age_270_365");
			dataFields[19] = new DataField("366-730天库龄", "age_365_730");
			dataFields[20] = new DataField("731天以上库龄", "age_731");

			DataPage pageParam = new DataPage(60000);
			new ExcelDataExportor<Object>(pageParam, dataFields, new ExportDataSource<Object>() {
				@Override
				public List getData() {
					PageParam pageParam = new PageParam(1, exportMaxNumber);
					TableData<StockAgeReportVO> pageResult = stockAgeReportService.listPage(pageParam, param);

					List<StockAgeReportVO> list = pageResult.getRows();
					List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
					if (!list.isEmpty()) {
						int temp = 0;
						for (int i = 0; i < list.size(); i++) {
							Map<String, Object> mapParam = new HashMap<String, Object>();
							StockAgeReportVO vo = (StockAgeReportVO) list.get(i);
							mapParam.put("departName", vo.getDepartName());
							mapParam.put("no", ++temp);
							mapParam.put("sku", vo.getSku());
							mapParam.put("sku_name", vo.getSku_name());
							mapParam.put("category_name", vo.getCategory_name());
							mapParam.put("warehouse_name", vo.getWarehouse_name());
							mapParam.put("stock_count", vo.getStock_count());
							mapParam.put("price", vo.getPrice());
							mapParam.put("money", vo.getMoney());
							mapParam.put("legaler_name",vo.getLegaler_name() );
							mapParam.put("age_0_15", vo.getAge_0_15());
							mapParam.put("age_15_30", vo.getAge_15_30());
							mapParam.put("age_30_60", vo.getAge_30_60());
							mapParam.put("age_60_90", vo.getAge_60_90());
							mapParam.put("age_90_120", vo.getAge_90_120());
							mapParam.put("age_120_150", vo.getAge_120_150());
							mapParam.put("age_150_180", vo.getAge_150_180());
							mapParam.put("age_180_270", vo.getAge_180_270());
							mapParam.put("age_270_365", vo.getAge_270_365());
							mapParam.put("age_365_730", vo.getAge_365_730());
							mapParam.put("age_731", vo.getAge_731());
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
		ModelAndView mv = new ModelAndView("stockAgeReport");
		return mv;
	}

}
