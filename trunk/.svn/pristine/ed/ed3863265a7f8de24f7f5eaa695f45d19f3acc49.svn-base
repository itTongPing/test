DROP PROCEDURE
IF EXISTS `sync_transfer_report`;
DELIMITER //


CREATE PROCEDURE sync_transfer_report ()
BEGIN
	TRUNCATE TABLE transfer_report ;
SET SESSION group_concat_max_len = 20000 ; 
INSERT INTO `transfer_report` (
	`id`,
	`transfer_no`,
	`transfer_date`,
	`legal_id`,
	`legal_name`,
	`expected_outtime`,
	`outtime`,
	`out_warehouse_id`,
	`out_warehouse_name`,
	`pass_warehouse_id`,
	`pass_warehouse_name`,
	`target_warehouse_id`,
	`target_warehouse_name`,
	`sku`,
	`sku_name`,
	`box_no`,
	`box_count`,
	`transport_type`,
	`actual_weight`,
	`box_grow`,
	`box_broad`,
	`box_height`,
	`box_volume`,
	`transfer_status`,
	`is_tax`,
	`site_id`,
	`site_name`,
	`account_id`,
	`account_name`,
	`shipment_id`,
	`fnsku`,
	`sellersku`,
	`money`,
	`tax_rate`,
	`return_tax`,
	`declare_order_id`,
	`declare_order_date`,
	`customs_number`,
	`unit_price`,
	`declare_money`,
	`currency`,
	num,
  hs_name,
  quantity_received
) SELECT
	UUID(),
	`transfer_no`,
	`transfer_date`,
	`legal_id`,
	a6.corporation_name `legal_name`,
	`expected_outtime`,
	`actual_outtime`,
	`out_warehouse_id`,
	a1. NAME AS `out_warehouse_name`,
	`pass_warehouse_id`,
	a2. NAME AS `pass_warehouse_name`,
	`target_warehouse_id`,
	a3. NAME AS `target_warehouse_name`,
	T1.`sku`,
	e.`name` AS `sku_name`,
	`box_no`,
	`box_count`,
	`transport_type`,
	`actual_weight`,
	`box_grow`,
	`box_broad`,
	`box_height`,
	`box_volume`,
	`transfer_status`,
	`is_tax`,
	T1.`site_id`,
	a4.site_name AS `site_name`,
	T1.`account_id`,
	a5.account_name AS `account_name`,
	`shipment_id`,
	`fnsku`,
	`sellersku`,
	`money`,
	`tax_rate`,
	phc.return_tax AS `return_tax`,
	`declare_order_id`,
	`declare_order_date`,
	`customs_number`,
	T1.`unit_price`,
	CASE
WHEN declare_order_id IS NULL THEN
	NULL
ELSE
	T1.box_count*T2.price 
END AS 'declare_money',
 T2.`currency`,
 T2.num AS num,
 phc.hs_name,
 IFNULL(T1.arrival_qty,0) AS 'arrival_qty'
FROM
	(
		SELECT
   a.transfer_no AS 'transfer_no',
   a.create_time AS 'transfer_date',
   a.transfer_status AS 'transfer_status',
   a.is_tax AS 'is_tax',
      sum(IF(a.is_tax='1',d.tax_unit_price,d.unit_price)*b.box_count)/sum(b.box_count) AS unit_price,
   a.expected_outtime AS 'expected_outtime',
   a.actual_outtime AS 'actual_outtime',
   a.out_warehouse_id AS 'out_warehouse_id',
   a.warehouse_id AS 'pass_warehouse_id',
   b.warehouse_id AS 'target_warehouse_id',
   b.sku AS 'sku',
   b.box_no AS 'box_no',
   sum(b.box_count) AS 'box_count',
   sum(tpa.arrival_qty) AS 'arrival_qty',
   sum(b.actual_weight) AS 'actual_weight',
   b.box_grow AS 'box_grow',
   b.box_broad AS 'box_broad',
   b.box_height AS 'box_height',
   b.box_grow * b.box_broad * b.box_height AS 'box_volume',
   c.transport_type AS 'transport_type',
   -- FBA
   c.site_id AS 'site_id',
   c.account_id AS 'account_id',
   c.shipment_id AS 'shipment_id',
   c.fba_number AS 'fnsku',
   c.seller_sku AS 'sellersku',
   -- 报关税务
   -- 从采购单
   sum(IF(a.is_tax='1',d.tax_unit_price,d.unit_price)*b.box_count) AS 'money',
   -- d.tax_rate AS 'tax_rate',
   CASE a.is_tax
  WHEN '0' THEN
   NULL
  WHEN '1' THEN
   d.tax_rate
  END AS 'tax_rate',
  i.legal_person_id AS 'legal_id'
 FROM
  supply_delivery.transfer_slip a -- 调拔主表
 JOIN supply_delivery.transfer_detail b ON a.transfer_id = b.transfer_id
 JOIN supply_delivery.delivery_plan c ON b.delivery_id = c.delivery_id -- 发货计划主表
 JOIN supply_chain.purchase_demand d ON b.requirement_no = d.purchase_demand_id -- 一个采购表对应多个需求单
 JOIN supply_chain.purchase_order i ON d.purchase_order_id = i.purchase_order_id 
 LEFT JOIN supply_delivery.transfer_part_arrival tpa ON tpa.box_no = b.box_no and tpa.transfer_id =a.transfer_id and tpa.requirement_no=b.requirement_no and tpa.delivery_id=b.delivery_id 
  WHERE  a.data_status='1' and b.data_status='1'
 GROUP BY
  a.transfer_no,
  a.transfer_status,
  a.is_tax,
  a.out_warehouse_id,
  a.warehouse_id,
  b.warehouse_id,
  b.sku,
  b.box_no,
  c.transport_type,
  c.site_id,
  c.account_id,
  c.shipment_id,
  c.fba_number,
  c.seller_sku,
  i.legal_person_id

	) T1
LEFT JOIN (
	SELECT 
		cf.sku_codes sku,
		dco.allot_order_id,
		dco.declare_order_id AS 'declare_order_id',
		dco.declare_order_date AS 'declare_order_date',
		dco.customs_number AS 'customs_number',
		cf.box_nos AS boxNos,
		cf.currency AS 'currency',
		round(cf.unit_price,2) AS price,
    cf.num
	FROM
		supply_bankroll.declare_customs_order dco

	JOIN supply_bankroll.customs_factors cf ON cf.declare_order_id = dco.declare_order_id 
 
	WHERE
		dco.data_status = '1' -- GROUP BY dco.allot_order_id,dco.declare_order_id

GROUP BY
		cf.sku_codes,
		dco.allot_order_id,
		dco.declare_order_id,
		dco.declare_order_date,
		dco.customs_number,
		cf.box_nos,
		cf.currency,
		cf.unit_price,
    cf.num
) T2 ON T1.transfer_no = T2.allot_order_id
AND FIND_IN_SET(t1.sku, T2.sku)
AND FIND_IN_SET(T1.box_no, T2.boxNos)
JOIN product_ms.sku e ON e.`code` = t1.sku -- sku主表
LEFT JOIN product_ms.stock a1 ON a1.stock_id = t1.out_warehouse_id
LEFT JOIN product_ms.stock a2 ON a2.stock_id = t1.pass_warehouse_id
LEFT JOIN product_ms.stock a3 ON a3.stock_id = t1.target_warehouse_id
LEFT JOIN product_ms.amazon_site_config a4 ON a4.site_id = t1.site_id
LEFT JOIN product_ms.account a5 ON a5.account_id = t1.account_id
LEFT JOIN supplier2.base_corporation a6 ON a6.corporation_id = t1.legal_id
LEFT JOIN warehouse_locator.stcodes_sku_relate ssr ON ssr.handle_status = '2'
AND ssr.sku_code = t1.sku
LEFT JOIN warehouse_locator.stcodes st ON ssr.stcodes_id = st.id
LEFT JOIN warehouse_locator.product_hs_code phc ON phc.code_id = st.hs_code_id
;

#------------------------------------------无主库存海外调拨--------------------------------------------------------
INSERT INTO `transfer_report` (
	`id`,
	`transfer_no`,
	`transfer_date`,
	`legal_id`,
	`legal_name`,
	`expected_outtime`,
	`outtime`,
	`out_warehouse_id`,
	`out_warehouse_name`,
	`pass_warehouse_id`,
	`pass_warehouse_name`,
	`target_warehouse_id`,
	`target_warehouse_name`,
	`sku`,
	`sku_name`,
	`box_no`,
	`box_count`,
	`transport_type`,
	`actual_weight`,
	`box_grow`,
	`box_broad`,
	`box_height`,
	`box_volume`,
	`transfer_status`,
	`is_tax`,
	`site_id`,
	`site_name`,
	`account_id`,
	`account_name`,
	`shipment_id`,
	`fnsku`,
	`sellersku`,
	`money`,
	`tax_rate`,
	`return_tax`,
	`declare_order_id`,
	`declare_order_date`,
	`customs_number`,
	`unit_price`,
	`declare_money`,
	`currency`,
	num,
  hs_name,
  quantity_received
)
SELECT
	UUID(),
	`transfer_no`,
	`transfer_date`,
	`legal_id`,
	a6.corporation_name `legal_name`,
	`expected_outtime`,
	`actual_outtime`,
	`out_warehouse_id`,
	a1. NAME AS `out_warehouse_name`,
	`pass_warehouse_id`,
	a2. NAME AS `pass_warehouse_name`,
	`target_warehouse_id`,
	a3. NAME AS `target_warehouse_name`,
	T1.`sku`,
	e.`name` AS `sku_name`,
	`box_no`,
	`box_count`,
	`transport_type`,
	`actual_weight`,
	`box_grow`,
	`box_broad`,
	`box_height`,
	`box_volume`,
	`transfer_status`,
	`is_tax`,
	T1.`site_id`,
	a4.site_name AS `site_name`,
	T1.`account_id`,
	a5.account_name AS `account_name`,
	`shipment_id`,
	`fnsku`,
	`sellersku`,
	`money`,
	`tax_rate`,
	null AS `return_tax`,
	null `declare_order_id`,
	null `declare_order_date`,
	null `customs_number`,
	T1.`unit_price`,
	null 'declare_money',
 null `currency`,
 null AS num,
 null,
 IFNULL(T1.arrival_qty,0) AS 'arrival_qty'
FROM
	(
		SELECT
			a.transfer_no AS 'transfer_no',
			a.create_time AS 'transfer_date',
			a.transfer_status AS 'transfer_status',
			a.is_tax AS 'is_tax',
      null AS unit_price,
			a.expected_outtime AS 'expected_outtime',
			a.actual_outtime AS 'actual_outtime',
			a.out_warehouse_id AS 'out_warehouse_id',
			a.warehouse_id AS 'pass_warehouse_id',
			b.warehouse_id AS 'target_warehouse_id',
			b.sku AS 'sku',
			b.box_no AS 'box_no',
			sum(b.box_count) AS 'box_count',
      sum(tpa.arrival_qty) AS 'arrival_qty',
			sum(b.actual_weight) AS 'actual_weight',
			b.box_grow AS 'box_grow',
			b.box_broad AS 'box_broad',
			b.box_height AS 'box_height',
			b.box_grow * b.box_broad * b.box_height AS 'box_volume',
			c.transport_type AS 'transport_type',
			-- FBA
			c.site_id AS 'site_id',
			c.account_id AS 'account_id',
			c.shipment_id AS 'shipment_id',
			c.fba_number AS 'fnsku',
			c.seller_sku AS 'sellersku',
			-- 报关税务
			-- 从采购单
			null AS 'money',
			-- d.tax_rate AS 'tax_rate',
			null 'tax_rate',
		3 AS 'legal_id'
 
	FROM
		nogoal_delivery.transfer_slip a -- 调拔主表
	JOIN nogoal_delivery.transfer_detail b ON a.transfer_id = b.transfer_id
	JOIN nogoal_delivery.delivery_plan c ON b.delivery_id = c.delivery_id -- 发货计划主表
    LEFT JOIN nogoal_delivery.transfer_part_arrival tpa ON tpa.box_no = b.box_no and tpa.transfer_id =a.transfer_id and tpa.requirement_no=b.requirement_no and tpa.delivery_id=b.delivery_id 

	GROUP BY
		a.transfer_no,
		a.transfer_status,
		a.is_tax,
		a.out_warehouse_id,
		a.warehouse_id,
		b.warehouse_id,
		b.sku,
		b.box_no,
		c.transport_type,
		c.site_id,
		c.account_id,
		c.shipment_id,
		c.fba_number,
		c.seller_sku
	) T1

JOIN product_ms.sku e ON e.`code` = t1.sku -- sku主表
LEFT JOIN product_ms.stock a1 ON a1.stock_id = t1.out_warehouse_id
LEFT JOIN product_ms.stock a2 ON a2.stock_id = t1.pass_warehouse_id
LEFT JOIN product_ms.stock a3 ON a3.stock_id = t1.target_warehouse_id
LEFT JOIN product_ms.amazon_site_config a4 ON a4.site_id = t1.site_id
LEFT JOIN product_ms.account a5 ON a5.account_id = t1.account_id
LEFT JOIN supplier2.base_corporation a6 ON a6.corporation_id = t1.legal_id;

#------------------------------------------无主库存海外调拨(海外调海外)--------------------------------------------------------
INSERT INTO `transfer_report` (
	`id`,
	`transfer_no`,
	`transfer_date`,
	`legal_id`,
	`legal_name`,
	`expected_outtime`,
	`outtime`,
	`out_warehouse_id`,
	`out_warehouse_name`,
	`pass_warehouse_id`,
	`pass_warehouse_name`,
	`target_warehouse_id`,
	`target_warehouse_name`,
	`sku`,
	`sku_name`,
	`box_no`,
	`box_count`,
	`transport_type`,
	`actual_weight`,
	`box_grow`,
	`box_broad`,
	`box_height`,
	`box_volume`,
	`transfer_status`,
	`is_tax`,
	`site_id`,
	`site_name`,
	`account_id`,
	`account_name`,
	`shipment_id`,
	`fnsku`,
	`sellersku`,
	`money`,
	`tax_rate`,
	`return_tax`,
	`declare_order_id`,
	`declare_order_date`,
	`customs_number`,
	`unit_price`,
	`declare_money`,
	`currency`,
	num,
  hs_name,
  quantity_received
)
SELECT
 UUID(),
 `transfer_no`,
 `transfer_date`,
 `legal_id`,
 a6.corporation_name `legal_name`,
 `expected_outtime`,
 `actual_outtime`,
 `out_warehouse_id`,
 a1. NAME AS `out_warehouse_name`,
 `pass_warehouse_id`,
 a2. NAME AS `pass_warehouse_name`,
 `target_warehouse_id`,
 a3. NAME AS `target_warehouse_name`,
 T1.`sku`,
 e.`name` AS `sku_name`,
 `box_no`,
 `box_count`,
 `transport_type`,
 `actual_weight`,
 `box_grow`,
 `box_broad`,
 `box_height`,
 `box_volume`,
 `transfer_status`,
 `is_tax`,
 T1.`site_id`,
 a4.site_name AS `site_name`,
 T1.`account_id`,
 T1.account_name AS `account_name`,
 `shipment_id`,
 `fnsku`,
 `sellersku`,
 `money`,
 `tax_rate`,
 null AS `return_tax`,
 null `declare_order_id`,
 null `declare_order_date`,
 null `customs_number`,
 T1.`unit_price`,
 null 'declare_money',
 null `currency`,
 null AS num,
 null,
 `quantity_received`
FROM  
(
SELECT
   a.transfer_no AS 'transfer_no',
   a.create_date AS 'transfer_date',
   CASE a.data_status 
   when '0' then '0'
   when '1' then '0'
	 when '2' then '0'
   when '3' then '0'
	 when '7' then '0'
	 when '4' then '1'
	 when '5' then '2'
	 when '6' then '3'
	 end
   AS 'transfer_status',
   null AS 'is_tax',
   null AS unit_price,
   a.expectation_date AS 'expected_outtime',
   c.create_date AS 'actual_outtime',
   a.from_warehouse_id AS 'out_warehouse_id',
   a.to_warehouse_id AS 'pass_warehouse_id',
   a.to_warehouse_id AS 'target_warehouse_id',
   b.from_wsku AS 'sku',
   d.box_number AS 'box_no',
   d.box_count AS 'box_count',
   d.box_weight AS 'actual_weight',
   d.length AS 'box_grow',
   d.width AS 'box_broad',
   d.height AS 'box_height',
   d.length * d.width * d.height AS 'box_volume',
   null AS 'transport_type',
   null AS 'site_id',
   null 'account_id',
   e.amazon_account AS 'account_name',
   e.shipmentid AS 'shipment_id',
   e.FNSKU AS 'fnsku',
   null AS 'sellersku',
   null AS 'money',
   -- d.tax_rate AS 'tax_rate',
   null 'tax_rate',
   3 AS 'legal_id',
   d.quantity_received AS 'quantity_received'
 FROM
  supply_sign.center_transfer a -- 调拔主表
 JOIN supply_sign.center_transfer_detail b ON a.transfer_no = b.transfer_no
  JOIN supply_sign.center_idle_stock_record c on a.transfer_no =c.relation_no and c.type='12'
  JOIN supply_sign.center_transfer_detail_box d on b.transfer_detail_id = d.transfer_detail_id
  JOIN supply_sign.center_transfer_detail_ex e on b.transfer_detail_id = e.transfer_detail_id
  GROUP by 
  d.box_number
) T1


JOIN product_ms.sku e ON e.`code` = t1.sku -- sku主表
LEFT JOIN product_ms.stock a1 ON a1.stock_id = t1.out_warehouse_id
LEFT JOIN product_ms.stock a2 ON a2.stock_id = t1.pass_warehouse_id
LEFT JOIN product_ms.stock a3 ON a3.stock_id = t1.target_warehouse_id
LEFT JOIN product_ms.amazon_site_config a4 ON a4.site_id = t1.site_id
LEFT JOIN supplier2.base_corporation a6 ON a6.corporation_id = t1.legal_id;



END//
DELIMITER ;