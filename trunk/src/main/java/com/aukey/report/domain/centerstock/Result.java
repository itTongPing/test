package com.aukey.report.domain.centerstock;

import com.aukey.report.utils.CommonUtil;

public class Result {

	private String result;
	private String message;
	private Object data;
	// 返回状态码
	private Integer code;
	
	// 是否返回成功
	private Boolean success;
	
	/**
     *  错误信息
     */
    private String errMsg;
    
    private Boolean succeed;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getResult() {
		return ((null == result) || (result.length() == 0)) ? CommonUtil.ERROR : result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Result() {

	}

	public Result(String errorMsg) {
		this.result = CommonUtil.ERROR;
		this.message = errorMsg;
	}

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public Boolean getSucceed()
    {
        return succeed;
    }

    public void setSucceed(Boolean succeed)
    {
        this.succeed = succeed;
    }
    
}
