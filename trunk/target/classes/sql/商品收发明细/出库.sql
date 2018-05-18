SELECT
	td.sku,
	ts.transfer_no AS '单据编号',
	ts.create_time AS '单据日期',
	'调拨出库' AS '业务类型',
	ts.is_tax '含税',
	td.subject_id AS '法人主体id',
	ts.common_carrier AS '承运商id',
	ts.out_warehouse_id AS '调出仓id',
	td.box_count AS '出库数量',
	td.sku_price AS '单位成本',
	po.currency AS '币种'
FROM
	supply_delivery.transfer_slip ts
JOIN supply_delivery.transfer_detail td ON ts.transfer_id = td.transfer_id
JOIN supply_chain.purchase_order po ON td.purchase_no = po.purchase_order_id
WHERE
	ts.transfer_status = '1'

#------------------------------------------调拨出库--------------------------------------------------------

SELECT
	wrs.sku_code,
	ws.document_number AS '单据编号',
	ws.document_date AS '单据日期',
	'其他出库' AS '业务类型',
	po.is_tax AS '含税',
	po.legal_person_id AS '法人主体id',
	po.supplier_id AS '供应商id',
	wrs.warehouse_id AS '出库仓库id',
	wrs.profit_and_loss AS '出库数量',

IF (
	po.is_tax = '1',
	pd.tax_unit_price,
	pd.unit_price
) AS '单位成本',
 po.currency AS '币种'
FROM
	supply_sign.warehouse_storage ws
JOIN supply_sign.warehouse_storage_record wrs ON ws.document_number = wrs.document_number
JOIN supply_sign.warehouse_storage_requirement wsr ON wrs.document_number = wsr.document_number and wrs.sku_code=wsr.sku_code
JOIN supply_chain.requirement rt ON rt.requirement_no = wsr.requirement_no
JOIN supply_chain.purchase_demand pd ON rt.requirement_no = pd.purchase_demand_id
JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
	ws.service_type IN ('1', '2', '3', '4')

#------------------------------------------其他出库--------------------------------------------------------

SELECT
	prd.sku_code,
	pro.purchase_return_id AS '单据编号',
	pro.documents_date AS '单据日期',
	'采购退货' AS '业务类型',
	pro.is_tax '含税',
	pro.legal_person_id AS '法人主体id',
	pro.supplier_id AS '承运商id',
	pro.warehouse_id AS '调出仓id',
	prd.refund_quality AS '出库数量',
	prd.purchase_price AS '单位成本',
	pro.currency AS '币种'
FROM
	supply_sign.stock_record st
JOIN supply_chain.purchase_return_order pro ON st.document_number = pro.purchase_return_id
JOIN supply_chain.purchase_return_demand prd ON pro.purchase_return_id = prd.purchase_return_id
AND st.sku_code = prd.sku_code
WHERE
	st.type = '21'
AND pro.order_type = '1'


#------------------------------------------退货出库--------------------------------------------------------

SELECT
	st.sku_code,
	max(st.document_number) AS '单据编号',
	max(st.create_time) AS '单据日期',
	'销售出库' AS '业务类型',
	NULL AS '含税',
	NULL AS '法人主体id',
	NULL AS '承运商id',
	st.stock_id AS '出库仓id',
	sum(st.quantity_available) AS '出库数量',
	NULL AS '单位成本',
	NULL AS '币种'
FROM
	supply_sign.stock_record st
WHERE
	st.type = '12'
    group by st.sku_code,st.stock_id

#------------------------------------------销售出库--------------------------------------------------------