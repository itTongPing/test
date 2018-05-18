package com.aukey.report.web.centerstock;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.aukey.report.domain.centerstock.CenterStockReport;

public class CenterStockReportExcel {
	
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
    			{"序号", "UPC", "SKU", "SKU名称",	"事业部","过去 24 小时发货数量","过去 7 天发货数量","过去 30 天发货数量","过去 90 天发货数量","过去 180 天发货数量","过去 365 天发货数量", 
    			"FBA退货在途", "FBA库存", "海外仓转运在途", "海外仓库存", "头程空运在途",	
    			"头程海运在途", "头程铁路在途", "国内中转仓库存", "采购订单在途", "采购周期"};
    	int[] widthArray = {2000,6000,4000,6000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000,2000};	
		Row row1 = sheet.createRow(0);
		for (int i = 0; i < widthArray.length; i++) {
			sheet.setColumnWidth(i, widthArray[i]);
			Cell cell = row1.createCell(i);
			cell.setCellValue(titilName[i]);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void appendSheetRow(Workbook workbook, Sheet sheet, int startRowIndex,  List<CenterStockReport> list){
		CellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 顶端居中对齐
		//cellStyle1.setWrapText(true);
		int temp = 1;
		try 
		{
			for (CenterStockReport info : list) 
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
				
				
				cell1.setCellValue(temp);
				cell2.setCellValue(isEmpty(info.getUpc()));
				cell3.setCellValue(isEmpty(info.getSku()));
				cell4.setCellValue(isEmpty(info.getSkuName()));
				cell5.setCellValue(isEmpty(info.getDeptName()));
				
				cell6.setCellValue(isEmpty(info.getShippedLast24hs()));
				cell7.setCellValue(isEmpty(info.getShippedLast7ds()));
				cell8.setCellValue(isEmpty(info.getShippedLast30ds()));
				cell9.setCellValue(isEmpty(info.getShippedLast90ds()));
				cell10.setCellValue(isEmpty(info.getShippedLast180ds()));
				cell11.setCellValue(isEmpty(info.getShippedLast365ds()));
				
				
				
				
				
				
				cell12.setCellValue(isEmpty(info.getFbaTransfer().toString()));
				cell13.setCellValue(isEmpty(info.getFbaStockCount().toString()));
				cell14.setCellValue(isEmpty(info.getOverseaStockTransfer().toString()));
				cell15.setCellValue(isEmpty(info.getOverseaStockCount().toString()));
				cell16.setCellValue(isEmpty(info.getFirstAirTransfer().toString()));
				cell17.setCellValue(isEmpty(info.getFirstShipTransfer().toString()));
				cell18.setCellValue(isEmpty(info.getFirstTrainsTransfer().toString()));
				cell19.setCellValue(isEmpty(info.getTransferWarehouseCount().toString()));
				cell20.setCellValue(isEmpty(info.getPurchaseTransfer().toString()));
				cell21.setCellValue(isEmpty(info.getPurchaseCycle()));
				
				
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
				temp++;
			}
		} catch (Exception e) 
		{
			logger.error("导出失败", e);
			e.printStackTrace();
			
		}
	}
	
	private String isEmpty(String object)
	{
		
		if(object==null || object.length()==0)
		{
			return "-";
		}else
		{
			return object;
		}
	}

	private int isEmpty(Integer obejct)
	{
		if(obejct==null)
		{
			return 0;
		}else
		{
			return obejct.intValue();
		}
	}
}
