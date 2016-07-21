package udp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

import Center.Message;

public class SendDoOperationMessage extends Thread {

	private int port;
	private Message message;
	private String resultResponse;
	public SendDoOperationMessage(int port , Message message){
		this.port =port;
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
        DatagramPacket sendObject = new DatagramPacket(data, data.length, IPAddress, port);
        socket.send(sendObject);
        outputStream.reset();
        os.reset();
        
        System.out.println("message has sent");
        socket.setSoTimeout(1000);
        byte[] incomingResultData = new byte[1024];
        DatagramPacket incomingResultPacket = new DatagramPacket(incomingResultData, incomingResultData.length);
        socket.receive(incomingResultPacket);
        resultResponse = new String(incomingResultPacket.getData());
        System.out.println("message has reciev " + resultResponse);
        socket.close();
        }catch(SocketTimeoutException e ){
        	if(socket != null)
        	socket.close();
        	resultResponse = "noResponse";
        }catch(IOException e){
        	if(socket != null)
        	socket.close();
        	e.printStackTrace();
        }
		
	}


	public String getResultResponse() {
		return resultResponse;
	}
	
	
	
}


