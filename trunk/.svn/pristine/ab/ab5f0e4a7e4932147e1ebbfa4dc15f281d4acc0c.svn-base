DROP PROCEDURE IF EXISTS `sync_stock_sum_v2`;

DELIMITER //
CREATE PROCEDURE sync_stock_sum_v2()
  BEGIN
	  
    UPDATE stock_report_intermediate sr
      SET 
          sr.available_inventory = sr.count_in - sr.count_out - sr.usage_inventory,
          sr.actual_inventory = sr.count_in - sr.count_out,
          sr.sum = sr.sum_in - sr.sum_out;

    UPDATE stock_report_intermediate sr
           LEFT JOIN product_ms.sku_spec ss ON ss.`code` = sr.sku
      SET sr.total_area = (	ss.goods_package_length * ss.goods_package_width * ss.goods_package_height) / 1000000 * sr.actual_inventory;
     #将中间表的数据插入到库存表中
     TRUNCATE TABLE aukey_report.stock_report;
     INSERT INTO aukey_report.stock_report (
		id,
		legaler,
		legaler_name,
		department,
		department_name,
		country,
		country_name,
		sku,
		sku_name,
		category,
		category_name,
		inventory,
		inventory_name,
		count_in,
		count_out,
		usage_inventory,
		way_inventory,
		available_inventory,
		actual_inventory,
		sum_in,
		sum_out,
		sum,
		include_tax,
		total_area,
		transport_type
	)
	SELECT
		id,
		legaler,
		legaler_name,
		department,
		department_name,
		country,
		country_name,
		sku,
		sku_name,
		category,
		category_name,
		inventory,
		inventory_name,
		count_in,
		count_out,
		usage_inventory,
		way_inventory,
		available_inventory,
		actual_inventory,
		sum_in,
		sum_out,
		sum,
		include_tax,
		total_area,
		transport_type
	FROM
		aukey_report.stock_report_intermediate;
      
  END//
DELIMITER ;
