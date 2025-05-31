/*
 Navicat Premium Data Transfer

 Source Server         : centos7-docker
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.72.148:3307
 Source Schema         : crud

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 31/05/2025 17:10:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for award
-- ----------------------------
DROP TABLE IF EXISTS `award`;
CREATE TABLE `award`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '奖项名称',
  `quantity` int(0) NULL DEFAULT NULL COMMENT '奖项数量',
  `prize` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '奖品内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '奖项信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of award
-- ----------------------------
INSERT INTO `award` VALUES (1, '特等奖', 20, '布加迪威龙一辆');
INSERT INTO `award` VALUES (2, '一等奖', 33, '宝马一辆');
INSERT INTO `award` VALUES (3, '二等奖', 39, '苹果16PM');
INSERT INTO `award` VALUES (4, '三等奖', 50, 'vivo x200u');
INSERT INTO `award` VALUES (5, '幸运奖', 69, '气球一个');

-- ----------------------------
-- Table structure for award_record
-- ----------------------------
DROP TABLE IF EXISTS `award_record`;
CREATE TABLE `award_record`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `award_id` int(0) NULL DEFAULT NULL,
  `award_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '奖项名称',
  `prize` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '奖品内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '中奖时间',
  PRIMARY KEY (`id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 87 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of award_record
-- ----------------------------
INSERT INTO `award_record` VALUES (1, 1, 'Jack', 1, '特等奖', '布加迪威龙一辆', '2025-05-26 16:13:18');
INSERT INTO `award_record` VALUES (2, 1, 'Jack', 5, '幸运奖', '气球一个', '2025-05-26 16:14:53');
INSERT INTO `award_record` VALUES (5, 2, 'Rose', 2, '一等奖', '宝马一辆', '2025-05-26 16:51:50');
INSERT INTO `award_record` VALUES (6, 3, 'Hope', 5, '幸运奖', '气球一个', '2025-05-26 17:27:14');
INSERT INTO `award_record` VALUES (56, 1, 'Jack', 5, '幸运奖', '气球一个', '2025-05-27 18:44:13');
INSERT INTO `award_record` VALUES (57, 1, 'Jack', 3, '二等奖', '苹果16PM', '2025-05-27 18:58:09');
INSERT INTO `award_record` VALUES (58, 1, 'Jack', 5, '幸运奖', '气球一个', '2025-05-27 19:34:33');
INSERT INTO `award_record` VALUES (59, 1, 'Jack', 1, '特等奖', '布加迪威龙一辆', '2025-05-27 19:46:49');
INSERT INTO `award_record` VALUES (69, 1, 'Jack', 1, '特等奖', '布加迪威龙一辆', '2025-05-30 17:40:33');
INSERT INTO `award_record` VALUES (70, 1, 'Jack', 4, '三等奖', 'vivo x200u', '2025-05-30 17:54:19');
INSERT INTO `award_record` VALUES (71, 26, 'lsy', 4, '三等奖', 'vivo x200u', '2025-05-30 17:55:18');
INSERT INTO `award_record` VALUES (72, 26, 'lsy', 3, '二等奖', '苹果16PM', '2025-05-30 17:55:22');
INSERT INTO `award_record` VALUES (73, 26, 'lsy', 3, '二等奖', '苹果16PM', '2025-05-30 17:55:25');
INSERT INTO `award_record` VALUES (74, 26, 'lsy', 5, '幸运奖', '气球一个', '2025-05-30 17:55:27');
INSERT INTO `award_record` VALUES (75, 3, 'Hope', 4, '三等奖', 'vivo x200u', '2025-05-31 09:38:20');
INSERT INTO `award_record` VALUES (87, 3, 'Hope', 3, '二等奖', '苹果16PM', '2025-05-31 16:10:01');
INSERT INTO `award_record` VALUES (88, 3, 'Hope', 5, '幸运奖', '气球一个', '2025-05-31 16:10:10');
INSERT INTO `award_record` VALUES (89, 3, 'Hope', 2, '一等奖', '宝马一辆', '2025-05-31 16:10:23');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `age` int(0) NULL DEFAULT NULL,
  `sex` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '他的话', 46, '男', '分殿', '17678673365');
INSERT INTO `user` VALUES (3, '多久', 55, '男', '山哦', '17678673365');
INSERT INTO `user` VALUES (4, '多久', 50, '男', '山哦', '17678673365');
INSERT INTO `user` VALUES (5, '多久', 55, '男', '山哦', '17678673365');
INSERT INTO `user` VALUES (6, '多久', 55, '男', '山哦', '17678673365');
INSERT INTO `user` VALUES (18, '多久', 55, '男', '山哦', '17678673365');
INSERT INTO `user` VALUES (19, '多久', 55, '男', '山哦', '17678673365');
INSERT INTO `user` VALUES (20, '多久', 55, '男', '山哦', '17678673365');
INSERT INTO `user` VALUES (21, '多久', 55, '男', '山哦', '17678673365');
INSERT INTO `user` VALUES (22, '多久', 55, '男', '山哦', '17678673365');
INSERT INTO `user` VALUES (23, '多久', 55, '男', '山哦', '17678673365');
INSERT INTO `user` VALUES (24, '1234', 0, '男', '234', '234');
INSERT INTO `user` VALUES (25, '1234', 0, '男', '234', '234');
INSERT INTO `user` VALUES (26, '1234', 0, '男', '234', '234');
INSERT INTO `user` VALUES (28, 'ew人', 32, '男', '温热天涯', '17628226732');
INSERT INTO `user` VALUES (29, 'dfr', 7, '女', 'fhh', '321431525');
INSERT INTO `user` VALUES (30, 'fdshgg', 3, '男', 'fdshg', '34324');
INSERT INTO `user` VALUES (31, 'dfg', 3, '男', 'sdfg', '34534534');
INSERT INTO `user` VALUES (32, '个大概', 5, '男', 'restg', '2344324324');
INSERT INTO `user` VALUES (33, '王鹏程', 99, '女', '撒谎法国i哦', '4243514325');
INSERT INTO `user` VALUES (34, '325', 2, '男', 'fsdhfh', 'sfdhfdh');

-- ----------------------------
-- Table structure for user_login
-- ----------------------------
DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册手机号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `status` int(0) NULL DEFAULT 1 COMMENT '使用状态（1正常 2冻结）',
  `balance` int(0) NULL DEFAULT NULL COMMENT '账户余额',
  `award` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `award_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_login
-- ----------------------------
INSERT INTO `user_login` VALUES (1, 'Jack', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112224', '2017-08-19 20:50:21', '2017-08-19 20:50:21', 1, 1000000, NULL, NULL);
INSERT INTO `user_login` VALUES (2, 'Rose', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112223', '2017-08-19 21:00:23', '2017-08-19 21:00:23', 1, 585100, NULL, NULL);
INSERT INTO `user_login` VALUES (3, 'Hope', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112222', '2017-08-19 22:37:44', '2017-08-19 22:37:44', 1, 1000000, NULL, NULL);
INSERT INTO `user_login` VALUES (4, 'Thomas', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '17701265258', '2017-08-19 23:44:45', '2017-08-19 23:44:45', 1, 1000000, NULL, NULL);
INSERT INTO `user_login` VALUES (19, 'ro', '$2a$10$.iq6DcJ41vEOT4xgvXc/BetJK4/fLVqNN2pZd4cjDqBWwgu0YXhBS', NULL, NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO `user_login` VALUES (20, 'chy', '$2a$10$6i8P7ge83TmGT2KfL416qOiSPfo4.hgO4XWayY1JhSycyeaIRHYLm', NULL, NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO `user_login` VALUES (21, 'ty', '$2a$10$kqrdlvTb8GT9VHKOCYpRJOv/kxhJN1KyMVlxD2HpffsqSTEchtDja', NULL, NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO `user_login` VALUES (22, 'er', '$2a$10$EY5fZXTEbrLwJEarw7s1Se6/Ye3bQFkY4vAvoxcd5y444je4YOs2y', NULL, NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO `user_login` VALUES (23, 'aaa', '$2a$10$sLCqExdo/pFPPRCuGinOUO6qOhMCF/17mEvfT5ZwhxtFff6kibtJ2', NULL, NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO `user_login` VALUES (24, 'rr', '$2a$10$XU5V.HAvqOv4vEO9aGrGbeFa09L0OP7Vc/ZJyaoxgALoI.n0gkDti', NULL, NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO `user_login` VALUES (25, 'Hop', '$2a$10$QB4UWYqF5T5oFXMgB/VlsuccEWGrshfSKM8Nk8i3r529Ip5sFn8P.', NULL, NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO `user_login` VALUES (26, 'lsy', '$2a$10$Z/1LA55Tq1DRbDoomdc40eJkxy0GcQf.35vLKFDIi.O0iCZnA2S2W', NULL, NULL, NULL, 1, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
