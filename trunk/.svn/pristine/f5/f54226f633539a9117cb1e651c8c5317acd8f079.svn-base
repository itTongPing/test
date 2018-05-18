DROP PROCEDURE IF EXISTS `sync_sku_in`;

DELIMITER //
CREATE PROCEDURE sync_sku_in(IN document_date_start DATETIME,IN document_date_end DATETIME)
  BEGIN
#SET @exec_date = DATE_FORMAT(	@exec_date,	'%Y-%m-%d 00:00:00');
#set @document_date_start=DATE_FORMAT(@exec_date,'%Y-%m-%d 00:00:00');
#set @document_date_end=DATE_FORMAT(@document_date_start,'%Y-%m-%d 23:59:59');

#select document_date_start, document_date_end;

TRUNCATE TABLE aukey_report.sku_report;
INSERT INTO `sku_report` (
	id,
	sku,
	sku_name,
	document_number,
	document_date,
	business_type,
	is_tax,
	legaler,
	legaler_name,
	intercourse_unit,
	intercourse_unit_name,
	warehouse,
	warehouse_name,
	quantity,
	price,
	cost,
	currency,
	creator,
	creator_name,
	transport_type,
	department,
	department_name
) SELECT
	UUID(),
	vsi.sku_code,
	sku.name,
	vsi.document_number,
	vsi.document_date,
	vsi.business_type,
	vsi.is_tax,
	vsi.legal_person_id,
	bc.corporation_name,
	vsi.supplier_id,
	ss.name,
	vsi.warehouse_id,
	sms.name,
	vsi.quantity,
	vsi.unit_price,
	vsi.unit_price * vsi.quantity,
	vsi.currency,
	vsi.create_user,
	u.name,
	vsi.transport_type,
	vsi.dept_id,
	e.name
FROM
	view_sku_in vsi
LEFT JOIN product_ms.sku ON vsi.sku_code = sku.code
LEFT JOIN product_ms.stock sms ON vsi.warehouse_id = sms.stock_id
LEFT JOIN supplier2.base_corporation bc ON vsi.legal_person_id = bc.corporation_id
LEFT JOIN supplier2.strict_supplier ss ON vsi.supplier_id = ss.supplier_id
LEFT JOIN cas.org_group e ON vsi.dept_id = e.id
left join cas.`user` u on vsi.create_user=u.user_id
where vsi.document_date>'2017-06-28 09:50:20';

  END//
DELIMITER ;
