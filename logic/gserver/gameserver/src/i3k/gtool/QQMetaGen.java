
package i3k.gtool;

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
import ket.util.Stream;

final class UniqueSet<E> extends AbstractSet<E>
{
	public UniqueSet(final String name, boolean bWarnDup)
	{
		this.name = name;
		this.bWarnDup = bWarnDup;
		set = new HashSet<E>();
	}
	
	public boolean addUnique(E e) throws ReaderException
	{
		if( set.contains(e) )
		{
			if( ! bWarnDup )
				throw new ReaderException("dup " + name + " : " + e + ".");
		}
		return set.add(e);
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
	private final boolean bWarnDup;
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
	public String strSize;
	public String lstSize;
	public String lstMaxSize;
}

class FieldsContainer
{
	public String name;
	public UniqueSet<String> ienames = new UniqueSet<String>("inner enum name", false);
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

public class QQMetaGen
{
	private static final String FIELD_NAME_PREFIX_RESERVED = "_qqmeta_";
	private final static char TLOG_SEP = '|';
	public QQMetaGen()
	{
		innertypes.put("bool", new InnerType("bool", "boolean", "Boolean", false));
		innertypes.put("int8", new InnerType("char", "byte", "Byte", false));
		innertypes.put("uint8", new InnerType("unsigned char", "byte", "Byte", false));
		innertypes.put("int16", new InnerType("short", "short", "Short", false));
		innertypes.put("uint16", new InnerType("uint16_t", "short", "Short", false));
		innertypes.put("int", new InnerType("int", "int", "Integer", false));
		innertypes.put("uint", new InnerType("unsigned int", "int", "Integer", false));
		innertypes.put("int32", new InnerType("int", "int", "Integer", false));
		innertypes.put("int64", new InnerType("int64_t", "long", "Long", false));
		innertypes.put("uint64", new InnerType("uint64_t", "long", "Long", false));
		innertypes.put("uint32", new InnerType("uint32_t", "int", "Integer", false));
		innertypes.put("float", new InnerType("float", "float", "Float", false));
		innertypes.put("string", new InnerType("std::string", "String", "String", true));
		innertypes.put("wstring", new InnerType("std::wstring", "String", "String", true));
		innertypes.put("datetime", new InnerType("std::string", "String", "String", true));
		innertypes.put("bytebuffer", new InnerType("KET::Util::ByteBuffer", "ByteBuffer", "ByteBuffer", true));
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
				if( container instanceof Bean )
				{
					return fieldType;
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
						{
							if( it.javawrapname.equals("String") )
							{
								if( javacfg.bIDIPEncode )
								{
									sb.append(t1).append("os.push").append(it.javawrapname).append('(').append(field.name).append(", " + field.strSize + ");").append(ls);
								}
								else
									sb.append(t1).append("os.push").append(it.javawrapname).append('(').append(field.name).append(");").append(ls);
							}
							else
								sb.append(t1).append("os.push").append(it.javawrapname).append('(').append(field.name).append(");").append(ls);
						}
						else
							sb.append(t1).append("os.push").append(it.javawrapname).append("List(").append(field.name).append(");").append(ls);
					}
					else
					{
						if( field.bList )
						{
							if( javacfg.bIDIPEncode )
							{
								sb.append(t1).append("os.pushList(").append(field.name).append(", " + field.lstSize + ");").append(ls);
							}
							else
								sb.append(t1).append("os.pushList(").append(field.name).append(");").append(ls);
						}
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
						{
							if( it.javawrapname.equals("String") )
							{
								if( javacfg.bIDIPEncode )
								{
									sb.append(t1).append(field.name).append(" = is.pop").append(it.javawrapname).append("(" + field.strSize + ");").append(ls);
								}
								else
									sb.append(t1).append(field.name).append(" = is.pop").append(it.javawrapname).append("();").append(ls);
							}
							else
								sb.append(t1).append(field.name).append(" = is.pop").append(it.javawrapname).append("();").append(ls);
						}
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
							if( javacfg.bIDIPEncode )
							{
								sb.append(t1).append(field.name).append(" = is.popList(").append(getTypeName(container, field.type)).append(
										".class, " + field.lstSize + ");").append(ls);
							}
							else
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
		
		public Module(File fileSrc)
		{
			this.fileSrc = fileSrc;
		}
				
		private void parseConfig(Node root) throws ReaderException
		{
			root.getChild("javaconfig", javacfg);
		}
		
		private void checkFieldName(String name) throws ReaderException
		{
			if( name.startsWith(FIELD_NAME_PREFIX_RESERVED) )
				throw new ReaderException("field name start with [" + FIELD_NAME_PREFIX_RESERVED + "] is reserved");
		}
		
		private void parseMacroGroups(List<Node> nodes) throws ReaderException
		{
			Iterator<Node> iter = nodes.iterator();
			while( iter.hasNext() )
			{
				Node node = iter.next();
				String name = node.getString("name");
				if( innertypes.containsKey(name) )
					throw new ReaderException("bad macrosgroup name " + name);
				if( ! macroGroupNames.addUnique(name) )
				{
					iter.remove();
				}
			}
			
			for(Node node : nodes)
			{
				String name = node.getString("name");
				MacroGroup group = new MacroGroup();
				group.name = name;
				group.desc = node.getString("desc", "");
				List<Node> macros = node.getChildren("macro");
				for(Node macronode : macros)
				{
					Macro macro = new Macro();
					macro.name = macronode.getString("name");
					macroNames.addUnique(macro.name);
					macro.desc = macronode.getString("desc", "");
					macro.val = macronode.getString("value", "UNDEFINED");
					group.macros.add(macro);
				}
				macroGroups.add(group);			
			}		
		}
				
		private void parseBeans(List<Node> nodes) throws ReaderException
		{
			for(Node node : nodes)
			{
				String beanname = node.getString("name");
				if( innertypes.containsKey(beanname) )
					throw new ReaderException("bad bean name " + beanname);
				beanNames.addUnique(beanname);
			}
			
			for(Node node : nodes)
			{
				String beanname = node.getString("name");
				Bean bean = new Bean();
				bean.name = beanname;
				bean.bToString = node.getBoolean("tostring", bean.bToString);
				bean.kclone = node.getString("kclone", bean.kclone);
				bean.scope = node.getByte("scope", bean.scope);
				if( javacfg.bIDIPEncode )
				{
					bean.idipID = node.getString("id", null);
				}
				parseInnerEnums(node, bean);
				parseIntConsts(node, bean);
				bean.comment = node.getString("desc", "");
				List<Node> fields = node.getChildren("entry");
				UniqueSet<String> usFieldName = new UniqueSet<String>("field name in bean " + beanname, false);
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
					else if( ! bean.ienames.contains(fieldtype) && ! beanNames.contains(fieldtype) 
							&& ! refbeans.containsKey(fieldtype) )
						throw new ReaderException("bad field type " + fieldtype + " in bean " + beanname);
					String defaultval = fieldnode.getString("default", "");
					Field field = new Field();
					field.name = fieldname;
					field.type = fieldtype;
					if( fieldtype.equals("string") )
						field.strSize = fieldnode.getString("size", null);
					field.uppername = fieldname; // no use
					//
					//
					if( javacfg.bIDIPEncode )
					{
						String vec = fieldnode.getString("param", null);
						if( vec != null && vec.startsWith("vector,struct,") )
						{
							field.bList = true;
							String[] v = vec.split(",");
							field.lstSize = v[2];
							field.lstMaxSize = fieldnode.getString("size");
						}
					}
					else
						field.bList = fieldnode.getBoolean("lst", false);
					if( field.bList )
						bLstInBeans = true;
					field.defaultval = checkDefaultVal(fieldtype, defaultval);
					field.comment = fieldnode.getString("desc", "");
					field.defaultalloc = fieldnode.getBoolean("defaultalloc", false);
					bean.fields.add(field);
				}
				beans.add(bean);			
				if( javacfg.bIDIPEncode )
				{
					if( bean.name.equals("IdipHeader") )
						beanIDIPHeader = bean;
				}
			}		
		}
	
		public void parseSrc() throws Exception
		{
			System.out.println("parsing src file " + fileSrc.getAbsolutePath());
			Node root = ket.xml.Factory.newReader(fileSrc).getRoot();
			parseConfig(root);
			parseMacroGroups(root.getChildren("macrosgroup"));
			parseBeans(root.getChildren("struct"));
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
		}
		
		private void genBeans() throws Exception
		{
			String fnJava = dirOut.getAbsolutePath() + File.separator + javacfg.sbeanclassname + ".java";
			PrintStream psJava = new PrintStream(fnJava);
			
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
			
			if (javacfg.bIDIPEncode)
			{
				System.out.println("generating IDIP return value macros ...");
				psJava.println("\tpublic static final int IDIP_RSP_SUCCESS = 0;");
				psJava.println("\tpublic static final int IDIP_RSP_FAILED = -1;");
				psJava.println("\tpublic static final int IDIP_RSP_ROLE_NOT_EXIST = -2;");
				psJava.println("\tpublic static final int IDIP_RSP_ITEM_ID_INVALID = -3;");
				psJava.println("\tpublic static final int IDIP_RSP_EQUIP_ID_INVALID = -4;");
				psJava.println("\tpublic static final int IDIP_RSP_GENERAL_ID_INVALID = -5;");
				psJava.println();
				psJava.println("\tpublic static final int IDIP_HEADER_RESULT_SUCCESS = 0;");
				psJava.println("\tpublic static final int IDIP_HEADER_RESULT_EMPTY_PACKET_SUCCESS = 1;");
				psJava.println("\tpublic static final int IDIP_HEADER_RESULT_NETWORK_EXCEPTION = -1;");
				psJava.println("\tpublic static final int IDIP_HEADER_RESULT_TIMEOUT = -2;");
				psJava.println("\tpublic static final int IDIP_HEADER_RESULT_BD_EXCEPTION = -3;");
				psJava.println("\tpublic static final int IDIP_HEADER_RESULT_API_EXCEPTION = -4;");
				psJava.println("\tpublic static final int IDIP_HEADER_RESULT_SERVER_BUSY = -5;");
				psJava.println("\tpublic static final int IDIP_HEADER_RESULT_OTHER_ERROR = -100;");
				psJava.println();
			}
			
			for(MacroGroup group : macroGroups)
			{
				System.out.println("generating macrosgroup " + group.name + " ...");
				
				// comment
				psJava.print(javaGen.getCommentString(group.desc, 1));
				for(Macro m : group.macros)
				{
					psJava.print(javaGen.getCommentString(m.desc, 1));
					psJava.println("\tpublic static final int " + m.name + " = " + m.val + ";");
				}
				psJava.println();
			}
			
			if( javacfg.bIDIPEncode && beanIDIPHeader != null )
			{
				psJava.print("\tpublic static final int PACKET_HEADER_SIZE =");
				for(int i = 0; i < beanIDIPHeader.fields.size(); ++i)
				{
					Field f = beanIDIPHeader.fields.get(i);
					switch( f.type )
					{
					case "uint32":
					case "int32":
						psJava.print(" 4");
						break;
					case "string":
						psJava.print(" " + f.strSize);
						break;
					default:
						throw new Error("bad filed type of idip header " + f.type);
					}
					if( i == beanIDIPHeader.fields.size() - 1)
						psJava.println(";");
					else
						psJava.print(" +");
				}
				psJava.println();
			}
			
			psJava.println();
			psJava.println();
				
			for(Bean bean : beans)
			{
				System.out.println("generating bean " + bean.name + " ...");
				
				// comment
				psJava.print(javaGen.getComment(bean, 1));
				
				// class body header
				if( javacfg.bTLogToString )
					psJava.println("\tpublic static class " + bean.name);
				else
					psJava.println("\tpublic static class " + bean.name + " implements Stream.IStreamable");
				psJava.println("\t{");
				psJava.println();
				
				if( javacfg.bIDIPEncode && bean.idipID != null )
				{
					psJava.println("\t\tpublic static final int idipID = " + bean.idipID + ";");
					psJava.println();
				}
	
				// int constants
				psJava.print(javaGen.getIntConstDecl(bean, 2));

				// inner enum
				psJava.print(javaGen.getInnerEnumDecl(bean, 2));		

				if( ! bean.fields.isEmpty() )
				{
					// default constructor
					psJava.print(javaGen.getDefaultConstructor(bean, 2));
					
					// copy constructor
					psJava.print(javaGen.getCopyConstructor(bean, 2));
					
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
				if( javacfg.bTLogToString )
				{
					psJava.println("\t\t@Override");
					psJava.println("\t\tpublic String toString()");
					psJava.println("\t\t{");
					//
					psJava.println("\t\t\tStringBuilder sb = new StringBuilder(\"\");");
					psJava.println("\t\t\tsb.append(\"" + bean.name + "\");");//.append('" + TLOG_SEP + "');");
					for(int i = 0; i < bean.fields.size(); ++i)
					{
						Field field = bean.fields.get(i);
						psJava.println("\t\t\tsb.append('" + TLOG_SEP + "').append(" + field.name + ");");
					}
					/*
					sb.append(gs.getConfig().id).append(TLOG_SEP);
					sb.append(gs.getTimeStampStr()).append(TLOG_SEP);
					sb.append(msdkInfo.gameappID).append(TLOG_SEP);
					sb.append(msdkInfo.platID).append(TLOG_SEP);
					sb.append(msdkInfo.openID).append(TLOG_SEP);
					sb.append(id).append(TLOG_SEP);
					sb.append(onlineTime).append(TLOG_SEP);
					sb.append(lvl).append(TLOG_SEP);
					sb.append(getFriendCountWithoutLock()).append(TLOG_SEP);
					sb.append(msdkInfo.clientVer).append(TLOG_SEP);
					sb.append(msdkInfo.systemHardware).append(TLOG_SEP);
					sb.append(msdkInfo.telecomOper).append(TLOG_SEP);
					sb.append(msdkInfo.network);
					*/
					psJava.println("\t\t\tsb.append('\\n');");
					psJava.println("\t\t\treturn sb.toString();");
					//
					psJava.println("\t\t}");
					psJava.println();
				}
				else if( bean.bToString && ! bean.fields.isEmpty() )
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
				
				if( javacfg.bTLogToString )
				{
					
				}
				else
				{
					// decode
					psJava.print(javaGen.getDecodeFunc(bean, 2));
				
					// encode
					psJava.print(javaGen.getEncodeFunc(bean, 2));
				}

				// fields
				psJava.print(javaGen.getFieldsDecl(bean, 2));
				
				// class body tailer
				psJava.println("\t}");
				psJava.println();
			}
			
			if( javacfg.bIDIPEncode )
			{
				psJava.println("\tpublic static Stream.IStreamable decodePacket(int cmdID, byte[] bodyData)");
				psJava.println("\t{");
				psJava.println("\t\tStream.BytesInputStream bais = new Stream.BytesInputStream(bodyData, 0, bodyData.length);");
				psJava.println("\t\tStream.AIStream is = new Stream.IStreamBE(bais);");
				psJava.println("\t\ttry");
				psJava.println("\t\t{");
				psJava.println("\t\t\tswitch( cmdID )");
				psJava.println("\t\t\t{");
				
				for(Bean b : beans)
				{
					if( b.idipID != null )
					{
						psJava.println("\t\t\tcase " + b.idipID + ":");
						psJava.println("\t\t\t\t{");
						psJava.println("\t\t\t\t\t" + b.name + " obj = new " + b.name + "();");
						psJava.println("\t\t\t\t\tobj.decode(is);");
						psJava.println("\t\t\t\t\treturn obj;");
						psJava.println("\t\t\t\t}");
					}
				}
				
				psJava.println("\t\t\tdefault:");
				psJava.println("\t\t\t\tbreak;");
				psJava.println("\t\t\t}");
				psJava.println("\t\t}");
				psJava.println("\t\tcatch(Exception ex)");
				psJava.println("\t\t{");		
				psJava.println("\t\t}");
				psJava.println("\t\treturn null;");
				psJava.println("\t}");
			}
			
			// tailer
			psJava.println("}");
			
			//
			psJava.close();
			
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
		}
		
		private void genHandlers() throws Exception
		{
			
		}
		
		public void gen(File dirOut) throws Exception
		{
			this.dirOut = dirOut;
			genBeans();
		}
		
		private File fileSrc;
		private File dirOut;
		private JavaConfig javacfg = new JavaConfig();
		private List<MacroGroup> macroGroups = new ArrayList<MacroGroup>();
		private List<Bean> beans = new ArrayList<Bean>();
		private Bean beanIDIPHeader = null;
		private UniqueSet<String> beanNames = new UniqueSet<String>("bean name", false);
		private UniqueSet<String> macroGroupNames = new UniqueSet<String>("macro group name", true);
		private UniqueSet<String> macroNames = new UniqueSet<String>("macro name", false);
		private UniqueMap<String, RefPackage> umRefPackage = new UniqueMap<String, RefPackage>("ref package");
		private UniqueMap<String, RefBean> refbeans = new UniqueMap<String, RefBean>("ref bean");
		private Set<String> innertypesusedinbean = new HashSet<String>();
		private boolean bLstInBeans = false;
		private JavaGenerator javaGen = new JavaGenerator();
	}
	
	public void parse(String fnSrc, String fnOutDir, String packName, String beanName)
	{
		//
		File dirOut = new File(fnOutDir).getAbsoluteFile();
		if( ! FileTool.ensureDirectoryExist(dirOut) )
			throw new Error("bad output dir " + dirOut.getAbsolutePath());
		System.out.println("output dir is " + dirOut.getAbsolutePath());
		try
		{
			Module m = new Module(new File(fnSrc).getAbsoluteFile().getCanonicalFile());
			m.javacfg.basepackage = packName;
			m.javacfg.sbeanclassname = beanName;
			if( beanName.equals("TLog") )
				m.javacfg.bTLogToString = true;
			if( beanName.equals("IDIP") )
				m.javacfg.bIDIPEncode = true;
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
	
	private static class Macro
	{
		public String name;
		public String val;
		public String desc = "";
	}
	
	private static class MacroGroup
	{
		public String name;
		public String desc = "";
		public List<Macro> macros = new ArrayList<Macro>();
	}

	private static class Bean extends FieldsContainer
	{
		public String idipID;
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
	
	private static class RefBean
	{
		public RefPackage refpackage;
		public String name;
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
			UniqueSet<String> iecnames = new UniqueSet<String>("inner enum constant in " + container.name + "." + iename, false);
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
	
	public static class JavaConfig
	{
		public String basepackage = "i3k";
		public String rpcpackage = "rpc";
		public String sbeanclassname = "IDIP";
	
		public boolean bTLogToString = false;
		public boolean bIDIPEncode = false;
		
		public String updateSrcDir;
	}
	
	public static class JavaUpdateConfig
	{
		public String srcdir;
	}
	
	private Map<String, InnerType> innertypes = new HashMap<String, InnerType>();
	private String logo = "// modified by " + this.getClass().getName()+ " at " + new java.util.Date().toString() + ".";

	public static void main(String[] args)
	{
		ArgsMap am = new ArgsMap(args);
		String beanName = am.get("-beanName");
		if( beanName == null || beanName.equals("") )
		{
			new QQMetaGen().parse(am.get("-src", "qsg_tlog.xml"), am.get("-outdir", "rpcgenout")
					, am.get("-packName", "i3k")
					, "TLog"
					);
			new QQMetaGen().parse(am.get("-src", "qsg_idip.xml"), am.get("-outdir", "rpcgenout")
					, am.get("-packName", "i3k")
					, "IDIP"
					);
		}
		else
		{
			new QQMetaGen().parse(am.get("-src", "qsg_idip.xml"), am.get("-outdir", "rpcgenout")
				, am.get("-packName", "i3k")
				, beanName
				);
		}
	}

}
