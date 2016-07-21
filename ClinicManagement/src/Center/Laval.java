package Center;



import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import failuredetectionSubSystem.PingServers;
import servers.records.Record;
import servers.records.RecordManager;
import udp.CountUdp.UDPClient;
import udp.CountUdp.UDPServerCount;
import udp.transferRecordUDP.TowPhaseProtocolServer;
import utilities.RmiLogger;

public class Laval implements ApplyOperations {
	private GenerateInfoforOtherServers getInfo;
	private RecordManager database;
    private RmiLogger logger;
    private int sequenceNum = 1;
    private String serverName;
    private boolean manager;
    private MessageTransport sendMessage;
    private MessagesCenter messageCenter;
    private PingServers pingServers;
    private ServersInfo server1;
    private ServersInfo server2;
    private int listenOnPort;
    private int front1Port;
    
    
    public Laval (int priority)
    {
    	 this.serverName = "Laval" + priority ;
        this.database = new RecordManager();
        this.logger = new RmiLogger(serverName, "server");
        this.database = new RecordManager();
        this.logger = new RmiLogger(serverName, "server");

        initializeVaules(priority);
        
        if(priority == 1){
        	this.manager = true;
        	startUdpForLeaderServer();
        }
    }
    
    private void initializeVaules(int priority){
    	
   	 getInfo = new GenerateInfoforOtherServers("LVL");
   	 
   	 switch(priority){
   	 
   	 	case 1 :
   	 		server1 = getInfo.getServer2();
   	 		server2 =  getInfo.getServer3();
   	 		listenOnPort = getInfo.getServer1().getPort();
   	 		front1Port = getInfo.getFrontEnd().getPort();
   	 	case 2 :
   	 		server1 = getInfo.getServer1();
   	 		server2 =  getInfo.getServer3();
   	 		listenOnPort = getInfo.getServer2().getPort();
   	 		front1Port = getInfo.getServer1().getPort();
   	 	case 3 :
   	 		server1 = getInfo.getServer1();
   	 		server2 =  getInfo.getServer2();
   	 		listenOnPort = getInfo.getServer3().getPort();
   	 		front1Port = getInfo.getServer1().getPort();
   	 }
   	 
   	 
     sendMessage = new MessageTransport(server1.getPort(), 
				server2.getPort());

     pingServers = new PingServers(server1, server2 , 
			getInfo.allServers ,
			this, priority);

     messageCenter = new MessagesCenter(manager, front1Port,
				listenOnPort, this );
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
	
	public void leaderChanged(boolean manager , int port){
		
		if(manager){
			startUdpForLeaderServer();
			messageCenter.managerHasChanged(manager, front1Port);
			pingServers.newLeader(port);
		}else{
			pingServers.newLeader(port);
			messageCenter.managerHasChanged(manager, port);
		}
	}
	
	public RecordManager getDatabase() {
		return database;
	}

	private synchronized int nextsequenceNum(){
		int num = sequenceNum;
		sequenceNum = sequenceNum++;
		return num;
	}
    public static void main(String []args){
    	Laval laval = new Laval(1);
    }
}