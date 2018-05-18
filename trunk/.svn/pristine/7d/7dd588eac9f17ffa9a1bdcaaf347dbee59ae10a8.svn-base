/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-09-28 12:03:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `purchase_exec_report`
-- ----------------------------
DROP TABLE IF EXISTS `purchase_exec_report`;
CREATE TABLE `purchase_exec_report` (
`purchase_no`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '采购单号' ,
`supplier`  int(11) NULL DEFAULT NULL COMMENT '供应商' ,
`supplier_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商名称' ,
`legaler`  int(11) NULL DEFAULT NULL COMMENT '法人主体' ,
`legaler_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人主体名称' ,
`purchase_date`  datetime NULL DEFAULT NULL COMMENT '采购日期' ,
`update_date`  datetime NULL DEFAULT NULL ,
`purchase_amount_all`  decimal(10,2) NULL DEFAULT NULL COMMENT '采购金额' ,
`purchase_currency`  varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '采购币别' ,
`purchaser`  int(11) NULL DEFAULT NULL COMMENT '采购员' ,
`purchaser_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`purchase_department`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '采购部门' ,
`purchase_department_id`  int(11) NULL DEFAULT NULL COMMENT '采购部门id' ,
`inventory_warehouseno`  int(11) NULL DEFAULT NULL COMMENT '入库仓库' ,
`inventory_warehouse_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库仓库名称' ,
`inventory_amount`  decimal(10,2) NULL DEFAULT NULL COMMENT '入库金额' ,
`un_inventory_amount`  decimal(10,2) NULL DEFAULT NULL COMMENT '未入库金额' ,
`inventory_status`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入库状态 0:未处理,1:审核中,2:未入库,3:部分入库,4:全部入库' ,
`payment_all`  decimal(10,2) NULL DEFAULT NULL COMMENT '已付合计' ,
`un_payment`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '未付金额' ,
`payment_status`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付状态 0未请款,1未付,2部分付,3已全付' ,
`is_tax`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否含税' ,
`exchange_rate`  double(5,2) NULL DEFAULT NULL COMMENT '汇率' ,
`bill_status`  varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '开票状态 开票状态 0全部开票 1未开票  2部分开票' ,
`un_bill_amount`  decimal(10,2) NULL DEFAULT NULL COMMENT '未开票金额' ,
`bill_amount`  decimal(10,2) NULL DEFAULT 0.00 COMMENT '开票金额' ,
`bill_contract`  int(2) NULL DEFAULT 0 COMMENT '开票合同 0 未签署，1已签署' ,
PRIMARY KEY (`purchase_no`),
INDEX `pk_purchase_date` (`purchase_date`) USING BTREE ,
INDEX `pk_update_date` (`update_date`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;