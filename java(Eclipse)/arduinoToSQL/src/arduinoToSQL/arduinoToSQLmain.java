package arduinoToSQL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;



//arduinoConnection
    //SerialPort
class serialPortEvent implements SerialPortEventListener{
	String arduinoPort = "COM3";
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 115200;
	
	

    SerialPort serialPort;
    private BufferedReader input;
    
   
    public void initialize(){
    	CommPortIdentifier portID = null;
    	CommPortIdentifier currPortID;
    	Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
    	while(portEnum.hasMoreElements()){
    		currPortID = (CommPortIdentifier) portEnum.nextElement();
    		if(currPortID.getName().equals(arduinoPort)){
    			portID = currPortID;
    			break;
    		}
    	}
    	
    	
    	if(portID == null){
    		System.out.println("cant find comport");
    	}
    	
    	try{
    		serialPort = (SerialPort) portID.open(this.getClass().getName(),TIME_OUT);
    		serialPort.setSerialPortParams(DATA_RATE, serialPort.DATABITS_8,serialPort.STOPBITS_1,serialPort.PARITY_NONE);
    		input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
    		serialPort.addEventListener(this);
    		serialPort.notifyOnDataAvailable(true);
    	}catch(Exception e){
    		System.err.println(e.toString());
    	}
    	
    }
    
	@Override
	public void serialEvent(SerialPortEvent oEvent) {
		if(oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE){
			
			//SQL create write connect arduino to database(under)
			
			
			try {
				Connection conn = DriverManager.getConnection(arduinoToSQLmain.DB_URL,arduinoToSQLmain.USER,arduinoToSQLmain.PASS);
				String inputLine = input.readLine();
				System.out.println("inputLinefromarduino:" + inputLine);
				sqlFunction sqlWRITE = new sqlFunction();
			    sqlWRITE.sqlWrite("INSERT INTO valuetable (value) VALUES ("+ inputLine +")", conn);
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
			//SQL create write connect arduino to database(above)
			
		}
		
	}
	
}

class sqlFunction{
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
		//sqlwriteCMD = "INSERT INTO valuetable (value) VALUES (8)";
	    PreparedStatement ppstmt = conn.prepareStatement(sqlwriteCMD);
	    ppstmt.executeUpdate();
	    ppstmt.close();
	}
	
}

public class arduinoToSQLmain{
  
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://192.168.137.220:3306/arduinotosqldb";
   static final String USER = "user";
   static final String PASS = "721207";
   static String sqlwriteCMD;
   static String sqlreadCMD;
   static String sqlTableTargetName = "value";
   
   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   
   
   //portinitialize
   serialPortEvent initport = new serialPortEvent();
   initport.initialize();
   
  
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	
   
   
   
//try{
      //Class.forName("com.mysql.jdbc.Driver");

      //System.out.println("Connecting to database...");
      
      //create connection(must)
      //conn = DriverManager.getConnection(DB_URL,USER,PASS);
      //stmt = conn.createStatement();
      
        
      //read
      
      //sqlFunction sqlREAD = new sqlFunction();
      //sqlREAD.sqlRead(sqlreadCMD, stmt,sqlTableTargetName);
      

      //write
      
      //sqlFunction sqlWRITE = new sqlFunction();
      //sqlWRITE.sqlWrite(sqlwriteCMD, conn);
      

      //stmt.close();
      //conn.close();
      
   //}catch(SQLException se){
      
      //se.printStackTrace();
   //}catch(Exception e){
      
      //e.printStackTrace();
      
   //}
   
   //finally{
      
      //try{
         //if(stmt!=null)
            //stmt.close();
      //}catch(SQLException se2){
      //}
      
      
      //try{
         //if(conn!=null)
            //conn.close();
      //}catch(SQLException se){
         //se.printStackTrace();
      //}
   //}
   	System.out.println("Goodbye!");
   }
}