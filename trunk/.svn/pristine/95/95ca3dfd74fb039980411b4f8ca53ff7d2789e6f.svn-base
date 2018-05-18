DROP PROCEDURE IF EXISTS `sync_stock_requirement_sum`;

DELIMITER //
CREATE PROCEDURE sync_stock_requirement_sum()
  BEGIN
    UPDATE stock_requirement
    SET available_inventory = count_in - count_out - usage_inventory, actual_inventory = count_in - count_out;

  END//
DELIMITER ;
