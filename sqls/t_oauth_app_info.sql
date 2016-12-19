/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : passport_oauth

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2016-12-19 23:28:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_oauth_app_info
-- ----------------------------
DROP TABLE IF EXISTS `t_oauth_app_info`;
CREATE TABLE `t_oauth_app_info` (
  `app_id` bigint(20) unsigned NOT NULL,
  `app_name` varchar(255) NOT NULL,
  `enable` tinyint(1) unsigned NOT NULL DEFAULT '0',
  `redirect_uri` varchar(4096) DEFAULT NULL,
  `scope` varchar(1024) DEFAULT NULL,
  `token_validity` int(11) NOT NULL DEFAULT '7776000' COMMENT 'seconds',
  `level` int(2) NOT NULL DEFAULT '1',
  `creator_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;
