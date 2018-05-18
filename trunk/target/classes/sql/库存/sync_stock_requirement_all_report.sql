DROP PROCEDURE IF EXISTS `sync_stock_requirement_all_report`;

DELIMITER //
CREATE PROCEDURE sync_stock_requirement_all_report()
  BEGIN
    SET @report_date = now();
    CALL sync_stock_requirement_in(@report_date);
    CALL sync_stock_requirement_out(@report_date);
    CALL sync_stock_requirement_lock(@report_date);
    CALL sync_stock_requirement_transit(@report_date);
    CALL sync_stock_requirement_sum();
  END//
DELIMITER ;
