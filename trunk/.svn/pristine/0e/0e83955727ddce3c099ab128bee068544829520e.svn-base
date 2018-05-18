/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-01-25 17:09:46
*/
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Event structure for call_sync_center_stock_report
-- ----------------------------
DROP EVENT
IF EXISTS `call_sync_center_stock_report`;
DELIMITER ;;
CREATE DEFINER = `aukey_report`@`%` EVENT `call_sync_center_stock_report` 
ON SCHEDULE EVERY 1 DAY STARTS '2018-01-25 02:00:00' 
ON COMPLETION NOT PRESERVE ENABLE DO
BEGIN
	CALL sync_center_stock_report_detail () ;
	CALL sync_center_stock_report_detail_info();
	CALL sync_center_stock_report () ;
END;;
DELIMITER ;
