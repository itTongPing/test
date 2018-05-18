package com.aukey.report.utils;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSONArray;

public class CommonUtil {

	/**
	 * 调用成功
	 */
	public static final String SUCCESS = "success";
	/**
	 * 调用失败
	 */
	public static final String ERROR = "error";
	/**
	 * 调用成功code
	 */
	public static final int SUCCESS_CODE = 1;
	/**
	 * 调用失败code
	 */
	public static final int ERROR_CODE = 0;
	/**
	 * 调用无数据
	 */
	public static final String NOT_DATA_MSG = "no data";
	/**
	 * 调用成功信息
	 */
	public static final String SUCCESS_MSG = "success";
	/**
	 * 调用失败信息
	 */
	public static final String ERROR_MSG = "error";
	/**
	 * 调用失败信息
	 */
	public static final String ERROR_ACCOUNT_MSG = "error_account";
	/**
	 * 调用参数错误信息
	 */
	public static final String ERROR_INPUT_MSG = "error_input";
	/**
	 * 调用POST请求
	 */
	public static final String HTTP_POST = "POST";
	/**
	 * 调用GET请求
	 */
	public static final String HTTP_GET = "GET";
	/**
	 * 调用PUT请求
	 */
	public static final String HTTP_PUT = "PUT";

	/**
	 * 请求错误
	 */
	public static final String REQUEST_ERROR = "request error";

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Short obj) {
		return obj == null || obj == 0;
	}

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Short obj) {
		return !isEmpty(obj);
	}

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(String obj) {
	    return obj == null || obj.length() == 0 || "N/A".equals(obj) || obj == String.valueOf(0) || "null".equals(obj)
            || "NULL".equals(obj);
	}

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(String obj) {
		return !isEmpty(obj);
	}

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Integer obj) {
		return obj == null || obj == 0;
	}

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Integer obj) {
		return !isEmpty(obj);
	}

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(@SuppressWarnings("rawtypes") List obj) {
		return obj == null || obj.size() == 0;
	}

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(@SuppressWarnings("rawtypes") List obj) {
		return !isEmpty(obj);
	}

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Byte obj) {
		return obj == null || obj == 0;
	}

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Byte obj) {
		return !isEmpty(obj);
	}

	/**
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(String[] obj) {
		return obj == null || obj.length == 0;
	}

	public static boolean isNotEmpty(String[] obj) {
		return !isEmpty(obj);
	}

	/**
	 * 转码
	 */
	public static String encodeStr(String str) {
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static boolean isEmpty(Object obj) {
		return obj == null || "".equals(obj.toString());
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	
    /**
     * 将前端json对象数组，转换成List
     * @param param json数组
     * @return
     */
	public static List<String> putValidateValue(String param) {
		List<String> statuses = new ArrayList<String>();

		List<Object> statusList = JSONArray.parseArray(param);
		for (Object status : statusList) {
			if (status instanceof String && CommonUtil.isNotEmpty(status)) {
				statuses.add(status + "");
			}
		}
		if (CollectionUtils.isNotEmpty(statuses)) {
			return statuses;
		}

		return null;
	}
	
	/**
	 * 去掉list重复的值
	 * @param li
	 * @return
	 */
	public static List<String> removeDuplicateList(List<String> li){
        List<String> list = new ArrayList<String>();
        for(int i=0; i<li.size(); i++){
            String str = li.get(i);  //获取传入集合对象的每一个元素
            if(!list.contains(str)){   //查看新集合中是否有指定的元素，如果没有则加入
                list.add(str);
            }
        }
        return list;  //返回集合
    }
 
	/**
	 * Double 数据类型比较
	 * @param a
	 * @param b
	 *  @return 1:a>b; -1:a<b; 0:a=b; 
	 */
	public static int compareDouble(Double a, Double b) {
		BigDecimal data1 = new BigDecimal(a);
		BigDecimal data2 = new BigDecimal(b);
		return data1.compareTo(data2);
	}
	
	/**
	 * double 转化成String 类型并保留两位小数
	 * @param value
	 * @return
	 */
	public static String doubleToString(double value) {

	    DecimalFormat df = new DecimalFormat("0.00");
	    df.setRoundingMode(RoundingMode.HALF_UP);
	    return df.format(value);
	}
	
	/**
	 * 去掉list重复的值
	 * @param li
	 * @return
	 */
	public static List<Integer> removeDuplicateList2(List<Integer> li){
        List<Integer> list = new ArrayList<Integer>();
        for(int i=0; i<li.size(); i++){
            Integer str = li.get(i);  //获取传入集合对象的每一个元素
            if(!list.contains(str)){   //查看新集合中是否有指定的元素，如果没有则加入
                list.add(str);
            }
        }
        return list;  //返回集合
    }
 
}
