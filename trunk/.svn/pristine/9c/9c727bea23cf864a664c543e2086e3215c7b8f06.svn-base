USE aukey_report;
CREATE PROCEDURE aukey_report.sync_center_stock_age_report()
BEGIN
TRUNCATE TABLE aukey_report.center_stock_age_report;
INSERT INTO aukey_report.center_stock_age_report (
	sku_code,
	department_id,
	purchase_count,
	purchase_in_way,
	purchase_receive,
	supplier_name,
	center_transfer_stock,
	center_transfer_age30,
	center_transfer_age30_60,
	center_transfer_age60_180,
	center_transfer_age180_365,
	center_transfer_age365plus,
	head_leg_air,
	head_leg_ship,
	head_leg_train,
	abroad_stock,
	abroad_stock_age90,
	abroad_stock_age90_120,
	abroad_stock_age120_150,
	abroad_stock_age150_180,
	abroad_stock_age180_365,
	abroad_stock_age365plus,
	abroad_stock_in_way,
	fba_stock,
	fba_stock_month3,
	fba_stock_month4_6,
	fba_stock_month6_12,
	fba_stock_month12plus,
	sell_date_count,
	sell_date_avg,
	fba_return_count,
	fba_return_in_way,
	fba_return_receive
)
SELECT
	sk.code,
	/*sk.name AS sku_name,*/
	IFNULL(csdr.department_id,-1),/*IFNULL(co.name,'N/A') AS department_name*/
	IFNULL(purchase.quantity,0),
	IFNULL(purchase.in_wayQ,0),
	IFNULL(purchase.accessQ,0),
	IFNULL(purchase.supply_name,'N/A'),
	IFNULL(center_transfer_stock.stock_count,0),
	IFNULL(center_transfer_stock.age0_30,0),
	IFNULL(center_transfer_stock.age30_60,0),
	IFNULL(center_transfer_stock.age60_180,0),
	IFNULL(center_transfer_stock.age180_365,0),
	IFNULL(center_transfer_stock.age365plus,0),
	IFNULL(head_transfer.first_air_transfer,0),
	IFNULL(head_transfer.first_ship_transfer,0),
	IFNULL(head_transfer.first_trains_transfer,0),
	IFNULL(out_warehouse.stock_count,0),
	IFNULL(out_warehouse.age0_90,0),
	IFNULL(out_warehouse.age90_120,0),
	IFNULL(out_warehouse.age120_150,0),
	IFNULL(out_warehouse.age150_180,0),
	IFNULL(out_warehouse.age180_356,0),
	IFNULL(out_warehouse.age365plus,0),
	IFNULL(out_warehouse_inway.in_way_ow,0),
	IFNULL(fbs.total_quantity,0),
	IFNULL(fbs.month3,0),
	IFNULL(fbs.month6,0),
	IFNULL(fbs.year_low,0),
	IFNULL(fbs.year_plus,0),
	IFNULL(fbs.sell_date_count,0),
	IFNULL(fbs.date_sell,0),
	IFNULL(fbr.return_quantity,0),
	IFNULL(fbr.in_way_re,0),
	IFNULL(fbr.storage_quantity,0)
FROM
product_ms.sku sk 
LEFT JOIN 
(
	SELECT a.sku_code,SUM(a.quantity) AS quantity,SUM(a.in_wayQ) AS in_wayQ,SUM(a.accessQ) AS accessQ,GROUP_CONCAT(DISTINCT a.supply_name) AS supply_name FROM 
	(
		SELECT
			pd.sku_code,pd.quantity,pd.quantity-IFNULL(sr.supply_quantity,0)-IFNULL(prd.refund_quality,0) AS in_wayQ,IFNULL(sr.supply_quantity,0) AS accessQ,ss.name AS supply_name
		FROM
			supply_chain.purchase_demand pd
		LEFT JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
		LEFT JOIN supply_sign.storage_requirement sr ON sr.requirement_no = pd.purchase_demand_id
		LEFT JOIN supply_chain.purchase_return_order pro ON pro.purchase_order_id = po.purchase_order_id AND pro.order_status = '6' AND pro.order_type = '3'
		LEFT JOIN supply_chain.purchase_return_demand prd ON prd.purchase_return_id = pro.purchase_return_id AND pd.purchase_demand_id = prd.purchase_demand_id
		LEFT JOIN supplier2.strict_supplier ss ON ss.supplier_id = po.supplier_id
		WHERE
			po.data_status = '1' AND pd.data_status = '1'
		AND po.order_status IN ('2', '3') AND po.create_date>='2017-6-25 00:00:00'
	) a GROUP BY a.sku_code
) purchase ON purchase.sku_code = sk.code
LEFT JOIN 
(
	SELECT
		sku,
		SUM(stock_count) AS stock_count,
		SUM(age_0_15)+SUM(age_15_30) AS age0_30,
		SUM(age_30_60) AS age30_60,
		SUM(age_60_90)+SUM(age_90_120) + SUM(age_120_150 )+ SUM(age_150_180) AS age60_180,
		SUM(age_180_270)+SUM(age_270_365) AS age180_365,
		SUM(age_365_730)+SUM(age_731) AS age365plus
	FROM
		aukey_report.stock_age_report sar
	LEFT JOIN product_ms.stock s ON sar.warehouse = s.stock_id
	WHERE
		s.stock_type = '3' AND stock_count>0
	GROUP BY
		sku
) center_transfer_stock ON center_transfer_stock.sku = sk.code
LEFT JOIN 
(
	SELECT
		a.sku,
		SUM(CASE WHEN a.transport_type='0' THEN a.count ELSE 0 END) AS first_air_transfer,
		SUM(CASE WHEN a.transport_type='1' THEN a.count ELSE 0 END) AS first_ship_transfer,
		SUM(CASE WHEN a.transport_type='3' THEN a.count ELSE 0 END) AS first_trains_transfer
	FROM
	(
		SELECT
			t1.sku,
			SUM(t1.box_count) AS count,
			t2.transport_type
		FROM
			supply_delivery.transfer_detail t1
		LEFT JOIN supply_delivery.transfer_slip t2 ON t1.transfer_id = t2.transfer_id
		WHERE t2.transfer_status ='1' AND t2.data_status='1'
		GROUP BY t1.sku
		UNION ALL
		SELECT
			t1.sku,
			SUM(tp.box_count-tp.arrival_qty) AS count,
			t2.transport_type
		FROM
			supply_delivery.transfer_part_arrival tp
		LEFT JOIN supply_delivery.transfer_detail t1 ON tp.transfer_id = t1.transfer_id AND tp.box_no=t1.box_no
		LEFT JOIN supply_delivery.transfer_slip t2 ON t1.transfer_id = t2.transfer_id
		WHERE t2.transfer_status = '3' AND t2.data_status='1'
		GROUP BY t1.sku
		UNION ALL
		SELECT
			t1.sku,
			SUM(t1.box_count) AS count,
			t2.transport_type
		FROM
			nogoal_delivery.transfer_detail t1
		LEFT JOIN nogoal_delivery.transfer_slip t2 ON t1.transfer_id = t2.transfer_id
		WHERE t2.transfer_status ='1' AND t2.data_status='1'
		GROUP BY t1.sku
		UNION ALL
		SELECT
			t1.sku,
			SUM(tp.box_count-tp.arrival_qty) AS count,
			t2.transport_type
		FROM
			nogoal_delivery.transfer_part_arrival tp
		LEFT JOIN nogoal_delivery.transfer_detail t1 ON tp.transfer_id = t1.transfer_id AND tp.box_no=t1.box_no
		LEFT JOIN nogoal_delivery.transfer_slip t2 ON t1.transfer_id = t2.transfer_id
		WHERE t2.transfer_status = '3' AND t2.data_status='1'
		GROUP BY t1.sku
) a
GROUP BY a.sku
) head_transfer ON head_transfer.sku = sk.code
LEFT JOIN 
(
	SELECT
		sku,
		SUM(stock_count) AS stock_count,
		SUM(age_0_15)+SUM(age_15_30)+SUM(age_30_60)+SUM(age_60_90) AS age0_90,
		SUM(age_90_120) AS age90_120,
		SUM(age_120_150 ) AS age120_150,
		SUM(age_150_180) AS age150_180,
		SUM(age_180_270)+SUM(age_270_365) AS age180_356,
		SUM(age_365_730)+SUM(age_731) AS age365plus
	FROM
		aukey_report.stock_age_report sar
	LEFT JOIN product_ms.stock s ON sar.warehouse = s.stock_id
	WHERE
		s.stock_type = '11' AND stock_count>0
	GROUP BY
		sku
) out_warehouse ON out_warehouse.sku = sk.code
LEFT JOIN 
(
	SELECT
		ctd.from_wsku,
		SUM(ctdb.box_count-IFNULL(ctdb.quantity_received,0)) AS in_way_ow
	FROM
		supply_sign.center_transfer ct
	INNER JOIN supply_sign.center_transfer_detail ctd ON ct.transfer_no = ctd.transfer_no
	INNER JOIN supply_sign.center_transfer_detail_box ctdb ON ctdb.transfer_detail_id = ctd.transfer_detail_id
	WHERE
		ct.data_status IN ('4', '6')
		AND ct.from_warehouse_id IN(
			SELECT
				stock_id
			FROM
				product_ms.stock
			WHERE
				stock_type = '11'
		)
	GROUP BY
		ctd.from_wsku
) out_warehouse_inway ON out_warehouse_inway.from_wsku = sk.code 
LEFT JOIN 
(
	SELECT f.sku,SUM(f.total_quantity) AS total_quantity,SUM(f.month3) AS month3,SUM(f.month6) AS month6,SUM(f.year_low) AS year_low,SUM(f.year_plus) AS year_plus,SUM(f.sell_30)/30 AS date_sell,IF(SUM(f.sell_30)=0,0,SUM(f.total_quantity)/SUM(f.sell_30)*30) AS sell_date_count FROM 
(
	SELECT
		IFNULL(fsm.sku,fai.company_sku) AS sku,
		total_quantity,
		fai.inv_age_0_to_90_days AS month3,
		fai.inv_age_91_to_180_days AS month6,
		fai.inv_age_181_to_270_days+fai.inv_age_271_to_365_days AS year_low,
		fai.inv_age_365_plus_days AS year_plus,
		fai.units_shipped_last_30_days AS sell_30
	FROM
		fba_stock.fba_age_inventory fai
		 LEFT JOIN (
		   select fnsku,sku from aukey_report.fnsku_sku_matching 
		   GROUP BY fnsku
		   ) fsm on fai.fnsku=fsm.fnsku
	WHERE
		 total_quantity>0
) f
GROUP BY f.sku
) fbs ON fbs.sku = sk.code
LEFT JOIN
(
	SELECT
		SKU,
		SUM(return_quantity) AS return_quantity,
		SUM(return_quantity - storage_quantity) AS in_way_re,
		SUM(storage_quantity) AS storage_quantity
	FROM
		supply_sign.center_fba_return_packages
	WHERE
		status IN (6, 7, 9, 10)
	GROUP BY
		SKU
) fbr ON fbr.SKU = sk.code
LEFT JOIN aukey_report.center_sku_depart_relation csdr ON csdr.sku_code = sk.code
/*LEFT JOIN cas.company_org co ON co.id = csdr.department_id*/
WHERE 
(purchase.sku_code IS NOT NULL OR
	center_transfer_stock.sku IS NOT NULL OR
	head_transfer.sku IS NOT NULL OR
	out_warehouse.sku IS NOT NULL OR
out_warehouse_inway.from_wsku IS NOT NULL OR
fbs.sku IS NOT NULL OR
fbr.SKU IS NOT NULL) /*AND sk.name IS NULL*/;
END;