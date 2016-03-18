// modified by ket.kio.RPCGen at Thu Oct 11 16:51:48 CST 2012.

package ket.kpc.rpc;

import ket.kio.SimplePacket;
import ket.kio.SimpleDencoder;

public abstract class ABaseDencoder extends SimpleDencoder
{

	@Override
	public SimplePacket createPacket(int ptype)
	{
		SimplePacket packet = null;
		switch( ptype )
		{
		// tracker to peer
		case Packet.eT2PPKTHello:
			packet = new Packet.T2P.Hello();
			break;
		case Packet.eT2PPKTLoginResponse:
			packet = new Packet.T2P.LoginResponse();
			break;
		case Packet.eT2PPKTTPSTestQuestion:
			packet = new Packet.T2P.TPSTestQuestion();
			break;
		case Packet.eT2PPKTTPSTestResponse:
			packet = new Packet.T2P.TPSTestResponse();
			break;
		case Packet.eT2PPKTUPSTestQuestion:
			packet = new Packet.T2P.UPSTestQuestion();
			break;
		case Packet.eT2PPKTUPSTestResponse:
			packet = new Packet.T2P.UPSTestResponse();
			break;
		case Packet.eT2PPKTSourceAnswer:
			packet = new Packet.T2P.SourceAnswer();
			break;
		case Packet.eT2PPKTTPSRCResponse:
			packet = new Packet.T2P.TPSRCResponse();
			break;
		case Packet.eT2PPKTTPSRCRequest:
			packet = new Packet.T2P.TPSRCRequest();
			break;
		case Packet.eT2PPKTRelay:
			packet = new Packet.T2P.Relay();
			break;
		case Packet.eT2PPKTUPSRCResponse:
			packet = new Packet.T2P.UPSRCResponse();
			break;
		case Packet.eT2PPKTUPSRCRequest:
			packet = new Packet.T2P.UPSRCRequest();
			break;
		case Packet.eT2PPKTRelayClose:
			packet = new Packet.T2P.RelayClose();
			break;
		case Packet.eT2PPKTDataGroupMemberQuery:
			packet = new Packet.T2P.DataGroupMemberQuery();
			break;
		case Packet.eT2PPKTNoticeLocalNetwork:
			packet = new Packet.T2P.NoticeLocalNetwork();
			break;
		case Packet.eT2PPKTTestNetworkTCP:
			packet = new Packet.T2P.TestNetworkTCP();
			break;
		case Packet.eT2PPKTTrackerRedirect:
			packet = new Packet.T2P.TrackerRedirect();
			break;
		case Packet.eT2PPKTPing:
			packet = new Packet.T2P.Ping();
			break;

		// peer to tracker
		case Packet.eP2TPKTLoginRequest:
			packet = new Packet.P2T.LoginRequest();
			break;
		case Packet.eP2TPKTTPSTestRequest:
			packet = new Packet.P2T.TPSTestRequest();
			break;
		case Packet.eP2TPKTTPSTestAnswer:
			packet = new Packet.P2T.TPSTestAnswer();
			break;
		case Packet.eP2TPKTUPSTestRequest:
			packet = new Packet.P2T.UPSTestRequest();
			break;
		case Packet.eP2TPKTUPSTestAnswer:
			packet = new Packet.P2T.UPSTestAnswer();
			break;
		case Packet.eP2TPKTShareUpdateRequest:
			packet = new Packet.P2T.ShareUpdateRequest();
			break;
		case Packet.eP2TPKTSourceQuery:
			packet = new Packet.P2T.SourceQuery();
			break;
		case Packet.eP2TPKTTPSRCRequest:
			packet = new Packet.P2T.TPSRCRequest();
			break;
		case Packet.eP2TPKTTPSRCResponse:
			packet = new Packet.P2T.TPSRCResponse();
			break;
		case Packet.eP2TPKTRelay:
			packet = new Packet.P2T.Relay();
			break;
		case Packet.eP2TPKTUPSRCRequest:
			packet = new Packet.P2T.UPSRCRequest();
			break;
		case Packet.eP2TPKTCreateDataGroup:
			packet = new Packet.P2T.CreateDataGroup();
			break;
		case Packet.eP2TPKTDataGroupMemberAnswer:
			packet = new Packet.P2T.DataGroupMemberAnswer();
			break;
		case Packet.eP2TPKTGroupShareUpdateRequest:
			packet = new Packet.P2T.GroupShareUpdateRequest();
			break;
		case Packet.eP2TPKTTestNetworkTCP:
			packet = new Packet.P2T.TestNetworkTCP();
			break;
		case Packet.eP2TPKTPing:
			packet = new Packet.P2T.Ping();
			break;
		case Packet.eP2TPKTLoadReport:
			packet = new Packet.P2T.LoadReport();
			break;

		// peer to peer
		case Packet.eP2PPKTHello:
			packet = new Packet.P2P.Hello();
			break;
		case Packet.eP2PPKTTPSRCResponse:
			packet = new Packet.P2P.TPSRCResponse();
			break;
		case Packet.eP2PPKTUPSRCResponse:
			packet = new Packet.P2P.UPSRCResponse();
			break;
		case Packet.eP2PPKTUPSPunch:
			packet = new Packet.P2P.UPSPunch();
			break;
		case Packet.eP2PPKTUPSClose:
			packet = new Packet.P2P.UPSClose();
			break;
		case Packet.eP2PPKTRegisterRequest:
			packet = new Packet.P2P.RegisterRequest();
			break;
		case Packet.eP2PPKTRegisterResponse:
			packet = new Packet.P2P.RegisterResponse();
			break;
		case Packet.eP2PPKTUploadRequest:
			packet = new Packet.P2P.UploadRequest();
			break;
		case Packet.eP2PPKTUploadResponse:
			packet = new Packet.P2P.UploadResponse();
			break;
		case Packet.eP2PPKTDataRequest:
			packet = new Packet.P2P.DataRequest();
			break;
		case Packet.eP2PPKTDataResponse:
			packet = new Packet.P2P.DataResponse();
			break;
		case Packet.eP2PPKTFinishUpload:
			packet = new Packet.P2P.FinishUpload();
			break;
		case Packet.eP2PPKTFinishDownload:
			packet = new Packet.P2P.FinishDownload();
			break;
		case Packet.eP2PPKTGroupQuery:
			packet = new Packet.P2P.GroupQuery();
			break;
		case Packet.eP2PPKTGroupAnswer:
			packet = new Packet.P2P.GroupAnswer();
			break;
		case Packet.eP2PPKTNoticeLocalNetworkMember:
			packet = new Packet.P2P.NoticeLocalNetworkMember();
			break;
		case Packet.eP2PPKTLocalNetworkKeepaliveReq:
			packet = new Packet.P2P.LocalNetworkKeepaliveReq();
			break;
		case Packet.eP2PPKTLocalNetworkKeepaliveRes:
			packet = new Packet.P2P.LocalNetworkKeepaliveRes();
			break;
		case Packet.eP2PPKTLocalNetworkShareUpdate:
			packet = new Packet.P2P.LocalNetworkShareUpdate();
			break;
		case Packet.eP2PPKTLocalQuery:
			packet = new Packet.P2P.LocalQuery();
			break;
		case Packet.eP2PPKTLocalQueryAnswer:
			packet = new Packet.P2P.LocalQueryAnswer();
			break;
		case Packet.eP2PPKTQueryRequest:
			packet = new Packet.P2P.QueryRequest();
			break;
		case Packet.eP2PPKTQueryResponse:
			packet = new Packet.P2P.QueryResponse();
			break;

		// monitor to peer
		case Packet.eM2PPKTHello:
			packet = new Packet.M2P.Hello();
			break;
		case Packet.eM2PPKTSnapshotRequest:
			packet = new Packet.M2P.SnapshotRequest();
			break;

		// peer to monitor
		case Packet.eP2MPKTHello:
			packet = new Packet.P2M.Hello();
			break;
		case Packet.eP2MPKTSnapshotResponse:
			packet = new Packet.P2M.SnapshotResponse();
			break;

		// control to peer
		case Packet.eC2PPKTHello:
			packet = new Packet.C2P.Hello();
			break;
		case Packet.eC2PPKTRegisterDistDigest:
			packet = new Packet.C2P.RegisterDistDigest();
			break;
		case Packet.eC2PPKTDistFile:
			packet = new Packet.C2P.DistFile();
			break;
		case Packet.eC2PPKTCheckFile:
			packet = new Packet.C2P.CheckFile();
			break;
		case Packet.eC2PPKTDistFileList:
			packet = new Packet.C2P.DistFileList();
			break;

		// peer to control
		case Packet.eP2CPKTHello:
			packet = new Packet.P2C.Hello();
			break;
		case Packet.eP2CPKTDistFile:
			packet = new Packet.P2C.DistFile();
			break;
		case Packet.eP2CPKTDistFileFrag:
			packet = new Packet.P2C.DistFileFrag();
			break;
		case Packet.eP2CPKTDistFileUpdate:
			packet = new Packet.P2C.DistFileUpdate();
			break;
		case Packet.eP2CPKTCheckFile:
			packet = new Packet.P2C.CheckFile();
			break;
		case Packet.eP2CPKTCheckFileFrag:
			packet = new Packet.P2C.CheckFileFrag();
			break;
		case Packet.eP2CPKTDistFileList:
			packet = new Packet.P2C.DistFileList();
			break;
		case Packet.eP2CPKTUpdatePeerStatus:
			packet = new Packet.P2C.UpdatePeerStatus();
			break;

		default:
			break;
		}
		return packet;
	}

	@Override
	public boolean doCheckPacketType(int ptype)
	{
		return true;
	}
}
