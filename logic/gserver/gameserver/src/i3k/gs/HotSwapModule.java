
package i3k.gs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ket.util.Stream;
import ket.util.Stream.AIStream;
import ket.util.Stream.AOStream;
import ket.util.Stream.DecodeException;
import ket.util.Stream.EOFException;

public class HotSwapModule
{
	
	public static final int STUB_ROLE_TIMER_CHECK = 0;
	public static final int STUB_ROLE_LOAD_CHECK = 1;
	public static final int STUB_ROLE_LUA_PACKET_CHECK = 2;
	public static final int STUB_TIMER_CHECK = 3;
	public static final int STUB_COUNT = 4;
	
	public HotSwapModule(GameServer gs)
	{
		this.gs = gs;
	}
	
	public static class RoleTimerPreChecker
	{
		public static int check(GameServer gs, Role role)
		{
			// TODO
			Field field;
			try {
				field = Role.class.getDeclaredField("money");
				field.setAccessible(true);
				try {
					field.set(role, 33);
					gs.getLogger().warn("set role lvl = 33");
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gs.getLogger().warn("RoleTimerPreChecker.check(role): v2");
			return 0;
		}

	}
	
	public static class RoleLoadPreChecker
	{
		public static int check(GameServer gs, Role role, Integer where)
		{
			//
			gs.cfg.godMode = 0;
			// TODO
			Field field;
			try {
				field = Role.class.getDeclaredField("lvl");
				field.setAccessible(true);
				try {
					field.set(role, (short)31);
					gs.getLogger().warn("set role lvl = 31");
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gs.getLogger().warn("RoleLoadPreChecker.check(role): v1");
			return 0;
		}

	}
	
	public static class RoleLuaPacketPreChecker
	{
		public static int check(GameServer gs, Role role, String[] data)
		{
			// TODO
			gs.getLogger().warn("RoleLuaPacketPreChecker.check(role): v0");
			if( data[0].equals("pay") )
				return 1;
			return 0;
		}

	}
	
	public static class TimerChecker
	{
		public static int check(GameServer gs, GameData gameData)
		{
			// TODO
			gs.getLogger().warn("TimerChecker.check(role): v0");
			return 0;
		}

	}
	
	public static class Jar implements Stream.IStreamable
	{
		
		final int VERSION_NOW = 2;

		public static class JarEntry implements Stream.IStreamable
		{
			@Override
			public void encode(AOStream os)
			{
				os.pushString(className);
				os.pushInteger(classData.length);
				os.pushBytes(classData);
			}

			@Override
			public void decode(AIStream is) throws EOFException, DecodeException
			{
				className = is.popString();
				int len = is.popInteger();
				classData = is.popBytes(len);
			}

			public String className;
			public byte[] classData;
		}
		
		@Override
		public void encode(AOStream os)
		{
			os.pushInteger(VERSION_NOW);
			os.pushInteger(map.size());
			for(JarEntry e : map.values())
			{
				os.push(e);
			}
			os.pushStringList(lua);
		}

		@Override
		public void decode(AIStream is) throws EOFException, DecodeException
		{
			int ver = is.popInteger();
			int n = is.popInteger();
			map.clear();
			for(int i = 0; i < n; ++i)
			{
				JarEntry e = new JarEntry();
				is.pop(e);
				map.put(e.className, e);
			}
			if( ver > 1 )
				lua = is.popStringList();
		}
		
		public byte[] getData(String className)
		{
			JarEntry e = map.get(className);
			return e == null ? null : e.classData;
		}
		
		public void addEntry(JarEntry e)
		{
			map.put(e.className, e);
		}
		
		Map<String, JarEntry> map = new HashMap<String, JarEntry>();
		List<String> lua = new ArrayList<String>();
	}
	
	public class CheckerStub
	{
		public CheckerStub(int index, String className)
		{
			this.index = index;
			this.className = className;
			this.cls = null;
			this.mtd = null;
		}
		
		public synchronized void load()
		{
			try
			{
				cls = cl.loadClass(className);
				switch( index )
				{
				case HotSwapModule.STUB_ROLE_TIMER_CHECK:
					{
						mtd = cls.getMethod("check", GameServer.class, Role.class); 
						mtd.setAccessible(true);
					}
					break;
				case HotSwapModule.STUB_ROLE_LOAD_CHECK:
					{
						mtd = cls.getMethod("check", GameServer.class, Role.class, Integer.class); 
						mtd.setAccessible(true);
					}
					break;
				case HotSwapModule.STUB_ROLE_LUA_PACKET_CHECK:
					{
						mtd = cls.getMethod("check", GameServer.class, Role.class, String[].class); // TODO 
						mtd.setAccessible(true);
					}
					break;
				case HotSwapModule.STUB_TIMER_CHECK:
					{
						mtd = cls.getMethod("check", GameServer.class, GameData.class); // TODO 
						mtd.setAccessible(true);
					}
					break;
				default:
					break;
				}
			}
			catch(ClassNotFoundException ex)
			{
				cls = null;
				mtd = null;
			}
			catch(Exception ex)
			{
				gs.getLogger().warn("patch class error", ex);
				cls = null;
				mtd = null;
			}
		}
		
		final int index;
		final String className;
		Class<?> cls;
		Method mtd;
	}
	
	class HowswapClassLoader extends ClassLoader { 


		public HowswapClassLoader() throws IOException { 
			super(null); // 指定父类加载器为 null
			for(String name : classNames)
			{ 
				byte[] raw = jarData.getData(name);
				if( raw != null )
					defineClass(name, raw, 0, raw.length);
			} 
		}
		
		@Override
		protected Class<?> loadClass(String name, boolean resolve) 
				throws ClassNotFoundException { 
			Class<?> cls = null; 
			cls = findLoadedClass(name); 
			if(! classNames.contains(name) && cls == null) 
				cls = getSystemClassLoader().loadClass(name); 
			if (cls == null) 
				throw new ClassNotFoundException(name); 
			if (resolve) 
				resolveClass(cls); 
			return cls; 
		} 

	}
	
	public boolean init(String jarFileName)
	{
		for(int i = 0; i < STUB_COUNT; ++i)
		{
			String clsName = null;
			switch( i )
			{
			case STUB_ROLE_TIMER_CHECK:
				clsName = "i3k.gs.HotSwapModule$RoleTimerPreChecker";
				break;
			case STUB_ROLE_LOAD_CHECK:
				clsName = "i3k.gs.HotSwapModule$RoleLoadPreChecker";
				break;
			case STUB_ROLE_LUA_PACKET_CHECK:
				clsName = "i3k.gs.HotSwapModule$RoleLuaPacketPreChecker";
				break;
			case STUB_TIMER_CHECK:
				clsName = "i3k.gs.HotSwapModule$TimerChecker";
				break;
			default:
				throw new Error("impossible");
			}
			classNames.add(clsName);
			stubs[i] = new CheckerStub(i, clsName); 
		}
		
		this.jarFileName = jarFileName;
		loadJarData();
		
		try {
			cl = new HowswapClassLoader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cl = null;
		}
		for(CheckerStub stub : stubs)
		{
			stub.load();
		}
		
		return true;
	}
	
	public int checkRoleBeforeTimer(GameServer gs, Role role)
	{
		Class<?> stub = null;
		Method mtd = null;
		CheckerStub stubC = stubs[STUB_ROLE_TIMER_CHECK];
		synchronized( stubC )
		{
			stub = stubC.cls;
			mtd = stubC.mtd;
		}
		if( stub == null || mtd == null )
			return 0;
		 //Method m = cls.getMethod("checkString", String.class);
		try {
			return ((Integer)mtd.invoke(null, gs, role)).intValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int checkTimer(GameServer gs, GameData gameData)
	{
		Class<?> stub = null;
		Method mtd = null;
		CheckerStub stubC = stubs[STUB_TIMER_CHECK];
		synchronized( stubC )
		{
			stub = stubC.cls;
			mtd = stubC.mtd;
		}
		if( stub == null || mtd == null )
			return 0;
		 //Method m = cls.getMethod("checkString", String.class);
		try {
			return ((Integer)mtd.invoke(null, gs, gameData)).intValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int checkRoleBeforeLuaPacket(GameServer gs, Role role, String[] data)
	{
		Class<?> stub = null;
		Method mtd = null;
		CheckerStub stubC = stubs[STUB_ROLE_LUA_PACKET_CHECK];
		synchronized( stubC )
		{
			stub = stubC.cls;
			mtd = stubC.mtd;
		}
		if( stub == null || mtd == null )
			return 0;
		 //Method m = cls.getMethod("checkString", String.class);
		try {
			return ((Integer)mtd.invoke(null, gs, role, data)).intValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<String> getLuaPatch()
	{
		return luaPatch;
	}
	
	public Map<String, String> getStateMap()
	{
		return stateMap;
	}
	
	public int checkRoleBeforeLoad(GameServer gs, Role role, Integer where)
	{
		Class<?> stub = null;
		Method mtd = null;
		CheckerStub stubC = stubs[STUB_ROLE_LOAD_CHECK]; 
		synchronized( stubC )
		{
			stub = stubC.cls;
			mtd = stubC.mtd;
		}
		if( stub == null || mtd == null )
			return 0;
		 //Method m = cls.getMethod("checkString", String.class);
		try {
			return ((Integer)mtd.invoke(null, gs, role, new Integer(where))).intValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	private void loadJarData()
	{
		try {
			Stream.loadObjLE(jarData, new FileInputStream(jarFileName));			
			luaPatch = jarData.lua;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reload()
	{
		gs.getLogger().warn("hot swap module do reload");
		loadJarData();
		HowswapClassLoader clNew;
		try {
			clNew = new HowswapClassLoader();
			cl = clNew;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(CheckerStub stub : stubs)
		{
			synchronized( stub )
			{
				CheckerStub stubNew = new CheckerStub(stub.index, stub.className);
				stubNew.load();
				stubs[stubNew.index] = stubNew;
			}
		}
	}
	
	public void onTimer()
	{
		if( jarFileName == null )
			return;
		// TODO
		if( checkTimer(gs, GameData.getInstance()) != 0 )
			gs.getLogger().warn("hotmodule timerchecker failed");
	}
	
	public static void main(String[] args)
	{
		Jar jarData = new Jar();
		try {
			/*
			{
				Jar.JarEntry e = new Jar.JarEntry();
				e.className = "i3k.gs.HotSwapModule$RoleTimerPreChecker";
				File f = new File("bin\\i3k\\gs\\HotSwapModule$RoleTimerPreChecker.class");
				FileInputStream fis = new FileInputStream(f);
				e.classData = new byte[(int)f.length()];
				fis.read(e.classData);
				fis.close();
				jarData.addEntry(e);
			}
			{
				Jar.JarEntry e = new Jar.JarEntry();
				e.className = "i3k.gs.HotSwapModule$RoleLoadPreChecker";
				File f = new File("bin\\i3k\\gs\\HotSwapModule$RoleLoadPreChecker.class");
				FileInputStream fis = new FileInputStream(f);
				e.classData = new byte[(int)f.length()];
				fis.read(e.classData);
				fis.close();
				jarData.addEntry(e);
			}
			{
				Jar.JarEntry e = new Jar.JarEntry();
				e.className = "i3k.gs.HotSwapModule$RoleLuaPacketPreChecker";
				File f = new File("bin\\i3k\\gs\\HotSwapModule$RoleLuaPacketPreChecker.class");
				FileInputStream fis = new FileInputStream(f);
				e.classData = new byte[(int)f.length()];
				fis.read(e.classData);
				fis.close();
				jarData.addEntry(e);
			}
			*/
			{
				Jar.JarEntry e = new Jar.JarEntry();
				e.className = "i3k.gs.HotSwapModule$TimerChecker";
				File f = new File("bin\\i3k\\gs\\HotSwapModule$TimerChecker.class");
				FileInputStream fis = new FileInputStream(f);
				e.classData = new byte[(int)f.length()];
				fis.read(e.classData);
				fis.close();
				jarData.addEntry(e);
			}
			String luaTest = "i3k_setTipMessage(\"test!!!!!!!\");g_MAX_ROLE_LEVEL = 30";
			jarData.lua.add(luaTest);
			Stream.storeObjLE(jarData, new File("patch.dat"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private GameServer gs;
	private HowswapClassLoader cl = null;
	private CheckerStub[] stubs = new CheckerStub[STUB_COUNT];
	private String jarFileName = null;
	private Set<String> classNames = new HashSet<String>();
	private Jar jarData = new Jar();
	private Map<String, String> stateMap = new HashMap<String, String>();
	private List<String> luaPatch = new ArrayList<String>();
}
