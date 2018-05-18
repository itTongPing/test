DROP PROCEDURE IF EXISTS `declare_report_single_syn`;

DELIMITER //
CREATE PROCEDURE declare_report_single_syn()
  BEGIN
    DELETE
    FROM
      declare_report_single;

    INSERT INTO declare_report_single (
      `id`,
      `stcodes_id`,
      `relate_id`,
      `declare_status`,
      `legaler`,
      `legaler_name`,
      `related_no`,
      `sign_date`,
      `declare_date`,
      `export_date`,
      `declare_no`,
      `contract_no`,
      `port`,
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
      `bill_month`,
      `bill_count`,
      `bill_no_tax_sum`,
      `no_bill_count`,
      `drawback_explain`,
      `declare_id`,
      `declare_element`,
      `brand`,
      `type`,
      `rule_unit`,
      `hs_code_id`,
      `currency`,
      `tax_rate`,
      contract_number
    ) SELECT
        UUID(),
        `stcodes_id`,
        `relate_id`,
        `审核状态`,
        `法人主体id`,
        `法人主体`,
        `关联编号`,
        `填单日期`,
        `申报日期`,
        `出口日期`,
        `报关单号`,
        `报关合同号`,
        `口岸`,
        `海关编号`,
        `品名`,
        `成交数量`,
        `成交单位`,
        `单价RMB`,
        `含税总价RMB`,
        `报关总价USD`,
        `采购单号`,
        `SKU`,
        `采购员id`,
        `采购员`,
        `采购部门id`,
        `采购部门`,
        `供应商id`,
        `供应商`,
        `退税率`,
        `退税金额`,
        `制单人id`,
        `制单人`,
        `审核员id`,
        `审核员`,
        `审核时间`,
        `核验员id`,
        `核验员`,
        `核验时间`,
        `发票月份`,
        `开票数量`,
        `开票不含税金额`,
        `未开票数量`,
        `退税说明`,
        declare_id,
        declare_element,
        brand,
        `type`,
        rule_unit,
        hs_code_id,
        currency,
        `税率`,
        `合同号`
      FROM
        view_declare_single
      WHERE
        declare_id IN
        (SELECT dco.declare_id
         FROM
           supply_bankroll.declare_customs_order dco
           LEFT JOIN
           declare_report_single drs ON dco.declare_id = drs.declare_id
         WHERE
           drs.id IS NULL AND dco.data_status = '1'
           AND dco.declare_order_status = '3');

    #计算报关总价
    UPDATE
        declare_report_single drs
        #JOIN supply_bankroll.record_rate rr ON rr.declare_order_id = drs.related_no
        JOIN supply_bankroll.customs_factors cf ON drs.related_no = cf.declare_order_id
                                                   AND cf.hs_code = drs.customs_no
                                                   AND cf.hs_name = drs.category
                                                   AND cf.declare_element = drs.declare_element
                                                   AND cf.brand = drs.brand
                                                   AND cf.`type` = drs.`type`
                                                   AND cf.`rule_unit` = drs.`rule_unit`
                                                   AND cf.`hs_code_id` = drs.`hs_code_id`
                                                   AND cf.`return_tax` = drs.`drawback_rate`
                                                   AND cf.unit_price_CN = drs.`price_tax`
                                                   AND cf.currency = drs.`currency`

    SET total_usd_tax =
    drs.transaction_count * (drs.price_tax / 1.17) * (
      SELECT rate
      FROM
        supply_bankroll.record_rate rr
      WHERE
        rr.declare_order_id = drs.related_no
    ) * cf.ratio,
      total_price_tax = drs.transaction_count * drs.price_tax,
      item_no         = cf.num;

  END//
DELIMITER ;
