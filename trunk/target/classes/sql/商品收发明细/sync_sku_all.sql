DROP PROCEDURE IF EXISTS `sync_sku_all`;

DELIMITER //
CREATE PROCEDURE sync_sku_all(IN document_date_start DATETIME,IN document_date_end DATETIME)
  BEGIN

#SET @exec_date=DATE_ADD(now(), INTERVAL - 1 DAY);
#SET @exec_date = DATE_FORMAT(@exec_date,'%Y-%m-%d 00:00:00');
call sync_sku_in(document_date_start,document_date_end);
call sync_sku_out(document_date_start,document_date_end);
#call sync_sku_surplus(document_date_start,document_date_end);

  END//
DELIMITER ;
