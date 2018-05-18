SELECT
  st.id                                       AS stcodes_id,
  dsor.relate_id,
  dco.seller_id                               AS '法人主体id',
  `bc`.corporation_name                       AS '法人主体',
  dco.declare_order_id                        AS '关联编号',
  dco.create_time                             AS '填单日期',
  dco.declare_date                            AS '申报日期',
  dco.declare_order_date                      AS '出口日期',
  dco.customs_number                          AS '报关单号',
  dco.declare_order_id                        AS '报关合同号',
  ifnull(dco.export_port,
         CASE dco.declare_type
         WHEN 1
           THEN
             '大鹏湾关'
         ELSE
           '深圳湾关'
         END)                                 AS '口岸',
  st.`code_ts`                                AS '海关编号',
  st.`g_name`                                 AS '品名',
  dsor.order_sku_number                       AS '成交数量',
  st.`rule_unit`                              AS '成交单位',
  ROUND(dcro.unit_price,
        2)                                    AS '单价RMB',
  #dcro.unit_price * dsor.order_sku_number AS '含税总价RMB',
  NULL                                        AS '含税总价RMB',
  #   IFNULL(
  #       ROUND(
  #           dsor.order_sku_number * (dcro.unit_price / 1.17) * (
  #             CASE
  #             WHEN dco.seller_id = 1
  #               THEN
  #                 1.4
  #             WHEN dco.seller_id = 2
  #               THEN
  #                 1.1
  #             WHEN (
  #               dco.seller_id = 4
  #               OR dco.seller_id = 9
  #             )
  #               THEN
  #                 1.1
  #             ELSE
  #               1
  #             END
  #           ) * (
  #             SELECT rate
  #             FROM
  #               supply_bankroll.record_rate rr
  #             WHERE
  #               rr.declare_order_id = dco.declare_order_id
  #           ),
  #           2
  #       ),
  #       '0.00'
  #   )                             AS '报关总价USD',
  NULL                                        AS '报关总价USD',
  dsor.purchase_order_id                      AS '采购单号',
  dcro.sku                                    AS 'SKU',
  po.buyer_id                                 AS '采购员id',
  uc.`name`                                   AS '采购员',
  po.purchase_group_id                        AS '采购部门id',
  og.`name`                                   AS '采购部门',
  po.supplier_id                              AS '供应商id',
  `ss`.`name`                                 AS '供应商',
  st.`return_tax`                             AS '退税率',
  IF(
      po.is_tax = '1',
      pd.tax_unit_price / (1 + pd.tax_rate),
      pd.unit_price
  ) * dsor.order_sku_number * st.`return_tax` AS '退税金额',
  dco.declarant                               AS '制单人id',
  uz.`name`                                   AS '制单人',

  IF(
      dco.declare_order_status = '3',
      DATE_FORMAT(dco.update_time, '%Y%m'),
      ''
  )                                           AS '发票月份',
  IFNULL(dninna_itc_idd.declaration_goods_num - dninna_itc_idd.remain_declaration_goods_num,
         0)                                   AS '开票数量',
  IFNULL(dcro.unit_price * (dninna_itc_idd.declaration_goods_num - dninna_itc_idd.remain_declaration_goods_num) /
         (1 + pd.tax_rate), 0.00)             AS '开票不含税金额',
  IFNULL(dninna_itc_idd.remain_declaration_goods_num,
         dsor.order_sku_number)               AS '未开票数量',
  ddo.drawback_remark                         AS '退税说明',

  IF(
      dninna_itc_idd.review_status_id = 'APPROVED',
      0,
      1
  )                                           AS '审核状态',
  ddo.auditor                                 AS '审核员id',
  us.`name`                                   AS '审核员',
  ddo.audit_time                              AS '审核时间',
  ddo.drawbacker                              AS '核验员id',
  uh.`name`                                   AS '核验员',
  ddo.drawback_time                           AS '核验时间',
  dco.declare_id,
  st.`declare_element`,
  st.`brand`,
  st.`type`,
  st.`rule_unit`,
  st.hs_code_id,
  dcro.currency,
  pd.tax_rate                                 AS '税率',
  dco.`contract_number` AS `合同号`
FROM
  (
    SELECT
      relate_id,
      purchase_order_id,
      sku_code,
      sum(order_sku_number) order_sku_number
    FROM
      supply_bankroll.declare_sku_order_relate
    GROUP BY
      relate_id,
      purchase_order_id,
      sku_code
  ) dsor
  JOIN supply_bankroll.declare_order_relate dor ON dsor.relate_id = dor.relate_id
  JOIN supply_bankroll.declare_customs_order dco ON dco.declare_id = dor.declare_id
  JOIN (SELECT *
        FROM supply_bankroll.stcodes
        GROUP BY relate_sku_id) st ON st.relate_sku_id = dsor.relate_id
  JOIN supply_bankroll.declare_customs_relate_order dcro ON dsor.relate_id = dcro.relate_id
  JOIN (
         SELECT
           pd.purchase_order_id,
           IFNULL(
               sc.new_sku_code,
               pd.sku_code
           ) AS 'sku_code',
           pd.tax_unit_price,
           pd.unit_price,
           pd.tax_rate
         FROM
           supply_chain.purchase_demand pd
           LEFT JOIN supply_chain.stock_correspondencea sc ON pd.purchase_demand_id = sc.requirement_id
                                                              AND pd.sku_code = sc.sku_code AND
                                                              pd.sku_code = sc.sku_code AND sc.document_state = 'FINISH'
         GROUP BY
           pd.purchase_order_id,
           pd.sku_code,
           pd.tax_unit_price,
           pd.unit_price,
           pd.tax_rate
       ) pd ON dsor.purchase_order_id = pd.purchase_order_id
               AND dsor.sku_code = pd.sku_code
  JOIN supply_chain.purchase_order po ON po.purchase_order_id = pd.purchase_order_id
  #LEFT JOIN invoice.invoice_declaration_detail idd ON dsor.purchase_order_id = idd.purchase_order_id
  #                                                    AND dsor.sku_code = idd.sku
  #                                                    AND dco.declare_order_id = idd.declaration_number

  LEFT JOIN (SELECT
               dninna.declaration_number,
               itc.review_status_id,
               dninna.purchase_order_id,
               idd.declaration_goods_num,
               idd.remain_declaration_goods_num,
               dninna.sku
             FROM invoice.declaration_number_invoice_no_assoc dninna LEFT JOIN invoice.invoice_tax_collect itc
                 ON itc.invoice_code = dninna.invoice_no
               JOIN invoice.invoice_declaration_detail idd ON dninna.purchase_order_id = idd.purchase_order_id
                                                              AND dninna.sku = idd.sku AND
                                                              dninna.declaration_number = idd.declaration_number
             WHERE itc.del_type = 'N' AND dninna.del_type = 'N'
             GROUP BY dninna.declaration_number, itc.review_status_id, dninna.purchase_order_id, dninna.sku


            ) dninna_itc_idd
    ON dninna_itc_idd.declaration_number = dco.declare_order_id AND
       dsor.purchase_order_id = dninna_itc_idd.purchase_order_id AND dsor.sku_code = dninna_itc_idd.sku
  LEFT JOIN invoice.purchase_invoice pi ON pi.purchase_order_id = dninna_itc_idd.purchase_order_id
                                           AND pi.sku = dninna_itc_idd.sku
  LEFT JOIN supply_bankroll.declare_drawback_order ddo ON ddo.declare_id = dor.declare_id
  LEFT JOIN supplier2.base_corporation bc ON dco.seller_id = bc.corporation_id
  LEFT JOIN supplier2.strict_supplier ss ON po.supplier_id = ss.supplier_id
  LEFT JOIN org_group og ON po.purchase_group_id = og.id
  LEFT JOIN `user` uc ON po.buyer_id = uc.user_id
  LEFT JOIN `user` uz ON dco.declarant = uz.user_id
  LEFT JOIN `user` us ON ddo.auditor = us.user_id
  LEFT JOIN `user` uh ON ddo.drawbacker = uh.user_id
WHERE
  dco.data_status = '1'
  AND dco.declare_order_status = '3'
