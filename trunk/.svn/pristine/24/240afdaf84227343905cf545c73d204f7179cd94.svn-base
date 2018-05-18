DROP PROCEDURE IF EXISTS sync_stock_age_info;
CREATE PROCEDURE sync_stock_age_info()
BEGIN
TRUNCATE TABLE aukey_report.stock_age_report;
#通过商品收发明细获取库龄报表的基本信息
INSERT INTO aukey_report.stock_age_report(
	id,
	legaler,
	legaler_name,
	sku,
	sku_name,
	warehouse,
	warehouse_name,
	price
)
SELECT 
	UUID(),
	legaler,
	legaler_name,
	sku,
	sku_name,
	warehouse,
	warehouse_name,
	price
FROM aukey_report.sku_report
GROUP BY 
	sku,
	legaler,
	warehouse
;
#更新品名
UPDATE `stock_age_report` sar
      JOIN product_ms.sku ON sku.`code` = sar.sku
      JOIN product_ms.category ON sku.category_id = category.cate_id
    SET sar.category       = sku.category_id,
				sar.category_name  = category.`name`;
#更新入库数量和入库金额
UPDATE aukey_report.stock_age_report sar 
LEFT JOIN view_stock_age_report_in vsai 
ON sar.sku =  vsai.sku 
AND sar.legaler = vsai.legaler
AND sar.warehouse = vsai.warehouse
SET sar.stock_count_in = IFNULL(vsai.count_in,0),
		sar.money = IFNULL(vsai.money_in,0.00);
#更新出库数量和出库金额
UPDATE aukey_report.stock_age_report sar 
LEFT JOIN view_stock_age_report_out vsao 
ON sar.sku =  vsao.sku 
AND sar.legaler = vsao.legaler
AND sar.warehouse = vsao.warehouse
SET sar.stock_count_out = sar.stock_count-IFNULL(vsao.count_out,0),
		sar.money = sar.money-IFNULL(vsao.money_out,0);
END