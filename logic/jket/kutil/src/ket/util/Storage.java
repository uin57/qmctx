
package ket.util;

import java.nio.ByteBuffer;

public interface Storage
{		
	public boolean isOK();
	public long getLength();
	public ByteBuffer read(long offset, int len, byte[] dstbuf);
	public boolean write(long offset, int len, byte[] srcbuf);
}

