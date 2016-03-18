package i3k.gs;


import i3k.DBMail;
import i3k.SBean;
import i3k.SBean.DiskBetItemsCFGS;
import i3k.SBean.DropEntryNew;
import i3k.SBean.DropTableEntryNew;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Stack;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;
import java.io.File;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;





import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;



public class GameActivities 
{
	//static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	GameServer gs;
	String configDir = "activities";
	public final static int NEWUSERSYSMAIL = -1;
	public final static int LOGINGIFTMAIL = -2;
	public final static int FREEPHYSICAL = -3;
	
	public final static int DOUBLEDROP = 1;
	public final static int EXTRADROP = 2;
	public final static int CHECKINGIFT = 3;
	public final static int FIRSTPAYGIFT = 4;
	public final static int CONSUMEGIFT = 5;
	public final static int CONSUMEREBATE = 6;
	public final static int UPGRADEGIFT = 7;
	public final static int EXCHANGEGIFT = 8;
	public final static int GATHERGIFT = 9;
	public final static int PAYGIFT = 10;
	public final static int LIMITEDSHOP = 11;
	public final static int LOGINGIFT = 12;
	public final static int TRADINGCENTER = 13;
	public final static int DISKBET = 14;
	public final static int DISKGIFT = 15;
	
	public final static int RECHARGERANK = 16;
	public final static int RECHARGEGIFT = 17;
	
	public final static int GIFTPACKAGE = 100;
	
	public final static int CONFIG_VALID = 0;
	public final static int CONFIG_TIME_INVALID = -1;
	public final static int CONFIG_MAIL_TITLE_INVALID = -2;
	public final static int CONFIG_MAIL_CONTENT_INVALID = -3;
	public final static int CONFIG_BRIEF_TITLE_INVALID = -4;
	public final static int CONFIG_DETAIL_CONTENT_INVALID = -5;
	public final static int CONFIG_VIT_INVALID = -6;
	public final static int CONFIG_RREE_VIT_TIME_INVALID = -7;
	public final static int CONFIG_RREE_VIT_TIME_SPAN_OVERLAP = -8;
	public final static int CONFIG_DROP_ITEM_COUNT_INVALID = -9;
	public final static int CONFIG_DROP_PROBABILITY_INVALID = -10;
	public final static int CONFIG_CHECKIN_DAYS_INVALID = -11;
	public final static int CONFIG_LEVEL_REQ_INVALID = -12;
	public final static int CONFIG_GIFT_COUNT_INVALID = -13;
	public final static int CONFIG_LIMITEDTIME_INVALID = -14;
	public final static int CONFIG_EXTRA_GIFT_COUNT_INVALID = -15;
	public final static int CONFIG_PROPS_OR_GIFT_COUNT_INVALID = -16;
	public final static int CONFIG_PROPS_ID_DUPLICATION_INVALID = -17;
	public final static int CONFIG_GENERALS_OR_GIFT_COUNT_INVALID = -18;
	public final static int CONFIG_GENERALS_ID_DUPLICATION_INVALID = -19;
	public final static int CONFIG_GEN_PACKAGE_GIFT_TIME_INVALID = -20;
	public final static int CONFIG_GEN_PACKAGE_GIFT_TITLE_INVALID = -21;
	public final static int CONFIG_GEN_PACKAGE_GIFT_CONTENT_INVALID = -22;
	public final static int CONFIG_GEN_PACKAGE_GIFT_EMPTY_INVALID = -23;
	public final static int CONFIG_GIFT_MAX_BUY_TIMES_INVALID = -24;
	public final static int CONFIG_GIFT_COST_INVALID = -25;
	public final static int CONFIG_LOGIN_DAYS_REQ_INVALID = -26;
	public final static int CONFIG_TRADINGCENTER_BUY_END_TIME_INVALID = -27;
	public final static int CONFIG_TRADINGCENTER_RETURN_TIME_INVALID = -28;
	public final static int CONFIG_TRADINGCENTER_GOODS_COUNT_INVALID = -29;
	public final static int CONFIG_TRADINGCENTER_GOODS_TYPE_INVALID = -30;
	public final static int CONFIG_TRADINGCENTER_GOODS_PRICE_INVALID = -31;
	public final static int CONFIG_TRADINGCENTER_GOODS_MAX_BUY_COUNT_INVALID = -32;
	public final static int CONFIG_TRADINGCENTER_GOODS_RETURN_VALUE_INVALID = -33;
	public final static int CONFIG_DISK_BET_VALUE_INVALID = -34;
	
	Map<Integer, ActivityImpl<? extends ActivityConfig>> activities = new TreeMap<Integer, ActivityImpl<? extends ActivityConfig>>();
	public GameActivities(GameServer gs)
	{
		this.gs = gs;
	}
	
	public void init(String cfgDir)
	{
		configDir = cfgDir;
		activities.put(NEWUSERSYSMAIL, new NewUserSysMail());
		activities.put(LOGINGIFTMAIL, new LoginGiftMail());
		activities.put(DOUBLEDROP, new DoubleDrop());
		activities.put(EXTRADROP, new ExtraDrop());
		activities.put(CHECKINGIFT, new CheckinGift());
		activities.put(FIRSTPAYGIFT, new FirstPayGift());
		activities.put(CONSUMEGIFT, new ConsumeGift());
		activities.put(CONSUMEREBATE, new ConsumeRebate());
		activities.put(UPGRADEGIFT, new UpgradeGift());
		activities.put(EXCHANGEGIFT, new ExchangeGift());
		activities.put(GATHERGIFT, new GatherGift());
		activities.put(PAYGIFT, new PayGift());
		activities.put(LIMITEDSHOP, new LimitedShop());
		activities.put(LOGINGIFT, new LoginGift());
		activities.put(FREEPHYSICAL, new FreePhysical());
		activities.put(TRADINGCENTER, new TradingCenter());
		activities.put(GIFTPACKAGE, new GiftPackage());
		activities.put(DISKBET, new DiskBet());
		activities.put(DISKGIFT, new DiskGift());
		
		activities.put(RECHARGERANK, new RechargeRank());
		activities.put(RECHARGEGIFT, new RechargeGift());
		gs.getLogger().info("game activities init satrt ...........................................................................................................");
		for (Map.Entry<Integer, ActivityImpl<? extends ActivityConfig>> e : activities.entrySet())
		{
			ActivityImpl<? extends ActivityConfig> act = e.getValue();
			act.init();
		}
		gs.getLogger().info("game activities init end .............................................................................................................");
	}
	
	public NewUserSysMail getNewUserSysMailActivity()
	{
		return (NewUserSysMail)activities.get(NEWUSERSYSMAIL);
	}
	
	public LoginGiftMail getLoginGiftMailActivity()
	{
		return (LoginGiftMail)activities.get(LOGINGIFTMAIL);
	}
	
	public DoubleDrop getDoubleDropActivity()
	{
		return (DoubleDrop)activities.get(DOUBLEDROP);
	}
	
	public ExtraDrop getExtraDropActivity()
	{
		return (ExtraDrop)activities.get(EXTRADROP);
	}
	
	public CheckinGift getCheckinGiftActivity()
	{
		return (CheckinGift)activities.get(CHECKINGIFT);
	}
	
	public FirstPayGift getFirstPayGiftActivity()
	{
		return (FirstPayGift)activities.get(FIRSTPAYGIFT);
	}
	
	public ConsumeGift getConsumeGiftActivity()
	{
		return (ConsumeGift)activities.get(CONSUMEGIFT);
	}
	
	public ConsumeRebate getConsumeRebateActivity()
	{
		return (ConsumeRebate)activities.get(CONSUMEREBATE);
	}
	
	public UpgradeGift getUpgradeGiftActivity()
	{
		return (UpgradeGift)activities.get(UPGRADEGIFT);
	}
	
	public ExchangeGift getExchangeGiftActivity()
	{
		return (ExchangeGift)activities.get(EXCHANGEGIFT);
	}
	
	public GatherGift getGatherGiftActivity()
	{
		return (GatherGift)activities.get(GATHERGIFT);
	}
	
	public DiskBet getDiskBetActivity()
	{
		return (DiskBet)activities.get(DISKBET);
	}
	
	public RechargeRank getRechargeRankActivity()
	{
		return (RechargeRank)activities.get(RECHARGERANK);
	}
	
	public PayGift getPayGiftActivity()
	{
		return (PayGift)activities.get(PAYGIFT);
	}
	
	public DiskGift getDiskGiftActivity()
	{
		return (DiskGift)activities.get(DISKGIFT);
	}
	
	public RechargeGift getRechargeGiftActivity()
	{
		return (RechargeGift)activities.get(RECHARGEGIFT);
	}
	
	public LimitedShop getLimitedShopActivity()
	{
		return (LimitedShop)activities.get(LIMITEDSHOP);
	}
	
	public LoginGift getLoginGiftActivity()
	{
		return (LoginGift)activities.get(LOGINGIFT);
	}
	
	public FreePhysical getFreePhysicalActivity()
	{
		return (FreePhysical)activities.get(FREEPHYSICAL);
	}
	
	public TradingCenter getTradingCenterActivity()
	{
		return (TradingCenter)activities.get(TRADINGCENTER);
	}
	
	public GiftPackage getGiftPackageActivity()
	{
		return (GiftPackage)activities.get(GIFTPACKAGE);
	}
	
	static int getActivityDisplayConfigSortSeq(ActivityDisplayConfig cfg)
	{
		int type = cfg.getType();
		int seq = 100;
		switch (type)
		{
		case DOUBLEDROP:
			seq = 13;
			break;
		case EXTRADROP:
			seq = 9;
			break;
		case CHECKINGIFT:
			seq = 8;
			break;
		case FIRSTPAYGIFT:
			seq = 1;
			break;
		case CONSUMEGIFT:
			seq = 11;
			break;
		case CONSUMEREBATE:
			seq = 12;
			break;
		case UPGRADEGIFT:
			seq = 2;
			break;
		case EXCHANGEGIFT:
			seq = 10;
			break;
		case GATHERGIFT:
			seq = 5;
			break;
		case PAYGIFT:
			seq = 3;
			break;
		case LIMITEDSHOP:
			seq = 6;
			break;
		case LOGINGIFT:
			seq = 7;
			break;
		case TRADINGCENTER:
			seq = 4;
			break;
		default:
			break;
		}
		seq *= 100;
		int days = cfg.getDays();
		if (days > 30)
			seq += 90;
		else
			seq += days;
		return seq;
	}
	
	public List<ActivityDisplayConfig> getDisplayActivityOpenSoon()
	{
		List<ActivityDisplayConfig> configs = new ArrayList<ActivityDisplayConfig>();
		for (Map.Entry<Integer, ActivityImpl<? extends ActivityConfig>> e : activities.entrySet())
		{
			ActivityImpl<? extends ActivityConfig> act = e.getValue();
			if (act.getConfigType().getSuperclass() == ActivityDisplayConfig.class)
			{
				List<? extends ActivityConfig> cfgs = act.getOpenSoonConfigs();
				for (ActivityConfig cfg : cfgs)
				{
					ActivityDisplayConfig dcfg = (ActivityDisplayConfig)cfg;
					configs.add(dcfg);
				}
			}
		}
		return configs;
	}
	
	public List<ActivityDisplayConfig> getDisplayActivityOpened()
	{
		List<ActivityDisplayConfig> configs = new ArrayList<ActivityDisplayConfig>();
		for (Map.Entry<Integer, ActivityImpl<? extends ActivityConfig>> e : activities.entrySet())
		{
			ActivityImpl<? extends ActivityConfig> act = e.getValue();
			if (act.getConfigType().getSuperclass() == ActivityDisplayConfig.class)
			{
				List<? extends ActivityConfig> cfgs = act.getOpenedConfigs();
				for (ActivityConfig cfg : cfgs)
				{
					ActivityDisplayConfig dcfg = (ActivityDisplayConfig)cfg;
					configs.add(dcfg);
				}
			}
		}
		Collections.sort(configs, new Comparator<ActivityDisplayConfig>()
				{
					public int compare(ActivityDisplayConfig l, ActivityDisplayConfig r)
					{
						return getActivityDisplayConfigSortSeq(l) - getActivityDisplayConfigSortSeq(r);
					}
				});
		return configs;
	}
	
	public boolean canShowDisplayActivityOpenedTip()
	{
		for (Map.Entry<Integer, ActivityImpl<? extends ActivityConfig>> e : activities.entrySet())
		{
			ActivityImpl<? extends ActivityConfig> act = e.getValue();
			if (act.getConfigType().getSuperclass() == ActivityDisplayConfig.class)
			{
				List<? extends ActivityConfig> cfgs = act.getOpenedConfigs();
				for (ActivityConfig cfg : cfgs)
				{
					if (cfg.getType() != GIFTPACKAGE)
						return true;
				}
			}
		}
		return false;
	}
	
	public int getServerOpenDayTime()
	{
		return gs.getLoginManager().getJubaoStartDay()*86400;
	}
	
	public static abstract class ActivityConfig
	{
		boolean open = false;
		int start = 0;
		int end = 0;
		int effectiveStart = 0;
		
		ActivityConfig()
		{
			
		}
		
		public abstract int getType();
		
		public int getID()
		{
			return getStartTime();
		}
		
		public int getStartTime()
		{
			return effectiveStart == 0 ? start : effectiveStart;
		}
		
		public int getEndTime()
		{
			return getStartTime() + (end-getRealStartTime());
		}
		
		public int getRealStartTime()
		{
			return start;
		}
		
		public int getTimeDayIndex(int curTime)
		{
			int startDay = (getStartTime())/86400;
			int curDay = (curTime)/86400;
			return curDay-startDay;
		}
		
		public int getDays()
		{
			int startDay = (getStartTime())/86400;
			int endDay = (getEndTime())/86400;
			return endDay-startDay+1;
		}
		
		public int checkValid()
		{
			if (getStartTime() <= 0 || getEndTime() <= 0 || getStartTime() >= getEndTime())
				return CONFIG_TIME_INVALID;
			return CONFIG_VALID;
		}
		
		public boolean isValid()
		{
			return checkValid() == CONFIG_VALID;
		}
		
		public boolean isOpen()
		{
			return open;
		}
		
		public boolean isOpenSoon(int curTime)
		{
			return isValid() && isOpen() && isInOpenSoonTime(curTime);
		}
		
		public boolean isOpened(int curTime)
		{
			return isValid() && isOpen() && isInOpenTime(curTime);
		}
		
		public boolean isInOpenSoonTime(int curTime)
		{
			return curTime + 3*24*3600 >= getStartTime() && curTime < getStartTime();
		}
		
		public boolean isInOpenTime(int curTime)
		{
			return curTime >= getStartTime() && curTime <= getEndTime();
		}
		
		public boolean isExclusion(ActivityConfig other)
		{
			return getID() == other.getID();
		}
		
		public boolean isTimeIntersect(ActivityConfig other)
		{
			return !(this.getStartTime() > other.getEndTime() || other.getStartTime() > this.getEndTime());
		}
		
		public String toIDString()
		{
			return " ID=" + getID();
		}
		
		public String toValidString()
		{
			int errorCode = checkValid();
			String errorStr = "is invalid(unknown code" + errorCode + ")";
			switch (errorCode)
			{
			case CONFIG_VALID:
				errorStr = "config is valid";
				break;
			case CONFIG_TIME_INVALID:
				errorStr = "config time is invalid";
				break;
			case CONFIG_MAIL_TITLE_INVALID:
				errorStr = "config mail title is invalid";
				break;
			case CONFIG_MAIL_CONTENT_INVALID:
				errorStr = "config mail content is invalid";
				break;
			case CONFIG_BRIEF_TITLE_INVALID:
				errorStr = "config brief title is invalid";
				break;
			case CONFIG_DETAIL_CONTENT_INVALID:
				errorStr = "config brief content is invalid";
				break;
			case CONFIG_VIT_INVALID:
				errorStr = "config vit value is invalid";
				break;
			case CONFIG_RREE_VIT_TIME_INVALID:
				errorStr = "config vit time is invalid";
				break;
			case CONFIG_RREE_VIT_TIME_SPAN_OVERLAP:
				errorStr = "config vit time span is overlap invalid";
				break;
			case CONFIG_DROP_ITEM_COUNT_INVALID:
				errorStr = "config drop item count is invalid";
				break;
			case CONFIG_DROP_PROBABILITY_INVALID:
				errorStr = "config drop item probability is invalid";
				break;
			case CONFIG_CHECKIN_DAYS_INVALID:
				errorStr = "config checkin days is invalid";
				break;
			case CONFIG_LEVEL_REQ_INVALID:
				errorStr = "config level req is invalid";
				break;
			case CONFIG_GIFT_COUNT_INVALID:
				errorStr = "config gift count is invalid";
				break;
			case CONFIG_LIMITEDTIME_INVALID:
				errorStr = "config limited time is invalid";
				break;
			case CONFIG_EXTRA_GIFT_COUNT_INVALID:
				errorStr = "config extra gift count is invalid";
				break;
			case CONFIG_PROPS_OR_GIFT_COUNT_INVALID:
				errorStr = "config props or gift count is invalid";
				break;
			case CONFIG_PROPS_ID_DUPLICATION_INVALID:
				errorStr = "config props id is duplication invalid";
				break;
			case CONFIG_GENERALS_OR_GIFT_COUNT_INVALID:
				errorStr = "config generals or gift count is invalid";
				break;
			case CONFIG_GENERALS_ID_DUPLICATION_INVALID:
				errorStr = "config generlas id is duplication invalid";
				break;
			case CONFIG_GEN_PACKAGE_GIFT_TIME_INVALID:
				errorStr = "config gen package gift time is invalid";
				break;
			case CONFIG_GEN_PACKAGE_GIFT_TITLE_INVALID:
				errorStr = "config gen package gift title is invalid";
				break;
			case CONFIG_GEN_PACKAGE_GIFT_CONTENT_INVALID:
				errorStr = "config gen package gift content is invalid";
				break;
			case CONFIG_GEN_PACKAGE_GIFT_EMPTY_INVALID:
				errorStr = "config gen package gift is empty invalid";
				break;
			case CONFIG_GIFT_MAX_BUY_TIMES_INVALID:
				errorStr = "config shop gift buy times invalid";
				break;
			case CONFIG_GIFT_COST_INVALID:
				errorStr = "config shop gift cost is invalid";
				break;
			case CONFIG_LOGIN_DAYS_REQ_INVALID:
				errorStr = "config login gift days is invalid";
				break;
			case CONFIG_TRADINGCENTER_BUY_END_TIME_INVALID:
				errorStr = "config trading center buy end time is invalid";
				break;
			case CONFIG_TRADINGCENTER_RETURN_TIME_INVALID:
				errorStr = "config trading center return time is invalid";
				break;
			case CONFIG_TRADINGCENTER_GOODS_COUNT_INVALID:
				errorStr = "config trading center goods count is invalid";
				break;
			case CONFIG_TRADINGCENTER_GOODS_TYPE_INVALID:
				errorStr = "config trading center goods type is invalid";
				break;
			case CONFIG_TRADINGCENTER_GOODS_PRICE_INVALID:
				errorStr = "config trading center goods price is invalid";
				break;
			case CONFIG_TRADINGCENTER_GOODS_MAX_BUY_COUNT_INVALID:
				errorStr = "config trading center goods max buy count is invalid";
				break;
			case CONFIG_TRADINGCENTER_GOODS_RETURN_VALUE_INVALID:
				errorStr = "config trading center goods return value is invalid";
				break;
			default:
				break;
			}
			return errorStr;
		}
		
		public String toOpenString()
		{
			return " is " + (isOpen() ? "open" : "not open");
		}
		
		public String toOpenTimeString(int curTime)
		{
			return " is " + (isInOpenTime(curTime) ? "in open time" : "not in open time") + " (" + GameServer.getTimeStampStr(getStartTime()) + "--" + GameServer.getTimeStampStr(getEndTime()) + ")";
		}
		
		public void dumpDetail(GameServer gs)
		{
			
		}
	}
	
	public static abstract class ActivityMailConfig extends ActivityConfig
	{
		String mailtitle = "";
		String mailcontent = "";
		List<SBean.DropEntryNew> mailatt = new ArrayList<SBean.DropEntryNew>();
		
		public ActivityMailConfig()
		{
			
		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			if (mailtitle.isEmpty())
				return CONFIG_MAIL_TITLE_INVALID;
			if (mailcontent.isEmpty())
				return CONFIG_MAIL_CONTENT_INVALID;
			return CONFIG_VALID;
		}
		
		public String getMailTitle()
		{
			return mailtitle;
		}
		
		public String getMailContent()
		{
			return mailcontent;
		}
		
		public List<SBean.DropEntryNew> getMailAttachItems()
		{
			return mailatt;
		}
	}
	
	public static abstract class ActivityDisplayConfig extends ActivityConfig
	{
		String briefTitle = "";
		String detailContent = "";
		
		public ActivityDisplayConfig()
		{
			
		}
		
		public String getBriefTitle()
		{
			return briefTitle;
		}
		
		public String getDetailContent()
		{
			return detailContent;
		}
		
//		public boolean isValid()
//		{
//			return super.isValid() && !briefTitle.isEmpty() && !detailContent.isEmpty();
//		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			if (briefTitle.isEmpty())
				return CONFIG_BRIEF_TITLE_INVALID;
			if (detailContent.isEmpty())
				return CONFIG_DETAIL_CONTENT_INVALID;
			return CONFIG_VALID;
		}
	}
	
	public abstract class ActivityImpl<T extends ActivityConfig>
	{
		Class<T> cfgType;
		String cfgFileName = "";
		String cfgFileExtensionName = ".xml";
		CustomXMLParser parser = new CustomXMLParser();
		List<? extends T> activityConfigs;
		
		ActivityImpl(Class<T> cfgType, String cfgFile)
		{
			this.cfgType = cfgType;
			this.cfgFileName = cfgFile;
		}
		
		public Class<T> getConfigType()
		{
			return cfgType;
		}
		
		public String getCfgFilePath()
		{
			String gsStr = "";
			if (gs.getConfig().id > 10000)
			{
				gsStr = "." + gs.getConfig().id;
			}
			return configDir + File.separator + cfgFileName + gsStr + cfgFileExtensionName;
		}
		
		protected void setConfigs(List<T> cfgs)
		{
			activityConfigs = cfgs;
		}
		
		protected boolean trySetConfigs(List<T> cfgs)
		{
			if (checkConfigs(cfgs))
			{
				setConfigs(cfgs);
				return true;
			}
			return false;
		}
		
		public abstract void setConfigureFile(String filepath);
		
		public void init()
		{
			final String filePath = getCfgFilePath();
			gs.mointorFileChanged(filePath, new Runnable()
				{
					@Override
					public void run()
					{
						setConfigureFile(filePath);
					}
				});
		}
		
		
		public List<T> getOpenedConfigs()
		{
			List<T> copyCfgs = new ArrayList<T>();
			copyCfgs.addAll(activityConfigs);
			int curTime = gs.getTime();
			List<T> cfgs = new ArrayList<T>();
			for (T cfg: copyCfgs)
			{
				if (cfg.isOpened(curTime))
					cfgs.add(cfg);
			}
			return cfgs;
		}
		
		public List<T> getOpenSoonConfigs()
		{
			List<T> copyCfgs = new ArrayList<T>();
			copyCfgs.addAll(activityConfigs);
			int curTime = gs.getTime();
			List<T> cfgs = new ArrayList<T>();
			for (T cfg: copyCfgs)
			{
				if (cfg.isOpenSoon(curTime))
					cfgs.add(cfg);
			}
			return cfgs;
		}
		
		public T getOpenedConfigById(int id)
		{
			T cfg = null;
			List<T> cfgs = getOpenedConfigs();
			for (T t : cfgs)
			{
				if (t.getID() == id)
				{
					cfg = t;
					break;
				}
			}
			return cfg;
		}
		
		public T getConfigById(int id)
		{
			T cfg = null;
			List<T> copyCfgs = new ArrayList<T>();
			copyCfgs.addAll(activityConfigs);
			for (T t : copyCfgs)
			{
				if (t.getID() == id)
				{
					cfg = t;
					break;
				}
			}
			return cfg;
		}
		
		boolean checkConfigs(List<T> cfgs)
		{
			//Set<Integer> ids = new TreeSet<Integer>();
			for (int i = 0; i < cfgs.size(); ++i)
			{
				T lcfg = cfgs.get(i);
				gs.getLogger().info(cfgType.getSimpleName() + lcfg.toIDString() + ", " + lcfg.toValidString() + ", and " + lcfg.toOpenString() + ", and" + lcfg.toOpenTimeString(gs.getTime()) + (i == cfgs.size()-1 ? "\n" : ""));
				if (lcfg.isValid() && lcfg.isOpen())
				{
					for (int j = i+1; j < cfgs.size(); ++j)
					{
						T rcfg = cfgs.get(j);
						if (rcfg.isValid() && rcfg.isOpen())
						{
							if (lcfg.isExclusion(rcfg))
							{
								gs.getLogger().info("find a config is is exclusive to other, configs is not changed!\n");
								return false;
							}
						}
					}
				}
				lcfg.dumpDetail(gs);
			}
			return true;
		}
	}

// TODO
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public static class NewUserSysMailConfig extends ActivityMailConfig
	{
		public NewUserSysMailConfig()
		{
			
		}
		
		public int getType()
		{
			return NEWUSERSYSMAIL;
		}
	}
	
	public class NewUserSysMail extends ActivityImpl<NewUserSysMailConfig>
	{
		
		public NewUserSysMail()
		{
			super(NewUserSysMailConfig.class, "NewRoleSysMail");
			parser.setActivityName("NewRoleSysMail");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("MailTitle");
			parser.addEntryName("MailContent");
			parser.addEntryName("Money");
			parser.addEntryName("Diamond");
			parser.addEntryName("Item");
			parser.addEntryName("Equip");
			parser.addEntryName("General");
			setConfigs(new ArrayList<NewUserSysMailConfig>());
		}
		
		public void sendNewUserSysMail(int roleId)
		{
			List<NewUserSysMailConfig> cfgs = getOpenedConfigs();
			for (NewUserSysMailConfig cfg : cfgs)
			{
				gs.getLoginManager().sysSendMessage(0, roleId, DBMail.Message.SUB_TYPE_NEWUSER_MAIL, cfg.getMailTitle(), cfg.getMailContent(), 0, true, cfg.getMailAttachItems());
			}
		}
		
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<NewUserSysMailConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<NewUserSysMailConfig> getConfig(XMLItem item)
		{
			List<NewUserSysMailConfig> cfgs = new ArrayList<NewUserSysMailConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("NewRoleSysMail"))
						{
							NewUserSysMailConfig cfg = new NewUserSysMailConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("MailTitle"))
									{
										cfg.mailtitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("MailContent"))
									{
										cfg.mailcontent = ccitem.value.toString().trim();
									}
									else if (aname.equals("Money"))
									{
										String cntStr = ccitem.getAttribute("count").trim();
										int count = Integer.parseInt(cntStr);
										if (count > 0 || count <= 100)
										{
											SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, count);
											cfg.mailatt.add(den);
										}
										else
										{
											throw new Exception("Mail attachment moeny count=" + count + " is invalid!");
										}	
									}
									else if (aname.equals("Diamond"))
									{
										String cntStr = ccitem.getAttribute("count").trim();
										int count = Integer.parseInt(cntStr);
										if (count > 0 || count <= 100)
										{
											SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, count);
											cfg.mailatt.add(den);
										}
										else
										{
											throw new Exception("Mail attachment diamond count=" + count + " is invalid!");
										}
									}
									else if (aname.equals("Item"))
									{
										String idStr = ccitem.getAttribute("id").trim();
										int id = Integer.parseInt(idStr);
										String cntStr = ccitem.getAttribute("count").trim();
										int count = Integer.parseInt(cntStr);
										if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
										{
											SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)id, count);
											cfg.mailatt.add(den);
										}
										else
										{
											throw new Exception("Mail attachment item id=" + id + " count=" + count + " is invalid!");
										}
									}
									else if (aname.equals("Equip"))
									{
										String idStr = ccitem.getAttribute("id").trim();
										int id = Integer.parseInt(idStr);
										String cntStr = ccitem.getAttribute("count").trim();
										int count = Integer.parseInt(cntStr);
										if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
										{
											SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_EQUIP, (short)id, count);
											cfg.mailatt.add(den);
										}
										else
										{
											throw new Exception("Mail attachment equip id=" + id + " count=" + count + " is invalid!");
										}
									}
									else if (aname.equals("General"))
									{
										String idStr = ccitem.getAttribute("id").trim();
										int id = Integer.parseInt(idStr);
										String cntStr = ccitem.getAttribute("count").trim();
										int count = Integer.parseInt(cntStr);
										if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
										{
											SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_GENERAL, (short)id, count);
											cfg.mailatt.add(den);
										}
										else
										{
											throw new Exception("Mail attachment general id=" + id + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();
			}
			return cfgs;
		}
	}
	
	public static class LoginGiftMailConfig extends  ActivityMailConfig
	{
		int lvlReq = 1;
		
		public LoginGiftMailConfig()
		{
			
		}
			
		public int getType()
		{
			return LOGINGIFTMAIL;
		}
	}
	
	public class LoginGiftMail extends ActivityImpl<LoginGiftMailConfig>
	{
		public LoginGiftMail()
		{
			super(LoginGiftMailConfig.class, "LoginGiftMail");
			parser.setActivityName("LoginGiftMail");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("LevelRequire");
			parser.addEntryName("MailTitle");
			parser.addEntryName("MailContent");
			parser.addEntryName("Money");
			parser.addEntryName("Diamond");
			parser.addEntryName("Item");
			parser.addEntryName("Equip");
			parser.addEntryName("General");
			setConfigs(new ArrayList<LoginGiftMailConfig>());
		}
			
		public void sendLoginGiftMail(int roleid, int curLvl, LoginGiftMailConfig cfg)
		{
			if (cfg == null || !cfg.isOpened(gs.getTime()))
				return;
			if (curLvl < cfg.lvlReq)
				return;
			gs.getLoginManager().sysSendMessage(0, roleid, DBMail.Message.SUB_TYPE_LOGIN_GIFT_MAIL, cfg.getMailTitle(), cfg.getMailContent(), 0, true, cfg.getMailAttachItems());
		}
		
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<LoginGiftMailConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<LoginGiftMailConfig> getConfig(XMLItem item)
		{
			List<LoginGiftMailConfig> cfgs = new ArrayList<LoginGiftMailConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("LoginGiftMail"))
						{
							LoginGiftMailConfig cfg = new LoginGiftMailConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("LevelRequire"))
									{
										String lvlStr = ccitem.getAttribute("level").trim();
										int lvl = Integer.parseInt(lvlStr);
										if (lvl > 0)
											cfg.lvlReq = lvl;
									}
									else if (aname.equals("MailTitle"))
									{
										cfg.mailtitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("MailContent"))
									{
										cfg.mailcontent = ccitem.value.toString().trim();
									}
									else if (aname.equals("Money"))
									{
										String cntStr = ccitem.getAttribute("count").trim();
										int count = Integer.parseInt(cntStr);
										if (count > 0 || count <= 100)
										{
											SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, count);
											cfg.mailatt.add(den);
										}
										else
										{
											throw new Exception("Mail attachment moeny count=" + count + " is invalid!");
										}
									}
									else if (aname.equals("Diamond"))
									{
										String cntStr = ccitem.getAttribute("count").trim();
										int count = Integer.parseInt(cntStr);
										if (count > 0 || count <= 100)
										{
											SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, count);
											cfg.mailatt.add(den);
										}
										else
										{
											throw new Exception("Mail attachment diamond count=" + count + " is invalid!");
										}
									}
									else if (aname.equals("Item"))
									{
										String idStr = ccitem.getAttribute("id").trim();
										int id = Integer.parseInt(idStr);
										String cntStr = ccitem.getAttribute("count").trim();
										int count = Integer.parseInt(cntStr);
										if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
										{
											SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)id, count);
											cfg.mailatt.add(den);
										}
										else
										{
											throw new Exception("Mail attachment item id=" + id + " count=" + count + " is invalid!");
										}
									}
									else if (aname.equals("Equip"))
									{
										String idStr = ccitem.getAttribute("id").trim();
										int id = Integer.parseInt(idStr);
										String cntStr = ccitem.getAttribute("count").trim();
										int count = Integer.parseInt(cntStr);
										if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
										{
											SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_EQUIP, (short)id, count);
											cfg.mailatt.add(den);
										}
										else
										{
											throw new Exception("Mail attachment equip id=" + id + " count=" + count + " is invalid!");
										}
									}
									else if (aname.equals("General"))
									{
										String idStr = ccitem.getAttribute("id").trim();
										int id = Integer.parseInt(idStr);
										String cntStr = ccitem.getAttribute("count").trim();
										int count = Integer.parseInt(cntStr);
										if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
										{
											SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_GENERAL, (short)id, count);
											cfg.mailatt.add(den);
										}
										else
										{
											throw new Exception("Mail attachment general id=" + id + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();
			}
			return cfgs;
		}
	}
	
	
	public static class FreePhysicalConfig extends ActivityConfig
	{
		public static final int MAX_FREE_VIT_SIZE = 3;
		public SBean.FreeVit[] freeVit = new SBean.FreeVit[MAX_FREE_VIT_SIZE];
		public FreePhysicalConfig()
		{
		}
		
		public int getType()
		{
			return FREEPHYSICAL;
		}
		
//		public boolean isValid()
//		{
//			if (!super.isValid())
//				return false;
//
//			for (int i = 0; i < MAX_FREE_VIT_SIZE; ++i)
//			{
//				SBean.FreeVit fv = freeVit[i];
//				if (fv != null)
//				{
//					if (fv.vit <= 0 || fv.startTime < 0 && fv.startTime > 24*3600 && fv.endTime < 0 && fv.endTime > 24*3600 && fv.startTime >= fv.endTime)
//						return false;
//					for (int j = i+1; j < MAX_FREE_VIT_SIZE; ++j)
//					{
//						SBean.FreeVit fvo = freeVit[j];
//						if (fvo != null)
//						{
//							if (fv.startTime == fvo.startTime)
//								return false;	
//						}
//					}	
//				}
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			for (int i = 0; i < MAX_FREE_VIT_SIZE; ++i)
			{
				SBean.FreeVit fv = freeVit[i];
				if (fv != null)
				{
					if (fv.vit <= 0)
						return CONFIG_VIT_INVALID;
					if (fv.startTime < 0 && fv.startTime > 24*3600 && fv.endTime < 0 && fv.endTime > 24*3600 && fv.startTime >= fv.endTime)
						return CONFIG_RREE_VIT_TIME_INVALID;
					for (int j = i+1; j < MAX_FREE_VIT_SIZE; ++j)
					{
						SBean.FreeVit fvo = freeVit[j];
						if (fvo != null)
						{
							if (fv.startTime == fvo.startTime)
								return CONFIG_RREE_VIT_TIME_SPAN_OVERLAP;
						}
					}	
				}
			}
			return CONFIG_VALID;
		}
		
		public SBean.FreeVit[] getFreeVit()
		{
			return freeVit;
		}
		
		public void setFreeVit(int index, SBean.FreeVit fv)
		{
			freeVit[index] = fv;
		}
		
	}
	
	
	public class FreePhysical extends ActivityImpl<FreePhysicalConfig>
	{
		public FreePhysical()
		{
			super(FreePhysicalConfig.class, "FreePhysical");
			parser.setActivityName("FreePhysical");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("ReceiveTime");
			setConfigs(new ArrayList<FreePhysicalConfig>());
		}
					
		public SBean.FreeVit[] getFreeVit()
		{
			SBean.FreeVit[] freevits = null; 
			List<FreePhysicalConfig> cfgs = getOpenedConfigs();
			if (!cfgs.isEmpty())
			{
				FreePhysicalConfig cfg = cfgs.get(0);
				freevits = cfg.getFreeVit();
			}
			return freevits;
		}
		
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<FreePhysicalConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<FreePhysicalConfig> getConfig(XMLItem item)
		{
			List<FreePhysicalConfig> cfgs = new ArrayList<FreePhysicalConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("FreePhysical"))
						{
							FreePhysicalConfig cfg = new FreePhysicalConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("ReceiveTime"))
									{
										SBean.FreeVit fv = new SBean.FreeVit();
										String seqStr = ccitem.getAttribute("seq").trim();
										int seq = Integer.parseInt(seqStr);
										if (seq > 0)
										{
											{
												String timeStr = ccitem.getAttribute("start").trim();
												Date date = new SimpleDateFormat("HH:mm:ss").parse(timeStr);
												if (date != null)
													fv.startTime = (int)(date.getTime()/1000) + 8*3600;
											}
											{
												String timeStr = ccitem.getAttribute("end").trim();
												Date date = new SimpleDateFormat("HH:mm:ss").parse(timeStr);
												if (date != null)
													fv.endTime = (int)(date.getTime()/1000) + 8*3600;
											}
											{
												String vitStr = ccitem.getAttribute("physical").trim();
												int vit = Integer.parseInt(vitStr);
												if (vit > 0)
													fv.vit = vit;
											}
											cfg.setFreeVit(seq-1, fv);	
										}
										else
										{
											throw new Exception("free vit seq=" + seq + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();
			}
			Collections.sort(cfgs,  new Comparator<FreePhysicalConfig>()
										{
											public int compare(FreePhysicalConfig l, FreePhysicalConfig r)
											{
												return l.start-r.start;
											}
										});
			return cfgs;
		}
	}
	
// TODO
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static final int NORMAL_BATTLE = 0;
	public static final int HERO_BATTLE = 1;
	public static final int CHIBI_BATTLE = 2;
	public static final int BAGUAZHEN_BATTLE = 3;
	public static final int CAPTURE_CITY = 4;
	
	public static final int DROP_EXPERIENCE = 0;
	public static final int DROP_DIAMOND = 1;
	public static final int DROP_MONEY = 2;
	public static final int DROP_ITEM = 3;
	
	public static class DoubleDropConfig extends ActivityDisplayConfig
	{
		int [][] dropTable = 
			{
				{1, 1, 1, 1,},//∆’Õ®’Ω“€
				{1, 1, 1, 1,},//”¢–€’Ω“€
				{1, 1, 1, 1,},//≥‡±⁄÷Æ’Ω
				{1, 1, 1, 1,},//∞Àÿ‘’Û
				{1, 1, 1, 1,},//∂·≥«’Ω
			};
		
		public DoubleDropConfig()
		{
			
		}
		
		public int getType()
		{
			return DOUBLEDROP;
		}
		
		public boolean isExclusion(ActivityConfig other)
		{
			return super.isExclusion(other) || isTimeIntersect(other);
		}
	}
	
	public class DoubleDrop  extends ActivityImpl<DoubleDropConfig>
	{
		public DoubleDrop()
		{
			super(DoubleDropConfig.class, "DoubleDrop");
			parser.setActivityName("DoubleDrop");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("NormalBattle");
			parser.addEntryName("HeroBattle");
			parser.addEntryName("ChibiBattle");
			parser.addEntryName("BaguazhenBattle");
			parser.addEntryName("CaptureCity");
			setConfigs(new ArrayList<DoubleDropConfig>());
		}
			
		public int getDropMultiple(int atype, int itype)
		{
			int multiple = 1;
			List<DoubleDropConfig> cfgs = getOpenedConfigs();
			if (!cfgs.isEmpty())
			{
				DoubleDropConfig cfg = cfgs.get(0);
				if (atype >=0 && atype < cfg.dropTable.length)
				{
					if (itype >=0 && itype < cfg.dropTable[atype].length)
					{
						multiple = cfg.dropTable[atype][itype];
					}
				}
			}
			return multiple;
		}
		
		
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<DoubleDropConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<DoubleDropConfig> getConfig(XMLItem item)
		{
			List<DoubleDropConfig> cfgs = new ArrayList<DoubleDropConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("DoubleDrop"))
						{
							DoubleDropConfig cfg = new DoubleDropConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("NormalBattle"))
									{
										String expStr = ccitem.getAttribute("experience").trim();
										if (!expStr.isEmpty())
										{
											int exp = Integer.parseInt(expStr);
											if (exp > 0)
												cfg.dropTable[NORMAL_BATTLE][DROP_EXPERIENCE] = exp;
											else
												throw new Exception("experience=" + exp + " is invalid!");
										}
										String stoneStr = ccitem.getAttribute("diamond").trim();
										if (!stoneStr.isEmpty())
										{
											int stone = Integer.parseInt(stoneStr);
											if (stone > 0)
												cfg.dropTable[NORMAL_BATTLE][DROP_DIAMOND] = stone;	
											else
												throw new Exception("diamond=" + stone + " is invalid!");
										}
										String moneyStr = ccitem.getAttribute("money").trim();
										if (!moneyStr.isEmpty())
										{
											int money = Integer.parseInt(moneyStr);
											if (money > 0)
												cfg.dropTable[NORMAL_BATTLE][DROP_MONEY] = money;
											else
												throw new Exception("money=" + money + " is invalid!");
										}
										String itemStr = ccitem.getAttribute("item").trim();
										if (!itemStr.isEmpty())
										{
											int iitem = Integer.parseInt(itemStr);
											if (iitem > 0)
												cfg.dropTable[NORMAL_BATTLE][DROP_ITEM] = iitem;
											else
												throw new Exception("item=" + iitem + " is invalid!");
										}
									}
									else if (aname.equals("HeroBattle"))
									{
										String expStr = ccitem.getAttribute("experience").trim();
										if (!expStr.isEmpty())
										{
											int exp = Integer.parseInt(expStr);
											if (exp > 0)
												cfg.dropTable[HERO_BATTLE][DROP_EXPERIENCE] = exp;
											else
												throw new Exception("experience=" + exp + " is invalid!");
										}
										String stoneStr = ccitem.getAttribute("diamond").trim();
										if (!stoneStr.isEmpty())
										{
											int stone = Integer.parseInt(stoneStr);
											if (stone > 0)
												cfg.dropTable[HERO_BATTLE][DROP_DIAMOND] = stone;
											else
												throw new Exception("diamond=" + stone + " is invalid!");
										}
										String moneyStr = ccitem.getAttribute("money").trim();
										if (!moneyStr.isEmpty())
										{
											int money = Integer.parseInt(moneyStr);
											if (money > 0)
												cfg.dropTable[HERO_BATTLE][DROP_MONEY] = money;
											else
												throw new Exception("money=" + money + " is invalid!");
										}
										String itemStr = ccitem.getAttribute("item").trim();
										if (!itemStr.isEmpty())
										{
											int iitem = Integer.parseInt(itemStr);
											if (iitem > 0)
												cfg.dropTable[HERO_BATTLE][DROP_ITEM] = iitem;
											else
												throw new Exception("item=" + iitem + " is invalid!");
										}
									}
									else if (aname.equals("ChibiBattle"))
									{
										String expStr = ccitem.getAttribute("experience").trim();
										if (!expStr.isEmpty())
										{
											int exp = Integer.parseInt(expStr);
											if (exp > 0)
												cfg.dropTable[CHIBI_BATTLE][DROP_EXPERIENCE] = exp;
											else
												throw new Exception("experience=" + exp + " is invalid!");
										}
										String stoneStr = ccitem.getAttribute("diamond").trim();
										if (!stoneStr.isEmpty())
										{
											int stone = Integer.parseInt(stoneStr);
											if (stone > 0)
												cfg.dropTable[CHIBI_BATTLE][DROP_DIAMOND] = stone;
											else
												throw new Exception("diamond=" + stone + " is invalid!");
										}
										String moneyStr = ccitem.getAttribute("money").trim();
										if (!moneyStr.isEmpty())
										{
											int money = Integer.parseInt(moneyStr);
											if (money > 0)
												cfg.dropTable[CHIBI_BATTLE][DROP_MONEY] = money;
											else
												throw new Exception("money=" + money + " is invalid!");
										}
										String itemStr = ccitem.getAttribute("item").trim();
										if (!itemStr.isEmpty())
										{
											int iitem = Integer.parseInt(itemStr);
											if (iitem > 0)
												cfg.dropTable[CHIBI_BATTLE][DROP_ITEM] = iitem;
											else
												throw new Exception("item=" + iitem + " is invalid!");
										}
									}
									else if (aname.equals("BaguazhenBattle"))
									{
										String expStr = ccitem.getAttribute("experience").trim();
										if (!expStr.isEmpty())
										{
											int exp = Integer.parseInt(expStr);
											if (exp > 0)
												cfg.dropTable[BAGUAZHEN_BATTLE][DROP_EXPERIENCE] = exp;
											else
												throw new Exception("experience=" + exp + " is invalid!");
										}
										String stoneStr = ccitem.getAttribute("diamond").trim();
										if (!stoneStr.isEmpty())
										{
											int stone = Integer.parseInt(stoneStr);
											if (stone > 0)
												cfg.dropTable[BAGUAZHEN_BATTLE][DROP_DIAMOND] = stone;
											else
												throw new Exception("diamond=" + stone + " is invalid!");
										}
										String moneyStr = ccitem.getAttribute("money").trim();
										if (!moneyStr.isEmpty())
										{
											int money = Integer.parseInt(moneyStr);
											if (money > 0)
												cfg.dropTable[BAGUAZHEN_BATTLE][DROP_MONEY] = money;
											else
												throw new Exception("money=" + money + " is invalid!");
										}
										String itemStr = ccitem.getAttribute("item").trim();
										if (!itemStr.isEmpty())
										{
											int iitem = Integer.parseInt(itemStr);
											if (iitem > 0)
												cfg.dropTable[BAGUAZHEN_BATTLE][DROP_ITEM] = iitem;
											else
												throw new Exception("item=" + iitem + " is invalid!");
										}
									}
									else if (aname.equals("CaptureCity"))
									{
										String expStr = ccitem.getAttribute("experience").trim();
										if (!expStr.isEmpty())
										{
											int exp = Integer.parseInt(expStr);
											if (exp > 0)
												cfg.dropTable[CAPTURE_CITY][DROP_EXPERIENCE] = exp;
											else
												throw new Exception("experience=" + exp + " is invalid!");
										}
										String stoneStr = ccitem.getAttribute("diamond").trim();
										if (!stoneStr.isEmpty())
										{
											int stone = Integer.parseInt(stoneStr);
											if (stone > 0)
												cfg.dropTable[CAPTURE_CITY][DROP_DIAMOND] = stone;
											else
												throw new Exception("diamond=" + stone + " is invalid!");
										}
										String moneyStr = ccitem.getAttribute("money").trim();
										if (!moneyStr.isEmpty())
										{
											int money = Integer.parseInt(moneyStr);
											if (money > 0)
												cfg.dropTable[CAPTURE_CITY][DROP_MONEY] = money;
											else
												throw new Exception("money=" + money + " is invalid!");
										}
										String itemStr = ccitem.getAttribute("item").trim();
										if (!itemStr.isEmpty())
										{
											int iitem = Integer.parseInt(itemStr);
											if (iitem > 0)
												cfg.dropTable[CAPTURE_CITY][DROP_ITEM] = iitem;
											else
												throw new Exception("item=" + iitem + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	public static class DropTable
	{
		List<SBean.DropTableEntry> dropTbl = new ArrayList<SBean.DropTableEntry>();
		
		DropTable()
		{
			
		}
		
		public void addDrop(float pro, int type, int id, int count)
		{
			dropTbl.add(new SBean.DropTableEntry(new SBean.DropEntry((byte)type, (byte)count, (short)id), pro));
		}
		
		int checkValid()
		{
			float dropPro = 0.0f;
			for (SBean.DropTableEntry d : dropTbl)
			{
				if (d.item.arg > 100)
					return CONFIG_DROP_ITEM_COUNT_INVALID;
				dropPro += d.pro;
			}
			if (dropPro > 1.0f)
				return CONFIG_DROP_PROBABILITY_INVALID;
			return CONFIG_VALID;
		}
		
		SBean.DropEntry getDrop()
		{
			float r = GameData.getInstance().getRandom().nextFloat();
			float dropPro = 0.0f;
			for (SBean.DropTableEntry d : dropTbl)
			{
				dropPro += d.pro;
				if (r < dropPro)
					return d.item;
			}
			return null;
		}
	}
	
	public static class ExtraDropConfig extends ActivityDisplayConfig
	{
		DropTable[] battleDrops = 
			{                          
				new DropTable(),//∆’Õ®’Ω“€ 
				new DropTable(),//”¢–€’Ω“€ 
				new DropTable(),//≥‡±⁄÷Æ’Ω 
				new DropTable(),//∞Àÿ‘’Û  
			};
		
		public ExtraDropConfig()
		{
			
		}
		
		public int getType()
		{
			return EXTRADROP;
		}
		
//		public boolean isValid()
//		{
//			if (!super.isValid())
//				return false;
//			for (DropTable e : battleDrops)
//			{
//				if (!e.isValid())
//					return false;
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			for (DropTable e : battleDrops)
			{
				errorCode= e.checkValid();
				if (errorCode != CONFIG_VALID)
					return errorCode;
			}
			return CONFIG_VALID;
		}
		
		public boolean isExclusion(ActivityConfig other)
		{
			return super.isExclusion(other) || isTimeIntersect(other);
		}
		
		DropTable getDropTable(int btype)
		{
			if (btype >= 0 && btype < battleDrops.length)
			{
				return battleDrops[btype];
			}
			return null;
		}
		
		public SBean.DropEntry getDrop(int btype)
		{
			if (btype >= 0 && btype < battleDrops.length)
			{
				return battleDrops[btype].getDrop();
			}
			return null;
		}
	}
	
	public class ExtraDrop extends ActivityImpl<ExtraDropConfig>
	{
		public ExtraDrop()
		{
			super(ExtraDropConfig.class, "ExtraDrop");
			parser.setActivityName("ExtraDrop");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("DropTable");
			parser.addEntryName("Drop");
			setConfigs(new ArrayList<ExtraDropConfig>());
		}
		
		public SBean.DropEntry getExtraDrop(int btype)
		{
			List<ExtraDropConfig> cfgs = getOpenedConfigs();
			if (!cfgs.isEmpty())
			{
				ExtraDropConfig cfg = cfgs.get(0);
				return cfg.getDrop(btype);
			}
			return null;
		}
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<ExtraDropConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<ExtraDropConfig> getConfig(XMLItem item)
		{
			List<ExtraDropConfig> cfgs = new ArrayList<ExtraDropConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("ExtraDrop"))
						{
							ExtraDropConfig cfg = new ExtraDropConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("DropTable"))
									{
										DropTable dropTbl = null;
										String battleTypeStr = ccitem.getAttribute("battle").trim();
										if (battleTypeStr.equals("Normal"))
										{
											dropTbl = cfg.getDropTable(NORMAL_BATTLE);
										}
										else if (battleTypeStr.equals("Hero"))
										{
											dropTbl = cfg.getDropTable(HERO_BATTLE);
										}
										else if (battleTypeStr.equals("Chibi"))
										{
											dropTbl = cfg.getDropTable(CHIBI_BATTLE);
										}
										else if (battleTypeStr.equals("Baguazhen"))
										{
											dropTbl = cfg.getDropTable(BAGUAZHEN_BATTLE);
										}
										else
										{
											throw new Exception("unknown battle type :" + battleTypeStr + " !");
										}
										if (dropTbl != null)
										{
											for (XMLItem cccitem : ccitem.childrenItems)
											{
												String aaname = cccitem.getAttribute("name");
												if (aaname.equals("Drop"))
												{
													String proStr = cccitem.getAttribute("probability").trim();
													float pro = Integer.parseInt(proStr)/100.0f;
													String typeStr = cccitem.getAttribute("type").trim();
													if (typeStr.equals("Money"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														byte count = Byte.parseByte(countStr);
														if (count > 0)
														{
															dropTbl.addDrop(pro, GameData.COMMON_TYPE_MONEY, 0, count);
														}
														else
														{
															throw new Exception("Money count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Diamond"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														byte count = Byte.parseByte(countStr);
														if (count > 0)
														{
															dropTbl.addDrop(pro, GameData.COMMON_TYPE_STONE, 0, count);
														}
														else
														{
															throw new Exception("Diamond count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Item"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														byte count = Byte.parseByte(countStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															dropTbl.addDrop(pro, GameData.COMMON_TYPE_ITEM, id, count);
														}
														else
														{
															throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Equip"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														byte count = Byte.parseByte(countStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															dropTbl.addDrop(pro, GameData.COMMON_TYPE_EQUIP, id, count);
														}
														else
														{
															throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("General"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														byte count = Byte.parseByte(countStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															dropTbl.addDrop(pro, GameData.COMMON_TYPE_GENERAL, id, count);
														}
														else
														{
															throw new Exception("General id=" + id + " is invalid!");
														}
													}
													else
													{
														throw new Exception("unknown drop type:" + typeStr + " !");
													}
												}
												else
												{
													throw new Exception("unknown DropTable entry:" + aaname + " !");
												}
											}
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	public static class GiftItem
	{
		public SBean.DropEntryNew item = new SBean.DropEntryNew();
		public int doubleCountLvl;
	}
	
	public static class DayGift
	{
		public int levelReq = 1;
		public List<GiftItem> gift = new ArrayList<GiftItem>();
	}
	
	public static class CheckinGiftConfig extends ActivityDisplayConfig
	{
		final static int MAX_DAY = 30;
		DayGift[] checkinGift = new DayGift[MAX_DAY];
		public CheckinGiftConfig()
		{
			for (int i = 0; i < MAX_DAY; ++i)
			{
				checkinGift[i] = new DayGift();
			}
		}
		
		public int getType()
		{
			return CHECKINGIFT;
		}
		
//		public boolean isValid()
//		{
//			if (!super.isValid())
//				return false;
//			int days = getDays();
//			if (days <= 0 && days > MAX_DAY)
//				return false;
//			int minLvlReq = 1;
//			for (int i = 0; i < days; ++i)
//			{
//				if (checkinGift[i].levelReq < minLvlReq)
//					return false;
//				minLvlReq = checkinGift[i].levelReq;
//				int gcount = checkinGift[i].gift.size();
//				if (gcount <= 0 || gcount > 5)
//					return false;
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			int days = getDays();
			if (days <= 0 || days > MAX_DAY)
				return CONFIG_CHECKIN_DAYS_INVALID;
			int minLvlReq = 1;
			for (int i = 0; i < days; ++i)
			{
				if (checkinGift[i].levelReq < minLvlReq)
					return CONFIG_LEVEL_REQ_INVALID;
				minLvlReq = checkinGift[i].levelReq;
				int gcount = checkinGift[i].gift.size();
				if (gcount <= 0 || gcount > 5)
					return CONFIG_GIFT_COUNT_INVALID;
			}
			return CONFIG_VALID;
		}
		
		public void setDayGiftLevelReq(int day, int lvlReq)
		{
			if (day > 0 && day <= MAX_DAY)
			{
				checkinGift[day-1].levelReq = lvlReq;
			}			
		}
		
		public void addDayGiftItem(int day, GiftItem item)
		{
			if (day > 0 && day <= MAX_DAY)
			{
				checkinGift[day-1].gift.add(item);
			}
		}
		
		public int getDayGiftLevelReq(int dayIndex)
		{
			int lvlReq = 1;
			if (dayIndex >= 0 && dayIndex < MAX_DAY)
			{
				lvlReq = checkinGift[dayIndex].levelReq;
			}
			return lvlReq; 
		}
		
		public Iterable<GiftItem> getDayGift(int dayIndex)
		{
			Iterable<GiftItem> daygift = null;
			if (dayIndex >= 0 && dayIndex < MAX_DAY)
			{
				daygift = checkinGift[dayIndex].gift;
			}
			return daygift;
		}
		
		public int getDayGiftCount(int dayIndex)
		{
			int count = 0;
			if (dayIndex >= 0 && dayIndex < MAX_DAY)
			{
				count = checkinGift[dayIndex].gift.size();
			}
			return count;
		}
	}
	
	public class CheckinGift extends ActivityImpl<CheckinGiftConfig>
	{
		public CheckinGift()
		{
			super(CheckinGiftConfig.class, "CheckinGift");
			parser.setActivityName("CheckinGift");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("DayGift");
			parser.addEntryName("Gift");
			setConfigs(new ArrayList<CheckinGiftConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<CheckinGiftConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<CheckinGiftConfig> getConfig(XMLItem item)
		{
			List<CheckinGiftConfig> cfgs = new ArrayList<CheckinGiftConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("CheckinGift"))
						{
							CheckinGiftConfig cfg = new CheckinGiftConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("DayGift"))
									{
										String dayStr = ccitem.getAttribute("day").trim();
										int day = Integer.parseInt(dayStr);
										String lvlReqStr = ccitem.getAttribute("level").trim();
										int lvlReq = Integer.parseInt(lvlReqStr);
										if (day > 0 && day <= CheckinGiftConfig.MAX_DAY && lvlReq > 0)
										{
											cfg.setDayGiftLevelReq(day, lvlReq);
											for (XMLItem cccitem : ccitem.childrenItems)
											{
												String aaname = cccitem.getAttribute("name");
												if (aaname.equals("Gift"))
												{
													String typeStr = cccitem.getAttribute("type").trim();
													if (typeStr.equals("Money"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															GiftItem gi = new GiftItem();
															gi.item.type = GameData.COMMON_TYPE_MONEY;
															gi.item.id = 0;
															gi.item.arg = count;
															String vipStr = cccitem.getAttribute("vip").trim();
															if (!vipStr.isEmpty())
															{
																int vip = Integer.parseInt(vipStr);
																if (vip > 0)
																	gi.doubleCountLvl = vip;
															}
															cfg.addDayGiftItem(day, gi);
														}
														else
														{
															throw new Exception("Money count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Diamond"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															GiftItem gi = new GiftItem();
															gi.item.type = GameData.COMMON_TYPE_STONE;
															gi.item.id = 0;
															gi.item.arg = count;
															String vipStr = cccitem.getAttribute("vip").trim();
															if (!vipStr.isEmpty())
															{
																int vip = Integer.parseInt(vipStr);
																if (vip > 0)
																	gi.doubleCountLvl = vip;
															}
															cfg.addDayGiftItem(day, gi);
														}
														else
														{
															throw new Exception("Diamond count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Item"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															GiftItem gi = new GiftItem();
															gi.item.type = GameData.COMMON_TYPE_ITEM;
															gi.item.id = (short)id;
															gi.item.arg = count;
															String vipStr = cccitem.getAttribute("vip").trim();
															if (!vipStr.isEmpty())
															{
																int vip = Integer.parseInt(vipStr);
																if (vip > 0)
																	gi.doubleCountLvl = vip;
															}
															cfg.addDayGiftItem(day, gi);
														}
														else
														{
															throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Equip"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															GiftItem gi = new GiftItem();
															gi.item.type = GameData.COMMON_TYPE_EQUIP;
															gi.item.id = (short)id;
															gi.item.arg = count;
															String vipStr = cccitem.getAttribute("vip").trim();
															if (!vipStr.isEmpty())
															{
																int vip = Integer.parseInt(vipStr);
																if (vip > 0)
																	gi.doubleCountLvl = vip;
															}
															cfg.addDayGiftItem(day, gi);
														}
														else
														{
															throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("General"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															GiftItem gi = new GiftItem();
															gi.item.type = GameData.COMMON_TYPE_GENERAL;
															gi.item.id = (short)id;
															gi.item.arg = count;
															cfg.addDayGiftItem(day, gi);
														}
														else
														{
															throw new Exception("General id=" + id + " is invalid!");
														}
													}
													else
													{
														throw new Exception("unknown Gift type:" + typeStr + " !");
													}
												}
												else
												{
													throw new Exception("unknown DayGift entry:" + aaname + " !");
												}
											}
										}
										else
										{
											throw new Exception("DayGift day=" + day + " lvlReq=" + lvlReq + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	public static class FirstPayGiftConfig extends ActivityDisplayConfig
	{
		public List<SBean.DropEntryNew> gift = new ArrayList<SBean.DropEntryNew>();
		public FirstPayGiftConfig()
		{
		}
		
		public int getType()
		{
			return FIRSTPAYGIFT;
		}
		
//		public boolean isValid()
//		{
//			if (!super.isValid())
//				return false;
//
//			int gcount = gift.size();
//			if (gcount <= 0 || gcount > 3)
//				return false;
//			return true;
//		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			int gcount = gift.size();
			if (gcount <= 0 || gcount > 3)
				return CONFIG_GIFT_COUNT_INVALID;
			return CONFIG_VALID;
		}
		
		public Iterable<SBean.DropEntryNew> getGift()
		{
			return gift;
		}
		
		public int getGiftCount()
		{
			return gift.size();
		}
		
		public void addGift(SBean.DropEntryNew g)
		{
			gift.add(g);
		}
	}
	
	
	public class FirstPayGift extends ActivityImpl<FirstPayGiftConfig>
	{
		public FirstPayGift()
		{
			super(FirstPayGiftConfig.class, "PayGift");
			parser.setActivityName("PayGift");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("Gift");
			setConfigs(new ArrayList<FirstPayGiftConfig>());
		}
					
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<FirstPayGiftConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<FirstPayGiftConfig> getConfig(XMLItem item)
		{
			List<FirstPayGiftConfig> cfgs = new ArrayList<FirstPayGiftConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("PayGift"))
						{
							FirstPayGiftConfig cfg = new FirstPayGiftConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("Gift"))
									{
										String typeStr = ccitem.getAttribute("type").trim();
										if (typeStr.equals("Money"))
										{
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (count > 0)
											{
												SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, count);
												cfg.addGift(den);
											}
											else
											{
												throw new Exception("Money count=" + count + " is invalid!");
											}
										}
										else if (typeStr.equals("Diamond"))
										{
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (count > 0)
											{
												SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, count);
												cfg.addGift(den);
											}
											else
											{
												throw new Exception("Diamond count=" + count + " is invalid!");
											}
										}
										else if (typeStr.equals("Item"))
										{
											String idStr = ccitem.getAttribute("id").trim();
											int id = Integer.parseInt(idStr);
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
											{
												SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)id, count);
												cfg.addGift(den);
											}
											else
											{
												throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
											}
										}
										else if (typeStr.equals("Equip"))
										{
											String idStr = ccitem.getAttribute("id").trim();
											int id = Integer.parseInt(idStr);
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
											{
												SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_EQUIP, (short)id, count);
												cfg.addGift(den);
											}
											else
											{
												throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
											}
										}
										else if (typeStr.equals("General"))
										{
											String idStr = ccitem.getAttribute("id").trim();
											int id = Integer.parseInt(idStr);
											String cntStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(cntStr);
											if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
											{
												SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_GENERAL, (short)id, count);
												cfg.addGift(den);
											}
											else
											{
												throw new Exception("General id=" + id + " is invalid!");
											}
										}
										else
										{
											throw new Exception("unknown Gift type:" + typeStr + " !");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	public static class ConsumeLvlGift
	{
		public int consumeReq = 0;
		public List<SBean.DropEntryNew> gift = new ArrayList<SBean.DropEntryNew>();
		public ConsumeLvlGift(int consumeReq)
		{
			this.consumeReq = consumeReq;
		}
		public void addGift(int type, int id, int count)
		{
			gift.add(new SBean.DropEntryNew((byte)type, (short)id, count));
		}
	}
	
	public static class ConsumeGiftConfig extends ActivityDisplayConfig
	{
		boolean isConsumeMoney = false;
		List<ConsumeLvlGift> consumeGift = new ArrayList<ConsumeLvlGift>();
		public ConsumeGiftConfig()
		{
			
		}
		
		public int getType()
		{
			return CONSUMEGIFT;
		}
		
//		public boolean isValid()
//		{
//			if (!super.isValid())
//				return false;
//			int minConsumeLvlReq = 1;
//			for (ConsumeLvlGift g : consumeGift)
//			{
//				if (g.consumeReq <= minConsumeLvlReq)
//					return false;
//				minConsumeLvlReq = g.consumeReq;
//				if (g.gift.size() <= 0 || g.gift.size() > 3)
//					return false;
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			int minConsumeLvlReq = 0;
			for (ConsumeLvlGift g : consumeGift)
			{
				if (g.consumeReq <= minConsumeLvlReq)
					return CONFIG_LEVEL_REQ_INVALID;
				minConsumeLvlReq = g.consumeReq;
				if (g.gift.size() <= 0 || g.gift.size() > 3)
					return CONFIG_GIFT_COUNT_INVALID;
			}
			return CONFIG_VALID;
		}
		
		public void setConsumeType(boolean isMoney)
		{
			this.isConsumeMoney = isMoney;
		}
		
		public boolean isConsumeMoney()
		{
			return isConsumeMoney;
		}
		
		public void addGift(ConsumeLvlGift gift)
		{
			consumeGift.add(gift);
		}
		
		public ConsumeLvlGift getConsumeLvlGift(int consume)
		{
			for (ConsumeLvlGift g : consumeGift)
			{
				if (g.consumeReq == consume)
					return g;
			}
			return null;
		}
		
		public int getConsumeLvlCount()
		{
			return consumeGift.size();
		}
		
		public Iterable<ConsumeLvlGift> getConsumeLvlGift()
		{
			return consumeGift;
		}
	}
	
	public class ConsumeGift extends ActivityImpl<ConsumeGiftConfig>
	{
		public ConsumeGift()
		{
			super(ConsumeGiftConfig.class, "ConsumeGift");
			parser.setActivityName("ConsumeGift");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("ConsumeType");
			parser.addEntryName("LevelGift");
			parser.addEntryName("Gift");
			setConfigs(new ArrayList<ConsumeGiftConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<ConsumeGiftConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<ConsumeGiftConfig> getConfig(XMLItem item)
		{
			List<ConsumeGiftConfig> cfgs = new ArrayList<ConsumeGiftConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("ConsumeGift"))
						{
							ConsumeGiftConfig cfg = new ConsumeGiftConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("ConsumeType"))
									{
										cfg.setConsumeType(ccitem.getAttribute("type").trim().equals("Money"));
									}
									else if (aname.equals("LevelGift"))
									{
										String consumeReqStr = ccitem.getAttribute("consume").trim();
										int consumeReq = Integer.parseInt(consumeReqStr);
										if (consumeReq > 0)
										{
											ConsumeLvlGift lvlgift = new ConsumeLvlGift(consumeReq);
											for (XMLItem cccitem : ccitem.childrenItems)
											{
												String aaname = cccitem.getAttribute("name");
												if (aaname.equals("Gift"))
												{
													String typeStr = cccitem.getAttribute("type").trim();
													if (typeStr.equals("Money"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_MONEY, 0, count);
														}
														else
														{
															throw new Exception("Money count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Diamond"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_STONE, 0, count);
														}
														else
														{
															throw new Exception("Diamond count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Item"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_ITEM, id, count);
														}
														else
														{
															throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Equip"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_EQUIP, id, count);
														}
														else
														{
															throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("General"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_GENERAL, id, count);
														}
														else
														{
															throw new Exception("General id=" + id + " is invalid!");
														}
													}
													else
													{
														throw new Exception("unknown Gift type:" + typeStr + " !");
													}
												}
												else
												{
													throw new Exception("unknown LevelGift entry:" + aaname + " !");
												}
											}
											cfg.addGift(lvlgift);
										}
										else
										{
											throw new Exception("LevelGift consumeReq=" + consumeReq + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	
	public static class ConsumeLvlRebate
	{
		public int consumeReq = 0;
		public int rebate = 0;
		public ConsumeLvlRebate(int consumeReq, int rebate)
		{
			this.consumeReq = consumeReq;
			this.rebate = rebate;
		}
	}
	
	public static class ConsumeRebateConfig extends ActivityDisplayConfig
	{
		List<ConsumeLvlRebate> consumeRebate = new ArrayList<ConsumeLvlRebate>();
		public ConsumeRebateConfig()
		{
			
		}
		
		public int getType()
		{
			return CONSUMEREBATE;
		}
		
//		public boolean isValid()
//		{
//			if (!super.isValid())
//				return false;
//			int minConsumeLvlReq = 1;
//			for (ConsumeLvlRebate g : consumeRebate)
//			{
//				if (g.consumeReq <= minConsumeLvlReq)
//					return false;
//				minConsumeLvlReq = g.consumeReq;
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			int minConsumeLvlReq = 0;
			for (ConsumeLvlRebate g : consumeRebate)
			{
				if (g.consumeReq <= minConsumeLvlReq)
					return CONFIG_LEVEL_REQ_INVALID;
				minConsumeLvlReq = g.consumeReq;
			}
			return CONFIG_VALID;
		}
		
		public void addGift(int consumeReq, int rebate)
		{
			consumeRebate.add(new ConsumeLvlRebate(consumeReq, rebate));
		}
		
		public ConsumeLvlRebate getConsumeLvlRebate(int consume)
		{
			for (ConsumeLvlRebate g : consumeRebate)
			{
				if (g.consumeReq == consume)
					return g;
			}
			return null;
		}
		
		public int getConsumeLvlCount()
		{
			return consumeRebate.size();
		}
		
		public Iterable<ConsumeLvlRebate> getConsumeLvlRebate()
		{
			return consumeRebate;
		}
	}
	
	public class ConsumeRebate extends ActivityImpl<ConsumeRebateConfig>
	{
		public ConsumeRebate()
		{
			super(ConsumeRebateConfig.class, "ConsumeRebate");
			parser.setActivityName("ConsumeRebate");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("LevelRebate");
			setConfigs(new ArrayList<ConsumeRebateConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<ConsumeRebateConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<ConsumeRebateConfig> getConfig(XMLItem item)
		{
			List<ConsumeRebateConfig> cfgs = new ArrayList<ConsumeRebateConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("ConsumeRebate"))
						{
							ConsumeRebateConfig cfg = new ConsumeRebateConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("LevelRebate"))
									{
										String consumeReqStr = ccitem.getAttribute("consume").trim();
										int consumeReq = Integer.parseInt(consumeReqStr);
										String rebateStr = ccitem.getAttribute("rebate").trim();
										int rebate = Integer.parseInt(rebateStr);
										if (consumeReq > 0 && rebate > 0)
										{
											cfg.addGift(consumeReq, rebate);
										}
										else
										{
											throw new Exception("LevelRebate consumeReq=" + consumeReq + " rebate=" + rebate + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	public static class DiskBetConfig extends ActivityDisplayConfig
	{
		public int MIN_SCORE = 3000;
		public int MIN_RMB = 500;
		public List<SBean.DropTableEntryNew> mailatt1 = new ArrayList<SBean.DropTableEntryNew>();
		public List<SBean.DropTableEntryNew> mailatt2 = new ArrayList<SBean.DropTableEntryNew>();
		public List<SBean.DiskBetItemsCFGS> mailatt3 = new ArrayList<SBean.DiskBetItemsCFGS>();
		public List<SBean.DiskBetItemsCFGS> mailatt4 = new ArrayList<SBean.DiskBetItemsCFGS>();
		public List<SBean.DiskBetItemsCFGS> mailatt5 = new ArrayList<SBean.DiskBetItemsCFGS>();
		public DiskBetConfig()
		{
		}
		
		public int getType()
		{
			return DISKBET;
		}
		
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;

            if(mailatt1.size()<=0
            		||mailatt2.size()<=0||mailatt3.size()<=0||mailatt4.size()<=0||mailatt5.size()<=0){
            	return CONFIG_DISK_BET_VALUE_INVALID;
            }
            
            
			return CONFIG_VALID;
		}
		
		
	}
	
	public static class RechargeRankConfig extends ActivityDisplayConfig
	{
		public int MIN_SCORE = 3000;
		public int MIN_RMB = 500;
		
		public List<SBean.DiskBetItemsCFGS> mailatt5 = new ArrayList<SBean.DiskBetItemsCFGS>();
		public RechargeRankConfig()
		{
		}
		
		public int getType()
		{
			return RECHARGERANK;
		}
		
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;

            if(mailatt5.size()<=0){
            	return CONFIG_DISK_BET_VALUE_INVALID;
            }
            
            
			return CONFIG_VALID;
		}
		
		
	}
	
	
	public class RechargeRank extends ActivityImpl<RechargeRankConfig>
	{
		public RechargeRank()
		{
			super(RechargeRankConfig.class, "RechargeRank");
			parser.setActivityName("RechargeRank");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("RechargeRankBonus");
			parser.addEntryName("Gift");
			parser.addEntryName("MinRmb");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			
			setConfigs(new ArrayList<RechargeRankConfig>());
		}
					
		public SBean.FreeVit[] getFreeVit()
		{
			SBean.FreeVit[] freevits = null; 
			List<RechargeRankConfig> cfgs = getOpenedConfigs();
			if (!cfgs.isEmpty())
			{
				RechargeRankConfig cfg = cfgs.get(0);
			}
			return freevits;
		}
		
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<RechargeRankConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<RechargeRankConfig> getConfig(XMLItem item)
		{
			List<RechargeRankConfig> cfgs = new ArrayList<RechargeRankConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("RechargeRank"))
						{
							RechargeRankConfig cfg = new RechargeRankConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("MinRmb"))
									{
									    String timeStr = ccitem.getAttribute("rmb").trim();
									
									    cfg.MIN_RMB= (int)Integer.parseInt(timeStr);
									}
									else if (aname.equals("RechargeRankBonus"))
									{
										if (ccitem.getAttribute("ttype").equals("3")){
											
											String rankmin1 = ccitem.getAttribute("rankmin");
											String rankmax1 = ccitem.getAttribute("rankmax");
											DiskBetItemsCFGS den2 = new SBean.DiskBetItemsCFGS();
											den2.type=3;
											den2.rankmin = (short) Integer.parseInt(rankmin1);
											den2.rankmax = (short) Integer.parseInt(rankmax1);
											den2.eItems = new ArrayList<DropEntryNew>();
											cfg.mailatt5.add(den2);
											
											for (XMLItem ccitem1 : ccitem.childrenItems)
											{
												String aname3 = ccitem1.getAttribute("name");
												if (aname3.equals("Gift"))
												{
													String aname4 = ccitem1.getAttribute("type");
													if (aname4.equals("Money"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment moeny count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Diamond"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment diamond count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Item"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Equip"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_EQUIP, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("General"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_GENERAL, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
													else if (aname4.equals("Pool"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ACTIVITY_POOL, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
												}
											}
											
									}
									}
									
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();
			}
			Collections.sort(cfgs,  new Comparator<RechargeRankConfig>()
										{
											public int compare(RechargeRankConfig l, RechargeRankConfig r)
											{
												return l.start-r.start;
											}
										});
			return cfgs;
		}
	}
	
	
	public class DiskBet extends ActivityImpl<DiskBetConfig>
	{
		public DiskBet()
		{
			super(DiskBetConfig.class, "DiskBet");
			parser.setActivityName("DiskBet");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DiskBetItemList");
			parser.addEntryName("DiskBetBonus");
			parser.addEntryName("Gift");
			parser.addEntryName("MinScore");
			parser.addEntryName("MinRmb");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			
			setConfigs(new ArrayList<DiskBetConfig>());
		}
					
		public SBean.FreeVit[] getFreeVit()
		{
			SBean.FreeVit[] freevits = null; 
			List<DiskBetConfig> cfgs = getOpenedConfigs();
			if (!cfgs.isEmpty())
			{
				DiskBetConfig cfg = cfgs.get(0);
			}
			return freevits;
		}
		
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<DiskBetConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<DiskBetConfig> getConfig(XMLItem item)
		{
			List<DiskBetConfig> cfgs = new ArrayList<DiskBetConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("DiskBet"))
						{
							DiskBetConfig cfg = new DiskBetConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("MinScore"))
									{
									    String timeStr = ccitem.getAttribute("score").trim();
									
									    cfg.MIN_SCORE= (int)Integer.parseInt(timeStr);
									}
									else if (aname.equals("MinRmb"))
									{
									    String timeStr = ccitem.getAttribute("rmb").trim();
									
									    cfg.MIN_RMB= (int)Integer.parseInt(timeStr);
									}
									else if (aname.equals("DiskBetItemList"))
									{
										if (ccitem.getAttribute("ttype").equals("1"))
										{
											for (XMLItem ccitem1 : ccitem.childrenItems)
											{
												String aname1 = ccitem1.getAttribute("name");
												if (aname1.equals("Gift"))
												{
													String aname2 = ccitem1.getAttribute("type");
													if (aname2.equals("Money"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt1.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment moeny count=" + count + " is invalid!");
														}
													}
													else if (aname2.equals("Diamond"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt1.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment diamond count=" + count + " is invalid!");
														}
													}
													else if (aname2.equals("Item"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)id, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt1.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname2.equals("Equip"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_EQUIP, (short)id, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt1.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname2.equals("General"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_GENERAL, (short)id, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt1.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
													else if (aname2.equals("Pool"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ACTIVITY_POOL, (short)id, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt1.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
												}
											}
										}else if (ccitem.getAttribute("ttype").equals("2")){
											
											for (XMLItem ccitem1 : ccitem.childrenItems)
											{
												String aname3 = ccitem1.getAttribute("name");
												if (aname3.equals("Gift"))
												{
													String aname4 = ccitem1.getAttribute("type");
													if (aname4.equals("Money"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt2.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment moeny count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Diamond"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt2.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment diamond count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Item"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)id, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt2.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Equip"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_EQUIP, (short)id, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt2.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("General"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_GENERAL, (short)id, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt2.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
													else if (aname4.equals("Pool"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ACTIVITY_POOL, (short)id, count);
															String weight = ccitem1.getAttribute("weight").trim();
															float wei = Float.parseFloat(weight);
															DropTableEntryNew den1 = new DropTableEntryNew(den,wei);
															cfg.mailatt2.add(den1);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
												}
											}
												
										}
									}
									else if (aname.equals("DiskBetBonus"))
									{
										if (ccitem.getAttribute("ttype").equals("1"))
										{
											String rankmin1 = ccitem.getAttribute("rankmin");
											String rankmax1 = ccitem.getAttribute("rankmax");
											DiskBetItemsCFGS den2 = new SBean.DiskBetItemsCFGS();
											den2.type=1;
											den2.rankmin = (short) Integer.parseInt(rankmin1);
											den2.rankmax = (short) Integer.parseInt(rankmax1);
											den2.eItems = new ArrayList<DropEntryNew>();
											cfg.mailatt3.add(den2);
											for (XMLItem ccitem1 : ccitem.childrenItems)
											{
												String aname1 = ccitem1.getAttribute("name");
												if (aname1.equals("Gift"))
												{
													String aname2 = ccitem1.getAttribute("type");
													if (aname2.equals("Money"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, count);														
															
															den2.eItems.add(den);									
														}
														else
														{
															throw new Exception("Mail attachment moeny count=" + count + " is invalid!");
														}
													}
													else if (aname2.equals("Diamond"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment diamond count=" + count + " is invalid!");
														}
													}
													else if (aname2.equals("Item"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname2.equals("Equip"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_EQUIP, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname2.equals("General"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_GENERAL, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
													else if (aname2.equals("Pool"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ACTIVITY_POOL, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
												}
											}
										}else if (ccitem.getAttribute("ttype").equals("2")){
											
											String rankmin1 = ccitem.getAttribute("rankmin");
											String rankmax1 = ccitem.getAttribute("rankmax");
											DiskBetItemsCFGS den2 = new SBean.DiskBetItemsCFGS();
											den2.type=2;
											den2.rankmin = (short) Integer.parseInt(rankmin1);
											den2.rankmax = (short) Integer.parseInt(rankmax1);
											den2.eItems = new ArrayList<DropEntryNew>();
											cfg.mailatt4.add(den2);
											
											for (XMLItem ccitem1 : ccitem.childrenItems)
											{
												String aname3 = ccitem1.getAttribute("name");
												
												if (aname3.equals("Gift"))
												{
													String aname4 = ccitem1.getAttribute("type");
													if (aname4.equals("Money"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment moeny count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Diamond"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment diamond count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Item"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Equip"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_EQUIP, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("General"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_GENERAL, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
													else if (aname4.equals("Pool"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ACTIVITY_POOL, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
												}
											}
												
										}else if (ccitem.getAttribute("ttype").equals("3")){
											
											String rankmin1 = ccitem.getAttribute("rankmin");
											String rankmax1 = ccitem.getAttribute("rankmax");
											DiskBetItemsCFGS den2 = new SBean.DiskBetItemsCFGS();
											den2.type=3;
											den2.rankmin = (short) Integer.parseInt(rankmin1);
											den2.rankmax = (short) Integer.parseInt(rankmax1);
											den2.eItems = new ArrayList<DropEntryNew>();
											cfg.mailatt5.add(den2);
											
											for (XMLItem ccitem1 : ccitem.childrenItems)
											{
												String aname3 = ccitem1.getAttribute("name");
												if (aname3.equals("Gift"))
												{
													String aname4 = ccitem1.getAttribute("type");
													if (aname4.equals("Money"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment moeny count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Diamond"))
													{
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (count > 0 || count <= 100)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment diamond count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Item"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("Equip"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_EQUIP, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (aname4.equals("General"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_GENERAL, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
													else if (aname4.equals("Pool"))
													{
														String idStr = ccitem1.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = ccitem1.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ACTIVITY_POOL, (short)id, count);
															den2.eItems.add(den);
														}
														else
														{
															throw new Exception("Mail attachment general id=" + id + " is invalid!");
														}
													}
												}
											}
											
									}
									}
									
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();
			}
			Collections.sort(cfgs,  new Comparator<DiskBetConfig>()
										{
											public int compare(DiskBetConfig l, DiskBetConfig r)
											{
												return l.start-r.start;
											}
										});
			return cfgs;
		}
	}
	
	public static class UpgradeLvlGift
	{
		public int lvlReq = 0;
		public List<SBean.DropEntryNew> gift = new ArrayList<SBean.DropEntryNew>();
		public List<SBean.DropEntryNew> giftEx = new ArrayList<SBean.DropEntryNew>();
		public UpgradeLvlGift(int lvlReq)
		{
			this.lvlReq = lvlReq;
		}
		public void addGift(int type, int id, int count, boolean isEx)
		{
			if (isEx)
				giftEx.add(new SBean.DropEntryNew((byte)type, (short)id, count));
			else
				gift.add(new SBean.DropEntryNew((byte)type, (short)id, count));
		}
	}
	
	public static class UpgradeGiftConfig extends ActivityDisplayConfig
	{
		int limitedEndTime;
		List<UpgradeLvlGift> upgradeGift = new ArrayList<UpgradeLvlGift>();
		public UpgradeGiftConfig()
		{
			
		}
		
		public int getType()
		{
			return UPGRADEGIFT;
		}
		
//		public boolean isValid()
//		{
//			if (!super.isValid())
//				return false;
//			if (getLimitedEndTime() <= getStartTime() || getLimitedEndTime() >= getEndTime())
//				return false;
//			int minLvlReq = 1;
//			for (UpgradeLvlGift g : upgradeGift)
//			{
//				if (g.lvlReq <= minLvlReq)
//					return false;
//				minLvlReq = g.lvlReq;
//				if (g.gift.size() <= 0 || g.gift.size() > 3)
//					return false;
//				if (g.giftEx.size() != 0 && g.giftEx.size() != 1)
//					return false;
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			if (getLimitedEndTime() <= getStartTime() || getLimitedEndTime() >= getEndTime())
				return CONFIG_LIMITEDTIME_INVALID;
			int minLvlReq = 0;
			for (UpgradeLvlGift g : upgradeGift)
			{
				if (g.lvlReq <= minLvlReq)
					return CONFIG_LEVEL_REQ_INVALID;
				minLvlReq = g.lvlReq;
				if (g.gift.size() <= 0 || g.gift.size() > 3)
					return CONFIG_GIFT_COUNT_INVALID;
				if (g.giftEx.size() != 0 && g.giftEx.size() != 1)
					return CONFIG_EXTRA_GIFT_COUNT_INVALID;
			}
			return CONFIG_VALID;
		}
		
		public void setLimitedEndTime(int time)
		{
			limitedEndTime = time;
		}
		
		public int getLimitedEndTime()
		{
			return (limitedEndTime - getRealStartTime()) + getStartTime();
		}
		
		public boolean isInLimitedTime(int curTime)
		{
			return curTime >= getStartTime() && curTime <= getLimitedEndTime();
		}
		
		public void addGift(UpgradeLvlGift gift)
		{
			upgradeGift.add(gift);
		}
		
		public UpgradeLvlGift getUpgradeLvlGift(int lvl)
		{
			for (UpgradeLvlGift g : upgradeGift)
			{
				if (g.lvlReq == lvl)
					return g;
			}
			return null;
		}
		
		public int getUpgradeLvlCount()
		{
			return upgradeGift.size();
		}
		
		public Iterable<UpgradeLvlGift> getUpgradeLvlGift()
		{
			return upgradeGift;
		}
	}
	
	public class UpgradeGift extends ActivityImpl<UpgradeGiftConfig>
	{
		public UpgradeGift()
		{
			super(UpgradeGiftConfig.class, "UpgradeGift");
			parser.setActivityName("UpgradeGift");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("LimitedEndTime");
			parser.addEntryName("LevelGift");
			parser.addEntryName("Gift");
			parser.addEntryName("GiftEx");
			setConfigs(new ArrayList<UpgradeGiftConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<UpgradeGiftConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<UpgradeGiftConfig> getConfig(XMLItem item)
		{
			List<UpgradeGiftConfig> cfgs = new ArrayList<UpgradeGiftConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("UpgradeGift"))
						{
							UpgradeGiftConfig cfg = new UpgradeGiftConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("LimitedEndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.setLimitedEndTime((int)(date.getTime()/1000) + 8*3600);
									}
									else if (aname.equals("LevelGift"))
									{
										String lvlReqStr = ccitem.getAttribute("level").trim();
										int lvlReq = Integer.parseInt(lvlReqStr);
										if (lvlReq > 0)
										{
											UpgradeLvlGift lvlgift = new UpgradeLvlGift(lvlReq);
											for (XMLItem cccitem : ccitem.childrenItems)
											{
												String aaname = cccitem.getAttribute("name");
												if (aaname.equals("Gift") || aaname.equals("GiftEx"))
												{
													boolean isGiftEx = aaname.equals("GiftEx");
													String typeStr = cccitem.getAttribute("type").trim();
													if (typeStr.equals("Money"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_MONEY, 0, count, isGiftEx);
														}
														else
														{
															throw new Exception("Money count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Diamond"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_STONE, 0, count, isGiftEx);
														}
														else
														{
															throw new Exception("Diamond count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Item"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_ITEM, id, count, isGiftEx);
														}
														else
														{
															throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Equip"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_EQUIP, id, count, isGiftEx);
														}
														else
														{
															throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("General"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_GENERAL, id, count, isGiftEx);
														}
														else
														{
															throw new Exception("General id=" + id + " is invalid!");
														}
													}
													else
													{
														throw new Exception("unknown Gift type:" + typeStr + " !");
													}
												}
												else
												{
													throw new Exception("unknown LevelGift entry:" + aaname + " !");
												}
											}
											cfg.addGift(lvlgift);
										}
										else
										{
											throw new Exception("LevelGift lvlReq=" + lvlReq + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	
	public static class PropsGift
	{
		public List<SBean.DropEntryNew> props = new ArrayList<SBean.DropEntryNew>();
		public SBean.DropEntryNew gift;
		public PropsGift()
		{
		}
		
		public void setGift(int type, int id, int count)
		{
			gift = new SBean.DropEntryNew((byte)type, (short)id, count);
		}
		
		public SBean.DropEntryNew getGift()
		{
			return gift;
		}
		
		public void addProp(int type, int id, int count)
		{
			props.add(new SBean.DropEntryNew((byte)type, (short)id, count));
		}
		
		List<SBean.DropEntryNew> getProps()
		{
			return props;
		}
		
//		public boolean isValid()
//		{
//			if (gift == null || props.isEmpty() || props.size() > 4)
//				return false;
//			for (int i = 0; i < props.size(); ++i)
//			{
//				SBean.DropEntryNew e1 = props.get(i);
//				for (int j = i+1; j < props.size(); ++j)
//				{
//					SBean.DropEntryNew e2 = props.get(j);
//					if (e1.type == e2.type && e1.id == e2.id)
//						return false;
//				}
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			if (gift == null || props.isEmpty() || props.size() > 4)
				return CONFIG_PROPS_OR_GIFT_COUNT_INVALID;
			for (int i = 0; i < props.size(); ++i)
			{
				SBean.DropEntryNew e1 = props.get(i);
				for (int j = i+1; j < props.size(); ++j)
				{
					SBean.DropEntryNew e2 = props.get(j);
					if (e1.type == e2.type && e1.id == e2.id)
						return CONFIG_PROPS_ID_DUPLICATION_INVALID;
				}
			}
			return CONFIG_VALID;
		}
	}
	
	public static class ExchangeGiftConfig extends ActivityDisplayConfig
	{
		List<PropsGift> exchangeGift = new ArrayList<PropsGift>();
		public ExchangeGiftConfig()
		{
			
		}
		
		public int getType()
		{
			return EXCHANGEGIFT;
		}
		
//		public boolean isValid()
//		{
//			if (!super.isValid())
//				return false;
//			for (PropsGift g : exchangeGift)
//			{
//				if (!g.isValid())
//					return false;
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			for (PropsGift g : exchangeGift)
			{
				errorCode = g.checkValid();
				if (errorCode != CONFIG_VALID)
					return errorCode;
			}
			return CONFIG_VALID;
		}
		
		public void addGift(PropsGift gift)
		{
			exchangeGift.add(gift);
		}
		
		public PropsGift getPropsGift(int index)
		{
			if (index >= 0 && index < exchangeGift.size())
			{
				return exchangeGift.get(index);
			}
			return null;
		}
		
		public int getGiftCount()
		{
			return exchangeGift.size();
		}
		
		public Iterable<PropsGift> getExchangeGift()
		{
			return exchangeGift;
		}
	}
	
	public class ExchangeGift extends ActivityImpl<ExchangeGiftConfig>
	{
		public ExchangeGift()
		{
			super(ExchangeGiftConfig.class, "ExchangeGift");
			parser.setActivityName("ExchangeGift");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("Gift");
			parser.addEntryName("Prop");
			setConfigs(new ArrayList<ExchangeGiftConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<ExchangeGiftConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<ExchangeGiftConfig> getConfig(XMLItem item)
		{
			List<ExchangeGiftConfig> cfgs = new ArrayList<ExchangeGiftConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("ExchangeGift"))
						{
							ExchangeGiftConfig cfg = new ExchangeGiftConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("Gift"))
									{
										PropsGift propsGift = new PropsGift();
										String gtypeStr = ccitem.getAttribute("type").trim();
										if (gtypeStr.equals("Money"))
										{
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (count > 0)
											{
												propsGift.setGift(GameData.COMMON_TYPE_MONEY, 0, count);
											}
											else
											{
												throw new Exception("Money count=" + count + " is invalid!");
											}
										}
										else if (gtypeStr.equals("Diamond"))
										{
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (count > 0)
											{
												propsGift.setGift(GameData.COMMON_TYPE_STONE, 0, count);
											}
											else
											{
												throw new Exception("Diamond count=" + count + " is invalid!");
											}
										}
										else if (gtypeStr.equals("Item"))
										{
											String idStr = ccitem.getAttribute("id").trim();
											int id = Integer.parseInt(idStr);
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
											{
												propsGift.setGift(GameData.COMMON_TYPE_ITEM, id, count);
											}
											else
											{
												throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
											}
										}
										else if (gtypeStr.equals("Equip"))
										{
											String idStr = ccitem.getAttribute("id").trim();
											int id = Integer.parseInt(idStr);
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
											{
												propsGift.setGift(GameData.COMMON_TYPE_EQUIP, id, count);
											}
											else
											{
												throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
											}
										}
										else if (gtypeStr.equals("General"))
										{
											String idStr = ccitem.getAttribute("id").trim();
											int id = Integer.parseInt(idStr);
											String cntStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(cntStr);
											if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
											{
												propsGift.setGift(GameData.COMMON_TYPE_GENERAL, id, count);
											}
											else
											{
												throw new Exception("General id=" + id + " is invalid!");
											}
										}
										else
										{
											throw new Exception("unknown Gift type:" + gtypeStr + " !");
										}
										for (XMLItem cccitem : ccitem.childrenItems)
										{
											String aaname = cccitem.getAttribute("name");
											if (aaname.equals("Prop"))
											{
												String typeStr = cccitem.getAttribute("type").trim();
												if (typeStr.equals("Money"))
												{
													String countStr = cccitem.getAttribute("count").trim();
													int count = Integer.parseInt(countStr);
													if (count > 0)
													{
														propsGift.addProp(GameData.COMMON_TYPE_MONEY, 0, count);
													}
													else
													{
														throw new Exception("Money count=" + count + " is invalid!");
													}
												}
												else if (typeStr.equals("Diamond"))
												{
													String countStr = cccitem.getAttribute("count").trim();
													int count = Integer.parseInt(countStr);
													if (count > 0)
													{
														propsGift.addProp(GameData.COMMON_TYPE_STONE, 0, count);
													}
													else
													{
														throw new Exception("Diamond count=" + count + " is invalid!");
													}
												}
												else if (typeStr.equals("Item"))
												{
													String idStr = cccitem.getAttribute("id").trim();
													int id = Integer.parseInt(idStr);
													String countStr = cccitem.getAttribute("count").trim();
													int count = Integer.parseInt(countStr);
													if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
													{
														propsGift.addProp(GameData.COMMON_TYPE_ITEM, id, count);
													}
													else
													{
														throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
													}
												}
												else if (typeStr.equals("Equip"))
												{
													String idStr = cccitem.getAttribute("id").trim();
													int id = Integer.parseInt(idStr);
													String countStr = cccitem.getAttribute("count").trim();
													int count = Integer.parseInt(countStr);
													if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
													{
														propsGift.addProp(GameData.COMMON_TYPE_EQUIP, id, count);
													}
													else
													{
														throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
													}
												}
												else if (typeStr.equals("General"))
												{
													String idStr = cccitem.getAttribute("id").trim();
													int id = Integer.parseInt(idStr);
													String cntStr = cccitem.getAttribute("count").trim();
													int count = Integer.parseInt(cntStr);
													if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
													{
														propsGift.addProp(GameData.COMMON_TYPE_GENERAL, id, count);
													}
													else
													{
														throw new Exception("General id=" + id + " is invalid!");
													}
												}
												else
												{
													throw new Exception("unknown prop type:" + typeStr + " !");
												}
											}
											else
											{
												throw new Exception("unknown Gift entry:" + aaname + " !");
											}
										}
										cfg.addGift(propsGift);
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	public static class GeneralGift
	{
		public List<Short> generals = new ArrayList<Short>();
		public SBean.DropEntryNew gift;
		public GeneralGift()
		{
		}
		
		public void setGift(int type, int id, int count)
		{
			gift = new SBean.DropEntryNew((byte)type, (short)id, count);
		}
		
		public SBean.DropEntryNew getGift()
		{
			return gift;
		}
		
		public void addGeneral(short id)
		{
			generals.add(id);
		}
		
		List<Short> getGenerals()
		{
			return generals;
		}
		
//		public boolean isValid()
//		{
//			if (gift == null || generals.isEmpty() || generals.size() > 5)
//				return false;
//			for (int i = 0; i < generals.size(); ++i)
//			{
//				short g1 = generals.get(i);
//				for (int j = i+1; j < generals.size(); ++j)
//				{
//					short g2 = generals.get(j);
//					if (g1 == g2)
//						return false;
//				}
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			if (gift == null || generals.isEmpty() || generals.size() > 5)
				return CONFIG_GENERALS_OR_GIFT_COUNT_INVALID;
			for (int i = 0; i < generals.size(); ++i)
			{
				short g1 = generals.get(i);
				for (int j = i+1; j < generals.size(); ++j)
				{
					short g2 = generals.get(j);
					if (g1 == g2)
						return CONFIG_GENERALS_ID_DUPLICATION_INVALID;
				}
			}
			return CONFIG_VALID;
		}
	}
	
	public static class GatherGiftConfig extends ActivityDisplayConfig
	{
		List<GeneralGift> gatherGift = new ArrayList<GeneralGift>();
		public GatherGiftConfig()
		{
			
		}
		
		public int getType()
		{
			return GATHERGIFT;
		}
		
//		public boolean isValid()
//		{
//			if (!super.isValid())
//				return false;
//			for (GeneralGift g : gatherGift)
//			{
//				if (!g.isValid())
//					return false;
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			for (GeneralGift g : gatherGift)
			{
				errorCode = g.checkValid();
				if (errorCode != CONFIG_VALID)
					return errorCode;
			}
			return CONFIG_VALID;
		}
		
		public void addGift(GeneralGift gift)
		{
			gatherGift.add(gift);
		}
		
		public GeneralGift getGeneralGift(int index)
		{
			if (index >= 0 && index < gatherGift.size())
			{
				return gatherGift.get(index);
			}
			return null;
		}
		
		public int getGiftCount()
		{
			return gatherGift.size();
		}
		
		public Iterable<GeneralGift> getGatherGift()
		{
			return gatherGift;
		}
	}
	
	
	
	public class GatherGift extends ActivityImpl<GatherGiftConfig>
	{
		public GatherGift()
		{
			super(GatherGiftConfig.class, "GatherGift");
			parser.setActivityName("GatherGift");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("Gift");
			parser.addEntryName("General");
			setConfigs(new ArrayList<GatherGiftConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<GatherGiftConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<GatherGiftConfig> getConfig(XMLItem item)
		{
			List<GatherGiftConfig> cfgs = new ArrayList<GatherGiftConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("GatherGift"))
						{
							GatherGiftConfig cfg = new GatherGiftConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("Gift"))
									{
										GeneralGift generalsGift = new GeneralGift();
										String gtypeStr = ccitem.getAttribute("type").trim();
										if (gtypeStr.equals("Money"))
										{
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (count > 0)
											{
												generalsGift.setGift(GameData.COMMON_TYPE_MONEY, 0, count);
											}
											else
											{
												throw new Exception("Money count=" + count + " is invalid!");
											}
										}
										else if (gtypeStr.equals("Diamond"))
										{
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (count > 0)
											{
												generalsGift.setGift(GameData.COMMON_TYPE_STONE, 0, count);
											}
											else
											{
												throw new Exception("Diamond count=" + count + " is invalid!");
											}
										}
										else if (gtypeStr.equals("Item"))
										{
											String idStr = ccitem.getAttribute("id").trim();
											int id = Integer.parseInt(idStr);
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
											{
												generalsGift.setGift(GameData.COMMON_TYPE_ITEM, id, count);
											}
											else
											{
												throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
											}
										}
										else if (gtypeStr.equals("Equip"))
										{
											String idStr = ccitem.getAttribute("id").trim();
											int id = Integer.parseInt(idStr);
											String countStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(countStr);
											if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
											{
												generalsGift.setGift(GameData.COMMON_TYPE_EQUIP, id, count);
											}
											else
											{
												throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
											}
										}
										else if (gtypeStr.equals("General"))
										{
											String idStr = ccitem.getAttribute("id").trim();
											int id = Integer.parseInt(idStr);
											String cntStr = ccitem.getAttribute("count").trim();
											int count = Integer.parseInt(cntStr);
											if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
											{
												generalsGift.setGift(GameData.COMMON_TYPE_GENERAL, id, count);
											}
											else
											{
												throw new Exception("General id=" + id + " is invalid!");
											}
										}
										for (XMLItem cccitem : ccitem.childrenItems)
										{
											String aaname = cccitem.getAttribute("name");
											if (aaname.equals("General"))
											{
												String idStr = cccitem.getAttribute("id").trim();
												short id = Short.parseShort(idStr);
												if (GameData.getInstance().getGeneralCFG((short)id) != null)
												{
													generalsGift.addGeneral(id);
												}
												else
												{
													throw new Exception("General id=" + id + " is invalid!");
												}
											}
											else
											{
												throw new Exception("unknown Gift entry:" + aaname + " !");
											}
										}
										cfg.addGift(generalsGift);
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	public static class PayLvlGift
	{
		public int payReq = 0;
		public List<SBean.DropEntryNew> gift = new ArrayList<SBean.DropEntryNew>();
		public PayLvlGift(int payReq)
		{
			this.payReq = payReq;
		}
		public void addGift(int type, int id, int count)
		{
			gift.add(new SBean.DropEntryNew((byte)type, (short)id, count));
		}
	}
	
	
	public static class DiskLvlGift
	{
		public int payReq = 0;
		public List<SBean.DropEntryNew> gift = new ArrayList<SBean.DropEntryNew>();
		public DiskLvlGift(int payReq)
		{
			this.payReq = payReq;
		}
		public void addGift(int type, int id, int count)
		{
			gift.add(new SBean.DropEntryNew((byte)type, (short)id, count));
		}
	}
	
	public static class DiskGiftConfig extends ActivityDisplayConfig
	{
		List<DiskLvlGift> payGift = new ArrayList<DiskLvlGift>();
		public DiskGiftConfig()
		{
			
		}
		
		public int getType()
		{
			return DISKGIFT;
		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			int minPayLvlReq = 0;
			for (DiskLvlGift g : payGift)
			{
				if (g.payReq <= minPayLvlReq)
					return CONFIG_LEVEL_REQ_INVALID;
				minPayLvlReq = g.payReq;
				if (g.gift.size() <= 0 || g.gift.size() > 3)
					return CONFIG_GIFT_COUNT_INVALID;
			}
			return CONFIG_VALID;
		}

		public void addGift(DiskLvlGift gift)
		{
			payGift.add(gift);
		}
		
		public DiskLvlGift getPayLvlGift(int pay)
		{
			for (DiskLvlGift g : payGift)
			{
				if (g.payReq == pay)
					return g;
			}
			return null;
		}
		
		public int getDiskLvlCount()
		{
			return payGift.size();
		}
		
		public Iterable<DiskLvlGift> getDiskLvlGift()
		{
			return payGift;
		}
	}
	
	
	public static class RechargeLvlGift
	{
		public int payReq = 0;
		public List<SBean.DropEntryNew> gift = new ArrayList<SBean.DropEntryNew>();
		public RechargeLvlGift(int payReq)
		{
			this.payReq = payReq;
		}
		public void addGift(int type, int id, int count)
		{
			gift.add(new SBean.DropEntryNew((byte)type, (short)id, count));
		}
	}
	
	public static class RechargeGiftConfig extends ActivityDisplayConfig
	{
		List< RechargeLvlGift> payGift = new ArrayList< RechargeLvlGift>();
		public  RechargeGiftConfig()
		{
			
		}
		
		public int getType()
		{
			return RECHARGEGIFT;
		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			int minPayLvlReq = 0;
			for (RechargeLvlGift g : payGift)
			{
				if (g.payReq <= minPayLvlReq)
					return CONFIG_LEVEL_REQ_INVALID;
				minPayLvlReq = g.payReq;
				if (g.gift.size() <= 0 || g.gift.size() > 3)
					return CONFIG_GIFT_COUNT_INVALID;
			}
			return CONFIG_VALID;
		}

		public void addGift(RechargeLvlGift gift)
		{
			payGift.add(gift);
		}
		
		public RechargeLvlGift getPayLvlGift(int pay)
		{
			for (RechargeLvlGift g : payGift)
			{
				if (g.payReq == pay)
					return g;
			}
			return null;
		}
		
		public int getDiskLvlCount()
		{
			return payGift.size();
		}
		
		public Iterable<RechargeLvlGift> getRechargeLvlGift()
		{
			return payGift;
		}
	}
	
	public static class PayGiftConfig extends ActivityDisplayConfig
	{
		List<PayLvlGift> payGift = new ArrayList<PayLvlGift>();
		public PayGiftConfig()
		{
			
		}
		
		public int getType()
		{
			return PAYGIFT;
		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			int minPayLvlReq = 0;
			for (PayLvlGift g : payGift)
			{
				if (g.payReq <= minPayLvlReq)
					return CONFIG_LEVEL_REQ_INVALID;
				minPayLvlReq = g.payReq;
				if (g.gift.size() <= 0 || g.gift.size() > 3)
					return CONFIG_GIFT_COUNT_INVALID;
			}
			return CONFIG_VALID;
		}

		public void addGift(PayLvlGift gift)
		{
			payGift.add(gift);
		}
		
		public PayLvlGift getPayLvlGift(int pay)
		{
			for (PayLvlGift g : payGift)
			{
				if (g.payReq == pay)
					return g;
			}
			return null;
		}
		
		public int getPayLvlCount()
		{
			return payGift.size();
		}
		
		public Iterable<PayLvlGift> getPayLvlGift()
		{
			return payGift;
		}
	}
	
	public class PayGift extends ActivityImpl<PayGiftConfig>
	{
		public PayGift()
		{
			super(PayGiftConfig.class, "PayLevelGift");
			parser.setActivityName("PayLevelGift");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("LevelGift");
			parser.addEntryName("Gift");
			setConfigs(new ArrayList<PayGiftConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<PayGiftConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<PayGiftConfig> getConfig(XMLItem item)
		{
			List<PayGiftConfig> cfgs = new ArrayList<PayGiftConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("PayLevelGift"))
						{
							PayGiftConfig cfg = new PayGiftConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("LevelGift"))
									{
										String payReqStr = ccitem.getAttribute("pay").trim();
										int payReq = Integer.parseInt(payReqStr);
										if (payReq > 0)
										{
											PayLvlGift lvlgift = new PayLvlGift(payReq);
											for (XMLItem cccitem : ccitem.childrenItems)
											{
												String aaname = cccitem.getAttribute("name");
												if (aaname.equals("Gift"))
												{
													String typeStr = cccitem.getAttribute("type").trim();
													if (typeStr.equals("Money"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_MONEY, 0, count);
														}
														else
														{
															throw new Exception("Money count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Diamond"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_STONE, 0, count);
														}
														else
														{
															throw new Exception("Diamond count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Item"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_ITEM, id, count);
														}
														else
														{
															throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Equip"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_EQUIP, id, count);
														}
														else
														{
															throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("General"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_GENERAL, id, count);
														}
														else
														{
															throw new Exception("General id=" + id + " is invalid!");
														}
													}
													else
													{
														throw new Exception("unknown Gift type:" + typeStr + " !");
													}
												}
												else
												{
													throw new Exception("unknown LevelGift entry:" + aaname + " !");
												}
											}
											cfg.addGift(lvlgift);
										}
										else
										{
											throw new Exception("LevelGift payReq=" + payReq + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	
	public class RechargeGift extends ActivityImpl<RechargeGiftConfig>
	{
		public RechargeGift()
		{
			super(RechargeGiftConfig.class, "RechargeGift");
			parser.setActivityName("RechargeGift");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("LevelGift");
			parser.addEntryName("Gift");
			setConfigs(new ArrayList<RechargeGiftConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<RechargeGiftConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<RechargeGiftConfig> getConfig(XMLItem item)
		{
			List<RechargeGiftConfig> cfgs = new ArrayList<RechargeGiftConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("RechargeGift"))
						{
							RechargeGiftConfig cfg = new RechargeGiftConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("LevelGift"))
									{
										String payReqStr = ccitem.getAttribute("pay").trim();
										int payReq = Integer.parseInt(payReqStr);
										if (payReq > 0)
										{
											RechargeLvlGift lvlgift = new RechargeLvlGift(payReq);
											for (XMLItem cccitem : ccitem.childrenItems)
											{
												String aaname = cccitem.getAttribute("name");
												if (aaname.equals("Gift"))
												{
													String typeStr = cccitem.getAttribute("type").trim();
													if (typeStr.equals("Money"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_MONEY, 0, count);
														}
														else
														{
															throw new Exception("Money count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Diamond"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_STONE, 0, count);
														}
														else
														{
															throw new Exception("Diamond count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Item"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_ITEM, id, count);
														}
														else
														{
															throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Equip"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_EQUIP, id, count);
														}
														else
														{
															throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("General"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_GENERAL, id, count);
														}
														else
														{
															throw new Exception("General id=" + id + " is invalid!");
														}
													}
													else
													{
														throw new Exception("unknown Gift type:" + typeStr + " !");
													}
												}
												else
												{
													throw new Exception("unknown LevelGift entry:" + aaname + " !");
												}
											}
											cfg.addGift(lvlgift);
										}
										else
										{
											throw new Exception("LevelGift payReq=" + payReq + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	
	public class DiskGift extends ActivityImpl<DiskGiftConfig>
	{
		public DiskGift()
		{
			super(DiskGiftConfig.class, "DiskGift");
			parser.setActivityName("DiskGift");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("LevelGift");
			parser.addEntryName("Gift");
			setConfigs(new ArrayList<DiskGiftConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<DiskGiftConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<DiskGiftConfig> getConfig(XMLItem item)
		{
			List<DiskGiftConfig> cfgs = new ArrayList<DiskGiftConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("DiskGift"))
						{
							DiskGiftConfig cfg = new DiskGiftConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("LevelGift"))
									{
										String payReqStr = ccitem.getAttribute("pay").trim();
										int payReq = Integer.parseInt(payReqStr);
										if (payReq > 0)
										{
											DiskLvlGift lvlgift = new DiskLvlGift(payReq);
											for (XMLItem cccitem : ccitem.childrenItems)
											{
												String aaname = cccitem.getAttribute("name");
												if (aaname.equals("Gift"))
												{
													String typeStr = cccitem.getAttribute("type").trim();
													if (typeStr.equals("Money"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_MONEY, 0, count);
														}
														else
														{
															throw new Exception("Money count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Diamond"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_STONE, 0, count);
														}
														else
														{
															throw new Exception("Diamond count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Item"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_ITEM, id, count);
														}
														else
														{
															throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Equip"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_EQUIP, id, count);
														}
														else
														{
															throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("General"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_GENERAL, id, count);
														}
														else
														{
															throw new Exception("General id=" + id + " is invalid!");
														}
													}
													else
													{
														throw new Exception("unknown Gift type:" + typeStr + " !");
													}
												}
												else
												{
													throw new Exception("unknown LevelGift entry:" + aaname + " !");
												}
											}
											cfg.addGift(lvlgift);
										}
										else
										{
											throw new Exception("LevelGift payReq=" + payReq + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	public static class LimitedGift
	{
		public int vipReq = 0;
		public int maxBuyTimes = 1;
		public int cost = 0;
		public List<SBean.DropEntryNew> gift = new ArrayList<SBean.DropEntryNew>();
		public LimitedGift()
		{
			
		}
		
		public void setVipReq(int vipReq)
		{
			this.vipReq = vipReq;
		}
		
		public void setMaxBuyTimes(int times)
		{
			this.maxBuyTimes = times;
		}
		
		public void setCost(int cost)
		{
			this.cost = cost;
		}
		
		public void addGift(int type, int id, int count)
		{
			gift.add(new SBean.DropEntryNew((byte)type, (short)id, count));
		}
		
	}
	
	public static class LimitedShopConfig extends ActivityDisplayConfig
	{
		List<LimitedGift> limitedGift = new ArrayList<LimitedGift>();
		public LimitedShopConfig()
		{
			
		}
		
		public int getType()
		{
			return LIMITEDSHOP;
		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			for (LimitedGift g : limitedGift)
			{
				if (g.maxBuyTimes == 0)
					return CONFIG_GIFT_MAX_BUY_TIMES_INVALID;
				if (g.cost <= 0)
					return CONFIG_GIFT_COST_INVALID;
				if (g.gift.size() <= 0 || g.gift.size() > 3)
					return CONFIG_GIFT_COUNT_INVALID;
			}
			return CONFIG_VALID;
		}
		
		public void addGift(LimitedGift gift)
		{
			limitedGift.add(gift);
		}
			
		public LimitedGift getLimitedGift(int index)
		{
			if (index >= 0 && index < limitedGift.size())
			{
				return limitedGift.get(index);
			}
			return null;
		}
		
		public int getGiftCount()
		{
			return limitedGift.size();
		}
		
		public Iterable<LimitedGift> getLimitedGift()
		{
			return limitedGift;
		}
	}
	
	public class LimitedShop extends ActivityImpl<LimitedShopConfig>
	{
		public LimitedShop()
		{
			super(LimitedShopConfig.class, "LimitedShop");
			parser.setActivityName("LimitedShop");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("Gift");
			parser.addEntryName("Goods");
			setConfigs(new ArrayList<LimitedShopConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<LimitedShopConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<LimitedShopConfig> getConfig(XMLItem item)
		{
			List<LimitedShopConfig> cfgs = new ArrayList<LimitedShopConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("LimitedShop"))
						{
							LimitedShopConfig cfg = new LimitedShopConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("Gift"))
									{
										LimitedGift limitedgift = new LimitedGift();
										String vipReqStr = ccitem.getAttribute("vip").trim();
										if (!vipReqStr.isEmpty())
										{
											int vipReq = Integer.parseInt(vipReqStr);
											if (vipReq > 0)
											{
												limitedgift.setVipReq(vipReq);
											}
										}
										String buyTimesStr = ccitem.getAttribute("buytimes").trim();
										int butTimes = Integer.parseInt(buyTimesStr);
										limitedgift.setMaxBuyTimes(butTimes);
										String costStr = ccitem.getAttribute("cost").trim();
										int cost = Integer.parseInt(costStr);
										limitedgift.setCost(cost);
										for (XMLItem cccitem : ccitem.childrenItems)
										{
											String aaname = cccitem.getAttribute("name");
											if (aaname.equals("Goods"))
											{
												String typeStr = cccitem.getAttribute("type").trim();
												if (typeStr.equals("Item"))
												{
													String idStr = cccitem.getAttribute("id").trim();
													int id = Integer.parseInt(idStr);
													String countStr = cccitem.getAttribute("count").trim();
													int count = Integer.parseInt(countStr);
													if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
													{
														limitedgift.addGift(GameData.COMMON_TYPE_ITEM, id, count);
													}
													else
													{
														throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
													}
												}
												else if (typeStr.equals("Equip"))
												{
													String idStr = cccitem.getAttribute("id").trim();
													int id = Integer.parseInt(idStr);
													String countStr = cccitem.getAttribute("count").trim();
													int count = Integer.parseInt(countStr);
													if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
													{
														limitedgift.addGift(GameData.COMMON_TYPE_EQUIP, id, count);
													}
													else
													{
														throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
													}
												}
												else if (typeStr.equals("General"))
												{
													String idStr = cccitem.getAttribute("id").trim();
													int id = Integer.parseInt(idStr);
													String cntStr = cccitem.getAttribute("count").trim();
													int count = Integer.parseInt(cntStr);
													if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
													{
														limitedgift.addGift(GameData.COMMON_TYPE_GENERAL, id, count);
													}
													else
													{
														throw new Exception("Gift General id=" + id + " is invalid!");
													}
												}
												else
												{
													throw new Exception("unknown Goods type:" + typeStr + " !");
												}
											}
											else
											{
												throw new Exception("unknown Gift entry:" + aaname + " !");
											}
										}
										cfg.addGift(limitedgift);
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
	public static class LoginLvlGift
	{
		public int loginDaysReq = 0;
		public List<SBean.DropEntryNew> gift = new ArrayList<SBean.DropEntryNew>();
		public LoginLvlGift(int loginDaysReq)
		{
			this.loginDaysReq = loginDaysReq;
		}
		public void addGift(int type, int id, int count)
		{
			gift.add(new SBean.DropEntryNew((byte)type, (short)id, count));
		}
	}
	
	public static class LoginGiftConfig extends ActivityDisplayConfig
	{
		final static int MAX_DAY = 30;
		List<LoginLvlGift> loginLvlGift = new ArrayList<LoginLvlGift>();
		public LoginGiftConfig()
		{
			
		}
		
		public int getType()
		{
			return LOGINGIFT;
		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			int days = getDays();
			if (days <= 0 || days > MAX_DAY)
				return CONFIG_CHECKIN_DAYS_INVALID;
			int minLoginDaysReq = 0;
			for (LoginLvlGift g : loginLvlGift)
			{
				if (g.loginDaysReq <= minLoginDaysReq || g.loginDaysReq > days)
					return CONFIG_LOGIN_DAYS_REQ_INVALID;
				minLoginDaysReq = g.loginDaysReq;
				if (g.gift.size() <= 0 || g.gift.size() > 3)
					return CONFIG_GIFT_COUNT_INVALID;
			}
			return CONFIG_VALID;
		}
		
		public void addGift(LoginLvlGift gift)
		{
			loginLvlGift.add(gift);
		}
		
		public LoginLvlGift getLoginLvlGift(int days)
		{
			for (LoginLvlGift g : loginLvlGift)
			{
				if (g.loginDaysReq == days)
					return g;
			}
			return null;
		}
		
		public int getLoginLvlCount()
		{
			return loginLvlGift.size();
		}
		
		public Iterable<LoginLvlGift> getLoginLvlGift()
		{
			return loginLvlGift;
		}
	}
	
	public class LoginGift extends ActivityImpl<LoginGiftConfig>
	{
		public LoginGift()
		{
			super(LoginGiftConfig.class, "LoginGift");
			parser.setActivityName("LoginGift");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("LevelGift");
			parser.addEntryName("Gift");
			setConfigs(new ArrayList<LoginGiftConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<LoginGiftConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<LoginGiftConfig> getConfig(XMLItem item)
		{
			List<LoginGiftConfig> cfgs = new ArrayList<LoginGiftConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("LoginGift"))
						{
							LoginGiftConfig cfg = new LoginGiftConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("LevelGift"))
									{
										String daysReqStr = ccitem.getAttribute("daysReq").trim();
										int daysReq = Integer.parseInt(daysReqStr);
										if (daysReq > 0)
										{
											LoginLvlGift lvlgift = new LoginLvlGift(daysReq);
											for (XMLItem cccitem : ccitem.childrenItems)
											{
												String aaname = cccitem.getAttribute("name");
												if (aaname.equals("Gift"))
												{
													String typeStr = cccitem.getAttribute("type").trim();
													if (typeStr.equals("Money"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_MONEY, 0, count);
														}
														else
														{
															throw new Exception("Money count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Diamond"))
													{
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_STONE, 0, count);
														}
														else
														{
															throw new Exception("Diamond count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Item"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_ITEM, id, count);
														}
														else
														{
															throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("Equip"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String countStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(countStr);
														if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_EQUIP, id, count);
														}
														else
														{
															throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
														}
													}
													else if (typeStr.equals("General"))
													{
														String idStr = cccitem.getAttribute("id").trim();
														int id = Integer.parseInt(idStr);
														String cntStr = cccitem.getAttribute("count").trim();
														int count = Integer.parseInt(cntStr);
														if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
														{
															lvlgift.addGift(GameData.COMMON_TYPE_GENERAL, id, count);
														}
														else
														{
															throw new Exception("General id=" + id + " is invalid!");
														}
													}
													else
													{
														throw new Exception("unknown Gift type:" + typeStr + " !");
													}
												}
												else
												{
													throw new Exception("unknown LevelGift entry:" + aaname + " !");
												}
											}
											cfg.addGift(lvlgift);
										}
										else
										{
											throw new Exception("LevelGift daysReq=" + daysReq + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	 
	
	public static final int GOODS_RETURN_TIMES = 4;
	public static class WarGoods
	{
		public static final int GOODS_PROVISIONS = 0;
		public static final int GOODS_CLOTH = 1;
		public static final int GOODS_ARM = 2;
		public static final int GOODS_ARM1 = 3;
		public static final int GOODS_COUNT = 4;
		
		public int type = 0;
		public int viplvl = 0;
		public int price = 0;
		//public int maxBuyCnt = 0;
		public int[]  returnVal = new int[GOODS_RETURN_TIMES];
		public WarGoods(int type)
		{
			this.type = type;
		}
		
		public int getType()
		{
			return type;
		}
		
		public int getVipLvlReq()
		{
			return viplvl;
		}
		
		public int getPrice()
		{
			return price;
		}
		
//		public int getMaxBuyCount()
//		{
//			return maxBuyCnt;
//		}
		
		public int getReturnTimes()
		{
			return returnVal.length;
		}
		
		public int getReturnValue(int index)
		{
			return returnVal[index];
		}
		
		public void setReturnValue(int index, int val)
		{
			returnVal[index] = val;
		}
	}
	
	public static class TradingCenterConfig extends ActivityDisplayConfig
	{
		public int buyEndTime = 0;
		public int[] returnStartTime = new int[GOODS_RETURN_TIMES];
		public WarGoods[] goods = new WarGoods[WarGoods.GOODS_COUNT];
		public int maxBuyCnt = 0; 
		public TradingCenterConfig()
		{
		}
		
		public int getType()
		{
			return TRADINGCENTER;
		}
	
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			if (getBuyEndTime() <= getStartTime() || getBuyEndTime() >= getEndTime())
				return CONFIG_TRADINGCENTER_BUY_END_TIME_INVALID;
			int minReturnTime = getBuyEndTime();
			for (int i = 0; i < GOODS_RETURN_TIMES; ++i)
			{
				if (getReturnTime(i) < minReturnTime || getReturnTime(i) >= getEndTime())
					return CONFIG_TRADINGCENTER_RETURN_TIME_INVALID;
				minReturnTime = getReturnTime(i);
			}
			if (getMaxBuyCount() <= 0)
				return CONFIG_TRADINGCENTER_GOODS_MAX_BUY_COUNT_INVALID;
			for (int i = 0; i < WarGoods.GOODS_COUNT; ++i)
			{
				WarGoods g = goods[i];
				if (g == null)
					return CONFIG_TRADINGCENTER_GOODS_COUNT_INVALID;
				if (g.getType() != i)
					return CONFIG_TRADINGCENTER_GOODS_TYPE_INVALID;
				if (g.price <= 0)
					return CONFIG_TRADINGCENTER_GOODS_PRICE_INVALID;
//				if (g.maxBuyCnt <= 0)
//					return CONFIG_TRADINGCENTER_GOODS_MAX_BUY_COUNT_INVALID;
				for (int t = 0; t < GOODS_RETURN_TIMES; ++t)
				{
					if (g.getReturnValue(t) <= 0)
						return CONFIG_TRADINGCENTER_GOODS_RETURN_VALUE_INVALID;
				}
			}
			return CONFIG_VALID;
		}
		
		public void setBuyEndTime(int time)
		{
			buyEndTime = time;
		}
		
		public int getBuyEndTime()
		{
			return (buyEndTime - getRealStartTime()) + getStartTime();
		}
		
		public void setReturnTime(int index, int time)
		{
			returnStartTime[index] = time;
		}
		
		public int getReturnTime(int index)
		{
			return (returnStartTime[index] - getRealStartTime()) + getStartTime();
		}
		
		public int getReturnTimeCount()
		{
			return returnStartTime.length;
		}
		
		public int getGoodsCount()
		{
			return goods.length;
		}
		
		public WarGoods getGoodsByIndex(int index)
		{
			return goods[index];
		}
		
		public WarGoods getGoods(int type)
		{
			for (int i = 0; i < goods.length; ++i)
			{
				WarGoods g = goods[i];
				if (g.getType() == type)
					return g;
			}
			return null;
		}
		
		public void setGoods(int type, WarGoods g)
		{
			goods[type] = g;
		}
		
		public int getMaxBuyCount()
		{
			return maxBuyCnt;
		}
		
		public void setMaxBuyCount(int c)
		{
			maxBuyCnt = c;
		}
	}
	
	
	public class TradingCenter extends ActivityImpl<TradingCenterConfig>
	{
		public TradingCenter()
		{
			super(TradingCenterConfig.class, "TradingCenter");
			parser.setActivityName("TradingCenter");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("MaxBuy");
			parser.addEntryName("BuyEndTime");
			parser.addEntryName("ReturnTime");
			parser.addEntryName("Goods");
			parser.addEntryName("Return");
			setConfigs(new ArrayList<TradingCenterConfig>());
		}
		
	
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<TradingCenterConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<TradingCenterConfig> getConfig(XMLItem item)
		{
			List<TradingCenterConfig> cfgs = new ArrayList<TradingCenterConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("TradingCenter"))
						{
							TradingCenterConfig cfg = new TradingCenterConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("BuyEndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.setBuyEndTime((int)(date.getTime()/1000) + 8*3600);
									}
									else if (aname.equals("ReturnTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										String seqStr = ccitem.getAttribute("seq").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										int seq = Integer.parseInt(seqStr);
										if (date != null && seq > 0 && seq <= GOODS_RETURN_TIMES)
										{
											cfg.setReturnTime(seq-1, (int)(date.getTime()/1000) + 8*3600);
										}
									}
									else if (aname.equals("MaxBuy"))
									{
										String maxbuyStr = ccitem.getAttribute("value").trim();
										int maxBuyCnt = Integer.parseInt(maxbuyStr);
										cfg.setMaxBuyCount(maxBuyCnt);
									}
									else if (aname.equals("Goods"))
									{
										String typeStr = ccitem.getAttribute("type").trim();
										int type = -1;
										if (typeStr.equals("Provisions"))
										{
											type = WarGoods.GOODS_PROVISIONS;
										}
										else if (typeStr.equals("Cloth"))
										{
											type = WarGoods.GOODS_CLOTH;
										}
										else if (typeStr.equals("Arm"))
										{
											type = WarGoods.GOODS_ARM;
										}
										else if (typeStr.equals("envelope"))
										{
											type = WarGoods.GOODS_ARM1;
										}
										else
										{
											throw new Exception("unknown Goods type:" + typeStr + " !");
										}
										{
											String vipStr = ccitem.getAttribute("viplvl").trim();
											String priceStr = ccitem.getAttribute("price").trim();
//											String maxbuyStr = ccitem.getAttribute("maxbuy").trim();
											WarGoods g = new WarGoods(type);
											g.viplvl = Integer.parseInt(vipStr);
											g.price = Integer.parseInt(priceStr);
//											g.maxBuyCnt = Integer.parseInt(maxbuyStr);
											for (XMLItem cccitem : ccitem.childrenItems)
											{
												String aaname = cccitem.getAttribute("name");
												if (aaname.equals("Return"))
												{
													String seqStr = cccitem.getAttribute("seq").trim();
													int seq = Integer.parseInt(seqStr);
													if (seq > 0 && seq <= GOODS_RETURN_TIMES)
													{
														String valStr = cccitem.getAttribute("value").trim();
														int val = Integer.parseInt(valStr);
														g.setReturnValue(seq-1, val);
													}
												}
												else
												{
													throw new Exception("unknown Goods entry:" + aaname + " !");
												}
											}
											cfg.setGoods(type, g);
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}
	
// TODO
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static class GenPackageGift
	{
		public int batchID;
		public int genID;
		public int start = 0;
		public int end = 0;
		public String title;
		public String content;
		public List<SBean.DropEntryNew> genGift = new ArrayList<SBean.DropEntryNew>();
		public GenPackageGift(int batchID, int genID)
		{
			this.batchID = batchID;
			this.genID = genID;
		}
		
		public void setStartTime(int time)
		{
			this.start = time;
		}
		
		public void setEndTime(int time)
		{
			this.end = time;
		}
		
		public void setTitle(String title)
		{
			this.title = title;
		}
		
		public void setContent(String content)
		{
			this.content = content;
		}
		
		public void addGift(SBean.DropEntryNew gift)
		{
			this.genGift.add(gift);
		}
		
		public int getStartTime()
		{
			return start;
		}
		
		public int getEndTime()
		{
			return end;
		}
		
		public String getTitle()
		{
			return title;
		}
		
		public String getContent()
		{
			return content;
		}
				
		public boolean isInValidTime(int curTime)
		{
			return curTime >= start && curTime <= end;
		}
		
//		public boolean isValid()
//		{
//			if (start <= 0 || end <= 0 || start >= end)
//				return false;
//			if (title == null || content.isEmpty())
//				return false;
//			if (content == null || content.isEmpty())
//				return false;
//			return !genGift.isEmpty();
//		}
		
		public boolean isValid()
		{
			return checkValid() == CONFIG_VALID;
		}
		
		public int checkValid()
		{
			if (start <= 0 || end <= 0 || start >= end)
				return CONFIG_GEN_PACKAGE_GIFT_TIME_INVALID;
			if (title == null || title.isEmpty())
				return CONFIG_GEN_PACKAGE_GIFT_TITLE_INVALID;
			if (content == null || content.isEmpty())
				return CONFIG_GEN_PACKAGE_GIFT_CONTENT_INVALID;
			if (genGift.isEmpty())
				return CONFIG_GEN_PACKAGE_GIFT_EMPTY_INVALID;
			return CONFIG_VALID;
		}
		
		public String toValidString()
		{
			return " check valid = " + checkValid();
		}
	}
	public static class BatchPackageGift
	{
		public int batchID;
		public Map<Integer, GenPackageGift> batchGift = new TreeMap<Integer, GenPackageGift>();
		public BatchPackageGift(int batchID)
		{
			this.batchID = batchID;
		}
		
		public void addGift(GenPackageGift gift)
		{
			batchGift.put(gift.genID, gift);
		}
		
//		public boolean isValid()
//		{
//			for (Map.Entry<Integer, GenPackageGift> e : batchGift.entrySet())
//			{
//				GenPackageGift gpg = e.getValue();
//				if (!gpg.isValid())
//					return false;
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			for (Map.Entry<Integer, GenPackageGift> e : batchGift.entrySet())
			{
				GenPackageGift gpg = e.getValue();
				int errorCode = gpg.checkValid();
				if (errorCode != CONFIG_VALID)
					return errorCode;
			}
			return CONFIG_VALID;
		}
	}
	public static class GiftPackageConfig extends ActivityDisplayConfig
	{
		static final int GEN_ID_MAX = 35; 
		Map<Integer, BatchPackageGift> giftPackages = new TreeMap<Integer, BatchPackageGift>();
		public GiftPackageConfig()
		{
		}
		
		public int getType()
		{
			return GIFTPACKAGE;
		}
		
//		public boolean isValid()
//		{
//			if (!super.isValid())
//				return false;
//			for (Map.Entry<Integer, BatchPackageGift> e : giftPackages.entrySet())
//			{
//				BatchPackageGift bpg = e.getValue();
//				if (!bpg.isValid())
//					return false;
//			}
//			return true;
//		}
		
		public int checkValid()
		{
			int errorCode= super.checkValid();
			if (errorCode != CONFIG_VALID)
				return errorCode;
			for (Map.Entry<Integer, BatchPackageGift> e : giftPackages.entrySet())
			{
				BatchPackageGift bpg = e.getValue();
				errorCode = bpg.checkValid();
				if (errorCode != CONFIG_VALID)
					return errorCode;
			}
			return CONFIG_VALID;
		}

		public void addGift(BatchPackageGift gift)
		{
			giftPackages.put(gift.batchID, gift);
		}

		
		public GenPackageGift getGift(int batchID, int genID)
		{
			BatchPackageGift bpgift = giftPackages.get(batchID);
			if (bpgift != null)
			{
				GenPackageGift gpgift = bpgift.batchGift.get(genID);
				return gpgift;
			}
			return null;
		}
		
		public void dumpDetail(GameServer gs)
		{
			gs.getLogger().info("-print all batch gift package count=" + giftPackages.size());
			for (Map.Entry<Integer, BatchPackageGift> e : giftPackages.entrySet())
			{
				BatchPackageGift bpg = e.getValue();
				gs.getLogger().info("--print batch " + bpg.batchID + " all gen gift package count=" + bpg.batchGift.size());
				for (Map.Entry<Integer, GenPackageGift> ee : bpg.batchGift.entrySet())
				{
					GenPackageGift gpg = ee.getValue();
					gs.getLogger().info("---batchID="+gpg.batchID+", genID="+gpg.genID+", " + gpg.toValidString() + ", open time:(" + GameServer.getTimeStampStr(gpg.start) + "--" + GameServer.getTimeStampStr(gpg.end) + "), title=" + gpg.title);
				}
			}
		}
	}
	
	public class GiftPackage extends ActivityImpl<GiftPackageConfig>
	{
		public GiftPackage()
		{
			super(GiftPackageConfig.class, "GiftPackage");
			parser.setActivityName("GiftPackage");
			parser.addEntryName("StartTime");
			parser.addEntryName("EndTime");
			parser.addEntryName("DisplayTitle");
			parser.addEntryName("DisplayContent");
			parser.addEntryName("BatchGiftPackage");
			parser.addEntryName("SequenceGiftPackage");
			parser.addEntryName("GiftPackageStartTime");
			parser.addEntryName("GiftPackageEndTime");
			parser.addEntryName("GiftPackageTitle");
			parser.addEntryName("GiftPackageContent");
			parser.addEntryName("Gift");
			setConfigs(new ArrayList<GiftPackageConfig>());
		}
		
//		public List<SBean.DropEntryNew> getGiftPage(int batchID, int genID)
//		{
//			List<SBean.DropEntryNew> lst = new ArrayList<SBean.DropEntryNew>();
//			List<GiftPackageConfig> cfgs = getOpenedConfigs();
//			for (GiftPackageConfig cfg : cfgs)
//			{
//				lst.addAll(cfg.getGift(batchID, genID));
//			}
//			return lst;
//		}
		
		public void setConfigureFile(String filepath)
		{
			gs.getLogger().info(this.getClass().getSimpleName() + " config file("+filepath+") changed!");
			ActivitiesXMLHandler handler = new ActivitiesXMLHandler(parser);
			ParseXML(filepath, handler);			
			XMLItem item = handler.getRootItem();
			List<GiftPackageConfig> cfgs = getConfig(item);
			gs.getLogger().info("reload " + this.getClass().getSimpleName() + " config file " + (cfgs.isEmpty() ? "failed" : "success"));
			trySetConfigs(cfgs);
		}
		
		List<GiftPackageConfig> getConfig(XMLItem item)
		{
			List<GiftPackageConfig> cfgs = new ArrayList<GiftPackageConfig>();
			try
			{
				if (item != null)
				{
					for (XMLItem citem : item.childrenItems)
					{
						if (citem.getAttribute("name").equals("GiftPackage"))
						{
							GiftPackageConfig cfg = new GiftPackageConfig();
							{
								cfg.open = citem.getAttribute("open").toLowerCase().equals("true");
								cfg.effectiveStart = citem.getAttribute("createopen").toLowerCase().equals("true") ? getServerOpenDayTime() : 0;
								for (XMLItem ccitem : citem.childrenItems)
								{
									String aname = ccitem.getAttribute("name");
									if (aname.equals("StartTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.start = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("EndTime"))
									{
										String timeStr = ccitem.getAttribute("time").trim();
										Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
										if (date != null)
											cfg.end = (int)(date.getTime()/1000) + 8*3600;
									}
									else if (aname.equals("DisplayTitle"))
									{
										cfg.briefTitle = ccitem.value.toString().trim();
									}
									else if (aname.equals("DisplayContent"))
									{
										cfg.detailContent = ccitem.value.toString().trim();
									}
									else if (aname.equals("BatchGiftPackage"))
									{
										String batchStr = ccitem.getAttribute("batch");
										int batchID = Integer.parseInt(batchStr);
										if (batchID >= 0)
										{
											BatchPackageGift bpg = new BatchPackageGift(batchID);
											for (XMLItem cccitem : ccitem.childrenItems)
											{
												String aaname = cccitem.getAttribute("name");
												if (aaname.equals("SequenceGiftPackage"))
												{
													String seqStr = cccitem.getAttribute("seq").trim();
													int genID = Integer.parseInt(seqStr);
													if (genID > 0 && genID <= GiftPackageConfig.GEN_ID_MAX)
													{
														GenPackageGift gpg = new GenPackageGift(batchID, genID);
														for (XMLItem ccccitem : cccitem.childrenItems)
														{
															String aaaname = ccccitem.getAttribute("name");
															if (aaaname.equals("GiftPackageStartTime"))
															{
																String timeStr = ccccitem.getAttribute("time").trim();
																Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
																if (date != null)
																{
																	int time = (int)(date.getTime()/1000) + 8*3600;
																	gpg.setStartTime(time); 
																}
															}
															else if (aaaname.equals("GiftPackageEndTime"))
															{
																String timeStr = ccccitem.getAttribute("time").trim();
																Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
																if (date != null)
																{
																	int time = (int)(date.getTime()/1000) + 8*3600;
																	gpg.setEndTime(time); 
																}
															}
															else if (aaaname.equals("GiftPackageTitle"))
															{
																String str = ccccitem.value.toString().trim();
																gpg.setTitle(str);
															}
															else if (aaaname.equals("GiftPackageContent"))
															{
																String str = ccccitem.value.toString().trim();
																gpg.setContent(str);
															}
															else if (aaaname.equals("Gift"))
															{
																String typeStr = ccccitem.getAttribute("type").trim();
																if (typeStr.equals("Money"))
																{
																	String countStr = ccccitem.getAttribute("count").trim();
																	int count = Integer.parseInt(countStr);
																	if (count > 0)
																	{
																		SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_MONEY, (short)0, count);
																		gpg.addGift(den);
																	}
																	else
																	{
																		throw new Exception("Money count=" + count + " is invalid!");
																	}
																}
																else if (typeStr.equals("Diamond"))
																{
																	String countStr = ccccitem.getAttribute("count").trim();
																	int count = Integer.parseInt(countStr);
																	if (count > 0)
																	{
																		SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_STONE, (short)0, count);
																		gpg.addGift(den);
																	}
																	else
																	{
																		throw new Exception("Diamond count=" + count + " is invalid!");
																	}
																}
																else if (typeStr.equals("Item"))
																{
																	String idStr = ccccitem.getAttribute("id").trim();
																	int id = Integer.parseInt(idStr);
																	String countStr = ccccitem.getAttribute("count").trim();
																	int count = Integer.parseInt(countStr);
																	if (GameData.getInstance().getItemCFG((short)id) != null && count > 0)
																	{
																		SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_ITEM, (short)id, count);
																		gpg.addGift(den);
																	}
																	else
																	{
																		throw new Exception("Item id=" + id + " count=" + count + " is invalid!");
																	}
																}
																else if (typeStr.equals("Equip"))
																{
																	String idStr = ccccitem.getAttribute("id").trim();
																	int id = Integer.parseInt(idStr);
																	String countStr = ccccitem.getAttribute("count").trim();
																	int count = Integer.parseInt(countStr);
																	if (GameData.getInstance().getEquipCFG((short)id) != null && count > 0)
																	{
																		SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_EQUIP, (short)id, count);
																		gpg.addGift(den);
																	}
																	else
																	{
																		throw new Exception("Equip id=" + id + " count=" + count + " is invalid!");
																	}
																}
																else if (typeStr.equals("General"))
																{
																	String idStr = ccccitem.getAttribute("id").trim();
																	int id = Integer.parseInt(idStr);
																	String cntStr = ccccitem.getAttribute("count").trim();
																	int count = Integer.parseInt(cntStr);
																	if (GameData.getInstance().getGeneralCFG((short)id) != null && count >= 1 && count <= 5)
																	{
																		SBean.DropEntryNew den = new SBean.DropEntryNew(GameData.COMMON_TYPE_GENERAL, (short)id, count);
																		gpg.addGift(den);
																	}
																	else
																	{
																		throw new Exception("General id=" + id + " is invalid!");
																	}
																}
																else
																{
																	throw new Exception("unknown Gift type:" + typeStr + " !");
																}
															}
															else
															{
																throw new Exception("unknown SequenceGiftPackage entry:" + aaaname + " !");
															}
															bpg.addGift(gpg);
														}
													}
													else
													{
														throw new Exception("SequenceGiftPackage genID=" + genID + " is invalid!");
													}
												}
												else
												{
													throw new Exception("unknown BatchGiftPackage entry:" + aaname + " !");
												}
												cfg.addGift(bpg);
											}
										}
										else
										{
											throw new Exception("BatchGiftPackage batchID=" + batchID + " is invalid!");
										}
									}
									else
									{
										throw new Exception("unknown entry name=" + aname + " !");
									}
								}
							}
							cfgs.add(cfg);
						}
					}
				}
			}
			catch (Exception e)
			{
				gs.getLogger().warn(e);
				e.printStackTrace();
				cfgs.clear();;
			}
			return cfgs;
		}
	}


// TODO
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void ParseXML(String filename, DefaultHandler handler)
	{
		try 
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			parser.parse(new BufferedInputStream(new FileInputStream(filename)), handler);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	static class XMLItem
	{
		XMLItem parent;
		String name;
		StringBuffer value = new StringBuffer();
		List<XMLItem> childrenItems = new ArrayList<XMLItem>();
		Map<String, String> attributes = new TreeMap<String, String>();
		
		XMLItem(String name)
		{
			this.name = name;
		}
		
		String getAttribute(String name)
		{
			String val = attributes.get(name);
			val = (val == null ? "" : val);
			return val;
		}
	}
	
	class CustomXMLParser
	{
		private String activityName = "";
		private Set<String> entryNameSet = new TreeSet<String>();
		public CustomXMLParser()
		{
			
		}
		
		public void setActivityName(String activityName)
		{
			this.activityName = activityName;
		}
		
		public void addEntryName(String name)
		{
			entryNameSet.add(name);
		}
		
		public boolean isElementValid(String parentName, String parentAttributeName, String childName, String attributeName)
		{
			if (parentName == null)
			{
				if (childName.equals("activities"))
				{
					return true;
				}
			}
			else if (parentName.equals("activities"))
			{
				if (childName.equals("struct") && attributeName.equals(activityName))
				{
					return true;
				}
			}
			else if (parentName.equals("struct") && parentAttributeName.equals(activityName) || parentName.equals("entry"))
			{
				if (childName.equals("entry") && entryNameSet.contains(attributeName))
				{
					return true;
				}
			}
			return false;
		}
	}
	
	class ActivitiesXMLHandler extends DefaultHandler
	{
		CustomXMLParser parser;
		XMLItem root = null;
		XMLItem curItem = null;
		boolean parseEnd = false;
		Stack<String> stack = new Stack<String>();
		int validLength = 0;
		
		public ActivitiesXMLHandler(CustomXMLParser parser)
		{
			this.parser = parser;
		}
		
		public XMLItem getRootItem()
		{
			XMLItem item = null;
			if (parseEnd)
				item = root;
			return item;
		}
		
		private String getAttributeNameValue(Attributes attributes)
		{
			String value = "";
			for (int i = 0; i < attributes.getLength(); ++i)
			{
				if (attributes.getQName(i).equals("name"))
				{
					value = attributes.getValue(i);
					break;
				}
			}
			return value;
		}
		
		public void startDocument()
		{
			//System.out.println("===============================>");
		}
		
		public void endDocument()
		{
			parseEnd = true;
			//System.out.println("<===============================");
		}
		
		
		public void startElement(String uri, String localName, String qName, Attributes attributes)
		{
			//System.out.println(">>>>>>>>>startElement>>>>>>curItem Name="+(curItem==null ? "null":curItem.name)+" selfName="+qName);
			int curLength = stack.size();
			stack.push(qName);
			if (curLength > validLength)
				return;
			String parentName = null;
			String parentAttributeNameValue = "";
			if (curItem!= null)
			{
				parentName = curItem.name;
				parentAttributeNameValue = curItem.getAttribute("name");
			}
			if (!parser.isElementValid(parentName, parentAttributeNameValue, qName, getAttributeNameValue(attributes)))
				return;
			++validLength;
			XMLItem item = new XMLItem(qName);
			for (int i = 0; i < attributes.getLength(); ++i)
			{
				item.attributes.put(attributes.getQName(i), attributes.getValue(i));
			}
			if (root == null)
			{
				root = curItem = item;
			}
			else
			{
				curItem.childrenItems.add(item);
				item.parent = curItem;
				curItem = item;
			}
//			System.out.println("startElement-------");
//			System.out.println("uri="+uri);
//			System.out.println("localName="+localName);
//			System.out.println("qName="+qName);
//			System.out.println("---------------------");
//			for (int i = 0; i < attributes.getLength(); ++i)
//			{
//				System.out.println(i+"-uri="+attributes.getURI(i));
//				System.out.println(i+"-localName="+attributes.getLocalName(i));
//				System.out.println(i+"-qName="+attributes.getQName(i));
//				System.out.println(i+"-type="+attributes.getType(i));
//				System.out.println(i+"-value="+attributes.getValue(i));
//			}
		}
		
		public void endElement(String uri, String localName, String qName) 
		{
			//System.out.println(">>>>>>>>>endElement>>>>>>>curItem Name="+(curItem==null ? "null":curItem.name)+" selfName="+qName);
			int curLength = stack.size();
			stack.pop();
			if (curLength > validLength)
				return;
			--validLength;
			curItem = curItem.parent;
//			System.out.println("---------------------");
//			System.out.println("uri="+uri);
//			System.out.println("localName="+localName);
//			System.out.println("qName="+qName);
//			System.out.println("endElement---------");
		}
		
		public void characters(char[] ch, int start, int length)
		{
			if (stack.size() > validLength)
				return;
			curItem.value.append(ch, start, length);
//			System.out.println("***********************");
//			System.out.println(new String(ch, start, length));
//			System.out.println("***********************");
		}
	}
	
	public static void main(String[] args)
	{
		
	}
}






