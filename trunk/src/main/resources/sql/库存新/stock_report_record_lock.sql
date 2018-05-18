CREATE TABLE `stock_report_record_lock` (
  `id` varchar(36) NOT NULL COMMENT '递增主健',
  `legaler` int(11) DEFAULT NULL COMMENT '法人主体',
  `legaler_name` varchar(128) DEFAULT NULL,
  `department` int(11) DEFAULT NULL COMMENT '需求部门',
  `department_name` varchar(64) DEFAULT NULL,
  `country` int(11) DEFAULT NULL COMMENT '国家',
  `country_name` varchar(64) DEFAULT NULL,
  `sku` varchar(64) DEFAULT NULL COMMENT 'sku',
  `sku_name` varchar(256) DEFAULT 'NA' COMMENT 'sku名称',
  `category` int(11) DEFAULT NULL COMMENT '品类',
  `category_name` varchar(128) DEFAULT NULL,
  `inventory` int(11) DEFAULT NULL,
  `inventory_name` varchar(128) DEFAULT NULL COMMENT '仓库名称',
  `usage_inventory` int(11) DEFAULT NULL COMMENT '占用数量',
  `transport_type` varchar(64) DEFAULT NULL COMMENT '运输方式',
  `include_tax` varchar(4) DEFAULT NULL COMMENT '是否含税',
  `report_date` datetime DEFAULT NULL COMMENT '报表时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

