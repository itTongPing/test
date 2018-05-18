DROP VIEW IF EXISTS view_stock_in;
CREATE OR REPLACE VIEW view_stock_in AS
#------------------------------------------普通采购--------------------------------------------------------
  SELECT
    rt.requirement_no,
    po.legal_person_id AS '法人主体id',
    rt.dept_id         AS '需求部门id',
    rt.category_id     AS '品类Id',
    po.is_tax          AS '含税',
    rt.transport_type  AS '运输方式',
    sri.stock_id        AS '仓库id',
    sri.sku_code,
	IF(po.currency='CNY',1,(
		SELECT
		 currency_rate
		FROM
		 product_ms.currency_set
		WHERE
		 audit_time = (
			SELECT
			 MAX(audit_time)
			FROM
			 product_ms.currency_set
			WHERE
				currency_code=po.currency
				AND audit_time <= po.create_date 
				AND audit_status = '1'
		 )
		AND currency_code = po.currency 
		AND audit_status = '1'
))*IF(po.is_tax='1',pd.tax_unit_price,pd.unit_price) AS price,
    supply_quantity      rk,
    sr.create_time     AS '入库时间',
    '入库'         AS '入库类型'
  FROM
    supply_sign.stock_request_inquiry sri
    JOIN
    supply_sign.storage_requirement sr ON sri.requirement_id = sr.requirement_no
                                          AND sr.`type` = '1'
    JOIN
    supply_chain.purchase_demand pd ON sr.requirement_no = pd.purchase_demand_id
    JOIN
    supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
    JOIN
    supply_chain.requirement rt ON pd.purchase_demand_id = rt.requirement_no
  WHERE
    sri.stock_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)


#------------------------------------------转SKU入库--------------------------------------------------------
UNION ALL

  SELECT
    rt.requirement_no,
    po.legal_person_id AS '法人主体id',
    rt.dept_id         AS '需求部门id',
    rt.category_id     AS '品类Id',
    po.is_tax          AS '含税',
    rt.transport_type  AS '运输方式',
    sr.stock_id        AS '仓库id',
    sr.sku_code,
	IF(po.currency='CNY',1,(
		SELECT
		 currency_rate
		FROM
		 product_ms.currency_set
		WHERE
		 audit_time = (
			SELECT
			 MAX(audit_time)
			FROM
			 product_ms.currency_set
			WHERE
				currency_code=po.currency
				AND audit_time <= po.create_date 
				AND audit_status = '1'
		 )
		AND currency_code = po.currency 
		AND audit_status = '1'
))*IF(po.is_tax='1',pd.tax_unit_price,pd.unit_price) AS price,
    sr.quantity_available rk,
    sr.create_time     AS '入库时间',
    '转SKU库存 入库'         AS '入库类型'
  FROM
    supply_sign.stock_record sr
    JOIN
    supply_chain.purchase_demand pd ON sr.document_number = pd.purchase_demand_id
    JOIN
    supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
    JOIN
    supply_chain.requirement rt ON pd.purchase_demand_id = rt.requirement_no
  WHERE
    sr.stock_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)
    AND sr.type = 16

    
#------------------------------------------无主调拨入库(中转调拨、盘盈入库)--------------------------------------------------------
UNION ALL

SELECT
	null requirement_no,
	3 AS '法人主体id',
	0 AS '需求部门id',
	null AS '品类Id',
	'' AS '含税',
	'' AS '运输方式',
	isr.stock_id AS '仓库id',
	isr.sku sku_code,
	0.00 AS price,
	isr.quantity rk,
	isr.create_date AS '入库时间',
	'无主调拨入库' AS '入库类型'
FROM
	supply_sign.center_idle_stock_record isr
inner join product_ms.stock s on isr.stock_id=s.stock_id
WHERE
	type in ('2','3','13','14')
	and s.stock_type in ('3','11') #只取中转仓、海外中转仓的数据
	
#------------------------------------------不良品入库--------------------------------------------------------
UNION ALL
#不良品入库
SELECT
	str.requirement_no    AS requirement_no,
	po.legal_person_id 	  AS '法人主体id',
	req.dept_id			  AS '需求部门id',
	NULL				  AS '品类Id',
	po.is_tax			  AS '含税',
	req.transport_type    AS '运输方式',
	st.warehouse_id		  AS '仓库id',
	st.sku_code			  AS sku_code,
	0					  AS price,
	st.rejects_number     AS rk,
	st.create_date		  AS '入库时间',
	'不良品入库'             AS '入库类型'
FROM
	supply_sign.`storage` st
LEFT JOIN supply_sign.storage_requirement str ON str.storage_number=st.storage_number
LEFT JOIN supply_chain.purchase_demand pd ON pd.purchase_demand_id = str.requirement_no
LEFT JOIN supply_chain.purchase_order po ON po.purchase_order_id = pd.purchase_order_id
LEFT JOIN supply_chain.requirement req ON req.requirement_no = str.requirement_no
WHERE
	st.type='0' and st.warehouse_id IN (12,13,141)
