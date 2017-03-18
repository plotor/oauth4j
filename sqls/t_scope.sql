SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_scope
-- ----------------------------
DROP TABLE IF EXISTS `t_scope`;
CREATE TABLE `t_scope` (
  `id` int(11) NOT NULL,
  `name` varchar(1024) NOT NULL,
  `description` varchar(2048) NOT NULL,
  `level` int(2) NOT NULL DEFAULT '1',
  `type` int(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
