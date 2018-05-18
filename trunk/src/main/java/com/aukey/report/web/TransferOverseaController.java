package com.aukey.report.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.aukey.report.domain.transferExportInfo;
import com.aukey.report.domain.base.Result;
import com.aukey.report.service.TransferOverseaService;
import com.aukey.report.utils.export.DataField;
import com.aukey.report.utils.export.DataPage;
import com.aukey.report.utils.export.ExportDataSource;
import com.aukey.report.utils.export.excel.ExcelDataExportor;
import com.aukey.report.utils.export.excel.MODE;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.InventoryResult;
import com.aukey.report.vo.InventoryResult.InventoryVO;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CommonUtil;
import com.aukey.util.HttpUtil;

@RequestMapping("/TransferExport")
@Controller
public class TransferOverseaController {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private TransferOverseaService transferOverseaService;
	
	@Value("${inventory.api.url}")
	private String inventory_name_list_url;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String indexHtml(ModelMap mode){
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
					inventoryV_list.add(vo);
				}
			}
		}
		mode.addAttribute("inventory", inventoryV_list);
		
		return "TransferOversea";
	}
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public Result upLoadFile(MultipartFile file,ModelMap modelMap)
	{
		Result result = new Result();
		long size = file.getSize();
		logger.info(size);
		if(size<=0 || file==null){
			result.setData("传输的excel文件为空,请重新输入");
			result.setSuccess(false);
			return result;
		}
		try {
			Sheet sheet = getSheet(file);
			/*InputStream is = file.getInputStream();
			HSSFWorkbook wb = new HSSFWorkbook(is);
			Sheet sheet = wb.getSheetAt(0);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook();*/
			if(sheet!=null && sheet.getLastRowNum()>0){
				transferOverseaService.delExistsFile(file.getOriginalFilename());
				Integer flag = transferOverseaService.saveTransferOversea(sheet,file.getOriginalFilename());
				if(flag==1){
					result.setData("导入成功！！");
					result.setSuccess(true);
				}else if(flag==0){//integer :-1 导入表格的列数不对 0:导入的表格的单元格的数据格式不对,1导入成功 2导入失败
					result.setData("数据出现异常，请确认数据格式是否正确");
					result.setSuccess(false);
				}else if(flag==-1){
					result.setData("导入表格的列数不对,请确认数据格式");
					result.setSuccess(false);
				}else if(flag==2){
					result.setData("导入格式有误，请确认格式是否正确");
					result.setSuccess(false);
				}
			}
		} catch (IOException e) {
			result.setData("数据出现异常，请确认数据格式是否正确");
			result.setSuccess(false);
			e.printStackTrace();
			return result;
			
		}
		modelMap.addAttribute("result",result);
		return result;
	}
	
	public Sheet getSheet(MultipartFile file) throws IOException{
		
		String filename = file.getOriginalFilename();
		int lastIndex=filename.lastIndexOf(".");
		String lastName = filename.substring(lastIndex);
		InputStream is = file.getInputStream();
		Workbook workbook = null;
		if(".xls".equals(lastName)){
			workbook = new HSSFWorkbook(is);
		}else{
			workbook = new XSSFWorkbook(is);
		}
		return workbook.getSheetAt(0);
	}
	
	@RequestMapping(value="/search",method=RequestMethod.GET)
	@ResponseBody
	public TableData<transferExportInfo> search( @RequestParam(value = "limit", defaultValue = "20", required = false) int limit,
	        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNum,
	        @RequestParam(value = "sku", required = false) String sku,
	        @RequestParam(value = "warehouse", required = false) String warehouse){
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<String> listSku = new ArrayList<String>();
		if(CommonUtil.isNotEmpty(sku)){
			String[] skuArr = sku.split(",");
			for (String string : skuArr) {
				listSku.add(string);
			}
		}
		List<String> listWareHouse = new ArrayList<String>();
		if("-1".equals(warehouse)){
			
		}else{
			String[] wareHouseArr = warehouse.split(",");
			for (String string : wareHouseArr) {
				listWareHouse.add(string);
			}
		}
		map.put("skus", listSku);
		map.put("warehouseIds", listWareHouse);
		map.put("pageNum", (pageNum-1)*limit);
		map.put("limit", limit);
		TableData<transferExportInfo> tableData = new TableData<transferExportInfo>();
		List<transferExportInfo> info = transferOverseaService.selectTransferOverseaInfo(map);
		int count = transferOverseaService.count(map);
		setStockName(info);
		tableData.setRows(info);
		tableData.setTotal(count);
		return tableData;
	}
	
	@RequestMapping(value="/exportFile",method=RequestMethod.GET)
	@ResponseBody
	public Result exportFile(
	        @RequestParam(value = "sku", required = false) String sku,
	        @RequestParam(value = "warehouse", required = false) String warehouse,HttpServletRequest request,HttpServletResponse response )
	{
		Result result = new Result();
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<String> listSku = new ArrayList<String>();
		if(CommonUtil.isNotEmpty(sku)){
			String[] skuArr = sku.split(",");
			for (String string : skuArr) {
				listSku.add(string);
			}
		}
		List<String> listWareHouse = new ArrayList<String>();
		if("-1".equals(warehouse)){
			
		}else{
			String[] wareHouseArr = warehouse.split(",");
			for (String string : wareHouseArr) {
				listWareHouse.add(string);
			}
		}
		map.put("skus", listSku);
		map.put("warehouseIds", listWareHouse);
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String filename = "海外调拨数据列表_" + sdf.format(new Date());
			response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题  
			response.setCharacterEncoding("utf-8");  
			response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1")+".xls");  
			OutputStream os = response.getOutputStream();

			int columns = 8;
			DataField[] dataFields = new DataField[columns];
			dataFields[0] = new DataField("出库仓", "warehouseName");
			dataFields[1] = new DataField("Shipment ID", "shipmentId");
			dataFields[2] = new DataField("Fnsku", "fnSku");
			dataFields[3] = new DataField("SKU", "skuCode");
			dataFields[4] = new DataField("发货数量", "shipQuantity");
			dataFields[5] = new DataField("在途数量", "inWayQuantity");
			dataFields[6] = new DataField("收货数量", "receiveQuantity");
			dataFields[7] = new DataField("文件名", "fileName");
			DataPage pageParam = new DataPage(20000);
			new ExcelDataExportor<Object>(pageParam, dataFields, new ExportDataSource<Object>() {
				@Override
				public List getData() {
					Integer userId = 0;
			        String username = "";
			        String password = "";
			        if (request.getAttribute("userId") != null)
			        {
			            userId = Integer.valueOf(request.getAttribute("userId").toString());
			            username = request.getAttribute("account").toString();
			            password = request.getAttribute("password").toString();
			        }
					PageParam pageParam = new PageParam(1, 20000);
					List<transferExportInfo> info = transferOverseaService.selectTransferOverseaInfo(map);
					setStockName(info);
					List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
					if (!info.isEmpty()) {
						int temp = 0;
						for (int i = 0; i < info.size(); i++) {
							Map<String, Object> mapParam = new HashMap<String, Object>();
							transferExportInfo vo =  info.get(i);
							mapParam.put("warehouseName", vo.getWarehouseName());
							mapParam.put("shipmentId", vo.getShipmentId());
							mapParam.put("fnSku", vo.getFnSku());
							mapParam.put("skuCode", vo.getSkuCode());
							mapParam.put("shipQuantity", vo.getShipQuantity());
							mapParam.put("inWayQuantity", vo.getInWayQuantity());
							mapParam.put("receiveQuantity", vo.getReceiveQuantity());
							mapParam.put("fileName", vo.getFileName());
							lists.add(mapParam);
						}
					}
					return lists;
				}
			}, os, MODE.EXCEL).export();
			result.setSuccess(true);
			result.setData("导出成功");
		} catch (UnsupportedEncodingException e) {
			result.setSuccess(false);
			result.setData("导出出错");
			logger.error("下载报表", e);
		} catch (IOException e) {
			result.setSuccess(false);
			result.setData("导出出错");
			logger.error("IO", e);
		}
		
		
		
		
		return result;
	}
	
	public void setStockName(List<transferExportInfo> info){
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
							inventoryV_list.add(vo);
						}
					}
				}
				for (transferExportInfo Infosingle : info) {
					for (InventoryVO inventoryVO : inventoryV_list) {
						if(CommonUtil.isNotEmpty(Infosingle.getWarehouseIds())){
							if(inventoryVO.getStock_id()==Infosingle.getWarehouseIds()){
								Infosingle.setWarehouseName(inventoryVO.getWarehouse_name());
							}
						}
						
					}
				}
				
	}
	
	
}
