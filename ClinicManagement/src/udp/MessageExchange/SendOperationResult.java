package udp.MessageExchange;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import FIFOsubsystem.Message;

public class SendOperationResult extends Thread {

	private Message message;
	public SendOperationResult(Message message){
		this.message = message;
	}
	
	public void run(){
		DatagramSocket socket = null;
		try{
		socket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] data = message.getResponse().getBytes();
        DatagramPacket sendObject = new DatagramPacket(data, data.length, IPAddress, message.getSenderPort());
        socket.send(sendObject);
        socket.close();
		}catch(Exception e){
			if(socket != null)
			e.printStackTrace(System.out);
		}
	}
}