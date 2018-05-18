package com.aukey.report.web;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aukey.domain.base.APIResult;
import com.aukey.report.utils.RoleUtil;

@RestController
@RequestMapping(value = "/report")
public class RoleRestcontroller {
 
	@RequestMapping(value = "/GetRoleList", method = RequestMethod.GET)
	public APIResult GetRoleList(ModelMap modelMap) {
	
		RoleUtil.getSalesGroupIds();
		
		RoleUtil.getWareHouseIds();
	 
		return new APIResult();
	}
}
