// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#ifndef __I3K__PACKET_H
#define __I3K__PACKET_H

#include <ket/kio/packet.h>

#include "sbean.h"


namespace I3K
{

	namespace Packet
	{

		enum
		{
			// server to client
			eS2CPKTServerChallenge = 1,
			eS2CPKTServerResponse = 2,
			eS2CPKTLuaChannel = 5,
			eS2CPKTLuaChannel2 = 7,
			eS2CPKTCmnRPCResponse = 100,
			eS2CPKTAntiData = 31,

			// client to server
			eC2SPKTClientResponse = 10001,
			eC2SPKTLuaChannel = 10004,
			eC2SPKTLuaChannel2 = 10005,
			eC2SPKTDataSync = 10030,
			eC2SPKTAntiData = 10031,

			// global to client
			eG2CPKTLuaChannel = 80001,
			eG2CPKTLuaChannel2 = 80002,

			// client to global
			eC2GPKTLuaChannel = 90001,
			eC2GPKTLuaChannel2 = 90002,

		};

		// server to client
		namespace S2C
		{

			class ServerChallenge : public KET::KIO::SimplePacket
			{
			public:
				ServerChallenge() { }

				ServerChallenge(int istate, const std::string& sstate, int flag, const std::vector<unsigned char>& key)
				    : m_istate(istate), m_sstate(sstate), m_flag(flag), m_key(key)
				{
				}

			public:
				virtual int GetType() const
				{
					return eS2CPKTServerChallenge;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_istate >> m_sstate >> m_flag >> m_key;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_istate << m_sstate << m_flag << m_key;
				}

			public:
				int GetIstate() const
				{
					return m_istate;
				}

				void SetIstate(int istate)
				{
					m_istate = istate;
				}

				const std::string& GetSstate() const
				{
					return m_sstate;
				}

				std::string& GetSstate()
				{
					return m_sstate;
				}

				void SetSstate(const std::string& sstate)
				{
					m_sstate = sstate;
				}

				int GetFlag() const
				{
					return m_flag;
				}

				void SetFlag(int flag)
				{
					m_flag = flag;
				}

				std::vector<unsigned char> GetKey() const
				{
					return m_key;
				}

				void SetKey(std::vector<unsigned char> key)
				{
					m_key = key;
				}

			private:
				int m_istate;
				std::string m_sstate;
				int m_flag;
				std::vector<unsigned char> m_key;
			};

			class ServerResponse : public KET::KIO::SimplePacket
			{
			public:
				ServerResponse() { }

				ServerResponse(int res)
				    : m_res(res)
				{
				}

			public:
				virtual int GetType() const
				{
					return eS2CPKTServerResponse;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_res;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_res;
				}

			public:
				int GetRes() const
				{
					return m_res;
				}

				void SetRes(int res)
				{
					m_res = res;
				}

			private:
				int m_res;
			};

			class LuaChannel : public KET::KIO::SimplePacket
			{
			public:
				LuaChannel() { }

				LuaChannel(const std::string& data)
				    : m_data(data)
				{
				}

			public:
				virtual int GetType() const
				{
					return eS2CPKTLuaChannel;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_data;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_data;
				}

			public:
				const std::string& GetData() const
				{
					return m_data;
				}

				std::string& GetData()
				{
					return m_data;
				}

				void SetData(const std::string& data)
				{
					m_data = data;
				}

			private:
				std::string m_data;
			};

			class LuaChannel2 : public KET::KIO::SimplePacket
			{
			public:
				LuaChannel2() { }

				LuaChannel2(const std::vector<std::string>& data)
				    : m_data(data)
				{
				}

			public:
				virtual int GetType() const
				{
					return eS2CPKTLuaChannel2;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_data;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_data;
				}

			public:
				const std::vector<std::string>& GetData() const
				{
					return m_data;
				}

				std::vector<std::string>& GetData()
				{
					return m_data;
				}

				void SetData(const std::vector<std::string>& data)
				{
					m_data = data;
				}

			private:
				std::vector<std::string> m_data;
			};

			class CmnRPCResponse : public KET::KIO::SimplePacket
			{
			public:
				CmnRPCResponse() { }

				CmnRPCResponse(const I3K::SBean::RPCRes& res)
				    : m_res(res)
				{
				}

			public:
				virtual int GetType() const
				{
					return eS2CPKTCmnRPCResponse;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_res;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_res;
				}

			public:
				const I3K::SBean::RPCRes& GetRes() const
				{
					return m_res;
				}

				I3K::SBean::RPCRes& GetRes()
				{
					return m_res;
				}

				void SetRes(const I3K::SBean::RPCRes& res)
				{
					m_res = res;
				}

			private:
				I3K::SBean::RPCRes m_res;
			};

			class AntiData : public KET::KIO::SimplePacket
			{
			public:
				AntiData() { }

				AntiData(const I3K::SBean::TSSAntiData& data)
				    : m_data(data)
				{
				}

			public:
				virtual int GetType() const
				{
					return eS2CPKTAntiData;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_data;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_data;
				}

			public:
				const I3K::SBean::TSSAntiData& GetData() const
				{
					return m_data;
				}

				I3K::SBean::TSSAntiData& GetData()
				{
					return m_data;
				}

				void SetData(const I3K::SBean::TSSAntiData& data)
				{
					m_data = data;
				}

			private:
				I3K::SBean::TSSAntiData m_data;
			};

		}

		// client to server
		namespace C2S
		{

			class ClientResponse : public KET::KIO::SimplePacket
			{
			public:
				ClientResponse() { }

				ClientResponse(const std::vector<unsigned char>& key)
				    : m_key(key)
				{
				}

			public:
				virtual int GetType() const
				{
					return eC2SPKTClientResponse;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_key;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_key;
				}

			public:
				std::vector<unsigned char> GetKey() const
				{
					return m_key;
				}

				void SetKey(std::vector<unsigned char> key)
				{
					m_key = key;
				}

			private:
				std::vector<unsigned char> m_key;
			};

			class LuaChannel : public KET::KIO::SimplePacket
			{
			public:
				LuaChannel() { }

				LuaChannel(const std::string& data)
				    : m_data(data)
				{
				}

			public:
				virtual int GetType() const
				{
					return eC2SPKTLuaChannel;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_data;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_data;
				}

			public:
				const std::string& GetData() const
				{
					return m_data;
				}

				std::string& GetData()
				{
					return m_data;
				}

				void SetData(const std::string& data)
				{
					m_data = data;
				}

			private:
				std::string m_data;
			};

			class LuaChannel2 : public KET::KIO::SimplePacket
			{
			public:
				LuaChannel2() { }

				LuaChannel2(const std::vector<std::string>& data)
				    : m_data(data)
				{
				}

			public:
				virtual int GetType() const
				{
					return eC2SPKTLuaChannel2;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_data;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_data;
				}

			public:
				const std::vector<std::string>& GetData() const
				{
					return m_data;
				}

				std::vector<std::string>& GetData()
				{
					return m_data;
				}

				void SetData(const std::vector<std::string>& data)
				{
					m_data = data;
				}

			private:
				std::vector<std::string> m_data;
			};

			class DataSync : public KET::KIO::SimplePacket
			{
			public:
				enum 
				{
					eBattleData = 9,
					eAutoUserID = 12
				};

			public:
				DataSync() { }

				DataSync(char dataType)
				    : m_dataType(dataType)
				{
				}

			public:
				virtual int GetType() const
				{
					return eC2SPKTDataSync;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_dataType;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_dataType;
				}

			public:
				char GetDataType() const
				{
					return m_dataType;
				}

				void SetDataType(char dataType)
				{
					m_dataType = dataType;
				}

			private:
				char m_dataType;
			};

			class AntiData : public KET::KIO::SimplePacket
			{
			public:
				AntiData() { }

				AntiData(const I3K::SBean::TSSAntiData& data)
				    : m_data(data)
				{
				}

			public:
				virtual int GetType() const
				{
					return eC2SPKTAntiData;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_data;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_data;
				}

			public:
				const I3K::SBean::TSSAntiData& GetData() const
				{
					return m_data;
				}

				I3K::SBean::TSSAntiData& GetData()
				{
					return m_data;
				}

				void SetData(const I3K::SBean::TSSAntiData& data)
				{
					m_data = data;
				}

			private:
				I3K::SBean::TSSAntiData m_data;
			};

		}

		// global to client
		namespace G2C
		{

			class LuaChannel : public KET::KIO::SimplePacket
			{
			public:
				LuaChannel() { }

				LuaChannel(const std::string& data)
				    : m_data(data)
				{
				}

			public:
				virtual int GetType() const
				{
					return eG2CPKTLuaChannel;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_data;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_data;
				}

			public:
				const std::string& GetData() const
				{
					return m_data;
				}

				std::string& GetData()
				{
					return m_data;
				}

				void SetData(const std::string& data)
				{
					m_data = data;
				}

			private:
				std::string m_data;
			};

			class LuaChannel2 : public KET::KIO::SimplePacket
			{
			public:
				LuaChannel2() { }

				LuaChannel2(const std::vector<std::string>& data)
				    : m_data(data)
				{
				}

			public:
				virtual int GetType() const
				{
					return eG2CPKTLuaChannel2;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_data;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_data;
				}

			public:
				const std::vector<std::string>& GetData() const
				{
					return m_data;
				}

				std::vector<std::string>& GetData()
				{
					return m_data;
				}

				void SetData(const std::vector<std::string>& data)
				{
					m_data = data;
				}

			private:
				std::vector<std::string> m_data;
			};

		}

		// client to global
		namespace C2G
		{

			class LuaChannel : public KET::KIO::SimplePacket
			{
			public:
				LuaChannel() { }

				LuaChannel(const std::string& data)
				    : m_data(data)
				{
				}

			public:
				virtual int GetType() const
				{
					return eC2GPKTLuaChannel;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_data;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_data;
				}

			public:
				const std::string& GetData() const
				{
					return m_data;
				}

				std::string& GetData()
				{
					return m_data;
				}

				void SetData(const std::string& data)
				{
					m_data = data;
				}

			private:
				std::string m_data;
			};

			class LuaChannel2 : public KET::KIO::SimplePacket
			{
			public:
				LuaChannel2() { }

				LuaChannel2(const std::vector<std::string>& data)
				    : m_data(data)
				{
				}

			public:
				virtual int GetType() const
				{
					return eC2GPKTLuaChannel2;
				}

				virtual void Decode(KET::Util::Stream::AIStream &istream)
				{
					istream >> m_data;
				}

				virtual void Encode(KET::Util::Stream::AOStream &ostream) const
				{
					ostream << m_data;
				}

			public:
				const std::vector<std::string>& GetData() const
				{
					return m_data;
				}

				std::vector<std::string>& GetData()
				{
					return m_data;
				}

				void SetData(const std::vector<std::string>& data)
				{
					m_data = data;
				}

			private:
				std::vector<std::string> m_data;
			};

		}

	}
}

#endif
