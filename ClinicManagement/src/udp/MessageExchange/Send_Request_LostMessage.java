package udp.MessageExchange;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import FIFOsubsystem.Message;

public class Send_Request_LostMessage extends Thread {

	Message message ;
	
	public Send_Request_LostMessage(Message message){
		this.message = message;
	}
	
	public void run(){
		DatagramSocket socket = null;
		try{
		socket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outputStream);
        os.writeObject(message);
        os.flush();
        byte[] data = outputStream.toByteArray();
        DatagramPacket sendObject = new DatagramPacket(data, data.length, IPAddress, message.getSenderPort());
        socket.send(sendObject);
        outputStream.reset();
        os.reset();
        socket.close();
		}catch(Exception e){
			if(socket !=null)
				socket.close();
			e.printStackTrace(System.out);
		}
	}
}
