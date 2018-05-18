CREATE OR REPLACE VIEW aukey_report.view_sku_in AS
/*------------------------------------------普通采购--------------------------------------------------------*/
SELECT
	sri.sku_code        AS sku_code,
	sr.storage_number   AS document_number,      /*'单据编号',*/
	sr.create_time      AS document_date,        /*'单据日期',*/
	'普通采购'             AS business_type,        /*'业务类型',*/
	po.is_tax           AS is_tax,               /*'含税',*/
	po.legal_person_id  AS legal_person_id,      /*'法人主体id',*/
	rt.dept_id          AS dept_id,              /*'需求部门id',*/
	rt.transport_type   AS transport_type,       /*'运输方式',*/
    sr.create_user      AS create_user,          /*'制单人',*/
	po.supplier_id      AS supplier_id,          /*'供应商id',*/
	sri.stock_id        AS warehouse_id,         /*'入库仓库id',*/
	sr.supply_quantity  AS quantity,             /*'入库数量',*/
 	IF (po.is_tax = '1',pd.tax_unit_price,pd.unit_price) AS unit_price,/*'单位成本',*/
 	po.currency         AS currency              /*'币种'*/
FROM
  supply_sign.stock_request_inquiry sri
  JOIN supply_sign.storage_requirement sr ON sri.requirement_id = sr.requirement_no
                                          AND sr.`type` = '1'
  JOIN supply_chain.purchase_demand pd ON sr.requirement_no = pd.purchase_demand_id
  JOIN supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
  JOIN supply_chain.requirement rt ON pd.purchase_demand_id = rt.requirement_no
WHERE
    sri.stock_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)


/*------------------------------------------转SKU入库--------------------------------------------------------*/
UNION ALL

SELECT
  sr.sku_code            AS sku_code,
  '-'                   AS document_number,      /*'单据编号',*/
  sr.time_ed             AS document_date,        /*'单据日期',*/
  '转SKU入库'              AS business_type,        /*'业务类型',*/
  po.is_tax              AS is_tax,               /*'含税',*/
  po.legal_person_id     AS legal_person_id,      /*'法人主体id',*/
  rt.dept_id             AS dept_id,              /*'需求部门id',*/
  rt.transport_type      AS transport_type,       /*'运输方式',*/
  sr.create_user         AS create_user,          /*'制单人',*/
  po.supplier_id         AS supplier_id,          /*'供应商id',*/
  sr.stock_id            AS warehouse_id,         /*'入库仓库id',*/
  sr.quantity_available  AS quantity,             /*'入库数量',*/
  IF (po.is_tax = '1',pd.tax_unit_price,pd.unit_price) AS unit_price,/*'单位成本',*/
  po.currency            AS currency              /*'币种',*/
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
	
/*------------------------------------------无主调拨入库(中转调拨、盘盈入库)--------------------------------------------------------*/
UNION ALL
SELECT
  isr.sku             AS sku_code,
  isr.relation_no     AS document_number,      /*'单据编号',*/
  isr.create_date     AS document_date,        /*'单据日期',*/
  case when type='2'  then '无主调拨入库'
       when type='3'  then '无主盘盈入库'
       when type='13' then '海外调海外入库'
       when type='14' then 'FBA退货入库'  
  end                 AS business_type,        /*'业务类型',*/
  0                   AS is_tax,               /*'含税',*/
  3                   AS legal_person_id,      /*'法人主体id',*/
  IFNULL(req.dept_id,0) AS dept_id,              /*'需求部门id',*/
  null                AS transport_type,       /*'运输方式',*/
  isr.create_user     AS create_user,          /*'制单人',*/
  0                   AS supplier_id,          /*'供应商id',*/
  isr.stock_id        AS warehouse_id,         /*'入库仓库id',*/
  isr.quantity        AS quantity,             /*'入库数量',*/
  null                AS unit_price,           /*'单位成本',*/
  null                AS currency              /*'币种'*/
FROM
	supply_sign.center_idle_stock_record isr
inner join product_ms.stock s on isr.stock_id=s.stock_id
LEFT JOIN supply_chain.requirement req ON req.requirement_no = isr.requirement_no
WHERE
	type in ('2','3','13','14')
	and s.stock_type in ('3','11') /*只取中转仓、海外中转仓的数据*/

/*------------------------------------------不良品入库(采购入库)---------------------------------------------*/
/*UNION ALL
不良品入库
SELECT
	st.sku_code										AS sku_code,
	st.storage_number								AS document_number,                     '单据编号',
	st.create_date									AS document_date,                       '单据日期',
	'不良品入库'        								AS business_type,                        '业务
	po.is_tax										AS is_tax,                               '含税',
	po.legal_person_id 								AS legal_person_id,                     '法人主体id',
	req.dept_id										AS dept_id,                             '需求部门id',
	req.transport_type 								AS transport_type,                      '运输方式',
	st.create_user									AS create_user,                         '制单人',
	req.supplier_id     							AS supplier_id,                         '供应商id',
	st.warehouse_id									AS warehouse_id,                        '入库仓库id',
	st.rejects_number								AS quantity,                            '入库数量',
	IF (po.is_tax = '1',pd.tax_unit_price,pd.unit_price) AS unit_price,                     '单位成本',
	po.currency         							AS currency                             '币种',
FROM
	supply_sign.`storage` st
LEFT JOIN supply_sign.storage_requirement str ON str.storage_number=st.storage_number
LEFT JOIN supply_chain.purchase_demand pd ON pd.purchase_demand_id = str.requirement_no
LEFT JOIN supply_chain.purchase_order po ON po.purchase_order_id = pd.purchase_order_id
LEFT JOIN supply_chain.requirement req ON req.requirement_no = str.requirement_no

WHERE
	st.type='0' and st.warehouse_id IN (12,13,141)*/
/*------------------------------------------小包中转仓期初库存---------------------------------------------*/
UNION  ALL

SELECT
  a.goodsCode            AS sku_code,
  a.document_number      AS document_number,            /*'单据编号',*/
  a.create_date          AS document_date,              /*'单据日期',*/
  '期初库存'                AS business_type,              /*'业务类型',*/
  a.include_tax          AS is_tax,                     /*'含税',*/
  a.legaler     		 AS legal_person_id,            /*'法人主体id',*/
  a.department           AS dept_id,                    /*'需求部门id',*/
  a.transport_type       AS transport_type,             /*'运输方式',*/
  a.create_by            AS create_user,                /*'制单人',*/
  a.supplier_id          AS supplier_id,                /*'供应商id',*/
  a.StoreHouseID         AS warehouse_id,               /*'入库仓库id',*/
  a.EndQty               AS quantity,                   /*'入数量',*/
  a.StandardPrice        AS unit_price,                 /*'单位成本',*/
  a.currency             AS currency                    /*'币种',*/
FROM
    aukey_report.kxh_stockqty a
WHERE
    a.StoreHouseID IN (70, 71, 72, 73, 93, 94, 139, 140)
   and a.EndQty>0