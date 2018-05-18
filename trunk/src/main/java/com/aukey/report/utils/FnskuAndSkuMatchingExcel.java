package com.aukey.report.utils;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


import com.aukey.report.domain.FnskuAndSkuMatching;



public class FnskuAndSkuMatchingExcel {
	
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(getClass());
	
	public void setSheetTitle(Workbook workbook, Sheet sheet){
		// 设置打印格式
//		 HSSFPrintSetup printSetup = sheet.getPrintSetup();
		// 新建单元格样式
//		HSSFCellStyle cellStyle = workbook.createCellStyle();
//		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		 // 顶端居中对齐
//		cellStyle.setWrapText(true);

    	String[] titilName = new String[]
    			{"序号","调出仓","目标仓", "SKU", "SKU名称", "调出仓sku", "单品重量",	
    			"单品长", "单品宽", "单品高", "单品体积", "单箱数量", "单箱重量", "单箱长", 
    			"单箱宽", "单箱高", "单箱体积", "shipmentID","fnsku","采购单价","销售部门","产品照片url"};
    	int[] widthArray = {2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000};	
		Row row1 = sheet.createRow(0);
		for (int i = 0; i < widthArray.length; i++) {
			sheet.setColumnWidth(i, widthArray[i]);
			Cell cell = row1.createCell(i);
			cell.setCellValue(titilName[i]);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void appendSheetRow(Workbook workbook, Sheet sheet, int startRowIndex,  List<FnskuAndSkuMatching> list){
		CellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 顶端居中对齐
		//cellStyle1.setWrapText(true);
		int temp = 1;
		try 
		{
			for (FnskuAndSkuMatching info : list) 
			{
				Row row = sheet.createRow(startRowIndex++);
				
				Cell cell1 = row.createCell(0);
				Cell cell2 = row.createCell(1);
				Cell cell3 = row.createCell(2);
				Cell cell4 = row.createCell(3);
				Cell cell5 = row.createCell(4);
				Cell cell6 = row.createCell(5);
				Cell cell7 = row.createCell(6);
				Cell cell8 = row.createCell(7);
				Cell cell9 = row.createCell(8);
				Cell cell10 = row.createCell(9);
				Cell cell11 = row.createCell(10);
				Cell cell12 = row.createCell(11);
				Cell cell13 = row.createCell(12);
				Cell cell14 = row.createCell(13);
				Cell cell15 = row.createCell(14);
				Cell cell16 = row.createCell(15);
				Cell cell17 = row.createCell(16);
				Cell cell18 = row.createCell(17);
				Cell cell19 = row.createCell(18);
				Cell cell20 = row.createCell(19);
				Cell cell21 = row.createCell(20);
				Cell cell22 = row.createCell(21);
				
				cell1.setCellValue(temp);
				cell2.setCellValue(isEmpty(info.getFromWarehouseName()));		
				cell3.setCellValue(isEmpty(info.getToWarehouseName()));
				cell4.setCellValue(isEmpty(info.getSku()));
				cell5.setCellValue(isEmpty(info.getSkuName()));
				cell6.setCellValue(isEmpty(info.getFromWsskuBox()));
				cell7.setCellValue(isEmpty(info.getGoodsWeight()));
				cell8.setCellValue(isEmpty(info.getGoodsPackageLength()));
				cell9.setCellValue(isEmpty(info.getGoodsPackageWidth()));
				cell10.setCellValue(isEmpty(info.getGoodsPackageHeight()));
				cell11.setCellValue(isEmpty(info.getGoodsPackageVolume()));
				cell12.setCellValue(isEmpty(info.getBoxCount()));
				cell13.setCellValue(isEmpty(info.getBoxWeight()));
				cell14.setCellValue(isEmpty(info.getBoxLength()));
				cell15.setCellValue(isEmpty(info.getBoxWidth()));
				cell16.setCellValue(isEmpty(info.getBoxHeight()));
				cell17.setCellValue(isEmpty(info.getBoxVolume()));
				cell18.setCellValue(isEmpty(info.getShipmentID()));
				cell19.setCellValue(isEmpty(info.getFnsku()));
				cell20.setCellValue(isEmpty(info.getTaxUnitPrice()));
				cell21.setCellValue(isEmpty(info.getSaleDepartment()));
				cell22.setCellValue(isEmpty(info.getPictureUrl()));
				
				cell1.setCellStyle(cellStyle1);
				cell2.setCellStyle(cellStyle1);
				cell3.setCellStyle(cellStyle1);
				cell4.setCellStyle(cellStyle1);
				cell5.setCellStyle(cellStyle1);				
				cell6.setCellStyle(cellStyle1);
				cell7.setCellStyle(cellStyle1);				
				cell8.setCellStyle(cellStyle1);				
				cell9.setCellStyle(cellStyle1);				
				cell10.setCellStyle(cellStyle1);				
				cell11.setCellStyle(cellStyle1);
				cell12.setCellStyle(cellStyle1);				
				cell13.setCellStyle(cellStyle1);				
				cell14.setCellStyle(cellStyle1);				
				cell15.setCellStyle(cellStyle1);
				cell16.setCellStyle(cellStyle1);				
				cell17.setCellStyle(cellStyle1);
				cell18.setCellStyle(cellStyle1);				
				cell19.setCellStyle(cellStyle1);
				cell20.setCellStyle(cellStyle1);
				cell21.setCellStyle(cellStyle1);
				
				
				if (!isEmpty(info.getPictureUrl()).equals("-")) {
					CellStyle hlinkstyle = workbook.createCellStyle();
				    Font hlinkfont = workbook.createFont();
				    hlinkfont.setUnderline(Font.U_SINGLE);
				    hlinkfont.setColor(HSSFColor.BLUE.index);
				    hlinkstyle.setFont(hlinkfont);
				    hlinkstyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					CreationHelper createHelper = workbook.getCreationHelper();
					org.apache.poi.ss.usermodel.Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
					link.setAddress(isEmpty(info.getPictureUrl()));
					cell22.setHyperlink((org.apache.poi.ss.usermodel.Hyperlink) link);
					cell22.setCellStyle(hlinkstyle);
				}else{
					cell22.setCellStyle(cellStyle1);	
				}
				temp++;
			}
		} catch (Exception e) 
		{
			logger.error("导出失败", e);
			e.printStackTrace();
			
		}
	}
	

	private String isEmpty(BigDecimal object) {
		if(object==null)
		{
			return "-";
		}else
		{			
			DecimalFormat decimalFormat = new DecimalFormat("###################.################");
			String a=decimalFormat.format(object);
			return decimalFormat.format(object);
		}
	}

	private String isEmpty(Double object) {
		if(object==null || object.equals(0.00)){
			return "-";
		}
		
		DecimalFormat decimalFormat = new DecimalFormat("###################.################");
		String a=decimalFormat.format(object);
		return decimalFormat.format(object);
	}

	private String isEmpty(String object)
	{
		
		if(object==null || object.length()==0 || "".equals(object) || "N/A".equals(object))
		{
			return "-";
		}else
		{
			return object;
		}
	}

	private String isEmpty(Integer obejct)
	{
		if(obejct==null)
		{
			return "-";
		}else
		{
			return obejct.toString();
		}
	}
}
