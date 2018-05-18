package com.aukey.report.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.aukey.report.domain.PurchaseWarnReport;
import com.aukey.report.vo.GoodReportVO;
import com.aukey.util.CommonUtil;

public class GoodsReceiveDetailedExcel {
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
    			{"序号", 	"SKU",	"SKU名称",	"单据编号", 	"单据日期",	"业务类型",
    			 "是否含税",		"法人主体",		"仓库",	"数量",	"币别",	"制单人",
    			 "运输方式",		"需求部门",	"金额",	"单价",	"来往单位" };
		
		HSSFRow row1 = sheet.createRow(0);
		for (int i = 0; i < titilName.length; i++) {
			sheet.setColumnWidth(i, 3 * 1500);
			HSSFCell cell = row1.createCell(i);
			cell.setCellValue(titilName[i]);
		}
	}
	@SuppressWarnings("deprecation")
	public void appendSheetRow(HSSFWorkbook workbook, HSSFSheet sheet, int startRowIndex,  List<GoodReportVO> list){
		HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 顶端居中对齐
		cellStyle1.setWrapText(true);
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int temp = 0;
		for (GoodReportVO info : list) {
			Optional.ofNullable(info).ifPresent(i ->{
				Optional.ofNullable(i.getBusiness_type()).ifPresent(bt ->{
					if(bt.equals("无主海外调拨出库") || bt.equals("无主调拨出库") || bt.equals("无主盘盈入库") || bt.equals("无主调拨入库") || bt.equals("无主盘亏出库")){
						i.setLegaler_name("傲基国际有限公司");
						i.setIs_tax("0");
					}
					if( bt.equals("需求单无主调拨出库")){
						i.setIs_tax("0");
					}
				});
			});
			
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
			
			try {
				cell1.setCellValue(++temp);
				cell2.setCellValue(info.getSku());
				cell3.setCellValue(info.getSku_name());
				cell4.setCellValue(info.getDocument_number());
				cell5.setCellValue(format2.format(info.getDocument_date()));//采购日期
				cell6.setCellValue(info.getBusiness_type());
				cell7.setCellValue(CommonUtil.isNotEmpty(info.getIs_tax())?"0".equals(info.getIs_tax())?"否":"是":"-");
				cell8.setCellValue(info.getLegaler_name()+"");
				cell9.setCellValue(info.getWarehouse_name()+"");
				
				cell11.setCellValue(info.getCurrency());//币别
				cell12.setCellValue(info.getCreator_name());//制单人
				cell13.setCellValue("1".equals(info.getTransport_type())?"海运":"0".equals(info.getTransport_type())?"空运":"-");
				cell14.setCellValue(info.getDepartment_name());
                 if(info != null && info.getBusiness_type().equals("需求单盘亏出库")){
					
					Optional.ofNullable(info.getQuantity()).ifPresent(q ->{
						String[] split = q.split(",");
						long num =0;
						for (int i = 0; i < split.length; i++) {
							long parseLong = Long.parseLong(split[i].toString());
							 num += parseLong;
						}
						if(info.getPrice() != null){
							String cost =new BigDecimal(info.getPrice()).multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
							info.setQuantity(String.valueOf(num));
							info.setCost(cost);
						}else{
							info.setQuantity(String.valueOf(num));
						}
					});
					cell10.setCellValue(removeSpace(info.getQuantity()+""));//数量
					cell15.setCellValue(info.getCost());//金额
					
				}else{
					cell10.setCellValue(removeSpace(info.getQuantity()+""));//数量
					cell15.setCellValue(info.getCost());//金额
				}
				
				cell16.setCellValue(info.getPrice());//单价
				cell17.setCellValue(info.getIntercourse_unit_name());//往来单位
				
				
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
