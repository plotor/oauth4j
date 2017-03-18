SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_oauth_app_info
-- ----------------------------
DROP TABLE IF EXISTS `t_oauth_app_info`;
CREATE TABLE `t_oauth_app_info` (
  `app_id` bigint(20) unsigned NOT NULL,
  `app_name` varchar(255) NOT NULL,
  `logo` varchar(1024) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `enable` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT 'app enable status, 0:disable, 1:enable',
  `redirect_uri` varchar(4096) DEFAULT NULL,
  `cancel_redirect_uri` varchar(4096) DEFAULT NULL,
  `scope` varchar(1024) DEFAULT NULL,
  `token_validity` int(11) NOT NULL DEFAULT '7776000' COMMENT 'seconds',
  `secret` varchar(255) NOT NULL,
  `level` int(2) NOT NULL DEFAULT '1',
  `creator_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
