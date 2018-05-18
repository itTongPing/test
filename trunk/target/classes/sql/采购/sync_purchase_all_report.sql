DROP PROCEDURE
IF EXISTS `sync_purchase_all_report`;

DELIMITER //


CREATE DEFINER=`aukey_report`@`10.1.1.%` PROCEDURE sync_purchase_all_report ()
BEGIN
	-- SELECT @stock_date_start:=max(stock_date) FROM purchase_report;
	-- set @stock_date_end=date_add(@stock_date_start, interval 1 day);
	--
	-- #select count(1) from view_purchase_storage a LEFT JOIN supplier2.base_corporation b ON a.法人主体id = b.corporation_id LEFT JOIN supplier2.strict_supplier c ON a.供应商id = c.supplier_id LEFT JOIN sku d ON a.sku_code = d.`code` LEFT JOIN org_group e ON a.采购部门id = e.id LEFT JOIN stock_ms f ON a.入库仓库id = f.stock_id LEFT JOIN user g ON a.采购员id = g.user_id JOIN category h ON a.品类id=h.cate_id where `采购时间` >=@documents_date_start and `采购时间`<@documents_date_end;
	--
	-- 		INSERT   INTO purchase_report(id,legaler,legaler_name,department,department_name,category,category_name,sku,sku_name,stock,stock_name,supplier,supplier_name,purchase_no,purchase_date,purchase_count,purchase_sum,price_wihtout_tax,price_tax,purchase_money_type,buyer,buyer_name,
	--     stock_number,stock_date,stock_count,stock_sum,bill_name,bill_unit,brand,version,include_tax,no_bill_count,pay_type,pay_type_name)
	--
	--  SELECT
	--         UUID(), 法人主体id,b.corporation_name,采购部门id,e.`name`,品类id,h.`name`,sku_code,d.`name`,入库仓库id,f.`name`,供应商id,c.name,采购单号,采购时间,采购数量,采购金额, 不含税单价,含税单价,币种,采购员id,g.name,入库单号,入库日期,
	--
	--        入库数量, 入库金额,开票品名,开票单位,品牌,型号,含税,入库数量,结算方式id,base_payment_method.method
	--
	--   from view_purchase_storage
	--   a LEFT JOIN supplier2.base_corporation b ON a.法人主体id = b.corporation_id LEFT JOIN supplier2.strict_supplier c ON a.供应商id = c.supplier_id LEFT JOIN product_ms.sku d ON a.sku_code = d.`code` LEFT JOIN org_group e ON a.采购部门id = e.id LEFT JOIN stock_ms f ON a.入库仓库id = f.stock_id LEFT JOIN user g ON a.采购员id = g.user_id LEFT JOIN category h ON a.品类id=h.cate_id
	--   LEFT JOIN supplier2.base_payment_method  ON a.结算方式id = base_payment_method.payment_method_id
	-- where `入库日期` >@stock_date_start and `入库日期`<=@stock_date_end;
	INSERT INTO purchase_report (
		id,
		legaler,
		legaler_name,
		department,
		department_name,
		category,
		category_name,
		sku,
		sku_name,
		stock,
		stock_name,
		supplier,
		supplier_name,
		purchase_no,
		purchase_date,
		purchase_count,
		purchase_sum,
		price_wihtout_tax,
		price_tax,
		purchase_money_type,
		buyer,
		buyer_name,
		stock_number,
		stock_date,
		stock_count,
		stock_sum,
		bill_name,
		bill_unit,
		brand,
		version,
		include_tax,
		no_bill_count,
		pay_type,
		pay_type_name,
		dept_id_xq,
		dept_name_xq
	) SELECT
		UUID(),
		法人主体id,
		b.corporation_name,
		采购部门id,
		e.`name`,
		品类id,
		h.`name`,
		sku_code,
		d.`name`,
		入库仓库id,
		f.`name`,
		供应商id,
		c. NAME,
		采购单号,
		采购时间,
		采购数量,
		采购金额,
		不含税单价,
		含税单价,
		币种,
		采购员id,
		g. NAME,
		入库单号,
		入库日期,
		入库数量,
		入库金额,
		开票品名,
		开票单位,
		品牌,
		型号,
		含税,
		入库数量,
		结算方式id,
		base_payment_method.method,
		需求部门id,
		e2. NAME
	FROM
		view_purchase_storage a
	LEFT JOIN supplier2.base_corporation b ON a.法人主体id = b.corporation_id
	LEFT JOIN supplier2.strict_supplier c ON a.供应商id = c.supplier_id
	LEFT JOIN product_ms.sku d ON a.sku_code = d.`code`
	LEFT JOIN cas.org_group e ON a.采购部门id = e.id
	LEFT JOIN product_ms.stock f ON a.入库仓库id = f.stock_id
	LEFT JOIN cas.`user` g ON a.采购员id = g.user_id
	LEFT JOIN product_ms.category h ON a.品类id = h.cate_id
	LEFT JOIN supplier2.base_payment_method ON a.结算方式id = base_payment_method.payment_method_id
	LEFT JOIN cas.org_group e2 ON a.需求部门id = e2.id
	WHERE
		`入库单号` IN (
			SELECT
				st.storage_number
			FROM
				purchase_report pr
			RIGHT JOIN supply_sign.`storage` st ON pr.stock_number = st.storage_number
			WHERE
				pr.stock_number IS NULL
		) ; #新增退货数据，以行的形式增加
		INSERT INTO purchase_report (
			id,
			legaler,
			legaler_name,
			department,
			department_name,
			category,
			category_name,
			sku,
			sku_name,
			stock,
			stock_name,
			supplier,
			supplier_name,
			purchase_no,
			purchase_date,
			purchase_count,
			purchase_sum,
			price_wihtout_tax,
			price_tax,
			purchase_money_type,
			buyer,
			buyer_name,
			stock_number,
			stock_date,
			stock_count,
			stock_sum,
			bill_name,
			bill_unit,
			brand,
			VERSION,
			include_tax,
			no_bill_count,
			pay_type,
			pay_type_name,
			dept_id_xq,
			dept_name_xq
		) SELECT
			UUID(),
			法人主体id,
			b.corporation_name,
			采购部门id,
			e.`name`,
			品类id,
			h.`name`,
			sku_code,
			d.`name`,
			退货仓库,
			f.`name`,
			供应商id,
			c. NAME,
			采购单号,
			采购时间,
			采购数量,
			采购金额,
			不含税单价,
			含税单价,
			币种,
			采购员id,
			g. NAME,
			退货单号,
			退货日期,
			退货数量,
			退货金额,
			开票品名,
			开票单位,
			品牌,
			型号,
			含税,
			退货数量,
			结算方式id,
			base_payment_method.method,
			需求部门id,
			e2. NAME
		FROM
			view_purchase_return a
		LEFT JOIN supplier2.base_corporation b ON a.法人主体id = b.corporation_id
		LEFT JOIN supplier2.strict_supplier c ON a.供应商id = c.supplier_id
		LEFT JOIN product_ms.sku d ON a.sku_code = d.`code`
		LEFT JOIN cas.org_group e ON a.采购部门id = e.id
		LEFT JOIN product_ms.stock f ON a.退货仓库 = f.stock_id
		LEFT JOIN cas.USER g ON a.采购员id = g.user_id
		LEFT JOIN product_ms.category h ON a.品类id = h.cate_id
		LEFT JOIN supplier2.base_payment_method ON a.结算方式id = base_payment_method.payment_method_id
		LEFT JOIN cas.org_group e2 ON a.需求部门id = e2.id
		WHERE
			`退货单号` IN (
				SELECT
					st.purchase_return_id
				FROM
					purchase_report pr
				RIGHT JOIN supply_chain.purchase_return_order st ON pr.stock_number = st.purchase_return_id
				WHERE
					pr.stock_number IS NULL
			) ;
		END//
DELIMITER ;