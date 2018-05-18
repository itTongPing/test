package com.aukey.report.base;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.aukey.report.utils.page.PageResult;

/**
 * 控制类基类
 * 
 * @author xiehz
 *
 */
public abstract class BaseController {

	public static final Integer exportMaxNumber = 600000;

	public ResponseResult success(String message, PageResult<? extends Object> pagerResult) {
		ResponseResult r = new ResponseResult();
		r.setStatus(ResponseResult.STATUS.S_200).setMessage(message).setDataPageNo(pagerResult.getPageNo()).setDataPageSize(pagerResult.getPageSize()).setDataPageCounts(pagerResult.getPageCounts())
				.setDataRecordsTotal(pagerResult.getRecordsTotal()).setDataResult(pagerResult.getResults());
		return r;
	}

	public ResponseResult successResponse(String message) {
		return new ResponseResult(ResponseResult.STATUS.S_200, message);
	}

	public ResponseResult failResponse(String message) {
		return new ResponseResult(ResponseResult.STATUS.S_500, message);
	}

	public ResponseResult responseResult(int status, String message) {
		return new ResponseResult(status, message);
	}

	/**
	 * 初始化Binder，在参数转换时调用 由于请求都是以字符串格式提交的，因此转换日期或者数字时可能会转换报错，比如""转int
	 * 下面处理了日期参数格式和""转为int和long时均转换为0
	 * 
	 * @param binder
	 *            WebDataBinder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// 转换字符串左右的空格
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
		// 转换日期参数的格式
		binder.registerCustomEditor(Date.class, new DateEditor(true));

		// 参数字符串为""转int时自动转换为0
		binder.registerCustomEditor(int.class, null, new CustomNumberEditor(Integer.class, true) {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (StringUtils.isBlank(text)) {
					setValue(0);
					return;
				}
				if (!NumberUtils.isNumber(text)) {
					setValue(-1);
					return;
				}
				super.setAsText(text);
			}
		});
		// 参数字符串为""转long时自动转换为0
		binder.registerCustomEditor(long.class, new CustomNumberEditor(Long.class, true) {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (StringUtils.isBlank(text)) {
					setValue(0L);
					return;
				}
				if (!NumberUtils.isNumber(text)) {
					setValue(-1);
					return;
				}
				super.setAsText(text);
			}
		});
		// 参数字符串为""转double时自动转换为0.0
		binder.registerCustomEditor(double.class, new CustomNumberEditor(Double.class, true) {
			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				if (StringUtils.isBlank(text)) {
					setValue(0.0d);
					return;
				}
				if (!NumberUtils.isNumber(text)) {
					setValue(0.0d);
					return;
				}
				super.setAsText(text);
			}
		});
	}

}
