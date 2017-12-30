SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_app_info
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
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of t_app_info
-- ----------------------------
INSERT INTO `t_app_info` VALUES ('2882303761517520186', '指间生活',
                                                        'https://github.com/interdigital-life/interdigital-life.github.io/blob/master/img/google-logo.jpg?raw=true',
                                                        '0', 'http://www.zhenchao.com', NULL, '1 2 4 5', '7776000', 'empty', '1', '888888',
                                 '2017-01-21 15:53:37', '2017-02-28 22:33:27');

-- ----------------------------
-- Table structure for t_authorize_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_authorize_relation`;
CREATE TABLE `t_authorize_relation` (
  `app_id`                        BIGINT(20)    NOT NULL,
  `user_id`                       BIGINT(20)    NOT NULL,
  `scope`                         VARCHAR(1024) NOT NULL,
  `scope_sign`                    CHAR(32)      NOT NULL,
  `token_key`                     VARCHAR(128)  NOT NULL,
  `refresh_token_key`             VARCHAR(128)       DEFAULT NULL,
  `refresh_token_expiration_time` BIGINT(20)         DEFAULT NULL COMMENT 'milli seconds',
  `create_time`                   DATETIME      NOT NULL,
  `update_time`                   TIMESTAMP     NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `index_app_user_scope` (`app_id`, `user_id`, `scope_sign`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of t_authorize_relation
-- ----------------------------
INSERT INTO `t_authorize_relation` VALUES
  ('2882303761517520186', '100000', '1 4', '024c64526d52b0cfdf038dfa', '8SGZuDfkcFzla1C3tHnbZIqcHH2fif43pvV1vH1gZhZT2X5jRBPoOh2FFuzk4xlV',
   'dfHyplbtEBjPGCKPPga2tzvW6GbSPf3oLCBa6bwsEo30krpZ0PXdB8Wg0EDC4WhT', '31536000', '2017-02-11 18:25:02', '2017-03-18 23:50:45');
INSERT INTO `t_authorize_relation` VALUES ('2882303761517520186', '100000', '1 2 4 5', 'e8b66581ac324fc39f031262',
                                           'fRgEdJsq6rR8TuH84mXkTZzAv0q6KyvQz7BVkkHYyln5FOVccPp4Cz4VuDcz9cfr',
                                           'P6VT5HmnXg5G0wIAMdeC5kI7TU8PWK7qD5oTfb2ycQSQG9kTQQsbJGsicORjVisX', '31536000',
                                           '2017-02-25 14:33:10', '2017-03-18 23:58:40');

-- ----------------------------
-- Table structure for t_scope
-- ----------------------------
DROP TABLE IF EXISTS `t_scope`;
CREATE TABLE `t_scope` (
  `id`          INT(11)       NOT NULL,
  `name`        VARCHAR(1024) NOT NULL,
  `description` VARCHAR(2048) NOT NULL,
  `level`       INT(2)        NOT NULL DEFAULT '1',
  `type`        INT(2)        NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of t_scope
-- ----------------------------
INSERT INTO `t_scope` VALUES ('1', '获取您的基本信息（昵称、头像）', '获取您的基本信息（昵称、头像）', '1', '1');
INSERT INTO `t_scope` VALUES ('2', '获取您的手机号码', '获取您的手机号码', '2', '1');
INSERT INTO `t_scope` VALUES ('3', '获取您的电子邮箱', '获取您的电子邮箱', '2', '1');
INSERT INTO `t_scope` VALUES ('4', '获取您在本APP的唯一标识（open id）', '获取您在本APP的唯一标识（open id）', '1', '1');
INSERT INTO `t_scope` VALUES ('5', '获取您在本公司的唯一标识（union id）', '获取您在本公司的唯一标识（union id）', '2', '1');

-- ----------------------------
-- Table structure for t_user_id
-- ----------------------------
DROP TABLE IF EXISTS `t_user_id`;
CREATE TABLE `t_user_id` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Records of t_user_id
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info` (
  `id`       BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'user id',
  `username` VARCHAR(20)         NOT NULL,
  `password` VARCHAR(32)         NOT NULL,
  `age`      INT(10) UNSIGNED    NOT NULL DEFAULT '0',
  `phone`    VARCHAR(20)                  DEFAULT NULL,
  `email`    VARCHAR(1024)                DEFAULT NULL,
  `avatar`   VARCHAR(1024)                DEFAULT NULL COMMENT 'avatar url',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE = InnoDB AUTO_INCREMENT = 100001 DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of t_user_info
-- ----------------------------
INSERT INTO `t_user_info` VALUES ('100000', 'zhenchao', 'Bm+8geOCBsXZIM+FsWgaYQ==', '18', '13212345678', NULL,
                                  'https://github.com/ZhenchaoWang/zhenchaowang.github.io/blob/master/img/zhenchao_100.jpg?raw=true');
