package testJavaClient;

import java.net.*;
import java.io.*;

class SimpleClient
{
	public static void main(String args[])
	{
		Socket client = null;
		InputStream in = null;
		OutputStream out = null;
		byte[] buf = new byte[100];
		
			try
			{
			    // Creates a stream socket and connects it to the specified port number 
			    // at the specified IP address.
				
					client = new Socket("192.168.137.220", 1989);
					// Send message to server
					out = client.getOutputStream();
					String data = "Client hello!!";
					String data1 = "123";
					out.write(data.getBytes());
					out.write(data1.getBytes());
					// Read message from server
					in = client.getInputStream();
					in.read(buf);
					System.out.println("Receive message: " + new String(buf));

					out.close();
					in.close();

				client.close();
			}
			catch(UnknownHostException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		
	}
}
