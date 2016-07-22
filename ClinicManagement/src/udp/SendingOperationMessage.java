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

public class SendingOperationMessage extends Thread {

	private int port;
	private Message message;
	private String resultResponse;
	private int timeOut;
	
	//#TODO
	// time out 
	
	public SendingOperationMessage(int port , Message message){
		this.port =port;
		this.message = message;
		this.timeOut = 3000;
		
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
        
        
        socket.setSoTimeout(timeOut);
        byte[] incomingResultData = new byte[1024];
        DatagramPacket incomingResultPacket = new DatagramPacket(incomingResultData, incomingResultData.length);
        socket.receive(incomingResultPacket);
        resultResponse = new String(incomingResultPacket.getData());
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


	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}


	public String getResultResponse() {
		return resultResponse;
	}
	
	
	
}


