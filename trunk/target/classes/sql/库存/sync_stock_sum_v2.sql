DROP PROCEDURE IF EXISTS `sync_stock_sum_v2`;

DELIMITER //
CREATE PROCEDURE sync_stock_sum_v2()
  BEGIN
	  
    UPDATE stock_report sr
      SET 
          sr.available_inventory = sr.count_in - sr.count_out - sr.usage_inventory,
          sr.actual_inventory = sr.count_in - sr.count_out,
          sr.sum = sr.sum_in - sr.sum_out;

    UPDATE stock_report sr
           LEFT JOIN product_ms.sku_spec ss ON ss.`code` = sr.sku
      SET sr.total_area = (	ss.goods_package_length * ss.goods_package_width * ss.goods_package_height) / 1000000 * sr.actual_inventory;
  END//
DELIMITER ;
