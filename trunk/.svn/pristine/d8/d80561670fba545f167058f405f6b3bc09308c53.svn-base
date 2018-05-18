DROP PROCEDURE IF EXISTS `sync_sku_out`;

DELIMITER //
CREATE PROCEDURE sync_sku_out(IN document_date_start DATETIME,IN document_date_end DATETIME)
  BEGIN
#SET @exec_date = DATE_FORMAT(	@exec_date,	'%Y-%m-%d 00:00:00');
#set @document_date_start=DATE_FORMAT(@exec_date,'%Y-%m-%d 00:00:00');
#set @document_date_end=DATE_FORMAT(@document_date_start,'%Y-%m-%d 23:59:59');

#出库无法做到增量，都是根据状态来取的
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
	department_name,
	remark
) SELECT
	UUID(),
	vso.sku_code,
	sku.name,
	vso.document_number,
	vso.document_date,
	vso.business_type,
	vso.is_tax,
	vso.legal_person_id,
	bc.corporation_name,
	vso.supplier_id,
	ss.name,
	vso.warehouse_id,
	sms.name,
	vso.quantity,
	vso.unit_price,
	vso.unit_price * vso.quantity,
	vso.currency,
	vso.create_user,
	u.name,
	vso.transport_type,
	vso.dept_id,
	e.name,
	vso.remark
FROM
	view_sku_out vso
LEFT JOIN product_ms.sku ON vso.sku_code = sku.code
LEFT JOIN product_ms.stock sms ON vso.warehouse_id = sms.stock_id
LEFT JOIN supplier2.base_corporation bc ON vso.legal_person_id = bc.corporation_id
LEFT JOIN supplier2.strict_supplier ss ON vso.supplier_id = ss.supplier_id
LEFT JOIN cas.org_group e ON vso.dept_id = e.id
left join cas.`user` u on vso.create_user=u.user_id
;#where vso.document_date>=document_date_start and vso.document_date<document_date_end;

  END//
DELIMITER ;
