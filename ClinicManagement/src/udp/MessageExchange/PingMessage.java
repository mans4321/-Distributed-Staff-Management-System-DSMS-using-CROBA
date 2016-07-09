package udp.MessageExchange;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class PingMessage extends Thread {
	
	int port;
	String respones ;
	public PingMessage(int port){
		this.port = port;
	}
	
	public void run(){
		DatagramSocket clientSocket = null;
		try{
		 clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName("localhost");
	      
	      byte[] sendData = new byte[1024];
	      byte[] receiveData = new byte[1024];
	      String sentence = "ping";
	      sendData = sentence.getBytes();
	      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port );
	      clientSocket.send(sendPacket);
	      
	      clientSocket.setSoTimeout(1500);
	      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	      clientSocket.receive(receivePacket);
	       respones = new String(receivePacket.getData());
	      clientSocket.close();
		}catch(SocketTimeoutException  e){
			respones = "noResponse";
			if(clientSocket != null)
				clientSocket.close();
		}catch(IOException e){
			if(clientSocket != null)
				clientSocket.close();
			e.printStackTrace(System.out);
		}
	}

	public String getRespones() {
		return respones;
	}
	
	
}
