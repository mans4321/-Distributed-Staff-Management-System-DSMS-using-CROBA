package udp;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import Center.Message;
import Center.MessagesCenter;
import Center.MessageTransport;

public class ServerOperationMessage extends Thread{

	private int port;
	private MessagesCenter messageCenter;
	private MessageTransport sendMessage;
	private boolean isFrontEnd;
	
	public ServerOperationMessage(int port , MessagesCenter messageCenter){
		this.port = port;
		this.messageCenter = messageCenter;
		isFrontEnd = false;
	}
	
	/**
	 * this only used by FE to receive lost messages request 
	 * @param port
	 */
	public ServerOperationMessage(int port , MessageTransport sendMessage ){
		this.port = port;
		this.sendMessage = sendMessage;
		isFrontEnd = true;
	}
	public void run(){
		   DatagramSocket socket;
		      try
		      {
		    	  System.out.println(port);
		    	 socket = new DatagramSocket(port);  
		         ObjectInputStream is;
		         ByteArrayInputStream in;
		         byte[] incomingData = new byte[1024];
		         while (true)
		         {
		             DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
		             socket.receive(incomingPacket);
		             byte[] data = incomingPacket.getData();
		             in = new ByteArrayInputStream(data);
		             is = new ObjectInputStream(in);
		             Message message = (Message) is.readObject();
		             message.setSenderPort(incomingPacket.getPort());
		             if(! isFrontEnd ){
		            	 messageCenter.recieive(message); 
		             }else{
		            	 sendMessage.recieive(message);
		             }
		         }
		      }catch(Exception e){
		    	  e.printStackTrace(System.out);
		      	}
		}
}