CREATE
OR REPLACE VIEW view_stock_lock AS 
#------------------------------------------发货计划锁定--------------------------------------------------------
SELECT
	rt.requirement_no,
	po.legal_person_id AS '法人主体id',
	rt.dept_id AS '需求部门id',
	rt.category_id AS '品类Id',
	dp.is_tax AS '含税',
	rt.transport_type AS '运输方式',
	dp.sku AS sku_code,
	dp.out_warehouse_id AS '仓库id',
	dp.allot_number - ifnull(di.box_count, 0) AS '占用库存',
	dp.update_time AS '更新时间'
FROM
	supply_delivery.delivery_plan dp
LEFT JOIN (
	SELECT
		delivery_id,
		sum(box_count) box_count
	FROM
		supply_delivery.delivery_info
	WHERE
		data_status = '1'
	GROUP BY
		delivery_id
) di ON dp.delivery_id = di.delivery_id
JOIN supply_chain.requirement rt ON dp.requirement_no = rt.requirement_no
AND dp.sku = rt.sku_code
JOIN supply_chain.purchase_demand pd ON rt.requirement_no = pd.purchase_demand_id
JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
	dp.`status` != '6'
AND dp.data_status = '1'
AND dp.out_warehouse_id IN  (8,70,71,72,73,93,94,139,140	)  

#------------------------------------------转SKU发货计划锁定--------------------------------------------------------
union all

SELECT
	rt.requirement_no,
	po.legal_person_id AS '法人主体id',
	rt.dept_id AS '需求部门id',
	rt.category_id AS '品类Id',
	dp.is_tax AS '含税',
	rt.transport_type AS '运输方式',
	dp.sku AS sku_code,
	dp.out_warehouse_id AS '仓库id',
	dp.allot_number - ifnull(di.box_count, 0) AS '占用库存',
	dp.update_time AS '更新时间'
FROM
	supply_delivery.delivery_plan dp
LEFT JOIN (
	SELECT
		delivery_id,
		sum(box_count) box_count
	FROM
		supply_delivery.delivery_info
	WHERE
		data_status = '1'
	GROUP BY
		delivery_id
) di ON dp.delivery_id = di.delivery_id
JOIN (
		select 
		`supply_chain`.`stock_correspondencea`.`requirement_id` AS `requirement_id`,
		`supply_chain`.`stock_correspondencea`.`sku_code` AS `sku_code`,
		`supply_chain`.`stock_correspondencea`.`new_sku_code` AS `new_sku_code` 
		from `supply_chain`.`stock_correspondencea` 
		group by `supply_chain`.`stock_correspondencea`.`sku_code`,
				 `supply_chain`.`stock_correspondencea`.`new_sku_code`,
				 `supply_chain`.`stock_correspondencea`.`requirement_id`
	) sto on sto.new_sku_code = dp.sku and dp.requirement_no=sto.requirement_id
JOIN supply_chain.requirement rt ON dp.requirement_no = rt.requirement_no
AND sto.sku_code = rt.sku_code
JOIN supply_chain.purchase_demand pd ON rt.requirement_no = pd.purchase_demand_id
JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
	dp.`status` != '6'
AND dp.data_status = '1'
AND dp.out_warehouse_id IN  (8,70,71,72,73,93,94,139,140	)  

#------------------------------------------调拨锁定--------------------------------------------------------
UNION ALL
	SELECT
		rt.requirement_no,
		transfer.subject_id AS '法人主体id',
		rt.dept_id AS '需求部门id',
		rt.category_id AS '品类Id',
		transfer.is_tax AS '含税',
		rt.transport_type AS '运输方式',
		transfer.sku AS sku_code,
		transfer.out_warehouse_id AS '仓库id',
		transfer.box_count AS '占用库存',
		transfer.update_time AS '更新时间'
	FROM
		(
			SELECT
				requirement_no,
				sku,
				ts.out_warehouse_id,
				td.subject_id,
				ts.is_tax,
				SUM(box_count) box_count,
				MAX(ts.update_time) update_time
			FROM
				supply_delivery.transfer_slip ts
			JOIN supply_delivery.transfer_detail td ON ts.transfer_id = td.transfer_id
			WHERE
				ts.transfer_status IN ('0')
			AND ts.data_status = '1'
			AND td.data_status = '1'
			GROUP BY
				td.requirement_no,
				td.sku,
				ts.out_warehouse_id,
				td.subject_id,
				ts.is_tax
		) transfer #LEFT JOIN
		#supply_chain.purchase_demand pd ON transfer.requirement_no = pd.purchase_demand_id
		#JOIN
		#supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
	JOIN supply_chain.requirement rt ON transfer.requirement_no = rt.requirement_no
	WHERE
		transfer.out_warehouse_id IN (8,70,71,72,73,93,94,139,140	) 

#------------------------------------------佰易退货出库 锁定--------------------------------------------------------
UNION ALL
	SELECT
		rt.requirement_no,
		pro.legal_person_id AS '法人主体id',
		rt.dept_id AS '需求部门id',
		rt.category_id AS '品类Id',
		pro.is_tax AS '含税',
		rt.transport_type AS '运输方式',
		prd.sku_code,
		pro.warehouse_id AS '仓库id',
		prd.refund_quality '占用库存',
		pro.update_date AS '更新时间'
	FROM
		supply_chain.purchase_return_order pro
	JOIN supply_chain.purchase_return_demand prd ON pro.purchase_return_id = prd.purchase_return_id
	JOIN supply_chain.purchase_order po ON pro.purchase_order_id = po.purchase_order_id
	JOIN supply_chain.requirement rt ON prd.purchase_demand_id = rt.requirement_no #
	WHERE
		pro.order_type = '1'
	AND pro.data_status = '1'
	AND pro.order_status IN ('1', '2', '3', '4')
	AND pro.warehouse_id IN  (8,70,71,72,73,93,94,139,140	) 

#------------------------------------------无主库存调拨锁定需求单对应部份--------------------------------------------------------
UNION ALL
	SELECT
		req.requirement_no,
		req.subject_id AS '法人主体id',
		req.dept_id AS '需求部门id',
		req.category_id AS '品类Id',
		po.is_tax AS '含税',
		req.transport_type AS '运输方式',
		csad.wsku,
		csa.warehouse_id AS '仓库id',
		csad.quantity '占用库存',
		csa.create_date AS '更新时间'
	FROM
		supply_sign.center_stockout_apply csa
	inner join supply_sign.center_stockout_apply_detail csad on csa.stockout_apply_no=csad.stockout_apply_no
	inner join supply_sign.center_transfer ct on csa.order_relation_n0=ct.transfer_no
	inner join supply_sign.center_transfer_detail ctd on ct.transfer_no=ctd.transfer_no
	inner join supply_chain.requirement req on ctd.relation_no=req.requirement_no
	inner JOIN supply_chain.purchase_demand pd ON req.requirement_no = pd.purchase_demand_id
	inner JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
	WHERE
		ct.data_status='3'
	AND csa.warehouse_id IN  (8,70,71,72,73,93,94,139,140	)	


#------------------------------------------无主库存调拨锁定--------------------------------------------------------
UNION ALL
SELECT
	null requirement_no,
	3    AS '法人主体id',
	0    AS '需求部门id',
	null AS '品类Id',
	0    AS '含税',
	null AS '运输方式',
	cis.sku,
	cis.warehouse_id  AS '仓库id',
	cis.lock_quantity '占用库存',
	cis.last_update_date AS '更新时间'
FROM
	supply_sign.center_idle_stock cis
	
	