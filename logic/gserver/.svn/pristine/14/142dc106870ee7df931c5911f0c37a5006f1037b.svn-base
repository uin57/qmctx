package i3k;

import i3k.SBean.DBGeneralBestow;
import i3k.SBean.DBGeneralBless;
import i3k.SBean.DBGeneralEquip;
import i3k.SBean.DBGeneralOfficial;
import i3k.SBean.DBGeneralSeyen;
import i3k.SBean.DBGeneralStone;
import i3k.SBean.DBWeapon;

import java.util.ArrayList;
import java.util.List;

import ket.util.Stream;

public class DBRoleGeneral implements Stream.IStreamable
{

	public DBRoleGeneral() { }

	public DBRoleGeneral(short id, short lvl, int exp, byte advLvl, 
	                     byte evoLvl, DBWeapon weapon, List<Short> skills, List<DBGeneralEquip> equips
	                     , List<DBGeneralSeyen> generalSeyen, List<DBGeneralOfficial> official
	                     ,short headicon, List<DBGeneralBestow>  bestow, List<DBGeneralBless>   bless)
	{
		this.id = id;
		this.lvl = lvl;
		this.exp = exp;
		this.advLvl = advLvl;
		this.evoLvl = evoLvl;
		this.weapon = weapon;
		this.skills = skills;
		this.equips = equips;
		this.generalSeyen = generalSeyen;
		this.official = official;
		this.headicon = (byte) headicon;
		this.bestow = bestow;
		this.bless = bless;
	}

	public DBRoleGeneral ksClone()
	{
		return new DBRoleGeneral(id, lvl, exp, advLvl, 
		                         evoLvl, weapon, skills, equips, generalSeyen, official,headicon, bestow,bless);
	}

	public DBRoleGeneral kdClone()
	{
		DBRoleGeneral _kio_clobj = ksClone();
		_kio_clobj.weapon = weapon.kdClone();
		_kio_clobj.skills = new ArrayList<Short>();
		for(Short _kio_iter : skills)
		{
			_kio_clobj.skills.add(_kio_iter);
		}
		_kio_clobj.equips = new ArrayList<DBGeneralEquip>();
		for(DBGeneralEquip _kio_iter : equips)
		{
			_kio_clobj.equips.add(_kio_iter.kdClone());
		}
		
		if(generalStone!=null){
			_kio_clobj.generalStone = new ArrayList<DBGeneralStone>();
			for(DBGeneralStone _kio_iter : generalStone)
			{
				_kio_clobj.generalStone.add(_kio_iter.kdClone());
			}
		}
		
		if(generalSeyen!=null){
			_kio_clobj.generalSeyen = new ArrayList<DBGeneralSeyen>();
			for(DBGeneralSeyen _kio_iter : generalSeyen)
			{
				_kio_clobj.generalSeyen.add(_kio_iter.kdClone());
			}
		}
		
		if(bestow!=null){
			_kio_clobj.bestow = new ArrayList<DBGeneralBestow>();
			for(DBGeneralBestow _kio_iter : bestow)
			{
				_kio_clobj.bestow.add(_kio_iter.kdClone());
			}
		}
		
		if(bless!=null){
			_kio_clobj.bless = new ArrayList<DBGeneralBless>();
			for(DBGeneralBless _kio_iter : bless)
			{
				_kio_clobj.bless.add(_kio_iter.kdClone());
			}
		}
		
		if(official!=null){
			_kio_clobj.official = new ArrayList<DBGeneralOfficial>();
			for(DBGeneralOfficial _kio_iter : official)
			{
				_kio_clobj.official.add(_kio_iter.kdClone());
			}
		}
		
		return _kio_clobj;
	}

	@Override
	public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
	{
		short flag = is.popShort();
		if (flag > 0) { // old
			id = flag;
			lvl = is.popShort();
			exp = is.popInteger();
			advLvl = is.popByte();
			evoLvl = is.popByte();
			if( weapon == null )
				weapon = new DBWeapon();
			is.pop(weapon);
			skills = is.popShortList();
			equips = is.popList(DBGeneralEquip.class);
			generalSeyen = new ArrayList<SBean.DBGeneralSeyen>();
			generalSeyen.add(new SBean.DBGeneralSeyen());
			bestow = new ArrayList<SBean.DBGeneralBestow>();
			SBean.DBGeneralBestow gbestow = new SBean.DBGeneralBestow();
			gbestow.bestowLevel = new ArrayList<SBean.DBBestowLevel>();
			bestow.add(gbestow);
		}
		else {
			version = is.popByte();
			id = is.popShort();
			lvl = is.popShort();
			exp = is.popInteger();
			advLvl = is.popByte();
			evoLvl = is.popByte();
			if( weapon == null )
				weapon = new DBWeapon();
			is.pop(weapon);
			skills = is.popShortList();
			equips = is.popList(DBGeneralEquip.class);
			generalStone = is.popList(DBGeneralStone.class);
			generalSeyen = is.popList(DBGeneralSeyen.class);
			official = is.popList(DBGeneralOfficial.class);
			headicon = is.popByte();
			bestow = is.popList(DBGeneralBestow.class);
			bless = is.popList(DBGeneralBless.class);
			r22 = is.popByte();
			r23 = is.popByte();
			r24 = is.popByte();
		}
	}

	@Override
	public void encode(Stream.AOStream os)
	{
		os.pushShort((short)-1);
		os.pushByte(version);
		os.pushShort(id);
		os.pushShort(lvl);
		os.pushInteger(exp);
		os.pushByte(advLvl);
		os.pushByte(evoLvl);
		os.push(weapon);
		os.pushShortList(skills);
		os.pushList(equips);
		os.pushList(generalStone==null?new ArrayList<DBGeneralStone>():generalStone);
		os.pushList(generalSeyen==null?new ArrayList<DBGeneralSeyen>():generalSeyen);
		os.pushList(official==null?new ArrayList<DBGeneralOfficial>():official);
		os.pushByte(headicon);
		os.pushList(bestow==null?new ArrayList<DBGeneralBestow>():bestow);
		os.pushList(bless==null?new ArrayList<DBGeneralBless>():bless);
		os.pushByte(r22);
		os.pushByte(r23);
		os.pushByte(r24);
	}

	public byte version = 1;
	public short id;
	public short lvl;
	public int exp;
	public byte advLvl;
	public byte evoLvl;
	public DBWeapon weapon;
	public List<Short> skills;
	public List<DBGeneralEquip> equips;
	public List<DBGeneralStone> generalStone;
	public List<DBGeneralSeyen> generalSeyen;
	public List<DBGeneralOfficial> official;
	public List<DBGeneralBestow>  bestow;
	public List<DBGeneralBless>  bless;
	
	public byte headicon;
	public byte r22;
	public byte r23;
	public byte r24;
}

