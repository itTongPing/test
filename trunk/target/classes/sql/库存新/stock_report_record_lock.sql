CREATE TABLE `stock_report_record_lock` (
  `id` varchar(36) NOT NULL COMMENT '��������',
  `legaler` int(11) DEFAULT NULL COMMENT '��������',
  `legaler_name` varchar(128) DEFAULT NULL,
  `department` int(11) DEFAULT NULL COMMENT '������',
  `department_name` varchar(64) DEFAULT NULL,
  `country` int(11) DEFAULT NULL COMMENT '����',
  `country_name` varchar(64) DEFAULT NULL,
  `sku` varchar(64) DEFAULT NULL COMMENT 'sku',
  `sku_name` varchar(256) DEFAULT 'NA' COMMENT 'sku����',
  `category` int(11) DEFAULT NULL COMMENT 'Ʒ��',
  `category_name` varchar(128) DEFAULT NULL,
  `inventory` int(11) DEFAULT NULL,
  `inventory_name` varchar(128) DEFAULT NULL COMMENT '�ֿ�����',
  `usage_inventory` int(11) DEFAULT NULL COMMENT 'ռ������',
  `transport_type` varchar(64) DEFAULT NULL COMMENT '���䷽ʽ',
  `include_tax` varchar(4) DEFAULT NULL COMMENT '�Ƿ�˰',
  `report_date` datetime DEFAULT NULL COMMENT '����ʱ��',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

