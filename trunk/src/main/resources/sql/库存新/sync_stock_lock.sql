/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-01-20 10:53:20
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Procedure structure for sync_stock_lock
-- ----------------------------
DROP PROCEDURE IF EXISTS `sync_stock_lock`;
DELIMITER ;;
CREATE DEFINER=`aukey_report`@`10.1.1.%` PROCEDURE `sync_stock_lock`()
BEGIN
    TRUNCATE TABLE stock_report_record_lock;
    #查询结果插入零时表
    INSERT INTO `stock_report_record_lock`
    (`id`,
     `legaler`,
			department,
     `sku`,
     `category`,
     `inventory`,
     `usage_inventory`,
     `transport_type`,
     `include_tax`,
     `report_date`)
      SELECT
        uuid(),
        法人主体id,
        需求部门id,
        sku_code,
        品类Id,
        仓库id,
        sum(`占用库存`) AS '占用库存',
        运输方式,
        含税,
        更新时间
      FROM
        view_stock_lock
      GROUP BY
        sku_code,
        法人主体id,
        需求部门id,
        仓库id,
        含税,
        运输方式;
    INSERT INTO aukey_report.qingyw_test (content) VALUES ('end INSERT INTO stock_report_record_lock');
    #更新已经有的库存
    UPDATE stock_report_intermediate a
      RIGHT JOIN
      stock_report_record_lock b ON a.`legaler` = b.`legaler`
                                    AND a.`department` = b.`department`
                                    AND a.`sku` = b.`sku`
                                    AND a.`inventory` = b.`inventory`
                                    AND a.`include_tax` = b.`include_tax`
                                    AND (a.`transport_type` = b.`transport_type` OR
                                         (a.`transport_type` IS NULL AND b.`transport_type` IS NULL))
    SET
      a.`usage_inventory` = b.`usage_inventory`,
			a.available_inventory = a.actual_inventory - b.`usage_inventory`
;
	INSERT INTO aukey_report.qingyw_test (content) VALUES ('end UPDATE stock_report_intermediate');
  END
;;
DELIMITER ;
