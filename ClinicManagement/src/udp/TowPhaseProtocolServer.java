package udp;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import servers.records.Record;
import servers.records.RecordManager;

public class TowPhaseProtocolServer extends Thread {

	private DatagramSocket socket;
	
	private int port;
	private RecordManager database;
	
	public TowPhaseProtocolServer(int port, RecordManager database){
		this.port = port;
		this.database = database;	
	}
	
	public void run(){
		DatagramPacket incomingVote ;
		try{
			socket = new DatagramSocket(port);
				while(true){	
					
		            byte[] incomingRecordData = new byte[1024];
		            DatagramPacket incomingRecordPacket = new DatagramPacket(incomingRecordData, incomingRecordData.length);
	                socket.receive(incomingRecordPacket);
	                
	                byte[] data = incomingRecordPacket.getData();
	                ByteArrayInputStream in = new ByteArrayInputStream(data);
	                ObjectInputStream object = new ObjectInputStream(in);
	                Record transferedRecord = (Record) object.readObject();
	               
	                byte[] sendCommitResult = new byte[1024];
		            DatagramPacket sendCommitResultPacket ;
	                if(database.addTransferedRecord(transferedRecord)){
	                	sendCommitResult = "done".getBytes();
	                	sendCommitResultPacket = new DatagramPacket(sendCommitResult, sendCommitResult.length,incomingRecordPacket.getAddress(), incomingRecordPacket.getPort());
	                	socket.send(sendCommitResultPacket);
	                	System.out.println("transferd has been added");
	                	System.out.println(database.getRecordCounts());
	                }else{
	                	sendCommitResult = "notDone".getBytes();
	                	sendCommitResultPacket = new DatagramPacket(sendCommitResult, sendCommitResult.length,incomingRecordPacket.getAddress(), incomingRecordPacket.getPort() );
	                	socket.send(sendCommitResultPacket);
	                }
				}
			
		}catch(Exception e){
			e.printStackTrace(System.out);
		}
	}
}
