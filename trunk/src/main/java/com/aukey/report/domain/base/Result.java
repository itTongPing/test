package com.aukey.report.domain.base;

/**
 * 公共返回值
 * 
 * @author xiaochanghiu 返回值数据在CommonUtil的常量中
 */
public class Result {
	// 返回值
	private Integer ret;
	// 返回具体数据
	private Object data;
	// 错误代码
	private Integer errCode;
	// 错误信息
	private String errMsg;
	
	   // 是否返回成功
    private Boolean success;

	public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public Integer getRet() {
		return null == ret ? 1 : ret;
	}

	public void setRet(Integer ret) {
		this.ret = ret;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
