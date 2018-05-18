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

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.aukey.report.domain.DepartmentResult;
import com.aukey.report.domain.InventoryResult;
import com.aukey.report.domain.StorageReport;
import com.aukey.report.domain.WarehouseAssessmentReport;
import com.aukey.report.domain.DepartmentResult.DepartmentVO;
import com.aukey.report.domain.InventoryResult.InventoryVO;
import com.aukey.report.domain.base.LegalerResult;
import com.aukey.report.domain.base.TableData;
import com.aukey.report.service.StorageReportService;
import com.aukey.report.service.WarehouseAssessmentReportService;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.WareHouseUtil;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CommonUtil;
import com.aukey.util.HttpUtil;

/**
 * 入库报表
 * 
 * @author xiehz
 *
 */
@RequestMapping("/report/warehouseAssessmentReport")
@Controller
public class WarehouseAssessmentReportController {
	
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private WarehouseAssessmentReportService warehouseAssessmentReportService;
	
	@Value("${inventory.api.url}")
	private String inventory_name_list_url;
	
	@Value("${product.warehouse.url}")
	private String warehouseUrl;
	
	
	 /**
     * 跳转到入库列表
     *
     * @return
     */
    @RequestMapping("/index")
   
    public String index(ModelMap modelMap)
    {
    	
    	
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
        
        return "Warehouse_Assessment_list";
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
    public TableData<WarehouseAssessmentReport> showReceiveList(
    	HttpServletRequest request,HttpServletResponse response,
        @RequestParam(value = "limit", defaultValue = "20", required = false) int limit,
        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
        @RequestParam(value = "queryNumber", required = false) String queryNumber,//入库/采购/质检单号
        @RequestParam(value = "skuCode",  required = false) String skuCode_s,//sku名称
        @RequestParam(value = "createDateStart", required = false) String createDateStart,//入库开始时间
        @RequestParam(value = "createDateEnd",  required = false) String createDateEnd,//入库结束时间
        @RequestParam(value = "inspector_name",  required = false) String inspector_name,//质检员
        @RequestParam(value = "inspector_result",  required = false) String inspector_result,//质检结果
        @RequestParam(value = "sale_name",  required = false) String sale_name,//采购员
        @RequestParam(value = "leaderCreateTimeStart",  required = false) String leaderCreateTimeStart,//采购订单审核时间开始时间
        @RequestParam(value = "leaderCreateTimeEnd",  required = false) String leaderCreateTimeEnd,//采购订单审核时间结束时间
        @RequestParam(value = "storageUserName",  required = false) String storageUserName,//入库员
        @RequestParam(value = "checkId",  required = false) String checkId,//入库员
        
        @RequestParam(value = "storageCreateDateStart",  required = false) String storageCreateDateStart,//入库开始时间
        @RequestParam(value = "storageCreateDateEnd",  required = false) String storageCreateDateEnd//入库结束时间
        
    		)
    {
        logger.info("------------开始入库列表查询----------------------");
        TableData<WarehouseAssessmentReport> tableData = new TableData<WarehouseAssessmentReport>();
         Map<String, Object> param = new HashMap<>();
        
        param.put("page", (pageNumber - 1) * limit);
        param.put("limit", limit);
   
        setFilterParam(param, queryNumber, skuCode_s, createDateStart, createDateEnd, inspector_name
        		, inspector_result, sale_name, leaderCreateTimeStart, leaderCreateTimeEnd
        		, storageUserName,checkId,storageCreateDateStart,storageCreateDateEnd);
        
        tableData = this.warehouseAssessmentReportService.getWarehouseAssessmentReportList(param);
        logger.info("------------入库列表查询结束----------------------");
        return tableData;
    }

	private void setFilterParam(Map<String, Object> param, String queryNumber, String skuCode_s, String createDateStart,
			String createDateEnd, String inspector_name, String inspector_result, String sale_name,
			String leaderCreateTimeStart, String leaderCreateTimeEnd, String storageUserName,String checkId,
			String storageCreateDateStart,String storageCreateDateEnd) {
		// TODO Auto-generated method stub
		
		if (CommonUtil.isNotEmpty(checkId))
		{
			param.put("checkIds", Arrays.asList(checkId.split(",")));
		}
		
		if (CommonUtil.isNotEmpty(skuCode_s))
		{	
			if(skuCode_s.split(",").length > 1){
				
				param.put("skucodes", Arrays.asList(skuCode_s.split(",")));
			}else{
				
				param.put("skucode", skuCode_s);
			}
			
		}
		
		if (CommonUtil.isNotEmpty(createDateStart))
		{
		      param.put("createDateStart", createDateStart + " 00:00:00");
		}
		  //
		if (CommonUtil.isNotEmpty(createDateEnd))
		{
		      param.put("createDateEnd", createDateEnd + " 23:59:59");
		}
		
		if (CommonUtil.isNotEmpty(inspector_name))
		{
			param.put("inspectorNames", Arrays.asList(inspector_name.split(",")));
		}
		
		if (CommonUtil.isNotEmpty(inspector_result))
		{
			param.put("inspector_result", inspector_result);
		}

		if (CommonUtil.isNotEmpty(sale_name))
		{
			param.put("saleNames",Arrays.asList(sale_name.split(",")) );
		}
		
		
		if (CommonUtil.isNotEmpty(leaderCreateTimeStart))
		{
		      param.put("leaderCreateTimeStart", leaderCreateTimeStart + " 00:00:00");
		}
		  //
		if (CommonUtil.isNotEmpty(leaderCreateTimeEnd))
		{
		      param.put("leaderCreateTimeEnd", leaderCreateTimeEnd + " 23:59:59");
		}
		
		
		if (CommonUtil.isNotEmpty(storageCreateDateStart))
		{
		      param.put("storageCreateDateStart", storageCreateDateStart + " 00:00:00");
		}
		  //
		if (CommonUtil.isNotEmpty(storageCreateDateEnd))
		{
		      param.put("storageCreateDateEnd", storageCreateDateEnd + " 23:59:59");
		}
		
		
		
		if (CommonUtil.isNotEmpty(storageUserName))
		{
		      param.put("storageUserNames", Arrays.asList(storageUserName.split(",")));
		}
		
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
		              param.put("storageNumbers", purchase);//入库单号
		          }
		          if (d)
		          {
		              param.put("storageNumbers", purchase);//入库单号
		          }
		          if (acg)
		          {
		              param.put("purchaseOrders", purchase);//采购单
		          }
		          if (c)
		          {
		              param.put("qualityInspectionNumbers", purchase);//质检单
		          }
		          if (!aqs && !acg && !c && !d)
		          {
		              String[] ss = "0".split(",");
		              param.put("purchaseOrders", ss);//采购单
		          }
		      }
		      else
		      {
		          param.put("purchaseOrder", queryNumber.trim());
		      }
		  }
		
		
		
		
		
	}
	
    
	
}
