DROP PROCEDURE IF EXISTS `sync_stock_requirement_in`;

DELIMITER //
CREATE PROCEDURE sync_stock_requirement_in(IN exec_date_in DATETIME)
  BEGIN
    SELECT exec_date_in;
    SELECT @report_date_start := max(report_date)
    FROM stock_report_requirement_in; #查询上次报表日期
    SET @report_date_start = IFNULL(@report_date_start, '2017-06-28 09:50:20');
    SET @report_date_end = exec_date_in; #一般为当前时间
    #查询结果插入零时表
    INSERT INTO `stock_report_requirement_in`
    (`id`,
     `requirement_no`,
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
     `count_in`,
     `transport_type`,
     `include_tax`,
     `report_date`)
      SELECT
        uuid(),
        `requirement_no`,
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
        sum(rk) AS '入库数量',
        运输方式,
        含税,
        @report_date_end
      FROM
        view_stock_in
        a LEFT JOIN supplier2.base_corporation b ON a.法人主体id = b.corporation_id
        LEFT JOIN cas.org_group e ON a.需求部门id = e.id
        LEFT JOIN product_ms.stock f ON a.仓库id = f.stock_id
        LEFT JOIN product_ms.sku d ON a.sku_code = d.`code`
        JOIN product_ms.category h ON d.category_id = h.cate_id
      WHERE `入库时间` > @report_date_start AND `入库时间` <= @report_date_end
      GROUP BY
        `requirement_no`,
        sku_code,
        法人主体id,
        需求部门id,
        仓库id,
        含税,
        运输方式;

    #更新已经有的库存
    UPDATE stock_requirement a
      RIGHT JOIN
      stock_report_requirement_in b ON a.`requirement_no` = b.`requirement_no` AND a.`sku` = b.`sku`
    SET
      a.count_in = a.count_in + b.count_in
    WHERE
      a.id IS NOT NULL
      AND b.report_date = @report_date_end;

    #插入没有的库存
    INSERT INTO `stock_requirement`
    (`id`,
     `requirement_no`,
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
     `count_in`,
     `transport_type`,
     `include_tax`)
      SELECT
        b.`id`,
        b.`requirement_no`,
        b.`legaler`,
        b.`legaler_name`,
        b.`department`,
        b.`department_name`,
        b.`country`,
        b.`country_name`,
        b.`sku`,
        b.`sku_name`,
        b.`category`,
        b.`category_name`,
        b.`inventory`,
        b.`inventory_name`,
        b.`count_in`,
        b.`transport_type`,
        b.`include_tax`
      FROM stock_requirement a
        RIGHT JOIN
        stock_report_requirement_in b ON a.`requirement_no` = b.`requirement_no` AND a.`sku` = b.`sku`
      WHERE
        a.id IS NULL
        AND b.report_date = @report_date_end;

  END//
DELIMITER ;
