/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-10-18 17:22:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- View structure for `view_purchase_storage`
-- ----------------------------
DROP VIEW IF EXISTS `view_purchase_storage`; CREATE ALGORITHM=UNDEFINED DEFINER=`aukey_report`@`%` SQL SECURITY DEFINER VIEW `view_purchase_storage` ASSELECT `purchase`.`legal_person_id` AS `法人主体id`,
        `purchase`.`supplier_id` AS `供应商id`,
        `purchase`.`purchase_order_id` AS `采购单号`,
        `purchase`.`documents_date` AS `采购时间`,
        `supply_sign`.`storage`.`sku_code` AS `sku_code`,
        `aukey_report`.`sku`.`category_id` AS `品类id`,
        `purchase`.`quantity` AS `采购数量`,
        `purchase`.`tax_unit_price` AS `含税单价`,
        `purchase`.`currency` AS `币种`,
        `purchase`.`no_tax_price` AS `不含税单价`,
        `purchase`.`is_tax` AS `含税`,
        `purchase`.`money_total` AS `采购金额`,
        `purchase`.`buyer_id` AS `采购员id`,
        `purchase`.`purchase_group_id` AS `采购部门id`,
        `purchase`.`dept_id` AS `需求部门id`,
        `supply_sign`.`storage`.`storage_number` AS `入库单号`,
        `supply_sign`.`storage`.`create_date` AS `入库日期`,
        `supply_sign`.`storage`.`warehouse_id` AS `入库仓库id`,
        `supply_sign`.`storage`.`nondefective_number` AS `入库数量`,
        (case `purchase`.`is_tax`
    WHEN '1' THEN
    (`supply_sign`.`storage`.`nondefective_number` * `purchase`.`tax_unit_price`)
    WHEN '0' THEN
    (`supply_sign`.`storage`.`nondefective_number` * `purchase`.`no_tax_price`) end) AS `入库金额`,`warehouse_locator`.`product_hs_code`.`hs_name` AS `开票品名`,`warehouse_locator`.`stcodes`.`rule_unit` AS `开票单位`,`warehouse_locator`.`stcodes`.`brand` AS `品牌`,`warehouse_locator`.`stcodes`.`type` AS `型号`,`purchase`.`pay_way` AS `结算方式id`
FROM (((((`supply_sign`.`storage`
JOIN 
    (SELECT `supply_chain`.`purchase_order`.`legal_person_id` AS `legal_person_id`,
        `supply_chain`.`purchase_order`.`supplier_id` AS `supplier_id`,
        `supply_chain`.`purchase_order`.`purchase_order_id` AS `purchase_order_id`,
        `supply_chain`.`purchase_order`.`documents_date` AS `documents_date`,
        `supply_chain`.`purchase_order`.`buyer_id` AS `buyer_id`,
        `supply_chain`.`purchase_order`.`purchase_group_id` AS `purchase_group_id`,
        `supply_chain`.`purchase_demand`.`sku_code` AS `sku_code`,
        `supply_chain`.`purchase_demand`.`tax_rate` AS `tax_rate`,
        `supply_chain`.`requirement`.`dept_id` AS `dept_id`,
        if((`supply_chain`.`purchase_order`.`is_tax` = '1'),(`supply_chain`.`purchase_demand`.`tax_unit_price` / (1 + `supply_chain`.`purchase_demand`.`tax_rate`)),`supply_chain`.`purchase_demand`.`unit_price`) AS `no_tax_price`,if((`supply_chain`.`purchase_order`.`is_tax` = '1'),`supply_chain`.`purchase_demand`.`tax_unit_price`,NULL) AS `tax_unit_price`,`supply_chain`.`purchase_order`.`currency` AS `currency`,`supply_chain`.`purchase_order`.`is_tax` AS `is_tax`,(case `supply_chain`.`purchase_order`.`is_tax`
        WHEN '1' THEN
        sum(`supply_chain`.`purchase_demand`.`tax_money_total`)
        WHEN '0' THEN
        sum(`supply_chain`.`purchase_demand`.`money`) end) AS `money_total`,sum(`supply_chain`.`purchase_demand`.`quantity`) AS `quantity`,`supply_chain`.`purchase_order`.`pay_way` AS `pay_way`
    FROM ((`supply_chain`.`purchase_demand`
    JOIN `supply_chain`.`purchase_order` on((`supply_chain`.`purchase_demand`.`purchase_order_id` = `supply_chain`.`purchase_order`.`purchase_order_id`)))
    LEFT JOIN `supply_chain`.`requirement` on((`supply_chain`.`requirement`.`requirement_no` = `supply_chain`.`purchase_demand`.`purchase_demand_id`)))
    GROUP BY  `supply_chain`.`purchase_demand`.`purchase_order_id`,`supply_chain`.`purchase_demand`.`sku_code`) `purchase` on(((`supply_sign`.`storage`.`purchase_number` = `purchase`.`purchase_order_id`)
            AND (`supply_sign`.`storage`.`sku_code` = `purchase`.`sku_code`))))
    LEFT JOIN `warehouse_locator`.`stcodes_sku_relate` on((`warehouse_locator`.`stcodes_sku_relate`.`sku_code` = `supply_sign`.`storage`.`sku_code`)))
    LEFT JOIN `warehouse_locator`.`stcodes` on((`warehouse_locator`.`stcodes_sku_relate`.`stcodes_id` = `warehouse_locator`.`stcodes`.`id`)))
    LEFT JOIN `warehouse_locator`.`product_hs_code` on((`warehouse_locator`.`product_hs_code`.`code_id` = `warehouse_locator`.`stcodes`.`hs_code_id`)))
    LEFT JOIN `aukey_report`.`sku` on((`aukey_report`.`sku`.`code` = `supply_sign`.`storage`.`sku_code`))); 