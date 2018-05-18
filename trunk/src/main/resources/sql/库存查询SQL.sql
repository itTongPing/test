SELECT
  法人主体id,
  需求部门id,
  stock_id,
  sku_code,
  品类Id,
  含税,
  运输方式,
  sum(rk) AS '入库数量'
FROM
  (
    SELECT
      c.legal_person_id AS '法人主体id',
      e.dept_id         AS '需求部门id',
      e.category_id     AS '品类Id',
      c.is_tax          AS '含税',
      e.transport_type  AS '运输方式',
      a.sku_code,
      a.stock_id,
      supply_quantity      rk
    FROM
      supply_sign.stock_request_inquiry a
      JOIN supply_sign.storage_requirement b ON a.requirement_id = b.requirement_no
                                                AND b.`type` = '1'
      JOIN supply_chain.purchase_demand d ON b.requirement_no = d.purchase_demand_id
      JOIN supply_chain.purchase_order c ON d.purchase_order_id = c.purchase_order_id
      JOIN supply_chain.requirement e ON d.purchase_demand_id = e.requirement_no
    WHERE a.stock_id IN (8, 41, 42, 45, 70, 71, 72, 73, 91, 92, 93, 94, 99, 100, 102, 118, 132, 133, 134, 139, 140)
    #
    #join stock_ms j on j.stock_id=a.stock_id
    # 采购入库
    #     UNION ALL
    #     SELECT
    #       c.legal_person_id AS '法人主体id',
    #       e.dept_id         AS '需求部门id',
    #       e.category_id     AS '品类Id',
    #       c.is_tax          AS '含税',
    #       e.transport_type  AS '运输方式',
    #       a.sku_code,
    #       a.stock_id,
    #       b.box_count
    #     FROM
    #       supply_sign.stock_record a
    #       JOIN supply_delivery.transfer_detail b ON b.transfer_id = a.document_number
    #       JOIN supply_chain.purchase_order c ON b.purchase_no = c.purchase_order_id
    #       JOIN supply_chain.requirement e ON b.requirement_no = e.requirement_no #
    #     #join (SELECT SKU_CODE,WAREHOUSE_ID,LOCATOR_CODE FROM warehouse_locator_sku_assoc a join warehouse_locator b on a.LOCATOR_ID=b.LOCATOR_ID) f on a.sku_code=f.SKU_CODE and a.stock_id=f.WAREHOUSE_ID
    #     #join stock_ms j on j.stock_id=a.stock_id
    #     WHERE
    #       a.type = '3' #
    #     #调拨入库
    UNION ALL
    SELECT
      ''            AS '法人主体id',
      ''            AS '需求部门id',
      f.category_id AS '品类Id',
      ''            AS '含税',
      ''            AS '运输方式',
      b.sku_code,
      b.warehouse_id,
      profit_and_loss
    FROM
      supply_sign.warehouse_storage a
      JOIN supply_sign.warehouse_storage_record b ON a.document_number = b.document_number
                                                     AND a.service_type IN ('5', '6', '7')
      #
      #join stock_ms j on j.stock_id=b.warehouse_id
      JOIN sku f ON f.CODE = b.sku_code

    WHERE b.warehouse_id IN (8, 41, 42, 45, 70, 71, 72, 73, 91, 92, 93, 94, 99, 100, 102, 118, 132, 133, 134, 139, 140)
    #
    #盘盈 其他入库
  ) x
GROUP BY
  sku_code,
  法人主体id,
  需求部门id,
  stock_id,
  含税,
  运输方式,
  品类Id;

#-----------------入库----------------------------------


SELECT
  法人主体id,
  需求部门id,
  stock_id,
  sku_code,
  品类Id,
  含税,
  运输方式,
  sum(ck) AS '出库数量'
FROM
  (
    SELECT
      c.legal_person_id AS '法人主体id',
      e.dept_id         AS '需求部门id',
      e.category_id     AS '品类Id',
      c.is_tax          AS '含税',
      e.transport_type  AS '运输方式',
      a.sku_code,
      a.stock_id,
      b.box_count       AS ck
    FROM
      supply_sign.stock_record a
      JOIN supply_delivery.transfer_detail b ON b.transfer_id = a.document_number
      JOIN supply_chain.purchase_order c ON b.purchase_no = c.purchase_order_id
      JOIN supply_chain.requirement e ON b.requirement_no = e.requirement_no #
    #join (SELECT SKU_CODE,WAREHOUSE_ID,LOCATOR_CODE FROM warehouse_locator_sku_assoc a join warehouse_locator b on a.LOCATOR_ID=b.LOCATOR_ID) f on a.sku_code=f.SKU_CODE and a.stock_id=f.WAREHOUSE_ID
    #join stock_ms j on j.stock_id=a.stock_id
    WHERE
      a.type = '4'
      AND a.ed_type_id = 'N/A'
      AND a.stock_id IN (8, 41, 42, 45, 70, 71, 72, 73, 91, 92, 93, 94, 99, 100, 102, 118, 132, 133, 134, 139, 140)
    #
    # 调拨出库
    UNION ALL
    SELECT
      c.legal_person_id AS '法人主体id',
      e.dept_id         AS '需求部门id',
      e.category_id     AS '品类Id',
      c.is_tax          AS '含税',
      e.transport_type  AS '运输方式',
      a.sku_code,
      a.stock_id,
      a.quantity_available
    FROM
      supply_sign.stock_record a
      JOIN supply_chain.purchase_return_order c ON a.document_number = c.purchase_return_id
      JOIN supply_chain.purchase_return_demand d ON c.purchase_return_id = d.purchase_return_id
      JOIN supply_chain.purchase_order h ON c.purchase_order_id = h.purchase_order_id
      JOIN supply_chain.requirement e ON d.purchase_demand_id = e.requirement_no #
    #join (SELECT SKU_CODE,WAREHOUSE_ID,LOCATOR_CODE FROM warehouse_locator_sku_assoc a join warehouse_locator b on a.LOCATOR_ID=b.LOCATOR_ID) f on a.sku_code=f.SKU_CODE and a.stock_id=f.WAREHOUSE_ID
    #join stock_ms j on j.stock_id=a.stock_id
    WHERE
      a.type = '21'
      AND a.stock_id IN (8, 41, 42, 45, 70, 71, 72, 73, 91, 92, 93, 94, 99, 100, 102, 118, 132, 133, 134, 139, 140)
    #
    #退货出库
    UNION ALL
    SELECT
      f.legal_person_id AS '法人主体id',
      e.dept_id         AS '需求部门id',
      e.category_id     AS '品类Id',
      f.is_tax          AS '含税',
      e.transport_type  AS '运输方式',
      a.sku_code,
      a.stock_id,
      a.quantity_available
    FROM
      supply_sign.stock_record a
      JOIN supply_sign.warehouse_storage_record b ON a.document_number = b.document_number
      JOIN supply_sign.warehouse_storage_requirement c ON b.document_number = c.document_number
      JOIN supply_chain.requirement e ON c.requirement_no = e.requirement_no
      JOIN supply_chain.purchase_demand d ON e.requirement_no = d.purchase_demand_id
      JOIN supply_chain.purchase_order f ON d.purchase_order_id = f.purchase_order_id
    WHERE a.stock_id IN (8, 41, 42, 45, 70, 71, 72, 73, 91, 92, 93, 94, 99, 100, 102, 118, 132, 133, 134, 139, 140)
    #
    #join stock_ms j on j.stock_id=a.stock_id
    #盘亏 其他出库
    #     UNION ALL
    #     SELECT
    #       '' AS '法人主体id',
    #       '' AS '需求部门id',
    #       f.category_id AS '品类Id',
    #       '' AS '含税',
    #       '' AS '运输方式',
    #       a.sku_code,
    #       a.stock_id,
    #       a.quantity_available
    #     FROM
    #       supply_sign.stock_record a #
    #       #join stock_ms j on j.stock_id=a.stock_id
    #       JOIN sku f ON f. CODE = a.sku_code
    #     WHERE
    #       a.type = '12' #
    #     #订单出库
  ) x
GROUP BY
  sku_code,
  法人主体id,
  需求部门id,
  stock_id,
  含税,
  运输方式,
  品类Id;

#-----------------出库----------------------------------


SELECT
  法人主体id,
  需求部门id,
  stock_id,
  sku_code,
  品类Id,
  含税,
  运输方式,
  sum(`可用库存`),
  sum(`实际库存`),
  sum(`占用库存`),
  sum(`金额`),
  sum(`在途`),
  sum(`体积`)
FROM
  (
    SELECT
      f.legal_person_id                       AS '法人主体id',
      e.dept_id                               AS '需求部门id',
      e.category_id                           AS '品类Id',
      f.is_tax                                AS '含税',
      e.transport_type                        AS '运输方式',
      a.sku_code,
      a.stock_id,
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
      ) / 1000000 * b.stock_quantity          AS '体积'
    FROM
      supply_sign.stock a
      JOIN supply_sign.stock_request_inquiry b ON a.stock_id = b.stock_id
                                                  AND a.sku_code = b.sku_code
      JOIN supply_chain.requirement e ON b.requirement_id = e.requirement_no
      JOIN supply_chain.purchase_demand d ON e.requirement_no = d.purchase_demand_id
      JOIN supply_chain.purchase_order f ON d.purchase_order_id = f.purchase_order_id
      #join stock_ms j on j.stock_id=a.stock_id
      LEFT JOIN supply_sign.stock_record h ON h.type = '4'
                                              AND h.stock_id = a.stock_id
                                              AND h.sku_code = a.sku_code
                                              AND h.ed_type_id = 'N/A'
      LEFT JOIN supply_sign.stock_record i ON h.document_number = i.document_number
                                              AND i.type = '3'
      JOIN sku_spec k ON a.sku_code = k.CODE
    WHERE a.stock_id IN (8, 41, 42, 45, 70, 71, 72, 73, 91, 92, 93, 94, 99, 100, 102, 118, 132, 133, 134, 139, 140)
    #
    # #能关联到采购单的库存金额
    #        UNION ALL
    #        SELECT
    #          ''                                                                                                         AS '法人主体id',
    #          ''                                                                                                         AS '需求部门id',
    #          f.category_id                                                                                              AS '品类Id',
    #          ''                                                                                                         AS '含税',
    #          ''                                                                                                         AS '运输方式',
    #          a.sku_code,
    #          a.stock_id,
    #          a.quantity_available                                                                                       AS '可用库存',
    #          a.quantity_available                                                                                       AS '实际库存',
    #          0                                                                                                          AS '占用库存',
    #          b.productCost *
    #          a.quantity_available                                                                                       AS '金额',
    #          0                                                                                                          AS '在途',
    #          (k.goods_package_length * k.goods_package_width * k.goods_package_height) / 1000000 *
    #          a.quantity_available                                                                                       AS '体积'
    #        FROM stock_record a
    #          #join stock_ms j on j.stock_id=a.stock_id
    #          JOIN sku f ON f.code = a.sku_code
    #          JOIN sku_spec k ON a.sku_code = k.code
    #          JOIN (SELECT
    #                  st1.`year_month` AS yearMonth,
    #                  st1.productCost  AS productCost,
    #                  st1.sku_code     AS skuCode,
    #                  st1.stock_id     AS stockId
    #                FROM (SELECT
    #                        MAX(sku_seq)                                         AS MaxSkuSeq,
    #                        `year_month`,
    #                        ROUND(SUM(util_price * quantity) / SUM(quantity), 2) AS productCost,
    #                        type,
    #                        stock_id,
    #                        sku_code
    #                      FROM
    #                        stock_costing
    #                      GROUP BY
    #                        type,
    #                        `year_month`,
    #                        sku_code
    #                     ) st1
    #                  JOIN (
    #                         SELECT
    #                           COUNT(*) AS quantity,
    #                           sku_code,
    #                           `year_month`,
    #                           stock_id,
    #                           type
    #                         FROM
    #                           stock_costing
    #                         GROUP BY
    #                           type,
    #                           `year_month`,
    #                           sku_code
    #                       ) st2 ON st1.sku_code = st2.sku_code
    #                                AND st1.`year_month` = st2.`year_month`
    #                                AND st1.type = st2.type
    #                                AND st1.MaxSkuSeq = st2.quantity
    #                                AND st1.stock_id = st2.stock_id
    #                WHERE st2.type = 'IN') b
    #            ON a.sku_code = b.skuCode AND a.stock_id = b.stockId AND b.yearMonth = DATE_FORMAT(a.create_time, '%Y%m')
    #        WHERE a.type IN ('1')
    #        #关联不到采购单的库存金额
  ) x
GROUP BY
  sku_code,
  法人主体id,
  需求部门id,
  stock_id,
  含税,
  运输方式,
  品类Id;

#-----------------实际+可用+占用----------------------------------
