/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-09-27 19:25:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Event structure for `call_sync_purchase_warn_report`
-- ----------------------------
DROP EVENT IF EXISTS `call_sync_purchase_warn_report`;
DELIMITER ;;
CREATE DEFINER=`aukey_report`@`%` EVENT `call_sync_purchase_warn_report` ON SCHEDULE EVERY 10 MINUTE STARTS '2017-09-14 15:46:17' ON COMPLETION NOT PRESERVE ENABLE DO call sync_purchase_warn_report(DATE_ADD(now(),INTERVAL -11 MINUTE ),now())
;;
DELIMITER ;
