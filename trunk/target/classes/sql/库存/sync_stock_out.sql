DROP PROCEDURE IF EXISTS `sync_stock_out`;
CREATE  PROCEDURE `sync_stock_out`(IN exec_date_in DATETIME)
BEGIN
    #暂时做不了增量
    TRUNCATE TABLE stock_report_record_out;
    UPDATE stock_report
    SET count_out = 0,sum_out=0;
    #只能全量
    SELECT @report_date_start := max(report_date)
    FROM stock_report_record_out; #查询上次报表日期
    SET @report_date_start = IFNULL(@report_date_start, date_add(now(), INTERVAL -2 MONTH));
    SET @report_date_end = exec_date_in; #一般为当前时间
    SELECT @report_date_end;
    #查询结果插入零时表
    INSERT INTO `stock_report_record_out`
    (`id`,
     `legaler`,
     `legaler_name`,
     `department`,
     `department_name`,
     `country`,
     `country_name`,
     `sku`,
     `sku_name`,
     `category`,
     `category_name`,
     `inventory`,
     `inventory_name`,
	  sum_money_out,
     `count_out`,
     `transport_type`,
     `include_tax`,
     `report_date`)
      SELECT
        uuid(),
        法人主体id,
        b.corporation_name,
        需求部门id,
        e.`name`,
        -1      AS `country`,
        '-'     AS `country_name`,
        sku_code,
        d.`name`,
        d.category_id,
        h.`name`,
        仓库id,
        f.`name`,
		SUM(ck*price),
        sum(ck) AS '出库数量',
        运输方式,
        含税,
        @report_date_end
      FROM
        view_stock_out
        a LEFT JOIN supplier2.base_corporation b ON a.法人主体id = b.corporation_id
        LEFT JOIN cas.org_group e ON a.需求部门id = e.id
        LEFT JOIN product_ms.stock f ON a.仓库id = f.stock_id
        LEFT JOIN product_ms.sku d ON a.sku_code = d.`code`
        left JOIN product_ms.category h ON d.category_id = h.cate_id
      GROUP BY
        sku_code,
        法人主体id,
        需求部门id,
        仓库id,
        含税,
        运输方式;
    #更新已经有的库存
    UPDATE stock_report a
      RIGHT JOIN
      stock_report_record_out b ON a.`legaler` = b.`legaler`
                                   AND a.`department` = b.`department`
                                   AND a.`sku` = b.`sku`
                                   AND a.`inventory` = b.`inventory`
                                   AND a.`include_tax` = b.`include_tax`
                                   AND ifnull(a.`transport_type`,'') = ifnull(b.`transport_type`,'')
    SET
      a.count_out = a.count_out + b.count_out,
      a.sum_out = a.sum_out + b.sum_money_out
    WHERE
      a.id IS NOT NULL
      AND b.report_date = @report_date_end;
  END