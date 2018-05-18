package com.aukey.report.utils;
import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.aukey.report.vo.PayOrderReportVo;

/**
 * 付款表
 * @author DengZhibin
 *
 */
public class PayOrderInfoExcel {
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(getClass());
	
	@SuppressWarnings("deprecation")
	public void setSheetTitle(HSSFWorkbook workbook, HSSFSheet sheet){
		// 设置打印格式
//		 HSSFPrintSetup printSetup = sheet.getPrintSetup();
		// 新建单元格样式
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		 // 顶端居中对齐
		cellStyle.setWrapText(true);

    	String[] titilName = new String[]
    			{"请款单号", 	"请款日期",	"法人主体",	"供应商", 	"采购单号",	"采购日期",
    			 "单据类型",		"币别",		"订单金额",	"入库金额",	"货款金额",	"已付金额",
    			 "付款利率",		"结算方式",	"请款人",	"采购员",	"采购部门",	"付款单号",
    			 "第三方订单编号","付款状态" };
		
		HSSFRow row1 = sheet.createRow(0);
		for (int i = 0; i < titilName.length; i++) {
			sheet.setColumnWidth(i, 3 * 800);
			HSSFCell cell = row1.createCell(i);
			cell.setCellValue(titilName[i]);
			cell.setCellStyle(cellStyle);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void appendSheetRow(HSSFWorkbook workbook, HSSFSheet sheet, int startRowIndex,  List<PayOrderReportVo> infos){
		HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 顶端居中对齐
		cellStyle1.setWrapText(true);
		
		for (int i = 0; i < infos.size(); i++) {
			PayOrderReportVo info  =  infos.get(i);
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
			try {
				cell1.setCellValue(info.getRequestOrderId());
				cell2.setCellValue(info.getBillDate());
				cell3.setCellValue(info.getCorporationName());
				cell4.setCellValue(info.getSupplierName());
				cell5.setCellValue(info.getPurchaseOrderId());
				cell6.setCellValue(info.getPurchaseOrderDate());
				cell7.setCellValue("0".equals(info.getType())?"预付":"账期");
				cell8.setCellValue(info.getCurrency());
				cell9.setCellValue(info.getSumTotalMoney()+"");
				cell10.setCellValue(info.getSumInvoiceMoney()+"");
				cell11.setCellValue(info.getSumReceiMoney()+"");//
				cell12.setCellValue(info.getSumPayedMoney()+"");//getPayTypeStr(info.getPayType())	
				BigDecimal ratio = new BigDecimal(info.getRequestRatio());
				cell13.setCellValue(ratio.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()+"");
				cell14.setCellValue(info.getPayWay());
				cell15.setCellValue(info.getRequesterName());
				cell16.setCellValue(info.getBuyerName());										
				cell17.setCellValue(info.getRequestDeptName());//请款部门
				cell18.setCellValue(info.getPayOrderId());								
				cell19.setCellValue(CommonUtil.isEmpty(info.getThirdOrderId())?"-":info.getThirdOrderId());
				cell20.setCellValue(getPayStatusStr(info.getPayStatus()));//支付状态
				
				
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
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			if(i % 1000 == 0){
				logger.info("当前写入到sheet中的数据：["+ i +"]，共需要写入：[" + infos.size() + "]");					
			}
		}
	}
	
	
	public String getPayStatusStr(String status){
		String statusName = "";
		if ("0".equals(status)) {
			statusName = "未支付";
		} else if ("1".equals(status)) {
			statusName = "已支付";
		} else if ("2".equals(status)) {
			statusName = "已拒付";
		} else if ("3".equals(status)) {
			statusName = "支付中";
		}
		return statusName;
	}
	
	public String getPayTypeStr(String handleStatus) {
		String statusName = "";
		if ("1".equals(handleStatus)) {
			statusName = "支付宝";
		} else if ("2".equals(handleStatus)) {
			statusName = "银行转账";
		} else if ("3".equals(handleStatus)) {
			statusName = "现金支付";
		} else if ("4".equals(handleStatus)) {
			statusName = "第三方编号";
		}
		return statusName;
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
