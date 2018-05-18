DROP VIEW IF EXISTS view_stock_out;
CREATE
OR REPLACE VIEW view_stock_out AS 

#------------------------------------------调拨出库--------------------------------------------------------
SELECT
	rt.requirement_no,
	transfer.subject_id AS '法人主体id',
	rt.dept_id AS '需求部门id',
	rt.category_id AS '品类Id',
	transfer.is_tax AS '含税',
	rt.transport_type AS '运输方式',
	transfer.sku AS 'sku_code',
	IF(po.currency='CNY',1,(
		SELECT
		 currency_rate
		FROM
		 product_ms.currency_set
		WHERE
		 audit_time = (
			SELECT
			 MAX(audit_time)
			FROM
			 product_ms.currency_set
			WHERE
				currency_code=po.currency
				AND audit_time <= po.create_date 
				AND audit_status = '1'
		 )
		AND currency_code = po.currency 
		AND audit_status = '1'
	))*IF(po.is_tax='1',pd.tax_unit_price,pd.unit_price) AS price,
	transfer.out_warehouse_id AS '仓库id',
	transfer.box_count AS ck,
	transfer.create_time AS '出库时间',
	'调拨出库' AS '出库类型'
FROM
	(
		SELECT
			requirement_no,
			sku,
			ts.out_warehouse_id,
			td.subject_id,
			ts.is_tax,
			SUM(box_count) box_count,
			MAX(ts.create_time) create_time
		FROM
			supply_delivery.transfer_slip ts
		JOIN supply_delivery.transfer_detail td ON ts.transfer_id = td.transfer_id
		WHERE
			ts.transfer_status IN ('1', '2', '3') #增加部份到货状态2017-11-28
		AND ts.data_status = '1'
		AND td.data_status = '1'
		GROUP BY
			td.requirement_no,
			td.sku,
			ts.out_warehouse_id,
			td.subject_id,
			ts.is_tax
	) transfer 
JOIN supply_chain.requirement rt ON transfer.requirement_no = rt.requirement_no
LEFT JOIN	supply_chain.purchase_demand pd ON transfer.requirement_no = pd.purchase_demand_id
LEFT JOIN	supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
	transfer.out_warehouse_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)

#------------------------------------------退货出库-------------------------------------------------------- 	
UNION ALL
	SELECT
		rt.requirement_no,
		pro.legal_person_id AS '法人主体id',
		rt.dept_id AS '需求部门id',
		rt.category_id AS '品类Id',
		pro.is_tax AS '含税',
		rt.transport_type AS '运输方式',
		prd.sku_code,
		IF(po.currency='CNY',1,(
			SELECT
			 currency_rate
			FROM
			 product_ms.currency_set
			WHERE
			 audit_time = (
				SELECT
				 MAX(audit_time)
				FROM
				 product_ms.currency_set
				WHERE
					currency_code=po.currency
					AND audit_time <= po.create_date 
					AND audit_status = '1'
			 )
			AND currency_code = po.currency 
			AND audit_status = '1'
	))*IF(po.is_tax='1',pd.tax_unit_price,pd.unit_price) AS price,
		pro.warehouse_id AS '仓库id',
		prd.refund_quality,
		pro.create_date AS '出库时间',
		'退货出库' AS '出库类型'
	FROM
		supply_chain.purchase_return_order pro
	JOIN supply_chain.purchase_return_demand prd ON pro.purchase_return_id = prd.purchase_return_id
	JOIN supply_chain.purchase_order po ON pro.purchase_order_id = po.purchase_order_id
	JOIN supply_chain.requirement rt ON prd.purchase_demand_id = rt.requirement_no 
	JOIN supply_chain.purchase_demand pd ON pd.purchase_demand_id = prd.purchase_demand_id AND pd.sku_code=prd.sku_code
	WHERE
		pro.order_type = '1'
	AND pro.data_status = '1'
	AND pro.order_status  in ('4','6')
	AND pro.warehouse_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140) or pro.order_type='4'

#------------------------------------------其他出库--------------------------------------------------------
UNION ALL
	SELECT
		rt.requirement_no,
		po.legal_person_id AS '法人主体id',
		rt.dept_id AS '需求部门id',
		rt.category_id AS '品类Id',
		po.is_tax AS '含税',
		rt.transport_type AS '运输方式',
		wrs.sku_code,
		IF(po.currency='CNY',1,(
			SELECT
			 currency_rate
			FROM
			 product_ms.currency_set
			WHERE
			 audit_time = (
				SELECT
				 MAX(audit_time)
				FROM
				 product_ms.currency_set
				WHERE
					currency_code=po.currency
					AND audit_time <= po.create_date 
					AND audit_status = '1'
			 )
			AND currency_code = po.currency 
			AND audit_status = '1'
	))*IF(po.is_tax='1',pd.tax_unit_price,pd.unit_price) AS price,
		wrs.warehouse_id AS '仓库id',
		wsr.quantity,
		ws.update_date AS '出库时间',
		'盘亏 其他出库' AS '出库类型'
	FROM
		supply_sign.warehouse_storage ws
	JOIN supply_sign.warehouse_storage_record wrs ON ws.document_number = wrs.document_number
	AND wrs.warehouse_id = ws.warehouse_id
	JOIN supply_sign.warehouse_storage_requirement wsr ON wrs.document_number = wsr.document_number
	AND wrs.sku_code = wsr.sku_code
	JOIN supply_chain.requirement rt ON rt.requirement_no = wsr.requirement_no
	JOIN supply_chain.purchase_demand pd ON rt.requirement_no = pd.purchase_demand_id
	JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
	WHERE
		ws.document_state = 'FINISH'
	AND ws.service_type IN ('1', '2', '3', '4')
	AND wrs.warehouse_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)

#------------------------------------------转SKU出库--------------------------------------------------------
UNION ALL
	SELECT
		rt.requirement_no,
		po.legal_person_id AS '法人主体id',
		rt.dept_id AS '需求部门id',
		rt.category_id AS '品类Id',
		po.is_tax AS '含税',
		rt.transport_type AS '运输方式',
		sr.sku_code,
		IF(po.currency='CNY',1,(
			SELECT
			 currency_rate
			FROM
			 product_ms.currency_set
			WHERE
			 audit_time = (
				SELECT
				 MAX(audit_time)
				FROM
				 product_ms.currency_set
				WHERE
					currency_code=po.currency
					AND audit_time <= po.create_date 
					AND audit_status = '1'
			 )
			AND currency_code = po.currency 
			AND audit_status = '1'
	))*IF(po.is_tax='1',pd.tax_unit_price,pd.unit_price) AS price,
		sr.stock_id AS '仓库id',
		sr.quantity_available ck,
		sr.create_time AS '出库时间',
		'转SKU库存 出库' AS '出库类型'
	FROM
		supply_sign.stock_record sr
	JOIN supply_chain.purchase_demand pd ON sr.document_number = pd.purchase_demand_id
	JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
	JOIN supply_chain.requirement rt ON pd.purchase_demand_id = rt.requirement_no
	WHERE
		sr.stock_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)
	AND sr.type = 19 

#------------------------------------------无主调拨出库 扣减需求库存--------------------------------------------------------
union all
	SELECT
		req.requirement_no,
		req.subject_id AS '法人主体id',
		req.dept_id AS '需求部门id',
		req.category_id AS '品类Id',
		po.is_tax AS '含税',
		req.transport_type AS '运输方式',
		ctd.from_wsku AS 'sku_code',
		IF(po.currency='CNY',1,(
			SELECT
			 currency_rate
			FROM
			 product_ms.currency_set
			WHERE
			 audit_time = (
				SELECT
				 MAX(audit_time)
				FROM
				 product_ms.currency_set
				WHERE
					currency_code=po.currency
					AND audit_time <= po.create_date 
					AND audit_status = '1'
			 )
			AND currency_code = po.currency 
			AND audit_status = '1'
	))*IF(po.is_tax='1',pd.tax_unit_price,pd.unit_price) AS price,
		ct.from_warehouse_id AS '仓库id',
		ctd.transfer_amount AS ck,
		ctd.create_date AS '出库时间',
		'无主调拨出库' AS '出库类型'
	
	FROM supply_sign.center_transfer ct
	inner join supply_sign.center_transfer_detail ctd on ct.transfer_no=ctd.transfer_no
	inner join supply_chain.requirement req on ctd.relation_no=req.requirement_no
	inner JOIN supply_chain.purchase_demand pd ON req.requirement_no = pd.purchase_demand_id
	inner JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
	WHERE
		ct.data_status in ('4','5')
	and ct.transfer_type='1'	
    and ct.from_warehouse_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)

#------------------------------------------转SKU无主调拨出库 扣减需求库存--------------------------------------------------------	
union all	
	SELECT
		req.requirement_no,
		req.subject_id AS '法人主体id',
		req.dept_id AS '需求部门id',
		req.category_id AS '品类Id',
		po.is_tax AS '含税',
		req.transport_type AS '运输方式',
		ctd.from_wsku AS 'sku_code',
		IF(po.currency='CNY',1,(
			SELECT
			 currency_rate
			FROM
			 product_ms.currency_set
			WHERE
			 audit_time = (
				SELECT
				 MAX(audit_time)
				FROM
				 product_ms.currency_set
				WHERE
					currency_code=po.currency
					AND audit_time <= po.create_date 
					AND audit_status = '1'
			 )
			AND currency_code = po.currency 
			AND audit_status = '1'
	))*IF(po.is_tax='1',pd.tax_unit_price,pd.unit_price) AS price,
		ct.from_warehouse_id AS '仓库id',
		ctd.transfer_amount AS ck,
		ctd.create_date AS '出库时间',
		'无主调拨出库' AS '出库类型'
	
	FROM
		supply_sign.center_transfer ct
	inner join supply_sign.center_transfer_detail ctd on ct.transfer_no=ctd.transfer_no
	inner join supply_chain.stock_correspondencea sc on ctd.relation_no=sc.id
	inner join supply_chain.requirement req on sc.requirement_id=req.requirement_no
	inner JOIN supply_chain.purchase_demand pd ON req.requirement_no = pd.purchase_demand_id
	inner JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
	WHERE
		ct.data_status in ('4','5')
	and ct.transfer_type='3'	
    and ct.from_warehouse_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)
	
#------------------------------------------无主盘亏出库 扣减需求库存--------------------------------------------------------
union all
SELECT
		rt.requirement_no,
		rt.subject_id AS '法人主体id',
		rt.dept_id AS '需求部门id',
		rt.category_id AS '品类Id',
		po.is_tax AS '含税',
		rt.transport_type AS '运输方式',
		sr.sku_code AS 'sku_code',
		IF(po.currency='CNY',1,(
				SELECT
				 currency_rate
				FROM
				 product_ms.currency_set
				WHERE
				 audit_time = (
					SELECT
					 MAX(audit_time)
					FROM
					 product_ms.currency_set
					WHERE
						currency_code=po.currency
						AND audit_time <= po.create_date 
						AND audit_status = '1'
				 )
				AND currency_code = po.currency 
				AND audit_status = '1'
	))*IF(po.is_tax='1',pd.tax_unit_price,pd.unit_price) AS price,
		sr.stock_id AS '仓库id',
		sr.quantity_available AS ck,
		sr.create_time AS '出库时间',
		'无主盘亏出库' AS '出库类型'
FROM
	supply_sign.stock_record sr
	JOIN supply_chain.requirement rt ON sr.document_number = rt.requirement_no
	JOIN supply_chain.purchase_demand pd ON rt.requirement_no = pd.purchase_demand_id
	JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
	sr.stock_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)
	AND sr.type = '27'

#------------------------------------------无主盘亏出库 扣减转SKU库存--------------------------------------------------------
union all
SELECT
		rt.requirement_no,
		rt.subject_id AS '法人主体id',
		rt.dept_id AS '需求部门id',
		rt.category_id AS '品类Id',
		po.is_tax AS '含税',
		rt.transport_type AS '运输方式',
		sr.sku_code AS 'sku_code',
		IF(po.currency='CNY',1,(
				SELECT
				 currency_rate
				FROM
				 product_ms.currency_set
				WHERE
				 audit_time = (
					SELECT
					 MAX(audit_time)
					FROM
					 product_ms.currency_set
					WHERE
						currency_code=po.currency
						AND audit_time <= po.create_date 
						AND audit_status = '1'
				 )
				AND currency_code = po.currency 
				AND audit_status = '1'
	))*IF(po.is_tax='1',pd.tax_unit_price,pd.unit_price) AS price,
		sr.stock_id AS '仓库id',
		sr.quantity_available AS ck,
		sr.create_time AS '出库时间',
		'无主盘亏出库' AS '出库类型'
FROM
	supply_sign.stock_record sr
	inner join supply_chain.stock_correspondencea sc on sr.document_number=sc.id
	JOIN supply_chain.requirement rt ON sc.requirement_id = rt.requirement_no
	JOIN supply_chain.purchase_demand pd ON rt.requirement_no = pd.purchase_demand_id
	JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
	sr.stock_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)
	AND sr.type = '27'
	
#------------------------------------------无主调拨出库(中转调拨、盘亏出库)--------------------------------------------------------
UNION ALL

SELECT
	null requirement_no,
	3 AS '法人主体id',
	0 AS '需求部门id',
	null AS '品类Id',
	'' AS '含税',
	'' AS '运输方式',
	isr.sku sku_code,
	0.00 AS `price`,
	isr.stock_id AS '仓库id',
	isr.quantity ck,
	isr.create_date AS '出库时间',
	'无主调拨出库' AS '出库类型'
FROM
	supply_sign.center_idle_stock_record isr
	inner join product_ms.stock s on isr.stock_id=s.stock_id
WHERE
	type in ('1','4','9','12')
	and s.stock_type in ('3','11') #只取中转仓、海外中转仓的数据
	

#------------------------------------------不良品出库-------------------------------------------------------------------------------
UNION ALL
SELECT
req.requirement_no AS requirement_no,
po.legal_person_id AS '法人主体id',
req.dept_id        AS '需求部门id',
NULL               AS '品类Id',
po.is_tax          AS '含税',
req.transport_type AS '运输方式',
ts.sku_code        AS sku_code,
IF(po.currency='CNY',1,(
				SELECT
				 currency_rate
				FROM
				 product_ms.currency_set
				WHERE
				 audit_time = (
					SELECT
					 MAX(audit_time)
					FROM
					 product_ms.currency_set
					WHERE
						currency_code=po.currency
						AND audit_time <= po.create_date 
						AND audit_status = '1'
				 )
				AND currency_code = po.currency 
				AND audit_status = '1'
	))*IF(po.is_tax='1',pd.tax_unit_price,pd.unit_price) AS price,
ts.rejects_warehouse_id   AS '仓库id',
th.operation_quantity     AS ck,
th.create_date            AS '出库时间',
'不良品出库'       AS '出库类型'
FROM
	supply_sign.rejects_history th
LEFT JOIN supply_sign.rejects ts ON ts.id = th.reject_id
LEFT JOIN supply_chain.purchase_order po ON po.purchase_order_id=ts.purchase_number
LEFT JOIN supply_chain.purchase_demand pd ON pd.purchase_order_id = po.purchase_order_id AND pd.sku_code=ts.sku_code
LEFT JOIN supply_chain.requirement req ON req.requirement_no=pd.purchase_demand_id
WHERE
	ts.operation_type IN ('3', '7', '9') AND th.operation_type IN('3', '7', '9') AND ts.rejects_warehouse_id IN (12,13,141)