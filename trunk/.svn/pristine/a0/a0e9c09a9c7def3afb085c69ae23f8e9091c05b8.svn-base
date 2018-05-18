DROP VIEW IF EXISTS view_purchase_return;
CREATE ALGORITHM=UNDEFINED DEFINER=`aukey_report`@`10.1.1.%` SQL SECURITY DEFINER VIEW view_purchase_return AS
SELECT
	purchase.legal_person_id AS '法人主体id',
	purchase.supplier_id AS '供应商id',
	purchase.purchase_order_id AS '采购单号',
	purchase.documents_date AS '采购时间',
	prd.sku_code,
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
	purop.purchase_return_id '退货单号',
	IFNULL((select sr.time_ed from supply_sign.stock_record sr where purop.purchase_return_id = sr.document_number and prd.sku_code=sr.sku_code and sr.type=21 LIMIT 1), purop.create_date) '退货日期',
	purop.warehouse_id '退货仓库',
	sum(prd.refund_quality) * - 1 '退货数量',
	sum(prd.refund_total_price) * - 1 '退货金额',
	product_hs_code.hs_name '开票品名',
	stcodes.rule_unit AS '开票单位',
	stcodes.brand AS '品牌',
	stcodes.type AS '型号',
	purchase.pay_way AS '结算方式id',
	purop.remark AS '说明'
FROM
	supply_chain.purchase_return_order purop
INNER JOIN supply_chain.purchase_return_demand prd ON purop.purchase_return_id = prd.purchase_return_id
INNER JOIN (
	SELECT
		purchase_order.legal_person_id,
		purchase_order.supplier_id,
		purchase_order.purchase_order_id,
		purchase_order.documents_date,
		purchase_order.buyer_id,
		purchase_order.purchase_group_id,
		purchase_demand.sku_code,
		purchase_demand.tax_rate,
	  req.dept_id,

	IF (
		purchase_order.is_tax = '1',
		purchase_demand.tax_unit_price / (1 + purchase_demand.tax_rate),
		purchase_demand.unit_price
	) AS no_tax_price,

IF (
	purchase_order.is_tax = '1',
	purchase_demand.tax_unit_price,
	NULL
) AS tax_unit_price,
 purchase_order.currency,
 purchase_order.is_tax,
 CASE is_tax
WHEN '1' THEN
	SUM(
		purchase_demand.tax_money_total
	)
WHEN '0' THEN
	SUM(purchase_demand.money)
END AS money_total,
 SUM(purchase_demand.quantity) quantity,
 purchase_order.pay_way
FROM
	supply_chain.purchase_demand
JOIN supply_chain.purchase_order ON purchase_demand.purchase_order_id = purchase_order.purchase_order_id
LEFT JOIN supply_chain.requirement req ON req.requirement_no = purchase_demand.purchase_demand_id
GROUP BY
	purchase_demand.purchase_order_id,
	purchase_demand.sku_code
) purchase ON purop.purchase_order_id = purchase.purchase_order_id
AND prd.sku_code = purchase.sku_code
LEFT JOIN warehouse_locator.stcodes_sku_relate ON stcodes_sku_relate.sku_code = prd.sku_code
LEFT JOIN warehouse_locator.stcodes ON stcodes_sku_relate.stcodes_id = stcodes.id
LEFT JOIN warehouse_locator.product_hs_code ON product_hs_code.code_id = stcodes.hs_code_id
LEFT JOIN product_ms.sku ON sku. CODE = prd.sku_code

WHERE
	purop.order_status in ('4','6')
AND purop.data_status = '1'
and purop.order_type = '1' OR purop.order_type = '4' #获取滞销和数据对冲的退货数据
GROUP BY
	purchase.legal_person_id,
	purchase.supplier_id,
	purchase.purchase_order_id,
	purchase.documents_date,
	prd.sku_code,
	sku.category_id,
	purchase.quantity,
	purchase.tax_unit_price,
	purchase.currency,
	purchase.no_tax_price,
	purchase.is_tax,
	purchase.money_total,
	purchase.buyer_id,
	purchase.purchase_group_id,
	purop.purchase_return_id,
	purop.create_date,
	purop.warehouse_id,
	product_hs_code.hs_name,
	stcodes.rule_unit,
	stcodes.brand,
	stcodes.type,
	purchase.pay_way