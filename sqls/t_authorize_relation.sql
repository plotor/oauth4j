SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user_app_authorization
-- ----------------------------
DROP TABLE IF EXISTS `t_authorize_relation`;
CREATE TABLE `t_authorize_relation` (
    `user_id`                       BIGINT(20)    NOT NULL,
    `app_id`                        BIGINT(20)    NOT NULL,
    `scope`                         VARCHAR(1024) NOT NULL,
    `scope_sign`                    CHAR(32)      NOT NULL,
    `token_key`                     VARCHAR(128)  NOT NULL,
    `refresh_token_key`             VARCHAR(128)       DEFAULT NULL,
    `refresh_token_expiration_time` BIGINT(20)         DEFAULT NULL COMMENT 'milli seconds',
    `create_time`                   DATETIME      NOT NULL,
    `update_time`                   TIMESTAMP     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `index_app_user_scope` (`user_id`, `app_id`, `scope_sign`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
