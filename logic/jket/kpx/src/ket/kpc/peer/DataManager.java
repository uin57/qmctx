
package ket.kpc.peer;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import ket.util.BitArray;
import ket.util.FileSys;
import ket.util.FileTool;
import ket.util.StringConverter;
import ket.util.Timeout;
import ket.util.Cooler;
import ket.pack.KDigest;
import ket.kpc.SBean;
import ket.kpc.ErrorCode;
import ket.kpc.IDownloadCallback;
import ket.kpc.KDownloadTask;
import ket.kpc.common.Constant;
import ket.pack.Digest;
import ket.util.FileMappingDataStorage;
import ket.util.FileDataSource;
import ket.util.Storage;
import ket.kpc.common.PeerID;
import ket.pack.DigestTool;
import ket.util.WrappedMemoryDataStorage;

final class DataManager
{
	private static final int MAX_UPLOAD_PACKET_LENGTH = 8192;
	
	private static class PieceDownloadStatus
	{
		public static final long SOURCE_QUERY_COOL_TIME = 1 * 1000; // ms
		public static final long REGISTER_COOL_TIME = 60 * 1000; // ms
		public static enum State
		{
			ePDSIdle,
			ePDSWaiting,
			ePDSDownloading
		}		
		
		/*
		public int getBytesCopied()
		{
			if( baFragments == null )
				return 0;
			return this.baFragments.count(true) * Constant.FRAGMENT_SIZE;
		}
		*/
		
		public State state = State.ePDSIdle;
		public List<PeerID> providers = new ArrayList<PeerID>();
		public Cooler coolerSourceQuery = new Cooler(SOURCE_QUERY_COOL_TIME);
		public PeerID peeridProvider = new PeerID();
		public Cooler coolerRegister = new Cooler(REGISTER_COOL_TIME);
		public int fragIndex = 0;
		public ByteBuffer fragBuffer = null;
		public BitArray baFragments = null;
		public IDownloadCallback callback = null;
		public Digest group = null;
	}
	
	private static class PieceUploadStatus
	{
		public static final long WAIT_DEMANDER_TIMEOUT = 30 * 1000;		
		public static enum State
		{
			ePUSIdle,
			ePUSWaiting,
			ePUSUploading
		}
		
		public State state = State.ePUSIdle;
		public List<PeerID> demanders = new ArrayList<PeerID>();
		public PeerID peeridDemander = new PeerID();
		public Timeout timeoutWaitDemander = new Timeout(WAIT_DEMANDER_TIMEOUT);
	}
	
	private static class Task
	{
		public Digest digest = null;
		public PieceDownloadStatus pds = null;
		public PieceUploadStatus pus = null;
		public Storage storage = null;
	}
	
	private static class DataGroup
	{
		public DataGroup(List<Digest> digests, boolean bLocal)
		{
			this.bLocal = bLocal;
			this.digests = digests;
			digestsMap = new HashMap<Digest, Integer>();
			int i = 0;
			for(Digest d : digests)
			{
				digestsMap.put(d, i);
				++i;
			}
		}
		
		public boolean bLocal;
		public List<Digest> digests;
		public Map<Digest, Integer> digestsMap;
	}
	
	public DataManager(Peer peer)
	{
		this.peer = peer;
	}
	
	private static class RegisterTask
	{
		public RegisterTask(PeerID peerid, Digest digest, Digest group)
		{
			this.peerid = peerid;
			this.digest = digest;
			this.group = group;
		}
		
		PeerID peerid;
		Digest digest;
		Digest group;
	}
	
	private static class UploadTask
	{
		public UploadTask(PeerID peerid, Digest digest)
		{
			this.peerid = peerid;
			this.digest = digest;
		}
		
		PeerID peerid;
		Digest digest;
	}
	
	private static class QueryTask
	{
		public QueryTask(Digest digest, Digest group)
		{
			this.digest = digest;
			this.group = group;
		}
		
		Digest digest;
		Digest group;
	}
	
	private static class CreateDataGroupTask
	{
		public CreateDataGroupTask(Digest group, int membercount)
		{
			this.group = group;
			this.membercount = membercount;
		}
		
		Digest group;		
		int membercount;
	}
	
	private class MainTask implements Runnable
	{
		@Override
		public void run()
		{
			List<RegisterTask> rts = new ArrayList<RegisterTask>();
			List<UploadTask> uts = new ArrayList<UploadTask>();
			List<QueryTask> qts = new ArrayList<QueryTask>();
			synchronized( DataManager.this )
			{
				for(Task task : mapTasks.values() )
				{
					if( task.pus != null )
					{
						switch( task.pus.state )
						{
						case ePUSIdle:
							{
								if( ! task.pus.demanders.isEmpty() )
								{
									PeerID peeridDemander = task.pus.demanders.remove(0);
									task.pus.peeridDemander = peeridDemander;
									task.pus.state = PieceUploadStatus.State.ePUSWaiting;
									task.pus.timeoutWaitDemander.reset();
									uts.add(new UploadTask(task.pus.peeridDemander, task.digest));							
								}
							}
							break;
						case ePUSWaiting:
							if( task.pus.timeoutWaitDemander.isTimeout() )
								task.pus.state = PieceUploadStatus.State.ePUSIdle;
							break;
						default:
							break;
						}
					}
					else if( task.pds != null )
					{
						switch( task.pds.state )
						{
						case ePDSIdle:
							if( task.pds.providers.isEmpty() )
							{
								if( task.pds.coolerSourceQuery.tryReset() )
									qts.add(new QueryTask(task.digest, task.pds.group));
							}
							else // try connecting
							{
								task.pds.peeridProvider = task.pds.providers.remove(task.pds.providers.size()-1);
								task.pds.coolerRegister.clear();
								task.pds.state = PieceDownloadStatus.State.ePDSWaiting;
							}
							break;
						case ePDSWaiting:
							if( task.pds.coolerRegister.tryReset() )
								rts.add(new RegisterTask(task.pds.peeridProvider, task.digest, task.pds.group));
							break;
						default:
							break;
						}
					}
				}
			}
			for(RegisterTask rt : rts)
				peer.getRPCManager().requestRegister(rt.peerid, rt.digest, rt.group);
			for(UploadTask ut : uts)
				peer.getRPCManager().requestUpload(ut.peerid, ut.digest);
			for(QueryTask qt : qts)
				peer.getRPCManager().querySource(qt.digest, qt.group);
		}
		
		public ScheduledFuture<?> future = null;
		
	}
	
	private class ShareUpdateTask implements Runnable
	{
		@Override
		public void run()
		{
			List<Digest> lst = new ArrayList<Digest>();
			synchronized( this )
			{
				for(Digest d : digests)
					lst.add(d);
				digests.clear();
			}
			if( peer.getRPCManager().isTrackerOK() )
			{
				Map<Digest, List<Integer>> gsuts = new HashMap<Digest, List<Integer>>();
				for(Digest d : lst)
				{
					Digest group = null;
					Integer seq = null;
					synchronized( mapDataGroups )
					{
						for(Map.Entry<Digest, DataGroup> e : mapDataGroups.entrySet())
						{
							DataGroup dg = e.getValue();
							seq = dg.digestsMap.get(d);
							if( seq != null )
							{
								group = e.getKey();
								break;
							}
						}
					}
					if( seq == null)
						peer.getRPCManager().shareUpdate(d, true);
					else
					{
						List<Integer> ilst = gsuts.get(group);
						if( ilst != null )
							ilst.add(seq);
						else
						{
							ilst = new ArrayList<Integer>();
							ilst.add(seq);
							gsuts.put(group, ilst);
						}
					}
				}
				for(Map.Entry<Digest, List<Integer>> e : gsuts.entrySet())
				{
					boolean bAll = false;
					synchronized( mapDataGroups )
					{
						bAll = ( mapDataGroups.get(e.getKey()).digests.size() == e.getValue().size() );
					}
					if( bAll )
						e.getValue().clear();
					final int MAX_INDEX_COUNT = 2000;
					int start = 0;
					do
					{
						int end = Math.min(start + MAX_INDEX_COUNT, e.getValue().size());
						peer.getRPCManager().groupShareUpdate(e.getKey(), e.getValue().subList(start, end));
						start = end;
					}
					while( start < e.getValue().size());
				}
			}
		}
		
		public void addDigest(Digest digest)
		{
			synchronized( this )
			{
				digests.add(digest);
			}
		}
		
		public ScheduledFuture<?> future = null;
		private Set<Digest> digests = new HashSet<Digest>();
	}
	
	public synchronized ByteBuffer requestData(Digest digest, PeerID peeridDemander, SBean.Section section)
	{
		Task task = mapTasks.get(digest);
		if( task == null || task.pus == null )
			return null;
		if( task.pus.state != PieceUploadStatus.State.ePUSUploading || ! task.pus.peeridDemander.equalsID(peeridDemander) )
			return null;
		int len = Math.min(section.count, MAX_UPLOAD_PACKET_LENGTH);
		return task.storage.read(section.offset, len, null);
	}	
	
	public boolean setDataRet(Digest digest, PeerID peeridProvider, SBean.Section section, ByteBuffer data)
	{
		boolean bFinished = false;
		synchronized( this )
		{
			Task task = mapTasks.get(digest);
			if( task == null || task.pds == null )
				return false;
			if( task.pds.state != PieceDownloadStatus.State.ePDSDownloading )
				return false;
			if( task.pds.baFragments == null || task.pds.fragBuffer == null )
				return false;
			if( section == null || data == null || data.limit() <= data.position() )
				return false;
			if( section.offset != task.pds.fragIndex * Constant.FRAGMENT_SIZE + task.pds.fragBuffer.position()) 
				return false;
			if( section.count > task.pds.fragBuffer.remaining() )
				return false;
			if( data.remaining() > section.count )
				return false;
			task.pds.fragBuffer.put(data);
			if( task.pds.fragBuffer.hasRemaining() )
			{
				peer.getRPCManager().requestData(peeridProvider, digest, new SBean.Section(task.pds.fragIndex * Constant.FRAGMENT_SIZE + task.pds.fragBuffer.position(),
					task.pds.fragBuffer.remaining()));
				return true;
			}
			if( ! task.storage.write(task.pds.fragIndex * Constant.FRAGMENT_SIZE,
					task.pds.fragBuffer.limit(), task.pds.fragBuffer.array()) )
				return false;
			task.pds.baFragments.setBit(task.pds.fragIndex);
			int total = task.digest.datalen;
			if( task.pds.baFragments.contains(false) )
			{
				for(int i=0; i< task.pds.baFragments.size(); ++i)
				{
					if( ++task.pds.fragIndex == task.pds.baFragments.size() )
						task.pds.fragIndex = 0;
					if( task.pds.baFragments.isClear(task.pds.fragIndex) )
						break;				
				}
				int nlast = total % Constant.FRAGMENT_SIZE;
				task.pds.fragBuffer = ByteBuffer.allocate((nlast == 0 || task.pds.fragIndex != task.pds.baFragments.size() - 1) ? Constant.FRAGMENT_SIZE: nlast);
				peer.getRPCManager().requestData(peeridProvider, digest, new SBean.Section(task.pds.fragIndex * Constant.FRAGMENT_SIZE + task.pds.fragBuffer.position(),
					task.pds.fragBuffer.remaining()));
			}
			else
			{
				if( ! dtool.verify(task.storage, task.digest) )
				{
					if( ++task.pds.fragIndex == task.pds.baFragments.size() )
						task.pds.fragIndex = 0;
					task.pds.baFragments.clearBit(task.pds.fragIndex);
					int nlast = total % Constant.FRAGMENT_SIZE;
					task.pds.fragBuffer = ByteBuffer.allocate((nlast == 0 || task.pds.fragIndex != task.pds.baFragments.size() - 1) ? Constant.FRAGMENT_SIZE: nlast);
					peer.getRPCManager().requestData(peeridProvider, digest, new SBean.Section(task.pds.fragIndex * Constant.FRAGMENT_SIZE + task.pds.fragBuffer.position(),
						task.pds.fragBuffer.remaining()));
				}
				else
					bFinished = true;
			}
		}
		if( bFinished )
			peer.getRPCManager().finishDownload(peeridProvider, digest);
		return true;
	}	
	
	public void setSourceAnswer(Digest digest, List<PeerID> providers)
	{
		if( providers == null || providers.isEmpty() )
			return;
		
		PeerID peeridProvider = null;
		Digest group = null;
		synchronized( this )
		{
			Task task = mapTasks.get(digest);
			if( task == null )
				return;
			if( task.pds == null )
				return;
			task.pds.providers.addAll(providers);
			Collections.shuffle(task.pds.providers);
			if( task.pds.state == PieceDownloadStatus.State.ePDSIdle )
			{	
				task.pds.peeridProvider = task.pds.providers.remove(task.pds.providers.size()-1);
				task.pds.state = PieceDownloadStatus.State.ePDSWaiting;
				
				peeridProvider = task.pds.peeridProvider;
				group = task.pds.group;
				task.pds.coolerRegister.reset();
			}
		}
		if( peeridProvider != null )
			peer.getRPCManager().requestRegister(peeridProvider, digest, group);
	}	
	
	public synchronized int[] register(Digest digest, PeerID peeridDemander)
	{
		int[] ret = new int[2];
		int rank = 0;
		Task task = mapTasks.get(digest);
		if( task != null && task.pus != null ) 
		{
			rank = 1;
			List<PeerID> lst = task.pus.demanders;
			for(PeerID peerid : lst )
			{
				if( peerid.equalsID(peeridDemander) )
				{
					ret[0] = rank;
					return ret;
				}
				++rank;
			}
			lst.add(peeridDemander);
		}
		if( rank == 1 && task.pus.state == PieceUploadStatus.State.ePUSIdle )
		{
			task.pus.demanders.clear();
			task.pus.peeridDemander = peeridDemander;
			task.pus.state = PieceUploadStatus.State.ePUSWaiting;
			task.pus.timeoutWaitDemander.reset();
			ret[1] = 1;
		}
		ret[0] = rank;
		return ret;
	}
	
	public synchronized void setRegisterRet(Digest digest, int rank)
	{
		Task task = mapTasks.get(digest);
		if( task == null || task.pds == null )
			return;
		if( task.pds.state != PieceDownloadStatus.State.ePDSWaiting )
			return;
		if( rank == 0 )
			task.pds.state = PieceDownloadStatus.State.ePDSIdle;
	}
	
	public synchronized void setUploadRet(PeerID peeridDemander, Digest digest, boolean bOK)
	{	
		Task task = mapTasks.get(digest);
		if( task == null || task.pus == null )
			return;
		if( task.pus.state != PieceUploadStatus.State.ePUSWaiting )
			return;
		if( ! task.pus.peeridDemander.equalsID(peeridDemander) )
			return;
		if( bOK )
			task.pus.state = PieceUploadStatus.State.ePUSUploading;
		else
			task.pus.state = PieceUploadStatus.State.ePUSIdle;
	}	
	
	public SBean.Section requestUpload(Digest digest, PeerID peeridProvider)
	{
		SBean.Section section = null;
		synchronized( this )
		{
			Task task = mapTasks.get(digest);
			if( task == null || task.pds == null )
				return null;
			if( task.pds.state != PieceDownloadStatus.State.ePDSWaiting
				|| ! task.pds.peeridProvider.equalsID(peeridProvider) )
				return null;
			task.pds.state = PieceDownloadStatus.State.ePDSDownloading;
			int total = task.digest.datalen;
			if( task.pds.baFragments == null )
			{
				int nf = total / Constant.FRAGMENT_SIZE;
				if( total % Constant.FRAGMENT_SIZE != 0 )
					++nf;
				task.pds.fragIndex = 0;
				task.pds.baFragments = new BitArray(nf, false);
				task.pds.fragBuffer = ByteBuffer.allocate((nf <= 1) ? total : Constant.FRAGMENT_SIZE);
			}
			section = new SBean.Section(task.pds.fragIndex * Constant.FRAGMENT_SIZE + task.pds.fragBuffer.position(),
					task.pds.fragBuffer.remaining());
		}
		return section;
	}
	
	public synchronized void finishUpload(Digest digest)
	{
		Task task = mapTasks.get(digest);
		if( task == null || task.pus == null )
			return;
		if( task.pus.state != PieceUploadStatus.State.ePUSUploading )
			return;
		task.pus.state = PieceUploadStatus.State.ePUSIdle;
	}
	
	public void finishDownload(Digest digest)
	{
		ErrorCode errcode = ErrorCode.eOK;
		IDownloadCallback callbackErr = null;
		IDownloadCallback callbackCom = null;
		boolean bOK = false;
		synchronized( this )
		{
			Task task = mapTasks.get(digest);
			if( task == null || task.pds == null )
				return;
			if( task.pds.baFragments != null && ! task.pds.baFragments.contains(false) )
			{				
				bOK = true;				
				FileMappingDataStorage storage = (FileMappingDataStorage)(task.storage);
				String fnTmp = storage.getFileName();
				String fnDownload = dirDownload + File.separator + StringConverter.getHexStringFromBytes(digest.getBytes());
				if( ! FileTool.ensureDirectoryExist(dirDownload) 
						|| ! FileSys.moveFile(fnTmp, fnDownload) )
				{
					errcode = ErrorCode.eMoveFileFailed;
					callbackErr = task.pds.callback;
					mapTasks.remove(task.digest);
				}
				else
				{
					task.storage = new FileDataSource(fnDownload);
					callbackCom = task.pds.callback;
					task.pds = null;
					task.pus = new PieceUploadStatus();
				}
			}
			else
				task.pds.state = PieceDownloadStatus.State.ePDSIdle;
		}
		if( callbackErr != null )
			callbackErr.onFailed(errcode);
		else if( callbackCom != null)
			callbackCom.onComplete();
		if( bOK )
			shareDigest(digest);
	}		
	
	public void createDataGroup(KDigest group, List<KDigest> members, boolean bLocal)
	{
		if( group == null || members == null )
			return;
		Digest digest = new Digest(group);
		synchronized( mapDataGroups )
		{
			if( mapDataGroups.containsKey(digest) )
				return;
			List<Digest> lst = new ArrayList<Digest>();
			for(KDigest kd : members)
			{
				lst.add(new Digest(kd));
			}
			mapDataGroups.put(digest, new DataGroup(lst, bLocal));
		}
		
		if( peer.getRPCManager().isTrackerOK() && ! bLocal )
		{
			peer.getRPCManager().createDataGroup(digest, members.size());
		}
	}
	
	public  List<Digest> queryDataGroupMembers(Digest group, SBean.Section section)
	{
		if( group == null || section == null )
			return null;
		DataGroup dg = null;
		synchronized( mapDataGroups )
		{
			dg = mapDataGroups.get(group);
			if( dg == null )
				return null;
			if( section.offset < 0 || section.count < 0 || section.offset + section.count > dg.digests.size() )
				return null;
			return dg.digests.subList(section.offset, section.offset + section.count);
		}
	}
	
	public synchronized boolean querySource(Digest digest)
	{
		Task task = mapTasks.get(digest);
		return task != null && task.pus != null;
	}
	
	public boolean queryGroup(Digest group)
	{
		synchronized( mapDataGroups )
		{
			return mapDataGroups.containsKey(group);
		}
	}
		
	public ErrorCode addShareDataByCopyTask(byte[] data, int offset, int len, List<KDigest> digest)
	{
		if( data == null || offset < 0 || len <= 0 || offset + len > data.length )
			return ErrorCode.eInval;
		
		byte[] datacopied = Arrays.copyOfRange(data, offset, offset + len);
		List<ket.pack.Digest> lst = null;
		if( digest != null && ! digest.isEmpty() )
		{
			lst = new ArrayList<ket.pack.Digest>();
			for(KDigest kd : digest)
				lst.add(new Digest(kd));
		}
		else
		{
			lst = dtool.digest(datacopied, 0, len, DigestTool.DEFAULT_PIECESIZE);			
			if( digest != null )
			{
				digest.clear();
				for(ket.pack.Digest dd : lst)
					digest.add(dd.getKDigest());
			}
		}
		
		List<Digest> digests = new ArrayList<Digest>();		
		synchronized( this )
		{
			int start = 0;
			for(Digest d : lst)
			{
				if( mapTasks.containsKey(d) )
				{
					start += d.datalen;
					continue;
				}
				Task task = new Task();
				task.digest = d;
				task.storage = new WrappedMemoryDataStorage(datacopied, start, d.datalen);
				task.pus = new PieceUploadStatus();
				mapTasks.put(d, task);
				digests.add(d);
				start += d.datalen;
			}
		}
		shareDigests(digests);
		return ErrorCode.eOK;
	}
	
	private void shareDigest(Digest d)
	{
		if( peer.getRPCManager().isTrackerOK() )
		{
			sut.addDigest(d);
		}
		List<Digest> digests = new ArrayList<Digest>();
		digests.add(d);
		peer.getRPCManager().localShareDigests(digests);
	}
	
	private void shareDigests(List<Digest> digests)
	{
		if( peer.getRPCManager().isTrackerOK() )
		{
			for(Digest d : digests)
				sut.addDigest(d);
		}
		peer.getRPCManager().localShareDigests(digests);
	}
	
	public ErrorCode addShareFileTask(String fnShare, List<KDigest> digest)
	{
		if( fnShare == null )
			return ErrorCode.eInval;
		
		if( ! new FileDataSource(fnShare).isOK() )
			return ErrorCode.eResourceNotFound;
		
		List<ket.pack.Digest> lst = null;
		if( digest != null && ! digest.isEmpty() )
		{
			lst = new ArrayList<ket.pack.Digest>();
			for(KDigest kd : digest)
				lst.add(new Digest(kd));
		}
		else
		{
			lst = dtool.digest(fnShare, DigestTool.DEFAULT_PIECESIZE);			
			if( digest != null )
			{
				digest.clear();
				for(ket.pack.Digest dd : lst)
					digest.add(dd.getKDigest());
			}
		}
		
		List<Digest> digests = new ArrayList<Digest>();
		synchronized( this )
		{
			int start = 0;
			for(Digest d : lst)
			{
				if( mapTasks.containsKey(d) )
				{
					start += d.datalen;
					continue;
				}
				Task task = new Task();
				task.digest = d;
				task.storage = new FileDataSource(fnShare, start, d.datalen);
				digests.add(d);
				task.pus = new PieceUploadStatus();
				mapTasks.put(d, task);
				start += d.datalen;
			}
		}
		shareDigests(digests);
		return ErrorCode.eOK;
	}
	
	public ErrorCode addDownloadTask(KDownloadTask dtask, IDownloadCallback callback)
	{
		ErrorCode errcode = ErrorCode.eOK;
		Digest digest = new Digest(dtask.digest);
		String fnTmp = dirDownloadTmp + File.separator + StringConverter.getHexStringFromBytes(digest.getBytes());
		boolean bQuerySource = false;
		Digest group = null;
		synchronized( this )
		{
			if( mapTasks.containsKey(digest) )
				errcode = ErrorCode.eDigestAlreadyExist;
			else
			{
				if( dtask.baFragmentsDownload != null ) // resume
				{
					FileMappingDataStorage storage = new FileMappingDataStorage(fnTmp, digest.datalen, false);
					if( ! storage.isOK() )
						errcode = ErrorCode.eOpenFileFailed;
					else
					{
						Task task = new Task();
						task.digest = digest;
						task.pds = new PieceDownloadStatus();
						task.pds.callback = callback;
						task.storage = storage;
						if( dtask.group != null )
							task.pds.group = new Digest(dtask.group);
						task.pds.baFragments = dtask.baFragmentsDownload;
						if( task.pds.baFragments != null )
						{
							if( !task.pds.baFragments.contains(false) )
							{
								task.pds.baFragments.clearBit(0);
								task.pds.fragIndex = 0;
							}
							else
							{
								for(task.pds.fragIndex=0; task.pds.fragIndex < task.pds.baFragments.size(); ++task.pds.fragIndex)
								{
									if( task.pds.baFragments.isClear(task.pds.fragIndex) )
									{
										break;
									}
								}
							}
							//
							int total = task.digest.datalen;
							int nlast = total % dtask.fragsize;
							task.pds.fragBuffer = ByteBuffer.allocate((nlast == 0 || task.pds.fragIndex != task.pds.baFragments.size() - 1) ? dtask.fragsize : nlast);
						}
						mapTasks.put(digest, task);						
						if( peer.getRPCManager().isTrackerOK() && task.pds.coolerSourceQuery.tryReset() )
						{
							bQuerySource = true;
							group = task.pds.group;
						}
					}
				}
				else // new
				{					
					Task task = new Task();
					task.digest = digest;
					task.pds = new PieceDownloadStatus();
					task.pds.callback = callback;
					if( dtask.group != null )
						task.pds.group = new Digest(dtask.group);
					FileMappingDataStorage storage = new FileMappingDataStorage(fnTmp, digest.datalen, true);
					if( ! storage.isOK() )
						errcode = ErrorCode.eCreateFileFailed;
					else
					{
						task.storage = storage;												
						mapTasks.put(digest, task);
						//
						if( task.pds.coolerSourceQuery.tryReset() )
						{
							bQuerySource = true;
							group = task.pds.group;
						}
						//
					}
					
				}
			}
		}
		if( bQuerySource )
			peer.getRPCManager().querySource(digest, group);
		
		if( callback != null )
		{
			if( errcode != ErrorCode.eOK )
				callback.onFailed(errcode);
		}
		return errcode;
	}	
	
	public ErrorCode removeTask(KDigest kd) 
	{
		Digest digest = new Digest(kd);
		Task task = null;
		boolean bSendSDU = false;
		boolean bDeleteFile = false;
		IDownloadCallback callback = null;
		synchronized( this )
		{
			task = mapTasks.get(digest);
			if( task == null )
				return ErrorCode.eDigestNotFound;
			if( task.pus != null )
				bSendSDU = true;
			if( task.pds != null )
			{
				bDeleteFile = true;
				callback = task.pds.callback;
			}
			mapTasks.remove(digest);
		}
		if( bSendSDU )
			peer.getRPCManager().shareUpdate(digest, false);
		if( bDeleteFile )
		{
			FileMappingDataStorage storage = (FileMappingDataStorage)task.storage;
			try
			{
				new File(storage.getFileName()).delete();
			}
			catch(SecurityException ex)
			{				
			}
		}
		if( callback != null )
			callback.onFailed(ErrorCode.eActive);
		return ErrorCode.eOK;
	}
	
	public void start()
	{
		try
		{
			mt.future = peer.getExecutor().scheduleAtFixedRate(mt, 100, 200, TimeUnit.MILLISECONDS);
			sut.future = peer.getExecutor().scheduleAtFixedRate(sut, 1, 1, TimeUnit.SECONDS);
			
		}
		catch(RejectedExecutionException ex)
		{			
		}
	}
	
	public synchronized List<Digest> getAllSharingDigests()
	{
		List<Digest> digests = new ArrayList<Digest>();
		for(Task task : mapTasks.values() )
		{
			if( task.pus != null )
			{
				digests.add(task.digest);
			}
		}
		return digests;
	}
	
	public void onTrackerConnect()
	{
		List<Digest> digests = new ArrayList<Digest>();
		List<CreateDataGroupTask> cdgts = new ArrayList<CreateDataGroupTask>();
		synchronized( this )
		{
			for(Task task : mapTasks.values() )
			{
				if( task.pus != null )
				{
					digests.add(task.digest);
				}
			}
		}
		
		synchronized( mapDataGroups )
		{
			for(Map.Entry<Digest, DataGroup> e : mapDataGroups.entrySet())
			{
				DataGroup dg = e.getValue();
				if( dg.bLocal )
					continue;
				cdgts.add(new CreateDataGroupTask(e.getKey(), dg.digests.size()));
			}
		}
		
		if( peer.getRPCManager().isTrackerOK() )
		{
			for(Digest d : digests)
				sut.addDigest(d);
			for(CreateDataGroupTask cdgt : cdgts )
				peer.getRPCManager().createDataGroup(cdgt.group, cdgt.membercount);
		}
	}
	
	public void onExecutorShutdown()
	{
		if( mt.future != null )
			mt.future.cancel(false);
		if( sut.future != null )
			sut.future.cancel(false);
	}
	
	public void destroy()
	{
		for(Map.Entry<Digest, Task> e : mapTasks.entrySet())
		{
			Task task = e.getValue();
			if( task.pds != null && task.pds.callback != null )
			{
				KDownloadTask dtask = new KDownloadTask();
				dtask.digest = task.digest.getKDigest();
				dtask.baFragmentsDownload = task.pds.baFragments;
				dtask.fragsize = Constant.FRAGMENT_SIZE;
				task.pds.callback.onSaveDownloadTask(dtask);
				task.pds.callback.onFailed(ErrorCode.eShutdown);
			}
		}
	}
	
	private Peer peer;
	private MainTask mt = new MainTask();
	private ShareUpdateTask sut = new ShareUpdateTask();
	private DigestTool dtool = new DigestTool();
	private Map<Digest, Task> mapTasks = new HashMap<Digest, Task>();
	private Map<Digest, DataGroup> mapDataGroups = new HashMap<Digest, DataGroup>();
	private final String dirDownload = "./kdist";
	private final String dirDownloadTmp = "./kdist/tmp";
}
