package udp.MessageExchange;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PingListener extends Thread {

	private int serverPort;
	
	public PingListener(int serverPort){
		this.serverPort = serverPort;
	}
	
	public void run(){
        DatagramSocket serverSocket = null;
        DatagramPacket receivePacket;
        DatagramPacket sendPacket;
        
		try{
				serverSocket = new DatagramSocket(serverPort);
				byte[] receiveData = new byte[1024];	
				byte[] sendData = new byte[1024];
				while(true) {
					receivePacket = new DatagramPacket(receiveData, receiveData.length);
					serverSocket.receive(receivePacket);
					String sentence = new String( receivePacket.getData());
					System.out.println("RECEIVED: " + sentence);
					
					InetAddress IPAddress = receivePacket.getAddress();
					int port = receivePacket.getPort();  
					sendData = "ping".getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					serverSocket.send(sendPacket);
				}
		}catch(Exception e){
    	e.printStackTrace(System.out);
		}
	}
}