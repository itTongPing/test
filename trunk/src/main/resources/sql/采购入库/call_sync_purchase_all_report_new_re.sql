/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-10-18 17:07:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Event structure for `call_sync_purchase_all_report_new_re`
-- ----------------------------
DROP EVENT IF EXISTS `call_sync_purchase_all_report_new_re`;
DELIMITER ;;
CREATE DEFINER=`aukey_report`@`%` EVENT `call_sync_purchase_all_report_new_re` ON SCHEDULE EVERY 1 DAY STARTS '2017-10-12 22:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
TRUNCATE TABLE purchase_report;
CALL sync_purchase_all_report_new();
END
;;
DELIMITER ;
