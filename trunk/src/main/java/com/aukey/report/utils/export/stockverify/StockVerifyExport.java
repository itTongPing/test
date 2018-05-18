package com.aukey.report.utils.export.stockverify;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.aukey.report.domain.stockverify.StockVerifyReport;
import com.aukey.util.CommonUtil;

public class StockVerifyExport {

	
	
	
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(getClass());
	
	public void setSheetTitle(Workbook workbook, Sheet sheet){
    	String[] titilName = new String[]
    			{"序号", 	"事业部","SKU名称" ,"品类","仓库名称","系统SKU","系统箱数","系统单箱数量",	"系统数量",	"长", "宽", "高", "体积", "重量", "单价",	
    			"所在仓SKU",	"所在仓库存","差异库存"};
    	int[] widthArray = {2000,4000,6000,6000,6000,4000,2000,2000,2000,2000,2000,2000,2000,2000,2000,4000,2000,2000};	
		Row row1 = sheet.createRow(0);
		for (int i = 0; i < widthArray.length; i++) {
			sheet.setColumnWidth(i, widthArray[i]);
			Cell cell = row1.createCell(i);
			cell.setCellValue(titilName[i]);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void appendSheetRow(Workbook workbook, Sheet sheet, int startRowIndex,  List<StockVerifyReport> list)
	{
		CellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		
		try {
			for (StockVerifyReport stockVerifyReport : list) 
			{
				Row row = sheet.createRow(startRowIndex);
				
				Cell cell1 = row.createCell(0);//序号
				Cell cell2 = row.createCell(1);//事业部
				Cell cell3 = row.createCell(2);//SKU名称
				Cell cell4 = row.createCell(3);//品类
				Cell cell5 = row.createCell(4);//仓库名称
				Cell cell6 = row.createCell(5);//系统SKU
				Cell cell7 = row.createCell(6);//系统箱数
				Cell cell8 = row.createCell(7);//系统单箱数量
				Cell cell9 = row.createCell(8);//系统数量
				Cell cell10 = row.createCell(9);//长
				Cell cell11 = row.createCell(10);//宽
				Cell cell12 = row.createCell(11);//高
				Cell cell13 = row.createCell(12);//体积
				Cell cell14 = row.createCell(13);//重量
				Cell cell15 = row.createCell(14);//单价
				Cell cell16 = row.createCell(15);//所在仓SKU
				Cell cell17 = row.createCell(16);//所在仓库存
				Cell cell18 = row.createCell(17);//差异库存
				
				cell1.setCellValue(startRowIndex);//序号
				cell2.setCellValue(isFuValue(stockVerifyReport.getDepartmentName()));//事业部
				cell3.setCellValue(isFuValue(stockVerifyReport.getSkuName()));//SKU名称
				cell4.setCellValue(isFuValue(stockVerifyReport.getCategoryName()));//品类
				cell5.setCellValue(isFuValue(stockVerifyReport.getStockName()));//仓库名称
				cell6.setCellValue(isFuValue(stockVerifyReport.getSkuCode()));//系统SKU
				cell7.setCellValue(stockVerifyReport.getSystemBoxNumber());//系统箱数
				cell8.setCellValue(stockVerifyReport.getSystemSingleBoxNumber());//系统单箱数量
				cell9.setCellValue(stockVerifyReport.getSystemNumberSum());//系统数量
				cell10.setCellValue(stockVerifyReport.getLength().doubleValue());//长
				cell11.setCellValue(stockVerifyReport.getWidth().doubleValue());//宽
				cell12.setCellValue(stockVerifyReport.getHeight().doubleValue());//高
				cell13.setCellValue(stockVerifyReport.getVolume().doubleValue());//体积
				cell14.setCellValue(stockVerifyReport.getWeight().doubleValue());//重量
				cell15.setCellValue(stockVerifyReport.getPrice()==null?0.00:stockVerifyReport.getPrice().doubleValue());//单价
				cell16.setCellValue(stockVerifyReport.getWsku());//所在仓sku
				cell17.setCellValue(stockVerifyReport.getInStockCount());//所在仓库存
				cell18.setCellValue(stockVerifyReport.getDifferenceQuantity());//差异库存
				
				startRowIndex++;
			}
		} catch (Exception e) {
			logger.error("导出失败", e);
			e.printStackTrace();
		}
	}
	
	private String isFuValue(String str)
	{
		return CommonUtil.isEmpty(str)?"-":str;
	}
	
	
}
