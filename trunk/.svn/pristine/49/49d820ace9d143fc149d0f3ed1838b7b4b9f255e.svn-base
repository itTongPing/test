package com.aukey.report.service.finance.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aukey.report.dao.finance.PurchaseFinanceReportDao;
import com.aukey.report.domain.base.Result;
import com.aukey.report.dto.PurchaseReportParam;
import com.aukey.report.service.finance.PurchaseFinanceReportService;
import com.aukey.report.utils.RoleUtil;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.PayMethodVO;
import com.aukey.report.vo.PurchaseReportVO;

@Service
public class PurchaseFinanceReportServiceImpl implements PurchaseFinanceReportService {
	
	@Autowired
    private PurchaseFinanceReportDao purchaseFinanceReportDao; // 多表查询用dao


    @Override
    public TableData<PurchaseReportVO> listPage(PageParam pageParam, PurchaseReportParam param,Integer userId)
    {
        //dataAuthority(userId,param);
        TableData<PurchaseReportVO> tableData = purchaseFinanceReportDao.queryPage(pageParam, param);
        return tableData;
    }
    @Override
	public Result selectCount(PageParam pageParam, PurchaseReportParam param, Integer userId) {
    	//dataAuthority(userId,param);
    	Result result = purchaseFinanceReportDao.selectCount(pageParam, param);
		return result;
	}

    // 权限控制
    public void dataAuthority(Integer userId, PurchaseReportParam param)
    {
        boolean purchaseUser = false; // 是否是采购人员
        boolean purchaseManager = false; // 是否采购类但非采购员
        boolean financeUser = false; // 是否为财务相关类-应付、关税和出纳
        List<Integer> purchaseGroupIdList = new ArrayList<>(); // 采购主管分组id

        // 查询
        List<Map<String, Object>> groups = RoleUtil.getUserGroup();
        if (groups != null && groups.size() > 0)
        {
            for (Map<String, Object> loginGroup : groups)
            {
                String calssNameCode = loginGroup.get("cateogryCode").toString(); // 角色分类
                String orgCode = loginGroup.get("orgCode").toString(); // 角色编码
                // 如果采购非采购员
                if (calssNameCode.equals("PURCHASE") && !orgCode.equals("4003"))
                {
                    purchaseManager = true;
                    purchaseGroupIdList.add((Integer) loginGroup.get("orgGroupId"));
                }
                // 如果是采购员
                if (orgCode.equals("4003") && loginGroup.get("cateogryCode").equals("PURCHASE"))
                {
                    purchaseUser = true;
                }

                // 如果是财务相关类-应付、关税和出纳
                if (calssNameCode.equals("TARIFF") || calssNameCode.equals("CUSTOMS")
                    || calssNameCode.equals("CASHIER"))
                {
                    financeUser = true;
                }
            }
        }

        /*if (financeUser)
        {
            // 采购员
            if (purchaseUser)
            {
                param.setPurchaseUser(userId);
            }
            // 主管
            if (purchaseManager)
            {
                param.getPurchaseGroupIdList().addAll(purchaseGroupIdList);
            }
            // 不是采购人员也不是不是主管
            if(purchaseUser==false && purchaseManager==false){
                param.setPurchaseUser(userId);
            }
        }*/
        if(purchaseUser==false && purchaseManager==false){
            param.setPurchaseUser(userId);
         // 不是采购人员也不是不是主管
        }
        if(purchaseUser){//采购员
        	param.setPurchaseUser(userId);
        }
        if(purchaseManager){//采购类但非采购员
        	param.getPurchaseGroupIdList().addAll(purchaseGroupIdList);
        	param.setPurchaseUser(null);
        }
        if (financeUser){
        	param.getPurchaseGroupIdList().addAll(new ArrayList<Integer>());
        	param.setPurchaseUser(null);
        }

    }

    @Override
    public List<PayMethodVO> payMethodList()
    {
        return purchaseFinanceReportDao.queryList();
    }

    @Override
    public int count(PurchaseReportParam param,Integer userId)
    {
    	//dataAuthority(userId,param);
        return purchaseFinanceReportDao.count(param);
    }
}
