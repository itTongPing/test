DROP PROCEDURE IF EXISTS `declare_report_syn`;

DELIMITER //
CREATE PROCEDURE declare_report_syn()
  BEGIN
    DELETE
    FROM
      declare_report;

    INSERT INTO `declare_report`
    (`id`,
     `legaler`,
     `legaler_name`,
     `related_no`,
     `declare_status`,
     `sign_date`,
     `declare_date`,
     `export_date`,
     `declare_no`,
     `contract_no`,
     `port`,
     `item_no`,
     `customs_no`,
     `category`,
     `transaction_count`,
     `transaction_unit`,
     `price_tax`,
     `total_price_tax`,
     `total_usd_tax`,
     `purchase_no`,
     `sku`,
     `buyer`,
     `buyer_name`,
     `department`,
     `department_name`,
     `supplier`,
     `supplier_name`,
     `drawback_rate`,
     `drawback_sum`,
     `producer`,
     `producer_name`,
     `auditor`,
     `auditor_name`,
     `audit_time`,
     `verifer`,
     `verifer_name`,
     `verif_time`,
     `bill_count`,
     `bill_no_tax_sum`,
     `no_bill_count`,
     `drawback_explain`,
     `bill_month`,
     `relate_ids`,
     `group_column`,
     contract_number
     )
      SELECT
        UUID(),
        a.`legaler`,
        a.`legaler_name`,
        a.`related_no`,
        a.`declare_status`,
        a.`sign_date`,
        a.`declare_date`,
        a.`export_date`,
        a.`declare_no`,
        a.`contract_no`,
        a.`port`,
        #         @group_row := CASE
        #                       WHEN @`contract_no` = a.`contract_no`
        #                         THEN @group_row + 1
        #                       ELSE 1
        #                       END                 AS 'item_no',
        a.item_no,
        a.`customs_no`,
        a.`category`,
        a.`transaction_count`,
        a.`transaction_unit`,
        a.`price_tax`,
        a.`total_price_tax`,
        a.`total_usd_tax`,
        a.`purchase_no`,
        a.`sku`,
        a.`buyer`,
        a.`buyer_name`,
        a.`department`,
        a.`department_name`,
        a.`supplier`,
        a.`supplier_name`,
        a.`drawback_rate`,
        a.`drawback_sum`,
        a.`producer`,
        a.`producer_name`,
        a.`auditor`,
        a.`auditor_name`,
        a.audit_time,
        a.`verifer`,
        a.`verifer_name`,
        a.verif_time,
        a.`bill_count`,
        a.`bill_no_tax_sum`,
        a.`no_bill_count`,
        a.`drawback_explain`,
        a.`bill_month`,
        a.`relate_ids`,
        #         @`contract_no` := a.`contract_no` AS 'group_column'
        NULL,
        a.contract_number
      FROM
        (SELECT
           GROUP_CONCAT(DISTINCT t.`relate_id`)     AS 'relate_ids',
           CASE
           WHEN SUM(t.no_bill_count) = 0
             THEN '全部开票'
           WHEN SUM(t.bill_count) = 0
             THEN '未开票'
           WHEN
             SUM(t.bill_count) > 0
             AND SUM(t.no_bill_count) > 0
             THEN
               '部分开票'
           WHEN
             SUM(t.no_bill_count) = 0
             AND t.declare_status > 0
             THEN
               '未审核'
           ELSE NULL
           END                                      AS declare_status,
           t.legaler,
           t.related_no,
           t.sign_date,
           t.declare_date,
           t.export_date,
           t.declare_no,
           t.contract_no,
           t.port,
           t.`item_no`,
           t.customs_no,
           t.category,
           SUM(t.transaction_count)                 AS 'transaction_count',
           t.transaction_unit,
           t.price_tax,
           ROUND(SUM(t.total_price_tax), 2)         AS 'total_price_tax',
           ROUND(SUM(t.total_usd_tax), 2)           AS 'total_usd_tax',
           GROUP_CONCAT(DISTINCT t.purchase_no)     AS 'purchase_no',
           GROUP_CONCAT(DISTINCT t.`SKU`)           AS 'SKU',
           GROUP_CONCAT(DISTINCT t.buyer)           AS 'buyer',
           GROUP_CONCAT(DISTINCT t.department)      AS 'department',
           GROUP_CONCAT(DISTINCT t.supplier)        AS 'supplier',
           t.drawback_rate,
           ROUND(SUM(t.drawback_sum), 2)            AS 'drawback_sum',
           GROUP_CONCAT(DISTINCT t.producer)        AS 'producer',
           t.bill_month,
           SUM(t.bill_count)                        AS 'bill_count',
           ROUND(SUM(t.bill_no_tax_sum), 2)         AS 'bill_no_tax_sum',
           SUM(t.no_bill_count)                     AS 'no_bill_count',
           t.drawback_explain,
           t.legaler_name,
           GROUP_CONCAT(DISTINCT t.supplier_name)   AS supplier_name,
           GROUP_CONCAT(DISTINCT t.department_name) AS department_name,
           GROUP_CONCAT(DISTINCT t.buyer_name)      AS buyer_name,
           GROUP_CONCAT(DISTINCT t.producer_name)   AS producer_name,
           GROUP_CONCAT(DISTINCT t.auditor)         AS 'auditor',
           GROUP_CONCAT(DISTINCT t.auditor_name)    AS 'auditor_name',
           t.audit_time,
           GROUP_CONCAT(DISTINCT t.verifer)         AS 'verifer',
           GROUP_CONCAT(DISTINCT t.verifer_name)    AS 'verifer_name',
           t.verif_time,
           t.hs_code_id,
           t.stcodes_id
         FROM
           declare_report_single t
         GROUP BY t.declare_id, t.customs_no, t.category, t.`declare_element`, t.brand, t.type, t.rule_unit,
           t.hs_code_id, t.`drawback_rate`, t.`price_tax`, t.currency) a
    #         ,(SELECT
    #            @group_row := 1,
    #            @`contract_no` := '') AS b
    #       ORDER BY a.contract_no, a.hs_code_id DESC, a.stcodes_id DESC
    ;

    #修改 所有全部开票的状态
    DROP TEMPORARY TABLE IF EXISTS temp_declare_report_related_no;

    CREATE TEMPORARY TABLE temp_declare_report_related_no SELECT related_no
                                                          FROM declare_report
                                                          GROUP BY related_no
                                                          HAVING GROUP_CONCAT(DISTINCT declare_status) = '全部开票';

    UPDATE declare_report
    SET declare_status = '开票完结'
    WHERE related_no IN (SELECT related_no
                         FROM temp_declare_report_related_no);


    DROP TEMPORARY TABLE IF EXISTS temp_declare_report_related_no;

  END//
DELIMITER ;
