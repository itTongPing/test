package com.aukey.report.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.aukey.report.base.BaseDao;
import com.aukey.report.dao.TransferReportDao;
import com.aukey.report.dto.TransferReportParam;
import com.aukey.report.utils.page.PageParam;
import com.aukey.report.utils.page.PageResult;
import com.aukey.report.utils.page.TableData;
import com.aukey.report.vo.CustomsFactorsVO;
import com.aukey.report.vo.TransferReportVO;

@Repository
public class TransferReportDaoImpl extends BaseDao implements TransferReportDao
{

    private Logger logger = Logger.getLogger(getClass());

    private static final String SQL_QUERY = "SELECT `transfer_no`, `transfer_date`, `legal_name`,`outtime`,`expected_outtime`, `out_warehouse_name`, `pass_warehouse_name`, `target_warehouse_name`, `sku`, `sku_name`, `box_no`, `box_count`, CASE `transport_type` WHEN 0 THEN '空运' WHEN 1 THEN '海运' WHEN 2 THEN '无' WHEN 3 THEN '铁运' END AS transport_type, `actual_weight`, `box_grow`, `box_broad`, `box_height`, `box_volume`, CASE `transfer_status` WHEN 0 THEN '待调拨' WHEN 1 THEN '已出库' WHEN 2 THEN '已到货' WHEN 3 THEN '部分到货' END AS transfer_status, CASE `is_tax` WHEN 0 THEN '非退税' WHEN 1 THEN '退税' END AS is_tax, `site_id`, `site_name`, `account_id`, `account_name`, `shipment_id`, `fnsku`, `sellersku`, `money`, `tax_rate`, tr.`return_tax`, tr.`declare_order_id`, `declare_order_date`,`customs_number`,`unit_price`, `declare_money`,tr.`currency`,`num`,hs_name AS name,quantity_received "
        + "FROM aukey_report.transfer_report tr " + " WHERE 1 = 1";

    private String[] split;

    @Override
    public TableData<TransferReportVO> queryPage(PageParam pageParam, TransferReportParam param)
    {
        PageResult<TransferReportVO> pageResult = new PageResult<TransferReportVO>(pageParam.getPageNum(),
            pageParam.getNumPerPage());

        StringBuilder sb = new StringBuilder(SQL_QUERY);
        List<Object> ob = new ArrayList<Object>();

        // 时间过滤
        if (param.getFrom_date() != null)
        {
            sb.append(" and transfer_date >= ?");
            ob.add(param.getFrom_date());
        }
        if (param.getTo_date() != null)
        {
            sb.append(" and transfer_date < ?");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(param.getTo_date());
            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
            ob.add(calendar.getTime());
        }
        // 出库时间过滤
        if (param.getOut_from_date() != null)
        {
            sb.append(" and outtime >= ?");
            ob.add(param.getOut_from_date());
        }
        if (param.getOut_to_date() != null)
        {
            sb.append(" and outtime < ?");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(param.getOut_to_date());
            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
            ob.add(calendar.getTime());
        }
        // sku过滤
        if (StringUtils.isNotEmpty(param.getSku()))
        {
            if (param.getSku().indexOf(",") > 0)
            {
                sb.append(" and sku in (");
                String[] skuCodes = param.getSku().split(",");
                for (int i = 0; i < skuCodes.length; i++)
                {
                    String skuCode = skuCodes[i];
                    sb.append("?");
                    if (i < skuCodes.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(skuCode);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and sku like ?");
                ob.add("%" + param.getSku() + "%");
            }
        }

        // 法人主体过滤
        if (StringUtils.isNotEmpty(param.getLegaler()) && !"-1".equalsIgnoreCase(param.getLegaler()))
        {
            if (param.getLegaler().indexOf(",") > 0)
            {
                String[] legalers = param.getLegaler().split(",");
                sb.append(" and legal_id in(");
                for (int i = 0; i < legalers.length; i++)
                {
                    sb.append("?");
                    if (i < legalers.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(legalers[i]);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and legal_id = ?");
                ob.add(param.getLegaler());
            }
        }

        // 调拔单号过滤
        if (StringUtils.isNotEmpty(param.getTransfer_no()))
        {
            if (param.getTransfer_no().indexOf(",") > 0)
            {
                sb.append(" and transfer_no in (");
                String[] orgs = param.getTransfer_no().split(",");
                for (int i = 0; i < orgs.length; i++)
                {
                    String org = orgs[i];
                    sb.append("?");
                    if (i < orgs.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(org);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and transfer_no like ?");
                ob.add("%" + param.getTransfer_no() + "%");
            }
        }

        // 目标仓库过滤
        if (StringUtils.isNotEmpty(param.getTarget_warehouse()) && !"-1".equalsIgnoreCase(param.getTarget_warehouse()))
        {
            if (param.getTarget_warehouse().indexOf(",") > 0)
            {
                String[] warehouses = param.getTarget_warehouse().split(",");
                sb.append(" and target_warehouse_id in(");
                for (int i = 0; i < warehouses.length; i++)
                {
                    sb.append("?");
                    if (i < warehouses.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(warehouses[i]);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and target_warehouse_id = ?");
                ob.add(param.getTarget_warehouse());
            }
        }

        // 线路仓库过滤
        if (StringUtils.isNotEmpty(param.getPass_warehouse()) && !"-1".equalsIgnoreCase(param.getPass_warehouse()))
        {
            if (param.getPass_warehouse().indexOf(",") > 0)
            {
                String[] warehouses = param.getPass_warehouse().split(",");
                sb.append(" and pass_warehouse_id in(");
                for (int i = 0; i < warehouses.length; i++)
                {
                    sb.append("?");
                    if (i < warehouses.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(warehouses[i]);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and pass_warehouse_id = ?");
                ob.add(param.getPass_warehouse());
            }
        }

        // 调出仓库过滤
        if (StringUtils.isNotEmpty(param.getOut_warehouse()) && !"-1".equalsIgnoreCase(param.getOut_warehouse()))
        {
            if (param.getOut_warehouse().indexOf(",") > 0)
            {
                String[] warehouses = param.getOut_warehouse().split(",");
                sb.append(" and out_warehouse_id in(");
                for (int i = 0; i < warehouses.length; i++)
                {
                    sb.append("?");
                    if (i < warehouses.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(warehouses[i]);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and out_warehouse_id = ?");
                ob.add(param.getOut_warehouse());
            }
        }

        // 是否含税过滤
        if (StringUtils.isNotEmpty(param.getIs_tax()) && !"-1".equalsIgnoreCase(param.getIs_tax()))
        {
            sb.append(" and is_tax ='").append(param.getIs_tax()).append("'");
        }

        // 调拔状态过滤
        if (StringUtils.isNotEmpty(param.getTransfer_status()) && !"-1".equalsIgnoreCase(param.getTransfer_status()))
        {
            if (param.getTransfer_status().indexOf(",") > 0)
            {
                String[] status = param.getTransfer_status().split(",");
                sb.append(" and transfer_status in(");
                for (int i = 0; i < status.length; i++)
                {
                    sb.append("?");
                    if (i < status.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(status[i]);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and transfer_status = ?");
                ob.add(param.getTransfer_status());
            }
        }
        // shipmentid过滤
        if (StringUtils.isNotEmpty(param.getShipmentid()))
        {
            if (param.getShipmentid().indexOf(",") > 0)
            {
                sb.append(" and shipment_id in(");
                String[] shipmentids = param.getShipmentid().split(",");
                for (int i = 0; i < shipmentids.length; i++)
                {
                    String shipmentid = shipmentids[i];
                    sb.append("?");
                    if (i < shipmentids.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(shipmentid);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and shipment_id like ?");
                ob.add("%" + param.getShipmentid() + "%");
            }

        }
        // fnsku过滤
        if (StringUtils.isNotEmpty(param.getFnsku()) && !"-1".equalsIgnoreCase(param.getFnsku()))
        {
            if (param.getFnsku().indexOf(",") > 0)
            {
                sb.append(" and fnsku in(");
                String[] fnskus = param.getFnsku().split(",");
                for (int i = 0; i < fnskus.length; i++)
                {
                    String fnsku = fnskus[i];
                    sb.append("?");
                    if (i < fnskus.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(fnsku);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and fnsku like ?");
                ob.add("%" + param.getFnsku() + "%");
            }

        }
        // sellersku过滤
        if (StringUtils.isNotEmpty(param.getSellersku()))
        {
            if (param.getSellersku().indexOf(",") > 0)
            {
                sb.append(" and sellersku in(");
                String[] sellerskus = param.getSellersku().split(",");
                for (int i = 0; i < sellerskus.length; i++)
                {
                    String sellersku = sellerskus[i];
                    sb.append("?");
                    if (i < sellerskus.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(sellersku);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and sellersku like ?");
                ob.add("%" + param.getSellersku() + "%");
            }

        }
        // 店铺
        if (StringUtils.isNotEmpty(param.getAccountName()))
        {
            if (param.getAccountName().indexOf(",") > 0)
            {
                sb.append(" and account_name in (");
                String[] acoountNames = param.getAccountName().split(",");
                for (int i = 0; i < acoountNames.length; i++)
                {
                    String accoutName = acoountNames[i];
                    sb.append("?");
                    if (i < acoountNames.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(accoutName);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and account_name like ?");
                ob.add("%" + param.getAccountName() + "%");
            }
        }
        int total = getRecordsTotal(sb.toString(), ob.toArray());
        sb.append(" order by transfer_date desc");
        sb.append(" limit ").append(pageResult.getStart()).append(", ").append(pageResult.getLimit());
        final BeanPropertyRowMapper<TransferReportVO> rowMapper = new BeanPropertyRowMapper<TransferReportVO>(
            TransferReportVO.class);

        logger.info(sb.toString());

        List<TransferReportVO> rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
        TableData<TransferReportVO> data = new TableData<TransferReportVO>();
        data.setRows(rows);
        data.setTotal(total);
        return data;
    }

    @Override
    public int count(TransferReportParam param)
    {
        StringBuilder sb = new StringBuilder(SQL_QUERY);
        List<Object> ob = new ArrayList<Object>();

        // 时间过滤
        if (param.getFrom_date() != null)
        {
            sb.append(" and transfer_date >= ?");
            ob.add(param.getFrom_date());
        }
        if (param.getTo_date() != null)
        {
            sb.append(" and transfer_date < ?");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(param.getTo_date());
            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
            ob.add(calendar.getTime());
        }
        // 出库时间过滤
        if (param.getOut_from_date() != null)
        {
            sb.append(" and outtime >= ?");
            ob.add(param.getOut_from_date());
        }
        if (param.getOut_to_date() != null)
        {
            sb.append(" and outtime < ?");
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(param.getOut_to_date());
            calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
            ob.add(calendar.getTime());
        }

        // sku过滤
        if (StringUtils.isNotEmpty(param.getSku()))
        {
            if (param.getSku().indexOf(",") > 0)
            {
                sb.append(" and sku in (");
                String[] skuCodes = param.getSku().split(",");
                for (int i = 0; i < skuCodes.length; i++)
                {
                    String skuCode = skuCodes[i];
                    sb.append("?");
                    if (i < skuCodes.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(skuCode);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and sku like ?");
                ob.add("%" + param.getSku() + "%");
            }
        }

        // 法人主体过滤
        if (StringUtils.isNotEmpty(param.getLegaler()) && !"-1".equalsIgnoreCase(param.getLegaler()))
        {
            if (param.getLegaler().indexOf(",") > 0)
            {
                String[] legalers = param.getLegaler().split(",");
                sb.append(" and legal_id in(");
                for (int i = 0; i < legalers.length; i++)
                {
                    sb.append("?");
                    if (i < legalers.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(legalers[i]);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and legal_id = ?");
                ob.add(param.getLegaler());
            }
        }

        // 调拔单号过滤
        if (StringUtils.isNotEmpty(param.getTransfer_no()))
        {
            if (param.getTransfer_no().indexOf(",") > 0)
            {
                sb.append(" and transfer_no in (");
                String[] orgs = param.getTransfer_no().split(",");
                for (int i = 0; i < orgs.length; i++)
                {
                    String org = orgs[i];
                    sb.append("?");
                    if (i < orgs.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(org);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and transfer_no like ?");
                ob.add("%" + param.getTransfer_no() + "%");
            }
        }

        // 目标仓库过滤
        if (StringUtils.isNotEmpty(param.getTarget_warehouse()) && !"-1".equalsIgnoreCase(param.getTarget_warehouse()))
        {
            if (param.getTarget_warehouse().indexOf(",") > 0)
            {
                String[] warehouses = param.getTarget_warehouse().split(",");
                sb.append(" and target_warehouse_id in(");
                for (int i = 0; i < warehouses.length; i++)
                {
                    sb.append("?");
                    if (i < warehouses.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(warehouses[i]);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and target_warehouse_id = ?");
                ob.add(param.getTarget_warehouse());
            }
        }

        // 线路仓库过滤
        if (StringUtils.isNotEmpty(param.getPass_warehouse()) && !"-1".equalsIgnoreCase(param.getPass_warehouse()))
        {
            if (param.getPass_warehouse().indexOf(",") > 0)
            {
                String[] warehouses = param.getPass_warehouse().split(",");
                sb.append(" and pass_warehouse_id in(");
                for (int i = 0; i < warehouses.length; i++)
                {
                    sb.append("?");
                    if (i < warehouses.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(warehouses[i]);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and pass_warehouse_id = ?");
                ob.add(param.getPass_warehouse());
            }
        }

        // 调出仓库过滤
        if (StringUtils.isNotEmpty(param.getOut_warehouse()) && !"-1".equalsIgnoreCase(param.getOut_warehouse()))
        {
            if (param.getOut_warehouse().indexOf(",") > 0)
            {
                String[] warehouses = param.getOut_warehouse().split(",");
                sb.append(" and out_warehouse_id in(");
                for (int i = 0; i < warehouses.length; i++)
                {
                    sb.append("?");
                    if (i < warehouses.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(warehouses[i]);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and out_warehouse_id = ?");
                ob.add(param.getOut_warehouse());
            }
        }

        // 是否含税过滤
        if (StringUtils.isNotEmpty(param.getIs_tax()) && !"-1".equalsIgnoreCase(param.getIs_tax()))
        {
            sb.append(" and is_tax ='").append(param.getIs_tax()).append("'");
        }

        // 调拔状态过滤
        if (StringUtils.isNotEmpty(param.getTransfer_status()) && !"-1".equalsIgnoreCase(param.getTransfer_status()))
        {
            if (param.getTransfer_status().indexOf(",") > 0)
            {
                String[] status = param.getTransfer_status().split(",");
                sb.append(" and transfer_status in(");
                for (int i = 0; i < status.length; i++)
                {
                    sb.append("?");
                    if (i < status.length - 1)
                    {
                        sb.append(",");
                    }
                    ob.add(status[i]);
                }
                sb.append(")");
            }
            else
            {
                sb.append(" and transfer_status = ?");
                ob.add(param.getTransfer_status());
            }
        }

        return getRecordsTotal(sb.toString(), ob.toArray());
    }

    private static final String SQL_QUERY_UNIT_PRICE = "SELECT `unit_price`,`currency`,`num` FROM supply_bankroll.customs_factors where 1=1";

    @Override
    public List<CustomsFactorsVO> queryUnitPrice(String declare_order_id, String sku)
    {
        StringBuilder sb = new StringBuilder(SQL_QUERY_UNIT_PRICE);
        List<Object> ob = new ArrayList<Object>();
        List<CustomsFactorsVO> rows = new ArrayList<>();
        if (StringUtils.isNotBlank(declare_order_id))
        {
            sb.append(" and declare_order_id = ?");
            ob.add(declare_order_id);
        }
        else
        {
            return rows;
        }
        /*
         * if (StringUtils.isNotBlank(declare_order_id)) { sb.append(
         * " and sku_codes like ?"); ob.add("%"+sku+"%"); }else{ return rows; }
         */
        if (StringUtils.isNotBlank(declare_order_id))
        {
            sb.append(" FIND_IN_SET('?");
            sb.append("',t.sku_codes)");
            ob.add(sku);
        }
        else
        {
            return rows;
        }

        final BeanPropertyRowMapper<CustomsFactorsVO> rowMapper = new BeanPropertyRowMapper<CustomsFactorsVO>(
            CustomsFactorsVO.class);
        logger.info(sb.toString());
        rows = getJdbcTemplate().query(sb.toString(), ob.toArray(), rowMapper);
        return rows;
    }

}
