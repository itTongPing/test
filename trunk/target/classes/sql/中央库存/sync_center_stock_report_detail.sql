use aukey_report;
DROP PROCEDURE IF EXISTS aukey_report.sync_center_stock_report_detail;
CREATE PROCEDURE aukey_report.sync_center_stock_report_detail()
BEGIN
	TRUNCATE TABLE aukey_report.center_fba_in_transit;
	TRUNCATE TABLE aukey_report.center_fba_stock_count;
	TRUNCATE TABLE aukey_report.center_oversea_transport_in_transit;
	TRUNCATE TABLE aukey_report.center_head_in_transit;
	TRUNCATE TABLE aukey_report.center_domestic_transfer_warehouse;
	TRUNCATE TABLE aukey_report.center_purchase_in_transit;
	TRUNCATE TABLE aukey_report.center_oversea_warehouse;
	/*添加fba退货在途信息*/
	INSERT INTO aukey_report.center_fba_in_transit (
		fba_return_plan_no,
		warehouse_id,
		sku,
		quantity,
		deliver_date
	)
	 SELECT
			t1.return_plan_no,
			t2.warehouse_id,
			t1.SKU,
			t1.return_quantity - t1.storage_quantity,
			t1.return_date
		FROM
			supply_sign.center_fba_return_packages t1
		LEFT JOIN supply_sign.center_fba_return_plan t2 ON t1.return_plan_no = t2.return_plan_no
		WHERE
			t1.status IN (7, 9, 10);

	/*FBA库存信息*/
	INSERT INTO aukey_report.center_fba_stock_count (
		account_id,
		account_name,
		site_group_id,
		area,
		sku,
		amazon_sku,
		fnsku,
		product_name,
		your_price,
		sale_quantity,
		no_sale_quantity,
		quantity,
		units_shipped_last_24_hrs,
		units_shipped_last_7_days,
		units_shipped_last_30_days,
       units_shipped_last_90_days,
       units_shipped_last_180_days,
       units_shipped_last_365_days
	)
	SELECT
		account_id,
		account_name,
		site_group_id,
		area,
		IFNULL(fsm.sku,fai.company_sku),
		amazon_sku,
		fai.fnsku,
		product_name,
		your_price,
		sellable_quantity,
        unsellable_quantity,
		total_quantity,
		units_shipped_last_24_hrs,
		units_shipped_last_7_days,
		units_shipped_last_30_days,
        units_shipped_last_90_days,
        units_shipped_last_180_days,
        units_shipped_last_365_days
	FROM
		fba_stock.fba_age_inventory fai
		 LEFT JOIN (
		   select fnsku,sku from aukey_report.fnsku_sku_matching 
		   GROUP BY fnsku
		   ) fsm on fai.fnsku=fsm.fnsku
	WHERE
		 total_quantity>0;


	/*海外转运仓在途信息*/
	INSERT INTO aukey_report.center_oversea_transport_in_transit (
		transfer_no,
		out_warehouse,
		in_warehouse,
		sku,
		quantity,
		deliver_date
	)
	SELECT
		ct.transfer_no,
		ct.from_warehouse_id,
		ct.to_warehouse_id,
		ctd.from_wsku,
		sum(
			ctd.transfer_amount - (
				SELECT
					ifnull(sum(quantity_received), 0)
				FROM
					supply_sign.center_transfer_detail_box
				WHERE
					transfer_detail_id = ctd.transfer_detail_id
			)
		) InTransferAmount,
		a.create_date
	FROM
		supply_sign.center_transfer ct
	INNER JOIN supply_sign.center_transfer_detail ctd ON ct.transfer_no = ctd.transfer_no
	LEFT JOIN (
		SELECT
			relation_no transfer_no,
			create_date
		FROM
			supply_sign.center_idle_stock_record
		WHERE
			type = '12'
		GROUP BY
			relation_no
	) a ON ct.transfer_no = a.transfer_no
	WHERE
		ct.data_status IN ('4', '6')
		AND ct.from_warehouse_id IN (
			SELECT
				stock_id
			FROM
				product_ms.stock
			WHERE
				stock_type = '11'
		)
	GROUP BY
		ct.transfer_no,
		ct.from_warehouse_id,
		ct.to_warehouse_id,
		ctd.from_wsku
		;


	/*头程在途信息*/
	INSERT INTO aukey_report.center_head_in_transit (
		transfer_no,
		out_warehouse,
		in_warehouse,
		sku,
		quantity,
		transport_type,
		deliver_date
	)
	SELECT
		t2.transfer_no,
		t2.out_warehouse_id,
		t2.warehouse_id,
		t1.sku,
		SUM(t1.box_count),
		t2.transport_type,
		t4.transfer_date
	FROM
		supply_delivery.transfer_detail t1
	LEFT JOIN supply_delivery.transfer_slip t2 ON t1.transfer_id = t2.transfer_id
	LEFT JOIN (
		SELECT type_id,record_content,MAX(create_time) AS transfer_date FROM supply_delivery.operating_record 
		WHERE record_content='调拨单出库'
		GROUP BY record_content,type_id
		) t4 ON t4.type_id=t2.transfer_id
	WHERE t2.transfer_status ='1' AND t2.data_status='1'
	GROUP BY t2.transfer_no,t2.out_warehouse_id,t2.warehouse_id,t1.sku
	UNION ALL
	SELECT
		t2.transfer_no,
		t2.out_warehouse_id,
		t2.warehouse_id,
		t1.sku,
		SUM(tp.box_count-tp.arrival_qty),
		t2.transport_type,
		t4.transfer_date
	FROM
		supply_delivery.transfer_part_arrival tp
	LEFT JOIN supply_delivery.transfer_detail t1 ON tp.transfer_id = t1.transfer_id AND tp.box_no=t1.box_no
	LEFT JOIN supply_delivery.transfer_slip t2 ON t1.transfer_id = t2.transfer_id
	LEFT JOIN (
		SELECT type_id,record_content,MAX(create_time) AS transfer_date FROM supply_delivery.operating_record 
		WHERE record_content='调拨单出库'
		GROUP BY record_content,type_id
		) t4 ON t4.type_id=t2.transfer_id
	WHERE t2.transfer_status = '3' AND t2.data_status='1'
	GROUP BY t2.transfer_no,t2.out_warehouse_id,t2.warehouse_id,t1.sku
	UNION ALL
	SELECT
		t2.transfer_no,
		t2.out_warehouse_id,
		t2.warehouse_id,
		t1.sku,
		SUM(t1.box_count),
		t2.transport_type,
		t4.transfer_date
	FROM
		nogoal_delivery.transfer_detail t1
	LEFT JOIN nogoal_delivery.transfer_slip t2 ON t1.transfer_id = t2.transfer_id
	LEFT JOIN (
		SELECT type_id,record_content,MAX(create_time) AS transfer_date FROM nogoal_delivery.operating_record 
		WHERE record_content='调拨单出库'
		GROUP BY record_content,type_id
		) t4 ON t4.type_id=t2.transfer_id
	WHERE t2.transfer_status ='1' AND t2.data_status='1'
	GROUP BY t2.transfer_no,t2.out_warehouse_id,t2.warehouse_id,t1.sku
	UNION ALL
	SELECT
		t2.transfer_no,
		t2.out_warehouse_id,
		t2.warehouse_id,
		t1.sku,
		SUM(tp.box_count-tp.arrival_qty),
		t2.transport_type,
		t4.transfer_date
	FROM
		nogoal_delivery.transfer_part_arrival tp
	LEFT JOIN nogoal_delivery.transfer_detail t1 ON tp.transfer_id = t1.transfer_id AND tp.box_no=t1.box_no
	LEFT JOIN nogoal_delivery.transfer_slip t2 ON t1.transfer_id = t2.transfer_id
	LEFT JOIN (
		SELECT type_id,record_content,MAX(create_time) AS transfer_date FROM nogoal_delivery.operating_record 
		WHERE record_content='调拨单出库'
		GROUP BY record_content,type_id
		) t4 ON t4.type_id=t2.transfer_id
	WHERE t2.transfer_status = '3' AND t2.data_status='1'
	GROUP BY t2.transfer_no,t2.out_warehouse_id,t2.warehouse_id,t1.sku
	;
	/*国内中转仓库存信息*/
	INSERT INTO aukey_report.center_domestic_transfer_warehouse (
		warehouse_id,
		sku,
		quantity
	)
	SELECT
		inventory,
		sku,
		SUM(actual_inventory)
	FROM
		aukey_report.stock_report
	WHERE
		inventory IN (
			SELECT
				stock_id
			FROM
				product_ms.stock
			WHERE
				stock_type = '3'
		)
	GROUP BY inventory,sku
	UNION ALL
	SELECT warehouse_id,sku,SUM(quantity) FROM supply_sign.center_warehouse_stock WHERE stock_type='0' AND warehouse_id in
	(
		SELECT
			stock_id
		FROM
			product_ms.stock
		WHERE
			stock_type = '3'	
	)
	 GROUP BY warehouse_id,sku
	 ;
	/*采购订单在途信息*/
	INSERT INTO aukey_report.center_purchase_in_transit (
		purchase_order_id,
		purchase_demand_id,
		warehouse_id,
		sku,
		quantity,
		in_way_quantity,
		deliver_date
	)
	SELECT
	   po.purchase_order_id,
	   pd.purchase_demand_id,
	   po.warehouse_id,
	   pd.sku_code,
	   pd.quantity,
	   IFNULL(pd.quantity,0) - IFNULL((SELECT
	  SUM(supply_quantity) 
	 FROM
	  supply_sign.storage_requirement sr
	 where sr.`type` = '1' and sr.requirement_no=rt.requirement_no
	 GROUP BY
	  requirement_no
	),0) - IFNULL(rd.refund_quality,0),
	   (
	SELECT
	    MAX(leader_create_time) 
	   FROM
	    supply_chain.purchase_order_flow pf
	  where pf.purchase_order_id = po.purchase_order_id
	) AS 'transfer_date'/*,
	   po.delivery_date AS 'delivery_date'*/
	  FROM
	   supply_chain.purchase_order po
	  JOIN supply_chain.purchase_demand pd ON pd.purchase_order_id = po.purchase_order_id
	  JOIN supply_chain.requirement rt ON pd.purchase_demand_id = rt.requirement_no
	
	  LEFT JOIN product_ms.stock st ON st.stock_id = po.warehouse_id
	  LEFT JOIN (
	   SELECT
	    t.purchase_order_id,
	    demand.purchase_demand_id,
	    sum(demand.refund_quality) refund_quality
	   FROM
	    supply_chain.purchase_return_order t
	   INNER JOIN supply_chain.purchase_return_demand demand ON t.purchase_return_id = demand.purchase_return_id
	   WHERE
	    t.order_status IN ('4', '6')
	   AND t.data_status = '1' AND t.order_type='3'
	   GROUP BY
	    t.purchase_order_id,demand.purchase_demand_id
	  ) rd ON pd.purchase_order_id = rd.purchase_order_id AND pd.purchase_demand_id = rd.purchase_demand_id
	  
	  WHERE
	   po.order_status in ('2', '3') AND po.create_date>='2017-06-28 00:00:00'
	   AND po.data_status = '1';
	/*海外仓库存*/
	INSERT INTO aukey_report.center_oversea_warehouse (
	warehouse_id,
	sku,
	no_lock_quantity,
	lock_quantity,
	quantity
	)
	SELECT
		warehouse_id,sku,SUM(available),SUM(lock_quantity),SUM(quantity)
	FROM
		supply_sign.center_warehouse_stock
	WHERE warehouse_id in
	(
		SELECT stock_id FROM product_ms.stock WHERE stock_type in('5','7')
	)
	GROUP BY
	sku,warehouse_id
	UNION ALL
	SELECT
		inventory,
		sku,
		SUM(available_inventory),
		SUM(usage_inventory),
		SUM(actual_inventory)
	FROM
		aukey_report.stock_report
	WHERE inventory in
	(
		SELECT stock_id FROM product_ms.stock WHERE stock_type in('11')
	)
	GROUP BY
		sku,
		inventory;
END;