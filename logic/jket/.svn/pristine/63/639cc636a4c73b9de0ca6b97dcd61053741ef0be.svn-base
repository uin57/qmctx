
package ket.xml;

import java.io.File;

public class Factory
{
	public static Reader newReader(File file)
	{
		return new DOMReader(file);
	}
	
	public static Reader newReader(String fileName)
	{
		return new DOMReader(new File(fileName));
	}
}
