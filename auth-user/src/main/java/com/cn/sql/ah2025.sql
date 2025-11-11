/*
 Navicat Premium Data Transfer

 Source Server         : AYz
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : ah2025

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 06/05/2025 14:07:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `state` int NULL DEFAULT NULL COMMENT '节点类型',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单url',
  `p_id` int NULL DEFAULT NULL COMMENT '上级菜单id',
  `acl_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限码',
  `grade` int NULL DEFAULT NULL COMMENT '菜单层级',
  `is_del` int NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, 'menu-1', '系统菜单', 1, '/', -1, '2099', -1, 0);
INSERT INTO `menu` VALUES (2, 'menu-2', '系统设置', 0, '/', 1, '10', 0, 0);
INSERT INTO `menu` VALUES (3, 'menu-3', '用户管理', 0, '/user/index', 2, '1010', 1, 0);
INSERT INTO `menu` VALUES (4, 'menu-4', '角色管理', 0, '/role/index', 2, '1020', 1, 0);
INSERT INTO `menu` VALUES (5, 'menu-5', '密码修改', 0, '/user/toPasswordPage', 2, '101001', 2, 0);
INSERT INTO `menu` VALUES (6, 'menu-6', '安全退出', 0, '/signout', 2, '101002', 2, 0);
INSERT INTO `menu` VALUES (7, NULL, '用户列表查询', 0, NULL, 3, '101003', 2, 0);
INSERT INTO `menu` VALUES (8, NULL, '用户添加', 0, NULL, 3, '101004', 2, 0);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'root', '系统管理员');
INSERT INTO `role` VALUES (2, 'user', '用户');
INSERT INTO `role` VALUES (3, 'ccc', '测试');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 149 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1, 1, 1);
INSERT INTO `role_menu` VALUES (2, 1, 2);
INSERT INTO `role_menu` VALUES (3, 1, 3);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  `accountNonExpired` tinyint(1) NULL DEFAULT NULL,
  `accountNonLocked` tinyint(1) NULL DEFAULT NULL,
  `credentialsNonExpired` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'ay', '123456', '1115060927@qq.com', 1, 1, 1, 1);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 1, 1);
INSERT INTO `user_role` VALUES (2, 1, 2);

SET FOREIGN_KEY_CHECKS = 1;
