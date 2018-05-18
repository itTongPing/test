package com.aukey.report.domain;

import java.util.Date;

/**
 * 用户实体
 */
public class User
{
    /**
     * userId
     */
    private Integer userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 是否启用：1-启用,0-禁用
     */
    private String funcState;

    /**
     * 数据状态:1-有效,0-无效
     */
    private String dataState;

    /**
     * 是否为超级管理员(字段已弃用)
     */
    private String _super;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 最近操作人
     */
    private Integer updateUser;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 最近操作时间
     */
    private Date updateDate;

    public String getFuncState()
    {
        return funcState;
    }

    public String get_super()
    {
        return _super;
    }

    public void set_super(String _super)
    {
        this._super = _super;
    }

    public void setFuncState(String funcState)
    {
        this.funcState = funcState;
    }

    public String getDataState()
    {
        return dataState;
    }

    public void setDataState(String dataState)
    {
        this.dataState = dataState;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getSuper()
    {
        return _super;
    }

    public void setSuper(String _super)
    {
        this._super = _super;
    }

    public Integer getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(Integer createUser)
    {
        this.createUser = createUser;
    }

    public Integer getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser)
    {
        this.updateUser = updateUser;
    }

    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    public Date getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }

}
