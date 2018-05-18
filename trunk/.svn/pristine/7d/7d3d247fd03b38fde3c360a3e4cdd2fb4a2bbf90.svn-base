DROP PROCEDURE
IF EXISTS `sync_sku_surplus`;

DELIMITER //


CREATE PROCEDURE sync_sku_surplus (
	IN document_date_start DATETIME,
	IN document_date_end DATETIME
)
BEGIN

DECLARE cur_id VARCHAR (36) ;
DECLARE cur_sku VARCHAR (50) ;
DECLARE cur_document_date datetime ;
DECLARE cur_business_type VARCHAR (50) ; # '业务类型',
DECLARE cur_is_tax INT (255) ; #'是否含税',
DECLARE cur_legaler INT (11) ; # '法人主体',
DECLARE cur_warehouse INT (11) ; # '仓库',
DECLARE cur_count_in INT (11) ; # '入库数量',
DECLARE cur_price_in DECIMAL (10, 2) ; # '入库单价',
DECLARE cur_cost_in DECIMAL (10, 2) ;
DECLARE cur_count_out INT (255) ; # '出库数量',
DECLARE cur_price_out DECIMAL (10, 2) ; #'出库单价',
DECLARE cur_cost_out DECIMAL (10, 2) ;
DECLARE cur_count_surplus INT (255) ; #'结余数量',
DECLARE cur_price_surplus DECIMAL (18, 2) ; #'结余单价',
DECLARE cur_cost_surplus DECIMAL (18, 2) ;
DECLARE cur_count_surplus_new INT (255) ; #'结余数量',
DECLARE cur_price_surplus_new DECIMAL (18, 2) ; #'结余单价',
DECLARE cur_cost_surplus_new DECIMAL (18, 2) ;
DECLARE cur_report_date datetime ; #'报表日期',
DECLARE done INT DEFAULT FALSE; -- 自定义控制游标循环变量,默认false  

DECLARE num INT DEFAULT 0;

DECLARE My_Cursor CURSOR FOR (
	SELECT
		id,
		sku,
		business_type,
		document_date,
		is_tax,
		legaler,
		warehouse,
		count_in,
		price_in,
		cost_in,
		count_out,
		price_out
	FROM
		sku_report
	WHERE
		report_date = document_date_start
	ORDER BY
		document_date asc
) ; -- 定义游标并输入结果集
DECLARE CONTINUE HANDLER FOR NOT FOUND
SET done = TRUE ; -- 绑定控制变量到游标,游标循环结束自动转true  
OPEN My_Cursor ; -- 打开游标  
myLoop :
LOOP
	-- 开始循环体,myLoop为自定义循环名,结束循环时用到  
	FETCH My_Cursor INTO cur_id,
	cur_sku,
	cur_business_type,
	cur_document_date,
	cur_is_tax,
	cur_legaler,
	cur_warehouse,
	cur_count_in,
	cur_price_in,
	cur_cost_in,
	cur_count_out,
	cur_price_out ; -- 将游标当前读取行的数据顺序赋予自定义变量12  


IF done THEN
	-- 判断是否继续循环  
	LEAVE myLoop ; -- 结束循环
 
END IF ; 

select 
if(count(1)>0,count_surplus,0),
	IFNULL(price_surplus,0),
	IFNULL(cost_surplus,0) INTO cur_count_surplus ,cur_price_surplus ,cur_cost_surplus
from (
	SELECT
		count_surplus,
		price_surplus,
		cost_surplus
	FROM
		sku_report
	WHERE 
		sku =cur_sku
	AND is_tax =cur_is_tax
	AND legaler =cur_legaler
	AND warehouse =cur_warehouse
	AND document_date <cur_document_date
	ORDER BY
		document_date DESC
	LIMIT 1 
) t;



SET cur_count_surplus_new = ifnull(cur_count_surplus, 0) + ifnull(cur_count_in, 0) - ifnull(cur_count_out, 0) ;
SET cur_price_surplus_new =case when (ifnull(cur_count_surplus, 0) + ifnull(cur_count_in, 0) + ifnull(cur_count_out, 0)) <>0 then  (	ifnull(cur_price_surplus,0) *ifnull(cur_count_surplus, 0) + ifnull(cur_price_in, 0) *ifnull(cur_count_in, 0) + ifnull(cur_price_out, 0) *ifnull(cur_count_out, 0)) 
/ 
(
	ifnull(cur_count_surplus, 0) + ifnull(cur_count_in, 0) + ifnull(cur_count_out, 0)
) end;
SET cur_cost_surplus_new =ifnull(cur_count_surplus_new, 0) * ifnull(cur_price_surplus_new, 0) ;
 
UPDATE sku_report
SET count_surplus =cur_count_surplus_new,
 price_surplus =cur_price_surplus_new,
 cost_surplus =cur_cost_surplus_new
WHERE
	sku =cur_sku
AND warehouse =cur_warehouse
AND business_type =cur_business_type
AND id =cur_id ;

END LOOP
	myLoop ; -- 结束自定义循环体  
SELECT num;
	CLOSE My_Cursor ; -- 关闭游标  
END//
DELIMITER ;
