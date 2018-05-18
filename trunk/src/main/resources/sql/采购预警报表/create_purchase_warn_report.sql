/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-09-27 19:25:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `purchase_warn_report`
-- ----------------------------
DROP TABLE IF EXISTS `purchase_warn_report`;

CREATE TABLE `purchase_warn_report` (
  `purchase_no` varchar(30) NOT NULL COMMENT '采购单号',
  `purchase_demand` varchar(30) NOT NULL COMMENT '需求单号',
  `sku_code` varchar(50) NOT NULL COMMENT 'sku',
  `sku_name` varchar(300) DEFAULT NULL COMMENT 'sku名称',
  `legaler_id` int(11) DEFAULT NULL COMMENT '法人主体id',
  `legaler_name` varchar(100) DEFAULT NULL COMMENT '法人名称',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供应商id',
  `supplier_name` varchar(250) DEFAULT NULL COMMENT '供应商名',
  `purchase_date` datetime DEFAULT NULL COMMENT '采购单日期',
  `purchase_count` int(11) DEFAULT NULL COMMENT '订单数量',
  `currency` varchar(10) DEFAULT NULL COMMENT '币别',
  `price_tax` double(16,2) DEFAULT NULL COMMENT '含税单价',
  `price_without_tax` double(16,2) DEFAULT NULL COMMENT '未税单价',
  `purchase_sum` double(16,2) DEFAULT NULL COMMENT '订单金额',
  `buyer_id` int(10) DEFAULT NULL COMMENT '采购员ID',
  `buyer_name` varchar(100) DEFAULT NULL COMMENT '采购员名',
  `purchase_group_name` varchar(200) NOT NULL DEFAULT 'N/A' COMMENT '采购组名称',
  `purchase_group_id` int(10) DEFAULT NULL COMMENT '采购组id',
  `department_id` int(12) DEFAULT NULL COMMENT '业务部门ID',
  `stock_count` int(11) DEFAULT NULL COMMENT '入库数量',
  `return_count` int(11) DEFAULT NULL COMMENT '退货数量',
  `lack_count` int(11) DEFAULT NULL COMMENT '欠货数量',
  `out_date` int(11) DEFAULT NULL COMMENT '超期天数',
  `out_date_count` int(11) DEFAULT NULL COMMENT '超期数量',
  `before_stock_date` datetime DEFAULT NULL COMMENT '预交货日期',
  `last_update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `stop_return_count` int(11) DEFAULT NULL COMMENT '滞销退货(良品退货)',
  PRIMARY KEY (`purchase_no`,`sku_code`,`purchase_demand`),
  KEY `IX_last_update_date` (`last_update_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;