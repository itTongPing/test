package com.aukey.report.utils;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.aukey.report.vo.DeclareReportVO;
import com.aukey.report.vo.PurchaseExeReportVO;

public class PurchaseReportExcel {
	/**
	 * 日志
	 */
	private Logger logger = Logger.getLogger(getClass());
	@SuppressWarnings("deprecation")
	public void setSheetTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
		String[] titilName = new String[] { "序号", "供应商名称", "法人主体", "采购单号", "采购日期", "采购金额", "采购币别", "采购员", "采购部门", "入库仓库",
				"入库金额", "未入库金额", "入库状态", "未付金额", "已付合计", "支付状态", "是否含税", "开票状态", "未开票金额", "已开票金额", "开票合同"};

		HSSFRow row1 = sheet.createRow(0);
		for (int i = 0; i < titilName.length; i++) {
			sheet.setColumnWidth(i, 3 * 1500);
			HSSFCell cell = row1.createCell(i);
			cell.setCellValue(titilName[i]);
		}
	}
	@SuppressWarnings("deprecation")
	public void appendSheetRow(HSSFWorkbook workbook, HSSFSheet sheet, int startRowIndex, List<PurchaseExeReportVO> list) {
		HSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 顶端居中对齐
		//cellStyle1.setWrapText(true);
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int temp = 0;
		for (PurchaseExeReportVO info : list) {
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
			try {
				cell1.setCellValue(++temp);
				cell2.setCellValue(info.getSupplier_name());
				cell3.setCellValue(info.getLegaler_name());
				cell4.setCellValue(info.getPurchase_no());
				cell5.setCellValue(format2.format(info.getPurchase_date()));// 采购日期
				cell6.setCellValue(info.getPurchase_amount_all());
				cell7.setCellValue(info.getPurchase_currency());
				cell8.setCellValue(info.getPurchaser_name());
				cell9.setCellValue(info.getDepartment_name());
				cell10.setCellValue(info.getInventory_warehouse_name());
				cell11.setCellValue(info.getInventory_amount());
				cell12.setCellValue(info.getUn_inventory_amount());
				cell13.setCellValue(info.getInventory_status());
				cell14.setCellValue(info.getUn_payment());
				cell15.setCellValue(info.getPayment_all());
				cell16.setCellValue(info.getPayment_status());
				cell17.setCellValue(info.getIs_tax());
				cell18.setCellValue(info.getBill_status());
				cell19.setCellValue(info.getUn_bill_amount());
				cell20.setCellValue(info.getBill_amount());
				cell21.setCellValue(info.getBill_contract());

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
				
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
	public String removeSpace(String resource) {
		if (CommonUtil.isEmpty(resource)) {
			return "-";
		}
		StringBuffer buffer = new StringBuffer();
		byte[] ch = resource.getBytes();
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] >= 48 && ch[i] <= 57) {
				buffer.append(ch[i] - 48);
			}
		}
		return buffer.toString();
	}
}
