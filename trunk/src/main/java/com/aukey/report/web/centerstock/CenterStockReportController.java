package com.aukey.report.web.centerstock;

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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.aukey.domain.base.TableData;
import com.aukey.report.domain.QcWarehouseInfo;
import com.aukey.report.domain.centerstock.Account;
import com.aukey.report.domain.centerstock.CenterDomesticTransferWarehouse;
import com.aukey.report.domain.centerstock.CenterFbaInTransit;
import com.aukey.report.domain.centerstock.CenterFbaStockCount;
import com.aukey.report.domain.centerstock.CenterHeadInTransit;
import com.aukey.report.domain.centerstock.CenterOverseaTransportInTransit;
import com.aukey.report.domain.centerstock.CenterOverseaWarehouse;
import com.aukey.report.domain.centerstock.CenterPurchaseInTransit;
import com.aukey.report.domain.centerstock.CenterStockReport;
import com.aukey.report.service.centerstock.CenterDomesticTransferWarehouseService;
import com.aukey.report.service.centerstock.CenterFbaInTransitService;
import com.aukey.report.service.centerstock.CenterFbaStockCountService;
import com.aukey.report.service.centerstock.CenterHeadInTransitService;
import com.aukey.report.service.centerstock.CenterOverseaTransportInTransitService;
import com.aukey.report.service.centerstock.CenterOverseaWarehouseService;
import com.aukey.report.service.centerstock.CenterPurchaseInTransitService;
import com.aukey.report.service.centerstock.CenterStockReportService;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.WareHouseUtil;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CommonUtil;
import com.aukey.util.DateUtil;
import com.aukey.util.HttpUtil;
import com.aukey.util.URLBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

@RequestMapping("/report/centerStock")
@Controller
public class CenterStockReportController {
	
	/**
	 * 打印日志
	 */
	private Logger logger = Logger.getLogger(getClass());
	
	/**
	 * 查询中央库存
	 */
	@Autowired
	private CenterStockReportService centerStockReportService;
	
	//FBA退货在途
	@Autowired
	private CenterFbaInTransitService centerFbaInTransitService;
	//国内中转仓仓库存
	@Autowired
	private CenterDomesticTransferWarehouseService centerDomesticTransferWarehouseService;
	//FBA库存
	@Autowired
	private CenterFbaStockCountService centerFbaStockCountService;
	//头程在途
	@Autowired
	private CenterHeadInTransitService centerHeadInTransitService;
	//海外转运在途
	@Autowired
	private CenterOverseaTransportInTransitService centerOverseaTransportInTransitService;
	//采购在途
	@Autowired
	private CenterPurchaseInTransitService centerPurchaseInTransitService;
	//海外仓库存
	@Autowired
	private CenterOverseaWarehouseService centerOverseaWarehouseService;
	
	/**
     * 亚马逊店铺列表
     */
    @Value("${String.product.amazon}")
    private String amazonList;
	
	@Value("${product.warehouse.url}")
	private String warehouseUrl;
	 
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response)
	{
		logger.info("*******进入中央库存报表*******");
		ModelAndView modelAndView = new ModelAndView("centerstock/center_stock_report");
		
		//事业部权限控制(事业部只能看到本事业部的信息，物流部可以看到所有)
		List<Map<String,Object>> authorities = RoleUtil.getAuthorities();
		List<Map<String,Object>> userGroup = RoleUtil.getUserGroup();
		List<Map<String,Object>> findAllOrgs = new ArrayList<Map<String,Object>>();
		//判断是否是物流部
		boolean flag = true;
		Map<String,Object> param = new HashMap<String,Object>();
		if(!authorities.isEmpty())
		{
			for (Map<String, Object> map2 : authorities) 
			{
				if("物流部".equals(map2.get("authority")))
				{
					findAllOrgs = centerStockReportService.findAllOrgs(param);
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
							findAllOrgs = centerStockReportService.findAllOrgs(param);
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
						findAllOrgs = centerStockReportService.findAllOrgs(param);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		modelAndView.addObject("deptList", findAllOrgs);
		return modelAndView;
	}
	
	@RequestMapping("/stock/index")
	public ModelAndView stockIndex(HttpServletRequest request,HttpServletResponse response,String sku,String code,ModelMap modelMap)
	{
		
		String str = "";
		//code : fba(FBA库存),overseaTransfer(海外转运),overseaStock(海外仓库库存) ,fbaTransfer(FBA退货在途)
		//firstAir ,firstShip , firstTrains(头程空，海，铁运),warehouseTransfer(国内中转仓),purchaseInWay(采购在途)
		String type = "";
		WareHouseUtil w = new WareHouseUtil();
		if("fba".equals(code))
		{
			logger.info("*******进入FBA库存查询*******");//仓库类型是	6
			str = "centerstock/fba_stock";
			//List<QcWarehouseInfo> warehouse = w.getWarehouse("6", null, warehouseUrl);
			String params = URLBuilder.generate(null, amazonList);
	        AjaxResponse results = HttpUtil.doGet(params);
	        List<Account> list = new ArrayList<Account>();
	        if(results.getData()!=null)
	        {
	        	list = JSON.parseObject(results.getData().toString(), new TypeReference<List<Account>>()
		        {
		        }.getType());
	        }
			modelMap.addAttribute("accountList", list);
		}else if("overseaTransfer".equals(code))
		{
			logger.info("*******进入海外转运在途查询*******");
			str = "centerstock/oversea_stock_transfer";//仓库类型6，7，11
			List<QcWarehouseInfo> warehouse = w.getWarehouse("6", null, warehouseUrl);
			warehouse.addAll(w.getWarehouse("7", null, warehouseUrl));
			warehouse.addAll(w.getWarehouse("11", null, warehouseUrl));
			modelMap.addAttribute("signWareHouse", warehouse);
		}else if("overseaStock".equals(code))
		{
			logger.info("*******进入海外仓库库存查询*******");//仓库类型是	7
			str = "centerstock/oversea_stock";
			List<QcWarehouseInfo> warehouse = w.getWarehouse("7", null, warehouseUrl);
			modelMap.addAttribute("signWareHouse", warehouse);
		}else if("firstAir".equals(code))
		{
			logger.info("*******进入头程空运在途查询*******");//仓库类型11
			str = "centerstock/first_transfer";
			type = "0";
			List<QcWarehouseInfo> warehouse = w.getWarehouse("11", null, warehouseUrl);
			modelMap.addAttribute("signWareHouse", warehouse);
		}else if("firstShip".equals(code))
		{
			logger.info("*******进入头程海运在途查询*******");//仓库类型11
			str = "centerstock/first_transfer";
			type = "1";
			List<QcWarehouseInfo> warehouse = w.getWarehouse("11", null, warehouseUrl);
			modelMap.addAttribute("signWareHouse", warehouse);
		}else if("firstTrains".equals(code))
		{
			logger.info("*******进入头程铁运在途查询*******");//仓库类型11
			str = "centerstock/first_transfer";
			type = "3";
			List<QcWarehouseInfo> warehouse = w.getWarehouse("11", null, warehouseUrl);
			modelMap.addAttribute("signWareHouse", warehouse);
		}else if("warehouseTransfer".equals(code))
		{
			logger.info("*******进入国内中转仓库存查询*******");//仓库类型3
			str = "centerstock/transfer_warehouse_count";
			List<QcWarehouseInfo> warehouse = w.getWarehouse("3", null, warehouseUrl);
			modelMap.addAttribute("signWareHouse", warehouse);
		}else if("purchaseInWay".equals(code))
		{
			logger.info("*******进入采购在途查询*******");//仓库类型3
			str = "centerstock/purchase_transfer";
			List<QcWarehouseInfo> warehouse = w.getWarehouse("3", null, warehouseUrl);
			modelMap.addAttribute("signWareHouse", warehouse);
		}else if("fbaTransfer".equals(code))
		{
			logger.info("*******进入FBA退货在途*******");//仓库类型11
			str = "centerstock/fba_transfer";
			List<QcWarehouseInfo> warehouse = w.getWarehouse("11", null, warehouseUrl);
			modelMap.addAttribute("signWareHouse", warehouse);
		}else
		{
			logger.info("*******code有误*******");
			str = "";
		}
		ModelAndView modelAndView = new ModelAndView(str);
		modelAndView.addObject("sku", sku);
		if(CommonUtil.isNotEmpty(type))
		{
			modelAndView.addObject("type", type);
		}
		return modelAndView;
	}
	
	/**
	 * 查询中央库存
	 * @param request
	 * @param response
	 * @param pageSize
	 * @param pageNumber
	 * @param sku
	 * @param upc
	 * @param deptIds
	 * @return
	 */
	@RequestMapping("/search")
	@ResponseBody
	public TableData<CenterStockReport> search(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
	        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
	        @RequestParam(value = "sku", required = false) String sku, //sku
	        @RequestParam(value = "upc", required = false) String upc,
	        @RequestParam(value = "stockType", required = false) String stockType,
	        @RequestParam(value = "deptIds", required = false) String deptIds)
	{
		TableData<CenterStockReport> tableData = new TableData<CenterStockReport>();
		Map<String,Object> map = new HashMap<String,Object>();
		setParam(map, pageSize, pageNumber, sku, upc, deptIds,null,stockType);
		List<CenterStockReport> selectAll = centerStockReportService.selectAll(map);
		Integer selectAllCount = centerStockReportService.selectAllCount(map);
		
		tableData.setRows(selectAll);
		tableData.setTotal(selectAllCount);
		
		return tableData;
	}
	
	@RequestMapping("/export")
	public String export(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "sku", required = false) String sku, //sku
	        @RequestParam(value = "upc", required = false) String upc,
	        @RequestParam(value = "stockType", required = false) String stockType,
	        @RequestParam(value = "deptIds", required = false) String deptIds,
	        @RequestParam(value = "id", required = false) String id)
	{
		//TableData<CenterStockReport> tableData = new TableData<CenterStockReport>();
		Map<String,Object> map = new HashMap<String,Object>();
		setParam(map, null, null, sku, upc, deptIds,id,stockType);
		List<CenterStockReport> selectAll = centerStockReportService.selectAll(map);
		//Integer selectAllCount = centerStockReportService.selectAllCount(map);
		CenterStockReportExcel centerStockReportExcel = new CenterStockReportExcel();
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		centerStockReportExcel.setSheetTitle(workbook, sheet);//设置表头样式
		
		centerStockReportExcel.appendSheetRow(workbook, sheet, 1, selectAll);
		String fileName = "中央库存查询报表"+DateUtil.dateFormat(new Date(), "yyyyMMddHHmmss");
      	try {
			response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题  
			response.setCharacterEncoding("utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1")+".xlsx");    	 
			workbook.write(response.getOutputStream());
			workbook.close();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			logger.error("中央库存查询报表导出异常",e);
			e.printStackTrace();
		}
		
		/*tableData.setRows(selectAll);
		tableData.setTotal(selectAllCount);*/
		
		return "/report/centerStock/index";
	}
	
	
	
	/**
	 * fba库存查询
	 * @param request
	 * @param response
	 * @param pageSize
	 * @param pageNumber
	 * @param sku
	 * @return
	 */
	@RequestMapping("/search/fbaStock")
	@ResponseBody
	public TableData<CenterFbaStockCount> searchFbaStock(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
	        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
	        @RequestParam(value = "sku", required = false) String sku,
	        @RequestParam(value = "stockType", required = false) String stockType,
	        @RequestParam(value = "accountIds", required = false) String accountIds,
	        @RequestParam(value = "siteGroupIds", required = false) String siteGroupIds)
	{
		TableData<CenterFbaStockCount> tableData = new TableData<CenterFbaStockCount>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("sku",sku);
		map.put("stockType", stockType);
		map.put("pageNumber",(pageNumber-1)*pageSize);
		map.put("pageSize",pageSize);
		if(!CommonUtil.isEmpty(accountIds) && !"-1".equals(accountIds))
		{
			map.put("accountIdList", Arrays.asList(accountIds.split(",")));
		}
		if(!CommonUtil.isEmpty(siteGroupIds) && !"-1".equals(siteGroupIds))
		{
			map.put("siteGroupIdList", Arrays.asList(siteGroupIds.split(",")));
		}
		List<CenterFbaStockCount> rows = centerFbaStockCountService.selectAll(map);	
		Integer total = centerFbaStockCountService.selectAllCount(map);
		tableData.setRows(rows);
		tableData.setTotal(total);		
		
		return tableData;
	}
	
	/**
	 * 海外仓库存查询
	 * @param request
	 * @param response
	 * @param pageSize
	 * @param pageNumber
	 * @param sku
	 * @return
	 */
	@RequestMapping("/search/overseaStock")
	@ResponseBody
	public TableData<CenterOverseaWarehouse> searchOverseaStock(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
	        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
	        @RequestParam(value = "sku", required = false) String sku,
	        @RequestParam(value = "stockType", required = false) String stockType,
	        @RequestParam(value = "stocks", required = false) String stocks)
	{
		TableData<CenterOverseaWarehouse> tableData = new TableData<CenterOverseaWarehouse>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("sku",sku);
		map.put("stockType", stockType);
		map.put("pageNumber",(pageNumber-1)*pageSize);
		map.put("pageSize",pageSize);
		if(!CommonUtil.isEmpty(stocks) && !"-1".equals(stocks))
		{
			map.put("stockList", Arrays.asList(stocks.split(",")));
		}
		List<CenterOverseaWarehouse> rows = centerOverseaWarehouseService.selectAll(map);	
		Integer total = centerOverseaWarehouseService.selectAllCount(map);
		tableData.setRows(rows);
		tableData.setTotal(total);		
		
		return tableData;
	}
	
	/**
	 * 海外仓转运在途查询
	 * @param request
	 * @param response
	 * @param pageSize
	 * @param pageNumber
	 * @param sku
	 * @return
	 */
	@RequestMapping("/search/overseaStockTransfer")
	@ResponseBody
	public TableData<CenterOverseaTransportInTransit> searchOverseaStockTransfer(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
	        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
	        @RequestParam(value = "sku", required = false) String sku,
	        @RequestParam(value = "stockType", required = false) String stockType,
	        @RequestParam(value = "stocks", required = false) String stocks)
	{
		TableData<CenterOverseaTransportInTransit> tableData = new TableData<CenterOverseaTransportInTransit>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("sku",sku);
		map.put("stockType", stockType);
		map.put("pageNumber",(pageNumber-1)*pageSize);
		map.put("pageSize",pageSize);
		if(!CommonUtil.isEmpty(stocks) && !"-1".equals(stocks))
		{
			map.put("stockList", Arrays.asList(stocks.split(",")));
		}
		List<CenterOverseaTransportInTransit> rows = centerOverseaTransportInTransitService.selectAll(map);
		Integer total = centerOverseaTransportInTransitService.selectAllCount(map);
		tableData.setRows(rows);
		tableData.setTotal(total);		
		
		return tableData;
	}
	
	/**
	 * 头程在途查询
	 * @param request
	 * @param response
	 * @param pageSize
	 * @param pageNumber
	 * @param sku
	 * @return
	 */
	@RequestMapping("/search/firstAirTransfer")
	@ResponseBody
	public TableData<CenterHeadInTransit> searchFirstAirTransfer(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
	        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
	        @RequestParam(value = "sku", required = false) String sku,
	        @RequestParam(value = "stockType", required = false) String stockType,
	        @RequestParam(value = "type", required = false) String type,
	        @RequestParam(value = "stocks", required = false) String stocks)
	{
		TableData<CenterHeadInTransit> tableData = new TableData<CenterHeadInTransit>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("sku",sku);
		map.put("stockType", stockType);
		map.put("type",type);
		map.put("pageNumber",(pageNumber-1)*pageSize);
		map.put("pageSize",pageSize);
		if(!CommonUtil.isEmpty(stocks) && !"-1".equals(stocks))
		{
			map.put("stockList", Arrays.asList(stocks.split(",")));
		}
		List<CenterHeadInTransit> rows = centerHeadInTransitService.selectAll(map);
		Integer total = centerHeadInTransitService.selectAllCount(map);
		tableData.setRows(rows);
		tableData.setTotal(total);		
		
		return tableData;
	}
	
	/**
	 * 国内中转仓库存
	 * @param request
	 * @param response
	 * @param pageSize
	 * @param pageNumber
	 * @param sku
	 * @return
	 */
	@RequestMapping("/search/transferWarehouseCount")
	@ResponseBody
	public TableData<CenterDomesticTransferWarehouse> searchTransferWarehouseCount(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
	        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
	        @RequestParam(value = "sku", required = false) String sku,
	        @RequestParam(value = "stockType", required = false) String stockType,
	        @RequestParam(value = "stocks", required = false) String stocks)
	{
		TableData<CenterDomesticTransferWarehouse> tableData = new TableData<CenterDomesticTransferWarehouse>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("sku",sku);
		map.putIfAbsent("stockType", stockType);
		map.put("pageNumber",(pageNumber-1)*pageSize);
		map.put("pageSize",pageSize);
		if(!CommonUtil.isEmpty(stocks) && !"-1".equals(stocks))
		{
			map.put("stockList", Arrays.asList(stocks.split(",")));
		}
		List<CenterDomesticTransferWarehouse> rows = centerDomesticTransferWarehouseService.selectAll(map);
		Integer total = centerDomesticTransferWarehouseService.selectAllCount(map);
		tableData.setRows(rows);
		tableData.setTotal(total);		
		
		return tableData;
	}
	
	/**
	 * 采购订单在途
	 * @param request
	 * @param response
	 * @param pageSize
	 * @param pageNumber
	 * @param sku
	 * @return
	 */
	@RequestMapping("/search/purchaseTransfer")
	@ResponseBody
	public TableData<CenterPurchaseInTransit> searchPurchaseTransfer(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
	        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
	        @RequestParam(value = "sku", required = false) String sku,
	        @RequestParam(value = "stockType", required = false) String stockType,
	        @RequestParam(value = "stocks", required = false) String stocks)
	{
		TableData<CenterPurchaseInTransit> tableData = new TableData<CenterPurchaseInTransit>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("sku",sku);
		map.put("stockType", stockType);
		map.put("pageNumber",(pageNumber-1)*pageSize);
		map.put("pageSize",pageSize);
		if(!CommonUtil.isEmpty(stocks) && !"-1".equals(stocks))
		{
			map.put("stockList", Arrays.asList(stocks.split(",")));
		}
		List<CenterPurchaseInTransit> rows = centerPurchaseInTransitService.selectAll(map);
		Integer total = centerPurchaseInTransitService.selectAllCount(map);
		tableData.setRows(rows);
		tableData.setTotal(total);		
		
		return tableData;
	}
	
	/**
	 * FBA在途
	 * @param request
	 * @param response
	 * @param pageSize
	 * @param pageNumber
	 * @param sku
	 * @return
	 */
	@RequestMapping("/search/fbaTransfer")
	@ResponseBody
	public TableData<CenterFbaInTransit> searchFbaTransfer(HttpServletRequest request,HttpServletResponse response,
	        @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
	        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
	        @RequestParam(value = "sku", required = false) String sku,
	        @RequestParam(value = "stockType", required = false) String stockType,
	        @RequestParam(value = "stocks", required = false) String stocks)
	{
		TableData<CenterFbaInTransit> tableData = new TableData<CenterFbaInTransit>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("sku",sku);
		map.put("stockType", stockType);
		map.put("pageNumber",(pageNumber-1)*pageSize);
		map.put("pageSize",pageSize);
		if(!CommonUtil.isEmpty(stocks) && !"-1".equals(stocks))
		{
			map.put("stockList", Arrays.asList(stocks.split(",")));
		}
		List<CenterFbaInTransit> rows = centerFbaInTransitService.selectAll(map);
		Integer total = centerFbaInTransitService.selectAllCount(map);
		tableData.setRows(rows);
		tableData.setTotal(total);		
		
		return tableData;
	}
	private void setParam(Map<String, Object> map,Integer pageSize,
			Integer pageNumber,String sku,String upc,String deptIds,String id,String stockType)
	{
		
		//事业部权限控制(事业部只能看到本事业部的信息，物流部可以看到所有)
		List<Map<String,Object>> authorities = RoleUtil.getAuthorities();
		List<Map<String,Object>> userGroup = RoleUtil.getUserGroup();
		if(CommonUtil.isEmpty(deptIds) || "-1".equals(deptIds))
		{
			if(!userGroup.isEmpty())
			{
				String initDepts = "";
				for (Map<String, Object> map2 : userGroup) 
				{
					String temDepts = map2.get("company_id_tree").toString();
					String[] split = temDepts.split(",");
					if(split.length>2)
					{
						if("23".equals(split[1]) || "22".equals(split[1]))
						{
							initDepts = null;
							break;
						}else
						{
							initDepts += split[1]+",";
						}
					}
				}
				for (Map<String, Object> map2 : authorities) 
				{
					if("物流部".equals(map2.get("authority")))
					{
						initDepts = null;
						break;
					}
				}
				if(initDepts!=null && CommonUtil.isNotEmpty(initDepts))
				{
					map.put("initDepts", Arrays.asList(initDepts.split(",")));
				}
			}
		}
		
		
		if(CommonUtil.isNotEmpty(stockType)){
			map.put("stockType", stockType);
		}
		
		if(CommonUtil.isNotEmpty(id))
		{
			map.put("ids", Arrays.asList(id.split(",")));
		}
		
		if(CommonUtil.isNotEmpty(sku))
		{
			map.put("skuList", Arrays.asList(sku.split(",")));
		}
		if(CommonUtil.isNotEmpty(upc))
		{
			map.put("upcList", Arrays.asList(upc.split(",")));
		}
		if(CommonUtil.isNotEmpty(deptIds) && !"-1".equals(deptIds))
		{
			map.put("deptList", Arrays.asList(deptIds.split(",")));
		}
		if(CommonUtil.isNotEmpty(pageSize)&&CommonUtil.isNotEmpty(pageNumber))
		{
			map.put("pageSize",pageSize);
			map.put("pageNumber",(pageNumber-1)*pageSize);
		}
	}

}
