SELECT
	dco.declare_order_id,
	dsor.sku_code,
	dsor.purchase_order_id,
	dsor.relate_id,
	dninna.invoice_no AS '发票号码',
	ir.invoice_serial_number AS '发票编号',
	pi.pro_gname AS '品名',
	pi.tax_unit_price AS '含税单价',
	pi.unit_price AS '不含税单价',
	dninna.allot_declaration_qty AS '开票数量',
	ir.invoice_date AS '开票时间',
	ir.invoice_date AS '录入时间',
	ir.creator AS '操作人',
	ir.invoice_date AS '操作时间',
	psir.invoice_num AS '发票数量',
	psir.remain_num AS '剩余数量',
	CASE itc.review_status_id
WHEN 'APPROVED' THEN
	'审核同意'
WHEN 'REJECTED' THEN
	'审核拒绝'
WHEN 'WAITING' THEN
	'等待审核'
END AS '状态'
FROM
(select relate_id,purchase_order_id,sku_code FROM supply_bankroll.declare_sku_order_relate group by relate_id,purchase_order_id,sku_code) dsor
JOIN supply_bankroll.declare_order_relate dor ON dsor.relate_id = dor.relate_id
JOIN supply_bankroll.declare_customs_order dco ON dco.declare_id = dor.declare_id
JOIN supply_bankroll.stcodes st ON st.relate_sku_id = dsor.relate_id and dsor.sku_code=st.sku_code
JOIN invoice.declaration_number_invoice_no_assoc dninna ON dninna.declaration_number = dco.declare_order_id
AND dsor.purchase_order_id = dninna.purchase_order_id
AND dsor.sku_code = dninna.sku and dninna.del_type='N'
JOIN invoice.purchase_invoice pi ON dsor.purchase_order_id = pi.purchase_order_id
AND dsor.sku_code = pi.sku
JOIN invoice.invoice_record ir ON ir.invoice_code = dninna.invoice_no and ir.del_type='N'
LEFT JOIN invoice.purchase_sku_invoice_record psir ON psir.purchase_order_id = pi.purchase_order_id
AND psir.sku = pi.sku and psir.del_type='N' and ir.invoice_code=psir.invoice_no
JOIN invoice.invoice_tax_collect itc ON itc.invoice_code = dninna.invoice_no and itc.del_type='N'

