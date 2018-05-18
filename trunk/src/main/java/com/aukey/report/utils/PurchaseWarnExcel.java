package com.aukey.report.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.aukey.report.domain.PurchaseWarnReport;

/**
 * 预警表
 * @author wangxiaowen
 *
 */
public class PurchaseWarnExcel {
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("deprecation")
	public void setSheetTitle(HSSFWorkbook workbook, HSSFSheet sheet){
		// 设置打印格式
//		 HSSFPrintSetup printSetup = sheet.getPrintSetup();
		// 新建单元格样式
//		HSSFCellStyle cellStyle = workbook.createCellStyle();
//		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		 // 顶端居中对齐
//		cellStyle.setWrapText(true);

    	String[] titilName = new String[]
    			{"序号", 	"法人主体",	"供应商",	"采购单号", "需求单号","需求人","指派采购员时间",	"生成订单时间",	"sku",
    			 "sku名称",		"订单数量",		"币别",	"含税单价",	"未税单价",	"订单金额",
    			 "采购员",	"采购部门", 	"业务部门",	"入库数量",	"退货数量",	"欠货数量",	"超期时间",
    			 "超期数量","预计交货时间" };
		
		HSSFRow row1 = sheet.createRow(0);
		for (int i = 0; i < titilName.length; i++) {
			sheet.setColumnWidth(i, 3 * 800);
			HSSFCell cell = row1.createCell(i);
			cell.setCellValue(titilName[i]);
//			cell.setCellStyle(cellStyle);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void appendSheetRow(HSSFWorkbook workbook, HSSFSheet sheet, int startRowIndex,  List<PurchaseWarnReport> list){
		HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 顶端居中对齐
		//cellStyle1.setWrapText(true);
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		int temp = 0;
		for (PurchaseWarnReport info : list) {
			HSSFRow row = sheet.createRow(startRowIndex++);
//			row.setHeight((short) (700));
			HSSFCell cell1 = row.createCell(0);
			HSSFCell cell2 = row.createCell(1);
			HSSFCell cell3 = row.createCell(2);
			HSSFCell cell4 = row.createCell(3);
			HSSFCell cell5 = row.createCell(4);
			HSSFCell cell6 = row.createCell(5);
			HSSFCell cell7 = row.createCell(6);
			HSSFCell cell8 = row.createCell(7);
			HSSFCell cell9 = row.createCell(8);
			HSSFCell cell10 = row.createCell(9);
			HSSFCell cell11 = row.createCell(10);
			HSSFCell cell12 = row.createCell(11);
			HSSFCell cell13 = row.createCell(12);
			HSSFCell cell14 = row.createCell(13);
			HSSFCell cell15 = row.createCell(14);
			HSSFCell cell16 = row.createCell(15);
			HSSFCell cell17 = row.createCell(16);
			HSSFCell cell18 = row.createCell(17);
			HSSFCell cell19 = row.createCell(18);
			HSSFCell cell20 = row.createCell(19);
			HSSFCell cell21 = row.createCell(20);
			HSSFCell cell22 = row.createCell(21);
			HSSFCell cell23 = row.createCell(22);
			HSSFCell cell24 = row.createCell(23);
			try {
				cell1.setCellValue(++temp);
				cell2.setCellValue(info.getLegaler_name());
				cell3.setCellValue(info.getSupplier_name());
				cell4.setCellValue(info.getPurchase_no());
				cell5.setCellValue(info.getPurchase_warn_demand());
				cell6.setCellValue(info.getDeptName());
				cell7.setCellValue(info.getAssignTime()==null?"-":format2.format(info.getAssignTime()));
				cell8.setCellValue(format2.format(info.getPurchase_date()));
				cell9.setCellValue(info.getSku_code());
				cell10.setCellValue(info.getSku_name());
				cell11.setCellValue(info.getPurchase_count()+"");
				cell12.setCellValue(info.getCurrency()+"");
				cell13.setCellValue(formatterMoney(info.getPrice_tax()));//含税单价
				cell14.setCellValue(formatterMoney(info.getPrice_without_tax()));//不含税单价
				cell15.setCellValue(formatterMoney(info.getPurchase_sum()));//订单金额
				cell16.setCellValue(info.getBuyer_name());
				cell17.setCellValue(info.getPurchase_group_name());//采购部门
				cell18.setCellValue(info.getDepartment_id());
				cell19.setCellValue(removeSpace(info.getStock_count()+""));//入库数量										
				cell20.setCellValue(removeSpace(info.getReturn_count()+""));//退货数量
				cell21.setCellValue(removeSpace(info.getLack_count()+""));//欠货数量								
				cell22.setCellValue(removeSpace(info.getOut_date()+""));//超期天数
				cell23.setCellValue(removeSpace(info.getOut_date_count()+""));//超期数量
				cell24.setCellValue(format2.format(info.getBefore_stock_date()));//预计交货时间
				
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
				cell22.setCellStyle(cellStyle1);
				cell23.setCellStyle(cellStyle1);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	
	 public String removeSpace(String resource)   
	 {   
		 if(CommonUtil.isEmpty(resource)){
			 return "-";
		 }
	     StringBuffer buffer=new StringBuffer();   
	     byte[] ch = resource.getBytes();
	     for (int i = 0; i < ch.length; i++) {
	    	 if(ch[i]>=48 && ch[i]<=57) {
	        	 buffer.append(ch[i]-48); 	        	 
	         }
		}
	     return buffer.toString();   
	 }
	 public String formatterMoney(Double money){
			if(CommonUtil.isEmpty(money)){
				return "-";
		}
		DecimalFormat format = new DecimalFormat("0.00");
		
		return format.format(money);
	}
	
}
