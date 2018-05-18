/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-09-28 12:06:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Procedure structure for `sync_purchase_increase_exec_report`
-- ----------------------------
DROP PROCEDURE IF EXISTS `sync_purchase_increase_exec_report`;
DELIMITER ;;
CREATE DEFINER=`aukey_report`@`%` PROCEDURE `sync_purchase_increase_exec_report`()
BEGIN
 
INSERT into purchase_exec_report(purchase_no,supplier,supplier_name,purchase_date,legaler,legaler_name,purchase_currency,purchaser,purchaser_name,purchase_department_id,purchase_department,inventory_warehouseno,inventory_warehouse_name,inventory_status,payment_status,purchase_amount_all,exchange_rate,is_tax,bill_amount,un_bill_amount,bill_status,bill_contract)

SELECT
  t4.purchase_order_id as '采购单号',
	t4.supplier_id AS '供应商id',
  ss.`name`  AS '供应商名称',
	t4.documents_date AS '单据日期',
	t4.legal_person_id AS '法人主体id',
  bc.corporation_name AS '法人主体名称',
	t4.currency AS '采购币别',
	t4.buyer_id AS '采购员',
  cs.`name` AS '采购员名称',
	t4.purchase_group_id AS '采购部门id',
	t4.purchase_group_name AS '采购部门',
  t4.warehouse_id as '入库仓库',
  st.`name` as '入库仓库名称',
	t4.order_status AS '入库状态',
	t4.pay_status AS '付款状态',

IF (
	t4.is_tax = '1',
	SUM(t5.tax_money_total),
	SUM(t5.money)
) AS '总金额',
 t4.exchange_rate AS '汇率',
 t4.is_tax AS '是否含税',
coalesce(iv.已开票总额,0) as '已开票总额',
IF (
	t4.is_tax = '1',
	SUM(t5.tax_money_total),
	SUM(t5.money)
) -coalesce(iv.已开票总额,0) 
AS '未开票总额',
IF (
	coalesce(iv.已开票总额,0)= 0,
	'1',
IF (IF (
	t4.is_tax = '1',
	SUM(t5.tax_money_total),
	SUM(t5.money)
) -coalesce(iv.已开票总额,0)=0,
  '0',
  '2' 
))AS '开票状态',
COUNT(pad.agreement_id) as '合同状态'
 FROM
	(
		SELECT
			t1.*
		FROM
			supply_chain.purchase_order t1  
		WHERE
	   	t1.documents_date> (SELECT  per.purchase_date  from  purchase_exec_report per ORDER BY per.purchase_date desc limit 1) and t1.data_status = '1'
		ORDER BY
			t1.id 
	 
	) t4
LEFT JOIN supply_chain.star_sign t3 ON t4.id = t3.type_id
LEFT JOIN supply_chain.purchase_demand t5 ON (
	t4.purchase_order_id = t5.purchase_order_id
)
INNER JOIN supplier2.base_corporation  bc ON(t4.legal_person_id=bc.corporation_id)
INNER JOIN supplier2.strict_supplier  ss ON(t4.supplier_id=ss.supplier_id)
INNER JOIN  `user`  cs ON(t4.buyer_id =cs.user_id)
INNER JOIN  stock_ms st ON(t4.warehouse_id=st.stock_id)
LEFT JOIN view_purchase_invoice_moeny  iv on(iv.purchase_order_id=t4.purchase_order_id )
left JOIN  supply_chain.purchase_agreement_detail pad on(t4.purchase_order_id=pad.purchase_order_id)
GROUP BY
	t4.documents_date,
	t4.supplier_id,
	t4.legal_person_id,
	t4.currency,
	t4.buyer_id,
	t4.purchase_group_name,
	t4.order_status,
	t4.purchase_order_id,
	t4.exchange_rate,
	t4.is_tax,
  t4.warehouse_id;

END
;;
DELIMITER ;
