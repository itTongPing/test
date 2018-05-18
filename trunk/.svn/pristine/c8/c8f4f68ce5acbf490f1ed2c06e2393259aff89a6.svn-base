package com.aukey.report.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class PurchaseReportVO
{

    private String id;

    // 法人主体
    private String legaler;

    // 供应商
    private String supplier;

    // 采购单号
    private String purchase_no;

    // 采购日期
    private Date purchase_date;

    // SKU
    private String sku;
    
    // 税率
    private Double tax_rate;

    // SKU名称
    private String sku_name;

    // 采购数量
    private int purchase_count;

    // 不含税单价
    private Double price_wihtout_tax;

    // 含税单价
    private Double price_tax;

    // 采购金额
    private Double purchase_sum;

    // 采购币别
    private String purchase_money_type;

    // 采购员
    private String buyer;

    // 采购部门
    private String department;

    // 需求部门
    private String deptIdxq;

    // 需求部门名称
    private String deptNamexq;

    // 入库单号
    private String stock_number;

    // 入库日期
    private Date stock_date;

    // 仓库
    private String stock_name;

    // 入库数量
    private int stock_count;

    // 入库金额
    private Double stock_sum;

    // 占用库存
    private String usage_inventory;

    // 可用库存
    private String available_inventory;

    // 品类
    private String category;

    // 开票品名
    private String bill_name;

    // 开票单位
    private String bill_unit;
    
    // 发票号码
    private String invoiceCode;
    
    // 开票数量
    private int invoice_num;

    // 未开票数量
    private int no_bill_count;

    // 开票状态
    private String bill_status;

    // 品牌
    private String brand;

    // 型号
    private String version;

    // 是否含税
    private String include_tax;

    // 运输方式
    private String transport_type;

    // 支付方式
    private String pay_type_name;

    //汇率
    private Double exchangRate;
    
    //未开票金额
    private String noMakeInvoice;
    
    //已开票金额
    public String makeInvoice;
 
    //采购本币金额
    private Double purchaseCurrencySum;
    
    //入库本币金额
    private Double stockCurrencySum;
    
    //未开票本币金额
    private Double noMakeInvoiceCurrency;
    
    //已开票本币金额
    private Double makeInvoiceCurrency;
    
    public Double getMakeInvoiceCurrency() {
    	if(this.exchangRate!=null)
    	{
    		if (this.include_tax.equals("否"))
            {
                /*return new BigDecimal((this.stock_count - this.no_bill_count) * this.price_wihtout_tax * this.exchangRate).setScale(2, RoundingMode.UP).doubleValue();*/
    			return 0.00;
            }
            else
            {
                return new BigDecimal((this.stock_count - this.no_bill_count) * this.price_tax * this.exchangRate).setScale(6, RoundingMode.UP).doubleValue();
            }
    	}else
    	{
    		return 0.00;
    	}
		
	}
      
    public Double getPurchaseCurrencySum() {
    	// include_tax price_wihtout_tax price_tax exchangRate
    	if(this.exchangRate!=null)
    	{
    		return new BigDecimal(purchase_sum * this.exchangRate).setScale(6, RoundingMode.UP).doubleValue();
    	}else
    	{
    		return 0.00;
    	}
	}

	
	public Double getStockCurrencySum() {
		if(this.exchangRate!=null)
    	{
			return new BigDecimal(stock_sum * this.exchangRate).setScale(6, RoundingMode.UP).doubleValue();
    	}else
    	{
    		return 0.00;
    	}
		
    	
	}

	

	public Double getNoMakeInvoiceCurrency() {
		if(this.exchangRate!=null && !"未开票".equals(this.bill_status))
    	{
			if (this.include_tax.equals("否"))
	        {
	            /*return new BigDecimal(this.no_bill_count * this.price_wihtout_tax * this.exchangRate).setScale(2, RoundingMode.UP).doubleValue();*/
				return 0.00;
	        }
	        else
	        {
	            return new BigDecimal(this.no_bill_count * this.price_tax * this.exchangRate).setScale(6, RoundingMode.UP).doubleValue();
	        }
    	}else
    	{
    		return 0.00;
    	}
		
	}

	
	public Double getExchangRate() {
		return exchangRate;
	}


	public void setExchangRate(Double exchangRate) {
		this.exchangRate = exchangRate;
	}

	public String getDeptNamexq()
    {
        return deptNamexq;
    }

    public void setDeptNamexq(String deptNamexq)
    {
        this.deptNamexq = deptNamexq;
    }

    public String getDeptIdxq()
    {
        return deptIdxq;
    }

    public void setDeptIdxq(String deptIdxq)
    {
        this.deptIdxq = deptIdxq;
    }

    public String getLegaler()
    {
        return legaler;
    }

    public void setLegaler(String legaler)
    {
        this.legaler = legaler;
    }

    public String getSupplier()
    {
        return supplier;
    }

    public void setSupplier(String supplier)
    {
        this.supplier = supplier;
    }

    public String getPurchase_no()
    {
        return purchase_no;
    }

    public void setPurchase_no(String purchase_no)
    {
        this.purchase_no = purchase_no;
    }

    public Date getPurchase_date()
    {
        return purchase_date;
    }

    public void setPurchase_date(Date purchase_date)
    {
        this.purchase_date = purchase_date;
    }

    public String getSku()
    {
        return sku;
    }

    public void setSku(String sku)
    {
        this.sku = sku;
    }

    public String getSku_name()
    {
        return sku_name;
    }

    public void setSku_name(String sku_name)
    {
        this.sku_name = sku_name;
    }

    public int getPurchase_count()
    {
        return purchase_count;
    }

    public void setPurchase_count(int purchase_count)
    {
        this.purchase_count = purchase_count;
    }

    public Double getPrice_wihtout_tax()
    {
        return price_wihtout_tax;
    }

    public void setPrice_wihtout_tax(Double price_wihtout_tax)
    {
        this.price_wihtout_tax = price_wihtout_tax;
    }

    public Double getPrice_tax()
    {
        return price_tax;
    }

    public void setPrice_tax(Double price_tax)
    {
        this.price_tax = price_tax;
    }

    public Double getPurchase_sum()
    {
        return purchase_sum;
    }

    public void setPurchase_sum(Double purchase_sum)
    {
        this.purchase_sum = purchase_sum;
    }

    public String getPurchase_money_type()
    {
        return purchase_money_type;
    }

    public void setPurchase_money_type(String purchase_money_type)
    {
        this.purchase_money_type = purchase_money_type;
    }

    public String getBuyer()
    {
        return buyer;
    }

    public void setBuyer(String buyer)
    {
        this.buyer = buyer;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getStock_number()
    {
        return stock_number;
    }

    public void setStock_number(String stock_number)
    {
        this.stock_number = stock_number;
    }

    public Date getStock_date()
    {
        return stock_date;
    }

    public void setStock_date(Date stock_date)
    {
        this.stock_date = stock_date;
    }

    public String getStock_name()
    {
        return stock_name;
    }

    public void setStock_name(String stock_name)
    {
        this.stock_name = stock_name;
    }

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
    public int getStock_count()
    {
        return stock_count;
    }

    public void setStock_count(int stock_count)
    {
        this.stock_count = stock_count;
    }

    public Double getStock_sum()
    {
        return stock_sum;
    }

    public void setStock_sum(Double stock_sum)
    {
        this.stock_sum = stock_sum;
    }

    public String getUsage_inventory()
    {
        return usage_inventory;
    }

    public void setUsage_inventory(String usage_inventory)
    {
        this.usage_inventory = usage_inventory;
    }

    public String getAvailable_inventory()
    {
        return available_inventory;
    }

    public void setAvailable_inventory(String available_inventory)
    {
        this.available_inventory = available_inventory;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

   

    public String getBill_name() {
		return bill_name;
	}

	public void setBill_name(String bill_name) {
		this.bill_name = bill_name;
	}

	public String getBill_unit()
    {
        return bill_unit;
    }

    public void setBill_unit(String bill_unit)
    {
        this.bill_unit = bill_unit;
    }

    public int getNo_bill_count()
    {
        return no_bill_count;
    }

    public void setNo_bill_count(int no_bill_count)
    {
        this.no_bill_count = no_bill_count;
    }

    public String getBill_status()
    {
        return bill_status;
    }

    public void setBill_status(String bill_status)
    {
        this.bill_status = bill_status;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getInclude_tax()
    {
        return include_tax;
    }

    public void setInclude_tax(String include_tax)
    {
        this.include_tax = include_tax;
    }

    public String getTransport_type()
    {
        return transport_type;
    }

    public void setTransport_type(String transport_type)
    {
        this.transport_type = transport_type;
    }

    public String getPay_type_name()
    {
        return pay_type_name;
    }

    public void setPay_type_name(String pay_type_name)
    {
        this.pay_type_name = pay_type_name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
    
    public int getInvoice_num() {
		return invoice_num;
	}
    
    public void setInvoice_num(int invoice_num) {
		this.invoice_num = invoice_num;
	}
    
    public Double getTax_rate() {
		return tax_rate;
	}
    
    public void setTax_rate(Double tax_rate) {
		this.tax_rate = tax_rate;
	}

    /**
     * 未开数量
     * @return
     */
    public String getNoMakeInvoice()
    {
        if (this.include_tax.equals("否"))
        {
            /*return new BigDecimal(this.no_bill_count * this.price_wihtout_tax).setScale(2, RoundingMode.UP).toString();*/
        	return "0";
        }
        else
        {
            return new BigDecimal(this.no_bill_count * this.price_tax).setScale(2, RoundingMode.UP).toString();
        }
    }
    
    public void setNoMakeInvoice(String noMakeInvoice) {
		this.noMakeInvoice = noMakeInvoice;
	}

    /**
     * 已开数量
     * @return
     */
    public String getMakeInvoice()
    {
        if (this.include_tax.equals("否"))
        {
            return new BigDecimal((this.stock_count - this.no_bill_count) * this.price_wihtout_tax)
                .setScale(2, RoundingMode.UP).toString();
        }
        else
        {
            return new BigDecimal((this.stock_count - this.no_bill_count) * this.price_tax).setScale(2, RoundingMode.UP)
                .toString();
        }
    }
    
    public void setMakeInvoice(String makeInvoice) {
		this.makeInvoice = makeInvoice;
	}

}
