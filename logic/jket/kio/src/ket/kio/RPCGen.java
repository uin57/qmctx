
package ket.kio;

import java.util.AbstractMap;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.Reader;

import ket.xml.ReaderException;
import ket.xml.Node;

import ket.util.ArgsMap;
import ket.util.FileTool;

final class UniqueSet<E> extends AbstractSet<E>
{
	public UniqueSet(final String name)
	{
		this.name = name;
		set = new HashSet<E>();
	}
	
	public void addUnique(E e) throws ReaderException
	{
		if( set.contains(e) )
			throw new ReaderException("dup " + name + " : " + e + ".");
		set.add(e);
	}
	
	@Override
	public Iterator<E> iterator()
	{
		return set.iterator();
	}

	@Override
	public int size()
	{
		return set.size();
	}
	
	private final String name;
	private final Set<E> set;
}

final class UniqueMap<K, V> extends AbstractMap<K, V>
{
	public UniqueMap(final String name)
	{
		this.name = name;
		map = new HashMap<K, V>();
	}
	
	public void putUnique(K key, V value) throws ReaderException
	{
		if( map.containsKey(key) )
			throw new ReaderException("dup " + name + " : " + key + ".");
		map.put(key, value);
	}
	
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		return map.entrySet();
	}		

	private final String name;
	private final Map<K, V> map;
}

class InnerEnum
{
	public String name;
	public List<String> constants = new ArrayList<String>();
}

class IntConst
{
	public String name;
	public String val;
}

class Field
{
	public String name;
	public String type;
	public boolean defaultalloc;
	public String uppername;
	public boolean bList = false;
	public String defaultval = null;
	public String comment = "";
}

class FieldsContainer
{
	public String name;
	public UniqueSet<String> ienames = new UniqueSet<String>("inner enum name");
	public List<InnerEnum> ienums = new ArrayList<InnerEnum>();
	public List<IntConst> intconsts = new ArrayList<IntConst>();
	public List<IntConst> int16consts = new ArrayList<IntConst>();
	public List<IntConst> int8consts = new ArrayList<IntConst>();
	public List<Field> fields = new ArrayList<Field>();
	public String comment = "";
}

class InnerType
{
	public InnerType(String cppname, String javaname, String javawrapname, boolean bObj)
	{
		this.cppname = cppname;
		this.javaname = javaname;
		this.javawrapname = javawrapname;
		this.bObj = bObj;
	}
	
	public String cppname;
	public String javaname;
	public String javawrapname;
	public boolean bObj;
}

abstract class Generator
{
	public static final String handlerBeginSig = "//// begin handlers.";
	public static final String handlerEndSig = "//// end handlers.";
	
	public abstract String getTypeName(InnerType innerType);
	public abstract String wrapListType(String typeName);	
	public abstract String getTypeName(FieldsContainer container, String fieldType);
	
	public String getTypeName(FieldsContainer container, Field field)
	{
		String typeName = getTypeName(container, field.type);
		if( field.bList )
			return wrapListType(typeName);
		return typeName;
	}
	
	public char[] getTabSeq(int nTab)
	{
		char[] a = new char[nTab];
		Arrays.fill(a, 0, nTab, '\t');
		return a;
	}
	
	public char[] getSpaceSeq(int nSpace)
	{
		char[] a = new char[nSpace];
		Arrays.fill(a, 0, nSpace, ' ');
		return a;
	}
	
	public abstract String getDefaultConstructor(FieldsContainer container, int nTab);
	public abstract String getCopyConstructor(FieldsContainer container, int nTab);
	public abstract String getIntConstDecl(FieldsContainer container, int nTab);
	public abstract String getInnerEnumDecl(FieldsContainer container, int nTab);
	public abstract String getEncodeFunc(FieldsContainer container, int nTab);
	public abstract String getDecodeFunc(FieldsContainer container, int nTab);
	public abstract String getFieldsDecl(FieldsContainer container, int nTab);
	
	public String getCommentString(String comment, int nTab)
	{
		StringBuilder sb = new StringBuilder();
		char[] t0 = getTabSeq(nTab);
		if( ! comment.isEmpty() )
		{
			String[] lines = comment.split("\\n");
			List<String> trimlines = new ArrayList<String>();
			for(String l : lines)
			{
				if( ! l.trim().isEmpty() )
					trimlines.add(l);
			}
			if( trimlines.size() > 1 )
			{
				sb.append(t0).append("/*").append(ls);				
				for(String l : trimlines)
					sb.append(t0).append(" * ").append(l).append(ls);
				sb.append(t0).append(" */").append(ls);
			}
			else
				sb.append(t0).append("// ").append(comment.trim()).append(ls);
		}
		return sb.toString();
	}
	public String getComment(FieldsContainer container, int nTab)
	{
		return getCommentString(container.comment, nTab);
	}
	
	public static String ls = System.getProperty("line.separator");
}

public class RPCGen
{
	private static final String FIELD_NAME_PREFIX_RESERVED = "_kio_";
	public RPCGen()
	{
		innertypes.put("bool", new InnerType("bool", "boolean", "Boolean", false));
		innertypes.put("int8", new InnerType("char", "byte", "Byte", false));
		innertypes.put("uint8", new InnerType("unsigned char", "byte", "Byte", false));
		innertypes.put("int16", new InnerType("short", "short", "Short", false));
		innertypes.put("int32", new InnerType("int", "int", "Integer", false));
		innertypes.put("int64", new InnerType("int64_t", "long", "Long", false));
		innertypes.put("uint32", new InnerType("uint32_t", "int", "Integer", false));
		innertypes.put("float", new InnerType("float", "float", "Float", false));
		innertypes.put("string", new InnerType("std::string", "String", "String", true));
		innertypes.put("wstring", new InnerType("std::wstring", "String", "String", true));
		innertypes.put("bytebuffer", new InnerType("KET::Util::ByteBuffer", "ByteBuffer", "ByteBuffer", true));
		
		peertypes.put("udppeer", Peer.Type.ePTUDPPpeer);
		peertypes.put("tcpserver", Peer.Type.ePTTCPServer);
		peertypes.put("tcpclient", Peer.Type.ePTTCPClient);
	}
	
	private class Module
	{
		private class JavaGenerator extends Generator
		{
			@Override
			public String getTypeName(FieldsContainer container, Field field)
			{
				String typeName = getTypeName(container, field.type);
				if( field.bList )
				{
					InnerType it = innertypes.get(field.type);
					if( it == null )
						return wrapListType(typeName);
					return wrapListType(it.javawrapname);
				}
				return typeName;
			}
			
			public String getInnerWrapName(FieldsContainer container, Field field)
			{
				String typeName = getTypeName(container, field.type);
				if( field.bList )
				{
					InnerType it = innertypes.get(field.type);
					if( it == null )
						return typeName;
					return it.javawrapname;
				}
				return typeName;
			}
			
			@Override
			public String getTypeName(InnerType innerType)
			{
				return innerType.javaname;
			}
			
			@Override
			public String wrapListType(String typeName)
			{
				return "List<" + typeName + ">";
			}
			
			@Override
			public String getTypeName(FieldsContainer container, String fieldType)
			{
				if( container.ienames.contains(fieldType) )
					return fieldType;
				InnerType it = innertypes.get(fieldType);
				if( it != null )
					return getTypeName(it);
				ExtBean eb = extbeanmap.get(fieldType);
				if( eb != null )
					return fieldType;
				if( container instanceof Bean )
				{
					return fieldType;
				}
				else if( container instanceof Packet || container instanceof Peer)
				{
					RefBean rb = refbeans.get(fieldType);
					if( rb != null )
						return fieldType;
					return javacfg.sbeanclassname + "." + fieldType;
				}
				return null;
			}
			
			@Override
			public String getDefaultConstructor(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				sb.append(getTabSeq(nTab)).append("public ").append(container.name).append("() { }").append(ls).append(ls);
				return sb.toString();
			}
			
			@Override
			public String getCopyConstructor(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab);
				char[] t1 = getTabSeq(nTab + 1);
				sb.append(t0).append("public ").append(container.name).append('(');
				for(int i = 0; i < container.fields.size(); ++i)
				{
					Field field = container.fields.get(i);
					sb.append(getTypeName(container, field) + " " + field.name);
					if( i != container.fields.size() - 1)
					{
						sb.append(", ");
						if( (i+1) % 4 == 0 )
							sb.append(ls).append(t0).append("       ").append(getSpaceSeq(container.name.length()+1));
					}
				}
				sb.append(')').append(ls).append(t0).append('{').append(ls);
				for(Field field : container.fields)
					sb.append(t1).append("this.").append(field.name).append(" = ").append(field.name).append(';').append(ls);
				sb.append(t0).append('}').append(ls).append(ls);
				return sb.toString();
			}
			
			public String getKSClone(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab);
				char[] t1 = getTabSeq(nTab + 1);
				sb.append(t0).append("public ").append(container.name).append(" ksClone(");
				sb.append(')').append(ls).append(t0).append('{').append(ls);
				sb.append(t1).append("return new ").append(container.name).append("(");
				for(int i = 0; i < container.fields.size(); ++i)
				{
					Field field = container.fields.get(i);
					sb.append(field.name);
					if( i != container.fields.size() - 1)
					{
						sb.append(", ");
						if( (i+1) % 4 == 0 )
							sb.append(ls).append(t1).append("           ").append(getSpaceSeq(container.name.length()+1));
					}
				}
				sb.append(");").append(ls);
				sb.append(t0).append('}').append(ls).append(ls);
				return sb.toString();
			}
			
			public String getKDClone(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab);
				char[] t1 = getTabSeq(nTab + 1);
				char[] t2 = getTabSeq(nTab + 2);
				sb.append(t0).append("public ").append(container.name).append(" kdClone(");
				sb.append(')').append(ls).append(t0).append('{').append(ls);
				sb.append(t1).append(container.name).append(" ").append(FIELD_NAME_PREFIX_RESERVED).append("clobj = ksClone();").append(ls);
				for(int i = 0; i < container.fields.size(); ++i)
				{
					Field field = container.fields.get(i);
					if( field.bList )
					{
						sb.append(t1).append(FIELD_NAME_PREFIX_RESERVED).append("clobj.").append(field.name);
						sb.append(" = new ArrayList<").append(getInnerWrapName(container, field)).append(">();").append(ls);
						sb.append(t1).append("for(").append(getInnerWrapName(container, field)).append(" ");
						sb.append(FIELD_NAME_PREFIX_RESERVED).append("iter : ").append(field.name).append(")").append(ls);
						sb.append(t1).append("{").append(ls);
						sb.append(t2).append(FIELD_NAME_PREFIX_RESERVED).append("clobj.").append(field.name).append(".add(");
						if( ! container.ienames.contains(field.type) && ! innertypes.containsKey(field.type) )
						{
							sb.append(FIELD_NAME_PREFIX_RESERVED).append("iter.kdClone()");
						}
						else
						{
							sb.append(FIELD_NAME_PREFIX_RESERVED).append("iter");
						}
						sb.append(");").append(ls).append(t1).append("}").append(ls);
					}
					else if( ! container.ienames.contains(field.type) && ! innertypes.containsKey(field.type) )
					{
						sb.append(t1).append(FIELD_NAME_PREFIX_RESERVED).append("clobj.").append(field.name).append(" = ");
						sb.append(field.name).append(".kdClone();").append(ls);
					}
				}
				sb.append(t1).append("return ").append(FIELD_NAME_PREFIX_RESERVED).append("clobj;").append(ls);
				sb.append(t0).append('}').append(ls).append(ls);
				return sb.toString();
			}
			
			@Override
			public String getIntConstDecl(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab);			
				for(IntConst ic : container.intconsts)
				{
					sb.append(t0).append("public static final int ").append(ic.name).append(" = ").append(ic.val).append(';').append(ls);
				}
				for(IntConst ic : container.int16consts)
				{
					sb.append(t0).append("public static final short ").append(ic.name).append(" = ").append(ic.val).append(';').append(ls);
				}
				for(IntConst ic : container.int8consts)
				{
					sb.append(t0).append("public static final byte ").append(ic.name).append(" = ").append(ic.val).append(';').append(ls);
				}
				if( ! container.intconsts.isEmpty() || ! container.int16consts.isEmpty() || ! container.int8consts.isEmpty() )
					sb.append(ls);
				return sb.toString();
			}
			
			@Override
			public String getInnerEnumDecl(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab);
				char[] t1 = getTabSeq(nTab + 1);
				for(InnerEnum ie : container.ienums)
				{
					sb.append(t0).append("public static enum ").append(ie.name).append(ls);
					sb.append(t0).append('{').append(ls);
					for(int i = 0; i < ie.constants.size(); ++i)
					{
						sb.append(t1).append(ie.constants.get(i));
						if( i < ie.constants.size() - 1 )
							sb.append(',');
						sb.append(ls);
					}
					sb.append(t0).append('}').append(ls);
				}
				return sb.toString();
			}
			
			@Override
			public String getEncodeFunc(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab);
				char[] t1 = getTabSeq(nTab + 1);
				sb.append(t0).append("@Override").append(ls);
				sb.append(t0).append("public void encode(Stream.AOStream os)").append(ls);
				sb.append(t0).append('{').append(ls);
				for(Field field : container.fields)
				{
					if( container.ienames.contains(field.type) )
					{
						sb.append(t1).append("os.pushEnum(").append(field.name).append(");").append(ls);
					}
					else if( innertypes.containsKey(field.type) )
					{
						InnerType it = innertypes.get(field.type);
						if( ! field.bList )
							sb.append(t1).append("os.push").append(it.javawrapname).append('(').append(field.name).append(");").append(ls);
						else
							sb.append(t1).append("os.push").append(it.javawrapname).append("List(").append(field.name).append(");").append(ls);
					}
					else
					{
						if( field.bList )
							sb.append(t1).append("os.pushList(").append(field.name).append(");").append(ls);
						else
							sb.append(t1).append("os.push(").append(field.name).append(");").append(ls);
					}
				}
				sb.append(t0).append('}').append(ls).append(ls);
				return sb.toString();
			}
			
			@Override
			public String getDecodeFunc(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab);
				char[] t1 = getTabSeq(nTab + 1);
				char[] t2 = getTabSeq(nTab + 2);
				sb.append(t0).append("@Override").append(ls);
				sb.append(t0).append("public void decode(Stream.AIStream is) throws Stream.EOFException, Stream.DecodeException").append(ls);
				sb.append(t0).append('{').append(ls);
				for(Field field : container.fields)
				{
					if( container.ienames.contains(field.type) )
					{
						sb.append(t1).append(field.name).append(" = is.popEnum(").append(field.type).append(".values());").append(ls);
					}
					else if( innertypes.containsKey(field.type) )
					{
						InnerType it = innertypes.get(field.type);
						if( ! field.bList )
							sb.append(t1).append(field.name).append(" = is.pop").append(it.javawrapname).append("();").append(ls);
						else
							sb.append(t1).append(field.name).append(" = is.pop").append(it.javawrapname).append("List();").append(ls);
					}
					else
					{
						if( ! field.defaultalloc && ! field.bList )
						{
							sb.append(t1).append("if( ").append(field.name).append(" == null )").append(ls);
							sb.append(t2).append(field.name).append(" = new ");
							if( field.bList )
								sb.append("Array");
							sb.append(getTypeName(container, field)).append("();").append(ls);
						}
						if( ! field.bList )
							sb.append(t1).append("is.pop(").append(field.name).append(");").append(ls);
						else
						{
							sb.append(t1).append(field.name).append(" = is.popList(").append(getTypeName(container, field.type)).append(
									".class);").append(ls);
						}
					}
				}
				sb.append(t0).append('}').append(ls).append(ls);
				return sb.toString();
			}
			
			@Override
			public String getFieldsDecl(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				for(Field field : container.fields)
				{
					if( ! field.comment.isEmpty() )
						sb.append(getCommentString(field.comment, nTab));
					sb.append(getTabSeq(nTab));
					if( container instanceof Bean )
						sb.append("public ");
					else
						sb.append("private ");
					String typeName = getTypeName(container, field); 
					sb.append(typeName);
					sb.append(' ');
					sb.append(field.name);
					InnerType it = innertypes.get(field.type);
					if( it != null && field.defaultval != null )
					{
						sb.append(" = ");
						sb.append(makeDefaultVal(field.type, field.defaultval));
					}
					else if( (it == null || it.bObj) && field.defaultalloc )
					{
						sb.append(" = new ");
						sb.append(typeName);
						sb.append("()");
					}
					sb.append(';').append(ls);
				}
				return sb.toString();
			}
		}
		
		private class CPPGenerator extends Generator
		{
			@Override
			public String getTypeName(InnerType innerType)
			{
				return innerType.cppname;
			}
			
			@Override
			public String wrapListType(String typeName)
			{
				return "std::vector<" + typeName + ">";
			}
			
			@Override
			public String getTypeName(FieldsContainer container, String fieldType)
			{
				if( container.ienames.contains(fieldType) )
					return fieldType;
				InnerType it = innertypes.get(fieldType);
				if( it != null )
					return getTypeName(it);
				ExtBean eb = extbeanmap.get(fieldType);
				if( eb != null )
					return eb.module.cppcfg.basenamespace + "::" + eb.module.cppcfg.sbeannamespace + "::" + eb.name;
				if( container instanceof Bean )
				{
					RefBean rb = refbeans.get(fieldType);
					if( rb != null )
						return ((rb.refpackage.cppnamespace == null) ? "" : (rb.refpackage.cppnamespace + "::")) + rb.name;
					return fieldType;
				}
				else if( container instanceof Packet || container instanceof Peer)
				{
					RefBean rb = refbeans.get(fieldType);
					if( rb != null )
						return ((rb.refpackage.cppnamespace == null) ? "" : (rb.refpackage.cppnamespace + "::")) + rb.name;
					return cppcfg.basenamespace + "::" + cppcfg.sbeannamespace + "::" + fieldType;
				}
				return null;
			}
			
			@Override
			public String getDefaultConstructor(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab - 1);
				char[] t1 = getTabSeq(nTab);
				if( !(container instanceof Bean) )
					sb.append(t0).append("public:").append(ls);
				List<Field> defaultFields = new ArrayList<Field>();
				for(Field field : container.fields)
				{
					InnerType it = innertypes.get(field.type);
					if( it != null && field.defaultval != null )
						defaultFields.add(field);
				}
				if( defaultFields.isEmpty() )
					sb.append(t1).append(container.name).append("() { }").append(ls).append(ls);
				else
				{
					sb.append(t1).append(container.name).append("()").append(ls).append(t1).append("    : ");
					for(int i = 0; i < defaultFields.size(); ++i)
					{
						Field field = defaultFields.get(i);
						sb.append("m_").append(field.name).append('(').append(makeDefaultVal(field.type, field.defaultval)).append(')');
						if( i != defaultFields.size() - 1)
						{
							sb.append(", ");
							if( (i+1) % 4 == 0 )
								sb.append(ls).append(t1).append("      ");
						}
					}
					sb.append(ls).append(t1).append('{').append(ls).append(t1).append('}').append(ls).append(ls);
				}
				return sb.toString();
			}
			
			@Override
			public String getCopyConstructor(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab - 1);
				char[] t1 = getTabSeq(nTab);
				sb.append(t1).append(container.name).append('(');
				for(int i = 0; i < container.fields.size(); ++i)
				{
					Field field = container.fields.get(i);
					InnerType it = innertypes.get(field.type);
					if( it == null || it.bObj || field.bList )
						sb.append("const ");
					sb.append(getTypeName(container, field));
					if( it == null || it.bObj || field.bList )
						sb.append('&');
					sb.append(' ').append(field.name);
					if( i != container.fields.size() - 1)
					{
						sb.append(", ");
						if( (i+1) % 4 == 0 )
							sb.append(ls).append(t1).append(getSpaceSeq(container.name.length() + 1));
					}
				}
				sb.append(')').append(ls).append(t1).append("    : ");
				for(int i = 0; i < container.fields.size(); ++i)
				{
					Field field = container.fields.get(i);
					sb.append("m_").append(field.name).append('(').append(field.name).append(')');
					if( i != container.fields.size() - 1)
					{
						sb.append(", ");
						if( (i+1) % 4 == 0 )
							sb.append(ls).append(t1).append("      ");
					}
				}
				sb.append(ls).append(t1).append('{').append(ls).append(t1).append('}').append(ls).append(ls);
				return sb.toString();
			}
			
			@Override
			public String getIntConstDecl(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab - 1);
				char[] t1 = getTabSeq(nTab);
				char[] t2 = getTabSeq(nTab + 1);
				if( ! container.intconsts.isEmpty() )
				{
					if( ! (container instanceof Bean) )
						sb.append(t0).append("public:").append(ls);
					sb.append(t1).append("enum ").append(ls);
					sb.append(t1).append('{').append(ls);
					for(int i = 0; i < container.intconsts.size(); ++i)
					{
						IntConst ic = container.intconsts.get(i);
						sb.append(t2).append(ic.name).append(" = ").append(ic.val);
						if( i < container.intconsts.size() - 1 )
							sb.append(',');
						sb.append(ls);
					}
					sb.append(t1).append("};").append(ls);
				}
				if( ! container.int16consts.isEmpty() )
				{
					if( ! (container instanceof Bean) )
						sb.append(t0).append("public:").append(ls);
					sb.append(t1).append("enum ").append(ls);
					sb.append(t1).append('{').append(ls);
					for(int i = 0; i < container.int16consts.size(); ++i)
					{
						IntConst ic = container.int16consts.get(i);
						sb.append(t2).append(ic.name).append(" = ").append(ic.val);
						if( i < container.int16consts.size() - 1 )
							sb.append(',');
						sb.append(ls);
					}
					sb.append(t1).append("};").append(ls);
				}
				if( ! container.int8consts.isEmpty() )
				{
					if( ! (container instanceof Bean) )
						sb.append(t0).append("public:").append(ls);
					sb.append(t1).append("enum ").append(ls);
					sb.append(t1).append('{').append(ls);
					for(int i = 0; i < container.int8consts.size(); ++i)
					{
						IntConst ic = container.int8consts.get(i);
						sb.append(t2).append(ic.name).append(" = ").append(ic.val);
						if( i < container.int8consts.size() - 1 )
							sb.append(',');
						sb.append(ls);
					}
					sb.append(t1).append("};").append(ls);
				}
				if( ! container.int8consts.isEmpty() || ! container.int16consts.isEmpty() || ! container.intconsts.isEmpty() )
					sb.append(ls);
				return sb.toString();
			}
			
			@Override
			public String getInnerEnumDecl(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab - 1);
				char[] t1 = getTabSeq(nTab);
				char[] t2 = getTabSeq(nTab + 1);
				if( ! container.ienums.isEmpty() && ! (container instanceof Bean) )
					sb.append(t0).append("public:").append(ls);
				for(InnerEnum ie : container.ienums)
				{
					sb.append(t1).append("enum ").append(ie.name).append(ls);
					sb.append(t1).append('{').append(ls);
					for(int i = 0; i < ie.constants.size(); ++i)
					{
						sb.append(t2).append(ie.constants.get(i));
						if( i < ie.constants.size() - 1 )
							sb.append(',');
						sb.append(ls);
					}
					sb.append(t1).append("};").append(ls);
					sb.append(t1).append("enum { ec").append(ie.name).append(" = ").append(ie.constants.size()).append(" };").append(ls).append(ls);
				}
				return sb.toString();
			}
			
			@Override
			public String getEncodeFunc(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab);
				char[] t1 = getTabSeq(nTab + 1);
				
				if( ! container.fields.isEmpty() )
					sb.append(t0).append("virtual void Encode(KET::Util::Stream::AOStream &ostream) const").append(ls);
				else
					sb.append(t0).append("virtual void Encode(KET::Util::Stream::AOStream & /*ostream*/) const").append(ls);
				sb.append(t0).append('{').append(ls);
				if( ! container.fields.isEmpty() )
				{
					for(Field field : container.fields)
					{
						if( container.ienames.contains(field.type) )
							sb.append(t1).append("KET::Util::Stream::EnumWrapper<").append(field.type).append(", ec").append(
									field.type).append("> ew_").append(field.name).append("(m_").append(field.name).append(");").append(ls);
					}
					sb.append(t1).append("ostream");
					for(int i = 0; i < container.fields.size(); ++i)
					{
						Field field = container.fields.get(i);
						if( container.ienames.contains(field.type) )
							sb.append(" << ew_").append(field.name);
						else
							sb.append(" << m_").append(field.name);
						if( i != container.fields.size() - 1)
						{
							if( (i+1) % 4 == 0 )
							{
								sb.append(ls).append(t1).append("       ");
							}
						}
					}
					sb.append(';').append(ls);
				}
				sb.append(t0).append('}').append(ls).append(ls);
				return sb.toString();
			}
			
			@Override
			public String getDecodeFunc(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				char[] t0 = getTabSeq(nTab);
				char[] t1 = getTabSeq(nTab + 1);
				if( ! container.fields.isEmpty() )
					sb.append(t0).append("virtual void Decode(KET::Util::Stream::AIStream &istream)").append(ls);
				else
					sb.append(t0).append("virtual void Decode(KET::Util::Stream::AIStream & /*istream*/)").append(ls);
				sb.append(t0).append('{').append(ls);
				if( ! container.fields.isEmpty() )
				{
					for(Field field : container.fields)
					{
						if( container.ienames.contains(field.type) )
							sb.append(t1).append("KET::Util::Stream::EnumWrapper<").append(field.type).append(", ec").append(
									field.type).append("> ew_").append(field.name).append("(m_").append(field.name).append(");").append(ls);
					}
					sb.append(t1).append("istream");
					for(int i = 0; i < container.fields.size(); ++i)
					{
						Field field = container.fields.get(i);
						if( container.ienames.contains(field.type) )
							sb.append(" >> ew_").append(field.name);
						else
							sb.append(" >> m_").append(field.name);
						if( i != container.fields.size() - 1)
						{
							if( (i+1) % 4 == 0 )
							{
								sb.append(ls).append(t1).append("       ");
							}
						}
					}
					sb.append(';').append(ls);
				}
				sb.append(t0).append('}').append(ls).append(ls);
				return sb.toString();
			}
			
			@Override
			public String getFieldsDecl(FieldsContainer container, int nTab)
			{
				StringBuilder sb = new StringBuilder();
				if( ! container.fields.isEmpty() && ( container instanceof Packet || container instanceof Peer ) )
				{
					sb.append(getTabSeq(nTab - 1));
					sb.append("private:").append(ls);
				}
				for(Field field : container.fields)
				{
					if( ! field.comment.isEmpty() )
						sb.append(getCommentString(field.comment, nTab));
					sb.append(getTabSeq(nTab));
					sb.append(cppGen.getTypeName(container, field));
					sb.append(" m_");
					sb.append(field.name);
					sb.append(';').append(ls);
				}
				return sb.toString();
			}
		}
		
		public Module(File fileSrc)
		{
			this.fileSrc = fileSrc;
		}
				
		private void parseConfig(Node root) throws ReaderException
		{
			root.getChild("javaconfig", javacfg);
			root.getChild("cppconfig", cppcfg);
		}

		private void parseExtBeans(List<Node> nodes) throws Exception
		{
			UniqueSet<String> refs = new UniqueSet<String>("ext refs");
			refs.addUnique(fileSrc.getAbsolutePath());
			for(Node node : nodes)
			{
				String ref = node.getString("ref");
				String fn = fileSrc.getParentFile().getAbsolutePath() + File.separator + ref;
				File refFileSrc = new File(fn).getCanonicalFile().getAbsoluteFile();
				refs.addUnique(refFileSrc.getAbsolutePath());
				Module m = new Module(refFileSrc);
				System.out.println("parsing src file " + refFileSrc.getAbsolutePath());
				Node root = ket.xml.Factory.newReader(refFileSrc).getRoot();
				m.parseConfig(root);
				if( m.cppcfg.sbeandirectory == null )
					throw new ReaderException("bad extbeans ref sbeandirectory");
				int n = 0;
				for(Node rn : root.getChildren("bean"))
				{
					ExtBean eb = new ExtBean();
					eb.name = rn.getString("name");
					eb.module = m;
					extbeanmap.put(m.javacfg.basepackage + "." + m.javacfg.sbeanclassname + "." + eb.name, eb);
					++n;
				}
				if( n != 0 )
					extmodules.add(m);
			}
		}
		
		private void checkFieldName(String name) throws ReaderException
		{
			if( name.startsWith(FIELD_NAME_PREFIX_RESERVED) )
				throw new ReaderException("field name start with [" + FIELD_NAME_PREFIX_RESERVED + "] is reserved");
		}
				
		private void parseBeans(List<Node> nodes) throws ReaderException
		{
			for(Node node : nodes)
			{
				String beanname = node.getString("name");
				if( innertypes.containsKey(beanname) )
					throw new ReaderException("bad bean name " + beanname);
				beannames.addUnique(beanname);
			}
			
			for(Node node : nodes)
			{
				String beanname = node.getString("name");
				Bean bean = new Bean();
				bean.name = beanname;
				bean.bToString = node.getBoolean("tostring", bean.bToString);
				bean.kclone = node.getString("kclone", bean.kclone);
				bean.scope = node.getByte("scope", bean.scope);
				parseInnerEnums(node, bean);
				parseIntConsts(node, bean);
				bean.comment = node.getTextContent();
				List<Node> fields = node.getChildren("field");
				UniqueSet<String> usFieldName = new UniqueSet<String>("field name in bean " + beanname);
				for(Node fieldnode : fields)
				{
					String fieldname = fieldnode.getString("name");
					usFieldName.addUnique(fieldname);
					checkFieldName(fieldname);
					String fieldtype = fieldnode.getString("type");
					if( fieldtype.equals(beanname) )
						throw new ReaderException("bad field node in bean " + beanname);
					if( innertypes.containsKey(fieldtype) )
						innertypesusedinbean.add(fieldtype);
					else if( ! bean.ienames.contains(fieldtype) && ! beannames.contains(fieldtype) 
							&& ! extbeanmap.containsKey(fieldtype) && ! refbeans.containsKey(fieldtype) )
						throw new ReaderException("bad field type " + fieldtype + " in bean " + beanname);
					String defaultval = fieldnode.getString("default", "");
					Field field = new Field();
					field.name = fieldname;
					field.type = fieldtype;
					field.uppername = fieldname; // no use
					field.bList = fieldnode.getBoolean("lst", false);
					if( field.bList )
						bLstInBeans = true;
					field.defaultval = checkDefaultVal(fieldtype, defaultval);
					field.comment = fieldnode.getTextContent();
					field.defaultalloc = fieldnode.getBoolean("defaultalloc", false);
					bean.fields.add(field);
				}
				beans.add(bean);			
			}		
		}
				
		private void parseRefPackages(List<Node> nodes) throws ReaderException
		{
			for(Node node : nodes)
			{
				String rpname = node.getString("name");
				RefPackage rp = new RefPackage();
				umRefPackage.putUnique(rpname, rp);
				rp.cppheader = node.getString("cppheader", rp.cppheader);
				rp.cppnamespace = node.getString("cppnamespace", rp.cppnamespace);
				rp.bLocalHeader = node.getBoolean("localheader", rp.bLocalHeader);
				List<Node> beans = node.getChildren("refbean");
				for(Node beannode : beans)
				{
					String rbname = beannode.getString("name");
					String lrbname = rpname + "." + rbname;
					RefBean rb = new RefBean();
					rb.refpackage = rp;
					rb.name = rbname;
					refbeans.putUnique(lrbname, rb);
				}
			}
		}
		
		private void parseChannels(List<Node> nodes) throws ReaderException
		{
			for(Node node : nodes)
			{
				String channelname = node.getString("name");
				if( channelnames.containsKey(channelname) )
					throw new ReaderException("dup channel name " + channelname);
				Channel channel = new Channel();
				channel.name = channelname;
				channel.comment = node.getString("comment", "");
				channel.scope = node.getByte("scope", channel.scope);
				parsePackets(node.getChildren("packet"), channel);
				channels.add(channel);
				channelnames.put(channelname, channel);
			}
		}
		
		private void parsePackets(List<Node> nodes, Channel channel) throws ReaderException
		{
			UniqueSet<String> usPacketName = new UniqueSet<String>("packet name in channel " + channel.name);
			for(Node node : nodes)
			{
				String packetname = node.getString("name");
				int pid = node.getInteger("id");
				usPacketName.addUnique(packetname);
				usPacketID.addUnique(pid);
				Packet packet = new Packet();
				packet.name = packetname;
				packet.id = pid;
				List<Node> fields = node.getChildren("field");
				UniqueSet<String> usFieldName = new UniqueSet<String>("field name in packet " + channel.name + "." + packet.name);
				parseInnerEnums(node, packet);
				parseIntConsts(node, packet);
				packet.comment = node.getTextContent();
				for(Node fieldnode : fields)
				{
					String fieldname = fieldnode.getString("name");
					usFieldName.addUnique(fieldname);
					checkFieldName(fieldname);
					String fieldtype = fieldnode.getString("type");
					if( fieldtype.equals(packetname) )
						throw new ReaderException("bad field node in packet " + channel.name + "." + packet.name);
					if( innertypes.containsKey(fieldtype) )
						innertypesusedinpacket.add(fieldtype);
					else if( ! packet.ienames.contains(fieldtype) && ! beannames.contains(fieldtype)
							&& ! refbeans.containsKey(fieldtype) && ! extbeanmap.containsKey(fieldtype) )
						throw new ReaderException("bad field type " + fieldtype + " in packet " + channel.name + "." + packet.name);
					String uppername = fieldname.substring(0, 1).toUpperCase();
					if( fieldname.length() > 1 )
						uppername = uppername + fieldname.substring(1);
					uppername = fieldnode.getString("uppername", uppername);
					Field field = new Field();
					field.name = fieldname;
					field.type = fieldtype;
					field.uppername = uppername;
					field.bList = fieldnode.getBoolean("lst", false);
					if( field.bList )
						bLstInPackets = true;
					field.comment = fieldnode.getTextContent();
					field.defaultalloc = fieldnode.getBoolean("defaultalloc", false);
					packet.fields.add(field);
				}
				channel.packets.add(packet);
			}
			packetnames.put(channel.name, usPacketName);
		}
		
		private void parsePeers(List<Node> nodes) throws ReaderException
		{
			for(Node node : nodes)
			{
				String peername = node.getString("name");
				String peertype = node.getString("type");
				if( ! peertypes.containsKey(peertype) )
					throw new ReaderException("unknown peer type " + peertype + " of peer " + peername);
				Peer peer = new Peer();
				peer.name = peername;
				peer.type = peertypes.get(peertype);
				if( peer.type == Peer.Type.ePTTCPServer || peer.type == Peer.Type.ePTTCPClient )
				{
					if( node.hasAttribute("maxidle") )
					{
						String maxidle = node.getString("maxidle");
						if( maxidle.equals("handler") )
							peer.maxidle = new Peer.MaxIdle(0);
						else
							peer.maxidle = new Peer.MaxIdle(Integer.parseInt(maxidle));
					}
				}
				
				peer.packagename = node.getString("package", peer.packagename);
				if( peernames.containsKey(peer.packagename + "." + peer.name) )
					throw new ReaderException("dup peer name " + peer.packagename + "." + peername);
				
				List<Node> fields = node.getChildren("field");			
				UniqueSet<String> usFieldName = new UniqueSet<String>("field name in peer " + peer.name);
				for(Node fieldnode : fields)
				{
					String fieldname = fieldnode.getString("name");
					usFieldName.addUnique(fieldname);
					String fieldtype = fieldnode.getString("type");
					if( fieldtype.equals(peer.name) )
						throw new ReaderException("bad field node in peer " + peer.name);
					if( ! innertypes.containsKey(fieldtype) )
					{
						if( ! beannames.contains(fieldtype) )
						{
							if( ! refbeans.containsKey(fieldtype) )
							{
								throw new ReaderException("bad field type " + fieldtype + " in peer " + peer.name);
							}
							else
								peer.refbeans.add(fieldtype);
						}
						else
							peer.beans.add(fieldtype);
					}
					String uppername = fieldname.substring(0, 1).toUpperCase();
					if( fieldname.length() > 1 )
						uppername = uppername + fieldname.substring(1);
					uppername = fieldnode.getString("uppername", uppername);
					Field field = new Field();
					field.name = fieldname;
					field.type = fieldtype;
					field.uppername = uppername;
					field.bList = fieldnode.getBoolean("lst", false);
					field.defaultalloc = fieldnode.getBoolean("defaultalloc", false);
					peer.fields.add(field);
				}
				
				List<Node> channels = node.getChildren("channelref");
				for(Node channelnode : channels)
				{
					String channelname = channelnode.getString("name");
					if( ! channelnames.containsKey(channelname) )
						throw new ReaderException("unknown channel name " + channelname + " of peer " + peername);
					Peer.ChannelRef channel = new Peer.ChannelRef();
					channel.channel = channelnames.get(channelname);
					List<Node> packetnodes = channelnode.getChildren("packetref");
					for(Node packetnode : packetnodes)
					{
						String packetname = packetnode.getString("name");
						if( ! packetnames.get(channelname).contains(packetname) )
							throw new ReaderException("unknown packet " + packetname + " of peer " + peername);
						channel.packets.add(packetname);
					}
					peer.channels.add(channel);
				}
				peers.add(peer);
				peernames.put(peer.packagename + "." + peer.name, peer);
			}
		}
		
		private void parseDefaultHandler(Node node) throws ReaderException
		{
			if( node == null )
				return;
			
			defaulthandler.name = node.getString("name");
			defaulthandler.vname = node.getString("vname");
			defaulthandler.packagename = node.getString("package", defaulthandler.packagename);
		}
		
		private void parseHandlers(List<Node> nodes) throws ReaderException
		{
			UniqueSet<String> usHandlerName = new UniqueSet<String>("handler name");
			boolean bSameNameWithDefaultHandler = false;
			for(Node node : nodes)
			{
				Handler handler = new Handler();
				handler.name = node.getString("name");
				handler.vname = node.getString("vname");
				handler.packagename = node.getString("package", handler.packagename);
				
				usHandlerName.addUnique(handler.packagename + "." + handler.name);
				if( (defaulthandler.packagename + "." + defaulthandler.name).equals(
						handler.packagename + "." + handler.name) )
					bSameNameWithDefaultHandler = true;
						
				List<Node> peerrefnodes = node.getChildren("peerref");
				for(Node peerrefnode : peerrefnodes)
				{
					String refname = peerrefnode.getString("name");
					if( ! peernames.containsKey(refname) )
						throw new ReaderException("unknown peer " + refname + " in handler " + handler.name);
					Peer peer = peernames.get(refname);
					if( peer.handler != null )
						throw new ReaderException("peer " + refname + " already has handler " + peer.handler.name);
					peer.handler = handler;
					handler.peers.add(peer);
				}
				handlers.add(handler);
			}
			
			for(Peer peer : peers)
			{
				if( peer.handler == null )
				{
					if( bSameNameWithDefaultHandler )
						throw new ReaderException("handler name " + defaulthandler.packagename + "."
								+ defaulthandler.name + " conflict with default handler");
					peer.handler = defaulthandler;
					defaulthandler.peers.add(peer);
				}
			}
			
			if( ! defaulthandler.peers.isEmpty() )
				handlers.add(defaulthandler);
		}
		
		public void parseSrc() throws Exception
		{
			System.out.println("parsing src file " + fileSrc.getAbsolutePath());
			Node root = ket.xml.Factory.newReader(fileSrc).getRoot();
			parseConfig(root);
			parseExtBeans(root.getChildren("extsbeans"));
			parseRefPackages(root.getChildren("refpackage"));
			parseBeans(root.getChildren("bean"));
			parseChannels(root.getChildren("channel"));
			parsePeers(root.getChildren("peer"));
			parseDefaultHandler(root.getChild("defaulthandler"));
			parseHandlers(root.getChildren("handler"));
		}
		
		public void parseUpdateSrc() throws Exception
		{
			String fnSrcUpdate = fileSrc.getParentFile().getAbsolutePath() + File.separator + "rpcupdate.xml";
			if( ! FileTool.fileExist(fnSrcUpdate) )
				return;
			System.out.println("parsing src update file " + fnSrcUpdate);
			File fSrcUpdate = new File(fnSrcUpdate);
			String parent = fSrcUpdate.getParent();
			Node root = ket.xml.Factory.newReader(fSrcUpdate).getRoot();
			JavaUpdateConfig jupdatecfg = root.getChild("javaconfig", JavaUpdateConfig.class);
			if( jupdatecfg != null )
			{
				if( ! FileTool.isAbsolute(jupdatecfg.srcdir) )
					jupdatecfg.srcdir = parent + File.separator + jupdatecfg.srcdir;
				javacfg.updateSrcDir = new File(jupdatecfg.srcdir).getCanonicalPath();
				if( ! FileTool.directoryExist(javacfg.updateSrcDir) )
					throw new FileNotFoundException(javacfg.updateSrcDir);
				System.out.println("java update src dir is " + javacfg.updateSrcDir);
			}
			CPPUpdateConfig cupdatecfg = root.getChild("cppconfig", CPPUpdateConfig.class);
			if( cupdatecfg != null )
			{
				if( ! FileTool.isAbsolute(cupdatecfg.srcdir) )
					cupdatecfg.srcdir = parent + File.separator + cupdatecfg.srcdir;
				cppcfg.updateSrcDir = new File(cupdatecfg.srcdir).getCanonicalPath();
				if( ! FileTool.directoryExist(cppcfg.updateSrcDir) )
					throw new FileNotFoundException(cppcfg.updateSrcDir);
				System.out.println("cpp update src dir is " + cppcfg.updateSrcDir);
				cppcfg.updateSBeanDir = cupdatecfg.sbeandir;
				cppcfg.updateRPCDir = cupdatecfg.rpcdir;
			}
		}
		
		private void genHandlers() throws Exception
		{
			for(Handler handler : handlers)
			{
				System.out.println("generating handler " + handler.packagename + "." + handler.name);
				String fnJava = dirOut.getAbsolutePath() + File.separator + hideOneDot(handler.packagename + ".") + handler.name + ".java";
				PrintStream psJava = new PrintStream(fnJava);
				List<HandlerBody> javaHandlers = new ArrayList<HandlerBody>();
				Map<String, HandlerBody> javaHandlerMap = new HashMap<String, HandlerBody>();
				String fnH = dirOut.getAbsolutePath() + File.separator
					+ (hideOneDot(handler.packagename + ".").replace(".", "_") + handler.name).toLowerCase() + ".h";
				PrintStream psH = new PrintStream(fnH);
				List<HandlerBody> hHandlers = new ArrayList<HandlerBody>();
				Map<String, HandlerBody> hHandlerMap = new HashMap<String, HandlerBody>();
				String fnCPP = dirOut.getAbsolutePath() + File.separator
					+ (hideOneDot(handler.packagename + ".").replace(".", "_") + handler.name).toLowerCase() + ".cpp";
				PrintStream psCPP = new PrintStream(fnCPP);
				List<HandlerBody> cppHandlers = new ArrayList<HandlerBody>();
				Map<String, HandlerBody> cppHandlerMap = new HashMap<String, HandlerBody>();
				
				int n = cppcfg.basenamespace.split("::").length;
				psH.println(logo);
				psH.println();
				psH.println("#ifndef __" + cppcfg.basenamespace.replaceAll("::", "__").toUpperCase() + "__" + handler.name.toUpperCase() + "_H");
				psH.println("#define __" + cppcfg.basenamespace.replaceAll("::", "__").toUpperCase() + "__" + handler.name.toUpperCase() + "_H");
				psH.println();
				
				psJava.println(logo);
				psJava.println();
				psJava.println("package " + javacfg.basepackage + hideOneDot("." + handler.packagename) +";");
				psJava.println();
				if( handler.hasPeerWithType(Peer.Type.ePTTCPServer) || handler.hasPeerWithType(Peer.Type.ePTUDPPpeer) )
					psJava.println("import ket.kio.NetAddress;");
				psJava.println("import ket.kio.SimplePacket;");
				psJava.println("import ket.kio.NetManager;");
				psH.println("#include <ket/kio/netmanager.h>");
				psH.println();
				psH.println("#include \"" + cppcfg.packetnamespace.toLowerCase() + ".h\"");
				for(Peer peer : handler.peers)
				{
					if( ! handler.packagename.equals(peer.packagename) )
						psJava.println("import " + javacfg.basepackage + "." + hideOneDot(peer.packagename + ".") + peer.name + ";");
					psH.println("#include \"" + peer.name.toLowerCase() + ".h\"");
				}
				psJava.println("import " + javacfg.basepackage + "." + javacfg.rpcpackage + "." + javacfg.packetclassname + ";");
				psJava.println("import " + javacfg.basepackage + "." + javacfg.sbeanclassname + ";");
				psJava.println();
				psJava.println("public class " + handler.name + "");
				psJava.println("{");
				psJava.println();
				psH.println();
				psCPP.println(logo);
				psCPP.println();
				psCPP.println("#include \"" + handler.name.toLowerCase() + ".h\"");
				psCPP.println();
				psH.println("namespace " + cppcfg.basenamespace.replaceAll("::", " { namespace "));
				psH.println("{");
				psH.println();
				psCPP.println("namespace " + cppcfg.basenamespace.replaceAll("::", " { namespace "));
				psCPP.println("{");
				psCPP.println();
				psH.println("\tclass " + handler.name);
				psH.println("\t{");
				psH.println("\tpublic:");
				psH.println();
				psJava.println("\t" + Generator.handlerBeginSig);
				psH.println("\t\t" + Generator.handlerBeginSig);
				psCPP.println("\t" + Generator.handlerBeginSig);
				StringBuilder sb;
				HandlerBody hb = null;
				for(Peer peer : handler.peers)
				{
					if( peer.type != Peer.Type.ePTUDPPpeer && peer.maxidle != null && peer.maxidle.value == 0 )
					{
						hb = new HandlerBody("int get" + peer.name + "MaxConnectionIdleTime");
						sb = new StringBuilder();
						
						sb.append("\tpublic " + hb.name + "()").append(Generator.ls);
						sb.append("\t{").append(Generator.ls);
						sb.append("\t\t// TODO").append(Generator.ls);
						sb.append("\t\treturn 0;").append(Generator.ls);
						sb.append("\t}").append(Generator.ls);
						sb.append(Generator.ls);
						
						hb.content = sb.toString();
						javaHandlers.add(hb);
						javaHandlerMap.put(hb.name, hb);
						
						hb = new HandlerBody("int Get" + peer.name + "MaxConnectionIdleTime");
						sb = new StringBuilder();					
						
						sb.append("\t\t" + hb.name + "();").append(Generator.ls);
						hb.content = sb.toString();
						hHandlers.add(hb);
						hHandlerMap.put(hb.name, hb);
						
						hb = new HandlerBody("int " + handler.name + "::Get" + peer.name + "MaxConnectionIdleTime");
						sb = new StringBuilder();					
						
						sb.append("\t" + hb.name + "()").append(Generator.ls);
						sb.append("\t{").append(Generator.ls);
						sb.append("\t\t// TODO").append(Generator.ls);
						sb.append("\t\treturn 0;").append(Generator.ls);
						sb.append("\t}").append(Generator.ls);
						sb.append(Generator.ls);
						hb.content = sb.toString();
						cppHandlers.add(hb);
						cppHandlerMap.put(hb.name, hb);
					}
					switch( peer.type )
					{
						case ePTUDPPpeer:
						{
							hb = new HandlerBody("void on" + peer.name + "Open");
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void on" + peer.name + "OpenFailed");
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer, ket.kio.ErrorCode errcode)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void on" + peer.name + "Close");
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer, ket.kio.ErrorCode errcode)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void On" + peer.name + "Open");
							sb = new StringBuilder();					
							
							sb.append("\t\t" + hb.name + "(" + peer.name + "* pPeer);").append(Generator.ls);
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void On" + peer.name + "OpenFailed");
							sb = new StringBuilder();					
							
							sb.append("\t\t" + hb.name + "(" + peer.name + "* pPeer, KET::KIO::ErrorCode errcode);").append(Generator.ls);
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void On" + peer.name + "Close");
							sb = new StringBuilder();					
							
							sb.append("\t\t" + hb.name + "(" + peer.name + "* pPeer, KET::KIO::ErrorCode errcode);").append(Generator.ls);
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "Open");
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "OpenFailed");
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "Close");
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);				
						}
						break;
					case ePTTCPServer:
						{
							hb = new HandlerBody("void on" + peer.name + "Open");
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void on" + peer.name + "OpenFailed");
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer, ket.kio.ErrorCode errcode)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void on" + peer.name + "Close");
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer, ket.kio.ErrorCode errcode)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void on" + peer.name + "SessionOpen");
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer, int sessionid, NetAddress addrClient)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void on" + peer.name + "SessionClose");
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer, int sessionid, ket.kio.ErrorCode errcode)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void On" + peer.name + "Open");
							sb = new StringBuilder();					
							
							sb.append("\t\t" + hb.name + "(" + peer.name + "* pPeer);").append(Generator.ls);
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void On" + peer.name + "OpenFailed");
							sb = new StringBuilder();					
							
							sb.append("\t\t" + hb.name + "(" + peer.name + "* pPeer, KET::KIO::ErrorCode errcode);").append(Generator.ls);
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void On" + peer.name + "Close");
							sb = new StringBuilder();					
							
							sb.append("\t\t" + hb.name + "(" + peer.name + "* pPeer, KET::KIO::ErrorCode errcode);").append(Generator.ls);
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void On" + peer.name + "SessionOpen");
							sb = new StringBuilder();					
							
							sb.append("\t\t" + hb.name + "(" + peer.name + "* pPeer, int sessionid, const KET::KIO::NetAddress &addrClient);").append(Generator.ls);
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);										
							
							hb = new HandlerBody("void On" + peer.name + "SessionClose");
							sb = new StringBuilder();					
							
							sb.append("\t\t" + hb.name + "(" + peer.name + "* pPeer, int sessionid, KET::KIO::ErrorCode errcode);").append(Generator.ls);
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "Open");
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "OpenFailed");
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "Close");
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "SessionOpen");
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/, int /*sessionid*/, const KET::KIO::NetAddress & /*addrClient*/)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "SessionClose");
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/, int /*sessionid*/, KET::KIO::ErrorCode /*errcode*/)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);
						}
						break;
					case ePTTCPClient:
						{
							hb = new HandlerBody("void on" + peer.name + "Open");
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void on" + peer.name + "OpenFailed");
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer, ket.kio.ErrorCode errcode)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content= sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void on" + peer.name + "Close");
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer, ket.kio.ErrorCode errcode)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);

							hb = new HandlerBody("void On" + peer.name + "Open");
							sb = new StringBuilder();					
							
							sb.append("\t\t" + hb.name + "(" + peer.name + "* pPeer);").append(Generator.ls);
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void On" + peer.name + "OpenFailed");
							sb = new StringBuilder();					
							
							sb.append("\t\t" + hb.name + "(" + peer.name + "* pPeer, KET::KIO::ErrorCode errcode);").append(Generator.ls);
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void On" + peer.name + "Close");
							sb = new StringBuilder();					
							
							sb.append("\t\t" + hb.name + "(" + peer.name + "* pPeer, KET::KIO::ErrorCode errcode);").append(Generator.ls);
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "Open");
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "OpenFailed");
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "Close");
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/, KET::KIO::ErrorCode /*errcode*/)").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);
						}
						break;
					default:
						break;
					}
					
					for(Peer.ChannelRef channel : peer.channels)
					{
						for(String packet : channel.packets)
						{
							hb = new HandlerBody("void on" + peer.name + "Recv" + packet);
							sb = new StringBuilder();
							
							sb.append("\tpublic " + hb.name + "(" + peer.name + " peer, "
									+ javacfg.packetclassname + "." + channel.channel.name + "." + packet + " packet");
							switch( peer.type )
							{
							case ePTUDPPpeer:
								sb.append(", NetAddress addrRemote");
								break;
							case ePTTCPServer:
								sb.append(", int sessionid");
								break;
							case ePTTCPClient:
								break;
							default:
								break;
							}
							sb.append(")").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							javaHandlers.add(hb);
							javaHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void On" + peer.name + "Recv" + packet);
							sb = new StringBuilder();
							
							sb.append("\t\t" + hb.name + "(" + peer.name + " *pPeer, const "
									+ cppcfg.basenamespace + "::" + cppcfg.packetnamespace + "::" + channel.channel.name + "::"
									+ packet + " *pPacket");
							switch( peer.type )
							{
							case ePTUDPPpeer:
								sb.append(", const KET::KIO::NetAddress &addrRemote");
								break;
							case ePTTCPServer:
								sb.append(", int sessionid");
								break;
							case ePTTCPClient:
								break;
							default:
								break;
							}
							sb.append(");").append(Generator.ls);
							
							hb.content = sb.toString();
							hHandlers.add(hb);
							hHandlerMap.put(hb.name, hb);
							
							hb = new HandlerBody("void " + handler.name + "::On" + peer.name + "Recv" + packet);
							sb = new StringBuilder();
							
							sb.append("\t" + hb.name + "(" + peer.name + "* /*pPeer*/, const "
									+ cppcfg.basenamespace + "::" + cppcfg.packetnamespace + "::" + channel.channel.name + "::"
									+ packet + " * /*pPacket*/");
							switch( peer.type )
							{
							case ePTUDPPpeer:
								sb.append(", const KET::KIO::NetAddress & /*addrRemote*/");
								break;
							case ePTTCPServer:
								sb.append(", int /*sessionid*/");
								break;
							case ePTTCPClient:
								break;
							default:
								break;
							}
							sb.append(")").append(Generator.ls);
							sb.append("\t{").append(Generator.ls);
							sb.append("\t\t// TODO").append(Generator.ls);
							sb.append("\t}").append(Generator.ls);
							sb.append(Generator.ls);
							
							hb.content = sb.toString();
							cppHandlers.add(hb);
							cppHandlerMap.put(hb.name, hb);
						}
					}
				}
				for(HandlerBody hbb : javaHandlers)
					psJava.print(hbb.content);
				psJava.println("\t" + Generator.handlerEndSig);
				psJava.println("}");
				
				for(HandlerBody hbb : hHandlers)
					psH.print(hbb.content);
				psH.println("\t\t" + Generator.handlerEndSig);
				
				for(HandlerBody hbb : cppHandlers)
					psCPP.print(hbb.content);
				psCPP.println("\t" + Generator.handlerEndSig);
			
				psH.println("\t};");
				for(int i = 0; i < n; ++i)
					psH.print("}");
				psH.println();
				psH.println();
				psH.println("#endif");
				for(int i = 0; i < n; ++i)
					psCPP.print("}");
				psCPP.println();
				psCPP.println();
				
				psJava.close();
				psH.close();
				psCPP.close();
				
				// update
				if( javacfg.updateSrcDir != null )
				{
					// TODO update handlers
					String fnJavaUpdate = javacfg.updateSrcDir + File.separator + javacfg.basepackage .replaceAll("\\.", "\\\\")
						+ File.separator + hideOneDot(handler.packagename + ".").replaceAll("\\.", "\\\\") + handler.name + ".java";
					if( ! FileTool.fileExist(fnJavaUpdate) )
					{
						System.out.println("updating " + fnJavaUpdate + "...");
						FileTool.copyFile(fnJava, fnJavaUpdate);
					}
					else
					{
						// TODO
						int beginSigIndex = -1;
						int endSigIndex = -1;
						String beginPref = null;
						String endPref = null;
						int i = 0;
						List<String> lines = new ArrayList<String>();
						LineNumberReader reader = new LineNumberReader(new FileReader(fnJavaUpdate));					
						String line = null;
						while( true )
						{
							line = reader.readLine();
							if( line == null )
								break;
							if( line.trim().startsWith(Generator.handlerBeginSig) )
							{
								beginSigIndex = i;
								beginPref = line.substring(0, line.indexOf(Generator.handlerBeginSig));
							}
							else if( line.trim().startsWith(Generator.handlerEndSig) )
							{
								endSigIndex = i;
								endPref = line.substring(0, line.indexOf(Generator.handlerEndSig));
							}
							lines.add(line);
							++i;
						}
						reader.close();
						if( beginSigIndex < 0 || endSigIndex < 0 || endSigIndex <= beginSigIndex || ! beginPref.equals(endPref) )
						{
							System.out.println("updating " + fnJavaUpdate + "...");
							PrintStream ps = new PrintStream(fnJavaUpdate);
							if( ! lines.isEmpty() )
							{
								String siggen = lines.get(0);
								if( siggen.startsWith("// modified by" ) )
									lines.set(0, logo);
								else
									lines.set(0, logo + Generator.ls + siggen);
							}
							for(String l : lines)
								ps.println(l);
							ps.println("\t" + Generator.handlerBeginSig);
							for(HandlerBody hbb : javaHandlers)
								ps.print(hbb.content);
							ps.println("\t" + Generator.handlerEndSig);
							ps.close();
						}
						else
						{
							List<String> oldJavaHandlerNames = new ArrayList<String>();
							List<Integer> oldJavaHandlerIndice = new ArrayList<Integer>();
							i = beginSigIndex + 1;
							while( i < endSigIndex )
							{
								String l = lines.get(i);
								String pref = beginPref + "public ";
								if( l.startsWith(pref) && l.endsWith(")") )
								{
									int c = l.indexOf("(");
									if( c != -1 )
									{
										String name = l.substring(pref.length(), c);
										oldJavaHandlerNames.add(name);
										oldJavaHandlerIndice.add(i);
									}
								}
								++i;
							}
							Set<String> oldJavaHandlerNameSet = new HashSet<String>(oldJavaHandlerNames);
							Set<String> newJavaHandlerNameSet = javaHandlerMap.keySet();
							boolean bNeedUpdate = oldJavaHandlerNameSet.size() != newJavaHandlerNameSet.size();
							if( ! bNeedUpdate )
							{
								oldJavaHandlerNameSet.removeAll(newJavaHandlerNameSet);
								if( ! oldJavaHandlerNameSet.isEmpty() )
								{
									bNeedUpdate = true;
								}
							}
							if( bNeedUpdate )
							{
								System.out.println("updating " + fnJavaUpdate + "...");
								Map<String, int[]> oldMap = new HashMap<String, int[]>();
								for(i = 0; i < oldJavaHandlerNames.size(); ++i)
								{
									int[] range = new int[2];
									range[0] = oldJavaHandlerIndice.get(i);
									if( i < oldJavaHandlerNames.size() - 1 )
										range[1] = oldJavaHandlerIndice.get(i+1)-1;
									else
										range[1] = endSigIndex - 1;
									oldMap.put(oldJavaHandlerNames.get(i), range);
								}
								PrintStream ps = new PrintStream(fnJavaUpdate);
								String siggen = lines.get(0);
								if( siggen.startsWith("// modified by" ) )
									lines.set(0, logo);
								else
									lines.set(0, logo + Generator.ls + siggen);
								for(i = 0; i <= beginSigIndex; ++i)
									ps.println(lines.get(i));
								for(HandlerBody hbb : javaHandlers)
								{
									int[] range = oldMap.remove(hbb.name);
									if( range != null )
									{
										for(int j = range[0]; j <= range[1]; ++j)
											ps.println(lines.get(j));
									}
									else
									{
										ps.print(hbb.content);
									}
								}
								for(Map.Entry<String, int[]> e : oldMap.entrySet())
								{
									int[] range = e.getValue();
									for(int j = range[0]; j <= range[1]; ++j)
									{
										ps.print("//");
										ps.println(lines.get(j));
									}
								}
								for(i = endSigIndex; i < lines.size(); ++i)
									ps.println(lines.get(i));
								ps.close();
							}
						}
					}
					if( cppcfg.updateSrcDir != null )
					{
						String fnHUpdate = cppcfg.updateSrcDir + File.separator + cppcfg.updateRPCDir + File.separator
							+ handler.name.toLowerCase() + ".h";
						if( ! FileTool.fileExist(fnHUpdate) )
						{
							System.out.println("updating " + fnHUpdate + "...");
							FileTool.copyFile(fnH, fnHUpdate);
						}
						else
						{
							// TODO
							int beginSigIndex = -1;
							int endSigIndex = -1;
							String beginPref = null;
							String endPref = null;
							int i = 0;
							List<String> lines = new ArrayList<String>();
							LineNumberReader reader = new LineNumberReader(new FileReader(fnHUpdate));					
							String line = null;
							while( true )
							{
								line = reader.readLine();
								if( line == null )
									break;
								if( line.trim().startsWith(Generator.handlerBeginSig) )
								{
									beginSigIndex = i;
									beginPref = line.substring(0, line.indexOf(Generator.handlerBeginSig));
								}
								else if( line.trim().startsWith(Generator.handlerEndSig) )
								{
									endSigIndex = i;
									endPref = line.substring(0, line.indexOf(Generator.handlerEndSig));
								}
								lines.add(line);
								++i;
							}
							reader.close();
							if( beginSigIndex < 0 || endSigIndex < 0 || endSigIndex <= beginSigIndex || ! beginPref.equals(endPref) )
							{
								System.out.println("updating " + fnHUpdate + "...");
								PrintStream ps = new PrintStream(fnHUpdate);
								if( ! lines.isEmpty() )
								{
									String siggen = lines.get(0);
									if( siggen.startsWith("// modified by" ) )
										lines.set(0, logo);
									else
										lines.set(0, logo + Generator.ls + siggen);
								}
								for(String l : lines)
									ps.println(l);
								ps.println("\t\t" + Generator.handlerBeginSig);
								for(HandlerBody hbb : hHandlers)
									ps.print(hbb.content);
								ps.println("\t\t" + Generator.handlerEndSig);
								ps.close();
							}
							else
							{
								List<String> oldHHandlerNames = new ArrayList<String>();
								List<Integer> oldHHandlerIndice = new ArrayList<Integer>();
								i = beginSigIndex + 1;
								while( i < endSigIndex )
								{
									String l = lines.get(i);
									String pref = beginPref;
									if( l.startsWith(pref) && l.endsWith(");") )
									{
										int c = l.indexOf("(");
										if( c != -1 )
										{
											String name = l.substring(pref.length(), c);
											oldHHandlerNames.add(name);
											oldHHandlerIndice.add(i);
										}
									}
									++i;
								}
								Set<String> oldHHandlerNameSet = new HashSet<String>(oldHHandlerNames);
								Set<String> newHHandlerNameSet = hHandlerMap.keySet();
								boolean bNeedUpdate = oldHHandlerNameSet.size() != newHHandlerNameSet.size();
								if( ! bNeedUpdate )
								{
									oldHHandlerNameSet.removeAll(newHHandlerNameSet);
									if( ! oldHHandlerNameSet.isEmpty() )
									{
										bNeedUpdate = true;
									}
								}
								if( bNeedUpdate )
								{
									System.out.println("updating " + fnHUpdate + "...");
									Map<String, int[]> oldMap = new HashMap<String, int[]>();
									for(i = 0; i < oldHHandlerNames.size(); ++i)
									{
										int[] range = new int[2];
										range[0] = oldHHandlerIndice.get(i);
										if( i < oldHHandlerNames.size() - 1 )
											range[1] = oldHHandlerIndice.get(i+1)-1;
										else
											range[1] = endSigIndex - 1;
										oldMap.put(oldHHandlerNames.get(i), range);
									}
									PrintStream ps = new PrintStream(fnHUpdate);
									String siggen = lines.get(0);
									if( siggen.startsWith("// modified by" ) )
										lines.set(0, logo);
									else
										lines.set(0, logo + Generator.ls + siggen);
									for(i = 0; i <= beginSigIndex; ++i)
										ps.println(lines.get(i));
									for(HandlerBody hbb : hHandlers)
									{
										int[] range = oldMap.remove(hbb.name);
										if( range != null )
										{
											for(int j = range[0]; j <= range[1]; ++j)
												ps.println(lines.get(j));
										}
										else
										{
											ps.print(hbb.content);
										}
									}
									for(Map.Entry<String, int[]> e : oldMap.entrySet())
									{
										int[] range = e.getValue();
										for(int j = range[0]; j <= range[1]; ++j)
										{
											ps.print("//");
											ps.println(lines.get(j));
										}
									}
									for(i = endSigIndex; i < lines.size(); ++i)
										ps.println(lines.get(i));
									ps.close();
								}
							}
						}
						//
						String fnCPPUpdate = cppcfg.updateSrcDir + File.separator + cppcfg.updateRPCDir + File.separator
						+ handler.name.toLowerCase() + ".cpp";
						if( ! FileTool.fileExist(fnCPPUpdate) )
						{
							System.out.println("updating " + fnCPPUpdate + "...");
							FileTool.copyFile(fnCPP, fnCPPUpdate);
						}
						else
						{
							// TODO
							int beginSigIndex = -1;
							int endSigIndex = -1;
							String beginPref = null;
							String endPref = null;
							int i = 0;
							List<String> lines = new ArrayList<String>();
							LineNumberReader reader = new LineNumberReader(new FileReader(fnCPPUpdate));					
							String line = null;
							while( true )
							{
								line = reader.readLine();
								if( line == null )
									break;
								if( line.trim().startsWith(Generator.handlerBeginSig) )
								{
									beginSigIndex = i;
									beginPref = line.substring(0, line.indexOf(Generator.handlerBeginSig));
								}
								else if( line.trim().startsWith(Generator.handlerEndSig) )
								{
									endSigIndex = i;
									endPref = line.substring(0, line.indexOf(Generator.handlerEndSig));
								}
								lines.add(line);
								++i;
							}
							reader.close();
							if( beginSigIndex < 0 || endSigIndex < 0 || endSigIndex <= beginSigIndex || ! beginPref.equals(endPref) )
							{
								System.out.println("updating " + fnCPPUpdate + "...");
								PrintStream ps = new PrintStream(fnCPPUpdate);
								if( ! lines.isEmpty() )
								{
									String siggen = lines.get(0);
									if( siggen.startsWith("// modified by" ) )
										lines.set(0, logo);
									else
										lines.set(0, logo + Generator.ls + siggen);
								}
								for(String l : lines)
									ps.println(l);
								ps.println("\t" + Generator.handlerBeginSig);
								for(HandlerBody hbb : cppHandlers)
									ps.print(hbb.content);
								ps.println("\t" + Generator.handlerEndSig);
								ps.close();
							}
							else
							{
								List<String> oldCPPHandlerNames = new ArrayList<String>();
								List<Integer> oldCPPHandlerIndice = new ArrayList<Integer>();
								i = beginSigIndex + 1;
								while( i < endSigIndex )
								{
									String l = lines.get(i);
									String pref1 = beginPref + "void " + handler.name + "::";
									String pref2 = beginPref + "int " + handler.name + "::";
									if( l.startsWith(pref1) && l.endsWith(")") )
									{
										int c = l.indexOf("(");
										if( c != -1 )
										{
											String name = l.substring(beginPref.length(), c);
											oldCPPHandlerNames.add(name);
											oldCPPHandlerIndice.add(i);
										}
									}
									else if( l.startsWith(pref2) && l.endsWith(")") )
									{
										int c = l.indexOf("(");
										if( c != -1 )
										{
											String name = l.substring(beginPref.length(), c);
											oldCPPHandlerNames.add(name);
											oldCPPHandlerIndice.add(i);
										}
									}
									++i;
								}
								Set<String> oldCPPHandlerNameSet = new HashSet<String>(oldCPPHandlerNames);
								Set<String> newCPPHandlerNameSet = cppHandlerMap.keySet();
								boolean bNeedUpdate = oldCPPHandlerNameSet.size() != newCPPHandlerNameSet.size();
								if( ! bNeedUpdate )
								{
									oldCPPHandlerNameSet.removeAll(newCPPHandlerNameSet);
									if( ! oldCPPHandlerNameSet.isEmpty() )
									{
										bNeedUpdate = true;
									}
								}
								if( bNeedUpdate )
								{
									System.out.println("updating " + fnCPPUpdate + "...");
									Map<String, int[]> oldMap = new HashMap<String, int[]>();
									for(i = 0; i < oldCPPHandlerNames.size(); ++i)
									{
										int[] range = new int[2];
										range[0] = oldCPPHandlerIndice.get(i);
										if( i < oldCPPHandlerNames.size() - 1 )
											range[1] = oldCPPHandlerIndice.get(i+1)-1;
										else
											range[1] = endSigIndex - 1;
										oldMap.put(oldCPPHandlerNames.get(i), range);
									}
									PrintStream ps = new PrintStream(fnCPPUpdate);
									String siggen = lines.get(0);
									if( siggen.startsWith("// modified by" ) )
										lines.set(0, logo);
									else
										lines.set(0, logo + Generator.ls + siggen);
									for(i = 0; i <= beginSigIndex; ++i)
										ps.println(lines.get(i));
									for(HandlerBody hbb : cppHandlers)
									{
										int[] range = oldMap.remove(hbb.name);
										if( range != null )
										{
											for(int j = range[0]; j <= range[1]; ++j)
												ps.println(lines.get(j));
										}
										else
										{
											ps.print(hbb.content);
										}
									}
									for(Map.Entry<String, int[]> e : oldMap.entrySet())
									{
										int[] range = e.getValue();
										for(int j = range[0]; j <= range[1]; ++j)
										{
											ps.print("//");
											ps.println(lines.get(j));
										}
									}
									for(i = endSigIndex; i < lines.size(); ++i)
										ps.println(lines.get(i));
									ps.close();
								}
							}
						}
						//
					}
				}
			}
		}
		
		private void genPeers() throws Exception
		{
			for(Peer peer : peers)
			{
				System.out.println("generating peer " + peer.packagename + "." + peer.name + " ...");
				String fnJava = dirOut.getAbsolutePath() + File.separator + hideOneDot(peer.packagename + ".") + peer.name + ".java";
				PrintStream psJava = new PrintStream(fnJava);
				String fnH = dirOut.getAbsolutePath() + File.separator + (hideOneDot(peer.packagename + ".").replace(".", "_") + peer.name).toLowerCase() + ".h";
				PrintStream psH = new PrintStream(fnH);
				String fnCPP = dirOut.getAbsolutePath() + File.separator
									+ (hideOneDot(peer.packagename + ".").replace(".", "_") + peer.name).toLowerCase() + ".cpp";
				PrintStream psCPP = new PrintStream(fnCPP);
				int n = cppcfg.basenamespace.split("::").length;
				//
				psH.println(logo);
				psH.println();
				psH.println("#ifndef __" + cppcfg.basenamespace.replaceAll("::", "__").toUpperCase() + "__" + peer.name.toUpperCase() + "_H");
				psH.println("#define __" + cppcfg.basenamespace.replaceAll("::", "__").toUpperCase() + "__" + peer.name.toUpperCase() + "_H");
				psH.println();
				psH.println("#include <ket/kio/packet.h>");
				psH.println("#include <ket/kio/netmanager.h>");
				psH.println();
				if( ! peer.beans.isEmpty() )
				{
					if( cppcfg.sbeandirectory == null )
					{
						psH.println("#include \"" + cppcfg.sbeannamespace.toLowerCase() + ".h\"");
					}
					else
						psH.println("#include <" + cppcfg.sbeandirectory.toLowerCase() + cppcfg.sbeannamespace.toLowerCase() + ".h>");
					psH.println();
				}
				psH.println("#include \"abasedencoder.h\"");
				psH.println();
				if( ! peer.refbeans.isEmpty() )
				{
					for(RefPackage rp : umRefPackage.values())
					{
						if( rp.cppheader != null )
						{
							if( rp.bLocalHeader )
								psH.println("#include \"" + rp.cppheader + "\"");
							else
								psH.println("#include <" + rp.cppheader + ">");
						}
					}
					psH.println();
				}
				psH.println("namespace " + cppcfg.basenamespace.replaceAll("::", " { namespace "));
				psH.println("{");
				psH.println();
				psH.println("\tclass " + peer.handler.name + ";");
				if( peer.type == Peer.Type.ePTUDPPpeer )
					psH.println("\tclass " + peer.name + " : public KET::KIO::UDPPeer<KET::KIO::SimplePacket>, public ABaseDencoder");
				else if( peer.type == Peer.Type.ePTTCPServer )
					psH.println("\tclass " + peer.name + " : public KET::KIO::TCPServer<KET::KIO::SimplePacket>, public ABaseDencoder");
				else if( peer.type == Peer.Type.ePTTCPClient )
					psH.println("\tclass " + peer.name + " : public KET::KIO::TCPClient<KET::KIO::SimplePacket>, public ABaseDencoder");
				psH.println("\t{");
				psH.println("\tpublic:");
				psH.println("\t\t" + peer.name + "(KET::KIO::NetManager &managerNet);");
				if( ! peer.fields.isEmpty() )
				{
					psH.print("\t\t" + peer.name + "(KET::KIO::NetManager &managerNet, ");
					for(int i = 0; i < peer.fields.size(); ++i)
					{
						Field field = peer.fields.get(i);
						InnerType it = innertypes.get(field.type);
						if( it == null || it.bObj )
							psH.print("const ");
						psH.print(cppGen.getTypeName(peer, field));
						if( it == null || it.bObj )
							psH.print("&");
						psH.print(" " + field.name);
						if( i != peer.fields.size() - 1)
						{
							psH.print(", ");
							if( (i+1) % 4 == 0 )
							{
								psH.println();
								psH.print("\t\t\t       ");
								for(int k = 0; k < peer.name.length() + 1; ++k )
									psH.print(" ");
							}
						}
					}
					psH.println(");");
				}
				psH.println("\t\tvirtual ~" + peer.name + "() { }");
				psH.println();
				psH.println("\tpublic:");
				psH.println("\t\tvoid Set" + peer.handler.name+ "(" + peer.handler.name+ " *p" +
						peer.handler.name+ ") { m_p" + peer.handler.name+ " = p" + peer.handler.name+ "; }");
				psH.println();
				psH.println("\tpublic:");
				psH.println("\t\tvirtual KET::KIO::IPacketEncoder<KET::KIO::SimplePacket>& GetEncoder() { return *this; }");
				psH.println("\t\tvirtual KET::KIO::IPacketDecoder<KET::KIO::SimplePacket>& GetDecoder() { return *this; }");
				psH.println();
				if( peer.type != Peer.Type.ePTUDPPpeer && peer.maxidle != null )
				{
					psH.println("\tpublic:");
					psH.println("\t\tvirtual int GetMaxConnectionIdleTime();");
					psH.println();
				}
				psH.println("\tpublic:");
				psH.println("\t\tvirtual bool DoCheckPacketType(int ptype);");
				psH.println();
				psH.println("\tpublic:");
				if( peer.type == Peer.Type.ePTUDPPpeer )
				{
					psH.println("\t\tvirtual void OnOpen();");
					psH.println("\t\tvirtual void OnOpenFailed(KET::KIO::ErrorCode errcode);");
					psH.println("\t\tvirtual void OnClose(KET::KIO::ErrorCode errcode);");
					psH.println("\t\tvirtual void OnPacketRecv(const KET::KIO::NetAddress &addrRemote, const KET::KIO::SimplePacket *pPacket);");
				}
				else if( peer.type == Peer.Type.ePTTCPClient )
				{
					psH.println("\t\tvirtual void OnOpen();");
					psH.println("\t\tvirtual void OnOpenFailed(KET::KIO::ErrorCode errcode);");
					psH.println("\t\tvirtual void OnClose(KET::KIO::ErrorCode errcode);");
					if( cppcfg.bsinglethread )
						psH.println("\t\tvirtual void OnPacketRecvParse(const KET::KIO::SimplePacket *pPacket);");
					psH.println("\t\tvirtual void OnPacketRecv(const KET::KIO::SimplePacket *pPacket);");
				}
				else if( peer.type == Peer.Type.ePTTCPServer )
				{
					psH.println("\t\tvirtual void OnOpen();");
					psH.println("\t\tvirtual void OnOpenFailed(KET::KIO::ErrorCode errcode);");
					psH.println("\t\tvirtual void OnClose(KET::KIO::ErrorCode errcode);");
					psH.println("\t\tvirtual void OnPacketSessionOpen(int sessionid, const KET::KIO::NetAddress &addrClient);");
					psH.println("\t\tvirtual void OnPacketSessionClose(int sessionid, KET::KIO::ErrorCode errcode);");
					psH.println("\t\tvirtual void OnPacketSessionRecv(int sessionid, const KET::KIO::SimplePacket *pPacket);");
				}
				
				psH.println();
				// getters & setters
				if( ! peer.fields.isEmpty() )
					psH.println("\tpublic:");
				for(Field field : peer.fields)
				{				
					InnerType it = innertypes.get(field.type);
					if( it != null && ! it.bObj )
					{
						psH.println("\t\t" + cppGen.getTypeName(peer, field) + " Get" + field.uppername + "() const");
						psH.println("\t\t{");
						psH.println("\t\t\treturn m_" + field.name + ";");
						psH.println("\t\t}");
						psH.println();
						
						psH.println("\t\tvoid Set" + field.uppername + "(" + cppGen.getTypeName(peer, field) + " "
								+ field.name + ")");
						psH.println("\t\t{");
						psH.println("\t\t\tm_" + field.name + " = " + field.name + ";");
						psH.println("\t\t}");
						psH.println();
					}
					else
					{
						psH.println("\t\tconst " + cppGen.getTypeName(peer, field) + "& Get" + field.uppername + "() const");
						psH.println("\t\t{");
						psH.println("\t\t\treturn m_" + field.name + ";");
						psH.println("\t\t}");
						psH.println();
						
						psH.println("\t\t" + cppGen.getTypeName(peer, field) + "& Get" + field.uppername + "()");
						psH.println("\t\t{");
						psH.println("\t\t\treturn m_" + field.name + ";");
						psH.println("\t\t}");
						psH.println();
						
						psH.println("\t\tvoid Set" + field.uppername + "(const " + cppGen.getTypeName(peer, field) + "& "
								+ field.name + ")");
						psH.println("\t\t{");
						psH.println("\t\t\tm_" + field.name + " = " + field.name + ";");
						psH.println("\t\t}");
						psH.println();
					}
				}
				psH.println("\tprivate:");
				psH.println("\t\t" + peer.handler.name+ "* m_p" + peer.handler.name+ ";");
				psH.print(cppGen.getFieldsDecl(peer, 2));
				psH.println("\t};");
				for(int i = 0; i < n; ++i)
					psH.print("}");
				psH.println();
				psH.println();
				psH.println("#endif");
				psH.close();
				
				psCPP.println(logo);
				psCPP.println();
				psCPP.println("#include \"" + peer.name.toLowerCase() + ".h\"");
				psCPP.println("#include \"" + peer.handler.name.toLowerCase() + ".h\"");
				psCPP.println("#include \"" + cppcfg.packetnamespace.toLowerCase() + ".h\"");			
				psCPP.println();
				psCPP.println("namespace " + cppcfg.basenamespace.replaceAll("::", " { namespace "));
				psCPP.println("{");
				psCPP.println();
				if( peer.type == Peer.Type.ePTUDPPpeer )
				{
					psCPP.println("\t" + peer.name + "::" + peer.name + "(KET::KIO::NetManager &managerNet)");
					psCPP.println("\t\t: KET::KIO::UDPPeer<KET::KIO::SimplePacket>(managerNet)");
				}
				else if( peer.type == Peer.Type.ePTTCPServer )
				{
					psCPP.println("\t" + peer.name + "::" + peer.name + "(KET::KIO::NetManager &managerNet)");
					psCPP.println("\t\t: KET::KIO::TCPServer<KET::KIO::SimplePacket>(managerNet)");
				}
				else if( peer.type == Peer.Type.ePTTCPClient )
				{
					psCPP.println("\t" + peer.name + "::" + peer.name + "(KET::KIO::NetManager &managerNet)");
					psCPP.println("\t\t: KET::KIO::TCPClient<KET::KIO::SimplePacket>(managerNet)");
				}
				psCPP.println("\t{");
				psCPP.println("\t}");
				psCPP.println();
				if( ! peer.fields.isEmpty() )
				{
					psCPP.print("\t" + peer.name + "::" + peer.name + "(KET::KIO::NetManager &managerNet, ");
					for(int i = 0; i < peer.fields.size(); ++i)
					{
						Field field = peer.fields.get(i);
						InnerType it = innertypes.get(field.type);
						if( it == null || it.bObj )
							psCPP.print("const ");
						psCPP.print(cppGen.getTypeName(peer, field));
						if( it == null || it.bObj )
							psCPP.print("&");
						psCPP.print(" " + field.name);
						if( i != peer.fields.size() - 1)
						{
							psCPP.print(", ");
							if( (i+1) % 4 == 0 )
							{
								psCPP.println();
								psCPP.print("\t\t\t       ");
								for(int k = 0; k < peer.name.length() + 1; ++k )
									psCPP.print(" ");
							}
						}
					}
					psCPP.println(")");
					psCPP.print("\t\t\t    : ");
					if( peer.type == Peer.Type.ePTUDPPpeer )
						psCPP.print(" KET::KIO::UDPPeer<KET::KIO::SimplePacket>(managerNet), ");
					else if( peer.type == Peer.Type.ePTTCPServer )
						psCPP.print(" KET::KIO::TCPServer<KET::KIO::SimplePacket>(managerNet), ");
					else
						psCPP.print(" KET::KIO::TCPClient<KET::KIO::SimplePacket>(managerNet), ");
					for(int i = 0; i < peer.fields.size(); ++i)
					{
						Field field = peer.fields.get(i);
						psCPP.print("m_" + field.name + "(" + field.name + ")");
						if( i != peer.fields.size() - 1)
						{
							psCPP.print(", ");
							if( (i+1) % 4 == 0 )
							{
								psCPP.println();
								psCPP.print("\t\t\t      ");
							}
						}
					}
					psCPP.println();
					psCPP.println("\t{");
					psCPP.println("\t}");
					psCPP.println();
				}
				
				psJava.println(logo);
				psJava.println();
				psJava.println("package " + javacfg.basepackage + hideOneDot("." + peer.packagename) +";");
				psJava.println();
				psJava.println("import ket.kio.PacketDecoder;");
				psJava.println("import ket.kio.PacketEncoder;");
				if( peer.type != Peer.Type.ePTTCPClient )
					psJava.println("import ket.kio.NetAddress;");
				if( peer.type == Peer.Type.ePTUDPPpeer )
					psJava.println("import ket.kio.UDPPeer;");
				else if( peer.type == Peer.Type.ePTTCPServer )
					psJava.println("import ket.kio.TCPServer;");
				else if( peer.type == Peer.Type.ePTTCPClient )
					psJava.println("import ket.kio.TCPClient;");
				psJava.println("import ket.kio.SimplePacket;");
				psJava.println();
				if( ! peer.beans.isEmpty() )
				{
					psJava.println("import " + javacfg.basepackage + "." + javacfg.sbeanclassname + ";");
				}
				psJava.println("import " + javacfg.basepackage + "." + javacfg.rpcpackage + ".ABaseDencoder;");
				if( ! peer.channels.isEmpty() )
					psJava.println("import " + javacfg.basepackage + "." + javacfg.rpcpackage + "." + javacfg.packetclassname + ";");
				if( ! peer.handler.packagename.equals(peer.packagename) )
				psJava.println("import " + javacfg.basepackage + "." + hideOneDot(peer.handler.packagename + ".") + peer.handler.name + ";");
							
				psJava.println();
				
				if( peer.type == Peer.Type.ePTUDPPpeer)
					psJava.println("public class " + peer.name + " extends UDPPeer<SimplePacket>");
				else if( peer.type == Peer.Type.ePTTCPServer )
					psJava.println("public class " + peer.name + " extends TCPServer<SimplePacket>");
				else
					psJava.println("public class " + peer.name + " extends TCPClient<SimplePacket>");
				psJava.println("{");
				psJava.println();
				
				psJava.println("\tpublic " + peer.name + "(" + peer.handler.name + " " + peer.handler.vname + ")");
				psJava.println("\t{");
				psJava.println("\t\tsuper(" + peer.handler.vname + ".getNetManager());");			
				psJava.println("\t\tthis." + peer.handler.vname + " = " + peer.handler.vname + ";");
				psJava.println("\t}");
				psJava.println();
				
				if( ! peer.fields.isEmpty() )
				{
					psJava.print("\tpublic " + peer.name + "(" + peer.handler.name + " " + peer.handler.vname + ", ");
					for(int i = 0; i < peer.fields.size(); ++i)
					{
						Field field = peer.fields.get(i);
						psJava.print(javaGen.getTypeName(peer, field) + " " + field.name);
						if( i != peer.fields.size() - 1)
						{
							psJava.print(", ");
							if( (i+1) % 4 == 0 )
							{
								psJava.println();
								psJava.print("\t\t       ");
								for(int k = 0; k < peer.name.length() + 1; ++k )
									psJava.print(" ");
							}
						}
					}
					psJava.println(")");
					psJava.println("\t{");
					psJava.println("\t\tsuper(" + peer.handler.vname + ".getNetManager());");			
					psJava.println("\t\tthis." + peer.handler.vname + " = " + peer.handler.vname + ";");
					for(Field field : peer.fields)
					{
						psJava.println("\t\tthis." + field.name + " = " + field.name + ";");
					}
					psJava.println("\t}");
					psJava.println();
				}
				
				if( peer.type != Peer.Type.ePTUDPPpeer && peer.maxidle != null )
				{
					psJava.println("\t@Override");
					psJava.println("\tpublic int getMaxConnectionIdleTime()");
					psJava.println("\t{");
					if( peer.maxidle.value != 0 )
						psJava.println("\t\treturn " + peer.maxidle.value + ";");
					else
						psJava.println("\t\treturn " + peer.handler.vname + ".get" + peer.name + "MaxConnectionIdleTime();");
					psJava.println("\t}");
					psJava.println();
				}

				if( peer.type != Peer.Type.ePTUDPPpeer && peer.maxidle != null )
				{
					psCPP.println("\tint " + peer.name + "::GetMaxConnectionIdleTime()");
					psCPP.println("\t{");
					if( peer.maxidle.value != 0 )
						psCPP.println("\t\treturn " + peer.maxidle.value + ";");
					else
						psCPP.println("\t\treturn m_p" + peer.handler.name + "->Get" + peer.name + "MaxConnectionIdleTime();");
					psCPP.println("\t}");
					psCPP.println();
				}
				
				psJava.println("\tprivate class Dencoder extends ABaseDencoder");
				psJava.println("\t{");
				
				if( peer.channels.isEmpty() )
				{
					psJava.println("\t\t@Override");
					psJava.println("\t\tpublic boolean doCheckPacketType(int ptype)");
					psJava.println("\t\t{");
					psJava.println("\t\t\treturn false;");
					psJava.println("\t\t}");
					psJava.println("\t}");
					psJava.println();
					
					psCPP.println("\tbool " + peer.name+ "::DoCheckPacketType(int /*ptype*/)");
					psCPP.println("\t{");
					psCPP.println("\t\treturn false;");
					psCPP.println("\t}");
					psCPP.println();
				}
				else
				{			
					psJava.println("\t\t@Override");
					psJava.println("\t\tpublic boolean doCheckPacketType(int ptype)");
					psJava.println("\t\t{");
					psCPP.println("\tbool " + peer.name+ "::DoCheckPacketType(int ptype)");
					psCPP.println("\t{");


					psJava.println("\t\t\tswitch( ptype )");
					psJava.println("\t\t\t{");						

					psCPP.println("\t\tswitch( ptype )");
					psCPP.println("\t\t{");

					for(Peer.ChannelRef channelref : peer.channels)
					{
						psJava.println("\t\t\t// " + channelref.channel.comment);
						psCPP.println("\t\t// " + channelref.channel.comment);
						for(String packet : channelref.packets)
						{
							psJava.println("\t\t\tcase " + javacfg.packetclassname + "." + getPacketEnumString(channelref.channel.name, packet) + ":");
							psCPP.println("\t\tcase " + cppcfg.packetnamespace + "::" + getPacketEnumString(channelref.channel.name, packet) + ":");
						}
					}
					psJava.println("\t\t\t\treturn true;");
					psJava.println("\t\t\tdefault:");
					psJava.println("\t\t\t\tbreak;");
					psJava.println("\t\t\t}");


					psCPP.println("\t\t\treturn true;");
					psCPP.println("\t\tdefault:");
					psCPP.println("\t\t\tbreak;");
					psCPP.println("\t\t}");


					psJava.println("\t\t\treturn false;");
					psJava.println("\t\t}");
					psJava.println("\t}");
					psJava.println();

					psCPP.println("\t\treturn false;");
					psCPP.println("\t}");
					psCPP.println();
				}
				
				psJava.println("\t@Override");
				psJava.println("\tpublic PacketEncoder<SimplePacket> getEncoder()");
				psJava.println("\t{");
				psJava.println("\t\treturn dencoder;");
				psJava.println("\t}");
				psJava.println();
				psJava.println("\t@Override");
				psJava.println("\tpublic PacketDecoder<SimplePacket> getDecoder()");
				psJava.println("\t{");
				psJava.println("\t\treturn dencoder;");
				psJava.println("\t}");
				psJava.println();
				
				if( peer.type == Peer.Type.ePTUDPPpeer )
				{
					psJava.println("\t@Override");
					psJava.println("\tpublic void onOpen()");
					psJava.println("\t{");
					psJava.println("\t\t" + peer.handler.vname + ".on" + peer.name + "Open(this);");
					psJava.println("\t}");
					psJava.println();
					psJava.println("\t@Override");
					psJava.println("\tpublic void onOpenFailed(ket.kio.ErrorCode errcode)");
					psJava.println("\t{");
					psJava.println("\t\t" + peer.handler.vname + ".on" + peer.name + "OpenFailed(this, errcode);");
					psJava.println("\t}");
					psJava.println();
					psJava.println("\t@Override");
					psJava.println("\tpublic void onClose(ket.kio.ErrorCode errcode)");
					psJava.println("\t{");
					psJava.println("\t\t" + peer.handler.vname + ".on" + peer.name + "Close(this, errcode);");
					psJava.println("\t}");
					psJava.println();
					
					psCPP.println("\tvoid " + peer.name + "::OnOpen()");
					psCPP.println("\t{");
					psCPP.println("\t\tm_p" + peer.handler.name + "->On" + peer.name + "Open(this);");
					psCPP.println("\t}");
					psCPP.println();
					psCPP.println("\tvoid " + peer.name + "::OnOpenFailed(KET::KIO::ErrorCode errcode)");
					psCPP.println("\t{");
					psCPP.println("\t\tm_p" + peer.handler.name + "->On" + peer.name + "OpenFailed(this, errcode);");
					psCPP.println("\t}");
					psCPP.println();
					psCPP.println("\tvoid " + peer.name + "::OnClose(KET::KIO::ErrorCode errcode)");
					psCPP.println("\t{");
					psCPP.println("\t\tm_p" + peer.handler.name + "->On" + peer.name + "Close(this, errcode);");
					psCPP.println("\t}");
					psCPP.println();
				}
				else if( peer.type == Peer.Type.ePTTCPServer )
				{
					psJava.println("\t@Override");
					psJava.println("\tpublic void onOpen()");
					psJava.println("\t{");
					psJava.println("\t\t" + peer.handler.vname + ".on" + peer.name + "Open(this);");
					psJava.println("\t}");
					psJava.println();
					psJava.println("\t@Override");
					psJava.println("\tpublic void onOpenFailed(ket.kio.ErrorCode errcode)");
					psJava.println("\t{");
					psJava.println("\t\t" + peer.handler.vname + ".on" + peer.name + "OpenFailed(this, errcode);");
					psJava.println("\t}");
					psJava.println();
					psJava.println("\t@Override");
					psJava.println("\tpublic void onClose(ket.kio.ErrorCode errcode)");
					psJava.println("\t{");
					psJava.println("\t\t" + peer.handler.vname + ".on" + peer.name + "Close(this, errcode);");
					psJava.println("\t}");
					psJava.println();
					psJava.println("\t@Override");
					psJava.println("\tpublic void onPacketSessionOpen(int sessionid, NetAddress addrClient)");
					psJava.println("\t{");
					psJava.println("\t\t" + peer.handler.vname + ".on" + peer.name + "SessionOpen(this, sessionid, addrClient);");
					psJava.println("\t}");
					psJava.println();
					psJava.println("\t@Override");
					psJava.println("\tpublic void onPacketSessionClose(int sessionid, ket.kio.ErrorCode errcode)");
					psJava.println("\t{");
					psJava.println("\t\t" + peer.handler.vname + ".on" + peer.name + "SessionClose(this, sessionid, errcode);");
					psJava.println("\t}");
					psJava.println();
					
					psCPP.println("\tvoid " + peer.name + "::OnOpen()");
					psCPP.println("\t{");
					psCPP.println("\t\tm_p" + peer.handler.name + "->On" + peer.name + "Open(this);");
					psCPP.println("\t}");
					psCPP.println();
					psCPP.println("\tvoid " + peer.name + "::OnOpenFailed(KET::KIO::ErrorCode errcode)");
					psCPP.println("\t{");
					psCPP.println("\t\tm_p" + peer.handler.name + "->On" + peer.name + "OpenFailed(this, errcode);");
					psCPP.println("\t}");
					psCPP.println();
					psCPP.println("\tvoid " + peer.name + "::OnClose(KET::KIO::ErrorCode errcode)");
					psCPP.println("\t{");
					psCPP.println("\t\tm_p" + peer.handler.name + "->On" + peer.name + "Close(this, errcode);");
					psCPP.println("\t}");
					psCPP.println();
					psCPP.println("\tvoid " + peer.name + "::OnPacketSessionOpen(int sessionid, const KET::KIO::NetAddress &addrClient)");
					psCPP.println("\t{");
					psCPP.println("\t\tm_p" + peer.handler.name + "->On" + peer.name + "SessionOpen(this, sessionid, addrClient);");
					psCPP.println("\t}");
					psCPP.println();
					psCPP.println("\tvoid " + peer.name + "::OnPacketSessionClose(int sessionid, KET::KIO::ErrorCode errcode)");
					psCPP.println("\t{");
					psCPP.println("\t\tm_p" + peer.handler.name + "->On" + peer.name + "SessionClose(this, sessionid, errcode);");
					psCPP.println("\t}");
					psCPP.println();
				}
				else if( peer.type == Peer.Type.ePTTCPClient )
				{
					psJava.println("\t@Override");
					psJava.println("\tpublic void onOpen()");
					psJava.println("\t{");
					psJava.println("\t\t" + peer.handler.vname + ".on" + peer.name + "Open(this);");
					psJava.println("\t}");
					psJava.println();
					psJava.println("\t@Override");
					psJava.println("\tpublic void onOpenFailed(ket.kio.ErrorCode errcode)");
					psJava.println("\t{");
					psJava.println("\t\t" + peer.handler.vname + ".on" + peer.name + "OpenFailed(this, errcode);");
					psJava.println("\t}");
					psJava.println();
					psJava.println("\t@Override");
					psJava.println("\tpublic void onClose(ket.kio.ErrorCode errcode)");
					psJava.println("\t{");
					psJava.println("\t\t" + peer.handler.vname + ".on" + peer.name + "Close(this, errcode);");
					psJava.println("\t}");
					psJava.println();
					
					psCPP.println("\tvoid " + peer.name + "::OnOpen()");
					psCPP.println("\t{");
					if( cppcfg.bsinglethread )
						psCPP.println("\t\tm_p" + peer.handler.name + "->AddOpenMessage();");
					else
						psCPP.println("\t\tm_p" + peer.handler.name + "->On" + peer.name + "Open(this);");
					psCPP.println("\t}");
					psCPP.println();
					psCPP.println("\tvoid " + peer.name + "::OnOpenFailed(KET::KIO::ErrorCode errcode)");
					psCPP.println("\t{");
					if( cppcfg.bsinglethread )
						psCPP.println("\t\tm_p" + peer.handler.name + "->AddOpenFailedMessage(errcode);");
					else
						psCPP.println("\t\tm_p" + peer.handler.name + "->On" + peer.name + "OpenFailed(this, errcode);");
					psCPP.println("\t}");
					psCPP.println();
					psCPP.println("\tvoid " + peer.name + "::OnClose(KET::KIO::ErrorCode errcode)");
					psCPP.println("\t{");
					if( cppcfg.bsinglethread )
						psCPP.println("\t\tm_p" + peer.handler.name + "->AddCloseMessage(errcode);");
					else
						psCPP.println("\t\tm_p" + peer.handler.name + "->On" + peer.name + "Close(this, errcode);");
					psCPP.println("\t}");
					psCPP.println();
				}
				

				if( peer.channels.isEmpty() )
				{
					psJava.println("\t@Override");
					if( peer.type == Peer.Type.ePTUDPPpeer )
					{
						psJava.println("\tpublic void onPacketRecv(NetAddress addrRemote, SimplePacket packet)");
						psCPP.println("\tvoid " + peer.name+ "::OnPacketRecv(const KET::KIO::NetAddress & /*addrRemote*/, const KET::KIO::SimplePacket * /*pPacket*/)");
					}
					else if( peer.type == Peer.Type.ePTTCPServer )
					{
						psJava.println("\tpublic void onPacketSessionRecv(int sessionid, SimplePacket packet)");
						psCPP.println("\tvoid " + peer.name+ "::OnPacketSessionRecv(int /*sessionid*/, const KET::KIO::SimplePacket * /*pPacket*/)");				
					}
					else if( peer.type == Peer.Type.ePTTCPClient )
					{
						psJava.println("\tpublic void onPacketRecv(SimplePacket packet)");
						psCPP.println("\tvoid " + peer.name+ "::OnPacketRecv(const KET::KIO::SimplePacket * /*pPacket*/)");
					}
					psJava.println("\t{");
					psJava.println("\t}");
					psJava.println();

					psCPP.println("\t{");
					psCPP.println("\t}");
					psCPP.println();
				}
				else
				{			
					psJava.println("\t@Override");
					if( peer.type == Peer.Type.ePTUDPPpeer )
					{
						psJava.println("\tpublic void onPacketRecv(NetAddress addrRemote, SimplePacket packet)");
						psCPP.println("\tvoid " + peer.name+ "::OnPacketRecv(const KET::KIO::NetAddress &addrRemote, const KET::KIO::SimplePacket *pPacket)");
					}
					else if( peer.type == Peer.Type.ePTTCPServer )
					{
						psJava.println("\tpublic void onPacketSessionRecv(int sessionid, SimplePacket packet)");
						psCPP.println("\tvoid " + peer.name+ "::OnPacketSessionRecv(int sessionid, const KET::KIO::SimplePacket *pPacket)");				
					}
					else if( peer.type == Peer.Type.ePTTCPClient )
					{
						psJava.println("\tpublic void onPacketRecv(SimplePacket packet)");
						if( cppcfg.bsinglethread )
						{
							psCPP.println("\tvoid " + peer.name+ "::OnPacketRecv(const KET::KIO::SimplePacket *pPacket)");
							psCPP.println("\t{");
							psCPP.println("\t\tm_p" + peer.handler.name + "->AddPacket(pPacket);");
							psCPP.println("\t}");
							psCPP.println("\tvoid " + peer.name+ "::OnPacketRecvParse(const KET::KIO::SimplePacket *pPacket)");
						}
						else
							psCPP.println("\tvoid " + peer.name+ "::OnPacketRecv(const KET::KIO::SimplePacket *pPacket)");
					}
					psJava.println("\t{");	
					psJava.println("\t\tswitch( packet.getType() )");
					psJava.println("\t\t{");

					psCPP.println("\t{");
					psCPP.println("\t\tswitch( pPacket->GetType() )");
					psCPP.println("\t\t{");

					for(Peer.ChannelRef channelref : peer.channels)
					{
						psJava.println("\t\t// " + channelref.channel.comment);
						for(String packet : channelref.packets)
						{
							psJava.println("\t\tcase " + javacfg.packetclassname + "." + getPacketEnumString(channelref.channel.name, packet) + ":");
							psJava.println("\t\t\t{");
							psJava.println("\t\t\t\t" + javacfg.packetclassname + "." + channelref.channel.name + "." + packet + " p = ("
									+ javacfg.packetclassname + "." + channelref.channel.name
									+ "." + packet + ")packet;");
							if( peer.type == Peer.Type.ePTUDPPpeer )
								psJava.println("\t\t\t\t" + peer.handler.vname + ".on" + peer.name + "Recv" + packet + "(this, p, addrRemote);");
							else if( peer.type == Peer.Type.ePTTCPServer )
								psJava.println("\t\t\t\t" + peer.handler.vname + ".on" + peer.name + "Recv" + packet + "(this, p, sessionid);");
							else if( peer.type == Peer.Type.ePTTCPClient )
								psJava.println("\t\t\t\t" + peer.handler.vname + ".on" + peer.name + "Recv" + packet + "(this, p);");
							psJava.println("\t\t\t}");
							psJava.println("\t\t\tbreak;");

							psCPP.println("\t\tcase " + cppcfg.packetnamespace + "::" + getPacketEnumString(channelref.channel.name, packet) + ":");
							psCPP.println("\t\t\t{");
							psCPP.println("\t\t\t\tconst " + cppcfg.packetnamespace + "::" + channelref.channel.name + "::" + packet + " *p = "
									+ "dynamic_cast<const " + cppcfg.packetnamespace + "::" + channelref.channel.name + "::" + packet + "*>(pPacket);");
							if( peer.type == Peer.Type.ePTUDPPpeer )
								psCPP.println("\t\t\t\tm_p" + peer.handler.name + "->On" + peer.name + "Recv" + packet + "(this, p, addrRemote);");
							else if( peer.type == Peer.Type.ePTTCPServer )
								psCPP.println("\t\t\t\tm_p" + peer.handler.name + "->On" + peer.name + "Recv" + packet + "(this, p, sessionid);");
							else if( peer.type == Peer.Type.ePTTCPClient )
								psCPP.println("\t\t\t\tm_p" + peer.handler.name + "->On" + peer.name + "Recv" + packet + "(this, p);");
							psCPP.println("\t\t\t}");
							psCPP.println("\t\t\tbreak;");
						}
					}
					psJava.println("\t\tdefault:");
					psJava.println("\t\t\tbreak;");
					psJava.println("\t\t}");
					psJava.println("\t}");
					psJava.println();

					psCPP.println("\t\tdefault:");
					psCPP.println("\t\t\tbreak;");
					psCPP.println("\t\t}");
					
					psCPP.println("\t}");
					psCPP.println();
				}
				
				// getters & setters
				for(Field field : peer.fields)
				{
					psJava.println("\tpublic " + javaGen.getTypeName(peer, field) + " get" + field.uppername + "()");
					psJava.println("\t{");
					psJava.println("\t\treturn " + field.name + ";");
					psJava.println("\t}");
					psJava.println();
					
					psJava.println("\tpublic void set" + field.uppername + "(" + javaGen.getTypeName(peer, field) + " "
							+ field.name + ")");
					psJava.println("\t{");
					psJava.println("\t\tthis." + field.name + " = " + field.name + ";");
					psJava.println("\t}");
					psJava.println();
				}		
				
				psJava.println("\t//todo");
				psJava.println("\tprivate Dencoder dencoder = new Dencoder();");
				psJava.println("\tprivate " + peer.handler.name + " " + peer.handler.vname + ";");
				
				// fields
				psJava.print(javaGen.getFieldsDecl(peer, 1));			
				
				psJava.println("}");
				
				for(int i = 0; i < n; ++i)
					psCPP.print("}");
				psCPP.println();
				psCPP.println();
				
				psJava.close();
				psCPP.close();
				
				// update
				if( javacfg.updateSrcDir != null )
				{
					// TODO
					String fnJavaUpdate = javacfg.updateSrcDir + File.separator + javacfg.basepackage .replaceAll("\\.", "\\\\")
						+ File.separator + hideOneDot(peer.packagename + ".").replaceAll("\\.", "\\\\") + peer.name + ".java";
					if( needUpdate(fnJava, fnJavaUpdate) )
					{
						System.out.println("updating " + fnJavaUpdate + "...");
						if( ! FileTool.copyFile(fnJava, fnJavaUpdate) )
							throw new Error("update failed");
					}
					if( cppcfg.updateSrcDir != null )
					{
						String fnHUpdate = cppcfg.updateSrcDir + File.separator + cppcfg.updateRPCDir + File.separator + peer.name.toLowerCase() + ".h";
						String fnCPPUpdate = cppcfg.updateSrcDir + File.separator + cppcfg.updateRPCDir + File.separator + peer.name.toLowerCase() + ".cpp";
						if( needUpdate(fnH, fnHUpdate) )
						{
							System.out.println("updating " + fnHUpdate + "...");
							if( ! FileTool.copyFile(fnH, fnHUpdate) )
								throw new Error("update failed");
						}
						if( needUpdate(fnCPP, fnCPPUpdate) )
						{
							System.out.println("updating " + fnCPPUpdate + "...");
							if( ! FileTool.copyFile(fnCPP, fnCPPUpdate) )
								throw new Error("update failed");
						}
					}
				}
			}		
		}
		
		private void genPackets() throws Exception
		{
			if( usPacketID.isEmpty() )
				return;
			
			String fnPacketJava = dirOut.getAbsolutePath() + File.separator + javacfg.rpcpackage + "." + javacfg.packetclassname + ".java";
			String fnDencoderJava = dirOut.getAbsolutePath() + File.separator + javacfg.rpcpackage + ".ABaseDencoder.java";
			PrintStream psPacketJava = new PrintStream(fnPacketJava);
			String fnPacketH = dirOut.getAbsolutePath() + File.separator + cppcfg.packetnamespace.toLowerCase() + ".h";
			PrintStream psPacketH = new PrintStream(fnPacketH);
			PrintStream psDencoderJava = new PrintStream(fnDencoderJava);
			String fnDencoderH = dirOut.getAbsolutePath() + File.separator + "abasedencoder.h";
			PrintStream psDencoderH = new PrintStream(fnDencoderH);
			String fnDencoderCPP = dirOut.getAbsolutePath() + File.separator + "abasedencoder.cpp";
			PrintStream psDencoderCPP = new PrintStream(fnDencoderCPP);
			
			// header		
			psPacketJava.println(logo);
			psPacketJava.println();
			psPacketJava.println("package " + javacfg.basepackage + "." + javacfg.rpcpackage + ";");
			psPacketJava.println();
			if( bLstInPackets )
			{
				psPacketJava.println("import java.util.List;");
				//psPacketJava.println("import java.util.ArrayList;");
			}
			if( innertypesusedinpacket.contains("bytebuffer") )
			{
				psPacketJava.println("import java.nio.ByteBuffer;");
			}
			psPacketJava.println();
			psPacketJava.println("import ket.util.Stream;");
			psPacketJava.println("import ket.kio.SimplePacket;");
			psPacketJava.println();
			psPacketJava.println("import " + javacfg.basepackage + "." + javacfg.sbeanclassname + ";");
			//if( ! refbeans.isEmpty() )
			//{
			//	for(String lrbname : refbeans.keySet())
			//	{
			//		psPacketJava.println("import " + javacfg.basepackage + "." + lrbname + ";");
			//	}
			//	psPacketJava.println();
			//}
			psPacketJava.println("public abstract class " + javacfg.packetclassname);
			psPacketJava.println("{");
			psPacketJava.println();
			
			int n = cppcfg.basenamespace.split("::").length;
			
			psDencoderH.println(logo);
			psDencoderH.println();
			psDencoderH.println("#ifndef __" + cppcfg.basenamespace.replaceAll("::", "__").toUpperCase() + "__ABASEDENCODER_H");
			psDencoderH.println("#define __" + cppcfg.basenamespace.replaceAll("::", "__").toUpperCase() + "__ABASEDENCODER_H");
			psDencoderH.println();
			psDencoderH.println("#include <ket/kio/packet.h>");
			psDencoderH.println();
			psDencoderH.println("namespace " + cppcfg.basenamespace.replaceAll("::", " { namespace "));
			psDencoderH.println("{");
			psDencoderH.println();
			psDencoderH.println("\tclass ABaseDencoder : public KET::KIO::SimpleDencoder");
			psDencoderH.println("\t{");
			psDencoderH.println("\tpublic:");
			psDencoderH.println("\t\tABaseDencoder() { }");
			psDencoderH.println("\t\tvirtual ~ABaseDencoder() { }");
			psDencoderH.println();
			psDencoderH.println("\tpublic:");
			psDencoderH.println("\t\tvirtual bool DoCheckPacketType(int /*ptype*/) { return true; }");
			psDencoderH.println("\t\tvirtual KET::KIO::SimplePacket* CreatePacket(int /*ptype*/);");
			if( cppcfg.bsinglethread )
				psDencoderH.println("\t\tvirtual void DestroyPacket(KET::KIO::SimplePacket * /*pPacket*/) { }");
			psDencoderH.println();
			psDencoderH.println("\t};");
			for(int i = 0; i < n; ++i)
				psDencoderH.print("}");
			psDencoderH.println();
			psDencoderH.println();
			psDencoderH.println("#endif");
			psDencoderH.close();
			
			psPacketH.println(logo);
			psPacketH.println();
			psPacketH.println("#ifndef __" + cppcfg.basenamespace.replaceAll("::", "__").toUpperCase() + "__" + cppcfg.packetnamespace.toUpperCase() + "_H");
			psPacketH.println("#define __" + cppcfg.basenamespace.replaceAll("::", "__").toUpperCase() + "__" + cppcfg.packetnamespace.toUpperCase() + "_H");
			psPacketH.println();
			for(Module m : extmodules)
			{
				if( m.cppcfg.sbeandirectory != null )
					psPacketH.println("#include <" + m.cppcfg.sbeandirectory.toLowerCase() + m.cppcfg.sbeannamespace.toLowerCase() + ".h>");
			}
			psPacketH.println("#include <ket/kio/packet.h>");
			if( cppcfg.sbeandirectory == null )
			{
				psPacketH.println();
				psPacketH.println("#include \"" + cppcfg.sbeannamespace.toLowerCase() + ".h\"");
			}
			else
				psPacketH.println("#include <" + cppcfg.sbeandirectory.toLowerCase() + cppcfg.sbeannamespace.toLowerCase() + ".h>");
			psPacketH.println();
			if( ! umRefPackage.isEmpty() )
			{
				for(RefPackage rp : umRefPackage.values())
				{
					if( rp.cppheader != null )
					{
						if( rp.bLocalHeader )
							psPacketH.println("#include \"" + rp.cppheader + "\"");
						else
							psPacketH.println("#include <" + rp.cppheader + ">");
					}
				}
				psPacketH.println();
			}
			psPacketH.println("namespace " + cppcfg.basenamespace.replaceAll("::", " { namespace "));
			psPacketH.println("{");
			psPacketH.println();
			psPacketH.println("\tnamespace " + cppcfg.packetnamespace);
			psPacketH.println("\t{");
			psPacketH.println();
			
			psDencoderJava.println(logo);
			psDencoderJava.println();
			psDencoderJava.println("package " + javacfg.basepackage + "." + javacfg.rpcpackage + ";");
			psDencoderJava.println();
			psDencoderJava.println("import ket.kio.SimplePacket;");
			psDencoderJava.println("import ket.kio.SimpleDencoder;");
			psDencoderJava.println();
			psDencoderJava.println("public abstract class ABaseDencoder extends SimpleDencoder");
			psDencoderJava.println("{");
			psDencoderJava.println();	
			psDencoderJava.println("\t@Override");
			psDencoderJava.println("\tpublic SimplePacket createPacket(int ptype)");
			psDencoderJava.println("\t{");
			psDencoderJava.println("\t\tSimplePacket packet = null;");
			psDencoderJava.println("\t\tswitch( ptype )");
			psDencoderJava.println("\t\t{");
			
			psDencoderCPP.println(logo);
			psDencoderCPP.println();
			psDencoderCPP.println("#include \"abasedencoder.h\"");
			psDencoderCPP.println("#include \"packet.h\"");
			psDencoderCPP.println();
			psDencoderCPP.println("namespace " + cppcfg.basenamespace.replaceAll("::", " { namespace "));
			psDencoderCPP.println("{");
			psDencoderCPP.println("\tKET::KIO::SimplePacket* ABaseDencoder::CreatePacket(int ptype)");
			psDencoderCPP.println("\t{");
			psDencoderCPP.println("\t\tKET::KIO::SimplePacket *p = NULL;");
			psDencoderCPP.println("\t\tswitch( ptype )");
			psDencoderCPP.println("\t\t{");
			
			// ids
			for(Channel channel : channels)
			{
				psPacketJava.println("\t// " + channel.comment);
				for(Packet packet : channel.packets)
				{
					psPacketJava.println("\tpublic static final int " + getPacketEnumString(channel.name, packet.name) + " = " + packet.id + ";");
				}
				psPacketJava.println();
			}
			
			psPacketH.println("\t\tenum");
			psPacketH.println("\t\t{");
			for(int j = 0; j < channels.size(); ++j)
			{
				Channel channel = channels.get(j);
				if( channel.scope == 1 )
					continue;
				psPacketH.println("\t\t\t// " + channel.comment);
				for(int i = 0; i < channel.packets.size(); ++i)
				{
					Packet packet = channel.packets.get(i);
					psPacketH.print("\t\t\t" + getPacketEnumString(channel.name, packet.name) + " = " + packet.id);
					if( i < channel.packets.size() - 1 || j < channels.size() - 1 )
						psPacketH.println(",");
					else
						psPacketH.println();
				}
				psPacketH.println();
			}
			psPacketH.println("\t\t};");
			psPacketH.println();
					
			// channels
			for(Channel channel : channels)
			{
				System.out.println("generating channel " + channel.name + " ...");
				boolean bGenCPP = (channel.scope != 1);
				
				// channel header
				psPacketJava.println("\t// " + channel.comment);
				psPacketJava.println("\tpublic static class " + channel.name);
				psPacketJava.println("\t{");
				psPacketJava.println();
				
				if( bGenCPP )
				{
					psPacketH.println("\t\t// " + channel.comment);
					psPacketH.println("\t\tnamespace " + channel.name);
					psPacketH.println("\t\t{");
					psPacketH.println();
				}
				
				psDencoderJava.println("\t\t// " + channel.comment);
				
				if( bGenCPP )
					psDencoderCPP.println("\t\t// " + channel.comment);
				
				// packets
				for(Packet packet : channel.packets)
				{
					System.out.println("generating packet " + channel.name + "." + packet.name + " ...");
					
					// comment
					psPacketJava.print(javaGen.getComment(packet, 2));
					if( bGenCPP )
						psPacketH.print(cppGen.getComment(packet, 3));
					
					// packet body header
					psPacketJava.println("\t\tpublic static class " + packet.name + " extends SimplePacket");
					psPacketJava.println("\t\t{");
					
					if( bGenCPP )
					{
						psPacketH.println("\t\t\tclass " + packet.name + " : public KET::KIO::SimplePacket");
						psPacketH.println("\t\t\t{");
					}
					
					psDencoderJava.println("\t\tcase " + javacfg.packetclassname + "." + getPacketEnumString(channel.name, packet.name) + ":");
					psDencoderJava.println("\t\t\tpacket = new " + javacfg.packetclassname + "." + channel.name + "." + packet.name + "();");
					psDencoderJava.println("\t\t\tbreak;");
					
					if( bGenCPP )
					{
						psDencoderCPP.println("\t\tcase Packet::" + getPacketEnumString(channel.name, packet.name) + ":");
						psDencoderCPP.println("\t\t\tp = new Packet::" + channel.name + "::" + packet.name + "();");
						psDencoderCPP.println("\t\t\tbreak;");
					}
					
					// int constants
					psPacketJava.print(javaGen.getIntConstDecl(packet, 3));
					if( bGenCPP )
						psPacketH.print(cppGen.getIntConstDecl(packet, 4));
					
					// inner enum
					psPacketJava.print(javaGen.getInnerEnumDecl(packet, 3));
					if( bGenCPP )
						psPacketH.print(cppGen.getInnerEnumDecl(packet, 4));				

					if( ! packet.fields.isEmpty() )
					{
						//default constructor
						psPacketJava.print(javaGen.getDefaultConstructor(packet, 3));
						if( bGenCPP )
							psPacketH.print(cppGen.getDefaultConstructor(packet, 4));
					
						// copy constructor
						psPacketJava.print(javaGen.getCopyConstructor(packet, 3));
						if( bGenCPP )
							psPacketH.print(cppGen.getCopyConstructor(packet, 4));
					}
					
					// destructor				
					//psPacketH.println("\t\t\t\tvirtual ~" + packet.name + "()");
					//psPacketH.println("\t\t\t\t{");
					//psPacketH.println("\t\t\t\t}");
					//psPacketH.println();
					
					// typeid
					psPacketJava.println("\t\t\t@Override");
					psPacketJava.println("\t\t\tpublic int getType()");
					psPacketJava.println("\t\t\t{");
					psPacketJava.println("\t\t\t\treturn " + javacfg.packetclassname + "." + getPacketEnumString(channel.name, packet.name) + ";");
					psPacketJava.println("\t\t\t}");
					psPacketJava.println();
					
					if( bGenCPP )
					{
					psPacketH.println("\t\t\tpublic:");
					psPacketH.println("\t\t\t\tvirtual int GetType() const");
					psPacketH.println("\t\t\t\t{");
					psPacketH.println("\t\t\t\t\treturn " + getPacketEnumString(channel.name, packet.name) + ";");
					psPacketH.println("\t\t\t\t}");
					psPacketH.println();
					}
					
					// decode
					psPacketJava.print(javaGen.getDecodeFunc(packet, 3));
					if( bGenCPP )
					psPacketH.print(cppGen.getDecodeFunc(packet, 4));		
					
					// encode
					psPacketJava.print(javaGen.getEncodeFunc(packet, 3));
					if( bGenCPP )
					psPacketH.print(cppGen.getEncodeFunc(packet, 4));
					
					
					// getters & setters
					if( bGenCPP )
					{
					if( ! packet.fields.isEmpty() )
					{
						psPacketH.println("\t\t\tpublic:");
					}
					}
					for(Field field : packet.fields)
					{
						psPacketJava.println("\t\t\tpublic " + javaGen.getTypeName(packet, field) + " get" + field.uppername + "()");
						psPacketJava.println("\t\t\t{");
						psPacketJava.println("\t\t\t\treturn " + field.name + ";");
						psPacketJava.println("\t\t\t}");
						psPacketJava.println();
						
						psPacketJava.println("\t\t\tpublic void set" + field.uppername + "(" + javaGen.getTypeName(packet, field) + " "
								+ field.name + ")");
						psPacketJava.println("\t\t\t{");
						psPacketJava.println("\t\t\t\tthis." + field.name + " = " + field.name + ";");
						psPacketJava.println("\t\t\t}");
						psPacketJava.println();
						
						if( bGenCPP )
						{
						InnerType it = innertypes.get(field.type);
						if( it != null && ! it.bObj )
						{
							psPacketH.println("\t\t\t\t" + cppGen.getTypeName(packet, field) + " Get" + field.uppername + "() const");
							psPacketH.println("\t\t\t\t{");
							psPacketH.println("\t\t\t\t\treturn m_" + field.name + ";");
							psPacketH.println("\t\t\t\t}");
							psPacketH.println();
							
							psPacketH.println("\t\t\t\tvoid Set" + field.uppername + "(" + cppGen.getTypeName(packet, field) + " "
									+ field.name + ")");
							psPacketH.println("\t\t\t\t{");
							psPacketH.println("\t\t\t\t\tm_" + field.name + " = " + field.name + ";");
							psPacketH.println("\t\t\t\t}");
							psPacketH.println();
						}
						else
						{
							psPacketH.println("\t\t\t\tconst " + cppGen.getTypeName(packet, field) + "& Get" + field.uppername + "() const");
							psPacketH.println("\t\t\t\t{");
							psPacketH.println("\t\t\t\t\treturn m_" + field.name + ";");
							psPacketH.println("\t\t\t\t}");
							psPacketH.println();
							
							psPacketH.println("\t\t\t\t" + cppGen.getTypeName(packet, field) + "& Get" + field.uppername + "()");
							psPacketH.println("\t\t\t\t{");
							psPacketH.println("\t\t\t\t\treturn m_" + field.name + ";");
							psPacketH.println("\t\t\t\t}");
							psPacketH.println();
							
							psPacketH.println("\t\t\t\tvoid Set" + field.uppername + "(const " + cppGen.getTypeName(packet, field) + "& "
									+ field.name + ")");
							psPacketH.println("\t\t\t\t{");
							psPacketH.println("\t\t\t\t\tm_" + field.name + " = " + field.name + ";");
							psPacketH.println("\t\t\t\t}");
							psPacketH.println();
						}
						}
					}				

					// fields
					if( bGenCPP )
					psPacketH.print(cppGen.getFieldsDecl(packet, 4));
					psPacketJava.print(javaGen.getFieldsDecl(packet, 3));				
					
					// packet body tailer
					psPacketJava.println("\t\t}");
					psPacketJava.println();
					
					if( bGenCPP )
					{
					psPacketH.println("\t\t\t};");
					psPacketH.println();
					}
				}
				
				// channel tailer
				psPacketJava.println("\t}");
				psPacketJava.println();
				
				if( bGenCPP )
				{
				psPacketH.println("\t\t}");
				psPacketH.println();
				}
				
				psDencoderJava.println();
			
				if( bGenCPP )
				psDencoderCPP.println();
			}
			
			// tailer
			psPacketJava.println("}");
			
			psPacketH.println("\t}");
			for(int i = 0; i < n; ++i)
				psPacketH.print("}");
			psPacketH.println();
			psPacketH.println();
			psPacketH.println("#endif");
			
			psDencoderJava.println("\t\tdefault:");
			psDencoderJava.println("\t\t\tbreak;");
			psDencoderJava.println("\t\t}");
			psDencoderJava.println("\t\treturn packet;");
			psDencoderJava.println("\t}");
			psDencoderJava.println();
			psDencoderJava.println("\t@Override");
			psDencoderJava.println("\tpublic boolean doCheckPacketType(int ptype)");
			psDencoderJava.println("\t{");
			psDencoderJava.println("\t\treturn true;");
			psDencoderJava.println("\t}");
			psDencoderJava.println("}");

			psDencoderCPP.println("\t\tdefault:");
			psDencoderCPP.println("\t\t\tbreak;");
			psDencoderCPP.println("\t\t}");
			psDencoderCPP.println("\t\treturn p;");
			psDencoderCPP.println("\t}");
			for(int i = 0; i < n; ++i)
				psDencoderCPP.print("}");
			psDencoderCPP.println();
			
			//
			psPacketJava.close();
			psPacketH.close();
			psDencoderJava.close();
			psDencoderCPP.close();
			
			if( javacfg.updateSrcDir != null )
			{
				// TODO
				String fnPacketJavaUpdate = javacfg.updateSrcDir + File.separator + javacfg.basepackage .replaceAll("\\.", "\\\\")
					+ File.separator + hideOneDot(javacfg.rpcpackage + ".").replaceAll("\\.", "\\\\") + javacfg.packetclassname+ ".java";
				if( needUpdate(fnPacketJava, fnPacketJavaUpdate) )
				{
					System.out.println("updating " + fnPacketJavaUpdate + "...");
					if( ! FileTool.copyFile(fnPacketJava, fnPacketJavaUpdate) )
						throw new Error("update failed");
				}
				String fnDencoderJavaUpdate = javacfg.updateSrcDir + File.separator + javacfg.basepackage .replaceAll("\\.", "\\\\")
					+ File.separator + hideOneDot(javacfg.rpcpackage + ".").replaceAll("\\.", "\\\\") + "ABaseDencoder.java";
				if( needUpdate(fnDencoderJava, fnDencoderJavaUpdate) )
				{
					System.out.println("updating " + fnDencoderJavaUpdate + "...");
					if( ! FileTool.copyFile(fnDencoderJava, fnDencoderJavaUpdate) )
						throw new Error("update failed");
				}
			}
			if( cppcfg.updateSrcDir != null )
			{
				String fnPacketHUpdate = cppcfg.updateSrcDir + File.separator + cppcfg.updateRPCDir + File.separator
											+ cppcfg.packetnamespace.toLowerCase() + ".h";
				String fnDencoderHUpdate = cppcfg.updateSrcDir + File.separator + cppcfg.updateRPCDir + File.separator + "abasedencoder.h";
				String fnDencoderCPPUpdate = cppcfg.updateSrcDir + File.separator + cppcfg.updateRPCDir + File.separator + "abasedencoder.cpp";
				if( needUpdate(fnPacketH, fnPacketHUpdate) )
				{
					System.out.println("updating " + fnPacketHUpdate + "...");
					if( ! FileTool.copyFile(fnPacketH, fnPacketHUpdate) )
						throw new Error("update failed");
				}
				if( needUpdate(fnDencoderH, fnDencoderHUpdate) )
				{
					System.out.println("updating " + fnDencoderHUpdate + "...");
					if( ! FileTool.copyFile(fnDencoderH, fnDencoderHUpdate) )
						throw new Error("update failed");
				}
				if( needUpdate(fnDencoderCPP, fnDencoderCPPUpdate) )
				{
					System.out.println("updating " + fnDencoderCPPUpdate + "...");
					if( ! FileTool.copyFile(fnDencoderCPP, fnDencoderCPPUpdate) )
						throw new Error("update failed");
				}
			}
		}
		
		private void genBeans() throws Exception
		{
			String fnJava = dirOut.getAbsolutePath() + File.separator + javacfg.sbeanclassname + ".java";
			PrintStream psJava = new PrintStream(fnJava);
			String fnH = dirOut.getAbsolutePath() + File.separator + cppcfg.sbeannamespace.toLowerCase() + ".h";
			PrintStream psH = new PrintStream(fnH);
			
			// header		
			psJava.println(logo);
			psJava.println();
			psJava.println("package " + javacfg.basepackage + ";");
			psJava.println();
			if( bLstInBeans )
			{
				psJava.println("import java.util.List;");
				psJava.println("import java.util.ArrayList;");
			}
			if( innertypesusedinbean.contains("bytebuffer") )
			{
				psJava.println("import java.nio.ByteBuffer;");
				psJava.println();
			}
			psJava.println("import ket.util.Stream;");
			psJava.println();
			psJava.println("public final class " + javacfg.sbeanclassname);
			psJava.println("{");
			psJava.println();
			
			psH.println(logo);
			psH.println();
			psH.println("#ifndef __" + cppcfg.basenamespace.replaceAll("::", "__").toUpperCase() + "__" + cppcfg.sbeannamespace.toUpperCase() + "_H");
			psH.println("#define __" + cppcfg.basenamespace.replaceAll("::", "__").toUpperCase() + "__" + cppcfg.sbeannamespace.toUpperCase() + "_H");
			psH.println();
			psH.println("#include <string>");
			psH.println("#include <ket/util/stream.h>");
			if( ! umRefPackage.isEmpty() )
			{
				for(RefPackage rp : umRefPackage.values())
				{
					if( ! rp.bLocalHeader && rp.cppheader != null )
					{
						if( rp.bLocalHeader )
							psH.println("#include \"" + rp.cppheader + "\"");
						else
							psH.println("#include <" + rp.cppheader + ">");
					}
				}
				psH.println();
			}
			for(Module m : extmodules)
			{
				if( m.cppcfg.sbeandirectory != null )
					psH.println("#include <" + m.cppcfg.sbeandirectory.toLowerCase() + m.cppcfg.sbeannamespace.toLowerCase() + ".h>");
					
			}
			psH.println();
			psH.println("namespace " + cppcfg.basenamespace.replaceAll("::", " { namespace "));
			psH.println("{");
			psH.println();
			psH.println("\tnamespace " + cppcfg.sbeannamespace);
			psH.println("\t{");
			psH.println();
				
			for(Bean bean : beans)
			{
				System.out.println("generating bean " + bean.name + " ...");
				boolean bGenCPP = (bean.scope != 1);
				
				// comment
				psJava.print(javaGen.getComment(bean, 1));
				if( bGenCPP )
					psH.print(cppGen.getComment(bean, 2));
				
				// class body header
				psJava.println("\tpublic static class " + bean.name + " implements Stream.IStreamable");
				psJava.println("\t{");
				psJava.println();
				
				if( bGenCPP )
				{
					psH.println("\t\tstruct " + bean.name + " : public KET::Util::Stream::IStreamable");
					psH.println("\t\t{");
				}
				
				// int constants
				psJava.print(javaGen.getIntConstDecl(bean, 2));
				if( bGenCPP )
					psH.print(cppGen.getIntConstDecl(bean, 3));

				// inner enum
				psJava.print(javaGen.getInnerEnumDecl(bean, 2));
				if( bGenCPP )
					psH.print(cppGen.getInnerEnumDecl(bean, 3));			

				if( ! bean.fields.isEmpty() )
				{
					// default constructor
					psJava.print(javaGen.getDefaultConstructor(bean, 2));
					if( bGenCPP )
						psH.print(cppGen.getDefaultConstructor(bean, 3));
					
					// copy constructor
					psJava.print(javaGen.getCopyConstructor(bean, 2));
					if( bGenCPP )
						psH.print(cppGen.getCopyConstructor(bean, 3));
					
					// kclone
					if( bean.kclone != null )
					{
						if( bean.kclone.equals("s") )
						{
							psJava.print(javaGen.getKSClone(bean, 2));
						}
						else if( bean.kclone.equals("d") )
						{
							psJava.print(javaGen.getKSClone(bean, 2));
							psJava.print(javaGen.getKDClone(bean, 2));
						}
					}
				}
				
				// tostring
				if( bean.bToString && ! bean.fields.isEmpty() )
				{
					psJava.println("\t\t@Override");
					psJava.println("\t\tpublic String toString()");
					psJava.println("\t\t{");
					psJava.print("\t\t\treturn \"[\"");
					for(int i = 0; i < bean.fields.size(); ++i)
					{
						Field field = bean.fields.get(i);
						psJava.print(" + " + field.name);
						if( i < bean.fields.size() - 1 )
							psJava.print(" + \" \"");
						if( (i+1) % 4 == 0 )
						{
							psJava.println();
							psJava.print("\t\t            ");
						}
					}
					psJava.println(" + \"]\";");
					psJava.println("\t\t}");
					psJava.println();
				}
				
				// decode
				psJava.print(javaGen.getDecodeFunc(bean, 2));
				if( bGenCPP )
					psH.print(cppGen.getDecodeFunc(bean, 3));	
				
				// encode
				psJava.print(javaGen.getEncodeFunc(bean, 2));
				if( bGenCPP )
					psH.print(cppGen.getEncodeFunc(bean, 3));			

				// fields
				psJava.print(javaGen.getFieldsDecl(bean, 2));
				if( bGenCPP )
					psH.print(cppGen.getFieldsDecl(bean, 3));
				
				// class body tailer
				psJava.println("\t}");
				psJava.println();
				
				if( bGenCPP )
				{
					psH.println("\t\t};");
					psH.println();
				}
			}
			
			// tailer
			psJava.println("}");
			psH.println("\t}");
			int n = cppcfg.basenamespace.split("::").length;
			for(int i = 0; i < n; ++i)
				psH.print("}");
			psH.println();
			psH.println();
			psH.println("#endif");
			
			//
			psJava.close();
			psH.close();
			
			// update
			if( javacfg.updateSrcDir != null )
			{
				// TODO
				String fnJavaUpdate = javacfg.updateSrcDir + File.separator
					+ javacfg.basepackage.replaceAll("\\.", "\\\\") + File.separator + javacfg.sbeanclassname + ".java";
				if( needUpdate(fnJava, fnJavaUpdate) )
				{
					System.out.println("updating " + fnJavaUpdate + "...");
					if( ! FileTool.copyFile(fnJava, fnJavaUpdate) )
						throw new Error("update failed");
				}
			}
			if( cppcfg.updateSrcDir != null )
			{
				// TODO
				String fnHUpdate = cppcfg.updateSrcDir + File.separator + cppcfg.updateSBeanDir + File.separator
					+ cppcfg.sbeannamespace.toLowerCase() + ".h";
				if( needUpdate(fnH, fnHUpdate) )
				{
					System.out.println("updating " + fnHUpdate + "...");
					if( ! FileTool.copyFile(fnH, fnHUpdate) )
						throw new Error("update failed");
				}
			}
		}
		
		public void gen(File dirOut) throws Exception
		{
			this.dirOut = dirOut;
			genBeans();
			genPackets();
			genPeers();
			genHandlers();
		}
		
		private File fileSrc;
		private File dirOut;
		private JavaConfig javacfg = new JavaConfig();
		private CPPConfig cppcfg = new CPPConfig();
		private List<Module> extmodules = new ArrayList<Module>();
		private Map<String, ExtBean> extbeanmap = new HashMap<String, ExtBean>();
		private List<Bean> beans = new ArrayList<Bean>();
		private UniqueSet<String> beannames = new UniqueSet<String>("bean name");
		private UniqueMap<String, RefPackage> umRefPackage = new UniqueMap<String, RefPackage>("ref package");
		private UniqueMap<String, RefBean> refbeans = new UniqueMap<String, RefBean>("ref bean");	
		private Map<String, Channel> channelnames = new HashMap<String, Channel>();
		private List<Channel> channels = new ArrayList<Channel>();
		private List<Peer> peers = new ArrayList<Peer>();
		private Map<String, Peer> peernames = new HashMap<String, Peer>();
		private Handler defaulthandler = new Handler();
		private List<Handler> handlers = new ArrayList<Handler>();
		private Set<String> innertypesusedinbean = new HashSet<String>();
		private Set<String> innertypesusedinpacket = new HashSet<String>();
		private UniqueSet<Integer> usPacketID = new UniqueSet<Integer>("packet id");
		private Map<String, Set<String>> packetnames = new HashMap<String, Set<String>>();
		private boolean bLstInBeans = false;
		private boolean bLstInPackets = false;
		private JavaGenerator javaGen = new JavaGenerator();
		private CPPGenerator cppGen = new CPPGenerator();
	}
	
	public void parse(String fnSrc, String fnOutDir)
	{
		File dirOut = new File(fnOutDir).getAbsoluteFile();
		if( ! FileTool.ensureDirectoryExist(dirOut) )
			throw new Error("bad output dir " + dirOut.getAbsolutePath());
		System.out.println("output dir is " + dirOut.getAbsolutePath());
		try
		{
			Module m = new Module(new File(fnSrc).getAbsoluteFile().getCanonicalFile());
			m.parseSrc();
			m.parseUpdateSrc();
			m.gen(dirOut);
			System.out.println("generate ok.");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private static class Bean extends FieldsContainer
	{
		public boolean bToString = false;
		public String kclone = null;
		public byte scope = 0;
	}
	
	private static class RefPackage
	{
		String cppnamespace;
		String cppheader;
		boolean bLocalHeader = true;
	}
	
	private static class ExtBean
	{
		String name;
		Module module;
	}
	
	private static class RefBean
	{
		public RefPackage refpackage;
		public String name;
	}
	
	private static class Channel
	{
		public String name;
		public String comment;
		public byte scope = 0;
		public List<Packet> packets = new ArrayList<Packet>();
	}
	
	private static class Packet extends FieldsContainer
	{
		public int id;		
	}
	
	private static class Peer extends FieldsContainer
	{
		public static enum Type
		{
			ePTUDPPpeer,
			ePTTCPServer,
			ePTTCPClient
		}
		
		public static class ChannelRef
		{
			public Channel channel;
			public List<String> packets = new ArrayList<String>();
		}
		
		public static class MaxIdle
		{
			public MaxIdle(int value)
			{
				this.value = value;
			}
			
			int value = 0;
		}
		
		public Type type;
		public String packagename = "";
		public Handler handler = null;
		public MaxIdle maxidle = null;
		public Set<String> beans = new HashSet<String>();
		public Set<String> refbeans = new HashSet<String>();
		public List<ChannelRef> channels = new ArrayList<ChannelRef>();
	}
	
	private static class Handler
	{
		public String name = "RPCManager";
		public String packagename = "";
		public String vname = "managerRPC";
		public List<Peer> peers = new ArrayList<Peer>();
		
		public boolean hasPeerWithType(Peer.Type type)
		{
			for(Peer peer : peers)
			{
				if( peer.type == type )
					return true;
			}
			return false;
		}
	}
	
	private void parseInnerEnums(Node node, FieldsContainer container) throws ReaderException
	{
		List<Node> ienums = node.getChildren("ienum");
		for(Node ienode : ienums)
		{
			String iename = ienode.getString("name");
			container.ienames.addUnique(iename);
			InnerEnum ie = new InnerEnum();
			ie.name = iename;
			UniqueSet<String> iecnames = new UniqueSet<String>("inner enum constant in " + container.name + "." + iename);
			List<Node> iecs = ienode.getChildren("constant");
			for(Node iecnode : iecs)
			{
				String iecname = iecnode.getString("name");
				iecnames.addUnique(iecname);
				ie.constants.add(iecname);
			}
			if( ! ie.constants.isEmpty() )
				container.ienums.add(ie);
		}
	}
	
	private void parseIntConsts(Node node, FieldsContainer container) throws ReaderException
	{
		List<Node> intconsts = node.getChildren("intconst");
		for(Node icnode : intconsts)
		{
			String name = icnode.getString("name");
			String val = icnode.getString("val");
			// TODO check name and val
			IntConst ic = new IntConst();
			ic.name = name;
			ic.val = val;
			container.intconsts.add(ic);
		}
		List<Node> int16consts = node.getChildren("int16const");
		for(Node icnode : int16consts)
		{
			String name = icnode.getString("name");
			String val = icnode.getString("val");
			// TODO check name and val
			IntConst ic = new IntConst();
			ic.name = name;
			ic.val = val;
			container.int16consts.add(ic);
		}
		List<Node> int8consts = node.getChildren("int8const");
		for(Node icnode : int8consts)
		{
			String name = icnode.getString("name");
			String val = icnode.getString("val");
			// TODO check name and val
			IntConst ic = new IntConst();
			ic.name = name;
			ic.val = val;
			container.int8consts.add(ic);
		}
	}
	
	private String checkDefaultVal(String fieldtype, String defaultval)
	{
		if( ! innertypes.containsKey(fieldtype) )
			return null;
		if( defaultval == null || defaultval.equals("") )
			return null;
		defaultval = defaultval.toLowerCase();
		// TODO
		try
		{
			if( fieldtype.equals("bool") )
			{
				Boolean.parseBoolean(defaultval);
				return defaultval;
			}
			else if( fieldtype.equals("int16") )
			{
				Short.parseShort(defaultval);
				return defaultval;
			}
			else if( fieldtype.equals("int32") )
			{
				Integer.parseInt(defaultval);
				return defaultval;
			}
			else if( fieldtype.equals("float") )
			{
				Float.parseFloat(defaultval);
				return defaultval;
			}
		}
		catch(Exception ex)
		{
		}
		return null;
	}
	
	private String makeDefaultVal(String fieldtype, String defaultval)
	{
		if( fieldtype.equals("bool") )
			return defaultval;
		else if( fieldtype.equals("int16") )
			return defaultval;
		else if( fieldtype.equals("int32") )
			return defaultval;
		else if( fieldtype.equals("float") )
		{
			if( defaultval.indexOf(".") != -1 )
			{
				if( defaultval.endsWith("f") )
					return defaultval;
				else
					return defaultval + "f";
			}
			else
				return defaultval;
		}
		throw new RuntimeException("impossible");
	}
	
	private String hideOneDot(String str) // haha
	{
		if( str.equals(".") )
			return "";
		return str;
	}
	
	private static class HandlerBody
	{
		public HandlerBody(String name)
		{
			this.name = name;
		}
		public String name;
		public String content;
	}
	
	private int readChar(Reader reader, char[] ca, int off, int len) throws IOException
	{
		int r = 0;
		while( len > 0 )
		{
			int n = reader.read(ca, off, len);
			if( n == -1 )
				break;
			if( n == 0 )
				continue;
			r += n;
			off += n;
			len -= n;
		}
		return r == 0 ? -1 : r;
	}
	
	private boolean needUpdate(String fnSrc, String fnDst) throws Exception
	{
		if( ! FileTool.fileExist(fnDst) )
			return true;
		LineNumberReader readerSrc = new LineNumberReader(new FileReader(fnSrc));
		LineNumberReader readerDst = new LineNumberReader(new FileReader(fnDst));
		readerSrc.readLine();
		String headDst = readerDst.readLine();
		if( headDst == null || ! headDst.startsWith("//") )
			return true;
		char[] caSrc = new char[8192];
		char[] caDst = new char[8192];
		while( true )
		{
			int nSrc = readChar(readerSrc, caSrc, 0, caSrc.length);
			int nDst = readChar(readerDst, caDst, 0, caDst.length);
			if( nSrc != nDst )
				return true;
			if( nSrc == -1 )
				return false;
			if( ! Arrays.equals(caSrc, caDst) )
				return true;
		}
	}
	
	private String getPacketEnumString(String channel, String packet)
	{
		return "e" + channel + "PKT" + packet;
	}
	
	public static class JavaConfig
	{
		public String basepackage = "fixme";
		public String rpcpackage = "rpc";
		public String packetclassname = "Packet";
		public String sbeanclassname = "SBean";
		
		public String updateSrcDir;
	}
	
	public static class JavaUpdateConfig
	{
		public String srcdir;
	}
	
	public static class CPPUpdateConfig
	{
		public String srcdir;
		public String sbeandir;
		public String rpcdir;
	}
	
	public static class CPPConfig
	{
		public String basenamespace = "FixMe";
		public String packetnamespace = "Packet";
		public String sbeannamespace = "SBean";
		public String sbeandirectory = null;
		public boolean bsinglethread = false;
		
		public String updateSrcDir;
		public String updateSBeanDir;
		public String updateRPCDir;
	}
	
	private Map<String, Peer.Type> peertypes = new HashMap<String, Peer.Type>();
	private Map<String, InnerType> innertypes = new HashMap<String, InnerType>();
	private String logo = "// modified by " + this.getClass().getName()+ " at " + new java.util.Date().toString() + ".";

	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		new RPCGen().parse(am.get("-src", "rpc.xml"), am.get("-outdir", "rpcgenout"));
	}

}
