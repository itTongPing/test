package com.aukey.report.service.centerstock.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aukey.report.utils.CommonUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import com.aukey.report.domain.centerstock.CenterSkuDepartRelation;
import com.aukey.report.domain.centerstock.Result;
import com.aukey.report.mapper.centerstock.CenterSkuDepartRelationMapper;
import com.aukey.report.service.centerstock.CenterSkuDepartRelationService;
import com.aukey.report.service.centerstock.CenterStockReportService;

@Service
public class CenterSkuDepartRelationServiceImpl implements CenterSkuDepartRelationService {

	@Autowired
	private CenterSkuDepartRelationMapper centerSkuDepartRelationMapper;
	
	@Autowired
	private CenterStockReportService centerStockReportService;
	
	/**
	 * 打印日志
	 */
	private Logger logger = Logger.getLogger(getClass());
	
	
	@Override
	public int insert(CenterSkuDepartRelation record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CenterSkuDepartRelation> selectAll(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerSkuDepartRelationMapper.selectAll(map);
	}

	@Override
	public int updateCenterSkuDepartRelation(CenterSkuDepartRelation record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer selectAllCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return centerSkuDepartRelationMapper.selectAllCount(map);
	}

	@Transactional
	@Override
	public Result uploadExcel(Workbook workbook,Integer userId) {
		
		Result result = new Result();
		result.setSuccess(false);
		result.setCode(200);
		result.setErrMsg("导入没有修改信息！！");
		Map<String, Object> paramIds = new HashMap<String,Object>();
		List<Map<String,Object>> findAllOrgs = centerStockReportService.findAllOrgs(paramIds);
		Sheet sheet = workbook.getSheetAt(0);
		if(sheet.getLastRowNum()<1 || sheet.getRow(0).getLastCellNum()!=6)
        {
            result.setSuccess(false);
            result.setErrMsg("请填写好表头！！");
            return result;
        }
		List<CenterSkuDepartRelation> existsList = new ArrayList<CenterSkuDepartRelation>();//更新
		List<CenterSkuDepartRelation> notExistsList = new ArrayList<CenterSkuDepartRelation>();//新增
		Map<String,Object> param = new HashMap<String,Object>();
		List<String> list = new ArrayList<String>();
		int id = 0;
		try 
		{
			
			for (int i = 1; i <= sheet.getLastRowNum(); i++) 
			{
				list.clear();
				id=0;
				CenterSkuDepartRelation centerSkuDepartRelation = new CenterSkuDepartRelation();
				Row row = sheet.getRow(i);
				String skuCode = getCellValue(row.getCell(0));
				if (CommonUtil.isEmpty(skuCode))
				{
					continue;
				}
				String departName = getCellValue(row.getCell(1));
				String purchasePrice = getCellValue(row.getCell(2));
				String length = getCellValue(row.getCell(3));
				String width = getCellValue(row.getCell(4));
				String height = getCellValue(row.getCell(5));
				for (Map<String,Object> map : findAllOrgs) 
				{
					if(map.get("name").equals(departName))
					{
						id = (int) map.get("id");
						break;
					}
				}
				list.add(skuCode);
				param.put("skuCodeList", list);
				Integer selectAllCount = centerSkuDepartRelationMapper.selectAllCount(param);
				centerSkuDepartRelation.setDepartmentId(id);
				centerSkuDepartRelation.setSkuCode(skuCode);
				centerSkuDepartRelation.setPurchasePrice(new BigDecimal(purchasePrice));
				centerSkuDepartRelation.setLength(new BigDecimal(length));
				centerSkuDepartRelation.setWidth(new BigDecimal(width));
				centerSkuDepartRelation.setHeight(new BigDecimal(height));
				centerSkuDepartRelation.setUserId(userId);
				if(selectAllCount>0)
				{
					//更新
					existsList.add(centerSkuDepartRelation);
				}else
				{
					//新增
					notExistsList.add(centerSkuDepartRelation);
				}
				
			}
			if(!existsList.isEmpty())
			{
				for (CenterSkuDepartRelation object : existsList) 
				{
					int relation = centerSkuDepartRelationMapper.updateCenterSkuDepartRelation(object);
					if(relation>0)
					{
						result.setSuccess(true);
						result.setCode(200);
						result.setErrMsg("导入成功!!");
					}
				}
			}
			
			if(!notExistsList.isEmpty())
			{
				
				for (CenterSkuDepartRelation object : notExistsList) 
				{
					
					int insert = centerSkuDepartRelationMapper.insert(object);
					if(insert>0)
					{
						result.setSuccess(true);
						result.setCode(200);
						result.setErrMsg("导入成功!!");
					}
				}
				
			}
			
			
		} catch (Exception e) 
		{
			logger.error("导入EXCEL失败", e);
			result.setSuccess(false);
			result.setErrMsg("导入失败!");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return result;
	}

	
	@SuppressWarnings("deprecation")
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
            	value = getStringType(cell.getNumericCellValue());
                 break;
             case Cell.CELL_TYPE_STRING:
                 value = String.valueOf(cell.getRichStringCellValue());
                 break;
             }
         }
         return value;
	}
	private String getStringType(Object object)
    {
        String s = String.valueOf(object);
        String[] split = s.split("\\.");
        if(split.length>1)
        {
            String s1 = split[1];
            boolean b = Integer.valueOf(s1).intValue() == 0 ? true : false;
            if(b)
            {
                return split[0];
            }else
            {
                return  s;
            }
        }else
        {
            return s;
        }

    }
}
