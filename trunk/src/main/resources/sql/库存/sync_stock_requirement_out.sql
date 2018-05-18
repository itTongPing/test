DROP PROCEDURE IF EXISTS `sync_stock_requirement_out`;

DELIMITER //
CREATE PROCEDURE sync_stock_requirement_out(IN exec_date_in DATETIME)
  BEGIN
    #暂时做不了增量
    TRUNCATE TABLE stock_report_requirement_out;
    UPDATE stock_requirement
    SET count_out = 0;
    #只能全量

    SELECT @report_date_start := max(report_date)
    FROM stock_report_requirement_out; #查询上次报表日期
    SET @report_date_start = IFNULL(@report_date_start, date_add(now(), INTERVAL -2 MONTH));
    SET @report_date_end = exec_date_in; #一般为当前时间
    SELECT @report_date_end;
    #查询结果插入零时表
    INSERT INTO `stock_report_requirement_out`
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
     `count_out`,
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
        JOIN product_ms.category h ON d.category_id = h.cate_id
      WHERE `出库时间` > @report_date_start AND `出库时间` <= @report_date_end
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
      stock_report_requirement_out b ON a.`requirement_no` = b.`requirement_no` AND a.`sku` = b.`sku`
    SET
      a.count_out = a.count_out + b.count_out
    WHERE
      a.id IS NOT NULL
      AND b.report_date = @report_date_end;

  END//
DELIMITER ;
