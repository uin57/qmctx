/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50615
Source Host           : localhost:3306
Source Database       : cdkeys

Target Server Type    : MYSQL
Target Server Version : 50615
File Encoding         : 65001

Date: 2014-03-07 10:38:04
*/

SET FOREIGN_KEY_CHECKS=0;

DROP DATABASE IF EXISTS `cdkey`;
CREATE DATABASE `cdkey` DEFAULT CHARACTER SET utf8;

use cdkey;
-- ----------------------------
-- Table structure for `keyinfo`
-- ----------------------------
DROP TABLE IF EXISTS `keyinfo`;
CREATE TABLE `keyinfo` (
  `kid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'cdkey内部ID',
  `bid` int(11) NOT NULL COMMENT '批次号',
  `gid` int(11) NOT NULL COMMENT '生成序号',
  `kvalue` varchar(16) NOT NULL COMMENT 'cdkey编码值',
  `usedid` int(11) DEFAULT NULL COMMENT 'CDKey被使用后的usedinfo ID',
  PRIMARY KEY (`kid`),
  UNIQUE KEY `index_cdkey` (`kvalue`) USING BTREE,
  KEY `fk_giftinfo` (`bid`,`gid`),
  KEY `fk_usedinfo_useid` (`usedid`),
  CONSTRAINT `fk_giftinfo` FOREIGN KEY (`bid`, `gid`) REFERENCES `giftinfo` (`bid`, `gid`),
  CONSTRAINT `fk_usedinfo_useid` FOREIGN KEY (`usedid`) REFERENCES `usedinfo` (`useid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of keyinfo
-- ----------------------------

-- ----------------------------
-- Table structure for `usedinfo`
-- ----------------------------
DROP TABLE IF EXISTS `usedinfo`;
CREATE TABLE `usedinfo` (
  `useid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'cdkey领取记录ID',
  `kid` int(11) NOT NULL COMMENT '领取过道具使用的cdkey ID',
  `gsid` int(11) NOT NULL COMMENT '使用CDKey兑换道具的玩家所在gameserver ID',
  `roleid` int(11) NOT NULL COMMENT '使用CDKey兑换道具的玩家的 ID',
  `rolename` varchar(128) NOT NULL COMMENT '使用CDKey兑换道具的玩家的用户名',
  `channel` varchar(64) DEFAULT NULL COMMENT '渠道名',
  `usetime` datetime NOT NULL COMMENT '使用CDKey兑换道具的时间',
  PRIMARY KEY (`useid`),
  KEY `fk_keyinfo_kid` (`kid`),
  CONSTRAINT `fk_keyinfo_kid` FOREIGN KEY (`kid`) REFERENCES `keyinfo` (`kid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of usedinfo
-- ----------------------------


-- ----------------------------
-- Table structure for `giftinfo`
-- ----------------------------
DROP TABLE IF EXISTS `giftinfo`;
CREATE TABLE `giftinfo` (
  `bid` int(11) NOT NULL COMMENT '批次号',
  `gid` int(11) NOT NULL COMMENT '生成序号',
  `items` varchar(1024) NOT NULL COMMENT 'CDKey所能兑换的道具的ID',
  `channel` varchar(64) DEFAULT NULL COMMENT '渠道名',
  `adate` date NOT NULL COMMENT 'cdkey激活日期',
  `edate` date NOT NULL COMMENT 'cdkey结束日期',
  `ctime` datetime NOT NULL COMMENT 'cdkey生成时间',
  PRIMARY KEY (`bid`,`gid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of usedinfo
-- ----------------------------

-- ----------------------------
-- Procedure structure for `exchangecdkey`
-- ----------------------------
DROP PROCEDURE IF EXISTS `exchangecdkey`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `exchangecdkey`(IN `cdkey` varchar(16),IN `inchannel` varchar(64),IN `ingsid` int,IN `inroleid` int,IN `inrolename` varchar(128) character set utf8,OUT `error` int,OUT `outitems` varchar(1024) character set utf8)
BEGIN
	#Routine body goes here...
	DECLARE vkid INT DEFAULT NULL;
	DECLARE vuid INT DEFAULT NULL;
	DECLARE vbid INT DEFAULT NULL;
	DECLARE vgid INT DEFAULT NULL;
	DECLARE vitems VARCHAR(1024) DEFAULT NULL;
	DECLARE vchannel VARCHAR(64) DEFAULT NULL;
	DECLARE vadate DATE DEFAULT NULL;
	DECLARE vedate DATE DEFAULT NULL;
	DECLARE vcnt INT DEFAULT 0;
	
	SELECT -1 INTO error;
	SELECT '' INTO outitems;
	SELECT kid, usedid, bid, gid FROM keyinfo WHERE kvalue=cdkey INTO vkid, vuid, vbid, vgid;
	IF vkid IS NOT NULL THEN
		IF vuid IS NOT NULL THEN
			SELECT -2 INTO error;
		ELSE 
			SELECT items, channel, adate, edate FROM giftinfo WHERE bid=vbid AND gid=vgid INTO vitems, vchannel, vadate, vedate;
			IF CurDate() < vadate THEN
				SELECT -9 INTO error;
			ELSE
				IF CurDate() > vedate THEN
					SELECT -10 INTO error;
				ELSE
					IF NOT IsChannelMatch(inchannel, vchannel) THEN
						SELECT -8 INTO error;
					ELSE
						SELECT COUNT(keyinfo.kid) FROM keyinfo, usedinfo WHERE usedinfo.gsid=ingsid AND usedinfo.roleid=inroleid AND keyinfo.bid=vbid AND keyinfo.kid=usedinfo.kid INTO vcnt;
						IF vcnt > 0 AND vbid > 0 THEN
							SELECT -7 INTO error;
						ELSE
							SELECT -3 INTO error;
							START TRANSACTION;
							SELECT kid, usedid FROM keyinfo WHERE kvalue=cdkey INTO vkid, vuid FOR UPDATE;
							IF vkid IS NOT NULL AND vuid IS NULL THEN
								INSERT INTO usedinfo(kid, gsid, roleid, rolename, channel, usetime) VALUES(vkid, ingsid, inroleid, inrolename, inchannel, NOW());
								UPDATE keyinfo SET usedid=LAST_INSERT_ID() WHERE kid=vkid;
								SELECT 0 INTO error;
								SELECT ifnull(vitems, '') into outitems;
							END IF;
							COMMIT;
						END IF;
					END IF;
				END IF;
			END IF;
		END IF;
	END IF;
END
;;
DELIMITER ;


-- ----------------------------
-- Function structure for `IsChannelMatch`
-- ----------------------------
DROP FUNCTION IF EXISTS `IsChannelMatch`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `IsChannelMatch`(`inchannel` varchar(64),`giftchannel` varchar(64)) RETURNS int
BEGIN
	DECLARE matched INT DEFAULT 0;
	SELECT 0 into matched;
	IF giftchannel='all' THEN
		SELECT 1 into matched;
	ELSEIF giftchannel='ios' THEN
		IF inchannel  regexp '^i[a-z0-9].+$' THEN
			SELECT 1 into matched;
		END IF;
	ELSEIF giftchannel='android' THEN
		IF inchannel regexp '^a[a-z0-9].+$' THEN
			SELECT 1 into matched;
		END IF;
	ELSEIF giftchannel='jios' THEN
		IF inchannel!='ilongtu' AND inchannel regexp '^i[a-z0-9].+$' THEN
			SELECT 1 into matched;
		END IF;
	ELSEIF giftchannel=inchannel THEN
		SELECT 1 into matched;
	END IF;
	RETURN matched;
END
;;
DELIMITER ;


