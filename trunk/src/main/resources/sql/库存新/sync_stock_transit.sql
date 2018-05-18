/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-01-20 10:48:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Procedure structure for sync_stock_transit
-- ----------------------------
DROP PROCEDURE IF EXISTS `sync_stock_transit`;
DELIMITER ;;
CREATE DEFINER=`aukey_report`@`10.1.1.%` PROCEDURE `sync_stock_transit`()
BEGIN
    
 #更新已经有的库存
    UPDATE stock_report_intermediate a
      LEFT JOIN
      (
				SELECT
					法人主体id AS legaler,
					需求部门id AS department,
					sku_code AS sku,
					仓库id AS inventory,
					sum(`出库在途库存`) AS way_inventory,
					运输方式 AS transport_type,
					含税 AS include_tax
				FROM
					view_stock_transit
				GROUP BY
					sku_code,
					法人主体id,
					需求部门id,
					仓库id,
					含税,
					运输方式

			) b ON a.`legaler` = b.`legaler`
                                       AND a.`department` = b.`department`
                                       AND a.`sku` = b.`sku`
                                       AND a.`inventory` = b.`inventory`
                                       AND a.`include_tax` = b.`include_tax`
                                       AND (a.`transport_type` = b.`transport_type` OR
                                            (a.`transport_type` IS NULL AND b.`transport_type` IS NULL))
    SET
      a.`way_inventory` = b.`way_inventory`
;
  END
;;
DELIMITER ;
