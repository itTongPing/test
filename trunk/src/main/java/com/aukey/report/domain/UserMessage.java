package com.aukey.report.domain;

import java.util.List;


public class UserMessage
{
    private String success;
    private String message;
    private List<User> data;
    public String getSuccess()
    {
        return success;
    }
    public void setSuccess(String success)
    {
        this.success = success;
    }
    public String getMessage()
    {
        return message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }
    public List<User> getData()
    {
        return data;
    }
    public void setData(List<User> data)
    {
        this.data = data;
    }
}