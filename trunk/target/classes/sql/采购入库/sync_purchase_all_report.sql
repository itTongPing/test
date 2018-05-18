/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-10-18 17:10:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Procedure structure for `sync_purchase_all_report`
-- ----------------------------
DROP PROCEDURE IF EXISTS `sync_purchase_all_report`;
DELIMITER ;;
CREATE DEFINER=`aukey_report`@`%` PROCEDURE `sync_purchase_all_report`()
BEGIN
  -- SELECT @stock_date_start:=max(stock_date) FROM purchase_report;
  INSERT INTO purchase_report (
    id,
    legaler,
    legaler_name,
    department,
    department_name,
    category,
    category_name,
    sku,
    sku_name,
    stock,
    stock_name,
    supplier,
    supplier_name,
    purchase_no,
    purchase_date,
    purchase_count,
    purchase_sum,
    price_wihtout_tax,
    price_tax,
    purchase_money_type,
    buyer,
    buyer_name,
    stock_number,
    stock_date,
    stock_count,
    stock_sum,
    bill_name,
    bill_unit,
    brand,
    VERSION,
    include_tax,
    no_bill_count,
    pay_type,
    pay_type_name,
    dept_id_xq,
    dept_name_xq
  ) 
  SELECT 
    UUID(),
    法人主体id,
    b.corporation_name,
    采购部门id,
    e.`name`,
    品类id,
    h.`name`,
    sku_code,
    d.`name`,
    入库仓库id,
    f.`name`,
    供应商id,
    c.name,
    采购单号,
    采购时间,
    采购数量,
    采购金额,
    不含税单价,
    含税单价,
    币种,
    采购员id,
    g.name,
    入库单号,
    入库日期,
    入库数量,
    入库金额,
    开票品名,
    开票单位,
    品牌,
    型号,
    含税,
    入库数量,
    结算方式id,
    base_payment_method.method,
    需求部门id,
    e2.name
  FROM
    view_purchase_storage a 
    LEFT JOIN supplier2.base_corporation b 
      ON a.法人主体id = b.corporation_id 
    LEFT JOIN supplier2.strict_supplier c 
      ON a.供应商id = c.supplier_id 
    LEFT JOIN sku d 
      ON a.sku_code = d.`code` 
    LEFT JOIN org_group e 
      ON a.采购部门id = e.id 
    LEFT JOIN stock_ms f 
      ON a.入库仓库id = f.stock_id 
    LEFT JOIN USER g 
      ON a.采购员id = g.user_id 
    LEFT JOIN category h 
      ON a.品类id = h.cate_id 
    LEFT JOIN supplier2.base_payment_method 
      ON a.结算方式id = base_payment_method.payment_method_id 
     LEFT JOIN org_group e2 
      ON a.需求部门id = e2.id 
  WHERE `入库单号` IN 
    (SELECT 
      st.storage_number 
    FROM
      purchase_report pr 
      RIGHT JOIN supply_sign.`storage` st 
        ON pr.stock_number = st.storage_number 
    WHERE pr.stock_number IS NULL) ;

INSERT INTO purchase_report (
    id,
    legaler,
    legaler_name,
    department,
    department_name,
    category,
    category_name,
    sku,
    sku_name,
    stock,
    stock_name,
    supplier,
    supplier_name,
    purchase_no,
    purchase_date,
    purchase_count,
    purchase_sum,
    price_wihtout_tax,
    price_tax,
    purchase_money_type,
    buyer,
    buyer_name,
    stock_number,
    stock_date,
    stock_count,
    stock_sum,
    bill_name,
    bill_unit,
    brand,
    VERSION,
    include_tax,
    no_bill_count,
    pay_type,
    pay_type_name,
    dept_id_xq,
    dept_name_xq
  ) 
  SELECT 
    UUID(),
    法人主体id,
    b.corporation_name,
    采购部门id,
    e.`name`,
    品类id,
    h.`name`,
    sku_code,
    d.`name`,
    退货仓库,
    f.`name`,
    供应商id,
    c.name,
    采购单号,
    采购时间,
    采购数量,
    采购金额,
    不含税单价,
    含税单价,
    币种,
    采购员id,
    g.name,
    退货单号,
    退货日期,
    退货数量,
    退货金额,
    开票品名,
    开票单位,
    品牌,
    型号,
    含税,
    退货数量,
    结算方式id,
    base_payment_method.method,
    需求部门id,
    e2.name
  FROM
    view_purchase_return a 
    LEFT JOIN supplier2.base_corporation b 
      ON a.法人主体id = b.corporation_id 
    LEFT JOIN supplier2.strict_supplier c 
      ON a.供应商id = c.supplier_id 
    LEFT JOIN sku d 
      ON a.sku_code = d.`code` 
    LEFT JOIN org_group e 
      ON a.采购部门id = e.id 
    LEFT JOIN stock_ms f 
      ON a.退货仓库 = f.stock_id 
    LEFT JOIN USER g 
      ON a.采购员id = g.user_id 
    LEFT JOIN category h 
      ON a.品类id = h.cate_id 
    LEFT JOIN supplier2.base_payment_method 
      ON a.结算方式id = base_payment_method.payment_method_id 
     LEFT JOIN org_group e2 
      ON a.需求部门id = e2.id 
  WHERE `退货单号` IN 
    (SELECT 
      st.purchase_return_id 
    FROM
      purchase_report pr 
      RIGHT JOIN supply_chain.purchase_return_order st 
        ON pr.stock_number = st.purchase_return_id 
    WHERE pr.stock_number IS NULL) ;


END
;;
DELIMITER ;
