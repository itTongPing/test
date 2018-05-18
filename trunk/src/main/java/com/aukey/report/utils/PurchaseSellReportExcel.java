package com.aukey.report.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;

import com.aukey.report.domain.purchaseSell.PurchaseSellStockVO;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class PurchaseSellReportExcel {
	private Logger logger = Logger.getLogger(getClass());
	
	private static final String[] DATA= {"法人主体","事业部","SKU","SKU名称","品类","仓库名称","入库","出库","库存","占用","在途"};
	
	public void writeExc(OutputStream os,List<PurchaseSellStockVO> purchaseSellStockList) throws RowsExceededException, WriteException{
		
		WritableWorkbook wwb = null;
		try 
		{ 
		wwb = Workbook.createWorkbook(os); 
		}
		catch (Exception e){ 
		e.printStackTrace(); 
		} 
		//创建Excel工作表 
		WritableSheet ws = wwb.createSheet("进销存报表", 0);//创建sheet
		createHeader(ws);
		createData(ws,purchaseSellStockList);
		//输出流
		try {
		wwb.write();
		} catch (IOException ex) {
		ex.printStackTrace();
		}
		//关闭流
		try {
		wwb.close();
		} catch (WriteException ex) {
		ex.printStackTrace();
		} catch (IOException ex) {
		ex.printStackTrace();
		}
		logger.info("excel写入成功！");
	}
	//添加数据
	private void createData(WritableSheet ws,
			List<PurchaseSellStockVO> purchaseSellStockList) throws RowsExceededException, WriteException {
		
		PurchaseSellStockVO vo=null;
		Label l=null;
		int index = 0;
		for(int i = 0;i<purchaseSellStockList.size();i++){
			vo =purchaseSellStockList.get(i);
			index = i+2;
			l = new Label(0,index , vo.getLegalerName(), getNormolCell());//第3行第1列
			ws.addCell(l);
			l = new Label(1, index, vo.getCompanyName(), getNormolCell());//第3行第2列
			ws.addCell(l);
			l = new Label(2, index, vo.getSku(), getNormolCell());//第3行第3列
			ws.addCell(l);
			l = new Label(3, index, vo.getSkuName(), getNormolCell());//第3行第4列
			ws.addCell(l);
			l = new Label(4, index, vo.getCategoryName(), getNormolCell());//第3行第5列
			ws.addCell(l);
			l = new Label(5, index, vo.getWarehouseName(), getNormolCell());//第3行第6列
			ws.addCell(l);
			
			l = new Label(6, index,vo.getInCount()==null?"":vo.getInCount().toString(), getNormolCell());//第3行第7列
			ws.addCell(l);
			l = new Label(7, index, vo.getInPrice()==null?"":vo.getInPrice().toString(), getNormolCell());//第3行第8列
			ws.addCell(l);
			l = new Label(8, index, vo.getInSize()==null?"":vo.getInSize().toString(), getNormolCell());//第3行第9列
			ws.addCell(l);
			
			l = new Label(9, index, vo.getOutCount()==null?"":vo.getOutCount().toString(), getNormolCell());//第3行第10列
			ws.addCell(l);
			l = new Label(10, index, vo.getOutPrice()==null?"":vo.getOutPrice().toString(), getNormolCell());//第3行第11列
			ws.addCell(l);
			l = new Label(11, index, vo.getOutSize()==null?"":vo.getOutSize().toString(), getNormolCell());//第3行第12列
			ws.addCell(l);
			
			l = new Label(12, index, vo.getStockCount()==null?"":vo.getStockCount().toString(), getNormolCell());//第3行第13列
			ws.addCell(l);
			l = new Label(13, index, vo.getStockPrice()==null?"":vo.getStockPrice().toString(), getNormolCell());//第3行第14列
			ws.addCell(l);
			l = new Label(14, index, vo.getStockSize()==null?"":vo.getStockSize().toString(), getNormolCell());//第3行第15列
			ws.addCell(l);
			
			l = new Label(15, index, vo.getOccupyCount()==null?"":vo.getOccupyCount().toString(), getNormolCell());//第3行第16列
			ws.addCell(l);
			l = new Label(16, index, vo.getOccupyPrice()==null?"":vo.getOccupyPrice().toString(), getNormolCell());//第3行第17列
			ws.addCell(l);
			l = new Label(17, index, vo.getOccupySize()==null?"":vo.getOccupySize().toString(), getNormolCell());//第3行第18列
			ws.addCell(l);
			
			l = new Label(18, index, vo.getOnwayCount()==null?"":vo.getOnwayCount().toString(), getNormolCell());//第3行第16列
			ws.addCell(l);
			l = new Label(19, index, vo.getOnwayPrice()==null?"":vo.getOnwayPrice().toString(), getNormolCell());//第3行第17列
			ws.addCell(l);
			l = new Label(20, index, vo.getOnwaySize()==null?"":vo.getOnwaySize().toString(), getNormolCell());//第3行第18列
			ws.addCell(l);
		}
		
		
		
		
	}
	//创建表头
	private void createHeader(WritableSheet ws) throws RowsExceededException, WriteException {
		Label header=null;
		for(int i = 0;i<DATA.length;i++){
			   if(i<=5){
				   ws.mergeCells(i, 0, i, 1);
				   header = new Label(i, 0, DATA[i],getTitle());
				   ws.addCell(header);
				   ws.setColumnView(i, 25);
			   }else{
				     ws.mergeCells(3*(i-6)+6, 0, 3*(i-6)+8, 0);
					 header = new Label(3*(i-6)+6, 0, DATA[i], getTitle());
					 ws.addCell(header);
			   }
		   }
		
		 //设置数量，金额，体积
		 header= new Label(6,1,"数量",getNormolCell());
		 ws.addCell(header);
		 header= new Label(7,1,"金额",getNormolCell());
		 ws.addCell(header);
		 header= new Label(8,1,"体积",getNormolCell());
		 ws.addCell(header);
		 
		 header= new Label(9,1,"数量",getNormolCell());
		 ws.addCell(header);
		 header= new Label(10,1,"金额",getNormolCell());
		 ws.addCell(header);
		 header= new Label(11,1,"体积",getNormolCell());
		 ws.addCell(header);
		 
		 header= new Label(12,1,"数量",getNormolCell());
		 ws.addCell(header);
		 header= new Label(13,1,"金额",getNormolCell());
		 ws.addCell(header);
		 header= new Label(14,1,"体积",getNormolCell());
		 ws.addCell(header);
		 
	
		 header= new Label(15,1,"数量",getNormolCell());
		 ws.addCell(header);
		 header= new Label(16,1,"金额",getNormolCell());
		 ws.addCell(header);
		 header= new Label(17,1,"体积",getNormolCell());
		 ws.addCell(header);
		 
		 header= new Label(18,1,"数量",getNormolCell());
		 ws.addCell(header);
		 header= new Label(19,1,"金额",getNormolCell());
		 ws.addCell(header);
		 header= new Label(20,1,"体积",getNormolCell());
		 ws.addCell(header);
		 
		 ws.getSettings().setVerticalFreeze(1);
		 ws.getSettings().setVerticalFreeze(2);
		 
	}
	/**
	* 设置标题样式
	* @return
	*/
	private WritableCellFormat getTitle(){
	WritableFont font = new WritableFont(WritableFont.TIMES, 12);
	try {
	font.setColour(Colour.BLUE);//蓝色字体
	} catch (WriteException e1) {
	e1.printStackTrace();
	}
	WritableCellFormat format = new WritableCellFormat(font);

	try {
	format.setAlignment(jxl.format.Alignment.CENTRE);
	format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	format.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
	} catch (WriteException e) {
	e.printStackTrace();
	}
	return format;
	}
	
	/**
	* 设置其他单元格样式
	* @return
	*/
	private  WritableCellFormat getNormolCell(){//12号字体,上下左右居中,带黑色边框
	WritableFont font = new WritableFont(WritableFont.TIMES, 12);
	WritableCellFormat format = new WritableCellFormat(font);
	try {
	format.setAlignment(jxl.format.Alignment.CENTRE);
	format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	format.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
	} catch (WriteException e) {
	e.printStackTrace();
	}
	return format;
	}

}
