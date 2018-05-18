#------------------------以下是新的SQL-------------------------------------

SELECT 
    @group_row:=CASE
        WHEN @`报关合同号` = a.`报关合同号` THEN @group_row + 1
        ELSE 1
    END AS '项号',
    @`报关合同号`:=a.`报关合同号` AS 'group_column',
    a.*
FROM
    (SELECT 
        GROUP_CONCAT(DISTINCT t.`relate_id`) AS 'relate_ids',
            t.`法人主体id`,
            t.`关联编号`,
            t.`填单日期`,
            t.`申报日期`,
            t.`出口日期`,
            t.`报关单号`,
            t.`报关合同号`,
            t.`口岸`,
            t.`海关编号`,
            t.`品名`,
            SUM(t.`成交数量`) AS '成交数量',
            t.`成交单位`,
            t.`单价RMB`,
            SUM(t.`含税总价RMB`) AS '含税总价RMB',
            SUM(t.`报关总价USD`) AS '报关总价USD',
            GROUP_CONCAT(DISTINCT t.`采购单号`) AS '采购单号',
            GROUP_CONCAT(DISTINCT t.`SKU`) AS 'SKU',
            GROUP_CONCAT(DISTINCT t.`采购员id`) AS '采购员id',
            GROUP_CONCAT(DISTINCT t.`采购部门id`) AS '采购部门id',
            GROUP_CONCAT(DISTINCT t.`供应商id`) AS '供应商',
            t.`退税率`,
            SUM(t.`退税金额`) AS '退税金额',
            GROUP_CONCAT(DISTINCT t.`制单人id`) AS '制单人id',
            t.`发票月份`,
            SUM(t.`开票数量`) AS '开票数量',
            SUM(t.`开票不含税金额`) AS '开票不含税金额',
            SUM(t.`未开票数量`) AS '未开票数量'
    FROM
        (SELECT 
        dsor.relate_id,
            dco.seller_id AS '法人主体id',
            dco.declare_order_id AS '关联编号',
            dco.create_time AS '填单日期',
            dco.declare_date AS '申报日期',
            dco.declare_order_date AS '出口日期',
            dco.customs_number AS '报关单号',
            dco.declare_order_id AS '报关合同号',
            CASE dco.declare_type
                WHEN 1 THEN '大鹏湾关'
                ELSE '深圳湾关'
            END AS '口岸',
            st.`code_ts` AS '海关编号',
            st.`g_name` AS '品名',
            dcro.request_ratio AS '成交数量',
            st.`rule_unit` AS '成交单位',
            ROUND(dcro.unit_price, 2) AS '单价RMB',
            ROUND(dcro.unit_price * dcro.request_ratio, 2) AS '含税总价RMB',
            IFNULL(ROUND(dcro.request_ratio * (dcro.unit_price / 1.17) * 1.1 * (SELECT 
                    rate
                FROM
                    record_rate rr
                WHERE
                    rr.declare_order_id = dco.declare_order_id), 2), '0.00') AS '报关总价USD',
            dsor.purchase_order_id AS '采购单号',
            dcro.sku AS 'SKU',
            po.buyer_id AS '采购员id',
            po.purchase_group_id AS '采购部门id',
            po.supplier_id AS '供应商id',
            st.`return_tax` AS '退税率',
            ROUND(IF(po.is_tax = '1', pd.tax_unit_price / (1 + pd.tax_rate), pd.unit_price) * dsor.order_sku_number * st.`return_tax`, 2) AS '退税金额',
            dco.auditor AS '制单人id',
            CASE dco.declare_order_status
                WHEN 3 THEN DATE_FORMAT(dco.update_time, '%Y%m')
                ELSE ''
            END AS '发票月份',
            idd.declaration_goods_num - idd.remain_declaration_goods_num AS '开票数量',
            pi.unit_price * (declaration_goods_num - remain_declaration_goods_num) AS '开票不含税金额',
            idd.remain_declaration_goods_num AS '未开票数量',
            dco.declare_id,
            st.`declare_element`,
            st.`brand`,
            st.`type`,
            st.`rule_unit`,
            st.hs_code_id,
            dcro.currency
    FROM
        supply_bankroll.declare_sku_order_relate dsor
    JOIN supply_bankroll.declare_order_relate dor ON dsor.relate_id = dor.relate_id
    JOIN supply_bankroll.declare_customs_order dco ON dco.declare_id = dor.declare_id
    JOIN supply_bankroll.stcodes st ON st.relate_sku_id = dsor.relate_id
    JOIN supply_bankroll.declare_customs_relate_order dcro ON dsor.relate_id = dcro.relate_id
    JOIN supply_chain.purchase_demand pd ON dsor.purchase_order_id = pd.purchase_order_id
        AND dsor.sku_code = pd.sku_code
    JOIN supply_chain.purchase_order po ON po.purchase_order_id = pd.purchase_order_id
    LEFT JOIN invoice.invoice_declaration_detail idd ON dsor.purchase_order_id = idd.purchase_order_id
        AND dsor.sku_code = idd.sku
        AND dco.declare_order_id = idd.declaration_number
    LEFT JOIN invoice.purchase_invoice pi ON pi.purchase_order_id = idd.purchase_order_id
        AND pi.sku = idd.sku
    WHERE
        dco.data_status = '1') t
    GROUP BY t.declare_id , t.`海关编号` , t.`品名` , t.`declare_element` , t.brand , t.type , t.rule_unit , t.hs_code_id , t.`退税率` , t.`单价RMB` , t.currency) a,
    (SELECT @group_row:=1, @`报关合同号`:='') AS b
ORDER BY a.`relate_ids`;



# ----------------------------------发票明细---------------

SELECT
	dco.declare_order_id,
	dsor.sku_code,
	dsor.purchase_order_id,
	dsor.relate_id,
	dninna.invoice_no AS '发票编号',
	pi.pro_gname AS '品名',
	pi.tax_unit_price AS '含税单价',
	pi.unit_price AS '不含税单价',
	dninna.allot_declaration_qty AS '开票数量',
	ir.invoice_date AS '开票时间',
	ir.invoice_date AS '录入时间',
	ir.creator AS '操作人',
	ir.invoice_date AS '操作时间',
	psir.invoice_num AS '发票数量',
	psir.remain_num AS '剩余数量'
FROM
	supply_bankroll.declare_sku_order_relate dsor
JOIN supply_bankroll.declare_order_relate dor ON dsor.relate_id = dor.relate_id
JOIN supply_bankroll.declare_customs_order dco ON dco.declare_id = dor.declare_id
JOIN supply_bankroll.stcodes st ON st.relate_sku_id = dsor.relate_id
JOIN invoice.declaration_number_invoice_no_assoc dninna ON dninna.declaration_number = dco.declare_order_id
AND dsor.purchase_order_id = dninna.purchase_order_id
AND dsor.sku_code = dninna.sku
JOIN invoice.purchase_invoice pi ON dsor.purchase_order_id = pi.purchase_order_id
AND dsor.sku_code = pi.sku
JOIN invoice.invoice_record ir ON ir.invoice_code = dninna.invoice_no
LEFT JOIN invoice.purchase_sku_invoice_record psir ON psir.purchase_order_id = pi.purchase_order_id
AND psir.sku = pi.sku;


