/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-01-25 17:30:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for center_stock_report
-- ----------------------------
DROP TABLE IF EXISTS center_stock_report;
CREATE TABLE `center_stock_report` (
  `upc` varchar(20) DEFAULT 'N/A' COMMENT 'UPC编码',
  `sku` varchar(50) NOT NULL DEFAULT 'N/A' COMMENT 'sku编码',
  `sku_name` varchar(300) DEFAULT NULL COMMENT 'SKU名称',
  `dept_id` int(20) DEFAULT NULL COMMENT '部门ID',
  `fba_transfer` int(11) DEFAULT '0' COMMENT 'FBA退货在途',
  `fba_stock_count` int(11) DEFAULT '0' COMMENT 'FBA库存',
  `oversea_stock_transfer` int(11) DEFAULT '0' COMMENT '海外仓转运在途',
  `oversea_stock_count` int(11) DEFAULT '0' COMMENT '海外仓库存',
  `first_air_transfer` int(11) DEFAULT '0' COMMENT '头程空运在途',
  `first_ship_transfer` int(11) DEFAULT '0' COMMENT '头程海运在途',
  `first_trains_transfer` int(11) DEFAULT '0' COMMENT '头程铁路在途',
  `transfer_warehouse_count` int(11) DEFAULT '0' COMMENT '国内中转仓库存',
  `purchase_transfer` int(11) DEFAULT '0' COMMENT '采购订单在途',
  `purchase_cycle` double(16,2) DEFAULT NULL COMMENT '采购周期',
  PRIMARY KEY (`sku`),
  KEY `index_upc` (`upc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='中央库存查询表';

#FBA在途;
DROP TABLE IF EXISTS center_fba_in_transit;
CREATE TABLE center_fba_in_transit
(
	id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	fba_return_plan_no VARCHAR(50) DEFAULT NULL COMMENT '退货计划单号',
	warehouse_id int(12) DEFAULT NULL COMMENT '退货仓库',
	sku varchar(50) DEFAULT NULL COMMENT 'sku_code',
	quantity int(12) DEFAULT 0 COMMENT '在途数量',
	deliver_date datetime DEFAULT NULL COMMENT '退件日期',
	PRIMARY KEY (id),
	KEY `index_sku` (`sku`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='FBA退货在途';

#FBA库存
DROP TABLE IF EXISTS center_fba_stock_count;
CREATE TABLE center_fba_stock_count
(
	id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	account_id int(11) DEFAULT NULL COMMENT '账号ID',
	account_name varchar(200) DEFAULT NULL COMMENT '账号',
	site_group_id int(11) NOT NULL DEFAULT '0' COMMENT '站点ID',
	area varchar(50) NOT NULL DEFAULT 'N/A' COMMENT '站点区域',
	sku varchar(50) DEFAULT NULL COMMENT 'sku_code',
	amazon_sku varchar(200) NOT NULL DEFAULT 'N/A' COMMENT '亚马逊SKU编码',
	fnsku varchar(200) NOT NULL DEFAULT 'N/A' COMMENT '货物发亚马逊时，亚马逊生成的SKU编码',
	product_name varchar(1500) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'N/A' COMMENT '产品名称',
	your_price decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品出价',
	sale_quantity int(11) NOT NULL DEFAULT 0 COMMENT '可售数量',
	no_sale_quantity int(11) NOT NULL DEFAULT 0 COMMENT '不可售数量',
	reserved_quantity int(11) NOT NULL DEFAULT '0' COMMENT '预留数量',
	quantity int(12) DEFAULT 0 COMMENT '合计库存',
	PRIMARY KEY (id),
	KEY index_sku (sku),
	KEY index_account_site_group_id (account_id,site_group_id) USING BTREE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='FBA库存';

#海外转运仓在途
DROP TABLE IF EXISTS center_oversea_transport_in_transit;
CREATE TABLE center_oversea_transport_in_transit
(
	id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	transfer_no varchar(30) DEFAULT NULL COMMENT '调拨单号',
	out_warehouse int(12) DEFAULT NULL COMMENT '出库仓',
	in_warehouse int(12) DEFAULT NULL COMMENT '入库仓',
	sku varchar(50) DEFAULT NULL COMMENT 'sku_code',
	quantity int(12) DEFAULT 0 COMMENT '在途数量',
	deliver_date datetime DEFAULT NULL COMMENT '发货日期',
	PRIMARY KEY (id),
	KEY `index_sku` (`sku`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='海外转运仓库存';


#头程海运(空运,铁运)
DROP TABLE IF EXISTS center_head_in_transit;
CREATE TABLE center_head_in_transit
(
	id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	transfer_no varchar(30) DEFAULT NULL COMMENT '调拨单号',
	out_warehouse int(12) DEFAULT NULL COMMENT '出库仓',
	in_warehouse int(12) DEFAULT NULL COMMENT '入库仓',
	sku varchar(50) DEFAULT NULL COMMENT 'sku_code',
	transport_type enum('0','1','2','3') DEFAULT '2' COMMENT '运输方式:0-空运,1-海运,2-无,3-铁运',
	quantity int(12) DEFAULT 0 COMMENT '在途数量',
	deliver_date datetime DEFAULT NULL COMMENT '发货日期',
	PRIMARY KEY (id),
	KEY `index_sku` (`sku`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='头程在途';

#国内中转仓库存
DROP TABLE IF EXISTS center_domestic_transfer_warehouse;
CREATE TABLE center_domestic_transfer_warehouse
(
	id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	warehouse_id int(12) DEFAULT NULL COMMENT '退货仓库',
	sku varchar(50) DEFAULT NULL COMMENT 'sku_code',
	no_lock_quantity int(12) DEFAULT 0 COMMENT '可用数量',
	lock_quantity int(12) DEFAULT 0 COMMENT '占用数量',
	quantity int(12) DEFAULT 0 COMMENT '合计库存',
	PRIMARY KEY (id),
	KEY `index_sku` (`sku`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='国内中转仓库存';

#采购订单在途
DROP TABLE IF EXISTS center_purchase_in_transit;
CREATE TABLE `center_purchase_in_transit` (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `purchase_order_id` varchar(30) DEFAULT NULL COMMENT '采购单号',
  `purchase_demand_id` varchar(30) DEFAULT NULL COMMENT '需求单号',
  `warehouse_id` int(12) DEFAULT NULL COMMENT '目的仓',
  `sku` varchar(50) DEFAULT NULL COMMENT 'sku_code',
  `quantity` int(12) DEFAULT '0' COMMENT '采购数量',
  `in_way_quantity` int(12) DEFAULT '0' COMMENT '在途数量',
  `deliver_date` datetime DEFAULT NULL COMMENT '发货日期',
  PRIMARY KEY (id),
  KEY `index_sku` (`sku`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='采购订单在途';

#海外仓库存
DROP TABLE IF EXISTS center_oversea_warehouse;
CREATE TABLE center_oversea_warehouse
(
	id bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	warehouse_id int(12) DEFAULT NULL COMMENT '仓库',
	sku varchar(50) DEFAULT NULL COMMENT 'sku_code',
	no_lock_quantity int(12) DEFAULT 0 COMMENT '可用数量',
	lock_quantity int(12) DEFAULT 0 COMMENT '占用数量',
	quantity int(12) DEFAULT 0 COMMENT '合计库存',
	PRIMARY KEY (id),
	KEY `index_sku` (`sku`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='海外仓库存';

-- ----------------------------
-- Records of center_stock_report
-- ----------------------------
BEGIN;
COMMIT;
