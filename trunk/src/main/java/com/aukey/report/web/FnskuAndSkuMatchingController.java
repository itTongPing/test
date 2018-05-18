package com.aukey.report.web;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aukey.domain.base.TableData;
import com.aukey.report.domain.FnskuAndSkuMatching;
import com.aukey.report.service.FnskuAndSkuMatchingService;
import com.aukey.report.utils.FnskuAndSkuMatchingExcel;
import com.aukey.util.CommonUtil;
import com.aukey.util.DateUtil;

@RequestMapping("/report/FnskuAndSkuMatching")
@Controller
public class FnskuAndSkuMatchingController {
	
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private FnskuAndSkuMatchingService fnskuAndSkuMatchingService;
	
	@RequestMapping("/index")
	public String index(ModelMap modelMap){
		return "fnskuAndSkuMatching";
	}
	
	@RequestMapping("/search")
	@ResponseBody
	public TableData<FnskuAndSkuMatching> search(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
	        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
	 		@RequestParam(value = "sku", required = false) String sku, 
	 		@RequestParam(value = "fromWsskuBox", required = false) String fromWsskuBox,
	 		@RequestParam(value = "fnsku", required = false) String fnsku)
	{
		try{
			TableData<FnskuAndSkuMatching> tableData = new TableData<FnskuAndSkuMatching>();
			Map<String,Object> map = new HashMap<String,Object>();
			setParam(map, pageSize, pageNumber,sku,fromWsskuBox,fnsku,null);
			tableData=fnskuAndSkuMatchingService.selectAllList(map);
			return tableData;
		}catch(Exception e){
			logger.error("FNSKU与SKU匹配表查询异常",e);
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping("/export")
	public String export(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "id", required = false) String id,
	        @RequestParam(value = "sku", required = false) String sku, 
	 		@RequestParam(value = "fromWsskuBox", required = false) String fromWsskuBox,
	 		@RequestParam(value = "fnsku", required = false) String fnsku)
	{
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			setParam(map, null, null,sku,fromWsskuBox,fnsku,id);
			TableData<FnskuAndSkuMatching> tableData = fnskuAndSkuMatchingService.selectAllList(map);
			List<FnskuAndSkuMatching> selectAll = tableData.getRows();
			FnskuAndSkuMatchingExcel fnskuAndSkuMatchingExcel = new FnskuAndSkuMatchingExcel();
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet();
			fnskuAndSkuMatchingExcel.setSheetTitle(workbook, sheet);//设置表头样式
			
			fnskuAndSkuMatchingExcel.appendSheetRow(workbook, sheet, 1, selectAll);
			String fileName = "FNSKU与SKU匹配表"+DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss");
      	
			response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题  
			response.setCharacterEncoding("utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1")+".xlsx");    	 
			workbook.write(response.getOutputStream());
			workbook.close();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			logger.error("FNSKU与SKU匹配表导出异常",e);
			e.printStackTrace();
		}		
		return "/report/FnskuAndSkuMatching/index";
	}
	
	private void setParam(Map<String, Object> map,Integer pageSize,
			Integer pageNumber,String sku,String fromWsskuBox,String fnsku,String id)
	{		
		if(CommonUtil.isNotEmpty(sku))
		{
			if(sku.split(",").length > 1){
				map.put("skuList", Arrays.asList(sku.split(",")));
			}else{
				map.put("sku", sku);
			}
		}
		if(CommonUtil.isNotEmpty(fromWsskuBox))
		{
			if(fromWsskuBox.split(",").length > 1){
				map.put("fromWsskuBoxList", Arrays.asList(fromWsskuBox.split(",")));
			}else{
				map.put("fromWsskuBox", fromWsskuBox);
			}
		}
		if(CommonUtil.isNotEmpty(fnsku))
		{
			if(fnsku.split(",").length > 1){
				map.put("fnskuList", Arrays.asList(fnsku.split(",")));
			}else{
				map.put("fnsku", fnsku);
			}
		}
		if(CommonUtil.isNotEmpty(id))
		{
			map.put("ids", Arrays.asList(id.split(",")));
		}
		if(CommonUtil.isNotEmpty(pageSize)&&CommonUtil.isNotEmpty(pageNumber))
		{
			map.put("pageSize",pageSize);
			map.put("pageNumber",(pageNumber-1)*pageSize);
		}
	}
	
}
