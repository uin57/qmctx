// modified by ket.kio.RPCGen at Thu Jan 17 13:04:56 CST 2013.

package ket.kpc;

import java.util.List;
import ket.util.Stream;

public final class SBean
{

	public static class PeerLoginRequest implements Stream.IStreamable
	{

		public PeerLoginRequest() { }

		public PeerLoginRequest(int peerVersion, String pubIP, long KUID, int peerFlag, 
		       																	String localIP, int localTCPPort, int localUDPPort)
		{
			this.peerVersion = peerVersion;
			this.pubIP = pubIP;
			this.KUID = KUID;
			this.peerFlag = peerFlag;
			this.localIP = localIP;
			this.localTCPPort = localTCPPort;
			this.localUDPPort = localUDPPort;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			peerVersion = is.popInteger();
			pubIP = is.popString();
			KUID = is.popLong();
			peerFlag = is.popInteger();
			localIP = is.popString();
			localTCPPort = is.popInteger();
			localUDPPort = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(peerVersion);
			os.pushString(pubIP);
			os.pushLong(KUID);
			os.pushInteger(peerFlag);
			os.pushString(localIP);
			os.pushInteger(localTCPPort);
			os.pushInteger(localUDPPort);
		}

		public int peerVersion;
		public String pubIP;
		public long KUID;
		public int peerFlag;
		public String localIP;
		public int localTCPPort;
		public int localUDPPort;
	}

	public static class LocalPeerID implements Stream.IStreamable
	{

		public LocalPeerID() { }

		public LocalPeerID(long peerID, String localIP, int localTCPPort, int localUDPPort)
		{
			this.peerID = peerID;
			this.localIP = localIP;
			this.localTCPPort = localTCPPort;
			this.localUDPPort = localUDPPort;
		}

		@Override
		public String toString()
		{
			return "[" + peerID + " " + localIP + " " + localTCPPort + " " + localUDPPort
		             + "]";
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			peerID = is.popLong();
			localIP = is.popString();
			localTCPPort = is.popInteger();
			localUDPPort = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushLong(peerID);
			os.pushString(localIP);
			os.pushInteger(localTCPPort);
			os.pushInteger(localUDPPort);
		}

		public long peerID;
		public String localIP;
		public int localTCPPort;
		public int localUDPPort;
	}

	public static class LocalNetworkNotice implements Stream.IStreamable
	{

		public LocalNetworkNotice() { }

		public LocalNetworkNotice(LocalPeerID head, List<LocalPeerID> members)
		{
			this.head = head;
			this.members = members;
		}

		@Override
		public String toString()
		{
			return "[" + head + " " + members + "]";
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( head == null )
				head = new LocalPeerID();
			is.pop(head);
			members = is.popList(LocalPeerID.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(head);
			os.pushList(members);
		}

		public LocalPeerID head;
		public List<LocalPeerID> members;
	}

	public static class NetworkTCPTest implements Stream.IStreamable
	{

		public NetworkTCPTest() { }

		public NetworkTCPTest(long headID, ket.kio.NetAddress addr)
		{
			this.headID = headID;
			this.addr = addr;
		}

		@Override
		public String toString()
		{
			return "[" + headID + " " + addr + "]";
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			headID = is.popLong();
			if( addr == null )
				addr = new ket.kio.NetAddress();
			is.pop(addr);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushLong(headID);
			os.push(addr);
		}

		public long headID;
		public ket.kio.NetAddress addr;
	}

	public static class PieceInfo implements Stream.IStreamable
	{

		public static final int eCryNull = 0;
		public static final int eCryZip = 1;

		public PieceInfo() { }

		public PieceInfo(int pieceSize, int orgSize, String digest, byte cryType)
		{
			this.pieceSize = pieceSize;
			this.orgSize = orgSize;
			this.digest = digest;
			this.cryType = cryType;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			pieceSize = is.popInteger();
			orgSize = is.popInteger();
			digest = is.popString();
			cryType = is.popByte();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(pieceSize);
			os.pushInteger(orgSize);
			os.pushString(digest);
			os.pushByte(cryType);
		}

		public int pieceSize;
		public int orgSize;
		public String digest;
		public byte cryType;
	}

	public static class Section implements Stream.IStreamable
	{

		public Section() { }

		public Section(int offset, int count)
		{
			this.offset = offset;
			this.count = count;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			offset = is.popInteger();
			count = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(offset);
			os.pushInteger(count);
		}

		public int offset;
		public int count;
	}

	public static class Session implements Stream.IStreamable
	{

		public static enum Type
		{
			eTTS,
			eTPS,
			eTPC
		}
		public static enum State
		{
			eIdle,
			eConnecting,
			eConnected
		}
		public Session() { }

		public Session(int id, Type type, ket.kio.NetAddress addr, State state, 
		       								int downloadBytes, int uploadBytes, int downloadSpeed, int uploadSpeed)
		{
			this.id = id;
			this.type = type;
			this.addr = addr;
			this.state = state;
			this.downloadBytes = downloadBytes;
			this.uploadBytes = uploadBytes;
			this.downloadSpeed = downloadSpeed;
			this.uploadSpeed = uploadSpeed;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			id = is.popInteger();
			type = is.popEnum(Type.values());
			if( addr == null )
				addr = new ket.kio.NetAddress();
			is.pop(addr);
			state = is.popEnum(State.values());
			downloadBytes = is.popInteger();
			uploadBytes = is.popInteger();
			downloadSpeed = is.popInteger();
			uploadSpeed = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(id);
			os.pushEnum(type);
			os.push(addr);
			os.pushEnum(state);
			os.pushInteger(downloadBytes);
			os.pushInteger(uploadBytes);
			os.pushInteger(downloadSpeed);
			os.pushInteger(uploadSpeed);
		}

		public int id;
		public Type type;
		public ket.kio.NetAddress addr;
		public State state;
		public int downloadBytes;
		public int uploadBytes;
		public int downloadSpeed;
		public int uploadSpeed;
	}

	public static class Task implements Stream.IStreamable
	{

		public static enum State
		{
			eShare,
			eDownload
		}
		public static enum DState
		{
			eIdle,
			eWaiting,
			eDownloading
		}
		public Task() { }

		public Task(String digest, State state, DState dState, int nProviders, 
		       					int nextQuery)
		{
			this.digest = digest;
			this.state = state;
			this.dState = dState;
			this.nProviders = nProviders;
			this.nextQuery = nextQuery;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			digest = is.popString();
			state = is.popEnum(State.values());
			dState = is.popEnum(DState.values());
			nProviders = is.popInteger();
			nextQuery = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(digest);
			os.pushEnum(state);
			os.pushEnum(dState);
			os.pushInteger(nProviders);
			os.pushInteger(nextQuery);
		}

		public String digest;
		public State state;
		public DState dState;
		public int nProviders;
		public int nextQuery;
	}

	public static class TaskX implements Stream.IStreamable
	{

		public static enum State
		{
			eShare,
			eDownloadIdle,
			eDownloadWaiting,
			eDownloading
		}
		public TaskX() { }

		public TaskX(int state, int size)
		{
			this.state = state;
			this.size = size;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			state = is.popInteger();
			size = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(state);
			os.pushInteger(size);
		}

		public int state;
		public int size;
	}

	public static class TaskXName implements Stream.IStreamable
	{

		public TaskXName() { }

		public TaskXName(String digest, int index)
		{
			this.digest = digest;
			this.index = index;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			digest = is.popString();
			index = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(digest);
			os.pushInteger(index);
		}

		public String digest;
		public int index;
	}

	public static class PeerSnapshot implements Stream.IStreamable
	{

		public PeerSnapshot() { }

		public PeerSnapshot(List<TaskX> tasks, List<TaskXName> taskNames, List<Session> sessions)
		{
			this.tasks = tasks;
			this.taskNames = taskNames;
			this.sessions = sessions;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			tasks = is.popList(TaskX.class);
			taskNames = is.popList(TaskXName.class);
			sessions = is.popList(Session.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushList(tasks);
			os.pushList(taskNames);
			os.pushList(sessions);
		}

		public List<TaskX> tasks;
		public List<TaskXName> taskNames;
		public List<Session> sessions;
	}

	public static class DistFileInfo implements Stream.IStreamable
	{

		public DistFileInfo() { }

		public DistFileInfo(String fileName, long size)
		{
			this.fileName = fileName;
			this.size = size;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fileName = is.popString();
			size = is.popLong();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(fileName);
			os.pushLong(size);
		}

		public String fileName;
		public long size;
	}

	public static class DistFileDownloadInfo implements Stream.IStreamable
	{

		public DistFileDownloadInfo() { }

		public DistFileDownloadInfo(long totalSize, long completedSize)
		{
			this.totalSize = totalSize;
			this.completedSize = completedSize;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			totalSize = is.popLong();
			completedSize = is.popLong();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushLong(totalSize);
			os.pushLong(completedSize);
		}

		public long totalSize;
		public long completedSize;
	}

	public static class DistFileListRequest implements Stream.IStreamable
	{

		public DistFileListRequest() { }

		public DistFileListRequest(String digest, int index)
		{
			this.digest = digest;
			this.index = index;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			digest = is.popString();
			index = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(digest);
			os.pushInteger(index);
		}

		public String digest;
		public int index;
	}

	public static class DistFileListResponse implements Stream.IStreamable
	{

		public static enum ErrorCode
		{
			eOK,
			eDigestErr
		}
		public DistFileListResponse() { }

		public DistFileListResponse(ErrorCode errcode, String digest, int totalCount, int index, 
		       																					int count, List<DistFileInfo> files)
		{
			this.errcode = errcode;
			this.digest = digest;
			this.totalCount = totalCount;
			this.index = index;
			this.count = count;
			this.files = files;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			errcode = is.popEnum(ErrorCode.values());
			digest = is.popString();
			totalCount = is.popInteger();
			index = is.popInteger();
			count = is.popInteger();
			files = is.popList(DistFileInfo.class);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushEnum(errcode);
			os.pushString(digest);
			os.pushInteger(totalCount);
			os.pushInteger(index);
			os.pushInteger(count);
			os.pushList(files);
		}

		public ErrorCode errcode;
		public String digest;
		public int totalCount;
		public int index;
		public int count;
		public List<DistFileInfo> files;
	}

	public static class DistFileRequest implements Stream.IStreamable
	{

		public static enum Priority
		{
			eCritical,
			eNormal,
			eBackground
		}
		public DistFileRequest() { }

		public DistFileRequest(String fileName, Priority priority)
		{
			this.fileName = fileName;
			this.priority = priority;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fileName = is.popString();
			priority = is.popEnum(Priority.values());
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(fileName);
			os.pushEnum(priority);
		}

		public String fileName;
		public Priority priority;
	}

	public static class DistFileResponse implements Stream.IStreamable
	{

		public static enum ErrorCode
		{
			eOK,
			eFileNotFound,
			eEmptyFile,
			eReadError,
			eNetError
		}
		public DistFileResponse() { }

		public DistFileResponse(String fileName, List<PieceInfo> pieces, ErrorCode errcode)
		{
			this.fileName = fileName;
			this.pieces = pieces;
			this.errcode = errcode;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			fileName = is.popString();
			pieces = is.popList(PieceInfo.class);
			errcode = is.popEnum(ErrorCode.values());
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushString(fileName);
			os.pushList(pieces);
			os.pushEnum(errcode);
		}

		public String fileName;
		public List<PieceInfo> pieces;
		public ErrorCode errcode;
	}

	public static class PeerStatus implements Stream.IStreamable
	{

		public PeerStatus() { }

		public PeerStatus(int downSpeed)
		{
			this.downSpeed = downSpeed;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			downSpeed = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(downSpeed);
		}

		public int downSpeed;
	}

	public static class TrackerRedirectInfo implements Stream.IStreamable
	{

		public TrackerRedirectInfo() { }

		public TrackerRedirectInfo(ket.kio.NetAddress addr)
		{
			this.addr = addr;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			if( addr == null )
				addr = new ket.kio.NetAddress();
			is.pop(addr);
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.push(addr);
		}

		public ket.kio.NetAddress addr;
	}

	public static class PeerLoadInfo implements Stream.IStreamable
	{

		public PeerLoadInfo() { }

		public PeerLoadInfo(int load)
		{
			this.load = load;
		}

		@Override
		public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException
		{
			load = is.popInteger();
		}

		@Override
		public void encode(Stream.AOStream os)
		{
			os.pushInteger(load);
		}

		public int load;
	}

}
