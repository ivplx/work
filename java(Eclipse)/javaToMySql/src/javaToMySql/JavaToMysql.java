package javaToMySql;
import java.sql.*;

class InitSql{
	 
	 
	
	 void sqlRead(String sqlreadCMD,Statement stmt,String sqlTableTargetName) throws SQLException{
		 
			sqlreadCMD = "SELECT * FROM valuetable";
		      ResultSet rs = stmt.executeQuery(sqlreadCMD);
		      while(rs.next()){   
		         int val  = rs.getInt(sqlTableTargetName);
		         System.out.print("sqlread : " + val);
		      }
		      rs.close();
		}
		
		void sqlWrite(String sqlwriteCMD,Connection conn) throws SQLException{
			sqlwriteCMD = "INSERT INTO valuetable (value) VALUES (8)";
			
		    PreparedStatement ppstmt = conn.prepareStatement(sqlwriteCMD);
		    
		    ppstmt.executeUpdate();
		    ppstmt.close();
		}
}


public class JavaToMysql{
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://192.168.137.220:3306/arduinotosqldb";
	static final String USER = "username";
	static final String PASS = "000000";
	static String sqlwriteCMD = "INSERT INTO tablename (columnname1,columnname2,..) VALUES (1,1,..)";
	static String sqlreadCMD = "SELECT * FROM tablename WHERE 1";
	static String sqlTableTargetName = "value";
	Connection conn = null;
	static Statement stmt = null;
	
	
	public static void main(String[] args) throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
		try{
			Class.forName(JDBC_DRIVER);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
		InitSql initSql = new InitSql();
		initSql.sqlRead(sqlreadCMD, stmt, sqlTableTargetName);
		initSql.sqlWrite(sqlwriteCMD, conn);
		
		//也可以不用以物件方式使用
	}
}
