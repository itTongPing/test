CREATE OR REPLACE VIEW view_stock_transit AS
  SELECT
    rt.requirement_no,
    transfer.subject_id       AS '法人主体id',
    rt.dept_id                AS '需求部门id',
    rt.category_id            AS '品类Id',
    transfer.is_tax           AS '含税',
    rt.transport_type         AS '运输方式',
    transfer.sku              AS sku_code,
    transfer.out_warehouse_id AS '仓库id',
    transfer.box_count        AS '出库在途库存',
    transfer.update_time      AS '更新时间'
  FROM
    (SELECT
       requirement_no,
       sku,
       ts.out_warehouse_id,
       td.subject_id,
       ts.is_tax,
       SUM(box_count)      box_count,
       MAX(ts.update_time) update_time
     FROM
       supply_delivery.transfer_slip ts
       JOIN supply_delivery.transfer_detail td ON ts.transfer_id = td.transfer_id
     WHERE
       ts.transfer_status IN ('1') AND ts.data_status = '1' AND td.data_status = '1'
     GROUP BY td.requirement_no, td.sku, ts.out_warehouse_id, td.subject_id, ts.is_tax) transfer
    #    LEFT JOIN
    #supply_chain.purchase_demand pd ON transfer.requirement_no = pd.purchase_demand_id
    #    JOIN
    #supply_chain.purchase_order po ON pd.purchase_order_id = po.purchase_order_id
    JOIN
    supply_chain.requirement rt ON transfer.requirement_no = rt.requirement_no
  WHERE
    transfer.out_warehouse_id IN (8, 70, 71, 72, 73, 93, 94, 139, 140)
