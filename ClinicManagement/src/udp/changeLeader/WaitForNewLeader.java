package udp.changeLeader;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import FIFOsubsystem.SendMessage;

public class WaitForNewLeader extends Thread {

	private SendMessage sendMessage;
	private int serverPort;
	public WaitForNewLeader (int serverPort, SendMessage sendMessage){
		this.serverPort = serverPort;
		this.sendMessage = sendMessage;
	}
	
	public void run(){
		
		    DatagramSocket serverSocket = null;
	        DatagramPacket receivePacket;
	        DatagramPacket sendPacket;
	        try {   
	                serverSocket = new DatagramSocket(serverPort);
	                byte[] receiveData = new byte[1024];
	                byte[] sendData = new byte[1024];
	                while(true) {
	                      receivePacket = new DatagramPacket(receiveData, receiveData.length);
	                      serverSocket.receive(receivePacket);
	    			      String recordCount = new String(receivePacket.getData());
	    			      int  port = Integer.parseInt(recordCount.trim());
	    			      sendMessage.setManagerPort(port);
	    			      sendData = "done".getBytes();
	    			      sendPacket = new DatagramPacket(sendData, sendData.length,receivePacket.getAddress() , receivePacket.getPort() );
	    			      serverSocket.send(sendPacket);
	    			      
	              }
	
	        }catch (Exception e){
	        	if(serverSocket != null){
	        		serverSocket.close();
	        	}
	        	e.printStackTrace(System.out);
	        }
	}
}
