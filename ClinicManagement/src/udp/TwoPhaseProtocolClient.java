package udp;


import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import servers.records.Record;
import servers.records.RecordManager;
import utilities.DealWithUndeletedRecord;

public class TwoPhaseProtocolClient extends Thread {
	
	  private DatagramSocket socket;
	  private RecordManager database;
	  private int port;
	  private Record record;
	  private boolean result;
	  private String status;
	  private  DealWithUndeletedRecord backupRecord;
	public TwoPhaseProtocolClient(int port , Record record,RecordManager database){
		
		this.port =port ;
		this.database = database;
		this.record = record; 
		this.result = false;
		this.backupRecord = new DealWithUndeletedRecord(record); ;
	}
	
	public void run(){
		
			
			try {
				socket = new DatagramSocket();
				InetAddress IPAddress = InetAddress.getByName("localhost");
				
	            	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		            ObjectOutputStream os = new ObjectOutputStream(outputStream);
		            os.writeObject(record);
		            os.flush();
		            byte[] data = outputStream.toByteArray();
		            DatagramPacket sendObjectToCommit = new DatagramPacket(data, data.length, IPAddress, port);
		            socket.send(sendObjectToCommit);
		            outputStream.reset();
		            os.reset();
		            
		            status = "send";
		            socket.setSoTimeout(1000);
		            
		            byte[] incomingResultData = new byte[1024];
		            DatagramPacket incomingResultPacket = new DatagramPacket(incomingResultData, incomingResultData.length);
		            socket.receive(incomingResultPacket);
		            String resultResponse = new String(incomingResultPacket.getData());
		            System.out.println(resultResponse);
		            if(resultResponse.trim().equalsIgnoreCase("notDone")){
		            	 socket.close(); 
		            }else{
		            	result = true;
		            	database.deleteTranferedRecord(record, backupRecord ); 
		            	 socket.close(); 
		            }

			}catch(SocketTimeoutException  e){
				 record.setStatusMessage("record has been sent but no response");
				 backupRecord.setRecord(record);
				 backupRecord.doSaveAsString();	
				if(socket != null){
					socket.close();
				}
			} catch(Exception e){
				if(socket != null){
					socket.close();
				}
				e.printStackTrace(System.out);
			}
	}
	public boolean isResult() {
		return result;
	}
}
