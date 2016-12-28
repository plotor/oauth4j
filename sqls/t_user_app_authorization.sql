/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : passport_oauth

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2016-12-19 23:28:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user_app_authorization
-- ----------------------------
DROP TABLE IF EXISTS `t_user_app_authorization`;
CREATE TABLE `t_user_app_authorization` (
  `app_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `scope` varchar(1024) NOT NULL,
  `scope_sign` char(32) NOT NULL,
  `token_key` varchar(128) NOT NULL,
  `refresh_token_key` varchar(128) DEFAULT NULL,
  `refresh_token_expiration_time` bigint(20) COMMENT 'milli seconds',
  `create_time` datetime NOT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `index_app_user_scope` (`app_id`,`user_id`,`scope_sign`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
