USE aukey_report;
DROP PROCEDURE IF EXISTS aukey_report.sync_purchase_warn_report;
CREATE PROCEDURE aukey_report.sync_purchase_warn_report(IN start_date datetime,IN end_date datetime)
BEGIN
SET @exec_date = now() ;
REPLACE INTO purchase_warn_report (
	purchase_no,
	/*采购单号*/
	purchase_warn_demand,
	/*需求单号*/
	sku_code,
	/*sku*/
	legaler_id,
	/*法人主体id*/
	supplier_id,
	/*供应商id*/
	purchase_date,
	/*采购单日期*/
	purchase_count,
	/*订单数量*/
	currency,
	/*币别*/
	price_tax,
	/*含税单价*/
	price_without_tax,
	/*未税单价*/
	purchase_sum,
	/*订单金额*/
	buyer_id,
	/*采购员ID*/
	department_id,
	/*采购组id*/
	purchase_group_id,
	/*采购组名*/
	purchase_group_name,
	/*业务部门ID*/
	before_stock_date,
  	/*预交货日期*/
  stock_count
) SELECT
	purchase_order.purchase_order_id AS '采购单号',
	purchase_demand.purchase_demand_id AS '需求单号',
	purchase_demand.sku_code,
	purchase_order.legal_person_id AS '法人主体id',
	purchase_order.supplier_id AS '供应商id',
	purchase_order.documents_date AS '采购日期',
	SUM(purchase_demand.quantity) '订单数量',
	purchase_order.currency '币种',

IF (
	purchase_order.is_tax = '1',
	purchase_demand.tax_unit_price,
	NULL
) AS '含税单价',

IF (
	purchase_order.is_tax = '1',
	purchase_demand.tax_unit_price / (1 + purchase_demand.tax_rate),
	purchase_demand.unit_price
) AS '不含税单价',
 CASE is_tax
WHEN '1' THEN
	SUM(
		purchase_demand.tax_money_total
	)
WHEN '0' THEN
	SUM(purchase_demand.money)
END AS '订单金额',
 purchase_order.buyer_id '采购员id',

 (/*目前采购单会对应多个需求部门，后续会限制一个采购单对应一个需求部门，所以历史数据随意选取*/
	select dept_id from supply_chain.requirement t where t.requirement_no=purchase_demand.purchase_demand_id LIMIT 1

) AS '需求部门id',
 purchase_order.purchase_group_id '采购组id',
 purchase_order.purchase_group_name '采购组名',
 purchase_demand.delivery_date '预计交货日期',
(SELECT
		sum(supply_quantity)
	FROM
		supply_sign.storage_requirement st
	WHERE
		st.requirement_no = purchase_demand.purchase_demand_id
		AND st.type = '1'
) stock_count
FROM
	supply_chain.purchase_demand
LEFT JOIN supply_chain.purchase_order ON purchase_demand.purchase_order_id = purchase_order.purchase_order_id
WHERE
	((purchase_order.update_date > start_date AND purchase_order.update_date < end_date) OR (purchase_demand.update_date>start_date AND purchase_demand.update_date<end_date))
AND purchase_order.order_status IN ('2', '3', '4', '5') AND supply_chain.purchase_demand.data_status = '1'
GROUP BY
	purchase_demand.purchase_order_id,
	purchase_demand.purchase_demand_id ; 
/*更新入库数量*/
	UPDATE purchase_warn_report pr
JOIN (
	SELECT
		st.requirement_no,
		SUM(supply_quantity) AS stock_count
	FROM
		supply_sign.storage_requirement st
	WHERE
		st.requirement_no IN (
			SELECT
				s.requirement_no
			FROM
				supply_sign.storage_requirement s
			WHERE
			s.`type` = '1'
			AND	s.update_time > start_date
			AND s.update_time < end_date
		)
	GROUP BY
		st.requirement_no
) stor ON pr.purchase_warn_demand = stor.requirement_no
SET pr.stock_count = stor.stock_count;
/*更新退货数量*/
UPDATE purchase_warn_report pr
JOIN (
	SELECT 
		t.purchase_order_id,demand.purchase_demand_id,sum(demand.refund_quality) refund_quality,sum(case when t.order_type='1' then  demand.refund_quality end) stop_return_count
	FROM 
		supply_chain.purchase_return_order t 
	INNER JOIN 
		supply_chain.purchase_return_demand demand ON t.purchase_return_id = demand.purchase_return_id 
	WHERE 
		t.order_status = '6' AND t.data_status='1' 
	GROUP BY 
		t.purchase_order_id,demand.purchase_demand_id
) ro ON pr.purchase_no = ro.purchase_order_id
AND pr.purchase_warn_demand = ro.purchase_demand_id
SET pr.return_count = ro.refund_quality,pr.stop_return_count = ro.stop_return_count;


/*更新关联信息名称*/
UPDATE purchase_warn_report	pr 
left JOIN product_ms.sku s ON s.`code`=pr.sku_code
left JOIN supplier2.base_corporation  bc ON bc.corporation_id = pr.legaler_id 
left join cas.`user` u on u.user_id = pr.buyer_id 
left join supplier2.supplier sup on sup.supplier_id = pr.supplier_id 
SET 
	pr.sku_name=s.`name` ,
	pr.legaler_name = bc.corporation_name,
  pr.buyer_name = u.`name`,
  pr.supplier_name=sup.`name`
where pr.last_update_date>=@exec_date;
END