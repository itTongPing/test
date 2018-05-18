/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-01-20 10:39:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Procedure structure for sync_stock_all_report
-- ----------------------------
DROP PROCEDURE IF EXISTS `sync_stock_all_report`;
DELIMITER ;;
CREATE DEFINER=`aukey_report`@`%` PROCEDURE `sync_stock_all_report`()
BEGIN
	TRUNCATE TABLE stock_report_intermediate;
	INSERT INTO `stock_report_intermediate`
	(`id`,
	 `legaler`,
	 `legaler_name`,
	 `department`,
	 `department_name`,
	 `country`,
	 `country_name`,
	 `sku`,
	 `sku_name`,
	 `category`,
	 `category_name`,
	 `inventory`,
	 `inventory_name`,
	 `count_in`,
	 count_out,
		sum_in,
    	sum_out,
	 `transport_type`,
	 `include_tax`)
	SELECT
		UUID(),
		a.legaler,
		a.legaler_name,
		a.department,
		a.department_name,
		'-1' AS country,
		'-' AS country_name,
		a.sku,
		a.sku_name,
		d.category_id,
		h.`name`,
		a.warehouse,
		a.warehouse_name,
		IFNULL(sum(case when a.business_type in ('普通采购','转SKU入库','无主调拨入库','无主盘盈入库','海外调海外入库','FBA退货入库','不良品入库','期初库存') then a.quantity end),0) count_in,
		IFNULL(sum(case when a.business_type in ('调拨出库','采购退货','其他出库','转SKU出库','需求单调拨出库','需求单盘亏出库','无主调拨出库','无主盘亏出库','无主海外调拨出库','海外调海外出库','不良品出库') then a.quantity end),0) count_out,
		IFNULL(sum(case when a.business_type in ('普通采购','转SKU入库','无主调拨入库','无主盘盈入库','海外调海外入库','FBA退货入库','不良品入库','期初库存') then a.cost end),0) sum_in,
		IFNULL(sum(case when a.business_type in ('调拨出库','采购退货','其他出库','转SKU出库','需求单调拨出库','需求单盘亏出库','无主调拨出库','无主盘亏出库','无主海外调拨出库','海外调海外出库','不良品出库') then a.cost end),0) sum_out,
		a.transport_type,
		a.is_tax
	from aukey_report.sku_report a
	LEFT JOIN product_ms.sku d ON a.sku = d.`code`
	LEFT JOIN product_ms.category h ON d.category_id = h.cate_id
GROUP BY a.sku,a.is_tax,a.legaler,a.warehouse,a.transport_type,a.department;		
END
;;
DELIMITER ;
