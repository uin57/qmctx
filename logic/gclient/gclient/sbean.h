// modified by ket.kio.RPCGen at Mon Jan 04 14:52:43 CST 2016.

#ifndef __I3K__SBEAN_H
#define __I3K__SBEAN_H

#include <string>
#include <ket/util/stream.h>


namespace I3K
{

	namespace SBean
	{

		struct Float3 : public KET::Util::Stream::IStreamable
		{
			Float3() { }

			Float3(float x, float y, float z)
			    : m_x(x), m_y(y), m_z(z)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_x >> m_y >> m_z;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_x << m_y << m_z;
			}

			float m_x;
			float m_y;
			float m_z;
		};

		struct UIEffectCFG : public KET::Util::Stream::IStreamable
		{
			UIEffectCFG() { }

			UIEffectCFG(short id, const std::string& path, float scale)
			    : m_id(id), m_path(path), m_scale(scale)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_path >> m_scale;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_path << m_scale;
			}

			short m_id;
			std::string m_path;
			float m_scale;
		};

		struct ArtEffectCFG : public KET::Util::Stream::IStreamable
		{
			ArtEffectCFG() { }

			ArtEffectCFG(short id, const std::string& path, float radius, const std::string& hsName)
			    : m_id(id), m_path(path), m_radius(radius), m_hsName(hsName)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_path >> m_radius >> m_hsName;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_path << m_radius << m_hsName;
			}

			short m_id;
			std::string m_path;
			float m_radius;
			std::string m_hsName;
		};

		struct FightEffectCFG : public KET::Util::Stream::IStreamable
		{
			FightEffectCFG() { }

			FightEffectCFG(char id, const std::string& name, short artEffectID)
			    : m_id(id), m_name(name), m_artEffectID(artEffectID)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_name >> m_artEffectID;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_name << m_artEffectID;
			}

			char m_id;
			std::string m_name;
			short m_artEffectID;
		};

		struct CombatMapCFG : public KET::Util::Stream::IStreamable
		{
			CombatMapCFG() { }

			CombatMapCFG(short id, const std::string& name, const std::string& path)
			    : m_id(id), m_name(name), m_path(path)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_name >> m_path;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_name << m_path;
			}

			short m_id;
			std::string m_name;
			std::string m_path;
		};

		struct CombatGeneral : public KET::Util::Stream::IStreamable
		{
			CombatGeneral() { }

			CombatGeneral(short generalID, char generalLvl)
			    : m_generalID(generalID), m_generalLvl(generalLvl)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_generalID >> m_generalLvl;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_generalID << m_generalLvl;
			}

			short m_generalID;
			char m_generalLvl;
		};

		struct DayReward : public KET::Util::Stream::IStreamable
		{
			enum 
			{
				eTypeItem = 0,
				eTypeEquip = 1,
				eTypeGeneral = 2
			};

			DayReward() { }

			DayReward(char type, char count, short id)
			    : m_type(type), m_count(count), m_id(id)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_type >> m_count >> m_id;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_type << m_count << m_id;
			}

			char m_type;
			char m_count;
			short m_id;
		};

		struct PackageCFGS : public KET::Util::Stream::IStreamable
		{
			PackageCFGS() { }

			PackageCFGS(short id, char generalLevel, char lvlReq, int money, 
			            int stone, const std::vector<DayReward>& dayRewards)
			    : m_id(id), m_generalLevel(generalLevel), m_lvlReq(lvlReq), m_money(money), 
			      m_stone(stone), m_dayRewards(dayRewards)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_generalLevel >> m_lvlReq >> m_money
				        >> m_stone >> m_dayRewards;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_generalLevel << m_lvlReq << m_money
				        << m_stone << m_dayRewards;
			}

			short m_id;
			char m_generalLevel;
			char m_lvlReq;
			int m_money;
			int m_stone;
			std::vector<DayReward> m_dayRewards;
		};

		struct PackageCFGC : public KET::Util::Stream::IStreamable
		{
			PackageCFGC() { }

			PackageCFGC(const PackageCFGS& cfgs, const std::string& name, const std::string& des)
			    : m_cfgs(cfgs), m_name(name), m_des(des)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_cfgs >> m_name >> m_des;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_cfgs << m_name << m_des;
			}

			PackageCFGS m_cfgs;
			std::string m_name;
			std::string m_des;
		};

		struct LoadingCFG : public KET::Util::Stream::IStreamable
		{
			LoadingCFG() { }

			LoadingCFG(const std::string& msg, float globalProp)
			    : m_msg(msg), m_globalProp(globalProp)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_msg >> m_globalProp;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_msg << m_globalProp;
			}

			std::string m_msg;
			float m_globalProp;
		};

		struct CombatCatEntryCFG : public KET::Util::Stream::IStreamable
		{
			CombatCatEntryCFG() { }

			CombatCatEntryCFG(short cid, char seq)
			    : m_cid(cid), m_seq(seq)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_cid >> m_seq;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_cid << m_seq;
			}

			short m_cid;
			char m_seq;
		};

		struct CombatCatEntryListCFG : public KET::Util::Stream::IStreamable
		{
			CombatCatEntryListCFG() { }

			CombatCatEntryListCFG(const std::vector<CombatCatEntryCFG>& combats)
			    : m_combats(combats)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_combats;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_combats;
			}

			std::vector<CombatCatEntryCFG> m_combats;
		};

		struct CombatCatCFG : public KET::Util::Stream::IStreamable
		{
			CombatCatCFG() { }

			CombatCatCFG(char id, const std::string& name, short mapIconID, short mapCityIconID, 
			             short mapCityDisableIconID, const std::vector<CombatCatEntryListCFG>& combats)
			    : m_id(id), m_name(name), m_mapIconID(mapIconID), m_mapCityIconID(mapCityIconID), 
			      m_mapCityDisableIconID(mapCityDisableIconID), m_combats(combats)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_name >> m_mapIconID >> m_mapCityIconID
				        >> m_mapCityDisableIconID >> m_combats;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_name << m_mapIconID << m_mapCityIconID
				        << m_mapCityDisableIconID << m_combats;
			}

			char m_id;
			std::string m_name;
			short m_mapIconID;
			short m_mapCityIconID;
			short m_mapCityDisableIconID;
			std::vector<CombatCatEntryListCFG> m_combats;
		};

		struct GeneralSkill : public KET::Util::Stream::IStreamable
		{
			GeneralSkill() { }

			GeneralSkill(short id, const std::vector<char>& learnLvl)
			    : m_id(id), m_learnLvl(learnLvl)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_learnLvl;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_learnLvl;
			}

			short m_id;
			std::vector<char> m_learnLvl;
		};

		struct IconCFG : public KET::Util::Stream::IStreamable
		{
			IconCFG() { }

			IconCFG(short id, const std::string& path)
			    : m_id(id), m_path(path)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_path;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_path;
			}

			short m_id;
			std::string m_path;
		};

		struct EquipEffect : public KET::Util::Stream::IStreamable
		{
			EquipEffect() { }

			EquipEffect(char effectID, float arg)
			    : m_effectID(effectID), m_arg(arg)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_effectID >> m_arg;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_effectID << m_arg;
			}

			char m_effectID;
			float m_arg;
		};

		struct TSSAntiData : public KET::Util::Stream::IStreamable
		{
			TSSAntiData() { }

			TSSAntiData(const KET::Util::ByteBuffer& anti_data)
			    : m_anti_data(anti_data)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_anti_data;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_anti_data;
			}

			KET::Util::ByteBuffer m_anti_data;
		};

		struct DlgTableEntry : public KET::Util::Stream::IStreamable
		{
			DlgTableEntry() { }

			DlgTableEntry(const std::string& dlg, float pro)
			    : m_dlg(dlg), m_pro(pro)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_dlg >> m_pro;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_dlg << m_pro;
			}

			std::string m_dlg;
			float m_pro;
		};

		struct DlgTableCFG : public KET::Util::Stream::IStreamable
		{
			DlgTableCFG() { }

			DlgTableCFG(short id, short iconID, const std::vector<DlgTableEntry>& entryList)
			    : m_id(id), m_iconID(iconID), m_entryList(entryList)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_iconID >> m_entryList;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_iconID << m_entryList;
			}

			short m_id;
			short m_iconID;
			std::vector<DlgTableEntry> m_entryList;
		};

		struct TipsCFG : public KET::Util::Stream::IStreamable
		{
			TipsCFG() { }

			TipsCFG(short id, short effID, char seq, char count, 
			        const std::string& title, const std::string& content)
			    : m_id(id), m_effID(effID), m_seq(seq), m_count(count), 
			      m_title(title), m_content(content)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_effID >> m_seq >> m_count
				        >> m_title >> m_content;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_effID << m_seq << m_count
				        << m_title << m_content;
			}

			short m_id;
			short m_effID;
			char m_seq;
			char m_count;
			std::string m_title;
			std::string m_content;
		};

		struct TaskReward : public KET::Util::Stream::IStreamable
		{
			TaskReward() { }

			TaskReward(char type, float arg1, float arg2, float arg3)
			    : m_type(type), m_arg1(arg1), m_arg2(arg2), m_arg3(arg3)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_type >> m_arg1 >> m_arg2 >> m_arg3;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_type << m_arg1 << m_arg2 << m_arg3;
			}

			char m_type;
			float m_arg1;
			float m_arg2;
			float m_arg3;
		};

		struct RandomNameCFG : public KET::Util::Stream::IStreamable
		{
			RandomNameCFG() { }

			RandomNameCFG(const std::vector<std::string>& lastNames, const std::vector<std::string>& mnames, const std::vector<std::string>& fnames, const std::vector<std::string>& robotNames)
			    : m_lastNames(lastNames), m_mnames(mnames), m_fnames(fnames), m_robotNames(robotNames)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_lastNames >> m_mnames >> m_fnames >> m_robotNames;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_lastNames << m_mnames << m_fnames << m_robotNames;
			}

			std::vector<std::string> m_lastNames;
			std::vector<std::string> m_mnames;
			std::vector<std::string> m_fnames;
			std::vector<std::string> m_robotNames;
		};

		struct CombatServerCFG : public KET::Util::Stream::IStreamable
		{
			CombatServerCFG() { }

			CombatServerCFG(short skipCombatItemID)
			    : m_skipCombatItemID(skipCombatItemID)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_skipCombatItemID;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_skipCombatItemID;
			}

			short m_skipCombatItemID;
		};

		struct CombatClientCFG : public KET::Util::Stream::IStreamable
		{
			CombatClientCFG() { }

			CombatClientCFG(short skipCombatItemID, float randomDamageRadius, char showGeneralName, short shadowEffectID, 
			                short moveTargetEffectID, short enemySelectEffectID, short allySelectEffectID, short attackRangeEffectID, 
			                short healEffectID, short damageEffectID, short healCriticalEffectID, short damageCriticalEffectID, 
			                short hseTime, short generalRefreshInterval, char deathDelTime, short commonSkillCool, 
			                short winEffectID, short loseEffectID, short ydbEffectID, short winEffectTime, 
			                short loseEffectTime, short ydbEffectTime, short maxPersistantAttackTime, float escapeHP, 
			                float continueFightHP, float aoeEffectRange, float healHP, short aoeInterval, 
			                char aoeEnemyCount, short firstCombatID, char firstCombatGeneralLevel, const std::vector<short>& firstCombatGenerals, 
			                short dlgIDGeneralStart, short dlgIDAssistantGeneralStart, short dlgIDEnemyGeneralStart, short dlgIDDie, 
			                short dlgIDIdle, short dlgIDKill, short dlgIDCry, float dlgStartHeroProp, 
			                float dlgStartAssistantProp, float dlgStartEnemyProp, float dlgShoutProp, float dlgShoutUseProp, 
			                float dlgDieProp, float dlgIdleProp, float dlgKillProp)
			    : m_skipCombatItemID(skipCombatItemID), m_randomDamageRadius(randomDamageRadius), m_showGeneralName(showGeneralName), m_shadowEffectID(shadowEffectID), 
			      m_moveTargetEffectID(moveTargetEffectID), m_enemySelectEffectID(enemySelectEffectID), m_allySelectEffectID(allySelectEffectID), m_attackRangeEffectID(attackRangeEffectID), 
			      m_healEffectID(healEffectID), m_damageEffectID(damageEffectID), m_healCriticalEffectID(healCriticalEffectID), m_damageCriticalEffectID(damageCriticalEffectID), 
			      m_hseTime(hseTime), m_generalRefreshInterval(generalRefreshInterval), m_deathDelTime(deathDelTime), m_commonSkillCool(commonSkillCool), 
			      m_winEffectID(winEffectID), m_loseEffectID(loseEffectID), m_ydbEffectID(ydbEffectID), m_winEffectTime(winEffectTime), 
			      m_loseEffectTime(loseEffectTime), m_ydbEffectTime(ydbEffectTime), m_maxPersistantAttackTime(maxPersistantAttackTime), m_escapeHP(escapeHP), 
			      m_continueFightHP(continueFightHP), m_aoeEffectRange(aoeEffectRange), m_healHP(healHP), m_aoeInterval(aoeInterval), 
			      m_aoeEnemyCount(aoeEnemyCount), m_firstCombatID(firstCombatID), m_firstCombatGeneralLevel(firstCombatGeneralLevel), m_firstCombatGenerals(firstCombatGenerals), 
			      m_dlgIDGeneralStart(dlgIDGeneralStart), m_dlgIDAssistantGeneralStart(dlgIDAssistantGeneralStart), m_dlgIDEnemyGeneralStart(dlgIDEnemyGeneralStart), m_dlgIDDie(dlgIDDie), 
			      m_dlgIDIdle(dlgIDIdle), m_dlgIDKill(dlgIDKill), m_dlgIDCry(dlgIDCry), m_dlgStartHeroProp(dlgStartHeroProp), 
			      m_dlgStartAssistantProp(dlgStartAssistantProp), m_dlgStartEnemyProp(dlgStartEnemyProp), m_dlgShoutProp(dlgShoutProp), m_dlgShoutUseProp(dlgShoutUseProp), 
			      m_dlgDieProp(dlgDieProp), m_dlgIdleProp(dlgIdleProp), m_dlgKillProp(dlgKillProp)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_skipCombatItemID >> m_randomDamageRadius >> m_showGeneralName >> m_shadowEffectID
				        >> m_moveTargetEffectID >> m_enemySelectEffectID >> m_allySelectEffectID >> m_attackRangeEffectID
				        >> m_healEffectID >> m_damageEffectID >> m_healCriticalEffectID >> m_damageCriticalEffectID
				        >> m_hseTime >> m_generalRefreshInterval >> m_deathDelTime >> m_commonSkillCool
				        >> m_winEffectID >> m_loseEffectID >> m_ydbEffectID >> m_winEffectTime
				        >> m_loseEffectTime >> m_ydbEffectTime >> m_maxPersistantAttackTime >> m_escapeHP
				        >> m_continueFightHP >> m_aoeEffectRange >> m_healHP >> m_aoeInterval
				        >> m_aoeEnemyCount >> m_firstCombatID >> m_firstCombatGeneralLevel >> m_firstCombatGenerals
				        >> m_dlgIDGeneralStart >> m_dlgIDAssistantGeneralStart >> m_dlgIDEnemyGeneralStart >> m_dlgIDDie
				        >> m_dlgIDIdle >> m_dlgIDKill >> m_dlgIDCry >> m_dlgStartHeroProp
				        >> m_dlgStartAssistantProp >> m_dlgStartEnemyProp >> m_dlgShoutProp >> m_dlgShoutUseProp
				        >> m_dlgDieProp >> m_dlgIdleProp >> m_dlgKillProp;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_skipCombatItemID << m_randomDamageRadius << m_showGeneralName << m_shadowEffectID
				        << m_moveTargetEffectID << m_enemySelectEffectID << m_allySelectEffectID << m_attackRangeEffectID
				        << m_healEffectID << m_damageEffectID << m_healCriticalEffectID << m_damageCriticalEffectID
				        << m_hseTime << m_generalRefreshInterval << m_deathDelTime << m_commonSkillCool
				        << m_winEffectID << m_loseEffectID << m_ydbEffectID << m_winEffectTime
				        << m_loseEffectTime << m_ydbEffectTime << m_maxPersistantAttackTime << m_escapeHP
				        << m_continueFightHP << m_aoeEffectRange << m_healHP << m_aoeInterval
				        << m_aoeEnemyCount << m_firstCombatID << m_firstCombatGeneralLevel << m_firstCombatGenerals
				        << m_dlgIDGeneralStart << m_dlgIDAssistantGeneralStart << m_dlgIDEnemyGeneralStart << m_dlgIDDie
				        << m_dlgIDIdle << m_dlgIDKill << m_dlgIDCry << m_dlgStartHeroProp
				        << m_dlgStartAssistantProp << m_dlgStartEnemyProp << m_dlgShoutProp << m_dlgShoutUseProp
				        << m_dlgDieProp << m_dlgIdleProp << m_dlgKillProp;
			}

			short m_skipCombatItemID;
			float m_randomDamageRadius;
			char m_showGeneralName;
			short m_shadowEffectID;
			short m_moveTargetEffectID;
			short m_enemySelectEffectID;
			short m_allySelectEffectID;
			short m_attackRangeEffectID;
			short m_healEffectID;
			short m_damageEffectID;
			short m_healCriticalEffectID;
			short m_damageCriticalEffectID;
			short m_hseTime;
			short m_generalRefreshInterval;
			char m_deathDelTime;
			short m_commonSkillCool;
			short m_winEffectID;
			short m_loseEffectID;
			short m_ydbEffectID;
			short m_winEffectTime;
			short m_loseEffectTime;
			short m_ydbEffectTime;
			short m_maxPersistantAttackTime;
			float m_escapeHP;
			float m_continueFightHP;
			float m_aoeEffectRange;
			float m_healHP;
			short m_aoeInterval;
			char m_aoeEnemyCount;
			short m_firstCombatID;
			char m_firstCombatGeneralLevel;
			std::vector<short> m_firstCombatGenerals;
			short m_dlgIDGeneralStart;
			short m_dlgIDAssistantGeneralStart;
			short m_dlgIDEnemyGeneralStart;
			short m_dlgIDDie;
			short m_dlgIDIdle;
			short m_dlgIDKill;
			short m_dlgIDCry;
			float m_dlgStartHeroProp;
			float m_dlgStartAssistantProp;
			float m_dlgStartEnemyProp;
			float m_dlgShoutProp;
			float m_dlgShoutUseProp;
			float m_dlgDieProp;
			float m_dlgIdleProp;
			float m_dlgKillProp;
		};

		struct MusicCFG : public KET::Util::Stream::IStreamable
		{
			MusicCFG() { }

			MusicCFG(const std::string& cityMusic, const std::string& combatMusic, const std::string& createRoleMusic, const std::string& pvpMusic)
			    : m_cityMusic(cityMusic), m_combatMusic(combatMusic), m_createRoleMusic(createRoleMusic), m_pvpMusic(pvpMusic)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_cityMusic >> m_combatMusic >> m_createRoleMusic >> m_pvpMusic;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_cityMusic << m_combatMusic << m_createRoleMusic << m_pvpMusic;
			}

			std::string m_cityMusic;
			std::string m_combatMusic;
			std::string m_createRoleMusic;
			std::string m_pvpMusic;
		};

		struct PVPMapCFG : public KET::Util::Stream::IStreamable
		{
			PVPMapCFG() { }

			PVPMapCFG(const std::string& map, const std::vector<Float3>& pos)
			    : m_map(map), m_pos(pos)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_map >> m_pos;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_map << m_pos;
			}

			std::string m_map;
			std::vector<Float3> m_pos;
		};

		struct MapCFG : public KET::Util::Stream::IStreamable
		{
			MapCFG() { }

			MapCFG(const std::string& cityMap, const std::string& createRoleMap, const std::string& createRoleScript, float createRoleMapSpeed, 
			       char createRoleMapEnable, const std::vector<PVPMapCFG>& pvpMaps, const std::string& heroBoxName, const std::string& heroBoxName2)
			    : m_cityMap(cityMap), m_createRoleMap(createRoleMap), m_createRoleScript(createRoleScript), m_createRoleMapSpeed(createRoleMapSpeed), 
			      m_createRoleMapEnable(createRoleMapEnable), m_pvpMaps(pvpMaps), m_heroBoxName(heroBoxName), m_heroBoxName2(heroBoxName2)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_cityMap >> m_createRoleMap >> m_createRoleScript >> m_createRoleMapSpeed
				        >> m_createRoleMapEnable >> m_pvpMaps >> m_heroBoxName >> m_heroBoxName2;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_cityMap << m_createRoleMap << m_createRoleScript << m_createRoleMapSpeed
				        << m_createRoleMapEnable << m_pvpMaps << m_heroBoxName << m_heroBoxName2;
			}

			std::string m_cityMap;
			std::string m_createRoleMap;
			std::string m_createRoleScript;
			float m_createRoleMapSpeed;
			char m_createRoleMapEnable;
			std::vector<PVPMapCFG> m_pvpMaps;
			std::string m_heroBoxName;
			std::string m_heroBoxName2;
		};

		struct AlarmCFG : public KET::Util::Stream::IStreamable
		{
			AlarmCFG() { }

			AlarmCFG(short time, const std::string& content)
			    : m_time(time), m_content(content)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_time >> m_content;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_time << m_content;
			}

			short m_time;
			std::string m_content;
		};

		struct EngineCFG : public KET::Util::Stream::IStreamable
		{
			EngineCFG() { }

			EngineCFG(const std::string& horseHS, const std::string& horseCS, float horseTitleOffset, const std::string& texBloodGreen, 
			          const std::string& texBloodRed, const std::string& texNumberEffectGreen, const std::string& texNumberEffectRed, short durNumberEffect, 
			          float gravity, char useCollision, char useKPack, char cameraFollowHero, 
			          char showFPSOnHead, char showErrorOnHead, const std::string& defaultFont, const std::string& defaultStandAction, 
			          const std::string& defaultRunAction, const std::string& defaultAttackAction, const std::vector<std::string>& defaultHurtAction, const std::vector<std::string>& defaultDeathAction, 
			          const std::vector<std::string>& defaultDeathLoopAction, const std::string& defaultWinAction, const std::string& defaultLoseAction, short loadingInitMaxCool, 
			          short dragMin, float dragSpeed, float moveStopRange, float rotateStopRange, 
			          float attackGridSize, float roundBoxScale, float X2Speed, float X3Speed, 
			          float horseBaseSpeed)
			    : m_horseHS(horseHS), m_horseCS(horseCS), m_horseTitleOffset(horseTitleOffset), m_texBloodGreen(texBloodGreen), 
			      m_texBloodRed(texBloodRed), m_texNumberEffectGreen(texNumberEffectGreen), m_texNumberEffectRed(texNumberEffectRed), m_durNumberEffect(durNumberEffect), 
			      m_gravity(gravity), m_useCollision(useCollision), m_useKPack(useKPack), m_cameraFollowHero(cameraFollowHero), 
			      m_showFPSOnHead(showFPSOnHead), m_showErrorOnHead(showErrorOnHead), m_defaultFont(defaultFont), m_defaultStandAction(defaultStandAction), 
			      m_defaultRunAction(defaultRunAction), m_defaultAttackAction(defaultAttackAction), m_defaultHurtAction(defaultHurtAction), m_defaultDeathAction(defaultDeathAction), 
			      m_defaultDeathLoopAction(defaultDeathLoopAction), m_defaultWinAction(defaultWinAction), m_defaultLoseAction(defaultLoseAction), m_loadingInitMaxCool(loadingInitMaxCool), 
			      m_dragMin(dragMin), m_dragSpeed(dragSpeed), m_moveStopRange(moveStopRange), m_rotateStopRange(rotateStopRange), 
			      m_attackGridSize(attackGridSize), m_roundBoxScale(roundBoxScale), m_X2Speed(X2Speed), m_X3Speed(X3Speed), 
			      m_horseBaseSpeed(horseBaseSpeed)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_horseHS >> m_horseCS >> m_horseTitleOffset >> m_texBloodGreen
				        >> m_texBloodRed >> m_texNumberEffectGreen >> m_texNumberEffectRed >> m_durNumberEffect
				        >> m_gravity >> m_useCollision >> m_useKPack >> m_cameraFollowHero
				        >> m_showFPSOnHead >> m_showErrorOnHead >> m_defaultFont >> m_defaultStandAction
				        >> m_defaultRunAction >> m_defaultAttackAction >> m_defaultHurtAction >> m_defaultDeathAction
				        >> m_defaultDeathLoopAction >> m_defaultWinAction >> m_defaultLoseAction >> m_loadingInitMaxCool
				        >> m_dragMin >> m_dragSpeed >> m_moveStopRange >> m_rotateStopRange
				        >> m_attackGridSize >> m_roundBoxScale >> m_X2Speed >> m_X3Speed
				        >> m_horseBaseSpeed;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_horseHS << m_horseCS << m_horseTitleOffset << m_texBloodGreen
				        << m_texBloodRed << m_texNumberEffectGreen << m_texNumberEffectRed << m_durNumberEffect
				        << m_gravity << m_useCollision << m_useKPack << m_cameraFollowHero
				        << m_showFPSOnHead << m_showErrorOnHead << m_defaultFont << m_defaultStandAction
				        << m_defaultRunAction << m_defaultAttackAction << m_defaultHurtAction << m_defaultDeathAction
				        << m_defaultDeathLoopAction << m_defaultWinAction << m_defaultLoseAction << m_loadingInitMaxCool
				        << m_dragMin << m_dragSpeed << m_moveStopRange << m_rotateStopRange
				        << m_attackGridSize << m_roundBoxScale << m_X2Speed << m_X3Speed
				        << m_horseBaseSpeed;
			}

			std::string m_horseHS;
			std::string m_horseCS;
			float m_horseTitleOffset;
			std::string m_texBloodGreen;
			std::string m_texBloodRed;
			std::string m_texNumberEffectGreen;
			std::string m_texNumberEffectRed;
			short m_durNumberEffect;
			float m_gravity;
			char m_useCollision;
			char m_useKPack;
			char m_cameraFollowHero;
			char m_showFPSOnHead;
			char m_showErrorOnHead;
			std::string m_defaultFont;
			std::string m_defaultStandAction;
			std::string m_defaultRunAction;
			std::string m_defaultAttackAction;
			std::vector<std::string> m_defaultHurtAction;
			std::vector<std::string> m_defaultDeathAction;
			std::vector<std::string> m_defaultDeathLoopAction;
			std::string m_defaultWinAction;
			std::string m_defaultLoseAction;
			short m_loadingInitMaxCool;
			short m_dragMin;
			float m_dragSpeed;
			float m_moveStopRange;
			float m_rotateStopRange;
			float m_attackGridSize;
			float m_roundBoxScale;
			float m_X2Speed;
			float m_X3Speed;
			float m_horseBaseSpeed;
		};

		struct GameDataCFGC : public KET::Util::Stream::IStreamable
		{
			GameDataCFGC()
			    : m_version(1)
			{
			}

			GameDataCFGC(int version, const std::vector<DlgTableCFG>& dlgtables, const std::vector<DlgTableCFG>& dlgtablesNew, const std::vector<TipsCFG>& tips, 
			             const std::vector<ArtEffectCFG>& artEffects, const std::vector<UIEffectCFG>& uiEffects, const std::vector<FightEffectCFG>& fightEffects, const std::vector<CombatMapCFG>& combatmaps, 
			             const std::vector<CombatCatCFG>& combatcats, const std::vector<LoadingCFG>& loadingTips, const std::vector<IconCFG>& icons, const std::vector<PackageCFGC>& packages, 
			             const std::vector<AlarmCFG>& alarms, const RandomNameCFG& randomNames, const CombatClientCFG& combat, const MusicCFG& music, 
			             const MapCFG& map, const EngineCFG& engine, const std::string& luaCfg)
			    : m_version(version), m_dlgtables(dlgtables), m_dlgtablesNew(dlgtablesNew), m_tips(tips), 
			      m_artEffects(artEffects), m_uiEffects(uiEffects), m_fightEffects(fightEffects), m_combatmaps(combatmaps), 
			      m_combatcats(combatcats), m_loadingTips(loadingTips), m_icons(icons), m_packages(packages), 
			      m_alarms(alarms), m_randomNames(randomNames), m_combat(combat), m_music(music), 
			      m_map(map), m_engine(engine), m_luaCfg(luaCfg)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_version >> m_dlgtables >> m_dlgtablesNew >> m_tips
				        >> m_artEffects >> m_uiEffects >> m_fightEffects >> m_combatmaps
				        >> m_combatcats >> m_loadingTips >> m_icons >> m_packages
				        >> m_alarms >> m_randomNames >> m_combat >> m_music
				        >> m_map >> m_engine >> m_luaCfg;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_version << m_dlgtables << m_dlgtablesNew << m_tips
				        << m_artEffects << m_uiEffects << m_fightEffects << m_combatmaps
				        << m_combatcats << m_loadingTips << m_icons << m_packages
				        << m_alarms << m_randomNames << m_combat << m_music
				        << m_map << m_engine << m_luaCfg;
			}

			int m_version;
			std::vector<DlgTableCFG> m_dlgtables;
			std::vector<DlgTableCFG> m_dlgtablesNew;
			std::vector<TipsCFG> m_tips;
			std::vector<ArtEffectCFG> m_artEffects;
			std::vector<UIEffectCFG> m_uiEffects;
			std::vector<FightEffectCFG> m_fightEffects;
			std::vector<CombatMapCFG> m_combatmaps;
			std::vector<CombatCatCFG> m_combatcats;
			std::vector<LoadingCFG> m_loadingTips;
			std::vector<IconCFG> m_icons;
			std::vector<PackageCFGC> m_packages;
			std::vector<AlarmCFG> m_alarms;
			RandomNameCFG m_randomNames;
			CombatClientCFG m_combat;
			MusicCFG m_music;
			MapCFG m_map;
			EngineCFG m_engine;
			std::string m_luaCfg;
		};

		struct DBRoleEquip : public KET::Util::Stream::IStreamable
		{
			DBRoleEquip() { }

			DBRoleEquip(short id, short count)
			    : m_id(id), m_count(count)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_count;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_count;
			}

			short m_id;
			short m_count;
		};

		struct DBCombatTypeScore : public KET::Util::Stream::IStreamable
		{
			enum 
			{
				eNull = 0,
				eAllComplete = 1
			};

			DBCombatTypeScore() { }

			DBCombatTypeScore(short catID, char flag, const std::vector<char>& score)
			    : m_catID(catID), m_flag(flag), m_score(score)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_catID >> m_flag >> m_score;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_catID << m_flag << m_score;
			}

			short m_catID;
			char m_flag;
			std::vector<char> m_score;
		};

		struct DBCombatScore : public KET::Util::Stream::IStreamable
		{
			DBCombatScore() { }

			DBCombatScore(char combatType, const std::vector<DBCombatTypeScore>& scores)
			    : m_combatType(combatType), m_scores(scores)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_combatType >> m_scores;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_combatType << m_scores;
			}

			char m_combatType;
			std::vector<DBCombatTypeScore> m_scores;
		};

		struct DBForceItemLog : public KET::Util::Stream::IStreamable
		{
			DBForceItemLog() { }

			DBForceItemLog(short id, int lastTime)
			    : m_id(id), m_lastTime(lastTime)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_lastTime;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_lastTime;
			}

			short m_id;
			int m_lastTime;
		};

		struct PVPGeneral : public KET::Util::Stream::IStreamable
		{
			PVPGeneral() { }

			PVPGeneral(short id, char lvl3, short horseID)
			    : m_id(id), m_lvl3(lvl3), m_horseID(horseID)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_id >> m_lvl3 >> m_horseID;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_id << m_lvl3 << m_horseID;
			}

			short m_id;
			char m_lvl3;
			short m_horseID;
		};

		struct CheckFunctionStateReq : public KET::Util::Stream::IStreamable
		{
			CheckFunctionStateReq() { }

			CheckFunctionStateReq(char countNeed, const std::vector<char>& funcs)
			    : m_countNeed(countNeed), m_funcs(funcs)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_countNeed >> m_funcs;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_countNeed << m_funcs;
			}

			char m_countNeed;
			std::vector<char> m_funcs;
		};

		struct ClearFunctionCoolReq : public KET::Util::Stream::IStreamable
		{
			ClearFunctionCoolReq() { }

			ClearFunctionCoolReq(char funcID, short stone)
			    : m_funcID(funcID), m_stone(stone)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_funcID >> m_stone;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_funcID << m_stone;
			}

			char m_funcID;
			short m_stone;
		};

		struct CheckFunctionStateRes : public KET::Util::Stream::IStreamable
		{
			CheckFunctionStateRes() { }

			CheckFunctionStateRes(const std::vector<char>& funcs, const std::vector<char>& states)
			    : m_funcs(funcs), m_states(states)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_funcs >> m_states;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_funcs << m_states;
			}

			std::vector<char> m_funcs;
			std::vector<char> m_states;
		};

		struct RPCRes : public KET::Util::Stream::IStreamable
		{
			enum 
			{
				eOK = 0,
				eFailed = 1,
				eCool = 2,
				eVit = 4,
				eYuanbao = 5,
				eMoney = 6,
				eMaxTimes = 7,
				eLevel = 8,
				eRoleType = 11,
				eNotFound = 30
			};

			RPCRes() { }

			RPCRes(char ret)
			    : m_ret(ret)
			{
			}

			virtual void Decode(KET::Util::Stream::AIStream &istream)
			{
				istream >> m_ret;
			}

			virtual void Encode(KET::Util::Stream::AOStream &ostream) const
			{
				ostream << m_ret;
			}

			char m_ret;
		};

	}
}

#endif
