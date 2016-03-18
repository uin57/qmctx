// modified by i3k.gtool.QQMetaGen at Thu Mar 17 10:14:53 CST 2016.

package i3k;

import ket.util.Stream;

public final class TLog
{

	// gm删除道具
	public static final int AT_GM_DEL_ITEM = 1;
	// gm发放道具
	public static final int AT_GM_ADD_ITEM = 2;
	// gm修改经验
	public static final int AT_GM_MODIFY_EXP = 3;
	// gm修改钻石
	public static final int AT_GM_MODIFY_DIAMOND = 4;
	// gm修改游戏币
	public static final int AT_GM_MODIFY_MONEY = 5;
	// gm修改体力
	public static final int AT_GM_MODIFY_VIT = 6;
	// gm删除装备
	public static final int AT_GM_DEL_EQUIP = 7;
	// gm发放装备
	public static final int AT_GM_ADD_EQUIP = 8;
	// gm清除数据
	public static final int AT_GM_CLEAR_DATA = 9;
	// 升级
	public static final int AT_LEVEL_UP = 99;
	// 系统奖励
	public static final int AT_SYS_REWARD = 100;
	// 全服奖励
	public static final int AT_WORLD_REWARD = 101;
	// 点金手购买金币
	public static final int AT_BUY_MONEY = 110;
	// 购买体力
	public static final int AT_BUY_VIT = 111;
	// 购买技能点
	public static final int AT_BUY_SKILLPOINT = 112;
	// 普通黄金宝箱抽卡
	public static final int AT_GOLD_EGG = 115;
	// 10连黄金宝箱抽卡
	public static final int AT_GOLD_EGG10 = 116;
	// 普通青铜宝箱抽卡
	public static final int AT_BRONZE_EGG = 117;
	// 10连青铜宝箱抽卡
	public static final int AT_BRONZE_EGG10 = 118;
	// 魂匣抽卡
	public static final int AT_SOUL_BOX = 119;
	// 商城消费
	public static final int AT_SHOP_BUY = 120;
	// 商城刷新
	public static final int AT_SHOP_REFRESH = 121;
	// 商人永久召唤
	public static final int AT_SHOP_SUMMON = 122;
	// 战役开始
	public static final int AT_COMBAT_START = 130;
	// 战役结束
	public static final int AT_COMBAT_FINISH = 131;
	// 战役跳过
	public static final int AT_COMBAT_SKIP = 132;
	// 战役重置
	public static final int AT_COMBAT_RESET = 133;
	// 赤壁战役开始
	public static final int AT_CHIBI_START = 134;
	// 赤壁战役结束
	public static final int AT_CHIBI_FINISH = 135;
	// 八卦阵试炼开始
	public static final int AT_BAGUAZHENG_START = 136;
	// 八卦阵试炼结束
	public static final int AT_BAGUAZHENG_FINISH = 137;
	// 赤壁战役扫荡
	public static final int AT_CHIBI_SKIP = 138;
	// 八卦阵试炼扫荡
	public static final int AT_BAGUAZHENG_SKIP = 139;
	// 竞技场或炼狱场增加挑战次数
	public static final int AT_ARENA_BUY_TIMES = 140;
	// 竞技或炼狱场场清除冷却
	public static final int AT_ARENA_CLEAR_COOL = 141;
	// 竞技场或炼狱场挑战结束
	public static final int AT_ARENA_FINISH = 142;
	// 过关斩将奖励
	public static final int AT_MARCH_REWARD = 143;
	// 过关斩将结束
	public static final int AT_MARCH_FINISH = 144;
	// 领取炼狱场排行奖励
	public static final int AT_SUPER_ARENA_REWARD = 145;
	// 隐藏炼狱场战队
	public static final int AT_SUPER_ARENA_HIDE_TEAMS = 146;
	// 过关斩将扫荡
	public static final int AT_MARCH_SKIP = 147;
	// 武将首次进化
	public static final int AT_GENERAL_FIRST_EVOLUTION = 150;
	// 武将进化
	public static final int AT_GENERAL_EVOLUTION = 151;
	// 武将技能升级
	public static final int AT_GENERAL_UPGRADE_SKILL = 152;
	// 装备武将
	public static final int AT_GENERAL_SET_EQUIP = 153;
	// 装备镀金
	public static final int AT_GENERAL_EQUIP_GILD = 154;
	// 神兵强化
	public static final int AT_GENERAL_WEAPON_ENHANCE = 155;
	// 神兵进阶
	public static final int AT_GENERAL_WEAPON_ADVANCE = 156;
	// 神兵洗练
	public static final int AT_GENERAL_WEAPON_RESET = 157;
	// 武将进阶
	public static final int AT_GENERAL_ADV = 158;
	// 宠物进化
	public static final int AT_PET_EVOLUTION = 160;
	// 宠物繁殖
	public static final int AT_PET_BREED = 161;
	// 宠物升级兽栏
	public static final int AT_PET_POUND_UPGRADE = 162;
	// 宠物喂食
	public static final int AT_PET_FEED = 163;
	// 宠物产出
	public static final int AT_PET_PRODUCE = 164;
	// 变异宠物喂养
	public static final int AT_DEFORM_PET_FEED = 165;
	// 变异宠物求赞
	public static final int AT_DEFORM_PET_BEG_LIKE = 166;
	// 变异宠物点赞
	public static final int AT_DEFORM_PET_LIKE = 167;
	// 变异宠物冲级
	public static final int AT_DEFORM_PET_TRY_UPGRADE = 168;
	// 买变异宠物冲级次数
	public static final int AT_DEFORM_PET_BUY_TRY_TIMES = 169;
	// 宠物觉醒
	public static final int AT_PET_AWAKE = 170;
	// 签到
	public static final int AT_CHECKIN = 200;
	// 每日送礼
	public static final int AT_DAILY_GIFT = 201;
	// 完成任务
	public static final int AT_FINISH_TASK = 202;
	// 日常任务奖励
	public static final int AT_DAILY_TASK_REWARD = 205;
	// 装备合成
	public static final int AT_EQUIP_COMBINE = 210;
	// 道具合成
	public static final int AT_ITEM_COMBINE = 211;
	// 装备出售
	public static final int AT_EQUIP_SELL = 220;
	// 道具出售
	public static final int AT_ITEM_SELL = 221;
	// 道具使用
	public static final int AT_ITEM_USE = 222;
	// 吃包子
	public static final int AT_EAT_BUNS = 230;
	// 好友领道具
	public static final int AT_TAKE_FRIEND_ITEM = 231;
	// 好友领钱
	public static final int AT_TAKE_FRIEND_BREED_REWARD = 232;
	// 好友召回
	public static final int AT_RECALL = 233;
	// 聊天
	public static final int AT_CHAT = 234;
	// 交易中心活动买战备物资
	public static final int AT_BUY_WARGOODS = 237;
	// 交易中心活动奖励
	public static final int AT_TRADING_CENTER = 238;
	// 累积登录活动奖励
	public static final int AT_LOGIN_GIFT = 239;
	// 签到送礼活动奖励
	public static final int AT_CEHCKIN_GIFT = 240;
	// 首充送礼活动奖励
	public static final int AT_FIRST_PAY_GIFT = 241;
	// 礼包码兑换活动奖励
	public static final int AT_EXCHANGE_GIFT_PACKAGE = 242;
	// 消费送礼活动奖励
	public static final int AT_CONSUME_GIFT = 243;
	// 消费返利活动奖励
	public static final int AT_CONSUME_REBATE = 244;
	// 升级送礼活动奖励
	public static final int AT_UPGRADE_GIFT = 245;
	// 收集兑换活动奖励
	public static final int AT_EXCHANGE_GIFT = 246;
	// 集卡送礼活动奖励
	public static final int AT_GATHER_GIFT = 247;
	// 充值送礼活动奖励
	public static final int AT_PAY_GIFT = 248;
	// 限时商城礼活动奖励
	public static final int AT_LIMITED_GIFT = 249;
	// 用户充值
	public static final int AT_USER_PAY = 250;
	// 神模式充值
	public static final int AT_GOD_PAY = 251;
	// 夺城战奖励
	public static final int AT_CITY_REWARD = 260;
	// 夺城战搜索
	public static final int AT_CITY_SEARCH = 261;
	// 夺城战搜索失败返还
	public static final int AT_CITY_SEARCH_FAILED_RETURN = 262;
	// 富甲天下天天向上
	public static final int AT_RICH_DAILYTASK = 270;
	// 富甲天下掷骰子
	public static final int AT_RICH_DICE = 271;
	// 富甲天下道路事件
	public static final int AT_RICH_ROAD = 272;
	// 富甲天下矿收入
	public static final int AT_RICH_MINE = 273;
	// 富甲天下占卜屋
	public static final int AT_RICH_DIVINE = 274;
	// 富甲天下买路
	public static final int AT_RICH_BYPASS = 275;
	// 富甲天下科技树
	public static final int AT_RICH_TECHTREE = 276;
	// 富甲天下买行动力
	public static final int AT_RICH_BUYMOVEMENT = 277;
	// 富甲天下聚宝盆boss
	public static final int AT_RICH_BOSS = 278;
	// 富甲天下占领矿
	public static final int AT_RICH_OCCUPY_MINE = 279;
	// 邀请好友任务奖励
	public static final int AT_INVITATION_TASK = 290;
	// 邀请好友积分奖励
	public static final int AT_INVITATION_POINTS = 291;
	// 设置邀请好友奖励
	public static final int AT_INVITATION_REWARD = 292;
	// qq会员奖励
	public static final int AT_QQ_VIP_REWARD = 295;
	// 封赏花费
	public static final int AT_BESTOW_ITEM = 401;
	// 轮盘活动花费
	public static final int AT_DISKBET = 402;
	// 角斗场花费
	public static final int AT_DUELBUYTIME = 403;
	// 世界BOSS花费
	public static final int AT_EXPIRAT_BOSS = 404;
	// 群英会花费
	public static final int AT_HERO_BOSS = 405;
	// 宠物改名花费
	public static final int AT_PET_CHANGE_NAME = 406;
	// 官职花费
	public static final int AT_RESET_OFFICIAL = 407;
	// 官职技能花费
	public static final int AT_RESET_OFFCIAL_SKILL = 408;
	// 大富翁花费
	public static final int AT_RICH_LOTERY = 409;
	// 炼狱场清时间花费
	public static final int AT_SUPER_CLEAR = 410;
	// 角斗场激活武将花费
	public static final int AT_SURA_ACTIVE = 411;
	// 天命魂石洗练花费
	public static final int AT_GENERAL_STONE_RETPROP = 412;
	// 藏宝图发布
	public static final int AT_TREASURE_MAP_USE = 300;
	// 藏宝图挖宝
	public static final int AT_TREASURE_MAP_DIG = 301;
	// 我是大魔王竞拍花费
	public static final int AT_BEMONSTER_REGISTER = 501;
	// 我是大魔王提升队伍血量花费
	public static final int AT_BEMONSTER_UPHP = 502;
	// 创建国家
	public static final int AT_CREATE_FORCE = 1000;
	// 国家膜拜
	public static final int AT_FORCE_WORSHIP = 1001;
	// 国家膜拜奖励
	public static final int AT_FORCE_WORSHIP_REWARD = 1002;
	// 国家贡献奖励
	public static final int AT_FORCE_CONTRIBUTION_REWARD = 1003;
	// 销毁国战道具
	public static final int AT_FORCE_WAR_DESTORY_ITEMS = 1004;
	// 捐献国战道具
	public static final int AT_FORCE_WAR_CONTRIBUTE = 1005;
	// 重置国家神兽防御五行
	public static final int AT_FORCE_RESET_BEAST_DEFENSE = 1006;
	// 洗练国家神兽攻击属性
	public static final int AT_FORCE_RESET_BEAST_ATTACK = 1007;
	// 喂食国家神兽
	public static final int AT_FORCE_FEED_BEAST = 1008;
	// 修罗战斗结束
	public static final int AT_SURA_FINISH = 2001;
	// BOSS战结束
	public static final int AT_EXPIRATBOSS_FINISH = 2002;
	// 富甲天下开箱子
	public static final int AT_RICH_OPENBOX = 2003;
	// 精元注魂
	public static final int AT_SEYEN_ADD = 2004;
	// 精元散元
	public static final int AT_SEYEN_REVERT = 2005;
	// 缘分升级
	public static final int AT_RELATION_UPGRADE = 2006;
	// 缘分激活
	public static final int AT_RELATION_ACTIVATE = 2007;
	// 跨服国战战斗结束
	public static final int AT_FORCE_SWAR_FINISH = 2008;

	// 经验
	public static final int COMMON_TYPE_EXP = -10;
	// 体力
	public static final int COMMON_TYPE_VIT = -8;
	// 大富翁赤金
	public static final int COMMON_TYPE_RICH_GOLD = -7;
	// 大富翁货币点数
	public static final int COMMON_TYPE_RICH = -6;
	// 国家货币点数
	public static final int COMMON_TYPE_FORCE = -5;
	// 过关斩将货币点数
	public static final int COMMON_TYPE_MARCH = -4;
	// 竞技场货币点数
	public static final int COMMON_TYPE_ARENA = -3;
	// 钻石
	public static final int COMMON_TYPE_DIAMOND = -2;
	// 金钱
	public static final int COMMON_TYPE_MONEY = -1;
	// 道具
	public static final int COMMON_TYPE_ITEM = 0;
	// 装备
	public static final int COMMON_TYPE_EQUIP = 1;
	// 武将
	public static final int COMMON_TYPE_GENERAL = 2;
	// 宠物
	public static final int COMMON_TYPE_PET = 3;

	// 普通战役
	public static final int BATTLE_COMMON = 0;
	// 英雄战役
	public static final int BATTLE_HERO = 1;

	// 普通战斗
	public static final int COMBAT_COMMON = 0;
	// 扫荡战斗
	public static final int COMBAT_SKIP = 1;

	// 战胜
	public static final int ARENARE_WIN = 0;
	// 战败
	public static final int ARENARE_LOSE = 1;

	// 平定内乱,完成任意战役10次
	public static final int DAILYTASK_BATTLE_ANY = 1;
	// 冲锋陷阵,完成任意英雄战役3次
	public static final int DAILYTASK_BATTLE_HERO = 2;
	// 财源滚滚,购买月卡，每天领取120钻
	public static final int DAILYTASK_BATTLE_MONTHLY_CARD = 3;
	// 精湛技艺,升级任意英雄技能3次
	public static final int DAILYTASK_BATTLE_UPGRADE_GENERAL_SKILL = 4;
	// 内政寻宝,完成5次探宝
	public static final int DAILYTASK_BATTLE_EGG = 5;
	// 精兵金甲,将任意装备镀金并升级1次
	public static final int DAILYTASK_BATTLE_GILD = 6;
	// 擂台比武,在竞技场中完成3次挑战
	public static final int DAILYTASK_BATTLE_ARENA = 7;
	// 过关斩将,在过关斩将中战胜一次敌人
	public static final int DAILYTASK_BATTLE_MARCH = 8;
	// 赤壁之战,完成2次赤壁之战
	public static final int DAILYTASK_BATTLE_CHIBI = 9;
	// 试炼高手,完成3次八卦阵试炼
	public static final int DAILYTASK_BATTLE_BAGUAZHEN = 10;
	// 正能量午餐,每日12:00至14：00可以领取60点体力
	public static final int DAILYTASK_BATTLE_FREE_VIT1 = 11;
	// 正能量晚餐,每日12:00至14：00可以领取60点体力
	public static final int DAILYTASK_BATTLE_FREE_VIT2 = 12;
	// 正能量夜宵,每日12:00至14：00可以领取60点体力
	public static final int DAILYTASK_BATTLE_FREE_VIT3 = 13;
	// 点石成金,使用一次摇钱树
	public static final int DAILYTASK_BATTLE_BUY_MONEY = 14;
	// 有福同享,给好友送5个包子
	public static final int DAILYTASK_BATTLE_SEND_BUNS = 15;
	// 精彩瞬间,分享一次精彩瞬间
	public static final int DAILYTASK_BATTLE_SHOW_OFF = 16;

	// 青铜宝箱
	public static final int CHEST_BRONZE = 0;
	// 黄金宝箱
	public static final int CHEST_GOLD = 1;
	// 魂匣宝箱
	public static final int CHEST_SOUL = 2;

	// 金钱
	public static final int CHEST_PRICE_UNIT_MONEY = 1;
	// 钻石
	public static final int CHEST_PRICE_UNIT_DIAMOND = 2;

	// 金钱
	public static final int SHOP_PRICE_UNIT_MONEY = 0;
	// 钻石
	public static final int SHOP_PRICE_UNIT_DIAMOND = 1;
	// 竞技场点数
	public static final int SHOP_PRICE_UNIT_ARENA = 2;
	// 过关斩将点数
	public static final int SHOP_PRICE_UNIT_MARCH = 3;

	// 竞技场防守
	public static final int GENERALEBATTLE_ARENA_DEFENSE = 0;
	// 竞技场进攻
	public static final int GENERALEBATTLE_ARENA_ATTACK = 1;
	// 普通战役
	public static final int GENERALEBATTLE_NORMAL_BATTLE = 2;
	// 英雄战役
	public static final int GENERALEBATTLE_HERO_BATTLE = 3;
	// 过关斩将
	public static final int GENERALEBATTLE_MARCH = 4;
	// 赤壁之战
	public static final int GENERALEBATTLE_CHIBI = 5;
	// 八卦阵
	public static final int GENERALEBATTLE_BAGUAZHEN = 6;

	// 武将升级
	public static final int GENERALEVOLUTION_LEVELUP = 1;
	// 武将升星
	public static final int GENERALEVOLUTION_STARUP = 2;
	// 武将升阶
	public static final int GENERALEVOLUTION_ADVUP = 3;

	// 国家膜拜
	public static final int FORCE_WORSHIP = 1;
	// 国家膜拜领奖
	public static final int FORCE_WORSHIP_REWARD = 2;
	// 国家贡献领奖
	public static final int FORCE_CONTRIBUTION_REWARD = 3;
	// 国家频道聊天
	public static final int FORCE_CHAT = 4;
	// 国家设置宣言
	public static final int FORCE_SET_ANNOUNCEMENT = 5;
	// 国家设置图标
	public static final int FORCE_SET_ICON = 6;
	// 国家创建
	public static final int FORCE_CREATE = 7;
	// 国家拒绝加入申请
	public static final int FORCE_REFUSE = 8;
	// 国家同意加入申请
	public static final int FORCE_ACCEPT = 9;
	// 国家设置成员职位
	public static final int FORCE_SET_POSITION = 10;
	// 国家国主禅让
	public static final int FORCE_PASS_POSITION = 11;
	// 国家踢除成员
	public static final int FORCE_KICK = 12;
	// 国家退出
	public static final int FORCE_QUIT = 13;
	// 国家解散
	public static final int FORCE_DISMISS = 14;
	// 国家设置准入条件
	public static final int FORCE_SET_JOIN = 15;
	// 国主开启国战,此时iEventArg1为当前国战ID值，iEventArg2为当前国家人数
	public static final int FORCE_WAR_OPEN = 20;
	// 国家报名参战,此时iEventArg1为当前国战ID值，iEventArg2为当前国家人数
	public static final int FORCE_WAR_JOIN = 21;
	// 国战捐献物资,此时iEventArg1为当前国战ID值，iEventArg2为当前国家人数
	public static final int FORCE_WAR_CONTRIBUTE = 22;
	// 国主调整国战出战次序,此时iEventArg1为当前国战ID值，iEventArg2为当前国家人数
	public static final int FORCE_WAR_CHANGE_SEQ = 23;
	// 国战竞猜,此时iEventArg1为当前国战ID值，iEventArg2为0
	public static final int FORCE_WAR_QUIZ = 26;

	// 月卡
	public static final int PAY_LEVEL_MONTHCARD = 0;
	// 6元档位
	public static final int PAY_LEVEL_1 = 1;
	// 首冲6元双倍档位
	public static final int PAY_LEVEL_FIRST_1 = -1;
	// 30元档位
	public static final int PAY_LEVEL_2 = 2;
	// 首冲30元双倍档位
	public static final int PAY_LEVEL_FIRST_2 = -2;
	// 68元档位
	public static final int PAY_LEVEL_3 = 3;
	// 首冲68元双倍档位
	public static final int PAY_LEVEL_FIRST_3 = -3;
	// 128元档位
	public static final int PAY_LEVEL_4 = 4;
	// 首冲128元双倍档位
	public static final int PAY_LEVEL_FIRST_4 = -4;
	// 258元档位
	public static final int PAY_LEVEL_5 = 5;
	// 首冲258元双倍档位
	public static final int PAY_LEVEL_FIRST_5 = -5;
	// 648元档位
	public static final int PAY_LEVEL_6 = 6;
	// 首冲648元双倍档位
	public static final int PAY_LEVEL_FIRST_6 = -6;
	// 充值返还
	public static final int PAY_LEVEL_RETURN = 100;
	// 系统赠送
	public static final int PAY_LEVEL_PRESENT = 101;

	// 炫耀
	public static final int SNS_SHOWOFF = 1;
	// 邀请好友
	public static final int SNS_INVITE = 2;
	// 召回好友
	public static final int SNS_RECALL = 3;
	// 赠送包子
	public static final int SNS_SENDBUNS = 4;
	// 领取包子
	public static final int SNS_RECEICEBUNS = 5;
	// 赠送经验道具
	public static final int SNS_SENDEXPITEMS = 6;
	// 领取经验道具
	public static final int SNS_RECEICEEXPITEMS = 7;
	// 赠送金币
	public static final int SNS_SENDMONEY = 8;
	// 领取金币
	public static final int SNS_RECEICEMONEY = 9;

	// 兽栏升级
	public static final int PETPOUNDEVENT_UPGRADE = 1;
	// 兽栏宠物放入
	public static final int PETPOUNDEVENT_PETPUT = 2;
	// 兽栏宠物取出
	public static final int PETPOUNDEVENT_PETGET = 3;
	// 兽栏喂食宠物
	public static final int PETPOUNDEVENT_FEED = 4;
	// 赠送经验丹
	public static final int PETPOUNDEVENT_SENDEXPITEMS = 5;

	// 驻守武将, iEventType=CITYWAREVENT_GUARD时,iEventArg1取-1(驻守夺取其他玩家的城池),0(驻守城池换将),1(首次驻守1小时),2(首次驻守4小时),3(首次驻守8小时)
	public static final int CITYWAREVENT_GUARD = 1;
	// 搜索城池
	public static final int CITYWAREVENT_SEARCH = 2;
	// 攻击
	public static final int CITYWAREVENT_ATTACK = 3;
	// 领奖
	public static final int CITYWAREVENT_REWARD = 4;

	// 战役
	public static final int SEC_ROUND_BATTLE = 1;
	// 赤壁之战
	public static final int SEC_ROUND_CHIBI = 2;
	// 八卦阵
	public static final int SEC_ROUND_BAGUA = 3;
	// 过关斩将
	public static final int SEC_ROUND_MARCH = 4;

	// 普通战役
	public static final int SEC_BATTLE_COMMON = 1;
	// 英雄战役
	public static final int SEC_BATTLE_HERO = 2;

	// 草船借箭
	public static final int SEC_CHIBI_GRASSBOARD = 1;
	// 火烧赤壁
	public static final int SEC_CHIBI_CHIBI = 2;

	// 生门
	public static final int SEC_BAGUA_LIFE = 1;
	// 死门
	public static final int SEC_BAGUA_DEATH = 2;

	// 无免疫
	public static final int SEC_IMMUNE_NONE = 0;
	// 物理免疫
	public static final int SEC_IMMUNE_PHYSICAL = 1;
	// 魔法免疫
	public static final int SEC_IMMUNE_MAGIC = 2;

	// 邮件私聊
	public static final int SEC_TALK_MAIL = 0;
	// 世界聊天
	public static final int SEC_TALK_CHAT = 1;
	// 修改签名
	public static final int SEC_TALK_NAME = 10;
	// 修改公告
	public static final int SEC_TALK_ANNOUNCE = 11;



	// (可选)玩家注册
	public static class PlayerRegister
	{

		public PlayerRegister() { }

		public PlayerRegister(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                      int iPlatID, String vopenid, int iRoleID, String vClientVersion, 
		                      String vSystemSoftware, String vSystemHardware, String vTelecomOper, String vNetwork, 
		                      int iScreenWidth, int iScreenHight, float fDensity, int iRegChannel, 
		                      String vCpuHardware, int iMemory, String vGLRender, String vGLVersion, 
		                      String vDeviceId)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.vClientVersion = vClientVersion;
			this.vSystemSoftware = vSystemSoftware;
			this.vSystemHardware = vSystemHardware;
			this.vTelecomOper = vTelecomOper;
			this.vNetwork = vNetwork;
			this.iScreenWidth = iScreenWidth;
			this.iScreenHight = iScreenHight;
			this.fDensity = fDensity;
			this.iRegChannel = iRegChannel;
			this.vCpuHardware = vCpuHardware;
			this.iMemory = iMemory;
			this.vGLRender = vGLRender;
			this.vGLVersion = vGLVersion;
			this.vDeviceId = vDeviceId;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerRegister");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(vClientVersion);
			sb.append('|').append(vSystemSoftware);
			sb.append('|').append(vSystemHardware);
			sb.append('|').append(vTelecomOper);
			sb.append('|').append(vNetwork);
			sb.append('|').append(iScreenWidth);
			sb.append('|').append(iScreenHight);
			sb.append('|').append(fDensity);
			sb.append('|').append(iRegChannel);
			sb.append('|').append(vCpuHardware);
			sb.append('|').append(iMemory);
			sb.append('|').append(vGLRender);
			sb.append('|').append(vGLVersion);
			sb.append('|').append(vDeviceId);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号,循环使用
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0 /android 1
		public int iPlatID;
		// (必填)用户OPENID号
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// (可选)客户端版本
		public String vClientVersion;
		// (可选)移动终端操作系统版本
		public String vSystemSoftware;
		// (可选)移动终端机型
		public String vSystemHardware;
		// (必填)运营商
		public String vTelecomOper;
		// (可选)3G/WIFI/2G
		public String vNetwork;
		// (可选)显示屏宽度
		public int iScreenWidth;
		// (可选)显示屏高度
		public int iScreenHight;
		// (可选)像素密度
		public float fDensity;
		// (必填)注册渠道
		public int iRegChannel;
		// (可选)cpu类型|频率|核数
		public String vCpuHardware;
		// (可选)内存信息单位M
		public int iMemory;
		// (可选)opengl render信息
		public String vGLRender;
		// (可选)opengl版本信息
		public String vGLVersion;
		// (可选)设备ID
		public String vDeviceId;
	}

	// (必填)玩家登陆
	public static class PlayerLogin
	{

		public PlayerLogin() { }

		public PlayerLogin(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                   int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                   int iVipLvl, int iPay, String vClientVersion, String vSystemHardware, 
		                   String vTelecomOper, String vNetwork, int iLoginChannel, int iForceID)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.vClientVersion = vClientVersion;
			this.vSystemHardware = vSystemHardware;
			this.vTelecomOper = vTelecomOper;
			this.vNetwork = vNetwork;
			this.iLoginChannel = iLoginChannel;
			this.iForceID = iForceID;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerLogin");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(vClientVersion);
			sb.append('|').append(vSystemHardware);
			sb.append('|').append(vTelecomOper);
			sb.append('|').append(vNetwork);
			sb.append('|').append(iLoginChannel);
			sb.append('|').append(iForceID);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)用户OPENID号
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// (必填)等级
		public int iLevel;
		// (必填)角色vip等级
		public int iVipLvl;
		// (必填)角色充值数量
		public int iPay;
		// (必填)客户端版本
		public String vClientVersion;
		// (必填)移动终端机型
		public String vSystemHardware;
		// (必填)运营商
		public String vTelecomOper;
		// (必填)3G/WIFI/2G
		public String vNetwork;
		// (必填)登录渠道
		public int iLoginChannel;
		// (必填)所属国家ID，无填0
		public int iForceID;
	}

	// (必填)玩家登出
	public static class PlayerLogout
	{

		public PlayerLogout() { }

		public PlayerLogout(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                    int iPlatID, String vopenid, int iRoleID, int iOnlineTime, 
		                    int iLevel, int iVipLvl, int iPay, int iPlayerFriendsNum, 
		                    String vClientVersion, String vSystemHardware, String vTelecomOper, String vNetwork, 
		                    int iForceID)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iOnlineTime = iOnlineTime;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iPlayerFriendsNum = iPlayerFriendsNum;
			this.vClientVersion = vClientVersion;
			this.vSystemHardware = vSystemHardware;
			this.vTelecomOper = vTelecomOper;
			this.vNetwork = vNetwork;
			this.iForceID = iForceID;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerLogout");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iOnlineTime);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iPlayerFriendsNum);
			sb.append('|').append(vClientVersion);
			sb.append('|').append(vSystemHardware);
			sb.append('|').append(vTelecomOper);
			sb.append('|').append(vNetwork);
			sb.append('|').append(iForceID);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)用户OPENID号
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// (必填)本次登录在线时间(秒)
		public int iOnlineTime;
		// (必填)等级
		public int iLevel;
		// (必填)角色vip等级
		public int iVipLvl;
		// (必填)角色充值数量
		public int iPay;
		// (必填)玩家好友数量
		public int iPlayerFriendsNum;
		// (必填)客户端版本
		public String vClientVersion;
		// (必填)移动终端机型
		public String vSystemHardware;
		// (必填)运营商
		public String vTelecomOper;
		// (必填)3G/WIFI/2G
		public String vNetwork;
		// (必填)所属国家ID，无填0
		public int iForceID;
	}

	// (可选)人物升级流水表
	public static class PlayerLevelUpFlow
	{

		public PlayerLevelUpFlow() { }

		public PlayerLevelUpFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                         int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                         int iVipLvl, int iPay, int iTime)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iTime = iTime;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerLevelUpFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iTime);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色当前等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// (必填)距离上次升级所用时间(秒)
		public int iTime;
	}

	// (可选)人物通用物品记录流水表
	public static class PlayerCommonChangeFlow
	{

		public PlayerCommonChangeFlow() { }

		public PlayerCommonChangeFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                              int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                              int iVipLvl, int iPay, int iEventID, int iCommonType, 
		                              int iCommonItemID, int iChangeCount, int iAfterCount, int iArg1, 
		                              int iArg2, int iArg3, int iArg4)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iEventID = iEventID;
			this.iCommonType = iCommonType;
			this.iCommonItemID = iCommonItemID;
			this.iChangeCount = iChangeCount;
			this.iAfterCount = iAfterCount;
			this.iArg1 = iArg1;
			this.iArg2 = iArg2;
			this.iArg3 = iArg3;
			this.iArg4 = iArg4;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerCommonChangeFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iEventID);
			sb.append('|').append(iCommonType);
			sb.append('|').append(iCommonItemID);
			sb.append('|').append(iChangeCount);
			sb.append('|').append(iAfterCount);
			sb.append('|').append(iArg1);
			sb.append('|').append(iArg2);
			sb.append('|').append(iArg3);
			sb.append('|').append(iArg4);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)用于关联一次动作产生多条不同类型的道具流动日志
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 游戏事件动作时间后玩家等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 导致通用物品变化的游戏事件ID
		public int iEventID;
		// 涉及的变化的通用物品类型ID
		public int iCommonType;
		// 涉及的变化的通用物品ID (对于金钱、钻石、体力无具体ID的类型填0)
		public int iCommonItemID;
		// 涉及的变化的通用物品数量  大于0获得增加,小于0消耗减少
		public int iChangeCount;
		// 变化后的通用物品数量
		public int iAfterCount;
		// (可选)游戏事件描述参数1,根据具体iEvnetID的不同有不同的意义,如完成战役事件在此含义为战役类型
		public int iArg1;
		// (可选)游戏事件描述参数2,根据具体iEvnetID的不同有不同的意义,如完成战役事件在此含义为战役序号
		public int iArg2;
		// (可选)游戏事件描述参数3,根据具体iEvnetID的不同有不同的意义,如完成战役事件在此含义为关卡序号
		public int iArg3;
		// (可选)游戏事件描述参数4,根据具体iEvnetID的不同有不同的意义,如完成战役事件在此含义为星级
		public int iArg4;
	}

	// (可选)人物通用物品记录流水表
	public static class PlayerCommonChangeFlow2
	{

		public PlayerCommonChangeFlow2() { }

		public PlayerCommonChangeFlow2(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                               int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                               int iVipLvl, int iPay, int iEventID, String iCommonItem, 
		                               int iArg1, int iArg2, int iArg3, int iArg4)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iEventID = iEventID;
			this.iCommonItem = iCommonItem;
			this.iArg1 = iArg1;
			this.iArg2 = iArg2;
			this.iArg3 = iArg3;
			this.iArg4 = iArg4;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerCommonChangeFlow2");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iEventID);
			sb.append('|').append(iCommonItem);
			sb.append('|').append(iArg1);
			sb.append('|').append(iArg2);
			sb.append('|').append(iArg3);
			sb.append('|').append(iArg4);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)用于关联一次动作产生多条不同类型的道具流动日志
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 游戏事件动作时间后玩家等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 导致通用物品变化的游戏事件ID
		public int iEventID;
		// 涉及的变化的通用物品类型ID、物品ID (对于金钱、钻石、体力无具体ID的类型填0)，变化数目， 变化后数量，以逗号分隔，不同类型物品之间以分号分隔
		public String iCommonItem;
		// (可选)游戏事件描述参数1,根据具体iEvnetID的不同有不同的意义,如完成战役事件在此含义为战役类型
		public int iArg1;
		// (可选)游戏事件描述参数2,根据具体iEvnetID的不同有不同的意义,如完成战役事件在此含义为战役序号
		public int iArg2;
		// (可选)游戏事件描述参数3,根据具体iEvnetID的不同有不同的意义,如完成战役事件在此含义为关卡序号
		public int iArg3;
		// (可选)游戏事件描述参数4,根据具体iEvnetID的不同有不同的意义,如完成战役事件在此含义为星级
		public int iArg4;
	}

	// 战役数据流水
	public static class PlayerBattleFlow
	{

		public PlayerBattleFlow() { }

		public PlayerBattleFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                        int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                        int iVipLvl, int iPay, int iBattleType, int iBattleID, 
		                        int iCheckPointID, int iRoundStar, int iRoundTime, int iCombatType)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iBattleType = iBattleType;
			this.iBattleID = iBattleID;
			this.iCheckPointID = iCheckPointID;
			this.iRoundStar = iRoundStar;
			this.iRoundTime = iRoundTime;
			this.iCombatType = iCombatType;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerBattleFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iBattleType);
			sb.append('|').append(iBattleID);
			sb.append('|').append(iCheckPointID);
			sb.append('|').append(iRoundStar);
			sb.append('|').append(iRoundTime);
			sb.append('|').append(iCombatType);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// (必填)本局战役类型,对应BATTLETYPE,其它说明参考FAQ文档
		public int iBattleType;
		// (必填)本局战役章节序号,从1开始
		public int iBattleID;
		// (必填)本局战斗关卡序号,从1开始
		public int iCheckPointID;
		// (必填)本局星级,大于0胜利，其他失败
		public int iRoundStar;
		// (必填)对局时长(秒)
		public int iRoundTime;
		// (必填)对局战斗类型,对应COMBATTYPE
		public int iCombatType;
	}

	// 竞技场数据流水
	public static class PlayerArenaFlow
	{

		public PlayerArenaFlow() { }

		public PlayerArenaFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                       int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                       int iVipLvl, int iPay, int iRank, int iORank, 
		                       int iResult)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iRank = iRank;
			this.iORank = iORank;
			this.iResult = iResult;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerArenaFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iRank);
			sb.append('|').append(iORank);
			sb.append('|').append(iResult);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// (必填)竞技场挑战时名次
		public int iRank;
		// (必填)竞技场对手名次
		public int iORank;
		// (必填)本局战斗结果，对应ARENARESULT
		public int iResult;
	}

	// 日常任务数据流水
	public static class PlayerDailyTaskFlow
	{

		public PlayerDailyTaskFlow() { }

		public PlayerDailyTaskFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                           int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                           int iVipLvl, int iPay, int iTaskID)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iTaskID = iTaskID;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerDailyTaskFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iTaskID);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色完成日常任务时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// (必填)完成的日常任务ID
		public int iTaskID;
	}

	// 日常任务领奖数据流水
	public static class PlayerDailyTaskRewardFlow
	{

		public PlayerDailyTaskRewardFlow() { }

		public PlayerDailyTaskRewardFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                                 int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                                 int iVipLvl, int iPay, int iTaskID)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iTaskID = iTaskID;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerDailyTaskRewardFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iTaskID);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// (必填)领奖的日常任务ID
		public int iTaskID;
	}

	// 宝箱数据流水
	public static class PlayerChestFlow
	{

		public PlayerChestFlow() { }

		public PlayerChestFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                       int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                       int iVipLvl, int iPay, int iChestTpe, int iBuyCount, 
		                       int iBuyPrice, int iPriceUnit)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iChestTpe = iChestTpe;
			this.iBuyCount = iBuyCount;
			this.iBuyPrice = iBuyPrice;
			this.iPriceUnit = iPriceUnit;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerChestFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iChestTpe);
			sb.append('|').append(iBuyCount);
			sb.append('|').append(iBuyPrice);
			sb.append('|').append(iPriceUnit);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 宝箱类型,对应CHESTTYPE
		public int iChestTpe;
		// 一次购买个数
		public int iBuyCount;
		// 一次购买总价
		public int iBuyPrice;
		// 购买价格单位,对应CHESTPRICEUNITTYPE
		public int iPriceUnit;
	}

	// 商城数据流水
	public static class PlayerShopFlow
	{

		public PlayerShopFlow() { }

		public PlayerShopFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                      int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                      int iVipLvl, int iPay, int iShopType, int iBuyGoodsType, 
		                      int iBuyGoodsID, int iBuyCount, int iBuyPrice, int iPriceUnit)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iShopType = iShopType;
			this.iBuyGoodsType = iBuyGoodsType;
			this.iBuyGoodsID = iBuyGoodsID;
			this.iBuyCount = iBuyCount;
			this.iBuyPrice = iBuyPrice;
			this.iPriceUnit = iPriceUnit;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerShopFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iShopType);
			sb.append('|').append(iBuyGoodsType);
			sb.append('|').append(iBuyGoodsID);
			sb.append('|').append(iBuyCount);
			sb.append('|').append(iBuyPrice);
			sb.append('|').append(iPriceUnit);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 商城类型,1普通商城,2竞技场商城,3过关斩将商店,4和5是神秘商人,6是国家商店
		public int iShopType;
		// 购买商品类型 CommonType
		public int iBuyGoodsType;
		// 购买商品ID
		public int iBuyGoodsID;
		// 一次购买个数，当前为1
		public int iBuyCount;
		// 一次购买总价
		public int iBuyPrice;
		// 购买价格单位,例如钻石、金钱，对应SHOPPRICEUNITTYPE
		public int iPriceUnit;
	}

	// 新手引导通过记录表
	public static class NewPlayerGuideRecord
	{

		public NewPlayerGuideRecord() { }

		public NewPlayerGuideRecord(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                            int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                            int iVipLvl, int iPay, int iGuideID)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iGuideID = iGuideID;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("NewPlayerGuideRecord");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iGuideID);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 通过的新手引导节点ID
		public int iGuideID;
	}

	// 任务通过记录表
	public static class PlayerTaskRecord
	{

		public PlayerTaskRecord() { }

		public PlayerTaskRecord(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                        int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                        int iVipLvl, int iPay, int iTaskID, int iTaskSeq)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iTaskID = iTaskID;
			this.iTaskSeq = iTaskSeq;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerTaskRecord");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iTaskID);
			sb.append('|').append(iTaskSeq);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 任务线ID
		public int iTaskID;
		// 任务线节点序号
		public int iTaskSeq;
	}

	// 签到记录表
	public static class PlayerCheckinRecord
	{

		public PlayerCheckinRecord() { }

		public PlayerCheckinRecord(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                           int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                           int iVipLvl, int iPay, int iCheckinMonthID, int iCheckinDaySeq)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iCheckinMonthID = iCheckinMonthID;
			this.iCheckinDaySeq = iCheckinDaySeq;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerCheckinRecord");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iCheckinMonthID);
			sb.append('|').append(iCheckinDaySeq);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 签到月份ID
		public int iCheckinMonthID;
		// 签到当月日期序号
		public int iCheckinDaySeq;
	}

	// 赤壁之战数据流水
	public static class PlayerChibiBattleFlow
	{

		public PlayerChibiBattleFlow() { }

		public PlayerChibiBattleFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                             int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                             int iVipLvl, int iPay, int iBattleID, int iDifficultyLvl, 
		                             int iRoundStar, int iRoundTime)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iBattleID = iBattleID;
			this.iDifficultyLvl = iDifficultyLvl;
			this.iRoundStar = iRoundStar;
			this.iRoundTime = iRoundTime;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerChibiBattleFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iBattleID);
			sb.append('|').append(iDifficultyLvl);
			sb.append('|').append(iRoundStar);
			sb.append('|').append(iRoundTime);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// (必填)本局赤壁战役活动名称
		public int iBattleID;
		// (必填)本局战役难度
		public int iDifficultyLvl;
		// (必填)本局星级,大于0胜利，其他失败
		public int iRoundStar;
		// (必填)对局时长(秒)
		public int iRoundTime;
	}

	// 八卦阵数据流水
	public static class PlayerBaguazhengBattleFlow
	{

		public PlayerBaguazhengBattleFlow() { }

		public PlayerBaguazhengBattleFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                                  int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                                  int iVipLvl, int iPay, int iBattleID, int iDifficultyLvl, 
		                                  int iRoundStar, int iRoundTime)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iBattleID = iBattleID;
			this.iDifficultyLvl = iDifficultyLvl;
			this.iRoundStar = iRoundStar;
			this.iRoundTime = iRoundTime;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerBaguazhengBattleFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iBattleID);
			sb.append('|').append(iDifficultyLvl);
			sb.append('|').append(iRoundStar);
			sb.append('|').append(iRoundTime);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// (必填)本局赤壁战役活动名称
		public int iBattleID;
		// (必填)本局战役难度
		public int iDifficultyLvl;
		// (必填)本局星级,大于0胜利，其他失败
		public int iRoundStar;
		// (必填)对局时长(秒)
		public int iRoundTime;
	}

	// 装备镀金数据流水
	public static class PlayerEquipGildFlow
	{

		public PlayerEquipGildFlow() { }

		public PlayerEquipGildFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                           int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                           int iVipLvl, int iPay, int iGeneralID, int iEquipID, 
		                           int iPosID, int iEquipLvl)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iGeneralID = iGeneralID;
			this.iEquipID = iEquipID;
			this.iPosID = iPosID;
			this.iEquipLvl = iEquipLvl;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerEquipGildFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iGeneralID);
			sb.append('|').append(iEquipID);
			sb.append('|').append(iPosID);
			sb.append('|').append(iEquipLvl);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// (必填)镀金装备的武将ID
		public int iGeneralID;
		// (必填)镀金装备的ID
		public int iEquipID;
		// (必填)装备位置ID
		public int iPosID;
		// (必填)装备镀金后等级
		public int iEquipLvl;
	}

	// 过关斩将数据流水
	public static class PlayerMarchFlow
	{

		public PlayerMarchFlow() { }

		public PlayerMarchFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                       int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                       int iVipLvl, int iPay, int iStageID, int iWin, 
		                       int iRoundTime)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iStageID = iStageID;
			this.iWin = iWin;
			this.iRoundTime = iRoundTime;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerMarchFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iStageID);
			sb.append('|').append(iWin);
			sb.append('|').append(iRoundTime);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// (必填)进度ID
		public int iStageID;
		// (必填)战役输赢，1赢，0输
		public int iWin;
		// (必填)战役时长(秒)
		public int iRoundTime;
	}

	// 过关斩将奖励流水
	public static class PlayerMarchRewardFlow
	{

		public PlayerMarchRewardFlow() { }

		public PlayerMarchRewardFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                             int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                             int iVipLvl, int iPay, int iStageID, int iPoints)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iStageID = iStageID;
			this.iPoints = iPoints;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerMarchRewardFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iStageID);
			sb.append('|').append(iPoints);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// (必填)进度ID
		public int iStageID;
		// (必填)奖励点数
		public int iPoints;
	}

	// 出战武将信息状态表
	public static class PlayerBattleGeneralsState
	{

		public PlayerBattleGeneralsState() { }

		public PlayerBattleGeneralsState(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                                 int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                                 int iVipLvl, int iPay, int iBattlePower, int igeneral1ID, 
		                                 int igeneral1Lvl, int igeneral1Star, int igeneral1Adv, int igeneral2ID, 
		                                 int igeneral2Lvl, int igeneral2Star, int igeneral2Adv, int igeneral3ID, 
		                                 int igeneral3Lvl, int igeneral3Star, int igeneral3Adv, int igeneral4ID, 
		                                 int igeneral4Lvl, int igeneral4Star, int igeneral4Adv, int igeneral5ID, 
		                                 int igeneral5Lvl, int igeneral5Star, int igeneral5Adv, int ipetID, 
		                                 int ipetLvl, int ipetStar, int iBattleType)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iBattlePower = iBattlePower;
			this.igeneral1ID = igeneral1ID;
			this.igeneral1Lvl = igeneral1Lvl;
			this.igeneral1Star = igeneral1Star;
			this.igeneral1Adv = igeneral1Adv;
			this.igeneral2ID = igeneral2ID;
			this.igeneral2Lvl = igeneral2Lvl;
			this.igeneral2Star = igeneral2Star;
			this.igeneral2Adv = igeneral2Adv;
			this.igeneral3ID = igeneral3ID;
			this.igeneral3Lvl = igeneral3Lvl;
			this.igeneral3Star = igeneral3Star;
			this.igeneral3Adv = igeneral3Adv;
			this.igeneral4ID = igeneral4ID;
			this.igeneral4Lvl = igeneral4Lvl;
			this.igeneral4Star = igeneral4Star;
			this.igeneral4Adv = igeneral4Adv;
			this.igeneral5ID = igeneral5ID;
			this.igeneral5Lvl = igeneral5Lvl;
			this.igeneral5Star = igeneral5Star;
			this.igeneral5Adv = igeneral5Adv;
			this.ipetID = ipetID;
			this.ipetLvl = ipetLvl;
			this.ipetStar = ipetStar;
			this.iBattleType = iBattleType;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerBattleGeneralsState");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iBattlePower);
			sb.append('|').append(igeneral1ID);
			sb.append('|').append(igeneral1Lvl);
			sb.append('|').append(igeneral1Star);
			sb.append('|').append(igeneral1Adv);
			sb.append('|').append(igeneral2ID);
			sb.append('|').append(igeneral2Lvl);
			sb.append('|').append(igeneral2Star);
			sb.append('|').append(igeneral2Adv);
			sb.append('|').append(igeneral3ID);
			sb.append('|').append(igeneral3Lvl);
			sb.append('|').append(igeneral3Star);
			sb.append('|').append(igeneral3Adv);
			sb.append('|').append(igeneral4ID);
			sb.append('|').append(igeneral4Lvl);
			sb.append('|').append(igeneral4Star);
			sb.append('|').append(igeneral4Adv);
			sb.append('|').append(igeneral5ID);
			sb.append('|').append(igeneral5Lvl);
			sb.append('|').append(igeneral5Star);
			sb.append('|').append(igeneral5Adv);
			sb.append('|').append(ipetID);
			sb.append('|').append(ipetLvl);
			sb.append('|').append(ipetStar);
			sb.append('|').append(iBattleType);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 出战战队战力
		public int iBattlePower;
		// 出战武将1ID,无填0
		public int igeneral1ID;
		// 出战武将1等级,无填0
		public int igeneral1Lvl;
		// 出战武将1星级,无填0,星级1-5对应1星到5星
		public int igeneral1Star;
		// 出战武将1进化等级,无填0
		public int igeneral1Adv;
		// 出战武将2ID,无填0
		public int igeneral2ID;
		// 出战武将2等级,无填0
		public int igeneral2Lvl;
		// 出战武将2星级,无填0
		public int igeneral2Star;
		// 出战武将2进化等级,无填0
		public int igeneral2Adv;
		// 出战武将3ID,无填0
		public int igeneral3ID;
		// 出战武将3等级,无填0
		public int igeneral3Lvl;
		// 出战武将3星级,无填0
		public int igeneral3Star;
		// 出战武将3进化等级,无填0
		public int igeneral3Adv;
		// 出战武将4ID,无填0
		public int igeneral4ID;
		// 出战武将4等级,无填0
		public int igeneral4Lvl;
		// 出战武将4星级,无填0
		public int igeneral4Star;
		// 出战武将4进化等级,无填0
		public int igeneral4Adv;
		// 出战武将5ID,无填0
		public int igeneral5ID;
		// 出战武将5等级,无填0
		public int igeneral5Lvl;
		// 出战武将5星级,无填0
		public int igeneral5Star;
		// 出战武将5进化等级,无填0
		public int igeneral5Adv;
		// 出战宠物ID,无填0
		public int ipetID;
		// 出战宠物等级,无填0
		public int ipetLvl;
		// 出战宠物星级,无填0
		public int ipetStar;
		// 战斗类型  GENERALEBATTLETYPE
		public int iBattleType;
	}

	// 武将进化流水表
	public static class GeneralEvolutionFlow
	{

		public GeneralEvolutionFlow() { }

		public GeneralEvolutionFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                            int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                            int iVipLvl, int iPay, int igeneralID, int igeneralLvl, 
		                            int igeneralStar, int igeneralAdv, int iEvolutionType, int igeneralLvlBefore, 
		                            int igeneralStarBefore, int igeneralAdvBefore)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.igeneralID = igeneralID;
			this.igeneralLvl = igeneralLvl;
			this.igeneralStar = igeneralStar;
			this.igeneralAdv = igeneralAdv;
			this.iEvolutionType = iEvolutionType;
			this.igeneralLvlBefore = igeneralLvlBefore;
			this.igeneralStarBefore = igeneralStarBefore;
			this.igeneralAdvBefore = igeneralAdvBefore;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("GeneralEvolutionFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(igeneralID);
			sb.append('|').append(igeneralLvl);
			sb.append('|').append(igeneralStar);
			sb.append('|').append(igeneralAdv);
			sb.append('|').append(iEvolutionType);
			sb.append('|').append(igeneralLvlBefore);
			sb.append('|').append(igeneralStarBefore);
			sb.append('|').append(igeneralAdvBefore);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 进化武将的ID
		public int igeneralID;
		// 进化武将等级
		public int igeneralLvl;
		// 进化武将星级
		public int igeneralStar;
		// 进化武将升阶等级
		public int igeneralAdv;
		// 进化武将进化类型
		public int iEvolutionType;
		// 进化武将进化前等级
		public int igeneralLvlBefore;
		// 进化武将进化前星级
		public int igeneralStarBefore;
		// 进化武将进化前升阶等级
		public int igeneralAdvBefore;
	}

	// 国家活动参与流水表
	public static class PlayerForceEventFlow
	{

		public PlayerForceEventFlow() { }

		public PlayerForceEventFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                            int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                            int iVipLvl, int iPay, int iForceID, int iForceEvent, 
		                            int iEventArg1, int iEventArg2)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iForceID = iForceID;
			this.iForceEvent = iForceEvent;
			this.iEventArg1 = iEventArg1;
			this.iEventArg2 = iEventArg2;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerForceEventFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iForceID);
			sb.append('|').append(iForceEvent);
			sb.append('|').append(iEventArg1);
			sb.append('|').append(iEventArg2);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 国家ID
		public int iForceID;
		// 国家活动事件 FORCEEVENTTYPE
		public int iForceEvent;
		// 国家事件参数1
		public int iEventArg1;
		// 国家事件参数2
		public int iEventArg2;
	}

	// 国家状态表
	public static class ForceState
	{

		public ForceState() { }

		public ForceState(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                  int iPlatID, int iForceID, String sForceName, int iForceLvl, 
		                  int iForceMemberCnt, int iForceVitality, int iForceVitalityMemCnt)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.iForceID = iForceID;
			this.sForceName = sForceName;
			this.iForceLvl = iForceLvl;
			this.iForceMemberCnt = iForceMemberCnt;
			this.iForceVitality = iForceVitality;
			this.iForceVitalityMemCnt = iForceVitalityMemCnt;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("ForceState");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(iForceID);
			sb.append('|').append(sForceName);
			sb.append('|').append(iForceLvl);
			sb.append('|').append(iForceMemberCnt);
			sb.append('|').append(iForceVitality);
			sb.append('|').append(iForceVitalityMemCnt);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// 国家ID
		public int iForceID;
		// 国家名字
		public String sForceName;
		// 国家等级
		public int iForceLvl;
		// 国家成员人数
		public int iForceMemberCnt;
		// 国家活跃度
		public int iForceVitality;
		// 国家活跃度参与人数
		public int iForceVitalityMemCnt;
	}

	// 玩家充值流水表
	public static class PlayerPayFlow
	{

		public PlayerPayFlow() { }

		public PlayerPayFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                     int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                     int iVipLvl, int iPay, int iVipLvlBefore, int iPayBefore, 
		                     int iPayLvlID, int iPayAmount, int iDiamondCnt)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iVipLvlBefore = iVipLvlBefore;
			this.iPayBefore = iPayBefore;
			this.iPayLvlID = iPayLvlID;
			this.iPayAmount = iPayAmount;
			this.iDiamondCnt = iDiamondCnt;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PlayerPayFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iVipLvlBefore);
			sb.append('|').append(iPayBefore);
			sb.append('|').append(iPayLvlID);
			sb.append('|').append(iPayAmount);
			sb.append('|').append(iDiamondCnt);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值总金额(单位钻石数)
		public int iPay;
		// 充值前角色vip等级
		public int iVipLvlBefore;
		// 充值前角色充值总金额(单位钻石数)
		public int iPayBefore;
		// 充值档位 PAYLEVELTYPE
		public int iPayLvlID;
		// 充值金额(单位RMB)
		public int iPayAmount;
		// 获取到钻石数
		public int iDiamondCnt;
	}

	// (必填)SNS流水
	public static class SnsFlow
	{

		public SnsFlow() { }

		public SnsFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		               int iPlatID, int iAreaID, String vopenid, int iRoleID, 
		               int iLevel, int iVipLvl, int iPay, int iRecNum, 
		               int iCount, int iSNSType, int iSNSSubType)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.iAreaID = iAreaID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iRecNum = iRecNum;
			this.iCount = iCount;
			this.iSNSType = iSNSType;
			this.iSNSSubType = iSNSSubType;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("SnsFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(iAreaID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iRecNum);
			sb.append('|').append(iCount);
			sb.append('|').append(iSNSType);
			sb.append('|').append(iSNSSubType);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)weixn 1/qq 2
		public int iAreaID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值总金额(单位钻石数)
		public int iPay;
		// (可选)接收玩家个数
		public int iRecNum;
		// (必填)发送的数量
		public int iCount;
		// (必填)交互一级类型,其它说明参考FAQ文档
		public int iSNSType;
		// (可选)交互二级类型
		public int iSNSSubType;
	}

	// 兽栏流水表
	public static class PetPoundFlow
	{

		public PetPoundFlow() { }

		public PetPoundFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                    int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                    int iVipLvl, int iPay, int iEventType, int iEventArg)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iEventType = iEventType;
			this.iEventArg = iEventArg;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("PetPoundFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iEventType);
			sb.append('|').append(iEventArg);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 兽栏事件类型  PETPOUNDEVENTTYPE
		public int iEventType;
		// 兽栏事件参数
		public int iEventArg;
	}

	// 攻城掠地流水表
	public static class CityWarFlow
	{

		public CityWarFlow() { }

		public CityWarFlow(String vGameSvrId, String dtEventTime, int iSequence, String vGameAppid, 
		                   int iPlatID, String vopenid, int iRoleID, int iLevel, 
		                   int iVipLvl, int iPay, int iEventType, int iEventArg1, 
		                   int iEventArg2, int iEventArg3, int iEventArg4)
		{
			this.vGameSvrId = vGameSvrId;
			this.dtEventTime = dtEventTime;
			this.iSequence = iSequence;
			this.vGameAppid = vGameAppid;
			this.iPlatID = iPlatID;
			this.vopenid = vopenid;
			this.iRoleID = iRoleID;
			this.iLevel = iLevel;
			this.iVipLvl = iVipLvl;
			this.iPay = iPay;
			this.iEventType = iEventType;
			this.iEventArg1 = iEventArg1;
			this.iEventArg2 = iEventArg2;
			this.iEventArg3 = iEventArg3;
			this.iEventArg4 = iEventArg4;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("CityWarFlow");
			sb.append('|').append(vGameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(iSequence);
			sb.append('|').append(vGameAppid);
			sb.append('|').append(iPlatID);
			sb.append('|').append(vopenid);
			sb.append('|').append(iRoleID);
			sb.append('|').append(iLevel);
			sb.append('|').append(iVipLvl);
			sb.append('|').append(iPay);
			sb.append('|').append(iEventType);
			sb.append('|').append(iEventArg1);
			sb.append('|').append(iEventArg2);
			sb.append('|').append(iEventArg3);
			sb.append('|').append(iEventArg4);
			sb.append('\n');
			return sb.toString();
		}

		// (必填)登录的游戏服务器编号
		public String vGameSvrId;
		// (必填)游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (可选)同一个服务器上一段时间内唯一的事件序号
		public int iSequence;
		// (必填)游戏APPID
		public String vGameAppid;
		// (必填)ios 0/android 1
		public int iPlatID;
		// (必填)玩家
		public String vopenid;
		// 内部角色ID
		public int iRoleID;
		// 角色进行战役时等级
		public int iLevel;
		// 角色vip等级
		public int iVipLvl;
		// 角色充值数量
		public int iPay;
		// 攻城掠地事件类型  CITYWAREVENTTYPE
		public int iEventType;
		// 攻城掠地事件参数1
		public int iEventArg1;
		// 攻城掠地事件参数2
		public int iEventArg2;
		// 攻城掠地事件参数3
		public int iEventArg3;
		// 攻城掠地事件参数4
		public int iEventArg4;
	}

	// (必填)服务器状态流水，每分钟一条日志
	public static class GameSvrState
	{

		public GameSvrState() { }

		public GameSvrState(String dtEventTime, String vGameIP)
		{
			this.dtEventTime = dtEventTime;
			this.vGameIP = vGameIP;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("GameSvrState");
			sb.append('|').append(dtEventTime);
			sb.append('|').append(vGameIP);
			sb.append('\n');
			return sb.toString();
		}

		// (必填) 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// (必填)服务器IP
		public String vGameIP;
	}

	// 单局游戏开始流水表
	public static class SecRoundStartFlow
	{

		public SecRoundStartFlow() { }

		public SecRoundStartFlow(String GameSvrId, String dtEventTime, String GameAppID, String OpenID, 
		                         int PlatID, int AreaID, int BattleID, String ClientStartTime, 
		                         String UserName, int SvrUserMoney1, int SvrUserMoney2, int SvrUserPoint, 
		                         int SvrMapid, int SvrRoundType, int SvrRoundDiffculty1, int SvrRoundChiBiType, 
		                         int SvrRoundBaGuaType, int SvrRoundDiffculty2, int SvrRoundlevelNeed, int SvrRoundFloorId, 
		                         int SvrCostPoint, int SvrroundBattlePoint, int SvrUserBattlePoint, int SvrRoundScene, 
		                         int SvrEnemyCount, int SvrBossCount, int SvrEmenyImmune)
		{
			this.GameSvrId = GameSvrId;
			this.dtEventTime = dtEventTime;
			this.GameAppID = GameAppID;
			this.OpenID = OpenID;
			this.PlatID = PlatID;
			this.AreaID = AreaID;
			this.BattleID = BattleID;
			this.ClientStartTime = ClientStartTime;
			this.UserName = UserName;
			this.SvrUserMoney1 = SvrUserMoney1;
			this.SvrUserMoney2 = SvrUserMoney2;
			this.SvrUserPoint = SvrUserPoint;
			this.SvrMapid = SvrMapid;
			this.SvrRoundType = SvrRoundType;
			this.SvrRoundDiffculty1 = SvrRoundDiffculty1;
			this.SvrRoundChiBiType = SvrRoundChiBiType;
			this.SvrRoundBaGuaType = SvrRoundBaGuaType;
			this.SvrRoundDiffculty2 = SvrRoundDiffculty2;
			this.SvrRoundlevelNeed = SvrRoundlevelNeed;
			this.SvrRoundFloorId = SvrRoundFloorId;
			this.SvrCostPoint = SvrCostPoint;
			this.SvrroundBattlePoint = SvrroundBattlePoint;
			this.SvrUserBattlePoint = SvrUserBattlePoint;
			this.SvrRoundScene = SvrRoundScene;
			this.SvrEnemyCount = SvrEnemyCount;
			this.SvrBossCount = SvrBossCount;
			this.SvrEmenyImmune = SvrEmenyImmune;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("SecRoundStartFlow");
			sb.append('|').append(GameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(GameAppID);
			sb.append('|').append(OpenID);
			sb.append('|').append(PlatID);
			sb.append('|').append(AreaID);
			sb.append('|').append(BattleID);
			sb.append('|').append(ClientStartTime);
			sb.append('|').append(UserName);
			sb.append('|').append(SvrUserMoney1);
			sb.append('|').append(SvrUserMoney2);
			sb.append('|').append(SvrUserPoint);
			sb.append('|').append(SvrMapid);
			sb.append('|').append(SvrRoundType);
			sb.append('|').append(SvrRoundDiffculty1);
			sb.append('|').append(SvrRoundChiBiType);
			sb.append('|').append(SvrRoundBaGuaType);
			sb.append('|').append(SvrRoundDiffculty2);
			sb.append('|').append(SvrRoundlevelNeed);
			sb.append('|').append(SvrRoundFloorId);
			sb.append('|').append(SvrCostPoint);
			sb.append('|').append(SvrroundBattlePoint);
			sb.append('|').append(SvrUserBattlePoint);
			sb.append('|').append(SvrRoundScene);
			sb.append('|').append(SvrEnemyCount);
			sb.append('|').append(SvrBossCount);
			sb.append('|').append(SvrEmenyImmune);
			sb.append('\n');
			return sb.toString();
		}

		// 登录的游戏服务端编号
		public String GameSvrId;
		// 游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// 游戏APPID
		public String GameAppID;
		// 用户OPENID号
		public String OpenID;
		// ios 0 /android 1
		public int PlatID;
		// 微信 0 /手Q 1
		public int AreaID;
		// 本局ID，能与成功日志的iBattleID关联上
		public int BattleID;
		// 客户端本地时间，格式 YYYY-MM-DD HH:MM:SS
		public String ClientStartTime;
		// 玩家昵称，只保留中文字符、英文和数字。如果昵称中带有特殊字符，则记录时去掉，比如 张|三 记为 张三
		public String UserName;
		// 游戏币数量
		public int SvrUserMoney1;
		// 钻石数量
		public int SvrUserMoney2;
		// 当前玩家包子（体力）（不计算本局消耗）
		public int SvrUserPoint;
		// 本局关卡编号
		public int SvrMapid;
		// 本局游戏类型,1为战役，2为赤壁之战，3为八卦阵，4为过关斩将
		public int SvrRoundType;
		// 普通副本的难度,1为普通，2为精英（其它游戏类型记0）
		public int SvrRoundDiffculty1;
		// 赤壁之战类型，1为草船借箭，2为火烧赤壁（其它游戏类型记0）
		public int SvrRoundChiBiType;
		// 八卦战类型，1为生门，2为死门（其它游戏类型记0）
		public int SvrRoundBaGuaType;
		// 战役副本的难度,1为普通，2为精英（其它游戏类型记0）
		public int SvrRoundDiffculty2;
		// 关卡所需的等级
		public int SvrRoundlevelNeed;
		// 过关斩将的当前关数（其它游戏类型记0）
		public int SvrRoundFloorId;
		// 本局消耗体力值
		public int SvrCostPoint;
		// 关卡推荐战斗力
		public int SvrroundBattlePoint;
		// 玩家战斗力
		public int SvrUserBattlePoint;
		// 当局怪物总波数
		public int SvrRoundScene;
		// 敌方数量（包括小怪和boss总数）
		public int SvrEnemyCount;
		// 敌方Boss数量
		public int SvrBossCount;
		// 关卡内存在的伤害免疫，0为不免疫，1为物理免疫，2为魔法免疫
		public int SvrEmenyImmune;
	}

	// 单局游戏结束流水表
	public static class SecRoundEndFlow
	{

		public SecRoundEndFlow() { }

		public SecRoundEndFlow(String GameSvrId, String dtEventTime, String GameAppID, String OpenID, 
		                       int PlatID, int AreaID, int BattleID, String ClientEndTime, 
		                       String ClientVersion, String UserIP, int Result, int RoundEndType, 
		                       int RoundStarCount, int RoundExp, int RoundGold, int RoundScene, 
		                       int DPSTotal, String DropItemType1, String DropItemType2, String DropItemType3, 
		                       int SvrTimeCount, int ClientTimeCount, int RoundSpeed, int EnemyCount, 
		                       int EnemyKillCount, int SecPauseTimeTotal)
		{
			this.GameSvrId = GameSvrId;
			this.dtEventTime = dtEventTime;
			this.GameAppID = GameAppID;
			this.OpenID = OpenID;
			this.PlatID = PlatID;
			this.AreaID = AreaID;
			this.BattleID = BattleID;
			this.ClientEndTime = ClientEndTime;
			this.ClientVersion = ClientVersion;
			this.UserIP = UserIP;
			this.Result = Result;
			this.RoundEndType = RoundEndType;
			this.RoundStarCount = RoundStarCount;
			this.RoundExp = RoundExp;
			this.RoundGold = RoundGold;
			this.RoundScene = RoundScene;
			this.DPSTotal = DPSTotal;
			this.DropItemType1 = DropItemType1;
			this.DropItemType2 = DropItemType2;
			this.DropItemType3 = DropItemType3;
			this.SvrTimeCount = SvrTimeCount;
			this.ClientTimeCount = ClientTimeCount;
			this.RoundSpeed = RoundSpeed;
			this.EnemyCount = EnemyCount;
			this.EnemyKillCount = EnemyKillCount;
			this.SecPauseTimeTotal = SecPauseTimeTotal;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("SecRoundEndFlow");
			sb.append('|').append(GameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(GameAppID);
			sb.append('|').append(OpenID);
			sb.append('|').append(PlatID);
			sb.append('|').append(AreaID);
			sb.append('|').append(BattleID);
			sb.append('|').append(ClientEndTime);
			sb.append('|').append(ClientVersion);
			sb.append('|').append(UserIP);
			sb.append('|').append(Result);
			sb.append('|').append(RoundEndType);
			sb.append('|').append(RoundStarCount);
			sb.append('|').append(RoundExp);
			sb.append('|').append(RoundGold);
			sb.append('|').append(RoundScene);
			sb.append('|').append(DPSTotal);
			sb.append('|').append(DropItemType1);
			sb.append('|').append(DropItemType2);
			sb.append('|').append(DropItemType3);
			sb.append('|').append(SvrTimeCount);
			sb.append('|').append(ClientTimeCount);
			sb.append('|').append(RoundSpeed);
			sb.append('|').append(EnemyCount);
			sb.append('|').append(EnemyKillCount);
			sb.append('|').append(SecPauseTimeTotal);
			sb.append('\n');
			return sb.toString();
		}

		// 登录的游戏服务端编号
		public String GameSvrId;
		// 游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// 游戏APPID
		public String GameAppID;
		// 用户OPENID号
		public String OpenID;
		// ios 0 /android 1
		public int PlatID;
		// 微信 0 /手Q 1
		public int AreaID;
		// 本局ID,等于开始日志的BattleID,用于关联日志
		public int BattleID;
		// 客户端本地时间,格式 YYYY-MM-DD HH:MM:SS
		public String ClientEndTime;
		// 客户端版本号
		public String ClientVersion;
		// 玩家ip地址
		public String UserIP;
		// 服务端判定作弊情况,0为通过,其它为作弊
		public int Result;
		// 本局结束方式，0为战斗胜利，1为战斗失败
		public int RoundEndType;
		// 本局评价星数
		public int RoundStarCount;
		// 当局获得经验（各种BUFF加成后）
		public int RoundExp;
		// 当局获得游戏币
		public int RoundGold;
		// 当局通过的怪物波数
		public int RoundScene;
		// 当局造成的总伤害（排除过量伤害）
		public int DPSTotal;
		// 当局掉落卡牌id列表 |id1,id2,id3,id4...|
		public String DropItemType1;
		// 当局掉落道具id列表（包含碎片） |id1,id2,id3,id4...|
		public String DropItemType2;
		// 当局掉落装备id列表 |id1,id2,id3,id4...|
		public String DropItemType3;
		// 服务器统计单局开始到结束时间消耗（包含游戏中的暂停时间）
		public int SvrTimeCount;
		// 客户端统计单局开始到结束时间消耗（包含游戏中的暂停时间）
		public int ClientTimeCount;
		// 客户端统计的实际加速倍数
		public int RoundSpeed;
		// 客户端统计战斗出现的怪物总数量（包括小兵和boss总数）
		public int EnemyCount;
		// 客户端统计战斗死亡的怪物总数量（包括小兵和boss总数）
		public int EnemyKillCount;
		// 暂停累计时间，单位：毫秒
		public int SecPauseTimeTotal;
	}

	// 聊天信息发送日志
	public static class SecTalkFlow
	{

		public SecTalkFlow() { }

		public SecTalkFlow(String GameSvrId, String dtEventTime, String GameAppID, String OpenID, 
		                   int PlatID, int AreaID, int RoleLevel, String UserIP, 
		                   String ReceiverOpenID, int ReceiverRoleLevel, int ChatType, String TitleContents, 
		                   String ChatContents)
		{
			this.GameSvrId = GameSvrId;
			this.dtEventTime = dtEventTime;
			this.GameAppID = GameAppID;
			this.OpenID = OpenID;
			this.PlatID = PlatID;
			this.AreaID = AreaID;
			this.RoleLevel = RoleLevel;
			this.UserIP = UserIP;
			this.ReceiverOpenID = ReceiverOpenID;
			this.ReceiverRoleLevel = ReceiverRoleLevel;
			this.ChatType = ChatType;
			this.TitleContents = TitleContents;
			this.ChatContents = ChatContents;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("SecTalkFlow");
			sb.append('|').append(GameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(GameAppID);
			sb.append('|').append(OpenID);
			sb.append('|').append(PlatID);
			sb.append('|').append(AreaID);
			sb.append('|').append(RoleLevel);
			sb.append('|').append(UserIP);
			sb.append('|').append(ReceiverOpenID);
			sb.append('|').append(ReceiverRoleLevel);
			sb.append('|').append(ChatType);
			sb.append('|').append(TitleContents);
			sb.append('|').append(ChatContents);
			sb.append('\n');
			return sb.toString();
		}

		// 登录的游戏服务端编号
		public String GameSvrId;
		// 游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// 游戏APPID
		public String GameAppID;
		// 发送方用户OPENID号
		public String OpenID;
		// ios 0 /android 1
		public int PlatID;
		// 微信 0 /手Q 1
		public int AreaID;
		// 发送方角色等级
		public int RoleLevel;
		// 发送信息玩家ip地址
		public String UserIP;
		// 接收方角色OPENID号
		public String ReceiverOpenID;
		// 接收方角色等级
		public int ReceiverRoleLevel;
		// 信息类型，0 为邮件（私聊），1 为小喇叭，10 为修改签名，11 为公会公告
		public int ChatType;
		// 邮件标题内容，目前最多能发送单条50字节的信息内容
		public String TitleContents;
		// 信息内容，目前最多能发送单条512字节的信息内容
		public String ChatContents;
	}

	// idip命令流水
	public static class IDIPCmdFlow
	{

		public IDIPCmdFlow() { }

		public IDIPCmdFlow(String GameSvrId, String dtEventTime, String GameAppID, String Openid, 
		                   int PlatID, int iArea, int item_type, int item_id, 
		                   int item_num, String serialnum, int source_id, String Cmd)
		{
			this.GameSvrId = GameSvrId;
			this.dtEventTime = dtEventTime;
			this.GameAppID = GameAppID;
			this.Openid = Openid;
			this.PlatID = PlatID;
			this.iArea = iArea;
			this.item_type = item_type;
			this.item_id = item_id;
			this.item_num = item_num;
			this.serialnum = serialnum;
			this.source_id = source_id;
			this.Cmd = Cmd;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder("");
			sb.append("IDIPCmdFlow");
			sb.append('|').append(GameSvrId);
			sb.append('|').append(dtEventTime);
			sb.append('|').append(GameAppID);
			sb.append('|').append(Openid);
			sb.append('|').append(PlatID);
			sb.append('|').append(iArea);
			sb.append('|').append(item_type);
			sb.append('|').append(item_id);
			sb.append('|').append(item_num);
			sb.append('|').append(serialnum);
			sb.append('|').append(source_id);
			sb.append('|').append(Cmd);
			sb.append('\n');
			return sb.toString();
		}

		// 登录的游戏服务端编号
		public String GameSvrId;
		// 游戏事件的时间, 格式 YYYY-MM-DD HH:MM:SS
		public String dtEventTime;
		// 游戏APPID
		public String GameAppID;
		// 发送方用户OPENID号
		public String Openid;
		// ios 0 /android 1
		public int PlatID;
		// 大区号
		public int iArea;
		// 道具类型
		public int item_type;
		// 道具id
		public int item_id;
		// 道具数量
		public int item_num;
		// 流水号
		public String serialnum;
		// 渠道号
		public int source_id;
		// 命令字
		public String Cmd;
	}

}
