/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-01-15 15:22:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- View structure for view_stock_age_report_overseas_in
-- ----------------------------
DROP VIEW IF EXISTS view_stock_age_report_overseas_in;
CREATE ALGORITHM=UNDEFINED DEFINER=`aukey_report`@`%` SQL SECURITY DEFINER VIEW view_stock_age_report_overseas_in AS
SELECT
		stock_in.sku,
		stock_in.stock_id,
		stock_kc.stock_count,
		SUM(case when stock_in.age_name = 'age15' then stock_in.quantity else 0 end) AS 'age15',
		SUM(case when stock_in.age_name = 'age15_30' then stock_in.quantity else 0 end) AS 'age15_30',
		SUM(case when stock_in.age_name = 'age30_60' then stock_in.quantity else 0 end) AS 'age30_60',
		SUM(case when stock_in.age_name = 'age60_90' then stock_in.quantity else 0 end) AS 'age60_90',
		SUM(case when stock_in.age_name = 'age90_120' then stock_in.quantity else 0 end) AS 'age90_120',
		SUM(case when stock_in.age_name = 'age120_150' then stock_in.quantity else 0 end) AS 'age120_150',
		SUM(case when stock_in.age_name = 'age150_180' then stock_in.quantity else 0 end) AS 'age150_180',
		SUM(case when stock_in.age_name = 'age180_270' then stock_in.quantity else 0 end) AS 'age180_270',
		SUM(case when stock_in.age_name = 'age270_365' then stock_in.quantity else 0 end) AS 'age270_365',
		SUM(case when stock_in.age_name = 'age365_730' then stock_in.quantity else 0 end) AS 'age365_730',
		SUM(case when stock_in.age_name = 'age731' then stock_in.quantity else 0 end) AS 'age731'
FROM
(
	SELECT
		srd.stock_id,
		srd.sku,
		srd.quantity,
		CASE
					WHEN IFNULL(srd.ori_create_date,srd.create_date) > DATE_ADD(NOW(), INTERVAL - 15 DAY) THEN	'age15'
					WHEN IFNULL(srd.ori_create_date,srd.create_date) > DATE_ADD(NOW(), INTERVAL - 30 DAY) AND IFNULL(srd.ori_create_date,srd.create_date) < DATE_ADD(NOW(), INTERVAL - 15 DAY) THEN 'age15_30'
					WHEN IFNULL(srd.ori_create_date,srd.create_date) > DATE_ADD(NOW(), INTERVAL - 60 DAY) AND IFNULL(srd.ori_create_date,srd.create_date) < DATE_ADD(NOW(), INTERVAL - 30 DAY) THEN 'age30_60'
					WHEN IFNULL(srd.ori_create_date,srd.create_date) > DATE_ADD(NOW(), INTERVAL - 90 DAY) AND IFNULL(srd.ori_create_date,srd.create_date) < DATE_ADD(NOW(), INTERVAL - 60 DAY) THEN 'age60_90'
					WHEN IFNULL(srd.ori_create_date,srd.create_date) > DATE_ADD(NOW(), INTERVAL - 120 DAY) AND IFNULL(srd.ori_create_date,srd.create_date) < DATE_ADD(NOW(), INTERVAL - 90 DAY) THEN 'age90_120'
					WHEN IFNULL(srd.ori_create_date,srd.create_date) > DATE_ADD(NOW(), INTERVAL - 150 DAY)	AND IFNULL(srd.ori_create_date,srd.create_date) < DATE_ADD(NOW(), INTERVAL - 120 DAY) THEN	'age120_150'
					WHEN IFNULL(srd.ori_create_date,srd.create_date) > DATE_ADD(NOW(), INTERVAL - 180 DAY)	AND IFNULL(srd.ori_create_date,srd.create_date) < DATE_ADD(NOW(), INTERVAL - 150 DAY) THEN	'age150_180'
					WHEN IFNULL(srd.ori_create_date,srd.create_date) > DATE_ADD(NOW(), INTERVAL - 270 DAY)	AND IFNULL(srd.ori_create_date,srd.create_date) < DATE_ADD(NOW(), INTERVAL - 180 DAY) THEN	'age180_270'
					WHEN IFNULL(srd.ori_create_date,srd.create_date) > DATE_ADD(NOW(), INTERVAL - 365 DAY)	AND IFNULL(srd.ori_create_date,srd.create_date) < DATE_ADD(NOW(), INTERVAL - 270 DAY) THEN	'age270_365'
					WHEN IFNULL(srd.ori_create_date,srd.create_date) > DATE_ADD(NOW(), INTERVAL - 730 DAY)	AND IFNULL(srd.ori_create_date,srd.create_date) < DATE_ADD(NOW(), INTERVAL - 365 DAY) THEN	'age365_730'
					ELSE 'age731' END AS age_name
	FROM
		supply_sign.center_idle_stock_record srd
	INNER JOIN product_ms.stock st ON st.stock_id = srd.stock_id
	WHERE
		st.stock_type IN ('11', '5') AND srd.type in('13','14','2')
)	stock_in
LEFT JOIN 
(
	SELECT
		warehouse_id,
		sku,
		SUM(quantity) AS stock_count
	FROM
		supply_sign.center_idle_stock
	GROUP BY
		warehouse_id,
		sku
) stock_kc ON stock_in.sku = stock_kc.sku AND stock_kc.warehouse_id = stock_in.stock_id
GROUP BY
		stock_in.sku,
		stock_in.stock_id;