SELECT
	`storage`.sku_code,
	`storage`.storage_number AS '单据编号',
	`storage`.create_date AS '单据日期',
	'普通采购' AS '业务类型',
	po.is_tax AS '含税',
	po.legal_person_id AS '法人主体id',
	po.supplier_id AS '供应商id',
	`storage`.warehouse_id AS '入库仓库id',
	`storage`.nondefective_number AS '入库数量',

IF (
	po.is_tax = '1',
	pd.tax_unit_price,
	pd.unit_price
) AS '单位成本',
 po.currency AS '币种'
FROM
	supply_sign.`storage`
JOIN supply_chain.purchase_order po ON `storage`.purchase_number = po.purchase_order_id
JOIN (SELECT purchase_order_id,sku_code,tax_unit_price,unit_price from supply_chain.purchase_demand GROUP BY purchase_order_id,sku_code,tax_unit_price,unit_price) pd ON pd.purchase_order_id = po.purchase_order_id and `storage`.sku_code=pd.sku_code
WHERE
	`storage`.create_date >= '2017-06-28 09:50:20'
AND `storage`.create_date < '2017-06-29 00:00:00';

#------------------------ 采购入库-----------------------------------------


SELECT
	wrs.sku_code,
	ws.document_number AS '单据编号',
	ws.document_date AS '单据日期',
	'其他入库' AS '业务类型',
	NULL AS '含税',
	NULL AS '法人主体id',
	NULL AS '来往单位id',
	wrs.warehouse_id AS '入库仓库id',
	wrs.profit_and_loss AS '入库数量',
	(select price_in from sku_report where document_date<ws.document_date and sku=wrs.sku_code ORDER BY document_date desc LIMIT 1) AS '单位成本',
	(select currency_in from sku_report where document_date<ws.document_date and sku=wrs.sku_code ORDER BY document_date desc LIMIT 1) AS '币种'
FROM
	supply_sign.warehouse_storage ws
JOIN supply_sign.warehouse_storage_record wrs ON ws.document_number = wrs.document_number
AND ws.service_type IN ('5', '6', '7')

#------------------------其他入库--------------------------------------

SELECT
	td.sku,
	ts.transfer_no AS '单据编号',
	ts.create_time AS '单据日期',
	'调拨入库' AS '业务类型',
	ts.is_tax '含税',
	td.subject_id AS '法人主体id',
	ts.common_carrier AS '承运商id',
	ts.warehouse_id AS '调入仓id',
	td.box_count AS '入库数量',
	td.sku_price AS '单位成本',
	po.currency AS '币种'
FROM
	supply_delivery.transfer_slip ts
JOIN supply_delivery.transfer_detail td ON ts.transfer_id = td.transfer_id
JOIN supply_chain.purchase_order po ON td.purchase_no = po.purchase_order_id
WHERE
	ts.transfer_status = '2'

#------------------------------------------调拨入库--------------------------------------------------------