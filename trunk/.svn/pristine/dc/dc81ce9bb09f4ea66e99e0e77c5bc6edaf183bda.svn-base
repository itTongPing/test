package com.aukey.report.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import com.alibaba.fastjson.JSON;
import com.aukey.util.AjaxResponse;
import com.aukey.util.HttpUtil;
import com.aukey.util.URLBuilder;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 角色工具类
 * 
 * @author xieben 2017年1月5日
 * @since 1.0
 */
public class RoleUtil {

	private static Logger logger = Logger.getLogger(RoleUtil.class);

	/**
	 * 判断当前用户是否为超管
	 * 
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public static boolean isAdmin() {
		Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (map.get("authList") != null) {
			List<String> authList = (List<String>) map.get("authList");
			for (String roleName : authList) {
				if ("系统管理员".equals(roleName)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
     * 获取当前用户id
     * 
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public static String getUserId()
    {
        String userId = null;
        Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        if (map.get("user") != null)
        {
            Map<String, Object> userObj = (Map<String, Object>) map.get("user");
            userId = userObj.get("userId").toString();
        }
        return userId;
    }
	
	/**
	 * 获取当前用户Account
	 * 
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public static String getUserAccount() {
		String userAccount = null;
		Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (map.get("user") != null) {
			Map<String, Object> userObj = (Map<String, Object>) map.get("user");
			userAccount = userObj.get("account").toString();
		}
		return userAccount;
	}

	/**
	 * 获取当前用户
	 * 
	 * @return Map<String, Object>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getUser() {
		Map<String, Object> user = null;
		Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (map.get("user") != null) {
			user = (Map<String, Object>) map.get("user");
		}
		return user;
	}

	/**
	 * 获取当前用户所有归属销售组id
	 * 
	 * @return boolean
	 */
	public static List<Integer> getSalesGroupIds() {
		List<Integer> groupIds = new ArrayList<Integer>();
		for (Map<String, Object> map : getUserGroup()) {

			if (map.get("cateogryCode").equals("SALES")) {
				groupIds.add((Integer) map.get("orgGroupId"));
			}
		}

		return groupIds;
	}

	/**
	 * 获取当前用户的仓库ID
	 * 
	 * @return
	 */
	public static List<Integer> getWareHouseIds() {

		List<Integer> wareHouseIds = new ArrayList<Integer>();
		for (Map<String, Object> map : getUserGroup()) {
			String warehousenos = "";
			if (map.get("warehouse_no") != null) {
				warehousenos = map.get("warehouse_no").toString();
			}
			if (StringUtils.isNotEmpty(warehousenos)) {
				for (String warehouseno : warehousenos.split(",")) {
					wareHouseIds.add(Integer.valueOf(warehouseno));
				}
			}
		}

		return wareHouseIds;
	}

	/**
	 * 获取当前用户group
	 * 
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getUserGroup() {
		List<Map<String, Object>> userGroup = null;
		Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (map.get("orgList") != null) {
			userGroup = ((List<Map<String, Object>>) map.get("orgList"));
		}
		return userGroup;
	}
	//
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getAuthorities()
	{
		List<Map<String, Object>> authorities = null;
		Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (map.get("authorities") != null) {
			authorities = ((List<Map<String, Object>>) map.get("authorities"));
		}
		return authorities;
	}
	
	
	/**
	 * 是否可以导出所有字段
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean  CanExportAll() {
		Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (map.get("moduleCodeList") != null) {
			List<String> moduleCodeList = (List<String>) map.get("moduleCodeList");
			if (moduleCodeList.contains("report_advance_view")) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 验证是否有该权限
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean  CanShanPinExportAll(String code) {
		Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (map.get("moduleCodeList") != null) {
			List<String> moduleCodeList = (List<String>) map.get("moduleCodeList");
			if (moduleCodeList.contains(code)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	
    /**
     * 获取当前用户销售类分组
     * @return List<Map<String, Object>>
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getUserSalesGroup()
    {
        List<Map<String, Object>> userGroup = null;
        List<Map<String, Object>> salesGroup = new ArrayList<>();
        Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (map.get("orgList") != null)
        {
            userGroup = ((List<Map<String, Object>>) map.get("orgList"));
            if(userGroup!=null)
            {
                for (Map<String, Object> groups : userGroup)
                {
                    if(groups.get("cateogryCode").equals("SALES"))
                    {
                        salesGroup.add(groups);
                    }
                }
            }
        }
        return salesGroup;
    }
    
    /**
     * 获取当前用户采购类分组
     * @return List<Map<String, Object>>
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getUserSPurchaseGroup()
    {
        List<Map<String, Object>> userGroup = null;
        List<Map<String, Object>> salesGroup = new ArrayList<>();
        Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (map.get("orgList") != null)
        {
            userGroup = ((List<Map<String, Object>>) map.get("orgList"));
            if(userGroup!=null)
            {
                for (Map<String, Object> groups : userGroup)
                {
                    if(groups.get("cateogryCode").equals("PURCHASE"))
                    {
                        salesGroup.add(groups);
                    }
                }
            }
        }
        return salesGroup;
    }
    
    
    /**
     * 获取当前用户group
     * @return List<Map<String, Object>>
     */
    @SuppressWarnings("unchecked")
    public static List<Integer> getUserWarehouse()
    {
        List<Integer> warehouses = new ArrayList<Integer>();
        Map<String, Object> map = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        if (map.get("orgList") != null)
        {
            for (Map<String, Object> group : (List<Map<String, Object>>) map.get("orgList"))
            {
                if (group.get("warehouse_no") != null)
                {
                    for (String warehouseId : group.get("warehouse_no").toString().split(","))
                    {
                        warehouses.add(Integer.valueOf(warehouseId));
                    }
                }
            }
        }
        return warehouses;
    }

}
