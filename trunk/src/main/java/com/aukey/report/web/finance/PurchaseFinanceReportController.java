package com.aukey.report.web.finance;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.aukey.report.base.BaseController;
import com.aukey.report.domain.Dept;
import com.aukey.report.domain.SalePartInfo;
import com.aukey.report.domain.StrictSupplier;
import com.aukey.report.domain.base.Result;
import com.aukey.report.dto.PurchaseReportParam;
import com.aukey.report.service.ISaleGroupService;
import com.aukey.report.service.SendEmailService;
import com.aukey.report.service.finance.PurchaseFinanceReportService;
import com.aukey.report.utils.CommonUtil;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.WareHouseUtil;
import com.aukey.report.utils.export.DataField;
import com.aukey.report.utils.export.DataPage;
import com.aukey.report.utils.export.ExportDataSource;
import com.aukey.report.utils.export.excel.ExcelDataExportor;
import com.aukey.report.utils.export.excel.MODE;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.DepartmentResult;
import com.aukey.report.vo.LegalerResult;
import com.aukey.report.vo.PayMethodVO;
import com.aukey.report.vo.PurchaseReportVO;
import com.aukey.report.vo.DepartmentResult.DepartmentVO;
import com.aukey.util.AjaxResponse;
import com.aukey.util.HttpUtil;
import com.aukey.util.URLBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/report/finance")
@Controller
public class PurchaseFinanceReportController extends BaseController {
	private Logger logger = Logger.getLogger(getClass());

    @Value("${inventory.api.url}")
    private String inventory_name_list_url;

    @Value("${legaler.api.url}")
    private String legaler_list_url;

    @Value("${purchase.department.api.url}")
    private String all_department_list_url;

    @Value("${purchase.department.sales.api.url}")
    private String all_department_sales_list_url;

    @Value("${department.api.url}")
    private String department_list_url;
    
    @Value("${product.warehouse.url}")
	private String warehouseUrl;
    
    @Autowired
    private PurchaseFinanceReportService purchaseFinanceReportService;
    /**
     * 分组关联的销售组用户列表
     */
    @Value("${String.cas.relevance.group.users.url}")
    private String relevanceGroupUrl;

    /**
     * 查询采购人员对应的销售组
     */
    @Value("${String.cas.kai.url}")
    private String saleGroupUrl;
    
    /**
     * * 供应商模糊搜索
     */
    @Value("${String.aliaslike.url}")
    private String aliaslikeUrl;
   
    @Autowired
    private ISaleGroupService saleGroupService;
    
    @Value("${fba.smb.file.zip.path}")
    private String zipPath;
    /**
	 * 用户邮件
	 */
	@Value("${spring.mail.username}")
	private String from;
	/**
	 * 发邮件service
	 */
	@Autowired
	private SendEmailService sendNewEmail;
    

    @RequestMapping("/index")
    public String index(ModelMap modelMap)
    {
    	logger.info("采购入库页面");
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
        modelMap.addAttribute("tax", include_tax_list);

        // 获取法人主体列表
        Map<String, Object> legaler_resultMap = null;
        AjaxResponse legaler_result = HttpUtil.doGet(legaler_list_url);
        if (legaler_result.getData() != null)
        {
        	LegalerResult LegalerResult = null;
        	try
        	{
        		LegalerResult = JSON.parseObject(legaler_result.getData().toString(), LegalerResult.class);
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        		logger.error("转换内容"+legaler_result.getData().toString());
        		logger.error("转换法人主体列表报错"+e);
        	} 
        	if(LegalerResult != null){
        		
        		String str = LegalerResult.getData();
                ObjectMapper objMapper = new ObjectMapper();
                try
                {
                    legaler_resultMap = objMapper.readValue(str, HashMap.class);
                }
                catch (IOException e)
                {
                    logger.error("ObjectMapper对象装换不成功");
                }
        	}
            
        }
        modelMap.addAttribute("legalers", legaler_resultMap);

        // 获取国家列表
        // List<Country> countryList = countryService.getCountryName(new
        // HashMap<String, Object>());
        // modelMap.addAttribute("countrys", countryList);

        // List<Integer> whs = RoleUtil.getWareHouseIds();
        // List<Integer> dpts = RoleUtil.getSalesGroupIds();
     // 查询
        List<Map<String, Object>> allGroups = RoleUtil.getUserGroup();
        boolean allDept = true; //
        boolean purchaseManager = false; // 是否采购类但非采购员
        List<Integer> purchaseGroupIdList = new ArrayList<>(); // 采购主管分组id
        boolean purchaseUser = false; // 是否是采购人员
        Map<String,Object> purchaseMap = new HashMap<String,Object>();//采购组参数
        List<Integer> list = new ArrayList<Integer>();//采购组id集合
        for (Map<String, Object> loginGroup : allGroups)
        {
            String calssNameCode = loginGroup.get("cateogryCode").toString(); // 角色分类
            // 如果是财务相关类-应付、关税和出纳 财务拥有所有权限
            if (calssNameCode.equals("TARIFF") || calssNameCode.equals("CUSTOMS") || calssNameCode.equals("CASHIER"))
            {
                allDept = false;
                break;
            }
        }
        //获取入库仓名称
        String warehouseIds = "";
        WareHouseUtil w = new WareHouseUtil();
        if(allDept)
        {
        	for (Integer warehouseId : RoleUtil.getUserWarehouse())
            {
                warehouseIds += warehouseId + ",";
            }
            if (!warehouseIds.equals(""))
            {
                modelMap.addAttribute("signWarehouse", w.getWarehouseTwo(warehouseIds, warehouseUrl));
               
            }
        }
        // 获取仓库名称列表
        /*List<InventoryVO> inventoryV_list = new ArrayList<InventoryVO>();
        AjaxResponse inventory_name_result = HttpUtil.doGet(inventory_name_list_url);
        if (inventory_name_result.getData() != null)
        {
            InventoryResult inventoryResult = null;
			try {
				inventoryResult = JSON.parseObject(inventory_name_result.getData().toString(),
				    InventoryResult.class);
				List<InventoryVO> inventoryV_list_all = inventoryResult.getList();
	            for (InventoryVO vo : inventoryV_list_all)
	            {
	                // if (whs.contains(vo.getStock_id())) {
	                // inventoryV_list.add(vo);
	                // }
	                inventoryV_list.add(vo);
	            }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				logger.error("转换内容"+inventory_name_result.getData().toString());
        		logger.error("转换仓库名称列表错误"+e);
				
			}
            
        }
        modelMap.addAttribute("inventorys", inventoryV_list);
*/
        // 采购部门
        List<DepartmentVO> department_list = new ArrayList<DepartmentVO>();
        AjaxResponse all_department_result = HttpUtil.doGet(all_department_list_url);
        if (all_department_result.getData() != null)
        {
        	try{
        		 DepartmentResult DepartmentResult = JSON.parseObject(all_department_result.getData().toString(),
        	                DepartmentResult.class);
        	            department_list = DepartmentResult.getData();
        	            
        	}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				logger.error("转换内容"+all_department_result.getData().toString());
        		logger.error("转换采购部门错误"+e);
				
			}
           
        }

        if (allDept)
        {
            if (allGroups != null && allGroups.size() > 0)
            {
                for (Map<String, Object> loginGroup : allGroups)
                {
                    // 如果采购非采购员
                    if (loginGroup.get("cateogryCode").equals("PURCHASE") && !loginGroup.get("orgCode").equals("4003"))
                    {
                        purchaseManager = true;
                        purchaseGroupIdList.add((Integer) loginGroup.get("orgGroupId"));
                        list.add((Integer)loginGroup.get("orgGroupId"));
                    }
                    // 如果是采购员
                    if (loginGroup.get("orgCode").equals("4003") && loginGroup.get("cateogryCode").equals("PURCHASE"))
                    {
                        purchaseGroupIdList.add((Integer) loginGroup.get("orgGroupId"));
                        purchaseUser = true;
                        list.add((Integer)loginGroup.get("orgGroupId"));
                    }
                }
            }

        }

        // 需求部门
        /*List<DepartmentVO> department_sale_list = new ArrayList<DepartmentVO>();
        AjaxResponse all_department_sale_result = HttpUtil.doGet(all_department_sales_list_url);
        if (all_department_sale_result.getData() != null)
        {	
        	try{
        		 DepartmentResult DepartmentResult = JSON.parseObject(all_department_sale_result.getData().toString(),
        	                DepartmentResult.class);
        	            department_sale_list = DepartmentResult.getData();
        		
        	}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				logger.error("转换内容"+all_department_sale_result.getData().toString());
        		logger.error("转换需求部门错误"+e);
				
			}
           
        }*/

        List<Integer> listOwner = new ArrayList<>();
        // 如果是采购员
        List<Dept> listDept = new ArrayList<>();
        if (purchaseUser)
        {
            for (DepartmentVO dv : department_list)
            {
//                for (Integer pg : purchaseGroupIdList)
//                {
//                    if (dv.getGroupId() == pg.intValue())
//                    {
//                        Dept dept = new Dept();
//                        dept.setGroupId(dv.getGroupId());
//                        dept.setGroupName(dv.getGroupName());
//                        listOwner.add(dv.getGroupId());
//                        listDept.add(dept);
//                    }
//                }
                Dept dept = new Dept();
                dept.setGroupId(dv.getGroupId());
                dept.setGroupName(dv.getGroupName());
                listOwner.add(dv.getGroupId());
                listDept.add(dept);
            }
        }

        if (purchaseManager)
        {
            for (DepartmentVO dv : department_list)
            {
                for (Integer pg : purchaseGroupIdList)
                {
                    if (dv.getGroupId() == pg.intValue())
                    {
                        Dept dept = new Dept();
                        dept.setGroupId(dv.getGroupId());
                        dept.setGroupName(dv.getGroupName());
                        listOwner.add(dv.getGroupId());
                        listDept.add(dept);
                    }
                }
            }
        }
        //List<Dept> listRq= new ArrayList<>();
        purchaseMap.put("purchase_group_id", list);
        if (!allDept)
        {
        	purchaseMap.put("purchase_group_id", null);
            modelMap.addAttribute("departments", department_list);
          //采购部门对应的销售组
            List<SalePartInfo> saleByPurchase = saleGroupService.getSaleByPurchase(purchaseMap);
            modelMap.addAttribute("departmentsSale", saleByPurchase);
            //modelMap.addAttribute("departmentsSale", department_sale_list);
        }
        else
        {
           /* List<Map<String, Object>> listSale = orgService.querySaleDept(listOwner);
            for (Map<String, Object> map : listSale)
            {
                Dept dept = new Dept();
                dept.setGroupId(Integer.parseInt(map.get("groupId").toString()));
                dept.setGroupName(map.get("groupName").toString());
                listRq.add(dept);
            }
            modelMap.addAttribute("departmentsSale", listRq);*/
            modelMap.addAttribute("departments", listDept);
            List<SalePartInfo> saleByPurchase = saleGroupService.getSaleByPurchase(purchaseMap);
            modelMap.addAttribute("departmentsSale", saleByPurchase);
        }

        // 获取支付方式列表
        List<PayMethodVO> payMethodList = purchaseFinanceReportService.payMethodList();
        modelMap.addAttribute("paymethods", payMethodList);

        return "finance/purchase_warehouse_list_finance";
    }

    @RequestMapping("/search")
    @ResponseBody
    public TableData<PurchaseReportVO> search(PurchaseReportParam param,
        @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
        @RequestParam(value = "limit", defaultValue = "20") int limit, HttpServletRequest request)
    {
        PageParam pageParam = new PageParam(pageNumber, limit);
        Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
        PurchaseReportParam params=null;
        HttpSession session = request.getSession();
        if(pageNumber>1){//页面点击下一页bug修复
        	session.setAttribute("pageNumber",pageNumber);
        	params = (PurchaseReportParam) session.getAttribute("param");
        }else{
        	if(session.getAttribute("pageNumber")!=null){
    	        if(pageNumber!=(Integer)session.getAttribute("pageNumber")){
    	        	session.setAttribute("pageNumber",pageNumber);
    	        	params = (PurchaseReportParam) session.getAttribute("param");
    	        }else{
    	        	session.setAttribute("param",param);
    	        	params=param;
    	        }
            }else{
            	session.setAttribute("param",param);
            	params=param;
            }
        	
        }
        
        TableData<PurchaseReportVO> result = purchaseFinanceReportService.listPage(pageParam, params, userId);
        return result;
    }
    
    /**
     * 汇总入库数量和入库金额
     * @return
     */
    @RequestMapping("/searchCount")
    @ResponseBody
    public Result searchCount(PurchaseReportParam param,HttpServletRequest request)
    {
    	Result result = new Result();
    	Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
    	
    	result = purchaseFinanceReportService.selectCount(null, param, userId);
    	
    	return result;
    }

    @RequestMapping("/preExport")
    @ResponseBody
    public Result preExport(PurchaseReportParam param, HttpServletRequest request, HttpServletResponse response)
    {
    	Result result = new Result();
    	result.setSuccess(false);
    	Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
        int count = purchaseFinanceReportService.count(param,userId);
        if (count > 100000)
        {
        	result.setSuccess(true);
        }
        return result;
    }

    @RequestMapping("/export")
    public ModelAndView export(PurchaseReportParam param, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {	
        	
        	DecimalFormat df = new DecimalFormat("#.##");
        	
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
            /*String filename = "采购入库财务查询报表_" + sdf.format(new Date());
            response.setHeader("Content-disposition",
                "attachment; filename=" + URLEncoder.encode(filename + ".xls", "utf-8"));*/
          //处理Excel表格表名乱码问题
            String filename = "采购入库财务查询报表_" + sdf.format(new Date());
            response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题  
            response.setCharacterEncoding("utf-8");  
            response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");  
            OutputStream os = response.getOutputStream();
            int columns = 36;
            // if (RoleUtil.CanExportAll()) {
            // columns = 30;
            // }
            DataField[] dataFields = new DataField[columns];

            dataFields[0] = new DataField("序号", "no");
            dataFields[1] = new DataField("法人主体", "legaler");
            dataFields[2] = new DataField("供应商", "supplier");
            dataFields[3] = new DataField("采购单号", "purchase_no");
            dataFields[4] = new DataField("采购日期", "purchase_date");
            dataFields[5] = new DataField("SKU", "sku");
            dataFields[6] = new DataField("SKU名称", "sku_name");
            dataFields[7] = new DataField("增值税率", "tax_rate");
            dataFields[8] = new DataField("采购数量", "purchase_count");
            dataFields[9] = new DataField("不含税单价", "price_wihtout_tax");
            dataFields[10] = new DataField("含税单价", "price_tax");
            dataFields[11] = new DataField("采购金额", "purchase_sum");
            dataFields[12] = new DataField("采购本币金额", "purchase_currency_sum");
            dataFields[13] = new DataField("采购币别", "purchase_money_type");
            dataFields[14] = new DataField("采购员", "buyer");
            dataFields[15] = new DataField("采购部门", "department");
            dataFields[16] = new DataField("需求部门", "dept_name_xq");
            dataFields[17] = new DataField("入库单号", "stock_number");
            dataFields[18] = new DataField("入库日期", "stock_date");
            dataFields[19] = new DataField("仓库", "stock_name");

            dataFields[20] = new DataField("入库数量", "stock_count");
            dataFields[21] = new DataField("入库金额", "stock_sum");
            dataFields[22] = new DataField("入库本币金额", "stock_currency_sum");
            dataFields[23] = new DataField("品类", "category");
            dataFields[24] = new DataField("开票品名", "bill_name");
            dataFields[25] = new DataField("开票单位", "bill_unit");
            
            
            
            dataFields[26] = new DataField("未开票数量", "no_bill_count");

            dataFields[27] = new DataField("未开票金额", "noMakeInvoice");
            dataFields[28] = new DataField("未开票本币金额", "noMakeCurrencyInvoice");
            dataFields[29] = new DataField("已开票金额", "makeInvoice");
            dataFields[30] = new DataField("已开票本币金额", "makeCurrencyInvoice");
            dataFields[31] = new DataField("开票状态", "bill_status");
            dataFields[32] = new DataField("品牌", "brand");
            dataFields[33] = new DataField("型号", "version");
            dataFields[34] = new DataField("是否含税", "include_tax");
            dataFields[35] = new DataField("结算方式", "pay_type_name");

            // dataFields[19] = new DataField("占用库存", "usage_inventory");
            // dataFields[20] = new DataField("可用库存", "available_inventory");
            // dataFields[29] = new DataField("运输方式", "transport_type");

            // if (RoleUtil.CanExportAll()) {
            //
            // }
            DataPage pageParam = new DataPage(60000);
            new ExcelDataExportor<Object>(pageParam, dataFields, new ExportDataSource<Object>()
            {
                @Override
                public List getData()
                {
                    PageParam pageParam = new PageParam(1, exportMaxNumber);
                    TableData<PurchaseReportVO> pageResult = purchaseFinanceReportService.listPage(pageParam, param, userId);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    List<PurchaseReportVO> list = pageResult.getRows();
                    List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
                    if (!list.isEmpty())
                    {
                        int temp = 0;
                        for (int i = 0; i < list.size(); i++)
                        {
                            Map<String, Object> mapParam = new HashMap<String, Object>();
                            PurchaseReportVO vo = (PurchaseReportVO) list.get(i);
                            mapParam.put("no", ++temp);
                            mapParam.put("legaler", vo.getLegaler());

                            mapParam.put("supplier", vo.getSupplier());
                            mapParam.put("purchase_no", vo.getPurchase_no());
                            mapParam.put("purchase_date", format.format(vo.getPurchase_date()));
                            mapParam.put("sku", vo.getSku());
                            mapParam.put("sku_name", vo.getSku_name());
                            mapParam.put("tax_rate", vo.getTax_rate());
                            mapParam.put("purchase_count", vo.getPurchase_count());

                            mapParam.put("price_wihtout_tax", vo.getPrice_wihtout_tax());
                            mapParam.put("price_tax", vo.getPrice_tax());
                            mapParam.put("purchase_sum", vo.getPurchase_sum());
                            mapParam.put("purchase_money_type", vo.getPurchase_money_type());
                            mapParam.put("buyer", vo.getBuyer());
                            mapParam.put("department", vo.getDepartment());

                            mapParam.put("stock_number", vo.getStock_number());
                            mapParam.put("stock_date", format.format(vo.getStock_date()));
                            mapParam.put("stock_name", vo.getStock_name());
                            mapParam.put("stock_count", vo.getStock_count());
                            mapParam.put("stock_sum", vo.getStock_sum());
                            mapParam.put("usage_inventory", vo.getUsage_inventory());

                            mapParam.put("available_inventory", vo.getAvailable_inventory());
                            mapParam.put("category", vo.getCategory());
                            mapParam.put("bill_name", vo.getBill_name());
                            mapParam.put("bill_unit", vo.getBill_unit());
                            if(!"否".equals(vo.getInclude_tax())){
                            	mapParam.put("no_bill_count", vo.getNo_bill_count());
                            }else{
                            	mapParam.put("no_bill_count", " ");
                            }
                            
                            mapParam.put("bill_status", vo.getBill_status());

                            mapParam.put("brand", vo.getBrand());
                            mapParam.put("version", vo.getVersion());
                            mapParam.put("include_tax", vo.getInclude_tax());
                            mapParam.put("transport_type", vo.getTransport_type());
                            mapParam.put("pay_type_name", vo.getPay_type_name());

                            mapParam.put("dept_name_xq", vo.getDeptNamexq());
                            mapParam.put("purchase_currency_sum", formatterMoney(vo.getPurchaseCurrencySum()));
                            mapParam.put("stock_currency_sum", formatterMoney(vo.getStockCurrencySum()));
                            mapParam.put("noMakeCurrencyInvoice", formatterMoney(vo.getNoMakeInvoiceCurrency()));
                            mapParam.put("makeCurrencyInvoice", formatterMoney(vo.getMakeInvoiceCurrency()));
                          
                            if(!"否".equals(vo.getInclude_tax())){
                            	
                            	mapParam.put("noMakeInvoice",df.format(vo.getNo_bill_count()*vo.getPrice_tax()));
                            }else{
                            	mapParam.put("noMakeInvoice"," ");
                            }
                            
                            
                            if(!"否".equals(vo.getInclude_tax())){
                            	mapParam.put("makeInvoice", df.format((vo.getStock_count()-vo.getNo_bill_count())*vo.getPrice_tax()));
                            	
                            }else{
                            	mapParam.put("makeInvoice"," ");
                            }
                            
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
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("下载报表", e);
        }
        catch (IOException e)
        {
            logger.error("IO", e);
        }
        ModelAndView mv = new ModelAndView("purchase_warehouse_list");
        return mv;
    }
    
    /**
	 * 导出采购报表
	 * 
	 * @param statmentNo
	 * @param httpServletRequest
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/emailExport", method = RequestMethod.GET)
	@ResponseBody
	public synchronized void emailExport(HttpServletRequest request,PurchaseReportParam param) // 第三方编号
	{
		 String userAccount = RoleUtil.getUserAccount();
		 Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
		 PageParam pageParam = new PageParam(1, exportMaxNumber);
		/************************** 开始调用 *******************************/
		 AjaxResponse response = new AjaxResponse();
		 Map<String, Object> conditon = new HashMap<>();
	       
		 ExecutorService exec = Executors.newCachedThreadPool();
         ArrayList<Future<AjaxResponse>>  results = new ArrayList<Future<AjaxResponse>>();
         results.add(exec.submit(new OrderExcelSyncThread(param,purchaseFinanceReportService,
             sendNewEmail,userAccount,from,zipPath,userId,pageParam)));
         for(Future<AjaxResponse> fs : results){
             try{
                 response =  fs.get();//可以调用很多方法，包括是否工作等等
             }catch(Exception e){
                 logger.error("线程返回值获取失败：", e);
             }finally{
                 exec.shutdown();
             }
         }
		/************************** 调用结束 *******************************/
	}
    
    /**
	  *	获取供应商信息
	  *
	  */
	 @RequestMapping("/getSupplierInfo")
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
	 public String formatterMoney(Double money){
			if(CommonUtil.isEmpty(money)){
				return "-";
		}
		DecimalFormat format = new DecimalFormat("0.00");
		
		return format.format(money);
	}
	 
}
