package com.aukey.report.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.aukey.report.vo.TransferReportVO;

public class TransferReportExcel {
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(getClass());
	@SuppressWarnings("deprecation")
	public void setSheetTitle(HSSFWorkbook workbook, HSSFSheet sheet){
    	String[] titilName = new String[]
    			{"序号", 	"调拨单号",	"单据日期",	"法人主体", 	"出库日期",	"调出仓",
    			 "线路仓",		"目标仓",		"SKU",	"SKU名称",	"箱号",	"单箱数量",
    			 "运输方式",		"货运毛重",	"长",	"宽",	"高",	"体积",
    			 "调拨状态","是否含税","站点","店铺","shipment_id","fnsku","sellersku","采购金额","税率","退税率",
    			 "报关单号","出口日期","海关单号","采购单价","报关金额","币种","项号"};
		
		HSSFRow row1 = sheet.createRow(0);
		for (int i = 0; i < titilName.length; i++) {
			sheet.setColumnWidth(i, 3 * 1500);
			HSSFCell cell = row1.createCell(i);
			cell.setCellValue(titilName[i]);
		}
	}
	@SuppressWarnings("deprecation")
	public void appendSheetRow(HSSFWorkbook workbook, HSSFSheet sheet, int startRowIndex,  List<TransferReportVO> list){
		HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 顶端居中对齐
		cellStyle1.setWrapText(true);
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int temp = 0;
		for (TransferReportVO info : list) {
			HSSFRow row = sheet.createRow(startRowIndex++);
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
			HSSFCell cell25 = row.createCell(24);
			HSSFCell cell26 = row.createCell(25);
			HSSFCell cell27 = row.createCell(26);
			HSSFCell cell28 = row.createCell(27);
			HSSFCell cell29 = row.createCell(28);
			HSSFCell cell30 = row.createCell(29);
			HSSFCell cell31 = row.createCell(30);
			HSSFCell cell32 = row.createCell(31);
			HSSFCell cell33 = row.createCell(32);
			HSSFCell cell34 = row.createCell(33);
			HSSFCell cell35 = row.createCell(34);
			try {
				cell1.setCellValue(++temp);
				cell2.setCellValue(info.getTransfer_no());
				if(info.getTransfer_date() != null){
					cell3.setCellValue(format2.format(info.getTransfer_date()));
				}
				
				cell4.setCellValue(info.getLegal_name());
				if(info.getOuttime() != null){
					cell5.setCellValue(format2.format(info.getOuttime()));//出库日期
				}
				
				cell6.setCellValue(info.getOut_warehouse_name());
				cell7.setCellValue(info.getPass_warehouse_name());
				cell8.setCellValue(info.getTarget_warehouse_name());
				cell9.setCellValue(info.getSku());
				cell10.setCellValue(info.getSku_name());
				cell11.setCellValue(info.getBox_no()+"");
				cell12.setCellValue(info.getBox_count());
				cell13.setCellValue(info.getTransport_type());
				cell14.setCellValue(info.getActual_weight());
				cell15.setCellValue(info.getBox_grow());										
				cell16.setCellValue(info.getBox_broad());
				cell17.setCellValue(info.getBox_height());								
				cell18.setCellValue(info.getBox_volume());
				cell19.setCellValue(info.getTransfer_status());
				cell20.setCellValue(info.getIs_tax());
				cell21.setCellValue(info.getSite_name());
				cell22.setCellValue(info.getAccount_name());
				cell23.setCellValue(info.getShipment_id());
				cell24.setCellValue(info.getFnsku());
				cell25.setCellValue(info.getSellersku());
				cell26.setCellValue(info.getMoney());
				cell27.setCellValue(info.getTax_rate());
				cell28.setCellValue(info.getReturn_tax());
				cell29.setCellValue(info.getDeclare_order_id());
				if(info.getDeclare_order_date() != null){
					cell30.setCellValue(format2.format(info.getDeclare_order_date()));
				}
				
				
				cell31.setCellValue(info.getCustoms_number());
				cell32.setCellValue(info.getUnit_price());
				cell33.setCellValue(info.getDeclare_money());
				cell34.setCellValue(info.getCurrency());
				cell35.setCellValue(info.getNum());
				
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
				cell24.setCellStyle(cellStyle1);				
				cell25.setCellStyle(cellStyle1);
				cell26.setCellStyle(cellStyle1);				
				cell27.setCellStyle(cellStyle1);				
				cell28.setCellStyle(cellStyle1);				
				cell29.setCellStyle(cellStyle1);				
				cell30.setCellStyle(cellStyle1);				
				cell31.setCellStyle(cellStyle1);
				cell32.setCellStyle(cellStyle1);
				cell33.setCellStyle(cellStyle1);				
				cell34.setCellStyle(cellStyle1);	
				cell35.setCellStyle(cellStyle1);
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

}
