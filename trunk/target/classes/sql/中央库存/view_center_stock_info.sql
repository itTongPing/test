
DROP VIEW
IF EXISTS `aukey_report`.`view_center_stock_info`;

CREATE  VIEW `aukey_report`.`view_center_stock_info` AS 


/*FBA库存*/
SELECT
	sku,
	stock_type,
	SUM(quantity) AS stock_count,
	'FBA库存' AS type,
    sum(units_shipped_last_24_hrs) as units_shipped_last_24_hrs ,
    sum(units_shipped_last_7_days) as units_shipped_last_7_days,
	sum(units_shipped_last_30_days) as units_shipped_last_30_days ,
    sum(units_shipped_last_90_days) as units_shipped_last_90_days,
    sum(units_shipped_last_180_days) as units_shipped_last_180_days,
    sum(units_shipped_last_365_days) as units_shipped_last_365_days
FROM
	aukey_report.center_fba_stock_count
WHERE	sku IS NOT NULL
GROUP BY
	sku,stock_type
UNION ALL
/*海外仓转运在途*/
SELECT
	sku,
	stock_type,
	SUM(quantity) AS stock_count,
	'海外仓转运' AS type,
    0 as units_shipped_last_24_hrs,
		0 as units_shipped_last_7_days,
		0 as units_shipped_last_30_days,
    0 as units_shipped_last_90_days,
    0 as units_shipped_last_180_days,
    0 as units_shipped_last_365_days
FROM
	aukey_report.center_oversea_transport_in_transit
GROUP BY
	sku,stock_type
UNION ALL
/*头程在途数量*/
SELECT
	sku,
	stock_type,
	SUM(quantity) AS stock_count,
	CASE transport_type
	WHEN '0' THEN '头程空运'
	WHEN '1' THEN '头程海运'
	WHEN '3' THEN '头程铁运'
	ELSE '无' END AS type,
   0 as units_shipped_last_24_hrs,
		0 as units_shipped_last_7_days,
		0 as units_shipped_last_30_days,
    0 as units_shipped_last_90_days,
    0 as units_shipped_last_180_days,
    0 as units_shipped_last_365_days
FROM
	aukey_report.center_head_in_transit
GROUP BY sku,transport_type,stock_type
UNION ALL
/*国内中转仓库存*/
SELECT
	sku,
	stock_type,
	SUM(quantity) AS stock_count,
	'国内中转仓库存' AS type,
   0 as units_shipped_last_24_hrs,
		0 as units_shipped_last_7_days,
		0 as units_shipped_last_30_days,
    0 as units_shipped_last_90_days,
    0 as units_shipped_last_180_days,
    0 as units_shipped_last_365_days
FROM
	aukey_report.center_domestic_transfer_warehouse
GROUP BY sku,stock_type
UNION ALL
/*采购订单在途*/
SELECT
	sku,
	stock_type,
	SUM(in_way_quantity) stock_count,
	'采购订单在途' AS type,
   0 as units_shipped_last_24_hrs,
		0 as units_shipped_last_7_days,
		0 as units_shipped_last_30_days,
    0 as units_shipped_last_90_days,
    0 as units_shipped_last_180_days,
    0 as units_shipped_last_365_days
FROM
	aukey_report.center_purchase_in_transit
GROUP BY sku,stock_type
UNION ALL
/*FBA退货在途*/
SELECT
	sku,
	stock_type,
	SUM(quantity) AS stock_count,
	'FBA退货在途' AS type,
    0 as units_shipped_last_24_hrs,
	0 as units_shipped_last_7_days,
	0 as units_shipped_last_30_days,
    0 as units_shipped_last_90_days,
    0 as units_shipped_last_180_days,
    0 as units_shipped_last_365_days
FROM
	aukey_report.center_fba_in_transit
GROUP BY
	sku,stock_type
UNION ALL
/*海外仓库存*/
SELECT
	sku,
	stock_type,
	SUM(quantity) AS stock_count,
	'海外仓库存' AS type,
   0 as units_shipped_last_24_hrs,
		0 as units_shipped_last_7_days,
		0 as units_shipped_last_30_days,
    0 as units_shipped_last_90_days,
    0 as units_shipped_last_180_days,
    0 as units_shipped_last_365_days
FROM
	aukey_report.center_oversea_warehouse
GROUP BY
	sku,stock_type
;