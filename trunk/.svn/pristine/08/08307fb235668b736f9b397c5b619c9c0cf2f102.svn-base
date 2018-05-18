/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-01-22 17:35:28
*/
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Event structure for call_sync_stock_all_report
-- ----------------------------
DROP EVENT
IF EXISTS `call_sync_stock_all_report`;
DELIMITER ;;


CREATE DEFINER = `aukey_report`@`%` EVENT `call_sync_stock_all_report` ON SCHEDULE EVERY 20 MINUTE STARTS '2017-10-24 10:10:00' ON COMPLETION NOT PRESERVE ENABLE DO

BEGIN
	INSERT INTO aukey_report.qingyw_test (content) VALUES ('start sync_stock_all_report');
	CALL sync_stock_all_report ();
	INSERT INTO aukey_report.qingyw_test (content) VALUES ('start sync_stock_transit');
	CALL sync_stock_transit ();
	INSERT INTO aukey_report.qingyw_test (content) VALUES ('start sync_stock_lock');
	CALL sync_stock_lock ();
	INSERT INTO aukey_report.qingyw_test (content) VALUES ('start sync_stock_sum_v2');
	CALL sync_stock_sum_v2();
	INSERT INTO aukey_report.qingyw_test (content) VALUES ('end sync_stock_sum_v2');
END;;
DELIMITER ;
