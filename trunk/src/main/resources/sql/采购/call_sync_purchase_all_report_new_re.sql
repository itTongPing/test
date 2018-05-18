/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.200aukey_report
Source Server Version : 50719
Source Host           : 10.1.1.200:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-02-07 09:41:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Event structure for call_sync_purchase_all_report_new_re
-- ----------------------------
DROP EVENT IF EXISTS `call_sync_purchase_all_report_new_re`;
DELIMITER ;;
CREATE DEFINER=`aukey_report`@`10.1.1.%` EVENT `call_sync_purchase_all_report_new_re` ON SCHEDULE EVERY 1 DAY STARTS '2017-12-12 03:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
TRUNCATE TABLE purchase_report;
CALL sync_purchase_all_report_new();
END
;;
DELIMITER ;
