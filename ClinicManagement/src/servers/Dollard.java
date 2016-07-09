package servers;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import FIFOsubsystem.Message;
import FIFOsubsystem.ReliableFIFO;
import FIFOsubsystem.SendMessage;
import failuredetectionSubSystem.PingServerInfo;
import failuredetectionSubSystem.PingServers;
import servers.records.Record;
import servers.records.RecordManager;
import udp.CountUdp.UDPClient;
import udp.CountUdp.UDPServerCount;
import udp.transferRecordUDP.TowPhaseProtocolServer;
import utilities.RmiLogger;

public class Dollard implements ApplyOperations {
	
    private RecordManager database;
    private RmiLogger logger;
    private int sequenceNum = 1;
    private final String serverName;
    private boolean manager;
    private SendMessage sendMessage;
    private ReliableFIFO reliableFIFO;
    private PingServers pingServers;
    public Dollard ()
    {
        this.serverName = "ddo";
        this.database = new RecordManager();
        this.logger = new RmiLogger(serverName, "server");
        // handle sending message 
        sendMessage = new SendMessage();
        sendMessage.setBackupPort1(30025);
        sendMessage.setBackupPort2(30045);
        // Failure  system 
        pingServers = new PingServers(new PingServerInfo(false , 30026 ) , 
        		new PingServerInfo(false , 30046 ), this , 30006,3007 );
        
        this.manager = true;
        this.reliableFIFO = new ReliableFIFO(manager, 30005 , 20012 , this);
        if(manager)
        	startUdpForLeaderServer();
    }
    
    public String createDRecord (
			String managerID,
			String firstName, 
			String lastName,
			String address, 
			String phone, 
			String specialization,
			String location
	)  {
    	System.out.println("Dollard  createDRecord");
	    Record record;
	    record = database.createDRecord(firstName, lastName, address, phone, specialization, location);
	    logger.log(record);
	      	if(record.isSuccessful()){
	      		Message message = new Message(1, nextsequenceNum(), managerID , record);
	      		sendMessage.sendTo(message);
	      	}
	      	return record.getStatusMessage();
	}
	    
	public String createNRecord (
		String managerID,
		String firstName, 
		String lastName, 
		String designation,
		String status,
		String statusDate
    ) {

            Record record;
            record = database.createNRecord(firstName, lastName, designation, status, statusDate);
    	    logger.log(record);
    	      	if(record.isSuccessful()){
    	      		Message message = new Message(2, nextsequenceNum(), managerID , record);
    	      		sendMessage.sendTo(message);
    	      	}
    	      	return record.getStatusMessage();
	}
	
    public String editRecord (String managerID,String recordId, String fieldName, String newValue) {
        
        Record record;
        record = database.editRecord(recordId, fieldName, newValue);
	    logger.log(record);
	      	if(record.isSuccessful()){
	      		Message message = new Message(3, nextsequenceNum(), managerID , recordId,fieldName, newValue );
	      		sendMessage.sendTo(message);
	      	}
	      	return record.getStatusMessage();
    } 

    
    public String getRecordCount (String managerID, int type) {
    	Message message = new Message(4,nextsequenceNum(),managerID,type );
    	sendMessage.sendTo(message);
    	final ExecutorService service;
        final Future<Integer>  LVL;
        final Future<Integer>  MTL;
        Integer LvLCount = -1 ;
        Integer MTLcount = -1 ;
        service = Executors.newFixedThreadPool(2); 
        int localCount = database.getRecordCounts();
        LVL = service.submit(new UDPClient(9999));
        MTL = service.submit(new UDPClient(9998));
        try {
			LvLCount = LVL.get();
			MTLcount = MTL.get();
		} catch (InterruptedException | ExecutionException e) {
	    	if(service != null)
	    	service.shutdown();
		}finally{
          	if(service != null)
	    	service.shutdown();
		}
        
        String allCounts = "DDO: " + localCount + ", LVL: " + LvLCount + ", MTL: " + MTLcount;
        return allCounts;
    }
    
	
	public String transferRecord(String managerID, String recordID, String remoteClinicServerName) {
		String result; 
		if(database.transferRecord(managerID, recordID, remoteClinicServerName)){
			result = "Record "+recordID + " has been transferd";
			logger.log(result);
      		Message message = new Message(5, nextsequenceNum(), managerID , recordID,remoteClinicServerName);
      		sendMessage.sendTo(message);
			return result;
		}
		else{ 
			result = " could't transfer record  " +recordID;
			logger.log(result);
			return result  ;
		}
	}
	
	private  void startUdpForLeaderServer() 
	{
		UDPServerCount udp = new UDPServerCount(9997, database);
	    udp.start();      
	    TowPhaseProtocolServer transferedRecord = new TowPhaseProtocolServer(8000,database);
	    transferedRecord.start();
	}
	
	/**
	 * just when a backup become a leader
	 * because backup need to change their behavior   
	 * 
	 * @param manager     
	 * @param port
	 */
	public void leaderChanged(boolean manager , int port){
		this.manager = manager;
		reliableFIFO.managerHasChanged(manager, port);
		startUdpForLeaderServer();
		// pingServers  adapt to new leader and deal with ping threads
	}
	public RecordManager getDatabase() {
		return database;
	}

	private synchronized int nextsequenceNum(){
		int num = sequenceNum;
		sequenceNum++;
		return num;
	}
	
public static void main(String []args){
	       		Dollard dollard = new Dollard();    		
}
}