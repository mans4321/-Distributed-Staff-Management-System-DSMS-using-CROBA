package udp.MessageExchange;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import FIFOsubsystem.Message;
import FIFOsubsystem.ReliableFIFO;
import FIFOsubsystem.SendMessage;

public class WaitDoOperationMessage extends Thread{

	private int port;
	private ReliableFIFO reliableFIFO;
	private SendMessage sendMessage;
	private boolean isFrontEnd;
	
	public WaitDoOperationMessage(int port , ReliableFIFO reliableFIFO){
		this.port = port;
		this.reliableFIFO = reliableFIFO;
		isFrontEnd = false;
	}
	
	/**
	 * this only used by FE to receive lost messages request 
	 * @param port
	 */
	public WaitDoOperationMessage(int port , SendMessage sendMessage ){
		this.port = port;
		this.sendMessage = sendMessage;
		isFrontEnd = true;
	}
	public void run(){
		   DatagramSocket socket;
		      try
		      {
		    	 socket = new DatagramSocket(port);  
		         ObjectInputStream is;
		         ByteArrayInputStream in;
		         byte[] incomingData = new byte[1024];
		         while (true)
		         {
		        	 System.out.println("waiting for messages");
		             DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
		             socket.receive(incomingPacket);
		             byte[] data = incomingPacket.getData();
		             in = new ByteArrayInputStream(data);
		             is = new ObjectInputStream(in);
		             Message message = (Message) is.readObject();
		             message.setSenderPort(incomingPacket.getPort());
		             if(! isFrontEnd ){
		             reliableFIFO.recieive(message); 
		             }else{
		            	 sendMessage.sendLostMessages(message);
		             }
		         }
		      }catch(Exception e){
		    	  e.printStackTrace(System.out);
		      	}
		}
}