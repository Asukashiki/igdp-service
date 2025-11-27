-- 种子信息服务模块数据库建表语句
-- 适配RuoYi-V4.7.5框架

-- =====================================================
-- 1. 种子推广信息表
-- =====================================================
CREATE TABLE `seed_promotion_info` (
  `promotion_id` varchar(32) NOT NULL COMMENT '推广信息唯一标识',
  `enterprise_id` varchar(32) NOT NULL COMMENT '关联企业唯一标识',
  `title` varchar(100) NOT NULL COMMENT '推广标题',
  `video_url` varchar(255) NOT NULL COMMENT '宣传视频存储路径',
  `promotion_summary` varchar(1000) DEFAULT NULL COMMENT '推广摘要',
  `recommended_varieties` varchar(255) NOT NULL COMMENT '推荐品种',
  `publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `valid_period` int(3) NOT NULL COMMENT '有效期（天）',
  `share_link` varchar(255) NOT NULL COMMENT '分享链接',
  `visit_count` int(10) NOT NULL DEFAULT 0 COMMENT '访问次数',
  PRIMARY KEY (`promotion_id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  CONSTRAINT `fk_seed_promotion_enterprise` FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise_info` (`enterprise_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='种子推广信息表';

-- =====================================================
-- 2. 种子品种查询记录表
-- =====================================================
CREATE TABLE `seed_variety_query_record` (
  `query_id` varchar(32) NOT NULL COMMENT '查询记录唯一标识',
  `query_keyword` varchar(100) NOT NULL COMMENT '查询关键词',
  `query_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '查询时间',
  `ip_address` varchar(50) DEFAULT NULL COMMENT '访问IP地址',
  `query_result_count` int(5) NOT NULL DEFAULT 0 COMMENT '查询结果数量',
  `viewed_publish_id` varchar(32) DEFAULT NULL COMMENT '查看的发布ID',
  PRIMARY KEY (`query_id`),
  KEY `idx_viewed_publish_id` (`viewed_publish_id`),
  CONSTRAINT `fk_seed_query_publish` FOREIGN KEY (`viewed_publish_id`) REFERENCES `variety_publish` (`publish_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='种子品种查询记录表';
