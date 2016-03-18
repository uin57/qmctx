
package ket.kio;

import ket.util.Stream;

public interface PacketDecoder<Packet>
{
	class Exception extends java.lang.Exception
	{
		private static final long serialVersionUID = 1L;
	}
	
	Packet decode(Stream.AIStream is) throws Exception;
}
