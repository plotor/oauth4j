SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user_id
-- ----------------------------
DROP TABLE IF EXISTS `t_user_id`;
CREATE TABLE `t_user_id` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
