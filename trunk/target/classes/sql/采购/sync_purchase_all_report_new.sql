/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.200
Source Server Version : 50719
Source Host           : 10.1.1.200:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-01-15 11:54:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Procedure structure for sync_purchase_all_report_new
-- ----------------------------
DROP PROCEDURE IF EXISTS `sync_purchase_all_report_new`;
DELIMITER ;;
CREATE DEFINER=`aukey_report`@`10.1.1.%` PROCEDURE `sync_purchase_all_report_new`()
BEGIN
	DECLARE done INT DEFAULT 0;  -- 声明一个标志done， 用来判断游标是否遍历完成
	DECLARE purchase_no_new VARCHAR(50) DEFAULT NULL; -- 采购单号
	DECLARE sku_new VARCHAR(50) DEFAULT NULL; 				-- SKU
	DECLARE invoice_num_new INT DEFAULT 0; -- 开票数量
	
	-- 定义游标
	DECLARE cur CURSOR FOR SELECT * FROM view_purchase_invoice; 
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1; 
		
	CALL sync_purchase_all_report(); 
	OPEN cur; 
		outerLoop:LOOP
			IF done=1 THEN
				LEAVE outerLoop; 
			END IF; 
			FETCH cur INTO purchase_no_new,sku_new,invoice_num_new; 
			IF done<>1 THEN
				-- SELECT purchase_no_new,sku_new,invoice_num_new; 
				BEGIN
					DECLARE done_report INT DEFAULT 0; 
					DECLARE report_id VARCHAR(50) DEFAULT NULL;						 -- id
					DECLARE report_stock_count INT DEFAULT 0;   -- 入库数量
					DECLARE bill_temp INT DEFAULT 0;   -- 下一次循环中剩余的开票数量
					DECLARE report_no_bill_count INT DEFAULT 0; -- 未开票数量
					DECLARE report_bill_status VARCHAR(16) DEFAULT NULL; -- 开票状态
					DECLARE rur_report CURSOR FOR SELECT id,stock_count FROM purchase_report t WHERE t.purchase_no=purchase_no_new AND t.sku=sku_new ORDER BY t.stock_date; 
					DECLARE CONTINUE HANDLER FOR NOT FOUND SET done_report=1; 
					
					OPEN rur_report; 
						innerLoop:LOOP
							IF done_report=1 THEN
								LEAVE innerLoop; 
							END IF; 
							FETCH rur_report INTO report_id,report_stock_count; 
							IF done_report<>1 THEN
								-- SELECT report_id,report_stock_count; 
								SET bill_temp = invoice_num_new - report_stock_count; 
								IF(invoice_num_new<=0) THEN  -- 如果当前循环的开票数量为0,则说明未开票
									SET report_no_bill_count = report_stock_count; 
									SET report_bill_status = '未开票'; 
								ELSEIF(bill_temp>=0) THEN
									SET report_no_bill_count = 0; 
									SET report_bill_status = '已开票'; 
								ELSE
									SET report_no_bill_count = -bill_temp; 
									SET report_bill_status = '部分开票'; 
								END IF; 
								-- 更新数据
								UPDATE purchase_report SET no_bill_count = report_no_bill_count, bill_status = report_bill_status WHERE id = report_id; 
							END IF; 
						END LOOP; 
					CLOSE rur_report; 
				END; 
			END IF;			
		END LOOP; 
	CLOSE cur; 
    END
;;
DELIMITER ;
SET FOREIGN_KEY_CHECKS=1;
