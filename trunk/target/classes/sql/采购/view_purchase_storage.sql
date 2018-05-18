DROP VIEW IF EXISTS `view_purchase_storage`;
CREATE ALGORITHM=UNDEFINED DEFINER=`aukey_report`@`10.1.1.%` SQL SECURITY DEFINER VIEW view_purchase_storage AS
SELECT 
    purchase.legal_person_id AS '法人主体id',
    purchase.supplier_id AS '供应商id',
    purchase.purchase_order_id AS '采购单号',
    purchase.documents_date AS '采购时间',
    `storage`.sku_code,
    sku.category_id AS '品类id',
    purchase.quantity AS '采购数量',
    purchase.tax_unit_price AS '含税单价',
    purchase.currency AS '币种',
    purchase.no_tax_price AS '不含税单价',
    purchase.is_tax AS '含税',
    purchase.money_total AS '采购金额',
    purchase.buyer_id AS '采购员id',
    purchase.purchase_group_id AS '采购部门id',
		purchase.dept_id as '需求部门id',
    `storage`.storage_number AS '入库单号',
    `storage`.create_date AS '入库日期',
    `storage`.warehouse_id AS '入库仓库id',
    `storage`.nondefective_number+`storage`.rejects_number AS '入库数量',
    CASE purchase.is_tax
        WHEN '1' THEN `storage`.nondefective_number * purchase.tax_unit_price
        WHEN '0' THEN `storage`.nondefective_number * purchase.no_tax_price
    END AS '入库金额',
    product_hs_code.hs_name '开票品名',
    stcodes.rule_unit AS '开票单位',
    stcodes.brand AS '品牌',
    stcodes.type AS '型号',
    purchase.pay_way AS '结算方式id'
FROM
    supply_sign.`storage`
        JOIN
    (SELECT
        purchase_order.legal_person_id,
            purchase_order.supplier_id,
            purchase_order.purchase_order_id,
            purchase_order.documents_date,
            purchase_order.buyer_id,
            purchase_order.purchase_group_id,
            purchase_demand.sku_code,
            purchase_demand.tax_rate,
						req.dept_id,
            IF(purchase_order.is_tax = '1', purchase_demand.tax_unit_price / (1 + purchase_demand.tax_rate), purchase_demand.unit_price) AS no_tax_price,
            IF(purchase_order.is_tax = '1', purchase_demand.tax_unit_price,null) AS tax_unit_price,
            purchase_order.currency,
            purchase_order.is_tax,
            CASE is_tax
                WHEN '1' THEN SUM(purchase_demand.tax_money_total)
                WHEN '0' THEN SUM(purchase_demand.money)
            END AS money_total,
            SUM(purchase_demand.quantity) quantity,
             purchase_order.pay_way
    FROM
        supply_chain.purchase_demand
    JOIN supply_chain.purchase_order ON purchase_demand.purchase_order_id = purchase_order.purchase_order_id
		LEFT JOIN supply_chain.requirement req ON req.requirement_no = purchase_demand.purchase_demand_id
    GROUP BY purchase_demand.purchase_order_id , purchase_demand.sku_code) purchase ON `storage`.purchase_number = purchase.purchase_order_id
        AND `storage`.sku_code = purchase.sku_code
     LEFT JOIN
warehouse_locator.stcodes_sku_relate ON stcodes_sku_relate.sku_code = `storage`.sku_code
     LEFT JOIN
    warehouse_locator.stcodes ON stcodes_sku_relate.stcodes_id = stcodes.id
     LEFT JOIN
    warehouse_locator.product_hs_code ON product_hs_code.code_id = stcodes.hs_code_id
    LEFT  JOIN
    product_ms.sku ON sku.code = `storage`.sku_code;