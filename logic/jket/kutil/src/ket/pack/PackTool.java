
package ket.pack;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;

import ket.pack.DigestTool;
import ket.pack.IndexData;
import ket.util.FileSys;
import ket.util.Stream;
import ket.util.ArgsMap;
import ket.util.StringConverter;
import ket.util.ICompressor;
import ket.util.ZipCompressor;
import ket.pack.KDigest; 

public class PackTool
{
	
	public static int DIST_PIECESIZE_DEFAULT = 4 * 1024 * 1024;
	
	public static int DIST_PIECESIZE_UNCOMPRESS_MAX = 8 * 1024 * 1024;
	public static long DIST_PIECESIZE_UNCOMPRESS_MIN = 512;
	public static int DIST_PIECESIZE_COMPRESS_MAX = 4 * 1024 * 1024;
	public static int DIST_PIECESIZE_COMPRESS_DEFAULT = 3 * 1024 * 1024;
	public static float DIST_COMPRESS_RATE_MIN = 0.9f;
	
	public static List<KDigest> digest(String fn)
	{
		List<KDigest> lst = new ArrayList<KDigest>();
		if( ! tool.digest(fn, DIST_PIECESIZE_DEFAULT, lst) )
			return null;
		return lst;
	}
	
	public static void main(String[] args)
	{
		new PackTool().run(new ArgsMap(args));
	}
		
	public static String normalizePathname(String p)
	{
		if( p == null )
			return null;
		String[] v = p.split("[\\\\/]");
		String r = "";
		List<String> lst = new ArrayList<String>();
		for(String s : v)
		{
			s = s.trim();
			if( s.equals("") || s.equals(".") )
				continue;
			else if( s.equals("..") )
			{
				if( ! lst.isEmpty() )
					lst.remove(lst.size() - 1);
			}
			else
				lst.add(s);
		}		
		for(String s : lst)
		{
			r += ( "/" + s);
		}
		return r;
	}
	
	public void run(ArgsMap am)
	{
		String path = am.get("-p");
		if( path == null )
		{
			System.out.println(usage);
			return;
		}
		try
		{
			File fn = new File(path).getCanonicalFile();
			if( ! fn.exists() )
			{
				System.out.println(usage);
				return;
			}
			path = fn.getAbsolutePath();
			if( fn.isDirectory() )
			{
				System.out.println("path is   [" + path + "].");
				if( am.containsKey("dist") )
				{
					new DistTask(fn, am).run();
				}
				else
				{
					FileSys.traverse(path,
							new FileSys.ITraversalVisitor()
							{

								@Override
								public boolean onEnterDirectory(String pathname, String name, int depth) throws BreakException
								{
									if( depth == 0 )
										return true;
									String holder = "";
									for(int i=0; i< depth; ++i)
										holder += "    ";
									System.out.print(holder);
									System.out.println("[" + name + "]");
									return true;
								}

								@Override
								public void onLeaveDirectory(String pathname, String name, int depth) throws BreakException
								{	
								}

								@Override
								public void visitFile(String pathname, String name, int depth) throws BreakException
								{
									String holder = "";
									for(int i=0; i< depth; ++i)
										holder += "    ";

									System.out.print(holder);
									System.out.print("(" + name + ")  :  ");
									List<KDigest> digests = digest(pathname);
									if( digests == null )
										System.out.println("digest failed.");
									else if( ! digests.isEmpty() )
									{
										System.out.println("[" + StringConverter.getHexStringFromBytes(digests.get(0).digest) + "]");
										for(int i = 1; i < digests.size(); ++i)
										{
											System.out.print(holder);
											System.out.print("(" + name + ")  :  ");
											System.out.println("[" + StringConverter.getHexStringFromBytes(digests.get(i).digest) + "]");
										}
									}
								}
						
							},
							am.containsKey("r") ? -1 : 1);
				}
			}
			else if( fn.isFile() )
			{
				System.out.println("file is   (" + path + ").");
				List<KDigest> digests = digest(path);
				if( digests == null )
					System.out.println("digest failed.");
				else if( ! digests.isEmpty() )
				{
					System.out.println("digest is [" + StringConverter.getHexStringFromBytes(digests.get(0).digest) + "]");
					for(int i = 1; i < digests.size(); ++i)
					{
						System.out.println("      [" + StringConverter.getHexStringFromBytes(digests.get(i).digest) + "]");
					}
				}
				else
					System.out.println("digest failed, empty digests.");
			}
			return;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		System.out.println(usage);
	}
	
	private class DistTask
	{
		public DistTask(File fDir, ArgsMap am)
		{
			this.am = am;
			this.fRootDir = fDir;
		}
		
		public void run() throws Exception
		{	
			datasize = 0;
			disttime = java.util.Calendar.getInstance().getTimeInMillis();
			boolean bIgnoreCase = am.containsKey("ic");
			boolean bPack = am.containsKey("pack");
			boolean bWeb = am.containsKey("web");
			String stamp = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
			String dirdist = stamp;
			if( ! bPack )
			{
				outdir = am.get("-outdir", ".");
				File odir = new File(outdir).getCanonicalFile();
				if( ! odir.exists() || ! odir.isDirectory() )
				{
					System.out.println("bad outdir.");
					return;
				}
				dirdist = odir.getAbsolutePath() + File.separator + stamp;
			}
			fDestDir = new File(dirdist);
			if( !fDestDir.mkdir() )
			{
				System.out.println("mkdir failed.");
				return;
			}
			//
			if( fDestDir.getAbsolutePath().startsWith(fRootDir.getAbsolutePath()) ||
					fRootDir.getParentFile().getAbsolutePath().equals(fDestDir.getAbsolutePath()) )
			{
				System.out.println("bad outdir.");
				return;
			}
			//
			System.out.println("outdir is " + fDestDir.getAbsolutePath());
			dd = new IndexData();
			dd.setIgnoreCase(bIgnoreCase);
			digestmap = new TreeMap<String, IndexData.PieceInfo>();
			tmpmap = new HashMap<String, List<String>>();
			distFile(fRootDir.getAbsolutePath());
			File fDistData = new File(fDestDir.getAbsolutePath() + File.separator + "dist.data");
			Map<String, Integer> filemap = new HashMap<String, Integer>();
			int i = 0;
			List<IndexData.PieceInfo> piecelst = new ArrayList<IndexData.PieceInfo>();
			for(Map.Entry<String, IndexData.PieceInfo> e : digestmap.entrySet())
			{
				piecelst.add(e.getValue());
				filemap.put(e.getKey(), i);
				++i;
			}
			dd.setPieces(piecelst);
			for(Map.Entry<String, List<String>> e : tmpmap.entrySet())
			{
				List<Integer> lst = new ArrayList<Integer>();
				for(String s : e.getValue())
				{
					lst.add(filemap.get(s));
				}
				dd.getMap().put(e.getKey(), lst);
			}
			Stream.storeZipObjLE(dd, fDistData);
			long timeused = java.util.Calendar.getInstance().getTimeInMillis() - disttime;
			KDigest kdistdigest = digest(fDistData.getAbsolutePath()).get(0);
			String distdigest = StringConverter.getHexStringFromBytes(kdistdigest.digest);
			String fnNewDistData = fDestDir.getAbsolutePath() + File.separator + distdigest;
			FileSys.moveFile(fDistData.getAbsolutePath(), fnNewDistData);
			System.out.println("\ndist completed, " + dd.getMap().size() + " file , " + datasize + " bytes copied, time used " +
					timeused + " ms, " + datasize/timeused+ "K/s.");
			System.out.println("stamp is [" + stamp +
					"], distdata digest is [" +  distdigest + "]");
			if( ! bPack )
			{
				if( bWeb )
				{
					String dirname = outdir + File.separator + "webshare";
					String vname = outdir + File.separator + "version.txt";
					new File(dirname).delete();
					fDestDir.renameTo(new File(dirname));
					Writer fos = new FileWriter(new File(vname));
					fos.write(distdigest);
					fos.close();
				}
				else
					fDestDir.renameTo(new File(fDestDir.getAbsolutePath() + "_" + distdigest));
			}
			else
			{
				String abspath = am.get("-packabspath", ".");
				String packFileName = abspath + File.separator + fRootDir.getName() + ".kpk";
				File fPack = new File(packFileName);
				fPack.delete();
				fPack.createNewFile();
				FileOutputStream fos = new FileOutputStream(fPack);
				Stream.OStreamLE os = new Stream.OStreamLE(fos);
				// TODO
				int sd0 = Stream.AOStream.getSizeTPackSize(kdistdigest.digest.length) - 4;
				for(IndexData.PieceInfo pi : piecelst)
				{
					sd0 += (Stream.AOStream.getSizeTPackSize(pi.digest.digest.length) - 4);
				}
				//
				int nPieceEntry = piecelst.size() + 1;
				int nHeaderSize = 8 + (4 + 4 + Digest.DIGEST_LENGTH + 4) * nPieceEntry + sd0;
				
				os.pushInteger(nHeaderSize);
				os.pushInteger(nPieceEntry);
				
				int offset = nHeaderSize;
				// indexdata
				os.pushInteger(offset);
				os.pushSizeT(kdistdigest.digest.length);
				os.pushBytes(kdistdigest.digest);
				
				offset += kdistdigest.datalen;
				
				for(IndexData.PieceInfo pi : piecelst)
				{
					os.pushInteger(offset);
					os.pushSizeT(pi.digest.digest.length);
					os.pushBytes(pi.digest.digest);
					offset += pi.digest.datalen;
				}
				byte[] buffer = new byte[DIST_PIECESIZE_DEFAULT];
				{
					File f = new File(fnNewDistData);
					FileInputStream fis = new FileInputStream(f);
					Stream.readAll(fis, buffer, 0, (int)f.length());
					os.pushBytes(buffer, 0, (int)f.length());
					fis.close();
				}
				for(IndexData.PieceInfo pi : piecelst)
				{
					File f = new File(fDestDir + File.separator + StringConverter.getHexStringFromBytes(pi.digest.digest));
					FileInputStream fis = new FileInputStream(f);
					Stream.readAll(fis, buffer, 0, (int)f.length());
					os.pushBytes(buffer, 0, (int)f.length());
					fis.close();
				}
				fos.close();
				FileSys.deleteFile(fDestDir.getAbsolutePath());
				System.out.println("\npack to " + fPack.getAbsolutePath() + " completed");
			}
		}
		
		private boolean doCopy(File fSrc, long filelen, List<String> sdigests) throws IOException
		{			
			FileInputStream fis = new FileInputStream(fSrc);
			byte[] rbuf = new byte[DIST_PIECESIZE_DEFAULT];
			while( filelen > 0 )
			{
				int dlen = ( filelen >= DIST_PIECESIZE_DEFAULT ) ? DIST_PIECESIZE_DEFAULT : (int)filelen;
				Stream.readAll(fis, rbuf, 0, dlen);
				KDigest kd = tool.digest(ByteBuffer.wrap(rbuf, 0, dlen));
				String s = StringConverter.getHexStringFromBytes(kd.digest);
				sdigests.add(s);
				if( ! digestmap.containsKey(s) )
				{
					//
					File fDst = new File(fDestDir.getAbsolutePath() + File.separator + s);
					if( fDst.exists() )
						fDst.delete();
					if( ! fDst.createNewFile() )
					{
						fis.close();
						return false;
					}
					//
					FileOutputStream fos = new FileOutputStream(fDst);
					System.out.println("\twrite file [" + fDst.getName() + "]");
					fos.write(rbuf, 0, dlen);
					fos.close();
					//
					datasize += kd.datalen;
					digestmap.put(s, new IndexData.PieceInfo(kd));
				}
				filelen -= dlen;
			}
			fis.close();
			return true;
		}
		
		private class FileCompressed
		{
			FileCompressed(int c, String s, File f)
			{
				clen = c;
				fn = s;
				file = f;
			}
			int clen;
			String fn;
			File file;
		}
		
		private boolean distFile(String src) throws Exception
		{
			File fSrc = new File(src).getCanonicalFile();
			if( ! fSrc.exists() )
				return false;
			if( fSrc.getName().startsWith(".") ) // todo
				return true;

			if( fSrc.isFile() )
			{				
				String fnrel = normalizePathname(fSrc.getAbsolutePath().substring(fRootDir.getAbsolutePath().length()));
				if( dd.isIgnoreCase() )
					fnrel = fnrel.toLowerCase();
				if( tmpmap.containsKey(fnrel) )
					return false;
				///////////////////////////////////////////////////////////////////////
				System.out.println("dist file: [" + fnrel + "]");
				List<String> sdigests = new ArrayList<String>();
				long filelen = fSrc.length();					
				if( filelen != 0 )
				{
					if( filelen <= DIST_PIECESIZE_UNCOMPRESS_MIN || ! am.containsKey("c") ) // todo
					{
						///// 1. copy
						if( ! doCopy(fSrc, filelen, sdigests) )
							return false;
					}
					else
					{
						List<FileCompressed> lstFC = new ArrayList<FileCompressed>();
						FileInputStream fis = new FileInputStream(fSrc);
						byte[] rbuf = new byte[DIST_PIECESIZE_UNCOMPRESS_MAX];
						int bufremain = 0;
						boolean bCompressed = false;
						float fr = 0.0f;
						long oldfilelen = filelen;
						while( filelen > 0 )
						{
							int ulen = 0;
							if( bCompressed )
							{
								ulen = (int)(DIST_PIECESIZE_COMPRESS_DEFAULT/fr);
								if( ulen > DIST_PIECESIZE_UNCOMPRESS_MAX )
									ulen = DIST_PIECESIZE_UNCOMPRESS_MAX;
							}
							else
								ulen = DIST_PIECESIZE_UNCOMPRESS_MAX;
							int readlen = 0;
							if( ulen > bufremain )
								readlen = ulen - bufremain;
							if( readlen > filelen )
								readlen = (int)filelen;
							ulen = bufremain + readlen;
							Stream.readAll(fis, rbuf, bufremain, readlen);
							ByteBuffer bbUCom = ByteBuffer.wrap(rbuf, 0, ulen);
							int oldulen = ulen;
							ByteBuffer bbCom = compressor.compress(bbUCom);
							bCompressed = true;
							fr = bbCom.remaining()/(float)ulen;
							if( fr > DIST_COMPRESS_RATE_MIN )
							{
								//System.out.println("fr is " + fr);
								sdigests.clear();
								for(FileCompressed fc : lstFC)
								{
									datasize -= fc.clen;
									digestmap.remove(fc.fn);
									fc.file.delete();
								}
								doCopy(fSrc, oldfilelen, sdigests);
								break;
							}
							int clen = bbCom.remaining();
							while( clen > DIST_PIECESIZE_COMPRESS_MAX )
							{
								ulen = (int)(DIST_PIECESIZE_COMPRESS_DEFAULT/fr);
								bbUCom.limit(ulen);
								bbCom = compressor.compress(bbUCom);
								clen = bbCom.remaining();
								fr = clen/(float)ulen;
							}
							if( oldulen > ulen )
							{
								bufremain = oldulen - ulen;
								System.arraycopy(rbuf, ulen, rbuf, 0, bufremain);
							}
							else
								bufremain = 0;
							{
								KDigest kd = tool.digest(bbCom);
								String s = StringConverter.getHexStringFromBytes(kd.digest);
								sdigests.add(s);
								if( ! digestmap.containsKey(s) )
								{
									//
									File fDst = new File(fDestDir.getAbsolutePath() + File.separator + s);
									if( fDst.exists() )
										fDst.delete();
									if( ! fDst.createNewFile() )
									{
										fis.close();
										return false;
									}
									//
									FileOutputStream fos = new FileOutputStream(fDst);
									System.out.println("\twrite file [" + fDst.getName() + "]");
									fos.write(bbCom.array(), bbCom.arrayOffset() + bbCom.position(), clen);
									fos.close();
									lstFC.add(new FileCompressed(clen, fDst.getName(), fDst));
									//
									datasize += clen;
									digestmap.put(s, new IndexData.PieceInfo(IndexData.PieceInfo.PCRY_ZIP, ulen, kd));
								}
							}
							filelen -= readlen;
						}
						fis.close();
					}
					/////
				}
				//if( fnrel.equals("/scene/map/Ò×h-fantasyÐü¸¡¿Õµº/sceneobjs/iv/(+000,+000)lightiv_1.iv") )
				//{
				//	int j = 0;
				//	++j;
				//}
				tmpmap.put(fnrel, sdigests);
				///////////////////////////////////////////////////////////////////////
			}
			else if( fSrc.isDirectory() )
			{
				String[] lst = fSrc.list();
				for(String fn : lst)
				{
					distFile(fSrc.getAbsolutePath() + File.separator + fn);
				}
			}
			else
				return false;
			return true;
		}	
		
		private ArgsMap am;
		private String outdir;
		private File fRootDir;
		private File fDestDir;
		private IndexData dd;
		private Map<String, IndexData.PieceInfo> digestmap;
		private Map<String, List<String>> tmpmap;
		private long datasize;
		private long disttime;
		private ICompressor compressor = new ZipCompressor();
	}	
	
	private static DigestTool tool = new DigestTool();
	private String usage = "usage: DistTool --p=<digestpath> [-r] [-c] [-dist [-ic] [--outdir=<outdir>]]";
}
