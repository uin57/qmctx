// modified by ket.kio.RPCGen at Mon Jan 04 14:34:52 CST 2016.

#ifndef __I3K__TCPCONTROLSERVER_H
#define __I3K__TCPCONTROLSERVER_H

#include <ket/kio/packet.h>
#include <ket/kio/netmanager.h>

#include "abasedencoder.h"

namespace I3K
{

	class RPCManager;
	class TCPControlServer : public KET::KIO::TCPServer<KET::KIO::SimplePacket>, public ABaseDencoder
	{
	public:
		TCPControlServer(KET::KIO::NetManager &managerNet);
		virtual ~TCPControlServer() { }

	public:
		void SetRPCManager(RPCManager *pRPCManager) { m_pRPCManager = pRPCManager; }

	public:
		virtual KET::KIO::IPacketEncoder<KET::KIO::SimplePacket>& GetEncoder() { return *this; }
		virtual KET::KIO::IPacketDecoder<KET::KIO::SimplePacket>& GetDecoder() { return *this; }

	public:
		virtual bool DoCheckPacketType(int ptype);

	public:
		virtual void OnOpen();
		virtual void OnOpenFailed(KET::KIO::ErrorCode errcode);
		virtual void OnClose(KET::KIO::ErrorCode errcode);
		virtual void OnPacketSessionOpen(int sessionid, const KET::KIO::NetAddress &addrClient);
		virtual void OnPacketSessionClose(int sessionid, KET::KIO::ErrorCode errcode);
		virtual void OnPacketSessionRecv(int sessionid, const KET::KIO::SimplePacket *pPacket);

	private:
		RPCManager* m_pRPCManager;
	};
}

#endif
