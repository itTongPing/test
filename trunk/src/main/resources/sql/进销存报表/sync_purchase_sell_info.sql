
CALL aukey_report.sync_purchase_sell_info()
use aukey_report;
DROP PROCEDURE IF EXISTS aukey_report.sync_purchase_sell_info;
CREATE PROCEDURE aukey_report.sync_purchase_sell_info()
BEGIN
TRUNCATE TABLE aukey_report.purchase_sell_report;
INSERT INTO aukey_report.purchase_sell_report(
		id,
		legaler,
		legaler_name,
		company_id,
		company_name,
		sku,
		sku_name,
		warehouse,
		warehouse_name,
		category_name,
		count_in,
		price_in,
		volume_in,
		count_out,
		price_out,
        volume_out,
		count_stock,
		price_stock,
       volume_stock,
	  count_occupy,
	  price_occupy,
    volume_occupy,
	  count_onway,
	  price_onway,
    volume_onway,
    document_date
		)
select 
UUID(),
a.legaler,
co.corporation_name legaler_name,
corg.id company_id,
corg.`name` company_name,
a.sku,
sku.`name` sku_name,
a.warehouse,
s.`name` warehouse_name,
cg.`name` category_name,
IFNULL(sum(a.quantity_in),0) count_in,
IFNULL(sum(a.quantity_in*csdr.purchase_price),0) price_in,
IFNULL(sum(a.quantity_in * csdr.height * csdr.length * csdr.width / 1000000),0) volume_in,
IFNULL(sum(a.quantity_out),0) count_out,
IFNULL(sum(a.quantity_out*csdr.purchase_price),0) price_out,
IFNULL(sum(a.quantity_out * csdr.height * csdr.length * csdr.width/ 1000000),0) volume_out,
IFNULL(sum(a.quantity_sum),0) count_stock,
IFNULL(sum(a.quantity_sum*csdr.purchase_price),0) price_stock,
IFNULL(sum(a.quantity_sum * csdr.height * csdr.length * csdr.width / 1000000),0) volume_stock,
IFNULL(sum(a.quantity_usage_sum),0) count_occupy,
IFNULL(sum(a.quantity_usage_sum*csdr.purchase_price),0) price_occupy,
IFNULL(sum(a.quantity_usage_sum * csdr.height * csdr.length * csdr.width / 1000000),0) volume_occupy,
IFNULL(sum(a.quantity_onway),0) count_onway,
IFNULL(sum(a.quantity_onway*csdr.purchase_price),0) volume_onway,
IFNULL(sum(a.quantity_onway * csdr.height * csdr.length * csdr.width / 1000000),0) volume_onway,
a.date
from
(
select 
legaler,
sku,
warehouse,
sum(
CASE
   WHEN business_type IN (
    '普通采购',
    '转SKU入库',
    '无主调拨入库',
    '无主盘盈入库',
    '海外调海外入库',
    'FBA退货入库',
    '期初库存'
   )  then quantity end
) quantity_in,
sum(
   CASE
   WHEN business_type IN (
    '调拨出库',
    '采购退货',
    '其他出库',
    '转SKU出库',
    '需求单调拨出库',
    '需求单盘亏出库',
    '无主调拨出库',
    '无主盘亏出库',
    '无主海外调拨出库',
    '海外调海外出库'
   ) THEN
    quantity
   END
  ) quantity_out,  
0 quantity_sum,
0 quantity_usage_sum,
0 quantity_onway,
DATE_FORMAT(document_date,'%Y-%m-%d') as date
from aukey_report.sku_report 
GROUP BY
legaler,
sku,
warehouse,
document_date
union all 

select 
legaler,
sku,
inventory warehouse,
0 quantity_in,
0 quantity_out,
sum(IFNULL(actual_inventory,0)) quantity_sum,
sum(IFNULL(usage_inventory,0)) quantity_usage_sum,
0 quantity_onway,
null as date
from aukey_report.stock_report sr
GROUP BY
legaler,
sku,
inventory

union all
SELECT
	3 AS legaler,
	b.sku ,
  a.warehouse_id warehouse, 
	0 quantity_in,
	0 quantity_out,
	0 quantity_sum,
	0 quantity_usage_sum,
    sum(ifnull(b.box_count,0) - ifnull(tpa.arrival_qty,0)) quantity_onway,/*由调拨的数量-部分到货的数量 = 在途数量*/
	DATE_ADD(ltst.actual_time,INTERVAL 14 DAY) as date
 FROM
  supply_delivery.transfer_slip a /*调拔主表*/
 JOIN supply_delivery.transfer_detail b ON a.transfer_id = b.transfer_id /*调拨详细表*/
 INNER JOIN aukey_tms.logistics_transport_segment lts on a.transfer_no = lts.reference_no
inner join aukey_tms.logistics_transport_segment_time ltst on lts.logistics_transport_segment_id=ltst.logistics_transport_segment_id
 LEFT JOIN supply_delivery.transfer_part_arrival tpa ON tpa.box_no = b.box_no and tpa.transfer_id =a.transfer_id and tpa.requirement_no=b.requirement_no and tpa.delivery_id=b.delivery_id 
 WHERE  a.data_status='1' and b.data_status='1'  /*未删除状态*/
and a.transfer_status in ('1','3')  /*已出库，部分到货*/
and ltst.current_state in ('1131','1231')
GROUP BY 
b.sku,
a.warehouse_id,
date
) a
LEFT JOIN aukey_report.center_sku_depart_relation as csdr on a.sku = csdr.sku_code
LEFT JOIN product_ms.sku on a.sku=sku.`code`
left join product_ms.stock s on a.warehouse = s.stock_id
left join product_ms.category cg on sku.category_id = cg.cate_id
LEFT JOIN supplier2.base_corporation co ON a.legaler = co.corporation_id
left join aukey_report.center_sku_depart_relation sdr on a.sku = sdr.sku_code
left join cas.company_org corg on sdr.department_id = corg.id
where a.warehouse not in ('12','13')
GROUP BY
a.legaler,
a.sku,
a.warehouse,
a.date;
END;