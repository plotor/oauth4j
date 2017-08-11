SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_oauth_app_info
-- ----------------------------
DROP TABLE IF EXISTS `t_app_info`;
CREATE TABLE `t_app_info` (
  `app_id`              BIGINT(20) UNSIGNED                 NOT NULL,
  `app_name`            VARCHAR(255)                        NOT NULL,
  `logo`                VARCHAR(1024) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `enable`              TINYINT(1) UNSIGNED                 NOT NULL DEFAULT '0' COMMENT 'app enable status, 0:disable, 1:enable',
  `redirect_uri`        VARCHAR(4096)                                DEFAULT NULL,
  `cancel_redirect_uri` VARCHAR(4096)                                DEFAULT NULL,
  `scope`               VARCHAR(1024)                                DEFAULT NULL,
  `token_validity`      INT(11)                             NOT NULL DEFAULT '7776000' COMMENT 'seconds',
  `secret`              VARCHAR(255)                        NOT NULL,
  `level`               INT(2)                              NOT NULL DEFAULT '1',
  `creator_id`          BIGINT(20)                          NOT NULL,
  `create_time`         DATETIME                            NOT NULL,
  `update_time`         TIMESTAMP                           NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`app_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
