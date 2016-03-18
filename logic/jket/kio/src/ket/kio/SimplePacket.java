
package ket.kio;

import ket.util.Stream;

public abstract class SimplePacket implements Stream.IStreamable
{
	public abstract int getType();
}
