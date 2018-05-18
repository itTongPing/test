
use aukey_report;
DROP PROCEDURE IF EXISTS aukey_report.sync_center_stock_report;
CREATE  PROCEDURE aukey_report.sync_center_stock_report()
BEGIN
TRUNCATE TABLE aukey_report.center_stock_report;
INSERT INTO aukey_report.center_stock_report (
	upc,
	sku,
	 stock_type,
	sku_name,
	fba_transfer,
	fba_stock_count,
	oversea_stock_transfer,
	first_air_transfer,
	first_ship_transfer,
	first_trains_transfer,
	transfer_warehouse_count,
	purchase_transfer,
	oversea_stock_count,
	units_shipped_last_24_hrs,   
    units_shipped_last_7_days,   
    units_shipped_last_30_days, 
    units_shipped_last_90_days, 
    units_shipped_last_180_days,
    units_shipped_last_365_days
	
)
SELECT
	u.upc,
	sku,
	stock_type,
	s.name,
	SUM(CASE  WHEN type='FBA退货在途' THEN stock_count ELSE 0 END) AS 'fba_transfer', 
	SUM(CASE  WHEN type='FBA库存' THEN stock_count ELSE 0 END) AS 'fba_stock_count', 
	SUM(CASE  WHEN type='海外仓转运' THEN stock_count ELSE 0 END) AS 'oversea_stock_transfer', 
	SUM(CASE  WHEN type='头程空运' THEN stock_count ELSE 0 END) AS 'first_air_transfer', 
	SUM(CASE  WHEN type='头程海运' THEN stock_count ELSE 0 END) AS 'first_ship_transfer', 
	SUM(CASE  WHEN type='头程铁运' THEN stock_count ELSE 0 END) AS 'first_trains_transfer', 
	SUM(CASE  WHEN type='国内中转仓库存' THEN stock_count ELSE 0 END) AS 'transfer_warehouse_count', 
	SUM(CASE  WHEN type='采购订单在途' THEN stock_count ELSE 0 END) AS 'purchase_transfer',
	SUM(CASE  WHEN type='海外仓库存' THEN stock_count ELSE 0 END) AS 'oversea_stock_count',
	SUM(units_shipped_last_24_hrs) as 'units_shipped_last_24_hrs',
    SUM(units_shipped_last_7_days) as 'units_shipped_last_7_days',
    SUM(units_shipped_last_30_days) as 'units_shipped_last_30_days',
    SUM(units_shipped_last_90_days) as 'units_shipped_last_90_days',
   SUM(units_shipped_last_180_days) as 'units_shipped_last_180_days',
   SUM(units_shipped_last_365_days) as 'units_shipped_last_365_days'
FROM
	aukey_report.view_center_stock_info a
LEFT JOIN product_ms.sku s ON s.code = a.sku
LEFT JOIN product_ms.upc_wsku_relate r ON r.sku_id = s.sku_id
LEFT JOIN product_ms.upc u ON u.id = r.upc_id
GROUP BY
	sku,
	stock_type
;
END
;
