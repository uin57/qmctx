

SET FOREIGN_KEY_CHECKS=0;

DROP DATABASE IF EXISTS `Online`;
CREATE DATABASE `Online` DEFAULT CHARACTER SET utf8;

use Online;
-- ----------------------------
-- Table structure for `OnlineUsers`
-- ----------------------------
DROP TABLE IF EXISTS `OnlineUsers`;
CREATE TABLE `OnlineUsers` (
  `CheckTime` datetime NOT NULL COMMENT '查询时间',
  `AreaID` int(11) NOT NULL COMMENT '大区ID',
  `GameSvrID` int(11) NOT NULL COMMENT '游戏服务器ID',
  `PlatID` int(11) NOT NULL COMMENT '平台ID',
  `UserCount` int(11) NOT NULL COMMENT '用户数',
  KEY `fk_time` (`CheckTime`),
  KEY `fk_area` (`AreaID`),
  KEY `fk_gs` (`GameSvrID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `RegisterUsers`
-- ----------------------------
DROP TABLE IF EXISTS `RegisterUsers`;
CREATE TABLE `RegisterUsers` (
  `CheckTime` datetime NOT NULL COMMENT '查询时间',
  `AreaID` int(11) NOT NULL COMMENT '大区ID',
  `GameSvrID` int(11) NOT NULL COMMENT '游戏服务器ID',
  `PlatID` int(11) NOT NULL COMMENT '平台ID',
  `RegisterCount` int(11) NOT NULL COMMENT '注册用户数',
  PRIMARY KEY `fk_gs` (`GameSvrID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Procedure structure for `AddOnlineInfo`
-- ----------------------------
DROP PROCEDURE IF EXISTS `AddOnlineInfo`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddOnlineInfo`(IN `atime` int, IN `areaid` int, IN `gsid` int, IN `platid` int, IN `usercount` int)
BEGIN
	#Routine body goes here...
	INSERT INTO OnlineUsers VALUES(FROM_UNIXTIME(atime), areaid, gsid, platid, usercount);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `AddGameInfo`
-- ----------------------------
DROP PROCEDURE IF EXISTS `AddGameInfo`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddGameInfo`(IN `atime` int, IN `areaid` int, IN `gsid` int, IN `platid` int, IN `usercount` int, IN `registercount` int)
BEGIN
	#Routine body goes here...
	INSERT INTO OnlineUsers VALUES(FROM_UNIXTIME(atime), areaid, gsid, platid, usercount);
	REPLACE INTO RegisterUsers VALUES(FROM_UNIXTIME(atime), areaid, gsid, platid, registercount);
END
;;
DELIMITER ;

