DROP PROCEDURE IF EXISTS `sync_stock_all_report`;

DELIMITER //
CREATE PROCEDURE sync_stock_all_report()
  BEGIN
    SET @report_date = now();
    CALL sync_stock_in (@report_date);
    CALL sync_stock_out (@report_date);
    CALL sync_stock_lock (@report_date);
    CALL sync_stock_transit (@report_date);
    CALL sync_stock_sum_v2 ();
  END//
DELIMITER ;
