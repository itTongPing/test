package com.aukey.report.web.centerstock;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.aukey.report.domain.base.TableData;
import com.aukey.report.domain.centerstock.CenterSkuDepartRelation;
import com.aukey.report.domain.centerstock.Result;
import com.aukey.report.service.centerstock.CenterSkuDepartRelationService;
import com.aukey.report.service.centerstock.CenterStockReportService;
import com.aukey.util.CommonUtil;

@RequestMapping("/report/centerSkuDepartRelation")
@Controller
public class CenterSkuDepartRelationController {
	
	/**
	 * 打印日志
	 */
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private CenterStockReportService centerStockReportService;
	
	@Autowired
	private CenterSkuDepartRelationService centerSkuDepartRelationService;
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
	{
		logger.info("*******进入SKU与部门对应关系表*******");
		ModelAndView modelAndView = new ModelAndView("centerstock/centerSkuDepartRelation");
		Map<String, Object> param = new HashMap<String,Object>();
		List<Map<String,Object>> findAllOrgs = centerStockReportService.findAllOrgs(param);
		modelAndView.addObject("departList", findAllOrgs);
		return modelAndView;
	}
	
	/**
	 * SKU与部门对应关系表查询
	 * @param pageSize
	 * @param pageNumber
	 * @param sku
	 * @param deptIds
	 * @return
	 */
	@RequestMapping("/search")
	@ResponseBody
	public TableData<CenterSkuDepartRelation> search(@RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
	        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
	        @RequestParam(value = "sku", required = false) String sku, //sku
	        @RequestParam(value = "deptIds", required = false) String deptIds)
	{
		
		TableData<CenterSkuDepartRelation> tableData = new TableData<CenterSkuDepartRelation>();
		Map<String, Object> map = new HashMap<String,Object>();
		setParam(map, sku, deptIds, pageSize, pageNumber);
		List<CenterSkuDepartRelation> selectAll = centerSkuDepartRelationService.selectAll(map);
		Integer count = centerSkuDepartRelationService.selectAllCount(map);
		tableData.setRows(selectAll);
		tableData.setTotal(count);
		return tableData;
	}
	

	/**
	 * 导入文件
	 * @param file
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/upLoadFile")
	@ResponseBody
	public Result upLoadFile(MultipartFile file, ModelMap modelMap,HttpServletRequest request,HttpServletResponse response)
	{
		Result result = new Result();
		
		long size = file.getSize();
		if(size>10*1024*1024 && size>0)
		{
			result.setSuccess(false);
			result.setErrMsg("文件限制10M以内!!");
			return result;
		}
		String originalFilename = file.getOriginalFilename();
		if(!".xlsx".equals(originalFilename.substring(originalFilename.indexOf("."))))
		{
			result.setSuccess(false);
			result.setErrMsg("只支持xlsx,请重新上传！");
			return result;
		}
		try {
			Workbook workbook = new XSSFWorkbook(file.getInputStream());
			//Sheet sheet = workbook.getSheetAt(0);
			Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
			result = centerSkuDepartRelationService.uploadExcel(workbook,userId);
		} catch (IOException e) {
			result.setSuccess(false);
			result.setErrMsg("请确认导入的excel格式是否正确!!");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 设置查询参数
	 * @param map
	 * @param sku
	 * @param deptIds
	 * @param pageSize
	 * @param pageNumber
	 */
	private void setParam(Map<String, Object> map,String sku,String deptIds,Integer pageSize,Integer pageNumber)
	{
		
		if(CommonUtil.isNotEmpty(sku))
		{
			map.put("skuCodeList", Arrays.asList(sku.split(",")));
		}
		if(CommonUtil.isNotEmpty(deptIds) && !"-1".equals(deptIds))
		{
			map.put("departmentIdList", Arrays.asList(deptIds.split(",")));
		}
		if(CommonUtil.isNotEmpty(pageSize)&&CommonUtil.isNotEmpty(pageNumber))
		{
			map.put("pageSize",pageSize);
			map.put("pageNumber",(pageNumber-1)*pageSize);
		}
	}
	
}
