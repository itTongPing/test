use aukey_report;
DROP PROCEDURE IF EXISTS aukey_report.sync_center_stock_report_detail_info;
CREATE PROCEDURE aukey_report.sync_center_stock_report_detail_info()
BEGIN
	
	/*添加fba退货在途信息*/
	INSERT INTO aukey_report.center_fba_in_transit (
		fba_return_plan_no,
		warehouse_id,
		sku,
		stock_type,
		quantity,
		deliver_date
	)
	select 
		fba_return_plan_no,
		warehouse_id,
		sku,
		2 as stock_type,
		quantity * csdr.height * csdr.length * csdr.width / 1000000 as quantity,
		deliver_date
   from aukey_report.center_fba_in_transit t
  LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
   union ALL
    select 
		fba_return_plan_no,
		warehouse_id,
		sku,
		3 as stock_type,
		quantity *csdr.purchase_price as quantity,
		deliver_date
   from aukey_report.center_fba_in_transit t
  LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
  where stock_type = '1';
   
   
   
   
	
	 /*FBA库存信息的体积和金额*/
	INSERT INTO aukey_report.center_fba_stock_count (
		account_id,
		account_name,
		site_group_id,
		area,
		sku,
		stock_type,
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
	select 
        account_id,
		account_name,
		site_group_id,
		area,
		sku,
		2 as stock_type,
		amazon_sku,
		fnsku,
		product_name,
		your_price,
		sale_quantity * csdr.height * csdr.length * csdr.width / 1000000 as sale_quantity,
		no_sale_quantity * csdr.height * csdr.length * csdr.width / 1000000 as no_sale_quantity,
		quantity * csdr.height * csdr.length * csdr.width / 1000000 as quantity,
		units_shipped_last_24_hrs,
		units_shipped_last_7_days,
		units_shipped_last_30_days,
        units_shipped_last_90_days,
        units_shipped_last_180_days,
        units_shipped_last_365_days
 from aukey_report.center_fba_stock_count t
 LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
  union ALL
	select 
    account_id,
		account_name,
		site_group_id,
		area,
		sku,
		3 as stock_type,
		amazon_sku,
		fnsku,
		product_name,
		your_price,
		sale_quantity*csdr.purchase_price as sale_quantity,
		no_sale_quantity*csdr.purchase_price as no_sale_quantity,
		quantity*csdr.purchase_price as quantity,
		units_shipped_last_24_hrs,
		units_shipped_last_7_days,
		units_shipped_last_30_days,
	    units_shipped_last_90_days,
	    units_shipped_last_180_days,
	    units_shipped_last_365_days
from aukey_report.center_fba_stock_count t
LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
where stock_type = '1';
				
	
/*海外转运仓在途信息的体积和金额*/
	INSERT INTO aukey_report.center_oversea_transport_in_transit (
		transfer_no,
		out_warehouse,
		in_warehouse,
		sku,
		stock_type,
		quantity,
		deliver_date
	)
	select
		transfer_no,
		out_warehouse,
		in_warehouse,
		sku,
		2 as stock_type,
		quantity* csdr.height * csdr.length * csdr.width / 1000000 as quantity,
		deliver_date
	from aukey_report.center_oversea_transport_in_transit t
LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
	union ALL
	select
			transfer_no,
			out_warehouse,
			in_warehouse,
			sku,
			3 as stock_type,
			quantity*csdr.purchase_price as quantity,
			deliver_date
	from aukey_report.center_oversea_transport_in_transit t
	LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
	where stock_type = '1';
	
						
	/*头程在途信息的体积和金额*/
	INSERT INTO aukey_report.center_head_in_transit (
		transfer_no,
		out_warehouse,
		in_warehouse,
		sku,
		stock_type,
		quantity,
		transport_type,
		deliver_date
	)
		select 
		transfer_no,
		out_warehouse,
		in_warehouse,
		sku,
		2 as stock_type,
		quantity * csdr.height * csdr.length * csdr.width / 1000000 as quantity,
		transport_type,
		deliver_date	
	from aukey_report.center_head_in_transit t
LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
	UNION ALL
		select 
			transfer_no,
			out_warehouse,
			in_warehouse,
			sku,
	    3 as stock_type, 
			quantity*csdr.purchase_price as quantity,
			transport_type,
			deliver_date	
	from aukey_report.center_head_in_transit t
  LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
  where stock_type = '1';
	
						

		/*国内中转仓库存信息的体积和金额*/
	INSERT INTO aukey_report.center_domestic_transfer_warehouse (
		warehouse_id,
		sku,
		stock_type,
		quantity
	)
	select   
		warehouse_id,
		sku,
		2 as stock_type,
		quantity * csdr.height * csdr.length * csdr.width / 1000000 as quantity
	from aukey_report.center_domestic_transfer_warehouse t
LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
	UNION ALL
	select   
		warehouse_id,
		sku,
		3 as stock_type,
		quantity*csdr.purchase_price as quantity
	from aukey_report.center_domestic_transfer_warehouse t
	LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
	where stock_type = '1';
	
	/*采购订单在途信息的体积和金额*/
	INSERT INTO aukey_report.center_purchase_in_transit (
		purchase_order_id,
		purchase_demand_id,
		warehouse_id,
		sku,
		stock_type,
		quantity,
		in_way_quantity,
		deliver_date
	)	
	SELECT 
		purchase_order_id,
		purchase_demand_id,
		warehouse_id,
		sku,
		2 as stock_type,
		quantity * csdr.height * csdr.length * csdr.width / 1000000 as quantity,
		in_way_quantity * csdr.height * csdr.length * csdr.width / 1000000 as in_way_quantity,
		deliver_date
	from aukey_report.center_purchase_in_transit t
	LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
	UNION ALL
		SELECT 
		purchase_order_id,
		purchase_demand_id,
		warehouse_id,
		sku,
		3 as stock_type,
		quantity*csdr.purchase_price as quantity,
		in_way_quantity*csdr.purchase_price as in_way_quantity,
		deliver_date
	from aukey_report.center_purchase_in_transit t
  LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
  where stock_type = '1';
						
	/*海外仓库存的体积和金额*/
	INSERT INTO aukey_report.center_oversea_warehouse (
	warehouse_id,
	sku,
	stock_type,
	no_lock_quantity,
	lock_quantity,
	quantity
	)
		select 
		warehouse_id,
		sku,
		2 as stock_type,
		no_lock_quantity * csdr.height * csdr.length * csdr.width / 1000000 as no_lock_quantity,
		lock_quantity * csdr.height * csdr.length * csdr.width / 1000000 as lock_quantity,
		quantity * csdr.height * csdr.length * csdr.width / 1000000 as quantity
		from aukey_report.center_oversea_warehouse t
	LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
	UNION ALL
	select 
		warehouse_id,
		sku,
		3 as stock_type,
		no_lock_quantity *csdr.purchase_price as no_lock_quantity,
		lock_quantity *csdr.purchase_price as lock_quantity,
		quantity *csdr.purchase_price as quantity
		from aukey_report.center_oversea_warehouse t
   LEFT JOIN aukey_report.center_sku_depart_relation csdr on t.sku = csdr.sku_code
   where stock_type = '1';
   end;