package testJavaServer;

import java.io.*;
import java.net.*;

public class testServer {

	
	public void ServerReceviedByTcp() {  
	    
	    ServerSocket serverSocket = null;  
	    try {  
	        
	        serverSocket = new ServerSocket(1986);
	        System.out.print("server ready");  
	        
	        Socket socket = serverSocket.accept();  
	        
	        InputStream inputStream = socket.getInputStream();  
	        byte buffer[] = new byte[1024 * 4];  
	        int temp = 0;  
	        while ((temp = inputStream.read(buffer)) != -1) {  
	            System.out.println(new String(buffer, 0, temp));  
	        }  
	        serverSocket.close();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	} 
	
	
	public static void main(String[] args) {
		
		testServer t = new testServer();
		t.ServerReceviedByTcp();
		
	}

}



