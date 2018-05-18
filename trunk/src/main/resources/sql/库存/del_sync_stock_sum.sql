DROP PROCEDURE IF EXISTS `sync_stock_sum`;

DELIMITER //
CREATE PROCEDURE sync_stock_sum(IN exec_date_in DATETIME)
  BEGIN
    SELECT @report_date_start := max(report_date)
    FROM stock_report_record_sum; #查询上次报表日期
    SET @report_date_start = IFNULL(@report_date_start, date_add(now(), INTERVAL -1 MONTH));
    SET @report_date_end = exec_date_in; #一般为当前时间
    SELECT @report_date_end;

    INSERT INTO `stock_report_record_sum_bak`
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
     `usage_inventory`,
     `way_inventory`,
     `available_inventory`,
     `actual_inventory`,
     `sum`,
     `transport_type`,
     `include_tax`,
     `total_area`,
     `report_date`)
      SELECT
        `id`,
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
        `usage_inventory`,
        `way_inventory`,
        `available_inventory`,
        `actual_inventory`,
        `sum`,
        `transport_type`,
        `include_tax`,
        `total_area`,
        `report_date`
      FROM `stock_report_record_sum`;

    TRUNCATE TABLE stock_report_record_sum;

    #查询结果插入零时表
    INSERT INTO `stock_report_record_sum`
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
     `usage_inventory`,
     `way_inventory`,
     `available_inventory`,
     `actual_inventory`,
     `sum`,
     `transport_type`,
     `include_tax`,
     `total_area`,
     `report_date`)
      SELECT
        uuid(),
        法人主体id,
        b.corporation_name,
        需求部门id,
        e.`name`,
        -1          AS `country`,
        '-'         AS `country_name`,
        sku_code,
        d.`name`,
        d.category_id,
        h.`name`,
        仓库id,
        f.`name`,
        sum(`占用库存`) AS '占用库存',
        sum(`在途`)   AS '在途',
        sum(`可用库存`) AS '可用库存',
        sum(`实际库存`) AS '实际库存',
        sum(`金额`)   AS '金额',
        运输方式,
        含税,
        sum(`体积`)   AS '体积',
        @report_date_end
      FROM
        view_stock_sum
        a LEFT JOIN supplier2.base_corporation b ON a.法人主体id = b.corporation_id
        LEFT JOIN org_group e ON a.需求部门id = e.id
        LEFT JOIN stock_ms f ON a.仓库id = f.stock_id
        LEFT JOIN sku d ON a.sku_code = d.`code`
        JOIN category h ON d.category_id = h.cate_id
      WHERE `更新时间` > '2017-06-24 16:53:07'
      #AND `更新时间` <= @report_date_end
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
      stock_report_record_sum b ON a.`legaler` = b.`legaler`
                                   AND a.`department` = b.`department`
                                   AND a.`sku` = b.`sku`
                                   AND a.`inventory` = b.`inventory`
                                   AND a.`include_tax` = b.`include_tax`
                                   AND (a.`transport_type` = b.`transport_type` OR
                                        (a.`transport_type` IS NULL AND b.`transport_type` IS NULL))
    SET
      a.`usage_inventory`     = b.`usage_inventory`,
      a.`way_inventory`       = b.`way_inventory`,
      a.`available_inventory` = b.`available_inventory`,
      a.`actual_inventory`    = b.`actual_inventory`,
      a.`sum`                 = b.`sum`,
      a.`total_area`          = b.`total_area`

    WHERE
      a.id IS NOT NULL
      AND b.report_date = @report_date_end;
  END//
DELIMITER ;
