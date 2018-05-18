package com.aukey.report.web.purchaseSell;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aukey.report.base.BaseController;
import com.aukey.report.domain.QcWarehouseInfo;
import com.aukey.report.domain.purchaseSell.PurchaseSellStockVO;
import com.aukey.report.service.purchaseSell.PurchaseSellStockService;
import com.aukey.report.dto.purchaseSell.PurchaseSellStockParam;
import com.aukey.report.utils.PurchaseSellReportExcel;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.WareHouseUtil;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CommonUtil;

@RequestMapping("/report/purchaseSellStock")
@Controller
public class PurchaseSellStockController extends BaseController{
	
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private PurchaseSellStockService purchaseSellStockService;
	
	@Value("${product.warehouse.url}")
	private String product_warehouse_url;
	
	@RequestMapping("/index")
	public String index(ModelMap modelMap) {
		// 获取仓库名称列表
		WareHouseUtil wareHouseUtil = new WareHouseUtil();
		List<QcWarehouseInfo> warehouse = wareHouseUtil.getWarehouse( "3,4,5,7,11",null, product_warehouse_url);
		//获取事业部名称列表
		//事业部权限控制(事业部只能看到本事业部的信息，物流部可以看到所有)
		        List<Map<String,Object>> deptList = getAuthDepartment("1");
		modelMap.addAttribute("deptList", deptList);
		modelMap.addAttribute("warehouses", warehouse);
		return "purchaseSellStockReport";
	}

	private List<Map<String, Object>> getAuthDepartment(String type) {
		List<Map<String,Object>> deptList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> authorities = RoleUtil.getAuthorities();
		List<Map<String,Object>> userGroup = RoleUtil.getUserGroup();
		//判断是否是物流部
		boolean flag = true;
		Map<String,Object> param = new HashMap<String,Object>();
		if(!authorities.isEmpty())
		{
			for (Map<String, Object> map2 : authorities) 
			{
				if("物流部".equals(map2.get("authority")))
				{
					if("-1".equals(type) || CommonUtil.isEmpty(type))
					{
						return null;
					}
					deptList = purchaseSellStockService.getDepartment(param);
					flag = false;
					break;
				}
			}
		}
		if(flag)
		{
			if(!userGroup.isEmpty())
			{
				String paramStr = "";
				for (Map<String, Object> map2 : userGroup) 
				{
					String temDepts = map2.get("company_id_tree").toString();
					String[] split = temDepts.split(",");
					if(split.length>2)
					{
						if("23".equals(split[1]) || "22".equals(split[1]))
						{
							deptList = purchaseSellStockService.getDepartment(param);
							paramStr = "";
							break;
						}else
						{
							paramStr += split[1]+",";
						}
						
					}
				}
				if(CommonUtil.isNotEmpty(paramStr))
				{
					param.put("ids", Arrays.asList(paramStr.split(",")));
					try {
						deptList = purchaseSellStockService.getDepartment(param);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return deptList;
	}
	
	@RequestMapping("/search")
	@ResponseBody
	public TableData<PurchaseSellStockVO> search(PurchaseSellStockParam param,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "limit", defaultValue = "10") int limit) {
		PageParam pageParam = new PageParam(pageNumber, limit);
		if(("-1").equals(param.getDeptId()) || CommonUtil.isEmpty(param.getDeptId())){
			List<Map<String,Object>> authDepartment = getAuthDepartment(param.getDeptId());
			if(authDepartment!=null)
			{
				StringBuilder deptId =new StringBuilder();
				for (Map<String,Object> dept : authDepartment) {
					deptId.append(dept.get("id")+",");
				}
				param.setDeptId(deptId.toString());
			}else
			{
				param.setDeptId(null);
			}
			
		}
		TableData<PurchaseSellStockVO> result = purchaseSellStockService.listPage(pageParam, param);
		return result;
	}
	
	@RequestMapping("/preExport")
	@ResponseBody
	public AjaxResponse preExport(PurchaseSellStockParam param, HttpServletRequest request, HttpServletResponse response) {
		int count = purchaseSellStockService.count(param);
		if (count > exportMaxNumber) {
			return new AjaxResponse().failure("导出的数据量太大，请选择过滤条件");
		}
		return new AjaxResponse();
	}
	
	@RequestMapping("/export")
	public ModelAndView export(PurchaseSellStockParam param, HttpServletRequest request, HttpServletResponse response) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String filename = "进销存报表_" + sdf.format(new Date());
			response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));
			OutputStream os = response.getOutputStream();
			PageParam pageParam = new PageParam(1, exportMaxNumber);
			TableData<PurchaseSellStockVO> pageResult = purchaseSellStockService.listPage(pageParam, param);
			List<PurchaseSellStockVO> list = pageResult.getRows();
			new PurchaseSellReportExcel().writeExc(os, list);
		} catch (RowsExceededException e) {
			logger.error("下载报表", e);
		} catch (IOException e) {
			logger.error("IO", e);
		} catch (WriteException e) {
			logger.error("WriteException", e);
		}
		ModelAndView mv = new ModelAndView("purchaseSellStockReport");
		return mv;
	}
	
	
}
