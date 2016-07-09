package udp.changeLeader;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class NewLeader extends Thread {

	private int serverPort;
	private int newLeaderPort;
	public NewLeader(int serverPort, int newLeaderPort){
		this.serverPort = serverPort;
		this.newLeaderPort = newLeaderPort;
	}
	
	public void run(){
		
	try{
	DatagramSocket clientSocket = new DatagramSocket();
    InetAddress IPAddress = InetAddress.getByName("localhost");
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];
    String aString = Integer.toString(newLeaderPort);
    sendData = aString.getBytes();
   
    clientSocket.setSoTimeout(1000);
    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort );
    clientSocket.send(sendPacket);
    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    clientSocket.receive(receivePacket);
    
	}catch(SocketTimeoutException e){
		new NewLeader(serverPort,newLeaderPort);
	}
	catch(Exception e ){
		e.printStackTrace(System.out);
	}
	}
}