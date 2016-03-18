

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
  `PlatID` int(11) NOT NULL COMMENT '平台ID',
  `GameSvrID` int(11) NOT NULL COMMENT '游戏服务器ID',
  `GameAppID` varchar(32) NOT NULL COMMENT '游戏AppID',
  `UserCount` int(11) NOT NULL COMMENT '用户数',
  PRIMARY KEY (`CheckTime`,`AreaID`,`GameSvrID`,`GameAppID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `RegisterUsers`
-- ----------------------------
DROP TABLE IF EXISTS `RegisterUsers`;
CREATE TABLE `RegisterUsers` (
  `CheckTime` datetime NOT NULL COMMENT '查询时间',
  `AreaID` int(11) NOT NULL COMMENT '大区ID',
  `PlatID` int(11) NOT NULL COMMENT '平台ID',
  `GameSvrID` int(11) NOT NULL COMMENT '游戏服务器ID',
  `RegisterCount` int(11) NOT NULL COMMENT '注册用户数',
  PRIMARY KEY (`CheckTime`,`AreaID`,`GameSvrID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Procedure structure for `AddOnlineInfo`
-- ----------------------------
DROP PROCEDURE IF EXISTS `AddOnlineInfo`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddOnlineInfo`(IN `atime` int, IN `areaid` int, IN `platid` int, IN `gsid` int, IN `gameappid` varchar(32) character set utf8, IN `usercount` int)
BEGIN
	#Routine body goes here...
	INSERT INTO OnlineUsers(CheckTime, AreaID, PlatID, GameSvrID, GameAppID, UserCount) VALUES(FROM_UNIXTIME(atime), areaid, platid, gsid, gameappid, usercount);
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for `AddRegisterInfo`
-- ----------------------------
DROP PROCEDURE IF EXISTS `AddRegisterInfo`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddRegisterInfo`(IN `atime` int, IN `areaid` int, IN `platid` int, IN `gsid` int, IN `registercount` int)
BEGIN
	#Routine body goes here...
	INSERT INTO RegisterUsers(CheckTime, AreaID, PlatID, GameSvrID, RegisterCount) VALUES(FROM_UNIXTIME(atime), areaid, platid, gsid, registercount);
END
;;
DELIMITER ;

