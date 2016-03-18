
package ket.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

class DOMReader implements Reader
{
	public DOMReader(File file)
	{
		this.file = file;
	}
	
	@Override
	public Node getRoot() throws ReaderException
	{
		try
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			return new DOMNode(doc.getDocumentElement());
		}
		catch(ParserConfigurationException ex)
		{
			throw new ReaderException("target file is " + file.getAbsolutePath() + ".", ex);
		}
		catch(IOException ex)
		{
			throw new ReaderException("target file is " + file.getAbsolutePath() + ".", ex);
		}
		catch(SAXException ex)
		{
			throw new ReaderException("target file is " + file.getAbsolutePath() + ".", ex);
		}
	}
	
	private File file;
}
