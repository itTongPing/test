package com.aukey.report.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.aukey.report.domain.DepartmentResult;
import com.aukey.report.domain.DepartmentResult.DepartmentVO;
import com.aukey.report.domain.InventoryResult;
import com.aukey.report.domain.InventoryResult.InventoryVO;
import com.aukey.report.domain.StockModel;
import com.aukey.report.domain.StockResult;
import com.aukey.report.domain.StorageReport;
import com.aukey.report.domain.StrictSupplier;
import com.aukey.report.domain.base.LegalerResult;
import com.aukey.report.domain.base.Result;
import com.aukey.report.domain.base.TableData;
import com.aukey.report.service.StorageReportService;
import com.aukey.report.service.TransferReportService;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.WareHouseUtil;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CommonUtil;
import com.aukey.util.HttpUtil;
import com.aukey.util.URLBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 入库报表
 * 
 * @author xiehz
 *
 */
@RequestMapping("/storageReport")
@Controller
public class StorageReportController {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private StorageReportService storageReportService;
    
	
	@Value("${inventory.api.url}")
	private String inventory_name_list_url;
	
	@Value("${legaler.api.url}")
	private String legaler_list_url;
	
	@Value("${purchase.department.api.url}")
	private String all_department_list_url;
	
	@Value("${product.warehouse.url}")
	private String warehouseUrl;
	
	
	@Value("${turnDeliPlan.querySock.url}")
	private String querySockUrl;
	
	@Value("${String.stock.info.url}")
    private String stockNameUrl;
	
	/**
     * * 供应商模糊搜索
     */
    @Value("${String.aliaslike.url}")
    private String aliaslikeUrl;
	
	
//	@RequestMapping("/getCount")
//    @ResponseBody
//	public int getCount(
//			@RequestParam(value = "stockId", required = false) String stockId, //目前仓库
//	        @RequestParam(value = "skuCode", required = false) String skuCode, //sku名称
//	        @RequestParam(value = "queryNumber", required = false) String queryNumber,//入库/采购/质检单号
//	        @RequestParam(value = "signWarehouse", required = false) String signWarehouse,//仓库
//	        @RequestParam(value = "createDateStart", required = false) String createDateStart,//入库开始时间
//	        @RequestParam(value = "createDateEnd", required = false) String createDateEnd,//入库结束时间
//	        @RequestParam(value = "signType", required = false) String signType,//标记
//	        @RequestParam(value = "muWarehouseName", required = false) String muWarehouseName, //目标仓
//	        @RequestParam(value = "legaler", required = false) String legaler, // 法人
//	        @RequestParam(value = "transferType", required = false) String transferType, //运输  0空 1海
//	        @RequestParam(value = "isTax", required = false) String isTax,  //含退税 # 1退税 2含税
//	        @RequestParam(value = "checkIds", required = false) String checkIds,
//	        @RequestParam(value = "department", required = false) String department //部门
//	       ){
//		
//	       Map<String, Object> map = new HashMap<String,Object>(); 
//	       
//	       setFilterParam(map, skuCode, queryNumber, signWarehouse, createDateStart, department
//	        		, createDateEnd, signType, muWarehouseName, legaler
//	        		, transferType, isTax, checkIds,stockId);
//		
//	       return this.storageReportService.getTotalStorage(map);
//	}
	
	
	
	
	   /**
     * 通过名称查询目标仓
     * @param stockName 名称
     * @param type 目的仓与调入仓-1,调出仓-2
     * @return 返回列表
     */
    @RequestMapping("/querySock")
    @ResponseBody
    public List<StockModel> querySock(@RequestParam(value = "stockName", required = false) String stockName,
        @RequestParam(value = "type", required = false) Integer type)
    {
    	
    	
        Map<String, Object> maps = new HashMap<String, Object>();
        if (CommonUtil.isNotEmpty(stockName))
        {
            maps.put("stockName", stockName);
        }
        if (CommonUtil.isEmpty(type))
        {
            type = 1;
        }
        String stockTypeStr = "";
        if (type == 1)
        {
            String[] stockTypeArr =
            {"4", "5", "6", "7", "10" };
            stockTypeStr = StringUtils.join(stockTypeArr, ",");
        }
        else
        {
            String[] stockTypeArr =
            { "3" };
            stockTypeStr = StringUtils.join(stockTypeArr, ",");
        }
        maps.put("stockType", stockTypeStr);
        List<StockModel> data = new ArrayList<>();
        String params = URLBuilder.generate(maps, stockNameUrl);
        AjaxResponse result = HttpUtil.doGet(params);
        if (result.isSuccess() && result.getData() != null)
        {
            StockResult results = JSON.parseObject(result.getData().toString(), StockResult.class);
            if (results != null && CommonUtil.isNotEmpty(results.getStatus()) && "success".equals(results.getStatus())
                && results.getList() != null)
            {
                data = JSON.parseObject(results.getList().toString(), new TypeReference<List<StockModel>>()
                {
                }.getType());
            }
        }
        return data;
    }
	
	 /**
     * 跳转到入库列表
     *
     * @return
     */
    @RequestMapping("/index")
   
    public String index(ModelMap modelMap)
    {
        logger.info("-----------------跳转到入库列表页------------------------");
        
		// 获取仓库名称列表 
        try{
        	
       
			List<InventoryVO> inventoryV_list = new ArrayList<InventoryVO>();
			AjaxResponse inventory_name_result = HttpUtil.doGet(inventory_name_list_url);
			if (inventory_name_result.getData() != null) {
				InventoryResult inventoryResult = JSON.parseObject(inventory_name_result.getData().toString(), InventoryResult.class);
				List<InventoryVO> inventoryV_list_all = inventoryResult.getList();
				for (InventoryVO vo : inventoryV_list_all) {
					inventoryV_list.add(vo);
				}
			}
			modelMap.addAttribute("inventorys", inventoryV_list);
	
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
	
	        String warehouseIds = "";
	        for (Integer warehouseId : RoleUtil.getUserWarehouse())
	        {
	            warehouseIds += warehouseId + ",";
	        }
	        WareHouseUtil w = new WareHouseUtil();
	        if (!warehouseIds.equals(""))
	        {
	            modelMap.addAttribute("signWarehouse", w.getWarehouseTwo(warehouseIds, warehouseUrl));
	           
	        }
	        
	        // 目的仓
	        List<StockModel> stockList = storageReportService.getStockByType(new String[]
	        { "5", "6", "7" });
	        modelMap.addAttribute("stockList", stockList);
        }catch(Exception e){
        	logger.error("入库报表 error", e);
        }
        return "warehouse_list";
    }
	
    
    /**
     * 查询入库列表
     * @param sort
     * @param order
     * @param limit
     * @param pageNumber
     * @param queryNumber
     * @param create_user
     * @param createDateStart
     * @param createDateEnd
     * @return
     */
    @RequestMapping("/search")
    @ResponseBody
    public TableData<StorageReport> showReceiveList(
    	HttpServletRequest request,HttpServletResponse response,
        @RequestParam(value = "limit", defaultValue = "20", required = false) int limit,
        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
        @RequestParam(value = "skuCode", required = false) String skuCode, //sku名称
        @RequestParam(value = "queryNumber", required = false) String queryNumber,//入库/采购/质检单号
        @RequestParam(value = "signWarehouse", required = false) String signWarehouse,//仓库
        @RequestParam(value = "createDateStart", required = false) String createDateStart,//入库开始时间
        @RequestParam(value = "createDateEnd", required = false) String createDateEnd,//入库结束时间
        @RequestParam(value = "signType", required = false) String signType,//标记
        @RequestParam(value = "muWarehouseName", required = false) String muWarehouseName, //目标仓
        @RequestParam(value = "legaler", required = false) String legaler, // 法人
        @RequestParam(value = "transferType", required = false) String transferType, //运输  0空 1海
        @RequestParam(value = "isTax", required = false) String isTax,  //含退税 # 1退税 2含税
        @RequestParam(value = "checkIds", required = false) String checkIds,
        @RequestParam(value = "deptName", required = false) String deptName, //部门
        @RequestParam(value = "stockId", required = false) String stockId, //目前仓库
        @RequestParam(value = "saleName", required = false) String saleName, //销售人员
        @RequestParam(value = "buyer", required = false) String buyer, //采购员
        @RequestParam(value = "supplier", required = false) String supplier //供应商
        
        
    		)
    {
        logger.info("------------开始入库列表查询----------------------");
        TableData<StorageReport> tableData = new TableData<StorageReport>();
         Map<String, Object> param = new HashMap<>();
         
        setFilterParam(param, skuCode, queryNumber, signWarehouse, createDateStart, deptName
        		, createDateEnd, signType, muWarehouseName, legaler
        		, transferType, isTax, checkIds,stockId,saleName,buyer,supplier);
        
        param.put("page", (pageNumber - 1) * limit);
        param.put("limit", limit);
   
         
        
        tableData = this.storageReportService.getStorageReportList(param);
        logger.info("------------入库列表查询结束----------------------");
        return tableData;
    }
	
    
    
    /*@RequestMapping(value = "/getSupplierInfo", method = RequestMethod.GET)
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
    }*/
    
    
    

	@RequestMapping("/exportStorageList")
    public String export( 
    		RedirectAttributes attr,
    		HttpServletRequest request, 
    		HttpServletResponse response,
            @RequestParam(value = "skuCode", required = false) String skuCode,//sku名称
            @RequestParam(value = "queryNumber", required = false) String queryNumber,//入库/采购/质检单号
            @RequestParam(value = "signWarehouse", required = false) String signWarehouse,//仓库
            @RequestParam(value = "createDateStart", required = false) String createDateStart,//入库开始时间
            @RequestParam(value = "createDateEnd", required = false) String createDateEnd,//入库结束时间
            @RequestParam(value = "signType", required = false) String signType,//标记
            @RequestParam(value = "muWarehouseName", required = false) String muWarehouseName, //目标仓
            @RequestParam(value = "legaler", required = false) String legaler, // 法人
            @RequestParam(value = "transferType", required = false) String transferType, //运输  0空 1海
            @RequestParam(value = "isTax", required = false) String isTax,  //含退税 # 1退税 2含税
            @RequestParam(value = "checkIds", required = false) String checkIds,  //含退税 # 1退税 2含税
            @RequestParam(value = "deptName", required = false) String deptName,  //需求部门
            @RequestParam(value = "stockIds", required = false) String stockIds,  
            @RequestParam(value = "saleName", required = false) String saleName, //销售人员
            @RequestParam(value = "buyer", required = false) String buyer, //采购员
            @RequestParam(value = "supplier", required = false) String supplier //供应商
            
    )
    {	
		
		Map<String, Object> param = new HashMap<String, Object>();
		String url="";
		Integer userId = 0;
        String username = "";
        String password = "";
        if (request.getAttribute("userId") != null)
        {
            userId = Integer.valueOf(request.getAttribute("userId").toString());
            username = request.getAttribute("account").toString();
            password = request.getAttribute("password").toString();
        }
		
		setFilterParam(param, skuCode, queryNumber, signWarehouse, createDateStart
				, deptName, createDateEnd, signType, muWarehouseName, legaler
				, transferType, isTax, checkIds,stockIds,saleName,buyer,supplier);
		
        
        TableData<StorageReport> tableData =  this.storageReportService.getStorageReportList(param);
        
        
            
        Result result = storageReportService.exportExcel(tableData.getRows(), userId, username,  password,
    			request, response);
        if(result.getSuccess()){
        	url="redirect:index";
        	attr.addFlashAttribute("msg", result.getData().toString());
        }
        
        return url;      
        
    }
	
	
	private void setFilterParam( Map<String, Object> param ,
		         String skuCode,
		        String queryNumber,
		         String signWarehouse,
		         String createDateStart,
		         String department,
		         String createDateEnd,
		         String signType,
		         String muWarehouseName, //目标仓
		         String legalPersonName, // 法人
		         String transferType, //运输  0空 1海
		         String includeTax,  //含退税 # 1退税 2含税
		        String checkIds,
		        String stockId,
		        String saleName,
		        String buyer,
		        String supplier){
	  //部门
	  if (CommonUtil.isNotEmpty(department)) {
	          
	          String[] departments = department.split(",");
		      
		      StringBuffer buffer = new StringBuffer();
	    	  buffer.append("(");
	    	  
	    	  for(String str: departments){
	    		  buffer.append(" or s.deptName like '%")
	    		  .append(str).append("%'");
	    	  }
	    	  buffer.append(")");
	    	  buffer = buffer.replace(1,4, " ");  
	    	  
	          param.put("department", buffer.toString());
	  }
	  //销售人员
	  if (CommonUtil.isNotEmpty(saleName)) {
		  
		  String[] saleNames = saleName.split(",");
	      
	      StringBuffer buffer = new StringBuffer();
    	  buffer.append("(");
    	  
    	  for(String str: saleNames){
    		  buffer.append(" or s.saleName like '%")
    		  .append(str).append("%'");
    	  }
    	  buffer.append(")");
    	  buffer = buffer.replace(1,4, " ");  
    	  
          param.put("saleName", buffer.toString());
	      
	  }
	  
	  
		
	  if(CommonUtil.isNotEmpty(stockId)){
		 //法人主体
	  	 param.put("stockIds",stockId.split(","));
	   }
		
	   if(CommonUtil.isNotEmpty(legalPersonName)){
		 //法人主体
	  	 param.put("corporationIds",legalPersonName.split(","));
	   }
	  
	   //需求 目前不需要
	   if(CommonUtil.isNotEmpty(muWarehouseName)){
	  	 param.put("inventory",muWarehouseName.split(","));
	   }
	  
	   if(CommonUtil.isNotEmpty(transferType)){
		 //运输方式
	  	 param.put("transportType",transferType);
	   }
	   
	   if (CommonUtil.isNotEmpty(checkIds)) {
	  	 //勾选
	  	 param.put("checkIds", Arrays.asList(checkIds.split(",")));
		}
	   
	   if(CommonUtil.isNotEmpty(includeTax)){
		 //含税方式
	  	 param.put("isTax",includeTax);
	   }
	  //标志
	  if (CommonUtil.isNotEmpty(signType))
	  {
	      String sign_type = "3";
	      String currentUserId = String.valueOf(RoleUtil.getUser().get("userId"));
	      if (signType.equals("Y"))
	      {
	          param.put("signType", sign_type);
	          param.put("userId", currentUserId);
	      }
	      else if (signType.equals("N"))
	      {
	          param.put("signTypes", sign_type);
	          param.put("userIds", currentUserId);
	      }
	  }
	  //仓库id
	  if (CommonUtil.isNotEmpty(signWarehouse))
	  {
	      param.put("warehouseIds", signWarehouse.split(","));
	  }else if(signWarehouse!=null)
	  {
		  String[] arr = new String[1];
		  arr[0]=signWarehouse;
		  param.put("warehouseIds", arr);
	  }
	  
	  //skucode
	  if (CommonUtil.isNotEmpty(skuCode))
	  {
	      String regEx = "[' ']+"; // 一个或多个空格和中文逗号
	      Pattern p = Pattern.compile(regEx);
	      String s = skuCode.toUpperCase().trim();
	      Matcher m = p.matcher(s);
	      String[] skuCodes = m.replaceAll(",").split(",");
	      if (skuCodes.length > 1)
	      {
	          param.put("skuCodes", Arrays.asList(skuCodes));
	      }
	      else
	      {
	    	  List<String> list = new ArrayList<String>();
	    	  list.add(s);
	          param.put("skuCodes", list);
	      }
	  }
	  //采购员
	  if (CommonUtil.isNotEmpty(buyer))
	  {
	      String regEx = "[' ']+"; // 一个或多个空格和中文逗号
	      Pattern p = Pattern.compile(regEx);
	      String b = buyer.trim();
	      Matcher m = p.matcher(b);
	      String[] buyers = m.replaceAll(",").split(",");
	      if (buyers.length > 1)
	      {
	          param.put("buyers", Arrays.asList(buyers));
	      }
	      else
	      {
	    	  List<String> list = new ArrayList<String>();
	    	  list.add(b);
	          param.put("buyers",list);
	      }
	  }
	  //供应商
	  if (CommonUtil.isNotEmpty(supplier))
	  {
	      String regEx = "[' ']+"; // 一个或多个空格和中文逗号
	      Pattern p = Pattern.compile(regEx);
	      String s = supplier.trim();
	      Matcher m = p.matcher(s);
	      String[] suppliers = m.replaceAll(",").split(",");
	      if (suppliers.length > 1)
	      {
	          param.put("suppliers", Arrays.asList(suppliers));
	      }
	      else
	      {
	    	  List<String> list = new ArrayList<String>();
	    	  list.add(s);
	          param.put("suppliers", list);
	      }
	  }
	  //
	  if (CommonUtil.isNotEmpty(queryNumber))
	  {
	      String regEx = "[' ']+"; // 一个或多个空格和中文逗号
	      Pattern p = Pattern.compile(regEx);
	      String s = queryNumber.toUpperCase().trim();
	      Matcher m = p.matcher(s);
	
	      String[] purchase = m.replaceAll(",").split(",");
	      if (purchase.length > 1)
	      {
	          String reg1 = "^(RKB(\\d)+,){" + purchase.length + "}$";
	          boolean aqs = Pattern.compile(reg1).matcher(s + ",").find();
	
	          String reg2 = "^(CG(\\d)+,){" + purchase.length + "}$";
	          boolean acg = Pattern.compile(reg2).matcher(s + ",").find();
	
	          String reg3 = "^(IQC(\\d)+,){" + purchase.length + "}$";
	          boolean c = Pattern.compile(reg3).matcher(s + ",").find();
	
	          String reg4 = "^(RKE(\\d)+,){" + purchase.length + "}$";
	          boolean d = Pattern.compile(reg4).matcher(s + ",").find();
	
	          if (aqs)
	          {
	              param.put("storageNumbers", purchase);
	          }
	          if (d)
	          {
	              param.put("storageNumbers", purchase);
	          }
	          if (acg)
	          {
	              param.put("purchaseOrders", purchase);
	          }
	          if (c)
	          {
	              param.put("qualityInspectionNumbers", purchase);
	          }
	          if (!aqs && !acg && !c && !d)
	          {
	              String[] ss = "0".split(",");
	              param.put("purchaseOrders", ss);
	          }
	      }
	      else
	      {
	          param.put("purchaseOrder", queryNumber.trim());
	      }
	  }
	  //
	  if (CommonUtil.isNotEmpty(createDateStart))
	  {
	      param.put("createDateStart", createDateStart + " 00:00:00");
	  }
	  //
	  if (CommonUtil.isNotEmpty(createDateEnd))
	  {
	      param.put("createDateEnd", createDateEnd + " 23:59:59");
	  }
		
	}
	
	
	
}
