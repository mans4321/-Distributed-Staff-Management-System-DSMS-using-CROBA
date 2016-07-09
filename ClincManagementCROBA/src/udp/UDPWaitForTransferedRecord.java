package udp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import servers.records.Record;
import servers.records.RecordManager;

 public class UDPWaitForTransferedRecord extends Thread {

	 private int port;
		private RecordManager database;
	    private DatagramSocket socket = null;
	    private ObjectInputStream is;
	    public UDPWaitForTransferedRecord(int port , RecordManager database) {

	    	this.port = port;
	    	this.database = database;
	    }
	    	public void run()  {
	        try {
	        	while (true) {
	        		System.out.println(port);
	            socket = new DatagramSocket(port);
	            byte[] incomingData = new byte[1024];
	            while (true) {
	            	System.out.println(database.getRecordCounts());
	            	System.out.println("waiting for a TransferedRecord");
	                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
	                socket.receive(incomingPacket);
	                System.out.println("got one TransferedRecord");
	                byte[] data = incomingPacket.getData();
	                ByteArrayInputStream in = new ByteArrayInputStream(data);
	                ObjectInputStream is = new ObjectInputStream(in);
	                Record transferedRecord = (Record) is.readObject();
	                InetAddress IPAddress = incomingPacket.getAddress();
	                int port = incomingPacket.getPort();
	                System.out.println("Student object received = "+ transferedRecord.getFirstName());
	                String reply = "Thank you for the message";
	                
	                byte[] replyBytea = reply.getBytes();
	                DatagramPacket replyPacket =
	                     new DatagramPacket(replyBytea, replyBytea.length, IPAddress, port);
	                socket.send(replyPacket);
	                byte[] incomingData2 = new byte[1024];
	                DatagramPacket incomingPacket2 = new DatagramPacket(incomingData2, incomingData2.length);
	                socket.receive(incomingPacket2);
	                String recordCount = new String(incomingPacket2.getData());
	                System.out.println("IS it done = " + recordCount);
	                if(recordCount.trim().equalsIgnoreCase("done")){
					 database.addTransferedRecord(transferedRecord);
					 System.out.println(database.getRecordCounts());
	                }else{
	                	System.out.println("Something wrong");
	                }
	            }
	        	}
	        } catch (SocketException e) {
	            e.printStackTrace();
	        } catch (IOException i) {
	            i.printStackTrace();
	        } catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
    	
}
