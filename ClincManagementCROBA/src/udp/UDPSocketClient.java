package udp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import servers.records.Record;
import servers.records.RecordManager;

public class UDPSocketClient implements Callable<Boolean>   {

	  private DatagramSocket Socket;
		private RecordManager database;
		private int port;
		private Record record;
		
	    public UDPSocketClient(int port , Record record,RecordManager database) {
	    	this.record = record;
	    	this.database = database;
	    	this.port = port;
	    } 

		@Override
		public Boolean call() throws Exception {
			
		       try {
		            Socket = new DatagramSocket();
		            InetAddress IPAddress = InetAddress.getByName("localhost");
		            byte[] incomingData = new byte[1024];
		            byte[] send = new byte[1024];
		            
		            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		            ObjectOutputStream os = new ObjectOutputStream(outputStream);
		            os.writeObject(record);
		            byte[] data = outputStream.toByteArray();
		            System.out.println(data.length);
		            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port);
		            Socket.send(sendPacket);
		            System.out.println("Message sent from client");
		            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
		            Socket.receive(incomingPacket);
		            
		            String response = new String(incomingPacket.getData());
		            System.out.println("Response from server:" + response);
		            
		            send = "DONE".getBytes();
		            DatagramPacket acno = new DatagramPacket("done".getBytes(), "done".getBytes().length, IPAddress, port);
		            Socket.send(acno);
		            // delete record 
		            return  database.deleteTranferedRecord(record);
		            
		        } catch (UnknownHostException e) {
		            e.printStackTrace();
		            return false;
		        } catch (SocketException e) {
		            e.printStackTrace();
		            return false;
		        } catch (IOException e) {
		            e.printStackTrace();
		            return false;
		        }
		    }
	}
