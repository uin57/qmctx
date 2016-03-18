
package ket.kiox;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

import ket.kio.ErrorCode;
import ket.kio.NetAddress;
import ket.kio.NetManager;

public abstract class HTTPGetClient extends NetManager.TCPClient
{
	public HTTPGetClient(NetManager manager)
	{
		manager.super();
	}
	
	public void request(String path)
	{
		this.path = path;
		errcode = ErrorCode.eFailed;
		bOK = false;
		buffer = null;
		open();
	}
	public abstract void onResponse(ErrorCode errcode, ByteBuffer buffer);

	@Override
	public void onOpen()
	{
		StringBuilder sbReq = new StringBuilder();
		sbReq.append("GET " + path + " HTTP/1.1\r\n");		
		sbReq.append("Host: " + getServerAddr().host + "\r\n");
		sbReq.append("ACCEPT-LANGUAGE: en;en-US\r\n\r\n");
		ByteBuffer bufferSend = null;
		try
		{
			bufferSend = ByteBuffer.wrap(sbReq.toString().getBytes("ISO-8859-1"));
		}
		catch(UnsupportedEncodingException ex)
		{
			close();
			return;
		}
		sendData(bufferSend);
	}
	
	@Override
	public void onOpenFailed(ErrorCode errcode)
	{
		onResponse(errcode, null);
	}

	@Override
	public void onClose(ErrorCode errcode)
	{
		onResponse(this.errcode, buffer);
	}
	
	private String getLine(byte[] data, int offset, int len, int[] size)
	{
		int firstNewLine = -1;
		for(int i = 0; i < len; ++i)
		{
			if( data[offset+i] == 0x0a ) // find first \n
			{
				firstNewLine = i;
				break;
			}
		}
		if( firstNewLine == -1 )
			return null;
		size[0] = firstNewLine + 1;
		try
		{
			return new String(data, offset, size[0], "ISO-8859-1").trim();
		}
		catch(UnsupportedEncodingException ex)
		{			
		}
		return null;
	}

	@Override
	public int onDataRecv(byte[] data, int offset, int len)
	{
		int eat = 0;
		int size[] = new int[1];
		if( ! bOK )
		{
			String firstLine = getLine(data, offset, len, size);
			if( firstLine == null )
				return 0;
			if( firstLine.equals("HTTP/1.1 200 OK") )
			{
				bOK = true;
				eat = size[0];
				offset += eat;
				len -= eat;
			}
			else
			{
				close();
				return len;
			}
		}
		if( len <= 0 )
			return eat;

		if( buffer == null )
		{
			int sizeRes = -1;
			int start = offset;
			int end = offset + len;
			while( true )
			{
				String line = getLine(data, start, end - start, size);
				if( line == null )
					return eat;
				start += size[0];
				if( line.isEmpty() ) // last line
				{
					if( sizeRes < 0 )
					{
						close();
						return eat;
					}
					else
					{
						eat += ( start - offset);
						len -= ( start - offset);
						offset = start;
						buffer = ByteBuffer.allocate(sizeRes);
						break;
					}
				}
				else
				{
					if( line.indexOf("Content-Length") == 0 
							|| line.indexOf("CONTENT-LENGTH") == 0 )
					{
						int nnlen = line.indexOf(":");
						if( nnlen != -1 )
						{
							try
							{
								sizeRes = Integer.parseInt(line.substring(nnlen+1).trim());
							}
							catch(Exception ex)
							{			
								sizeRes = -1;
							}
						}
					}
				}
			}
		}
		if( len <= 0 )
			return eat;
		if( len > buffer.remaining() )
		{
			close();
			return eat;
		}
		buffer.put(data, offset, len);
		if( buffer.hasRemaining() )
			return eat + len;
		buffer.rewind();
		errcode = ErrorCode.eOK;
		close();
		return eat + len;
	}
	
	protected String path;
	private boolean bOK = false;
	private ByteBuffer buffer;
	private ErrorCode errcode;

	public static void main(String[] args)
	{
		NetManager managerNet = new NetManager();
		managerNet.start();
		final CountDownLatch cdl = new CountDownLatch(1);
		HTTPGetClient client = new HTTPGetClient(managerNet)
		{
			@Override
			public void onResponse(ErrorCode errcode, ByteBuffer buffer)
			{	
				System.out.println("on response " + errcode + ", buffer is " + buffer);
				cdl.countDown();
			}

		};		
		NetAddress addr = new NetAddress("172.16.10.57", 80);
		//NetAddress addr = new NetAddress("192.168.44.131", 80);
		if( args.length > 0 )
			addr.host = args[0];
		if( args.length > 1 )
		{
			try
			{
				addr.port = Integer.parseInt(args[1]);
			}
			catch(Exception ex)
			{				
			}
		}
		client.setServerAddr(addr);
		if( args.length > 2 )
			client.request(args[2]);
		else
			client.request("/");
		try { cdl.await(); } catch(Exception ex) { ex.printStackTrace(); }
		managerNet.destroy();
	}
}
