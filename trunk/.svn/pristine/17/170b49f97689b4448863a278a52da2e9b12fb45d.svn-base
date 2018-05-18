package com.aukey.report.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.aukey.report.domain.TransferOverseaInfo;
import com.aukey.report.domain.transferExportInfo;
import com.aukey.report.mapper.TransferOverseaMapper;
import com.aukey.report.service.TransferOverseaService;
import com.aukey.report.vo.InventoryResult;
import com.aukey.report.vo.InventoryResult.InventoryVO;
import com.aukey.util.AjaxResponse;
import com.aukey.util.CommonUtil;
import com.aukey.util.HttpUtil;

@Service
public class TransferOverseaServiceImpl implements TransferOverseaService {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private TransferOverseaMapper transferOverseaMapper;
	
	@Value("${inventory.api.url}")
	private String inventory_name_list_url;

	@Override
	public Integer saveTransferOversea(Sheet sheet,String fileName) {
		
		Integer integer=-1;
		Row row = sheet.getRow(0);
		short num = row.getLastCellNum();
		
		if(num!=30){
			integer=-1;
			return integer;
		}
		
		List<TransferOverseaInfo> list=null;
		try {
			list = setTransferOversea(sheet,fileName);
		} catch (Exception e) {
			integer=0;
			e.printStackTrace();
		}
		if(CommonUtil.isNotEmpty(list)){
			integer = transferOverseaMapper.saveTransferOversea(list);
			integer=1;
		}else{
			integer=2;
		}
		//integer :-1 导入表格的列数不对 0:导入的表格的单元格的数据格式不对,1导入成功 2导入失败
		return integer;
	}
	
	private List<TransferOverseaInfo> setTransferOversea(Sheet sheet,String fileName){
		
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
		List<TransferOverseaInfo> list = new ArrayList<TransferOverseaInfo>();
		
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			int check = 0;
			Row row = sheet.getRow(i);
			Cell cell1 = row.getCell(1);
			String cellValue1 = getCellValue(cell1);
			Integer shippingDateStr = null;
			if(CommonUtil.isNotEmpty(cellValue1)){
				shippingDateStr = (int) Double.valueOf(cellValue1).doubleValue();
			}
			check+=isEmptyStr(shippingDateStr);
			
			Cell cell2 = row.getCell(2);
			String cellValue2 = getCellValue(cell2);
			String warehouseName = null;
			if(CommonUtil.isNotEmpty(cellValue2)){
				warehouseName = cellValue2;
			}
			check+=isEmptyStr(warehouseName);
			Integer warehouseId = null;
			if(CommonUtil.isNotEmpty(warehouseName))
			for (InventoryVO inventoryVO : inventoryV_list) {
				if(warehouseName.equals(inventoryVO.getWarehouse_name())){
					warehouseId = inventoryVO.getStock_id();
				}
			}
			
			Cell cell3 = row.getCell(3);
			String skuCode = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell3))){
				skuCode = getCellValue(cell3);
			}
			
			check+=isEmptyStr(skuCode);
			
			Cell cell4 = row.getCell(4);
			String operateInner = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell4))){
				operateInner = getCellValue(cell4);
			}
			check+=isEmptyStr(operateInner);
			
			Cell cell5 = row.getCell(5);
			String shipmentCode = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell5))){
				shipmentCode = getCellValue(cell5);
			}
			check+=isEmptyStr(shipmentCode);
			
			Cell cell6 = row.getCell(6);
			String shipmentCodeOld = getCellValue(cell6);
			check+=isEmptyStr(shipmentCodeOld);
			
			Cell cell7 = row.getCell(7);
			String shipMark = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell7))){
				shipMark = getCellValue(cell7);
			}
			check+=isEmptyStr(shipMark);
			
			Cell cell8 = row.getCell(8);
			Integer shipQuantity = null ;
			if(CommonUtil.isNotEmpty(getCellValue(cell8))){
				shipQuantity = (int) Double.valueOf(getCellValue(cell8)).doubleValue();
			}
			check+=isEmptyStr(shipQuantity);
			
			Cell cell9 = row.getCell(9);
			Integer boxCount = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell9))){
				boxCount = (int) Double.valueOf(getCellValue(cell9)).doubleValue();
			}
			check+=isEmptyStr(boxCount);
			
			Cell cell10 = row.getCell(10);
			Integer boxs = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell10))){
				boxs = (int) Double.valueOf(getCellValue(cell10)).doubleValue();
			}
			check+=isEmptyStr(boxs);
			
			Cell cell11 = row.getCell(11);
			Double boxWeight = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell11))){
				boxWeight = Double.valueOf(getCellValue(cell11));
			}
			check+=isEmptyStr(boxWeight);
			
			Cell cell12 = row.getCell(12);
			Integer length = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell12))){
				length = (int) Double.valueOf(getCellValue(cell12)).doubleValue();
			}
			check+=isEmptyStr(length);
			
			Cell cell13 = row.getCell(13);
			Integer width = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell13))){
				length = (int) Double.valueOf(getCellValue(cell13)).doubleValue();
			}
			check+=isEmptyStr(width);
			
			Cell cell14 = row.getCell(14);
			Integer height = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell14))){
				length = (int) Double.valueOf(getCellValue(cell14)).doubleValue();
			}
			check+=isEmptyStr(height);
			
			Cell cell15 = row.getCell(15);
			String shipAddress = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell15))){
				shipAddress = getCellValue(cell15);
			}
			check+=isEmptyStr(shipAddress);
			
			Cell cell16 = row.getCell(16);
			String fbaCode = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell16))){
				fbaCode = getCellValue(cell16);
			}
			check+=isEmptyStr(fbaCode);
			
			Cell cell17 = row.getCell(17);
			String amazonAccount = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell17))){
				amazonAccount = getCellValue(cell17);
			}
			check+=isEmptyStr(amazonAccount);
			
			Cell cell18 = row.getCell(18);
			String creator = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell18))){
				creator = getCellValue(cell18);
			}
			check+=isEmptyStr(creator);
			
			Cell cell19 = row.getCell(19);
			String referenceId = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell19))){
				referenceId = getCellValue(cell19);
			}
			check+=isEmptyStr(referenceId);
			
			Cell cell20 = row.getCell(20);
			String receiver = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell20))){
				receiver = getCellValue(cell20);
			}
			check+=isEmptyStr(receiver);
			
			Cell cell21 = row.getCell(21);
			String countryCode = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell21))){
				countryCode = getCellValue(cell21);
			}
			check+=isEmptyStr(countryCode);
			
			Cell cell22 = row.getCell(22);
			String province = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell22))){
				province = getCellValue(cell22);
			}
			check+=isEmptyStr(province);
			
			Cell cell23 = row.getCell(23);
			String city = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell23))){
				city = getCellValue(cell23);
			}
			check+=isEmptyStr(city);
			
			Cell cell24 = row.getCell(24);
			String address1 = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell24))){
				address1 = getCellValue(cell24);
			}
			check+=isEmptyStr(address1);
			
			Cell cell25 = row.getCell(25);
			String postCode = null;
			if(CommonUtil.isNotEmpty(getCellValue(cell25))){
				postCode = getCellValue(cell25);
			}
			check+=isEmptyStr(postCode);
			
			Cell cell26 = row.getCell(26);
			String countryName = getCellValue(cell26);
			if(CommonUtil.isNotEmpty(getCellValue(cell26))){
				countryName = getCellValue(cell26);
			}
			/*if(cell26.getCellType()==Cell.CELL_TYPE_STRING){
				countryName = cell26.getStringCellValue();
			}else{
				if (cell26.getNumericCellValue()!=0.0){
					countryName = (int) cell26.getNumericCellValue() +"";
				}
			}*/
			check+=isEmptyStr(countryName);
			
			Cell cell27 = row.getCell(27);
			String shipmentId = getCellValue(cell27);
			if(CommonUtil.isNotEmpty(getCellValue(cell27))){
				shipmentId = getCellValue(cell27);
			}
			check+=isEmptyStr(shipmentId);
			
			Cell cell28 = row.getCell(28);
			String fnSku = getCellValue(cell28);
			if(CommonUtil.isNotEmpty(getCellValue(cell28))){
				fnSku = getCellValue(cell28);
			}
			check+=isEmptyStr(fnSku);
			
			if(check==28){
				continue;
			}
			
			Date shippingDate = new Date();
			if(CommonUtil.isNotEmpty(shippingDateStr)){
				shippingDate = new Date(shippingDateStr);
			}
			
			TransferOverseaInfo overseaInfo = new TransferOverseaInfo(shippingDate, warehouseId, skuCode, operateInner,
					shipmentCode, shipmentCodeOld, shipMark, shipQuantity, boxCount, boxs, boxWeight, length, width,
					height, shipAddress, fbaCode, amazonAccount, creator, referenceId, receiver, countryCode, province,
					city, address1, postCode+"", countryName, shipmentId, fnSku, 0, fileName);
			list.add(overseaInfo);
			
		}

		return list;
	}
	
	private int isEmptyStr(String obj){
		
		if(CommonUtil.isEmpty(obj) || "0.0".equals(obj)){
			return 1;
		}
		
		return 0;
	}
	private int isEmptyStr(Integer obj){
			
		if(CommonUtil.isEmpty(obj) || obj==0){
			return 1;
		}
		
		return 0;
	}
	private int isEmptyStr(Double obj){
		
		if(obj==null){
			return 1;
		}
		
		return 0;
	}

	@Override
	public Integer delExistsFile(String fileName) {
		// TODO Auto-generated method stub
		return transferOverseaMapper.delExistsFile(fileName);
	}

	@Override
	public List<transferExportInfo> selectTransferOverseaInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return transferOverseaMapper.selectTransferOverseaInfo(map);
	}
	public String getCellValue(Cell cell) {
		        String value = null;
		         if (cell != null) {
		             switch (cell.getCellType()) {
		             case Cell.CELL_TYPE_FORMULA:
		                 // cell.getCellFormula();
		                 try {
		                    value = String.valueOf(cell.getNumericCellValue());
		                 } catch (IllegalStateException e) {
		                    value = String.valueOf(cell.getRichStringCellValue());
		                 }
		                 break;
		            case Cell.CELL_TYPE_NUMERIC:
		                 value = String.valueOf(cell.getNumericCellValue());
		                 break;
		             case Cell.CELL_TYPE_STRING:
		                 value = String.valueOf(cell.getRichStringCellValue());
		                 break;
		             }
		         }
		 
		         return value;
	     }

	@Override
	public int count(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return transferOverseaMapper.count(map);
	}
}



