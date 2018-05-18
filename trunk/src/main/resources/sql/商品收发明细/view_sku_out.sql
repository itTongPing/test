CREATE OR REPLACE VIEW aukey_report.view_sku_out AS

/*------------------------------------------调拨出库--------------------------------------------------------*/
SELECT
  transfer.sku               AS sku_code,
  transfer.transfer_no       AS document_number,      /*'单据编号',*/
  transfer.create_time       AS document_date,        /*'单据日期',*/
  '调拨出库'                    AS business_type,        /*'业务类型',*/
  transfer.is_tax            AS is_tax,               /*'含税',*/
  transfer.subject_id        AS legal_person_id,      /*'法人主体id',*/
  rt.dept_id                 AS dept_id,              /*'需求部门id',*/
  rt.transport_type          AS transport_type,       /*'运输方式',*/
  transfer.create_user       AS create_user,          /*'制单人',*/
  po.supplier_id             AS supplier_id,          /*'供应商id',*/
  transfer.out_warehouse_id  AS warehouse_id,         /*'出库仓库id',*/
  transfer.box_count         AS quantity,             /*'出库数量',*/
  IF(
      po.is_tax = '1',
      pd.tax_unit_price,
      pd.unit_price
  )                          AS unit_price,           /*'单位成本',*/
  po.currency                AS currency,             /*'币种',*/
	null                        AS remark             /*说明*/
FROM
	(
		SELECT
    	td.requirement_no,
    	ts.transfer_no,
    	td.sku,
	    ts.out_warehouse_id,
	    td.subject_id,
	    ts.is_tax,
	    ts.transport_type,
	    SUM(box_count) box_count,
	    ts.actual_outtime create_time,
	    ts.create_user
    FROM
	    supply_delivery.transfer_slip ts
      JOIN supply_delivery.transfer_detail td ON ts.transfer_id = td.transfer_id
    WHERE
	    ts.transfer_status IN ('1', '2', '3') /*增加部份到货状态2017-11-28*/
      AND ts.data_status = '1'
      AND td.data_status = '1'
    GROUP BY
	    td.requirement_no,
	    ts.transfer_no,
	    td.sku,
	    ts.out_warehouse_id,
	    td.subject_id,
	    ts.is_tax,
	    ts.transport_type
	) transfer 
JOIN supply_chain.requirement rt ON transfer.requirement_no = rt.requirement_no
JOIN supply_chain.purchase_demand pd ON rt.requirement_no = pd.purchase_demand_id
JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
  transfer.out_warehouse_id  in (8, 70, 71, 72, 73, 93, 94, 139, 140)

/*------------------------------------------退货出库--------------------------------------------------------*/  
UNION ALL

SELECT
	prd.sku_code               AS sku_code,
	pro.purchase_return_id     AS document_number,      /*'单据编号',*/
	IFNULL((select sr.time_ed from supply_sign.stock_record sr where pro.purchase_return_id = sr.document_number and prd.sku_code=sr.sku_code and sr.type=21 LIMIT 1), pro.create_date)         AS document_date,        /*'单据日期',*/
    '采购退货'                    AS business_type,        /*'业务类型',*/
	pro.is_tax                 AS is_tax,               /*'含税',*/
	pro.legal_person_id        AS legal_person_id,      /*'法人主体id',*/
	rt.dept_id                 AS dept_id,              /*'需求部门id',*/
	rt.transport_type          AS transport_type,       /*'运输方式',*/
    pro.create_user            AS create_user,          /*'制单人',*/
	pro.supplier_id            AS supplier_id,          /*'供应商id',*/
	pro.warehouse_id           AS warehouse_id,         /*'出库仓库id',*/
	prd.refund_quality         AS quantity,             /*'出库数量',*/
	prd.purchase_price         AS unit_price,           /*'单位成本',*/
	pro.currency               AS currency,             /*'币种',*/
	pro.remark                 AS remark                /*说明*/
FROM
	supply_chain.purchase_return_order pro
JOIN supply_chain.purchase_return_demand prd ON pro.purchase_return_id = prd.purchase_return_id
JOIN supply_chain.purchase_order po ON pro.purchase_order_id = po.purchase_order_id
JOIN supply_chain.requirement rt ON prd.purchase_demand_id = rt.requirement_no 
WHERE
	(/*(*/pro.order_type=1 
AND pro.data_status = '1'
AND pro.order_status  in ('4','6')
  and pro.warehouse_id  in (8, 70, 71, 72, 73, 93, 94, 139, 140)/*) OR pro.order_type='4'*/)

  
/*------------------------------------------其他出库--------------------------------------------------------*/
UNION ALL

SELECT
	wsr.sku_code                AS sku_code,
  ws.document_number          AS document_number,      /*'单据编号',*/
  ws.verifyed_date            AS document_date,        /*'单据日期',*/
  '其他出库'                     AS business_type,        /*'业务类型',*/
  po.is_tax                   AS is_tax,               /*'含税',*/
  po.legal_person_id          AS legal_person_id,      /*'法人主体id',*/
  rt.dept_id                  AS dept_id,              /*'需求部门id',*/
  rt.transport_type           AS transport_type,       /*'运输方式',*/
  po.create_user              AS create_user,          /*'制单人',*/
  po.supplier_id              AS supplier_id,          /*'供应商id',*/
  wsr.stock_id            		AS warehouse_id,         /*'出库仓库id',*/
  wsr.quantity                AS quantity,             /*'出库数量',*/
  IF(
      po.is_tax = '1',
      pd.tax_unit_price,
      pd.unit_price
  )                          AS unit_price,           /*'单位成本',*/
  po.currency                AS currency,              /*'币种',*/
	null                        AS remark                /*说明*/
FROM supply_sign.warehouse_storage_requirement wsr
JOIN supply_sign.warehouse_storage ws ON wsr.document_number = ws.document_number 
JOIN supply_chain.requirement rt ON rt.requirement_no = wsr.requirement_no
JOIN supply_chain.purchase_demand pd ON rt.requirement_no = pd.purchase_demand_id
JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
  ws.document_state = 'FINISH'
  and ws.service_type IN ('1', '2', '3', '4')
  and wsr.stock_id  in (8, 70, 71, 72, 73, 93, 94, 139, 140)

/*------------------------------------------转SKU出库--------------------------------------------------------*/
UNION  ALL

SELECT
  sr.sku_code            AS sku_code,
  '-'                   AS document_number,       /*'单据编号',*/
  sr.time_ed         AS document_date,        /*'单据日期',*/
  '转SKU出库'              AS business_type,        /*'业务类型',*/
  po.is_tax              AS is_tax,               /*'含税',*/
  po.legal_person_id     AS legal_person_id,      /*'法人主体id',*/
  rt.dept_id             AS dept_id,              /*'需求部门id',*/
  rt.transport_type      AS transport_type,       /*'运输方式',*/
  po.create_user         AS create_user,          /*'制单人',*/
  po.supplier_id         AS supplier_id,          /*'供应商id',*/
  sr.stock_id            AS warehouse_id,         /*'出库仓库id',*/
  sr.quantity_available  AS quantity,             /*'出库数量',*/
  IF (po.is_tax = '1',pd.tax_unit_price,pd.unit_price)         AS unit_price,           /*'单位成本',*/
  po.currency            AS currency,             /*'币种',*/
	null                    AS remark                /*说明*/
FROM
	supply_sign.stock_record sr
	JOIN supply_chain.purchase_demand pd ON sr.document_number = pd.purchase_demand_id
	JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
	JOIN supply_chain.requirement rt ON pd.purchase_demand_id = rt.requirement_no
WHERE
	sr.stock_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)
	AND sr.type = 19


/*------------------------------------------无主调拨出库--------------------------------------------------------*/
union all
SELECT
  ctd.from_wsku              AS sku_code,
  ct.transfer_no             AS document_number,      /*'单据编号',*/
  ifnull((select sr.time_ed from supply_sign.stock_record sr where type='25' and sr.document_number=ct.transfer_no and sr.sku_code=ctd.from_wsku and sr.stock_id=ct.from_warehouse_id LIMIT 1),ctd.create_date)           AS document_date,
  '需求单调拨出库'             AS business_type,        /*'业务类型',*/
  po.is_tax                  AS is_tax,               /*'含税',*/
  po.legal_person_id         AS legal_person_id,      /*'法人主体id',*/
  req.dept_id                AS dept_id,              /*'需求部门id',*/
  req.transport_type         AS transport_type,       /*'运输方式',*/
  po.create_user             AS create_user,          /*'制单人',*/
  po.supplier_id             AS supplier_id,          /*'供应商id',*/
  ct.from_warehouse_id       AS warehouse_id,         /*'出库仓库id',*/
  ctd.transfer_amount        AS quantity,             /*'出库数量',*/
  IF (po.is_tax = '1',pd.tax_unit_price,pd.unit_price)         AS unit_price,           /*'单位成本',*/
  po.currency                AS currency,             /*'币种'*/
	null                       AS remark                /*说明*/
FROM
	supply_sign.center_transfer ct
	inner join supply_sign.center_transfer_detail ctd on ct.transfer_no=ctd.transfer_no
	inner join supply_chain.requirement req on ctd.relation_no=req.requirement_no
	inner JOIN supply_chain.purchase_demand pd ON req.requirement_no = pd.purchase_demand_id
	inner JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
		ct.data_status in ('4','5')
	and ct.transfer_type='1'	
    and ct.from_warehouse_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)
	
union all
SELECT
  ctd.from_wsku              AS sku_code,
  ct.transfer_no             AS document_number,      /*'单据编号',*/
  ifnull((select sr.time_ed from supply_sign.stock_record sr where type='25' and sr.document_number=ct.transfer_no and sr.sku_code=ctd.from_wsku and sr.stock_id=ct.from_warehouse_id LIMIT 1),ctd.create_date)           AS document_date,
  '需求单调拨出库'             AS business_type,           /*'业务类型',*/
  po.is_tax                  AS is_tax,               /*'含税',*/
  po.legal_person_id         AS legal_person_id,      /*'法人主体id',*/
  req.dept_id                AS dept_id,              /*'需求部门id',*/
  req.transport_type         AS transport_type,       /*'运输方式',*/
  po.create_user             AS create_user,          /*'制单人',*/
  po.supplier_id             AS supplier_id,          /*'供应商id',*/
  ct.from_warehouse_id       AS warehouse_id,         /*'出库仓库id',*/
  ctd.transfer_amount        AS quantity,             /*'出库数量',*/
  IF (po.is_tax = '1',pd.tax_unit_price,pd.unit_price)         AS unit_price,           /*'单位成本',*/
  po.currency                AS currency,             /*'币种'*/
	null                       AS remark                /*说明*/
FROM
	supply_sign.center_transfer ct
	inner join supply_sign.center_transfer_detail ctd on ct.transfer_no=ctd.transfer_no 
	inner join supply_chain.stock_correspondencea sc on ctd.relation_no=sc.id
	inner join supply_chain.requirement req on sc.requirement_id=req.requirement_no
	inner JOIN supply_chain.purchase_demand pd ON req.requirement_no = pd.purchase_demand_id
	inner JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
		ct.data_status in ('4','5')
	and ct.transfer_type='3'	
    and ct.from_warehouse_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)
	

/*------------------------------------------需求单无主盘亏出库--------------------------------------------------------*/
UNION  ALL

SELECT
  sr.sku_code            AS sku_code,
  sr.document_number     AS document_number,      /*'单据编号',*/
  sr.time_ed             AS document_date,        /*'单据日期',*/
  '需求单盘亏出库'            AS business_type,        /*'业务类型',*/
  po.is_tax              AS is_tax,               /*'含税',*/
  po.legal_person_id     AS legal_person_id,      /*'法人主体id',*/
  rt.dept_id             AS dept_id,              /*'需求部门id',*/
  rt.transport_type      AS transport_type,       /*'运输方式',*/
  po.create_user         AS create_user,          /*'制单人',*/
  po.supplier_id         AS supplier_id,          /*'供应商id',*/
  sr.stock_id            AS warehouse_id,         /*'出库仓库id',*/
  sr.quantity_available  AS quantity,             /*'出库数量',*/
  IF (po.is_tax = '1',pd.tax_unit_price,pd.unit_price)         AS unit_price,           /*'单位成本',*/
  po.currency            AS currency,             /*'币种',*/
	null                    AS remark                /*说明*/
FROM
	supply_sign.stock_record sr
	JOIN supply_chain.requirement rt ON sr.document_number = rt.requirement_no
	JOIN supply_chain.purchase_demand pd ON rt.requirement_no = pd.purchase_demand_id
	JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
	sr.stock_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)
	AND sr.type = '27'

/*------------------------------------------转SKU无主盘亏出库--------------------------------------------------------*/
UNION  ALL

SELECT
  sr.sku_code            AS sku_code,
  sr.document_number     AS document_number,      /*'单据编号',*/
  sr.time_ed             AS document_date,        /*'单据日期',*/
  '需求单盘亏出库'            AS business_type,        /*'业务类型',*/
  po.is_tax              AS is_tax,               /*'含税',*/
  po.legal_person_id     AS legal_person_id,      /*'法人主体id',*/
  rt.dept_id             AS dept_id,              /*'需求部门id',*/
  rt.transport_type      AS transport_type,       /*'运输方式',*/
  po.create_user         AS create_user,          /*'制单人',*/
  po.supplier_id         AS supplier_id,          /*'供应商id',*/
  sr.stock_id            AS warehouse_id,         /*'出库仓库id',*/
  sr.quantity_available  AS quantity,             /*'出库数量',*/
  IF (po.is_tax = '1',pd.tax_unit_price,pd.unit_price)         AS unit_price,           /*'单位成本',*/
  po.currency            AS currency,             /*'币种',*/
	null                    AS remark                /*说明*/
FROM
	supply_sign.stock_record sr
	inner join supply_chain.stock_correspondencea sc on sr.document_number=sc.id
	JOIN supply_chain.requirement rt ON sc.requirement_id = rt.requirement_no
	JOIN supply_chain.purchase_demand pd ON rt.requirement_no = pd.purchase_demand_id
	JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
WHERE
	sr.stock_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)
	AND sr.type = '27'	
/*------------------------------------------无主调拨出库(中转调拨、盘亏出库)--------------------------------------------------------*/
UNION ALL

SELECT
  isr.sku               AS sku_code,
  isr.relation_no       AS document_number,      /*'单据编号',*/
  isr.create_date       AS document_date,        /*'单据日期',*/
  (case when type='1' 
        then '无主调拨出库'
        when type ='4'
        then '无主盘亏出库'
        when type ='9'
        then '无主海外调拨出库'
        when type ='12'
        then '海外调海外出库' end ) 
                        AS business_type,        /*'业务类型',*/
  0                  	AS is_tax,               /*'含税',*/
  3                     AS legal_person_id,      /*'法人主体id',*/
  IFNULL(req.dept_id,0) AS dept_id,              /*'需求部门id',*/
  null                  AS transport_type,       /*'运输方式',*/
  isr.create_user       AS create_user,          /*'制单人',*/
  0                     AS supplier_id,          /*'供应商id',*/
  isr.stock_id          AS warehouse_id,         /*'出库仓库id',*/
  isr.quantity          AS quantity,             /*'出库数量',*/
  null                  AS unit_price,           /*'单位成本',*/
  null                  AS currency,             /*'币种',*/
	null                   AS remark                /*说明*/
FROM
	supply_sign.center_idle_stock_record isr
	inner join product_ms.stock s on isr.stock_id=s.stock_id
	LEFT JOIN supply_chain.requirement req ON req.requirement_no = isr.requirement_no
WHERE
	type in ('1','4','9','12')
	and s.stock_type in ('3','11') /*只取中转仓、海外中转仓的数据*/

/*------------------------------------------不良品出库(不良品退货,不良品换货,不良品转良)---------------------------------------------*/
/*UNION ALL
SELECT
	ts.sku_code											AS sku_code,
	ts.quality_inspection_number  						AS document_number,		'单据编号',
	th.create_date										AS documents_date,		'单据日期',
	'不良品出库'        									AS business_type,		'业务类型',
	po.is_tax											AS is_tax,				'含税',
	po.legal_person_id									AS legal_person_id,		'法人主体id',
	req.dept_id											AS dept_id,				'需求部门id',
	req.transport_type									AS transport_type,		'运输方式',
	th.create_user										AS create_user,			'制单人',
	req.supplier_id    									AS supplier_id,			'供应商id',
	ts.rejects_warehouse_id								AS warehouse_id,		'出库仓库id',
	th.operation_quantity								AS quantity,			'出库数量',
	IF (po.is_tax = '1',pd.tax_unit_price,pd.unit_price) AS unit_price, 		'单位成本',
	po.currency         								AS currency,			'币种',
	NULL												AS remark				  说明
FROM
	supply_sign.rejects_history th
LEFT JOIN supply_sign.rejects ts ON ts.id = th.reject_id
LEFT JOIN supply_sign.`storage` st ON st.quality_inspection_number = ts.quality_inspection_number
LEFT JOIN supply_sign.storage_requirement sr ON sr.storage_number = st.storage_number
LEFT JOIN supply_chain.purchase_order po ON po.purchase_order_id=ts.purchase_number
LEFT JOIN supply_chain.purchase_demand pd ON pd.purchase_order_id = po.purchase_order_id AND pd.purchase_demand_id=sr.requirement_no
LEFT JOIN supply_chain.requirement req ON req.requirement_no=pd.purchase_demand_id
WHERE
	ts.operation_type IN ('3', '7', '9') AND th.operation_type IN('3', '7', '9') and ts.rejects_warehouse_id  in (12,13,141)
AND st.rejects_number>0
*/