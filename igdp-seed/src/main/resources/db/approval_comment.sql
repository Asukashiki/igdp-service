/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : localhost:3306
 Source Schema         : igdp_seed

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 15/12/2025 10:30:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for approval_comment
-- ----------------------------
DROP TABLE IF EXISTS `approval_comment`;
CREATE TABLE `approval_comment`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `data_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据ID（关联的数据记录ID）',
  `approver_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人ID',
  `approver_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批人名称',
  `comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '审批意见',
  `approval_time` datetime NULL DEFAULT NULL COMMENT '审批时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批意见表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;