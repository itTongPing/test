DROP PROCEDURE IF EXISTS aukey_report.sync_stock_age_info_base;
CREATE PROCEDURE aukey_report.sync_stock_age_info_base ()
BEGIN
/*国内自发货仓*/ 
UPDATE aukey_report.stock_age_report sar
LEFT JOIN (
	SELECT
		stock_in.sku,
		stock_in.legaler,
		stock_in.warehouse,
		IFNULL(MAX(	CASE WHEN stock_in.age_name = 'age15' THEN stock_in.quantity END),0	) AS age15,
		IFNULL(MAX(CASE WHEN stock_in.age_name = 'age15_30' THEN stock_in.quantity	END),0) AS age15_30,
		IFNULL(MAX(CASE WHEN stock_in.age_name = 'age30_60' THEN stock_in.quantity END),0) AS age30_60,
		IFNULL(MAX(CASE	WHEN stock_in.age_name = 'age60_90' THEN stock_in.quantity END),0) AS age60_90,
		IFNULL(MAX(CASE	WHEN stock_in.age_name = 'age90_120' THEN stock_in.quantity	END),0) AS age90_120,
		IFNULL(MAX(CASE	WHEN stock_in.age_name = 'age120_150' THEN stock_in.quantity END),0) AS age120_150,
		IFNULL(MAX(CASE	WHEN stock_in.age_name = 'age150_180' THEN stock_in.quantity END),0) AS age150_180,
		IFNULL(MAX(CASE	WHEN stock_in.age_name = 'age180_270' THEN stock_in.quantity END),0) AS age180_270,
		IFNULL(MAX(CASE	WHEN stock_in.age_name = 'age270_365' THEN stock_in.quantity END),0) AS age270_365,
		IFNULL(MAX(CASE	WHEN stock_in.age_name = 'age365_730' THEN stock_in.quantity END),0) AS age365_730,
		IFNULL(MAX(CASE	WHEN stock_in.age_name = 'age731' THEN stock_in.quantity END),0) AS age731
	FROM
		(
		SELECT
			CASE
			WHEN sr.document_date > DATE_ADD(NOW(), INTERVAL - 15 DAY) THEN	'age15'
			WHEN sr.document_date > DATE_ADD(NOW(), INTERVAL - 30 DAY) AND sr.document_date < DATE_ADD(NOW(), INTERVAL - 15 DAY) THEN 'age15_30'
			WHEN sr.document_date > DATE_ADD(NOW(), INTERVAL - 60 DAY) AND sr.document_date < DATE_ADD(NOW(), INTERVAL - 30 DAY) THEN 'age30_60'
			WHEN sr.document_date > DATE_ADD(NOW(), INTERVAL - 90 DAY) AND sr.document_date < DATE_ADD(NOW(), INTERVAL - 60 DAY) THEN 'age60_90'
			WHEN sr.document_date > DATE_ADD(NOW(), INTERVAL - 120 DAY) AND sr.document_date < DATE_ADD(NOW(), INTERVAL - 90 DAY) THEN 'age90_120'
			WHEN sr.document_date > DATE_ADD(NOW(), INTERVAL - 150 DAY)	AND sr.document_date < DATE_ADD(NOW(), INTERVAL - 120 DAY) THEN	'age120_150'
			WHEN sr.document_date > DATE_ADD(NOW(), INTERVAL - 180 DAY)	AND sr.document_date < DATE_ADD(NOW(), INTERVAL - 150 DAY) THEN	'age150_180'
			WHEN sr.document_date > DATE_ADD(NOW(), INTERVAL - 270 DAY)	AND sr.document_date < DATE_ADD(NOW(), INTERVAL - 180 DAY) THEN	'age180_270'
			WHEN sr.document_date > DATE_ADD(NOW(), INTERVAL - 365 DAY)	AND sr.document_date < DATE_ADD(NOW(), INTERVAL - 270 DAY) THEN	'age270_365'
			WHEN sr.document_date > DATE_ADD(NOW(), INTERVAL - 730 DAY)	AND sr.document_date < DATE_ADD(NOW(), INTERVAL - 365 DAY) THEN	'age365_730'
			ELSE 'age731' END AS age_name,
			SUM(sr.quantity) AS quantity,
			sr.sku AS sku,
			sr.legaler AS legaler,
			sr.warehouse AS warehouse
		FROM
			aukey_report.sku_report sr
		WHERE
			sr.business_type IN ('普通采购','转SKU入库','调拨入库','其他入库','无主调拨入库','无主盘盈入库','期初库存','不良品入库')
		GROUP BY
			sr.sku,
			sr.legaler,
			sr.warehouse,
			age_name
		) stock_in
	GROUP BY
		stock_in.sku,
		stock_in.legaler,
		stock_in.warehouse
) stock_age ON sar.sku = stock_age.sku
AND sar.legaler = stock_age.legaler
AND sar.warehouse = stock_age.warehouse
SET sar.age_0_15 =
	IF (
		stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + stock_age.age60_90 + stock_age.age30_60 + stock_age.age15_30 + sar.stock_count_out > 0,
		stock_age.age15,
		IF (
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + stock_age.age60_90 + stock_age.age30_60 + stock_age.age15_30 + stock_age.age15 + sar.stock_count_out < 0,
			0,
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + stock_age.age60_90 + stock_age.age30_60 + stock_age.age15_30 + stock_age.age15 + sar.stock_count_out
		)
	),/*计算库龄为15天以内*/
    sar.age_15_30 =
	IF (
		stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + stock_age.age60_90 + stock_age.age30_60 + sar.stock_count_out > 0,
		stock_age.age15_30,
		IF (
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + stock_age.age60_90 + stock_age.age30_60 + stock_age.age15_30 + sar.stock_count_out < 0,
			0,
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + stock_age.age60_90 + stock_age.age30_60 + stock_age.age15_30 + sar.stock_count_out
		)
	),/*计算库龄为15天到30天*/
    sar.age_30_60 =
	IF (
		stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + stock_age.age60_90 + sar.stock_count_out > 0,
		stock_age.age30_60,
		IF (
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + stock_age.age60_90 + stock_age.age30_60 + sar.stock_count_out < 0,
			0,
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + stock_age.age60_90 + stock_age.age30_60 + sar.stock_count_out
		)
	),/*计算库龄为30天到60天*/
    sar.age_60_90 =
	IF (
		stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + sar.stock_count_out > 0,
		stock_age.age60_90,
		IF (
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + stock_age.age60_90 + sar.stock_count_out < 0,
			0,
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + stock_age.age60_90 + sar.stock_count_out
		)
	),/*计算库龄为60天到90天*/
    sar.age_90_120 =
	IF (
		stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + sar.stock_count_out > 0,
		stock_age.age90_120,
		IF (
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + sar.stock_count_out < 0,
			0,
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + stock_age.age90_120 + sar.stock_count_out
		)
	),/*计算库龄为90天到120天*/
	 sar.age_120_150 =
	IF (
		stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 + sar.stock_count_out > 0,
		stock_age.age120_150,
		IF (
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + sar.stock_count_out < 0,
			0,
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 +stock_age.age120_150 + sar.stock_count_out
		)
	),/*计算库龄为120天到150天*/
	
	sar.age_150_180 =
	IF (
		stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + sar.stock_count_out > 0,
		stock_age.age150_180,
		IF (
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 + sar.stock_count_out < 0,
			0,
			stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + stock_age.age180_270 + stock_age.age150_180 + sar.stock_count_out
		)
	),/*计算库龄为150天到180天*/
	
    sar.age_180_270 =
	IF (
		stock_age.age731 + stock_age.age365_730 + stock_age.age270_365 + sar.stock_count_out > 0,
		stock_age.age180_270,
		IF (
			stock_age.age365_730 + stock_age.age731 + stock_age.age270_365 + stock_age.age180_270 + sar.stock_count_out < 0,
			0,
			stock_age.age365_730 + stock_age.age731 + stock_age.age270_365 + stock_age.age180_270 + sar.stock_count_out
		)
	),/*计算库龄为180天到270天*/
    sar.age_270_365 =
	IF (
		stock_age.age731 + stock_age.age365_730 + sar.stock_count_out > 0,
		stock_age.age270_365,
		IF (
			stock_age.age365_730 + stock_age.age731 + stock_age.age270_365 + sar.stock_count_out < 0,
			0,
			stock_age.age365_730 + stock_age.age731 + stock_age.age270_365 + sar.stock_count_out
		)
	),/*计算库龄为270天到365天*/
    sar.age_365_730 =
	IF (
		stock_age.age731 + sar.stock_count_out > 0,
		stock_age.age365_730,
		IF (
			stock_age.age365_730 + stock_age.age731 + sar.stock_count_out < 0,
			0,
			stock_age.age365_730 + stock_age.age731 + sar.stock_count_out
		)
	),/*计算库龄为365天到730天*/
    sar.age_731 =
	IF (
		(
			stock_age.age731 + sar.stock_count_out
		) < 0,
		0,
		stock_age.age731 + sar.stock_count_out
	),/*计算库龄大于731天*/
	sar.stock_count = sar.stock_count_in + sar.stock_count_out,/*统计库存*/
	sar.price=IF(sar.stock_count_in + sar.stock_count_out=0,0,sar.money/(sar.stock_count_in + sar.stock_count_out))/*统计平均单价*/
WHERE sar.warehouse in(SELECT stock_id FROM product_ms.stock WHERE stock_type='3')
;
/*海外仓*/
UPDATE aukey_report.stock_age_report sar INNER JOIN 
aukey_report.view_stock_age_report_overseas_in a ON sar.sku = a.sku
AND sar.warehouse = a.stock_id
SET 
		sar.age_0_15 = IF(a.age15<a.stock_count,a.age15,a.stock_count),

		sar.age_15_30 = IF(a.age15+a.age15_30<a.stock_count,a.age15_30,IF(a.stock_count-a.age15>0,a.stock_count-a.age15,0)),
		
		sar.age_30_60 = IF(a.age15+a.age15_30+a.age30_60<a.stock_count,a.age30_60,IF(a.stock_count-a.age15-a.age15_30>0,a.stock_count-a.age15-a.age15_30,0)),

		sar.age_60_90 = IF(a.age15+a.age15_30+a.age30_60+a.age60_90<a.stock_count,a.age60_90,IF(a.stock_count-a.age15-a.age15_30-a.age30_60>0,a.stock_count-a.age15-a.age15_30-a.age30_60,0)),

		sar.age_90_120 = IF(a.age15+a.age15_30+a.age30_60+a.age60_90+a.age90_120<a.stock_count,a.age90_120,IF(a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90>0,a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90,0)),
		
		sar.age_120_150 = IF(a.age15+a.age15_30+a.age30_60+a.age60_90+a.age90_120+a.age120_150<a.stock_count,a.age120_150,IF(a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120>0,a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120,0)),

		sar.age_150_180 = IF(a.age15+a.age15_30+a.age30_60+a.age60_90+a.age90_120+a.age120_150+a.age150_180<a.stock_count,a.age150_180,IF(a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120-a.age120_150>0,a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120-a.age120_150,0)),

		sar.age_180_270 = IF(a.age15+a.age15_30+a.age30_60+a.age60_90+a.age90_120+a.age120_150+a.age150_180+a.age180_270<a.stock_count,a.age180_270,IF(a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120-a.age120_150-a.age150_180>0,a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120-a.age120_150-a.age150_180,0)),

		sar.age_270_365 = IF(a.age15+a.age15_30+a.age30_60+a.age60_90+a.age90_120+a.age120_150+a.age150_180+a.age180_270+a.age270_365<a.stock_count,a.age270_365,IF(a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120-a.age120_150-a.age150_180-a.age180_270>0,a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120-a.age120_150-a.age150_180-a.age180_270,0)),

		sar.age_365_730 = IF(a.age15+a.age15_30+a.age30_60+a.age60_90+a.age90_120+a.age120_150+a.age150_180+a.age180_270+a.age270_365+a.age365_730<a.stock_count,a.age365_730,IF(a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120-a.age120_150-a.age150_180-a.age180_270-a.age270_365>0,a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120-a.age120_150-a.age150_180-a.age180_270-a.age270_365,0)),

		sar.age_731 = IF(a.age15+a.age15_30+a.age30_60+a.age60_90+a.age90_120+a.age120_150+a.age150_180+a.age180_270+a.age270_365+a.age365_730+a.age731<a.stock_count,a.age731,IF(a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120-a.age120_150-a.age150_180-a.age180_270-a.age270_365-a.age365_730>0,a.stock_count-a.age15-a.age15_30-a.age30_60-a.age60_90-a.age90_120-a.age120_150-a.age150_180-a.age180_270-a.age270_365-a.age365_730,0)),
		sar.stock_count = a.stock_count/*统计库存*/
;
END;