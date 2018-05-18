#创建出库数量金额的视图
DROP VIEW IF EXISTS view_stock_age_report_out;
CREATE VIEW view_stock_age_report_out AS
SELECT
	`sro`.`sku` AS `sku`,
	`sro`.`legaler` AS `legaler`,
	`sro`.`warehouse` AS `warehouse`,
	sum(`sro`.`quantity`) AS `count_out`,
	sum(`sro`.`cost`) AS `money_out`
FROM
	`sku_report` `sro`
	INNER JOIN product_ms.stock st ON st.stock_id = sro.warehouse
WHERE	
	`sro`.`business_type` IN ('调拨出库','采购退货','其他出库','转SKU出库','需求单调拨出库','需求单盘亏出库','无主调拨出库','无主盘亏出库','无主海外调拨出库','海外调海外出库','不良品出库')
	AND st.stock_type = '3'
GROUP BY
	`sro`.`sku`,
	`sro`.`legaler`,
	`sro`.`warehouse`
;