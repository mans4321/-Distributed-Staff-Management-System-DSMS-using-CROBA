

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import servers.records.DoctorRecord;
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

		            
		            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		            ObjectOutputStream os = new ObjectOutputStream(outputStream);
		            os.writeObject(record);
		            os.flush();
		            byte[] data = outputStream.toByteArray();

		            
		            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port);
		            Socket.send(sendPacket);
		            
		            
		            byte[] incomingData = new byte[1024];
		            System.out.println("Message sent from client");
		            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
		            Socket.receive(incomingPacket);
		            String response = new String(incomingPacket.getData());
		            System.out.println("Response from server:" + response);
		            
		            
		            String reply = "DONE";
	                byte[] replyBytea = reply.getBytes();
	                DatagramPacket replyPacket =
	                     new DatagramPacket(replyBytea, replyBytea.length, IPAddress, port);
	                Socket.send(replyPacket);
		       
		            String responseww = new String(replyPacket.getData());
		            System.out.println("Response from server:" + responseww);
	                
		            return true;
		            		//database.deleteTranferedRecord(record);
		            
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
		
    	public static void main(String[]args){
    		final ExecutorService service;
            final Future<Boolean>  LVL;
            final Future<Boolean>  LVL1;
//            final Future<Boolean>  LVL2;
//            final Future<Boolean>  LVL3;
//            final Future<Boolean>  LVL4;
//            final Future<Boolean>  LVL5;
//            final Future<Boolean>  LVL6;
//            final Future<Boolean>  LVL7;
//            final Future<Boolean>  LVL8;
//            final Future<Boolean>  LVL9;
            

            service = Executors.newFixedThreadPool(10); 
            LVL = service.submit(new UDPSocketClient(8000,new DoctorRecord("sddd","mansour","ssss","dd","dd","dd","ddd"),new RecordManager()));
            LVL1 = service.submit(new UDPSocketClient(8000,new DoctorRecord("sddd","lll","ksssjjjjs","dd","dd","dd","dd"),new RecordManager()));
//            LVL2 = service.submit(new UDPSocketClient(8000,new DoctorRecord("sddd","mansour","ssss","dd","dd","dd","dd"),new RecordManager()));
//            LVL3 = service.submit(new UDPSocketClient(8000,new DoctorRecord("sddd","mansour","ssss","dd","dd","dd","dd"),new RecordManager()));
//            LVL4 = service.submit(new UDPSocketClient(8000,new DoctorRecord("sddd","mansour","ssss","dd","ddd","dd","dd"),new RecordManager()));
//            LVL5 = service.submit(new UDPSocketClient(8000,new DoctorRecord("sddd","mansour","ssss","dd","dd","dd","dd"),new RecordManager()));
//            LVL6 = service.submit(new UDPSocketClient(8000,new DoctorRecord("sddd","mansour","ssss","dd","dd","dd","dd"),new RecordManager()));
//            LVL7 = service.submit(new UDPSocketClient(8000,new DoctorRecord("sddd","mansour","ssss","dd","dd","dd","dd"),new RecordManager()));
//            LVL8 = service.submit(new UDPSocketClient(8000,new DoctorRecord("sddd","mansour","ssss","dd","dd","dd","dd"),new RecordManager()));
//            LVL9 = service.submit(new UDPSocketClient(8000,new DoctorRecord("sddd","mansour","ssss","dd","dd","dd","dd"),new RecordManager()));
            try {
    			System.out.println(LVL.get());
//    			System.out.println(LVL2.get());
//    			System.out.println(LVL3.get());
//    			System.out.println(LVL4.get());
//    			System.out.println(LVL5.get());
//    			System.out.println(LVL6.get());
//    			System.out.println(LVL7.get());
//    			System.out.println(LVL8.get());
//    			System.out.println(LVL9.get());
    			System.out.println(LVL1.get());

    		} catch (InterruptedException | ExecutionException e) {
    	    	if(service != null)
    	    	service.shutdown();
    		}finally{
              	if(service != null)
    	    	service.shutdown();
    		}
    	}
	}
