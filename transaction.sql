/*
Navicat MySQL Data Transfer

Source Server         : root@localhost
Source Server Version : 80022
Source Host           : localhost:3306
Source Database       : security

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2021-04-16 08:18:16
Name: Created by Jason（蒋武）
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for transaction
-- ----------------------------
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '//TransactionID',
  `trade_id` int NOT NULL DEFAULT '0' COMMENT '//TradeID，交易ID',
  `version` int NOT NULL DEFAULT '0' COMMENT '//Version，版本号，从1开始计数',
  `security_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '//证券代码',
  `quantity` int NOT NULL DEFAULT '0' COMMENT '//股票数量',
  `action` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '//INSERT,UPDATE,CANCEL',
  `trade_direction` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '//BUY，SELL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of transaction
-- ----------------------------
INSERT INTO `transaction` VALUES ('1', '1', '1', 'REL', '50', 'INSERT', 'BUY');
INSERT INTO `transaction` VALUES ('2', '2', '1', 'ITC', '40', 'INSERT', 'SELL');
INSERT INTO `transaction` VALUES ('3', '3', '1', 'INF', '70', 'INSERT', 'BUY');
INSERT INTO `transaction` VALUES ('4', '1', '2', 'REL', '60', 'UPDATE', 'BUY');
INSERT INTO `transaction` VALUES ('5', '2', '2', 'ITC', '30', 'CANCEL', 'BUY');
INSERT INTO `transaction` VALUES ('6', '4', '1', 'INF', '20', 'INSERT', 'SELL');
