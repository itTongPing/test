DROP PROCEDURE IF EXISTS `declare_report_item_syn`;

DELIMITER //
CREATE PROCEDURE declare_report_item_syn()
  BEGIN
DELETE
FROM
	declare_report_item;

INSERT INTO `declare_report_item`
(
`sku`,
`purchase_no`,
`related_no`,
`bill_no`,
`invoice_serial_number`,
`category`,
`price_tax`,
`price_wihtout_tax`, 
`bill_count`,
`sign_date`,
`entry_date`,
`operator`,
`operator_date`,
`invoice_count`,
`surplus_count`,
`relate_id`,
`invoice_status`)
SELECT
`sku_code`,
`purchase_order_id`,
null,
`发票号码`,
`发票编号`,
`品名`,
`含税单价`,
`不含税单价`,
`开票数量`,
`开票时间`,
`录入时间`,
`操作人`,
`操作时间`,
`发票数量`,
`剩余数量`,
`relate_id`,
`状态`
FROM `view_declare_item`;


    END//
DELIMITER ;
