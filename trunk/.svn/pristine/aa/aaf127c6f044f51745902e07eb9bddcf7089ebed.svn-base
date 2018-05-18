/*
Navicat MySQL Data Transfer

Source Server         : 10.1.1.86_QA(aukey_report)
Source Server Version : 50719
Source Host           : 10.1.1.86:3306
Source Database       : aukey_report

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-10-18 20:23:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Procedure structure for `sync_storage_report`
-- ----------------------------
DROP PROCEDURE IF EXISTS `sync_storage_report`;
DELIMITER ;;
CREATE DEFINER=`aukey_report`@`%` PROCEDURE `sync_storage_report`()
begin -- 开始存储过程  
declare storage_number varchar(50); -- 自定义变量1  
declare quality_inspection_number varchar(50); -- 自定义变量2 
declare purchase_number varchar(50); -- 自定义变量2
declare supplier_id int(11); -- 自定义变量2
declare Inspector varchar(50); -- 自定义变量2
declare warehouse_id mediumint(6); -- 自定义变量2
declare nondefective_number int(11); -- 自定义变量2
declare rejects_number int(11); -- 自定义变量2
declare spare_parts_number int(11); -- 自定义变量2
declare storage_location_code varchar(50); -- 自定义变量2
declare create_user varchar(50); -- 自定义变量2
declare create_date datetime; -- 自定义变量2
declare sku_code varchar(50); -- 自定义变量2
declare type varchar(50); -- 自定义变量2

declare sku_name varchar(500); -- 自定义变量2
declare muWarehouseName varchar(50); -- 自定义变量2
declare legal_person_id varchar(50); -- 自定义变量2
declare transport_type varchar(50); -- 自定义变量2
declare transport_type_name varchar(50); -- 自定义变量2
declare is_tax varchar(50); -- 自定义变量2
declare is_tax_name varchar(50); -- 自定义变量2

declare corporation_id varchar(50); -- 自定义变量2
declare corporation_name varchar(50); -- 自定义变量2
declare buyer_id varchar(50); -- 自定义变量2
declare reqUserName varchar(50); -- 自定义变量2

declare saleName varchar(500); -- 自定义变量2
declare deptName varchar(500); -- 自定义变量2

declare stockname varchar(50); -- 自定义变量2

declare stock_id varchar(50); -- 自定义变量2

declare no_tax_price VARCHAR(50);
declare tax_unit_price VARCHAR(50);
declare supplier_name VARCHAR(255);
declare currency VARCHAR(10);


DECLARE done INT DEFAULT FALSE; -- 自定义控制游标循环变量,默认false  

DECLARE num INT DEFAULT 0;

DECLARE My_Cursor CURSOR FOR ( 

select  	 T1.storage_number ,
					 T1.quality_inspection_number ,
					 T1.purchase_number ,
					 T1.supplier_id ,
					 T1.Inspector ,
					 T1.warehouse_id ,
					 T1.nondefective_number ,
					 T1.rejects_number ,
					 T1.spare_parts_number,
					 T1.storage_location_code,
					 T1.create_user ,
					 T1.create_date ,
					 T1.sku_code ,
					 T1.type 
from supply_sign.STORAGE T1 LEFT JOIN 
		aukey_report.storage_report T2 on T1.storage_number = T2.storage_number WHERE T2.storage_number IS  NULL 

 ); -- 定义游标并输入结果集  
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE; -- 绑定控制变量到游标,游标循环结束自动转true  
  
OPEN My_Cursor; -- 打开游标  
  myLoop: LOOP -- 开始循环体,myLoop为自定义循环名,结束循环时用到  
    FETCH My_Cursor into
					 storage_number ,
					 quality_inspection_number ,
					 purchase_number ,
					 supplier_id ,
					 Inspector ,
					 warehouse_id ,
					 nondefective_number ,
					 rejects_number ,
					 spare_parts_number,
					 storage_location_code,
					 create_user ,
					 create_date ,
					 sku_code ,
					 type ; -- 将游标当前读取行的数据顺序赋予自定义变量12  
     IF done THEN -- 判断是否继续循环  
       LEAVE myLoop; -- 结束循环  
     END IF;  
    -- 自己要做的事情,在 sql 中直接使用自定义变量即可 

		 SELECT st.name into supplier_name from supplier2.supplier st where st.supplier_id = supplier_id;
			
		 SELECT  sku.NAME into sku_name  from product_ms.sku sku where sku.CODE = sku_code;
		
		 SELECT  stock.NAME into muWarehouseName  from  product_ms.stock stock where stock.stock_id = warehouse_id;

    -- 计算含税单价和不含税单价 	
		 SELECT
					IF (
									`supply_chain`.`purchase_order`.`is_tax` = '1'
								,
									`supply_chain`.`purchase_demand`.`tax_unit_price` / (
										1 + `supply_chain`.`purchase_demand`.`tax_rate`
									)
								,
								`supply_chain`.`purchase_demand`.`unit_price`
							) AS `no_tax_price`,

						IF (
								`supply_chain`.`purchase_order`.`is_tax` = '1'
							,
							`supply_chain`.`purchase_demand`.`tax_unit_price`,
							NULL
						) AS `tax_unit_price`,
					`supply_chain`.`purchase_order`.currency AS currency
					into no_tax_price , tax_unit_price,currency
					FROM
					
								`supply_chain`.`purchase_demand`
								JOIN `supply_chain`.`purchase_order` ON 
	
								`supply_chain`.`purchase_demand`.`purchase_order_id` = `supply_chain`.`purchase_order`.`purchase_order_id`
					WHERE `supply_chain`.`purchase_order`.`purchase_order_id` = purchase_number
								 and `supply_chain`.`purchase_demand`.`sku_code` = sku_code
					GROUP BY
						`supply_chain`.`purchase_demand`.`purchase_order_id`,
						`supply_chain`.`purchase_demand`.`sku_code`;

		 select   po.transport_type,
					 CASE
					WHEN po.transport_type = '0' THEN
					 '空运'
					WHEN po.transport_type = '1' THEN
					 '海运'
					WHEN po.transport_type = '3' THEN
					 '铁运'
					ELSE
					 '无'
					END  AS transport_type_name, 
					po.is_tax ,
					IF (
					 po.is_tax = '1',
					 '含税',
					 '退税'
					) AS is_tax_name,po.legal_person_id,po.buyer_id INTO transport_type,transport_type_name,is_tax
					,is_tax_name,legal_person_id,buyer_id    
					from  supply_chain.purchase_order po where po.purchase_order_id = purchase_number;


		 SELECT basep.corporation_id , basep.corporation_name into corporation_id,corporation_name  from  
					supplier2.base_corporation basep WHERE basep.corporation_id = legal_person_id;

			SELECT reqUser.NAME INTO reqUserName   FROM  cas.USER reqUser WHERE reqUser.user_id = buyer_id;
			
			SELECT GROUP_CONCAT(DISTINCT salUser.name) INTO saleName  FROM supply_chain.purchase_demand pd
					INNER JOIN supply_chain.requirement requr
					ON requr.requirement_no=pd.purchase_demand_id
					INNER JOIN cas.USER salUser ON salUser.user_id = requr.create_user 
					WHERE pd.purchase_order_id = purchase_number;
		
			SELECT GROUP_CONCAT(DISTINCT orgp.name) INTO  deptName FROM supply_chain.purchase_demand pd
					INNER JOIN supply_chain.requirement requr
					ON requr.requirement_no=pd.purchase_demand_id
					INNER JOIN cas.org_group orgp
					ON orgp.id=requr.dept_id
					WHERE pd.purchase_order_id = purchase_number;

		select stocks.name ,stocks.stock_id INTO  stockname,stock_id   FROM  supply_chain.purchase_demand dema
     LEFT JOIN supply_chain.requirement req on dema.purchase_demand_id = req.requirement_no   
     LEFT JOIN product_ms.stock stocks ON stocks.stock_id = req.warehouse_id

		 where dema.purchase_order_id = purchase_number and dema.sku_code = sku_code LIMIT 1;
		
			
			 
			INSERT into 
				storage_report  (
				storage_number,
				quality_inspection_number,
				purchase_number,
				supplier_id,
				Inspector,
				warehouse_id,
				muWarehouseName,
				nondefective_number,
				rejects_number,
				spare_parts_number,
				storage_location_code,
				create_user,
				create_date,
				sku_code,
				SKUname,
				type,
				corporation_id,
				corporation_name,
				transport_type,
				transport_type_name,
				is_tax,
				is_tax_name,
				reqUserName,
				saleName,
				deptName,
				stockname,
				stock_id,
				no_tax_price , 
				tax_unit_price,
				supplier_name,
				currency
			) VALUES (
				storage_number,
				quality_inspection_number,
				purchase_number,
				supplier_id,
				Inspector,
				warehouse_id,
				muWarehouseName,
				nondefective_number,
				rejects_number,
				spare_parts_number,
				storage_location_code,
				create_user,
				create_date,
				sku_code,
				sku_name,
				type,
				corporation_id,
				corporation_name,
				transport_type,
				transport_type_name,
				is_tax,
				is_tax_name,
				reqUserName,
				saleName,
				deptName,
				stockname,
				stock_id,
				no_tax_price , 
				tax_unit_price,
				supplier_name,
				currency
				);
			
			SET num = num + 1 ;
			
		 IF mod(num,10000) THEN
				 COMMIT; -- 提交事务  
		 END IF;
  END LOOP myLoop; -- 结束自定义循环体  
  CLOSE My_Cursor; -- 关闭游标  
END
;;
DELIMITER ;
