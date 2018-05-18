package com.aukey.report.service.impl;

import com.aukey.report.dao.TransferReportDao;
import com.aukey.report.dto.TransferReportParam;
import com.aukey.report.service.TransferReportService;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.CustomsFactorsVO;
import com.aukey.report.vo.TransferReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransferReportServiceImpl implements TransferReportService {

    @Autowired
    private TransferReportDao transferReportDao; // 多表查询用dao'
    
    

    @Override
    public TableData<TransferReportVO> listPage(PageParam pageParam, TransferReportParam param) {
        TableData<TransferReportVO> transferReportVOTableData = transferReportDao.queryPage(pageParam, param);
        /* for (TransferReportVO transferReportVO : transferReportVOTableData.getRows()) {
            //获取sku的单价
        	String declare_order_id = transferReportVO.getDeclare_order_id();
        	String sku = transferReportVO.getSku();
        	String keys = declare_order_id+sku;
        	List<CustomsFactorsVO> customsFactorsVOS = transferReportDao.queryUnitPrice(transferReportVO.getDeclare_order_id(), transferReportVO.getSku());
            if (null != customsFactorsVOS && customsFactorsVOS.size() != 0)
                for (CustomsFactorsVO customsFactorsVO : customsFactorsVOS) {
                    transferReportVO.setCurrency(customsFactorsVO.getCurrency());
                    transferReportVO.setNum(customsFactorsVO.getNum());
                   // transferReportVO.setDeclare_money((new BigDecimal(customsFactorsVO.getUnitPrice()).multiply(new BigDecimal(transferReportVO.getBox_count()))).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                    //transferReportVO.setMoney((new BigDecimal(transferReportVO.getUnit_price()).multiply(new BigDecimal(transferReportVO.getBox_count()))).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
                   if(transferReportVO.getRate() != null && transferReportVO.getRatio() != null){
                	   Optional.ofNullable(transferReportVO.getUnit_price()).ifPresent(price ->{
                		   String str = (new BigDecimal(transferReportVO.getUnit_price())).divide(new BigDecimal(1.17),7,BigDecimal.ROUND_UP).toString();
                    	   String dj = (new BigDecimal(str)).multiply(new BigDecimal(transferReportVO.getRate())).multiply(new BigDecimal(transferReportVO.getRatio())).setScale(2, BigDecimal.ROUND_HALF_UP).toString();  
                    	  transferReportVO.setDeclare_money(new BigDecimal(dj).multiply(new BigDecimal(transferReportVO.getBox_count())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                	   });
                	  
                	   
                   }else{
                	   Optional.ofNullable(transferReportVO.getRate()).ifPresent(rate ->{
                		   
                		   Optional.ofNullable(transferReportVO.getUnit_price()).ifPresent(price ->{
                			   
                			   String str = (new BigDecimal(transferReportVO.getUnit_price())).divide(new BigDecimal(1.17),7,BigDecimal.ROUND_UP).toString();
                    		   String dj = (new BigDecimal(str)).multiply(new BigDecimal(transferReportVO.getRate())).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    		   transferReportVO.setDeclare_money(new BigDecimal(dj).multiply(new BigDecimal(transferReportVO.getBox_count())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                		   });
                		  
                	   });
                	   Optional.ofNullable(transferReportVO.getRatio()).ifPresent(ration ->{
                		   
                		   Optional.ofNullable(transferReportVO.getUnit_price()).ifPresent(price ->{
                			   String str = (new BigDecimal(transferReportVO.getUnit_price())).divide(new BigDecimal(1.17),7,BigDecimal.ROUND_UP).toString();
                    		   String dj = (new BigDecimal(str)).multiply(new BigDecimal(transferReportVO.getRatio())).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                    		   transferReportVO.setDeclare_money(new BigDecimal(dj).multiply(new BigDecimal(transferReportVO.getBox_count())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                		   });
                		  
                	   });
                   }
                   
                }
        }*/
        return transferReportVOTableData;
    }

    @Override
    public int count(TransferReportParam param) {
        return transferReportDao.count(param);
    }

}
