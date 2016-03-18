package com.joypiegame.gameservice.db;



public class DBExecutor {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private String dbName;
    static {
        registerDriver();
    }
    public DBExecutor() {
    }

    private static void registerDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("register database driver success");
        } catch (ClassNotFoundException e) {
            System.out.println("Caught " + e);
            e.printStackTrace();
            System.out.println("register database driver failed");
        }
    }

    public void setDbUrl(String ip, String port)
    {
    	dbUrl = "jdbc:mysql://" + ip + ":" + port + "/";
    	System.out.println("Set database url");
    }
    
    public void setDbUser(String userName, String password)
    {
    	dbUser = "user=" + userName + (password.isEmpty() ? "" : "&password=" + password);
    }
    
    public void setDbName(String name)
    {
    	dbName = name;
    }
    
    public java.sql.Connection getDbConnection()
    {
    	return getDbConnection(dbName);
    }
    
    private String getDbConnectionUrl(String dbName)
    {
    	return dbUrl + dbName + "?" + dbUser + "&useUnicode=true&characterEncoding=UTF8";
    }

    public java.sql.Connection getDbConnection(String dbName) {
        if (dbUrl != null && dbUser != null)
            return connect(getDbConnectionUrl(dbName));
        return null;
    }
    
    public static java.sql.Connection connect(String dburl) {
        java.sql.Connection con = null;
        try {
            con = java.sql.DriverManager.getConnection(dburl);
        } catch (Exception e) {
        	System.out.println("Caught " + e);
            e.printStackTrace(System.out);
        } finally {
            //System.out.println("Connect database (" + dburl + ") " + (con != null ? " success" : " failed"));
        }
        return con;
    }

    public static void closeConnection(java.sql.Connection con) {
        try {
            con.close();
            //System.out.println("close database connection success");
        } catch (Exception e) {
        	System.out.println("Caught " + e);
            e.printStackTrace(System.out);
            System.out.println("close database connection failed");
        }
    }

    public static java.sql.CallableStatement createPrepareCall(java.sql.Connection con, String call) {
    	java.sql.CallableStatement cstm = null;
    	try {
    		cstm = con.prepareCall(call);
    		//System.out.println("create CallableStatement success");
    	} catch (Exception e) {
    		System.out.println("Caught " + e);
    		e.printStackTrace(System.out);
    	}
    	return cstm;
    }
    
    public static int executeUpdate(java.sql.CallableStatement cstm) {
    	int result = -1;
    	try {
    		result = cstm.executeUpdate();
    		//System.out.println("executeUpdate success");
    	} catch (Exception e) {
    		System.out.println("Caught " + e);
    		e.printStackTrace();
    	}
    	return result;
    }
    
    public static void closeCallableStatement(java.sql.CallableStatement cstm) {
    	try {
    		cstm.close();
    		//System.out.println("close CallableStatement success");
    	} catch (Exception e) {
    		System.out.println("Caught " + e);
    		e.printStackTrace(System.out);
    	}
    }
    
    public static int executeUpdate(java.sql.Connection con, String sql) {
        java.sql.Statement stm = null;
        int result = -1;
        try {
            stm = con.createStatement();
            result = stm.executeUpdate(sql);
            //System.out.println("execute update (" + sql + ") success : result = " + result);
        } catch (Exception e) {
            System.out.println("Caught " + e);
            //printStackTrace(System.out);
            System.out.println("execute update (" + sql + ") failed");
        } finally {
        	try {
                if (stm != null) {
                    stm.close();
                    //System.out.println("close statement success");
                }
            } catch (Exception e) {
                System.out.println("Caught " + e);
                //e.printStackTrace(System.out);
                //System.out.println("close executeUpdate (" + sql + ") statement failed");
            }
        }
        return result;
    }

    public static void main(String[] args) {
//        java.sql.Connection con = connectDb("localhost", "3306", "root", "zhangfeiwoyao", "crashcourse");
//        if (con != null) {
//            try {
//                executeQuery(con, "show tables", null);
//                //java.sql.CallableStatement cs = con.prepareCall("{call productpricing(?, ?, ?)}");
//                //cs.registerOutParameter(1, int
//            } catch (Exception e) {
//            } finally {
//                closeConnection(con);
//            }
//        }
    }
}




