package udp;

import java.net.*;
import java.util.concurrent.Callable;
import java.io.*;

public class UDPClient  implements Callable<Integer>{
    
	
		private int serverPort ;
		public UDPClient(int serverPort){
			this.serverPort = serverPort;
		}
		@Override
		public Integer call() throws Exception {
			try{
			      DatagramSocket clientSocket = new DatagramSocket();
			      InetAddress IPAddress = InetAddress.getByName("localhost");
			      byte[] sendData = new byte[1024];
			      byte[] receiveData = new byte[1024];
			      String sentence = "getCount";
			      sendData = sentence.getBytes();
			      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort );
			      clientSocket.send(sendPacket);
			      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			      clientSocket.receive(receivePacket);
			      String recordCount = new String(receivePacket.getData());
			      clientSocket.close();
			      int  count = Integer.parseInt(recordCount.trim());
			      return count;
				}catch(IOException e){
				return -1;
				}
		}
		}

	
   
    
