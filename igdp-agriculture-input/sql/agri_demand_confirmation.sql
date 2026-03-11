-- =============================================
-- 需求确认日志表
-- =============================================

-- ----------------------------
-- 需求确认日志表
-- ----------------------------
DROP TABLE IF EXISTS `agri_demand_confirmation`;
CREATE TABLE `agri_demand_confirmation` (
  `confirmation_id` varchar(50) NOT NULL COMMENT '确认ID',
  `from_actor` varchar(50) NOT NULL COMMENT '发送方（DA/Coop/Union/District/Zone/Region）',
  `to_actor` varchar(50) NOT NULL COMMENT '接收方',
  `reference_id` varchar(50) NOT NULL COMMENT '关联需求记录ID',
  `confirmation_type` varchar(50) NOT NULL COMMENT '确认类型(Receipt-接收确认/Delivery-发送确认)',
  `confirmed_time` datetime NOT NULL COMMENT '确认时间',
  `create_people` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_people` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志(0-正常/2-删除)',
  PRIMARY KEY (`confirmation_id`),
  KEY `idx_from_actor` (`from_actor`),
  KEY `idx_to_actor` (`to_actor`),
  KEY `idx_reference_id` (`reference_id`),
  KEY `idx_confirmation_type` (`confirmation_type`),
  KEY `idx_confirmed_time` (`confirmed_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='需求确认日志表';
