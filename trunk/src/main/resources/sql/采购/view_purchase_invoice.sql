SELECT
    purchase_order_id, sku, SUM(invoice_num) AS '开票数量'
FROM
    invoice.purchase_sku_invoice_record
  WHERE  del_type='N'
GROUP BY purchase_order_id , sku