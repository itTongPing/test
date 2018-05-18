#创建入库数量金额的视图
DROP VIEW IF EXISTS view_stock_age_report_in;
CREATE VIEW view_stock_age_report_in AS
SELECT
	`sr`.`sku` 				AS `sku`,
	`sr`.`legaler` 			AS `legaler`,
	`sr`.`warehouse` 		AS `warehouse`,
	sum(`sr`.`quantity`) 	AS `count_in`,
	sum(`sr`.`cost`) 		AS `money_in`
FROM
	`sku_report` `sr`
	INNER JOIN product_ms.stock st ON st.stock_id = sr.warehouse
WHERE
	`sr`.`business_type` IN ('普通采购','转SKU入库','无主调拨入库','无主盘盈入库','海外调海外入库','FBA退货入库','不良品入库','期初库存') 
	AND st.stock_type = '3'
GROUP BY
	`sr`.`sku`,
	`sr`.`legaler`,
	`sr`.`warehouse`
;