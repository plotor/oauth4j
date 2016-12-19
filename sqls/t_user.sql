/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : passport_oauth

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2016-12-19 23:28:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'user id',
  `username` varchar(20) NOT NULL COMMENT 'username',
  `password` varchar(32) NOT NULL,
  `age` int(10) unsigned NOT NULL DEFAULT '0',
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(1024) DEFAULT NULL,
  `avatar` varchar(1024) DEFAULT NULL COMMENT 'avatar url',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
