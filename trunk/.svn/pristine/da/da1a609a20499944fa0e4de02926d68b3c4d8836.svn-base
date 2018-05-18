SELECT
  f.legal_person_id                       AS '法人主体id',
  e.dept_id                               AS '需求部门id',
  e.category_id                           AS '品类Id',
  f.is_tax                                AS '含税',
  e.transport_type                        AS '运输方式',
  a.sku_code,
  a.stock_id                              AS '仓库id',
  b.quantity_available                    AS '可用库存',
  b.stock_quantity                        AS '实际库存',
  b.stock_quantity - b.quantity_available AS '占用库存',

  IF(
      f.is_tax = '1',
      d.tax_unit_price,
      d.unit_price
  ) * b.stock_quantity                    AS '金额',
  `b`.`in_transit_quantity`               AS '在途',
  (
    k.goods_package_length * k.goods_package_width * k.goods_package_height
  ) / 1000000 * b.stock_quantity          AS '体积',
  `a`.`update_time`                       AS '更新时间'
FROM
  supply_sign.stock a
  JOIN supply_sign.stock_request_inquiry b ON a.stock_id = b.stock_id
                                              AND a.sku_code = b.sku_code
  JOIN supply_chain.requirement e ON b.requirement_id = e.requirement_no
  JOIN supply_chain.purchase_demand d ON e.requirement_no = d.purchase_demand_id
  JOIN supply_chain.purchase_order f ON d.purchase_order_id = f.purchase_order_id
  #join stock_ms j on j.stock_id=a.stock_id
  #LEFT JOIN supply_sign.stock_record h ON h.type = '4'
  #                                        AND h.stock_id = a.stock_id
  #                                        AND h.sku_code = a.sku_code
  #                                        AND h.ed_type_id = 'N/A'
  #LEFT JOIN supply_sign.stock_record i ON h.document_number = i.document_number
  #                                        AND i.type = '3'
  JOIN sku_spec k ON a.sku_code = k.CODE
WHERE a.stock_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)

UNION ALL
########################### 转SKU库存
SELECT
  po.legal_person_id                                                                                         AS '法人主体id',
  rt.dept_id                                                                                                 AS '需求部门id',
  rt.category_id                                                                                             AS '品类Id',
  po.is_tax                                                                                                  AS '含税',
  rt.transport_type                                                                                          AS '运输方式',
  sc.new_sku_code,
  sc.stock_id                                                                                                AS '仓库id',
  sc.operation_quantity                                                                                      AS '可用库存',
  sc.stock_quantity                                                                                          AS '实际库存',
  sc.stock_quantity - sc.operation_quantity                                                                  AS '占用库存',
  IF(po.is_tax = '1',
     pd.tax_unit_price,
     pd.unit_price) * sc.stock_quantity                                                                      AS '金额',
  sc.`in_transit_quantity`                                                                                   AS '在途',
  (ss.goods_package_length * ss.goods_package_width * ss.goods_package_height) / 1000000 * sc.stock_quantity AS '体积',
  sc.`operating_time`                                                                                        AS '更新时间'
FROM
  supply_chain.stock_correspondencea sc
  JOIN
  supply_chain.requirement rt ON sc.requirement_id = rt.requirement_no
                                 AND rt.sku_code = sc.sku_code
  JOIN
  supply_chain.purchase_demand pd ON rt.requirement_no = pd.purchase_demand_id
                                     AND rt.sku_code = pd.sku_code
  JOIN
  supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
  JOIN
  sku_spec ss ON sc.sku_code = ss.`code`
WHERE
  sc.stock_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)