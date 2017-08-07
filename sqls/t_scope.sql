SET FOREIGN_KEY_CHECKS = 0;

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
