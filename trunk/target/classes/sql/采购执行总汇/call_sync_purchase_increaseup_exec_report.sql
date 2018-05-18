/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-09-28 12:08:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Procedure structure for `sync_purchase_increaseup_exec_report`
-- ----------------------------
DROP PROCEDURE IF EXISTS `sync_purchase_increaseup_exec_report`;
DELIMITER ;;
CREATE DEFINER=`aukey_report`@`%` PROCEDURE `sync_purchase_increaseup_exec_report`()
BEGIN

DECLARE purchase_no,currency VARCHAR (50);

DECLARE legaler_name VARCHAR (200);

DECLARE legaler int;



DECLARE order_status,pay_status VARCHAR (2);
 

DECLARE update_date datetime;

DECLARE b INT DEFAULT 0;


DECLARE cur_1 CURSOR FOR SELECT
	per.purchase_no,t1.update_date,t1.order_status,t1.pay_status,t1.currency,t1.legal_person_id,bc.corporation_name
FROM
	purchase_exec_report per
JOIN supply_chain.purchase_order t1 ON (
	per.purchase_no = t1.purchase_order_id
)
INNER JOIN supplier2.base_corporation  bc ON(t1.legal_person_id=bc.corporation_id)
WHERE
	per.update_date != t1.update_date or per.update_date is NULL;


DECLARE CONTINUE HANDLER FOR NOT FOUND
SET b = 1;

OPEN cur_1;

FETCH cur_1 INTO purchase_no,update_date,order_status,pay_status,currency,legaler,legaler_name;

 while b<>1 do

  SELECT 
 COALESCE(SUM(CASE WHEN rf.`verify_status`='FLOW_END' THEN recei_money ELSE 0 END),0),

 total_money-COALESCE(SUM(CASE WHEN rf.`verify_status`='FLOW_END' THEN recei_money ELSE 0 END),0)

 into @s_paid,@s_unpiad
                      FROM   supply_bankroll.`request_relate_order`
		 rro
		JOIN supply_bankroll.`request_funds_order_relate` rfor ON (
			rro.`relate_id` = rfor.`relate_id`
		)
		JOIN supply_bankroll.`request_funds_order` rf ON (
			rfor.`request_id` = rf.`request_id`
			AND data_status = '1'
			AND func_state = '1'
		)
    where relate_order_id=purchase_no;
SELECT
IF(purd.is_tax='1',SUM(pd.tax_money_total),SUM(pd.money)),purd.is_tax INTO @s_moeny,@s_istax
FROM
supply_chain.purchase_order purd
INNER JOIN supply_chain.purchase_demand pd
on purd.purchase_order_id=pd.purchase_order_id
where purd.purchase_order_id=purchase_no;

 SELECT
  COALESCE(
	sum(IF (
			is_tax = '1',
			tax_unit_price,
			unit_price
		) * nondefective_number
	),0) into @s_incomed
FROM	 
(

SELECT nondefective_number ,pd.unit_price,pd.tax_unit_price,po.is_tax

FROM
(
   select    
    sum(s.nondefective_number) as nondefective_number,s.sku_code,s.purchase_number

    from supply_sign.storage s 
    where (type='1' or type='3') and
			s.purchase_number =purchase_no GROUP BY s.sku_code
)
s
inner JOIN 
	supply_chain.purchase_order po
ON (
	po.purchase_order_id = s.purchase_number
	
  
)
inner JOIN supply_chain.purchase_demand pd ON (
	po.purchase_order_id = pd.purchase_order_id
  AND s.sku_code = pd.sku_code
)
WHERE
	po.purchase_order_id =purchase_no
 GROUP BY s.sku_code
 ) TT;

UPDATE  purchase_exec_report per 
set per.un_payment=@s_unpiad,per.payment_all=@s_paid,per.inventory_amount= @s_incomed,per.purchase_amount_all=@s_moeny,per.is_tax=@s_istax,
per.un_inventory_amount=(per.purchase_amount_all-per.inventory_amount),per.update_date=update_date,per.inventory_status=order_status,per.payment_status=pay_status,per.purchase_currency=currency,
per.legaler=legaler,per.legaler_name=legaler_name
where per.purchase_no=purchase_no;

   FETCH cur_1 INTO purchase_no,update_date,order_status,pay_status,currency,legaler,legaler_name; /*取下一条记录*/
 end while;


CLOSE cur_1;


END
;;
DELIMITER ;
