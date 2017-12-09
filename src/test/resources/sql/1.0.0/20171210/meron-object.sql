/*
Navicat MySQL Data Transfer

Source Server         : localhost.MySQL
Source Server Version : 50718
Source Host           : localhost:3306
Source Database       : meeqs

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2017-12-10 00:58:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tmp
-- ----------------------------
DROP TABLE IF EXISTS `tmp`;
CREATE TABLE `tmp` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `deleteStatus` bit(1) DEFAULT b'0' COMMENT '删除标记, 0:正常, 1:已删除',
  PRIMARY KEY (`id`),
  KEY `idx_t_createtime` (`createTime`),
  KEY `idx_t_updatetime` (`updateTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板表，创建其他表时直接复制该表修改';


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `deleteStatus` bit(1) DEFAULT b'0' COMMENT '删除标记, 0:正常, 1:已删除',
  `username` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
  `mobile` varchar(20) NOT NULL COMMENT '手机号',
  `nickname` varchar(20) DEFAULT '' COMMENT '昵称',
  `realname` varchar(20) DEFAULT '' COMMENT '真实姓名',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `sex` tinyint(1) unsigned DEFAULT '3' COMMENT '性别 - 1:男, 2:女, 3:保密',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_username` (`username`),
  UNIQUE KEY `idx_user_mobile` (`mobile`),
  UNIQUE KEY `idx_user_email` (`email`),
  KEY `idx_user_createtime` (`createTime`) USING BTREE,
  KEY `idx_user_updatetime` (`updateTime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表';


-- ----------------------------
-- Procedure structure for proc_test
-- ----------------------------
DROP PROCEDURE IF EXISTS `proc_test`;
DELIMITER ;;
CREATE  PROCEDURE `proc_test`(IN in_str varchar(500), IN in_delimiter varchar(5), IN in_trim tinyint(1), INOUT io_var int(1), OUT out_total int, OUT out_result varchar(500))
BEGIN
  -- 测试存储过程输入输出
  DECLARE total INT DEFAULT 0;
  DECLARE i INT DEFAULT 0;
  DECLARE str VARCHAR(255) DEFAULT '';
  DECLARE result varchar(500) DEFAULT '';

  SET total = func_get_split_string_total(in_str,in_delimiter);
  
	WHILE i < total DO
    SET i = i + 1;
		IF in_trim = 1 THEN
			SET str = TRIM(func_get_split_string(in_str,in_delimiter,i));
	  ELSE
			SET str = func_get_split_string(in_str,in_delimiter,i);
		END IF;
		
		SET result = CONCAT(result, '#', str);
  END WHILE;
	
	SET out_total = total;
	SET out_result = result;
	SET io_var = io_var + 1;

END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for func_get_split_string
-- ----------------------------
DROP FUNCTION IF EXISTS `func_get_split_string`;
DELIMITER ;;
CREATE  FUNCTION `func_get_split_string`(f_string varchar(1000),f_delimiter varchar(5),f_order int) RETURNS varchar(255) CHARSET utf8
BEGIN
  -- 用f_delimiter分割字符串f_string，并返回分割后的第f_order个字符串
  -- f_order=0则返回空串; f_order>=子串总个数,则返回最后一个子串
  declare result varchar(255) default '';

  set result = reverse(substring_index(reverse(substring_index(f_string,f_delimiter,f_order)),f_delimiter,1));
  return result;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for func_get_split_string_total
-- ----------------------------
DROP FUNCTION IF EXISTS `func_get_split_string_total`;
DELIMITER ;;
CREATE  FUNCTION `func_get_split_string_total`(f_string varchar(1000),f_delimiter varchar(5)) RETURNS int(11)
BEGIN
  -- 获取字符串列表中字符串总个数
  return 1+(length(f_string) - length(replace(f_string,f_delimiter,'')));
END
;;
DELIMITER ;
