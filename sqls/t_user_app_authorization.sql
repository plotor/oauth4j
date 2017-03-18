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
  `refresh_token_expiration_time` bigint(20) DEFAULT NULL COMMENT 'milli seconds',
  `create_time` datetime NOT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `index_app_user_scope` (`app_id`,`user_id`,`scope_sign`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
