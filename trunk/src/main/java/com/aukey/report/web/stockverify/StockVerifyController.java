package com.aukey.report.web.stockverify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.aukey.report.domain.QcWarehouseInfo;
import com.aukey.report.domain.base.TableData;
import com.aukey.report.domain.stockverify.StockVerifyReport;
import com.aukey.report.service.stockverify.StockVerifyReportService;
import com.aukey.report.utils.WareHouseUtil;
import com.aukey.report.utils.export.stockverify.StockVerifyExport;
import com.aukey.util.CommonUtil;
import com.aukey.util.DateUtil;

@RequestMapping("/report/stockverify")
@Controller
public class StockVerifyController {
	
	
	/**
	 * 打印日志
	 */
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private StockVerifyReportService stockVerifyReportService;
	
	
	@Value("${product.warehouse.url}")
	private String warehouseUrl;
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
	{
		ModelAndView modelAndView = new ModelAndView("stockverify/stockVerifyReport");
		
		WareHouseUtil w = new WareHouseUtil();
		List<QcWarehouseInfo> warehouse = new ArrayList<QcWarehouseInfo>();
		try {
			warehouse = w.getWarehouse("11", null, warehouseUrl);
			//过滤易达仓
			warehouse = filterWareHouse(warehouse);
			logger.info(JSON.toJSONString(warehouse));
		} catch (Exception e) {
			logger.error("查询仓库异常", e);
			e.printStackTrace();
		}finally 
		{
			modelAndView.addObject("stockList", warehouse);
		}
		return modelAndView;
	}
	
	/**
	 * 查询
	 * @param pageNumber
	 * @param pageSize
	 * @param sku
	 * @param wsku
	 * @param stockId
	 * @return
	 */
	@RequestMapping("/search")
	@ResponseBody
	public TableData<StockVerifyReport> search(
			@RequestParam(value = "pageNumber", defaultValue = "1") String pageNumber, 
			@RequestParam(value = "pageSize", defaultValue = "20") String pageSize,
			@RequestParam(value = "sku", required=false) String sku,
			@RequestParam(value = "wsku", required=false) String wsku,
			@RequestParam(value = "stockIds", required=false) String stockIds)
	{
		TableData<StockVerifyReport> data = new TableData<StockVerifyReport>();
		Map<String,Object> map = new HashMap<String,Object>();
		setParam(map, sku, wsku, stockIds, pageSize, pageNumber,null);
		List<StockVerifyReport> selectAll=null;
		Integer count=0;
		try {
			selectAll = stockVerifyReportService.selectAll(map);
			count = stockVerifyReportService.selectAllCount(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		data.setRows(selectAll);
		data.setTotal(count);
		return data;
	}
	
	
	@RequestMapping("/export")
	public String export(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "sku", required=false) String sku,
			@RequestParam(value = "wsku", required=false) String wsku,
			@RequestParam(value = "stockIds", required=false) String stockIds,
			@RequestParam(value = "id", required=false) String id)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		setParam(map, sku, wsku, stockIds, null, null, id);
		List<StockVerifyReport> list = stockVerifyReportService.selectAll(map);
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("海外仓库存核实");
		StockVerifyExport stockVerifyExport = new StockVerifyExport();
		stockVerifyExport.setSheetTitle(workbook, sheet);
		stockVerifyExport.appendSheetRow(workbook, sheet, 1, list);
		String fileName = "海外仓库存核实报表"+DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss");
		try {
			response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题  
			response.setCharacterEncoding("utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1")+".xlsx");    	 
			workbook.write(response.getOutputStream());
			workbook.close();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			logger.error("海外仓库存核实报表导出失败", e);
			e.printStackTrace();
		}
		
		return "report/stockverify/index";
	}
	
	
	
	private void setParam(Map<String,Object> map,String sku,String wsku,String stockId,String pageSize,String pageNumber,String id)
	{
		
		if (CommonUtil.isNotEmpty(sku)) 
		{
			List<String> asList = Arrays.asList(sku.split(","));
			
			if(asList.size()>1)
			{
				map.put("skuCodeList",asList );
			}else
			{
				map.put("skuCode","%"+asList.get(0)+"%" );
			}
		}
		
		if (CommonUtil.isNotEmpty(wsku)) 
		{
			List<String> asList = Arrays.asList(wsku.split(","));
			
			if(asList.size()>1)
			{
				map.put("wskuList",asList );
			}else
			{
				map.put("wsku","%"+asList.get(0)+"%" );
			}
		}
		if (CommonUtil.isNotEmpty(stockId)&&!"-1".equals(stockId)) 
		{
			List<String> asList = Arrays.asList(stockId.split(","));
			map.put("stockIdList",asList );
			
		}
		
		if(CommonUtil.isNotEmpty(pageNumber) && CommonUtil.isNotEmpty(pageSize))
		{
			map.put("pageSize", Integer.valueOf(pageSize));
			map.put("pageNumber", Integer.valueOf(pageSize)*(Integer.valueOf(pageNumber)-1));
			
		}
		
		if(CommonUtil.isNotEmpty(id))
		{
			map.put("idList", Arrays.asList(id.split(",")));
		}
		
	}
	
	/**
	 * 过滤易达仓库
	 * @param warehouse
	 */
	private List<QcWarehouseInfo> filterWareHouse(List<QcWarehouseInfo> warehouse)
	{
		List<QcWarehouseInfo> list = new ArrayList<QcWarehouseInfo>();
		for (QcWarehouseInfo qcWarehouseInfo : warehouse) 
		{
		
			if(qcWarehouseInfo.getWarehouse_name().contains("4PX"))
			{
				list.add(qcWarehouseInfo);
			}
		}
		return list;
	}

}
